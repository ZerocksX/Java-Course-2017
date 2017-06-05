package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * Program that represent a simple editing tool<br/>
 * Program consists of multiple tabs, each representing a document<br/>
 * Some simple editing options are implemented, i.e. case toggling, sorting...<br/>
 * User can load and save files from/to the disk<br/>
 * If the user wants to exit before saving all of their progress, user will be asked if they want to save their current progress for each tab opened<br/>
 *
 * @author Pavao Jerebić
 */
public class JNotepadPP extends JFrame {

    /**
     * Tabbed pane
     */
    private JTabbedPane tabbedPane;
    /**
     * Stop requested, for time thread
     */
    private boolean stopRequested;
    /**
     * Length info label
     */
    private JLabel lengthInfo;
    /**
     * Caret info label
     */
    private JLabel caretInfo;
    /**
     * Time info label
     */
    private JLabel timeInfo;
    /**
     * Provider
     */
    private LocalizationProvider provider;

    /**
     * Constructor that initializes everything<br/>
     * Sets up listeners for exiting the applications, thread for time displaying, toolbars, menus and components
     */
    public JNotepadPP() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setTitle("Untitled - JNotepadPP");
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exitAction.actionPerformed(new ActionEvent(JNotepadPP.this, 0, null));
            }
        });

        initGui();
        provider = LocalizationProvider.getInstance();
        new FormLocalizationProvider(provider, JNotepadPP.this);

        Thread timeTask = new Thread(() -> {
            while (!stopRequested) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SwingUtilities.invokeLater(() -> timeInfo.setText(sdf.format(new Date(System.currentTimeMillis()))));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ignored) {
                }
            }
        });
        timeTask.setDaemon(true);
        timeTask.start();

    }

    /**
     * Helping method that initializes all components
     */
    private void initGui() {

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel centralPanel = new JPanel(new BorderLayout());

        JPanel statusBar = new JPanel(new GridLayout(1, 3));
        lengthInfo = new JLabel("length: ", SwingConstants.LEFT);
        lengthInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        caretInfo = new JLabel("Ln: Col: Sel:", SwingConstants.CENTER);
        caretInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        timeInfo = new JLabel("", SwingConstants.RIGHT);
        timeInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        statusBar.add(lengthInfo);
        statusBar.add(caretInfo);
        statusBar.add(timeInfo);

        centralPanel.add(statusBar, BorderLayout.PAGE_END);

        tabbedPane = new JTabbedPane();
//        tabbedPane.addTab(
//                "Untitled",
//                Utils.createImageIcon(this.getClass(), "red_circle.png"),
//                new JScrollPane(new MyJTextArea(true, JNotepadPP.this, null))
//        );

        tabbedPane.addChangeListener(changeEvent -> {
                    MyJTextArea textArea = getCurrentTextArea();
                    if (textArea != null) {
                        if (textArea.getFilePath() == null) {
                            setTitle("Untitled - JNotepadPP");
                        } else {
                            setTitle(textArea.getFilePath().toAbsolutePath().toString() + " - JNotepadPP");
                        }
                        textArea.updateInfo();
                    }
                }
        );

        centralPanel.add(tabbedPane, BorderLayout.CENTER);

        cp.add(centralPanel, BorderLayout.CENTER);

        createActions();

        createMenus();

        createToolbars();

    }


    /**
     * Action that represents a new document creation
     */
    private Action newDocumentAction = new LocalizableAction("new", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            tabbedPane.addTab(
                    "Untitled",
                    Utils.createImageIcon(this.getClass(), "red_circle.png"),
                    new JScrollPane(new MyJTextArea(true, JNotepadPP.this, null))
            );
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
    };

    /**
     * Action that represents opening a document
     */
    private Action openDocumentAction = new LocalizableAction("open", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");

            if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            Path filePath = fc.getSelectedFile().toPath();

            if (!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
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
                        JNotepadPP.this,
                        "There was an error while reading file " + filePath + ".",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            tabbedPane.addTab(
                    filePath.getFileName().toString(),
                    Utils.createImageIcon(this.getClass(), "green_circle.png"),
                    new JScrollPane(new MyJTextArea(text, false, JNotepadPP.this, filePath))
            );
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

        }
    };

    /**
     * Action that represents saving a document
     */
    private Action saveDocumentAction = new LocalizableAction("save", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea currentTextArea = getCurrentTextArea();

            if (currentTextArea == null) {
                return;
            }
            Path openedFilePath = currentTextArea.getFilePath();

            saveCurrentTextAreaWithPath(currentTextArea, openedFilePath);
        }
    };

    /**
     * Helping method that represents saving document as
     */
    private Action saveDocumentAsAction = new LocalizableAction("save_as", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea currentTextArea = getCurrentTextArea();

            if (currentTextArea == null) {
                return;
            }

            saveCurrentTextAreaWithPath(currentTextArea, null);
        }
    };

    /**
     * Helping method that saves the current text area to the given path, if the path is null user will be asked where to save the text
     *
     * @param currentTextArea text area
     * @param openedFilePath  path
     */
    private void saveCurrentTextAreaWithPath(MyJTextArea currentTextArea, Path openedFilePath) {
        if (openedFilePath == null) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save file");
            if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {

                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        "Saving canceled",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            openedFilePath = fc.getSelectedFile().toPath();

        }

        try {
            Files.write(openedFilePath, currentTextArea.getText().getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    "Saving failed. Contents of the file " + openedFilePath + " are unknown.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        currentTextArea.setFilePath(openedFilePath);
        currentTextArea.setChanged(false);

        JOptionPane.showMessageDialog(
                JNotepadPP.this,
                "Successfully saved!",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Action representing closing a tab
     */
    private Action closeDocumentAction = new LocalizableAction("close", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea currentTextArea = getCurrentTextArea();
            if (currentTextArea == null) {
                return;
            }
            if (currentTextArea.isChanged()) {

                int response = JOptionPane.showConfirmDialog(
                        JNotepadPP.this,
                        "Do you wish to save your progress on this tab?",
                        "Message",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {

                    saveDocumentAction.actionPerformed(actionEvent);
                    if (!currentTextArea.isChanged()) {
                        tabbedPane.remove(tabbedPane.getSelectedIndex());
                    }

                } else if (response == JOptionPane.NO_OPTION) {
                    tabbedPane.remove(tabbedPane.getSelectedIndex());

                }
            } else {
                tabbedPane.remove(tabbedPane.getSelectedIndex());
            }

        }
    };

    /**
     * Action representing pasting from clipboard
     */
    private Action pasteAction = new LocalizableAction("paste", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea textArea = getCurrentTextArea();
            if (textArea == null) {
                return;
            }
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            String result;
            try {
                result = (String) clipboard.getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ignored) {
                return;
            }

            if (result == null) {
                return;
            }

            Document doc = textArea.getDocument();

            int len = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
            int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
            if (offset == 0 && len == 0) {
                offset = doc.getLength();
            }
            try {
                doc.remove(offset, len);
                doc.insertString(offset, result, null);
            } catch (BadLocationException ignored) {
            }


        }
    };

    /**
     * Action representing copying text to clipboard
     */
    private Action copyAction = new LocalizableAction("copyText", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea textArea = getCurrentTextArea();
            if (textArea == null) {
                return;
            }

            Document doc = textArea.getDocument();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();

            int len = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
            int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());

            if (len == 0) {
                return;
            }

            try {
                String text = doc.getText(offset, len);
                StringSelection selection = new StringSelection(text);
                clipboard.setContents(selection, selection);
            } catch (BadLocationException ignored) {
            }
        }
    };

    /**
     * Action representing cutting text to clipboard
     */
    private Action cutAction = new LocalizableAction("cut", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea textArea = getCurrentTextArea();
            if (textArea == null) {
                return;
            }

            Document doc = textArea.getDocument();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();

            int len = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
            int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());

            if (len == 0) {
                return;
            }

            try {
                String text = doc.getText(offset, len);
                StringSelection selection = new StringSelection(text);
                clipboard.setContents(selection, selection);
                doc.remove(offset, len);
            } catch (BadLocationException ignored) {
            }


        }
    };

    /**
     * Action representing toggling text
     */
    private Action toggleSelectedPartAction = new LocalizableAction("toggle", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            changeText((text) -> {
                StringBuilder sb = new StringBuilder(text.length());

                for (Character c : text.toCharArray()) {

                    if (Character.isUpperCase(c)) {
                        sb.append(Character.toLowerCase(c));
                    } else if (Character.isLowerCase(c)) {
                        sb.append(Character.toUpperCase(c));
                    } else {
                        sb.append(c);
                    }
                }

                return sb.toString();
            });

        }
    };

    /**
     * Action representing switching text to upper case
     */
    private Action toUpperCaseAction = new LocalizableAction("upper", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeText((text) -> {
                StringBuilder sb = new StringBuilder(text.length());

                for (Character c : text.toCharArray()) {
                    sb.append(Character.toUpperCase(c));
                }

                return sb.toString();
            });
        }
    };

    /**
     * Action representing switching text to lower case
     */
    private Action toLowerCaseAction = new LocalizableAction("lower", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeText((text) -> {
                StringBuilder sb = new StringBuilder(text.length());

                for (Character c : text.toCharArray()) {
                    sb.append(Character.toLowerCase(c));
                }

                return sb.toString();
            });
        }
    };

    /**
     * Helping method that transforms currently selected text, or the whole document, with the given function
     *
     * @param function function to transform text
     */
    private void changeText(Function<String, String> function) {
        MyJTextArea editor = getCurrentTextArea();
        if (editor == null) {
            return;
        }

        Document doc = editor.getDocument();

        int offset = 0;
        int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

        if (len == 0) {
            len = doc.getLength();
        } else {

            offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
        }
        try {
            String text = doc.getText(offset, len);
            text = function.apply(text);
            doc.remove(offset, len);
            doc.insertString(offset, text, null);
        } catch (BadLocationException ignored) {
        }

    }

    /**
     * Action representing displaying statistics
     */
    private Action displayStatisticsAction = new LocalizableAction("statistics", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MyJTextArea textArea = getCurrentTextArea();
            if (textArea == null) {
                return;
            }
            String text = textArea.getText();

            int nOfCharacters = 0, nOfNonBlankCharacters = 0, nOfLines = 0;

            for (Character character : text.toCharArray()) {
                nOfCharacters++;
                if (!Character.isWhitespace(character)) {
                    nOfNonBlankCharacters++;
                }
                if (String.format("%n").charAt(0) == character) {
                    nOfLines++;
                }
            }

            if (nOfCharacters != 0) {
                nOfLines++;
            }

            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    String.format("A number of characters found in document is %d,%n" +
                            "a number of non-blank characters found in document is %d,%n" +
                            "a number of lines that the document contains is %d", nOfCharacters, nOfNonBlankCharacters, nOfLines
                    ),
                    "Statistics",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }
    };


    /**
     * Action representing exiting the program
     */
    private Action exitAction = new LocalizableAction("exit", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int numberOfChangedTabs = 0;

            for (int i = 0, n = tabbedPane.getTabCount(); i < n; i++) {
                tabbedPane.setSelectedIndex(i);
                MyJTextArea textArea = getCurrentTextArea();
                if (textArea == null) {
                    continue;
                }
                if (textArea.isChanged()) {
                    numberOfChangedTabs++;
                }
            }

            if (numberOfChangedTabs == 0) {

                tabbedPane.removeAll();

            } else {
                int response = JOptionPane.showConfirmDialog(
                        JNotepadPP.this,
                        "Do you wish to save changes?",
                        "Message",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (response == JOptionPane.NO_OPTION) {

                    tabbedPane.removeAll();

                } else if (response == JOptionPane.YES_OPTION) {

                    for (int i = 0, n = tabbedPane.getTabCount(); i < n; i++) {
                        tabbedPane.setSelectedIndex(i);
                        closeDocumentAction.actionPerformed(actionEvent);
                        if (tabbedPane.getTabCount() != n) {
                            i--;
                            n = tabbedPane.getTabCount();
                        }
                    }
                }
            }


            if (tabbedPane.getTabCount() == 0) {
                dispose();
                stopRequested = true;
            }
        }
    };

    /**
     * Action representing sorting selected lines ascending
     */
    private Action sortLinesAscendingAction = new LocalizableAction("ascending", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
            Collator collator = Collator.getInstance(locale);
            sortLinesOnCurrentSelection(collator::compare);
        }
    };

    /**
     * Action representing sorting selected lines descending
     */
    private Action sortLinesDescendingAction = new LocalizableAction("descending", LocalizationProvider.getInstance()) {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
            Collator collator = Collator.getInstance(locale);
            sortLinesOnCurrentSelection((s1, s2) -> collator.compare(s2, s1));
        }
    };

    /**
     * Helping method that sorts selected lines in the current document using the given comparator
     *
     * @param comparator comparator
     */
    private void sortLinesOnCurrentSelection(Comparator<String> comparator) {
        MyJTextArea textArea = getCurrentTextArea();
        if (textArea == null) {
            return;
        }

        Caret caret = textArea.getCaret();

        if (caret.getMark() - caret.getDot() == 0) {
            return;
        }

        int start = Math.min(caret.getMark(), caret.getDot()), stop = Math.max(caret.getMark(), caret.getDot());

        int lineStart;
        int lineStop;

        int startingOffset = 0;
        int endingOffset = 0;
        String[] linesAsArray = null;


        try {
            lineStart = textArea.getLineOfOffset(start);
            lineStop = textArea.getLineOfOffset(stop);

            startingOffset = textArea.getLineStartOffset(lineStart);
            endingOffset = textArea.getLineEndOffset(lineStop);

            String text = textArea.getDocument().getText(startingOffset, endingOffset - startingOffset);
            linesAsArray = text.split(String.format("%n"), -1);
        } catch (BadLocationException ignored) {
        }


        if (linesAsArray == null) {
            return;
        }

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(linesAsArray));

        lines.sort(comparator);

        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = lines.size(); i < n; i++) {
            String line = lines.get(i);
            sb.append(line);

            if (i != n - 1) {
                sb.append(String.format("%n"));
            }
        }


        String result = sb.toString();

        try {
            textArea.getDocument().remove(startingOffset, endingOffset - startingOffset);
            textArea.getDocument().insertString(startingOffset, result, null);
        } catch (BadLocationException ignored) {
        }


    }

    /**
     * Action representing changing the language to Croatian
     */
    private Action hrLanguageAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            provider.setLanguage("hr");
        }
    };

    /**
     * Action representing changing the language to English
     */
    private Action enLanguageAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            provider.setLanguage("en");
        }
    };

    /**
     * Action representing changing the language to German
     */
    private Action deLanguageAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            provider.setLanguage("de");
        }
    };

    /**
     * Helping method that initializes all actions to english<br/>
     * Also sets their mnemonics and accelerators
     */
    private void createActions() {
        newDocumentAction.putValue(Action.NAME, "New");
        newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        newDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used create a new File");

        openDocumentAction.putValue(Action.NAME, "Open");
        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open a document from the disk");

        saveDocumentAction.putValue(Action.NAME, "Save");
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save a document to the disk");

        saveDocumentAsAction.putValue(Action.NAME, "Save as");
        saveDocumentAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveDocumentAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveDocumentAsAction.putValue(Action.SHORT_DESCRIPTION, "Used to save a document to a specific path at the disk");

        closeDocumentAction.putValue(Action.NAME, "Close tab");
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to close the current document");

        toggleSelectedPartAction.putValue(Action.NAME, "Toggle case");
        toggleSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
        toggleSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        toggleSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, "Used to toggle case in selection or in the whole document if the selection does not exist");

        toUpperCaseAction.putValue(Action.NAME, "Switch to upper case");
        toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F4"));
        toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
        toUpperCaseAction.putValue(Action.SHORT_DESCRIPTION, "Used to switch to upper case in selection or in the whole document if the selection does not exist");

        toLowerCaseAction.putValue(Action.NAME, "Switch to lower case");
        toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F5"));
        toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
        toLowerCaseAction.putValue(Action.SHORT_DESCRIPTION, "Used to switch to lower case in selection or in the whole document if the selection does not exist");

        pasteAction.putValue(Action.NAME, "Paste");
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
        pasteAction.putValue(Action.SHORT_DESCRIPTION, "Used to paste text from clipboard into the editor. Overwrites selected text");

        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        copyAction.putValue(Action.SHORT_DESCRIPTION, "Used to copy text from the editor into the clipboard.");

        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
        cutAction.putValue(Action.SHORT_DESCRIPTION, "Used to cut text from the editor into the clipboard.");

        displayStatisticsAction.putValue(Action.NAME, "Statistics");
        displayStatisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F10"));
        displayStatisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        displayStatisticsAction.putValue(Action.SHORT_DESCRIPTION, "Used to display statistics calculated from the text.");

        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Used to close all tabs");

        sortLinesAscendingAction.putValue(Action.NAME, "Sort lines ascending");
        sortLinesAscendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift a"));
        sortLinesAscendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        sortLinesAscendingAction.putValue(Action.SHORT_DESCRIPTION, "Used to sort all selected lines ascending");

        sortLinesDescendingAction.putValue(Action.NAME, "Sort lines descending");
        sortLinesDescendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift d"));
        sortLinesDescendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        sortLinesDescendingAction.putValue(Action.SHORT_DESCRIPTION, "Used sort all selected lines descending");

        enLanguageAction.putValue(Action.NAME, "English");
//        enLanguageAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift a"));
        enLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
        enLanguageAction.putValue(Action.SHORT_DESCRIPTION, "Changes language to english");

        deLanguageAction.putValue(Action.NAME, "Deutsche");
//        deLanguageAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift a"));
        deLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        deLanguageAction.putValue(Action.SHORT_DESCRIPTION, "Ändert die Sprache auf Deutsch");

        hrLanguageAction.putValue(Action.NAME, "Hrvatski");
//        hrLanguageAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift a"));
        hrLanguageAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
        hrLanguageAction.putValue(Action.SHORT_DESCRIPTION, "Mijenja jezik u hrvatske");


    }

    /**
     * Helping method that creates program menu from created actions
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new LJMenu("File", "file", LocalizationProvider.getInstance());

        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new LJMenu("Edit", "edit", LocalizationProvider.getInstance());
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));

        menuBar.add(editMenu);


        JMenu documentMenu = new LJMenu("Document", "document", LocalizationProvider.getInstance());

        menuBar.add(documentMenu);

        documentMenu.add(new JMenuItem(closeDocumentAction));
        documentMenu.addSeparator();
        documentMenu.add(new JMenuItem(displayStatisticsAction));

        JMenu toolsMenu = new LJMenu("Tools", "tools", LocalizationProvider.getInstance());

        menuBar.add(toolsMenu);

        toolsMenu.add(new JMenuItem(toLowerCaseAction));
        toolsMenu.add(new JMenuItem(toUpperCaseAction));
        toolsMenu.add(new JMenuItem(toggleSelectedPartAction));
        toolsMenu.addSeparator();
        toolsMenu.add(new JMenuItem(sortLinesAscendingAction));
        toolsMenu.add(new JMenuItem(sortLinesDescendingAction));

        JMenu languageMenu = new JMenu("Languages/Jezici/Sprache");

        menuBar.add(languageMenu);

        languageMenu.add(new JMenuItem(enLanguageAction));
        languageMenu.add(new JMenuItem(hrLanguageAction));
        languageMenu.add(new JMenuItem(deLanguageAction));


        setJMenuBar(menuBar);
    }

    /**
     * Helping method that creates the toolbar with buttons
     */
    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Toolbar");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.add(new JButton(saveDocumentAsAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(newDocumentAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(closeDocumentAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(cutAction));
        toolBar.add(new JButton(copyAction));
        toolBar.add(new JButton(pasteAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(displayStatisticsAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(exitAction));


        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Getter for current text area<br/>
     * Can return null if no text area is selected
     *
     * @return current text area or null if it does not exist
     */
    private MyJTextArea getCurrentTextArea() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if (scrollPane == null) return null;
        JViewport viewport = scrollPane.getViewport();

        return (MyJTextArea) viewport.getView();
    }

    /**
     * Staring method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new JNotepadPP().setVisible(true)
        );
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
     * Getter for length info label
     *
     * @return length info label
     */
    public JLabel getLengthInfo() {
        return lengthInfo;
    }

    /**
     * Getter for caret info label
     *
     * @return caret info label
     */
    public JLabel getCaretInfo() {
        return caretInfo;
    }
}

