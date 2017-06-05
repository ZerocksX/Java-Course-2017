package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;

/**
 * Node visitor that prints a tree from a given expression
 *
 * @author Pavao Jerebić
 */
public class ExpressionTreePrinter implements NodeVisitor {

    /**
     * Current depth, used for indentation
     */
    private int depth = 0;

    @Override
    public void visit(ConstantNode node) {

        System.out.println(indent() + (node.getValue() ? "1" : "0"));

    }

    @Override
    public void visit(VariableNode node) {

        System.out.println(indent() + node.getName());

    }

    @Override
    public void visit(UnaryOperatorNode node) {

        System.out.println(indent() + node.getName());

        depth++;

        node.getChild().accept(this);

        depth--;

    }

    @Override
    public void visit(BinaryOperatorNode node) {

        System.out.println(indent() + node.getName());

        depth++;

        node.getChildren().forEach(node1 -> node1.accept(this));

        depth--;

    }

    /**
     * Helping method that creates n times of double blank spaces
     *
     * @return indentation
     */
    private String indent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; ++i) {
            sb.append("  ");
        }
        return sb.toString();
    }
}
