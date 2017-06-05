package hr.fer.zemris.java.hw06.shell.environments;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.commands.*;

import java.util.*;

/**
 * Default implementation of Environment
 *
 * @author Pavao Jerebić
 */
public class DefaultShellEnvironment implements Environment {


    /**
     * Scanner used to read lines
     */
    private Scanner scanner;
    /**
     * Map of commands
     */
    private SortedMap<String, ShellCommand> commands;
    /**
     * PROMPT symbol
     */
    private Character promptSymbol;
    /**
     * MORELINES symbol
     */
    private Character moreLinesSymbol;
    /**
     * MULTILINES symbol
     */
    private Character multiLinesSymbol;

    /**
     * Default constructor that sets all fields<br/>
     * Commands map is initialized with cat, charsets, copy, exit, help, hexdump, ls, mkdir, symbol and tree commands <br/>
     * PROMPT is '>'<br/>
     * MORELINES is '\'<br/>
     * MULTILINES is '|'
     * @param sc Scanner used to read lines
     */
    public DefaultShellEnvironment(Scanner sc) {
        scanner = sc;
        commands = new TreeMap<>();
        commands.put("cat", new CatShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("symbol", new SymbolShellCommand());
        commands.put("tree", new TreeShellCommand());
        promptSymbol = '>';
        moreLinesSymbol = '\\';
        multiLinesSymbol = '|';
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public Character getMultilineSymbol() {
        return multiLinesSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol can not be null");
        }
        multiLinesSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol can not be null");
        }
        promptSymbol = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return moreLinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol can not be null");
        }
        moreLinesSymbol = symbol;
    }
}
