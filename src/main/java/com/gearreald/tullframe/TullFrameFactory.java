package com.gearreald.tullframe;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;

import com.gearreald.tullframe.exceptions.DataFrameException;
import com.gearreald.tullframe.utils.ColumnType;
import com.opencsv.CSVReader;

import net.tullco.tullutils.FileUtils;

public class TullFrameFactory {
	
	private File csvFile;
	private ResultSet sqlResult;
	private String[] headers;
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
	public TullFrameFactory fromSQL(ResultSet rs){
		sqlResult = rs;
		return this;
	}
	public TullFrameFactory setColumnHeaders(String[] headers){
		this.headers = headers;
		return this;
	}
	public TullFrameFactory setColumnTypes(){
		return this;
	}
	public TullFrame build() {
		if (headers != null && columnTypes != null){
			if (headers.length != columnTypes.length){
				throw new DataFrameException("The headers and the column types don't match up.");
			}
		}
		TullFrame frame;
		if(csvFile != null){
			try (CSVReader reader = FileUtils.getCSVReader(csvFile)){
				String[] headerRow = reader.readNext();
				headers = (headers == null?headerRow:headers);
			} catch (IOException e){}
			if (columnTypes == null){
				columnTypes = new ColumnType[headers.length];
				for(int i=0; i< columnTypes.length; i++){
					columnTypes[i] = ColumnType.STRING;
				}
			}
			frame = new TullFrame(headers, columnTypes);
			try (CSVReader reader = FileUtils.getCSVReader(csvFile)){
				String[] line;
				reader.readNext();
				while ((line = reader.readNext()) != null){
					frame.addRow(line);
				}
			} catch (IOException e){}
		}
		else if(sqlResult == null){
			frame = new TullFrame(headers, columnTypes);
		}
		else{
			frame = new TullFrame(headers, columnTypes);
		}
		
		return frame;
	}
}
