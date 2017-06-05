package hr.fer.zemris.java.custom.scripting.nodes;


import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all graph nodes
 *
 * @author Pavao Jerebić
 */
public abstract class Node {

    /**
     * Collection of children
     */
    private List<Node> children;

    /**
     * Adds given child to an internally managed collection of children
     *
     * @param child child node
     * @throws IllegalArgumentException if child is null
     */
    public void addChildNode(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Can't add null");
        }
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    /**
     * Returns a number of (direct) children
     *
     * @return number of children
     */
    public int numberOfChildren() {
        return children == null ? 0 : children.size();
    }

    /**
     * returns selected child
     *
     * @param index index
     * @return selected child
     * @throws IllegalArgumentException if child doesn't exist at given index
     */
    public Node getChild(int index) {
        try {
            return children.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Index " + index + " out of bounds");
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "children=" + children +
                '}';
    }

    /**
     * Visitors visits this node [ and its children ]
     *
     * @param visitor visitor
     */
    public abstract void accept(INodeVisitor visitor);
}
