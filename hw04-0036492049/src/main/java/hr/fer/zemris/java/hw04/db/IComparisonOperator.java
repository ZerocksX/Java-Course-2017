package hr.fer.zemris.java.hw04.db;

/**
 * Filter that is comparing two strings and returns true if operation is true, false otherwise
 * @author Pavao Jerebić
 */
public interface IComparisonOperator {
    /**
     * Method that returns true if operation is true
     * @param value1 first string
     * @param value2 other string
     * @return true if operation is true
     */
    public boolean satisfied(String value1, String value2);
}
