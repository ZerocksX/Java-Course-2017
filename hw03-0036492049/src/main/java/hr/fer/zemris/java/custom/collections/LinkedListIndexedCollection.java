package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Collection implemented as double linked list
 *
 * @author Pavao Jerebić
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * List node
     */
    private static class ListNode {
        /**
         * value
         */
        Object value;
        /**
         * previous node
         */
        ListNode previous;
        /**
         * next node
         */
        ListNode next;
    }

    /**
     * size of the list
     */
    private int size;
    /**
     * first element
     */
    private ListNode first;
    /**
     * last element
     */
    private ListNode last;

    /**
     * Initializes all to null
     */
    public LinkedListIndexedCollection() {
        first = last = null;
    }

    /**
     * Constructor that takes other collection and adds all elements to this collection
     *
     * @param other other collection
     */
    public LinkedListIndexedCollection(Collection other) {
        addAll(other);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Adds value into collection
     * If given value is null then throws {@link IllegalArgumentException}
     *
     * @param value inserted object
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        if (first == null) {
            ListNode node = new ListNode();
            node.value = value;
            first = last = node;
        } else {
            ListNode node = new ListNode();
            node.value = value;
            node.previous = last;
            last.next = node;
            last = node;
        }
        size++;
    }

    @Override
    public boolean contains(Object value) {
        ListNode temp = first;
        while (temp != null) {
            if (temp.value.equals(value)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object value) {
        ListNode temp = first;
        while (temp != null) {
            if (temp.value.equals(value)) {
                temp.previous.next = temp.next;
                temp.next.previous = temp.previous;
                size--;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (ListNode temp = first; temp != null; temp = temp.next) {
            array[i++] = temp.value;
        }
        return array;
    }

    @Override
    public void forEach(Processor processor) {
        for (ListNode temp = first; temp != null; temp = temp.next) {
            processor.process(temp.value);
        }
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Returns value at given index
     * If index is out of bounds then throws {@link IndexOutOfBoundsException}
     *
     * @param index index of the node
     * @return value at given node
     */
    public Object get(int index) {
        int i = 0;

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        for (ListNode temp = first; temp != null; temp = temp.next) {
            if (i++ == index) {
                return temp.value;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * Inserts(without erasing) value at given position
     * If position is out of bounds throws {@link IndexOutOfBoundsException}
     *
     * @param value    value
     * @param position position to insert into
     */
    public void insert(Object value, int position) {
        int i = 0;

        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException();
        }

        if (first == null) {
            ListNode node = new ListNode();
            node.value = value;
            first = last = node;
        } else if (position == size) {
            add(value);
        } else {
            ListNode node = new ListNode();
            node.value = value;
            for (ListNode temp = first; temp != null; temp = temp.next) {
                if (i == position) {
                    node.next = temp;
                    node.previous = temp.previous;
                    temp.previous.next = temp.next.previous = node;
                    return;
                }
                i++;
            }
        }
    }

    /**
     * Returns index of given value. If value doesn't exist in the collection then returns -1
     *
     * @param value given value
     * @return -1 if value doesn't exist, index if it does
     */
    public int indexOf(Object value) {
        int i = 0;
        for (ListNode temp = first; temp != null; temp = temp.next) {
            if (temp.value.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes node at given index
     * If index is out of bounds throws {@link IndexOutOfBoundsException}
     *
     * @param index given index
     */
    void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        int i = 0;
        for (ListNode temp = first; temp != null; temp = temp.next) {
            if (i++ == index) {
                temp.previous.next = temp.next;
                temp.next.previous = temp.previous;
                size--;
                return;
            }
        }

    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
