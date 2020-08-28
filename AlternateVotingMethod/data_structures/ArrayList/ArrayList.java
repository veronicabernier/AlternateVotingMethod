package ArrayList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E> {
	
	private E elements[];
	private int currentSize;

	public class ListIterator implements Iterator<E> {

		private int currentPosition;
		
		@Override
		public boolean hasNext() {
			return this.currentPosition < size();
		}

		@Override
		public E next() {
			if(hasNext()) {
				return (E) elements[this.currentPosition++];
			}
			else {
				throw new NoSuchElementException();
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList(int initialCapacity) {
		if(initialCapacity >= 0) {
			this.currentSize = 0;
			this.elements = (E[]) new Object[initialCapacity];
		}
		else {
			throw new IllegalArgumentException("Initial Capacity must be at least 1.");
		}
	}
	@Override
	public Iterator<E> iterator() {
		return new ListIterator();
	}

	@Override
	public void add(E obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		else {
			if(currentSize == size()) {
				reAllocate();
			}
			elements[currentSize++] = obj;
		}
	}

	@Override
	public void add(int index, E obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		if (index == currentSize)
			this.add(obj);
		else if (index >= 0 && index < currentSize) {
			if (this.currentSize == this.elements.length) {
				reAllocate();
			}
			for (int i = currentSize; i > index; i--) {
				elements[i] = elements[i - 1];
			}
			elements[index] = obj;
			currentSize++;
		}
		else {
			throw new ArrayIndexOutOfBoundsException();

		}
	}

	@Override
	public boolean remove(E obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		for (int i = 0; i < currentSize; i++) {
			if(elements[i] != null) {
				if(elements[i].equals(obj)) {
					for (int j = i; j < currentSize - 1; j++) {
						elements[j] = elements[j + 1];
					}
					elements[--currentSize] = null;
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean removeByIndex(int index) {
		if (index >= 0 && index < currentSize) {
			for (int i = index; i < currentSize - 1; i++) {
				elements[i] = elements[i + 1];
			}
			elements[--currentSize] = null;
			return true;
		}
		else
			return false;
	}

	@Override
	public int removeAll(E obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		int numRemoved = 0;
		for (int i = 0; i < currentSize; i++) {
			if(elements[i] != null) {
				if(elements[i].equals(obj)) {
					for (int j = i; j < currentSize - 1; j++) {
						elements[j] = elements[j + 1];
					}
					elements[--currentSize] = null;
					i--;
					numRemoved++;
				}
			}
		}
		return numRemoved;
	}

	@Override
	public E get(int index) {
		if (index >= 0 && index < currentSize) {
			return elements[index];
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public E set(int index, E obj) {
		if (obj == null) 
			throw new IllegalArgumentException("Object cannot be null.");
		if (index >= 0 && index < currentSize) {
			E temp = this.elements[index];
			this.elements[index] = obj;
			return temp;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public E first() {
		if(!isEmpty()) {
			return elements[0];
		}
		throw new NoSuchElementException();
	}

	@Override
	public E last() {
		if(!isEmpty()) {
			return elements[currentSize - 1];
		}
		throw new NoSuchElementException();
	}

	@Override
	public int firstIndex(E obj) {
		for (int i = 0; i < elements.length; i++) {
			if(elements[i] != null) {
				if(elements[i].equals(obj)) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int lastIndex(E obj) {
		for (int i = elements.length - 1; i >= 0; i--) {
			if(elements[i] != null) {
				if(elements[i].equals(obj)) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public boolean contains(E obj) {
		return firstIndex(obj) != -1;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.currentSize; i++)
			this.elements[i] = null;
		this.currentSize = 0;
	}
	
	@SuppressWarnings("unchecked")
	private void reAllocate() {
		E newList[] = (E[]) new Object[2*this.elements.length];
		for (int i = 0; i < this.size(); i++)
			newList[i] = this.elements[i];
		int oldSize = size();
		this.clear();  // Sets current size to 0; needs to fix
		this.elements = newList;
		currentSize = oldSize;
	}

}
