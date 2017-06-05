package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/**
 * The tree command expects a single argument: directory name and prints a tree<br/>
 * Use: write tree path<br/>
 * Arguments: exactly one argument, path to a directory
 *
 * @author Pavao Jerebić
 */
public class TreeShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] argumentsArray = null;
        try {
            argumentsArray = Util.split(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (argumentsArray.length != 1) {
            env.writeln("Invalid number of arguments");
            return ShellStatus.CONTINUE;
        }
        try {
            Files.walkFileTree(Paths.get(argumentsArray[0]), new MyFileVisitor(env));
        } catch (IOException e) {
            env.writeln("Failed walking tree from " + arguments + ". Message was: " + e.getMessage());
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "The tree command expects a single argument: directory name and prints a tree",
                "Use: write tree path",
                "Arguments: exactly one argument, path to a directory"
        );
    }

    /**
     * File visitor used to output file tree
     */
    private class MyFileVisitor implements FileVisitor<Path> {
        /**
         * Current depth
         */
        private int depth;
        /**
         * Environment
         */
        private Environment env;

        /**
         * Constructor that sets environment
         *
         * @param env environment
         */
        public MyFileVisitor(Environment env) {
            depth = 0;
            this.env = env;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {

            env.writeln(formatOutput(path));
            depth++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            env.writeln(formatOutput(path));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
            if (e == null) {
                depth--;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        /**
         * Formats the output
         *
         * @param path current file
         * @return formatted output
         */
        private String formatOutput(Path path) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depth; ++i) {
                sb.append("  ");
            }
            return String.format("%s%s", sb.toString(), path.getFileName() == null ? "" : path.getFileName().toString());
        }
    }
}
