package com.gearreald.tullframe;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.exceptions.UnimplementedException;
import com.gearreald.tullframe.utils.ColumnType;
import com.opencsv.CSVReader;

import net.tullco.tullutils.FileUtils;
import net.tullco.tullutils.SQLUtil;

public class TullFrameFactory {
	
	private File csvFile;
	private String[] headers;
	private Connection conn;
	private String sqlStatement;
	private ColumnType[] columnTypes;

	public TullFrameFactory(){
		
	}
	public TullFrameFactory fromCSV(File f){
		csvFile = f;
		if(headers == null) {
			try (CSVReader reader = FileUtils.getCSVReader(csvFile)){
				headers = reader.readNext();
			} catch (IOException e) {}
		}
		return this;
	}
	public TullFrameFactory fromSQL(Connection conn, String statement){
		this.conn = conn;
		this.sqlStatement = statement;
		csvFile = null;
		return this;
	}
	public TullFrameFactory setColumnHeaders(String[] headers){
		this.headers = headers;
		return this;
	}
	public TullFrameFactory setColumnTypes(ColumnType[] columnTypes){
		this.columnTypes = columnTypes;
		return this;
	}
	public TullFrame build() {
		if (headers != null && columnTypes != null){
			if (headers.length != columnTypes.length){
				throw new TullFrameException("The headers and the column types don't match up.");
			}
		}
		TullFrame frame;
		if(csvFile != null){
			try (CSVReader reader = FileUtils.getCSVReader(csvFile)){
				String[] headerRow = reader.readNext();
				headers = (headers == null?headerRow:headers);
				if (columnTypes == null){
					columnTypes = new ColumnType[headers.length];
					for(int i=0; i< columnTypes.length; i++){
						columnTypes[i] = ColumnType.STRING;
					}
				}
				frame = new TullFrame(headers, columnTypes);
				String[] line;
				while ((line = reader.readNext()) != null){
					frame.addRow(line);
				}
			} catch (IOException e){
				throw new TullFrameException("Error reading CSV data.", e);
			}
		}
		else if(conn != null && sqlStatement != null){
			try (SQLUtil sql = new SQLUtil(conn)){
			
				ResultSet rs = sql.executeSelect(sqlStatement);
				ResultSetMetaData rsmd = rs.getMetaData();
				int sqlColumns = rsmd.getColumnCount();
				String[] sqlHeaders = new String[sqlColumns];
				ColumnType[] sqlColumnTypes = new ColumnType[sqlColumns];
				for (int i = 1; i <= sqlColumns; i++){
					sqlHeaders[i-1] = rsmd.getColumnLabel(i);
					sqlColumnTypes[i-1] = getColumnTypeFromSQLType(rsmd.getColumnType(i));
				}
				if(headers == null)
					headers = sqlHeaders;
				if(columnTypes == null)
					columnTypes = sqlColumnTypes;
				frame = new TullFrame(headers, columnTypes);
				while(rs.next()){
					String[] row = new String[sqlColumns];
					for (int i = 1; i <= sqlColumns; i++){
						row[i-1] = rs.getString(i);
					}
					frame.addRow(row);
				}
			}catch (SQLException e){
				throw new TullFrameException("Error fetching SQL results.", e);
			}
		}
		else{
			frame = new TullFrame(headers, columnTypes);
		}
		
		return frame;
	}
	private static ColumnType getColumnTypeFromSQLType(int sqlType){
		if(sqlType == -6 || sqlType == 5 || sqlType == 4)
			return ColumnType.INTEGER;
		else if(sqlType == -5)
			return ColumnType.LONG;
		else if(sqlType == 6 || sqlType == 7 || sqlType == 8 || sqlType == 2 || sqlType == 3)
			return ColumnType.DOUBLE;
		else if(sqlType == 1 || sqlType == 12 || sqlType == -1)
			return ColumnType.STRING;
		else if(sqlType == 91)
			return ColumnType.DATE;
		else if(sqlType == 92 || sqlType == 93)
			return ColumnType.TIME;
		else if(sqlType == 16 || sqlType == -7)
			return ColumnType.BOOLEAN;
		else
			throw new UnimplementedException(String.format("The datatype %d is not supported", sqlType));
	}
}
