package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing an entire document
 *
 * @author Pavao Jerebić
 */
public class DocumentNode extends Node {
    @Override
    public String toString() {
        return "";
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitDocumentNode(this);
    }
}
