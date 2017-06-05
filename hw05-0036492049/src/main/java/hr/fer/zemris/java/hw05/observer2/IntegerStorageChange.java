package hr.fer.zemris.java.hw05.observer2;

/**
 * Representation of {@link IntegerStorage} change
 * @author Pavao Jerebić
 */
public class IntegerStorageChange {
    /**
     * Subject
     */
    private IntegerStorage integerStorage;
    /**
     * Old value
     */
    private int previousValue;
    /**
     * New Value
     */
    private int currentValue;

    /**
     * Constructor that sets all fields
     * @param integerStorage subject
     * @param previousValue old value
     * @param currentValue new value
     */
    public IntegerStorageChange(IntegerStorage integerStorage, int previousValue, int currentValue) {
        this.integerStorage = integerStorage;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
    }

    /**
     * Getter for integerStorage
     * @return integer storage
     */
    public IntegerStorage getIntegerStorage() {
        return integerStorage;
    }

    /**
     * Getter for previousValue
     * @return previous value
     */
    public int getPreviousValue() {
        return previousValue;
    }

    /**
     * Getter for current value
     * @return current value
     */
    public int getCurrentValue() {
        return currentValue;
    }
}
