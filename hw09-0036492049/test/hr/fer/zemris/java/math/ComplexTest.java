package hr.fer.zemris.java.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class ComplexTest {
    private static final double doubleDelta = 0.0000001;

    @Test
    public void fromReal() throws Exception {
        assertEquals(5.0, new Complex(5, 4).getRe(), doubleDelta);
    }

    @Test
    public void fromImaginary() throws Exception {
        assertEquals(5.0, new Complex(0, 5).getIm(), doubleDelta);
    }

    @Test
    public void fromMagnitudeAndAngle() throws Exception {
        assertEquals(1, Complex.parse("1i").module(), doubleDelta);
    }

    @Test
    public void testParse() throws Exception {
        Complex c = Complex.parse("-4+3i");
        assertEquals(-4, c.getRe(), doubleDelta);
        assertEquals(3, c.getIm(), doubleDelta);
    }

    @Test
    public void testParse0() throws Exception {
        Complex c = Complex.parse("0");
        assertEquals(0, c.getRe(), doubleDelta);
        assertEquals(0, c.getIm(), doubleDelta);
    }
    @Test
    public void testParse0i() throws Exception {
        Complex c = Complex.parse("0i");
        assertEquals(0, c.getRe(), doubleDelta);
        assertEquals(0, c.getIm(), doubleDelta);
    }
    @Test
    public void testParseNeg1i0() throws Exception {
        Complex c = Complex.parse("-1+0i");
        assertEquals(-1, c.getRe(), doubleDelta);
        assertEquals(0, c.getIm(), doubleDelta);
    }

    @Test
    public void testNegate() throws Exception {
        assertEquals(-5, new Complex(0, 5).negate().getIm(), doubleDelta);
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(Complex.parse("2+4i"), (Complex.parse("2+3i").add(Complex.parse("i"))));
    }

    @Test
    public void testSub() throws Exception {
        assertEquals(Complex.parse("2+2i"), (Complex.parse("2+3i").sub(Complex.parse("i"))));
    }


    @Test
    public void testMultiply() throws Exception {
        assertEquals(Complex.parse("-3+2i"), (Complex.parse("2+3i").multiply(Complex.parse("i"))));
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals(0.5, (Complex.parse("2+3i").divide(Complex.parse("1+i"))).getIm(), doubleDelta);
    }

    @Test
    public void testPower() throws Exception {
        assertEquals(-5, (Complex.parse("2+3i").power(2)).getRe(), doubleDelta);
    }

    @Test
    public void testPowerWith0() throws Exception {
        assertEquals(1, (Complex.parse("2+3i").power(0)).getRe(), doubleDelta);
    }

    @Test
    public void testRoot() throws Exception {

        assertEquals(1.674149228, (Complex.parse("2+3i").root(2)).get(0).getRe(), doubleDelta);
    }

}