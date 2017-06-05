package hr.fer.zemris.java.custom.scripting.elems;

/**
 * String implementation of {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementString extends Element {
    /**
     * value
     */
    private String value;

    /**
     * Constructor that sets value
     *
     * @param value value
     * @throws IllegalArgumentException if value is null
     */
    public ElementString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value can not be null");
        }
        this.value = value;
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String asText() {
        return value;
    }
}
