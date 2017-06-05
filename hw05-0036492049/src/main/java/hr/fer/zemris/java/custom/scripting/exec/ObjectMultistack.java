package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a multistack<br/>
 * Different stacks are identified with strings
 */
public class ObjectMultistack {

    /**
     * Map of stack names and stack top entries
     */
    private Map<String, MultistackEntry> stacks;

    /**
     * Constructor that initializes multistack
     */
    public ObjectMultistack() {
        stacks = new HashMap<>();
    }

    /**
     * Pushes given value on stack with given name
     *
     * @param name         name of the stack
     * @param valueWrapper value
     * @throws IllegalArgumentException if name or valueWrapper is null
     */
    public void push(String name, ValueWrapper valueWrapper) {
        if (name == null || valueWrapper == null) {
            throw new IllegalArgumentException("Stack name or value can not be null");
        }
        MultistackEntry top = stacks.get(name);
        MultistackEntry temp = new MultistackEntry(valueWrapper);
        temp.previous = top;
        stacks.put(name, temp);
    }

    /**
     * Pops the top value from stack with given name
     *
     * @param name name of the stack
     * @return value of the top entry
     * @throws EmptyStackException      if stack is empty
     * @throws IllegalArgumentException if name is null
     */
    public ValueWrapper pop(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Stack name can not be null");
        }

        MultistackEntry top = stacks.get(name);
        if (top == null) {
            throw new EmptyStackException();
        }
        stacks.put(name, top.previous);
        return top.getValue();
    }

    /**
     * Peeks at the top value from the stack with given name
     *
     * @param name name of the stack
     * @return value of the top entry
     * @throws EmptyStackException      if stack is empty
     * @throws IllegalArgumentException if name is null
     */
    public ValueWrapper peek(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Stack name can not be null");
        }
        MultistackEntry top = stacks.get(name);
        if (top == null) {
            throw new EmptyStackException();
        }
        return top.getValue();
    }

    /**
     * Returns true if stack with given name is empty
     *
     * @param name name of the stack
     * @return true is stack with given name is empty
     * @throws IllegalArgumentException if name is null
     */
    public boolean isEmpty(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Stack name can not be null");
        }
        return stacks.get(name) == null;
    }

    /**
     * {@link ObjectMultistack} entry class
     */
    public static class MultistackEntry {
        /**
         * Value of the element
         */
        private ValueWrapper value;
        /**
         * Previous element
         */
        private MultistackEntry previous;

        /**
         * Constructor that sets entry value
         *
         * @param value value
         */
        public MultistackEntry(ValueWrapper value) {
            this.value = value;
        }

        /**
         * Getter for value
         *
         * @return value
         */
        public ValueWrapper getValue() {
            return value;
        }

        /**
         * Setter for value
         *
         * @param value value
         */
        public void setValue(ValueWrapper value) {
            this.value = value;
        }
    }
}