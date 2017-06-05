package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Node representing a binary operator
 *
 * @author Pavao Jerebić
 */
public class BinaryOperatorNode implements Node {

    /**
     * Operator name
     */
    private String name;
    /**
     * List of children
     */
    private List<Node> children;
    /**
     * Operator function
     */
    private BinaryOperator<Boolean> operator;

    /**
     * Constructor that sets all fields
     *
     * @param name     name
     * @param children children
     * @param operator operator function
     * @throws IllegalArgumentException if any argument is null
     */
    public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
        if (name == null
                || children == null
                || operator == null) {
            throw new IllegalArgumentException("All arguments must not be null");
        }
        this.name = name;
        this.children = children;
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
     * Getter for children
     *
     * @return children
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Getter for operator function
     *
     * @return operator function
     */
    public BinaryOperator<Boolean> getOperator() {
        return operator;
    }

    @Override
    public void accept(NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
