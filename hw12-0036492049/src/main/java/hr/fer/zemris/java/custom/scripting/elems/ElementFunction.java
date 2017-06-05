package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Function implementation for {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementFunction extends Element {
    /**
     * function name
     */
    private String name;

    /**
     * Constructor that sets function name
     *
     * @param name name
     * @throws IllegalArgumentException if name is null
     */
    public ElementFunction(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }
        this.name = name;
    }

    /**
     * getter for name
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
