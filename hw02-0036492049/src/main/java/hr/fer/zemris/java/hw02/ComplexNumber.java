package hr.fer.zemris.java.hw02;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Complex number representation with basic functionality
 * Real + Imaginary * i
 *
 * @author Pavao Jerebić
 */
public class ComplexNumber {
    /**
     * Real part
     */
    private double real;
    /**
     * Imaginary part
     */
    private double imaginary;

    /**
     * Constructor that sets real and imaginary value
     *
     * @param real      real value
     * @param imaginary imaginary value
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Builds ComplexNumber form real value
     *
     * @param real real value
     * @return ComplexNumber
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0.0);
    }

    /**
     * Builds ComplexNumber form imaginary value
     *
     * @param imaginary imaginary value
     * @return ComplexNumber
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0.0, imaginary);
    }

    /**
     * Builds ComplexNumber form magnitude and angle
     *
     * @param magnitude magnitude
     * @param angle     angle
     * @return ComplexNumber
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    /**
     * Builds ComplexNumber from string (accepts strings such as: "3.51", "-3.17", "-2.71i", "i", "1","-2.71-3.15i")
     *
     * @param s string
     * @return ComplexNumber
     */
    public static ComplexNumber parse(String s) {

        Pattern pattern = Pattern.compile("[+-]?\\d*\\.?\\d+");
        Matcher matcher = pattern.matcher(s);

        ArrayList<Double> values = new ArrayList<>();

        while (matcher.find()) {
            values.add(Double.parseDouble(matcher.group()));
        }

        if (values.size() == 2) {
            return new ComplexNumber(values.get(0), values.get(1));

        } else if (values.size() == 1) {
            if (s.charAt(s.length() - 1) == 'i') {
                if(s.charAt(s.length()-2)=='+' || s.charAt(s.length()-2)=='-'){
                    return new ComplexNumber(values.get(0),s.charAt(s.length()-2) == '-' ? -1 : 1);
                }else{
                    return new ComplexNumber(0, values.get(0));
                }

            } else {
                return new ComplexNumber(values.get(0), 0);

            }
        } else if (s.charAt(s.length()-1) == 'i') {
            return new ComplexNumber(0, s.charAt(0) == '-' ? -1 : 1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter for real value
     *
     * @return real value
     */
    public double getReal() {
        return real;
    }

    /**
     * Getter for imaginary value
     *
     * @return imaginary value
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * Getter for magnitude
     *
     * @return magnitude
     */
    public double getMagnitude() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    /**
     * Getter for angle
     *
     * @return angle
     */
    public double getAngle() {
        return Math.atan2(imaginary, real);
    }

    /**
     * Sums complex number with given parameter
     *
     * @param c other complex number
     * @return result of the sum
     */
    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Substitutes number with given parameter
     *
     * @param c complex number
     * @return result of substitution
     */
    public ComplexNumber sub(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies number with given parameter
     *
     * @param c complex number
     * @return result of multiplication
     */
    public ComplexNumber mul(ComplexNumber c) {
        return ComplexNumber.fromMagnitudeAndAngle(this.getMagnitude() * c.getMagnitude(), this.getAngle() + c.getAngle());
    }

    /**
     * return all results of division of number with parameter
     *
     * @param c complex number
     * @return array of complex numbers
     */
    public ComplexNumber div(ComplexNumber c) {
        return ComplexNumber.fromMagnitudeAndAngle(this.getMagnitude() / c.getMagnitude(), this.getAngle() - c.getAngle());
    }

    /**
     * Calculates nth power of complex number
     *
     * @param n power
     * @return nth power of number
     */
    public ComplexNumber power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return ComplexNumber.fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), n), this.getAngle() * n);
    }

    /**
     * Calculates nth root of complex number
     *
     * @param n root
     * @return nth root of number
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<ComplexNumber> numbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numbers.add(ComplexNumber.fromMagnitudeAndAngle(Math.pow(this.getMagnitude(), 1.0 / n), (this.getAngle() + 2 * i * Math.PI) / n));
        }

        return numbers.toArray(new ComplexNumber[n]);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (real != 0) {
            builder.append(real);
        }
        if (imaginary > 0) {
            builder.append("+").append(imaginary).append("i");
        } else if (imaginary < 0) {
            builder.append(imaginary).append("i");
        }
        return builder.toString();
    }
}
