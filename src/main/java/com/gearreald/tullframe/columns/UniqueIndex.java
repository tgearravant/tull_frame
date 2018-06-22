package com.gearreald.tullframe.columns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gearreald.tullframe.exceptions.IndexException;

public class UniqueIndex implements Serializable{

	private static final long serialVersionUID = 2031295145301772492L;
	private Map<Object, Integer> columnIndex;
	private Set<Integer> nullIndices;
	
	protected UniqueIndex(Column c) throws IndexException{
		columnIndex = new HashMap<Object, Integer>();
		nullIndices = new HashSet<Integer>();
		List<? extends Object> columnValues = c.getBackingList();
		for(int i = 0; i < columnValues.size(); i++){
			Object value = columnValues.get(i);
			addValuetoIndex(value, i);
		}
	}
	protected void addValuetoIndex(Object o, int i) throws IndexException{
		if(o == null)
			throw new IndexException("Unique indexes cannot be null.");
		if(!columnIndex.containsKey(o)){
			columnIndex.put(o, i);
		}
		else{
			throw new IndexException("Unique index contraint violated. Value: " + o.toString());
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
