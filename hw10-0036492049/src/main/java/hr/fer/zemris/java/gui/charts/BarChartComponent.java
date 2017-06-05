package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Bar chart component that draws whole chart
 *
 * @author Pavao Jerebić
 */
public class BarChartComponent extends JComponent {

    /**
     * Parent
     */
    private BarChart barChart;

    /**
     * Gap between elements on Y axis
     */
    private static final int Y_GAP = 5;
    /**
     * Gap between elements on X axis
     */
    private static final int X_GAP = 5;
    /**
     * Length of grid markings
     */
    private static final int GRID_MARKING_LENGTH = 7;
    /**
     * Size of the shadow behind rectangles
     */
    private static final int SHADOW_SIZE = 5;

    /**
     * Basic constructor that sets bar chart parent
     *
     * @param barChart bar chart
     */
    public BarChartComponent(BarChart barChart) {
        this.barChart = barChart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Insets ins = getInsets();
        Dimension dim = getSize();
        int nRows = (barChart.getMaxY() - barChart.getMinY()) / barChart.getStep();
        int nColumns = barChart.getMaxX();
        int temp[] = new int[]{0};
        barChart.getXyValues().forEach(xyValue ->
                temp[0] = Math.max(temp[0], getMyFontMetrics().stringWidth(Integer.toString(xyValue.getY())))
        );
        int rowNumberWidth = temp[0];
        int columnNumberHeight = getMyFontMetrics().getAscent();
        int xLabelHeight = getMyFontMetrics().getAscent();
        int ylabelWidth = getMyFontMetrics().getAscent();

        drawGrid(
                g,
                ins.left + Y_GAP + rowNumberWidth + Y_GAP + ylabelWidth,
                dim.height - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right - Y_GAP - rowNumberWidth - Y_GAP - ylabelWidth,
                dim.height - ins.top - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                nRows,
                nColumns
        );

        drawColumnNumbers(
                g,
                ins.left + Y_GAP + rowNumberWidth + Y_GAP + ylabelWidth,
                dim.height - ins.bottom - xLabelHeight - X_GAP - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right - Y_GAP - rowNumberWidth - Y_GAP - ylabelWidth,
                dim.height - ins.top - ins.bottom - getMyFontMetrics().getAscent(),
                nColumns
        );

        drawRowNumbers(
                g,
                ins.left + Y_GAP + ylabelWidth,
                dim.height - ins.bottom - X_GAP - columnNumberHeight - xLabelHeight - X_GAP - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right - Y_GAP - ylabelWidth,
                dim.height - ins.top - ins.bottom - X_GAP - columnNumberHeight - xLabelHeight - X_GAP - getMyFontMetrics().getAscent(),
                nRows,
                barChart.getStep()
        );

        drawXLabel(
                g,
                ins.left + rowNumberWidth + Y_GAP + ylabelWidth + Y_GAP,
                dim.height - ins.bottom - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right - Y_GAP - rowNumberWidth - Y_GAP - ylabelWidth,
                dim.height - ins.top - ins.bottom - X_GAP - columnNumberHeight - getMyFontMetrics().getAscent(),
                barChart.getxName()
        );

        drawYLabel(
                g,
                ins.left,
                dim.height - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right,
                dim.height - ins.top - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                barChart.getyName()
        );

        drawRectangles(
                g,
                ins.left + Y_GAP + rowNumberWidth + Y_GAP + ylabelWidth,
                dim.height - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                dim.width - ins.left - ins.right - Y_GAP - rowNumberWidth - Y_GAP - ylabelWidth,
                dim.height - ins.top - ins.bottom - X_GAP - columnNumberHeight - X_GAP - xLabelHeight - getMyFontMetrics().getAscent(),
                barChart.getXyValues(),
                nRows,
                nColumns,
                barChart.getStep()
        );
    }

    /**
     * Helping method that draws rectangles representing xyValues
     *
     * @param g        graphics
     * @param x        lower left corner x value
     * @param y        lower left corner y value
     * @param width    width of component
     * @param height   height
     * @param xyValues xy values
     * @param nRows    number of rows
     * @param nColumns number of columns
     * @param step     step on y axis
     */
    private void drawRectangles(Graphics g, int x, int y, int width, int height, List<XYValue> xyValues, int nRows, int nColumns, int step) {
        int xBaseLength = width - 2 * GRID_MARKING_LENGTH;
        int xDist = xBaseLength / nColumns;
        int yBaseLength = height - 2 * GRID_MARKING_LENGTH;
        int yDist = yBaseLength / nRows;
        for (XYValue value : xyValues) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(
                    x + GRID_MARKING_LENGTH + (value.getX() - 1) * xDist + SHADOW_SIZE,
                    y - GRID_MARKING_LENGTH - yDist * value.getY() / step + SHADOW_SIZE,
                    xDist,
                    yDist * value.getY() / step - SHADOW_SIZE
            );
            g.setColor(Color.RED);
            g.fillRect(
                    x + GRID_MARKING_LENGTH + (value.getX() - 1) * xDist,
                    y - GRID_MARKING_LENGTH - yDist * value.getY() / step,
                    xDist,
                    yDist * value.getY() / step
            );
            g.setColor(Color.WHITE);
            g.drawRect(
                    x + GRID_MARKING_LENGTH + (value.getX() - 1) * xDist,
                    y - GRID_MARKING_LENGTH - yDist * value.getY() / step,
                    xDist,
                    yDist * value.getY() / step
            );
        }
    }

    /**
     * Helping method that draws y axis name
     *
     * @param g         graphics
     * @param x         lower left corner x value
     * @param y         lower left corner y value
     * @param width     width of component
     * @param height    height
     * @param labelName name of the label
     */
    private void drawYLabel(Graphics g, int x, int y, int width, int height, String labelName) {
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform defaultAt = g2d.getTransform();
        g2d.setTransform(at);

        g2d.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2d.setColor(Color.BLACK);
        FontMetrics fm = getMyFontMetrics();
        int v = fm.stringWidth(labelName);
        g2d.drawString(
                labelName,
                -(y - GRID_MARKING_LENGTH - (height - v) / 2),
                x + fm.getAscent()
        );

        g2d.setTransform(defaultAt);
    }

    /**
     * Helping method that draws x axis name
     *
     * @param g         graphics
     * @param x         lower left corner x value
     * @param y         lower left corner y value
     * @param width     width of component
     * @param height    height
     * @param labelName name of the label
     */
    private void drawXLabel(Graphics g, int x, int y, int width, int height, String labelName) {
        g.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g.setColor(Color.BLACK);
        FontMetrics fm = getMyFontMetrics();
        int v = fm.stringWidth(labelName);
        g.drawString(
                labelName,
                x + GRID_MARKING_LENGTH + (width - v) / 2,
                y
        );
    }

    /**
     * Returns {@link FontMetrics} used to draw {@link String}s
     *
     * @return custom font metrics
     */
    private FontMetrics getMyFontMetrics() {
        return getFontMetrics(getFont().deriveFont(Font.BOLD, 12f));
    }

    /**
     * Helping method that draws the grid
     *
     * @param g        graphics
     * @param x        lower left corner x value
     * @param y        lower left corner y value
     * @param width    width of component
     * @param height   height
     * @param nRows    number of rows
     * @param nColumns number of columns
     */
    private void drawGrid(Graphics g, int x, int y, int width, int height, int nRows, int nColumns) {
        g.setColor(Color.GRAY);
        g.drawLine(x + GRID_MARKING_LENGTH, y, x + GRID_MARKING_LENGTH, y - height);
        g.drawLine(x, y - GRID_MARKING_LENGTH, width + x, y - GRID_MARKING_LENGTH);
        int xBaseLength = width - 2 * GRID_MARKING_LENGTH;
        int xDist = xBaseLength / nColumns;
        for (int i = 1; i <= nColumns; i++) {
            g.setColor(Color.GRAY);
            g.drawLine(x + GRID_MARKING_LENGTH + i * xDist, y, x + GRID_MARKING_LENGTH + i * xDist, y - GRID_MARKING_LENGTH + 1);
            g.setColor(Color.ORANGE);
            g.drawLine(x + GRID_MARKING_LENGTH + i * xDist, y - GRID_MARKING_LENGTH, x + GRID_MARKING_LENGTH + i * xDist, y - height);
        }
        int yBaseLength = height - 2 * GRID_MARKING_LENGTH;
        int yDist = yBaseLength / nRows;
        for (int i = 1; i <= nRows; i++) {
            g.setColor(Color.GRAY);
            g.drawLine(x, y - i * yDist - GRID_MARKING_LENGTH, x + GRID_MARKING_LENGTH, y - i * yDist - GRID_MARKING_LENGTH);
            g.setColor(Color.ORANGE);
            g.drawLine(x + GRID_MARKING_LENGTH, y - GRID_MARKING_LENGTH - i * yDist, width + x, y - GRID_MARKING_LENGTH - i * yDist);
        }
    }

    /**
     * Helping method that draws rectangles representing xyValues
     *
     * @param g        graphics
     * @param x        lower left corner x value
     * @param y        lower left corner y value
     * @param width    width of component
     * @param height   height
     * @param nColumns number of columns
     */
    private void drawColumnNumbers(Graphics g, int x, int y, int width, int height, int nColumns) {


        int xBaseLength = width - 2 * GRID_MARKING_LENGTH;
        int xDist = xBaseLength / nColumns;

        for (int i = 1; i <= nColumns; i++) {
            g.setFont(getFont().deriveFont(Font.BOLD, 12f));
            g.setColor(Color.BLACK);
            FontMetrics fm = getMyFontMetrics();
            int v = fm.stringWidth(Integer.toString(i));
            g.drawString(
                    Integer.toString(i),
                    x + GRID_MARKING_LENGTH + xDist * (i - 1) + (xDist + v) / 2,
                    y
            );
        }

    }

    /**
     * Helping method that draws rectangles representing xyValues
     *
     * @param g      graphics
     * @param x      lower left corner x value
     * @param y      lower left corner y value
     * @param width  width of component
     * @param height height
     * @param nRows  number of rows
     * @param step   step on y axis
     */
    private void drawRowNumbers(Graphics g, int x, int y, int width, int height, int nRows, int step) {
        int yBaseLength = height - 2 * GRID_MARKING_LENGTH;
        int yDist = yBaseLength / nRows;
        int maxV = getMyFontMetrics().stringWidth(Integer.toString(barChart.getMaxY()));

        for (int i = 0; i <= nRows; i++) {
            g.setFont(getFont().deriveFont(Font.BOLD, 12f));
            g.setColor(Color.BLACK);
            FontMetrics fm = getMyFontMetrics();
            int v = fm.stringWidth(Integer.toString(i * step));
            g.drawString(
                    Integer.toString(i * step),
                    x + maxV - v,
                    y - GRID_MARKING_LENGTH + fm.getAscent() / 2 - yDist * i
            );
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                2 * (getMyFontMetrics().stringWidth(barChart.getxName()) + Y_GAP + getMyFontMetrics().getAscent() + Y_GAP + getMyFontMetrics().stringWidth(Integer.toString(barChart.getMaxY())) + GRID_MARKING_LENGTH),
                2 * (getMyFontMetrics().stringWidth(barChart.getyName()) + X_GAP + getMyFontMetrics().getAscent() + X_GAP + getMyFontMetrics().getAscent() + GRID_MARKING_LENGTH)
        );
    }
}
