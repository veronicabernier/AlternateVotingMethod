package Set;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class DynamicSet<E> implements Set<E>{

	private int currentSize;
	private E elements[];
	private int maxCapacity;
	
	public DynamicSet(int initialSize) {
		if (initialSize < 1) {
			throw new IllegalArgumentException("Initial size must be at least 1");
		}
		this.currentSize = 0;
		this.elements = (E[]) new Object[initialSize];
		this.maxCapacity = initialSize;
	}
	
	private class SetIterator<T> implements Iterator<T> {
		private int currentPosition;
		
		public SetIterator() {
			this.currentPosition = 0;
		}

		@Override
		public boolean hasNext() {
			return this.currentPosition < size();
		}

		@Override
		public T next() {
			if (this.hasNext()) {
				T result = (T) elements[this.currentPosition++];
				return result;
			}
			else
				throw new NoSuchElementException();				
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new SetIterator<E>();
	}

	@Override
	public boolean add(E obj) {
		if (this.isMember(obj)) {
			return false;
		}
		if (this.maxCapacity == this.size()) {
			this.maxCapacity *= 2;
			DynamicSet<E> newSet = new DynamicSet<E>(this.maxCapacity);
			for (E e : this) {
				newSet.add(e);
			}
			this.elements = newSet.elements;
		}
		this.elements[this.currentSize++] = obj;
		return true;
	}

	@Override
	public boolean isMember(E obj) {
		for (int i = 0; i < size(); i++) {
			if (elements[i].equals(obj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(E obj) {
		for (int i = 0; i < size(); i++)
			if (elements[i].equals(obj)) {
				elements[i] = elements[--currentSize];
				elements[currentSize] = null;
				return true;
			}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size(); i++) {
			elements[i] = null;
		}
		currentSize = 0;

	}

	@Override
	public Set<E> union(Set<E> S2) {
		Set<E> union = new DynamicSet<E>(this.size() + S2.size());
		for (E e : this) {
			union.add(e);
		}
		for (E e : S2) {
			if(e!=null) {
				if(!union.isMember(e)) {
					union.add(e);
				}
			}
		}
		return union;
	}

	@Override
	public Set<E> difference(Set<E> S2) {
		Set<E> difference = new DynamicSet<E>(this.size());
		for (E e : this) {
			if(e!=null) {
				if(!S2.isMember(e)) {
					difference.add(e);
				}
			}
		}
		return difference;
	}

	@Override
	public Set<E> intersection(Set<E> S2) {
		Set<E> intersection = new DynamicSet<E>(this.size());
		for (E e : S2) {
			if(e!=null) {
				if(this.isMember(e)) {
					intersection.add(e);
				}
			}
		}
		return intersection;
	}

	@Override
	public boolean isSubSet(Set<E> S2) {
		for (E e : this) {
			if(!S2.isMember(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Set<E> S2) {
		if(this.size() == S2.size()) {
			for (int i = 0; i < elements.length; i++) {
				if(!S2.isMember(this.elements[i])) {
					return false;
				}
			}
			return true; 
		}
		return false;
	}

}
