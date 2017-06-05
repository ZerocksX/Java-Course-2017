package hr.fer.zemris.java.hw05.observer2;

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
     * @param integerStorageChange integer storage change
     */
    @Override
    public void valueChanged(IntegerStorageChange integerStorageChange) {
        if (n > 0) {
            System.out.printf("Old value: %d. Double value: %d%n",
                    integerStorageChange.getPreviousValue(),
                    integerStorageChange.getCurrentValue() * 2
            );
            n--;
        } else {
            integerStorageChange.getIntegerStorage().removeObserver(this);
        }
    }
}
