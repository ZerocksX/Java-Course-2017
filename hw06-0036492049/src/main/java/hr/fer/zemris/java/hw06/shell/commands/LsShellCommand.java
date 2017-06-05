package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Command ls takes a single argument – directory – and writes a directory listing<br/>
 * Use: write ls path<br/>
 * Argument: exacty one argument, a directory
 *
 * @author Pavao Jerebić
 */
public class LsShellCommand implements ShellCommand {

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

        if (Files.isDirectory(path)) {
            try {
                Files.list(path).forEach(p -> {
                    BasicFileAttributeView faView = Files.getFileAttributeView(
                            p,
                            BasicFileAttributeView.class,
                            LinkOption.NOFOLLOW_LINKS);
                    try {
                        StringBuilder sb = new StringBuilder();
                        BasicFileAttributes attributes = faView.readAttributes();
                        sb.append(Files.isDirectory(p) ? "d" : "-")
                                .append(Files.isReadable(p) ? "r" : "-")
                                .append(Files.isWritable(p) ? "w" : "-")
                                .append(Files.isExecutable(p) ? "x" : "-")
                                .append(String.format(" %10d", attributes.size()))
                                .append(new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ").format(new Date(attributes.creationTime().toMillis())))
                                .append(p.getFileName().toString());
                        env.writeln(sb.toString());
                    } catch (IOException e) {
                        env.writeln("Could not read attributes of: " + p.toString());
                    }

                });
            } catch (IOException e) {
                env.writeln("Failed to list directory. " + e.getMessage());
                return ShellStatus.CONTINUE;
            }
        } else {
            env.writeln("Given path " + path + " is not a directory");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "Command ls takes a single argument – directory – and writes a directory listing",
                "Use: write ls path",
                "Argument: exacty one argument, a directory"
        );
    }
}
