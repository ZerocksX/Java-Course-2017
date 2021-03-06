package hr.fer.zemris.java.hw05.observer2;

/**
 * Observer that prints number of changes since it was first called
 *
 * @author Pavao Jerebić
 */
public class ChangeCounter implements IntegerStorageObserver {
    /**
     * Number of changes
     */
    private int i = 0;

    /**
     * Prints number of changes
     *
     * @param integerStorageChange integer storage change
     */
    @Override
    public void valueChanged(IntegerStorageChange integerStorageChange) {
        System.out.printf("Number of value changes since tracking: %d%n", ++i);
    }
}
