package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.colors.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

/**
 * Canvas that contains {@link GeometricalObject}s contained in {@link DrawingObjectListModel}<br/>
 * Objects are line, circle and filled circle<br/>
 * First point is decided when user first clicks on the canvas<br/>
 * When user moves the mouse a preview of the object will appear<br/>
 * When user clicks again object will be added to {@link DrawingObjectListModel} and drawn on the canvas<br/>
 * Right click cancels current operation
 *
 * @author Pavao Jerebić
 */
public class JDrawingCanvas extends JComponent {

    /**
     * List model
     */
    private DrawingObjectListModel listModel;
    /**
     * First click
     */
    private Point previousClick;
    /**
     * Size of the indicator of the first click
     */
    private static final int INDICATOR_SIZE = 5;
    /**
     * List of buttons that define what object is being drawn(names must be "Line", "Circle", "Filled circle")
     */
    private List<JToggleButton> buttons;
    /**
     * Color provider for foreground color
     */
    private IColorProvider fColor;
    /**
     * Color provider for background color
     */
    private IColorProvider bColor;


    /**
     * Creates the canvas and initializes all the properties
     *
     * @param listModel list model
     * @param buttons   toggle buttons that define what object is being drawn(names must be "Line", "Circle", "Filled circle")
     * @param fColor    provider for foreground color
     * @param bColor    provider for background color
     */
    public JDrawingCanvas(DrawingObjectListModel listModel, List<JToggleButton> buttons, IColorProvider fColor, IColorProvider bColor) {
        this.listModel = listModel;
        this.buttons = buttons;
        this.fColor = fColor;
        this.bColor = bColor;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    previousClick = null;
                    repaint();
                    return;
                }
                if (previousClick == null) {
                    previousClick = mouseEvent.getPoint();
                    getGraphics().drawRect(previousClick.x - INDICATOR_SIZE, previousClick.y - INDICATOR_SIZE, INDICATOR_SIZE, INDICATOR_SIZE);
                } else {
                    GeometricalObject object = createObject(mouseEvent);
                    if (object == null) {
                        return;
                    }
                    previousClick = null;
                    listModel.add(object);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (previousClick != null) {
                    GeometricalObject object = createObject(mouseEvent);
                    if (object == null) {
                        return;
                    }
                    paint(getGraphics());
                    object.paint(getGraphics());
                }
            }
        });
    }

    /**
     * Helping method that creates an object if possible, otherwise returns null
     *
     * @param mouseEvent mouse event
     * @return new geometrical object
     */
    private GeometricalObject createObject(MouseEvent mouseEvent) {
        String pressed = "";
        for (JToggleButton btn : buttons) {
            if (btn.isSelected()) {
                pressed = btn.getText();
            }
        }

        int x1 = previousClick.x;
        int y1 = previousClick.y;
        int x2 = mouseEvent.getX();
        int y2 = mouseEvent.getY();
        GeometricalObject object = null;

        switch (pressed) {
            case "Line":
                object = new LineObject(x1, y1, x2, y2);
                break;
            case "Circle":
                object = new OvalObject(x1, y1, (int) Math.round(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))));
                break;
            case "Filled circle":
                object = new FilledOvalObject(x1, y1, (int) Math.round(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))));
                break;
            default:
                break;

        }

        if (object == null) {
            return null;
        }

        object.setForeground(fColor.getCurrentColor());
        object.setBackground(bColor.getCurrentColor());

        return object;
    }

    /**
     * Getter for drawing model listener that repaints this class when there is any change
     *
     * @return a listener
     */
    public DrawingModelListener getDmListener() {
        return new DrawingModelListener() {
            @Override
            public void objectsAdded(DrawingModel source, int index0, int index1) {
                JDrawingCanvas.this.repaint();
            }

            @Override
            public void objectsRemoved(DrawingModel source, int index0, int index1) {
                JDrawingCanvas.this.repaint();

            }

            @Override
            public void objectsChanged(DrawingModel source, int index0, int index1) {
                JDrawingCanvas.this.repaint();

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0, n = listModel.getSize(); i < n; i++) {
            GeometricalObject object = listModel.getElementAt(i);
            object.paint(g);
        }
    }

}
