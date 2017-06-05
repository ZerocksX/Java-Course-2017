package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node visitor
 *
 * @author Pavao Jerebić
 */
public interface INodeVisitor {
    /**
     * Visits this node
     *
     * @param node node
     */
    void visitTextNode(TextNode node);

    /**
     * Visits this node
     *
     * @param node node
     */
    void visitForLoopNode(ForLoopNode node);

    /**
     * Visits this node
     *
     * @param node node
     */
    void visitEchoNode(EchoNode node);

    /**
     * Visits this node
     *
     * @param node node
     */
    void visitDocumentNode(DocumentNode node);
}
