package hr.fer.zemris.java.hw16.jvdraw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link DrawingModel}
 *
 * @author Pavao Jerebić
 */
public class DrawingModelImpl implements DrawingModel {
    /**
     * Collection of geometrical objects
     */
    private List<GeometricalObject> objects;
    /**
     * Collection of listeners
     */
    private List<DrawingModelListener> listeners;

    /**
     * Constructor that initializes collections
     */
    public DrawingModelImpl() {
        objects = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public GeometricalObject getObject(int index) {
        return (index < getSize() && index >= 0) ? objects.get(index) : null;
    }

    @Override
    public void add(GeometricalObject object) {
        objects.add(object);
        listeners.forEach((l) -> l.objectsAdded(DrawingModelImpl.this, getSize(), getSize()));
    }

    @Override
    public void addDrawingModelListener(DrawingModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeDrawingModelListener(DrawingModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void clear() {
        objects.clear();
        listeners.forEach((l) -> l.objectsRemoved(DrawingModelImpl.this, 0, getSize()));
    }
}
