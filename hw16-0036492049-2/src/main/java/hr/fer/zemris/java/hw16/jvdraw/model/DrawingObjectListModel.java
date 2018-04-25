package hr.fer.zemris.java.hw16.jvdraw.model;

import javax.swing.*;

/**
 * List model that serves as object adapter for {@link DrawingModel}
 *
 * @author Pavao Jerebić
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> {

    /**
     * Instance of a drawing model
     */
    private DrawingModel drawingModel;

    /**
     * Constructor that sets drawing model
     *
     * @param drawingModel drawing model
     */
    public DrawingObjectListModel(DrawingModel drawingModel) {
        this.drawingModel = drawingModel;
    }

    @Override
    public int getSize() {
        return drawingModel.getSize();
    }

    @Override
    public GeometricalObject getElementAt(int i) {
        return drawingModel.getObject(i);
    }

    /**
     * Adds a new geometrical object
     *
     * @param object object
     * @see DrawingModel#add(GeometricalObject)
     */
    public void add(GeometricalObject object) {
        int position = getSize();
        drawingModel.add(object);
        fireIntervalAdded(this, position, position);
    }

    /**
     * Clears all geometrical objects
     *
     * @see DrawingModel#clear()
     */
    public void clear() {
        drawingModel.clear();
        fireIntervalRemoved(this, 0, getSize());
    }
}
