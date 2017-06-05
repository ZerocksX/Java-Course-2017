package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * Node representing a unary operator
 *
 * @author Pavao Jerebić
 */
public class UnaryOperatorNode implements Node {

    /**
     * Name of unary operator
     */
    private String name;
    /**
     * Operator's child
     */
    private Node child;
    /**
     * Operator function
     */
    private UnaryOperator<Boolean> operator;


    /**
     * Constructor for the operator
     *
     * @param name     name
     * @param child    child
     * @param operator operator function
     * @throws IllegalArgumentException if any argument is null
     */
    public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
        if (name == null
                || child == null
                || operator == null) {
            throw new IllegalArgumentException("All arguments must not be null");
        }
        this.name = name;
        this.child = child;
        this.operator = operator;
    }


    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for child
     *
     * @return child
     */
    public Node getChild() {
        return child;
    }

    /**
     * Getter for operator function
     *
     * @return operator function
     */
    public UnaryOperator<Boolean> getOperator() {
        return operator;
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
