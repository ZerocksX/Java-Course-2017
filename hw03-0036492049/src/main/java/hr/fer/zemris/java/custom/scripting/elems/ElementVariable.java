package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Variable implementation of {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementVariable extends Element {
    /**
     * name
     */
    private String name;

    /**
     * Constructor that sets name
     *
     * @param name name
     * @throws IllegalArgumentException if name is null
     */
    public ElementVariable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }
        this.name = name;
    }

    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return name;
    }
}
