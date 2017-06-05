package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Class that parses input and creates a document model tree using {@link Node} hierarchy<br/>
 * Nodes that are used are {@link DocumentNode} for root, {@link TextNode} for text outside of tags, {@link ForLoopNode} for for loops nad {@link EchoNode} for nameless tags(name is "=")<br/>
 * Document is parsed using {@link SmartScriptLexer}<br/>
 * For loop : {$ FOR variable start end step $}<br/>
 * Echo loop : {$= elements $}<br/>
 * End : {$ END $}<br/>
 *
 * @author Pavao Jerebić
 */
public class SmartScriptParser {
    /**
     * Document body
     */
    private String document;
    /**
     * Lexer
     */
    private SmartScriptLexer lexer;
    /**
     * Stack for tree construction
     */
    private Stack<Node> stack;
    /**
     * Root of the tree
     */
    private DocumentNode root;

    /**
     * Constructor that parses document body and creates a tree
     *
     * @param document document body
     * @throws IllegalArgumentException if document is null
     * @throws SmartScriptParserException if parsing failed
     */
    public SmartScriptParser(String document) {
        if (document == null) {
            throw new IllegalArgumentException("Document can not be null");
        }
        this.document = document;
        lexer = new SmartScriptLexer(document);
        stack = new Stack<>();
        root = new DocumentNode();
        stack.push(root);
        parse();
    }

    /**
     * Prints tree from root
     */
    @Deprecated
    public void printTree() {
        printTree(root, 0);
    }

    /**
     * Prints tree
     *
     * @param node  starting node
     * @param depth initial depth
     */
    private void printTree(Node node, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.printf("\t");
        }
        System.out.println(node);
        for (int i = 0; i < node.numberOfChildren(); i++) {
            printTree(node.getChild(i), depth + 1);
        }
    }

    /**
     * Parses document body and constructs tree
     *
     * @throws SmartScriptParserException if document is malformed
     */
    private void parse() {
        boolean cont = true;
        try {

            while (cont) {
                SmartScriptToken token = lexer.nextSmartScriptToken();
                switch (token.getType()) {
                    case WORD:
                        TextNode textNode = new TextNode((String) token.getValue());
                        Node node = stack.peek();
                        node.addChildNode(textNode);
                        break;
                    case TAG:
                        processTag();
                        break;
                    case EOF:
                        cont = false;
                        break;
                }
            }

        } catch (SmartScriptLexerException ex) {
            throw new SmartScriptParserException(ex.getMessage());
        }
    }

    /**
     * Helping method to process tag and construct tree
     *
     * @throws SmartScriptParserException if tag is malformed
     */
    private void processTag() {
        if (lexer.nextSmartScriptToken().getValue().equals('=')) {

            EchoNode echoNode = processEchoTag();

            Node node = stack.peek();

            node.addChildNode(echoNode);

        } else if (((String) lexer.getSmartScriptToken().getValue()).toUpperCase().equals("FOR")) {

            ForLoopNode forLoopNode = processForLoopTag();

            Node node = stack.peek();

            node.addChildNode(forLoopNode);

            stack.push(forLoopNode);

        } else if (((String) lexer.getSmartScriptToken().getValue()).toUpperCase().equals("END")) {

            processEndTag();

        } else {

            throw new SmartScriptParserException("Invalid input. Wrong tag name" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
        }
    }

    /**
     * Helping method to process end tag
     *
     * @throws SmartScriptParserException if tag is malformed or in inappropriate position
     */
    private void processEndTag() {
        try {
            lexer.nextSmartScriptToken();
            if (lexer.getSmartScriptToken().getType() != SmartScriptTokenType.TAG) {
                throw new SmartScriptParserException("Invalid input. Malformed tag" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
            }
            stack.pop();
        } catch (EmptyStackException ex) {
            throw new SmartScriptLexerException("Invalid input. Unexpected end tag" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
        }
        if (stack.size() == 0) {
            throw new SmartScriptLexerException("Invalid input. Unexpected end tag" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
        }
    }

    /**
     * Helping method to create echo node
     *
     * @return echo node
     * @throws SmartScriptParserException if tag is malformed
     */
    private EchoNode processEchoTag() {
        List<Element> collection = new ArrayList<>();
        while (lexer.nextSmartScriptToken().getType() != SmartScriptTokenType.TAG) {
            SmartScriptToken token = lexer.getSmartScriptToken();

            if (token.getType() == SmartScriptTokenType.EOF) {
                throw new SmartScriptParserException("Invalid input. Tags not closed" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
            }

            Element element = getElementFromToken(token);

            collection.add(element);
        }
        Element[] elements = new Element[collection.size()];
        for (int i = 0; i < collection.size(); i++) {
            elements[i] = collection.get(i);
        }
        return new EchoNode(elements);
    }

    /**
     * Helping method to create for loop node
     *
     * @return for loop node with its arguments set
     * @throws SmartScriptParserException if for loop tag is malformed
     */
    private ForLoopNode processForLoopTag() {

        SmartScriptToken token = lexer.nextSmartScriptToken();

        if (token.getType() != SmartScriptTokenType.VARIABLE) {
            throw new SmartScriptParserException("Invalid input. First argument is not a variable" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
        }

        List<Element> collection = new ArrayList<>();
        collection.add(new ElementVariable((String) token.getValue()));

        int cnt = 0;
        while (lexer.nextSmartScriptToken().getType() != SmartScriptTokenType.TAG) {
            token = lexer.getSmartScriptToken();
            switch (token.getType()) {
                case EOF:
                    throw new SmartScriptParserException("Invalid input. Tags not closed" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
                case VARIABLE:
                    collection.add(new ElementVariable((String) token.getValue()));
                    break;
                case CONSTANTINTEGER:
                    collection.add(new ElementConstantInteger((Integer) token.getValue()));
                    break;
                case CONSTANTDOUBLE:
                    collection.add(new ElementConstantDouble((Double) token.getValue()));
                    break;
                case STRING:
                    collection.add(new ElementString((String) token.getValue()));
                    break;
                default:
                    throw new SmartScriptParserException("Invalid input. Argument not variable, number or string" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());
            }
            cnt++;
        }

        Element[] elements = new Element[collection.size()];
        for (int i = 0; i < collection.size(); i++) {
            elements[i] = collection.get(i);
        }

        if (cnt == 2) {

            return new ForLoopNode((ElementVariable) elements[0], elements[1], elements[2], null);

        } else if (cnt == 3) {

            return new ForLoopNode((ElementVariable) elements[0], elements[1], elements[2], elements[3]);

        } else {

            throw new SmartScriptParserException("Invalid input. Wrong number of arguments" + "\nCurrent token : " + lexer.getSmartScriptToken().toString());

        }
    }

    /**
     * Returns element from token<br/>
     * to use when inside of tag
     * will return null if token is EOF,TAG or WORD
     *
     * @param token token
     * @return appropriate element
     */
    private Element getElementFromToken(SmartScriptToken token) {
        Element element = null;

        switch (token.getType()) {
            case CONSTANTDOUBLE:
                element = new ElementConstantDouble((Double) token.getValue());
                break;
            case CONSTANTINTEGER:
                element = new ElementConstantInteger((Integer) token.getValue());
                break;
            case FUNCTION:
                element = new ElementFunction((String) token.getValue());
                break;
            case OPERATOR:
                element = new ElementOperator(String.valueOf((char) token.getValue()));
                break;
            case STRING:
                element = new ElementString((String) token.getValue());
                break;
            case VARIABLE:
                element = new ElementVariable((String) token.getValue());
                break;
        }

        return element;
    }

    /**
     * Getter for root node
     *
     * @return root node;
     */
    public DocumentNode getDocumentNode() {
        return root;
    }

}
