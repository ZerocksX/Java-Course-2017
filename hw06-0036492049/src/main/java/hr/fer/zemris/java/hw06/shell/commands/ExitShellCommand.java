package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

/**
 * This commands terminates shell<br/>
 * Use: write exit<br/>
 * Arguments: ignored
 *
 * @author Pavao Jerebić
 */
public class ExitShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList("This commands terminates shell", "Use: write exit", "Arguments: ignored");
    }
}
