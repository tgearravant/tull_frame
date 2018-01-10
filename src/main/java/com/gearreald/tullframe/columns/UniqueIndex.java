package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gearreald.tullframe.exceptions.IndexException;

public class UniqueIndex {
	private Map<Object, Integer> columnIndex;
	private Set<Integer> nullIndices;
	
	protected UniqueIndex(Column c) throws IndexException{
		columnIndex = new HashMap<Object, Integer>();
		nullIndices = new HashSet<Integer>();
		Map<Integer, ? extends Object> columnValues = c.getBackingMap();
		for (int index: columnValues.keySet()){
			Object value = columnValues.get(index);
			addValuetoIndex(value, index);
		}
	}
	protected void addValuetoIndex(Object o, int i) throws IndexException{
		if(o == null)
			throw new IndexException("Unique indexes cannot be null.");
		if(!columnIndex.containsKey(o)){
			columnIndex.put(o, i);
		}
		else{
			throw new IndexException("Unique index contraint violated.");
		}
	}
	protected Integer getValuesFromIndex(Object o){
		if (o == null)
			return null;
		return columnIndex.get(o);
	}
	protected void removeValueFromIndex(Object o, int i){
		if(o == null)
			nullIndices.remove(i);
		else
			columnIndex.remove(o);
	}
}
