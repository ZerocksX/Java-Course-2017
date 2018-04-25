package hr.fer.zemris.java.hw16.trazilica;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * File visitor that initializes vocabulary and frequency vectors with a given set of stop words
 */
public class VocabularyFileVisitor implements FileVisitor<Path> {

    /**
     * document count
     */
    private long documentCount;
    /**
     * stop words
     */
    private Set<String> stopWords;
    /**
     * vocabulary
     */
    private Map<String, Integer> vocabulary;
    /**
     * frequency vectors
     */
    private Map<Path, Vector> frequencyVectors;

    /**
     * basic constructor
     *
     * @param stopWords stop words set
     */
    public VocabularyFileVisitor(Set<String> stopWords) {
        this.stopWords = stopWords;
        this.vocabulary = new HashMap<>();
        frequencyVectors = new HashMap<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {

        VocabularyParser parser = new VocabularyParser(Files.newBufferedReader(path, StandardCharsets.UTF_8), stopWords);
        documentCount++;
        parser.getVocabulary().forEach((word, count) ->
                vocabulary.merge(word, count, (oldValue, newValue) -> oldValue + newValue)
        );

        Vector vector = new Vector(parser.getVocabulary());

        frequencyVectors.put(path, vector);


        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * document count without stop words file
     *
     * @return document count
     */
    public long getDocumentCount() {
        return documentCount - 1;
    }

    /**
     * Getter for vocabulary
     *
     * @return vocabulary
     */
    public Map<String, Integer> getVocabulary() {
        return vocabulary;
    }

    /**
     * Getter for frequency vectors
     *
     * @return frequency vectors
     */
    public Map<Path, Vector> getFrequencyVectors() {
        return frequencyVectors;
    }
}