package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * The hexdump command expects a single argument: file name, and produces hex-output<br/>
 * Use: write hexdump filepath<br/>
 * Arguments: exactly 1 argument, path to a FILE
 *
 * @author Pavao Jerebić
 */
public class HexdumpShellCommand implements ShellCommand {

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
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path.toString()));
            byte[] buffer = new byte[16];
            long counter = 0;
            while (true) {
                int bytes = bis.read(buffer);
                if (bytes <= 0) {
                    break;
                }
                String data = processData(buffer, bytes);
                env.writeln(String.format("%s: %s|%s | %s",
                        Util.longtohex(counter),
                        prettyPrint(buffer, bytes, 0, 8),
                        prettyPrint(buffer, bytes, 8, 16),
                        data));
                counter += 16;
            }
            bis.close();
        } catch (IOException e) {
            env.writeln("Failed to read file: " + path.toString() + ". Message was: " + e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Formats given byte array
     *
     * @param buffer byte array
     * @param bytes  number of bytes in array
     * @param l      left bound
     * @param r      right bound
     * @return formatted byte array as string
     */
    private String prettyPrint(byte[] buffer, int bytes, int l, int r) {
        StringBuilder sb = new StringBuilder();
        sb.append((Util.bytetohex(Arrays.copyOfRange(buffer, l, Math.max(l, Math.min(bytes, r))))));
        for (int i = Math.max(bytes - l, 0); i <= r - l + 1; i++) {
            sb.append("   ");
        }
        if (sb.length() >= 8 * 2 + 7) sb = new StringBuilder(sb.subSequence(0, 8 * 2 + 7));
        return sb.toString();
    }

    /**
     * Transforms bytes in byte array into string using ascii table, if byte is < 32 or > 127 then it is processed as '.'
     *
     * @param buffer byte array
     * @param bytes  number of bytes
     * @return transformed bytes
     */
    private String processData(byte[] buffer, int bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes; i++) {
            byte b = buffer[i];
            if (b < 32 || b > 127) {
                sb.append(".");
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                " the hexdump command expects a single argument: file name, and produces hex-output",
                "Use: write hexdump filepath",
                "Arguments: exactly 1 argument, path to a FILE"
        );
    }
}
