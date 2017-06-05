package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Program that draws a bar chart from given xyValues, name of x and y axis, and minimum, maximum and step value on y axis
 *
 * @author Pavao Jerebić
 */
public class BarChart extends JFrame {

    /**
     * xy values
     */
    private List<XYValue> xyValues;
    /**
     * Name of x axis
     */
    private String xName;
    /**
     * Name of y axis
     */
    private String yName;
    /**
     * minimum y value
     */
    private int minY;
    /**
     * maximum y value
     */
    private int maxY;
    /**
     * step on y axis
     */
    private int step;
    /**
     * maximum x value
     */
    private int maxX;

    /**
     * Basic constructor that takes given data and draws a {@link BarChartComponent}
     *
     * @param xyValues xy values
     * @param xName    x axis
     * @param yName    y axis
     * @param minY     minimum y value
     * @param maxY     maximum y value
     * @param step     y axis step
     */
    public BarChart(List<XYValue> xyValues, String xName, String yName, int minY, int maxY, int step) {
        this.xyValues = xyValues;
        this.xName = xName;
        this.yName = yName;
        this.minY = minY;
        this.maxY = maxY;
        this.step = step;

        maxX = 0;
        xyValues.forEach(xyValue -> maxX = Math.max(maxX, xyValue.getX()));

        setLocation(20, 20);
        setSize(500, 200);
        setTitle("Bar chart");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGui();
    }

    /**
     * Getter for xy values
     *
     * @return xy values
     */
    public List<XYValue> getXyValues() {
        return xyValues;
    }

    /**
     * Getter for x name
     *
     * @return x name
     */
    public String getxName() {
        return xName;
    }

    /**
     * Getter for y name
     *
     * @return y name
     */
    public String getyName() {
        return yName;
    }

    /**
     * Getter for min y
     *
     * @return min y
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Getter for max y
     *
     * @return max y
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Getter for max x
     *
     * @return max x
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Getter for step
     *
     * @return step
     */
    public int getStep() {
        return step;
    }

    /**
     * Helping method that initializes gui
     */
    private void initGui() {
        Container cp = getContentPane();
        BarChartComponent component = new BarChartComponent(this);
        component.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        cp.add(component);
    }

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        BarChart model = new BarChart(
                Arrays.asList(
                        new XYValue(1, 8), new XYValue(2, 20), new XYValue(3, 22),
                        new XYValue(4, 10), new XYValue(5, 4)
                ),
                "Number of people in the car",
                "Frequency",
                0, // y-os kreće od 0
                22, // y-os ide do 22
                2
        );
        SwingUtilities.invokeLater(() ->
                model.setVisible(true)
        );
    }
}
