package hr.fer.zemris.java.hw05.demo2;

/**
 * Demo for {@link PrimesCollection}<br/>
 * Should output four pairs of prime numbers:<br/>
 * 2,2<br/>
 * 2,3<br/>
 * 3,2<br/>
 * 3,3<br/>
 *
 * @author Pavao Jerebić
 */
public class PrimesDemo2 {
    /**
     * Starting method
     * @param args ignored
     */
    public static void main(String[] args) {
        PrimesCollection primesCollection = new PrimesCollection(2);
        for (Integer prime : primesCollection) {
            for (Integer prime2 : primesCollection) {
                System.out.println("Got prime pair: " + prime + ", " + prime2);
            }
        }
    }
}
