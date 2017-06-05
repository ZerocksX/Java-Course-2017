package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a storage with observers
 *
 * @author Pavao Jerebić
 */
public class IntegerStorage {

    /**
     * Value
     */
    private int value;
    /**
     * Observers
     */
    private List<IntegerStorageObserver> observers; // use ArrayList here!!!

    /**
     * Constructor that sets initial value
     *
     * @param initialValue initial value
     */
    public IntegerStorage(int initialValue) {
        this.value = initialValue;
    }

    /**
     * Adds observer to observer list if it is not already in the list
     *
     * @param observer observer
     * @throws IllegalArgumentException if given observer is null
     */
    public void addObserver(IntegerStorageObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer can not be null");
        }

        if (observers == null) {
            observers = new ArrayList<>();
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes observer from observer list if it is in the list
     *
     * @param observer observer
     * @throws IllegalArgumentException if given observer is null
     */
    public void removeObserver(IntegerStorageObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer can not be null");
        }

        if (observers == null) return;

        observers.remove(observer);
    }

    /**
     * Clears whole observer list
     */
    public void clearObservers() {

        if (observers == null) return;

        observers.clear();
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value and notifies observers
     *
     * @param value value
     */
    public void setValue(int value) {
        if (this.value != value) {
            this.value = value;

            if (observers != null) {
                List<IntegerStorageObserver> finalObservers = new ArrayList<>(observers);
                for (IntegerStorageObserver observer : finalObservers) {
                    observer.valueChanged(this);
                }
            }
        }
    }

}
