package hr.fer.zemris.java.hw05.observer2;

/**
 * Observer that prints squared value of given IntegerStorage
 *
 * @author Pavao Jerebić
 */
public class SquareValue implements IntegerStorageObserver {
    /**
     * Prints squared value of given IntegerStorage
     *
     * @param integerStorageChange integer storage change
     */
    @Override
    public void valueChanged(IntegerStorageChange integerStorageChange) {
        System.out.printf("Old value: %d. Provided new value: %d, square is %d%n",
                integerStorageChange.getPreviousValue(),
                integerStorageChange.getCurrentValue(),
                integerStorageChange.getCurrentValue() * integerStorageChange.getCurrentValue()
        );
    }
}
