package hr.fer.zemris.java.hw01;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class FactorialTest {
    /**
     * tests method Factorial.nthFactorial for input 0
     * expects 1
     * @throws Exception if needed
     */
    @Test
    public void nthFactorialZero() throws Exception {
        assertEquals(1,Factorial.nthFactorial(0));
    }

    /**
     * tests method Factorial.nthFactorial for input 1
     * expects 1
     * @throws Exception if needed
     */
    @Test
    public void nthFactorialOne() throws Exception {
        assertEquals(1,Factorial.nthFactorial(1));
    }

    /**
     * tests method Factorial.nthFactorial for input 20
     * expects 2432902008176640000
     * @throws Exception if needed
     */
    @Test
    public void nthFactorialTwenty() throws Exception {
        assertEquals(2432902008176640000L,Factorial.nthFactorial(20));
    }

    /**
     * tests method Factorial.nthFactorial for input 3
     * expects 6
     * @throws Exception if needed
     */
    @Test
    public void nthFactorialThree() throws Exception {
        assertEquals(6,Factorial.nthFactorial(3));
    }

}