package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.<br/>
 * Use: write mkdir directoryPath<br/>
 * Arguments: exactly one argument, directory path. Can create whole structure
 *
 * @author Pavao Jerebić
 */
public class MkdirShellCommand implements ShellCommand {

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

        Path path = Paths.get(argumentsArray[0]);
        try {
            if (Files.exists(Files.createDirectories(path))) {
                env.writeln("Successfully create directiories " + path.toString());
            } else {
                env.writeln("Could not create directiories " + path.toString());
            }
        } catch (IOException e) {
            env.writeln("Could not create directiories " + path.toString() + ". Message was: " + e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.",
                "Use: write mkdir directoryPath",
                "Arguments: exactly one argument, directory path. Can create whole structure"
        );
    }
}
