package hr.fer.zemris.java.hw04.db;

/**
 * Getter for {@link StudentRecord} field
 * @author Pavao Jerebić
 */
public interface IFieldValueGetter {
    /**
     * Returns value of the field
     * @param record given record
     * @return value of the field
     */
    public String get(StudentRecord record);
}
