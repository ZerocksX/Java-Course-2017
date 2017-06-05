package hr.fer.zemris.bf.model;

/**
 * Node in expression tree
 *
 * @author Pavao Jerebić
 */
public interface Node {
    /**
     * Method that accepts given node visitor and does its' operations on this node and its' children(if any exist)
     *
     * @param nodeVisitor node visitor
     */
    void accept(NodeVisitor nodeVisitor);
}
