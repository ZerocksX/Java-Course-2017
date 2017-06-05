package hr.fer.zemris.java.custom.collections;


/**
 * Implementation of Collection using indexed array
 *
 * @author Pavao Jerebić
 */
public class ArrayIndexedCollection extends Collection {

    /**
     * size of the collection
     */
    private int size;
    /**
     * capacity of the collection
     */
    private int capacity;
    /**
     * elements of the collection
     */
    private Object[] elements;

    /**
     * Default constructor that sets capacity to 16
     */
    public ArrayIndexedCollection() {
        this(16, new Object[16]);
    }

    /**
     * Constructor that sets capacity to given parameter
     *
     * @param initialCapacity initial capacity
     */
    public ArrayIndexedCollection(int initialCapacity) {
        this(initialCapacity, new Object[initialCapacity]);
    }

    /**
     * Constructor that adds all objects from given collection to this. Default capacity is 16 (may increase depending of the given class' size
     *
     * @param other other collection
     */
    public ArrayIndexedCollection(Collection other) {
        this(16, new Object[16]);
        addAll(other);
    }

    /**
     * Constructor that adds all objects from given collection to this and sets capacity to given capacity
     *
     * @param other           other collection
     * @param initialCapacity initial capacity
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        this(initialCapacity, new Object[initialCapacity]);
        addAll(other);
    }

    /**
     * Constructor that sets capacity and initializes array
     *
     * @param capacity capacity
     * @param elements array
     */
    private ArrayIndexedCollection(int capacity, Object[] elements) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity is less than 1");
        }
        this.capacity = capacity;
        this.elements = elements;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null");
        }

        if (this.size == this.capacity) {

            resize(this);
        }

        this.elements[size] = value;

        size++;

    }

    @Override
    public boolean contains(Object value) {
        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object value) {
        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                for (int j = i + 1; j < this.size; j++) {
                    this.elements[j - 1] = this.elements[j];
                }

                this.elements[this.size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        for (int i = 0; i < size; i++) {
            array[i] = this.elements[i];
        }
        return array;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            this.elements[i] = null;
        }
        this.size = 0;
    }


    @Override
    public void forEach(Processor processor) {
        for (int i = 0; i < size; i++) {
            processor.process(elements[i]);
        }
    }

    /**
     * returns element from collection at given index
     *
     * @param index index
     * @return element at given index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return this.elements[index];
    }

    /**
     * inserts object at given position and shifts all other elements
     *
     * @param value    object to insert
     * @param position position
     */
    public void insert(Object value, int position) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        if (size + 1 > capacity) {
            resize(this);
        }

        Object previous = elements[position];
        elements[position] = value;

        for (int i = position + 1; i <= size + 1; i++) {
            Object temp = elements[i];
            elements[i] = previous;
            previous = temp;
        }

        size++;

    }

    /**
     * Returns index of the given element
     *
     * @param value given element
     * @return index of the element
     */
    public int indexOf(Object value) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes element at given index and shifts elements to fill the blank
     * If index is out of bounds method will throw {@link IndexOutOfBoundsException}
     *
     * @param index index of element to remove
     */
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        elements[index] = null;

        for (int i = index + 1; i < size; i++) {
            elements[i] = elements[i - 1];
        }

        size--;

    }

    /**
     * Resize collection by doubling its capacity and allocating more memory for the array
     *
     * @param collection collection to resize
     */
    private void resize(ArrayIndexedCollection collection) {

        ArrayIndexedCollection first = new ArrayIndexedCollection();
        first.addAll(this);

        collection.capacity *= 2;
        collection.elements = new Object[this.capacity];
        collection.size = 0;

        collection.addAll(first);

    }

}
