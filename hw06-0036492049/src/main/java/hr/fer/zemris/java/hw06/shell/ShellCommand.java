package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Class representing a shell command
 *
 * @author Pavao Jerebić
 */
public interface ShellCommand {
    /**
     * Method that executes the command
     *
     * @param env       given environment
     * @param arguments arguments
     * @return ShellStatus enum, CONTINUE if shell can continue with it's work, TERMINATE if shell can not continue
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Getter for command name
     *
     * @return command name
     */
    String getCommandName();

    /**
     * Returns list of strings representing command description
     *
     * @return description
     */
    List<String> getCommandDescription();
}
