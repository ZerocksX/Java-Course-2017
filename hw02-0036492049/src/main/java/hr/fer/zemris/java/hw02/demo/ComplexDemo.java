package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

/**
 * Demo representation of {@link ComplexNumber} class
 *
 * @author Pavao Jerebić
 */
public class ComplexDemo {
    /**
     * Main method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        System.out.println(ComplexNumber.parse("1+i"));
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
        ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57))
                .div(c2).power(3).root(2)[1];
        System.out.println(c3);
    }
}
