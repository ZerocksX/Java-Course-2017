package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing a piece of textual data.
 *
 * @author Pavao Jerebić
 */
public class TextNode extends Node {
    /**
     * Text
     */
    private String text;

    /**
     * Constructor that sets text
     *
     * @param text text
     */
    public TextNode(String text) {
        this.text = text;
    }

    /**
     * Getter for text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "text='" + text + '\'' +
                '}';
    }
}
