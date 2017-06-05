package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Layout manager for {@link hr.fer.zemris.java.gui.calc.Calculator}
 *
 * @author Pavao Jerebić
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * Gap between components
     */
    private int gap;
    /**
     * Min height possible that suits all components
     */
    private int minWidth = 0;
    /**
     * Min height possible that suits all components
     */
    private int minHeight = 0;
    /**
     * Preferred width
     */
    private int preferredWidth = 0;
    /**
     * Preferred height
     */
    private int preferredHeight = 0;

    /**
     * Position to component map
     */
    private Map<RCPosition, Component> position_to_component;
    /**
     * Component to position map
     */
    private Map<Component, RCPosition> component_to_position;
    /**
     * Number of rows
     */
    private static final int MAX_ROWS = 5;
    /**
     * Number of Columns
     */
    private static final int MAX_COLUMNS = 7;

    /**
     * Constructor that sets gap
     *
     * @param gap gap
     */
    public CalcLayout(int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException("Gap must not be < 0");
        }
        this.gap = gap;
        position_to_component = new HashMap<>();
        component_to_position = new HashMap<>();
    }

    /**
     * Default constructor that sets gap to 0
     */
    public CalcLayout() {
        this(0);
    }


    /**
     * Helping method that sets {@link #minWidth} and {@link #minHeight}, {@link #preferredWidth} and {@link #preferredHeight}
     *
     * @param parent parent container
     */
    private void setSizes(Container parent) {

        int nComps = parent.getComponentCount();
        Dimension dimension;

        //Reset preferred/minimum width and height.
        preferredWidth = 0;
        preferredHeight = 0;
        minWidth = 0;
        minHeight = 0;

        for (int i = 0; i < nComps; i++) {

            Component component = parent.getComponent(i);


            if (component.isVisible()) {

                dimension = new Dimension(component.getPreferredSize());
                RCPosition position = component_to_position.get(component);
                if (position.getRow() == 1 && position.getColumn() == 1) {
                    dimension.width = (dimension.width - 3 * gap) / 5;
                }
                minHeight = Math.max(minHeight, dimension.height);
                minWidth = Math.max(minWidth, dimension.width);
            }
        }

        preferredWidth = (MAX_COLUMNS - 1) * gap + MAX_COLUMNS * minWidth;
        preferredHeight = (MAX_ROWS - 1) * gap + MAX_ROWS * minHeight;

    }

    @Override
    public void addLayoutComponent(Component component, Object o) {
        if (component == null) {
            throw new IllegalArgumentException("Component must not be null");
        }

        RCPosition position;

        if (o instanceof String) {
            String constraint = (String) o;
            position = RCPosition.parse(constraint);
        } else if (o instanceof RCPosition) {
            position = (RCPosition) o;
        } else {
            throw new IllegalArgumentException("Constraint must be an instance of java.lang.String or hr.fer.zemris.java.gui.layouts.RCPosition");
        }

        if (position.getRow() < 1 || position.getRow() > MAX_ROWS
                || position.getColumn() < 1 || position.getColumn() > MAX_COLUMNS) {
            throw new IllegalArgumentException("Position must be two numbers greater than zero and row number must be less than or equal to "
                    + MAX_ROWS + " and column number must be less than or equal to " + MAX_COLUMNS);
        }

        if (position.getRow() == 1) {
            if (position.getColumn() < 6 && position.getColumn() > 1) {
                throw new IllegalArgumentException("First row has only columns 1, 6 and 7");
            }
        }

        if (position_to_component.containsKey(position)) {
            throw new IllegalArgumentException("Component at this position already exists");
        }

        position_to_component.put(position, component);
        component_to_position.put(component, position);

    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container container) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container container) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container container) {
    }

    @Override
    public void addLayoutComponent(String s, Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Component must not be null");
        }

        RCPosition position = component_to_position.get(component);

        if (position != null) {
            position_to_component.remove(position);
            component_to_position.remove(component);
        }

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);

        setSizes(parent);

        Insets insets = parent.getInsets();
        dim.width = preferredWidth
                + insets.left + insets.right;
        dim.height = preferredHeight
                + insets.top + insets.bottom;


        return dim;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth()
                - (insets.left + insets.right);
        int maxHeight = parent.getHeight()
                - (insets.top + insets.bottom);
        int nComps = parent.getComponentCount();
        double xRatio = 1, yRatio = 1;


        setSizes(parent);

        if (maxWidth > preferredWidth) {
            xRatio = maxWidth / 1.0 / preferredWidth;
        }

        if (maxHeight > preferredHeight) {
            yRatio = maxHeight / 1.0 / preferredHeight;
        }


        for (int i = 0; i < nComps; i++) {
            Component component = parent.getComponent(i);

            if (component.isVisible()) {

                RCPosition position = component_to_position.get(component);

                Dimension dim = new Dimension(minWidth, minHeight);

                if (position.getRow() == 1 && position.getColumn() == 1) {
                    dim.width *= MAX_ROWS;
                    dim.width += (MAX_ROWS - 1) * gap;
                }


                component.setBounds(
                        (int) Math.round(insets.left + (position.getColumn() - 1) * (xRatio) * (gap + minWidth)),
                        (int) Math.round(insets.top + (position.getRow() - 1) * (yRatio) * (gap + minHeight)),
                        (int) Math.round(dim.width * xRatio),
                        (int) Math.round(dim.height * yRatio)
                );

            }
        }
    }
}
