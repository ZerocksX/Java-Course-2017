package hr.fer.zemris.java.hw16.jvdraw.colors;

import java.awt.*;

/**
 * Listens to {@link IColorProvider#changeColor(Color)}
 *
 * @author Pavao Jerebić
 */
public interface ColorChangeListener {
    /**
     * Fired when there is a new color
     *
     * @param source   source
     * @param oldColor old color
     * @param newColor new color
     */
    void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
