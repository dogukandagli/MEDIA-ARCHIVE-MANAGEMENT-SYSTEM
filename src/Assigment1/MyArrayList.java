package Assigment1;


import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class MyArrayList<T> implements Collection<T> {
    private Object[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public MyArrayList() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(T element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    public void addAll(MyArrayList<? extends T> otherList) {
        for (T element : otherList) {
            add(element);
        }
    }

    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    public void set(int index, T element) {
        checkIndex(index);
        elements[index] = element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator();
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(elements, size);
    }

    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            return (E[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int index) {
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                removeAt(i--);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = java.util.Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private class MyArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements to iterate over");
            }
            return (T) elements[currentIndex++];
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    public void sort(Comparator<? super T> comparator) {
        if (size < 2) return;

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                T a = (T) elements[j];
                T b = (T) elements[j + 1];
                
                if (comparator.compare(a, b) > 0) {
                    elements[j] = b;
                    elements[j + 1] = a;
                }
            }
        }
    }
}