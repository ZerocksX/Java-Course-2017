package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.*;

/**
 * Filled oval component
 *
 * @author Pavao Jerebić
 */
public class FilledOvalObject extends OvalObject {
    /**
     * Basic constructor
     *
     * @param x center x
     * @param y center y
     * @param r radius
     */
    public FilledOvalObject(int x, int y, int r) {
        super(x, y, r);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillOval(getLocation().x + getCenterX() - getRadius(), getLocation().y + getCenterY() - getRadius(), 2 * getRadius(), 2 * getRadius());
        super.paint(g);
    }

    @Override
    public String toString() {
        Color fc = getForeground();
        Color bc = getBackground();
        return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", getCenterX(), getCenterY(), getRadius(), fc.getRed(), fc.getGreen(), fc.getBlue(), bc.getRed(), bc.getGreen(), bc.getBlue());
    }
}
