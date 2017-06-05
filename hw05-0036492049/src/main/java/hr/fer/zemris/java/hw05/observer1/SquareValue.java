package hr.fer.zemris.java.hw05.observer1;

/**
 * Observer that prints squared value of given IntegerStorage
 *
 * @author Pavao Jerebić
 */
public class SquareValue implements IntegerStorageObserver {
    /**
     * Prints squared value of given IntegerStorage
     *
     * @param integerStorage integer storage
     */
    @Override
    public void valueChanged(IntegerStorage integerStorage) {
        System.out.printf("Provided new value: %d, square is %d%n",
                integerStorage.getValue(),
                integerStorage.getValue() * integerStorage.getValue()
        );
    }
}
