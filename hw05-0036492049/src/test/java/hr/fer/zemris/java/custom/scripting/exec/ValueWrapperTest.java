package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class ValueWrapperTest {
    @Test
    public void testNullAddition() throws Exception {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
        assertEquals(0, v1.getValue());
        assertNull(v2.getValue());
    }

    @Test
    public void testDoubleAdditionWithInteger() throws Exception {
        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(1);
        v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
        assertEquals(13.0, v3.getValue());
        assertEquals(1, v4.getValue());

    }

    @Test
    public void testStringAsIntegerAdditionWithInteger() throws Exception {
        ValueWrapper v5 = new ValueWrapper("12");
        ValueWrapper v6 = new ValueWrapper(1);
        v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
        assertEquals(13, v5.getValue());
        assertEquals(1, v6.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testStringAdditionWithInteger() throws Exception {
        ValueWrapper v7 = new ValueWrapper("Ankica");
        ValueWrapper v8 = new ValueWrapper(1);
        v7.add(v8.getValue()); // throws RuntimeException
        fail();
    }

    @Test
    public void testIntegerMultiplicationWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.multiply(v2.getValue());
        assertEquals(35, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test
    public void testStringAsIntegerMultiplicationWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper("15");
        ValueWrapper v2 = new ValueWrapper(7.0);
        v1.multiply(v2.getValue());
        assertEquals(105.0, v1.getValue());
        assertEquals(7.0, v2.getValue());
    }

    @Test
    public void testDoubleMultiplicationWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5.0);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.multiply(v2.getValue());
        assertEquals(35.0, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test
    public void testNullMultiplicationWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.multiply(v2.getValue());
        assertEquals(0, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testStringMultiplicationWithInteger() throws Exception {

        ValueWrapper v1 = new ValueWrapper("GRRRR");
        ValueWrapper v2 = new ValueWrapper(7);
        v1.multiply(v2.getValue());
        fail();
    }

    @Test
    public void testIntegerSubtractionWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.subtract(v2.getValue());
        assertEquals(-2, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test
    public void testStringAsIntegerSubtractionWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper("15");
        ValueWrapper v2 = new ValueWrapper(7.0);
        v1.subtract(v2.getValue());
        assertEquals(8.0, v1.getValue());
        assertEquals(7.0, v2.getValue());
    }

    @Test
    public void testDoubleSubtractionWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5.0);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.subtract(v2.getValue());
        assertEquals(-2.0, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testStringSubtractionWithInteger() throws Exception {

        ValueWrapper v1 = new ValueWrapper("GRRRR");
        ValueWrapper v2 = new ValueWrapper(7);
        v1.subtract(v2.getValue());
        fail();
    }

    @Test
    public void testIntegerDivisionWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(7);
        v1.divide(v2.getValue());
        assertEquals(0, v1.getValue());
        assertEquals(7, v2.getValue());
    }

    @Test
    public void testStringAsIntegerDivisionWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper("15");
        ValueWrapper v2 = new ValueWrapper(4.0);
        v1.divide(v2.getValue());
        assertEquals(3.75, v1.getValue());
        assertEquals(4.0, v2.getValue());
    }

    @Test
    public void testDoubleDivisionWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(15.0);
        ValueWrapper v2 = new ValueWrapper(4);
        v1.divide(v2.getValue());
        assertEquals(3.75, v1.getValue());
        assertEquals(4, v2.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testStringDivisionWithInteger() throws Exception {

        ValueWrapper v1 = new ValueWrapper("GRRRR");
        ValueWrapper v2 = new ValueWrapper(7);
        v1.divide(v2.getValue());
        fail();
    }

    @Test(expected = RuntimeException.class)
    public void testIntegerDivisionWithZero() throws Exception {

        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(0);
        v1.divide(v2.getValue());
        fail();
    }

    @Test
    public void testNullNumCompareWithNum() throws Exception {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        int c = v1.numCompare(v2.getValue());
        assertNull(v1.getValue());
        assertEquals(0,c);

    }

    @Test
    public void testNullNumCompareWithInteger() throws Exception {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(5);
        int c = v1.numCompare(v2.getValue());
        assertNull(v1.getValue());
        assertTrue(c < 0);

    }

    @Test
    public void testIntegerNumCompareWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper(5);
        ValueWrapper v2 = new ValueWrapper(3.14);
        int c = v1.numCompare(v2.getValue());
        assertEquals(5,v1.getValue());
        assertTrue(c > 0);
    }

    @Test
    public void testStringAsDoubleNumCompareWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper("4.21E1");
        ValueWrapper v2 = new ValueWrapper(3.14);
        int c = v1.numCompare(v2.getValue());
        assertEquals("4.21E1",v1.getValue());
        assertTrue(c > 0);
    }

    @Test(expected = RuntimeException.class)
    public void testStringNumCompareWithDouble() throws Exception {
        ValueWrapper v1 = new ValueWrapper("4.21E1ddd");
        ValueWrapper v2 = new ValueWrapper(3.14);
        v1.numCompare(v2.getValue());
        fail();
    }


}