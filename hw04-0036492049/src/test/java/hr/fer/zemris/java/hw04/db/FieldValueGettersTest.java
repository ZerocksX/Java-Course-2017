package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class FieldValueGettersTest {

    @Test
    public void testAllGetters() throws Exception {
        StudentRecord record = new StudentRecord("0036492049","Ime","Prezime",5);
        assertEquals("Ime",FieldValueGetters.FIRST_NAME.get(record));
        assertEquals("Prezime",FieldValueGetters.LAST_NAME.get(record));
        assertEquals("0036492049", FieldValueGetters.JMBAG.get(record));
    }
}