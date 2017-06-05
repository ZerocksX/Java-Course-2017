package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.awt.*;

/**
 * Result label for {@link Calculator}
 *
 * @author Pavao Jerebić
 */
public class CalculatorResultLabel extends JLabel {
    /**
     * Gap between text and border
     */
    private static final int GAP = 5;

    /**
     * Default size of the component
     */
    private static final int DEFAULT_SIZE = 50;

    /**
     * Basic constructor that sets text
     *
     * @param s text
     * @see JLabel#JLabel(String)
     */
    public CalculatorResultLabel(String s) {
        super(s);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Insets ins = getInsets();
        Dimension dim = getSize();

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(
                    ins.left,
                    ins.top,
                    dim.width - (ins.right + ins.left),
                    dim.height - (ins.top + ins.bottom)
            );
        }

        g.setColor(getForeground());
        g.setFont(getMyFont());

        String str = getText();

        FontMetrics fm = g.getFontMetrics();
        int v = fm.stringWidth(str);

        g.drawString(
                str,
                dim.width - ins.left - v,
                (dim.height - ins.top - ins.bottom - fm.getAscent()) / 2 + ins.top + fm.getAscent()
        );
    }

    @Override
    public Dimension getPreferredSize() {

        Insets ins = getInsets();
        String str = getText();

        FontMetrics fm = getFontMetrics(getMyFont());
        return new Dimension(Math.max(DEFAULT_SIZE, ins.left + ins.right + fm.stringWidth(str) + 2 * GAP), Math.max(DEFAULT_SIZE, 2 * GAP + fm.getHeight()));
    }

    /**
     * Getter for font( bold, 20p)
     *
     * @return font
     */
    private Font getMyFont() {
        return getFont().deriveFont(Font.BOLD, 20f);
    }
}
