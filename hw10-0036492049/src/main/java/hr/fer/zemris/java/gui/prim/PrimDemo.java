package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * Program that display 2 identical lists, and by clicking button 'sljedeći' adds next number to both lists using {@link PrimListModel}
 *
 * @author Pavao Jerebić
 */
public class PrimDemo extends JFrame {

    /**
     * Basic Constructor
     */
    public PrimDemo() {
        setLocation(20, 20);
        setSize(500, 200);
        setTitle("Bar chart");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGui();
    }

    /**
     * Helping method that initializes gui
     */
    private void initGui() {
        Container cp = getContentPane();
        PrimListModel model = new PrimListModel();
        JList<String> jList1 = new JList<>(model);
        JList<String> jList2 = new JList<>(model);
        cp.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(new JScrollPane(jList1));
        panel.add(new JScrollPane(jList2));
        cp.add(panel, BorderLayout.CENTER);
        JButton button = new JButton("sljedeći");
        button.addActionListener(actionEvent -> model.next());
        cp.add(button, BorderLayout.PAGE_END);
    }

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new PrimDemo().setVisible(true)
        );
    }
}
