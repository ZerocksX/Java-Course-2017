package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Constant integer implementation of {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementConstantInteger extends Element {
    /**
     * value
     */
    private int value;

    /**
     * Constructor that sets element's value
     *
     * @param value value
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * getter for value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }
}
