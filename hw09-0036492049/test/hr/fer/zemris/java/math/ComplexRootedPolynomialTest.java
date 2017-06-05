package hr.fer.zemris.java.math;

import org.junit.Test;

/**
 * @author Pavao Jerebić
 */
public class ComplexRootedPolynomialTest {
    @Test
    public void toComplexPolynom() throws Exception {
        ComplexRootedPolynomial p = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG);
        System.out.println(p.toComplexPolynom());
        System.out.println(p.toComplexPolynom().derive());
    }

    @Test
    public void testIndexOfClosestRootFor() throws Exception {
        ComplexRootedPolynomial p = new ComplexRootedPolynomial(Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG);
        System.out.println(p.indexOfClosestRootFor(new Complex(-0.9995, 0), 0.001));
    }
}