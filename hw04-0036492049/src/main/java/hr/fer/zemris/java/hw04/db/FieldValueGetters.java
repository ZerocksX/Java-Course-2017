package hr.fer.zemris.java.hw04.db;

/**
 * Set of {@link IFieldValueGetter} constants
 * @author Pavao Jerebić
 */
public class FieldValueGetters {
    /**
     * {@link StudentRecord#firstName} {@link IFieldValueGetter}
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
    /**
     * {@link StudentRecord#lastName} {@link IFieldValueGetter}
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
    /**
     * {@link StudentRecord#jmbag} {@link IFieldValueGetter}
     */
    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
