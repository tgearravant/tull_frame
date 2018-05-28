package com.gearreald.tullframe.columns;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gearreald.tullframe.exceptions.BooleanParseException;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.exceptions.IndexException;
import com.gearreald.tullframe.exceptions.UnimplementedException;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.Pair;

public abstract class Column implements Serializable, Iterable<Pair<Integer, Object>>{

	private static final long serialVersionUID = -8626093972148251030L;
	protected boolean hasUniqueIndex = false;
	protected boolean hasLookupIndex = false;
	protected UniqueIndex uniqueIndex;
	protected LookupIndex lookupIndex;
	protected static final int QUICK_TYPE_VALUES = 5;

	public void createUniqueIndex(){
		uniqueIndex = new UniqueIndex(this);
		hasUniqueIndex = true;
	}
	public void createLookupIndex(){
		lookupIndex = new LookupIndex(this);
		hasLookupIndex = true;
	}
	public Integer uniqueLookup(Object o) {
		return uniqueLookup(o, false);
	}
	public Integer uniqueLookup(Object o, boolean force) throws IndexException{
		if(!hasUniqueIndex && !force)
			throw new IndexException("The column is not indexed. Lookups will be slow and will not check for duplicates. To override, use uniqueLookup(object, true)");
		if(hasUniqueIndex){
			return uniqueIndex.getValuesFromIndex(o); 
		}else if (hasLookupIndex){
			Set<Integer> values = lookupIndex.getValuesFromIndex(o);
			if (values == null || values.isEmpty())
				return null;
			return values.iterator().next();
		}else {
			for(Integer i: this.getBackingMap().keySet()){
				Object inputItem = this.getBackingMap().get(i);
				if (o.equals(inputItem)){
					return i;
				}
			}
			return null;
		}
	}
	public Set<Integer> valueLookup(Object o){
		return valueLookup(o, true);
	}
	public Set<Integer> valueLookup(Object o, boolean force) throws IndexException{
		if((!hasUniqueIndex && !hasLookupIndex) && !force){
			throw new IndexException("The column is not indexed. Lookups will be slow. To override, use valueLookup(object, true)");
		}
		if(hasLookupIndex){
			return lookupIndex.getValuesFromIndex(o);
		}else if(hasUniqueIndex){
			Integer i = uniqueIndex.getValuesFromIndex(o);
			Set<Integer> set = new HashSet<Integer>();
			set.add(i);
			return set;
		}else{
			Set<Integer> set = new HashSet<Integer>();
			for(Integer i: this.getBackingMap().keySet()){
				Object inputItem = this.getBackingMap().get(i);
				if (o.equals(inputItem)){
					set.add(i);
				}
			}
			if(set.isEmpty())
				return null;
			return set;
		}
	}
	public Integer getInt(int index){
		throw new ColumnTypeMismatchException("This is not an integer column.");
	}
	public LocalDate getDate(int index){
		throw new ColumnTypeMismatchException("This is not a date column.");
	}
	public LocalDateTime getTime(int index){
		throw new ColumnTypeMismatchException("This is not a time column.");
	}
	public String getString(int index){
		if(this.getValue(index) == null)
			return null;
		return this.getValue(index).toString();
	}
	public Boolean getBoolean(int index){
		throw new ColumnTypeMismatchException("This is not a boolean column.");
	}
	public Long getLong(int index) {
		throw new ColumnTypeMismatchException("This is not a long column.");
	}
	public Double getDouble(int index){
		throw new ColumnTypeMismatchException("This is not a double column.");
	}
	public abstract Object getValue(int index);
	
	public Integer optInt(int index){
		try{
			return getInt(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public LocalDate optDate(int index) {
		try{
			return getDate(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public LocalDateTime optTime(int index){
		try{
			return getTime(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public String optString(int index){
		try{
			return getString(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public Boolean optBoolean(int index){
		try{
			return getBoolean(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public Long optLong(int index){
		try{
			return getLong(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}		
	}
	public Double optDouble(int index){
		try{
			return getDouble(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}		
	}
	public Object optValue(int index){
		try{
			return getValue(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	
	private void checkUniqueness(Object o){
		if(hasUniqueIndex)
			if(uniqueIndex.getValuesFromIndex(o) != null)
				throw new IndexException("Unique constraint violated");
	}
	private void addToIndices(int index, Object o){
		if(hasUniqueIndex)
			uniqueIndex.addValuetoIndex(o, index);
		if(hasLookupIndex)
			lookupIndex.addValuetoIndex(o, index);
	}
	public void set(int index, Integer value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, Double value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, Boolean value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, LocalDate value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, LocalDateTime value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, Long value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	public void set(int index, String value){
		checkUniqueness(value);
		Object o = setValue(index, value);
		addToIndices(index, o);
	}
	protected  Object setValue(int index, Integer value){
		throw new ColumnTypeMismatchException("This is not an integer column.");
	}
	protected Object setValue(int index, Double value){
		throw new ColumnTypeMismatchException("This is not a double column.");
	}
	protected Object setValue(int index, Boolean value){
		throw new ColumnTypeMismatchException("This is not a boolean column.");
	}
	protected Object setValue(int index, LocalDate value){
		throw new ColumnTypeMismatchException("This is not a date column.");
	}
	protected Object setValue(int index, LocalDateTime value){
		throw new ColumnTypeMismatchException("This is not a time column.");
	}
	protected Object setValue(int index, Long value){
		throw new ColumnTypeMismatchException("This is not a long column.");
	}
	protected Object setValue(int index, String value){
		throw new ColumnTypeMismatchException("This is not a string column.");
	}
	public void set(int index, Object value){
		set(index, (value==null ? null : value.toString()));
	}
	
	protected abstract Map<Integer,? extends Object> getBackingMap();
	
	public Object removeIndex(int index){
		return getBackingMap().remove(index);
	}
	
	@Override
	public Iterator<Pair<Integer, Object>> iterator() {
		Iterator<Pair<Integer, Object>> i = new Iterator<Pair<Integer, Object>>(){

			private Iterator<? extends Entry<Integer, ? extends Object>> internalIterator = getBackingMap().entrySet().iterator();
			@Override
			public boolean hasNext() {
				return internalIterator.hasNext();
			}

			@Override
			public Pair<Integer, Object> next() {
				Entry<Integer, ? extends Object> entry = internalIterator.next();
				return Pair.<Integer, Object>of(entry.getKey(), entry.getValue());
			}
				
		};
		return i;
	}
	
	public ColumnType inferType(){
		boolean isBool = true;
		boolean isDate = true;
		boolean isTime = true;
		boolean isInt = true;
		boolean isLong = true;
		boolean isDouble = true;
		boolean hasNonNullValues = false;
		
		if (this.getColumnType().equals(ColumnType.STRING))
			return this.getColumnType();
		
		Map<Integer, ? extends Object> valueMap = getBackingMap();
		for(int i: valueMap.keySet()){
			Object o = valueMap.get(i);
			hasNonNullValues = true;
			if (o == null){				
				continue;
			}
			hasNonNullValues = true;
			String value = o.toString();
			if(isLong){
				try{
					long l = Long.parseLong(value);
					if (l > Integer.MAX_VALUE) {
						isInt = false;
					}
				}catch(NumberFormatException e){
					isLong = false;
					isInt = false;
				}
			}
			if(isDouble){
				try{
					Double.parseDouble(value);
				}catch(NumberFormatException e){
					isDouble = false;
				}
			}
			if(isDate){
				try{
					LocalDate.parse(value);
				}catch(DateTimeParseException e){
					isDate = false;
				}
			}if(isTime){
				try{
					LocalDateTime.parse(value);
				}catch(DateTimeParseException e){
					isTime = false;
				}
			}if(isBool){
				try{
					BooleanColumn.parseBoolean(value);
				} catch (BooleanParseException e) {
					isBool = false;
				}
			}
			if(booleanAdder(isBool, isDate, isTime, isInt, isLong, isDouble) == 0)
				break;
		}
		if(!hasNonNullValues){
			return ColumnType.STRING;
		}else if(isTime){
			return ColumnType.TIME;
		}else if(isDate){
			return ColumnType.DATE;
		}else if(isInt){
			return ColumnType.INTEGER;
		}else if(isLong){
			return ColumnType.LONG;
		}else if(isDouble){
			return ColumnType.DOUBLE;
		}else if(isBool){
			return ColumnType.BOOLEAN;
		}else{
			return ColumnType.STRING;
		}
	}
	private static int booleanAdder(boolean... bs){
		int currentCount = 0;
		for(boolean b: bs){
			if(b)
				currentCount++;
		}
		return currentCount;
	}
	
	public abstract ColumnType getColumnType();
	
	public static Column getColumnFromColumnType(ColumnType type){
		switch (type){
		case BOOLEAN:
			return new BooleanColumn();
		case DATE:
			return new DateColumn();
		case TIME:
			return new TimeColumn();
		case LONG:
			return new LongColumn();
		case INTEGER:
			return new IntegerColumn();
		case DOUBLE:
			return new DoubleColumn();
		case STRING:
			return new StringColumn();
		default:
			throw new UnimplementedException("ColumnType is unsupported. I honestly have no idea how you got this error... I'd love to know, though. :)");
		}
	}
}
