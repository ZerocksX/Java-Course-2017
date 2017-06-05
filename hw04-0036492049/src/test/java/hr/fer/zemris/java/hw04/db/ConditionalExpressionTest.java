package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class ConditionalExpressionTest {

    @Test
    public void testExpression() throws Exception {
        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Pre*",
                ComparisonOperators.LIKE
        );
        StudentRecord record = new StudentRecord("0036492049","Ime","Prezime",5);
        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record), // returns lastName from given record
                expr.getStringLiteral() // returns "Bos*"
        );
        assertTrue(recordSatisfies);
    }
}