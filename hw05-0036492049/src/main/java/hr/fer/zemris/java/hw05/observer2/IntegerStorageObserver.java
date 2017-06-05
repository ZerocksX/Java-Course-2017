package hr.fer.zemris.java.hw05.observer2;


/**
 * Observer for {@link IntegerStorage}
 * @author Pavao Jerebić
 */
public interface IntegerStorageObserver {
    /**
     * To be called when value is changed
     * @param integerStorageChange integer storage change
     */
    public void valueChanged(IntegerStorageChange integerStorageChange);
}
