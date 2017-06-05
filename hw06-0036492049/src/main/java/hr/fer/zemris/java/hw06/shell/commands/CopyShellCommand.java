package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * The copy command expects two arguments: source file name and destination file name (i.e. paths and names)<br/>
 * Use: write copy sourcePath destinationPath<br/>
 * Arguments: exactly 2 arguments. First is FILE path. Second can be directory or file path<br/>
 * If second argument is directory then file name is same as source
 * @author Pavao Jerebić
 */
public class CopyShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] argumentsArray = null;
        try{
            argumentsArray =  Util.split(arguments);
        }catch (IllegalArgumentException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if (argumentsArray.length != 2) {
            env.writeln("Invalid number of arguments");
            return ShellStatus.CONTINUE;
        }

        Path src = Paths.get(argumentsArray[0]);
        Path dst = Paths.get(argumentsArray[1]);

        if (Files.isDirectory(src)) {
            env.writeln("First argument must be a file");
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(dst)) {
            dst = (new File(dst.toFile(), src.getFileName().toString())).toPath();
        }

        if(src.equals(dst)){
            env.writeln("Source path and destination path are same. Cancelling...");
            return ShellStatus.CONTINUE;
        }

        if (Files.exists(dst)) {
            env.writeln("File already exists. Do you want to overwrite?%n(Y/n) ");
            String answer = env.readLine();
            if (!answer.toUpperCase().equals("Y")) {
                env.writeln("Cancelling...");
                return ShellStatus.CONTINUE;
            }
        }

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src.toString()));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst.toString()));
            byte[] buffer = new byte[4096];
            while (true) {
                int bytes = bis.read(buffer);
                if (bytes < 0) {
                    break;
                }
                bos.write(buffer, 0, bytes);
            }
            bos.flush();
            bis.close();
            bos.close();
            env.writeln("Successfully copied file from " + src.toString() + " to " + dst.toString());
        } catch (IOException e) {
            env.writeln("Failed to copy file. Message was: " + e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return Arrays.asList(
                "The copy command expects two arguments: source file name and destination file name (i.e. paths and names)",
                "Use: write copy sourcePath destinationPath",
                "Arguments: exactly 2 arguments. First is FILE path. Second can be directory or file path",
                "   If second argument is directory then file name is same as source"
        );
    }
}
