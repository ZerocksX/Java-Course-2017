package hr.fer.zemris.java.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class ComplexPolynomialTest {
    @Test
    public void testMultiply() throws Exception {
        ComplexPolynomial p1 = new ComplexPolynomial(Complex.ONE, Complex.ZERO, Complex.ONE);
        ComplexPolynomial p2 = new ComplexPolynomial(Complex.ONE, Complex.ZERO);
        System.out.println(p1.multiply(p2));
    }

    @Test
    public void testApply() throws Exception {
        ComplexPolynomial p1 = new ComplexPolynomial(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE_NEG);
        System.out.println(p1.apply(new Complex(0.5,-0.2)));
    }
}