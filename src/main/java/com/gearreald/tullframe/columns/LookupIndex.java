package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LookupIndex {
	private Map<Object, Set<Integer>> columnIndex;
	protected boolean uniqueIndex;
	
	protected LookupIndex(Column c){
		columnIndex = new HashMap<Object, Set<Integer>>();
		uniqueIndex = true;
		Map<Integer, ? extends Object> columnValues = c.getBackingMap();
		for (int index: columnValues.keySet()){
			Object value = columnValues.get(index);
			addValuetoIndex(value, index);
		}
	}
	protected void addValuetoIndex(Object o, int i){
		Set<Integer> values;
		if(!columnIndex.containsKey(o)){
			values = new HashSet<Integer>();
			columnIndex.put(o, values);
		}
		else{
			values = columnIndex.get(o);
			uniqueIndex = false;
		}
		values.add(i);
	}
}
