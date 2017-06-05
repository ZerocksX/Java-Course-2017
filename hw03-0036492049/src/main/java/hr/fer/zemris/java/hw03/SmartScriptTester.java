package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.io.InvalidClassException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Program that takes exactly 1 argument from the command line<br/>
 * Program takes file path and parses given file<br/>
 * If the file can not be parsed program will display a message and then quit<br/>
 * Program will re create the file after it is parsed and then write it on standard output
 *
 * @author Pavao Jerebić
 */
public class SmartScriptTester {

    /**
     * Starting method
     *
     * @param args path to document
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please input exactly 1 argument");
            System.exit(-1);
        }

        String filepath = args[0];
        String docBody = null;
        try {
            docBody = new String(
                    Files.readAllBytes(Paths.get(filepath)),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println("Can not read from given path");
            System.exit(-1);
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }

        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = null;
        try {
            originalDocumentBody = createOriginalDocumentBody(document);
        } catch (InvalidClassException e) {
            System.out.println("Tree not formed properly");
            System.exit(-1);
        }

        System.out.println(originalDocumentBody); // should write something like original content of docBody
    }

    /**
     * Method that re-creates original document from given document node
     *
     * @param document document node
     * @return string representing original document
     * @throws InvalidClassException if any of the child nodes are not of a valid type ( not {@link EchoNode}, {@link ForLoopNode} or {@link TextNode}
     */
    public static String createOriginalDocumentBody(DocumentNode document) throws InvalidClassException {
        StringBuilder sb = new StringBuilder();

        sb.append(processChildren(document));

        return sb.toString();
    }

    /**
     * Helping method that creates string representation of all children then appends them to one string
     *
     * @param node parent node
     * @return string representation of all children
     * @throws InvalidClassException if any of the child nodes are not of a valid type ( not {@link EchoNode}, {@link ForLoopNode} or {@link TextNode}
     */
    private static String processChildren(Node node) throws InvalidClassException {

        StringBuilder sb = new StringBuilder();

        for (int i = 0, size = node.numberOfChildren(); i < size; i++) {

            Node child = node.getChild(i);

            if (child instanceof TextNode) {

                sb.append(processTextNode((TextNode) child));

            } else if (child instanceof EchoNode) {

                sb.append(processEchoNode((EchoNode) child));

            } else if (child instanceof ForLoopNode) {

                sb.append(processForLoopNode((ForLoopNode) child));

            } else {
                throw new InvalidClassException("Node class not supported");
            }
        }

        return sb.toString();
    }

    /**
     * Helping method that processes for loop node and all its' children
     *
     * @param child for loop node
     * @return string representation of for loop node and all its' children
     * @throws InvalidClassException if any of the child nodes are not of a valid type ( not {@link EchoNode}, {@link ForLoopNode} or {@link TextNode}
     */
    private static String processForLoopNode(ForLoopNode child) throws InvalidClassException {
        StringBuilder sb = new StringBuilder();

        sb.append("{$FOR");

        sb.append(" ").append(child.getVariable().asText());
        sb.append(" ").append(child.getStartExpression().asText());
        sb.append(" ").append(child.getEndExpression().asText());
        if (child.getStepExpression() != null) {
            sb.append(" ").append(child.getStepExpression().asText());
        }

        sb.append("$}");

        sb.append(processChildren(child));

        sb.append("{$END$}");

        return sb.toString();

    }

    /**
     * Helping method that processes EchoNode
     *
     * @param child echo node
     * @return string representation of echo node
     */
    private static String processEchoNode(EchoNode child) {
        StringBuilder sb = new StringBuilder();

        sb.append("{$=");

        for (Element element : child.getElements()) {
            if (element instanceof ElementString) {
                String text = element.asText();
                text = text.substring(1, text.length() - 1);
                text = text.replaceAll("\\\\", "\\\\\\\\");
                text = text.replaceAll("\"", "\\\\\"");
                sb.append("\"").append(text).append("\" ");
            } else {
                sb.append(element.asText()).append(" ");
            }
        }

        sb.append("$}");

        return sb.toString();
    }

    /**
     * Helping method that processes text node
     *
     * @param child text node
     * @return string representation of text node
     */
    private static String processTextNode(TextNode child) {
        StringBuilder sb = new StringBuilder();

        String text = child.getText();
        text = text.replaceAll("\\\\", "\\\\\\\\");
        text = text.replaceAll("\\{", "\\\\{");
        sb.append(text);

        return sb.toString();
    }
}
