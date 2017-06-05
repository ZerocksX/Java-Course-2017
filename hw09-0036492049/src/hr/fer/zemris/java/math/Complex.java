package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a complex number<br/>
 * Supports all basic operations<br/>
 * Can parse expression as descibed in {@link Complex#parse(String)}
 *
 * @author Pavao Jerebić
 */
public class Complex {

    /**
     * Real
     */
    private double re;
    /**
     * Imaginary
     */
    private double im;

    /**
     * Zero
     */
    public static final Complex ZERO = new Complex(0, 0);
    /**
     * One
     */
    public static final Complex ONE = new Complex(1, 0);
    /**
     * Negative one
     */
    public static final Complex ONE_NEG = new Complex(-1, 0);
    /**
     * i
     */
    public static final Complex IM = new Complex(0, 1);
    /**
     * Negative i
     */
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * Delta fro comparing doubles
     */
    private static final double DELTA = 1e-6;

    /**
     * Constructor that sets real and imaginary part of a complex number
     *
     * @param re real
     * @param im imaginary
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Builds Complex form magnitude and angle
     *
     * @param magnitude magnitude
     * @param angle     angle
     * @return Complex
     */
    private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
        return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Getter for magnitude
     *
     * @return magnitude
     */
    private double getMagnitude() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Getter for angle
     *
     * @return angle
     */
    private double getAngle() {
        return Math.atan2(im, re);
    }

    /**
     * Sums complex number with given parameter
     *
     * @param c other complex number
     * @return result of the sum
     */
    public Complex add(Complex c) {
        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * Substitutes number with given parameter
     *
     * @param c complex number
     * @return result of substitution
     */
    public Complex sub(Complex c) {
        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * Multiplies number with given parameter
     *
     * @param c complex number
     * @return result of multiplication
     */
    public Complex multiply(Complex c) {
        return Complex.fromMagnitudeAndAngle(this.getMagnitude() * c.getMagnitude(), this.getAngle() + c.getAngle());
    }

    /**
     * return all results of division of number with parameter
     *
     * @param c complex number
     * @return array of complex numbers
     */
    public Complex divide(Complex c) {
        return Complex.fromMagnitudeAndAngle(this.getMagnitude() / c.getMagnitude(), this.getAngle() - c.getAngle());
    }

    /**
     * Calculates nth power of complex number
     *
     * @param n power
     * @return nth power of number
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return Complex.fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), n), this.getAngle() * n);
    }

    /**
     * Calculates nth root of complex number
     *
     * @param n root
     * @return nth root of number
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<Complex> numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numbers.add(Complex.fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), 1.0 / n), (this.getAngle() + 2 * i * Math.PI) / n));
        }
        return numbers;
    }

    /**
     * Returns module of the given complex number
     *
     * @return module
     */
    public double module() {
        return getMagnitude();
    }

    /**
     * Returns a negative version of this number
     *
     * @return negative version of this complex number
     */
    public Complex negate() {
        return new Complex(-re, -im);
    }

    /**
     * Returns a new complex number of this which is scaled by s
     *
     * @param s scale
     * @return scaled version of this
     */
    public Complex scale(int s) {
        return new Complex(re * s, im * s);
    }

    /**
     * Getter for real part
     *
     * @return real
     */
    public double getRe() {
        return re;
    }

    /**
     * Getter for imaginary part
     *
     * @return imaginary
     */
    public double getIm() {
        return im;
    }

    @Override
    public String toString() {

        if (this.equalsWithTreshold(ZERO, DELTA)) {
            return "0";
        }

        StringBuilder builder = new StringBuilder();
        if (Math.abs(re) > DELTA) {
            builder.append(re);
        }
        if (Math.abs(im) < DELTA) {
            return builder.toString();
        }
        if (im > 0) {
            builder.append("+").append(im).append("i");
        } else if (im < 0) {
            builder.append(im).append("i");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complex)) return false;

        Complex complex = (Complex) o;

        if (Double.compare(complex.re, re) != 0) return false;
        return Double.compare(complex.im, im) == 0;
    }

    /**
     * Returns true if this is equals to other with a given threshold
     *
     * @param other    other
     * @param treshold threshold
     * @return true if this equals other with threshold
     */
    public boolean equalsWithTreshold(Complex other, double treshold) {
        return (Math.abs(this.getRe() - other.getRe()) < treshold
                && Math.abs(this.getIm() - other.getIm()) < treshold);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(re);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(im);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Builds Complex from string (accepts strings such as: "3.51", "-3.17", "-2.71i", "i", "1","-2.71-3.15i")
     *
     * @param s string
     * @return Complex
     */
    public static Complex parse(String s) {

        Pattern pattern = Pattern.compile("[+-]?\\d*\\.?\\d+");
        Matcher matcher = pattern.matcher(s);

        List<Double> values = new ArrayList<>();

        while (matcher.find()) {
            values.add(Double.parseDouble(matcher.group()));
        }

        if (values.size() == 2) {
            return new Complex(values.get(0), values.get(1));

        } else if (values.size() == 1) {
            if (s.charAt(s.length() - 1) == 'i') {
                if (s.charAt(s.length() - 2) == '+' || s.charAt(s.length() - 2) == '-') {
                    return new Complex(values.get(0), s.charAt(s.length() - 2) == '-' ? -1 : 1);
                } else {
                    return new Complex(0, values.get(0));
                }

            } else {
                return new Complex(values.get(0), 0);

            }
        } else if (s.charAt(s.length() - 1) == 'i') {
            return new Complex(0, s.charAt(0) == '-' ? -1 : 1);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
