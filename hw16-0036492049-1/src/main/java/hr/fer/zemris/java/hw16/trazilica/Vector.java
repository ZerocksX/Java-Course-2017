package hr.fer.zemris.java.hw16.trazilica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a vector with some data<br/>
 * User can acces data with indexes or words representing some data
 *
 * @author Pavao Jerebić
 */
public class Vector {
    /**
     * data
     */
    private List<Double> vector;
    /**
     * mapp of words with indexes of their values
     */
    private Map<String, Integer> wordPosition;

    /**
     * Basic constructor
     */
    public Vector() {
        this(new HashMap<>());
    }

    /**
     * Initializes vector from given dataset
     *
     * @param vocabulary dataset
     */
    public Vector(Map<String, Integer> vocabulary) {
        vector = new ArrayList<>(vocabulary.size());
        wordPosition = new HashMap<>();
        int[] n = new int[1];
        vocabulary.forEach((word, count) -> {
            wordPosition.put(word.toLowerCase(), n[0]);
            vector.add(count.doubleValue());
            n[0]++;
        });
    }

    /**
     * Calculates cosine angle between two vectors
     *
     * @param v1         vector 1
     * @param v2         vector 2
     * @param vocabulary vocabulary for proper index multiplication
     * @return cosine angle between two given vectors
     */
    public static double cosAngle(Vector v1, Vector v2, Map<String, Integer> vocabulary) {
        double angle;
        final double[] product = {0};
        vocabulary.forEach((word, count) -> {
            if (v2.get(word.toLowerCase()) > 12 && v1.get(word.toLowerCase()) > 0) {
                System.out.println(word + " " + v2.get(word.toLowerCase()));
            }
            product[0] += v1.get(word.toLowerCase()) * v2.get(word.toLowerCase());
        });
        angle = product[0] / (v1.norm() * v2.norm());
        return angle;
    }

    /**
     * Norm of the vector
     *
     * @return norm
     */
    private double norm() {
        double sum = 0;
        for (int i = 0, n = size(); i < n; i++) {
            sum += Math.pow(vector.get(i), 2);
        }
        return Math.sqrt(sum);
    }


    /**
     * size
     *
     * @return size
     */
    public int size() {
        return vector.size();
    }

    /**
     * Getter for data at index representing the given word
     *
     * @param word word
     * @return data at index representing word
     */
    public Double get(String word) {
        Integer index = wordPosition.get(word.toLowerCase());
        if (index == null) {
            return 0.0;
        }
        return vector.get(index);
    }

    /**
     * Adds new word with its data to the end of the vector
     *
     * @param word  word
     * @param count count
     */
    public void add(String word, double count) {
        Integer index = wordPosition.get(word.toLowerCase());
        if (index == null) {
            wordPosition.put(word.toLowerCase(), this.size());
            vector.add(count);
        } else {
            vector.set(index, vector.get(index) + count);
        }
    }

    /**
     * Creates a new vector representing TFIDF values
     *
     * @param idfValues  precalulated idf values
     * @param vocabulary vocabulary
     * @return vector representing tfidf values
     */
    public Vector calculateTFIDF(Map<String, Double> idfValues, Map<String, Integer> vocabulary) {
        Vector result = new Vector();
        vocabulary.forEach((word, count) -> result.add(
                word.toLowerCase(), this.get(word.toLowerCase()) * idfValues.get(word)
        ));
        return result;
    }
}
