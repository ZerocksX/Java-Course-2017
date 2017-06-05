package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Prime collection that provides n prime number ordered ascending starting from 2
 *
 * @author Pavao Jerebić
 */
public class PrimesCollection implements Iterable<Integer> {

    /**
     * Size
     */
    private int n;

    /**
     * Constructor that sets collection's size
     *
     * @param n size of the collection
     */
    public PrimesCollection(int n) {
        this.n = n;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PrimeIterator(n);
    }

    /**
     * Iterator that provides with prime numbers
     */
    public static class PrimeIterator implements Iterator<Integer> {

        /**
         * Collection size
         */
        private int n;
        /**
         * Element counter
         */
        private int i;
        /**
         * Current prime number
         */
        private int current;

        /**
         * Constructor that sets collection size
         *
         * @param n collection size
         */
        public PrimeIterator(int n) {
            this.n = n;
            i = 0;
            current = 1;
        }


        @Override
        public boolean hasNext() {
            return i < n;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            i++;

            try {
                current = calculateNextPrime();
            } catch (NoSuchElementException ex) {
                i = n;
                throw new NoSuchElementException(ex.getMessage());
            }

            return current;
        }

        /**
         * Helping method to calculate next prime number
         *
         * @return next prime number
         */
        private int calculateNextPrime() {
            for (int i = current + 1; i < Integer.MAX_VALUE; i++) {
                if (isPrime(i)) {
                    return i;
                }
            }
            throw new NoSuchElementException("No more prime numbers in Integer range");
        }

        /**
         * Returns true if number is prime
         *
         * @param x number
         * @return true if number is prime
         */
        private boolean isPrime(int x) {
            for (int i = 2; i < x; i++) {
                if (x % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
