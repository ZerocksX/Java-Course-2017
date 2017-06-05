package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Visitor that evaluates given expression with a given array of values
 *
 * @author Pavao Jerebić
 */
public class ExpressionEvaluator implements NodeVisitor {

    /**
     * Values of variables
     */
    private boolean[] values;
    /**
     * Position of variables in values array
     */
    private Map<String, Integer> positions;
    /**
     * Stack used to recursively calculate expression
     */
    private Stack<Boolean> stack = new Stack<>();
    /**
     * List of variables
     */
    private List<String> variables;

    /**
     * Setter for values and prepares for evaluation
     *
     * @param values values for variables
     * @throws IllegalArgumentException if values is not of valid size
     */
    public void setValues(boolean[] values) {
        if (values.length != variables.size()) {
            throw new IllegalArgumentException("Not a valid size of values");
        }
        start();
        this.values = values;
    }

    /**
     * Clears stack and prepares for evaluation
     */
    public void start() {
        stack.clear();
    }


    /**
     * Getter for result
     *
     * @return result
     */
    public boolean getResult() {
        return stack.peek();
    }

    /**
     * Constructor that accepts a list of variables and initializes fields
     *
     * @param variables list of variables
     */
    public ExpressionEvaluator(List<String> variables) {
        this.variables = variables;
        int i = 0;
        positions = new HashMap<>();
        for (String variable : variables) {
            positions.put(variable, i++);
        }

    }

    @Override
    public void visit(ConstantNode node) {
        stack.push(node.getValue());
    }

    @Override
    public void visit(VariableNode node) {
        Integer position = positions.get(node.getName());
        if (position == null) {
            throw new IllegalStateException("Not a valid variable: " + node.getName());
        }

        stack.push(values[position]);
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        node.getChild().accept(this);
        boolean value = stack.pop();
        stack.push(node.getOperator().apply(value));
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        boolean value = node.getOperator().apply(stack.pop(), stack.pop());
        for (int i = 2, n = node.getChildren().size(); i < n; i++) {
            value = node.getOperator().apply(value, stack.pop());
        }
        stack.push(value);
    }
}
