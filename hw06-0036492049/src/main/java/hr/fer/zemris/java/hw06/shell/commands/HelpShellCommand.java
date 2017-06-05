package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

/**
 * If started with no arguments, it will list names of all supported commands.<br/>
 * If started with single argument, it will print name and the description of selected command <br/>
 * Use: write help [commandName]<br/>
 * Arguments: zero or one. If given it must be a valid command name
 *
 * @author Pavao Jerebić
 */
public class HelpShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String[] argumentsArray = null;
        try {
            argumentsArray = Util.split(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if (argumentsArray.length == 0) {
            env.commands().forEach((s, shellCommand) ->
                    env.writeln(shellCommand.getCommandName())
            );
        } else if (argumentsArray.length == 1) {
            ShellCommand command = env.commands().get(argumentsArray[0]);
            if (command == null) {
                env.writeln("Given command " + argumentsArray[0] + " does not exist");
            } else {
                command.getCommandDescription().forEach(env::writeln);
            }

        } else {
            env.writeln("Invalid number of arguments");
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "If started with no arguments, it will list names of all supported commands.",
                "If started with single argument, it will print name and the description of selected command ",
                "Use: write help [commandName]",
                "Arguments: zero or one. If given it must be a valid command name"
        );
    }
}
