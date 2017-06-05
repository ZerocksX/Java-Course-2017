package hr.fer.zemris.java.custom.collections;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectStackTest {

    ObjectStack stack;

    @Before
    public void setUp() throws Exception {
        stack = new ObjectStack();
    }

    @Test
    public void size() throws Exception {
        assertEquals(0, stack.size());
    }

    @Test
    public void isEmpty() throws Exception {
        assertEquals(true, stack.isEmpty());
    }

    @Test
    public void push() throws Exception {
        stack.push(5);
        stack.push(2);
        assertEquals(2, stack.size());
    }

    @Test
    public void pop() throws Exception {
        stack.push(3);
        stack.push(4);
        assertEquals(4, stack.pop());
        assertEquals(1, stack.size());
    }

    @Test
    public void peek() throws Exception {
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.peek());
    }

    @Test
    public void clear() throws Exception {
        stack.push(3);
        stack.push(1);
        stack.clear();
        assertEquals(true, stack.isEmpty());
    }

}