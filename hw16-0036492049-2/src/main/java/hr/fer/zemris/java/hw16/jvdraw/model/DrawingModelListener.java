package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Listeners that checks if there was a change in drawn geometrical objects
 *
 * @author Pavao Jerebić
 */
public interface DrawingModelListener {
    /**
     * Fired when objects are added
     *
     * @param source source
     * @param index0 starting index
     * @param index1 ending index
     */
    void objectsAdded(DrawingModel source, int index0, int index1);

    /**
     * Fired when objects are removed
     *
     * @param source source
     * @param index0 starting index
     * @param index1 ending index
     */
    void objectsRemoved(DrawingModel source, int index0, int index1);

    /**
     * Fired when objects are changed
     *
     * @param source source
     * @param index0 starting index
     * @param index1 ending index
     */
    void objectsChanged(DrawingModel source, int index0, int index1);
}
