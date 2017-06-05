package hr.fer.zemris.bf.parser;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Parser that creates a tree of a given boolean expression<br/>
 * It follows the rules of boolean algebra.<br/>
 * Supported operations are: not, or, and, xor ( {@link TokenType#OPERATOR}<br/>
 * It supports variables as described in {@link TokenType#VARIABLE} and constants ({@link TokenType#CONSTANT}<br/>
 * This parsers supports use of brackets<br/>
 * If symbols are given with special characters then they must be separated with a space( with the exception of xor )
 *
 * @author Pavao Jerebić
 */
public class Parser {

    /**
     * Root of expression tree
     */
    private Node root;
    /**
     * Lexer used when parsing
     */
    private Lexer lexer;

    /**
     * Constructor that creates a expression tree
     *
     * @param expression given string representation of a boolean expression
     * @throws ParserException if it fails
     */
    public Parser(String expression) {
        try {
            lexer = new Lexer(expression);

            root = parse();

            if (lexer.getToken().getTokenType() != TokenType.EOF) {
                throw new ParserException("Unexpected token: " + lexer.getToken());
            }


            root = simplify(root);

        } catch (LexerException | ParserException ex) {
            throw new ParserException(ex.getMessage());
        }
    }

    /**
     * Main method that parses given data
     *
     * @return root of expression tree
     * @throws ParserException if method fails to parse
     * @throws LexerException  if any expression is malformed
     */
    private Node parse() {

        return parseOR();

    }

    /**
     * Parses constant, variable, not, ( )
     *
     * @return a node
     * @throws ParserException if method fails to parse
     * @throws LexerException  if any expression is malformed
     */
    private Node parseBasic() {
        Token token = lexer.nextToken();

        switch (token.getTokenType()) {
            case CONSTANT:
                lexer.nextToken();
                return new ConstantNode((Boolean) token.getTokenValue());
            case VARIABLE:
                lexer.nextToken();
                return new VariableNode((String) token.getTokenValue());
            case OPERATOR:
                if (token.getTokenValue().equals("not")) {
                    return new UnaryOperatorNode("not", parseBasic(), a -> !a);
                } else {
                    throw new ParserException("Unexpected token: " + token);
                }
            case OPEN_BRACKET:
                Node node = parseOR();
                token = lexer.getToken();
                if (token.getTokenType() != TokenType.CLOSED_BRACKET) {
                    throw new ParserException("Unexpected token: " + token);
                }
                lexer.nextToken();
                return node;
            default:
                throw new ParserException("Unexpected token: " + token);
        }

    }


    /**
     * Parses and expression
     *
     * @return node representing and
     * @throws ParserException if method fails to parse
     * @throws LexerException  if any expression is malformed
     */
    private Node parseAND() {
        Node left = parseBasic();
        if (lexer.getToken().getTokenType() != TokenType.EOF && lexer.getToken().getTokenType() != TokenType.CLOSED_BRACKET) {
            Token token = lexer.getToken();
            if (token.getTokenType() == TokenType.OPERATOR && token.getTokenValue().equals("and")) {
                return new BinaryOperatorNode("and", Arrays.asList(left, parseAND()), (a, b) -> a & b);
            }
        }
        return left;
    }

    /**
     * Parses xor expression
     *
     * @return node representing xor
     * @throws ParserException if method fails to parse
     * @throws LexerException  if any expression is malformed
     */
    private Node parseXOR() {
        Node left = parseAND();
        if (lexer.getToken().getTokenType() != TokenType.EOF) {
            Token token = lexer.getToken();
            if (token.getTokenType() == TokenType.OPERATOR && token.getTokenValue().equals("xor")) {
                return new BinaryOperatorNode("xor", Arrays.asList(left, parseXOR()), (a, b) -> a ^ b);
            }
        }
        return left;
    }

    /**
     * Parses or expression
     *
     * @return node representing or
     * @throws ParserException if method fails to parse
     * @throws LexerException  if any expression is malformed
     */
    private Node parseOR() {
        Node left = parseXOR();
        if (lexer.getToken().getTokenType() != TokenType.EOF) {
            Token token = lexer.getToken();
            if (token.getTokenType() == TokenType.OPERATOR && token.getTokenValue().equals("or")) {
                return new BinaryOperatorNode("or", Arrays.asList(left, parseOR()), (a, b) -> a | b);
            }
        }
        return left;
    }

    /**
     * Method that simplifies given node tree
     *
     * @param root root of the tree
     * @return simplified version of a given tree
     */
    private Node simplify(Node root) {
        if (root instanceof UnaryOperatorNode) {
            root = simplify((UnaryOperatorNode) root);
        } else if (root instanceof BinaryOperatorNode) {
            root = simplify((BinaryOperatorNode) root);
        }
        return root;
    }

    /**
     * Helping method to simplify UnaryOperator node tree
     *
     * @param root unary operator node
     * @return simplified version of a given unary operator node tree
     */
    private Node simplify(UnaryOperatorNode root) {
        Node child = root.getChild();
        child = simplify(child);
        return new UnaryOperatorNode(root.getName(), child, root.getOperator());
    }

    /**
     * Helping method to simplify BinaryOperator node tree
     *
     * @param root binary operator node
     * @return simplified version of a given binary operator node tree
     */
    private Node simplify(BinaryOperatorNode root) {
        List<Node> children = new ArrayList<>();
        children.addAll(root.getChildren());
        for (Node child : root.getChildren()) {
            if (child instanceof BinaryOperatorNode) {
                Node oldChild = child;
                child = simplify((BinaryOperatorNode) child);
                children.remove(oldChild);
                children.add(child);
                if (((BinaryOperatorNode) child).getName().equals(root.getName())) {
                    children.remove(child);
                    children.addAll(((BinaryOperatorNode) child).getChildren());
                }
            } else {
                Node oldChild = child;
                child = simplify(child);
                children.remove(oldChild);
                children.add(child);
            }

        }
        return new BinaryOperatorNode(root.getName(), children, root.getOperator());
    }

    /**
     * Getter for expression
     *
     * @return expression
     */
    public Node getExpression() {
        return root;
    }
}
