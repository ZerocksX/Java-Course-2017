package hr.fer.zemris.java.hw04.db;

/**
 * Representation of a complete conditional expression
 *
 * @author Pavao Jerebić
 */
public class ConditionalExpression {
    /**
     * Field value getter, first operand
     */
    private IFieldValueGetter getter;
    /**
     * String representing second operand
     */
    private String value;
    /**
     * Operator
     */
    private IComparisonOperator operator;

    /**
     * Constructor for all fields
     *
     * @param getter   field value getter
     * @param value    second operand
     * @param operator operator
     * @throws IllegalArgumentException if any given argument is null
     */
    public ConditionalExpression(IFieldValueGetter getter, String value, IComparisonOperator operator) {
        if (getter == null || value == null || operator == null) {
            throw new IllegalArgumentException("One or more arguments are null");
        }
        this.getter = getter;
        this.value = value;
        this.operator = operator;
    }

    /**
     * Getter for field value getter
     *
     * @return field value getter
     */
    public IFieldValueGetter getFieldGetter() {
        return getter;
    }

    /**
     * Getter for second value
     *
     * @return second value
     */
    public String getStringLiteral() {
        return value;
    }

    /**
     * Getter for operator
     *
     * @return operator
     */
    public IComparisonOperator getComparisonOperator() {
        return operator;
    }
}
