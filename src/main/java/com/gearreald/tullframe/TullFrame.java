package com.gearreald.tullframe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nustaq.serialization.FSTObjectOutput;

import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.columns.StringColumn;
import com.gearreald.tullframe.exceptions.ColumnNameException;
import com.gearreald.tullframe.exceptions.IndexException;
import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.interfaces.Filter;
import com.gearreald.tullframe.interfaces.column_adders.BooleanColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.DateColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.DoubleColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.IntColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.LongColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.StringColumnAdder;
import com.gearreald.tullframe.interfaces.column_adders.TimeColumnAdder;
import com.gearreald.tullframe.utils.ColumnType;
import com.opencsv.CSVWriter;

import net.tullco.tullutils.FileUtils;

public class TullFrame implements Iterable<Row>, Serializable {

	private Map<String, Column> columns;
	private List<String> columnNames;
	private int currentIndex;
	private List<Integer> indexList;
	private Set<Integer> indexSet;
	private static final long serialVersionUID = 5681357572939610351L;
	
	protected TullFrame(String[] headers, ColumnType[] columnTypes){
		columns = new HashMap<String, Column>();
		columnNames = new ArrayList<String>();
		indexSet = new HashSet<Integer>();
		currentIndex = 0;
		indexList = new ArrayList<Integer>();
		if(headers!= null){
			for(int i = 0; i < headers.length; i++){
				addEmptyColumn(headers[i], columnTypes[i]);
			}
		}
	}
	/**
	 * Adds an empty column of the given type to the end of the dataframe.
	 * @param name The name of the new empty column
	 * @param type The type of the new empty column (i.e. ColumnType.STRING)
	 * @return The newly added column
	 */
	public Column addEmptyColumn(String name, ColumnType type){
		Column c = Column.getColumnFromColumnType(type);
		addColumn(name, c);
		return c;
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
	public void setUniqueIndex(String columnName){
		try{
			this.columns.get(columnName).createUniqueIndex();
		}catch(NullPointerException e){
			throw new ColumnNameException(e);
		}
	}
	public void setLookupIndex(String columnName){
		try{
			this.columns.get(columnName).createLookupIndex();
		}catch(NullPointerException e){
			throw new ColumnNameException(e);
		}
	}
	public Row lookupRowByUniqueKey(Object lookupValue, String columnName){
		return lookupRowByUniqueKey(lookupValue, columnName, false);
	}
	public Row lookupRowByUniqueKey(Object lookupValue, String columnName, boolean forceNoIndex){
		Column c = this.columns.get(columnName);
		if(c == null)
			throw new ColumnNameException("That column doesn't exist.");
		Integer valueIndex = c.uniqueLookup(lookupValue, forceNoIndex);
		return (valueIndex == null ? null : this.getRowByIndex(valueIndex));
	}
	public Set<Row>lookupRows(Object lookupValue, String columnName){
		return lookupRows(lookupValue, columnName, false);
	}
	public Set<Row> lookupRows(Object lookupValue, String columnName, boolean forceNoIndex){
		Column c = this.columns.get(columnName);
		if(c == null)
			throw new ColumnNameException("That column doesn't exist.");
		Set<Row> rows = new HashSet<Row>();
		Set<Integer> lookupIndices = c.valueLookup(lookupValue, forceNoIndex);
		for(Integer i: lookupIndices){
			rows.add(this.getRowByIndex(i));
		}
		return rows;
	}
	public void addRow(List<String> valueList){
		addRow(valueList.toArray(new String[0]));
	}
	public synchronized void addRow(String[] valueArray){
		if(valueArray.length != columns.size()){
			throw new TullFrameException("The new row has the wrong number of elements.");
		}
		for(int i = 0; i < columnNames.size(); i++){
			Column col = columns.get(columnNames.get(i));
			col.set(currentIndex,valueArray[i]);
		}
		indexList.add(currentIndex);
		indexSet.add(currentIndex);
		currentIndex++;
		
	}
	public int countColumns(){
		return columns.size();
	}
	public void filterRows(Filter... filters){
		List<Integer> indexesToRemove = new ArrayList<Integer>();
		for(Row r: this){
			for(Filter f: filters){
				try{
					if(!f.condition(r))
						indexesToRemove.add(r.getIndex());
				}catch(NullPointerException e){
					indexesToRemove.add(r.getIndex());
				}
			}
		}
		for(int i: indexesToRemove){
			this.removeRowAtIndex(i);
		}
	}
	public Column getColumn(String columnName){
		return this.columns.get(columnName);
	}
	public List<String> getColumnNames(){
		return this.columnNames;
	}
	public List<ColumnType> getColumnTypes(){
		List<ColumnType> types = new ArrayList<ColumnType>();
		for (String s: columnNames){
			types.add(this.getColumn(s).getColumnType());
		}
		return types;
	}
	@Override
	public Iterator<Row> iterator() {
		Iterator<Row> i = new Iterator<Row>(){

			private Iterator<Integer> internalIterator = indexList.iterator();
			@Override
			public boolean hasNext() {
				return internalIterator.hasNext();
			}

			@Override
			public Row next() {
				return getRowByIndex(internalIterator.next());
			}
				
		};
		return i;
	}
	/**
	 * Removes the given columns from the frame.
	 * @param columnNames The names of the columns to remove.
	 * @return A list of the columns that have been removed.
	 */
	public List<Column> removeColumns(List<String> columnNames){
		return removeColumns(columnNames.toArray(new String[0]));
	}
	/**
	 * Removes the given columns from the frame.
	 * @param columnNames The names of the columns to remove.
	 * @return A list of the columns that have been removed.
	 */
	public List<Column> removeColumns(String... columnNames){
		List<Column> removedColumns = new ArrayList<Column>();
		for(String columnName: columnNames){
			removedColumns.add(removeColumn(columnName));
		}
		return removedColumns;
	}
	/**
	 * Removes all columns except the given ones from the frame.
	 * @param subsetColumns A list of the columns to keep.
	 * @return A list of the columns that have been removed.
	 */
	public List<Column> subsetToColumns(List<String> subsetColumns){
		List<String> removedColumns = new ArrayList<String>();
		for(String colName: this.columnNames){
			if(!subsetColumns.contains(colName)){
				removedColumns.add(colName);
			}
		}
		return removeColumns(removedColumns);
	}
	/**
	 * Removes all columns except the given ones from the frame.
	 * @param subsetColumns The columns to keep.
	 * @return A list of the columns that have been removed.
	 */
	public List<Column> subsetToColumns(String... subsetColumns) {
		return subsetToColumns(Arrays.<String>asList(subsetColumns));
	}
	/**
	 * Removes the given column from the data frame.
	 * @param columnName The name of the column to remove.
	 * @return The column that was removed.
	 */
	public Column removeColumn(String columnName){
		this.columnNames.remove(columnName);
		return this.columns.remove(columnName);
	}
	/**
	 * Removes the given row from the dataframe.
	 * @param rowNum The row number of the row to remove.
	 */
	public void removeRow(int rowNum){
		removeRowAtIndex(rowNumToIndex(rowNum));
	}
	/**
	 * Removes the row from the data frame.
	 * @param r The row object you want removed.
	 * Please make sure that it's actually a row object from this data frame otherwise you may get unpredictable behavior.
	 */
	public void removeRow(Row r){
		removeRowAtIndex(r.getIndex());
	}
	/**
	 * Gets the number of rows in the frame.
	 * @return The number of rows in the frame.
	 */
	public int rowCount(){
		return size();
	}
	/**
	 * Gets the number of rows in the frame.
	 * @return The number of rows in the frame.
	 */
	public int size(){
		return indexList.size();
	}
	/**
	 * Writes the contents of the frame to a CSV at the given location.
	 * @param f The output location
	 * @throws IOException If there was a problem writing the file.
	 */
	public void toCsv(File f) throws IOException{
		try(CSVWriter writer = FileUtils.getCSVWriter(f)){
			String[] headers = columnNames.toArray(new String[0]); 
			writer.writeNext(headers);
			for(Row r: this){
				writer.writeNext(r.toStringArray());
			}
		}
	}
	public void serializeToFile(File f) throws IOException{
		try (FileOutputStream fout = new FileOutputStream(f);
			FSTObjectOutput  oos = new FSTObjectOutput(fout);){
			oos.writeObject(this);
		}
	}
	protected void addColumn(String name, Column c){
		if(columns.containsKey(name))
			throw new ColumnNameException(String.format("The column %s already exists. Duplicate column names are not allowed.", name));
		columns.put(name, c);
		columnNames.add(name);
	}
	protected Row getRowByIndex(int index){
		return new Row(index, this.columns, this.columnNames, this);
	}
	public Row getRow(int rowNum){
		return getRowByIndex(rowNumToIndex(rowNum));
	}
	protected int indexToRowNum(int index){
		if(!indexSet.contains(index))
			throw new TullFrameException("Somehow, you've gotten an invalid index. This is likely a bug. It's probably not your fault. This time O:)");
		return indexList.indexOf(index);
	}
	protected void initializeStringColumns(String[] names){
		columns.clear();
		columnNames.clear();
		for (String name: names){
			columns.put(name, new StringColumn());
			columnNames.add(name);
		}
	}
	public void renameColumn(String currentName, String newName){
		if(!columns.containsKey(currentName))
			throw new ColumnNameException("The column does not exist.");
		if(columns.containsKey(newName))
			throw new ColumnNameException("The new column name is already taken.");
		int colIndex = columnNames.indexOf(currentName);
		columnNames.set(colIndex, newName);	
		columns.put(newName, columns.remove(currentName));
	}
	public boolean hasColumn(String columnName){
		return this.columnNames.contains(columnName);
	}
	public ColumnType getTypeOfColumn(String columnName){
		return this.columns.get(columnName).getColumnType();
	}
	protected int rowNumToIndex(int rowNum){
		return this.indexList.get(rowNum);
	}
	private void removeRowAtIndex(int index){
		this.indexList.remove(indexList.indexOf(index));
		this.indexSet.remove(index);
		for(String columnName: columnNames){
			this.columns.get(columnName).removeIndex(index);
		}
	}
	public void clear(){
		for(Column c: this.columns.values()){
			c.clear();
		}
		this.columnNames.clear();
		this.columns.clear();
		this.currentIndex = 0;
		this.columnNames.clear();
		this.indexList.clear();
		this.indexSet.clear();
	}
	/**
	 * @param base The frame that you want to merge columns into
	 * @param merge The frame to merge into the base frame.
	 * @param mergeColumn The name of the column to merge on.
	 * @throws IndexException If the merge columns aren't indexed.
	 * @return A new TullFrame that has the merged data from both frames.
	 */
	public static TullFrame merge(TullFrame base, TullFrame merge, String mergeColumn) {
		return merge(base, merge, mergeColumn, false);
	}
	/**
	 * @param base The frame that you want to merge columns into
	 * @param merge The frame to merge into the base frame.
	 * @param mergeColumn The name of the column to merge on.
	 * @param forceNoIndex Allows merging on columns that don't have indexes.
	 * @return A new TullFrame that has the merged data from both frames.
	 */
	public static TullFrame merge(TullFrame base, TullFrame merge, String mergeColumn, boolean forceNoIndex){
		return merge(base, merge, mergeColumn, mergeColumn, forceNoIndex);
	}
	public static TullFrame merge(TullFrame base, TullFrame merge, String baseColumn, String mergeColumn){
		return merge(base, merge, baseColumn, mergeColumn, false);
	}
	public static TullFrame merge(TullFrame base, TullFrame merge, String baseColumn, String mergeColumn, boolean forceNoIndex){
		if(!base.hasColumn(baseColumn) || !merge.hasColumn(mergeColumn))
			throw new TullFrameException("Both frames need to contain the merge key");
		
		List<String> baseHeaders = new ArrayList<String>();
		List<ColumnType> baseColumnTypes = new ArrayList<ColumnType>();
		for(String header: base.columnNames){
			baseHeaders.add(header);
			baseColumnTypes.add(base.getTypeOfColumn(header));
		}
		
		List<String> mergeHeaders = new ArrayList<String>();
		List<ColumnType> mergeColumnTypes = new ArrayList<ColumnType>();
		for(String header: merge.columnNames){
			mergeHeaders.add(header);
			mergeColumnTypes.add(merge.getTypeOfColumn(header));
		}
		
		List<String> newHeaders = new ArrayList<String>();
		List<ColumnType> newColumnTypes = new ArrayList<ColumnType>();
		Map<String, String> mapOfNewHeadersToOldHeaders = new HashMap<String, String>();
		newHeaders.addAll(baseHeaders);
		newColumnTypes.addAll(baseColumnTypes);
		
		for(int i=0; i < mergeHeaders.size(); i++){
			String header = mergeHeaders.get(i);
			ColumnType colType = mergeColumnTypes.get(i);
			if(header.equals(mergeColumn))
				continue;
			else{
				String newHeader = getNewHeader(newHeaders, header);
				newHeaders.add(newHeader);
				newColumnTypes.add(colType);
				mapOfNewHeadersToOldHeaders.put(newHeader, header);
			}
		}
		
		TullFrame newFrame = new TullFrameFactory()
				.setColumnHeaders(newHeaders).
				setColumnTypes(newColumnTypes)
				//.setUniqueIndex(mergeColumn)
				.build();
		
		for(Row r: base){
			List<String> valuesToAdd = new ArrayList<String>();
			Object mergeKey = r.getValue(baseColumn);
			Row mergeRow = merge.lookupRowByUniqueKey(mergeKey, mergeColumn, forceNoIndex);
			for(String newHeader: newHeaders){
				if(newHeader.equals(mergeColumn)){
					valuesToAdd.add(r.getString(mergeColumn));
				}else if(base.hasColumn(newHeader)){
					valuesToAdd.add(r.getString(newHeader));
				}else{
					if(mergeRow == null)
						valuesToAdd.add(null);
					else if(mergeRow.hasColumn(newHeader)){
						valuesToAdd.add(mergeRow.getString(newHeader));
					}else{
						valuesToAdd.add(mergeRow.getString(mapOfNewHeadersToOldHeaders.get(newHeader)));
					}
				}
			}
			newFrame.addRow(valuesToAdd);
		}
		
		return newFrame;
	}
	private static String getNewHeader(List<String> existingHeaders, String headerToAdd){
		if(!existingHeaders.contains(headerToAdd))
			return headerToAdd;
		else
			return getNewHeader(existingHeaders, headerToAdd, 1);
	}
	private static String getNewHeader(List<String> existingHeaders, String headerToAdd, int index){
		String headerToTest = headerToAdd+"_"+index;
		if(!existingHeaders.contains(headerToTest)){
			return headerToTest;
		}else{
			return getNewHeader(existingHeaders, headerToAdd, index + 1);
		}
	}
}
