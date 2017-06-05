package hr.fer.zemris.java.hw04.db.parser;

import hr.fer.zemris.java.hw04.db.ComparisonOperators;
import hr.fer.zemris.java.hw04.db.ConditionalExpression;
import hr.fer.zemris.java.hw04.db.FieldValueGetters;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class QueryParserTest {

    @Test
    public void testManyQueries() throws Exception {
        QueryParser parser = new QueryParser("firstName = \"pero\" aNd jmbag > \"5\"  and lastName LIKE \"abba*\"");
        List<ConditionalExpression> list = parser.getQuery();
        assertEquals(ComparisonOperators.EQUALS.getClass(),list.get(0).getComparisonOperator().getClass());
        assertEquals(ComparisonOperators.GREATER.getClass(),list.get(1).getComparisonOperator().getClass());
        assertEquals(ComparisonOperators.LIKE.getClass(),list.get(2).getComparisonOperator().getClass());

        assertEquals(FieldValueGetters.FIRST_NAME.getClass(),list.get(0).getFieldGetter().getClass());
        assertEquals(FieldValueGetters.JMBAG.getClass(),list.get(1).getFieldGetter().getClass());
        assertEquals(FieldValueGetters.LAST_NAME.getClass(),list.get(2).getFieldGetter().getClass());

    }
}