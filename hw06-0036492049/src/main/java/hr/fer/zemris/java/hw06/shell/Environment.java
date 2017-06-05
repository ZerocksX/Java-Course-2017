package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Shell environment
 *
 * @author Pavao Jerebić
 */
public interface Environment {
    /**
     * Reads a line
     *
     * @return a line
     * @throws ShellIOException if can not read a line
     */
    String readLine() throws ShellIOException;

    /**
     * Writes without terminating a line
     *
     * @param text text to write
     * @throws ShellIOException if can not write
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes a line then terminates it entering a new line
     *
     * @param text text to write
     * @throws ShellIOException if can not write
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns a non changeable sorted map of all commands
     *
     * @return non changeable sorted map of all commands
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns MULTILINE symbol
     *
     * @return MULTILINE symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets MULTILINE symbol
     *
     * @param symbol MULTILINE symbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns PROMPT symbol
     *
     * @return PROMPT symbol
     */
    Character getPromptSymbol();

    /**
     * Sets PROMPT symbol
     *
     * @param symbol new PROMPT symbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns MORELINES symbol
     *
     * @return MORELINES symbol
     */
    Character getMorelinesSymbol();

    /**
     * Sets MORELINES symbol
     * @param symbol new MORELINES symbol
     */
    void setMorelinesSymbol(Character symbol);
}
