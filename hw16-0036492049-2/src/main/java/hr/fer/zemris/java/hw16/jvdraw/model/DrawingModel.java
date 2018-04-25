package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Drawing model that contains {@link GeometricalObject}s and listeners that check if there was a change<br/>
 * in the collection of {@link GeometricalObject}s
 *
 * @author Pavao Jerebić
 */
public interface DrawingModel {
    /**
     * Number of objects
     *
     * @return size
     */
    int getSize();

    /**
     * Getter for object at the given index
     *
     * @param index index
     * @return object at index, or null if it does not exist
     */
    GeometricalObject getObject(int index);

    /**
     * Adds a new {@link GeometricalObject} and fires all listeners
     *
     * @param object object
     */
    void add(GeometricalObject object);

    /**
     * Adds a listener
     *
     * @param l listener
     */
    void addDrawingModelListener(DrawingModelListener l);

    /**
     * Removes a listener
     *
     * @param l listener
     */
    void removeDrawingModelListener(DrawingModelListener l);

    /**
     * Removes all objects and informs listeners
     */
    void clear();
}
