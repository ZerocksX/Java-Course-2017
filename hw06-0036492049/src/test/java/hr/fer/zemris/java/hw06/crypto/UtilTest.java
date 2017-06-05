package hr.fer.zemris.java.hw06.crypto;

import org.junit.Test;

import static hr.fer.zemris.java.hw06.crypto.Util.bytetohex;
import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class UtilTest {

    @Test
    public void hextobyteOne() throws Exception {
        assertArrayEquals(new byte[]{1}, hextobyte("01"));
    }

    @Test
    public void hextobyteZeroes() throws Exception {
        assertArrayEquals(new byte[]{0,0}, hextobyte("0000"));
    }


    @Test
    public void hextobyteNegative() throws Exception {
        assertArrayEquals(new byte[]{-1}, hextobyte("ff"));
    }


    @Test
    public void hextobyteMultiplePositive() throws Exception {
        assertArrayEquals(new byte[]{17, 34}, hextobyte("1122"));
    }


    @Test
    public void hextobyteMultiple() throws Exception {
        assertArrayEquals(new byte[]{1, -82, 34}, hextobyte("01aE22"));
    }

    @Test
    public void bytetohexOne() throws Exception {
        assertEquals("01", bytetohex(new byte[]{1}));
    }

    @Test
    public void bytetohexNegativeOne() throws Exception {
        assertEquals("ff", bytetohex(new byte[]{-1}));
    }

    @Test
    public void bytetohexMultiplePositive() throws Exception {
        assertEquals("1122", bytetohex(new byte[]{17, 34}));
    }

    @Test
    public void bytetohexMultiple() throws Exception {
        assertEquals("01ae22", bytetohex(new byte[]{1, -82, 34}));
    }

}