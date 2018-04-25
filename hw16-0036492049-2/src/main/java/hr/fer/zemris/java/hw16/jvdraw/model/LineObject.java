package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.*;

/**
 * Line object
 *
 * @author Pavao Jerebić
 */
public class LineObject extends GeometricalObject {
    /**
     * x1
     */
    private int x1;
    /**
     * y1
     */
    private int y1;
    /**
     * x2
     */
    private int x2;
    /**
     * y2
     */
    private int y2;

    /**
     * Constructor that sets starting and ending point
     *
     * @param x1 starting x
     * @param y1 starting y
     * @param x2 ending x
     * @param y2 ending y
     */
    public LineObject(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    @Override
    public Rectangle boundingBox() {
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getForeground());
        g.drawLine(getLocation().x + x1, getLocation().y + y1, getLocation().x + x2, getLocation().y + y2);
    }

    @Override
    public String toString() {
        Color c = getForeground();
        return String.format("LINE %d %d %d %d %d %d %d", x1, y1, x2, y2, c.getRed(), c.getGreen(), c.getBlue());
    }
}
