package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Program that reads input from a file with format
 * X axis label<br/>
 * Y axis label<br>
 * X and Y values separated with ',', next value is separated with ' '<br/>
 * Lowest number on Y axis<br/>
 * Highest number on Y axis<br/>
 * Y axis step<br/>
 * An example(In 'src/main/resources/data.txt'):<br/>
 * Number of people in the car<br/>
 * Frequency<br/>
 * 1,8 2,20 3,22 4,10 5,4<br/>
 * 0<br/>
 * 22<br/>
 * 2<br/>
 * <p>
 * Path to the file is given in program arguments<br/>
 * After reading the file program produces a {@link BarChart} containing the information stored in the file
 *
 * @author Pavao Jerebić
 */
public class BarChartDemo extends JFrame {
    /**
     * Bar chart
     */
    private BarChart barChart;
    /**
     * Path to the file as a string
     */
    private String path;

    /**
     * Basic constructor that creates a bar chart demo with given BarChart and a title(Path to the file)
     *
     * @param barChart bar chart to draw
     * @param path     path to the file
     */
    public BarChartDemo(BarChart barChart, String path) {
        setLocation(20, 20);
        setTitle("Bar chart");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.barChart = barChart;
        this.path = path;
        initGui();
        pack();
    }

    /**
     * Helping method that initializes gui
     */
    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(new BarChartComponent(barChart), BorderLayout.CENTER);
        cp.add(new JLabel(path, SwingConstants.CENTER), BorderLayout.PAGE_START);
    }

    /**
     * Starting method
     *
     * @param args exactly 1 argument, path to the file with data
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please give exactly one argument, a path to the file");
            return;
        }

        Path path = Paths.get(args[0]);

        try {
            BufferedReader br = Files.newBufferedReader(path);
            String xLabel = br.readLine();
            String yLabel = br.readLine();
            String data = br.readLine();
            List<XYValue> valueList = new ArrayList<>();
            for (String st : data.split(" ")) {
                String[] xy = st.split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                valueList.add(new XYValue(x, y));
            }
            int minY = Integer.parseInt(br.readLine());
            int maxY = Integer.parseInt(br.readLine());
            int step = Integer.parseInt(br.readLine());
            BarChart barChart = new BarChart(valueList, xLabel, yLabel, minY, maxY, step);

            SwingUtilities.invokeLater(() ->
                    new BarChartDemo(barChart, path.toAbsolutePath().toString()).setVisible(true)
            );
        } catch (Exception e) {
            System.out.println("Could not create a bar graph from file " + path.toAbsolutePath().toString());
        }
    }
}
