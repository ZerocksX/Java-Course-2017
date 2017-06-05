package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.Arrays;
import java.util.List;

/**
 * The symbol command can take either 1 or 2 arguments. First argument must be symbol name.<br/>
 * If there is a second argument then symbol with given name is changed to second argument<br/>
 * Use: write symbol symbolName [ newSymbol]<br/>
 * Arguments: First argument is symbol name in upper case, second is 1 character, new symbol
 *
 * @author Pavao Jerebić
 */
public class SymbolShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] argumentsArray = null;
        try {
            argumentsArray = Util.split(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        String newSymbol = null;
        if (argumentsArray.length > 2 || argumentsArray.length <= 0) {
            env.writeln("Wrong number of arguments");
            return ShellStatus.CONTINUE;
        } else if (argumentsArray.length == 2) {
            newSymbol = argumentsArray[1].trim();
            if (newSymbol.length() != 1) {
                env.writeln("Illegal new symbol: " + newSymbol);
                return ShellStatus.CONTINUE;
            }
        }

        String name = argumentsArray[0];
        switch (name) {
            case "PROMPT":
                if (newSymbol == null) {
                    env.writeln(String.format("Symbol for PROMPT is '%c'", env.getPromptSymbol()));
                } else {
                    env.writeln(String.format("Symbol for PROMPT changed from '%c' to '%c'", env.getPromptSymbol(), newSymbol.charAt(0)));
                    env.setPromptSymbol(newSymbol.charAt(0));
                }
                break;
            case "MORELINES":
                if (newSymbol == null) {
                    env.writeln(String.format("Symbol for MORELINES is '%c'", env.getMorelinesSymbol()));
                } else {
                    env.writeln(String.format("Symbol for MORELINES changed from '%c' to '%c'", env.getMorelinesSymbol(), newSymbol.charAt(0)));
                    env.setMorelinesSymbol(newSymbol.charAt(0));
                }
                break;
            case "MULTILINE":
                if (newSymbol == null) {
                    env.writeln(String.format("Symbol for MULTILINE is '%c'", env.getMultilineSymbol()));
                } else {
                    env.writeln(String.format("Symbol for MULTILINE changed from '%c' to '%c'", env.getMultilineSymbol(), newSymbol.charAt(0)));
                    env.setMultilineSymbol(newSymbol.charAt(0));
                }
                break;
            default:
                env.writeln("Wrong symbol name: " + name);
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "The symbol command can take either 1 or 2 arguments. First argument must be symbol name.",
                " If there is a second argument then symbol with given name is changed to second argument",
                "Use: write symbol symbolName [ newSymbol]",
                "Arguments: First argument is symbol name in upper case, second is 1 character, new symbol"
        );
    }
}
