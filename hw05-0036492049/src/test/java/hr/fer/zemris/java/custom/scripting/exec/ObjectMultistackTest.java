package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class ObjectMultistackTest {

    private ObjectMultistack multistack;

    @Before
    public void setUp() throws Exception {
        multistack  = new ObjectMultistack();
    }

    @Test
    public void pushAndPeek() throws Exception {
        multistack.push("ime", new ValueWrapper(4));
        assertEquals(0 , multistack.peek("ime").numCompare(4));
    }

    @Test
    public void pop() throws Exception {
        multistack.push("ime", new ValueWrapper(4));
        multistack.pop("ime");
        assertTrue(multistack.isEmpty("ime"));
    }

    @Test(expected = EmptyStackException.class)
    public void peekWhenEmpty() throws Exception {
        multistack.peek("ime");
        fail();
    }

    @Test(expected = EmptyStackException.class)
    public void popWhenEmpty() throws Exception {
        multistack.pop("ime");
        fail();
    }

    @Test
    public void isEmptyTrue() throws Exception {
        assertTrue(multistack.isEmpty("blahblah"));
    }

    @Test
    public void isEmptyFalse() throws Exception {
        multistack.push("Ime",new ValueWrapper("555"));
        assertFalse(multistack.isEmpty("Ime"));
    }

}