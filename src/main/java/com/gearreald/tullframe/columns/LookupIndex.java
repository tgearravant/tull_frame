package com.gearreald.tullframe.columns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LookupIndex implements Serializable{

	private static final long serialVersionUID = 6635710772683403165L;
	private Map<Object, Set<Integer>> lookupIndex;
	private Set<Integer> nullIndices;
	
	protected LookupIndex(Column c){
		lookupIndex = new HashMap<Object, Set<Integer>>();
		nullIndices = new HashSet<Integer>();
		List<? extends Object> columnValues = c.getBackingList();
		for(int i = 0; i < columnValues.size(); i++){
			Object value = columnValues.get(i);
			addValuetoIndex(value, i);
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
