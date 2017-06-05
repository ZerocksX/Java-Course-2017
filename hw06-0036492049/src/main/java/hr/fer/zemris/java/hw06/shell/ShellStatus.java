package hr.fer.zemris.java.hw06.shell;

/**
 * Shell status enum, result of {@link ShellCommand#executeCommand(Environment, String)}
 *
 * @author Pavao Jerebić
 */
public enum ShellStatus {
    /**
     * Shell can continue with its' work
     */
    CONTINUE,
    /**
     * Shell can not continue with its' work
     */
    TERMINATE
}
