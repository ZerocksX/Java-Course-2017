package hr.fer.zemris.java.gui.charts;

/**
 * Class that stores x and y value
 *
 * @author Pavao Jerebić
 */
public class XYValue {
    /**
     * X
     */
    private int x;
    /**
     * Y
     */
    private int y;

    /**
     * Constructor that sets x and y value
     *
     * @param x x
     * @param y y
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     *
     * @return y
     */
    public int getY() {
        return y;
    }
}
