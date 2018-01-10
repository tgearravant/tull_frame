package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LookupIndex {
	private Map<Object, Set<Integer>> lookupIndex;
	private Set<Integer> nullIndices;
	
	protected LookupIndex(Column c){
		lookupIndex = new HashMap<Object, Set<Integer>>();
		nullIndices = new HashSet<Integer>();
		Map<Integer, ? extends Object> columnValues = c.getBackingMap();
		for (int index: columnValues.keySet()){
			Object value = columnValues.get(index);
			addValuetoIndex(value, index);
		}
	}
	protected void addValuetoIndex(Object o, int i){
		if(o == null)
			nullIndices.add(i);
		Set<Integer> values;
		if(!lookupIndex.containsKey(o)){
			values = new HashSet<Integer>();
			lookupIndex.put(o, values);
		}
		else{
			values = lookupIndex.get(o);
		}
		values.add(i);
	}
	protected void removeValueFromIndex(Object o, int i){
		if(o == null)
			nullIndices.remove(i);
		Set<Integer> valueIndices = lookupIndex.get(o);
		if(valueIndices != null){
			valueIndices.remove(i);
		}
	}
	protected Set<Integer> getValuesFromIndex(Object o){
		return lookupIndex.get(o);
	}
}
