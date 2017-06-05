package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a complex polynomial
 *
 * @author Pavao Jerebić
 */
public class ComplexPolynomial {

    /**
     * Factors
     */
    private List<Complex> factors;
    /**
     * Size
     */
    private int size;

    /**
     * Constructor
     *
     * @param factors factors
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = new ArrayList<>();
        this.factors.addAll(Arrays.asList(factors));
        size = factors.length;
    }


    /**
     * Returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
     *
     * @return order of this polynom
     */
    public short order() {
        return (short) (size - 1);
    }

    /**
     * computes a new polynomial this*p
     *
     * @param p other complex polynomial
     * @return result of this*p
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        int size = this.order() + p.order() + 1;
        Complex[] factors = new Complex[size];
        for (int i = 0; i < size; i++) {
            factors[i] = Complex.ZERO;
        }

        for (int i = 0, n = this.order() + 1; i < n; i++) {
            for (int j = 0, m = p.order() + 1; j < m; j++) {
                Complex temp = this.factors.get(i).multiply(p.factors.get(j));
                factors[i + j] = factors[i + j].add(temp);
            }
        }

        return new ComplexPolynomial(factors);
    }


    /**
     * computes first derivative of this polynomial; for example, for<br/>
     * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     *
     * @return first derivative of this
     */
    public ComplexPolynomial derive() {
        int n = order();
        Complex[] factors = new Complex[n];
        for (int i = 0; i < n; i++) {
            factors[i] = this.factors.get(i).scale(n - i);
        }
        return new ComplexPolynomial(factors);
    }

    /**
     * Computes polynomial value at given point z
     *
     * @param z point
     * @return polynomial value
     */
    public Complex apply(Complex z) {

        int n = order();

        if (n == 0) {
            return this.factors.get(0);
        }

        Complex result = this.factors.get(0);
        result = result.multiply(z.power(n));
        for (int i = 1; i <= n; i++) {
            result = result.add(this.factors.get(i).multiply(z.power(n - i)));
        }

        return result;
    }

    @Override
    public String toString() {
        return factors.toString();
    }

}
