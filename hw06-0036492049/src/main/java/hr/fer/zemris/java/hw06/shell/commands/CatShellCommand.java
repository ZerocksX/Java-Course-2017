package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This command opens given file and writes its content to console.<br/>
 * Use: write cat and then follows 1 or 2 arguments.<br/>
 * Arguments: First argument is always path to a file. Second argument is charset. By default this value is platform default charset
 *
 * @author Pavao Jerebić
 */
public class CatShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] argumentsArray = null;
        try {
            argumentsArray = Util.split(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        String path = null;
        Charset charset = Charset.defaultCharset();

        if (argumentsArray.length == 1) {
            path = argumentsArray[0];
        } else if (argumentsArray.length == 2) {
            path = argumentsArray[0];
            try {
                charset = Charset.forName(argumentsArray[1]);
            } catch (IllegalCharsetNameException | UnsupportedCharsetException ex) {
                env.writeln("Illegal/Unsupported charset name : " + ex.getMessage());
                return ShellStatus.CONTINUE;
            } catch (IllegalArgumentException ex) {
                env.writeln(ex.getMessage());
                return ShellStatus.CONTINUE;
            }
        } else {
            env.writeln("Invalid number of arguments");
            return ShellStatus.CONTINUE;
        }

        try {
//            Files.readAllLines(Paths.get(path),charset).forEach(env::writeln);
            BufferedReader br = Files.newBufferedReader(Paths.get(path), charset);
            br.lines().forEach(env::writeln);
            br.close();
        } catch (IOException | UncheckedIOException ex) {
            env.writeln("Failed to read file. Message was: " + ex.getMessage());
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList("This command opens given file and writes its content to console.",
                "Use: write cat and then follows 1 or 2 arguments.",
                "Arguments: First argument is always path to a file. Second argument is charset. By default this value is platform default charset");
    }
}
