package com.gearreald.tullframe.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a list that is backed by a hashmap as well as an array list.
 * Contains, indexOf, and remove an object performance will be high
 * , but inserting or removing at an index performance will be reduced
 * @author Tull Gearreald
 *
 */
public class HashList<T> extends ArrayList<T> {

	private static final long serialVersionUID = -2774347878840399451L;
	HashMap<T, Integer> indexMap;
	
	public HashList(){
		super();
		indexMap = new HashMap<T, Integer>();
	}
	@Override
	public boolean add(T e) {
		super.add(e);
		indexMap.put(e, this.size() - 1);
		return true;
	}

	@Override
	public void add(int index, T element) {
		super.add(index, element);
		rebuildIndexMap();
	}
	
	private void rebuildIndexMap(){
		indexMap.clear();
		for(int i = 0; i < this.size(); i++){
			indexMap.put(this.get(i), i);
		}
	}
	
	private void rebuildIndexMapFromIndex(int index){
		for(int i = index; i < super.size(); i++){
			indexMap.put(super.get(i), i);
		}
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		super.addAll(index, c);
		rebuildIndexMap();
		return false;
	}

	@Override
	public void clear() {
		super.clear();
		indexMap.clear();
	}

	@Override
	public boolean contains(Object o) {
		try{
			if (indexMap.containsKey(o))
				return true;
			return false;
		}catch(ClassCastException e){
			return false;
		}
	}

	@Override
	public int indexOf(Object o) {
		try{
			Integer index = indexMap.get(o);
			if (index == null)
				return -1;
			return index;
		}catch(ClassCastException e){
			return -1;
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.indexOf(o);
	}

	@Override
	public boolean remove(Object o) {
		Integer startIndex = indexMap.get(o);
		if (startIndex == null)
			return false;
		else {
			this.remove(startIndex.intValue());
		}
		return true;
	}
	
	/**
	 * Removes from the list all indices in the given list.
	 * @param indexList The indices to remove from the list.
	 * @return True if any elements were removed. False otherwise.
	 */
	public boolean removeAllAtIndices(List<Integer> indexList){
		boolean listChanged = false;
		List<Integer> listCopy = new ArrayList<Integer>(indexList);
		Comparator<Integer> reverseSorting = (Integer i1, Integer i2) -> {
			return i2.compareTo(i1);
		};
		listCopy.sort(reverseSorting);
		for(Integer o: listCopy){
			T removedElement = super.remove(o.intValue());
			if (removedElement != null){
				listChanged = true;
				indexList.remove(removedElement);
			}
		}
		if(listChanged)
			rebuildIndexMapFromIndex(listCopy.get(listCopy.size()-1));
		return listChanged;
	}
	
	@Override 
	public boolean removeAll(Collection<?> col){
		boolean listChanged = false;
		int earliestIndex = super.size() - 1;
		for(Object o: col){
			int lastIndex = this.indexOf(o);
			if (lastIndex == -1)
				continue;
			else{
				this.remove(lastIndex);
				listChanged = true;
				earliestIndex = Math.min(earliestIndex, lastIndex);
			}
		}
		rebuildIndexMapFromIndex(earliestIndex);
		return listChanged;
	}
	
	@Override
	public T remove(int index) {
		T item = super.remove(index);
		indexMap.remove(item);
		for(int i = index; i < super.size(); i++){
			indexMap.put(super.get(i), i);
		}
		return item;
	}

	@Override
	public T set(int index, T element) {
		T item = this.get(index);
		super.set(index, element);
		this.indexMap.put(element, index);
		return item;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		HashList<T> newList = new HashList<T>();
		for (int i = fromIndex; i < toIndex; i++){
			newList.add(this.get(i));
		}
		return newList;
	}
}
