package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Stack;

/**
 * Program that creates a simple calculator<br/>
 * Memory is provided with a stack implementation using 'push' and 'pop' buttons<br/>
 * Check box 'Inv' converts operations  sin, cos, tan, ctg, log, ln, x^n into their inverse versions<br/>
 * Operation 'res' resets calculator, and operation 'clr' removes last given number
 *
 * @author Pavao Jerebić
 */
public class Calculator extends JFrame {

    /**
     * Result label
     */
    private JLabel result;
    /**
     * Stack used for calculation
     */
    private Stack<String> stack;
    /**
     * Memory stack
     */
    private Stack<String> memory;
    /**
     * Current value builder
     */
    private StringBuilder currentValueBuilder;
    /**
     * 'Inv' check box
     */
    private JCheckBox inverted;

    /**
     * Basic constructor
     */
    public Calculator() {

        setLocation(20, 20);
        setSize(500, 200);
        setTitle("Calculator");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        stack = new Stack<>();
        memory = new Stack<>();
        currentValueBuilder = new StringBuilder();
        initGui();
        pack();
    }

    /**
     * Helping method used to initialize gui
     */
    private void initGui() {
        Container container = getContentPane();

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        JPanel p = new JPanel(new CalcLayout(3));
        result = new CalculatorResultLabel("");
        result.setBackground(Color.YELLOW);
        result.setForeground(Color.BLACK);
        result.setOpaque(true);
        result.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        p.add(result, "1,1");
        p.add(customJLabel("="), "1,6");
        p.add(customJLabel("clr"), "1,7");
        p.add(customJLabel("1/x"), "2,1");
        p.add(customJLabel("sin"), "2,2");
        p.add(customJLabel("7"), "2,3");
        p.add(customJLabel("8"), "2,4");
        p.add(customJLabel("9"), "2,5");
        p.add(customJLabel("/"), "2,6");
        p.add(customJLabel("res"), "2,7");
        p.add(customJLabel("log"), "3,1");
        p.add(customJLabel("cos"), "3,2");
        p.add(customJLabel("4"), "3,3");
        p.add(customJLabel("5"), "3,4");
        p.add(customJLabel("6"), "3,5");
        p.add(customJLabel("*"), "3,6");
        p.add(customJLabel("push"), "3,7");
        p.add(customJLabel("ln"), "4,1");
        p.add(customJLabel("tan"), "4,2");
        p.add(customJLabel("1"), "4,3");
        p.add(customJLabel("2"), "4,4");
        p.add(customJLabel("3"), "4,5");
        p.add(customJLabel("-"), "4,6");
        p.add(customJLabel("pop"), "4,7");
        p.add(customJLabel("x^n"), "5,1");
        p.add(customJLabel("ctg"), "5,2");
        p.add(customJLabel("0"), "5,3");
        p.add(customJLabel("+/-"), "5,4");
        p.add(customJLabel("."), "5,5");
        p.add(customJLabel("+"), "5,6");
        inverted = new JCheckBox("Inv");
        inverted.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        inverted.setBackground(new Color(114, 159, 207));
        inverted.setForeground(Color.BLACK);
        inverted.setFont(inverted.getFont().deriveFont(Font.BOLD, 20f));
        p.add(inverted, "5,7");


        container.add(p);
    }

    /**
     * Helping method that creates an instance of {@link CalculatorButtonLabel} with a mouse listener
     *
     * @param text name of the clickable label
     * @return clickable label
     */
    private JLabel customJLabel(String text) {
        CalculatorButtonLabel button = new CalculatorButtonLabel(text);
        button.setBackground(new Color(114, 159, 207));
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        button.addMouseListener(customMouseListener(text));
        return button;
    }

    /**
     * Helping method that creates mouse listener
     *
     * @param text text of the button
     * @return mouse listener
     */
    private MouseListener customMouseListener(String text) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (text.equals("res")) {
                    stack.clear();
                    memory.clear();
                    currentValueBuilder = new StringBuilder();
                    result.setText("");
                    return;
                } else if (text.equals("pop")) {
                    if (memory.isEmpty()) {
                        JOptionPane.showMessageDialog(Calculator.this, "Memory stack is empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!stack.isEmpty()) {
                        if (isDouble(stack.peek()).isPresent()) {
                            stack.pop();
                            stack.push(memory.pop());
                            result.setText(stack.peek());
                        } else {
                            stack.push(memory.pop());
                            result.setText(stack.peek());
                        }
                    } else if (stack.isEmpty()) {
                        stack.push(memory.pop());
                        result.setText(stack.peek());
                    } else {
                        JOptionPane.showMessageDialog(Calculator.this, "Invalid argument", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }
                Optional<Integer> isInt = isInteger(text);
                if (isInt.isPresent()) {
                    if (!stack.isEmpty() && isDouble(stack.peek()).isPresent()) {
                        stack.pop();
                    }
                    currentValueBuilder.append(text.charAt(0));
                    result.setText(currentValueBuilder.toString());
                } else {
                    Optional<Double> currentValue = isDouble(currentValueBuilder.toString());
                    if (currentValue.isPresent()) {
                        stack.push(currentValue.get().toString());
                        currentValueBuilder = new StringBuilder();
                    } else if (!stack.isEmpty() && isDouble(stack.peek()).isPresent()) {
                        currentValue = isDouble(stack.peek());
                    } else {
                        JOptionPane.showMessageDialog(Calculator.this, "Invalid arguments", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    switch (text) {
                        case "=":
                            if (stack.size() == 3) {
                                calculateBinary();
                            }
                            break;
                        case ".":
                            String top = stack.peek();
                            if (top.endsWith(".0")) {
                                currentValueBuilder = new StringBuilder(stack.pop().substring(0, top.length() - 2));
                                currentValueBuilder.append(text);
                                System.out.println(currentValueBuilder);
                                result.setText(currentValueBuilder.toString());
                            } else {
                                JOptionPane.showMessageDialog(Calculator.this, "Can not append '.'", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case "+/-":
                            if (currentValue.isPresent()) {
                                currentValueBuilder = new StringBuilder(Double.toString(currentValue.get() * -1.0));
                                stack.pop();
                                System.out.println(currentValueBuilder);
                                result.setText(currentValueBuilder.toString());
                            } else {
                                JOptionPane.showMessageDialog(Calculator.this, "Can not append change sign", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case "clr":
                            if (!stack.isEmpty() && isDouble(stack.peek()).isPresent()) {
                                stack.pop();
                                currentValueBuilder = new StringBuilder();
                                result.setText("");
                            }
                            break;
                        case "push":
                            if (!stack.isEmpty() && isDouble(stack.peek()).isPresent()) {
                                memory.push(stack.peek());
                            }
                            break;
                        default:
//                            System.out.println(stack);

                            doOperation(text);

                    }
                }
                System.out.println(stack.toString());
                System.out.println(memory.toString());
            }
        };
    }

    /**
     * Array of unary operators
     */
    private static final String[] UNARY_OPERATORS = new String[]{
//            "+/-",
            "1/x",
            "sin",
            "cos",
            "tan",
            "ctg",
            "log",
            "ln"
    };

    /**
     * Checks if given text is in unary operators
     *
     * @param text text
     * @return true if text is in unary operators
     */
    private boolean isUnary(String text) {
        for (String operator : UNARY_OPERATORS) {
            if (operator.equals(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helping method that does and operation given in text
     *
     * @param text text
     */
    private void doOperation(String text) {
        if (stack.size() == 1) {
            Optional<Double> doubleOptional = isDouble(stack.peek());

            if (doubleOptional.isPresent()) {
                if (isUnary(text)) {
                    calculateUnary(text);
                } else {
                    stack.push(text);
                }
            } else {
                System.out.println(doubleOptional);
                JOptionPane.showMessageDialog(Calculator.this, "Please input a number first", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (stack.size() == 3) {
            calculateBinary();

            Optional<Double> doubleOptional = isDouble(stack.peek());
            if (doubleOptional.isPresent()) {
                if (isUnary(text)) {
                    calculateUnary(text);
                } else {
                    stack.push(text);
                }
            } else {
                JOptionPane.showMessageDialog(Calculator.this, "Please input a number first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(Calculator.this, "Please input a number first", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helping method that calculates given unary operation and stores result on {@link #stack}
     *
     * @param text operation
     */
    private void calculateUnary(String text) {
        try {

            Optional<Double> a = isDouble(stack.pop());

            if (!a.isPresent()) {
                JOptionPane.showMessageDialog(Calculator.this, "Invalid arguments", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                switch (text) {
                    case "1/x":
                        stack.push(Double.toString(1.0 / a.get()));
                        break;
                    case "log":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.pow(10, a.get())));
                        } else {
                            stack.push(Double.toString(Math.log(a.get())));
                        }
                        break;
                    case "sin":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.asin(a.get())));
                        } else {
                            stack.push(Double.toString(Math.sin(a.get())));
                        }
                        break;
                    case "cos":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.acos(a.get())));
                        } else {
                            stack.push(Double.toString(Math.cos(a.get())));
                        }
                        break;
                    case "tan":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.atan(a.get())));
                        } else {
                            stack.push(Double.toString(Math.tan(a.get())));
                        }
                        break;
                    case "ctg":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.atan(1.0 / a.get())));
                        } else {
                            stack.push(Double.toString(1.0 / Math.tan(a.get())));
                        }
                        break;
                    case "ln":
                        if (inverted.isSelected()) {
                            stack.push(Double.toString(Math.pow(Math.E, a.get())));
                        } else {
                            stack.push(Double.toString(Math.log(a.get()) / Math.log(Math.E)));
                        }
                        break;
                }
            }
            result.setText(stack.peek());

        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(Calculator.this, "Not enough parameters", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helping method that calculates binary operation by taking arguments and operator from {@link #stack} and stores result on it
     */
    private void calculateBinary() {
        try {

            Optional<Double> b = isDouble(stack.pop());
            String operator = stack.pop();
            Optional<Double> a = isDouble(stack.pop());

            if (!a.isPresent() || !b.isPresent()) {
                JOptionPane.showMessageDialog(Calculator.this, "Invalid arguments", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            switch (operator) {
                case "/":
                    stack.push(Double.toString(a.get() / b.get()));
                    break;
                case "+":
                    stack.push(Double.toString(a.get() + b.get()));
                    break;
                case "*":
                    stack.push(Double.toString(a.get() * b.get()));
                    break;
                case "-":
                    stack.push(Double.toString(a.get() - b.get()));
                    break;
                case "x^n":
                    if (inverted.isSelected()) {
                        stack.push(Double.toString(Math.pow(a.get(), 1.0 / b.get())));
                    } else {

                        stack.push(Double.toString(Math.pow(a.get(), b.get())));
                    }
                    break;
                default:
                    stack.push("");
            }
            result.setText(stack.peek());

        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(Calculator.this, "Not enough parameters", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Parses given string into optional integer
     *
     * @param st string
     * @return optional integer
     */
    private Optional<Integer> isInteger(String st) {
        try {
            return Optional.of(Integer.parseInt(st));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    /**
     * Parses given string into optional double
     *
     * @param st string
     * @return optional double
     */
    private Optional<Double> isDouble(String st) {
        try {
            return Optional.of(Double.parseDouble(st));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Calculator().setVisible(true)
        );
    }
}
