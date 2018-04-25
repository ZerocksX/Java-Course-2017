package hr.fer.zemris.java.hw16.jvdraw.colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Color area that displays currently selected color from {@link IColorProvider}<br/>
 * When clicked displays a {@link JColorChooser}
 *
 * @author Pavao Jerebić
 */
public class JColorArea extends JComponent implements IColorProvider {
    /**
     * current color
     */
    private Color selectedColor;
    /**
     * listeners
     */
    private List<ColorChangeListener> listeners;
    /**
     * Name of this color
     */
    private String name;

    /**
     * Constructor that sets current color
     *
     * @param selectedColor current color
     * @param name          name of the color area
     */
    public JColorArea(Color selectedColor, String name) {
        this.selectedColor = selectedColor;
        this.listeners = new ArrayList<>();
        this.name = name;
        addColorChangeListener((source, oldColor, newColor) -> {
            JColorArea.this.selectedColor = newColor;
            JColorArea.this.repaint();
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Color color = JColorChooser.showDialog(getRootPane(), "Choose " + name + " color", getCurrentColor());
                if (color != null) {
                    changeColor(color);
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Color previousColor = g.getColor();
        g.setColor(selectedColor);
        Insets ins = getInsets();
        g.fillRect(ins.left, ins.top, getHeight() - ins.bottom, getWidth() - ins.right);
        g.setColor(previousColor);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(15, 15);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(15, 15);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(15, 15);
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }

    @Override
    public void addColorChangeListener(ColorChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removeColorChangeListener(ColorChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public void changeColor(Color newColor) {
        selectedColor = newColor;
        List<ColorChangeListener> finalListeners = new ArrayList<>(listeners);
        for (ColorChangeListener l : finalListeners) {
            l.newColorSelected(this, selectedColor, newColor);
        }
    }

    /**
     * Getter for name
     *
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
}
