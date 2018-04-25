package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.colors.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Application that uses {@link JDrawingCanvas}<br/>
 * Has simple menu that provides user with opening, saving, saving as, and exiting actions<br/>
 * Supported format for open is '.jvc'<br/>
 * Supported formats for saving are '.gif', '.jpg', '.png' and '.jvc'<br/>
 * JVC format is text file that describes objects, ex. LINE x1 y1 x2 y2 r g b<br/>
 * User can choose three {@link GeometricalObject}s<br/>
 * User can also choose foreground and background color for next object by clicking one of the two color areas in the toolbar
 *
 * @author Pavao Jerebić
 */
public class JVDraw extends JFrame {

    /**
     * list model
     */
    private DrawingObjectListModel listModel;
    /**
     * Current file path
     */
    private Path openedFilePath;
    /**
     * has canvas changed
     */
    private boolean changed;

    /**
     * Constructor that initializes whole application
     */
    public JVDraw() {
        setLocation(20, 20);
        setSize(700, 500);
        setTitle("JVDraw");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initActions();
        initGui();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exitAction.actionPerformed(new ActionEvent(JVDraw.this, 0, null));
            }
        });
    }

    /**
     * Initializes actions
     */
    private void initActions() {
        openAction.putValue(Action.NAME, "Open");
        openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openAction.putValue(Action.SHORT_DESCRIPTION, "Used to open a document from the disk");

        saveAction.putValue(Action.NAME, "Save");
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveAction.putValue(Action.SHORT_DESCRIPTION, "Used to save a document to the disk");

        saveAsAction.putValue(Action.NAME, "Save as");
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveAsAction.putValue(Action.SHORT_DESCRIPTION, "Used to save a document to a specific path at the disk");

        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Used to close the application");

        clearAction.putValue(Action.NAME, "Clear");
        clearAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift c"));
        clearAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        clearAction.putValue(Action.SHORT_DESCRIPTION, "Used to clear the canvas");

    }

    /**
     * Initializes gui
     */
    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        initMenus();

        JPanel mainPane = new JPanel(new BorderLayout());
        JToolBar toolBar = new JToolBar();

        JColorArea foregroundColor = new JColorArea(Color.RED, "foreground");
        JColorArea backgroundColor = new JColorArea(Color.BLUE, "background");
        toolBar.add(foregroundColor);
        toolBar.addSeparator();
        toolBar.add(backgroundColor);
        toolBar.addSeparator();

        ButtonGroup buttonGroup = new ButtonGroup();
        JToggleButton lineButton = new JToggleButton("Line");
        JToggleButton circleButton = new JToggleButton("Circle");
        JToggleButton filledCircleButton = new JToggleButton("Filled circle");
        buttonGroup.add(lineButton);
        buttonGroup.add(circleButton);
        buttonGroup.add(filledCircleButton);
        toolBar.add(lineButton);
        toolBar.add(circleButton);
        toolBar.add(filledCircleButton);

        DrawingModel model = new DrawingModelImpl();
        model.addDrawingModelListener(new DrawingModelListener() {
            @Override
            public void objectsAdded(DrawingModel source, int index0, int index1) {
                setChanged(true);
            }

            @Override
            public void objectsRemoved(DrawingModel source, int index0, int index1) {
                setChanged(true);
            }

            @Override
            public void objectsChanged(DrawingModel source, int index0, int index1) {
                setChanged(true);
            }
        });

        listModel = new DrawingObjectListModel(model);

        JList<GeometricalObject> list = new JList<>(listModel);


        mainPane.add(new JScrollPane(list), BorderLayout.EAST);

        JDrawingCanvas canvas = new JDrawingCanvas(
                listModel,
                new ArrayList<>(Arrays.asList(lineButton, circleButton, filledCircleButton)),
                foregroundColor,
                backgroundColor
        );
        model.addDrawingModelListener(canvas.getDmListener());

        mainPane.add(toolBar, BorderLayout.NORTH);

        mainPane.add(canvas, BorderLayout.CENTER);

        cp.add(mainPane, BorderLayout.CENTER);
    }

    /**
     * Initializes menus
     */
    private void initMenus() {
        Container cp = getContentPane();

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(openAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(saveAction));
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem(clearAction));

        menuBar.add(editMenu);


        cp.add(menuBar, BorderLayout.NORTH);
    }

    /**
     * Action that opens a new file
     */
    private Action openAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");

            if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            Path filePath = fc.getSelectedFile().toPath();

            if (!Files.isReadable(filePath) || !filePath.toAbsolutePath().toString().endsWith(".jvc")) {
                JOptionPane.showMessageDialog(
                        JVDraw.this,
                        "File " + filePath + " is not readable.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String text;

            try {
                text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        JVDraw.this,
                        "There was an error while reading file " + filePath + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Scanner sc = new Scanner(new ByteArrayInputStream(text.getBytes()));
            listModel.clear();

            parseAndAddObjects(sc);

            sc.close();
            setOpenedFilePath(filePath);

        }
    };

    /**
     * Helping method that parses JVC format using data from the given scanner
     *
     * @param sc scanner
     */
    private void parseAndAddObjects(Scanner sc) {

        while (sc.hasNext()) {
            String token = sc.next();
            GeometricalObject object;

            try {
                switch (token) {
                    case "LINE": {
                        int x1 = sc.nextInt();
                        int y1 = sc.nextInt();
                        int x2 = sc.nextInt();
                        int y2 = sc.nextInt();
                        int r = sc.nextInt();
                        int g = sc.nextInt();
                        int b = sc.nextInt();
                        object = new LineObject(x1, y1, x2, y2);
                        object.setForeground(new Color(r, g, b));
                        break;
                    }
                    case "CIRCLE": {
                        int x1 = sc.nextInt();
                        int y1 = sc.nextInt();
                        int radius = sc.nextInt();
                        int r = sc.nextInt();
                        int g = sc.nextInt();
                        int b = sc.nextInt();
                        object = new OvalObject(x1, y1, radius);
                        object.setForeground(new Color(r, g, b));
                        break;
                    }
                    case "FCIRCLE": {
                        int x1 = sc.nextInt();
                        int y1 = sc.nextInt();
                        int radius = sc.nextInt();
                        int r1 = sc.nextInt();
                        int g1 = sc.nextInt();
                        int b1 = sc.nextInt();
                        int r2 = sc.nextInt();
                        int g2 = sc.nextInt();
                        int b2 = sc.nextInt();
                        object = new FilledOvalObject(x1, y1, radius);
                        object.setForeground(new Color(r1, g1, b1));
                        object.setBackground(new Color(r2, g2, b2));
                        break;
                    }
                    default:
                        continue;
                }
                listModel.add(object);
            } catch (Exception ignored) {
            }
        }

    }

    /**
     * Action that represents saving an image
     */
    private Action saveAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            saveToPath(openedFilePath);
        }
    };

    /**
     * Helping method that represents saving image as
     */
    private Action saveAsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            saveToPath(null);
        }
    };

    /**
     * Helping method that saves the current image to the given path, if the path is null user will be asked where to save the image
     *
     * @param openedFilePath path
     */
    private void saveToPath(Path openedFilePath) {
        if (openedFilePath == null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save file");
            if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {

                JOptionPane.showMessageDialog(
                        JVDraw.this,
                        "Saving canceled",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            openedFilePath = fc.getSelectedFile().toPath();

            if (Files.exists(openedFilePath)) {
                if (JOptionPane.showConfirmDialog(
                        JVDraw.this,
                        "File already exists. Do you wish to overwrite?",
                        "Message",
                        JOptionPane.YES_NO_OPTION
                ) == JOptionPane.NO_OPTION) {
                    return;
                }
            }


        }

        try {
            if (openedFilePath.toAbsolutePath().toString().endsWith(".jvc")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, n = listModel.getSize(); i < n; i++) {
                    sb.append(String.format("%s%n", listModel.getElementAt(i).toString()));
                }
                Files.write(openedFilePath, sb.toString().getBytes(StandardCharsets.UTF_8));

            } else {
                List<GeometricalObject> objects = new ArrayList<>();
                for (int i = 0, n = listModel.getSize(); i < n; i++) {
                    objects.add(listModel.getElementAt(i));
                }
                Point minPoint = objects.size() > 0 ? new Point(objects.get(0).boundingBox().x, objects.get(0).boundingBox().y) : new Point();
                Point maxPoint = objects.size() > 0 ? new Point(objects.get(0).boundingBox().x, objects.get(0).boundingBox().y) : new Point();

                objects.forEach(object -> {
                    Rectangle box = object.boundingBox();
                    minPoint.x = Math.min(minPoint.x, box.x);
                    minPoint.y = Math.min(minPoint.y, box.y);
                    maxPoint.x = Math.max(maxPoint.x, box.x + box.width);
                    maxPoint.y = Math.max(maxPoint.y, box.y + box.height);
                });


                BufferedImage image = new BufferedImage(
                        maxPoint.x - minPoint.x, maxPoint.y - minPoint.y, BufferedImage.TYPE_3BYTE_BGR
                );
                Graphics2D g = image.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                objects.forEach(object -> {
                    Point previousLocation = object.getLocation();
                    Point newPoint = new Point(new Point(previousLocation.x - minPoint.x, previousLocation.y - minPoint.y));
                    object.setLocation(newPoint);
                    object.paint(g);
                    object.setLocation(previousLocation);
                });
                g.dispose();

                if (openedFilePath.toAbsolutePath().toString().endsWith(".gif")) {
                    ImageIO.write(image, "gif", openedFilePath.toFile());
                } else if (openedFilePath.toAbsolutePath().toString().endsWith(".png")) {
                    ImageIO.write(image, "png", openedFilePath.toFile());
                } else if (openedFilePath.toAbsolutePath().toString().endsWith(".jpg")) {
                    ImageIO.write(image, "jpg", openedFilePath.toFile());
                } else {
                    JOptionPane.showMessageDialog(
                            JVDraw.this,
                            "Invalid extension(png, jpg, jvc and gif only). Contents of the file " + openedFilePath + " are unknown.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    JVDraw.this,
                    "Saving failed. Contents of the file " + openedFilePath + " are unknown.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        setOpenedFilePath(openedFilePath);
        setChanged(false);

        JOptionPane.showMessageDialog(
                JVDraw.this,
                "Successfully saved!",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    /**
     * Action that exits the application<br/>
     * If there is any unsaved data user will be asked whether they want to save it
     */
    private Action exitAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (changed) {
                int response = JOptionPane.showConfirmDialog(
                        JVDraw.this,
                        "You have unsaved changes. Do you wish to save them?",
                        "Message",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    saveToPath(openedFilePath);
                } else if (response == JOptionPane.NO_OPTION) {
                    dispose();
                } else {
                    return;
                }
            }
            if (changed) {
                return;
            }
            dispose();
        }
    };

    /**
     * Action that clears the whole canvas
     */
    private Action clearAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            listModel.clear();
        }
    };

    /**
     * Helping method that sets current file path and changes title
     *
     * @param openedFilePath file path
     */
    private void setOpenedFilePath(Path openedFilePath) {
        this.openedFilePath = openedFilePath;
        JVDraw.this.setTitle("JVDraw - " + openedFilePath.toAbsolutePath().toString());
    }

    /**
     * Helping method that sets changed flag and changes title if needed
     *
     * @param changed changed flag
     */
    private void setChanged(boolean changed) {
        this.changed = changed;
        if (changed && !getTitle().startsWith("*")) {
            setTitle("*" + getTitle());
        }
    }

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
    }

}
