package com.gearreald.tullframe;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.column_adders.BooleanColumnAdder;
import com.gearreald.tullframe.column_adders.DateColumnAdder;
import com.gearreald.tullframe.column_adders.DoubleColumnAdder;
import com.gearreald.tullframe.column_adders.IntColumnAdder;
import com.gearreald.tullframe.column_adders.LongColumnAdder;
import com.gearreald.tullframe.column_adders.StringColumnAdder;
import com.gearreald.tullframe.column_adders.TimeColumnAdder;
import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.columns.StringColumn;
import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.utils.ColumnType;
import com.opencsv.CSVWriter;

import net.tullco.tullutils.FileUtils;

public class TullFrame implements Iterable<Row>, Serializable {

	private static final long serialVersionUID = 5681357572939610351L;
	private Map<String, Column> columns;
	private List<String> columnNames;
	private int currentIndex;
	private List<Integer> indices;
	
	protected TullFrame(String[] headers, ColumnType[] columnTypes){
		columns = new HashMap<String, Column>();
		columnNames = new ArrayList<String>();
		currentIndex = 0;
		indices = new ArrayList<Integer>();
		for(int i = 0; i < headers.length; i++){
			addEmptyColumn(headers[i], columnTypes[i]);
		}
	}
	public Column addEmptyColumn(String name, ColumnType type){
		Column c = Column.getColumnFromColumnType(type);
		addColumn(name, c);
		return c;
	}
	protected void addColumn(String name, Column c){
		columns.put(name, c);
		columnNames.add(name);
	}
	public Column removeColumn(String columnName){
		this.columnNames.remove(columnName);
		return this.columns.remove(columnName);
	}
	public void removeRow(int index){
		for(String columnName: columnNames){
			this.columns.get(columnName).removeIndex(index);
		}
	}
	protected Row getRow(int index){
		if(!indices.contains(index))
			throw new TullFrameException("Index does not exist");
		return new Row(index, this.columns, this.columnNames);
	}
	protected void initializeStringColumns(String[] names){
		columns.clear();
		columnNames.clear();
		for (String name: names){
			columns.put(name, new StringColumn());
			columnNames.add(name);
		}
	}
	public void addRow(String[] valueArray){
		if(valueArray.length != columns.size()){
			throw new TullFrameException("The new row has the wrong number of elements.");
		}
		for(int i = 0; i < columnNames.size(); i++){
			Column col = columns.get(columnNames.get(i));
			col.set(currentIndex,valueArray[i]);
		}
		indices.add(currentIndex);
		currentIndex++;
		
	}
	public int size(){
		return indices.size();
	}
	public int rowCount(){
		return size();
	}
	public List<String> getColumnNames(){
		return this.columnNames;
	}
	public Column getColumn(String columnName){
		return this.columns.get(columnName);
	}
	public int countColumns(){
		return columns.size();
	}
	public void toCsv(File f) throws IOException{
		try(CSVWriter writer = FileUtils.getCSVWriter(f)){
			String[] headers = columnNames.toArray(new String[0]); 
			writer.writeNext(headers);
			for(Integer i: indices){
				String[] row = new String[headers.length];
				for (int j=0;j<headers.length;j++){
					String columnName = columnNames.get(j); 
					row[j] = columns.get(columnName).getString(i);
				}
				writer.writeNext(row);
			}
		}
	}
	public void addNewIntegerColumn(String columnName, IntColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.INTEGER);
		for(Row r: this){
			int index = r.getIndex();
			Integer newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewLongColumn(String columnName, LongColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.LONG);
		for(Row r: this){
			int index = r.getIndex();
			Long newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewDoubleColumn(String columnName, DoubleColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.DOUBLE);
		for(Row r: this){
			int index = r.getIndex();
			Double newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewDateColumn(String columnName, DateColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.DATE);
		for(Row r: this){
			int index = r.getIndex();
			LocalDate newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewTimeColumn(String columnName, TimeColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.TIME);
		for(Row r: this){
			int index = r.getIndex();
			LocalDateTime newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewBooleanColumn(String columnName, BooleanColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.BOOLEAN);
		for(Row r: this){
			int index = r.getIndex();
			Boolean newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	public void addNewStringColumn(String columnName, StringColumnAdder adder){
		Column c = addEmptyColumn(columnName, ColumnType.STRING);
		for(Row r: this){
			int index = r.getIndex();
			String newValue;
			try{
				newValue = adder.calculation(r);
			}catch(NullPointerException e){
				newValue = null;
			}
			c.set(index, newValue);
		}
	}
	@Override
	public Iterator<Row> iterator() {
		Iterator<Row> i = new Iterator<Row>(){

			private Iterator<Integer> internalIterator = indices.iterator();
			@Override
			public boolean hasNext() {
				return internalIterator.hasNext();
			}

			@Override
			public Row next() {
				return getRow(internalIterator.next());
			}
				
		};
		return i;
	}
}
