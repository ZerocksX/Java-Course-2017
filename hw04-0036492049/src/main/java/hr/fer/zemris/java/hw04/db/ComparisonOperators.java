package hr.fer.zemris.java.hw04.db;

/**
 * Set of {@link IComparisonOperator} constants
 *
 * @author Pavao Jerebić
 */
public class ComparisonOperators {
    /**
     * Lexicographical operator <
     */
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    /**
     * Lexicographical operator <=
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    /**
     * Lexicographical operator >
     */
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    /**
     * Lexicographical operator >=
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    /**
     * Lexicographical operator =
     */
    public static final IComparisonOperator EQUALS = String::equals;

    /**
     * Lexicographical operator <>
     */
    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> !value1.equals(value2);

    /**
     * Checks if value1 matches regular expression value2
     */
    public static final IComparisonOperator LIKE = (value1, value2) -> value1.matches(value2.replaceAll("\\*", ".*"));

}
