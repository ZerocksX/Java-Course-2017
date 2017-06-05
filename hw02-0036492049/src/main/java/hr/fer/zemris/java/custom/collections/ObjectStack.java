
package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Stack with objects as values
 *
 * @author Pavao Jerebić
 */
public class ObjectStack {

    private ArrayIndexedCollection collection;

    private int size;

    /**
     * Returns size of the stack
     *
     * @return size of the stack
     */
    public int size() {
        return size;
    }

    /**
     * Returns if the stack is empty
     *
     * @return is the stack empty
     */
    public boolean isEmpty() {
        return size() <= 0;
    }

    /**
     * Pushes value on top of stack
     *
     * @param value value to push
     */
    public void push(Object value) {
        collection.add(value);
        size++;
    }

    /**
     * Removes top value from stack and returns it to user
     *
     * @return top element of the stack
     */
    public Object pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        Object value = collection.get(size - 1);
        collection.remove(size - 1);
        size--;
        return value;
    }

    /**
     * Returns top element of the stack
     *
     * @return top element of the stack
     */
    public Object peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return collection.get(size - 1);
    }

    /**
     * Removes all elements from the stack
     */
    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    /**
     * Default constructor
     * Initializes stack to be empty
     */
    public ObjectStack() {
        collection = new ArrayIndexedCollection();
    }

    @Override
    public String toString() {
        return Arrays.toString(collection.toArray());
    }
}
