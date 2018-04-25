package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.*;

/**
 * Oval object
 *
 * @author Pavao Jerebić
 */
public class OvalObject extends GeometricalObject {
    /**
     * center x
     */
    private int x;
    /**
     * center y
     */
    private int y;
    /**
     * radius
     */
    private int r;

    /**
     * Sets all field values
     *
     * @param x center x
     * @param y center y
     * @param r radius
     */
    public OvalObject(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public Rectangle boundingBox() {
        return new Rectangle(x - r, y - r, 2 * r, 2 * r);
    }

    @Override
    public String toString() {
        Color c = getForeground();
        return String.format("CIRCLE %d %d %d %d %d %d", x, y, r, c.getRed(), c.getGreen(), c.getBlue());
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(getLocation().x + x - r, getLocation().y + y - r, 2 * r, 2 * r);
    }

    /**
     * Getter for center x
     *
     * @return center x
     */
    public int getCenterX() {
        return x;
    }

    /**
     * Getter for center y
     *
     * @return center y
     */
    public int getCenterY() {
        return y;
    }

    /**
     * Getter for radius
     *
     * @return radius
     */
    public int getRadius() {
        return r;
    }
}
