package hr.fer.zemris.java.hw16.jvdraw.model;

import javax.swing.*;
import java.awt.*;

/**
 * Geometrical object
 *
 * @author Pavao Jerebić
 */
public abstract class GeometricalObject extends JComponent {
    /**
     * Bounding box of this geometrical object<br/>
     * x and y values are top left corner
     *
     * @return bounding box
     */
    public abstract Rectangle boundingBox();
}
