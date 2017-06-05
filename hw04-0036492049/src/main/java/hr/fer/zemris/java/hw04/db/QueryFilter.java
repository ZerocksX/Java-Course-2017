package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Implementation of {@link IFilter} that checks if all {@link ConditionalExpression} are satisfied
 *
 * @author Pavao Jerebić
 */
public class QueryFilter implements IFilter {
    /**
     * List of conditional expressions
     */
    List<ConditionalExpression> expressions;

    /**
     * Constructor that sets conditional expression list
     *
     * @param expressions expression list
     * @throws IllegalArgumentException if expression list is null
     */
    public QueryFilter(List<ConditionalExpression> expressions) {
        if (expressions == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        this.expressions = expressions;
    }

    @Override
    public boolean accepts(StudentRecord record) {
        for (ConditionalExpression expression : expressions) {
            if (!expression.getComparisonOperator().satisfied(
                    expression.getFieldGetter().get(record),
                    expression.getStringLiteral())
                    ) {
                return false;
            }
        }
        return true;
    }
}
