package com.gearreald.tullframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.exceptions.UnimplementedException;
import com.gearreald.tullframe.utils.ColumnType;
import com.opencsv.CSVReader;

import net.tullco.tullutils.FileUtils;
import net.tullco.tullutils.SQLUtil;

public final class TullFrameFactory {
	
	private File csvFile;
	private String[] headers;
	private String[] uniqueIndexes;
	private String[] lookupIndexes;
	private Connection conn;
	private String sqlStatement;
	private ColumnType[] columnTypes;
	private TullFrame copyFrame;
	private File serializedFile;

	public TullFrameFactory(){
		csvFile = null;
		headers = null;
		uniqueIndexes = null;
		lookupIndexes = null;
		conn = null;
		sqlStatement = null;
		columnTypes = null;
		copyFrame = null;
		serializedFile = null;
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
	public TullFrameFactory fromSerializedFile(File f){
		serializedFile = f;
		return this;
	}
	public TullFrameFactory setColumnHeaders(List<String> headers){
		return setColumnHeaders(headers.toArray(new String[0]));
	}
	public TullFrameFactory setColumnHeaders(String[] headers){
		this.headers = headers;
		return this;
	}
	public TullFrameFactory setColumnTypes(List<ColumnType> columnTypes){
		return setColumnTypes(columnTypes.toArray(new ColumnType[0]));
	}
	public TullFrameFactory setColumnTypes(ColumnType[] columnTypes){
		this.columnTypes = columnTypes;
		return this;
	}
	public TullFrameFactory fromTullFrame(TullFrame tf){
		this.copyFrame = tf;
		return this;
	}
	public TullFrameFactory setUniqueIndex(String... columnNames){
		uniqueIndexes = columnNames;
		return this;
	}
	public TullFrameFactory setLookupIndex(String... columnNames){
		lookupIndexes = columnNames;
		return this;
	}
	public TullFrame build() {
		if (headers != null && columnTypes != null){
			if (headers.length != columnTypes.length){
				throw new TullFrameException("The headers and the column types don't match up.");
			}
		}
		TullFrame frame;
		if(serializedFile != null){
			try(FileInputStream fileInputStream = new FileInputStream(serializedFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);){
				Object object = objectInputStream.readObject();
				frame = (TullFrame) object;
			}catch(IOException | ClassNotFoundException e){
				frame = new TullFrame(headers, columnTypes);
			}
		}else if(copyFrame != null){
			try{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(copyFrame);
	
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				frame = (TullFrame) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				frame = new TullFrame(headers, columnTypes);
			}
		}else if(csvFile != null){
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
				setIndexes(frame);
				String[] line;
				while ((line = reader.readNext()) != null){
					for(int i = 0; i < line.length; i++){
						if(line[i].equals(""))
							line[i] = null;
					}
					frame.addRow(line);
				}
			} catch (IOException e){
				throw new TullFrameException("Error reading CSV data.", e);
			}
		}else if(conn != null && sqlStatement != null){
			try (SQLUtil sql = new SQLUtil(conn)){
			
				ResultSet rs = sql.executeSelect(sqlStatement);
				ResultSetMetaData rsmd = rs.getMetaData();
				int sqlColumns = rsmd.getColumnCount();
				String[] sqlHeaders = new String[sqlColumns];
				ColumnType[] sqlColumnTypes = new ColumnType[sqlColumns];
				for (int i = 1; i <= sqlColumns; i++){
					sqlHeaders[i-1] = rsmd.getColumnLabel(i);
					sqlColumnTypes[i-1] = getColumnTypeFromSQLType(rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
				}
				if(headers == null)
					headers = sqlHeaders;
				if(columnTypes == null)
					columnTypes = sqlColumnTypes;
				frame = new TullFrame(headers, columnTypes);
				setIndexes(frame);
				while(rs.next()){
					String[] row = new String[sqlColumns];
					for (int i = 1; i <= sqlColumns; i++){
						row[i-1] = getSQLValueAtIndexAsString(rs, i);
					}
					frame.addRow(row);
				}
			}catch (SQLException e){
				throw new TullFrameException("Error fetching SQL results.", e);
			}
		}
		else{
			frame = new TullFrame(headers, columnTypes);
			setIndexes(frame);
		}
		
		return frame;
	}
	private static ColumnType getColumnTypeFromSQLType(int sqlType, String sqlTypeName){
		if(sqlType == -6 || sqlType == 5 || sqlType == 4)
			return ColumnType.INTEGER;
		else if(sqlType == -5)
			return ColumnType.LONG;
		else if(sqlType == 6 || sqlType == 7 || sqlType == 8 || sqlType == 2 || sqlType == 3)
			return ColumnType.DOUBLE;
		else if(sqlType == 1 || sqlType == 12 || sqlType == -1 || sqlType == -9 || sqlType == -16)
			return ColumnType.STRING;
		else if(sqlType == 91 || sqlType == -16)
			return ColumnType.DATE;
		else if(sqlType == 92 || sqlType == 93)
			return ColumnType.TIME;
		else if(sqlType == 16 || sqlType == -7)
			return ColumnType.BOOLEAN;
		else if(sqlType == 1111 && sqlTypeName.equals("jsonb"))
			return ColumnType.STRING;
		else
			throw new UnimplementedException(String.format("The datatype %d is not supported", sqlType));
	}
	private static String getSQLValueAtIndexAsString(ResultSet rs, int index) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		if((rsmd.getColumnType(index)==Types.INTEGER) ||
				(rsmd.getColumnType(index)==Types.SMALLINT) ||
				(rsmd.getColumnType(index)==Types.TINYINT)){
			return Integer.toString(rs.getInt(index));
		}
		else if(rsmd.getColumnType(index)==Types.BIGINT){
			return Long.toString(rs.getLong(index));
		}
		else if(rsmd.getColumnType(index)==Types.DOUBLE ||
				rsmd.getColumnType(index)==Types.FLOAT ||
				rsmd.getColumnType(index)==Types.DECIMAL ||
				rsmd.getColumnType(index)==Types.NUMERIC ||
				rsmd.getColumnType(index)==Types.REAL){
			return Double.toString(rs.getDouble(index));
		}
		else if(rsmd.getColumnType(index)==Types.VARCHAR ||
				rsmd.getColumnType(index)==Types.BLOB ||
				rsmd.getColumnType(index)==Types.LONGNVARCHAR ||
				rsmd.getColumnType(index)==Types.LONGVARCHAR ||
				rsmd.getColumnType(index)==Types.CHAR ||
				rsmd.getColumnType(index)==Types.SQLXML){
			return rs.getString(index);
		}
		else if(rsmd.getColumnType(index)==Types.BOOLEAN ||
				rsmd.getColumnType(index)==(Types.BIT)){
			return Boolean.toString(rs.getBoolean(index));
		}
		else if(rsmd.getColumnType(index)==Types.DATE){
			return rs.getDate(index).toLocalDate().toString();
		}
		else if(rsmd.getColumnType(index)==Types.TIME ||
			rsmd.getColumnType(index)==Types.TIME_WITH_TIMEZONE){
			return rs.getTime(index).toLocalTime().toString();
		}
		else if(rsmd.getColumnType(index)==Types.TIMESTAMP ||
				rsmd.getColumnType(index)==Types.TIMESTAMP_WITH_TIMEZONE){
			return rs.getTimestamp(index).toLocalDateTime().toString();
		}
		else if(rsmd.getColumnType(index)==Types.OTHER && rsmd.getColumnTypeName(index).equals("jsonb")){
			return rs.getString(index);
		}
		else{
			throw new SQLException("Coult not convert "+rsmd.getColumnTypeName(index)+".");
		}
	}
	private void setIndexes(TullFrame tf){
		if(uniqueIndexes != null) {
			for(String s: uniqueIndexes){
				tf.setUniqueIndex(s);
			}
		}
		if(lookupIndexes != null) {
			for(String s: lookupIndexes){
				tf.setLookupIndex(s);
			}
		}
	}
}
