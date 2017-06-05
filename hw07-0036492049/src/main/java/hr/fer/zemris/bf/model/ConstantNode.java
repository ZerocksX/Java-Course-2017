package hr.fer.zemris.bf.model;

/**
 * Node representing a constant( true or false )
 *
 * @author Pavao Jerebić
 */
public class ConstantNode implements Node {

    /**
     * Value
     */
    private boolean value;

    /**
     * Constructor that sets value
     *
     * @param value given value
     */
    public ConstantNode(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public boolean getValue() {
        return value;
    }
}
