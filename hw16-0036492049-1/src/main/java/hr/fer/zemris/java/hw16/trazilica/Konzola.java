package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Simple application that reades files from the given path and processes given queries to calculate some data<br/>
 * queries are:<br/>
 * query followed by set of words with which similarity will be calculated and files that are the most similar to the given<br/>
 * text will be displayed<br/>
 * results displays previous results, if any exist<br/>
 * type followed by index of previous result set displays the given file in console<br/>
 * exit terminates the application
 *
 * @author Pavao Jerebić
 */
public class Konzola {
    /**
     * delta
     */
    private static final double DELTA = 0.000001;
    /**
     * words
     */
    private static Set<String> stopWords;
    /**
     * vocabulary
     */
    private static Map<String, Integer> vocabulary;
    /**
     * frequency vectors
     */
    private static Map<Path, Vector> frequencyVectors;
    /**
     * idf values
     */
    private static Map<String, Double> idfValues;
    /**
     * idf vectors
     */
    private static Map<Path, Vector> idfVectors;

    /**
     * Starting method
     *
     * @param args path to the documents folder
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Očekivao sam jedan argument, put do direktorija s datotekama");
            return;
        }
        Path documents = Paths.get(args[0]);
        if (!Files.isDirectory(documents)) {
            System.out.println("Predani put(" + args[0] + ") nije put do direktorija");
            return;
        }

        if (!initializeStopWords(documents)) {
            return;
        }

        if (!initializeVocabularyAndIDFValues(documents)) {
            return;
        }


        Scanner sc = new Scanner(System.in);
        Set<DocumentSimilarity> queryResult = new TreeSet<>();
        while (true) {
            System.out.print("Unesi naredbu > ");
            String line = sc.nextLine().trim().toLowerCase();
            if (line.equals("exit")) {
                break;
            }
            if (line.startsWith("query")) {
                line = line.substring(line.indexOf("query") + 5).trim();
                queryResult = processQuery(line);
                printQueryResult(queryResult);

            } else if (line.startsWith("results")) {
                printQueryResult(queryResult);

            } else if (line.startsWith("type")) {
                line = line.substring(line.indexOf("type") + 4).trim();
                processType(line, queryResult);

            } else {
                System.out.println("Nepoznata naredba.");
            }
        }
        sc.close();
    }

    /**
     * Processes type query
     *
     * @param line        line
     * @param queryResult query result
     */
    private static void processType(String line, Set<DocumentSimilarity> queryResult) {
        Integer index;
        try {
            index = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Nije ispravno predan broj");
            return;
        }
        int i = 0;
        for (DocumentSimilarity ds : queryResult) {
            if (i == index) {
                try {
                    System.out.println("Dokument: " + ds.getDocument().toString());
                    System.out.println(new String(Files.readAllBytes(ds.getDocument()), StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.out.println("Neuspješno pročitana datoteka: " + ds.toString());
                }
                break;
            }
            i++;
        }
    }

    /**
     * Processes query 'query'
     *
     * @param line line of words
     * @return query result, sorted set of {@link DocumentSimilarity} entities
     */
    private static Set<DocumentSimilarity> processQuery(String line) {
        Set<DocumentSimilarity> queryResult;

        VocabularyParser parser = new VocabularyParser(
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(
                                        line.getBytes()
                                )
                        )
                ),
                stopWords
        );
        Vector testDoc = new Vector(parser.getVocabulary());
        Vector testIdf = testDoc.calculateTFIDF(idfValues, vocabulary);
        queryResult = new TreeSet<>();

        frequencyVectors.forEach((path, vector) -> {
            Double angle = Vector.cosAngle(testIdf, idfVectors.get(path), vocabulary);
            if (!Double.isNaN(angle) && angle > DELTA) {
                queryResult.add(new DocumentSimilarity(path, angle));
            }

        });

        return queryResult;
    }

    /**
     * Prints query result
     *
     * @param queryResult query result
     */
    private static void printQueryResult(Set<DocumentSimilarity> queryResult) {
        int i = 0;
        System.out.println("Najboljih 10 rezultata:");
        for (DocumentSimilarity ds : queryResult) {
            System.out.printf("[%d] (%f) %s%n", i++, ds.getSimilarity(), ds.getDocument());
            if (i >= 10) {
                break;
            }
        }
    }

    /**
     * Initializes vocabulary and idf values
     *
     * @param documents documents path
     * @return true if success
     */
    private static boolean initializeVocabularyAndIDFValues(Path documents) {
        VocabularyFileVisitor visitor = new VocabularyFileVisitor(stopWords);
        try {
            Files.walkFileTree(documents, visitor);
        } catch (IOException e) {
            System.out.println("Nisam uspio pročitati datoteke u zadanom direktoriju");
            return false;
        }


        vocabulary = visitor.getVocabulary();
        frequencyVectors = visitor.getFrequencyVectors();
        idfValues = new HashMap<>();
        for (String word : vocabulary.keySet()) {
            final double[] nd = {0};
            frequencyVectors.forEach((path, vector) -> {
                if (vector.get(word) > 0) {
                    nd[0]++;
                }
            });
            idfValues.put(word, Math.log(visitor.getDocumentCount() / nd[0]));
        }

        idfVectors = new HashMap<>();
        frequencyVectors.forEach((path, vector) -> idfVectors.put(path, vector.calculateTFIDF(idfValues, vocabulary)));
        System.out.printf("Veličina riječnika je %d riječi.%n", vocabulary.size());
        return true;
    }

    /**
     * Initializes stop words
     *
     * @param documents document path
     * @return true if success
     */
    private static boolean initializeStopWords(Path documents) {

        Path stopWordPath = Paths.get(documents.toAbsolutePath().toString(), "hrvatski_stoprijeci.txt");
        stopWords = new HashSet<>();
        VocabularyParser parser;
        try {
            parser = new VocabularyParser(Files.newBufferedReader(stopWordPath, StandardCharsets.UTF_8), new HashSet<>());
        } catch (IOException e) {
            System.out.println("Očekivao sam datoteku hrvatski_stoprijeci.txt u zadanom direktoriju, ali je nisam uspio pročitati");
            return false;
        }

        parser.getVocabulary().keySet().forEach(word -> stopWords.add(word.toLowerCase()));
        return true;
    }

}
