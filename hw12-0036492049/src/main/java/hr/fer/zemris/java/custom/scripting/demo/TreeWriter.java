package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demo program that takes 1 argument, a path to a SmartScript file(UTF-8)<br/>
 * It parses given file and outputs an approximate version of it to standard output
 *
 * @author Pavao Jerebić
 */
public class TreeWriter {
    /**
     * Starting method
     *
     * @param args path to the file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments");
            return;
        }

        Path path = Paths.get(args[0]);
        String docBody;
        try {
            docBody = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error while reading the file");
            return;
        }

        SmartScriptParser p;
        try {
            p = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Parsing failed. " + e.getMessage());
            return;
        }
        //p.printTree();

        WriterVisitor visitor = new WriterVisitor();
        p.getDocumentNode().accept(visitor);


    }

    /**
     * Visitor that outputs given {@link DocumentNode} and its children as an approximate version of the original file
     */
    public static class WriterVisitor implements INodeVisitor {
        @Override
        public void visitTextNode(TextNode node) {
            System.out.print(node.toString());
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            System.out.print(node.toString());
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
            System.out.print("{$ END $}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            System.out.print(node.toString());
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            System.out.print(node.toString());
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }

    }
}
