package hr.fer.zemris.java.gui;

import hr.fer.zemris.java.console.parser.Parser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Pavao Jerebić
 */
public class Main extends JFrame {
    private JTextArea textArea;
    private JTable table;
    private DefaultTableModel tableModel;

    public Main() {
        setLocation(20, 20);
        setSize(700, 500);
        setTitle("Mijenjalo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem load = new JMenuItem(new AbstractAction("Učitaj") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Choose file");

                if (fc.showOpenDialog(Main.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                Path selectedDir = fc.getSelectedFile().toPath();

                if (!Files.isReadable(selectedDir)) {
                    JOptionPane.showMessageDialog(
                            Main.this,
                            selectedDir + " is not readable.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                try {
                    String text = new String(Files.readAllBytes(selectedDir));
                    String[] textInput = text.split(System.lineSeparator());
                    java.util.List<String> textLine = new ArrayList<>();
                    for (String line : textInput) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        textLine.add(line);
                    }
                    StringBuilder sb = new StringBuilder();
                    textLine.forEach(s -> sb.append(s).append(System.lineSeparator()));
                    text = sb.toString();
                    textArea.setText(text);
                    Parser parser = new Parser(text.toCharArray());
                    String input = text.substring(parser.getEndPoint());
                    String[] inputArray = input.split(System.lineSeparator());
                    java.util.List<String> lines = new ArrayList<>();
                    for (String line : inputArray) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        lines.add(line);
                    }
                    parser.calculate(lines.get(0));
                    java.util.List<String> variables = new ArrayList<>();
                    parser.getLabels().forEach(variable -> variables.add(variable.getName()));
                    parser.getExpandees().forEach(variable -> variables.add(variable.getName()));
                    String[][] data = new String[lines.size()][variables.size()];
                    for (int i = 0; i < lines.size(); i++) {
                        String output = parser.calculate(lines.get(i));
                        String[] spli = output.split("[;]");
                        for (int j = 0; j < variables.size(); j++) {
                            data[i][j] = spli[j];
                        }
                    }
                    tableModel.setDataVector(data, variables.toArray());
                    System.out.println(Arrays.deepToString(data));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Main.this, "Greška pri obradi datoteke.");
                }

            }
        });
        JMenuItem exit = new JMenuItem(new AbstractAction("Kraj") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.this.dispose();
            }
        });
        file.add(load);
        file.add(exit);
        menuBar.add(file);

        cp.add(menuBar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        textArea = new JTextArea();
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        splitPane.add(new JScrollPane(textArea));
        splitPane.add(new JScrollPane(table));

        cp.add(splitPane, BorderLayout.CENTER);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
