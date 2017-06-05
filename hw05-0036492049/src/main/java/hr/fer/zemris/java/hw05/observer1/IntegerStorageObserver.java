package hr.fer.zemris.java.hw05.observer1;

/**
 * Observer for {@link IntegerStorage}
 * @author Pavao Jerebić
 */
public interface IntegerStorageObserver {
    /**
     * To be called when value is changed
     * @param integerStorage integer storage
     */
    public void valueChanged(IntegerStorage integerStorage);
}
