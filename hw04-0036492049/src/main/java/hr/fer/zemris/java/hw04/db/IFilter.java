package hr.fer.zemris.java.hw04.db;

/**
 * Filter for {@link StudentRecord}
 *
 * @author Pavao Jerebić
 */
public interface IFilter {
    /**
     * Returns true if conditions are true for given record, false otherwise
     *
     * @param record student record
     * @return true if conditions are true
     */
    public boolean accepts(StudentRecord record);
}
