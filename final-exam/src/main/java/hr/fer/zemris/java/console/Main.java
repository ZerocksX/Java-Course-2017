package hr.fer.zemris.java.console;

import hr.fer.zemris.java.console.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavao Jerebić
 */
public class Main {
    public static void main(String[] args) throws IOException {


        Path root = Paths.get(args[0]);
        MyVisitor visitor = new MyVisitor();
        Files.walkFileTree(root, visitor);
        System.out.println("Uspješno obavljeno");
    }

    private static class MyVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            String data = new String(Files.readAllBytes(path));
            Parser parser = new Parser(data.toCharArray());
            String input = data.substring(parser.getEndPoint());

            String[] inputArray = input.split(System.lineSeparator());
            List<String> lines = new ArrayList<>();
            for (String line : inputArray) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                lines.add(line);
            }

            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(parser.calculate(line)).append(System.lineSeparator());
            }
            String name = path.getFileName().toString();
            try{
                name = name.substring(0, name.indexOf("-in.txt")) + "-out.txt";
            }catch (Exception e){
                return FileVisitResult.CONTINUE;
            }
            Path filePath = Paths.get(path.toAbsolutePath().toString().substring(0, path.toAbsolutePath().toString().lastIndexOf(File.separator)), name);
            Files.write(
                    filePath,
                    sb.toString().getBytes()
            );
            return FileVisitResult.CONTINUE;
        }
    }
}
