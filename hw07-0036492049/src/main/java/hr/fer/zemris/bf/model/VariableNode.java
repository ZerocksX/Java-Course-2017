package hr.fer.zemris.bf.model;

/**
 * Node representation of a variable
 *
 * @author Pavao Jerebić
 */
public class VariableNode implements Node {

    /**
     * Name of the variable
     */
    private String name;

    /**
     * Constructor that sets variable name
     *
     * @param name variable name
     * @throws IllegalArgumentException if name is null
     */
    public VariableNode(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }
        this.name = name;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }
}
