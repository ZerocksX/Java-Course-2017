package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Command charsets takes no arguments and lists names of supported charsets for your Java platform<br/>
 * A single charset name is written per line<br/>
 * Use: write charsets<br/>
 * Arguments: ignored
 *
 * @author Pavao Jerebić
 */
public class CharsetsShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        env.writeln("Supported charsets are:");
        Charset.availableCharsets().forEach((s, charset) -> env.writeln(s));
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList("Command charsets takes no arguments and lists names of supported charsets for your Java platform",
                "A single charset name is written per line",
                "Use: write charsets",
                "Arguments: ignored");
    }
}
