package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Constant double implementation of {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementConstantDouble extends Element {
    /**
     * value
     */
    private double value;

    /**
     * Constructor that sets element's value
     *
     * @param value value
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * simple getter for value
     *
     * @return value
     */
    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }
}
