package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.*;

/**
 * Provides color to subjects<br/>
 * Informs subjects when there is a color change
 *
 * @author Pavao Jerebić
 */
public interface IColorProvider {

    /**
     * Returns current color
     *
     * @return current color
     */
    Color getCurrentColor();

    /**
     * Adds a new color change listener
     *
     * @param l listener
     */
    void addColorChangeListener(ColorChangeListener l);

    /**
     * Removes a color change listener
     *
     * @param l listener
     */
    void removeColorChangeListener(ColorChangeListener l);

    /**
     * Changes color and fires {@link ColorChangeListener#newColorSelected(IColorProvider, Color, Color)}
     *
     * @param newColor new color
     */
    void changeColor(Color newColor);
}
