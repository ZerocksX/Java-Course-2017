package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author ne ja
 */
public class ValueWrapperTest2 {
    @Test
    public void testAdd2Integers() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));

        v1.add(v2.getValue());

        assertEquals(7, v1.getValue());
    }

    @Test
    public void testAdd2Null() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        v1.add(v2.getValue());

        assertEquals(0, v1.getValue());
    }

    @Test
    public void testAdd2Doubles() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(2.0));

        v1.add(v2.getValue());

        assertEquals(7.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd2Doubles3Decimals() {
        ValueWrapper v1 = new ValueWrapper(5.222);
        ValueWrapper v2 = new ValueWrapper(4.444);

        v1.add(v2.getValue());

        assertEquals(9.666, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1String1Integer() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.add(v2.getValue());

        assertEquals(15, v1.getValue());
    }

    @Test
    public void testAdd1DoubleString1Integer() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.add(v2.getValue());

        assertEquals(15.3, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1DoubleEString1Integer() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.add(v2.getValue());

        assertEquals(15.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1Double1Integer() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.2));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.add(v2.getValue());

        assertEquals(8.2, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1String1Double() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(15.6));

        v1.add(v2.getValue());

        assertEquals(27.6, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1DoubleString1Double() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(8.76));

        v1.add(v2.getValue());

        assertEquals(21.06, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testAdd1DoubleEString1Double() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.add(v2.getValue());

        assertEquals(15.0, (Double)v1.getValue(), 1e-6);
    }

    @Test (expected = RuntimeException.class)
    public void testAddAnkica() {
        ValueWrapper v1 = new ValueWrapper("Ankica");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.add(v2.getValue());
    }

    //	---------------------	-	-------------------------

    @Test
    public void testSub2Integers() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));

        v1.subtract(v2.getValue());

        assertEquals(3, v1.getValue());
    }

    @Test
    public void testSub2Null() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        v1.subtract(v2.getValue());

        assertEquals(0, v1.getValue());
    }

    @Test
    public void testSub2Doubles() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(2.0));

        v1.subtract(v2.getValue());

        assertEquals(3.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub2Doubles3Decimals() {
        ValueWrapper v1 = new ValueWrapper(5.222);
        ValueWrapper v2 = new ValueWrapper(4.444);

        v1.subtract(v2.getValue());

        assertEquals(0.778, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1String1Integer() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.subtract(v2.getValue());

        assertEquals(9, v1.getValue());
    }

    @Test
    public void testSub1DoubleString1Integer() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.subtract(v2.getValue());

        assertEquals(9.3, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1DoubleEString1Integer() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.subtract(v2.getValue());

        assertEquals(9.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1Double1Integer() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.2));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.subtract(v2.getValue());

        assertEquals(2.2, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1String1Double() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(15.6));

        v1.subtract(v2.getValue());

        assertEquals(-3.6, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1DoubleString1Double() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(8.76));

        v1.subtract(v2.getValue());

        assertEquals(3.54, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testSub1DoubleEString1Double() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.subtract(v2.getValue());

        assertEquals(9.0, (Double)v1.getValue(), 1e-6);
    }

    @Test (expected = RuntimeException.class)
    public void testSubAnkica() {
        ValueWrapper v1 = new ValueWrapper("Ankica");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.subtract(v2.getValue());
    }

    //	---------------------	*	-------------------------

    @Test
    public void testMul2Integers() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));

        v1.multiply(v2.getValue());

        assertEquals(10, v1.getValue());
    }

    @Test
    public void testMul2Null() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        v1.multiply(v2.getValue());

        assertEquals(0, v1.getValue());
    }

    @Test
    public void testMul2Doubles() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(2.0));

        v1.multiply(v2.getValue());

        assertEquals(10.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul2Doubles3Decimals() {
        ValueWrapper v1 = new ValueWrapper(5.222);
        ValueWrapper v2 = new ValueWrapper(4.444);

        v1.multiply(v2.getValue());

        assertEquals(23.206568, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1String1Integer() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.multiply(v2.getValue());

        assertEquals(36, v1.getValue());
    }

    @Test
    public void testMul1DoubleString1Integer() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.multiply(v2.getValue());

        assertEquals(36.9, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1DoubleEString1Integer() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.multiply(v2.getValue());

        assertEquals(36.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1Double1Integer() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.2));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.multiply(v2.getValue());

        assertEquals(15.6, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1String1Double() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(15.6));

        v1.multiply(v2.getValue());

        assertEquals(187.2, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1DoubleString1Double() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(8.76));

        v1.multiply(v2.getValue());

        assertEquals(107.748, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testMul1DoubleEString1Double() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.multiply(v2.getValue());

        assertEquals(36.0, (Double)v1.getValue(), 1e-6);
    }

    @Test (expected = RuntimeException.class)
    public void testMulAnkica() {
        ValueWrapper v1 = new ValueWrapper("Ankica");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.multiply(v2.getValue());
    }

    //	---------------------	/	-------------------------

    @Test
    public void testDiv2Integers() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));

        v1.divide(v2.getValue());

        assertEquals(2, v1.getValue());
    }

    @Test (expected = RuntimeException.class)
    public void testDiv2Null() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        v1.divide(v2.getValue());
    }

    @Test
    public void testDiv2Doubles() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(2.0));

        v1.divide(v2.getValue());

        assertEquals(2.5, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv2Doubles3Decimals() {
        ValueWrapper v1 = new ValueWrapper(5.222);
        ValueWrapper v2 = new ValueWrapper(4.444);

        v1.divide(v2.getValue());

        assertEquals(1.175067507, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1String1Integer() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.divide(v2.getValue());

        assertEquals(4, v1.getValue());
    }

    @Test
    public void testDiv1DoubleString1Integer() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.divide(v2.getValue());

        assertEquals(4.1, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1DoubleEString1Integer() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.divide(v2.getValue());

        assertEquals(4.0, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1Double1Integer() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.2));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(3));

        v1.divide(v2.getValue());

        assertEquals(1.733333333, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1String1Double() {
        ValueWrapper v1 = new ValueWrapper("12");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(15.6));

        v1.divide(v2.getValue());

        assertEquals(0.769230769, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1DoubleString1Double() {
        ValueWrapper v1 = new ValueWrapper("12.3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(8.76));

        v1.divide(v2.getValue());

        assertEquals(1.404109589, (Double)v1.getValue(), 1e-6);
    }

    @Test
    public void testDiv1DoubleEString1Double() {
        ValueWrapper v1 = new ValueWrapper("1.2E1");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.divide(v2.getValue());

        assertEquals(4.0, (Double)v1.getValue(), 1e-6);
    }

    @Test(expected = RuntimeException.class)
    public void testDivAnkica() {
        ValueWrapper v1 = new ValueWrapper("Ankica");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        v1.divide(v2.getValue());
    }

    //	---------------------	numCompare	-------------------------

    @Test
    public void testNumCompare2Null() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        assertEquals(true, v1.numCompare(v2.getValue()) == 0);
    }

    @Test
    public void testNumCompare1Null1Integer() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare1Null1Double() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(3.0));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare2SameIntegers() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) == 0);
    }

    @Test
    public void testNumCompare2IntegersGreater() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(5));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));

        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare2IntegersLess() {
        ValueWrapper v1 = new ValueWrapper(Integer.valueOf(2));
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare2SameDouble() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) == 0);
    }

    @Test
    public void testNumCompare2SameDoubleDifferent8thDecimal() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.00000001));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.00000002));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare2DoublesGreater() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(5.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(2.0));

        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare2DoublesLess() {
        ValueWrapper v1 = new ValueWrapper(Double.valueOf(2.0));
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    // 1 String 1 Integer

    @Test
    public void testNumCompare1String1IntegerEquals() {
        ValueWrapper v1 = new ValueWrapper("5");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) == 0);
    }

    @Test
    public void testNumCompare1String1IntegerGreater() {
        ValueWrapper v1 = new ValueWrapper("7");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare1String1IntegerLess() {
        ValueWrapper v1 = new ValueWrapper("3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare1String1IntegerEqualsReversed() {
        ValueWrapper v1 = new ValueWrapper("5");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(true, v2.numCompare(v1.getValue()) == 0);
    }

    @Test
    public void testNumCompare1String1IntegerGreaterReversed() {
        ValueWrapper v1 = new ValueWrapper("7");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(false, v2.numCompare(v1.getValue()) > 0);
    }

    @Test
    public void testNumCompare1String1IntegerLessReversed() {
        ValueWrapper v1 = new ValueWrapper("3");
        ValueWrapper v2 = new ValueWrapper(Integer.valueOf(5));

        assertEquals(false, v2.numCompare(v1.getValue()) < 0);
    }

    // 1 String 1 Double

    @Test
    public void testNumCompare1String1DoubleEquals() {
        ValueWrapper v1 = new ValueWrapper("5");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) == 0);
    }

    @Test
    public void testNumCompare1String1DoubleGreater() {
        ValueWrapper v1 = new ValueWrapper("7");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare1String1DoubleLess() {
        ValueWrapper v1 = new ValueWrapper("3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare1String1DoubleEqualsReversed() {
        ValueWrapper v1 = new ValueWrapper("5");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v2.numCompare(v1.getValue()) == 0);
    }

    @Test
    public void testNumCompare1String1DoubleGreaterReversed() {
        ValueWrapper v1 = new ValueWrapper("7");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(false, v2.numCompare(v1.getValue()) > 0);
    }

    @Test
    public void testNumCompare1String1DoubleLessReversed() {
        ValueWrapper v1 = new ValueWrapper("3");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(false, v2.numCompare(v1.getValue()) < 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleEquals() {
        ValueWrapper v1 = new ValueWrapper("5.00000001");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));
        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleGreater() {
        ValueWrapper v1 = new ValueWrapper("7.23");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) > 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleLess() {
        ValueWrapper v1 = new ValueWrapper("3.54");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v1.numCompare(v2.getValue()) < 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleEqualsReversed() {
        ValueWrapper v1 = new ValueWrapper("5.00000001");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(true, v2.numCompare(v1.getValue()) < 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleGreaterReversed() {
        ValueWrapper v1 = new ValueWrapper("7.29");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(false, v2.numCompare(v1.getValue()) > 0);
    }

    @Test
    public void testNumCompare1StringD1DoubleLessReversed() {
        ValueWrapper v1 = new ValueWrapper("3.99");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        assertEquals(false, v2.numCompare(v1.getValue()) < 0);
    }

    //Invalid String test parsing
    @Test (expected = RuntimeException.class)
    public void testInvalidStringParsing() {
        ValueWrapper v1 = new ValueWrapper("3.99.");
        ValueWrapper v2 = new ValueWrapper(Double.valueOf(5.0));

        v1.add(v2.getValue());
    }

}
