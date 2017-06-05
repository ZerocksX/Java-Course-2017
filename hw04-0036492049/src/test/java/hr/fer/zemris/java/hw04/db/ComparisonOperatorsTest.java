package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class ComparisonOperatorsTest {


    @Test
    public void testGreater() throws Exception {
        IComparisonOperator oper = ComparisonOperators.GREATER;
        assertTrue(oper.satisfied("ZAGG" , "AAA"));
    }

    @Test
    public void testGreaterOrEquals() throws Exception {
        IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
        assertTrue(oper.satisfied("ZAGG" , "ZAGG"));
    }

    @Test
    public void testLess() throws Exception {
        IComparisonOperator oper = ComparisonOperators.LESS;
        assertTrue(oper.satisfied("AAA" , "ZAGG"));
    }

    @Test
    public void testLessOrEquals() throws Exception {
        IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
        assertTrue(oper.satisfied("ZAGG" , "ZAGG"));
    }

    @Test
    public void testEquals() throws Exception {
        IComparisonOperator oper = ComparisonOperators.EQUALS;
        assertTrue(oper.satisfied("ZAGG" , "ZAGG"));
    }

    @Test
    public void testNotEquals() throws Exception {
        IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
        assertFalse(oper.satisfied("ZAGG" , "ZAGG"));
    }

    @Test
    public void testLike() throws Exception {
        IComparisonOperator oper = ComparisonOperators.LIKE;
        assertFalse(oper.satisfied("Zagreb", "Aba*")); // false
        assertFalse(oper.satisfied("AAA", "AA*AA")); // false
        assertTrue(oper.satisfied("AAAA", "AA*AA")); // true
    }
}