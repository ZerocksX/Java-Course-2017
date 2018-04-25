package hr.fer.zemris.java.hw16.trazilica;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Parses given strings from the given buffered reader and creates a vocabulary with word count<br/>
 * Accepts set of words to be discarded
 *
 * @author Pavao Jerebić
 */
public class VocabularyParser {
    /**
     * buffered reader
     */
    private BufferedReader bufferedReader;
    /**
     * Vocabulary
     */
    private Map<String, Integer> vocabulary;
    /**
     * Stop words
     */
    private Set<String> stopWords;

    /**
     * Constructor that parses data from buffered reader
     *
     * @param bufferedReader buffered reader
     * @param stopWords      stop words
     * @throws IllegalArgumentException if given input is invalid
     */
    public VocabularyParser(BufferedReader bufferedReader, Set<String> stopWords) {
        this.bufferedReader = bufferedReader;
        this.stopWords = stopWords;
        this.vocabulary = new HashMap<>();
        try {
            parse();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Helping method used to parse data from buffered reader
     *
     * @throws IOException if reading fails
     */
    private void parse() throws IOException {
        BufferedReader br = bufferedReader;
        char[] buffer = new char[1024];
        StringBuilder sb = new StringBuilder();
        int n;
        while ((n = br.read(buffer)) >= 0) {
            for (int i = 0; i < n; i++) {
                if (Character.isLetter(buffer[i])) {
                    sb.append(buffer[i]);
                } else {
                    String word = sb.toString().trim();
                    if (!word.isEmpty() && !stopWords.contains(word.toLowerCase())) {
                        vocabulary.merge(word.toLowerCase(), 1, (oldValue, newValue) -> oldValue + 1);
                    }
                    sb = new StringBuilder();
                }
            }
        }
        String word = sb.toString().trim();
        if (!word.isEmpty() && !stopWords.contains(word.toLowerCase())) {
            vocabulary.merge(word.toLowerCase(), 1, (oldValue, newValue) -> oldValue + 1);
        }
    }

    /**
     * Getter for vocabulary
     *
     * @return vocabulary
     */
    public Map<String, Integer> getVocabulary() {
        return vocabulary;
    }

}
