package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;


/**
 * A node representing a command which generates some textual output dynamically.
 *
 * @author Pavao Jerebić
 */
public class EchoNode extends Node {
    /**
     * elements
     */
    private Element[] elements;

    /**
     * Constructor
     *
     * @param elements elements
     * @throws IllegalArgumentException if elements is null
     */
    public EchoNode(Element[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements can not be null");
        }
        this.elements = elements;
    }

    /**
     * Getter for elements
     *
     * @return elements
     */
    public Element[] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EchoNode{elements= ");
        for (Element element : elements) {
            sb.append(element.asText());
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }
}
