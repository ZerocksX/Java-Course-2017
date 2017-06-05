package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.util.Utils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.nio.file.Path;

/**
 * {@link JTextArea} decorator that stores data to be used in {@link JTabbedPane}, ex. is content changed, path to file on the disk<br/>
 * if file path is null it means that this document has not been saved yet
 *
 * @author Pavao Jerebić
 */
public class MyJTextArea extends JTextArea {
    /**
     * Is changed flag
     */
    private boolean changed;
    /**
     * Tabbed pane, parent object
     */
    private JTabbedPane tabbedPane;
    /**
     * JNotepadPP parent object
     */
    private JNotepadPP jNotepadPP;
    /**
     * Path to the file on the disk representing text on screen
     */
    private Path filePath;

    /**
     * Constructor that sets all private fields and adds a document and caret listener
     *
     * @param s          text
     * @param changed    changed flag
     * @param jNotepadPP JNotepadPP parent object
     * @param filePath   path to the file
     * @throws IllegalArgumentException if any parameter is null other than file path
     */
    public MyJTextArea(String s, boolean changed, JNotepadPP jNotepadPP, Path filePath) {
        super(s);
        if (s == null || jNotepadPP == null) {
            throw new IllegalArgumentException();
        }
        this.changed = changed;
        this.tabbedPane = jNotepadPP.getTabbedPane();
        this.jNotepadPP = jNotepadPP;
        this.filePath = filePath;

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                setChanged(true);
                updateLengthInfo(documentEvent);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                setChanged(true);
                updateLengthInfo(documentEvent);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                setChanged(true);
                updateLengthInfo(documentEvent);
            }
        });

        addCaretListener(this::updateCaretInfo);

        updateInfo();
    }

    /**
     * Helping method that updates caretInfo label
     *
     * @param caretEvent caret event
     */
    private void updateCaretInfo(CaretEvent caretEvent) {
        JLabel caretInfo = jNotepadPP.getCaretInfo();
        int lineNumber = 1;
        int columnNumber = 1;
        int selection = Math.abs(caretEvent.getDot() - caretEvent.getMark());

        try {

            lineNumber = getLineOfOffset(caretEvent.getDot());
            columnNumber = caretEvent.getDot() - getLineStartOffset(lineNumber);
            lineNumber++;
        } catch (BadLocationException ignored) {
            System.out.println(ignored.toString());
        }

        caretInfo.setText(
                String.format(
                        "Ln: %d Col: %d Sel: %d ",
                        lineNumber,
                        columnNumber,
                        selection
                )
        );
    }

    /**
     * Helping method that updates caretInfo without caretEvent
     */
    private void updateCaretInfo() {
        updateCaretInfo(new CaretEvent(this) {
            @Override
            public int getDot() {
                return getCaret().getDot();
            }

            @Override
            public int getMark() {
                return getCaret().getMark();
            }
        });
    }

    /**
     * Helping method that updates length label
     *
     * @param documentEvent document event
     */
    private void updateLengthInfo(DocumentEvent documentEvent) {
        JLabel lengthInfo = jNotepadPP.getLengthInfo();
        lengthInfo.setText("length: " + Integer.toString(documentEvent.getDocument().getLength()));
    }

    /**
     * Helping method that updates length label without document event
     */
    private void updateLengthInfo() {
        JLabel lengthInfo = jNotepadPP.getLengthInfo();
        lengthInfo.setText("length: " + Integer.toString(getDocument().getLength()));
    }

    /**
     * Method that updates length and caret info labels
     */
    public void updateInfo() {
        updateLengthInfo();
        updateCaretInfo();
    }

    /**
     * Constructor that sets all private fields
     *
     * @param changed    changed flag
     * @param jNotepadPP JNotepadPP instance, parent
     * @param filePath   path to the file
     * @throws IllegalArgumentException if any parameter is null other that filepath
     */
    public MyJTextArea(boolean changed, JNotepadPP jNotepadPP, Path filePath) {
        this("", changed, jNotepadPP, filePath);
    }

    /**
     * Getter for changed
     *
     * @return is changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Getter for tabbed pane
     *
     * @return tabbed pane
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Getter for file path
     *
     * @return file path
     */
    public Path getFilePath() {
        return filePath;
    }


    /**
     * Setter for file path, updates tab title
     *
     * @param filePath file path
     * @throws IllegalArgumentException if file path is null
     */
    public void setFilePath(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException();
        }
        this.filePath = filePath;
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), filePath.getFileName().toString());
    }

    /**
     * Setter for changed flag, updates icon
     *
     * @param changed is changed
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
        if (changed) {
            tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), Utils.createImageIcon(this.getClass(), "red_circle.png"));
        } else {
            tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), Utils.createImageIcon(this.getClass(), "green_circle.png"));
        }
    }
}
