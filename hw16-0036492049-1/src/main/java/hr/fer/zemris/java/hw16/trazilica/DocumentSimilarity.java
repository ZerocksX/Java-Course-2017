package hr.fer.zemris.java.hw16.trazilica;

import java.nio.file.Path;

/**
 * Class containing path to the document and a double value
 */
public class DocumentSimilarity implements Comparable<DocumentSimilarity> {
    /**
     * Document path
     */
    private Path document;
    /**
     * Similarity to another document
     */
    private Double similarity;
    /**
     * delta
     */
    private static final double DELTA = 0.00001;

    /**
     * Basic constructor
     *
     * @param document   document
     * @param similarity similarity
     */
    public DocumentSimilarity(Path document, Double similarity) {
        this.document = document;
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentSimilarity)) return false;

        DocumentSimilarity that = (DocumentSimilarity) o;

        if (!document.equals(that.document)) return false;
        return similarity.equals(that.similarity);
    }

    @Override
    public int hashCode() {
        int result = document.hashCode();
        result = 31 * result + similarity.hashCode();
        return result;
    }

    /**
     * Getter for document
     *
     * @return document
     */
    public Path getDocument() {
        return document;
    }

    /**
     * Getter for similarity
     *
     * @return similarity
     */
    public Double getSimilarity() {
        return similarity;
    }


    @Override
    public int compareTo(DocumentSimilarity other) {
        if (this.getSimilarity() - other.getSimilarity() > DELTA) {
            return -1;
        } else {
            return 1;
        }
    }
}