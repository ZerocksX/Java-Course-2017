package hr.fer.zemris.bf.model;

/**
 * Visitor that visits {@link Node}s and does an operation with its' value
 *
 * @author Pavao Jerebić
 */
public interface NodeVisitor {
    /**
     * Method that processes a {@link ConstantNode}
     *
     * @param node constant
     */
    void visit(ConstantNode node);

    /**
     * Method that processes a {@link VariableNode}
     *
     * @param node variable
     */
    void visit(VariableNode node);

    /**
     * Method that processes a {@link UnaryOperatorNode}
     *
     * @param node unary operator
     */
    void visit(UnaryOperatorNode node);

    /**
     * Method that processes a {@link BinaryOperatorNode}
     *
     * @param node binary operator
     */
    void visit(BinaryOperatorNode node);
}
