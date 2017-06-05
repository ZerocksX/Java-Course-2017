package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Operator implementation of {@link Element}
 *
 * @author Pavao Jerebić
 */
public class ElementOperator extends Element {
    /**
     * symbol
     */
    private String symbol;

    /**
     * Constructor that sets symbol
     *
     * @param symbol symbol
     * @throws IllegalArgumentException if symbol is null
     */
    public ElementOperator(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol can not be null");
        }
        this.symbol = symbol;
    }

    /**
     * Getter for symbol
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String asText() {
        return symbol;
    }
}
