package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a complex polynomial with given roots
 *
 * @author Pavao Jerebić
 */
public class ComplexRootedPolynomial {

    /**
     * roots
     */
    private List<Complex> roots;

    /**
     * Constructor that sets roots of the complex polynomial
     *
     * @param roots roots
     */
    public ComplexRootedPolynomial(Complex... roots) {
        this.roots = new ArrayList<>();
        this.roots.addAll(Arrays.asList(roots));
    }

    /**
     * Computes polynomial value at given point z
     *
     * @param z point
     * @return polynomial value
     */
    public Complex apply(Complex z) {
        int n = roots.size();
        if (n == 0) {
            return Complex.ZERO;
        }

        Complex result = z.sub(roots.get(0));

        for (int i = 1; i < n; i++) {
            result = result.multiply(z.sub(roots.get(i)));
        }

        return result;
    }

    /**
     * Converts this representation to ComplexPolynomial type
     *
     * @return ComplexPolynomial representation of this
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(Complex.ONE, roots.get(0).negate());
        for (int i = 1, n = roots.size(); i < n; i++) {
            result = result.multiply(new ComplexPolynomial(Complex.ONE, roots.get(i).negate()));
        }
        return result;
    }

    @Override
    public String toString() {
        return roots.toString();
    }

    /**
     * Finds index of closest root for given complex number z that is within treshold<br/>
     * if there is no such root, returns -1
     *
     * @param z        complex number
     * @param treshold treshold
     * @return index of first root that is within treshold
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        int i = 0;
        int index = -1;
        double minDist = Double.MAX_VALUE;
        for (Complex root : roots) {
            double dist = root.sub(z).module();
            if (dist < minDist && dist < treshold) {
                index = i + 1;
                minDist = dist;
            }
            i++;
        }
        return index;
    }
}
