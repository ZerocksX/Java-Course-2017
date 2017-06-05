package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.environments.DefaultShellEnvironment;

import java.util.Scanner;

/**
 * Program that has basic shell functionalities<br/>
 * Its' commands are {@link hr.fer.zemris.java.hw06.shell.commands.CatShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.LsShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand}, {@link hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand} and exit<br/>
 * All of those commands' are described in their {@link ShellCommand#getCommandDescription()} with exception of exit<br/>
 * Exit will terminate shell<br/>
 * In this implementation all IO operations are on standard input and output<br/>
 * <br/>
 * Program starts by greeting the user then it reads input<br/>
 * This program supports multiline commands by adding MORELINE symbol at the end of the line(but separated from command or arguments with a white space)<br/>
 * When user enters multiline state it will read input with MULTILINE prompt<br/>
 * When entering arguments with quotation marks escaping is allowed<br/>
 * Escaping sequences that are allowed are \" and \\<br/>
 *
 * @author Pavao Jerebić
 */
public class MyShell {
    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        Scanner sc  = new Scanner(System.in);
        Environment environment = new DefaultShellEnvironment(sc);
        ShellStatus shellStatus = ShellStatus.CONTINUE;

        environment.writeln("Welcome to MyShell v 1.0");

        while (shellStatus != ShellStatus.TERMINATE) {
            environment.write(String.valueOf(environment.getPromptSymbol()) + " ");
            String line = null;
            try {
                line = environment.readLine();
            } catch (ShellIOException e) {
                break;
            }

            String[] lineArray = splitLine(line, environment);

            ShellCommand command = environment.commands().get(lineArray[0]);
            if (command == null) {
                environment.writeln("Not a valid command: " + lineArray[0]);
                continue;
            }

            try {
                    shellStatus = command.executeCommand(environment, lineArray[1]);
            } catch (ShellIOException e) {
                shellStatus = ShellStatus.TERMINATE;
            }
        }
        sc.close();
    }

    /**
     * Helping method that splits line into command and argument. If command ended with MORELINES symbol then it will read until there are no more MORELINES symbols
     *
     * @param line        line
     * @param environment shell environment
     * @return array of strings where the first is command name and second is arguments separated with space
     */
    private static String[] splitLine(String line, Environment environment) {

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < line.length() && !Character.isWhitespace(line.charAt(i))) {
            sb.append(line.charAt(i++));
        }
        i++;

        String arguments = i >= line.length() ? "" : line.substring(i);
        StringBuilder argumentSB = new StringBuilder();
        arguments = arguments.trim();
        argumentSB.append(arguments);
        if (arguments.endsWith(String.valueOf(environment.getMorelinesSymbol()))) {
            if (arguments.length() == 1 || Character.isWhitespace(arguments.charAt(arguments.length() - 2))) {
                do {
                    argumentSB = new StringBuilder(argumentSB.subSequence(0, argumentSB.length() - 1));
                    environment.write(String.valueOf(environment.getMultilineSymbol()) + " ");
                    argumentSB.append(environment.readLine().trim());
                } while (argumentSB.toString().endsWith(" " + String.valueOf(environment.getMorelinesSymbol())));
            }

        }
        return new String[]{
                sb.toString(),
                argumentSB.toString().trim()
        };
    }
}
