package hr.fer.zemris.java.hw05.demo2;

/**
 * Demo for {@link PrimesCollection}<br/>
 * Should output first 5 prime numbers starting from 2
 *
 * @author Pavao Jerebić
 */
public class PrimesDemo1 {
    /**
     * Starting method
     * @param args ignored
     */
    public static void main(String[] args) {
        PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
        for (Integer prime : primesCollection) {
            System.out.println("Got prime: " + prime);
        }
    }
}
