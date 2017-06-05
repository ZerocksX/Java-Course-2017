package hr.fer.zemris.java.hw04.db.parser;

import hr.fer.zemris.java.hw04.db.*;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw04.db.lexer.QueryToken;
import hr.fer.zemris.java.hw04.db.lexer.QueryTokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses query<br/>
 * Takes given string and then produces a list of {@link hr.fer.zemris.java.hw04.db.ConditionalExpression} - queries<br/>
 * There are 2 types of queries:<br/>
 * Direct query - jmbag = "something"<br/>
 * All other queries<br/>
 * Syntax is fieldName operator string literal [and ...]
 *
 * @author Pavao Jerebić
 */
public class QueryParser {
    /**
     * is query direct
     */
    private boolean directQuery;
    /**
     * list of conditional expressions
     */
    private List<ConditionalExpression> query;
    /**
     * input data
     */
    private String data;
    /**
     * lexer for token production
     */
    private QueryLexer lexer;

    /**
     * Returns true if query is directs
     *
     * @return is query direct
     */
    public boolean isDirectQuery() {
        return directQuery;
    }

    /**
     * Getter for list of {@link ConditionalExpression}
     *
     * @return query
     */
    public List<ConditionalExpression> getQuery() {
        return query;
    }

    /**
     * Getter for queried JMBAG
     *
     * @return queried JMBAG
     * @throws IllegalStateException if query is not direct
     */
    public String getQueriedJMBAG() {
        if (!directQuery) {
            throw new IllegalStateException("Query is not direct");
        }
        return query.get(0).getStringLiteral();
    }

    /**
     * Constructor that parses data and creates a list of {@link ConditionalExpression}
     *
     * @param data input data
     * @throws IllegalArgumentException if data is null
     * @throws QueryParserException     if anything goes wrong
     */
    public QueryParser(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        this.data = data;
        lexer = new QueryLexer(data);
        query = new ArrayList<>();
        try {
            parse();
        } catch (QueryParserException | QueryLexerException e) {
            throw new QueryParserException(e.getMessage());
        }
        if (query.size() == 1
                && query.get(0).getFieldGetter() == FieldValueGetters.JMBAG
                && query.get(0).getComparisonOperator() == ComparisonOperators.EQUALS) {
            directQuery = true;
        }
    }

    /**
     * Helping method to parse tokens<br/>
     * it has form field operator string [and ...]
     */
    private void parse() {
        QueryToken token = lexer.nextToken();
        while (token.getType() != QueryTokenType.EOL) {
            if (token.getType() == QueryTokenType.FIELD) {
                IFieldValueGetter getter;
                switch (token.getValue()) {
                    case "jmbag":
                        getter = FieldValueGetters.JMBAG;
                        break;
                    case "firstName":
                        getter = FieldValueGetters.FIRST_NAME;
                        break;
                    case "lastName":
                        getter = FieldValueGetters.LAST_NAME;
                        break;
                    default:
                        throw new QueryParserException("Not a valid field value");
                }
                token = lexer.nextToken();
                if (token.getType() == QueryTokenType.OPERATOR) {
                    IComparisonOperator operator;
                    switch (token.getValue()) {
                        case "<":
                            operator = ComparisonOperators.LESS;
                            break;
                        case "<=":
                            operator = ComparisonOperators.LESS_OR_EQUALS;
                            break;
                        case "<>":
                            operator = ComparisonOperators.NOT_EQUALS;
                            break;
                        case "=":
                            operator = ComparisonOperators.EQUALS;
                            break;
                        case ">":
                            operator = ComparisonOperators.GREATER;
                            break;
                        case ">=":
                            operator = ComparisonOperators.GREATER_OR_EQUALS;
                            break;
                        case "LIKE":
                            operator = ComparisonOperators.LIKE;
                            break;
                        default:
                            throw new QueryParserException("Not a valid operator value");
                    }

                    token = lexer.nextToken();

                    if (token.getType() == QueryTokenType.STRING) {
                        String value = token.getValue().substring(1, token.getValue().length() - 1);

                        query.add(new ConditionalExpression(getter, value, operator));

                    } else {
                        throw new QueryParserException("Expected string but got: " + token);
                    }

                } else {
                    throw new QueryParserException("Expected operator but got: " + token);
                }
            } else {
                throw new QueryParserException("Expected field but got: " + token);
            }

            token = lexer.nextToken();

            if (token.getType() == QueryTokenType.AND) {
                token = lexer.nextToken();
            }
        }
    }
}
