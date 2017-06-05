package hr.fer.zemris.java.hw06.shell.commands;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;


/**
 * @author Pavao Jerebić
 */
public class UtilTest {
    @Test
    public void split() throws Exception {
        String st = "\"test/ime ime\" /nesto/nanana";
        String[] arguments = Util.split(st);
        assertArrayEquals(new String[]{"test/ime ime", "/nesto/nanana"}, arguments);
    }

}