package hr.fer.zemris.java.hw05.observer1;

/**
 * Observer that prints the double value of given storage for n times
 *
 * @author Pavao Jerebić
 */
public class DoubleValue implements IntegerStorageObserver {

    /**
     * Number of times this observer will write a message
     */
    private int n;

    /**
     * Constructor that sets number of outputs
     *
     * @param n number of outputs
     */
    public DoubleValue(int n) {
        this.n = n;
    }

    /**
     * Prints doubled value if observer printed less than n times
     *
     * @param integerStorage subject
     */
    @Override
    public void valueChanged(IntegerStorage integerStorage) {
        if (n > 0) {
            System.out.printf("Double value: %d%n", integerStorage.getValue() * 2);
            n--;
        } else {
            integerStorage.removeObserver(this);
        }
    }
}
