package hr.fer.zemris.java.math;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class Vector3Test {

    public static final double DELTA = 1e-6;

    @Test
    public void test00() throws Exception {
        Vector3 i = new Vector3(1, 0, 0);
        Vector3 j = new Vector3(0, 1, 0);
        Vector3 k = i.cross(j);
        Vector3 l = k.add(j).scale(5);
        Vector3 m = l.normalized();
        assertEquals("(1.000000, 0.000000, 0.000000)", i.toString());
        assertEquals("(0.000000, 1.000000, 0.000000)", j.toString());
        assertEquals("(0.000000, 0.000000, 1.000000)", k.toString());
        assertEquals("(0.000000, 5.000000, 5.000000)", l.toString());
        assertEquals(7.0710678118654755, l.norm(), DELTA);
        assertEquals("(0.000000, 0.707107, 0.707107)", m.toString());
        assertEquals(5, l.dot(j), DELTA);
        assertEquals(0.4999999999999999, i.add(new Vector3(0, 1, 0)).cosAngle(l), DELTA);
    }

    @Test
    public void testNormOn0() throws Exception {
        Vector3 v = new Vector3(0, 0, 0);
        assertEquals(0, v.norm(), DELTA);
    }

    @Test
    public void testNorm() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals(Math.sqrt(3), v.norm(), DELTA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNormalizedOn0() throws Exception {
        Vector3 v = new Vector3(0, 0, 0);
        v.normalized();
    }

    @Test
    public void testNormalized() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals("(0.577350, 0.577350, 0.577350)", v.normalized().toString());
    }

    @Test
    public void testAddition() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals(new Vector3(1, 2, 1), v.add(new Vector3(0, 1, 0)));
    }

    @Test
    public void testSubstitution() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals(new Vector3(1, 0, 1), v.sub(new Vector3(0, 1, 0)));
    }

    @Test
    public void testDotProduct() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals(0, v.dot(new Vector3(0, 0, 0)), DELTA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCosAngleOn0() throws Exception {
        Vector3 v = new Vector3(1, 1, 1);
        assertEquals(0, v.cosAngle(new Vector3(0, 0, 0)), DELTA);
    }

    @Test
    public void testCosAngle() throws Exception {
        Vector3 v = new Vector3(1, 0, 0);
        assertEquals(0, v.cosAngle(new Vector3(0, 1, 0)), DELTA);
    }

    @Test
    public void testToArray() throws Exception {
        Vector3 v = new Vector3(1, 0, 0);
        assertArrayEquals(new double[]{1, 0, 0}, v.toArray(), DELTA);
    }

    @Test
    public void testToString() throws Exception {
        Vector3 v = new Vector3(1, 0, 0);
        assertEquals("(1.000000, 0.000000, 0.000000)", v.toString());
    }
}