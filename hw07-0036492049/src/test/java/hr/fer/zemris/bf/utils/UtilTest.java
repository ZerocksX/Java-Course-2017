package hr.fer.zemris.bf.utils;

import org.junit.Test;

import static hr.fer.zemris.bf.utils.Util.indexToByteArray;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Pavao Jerebić
 */
public class UtilTest {

    @Test
    public void indexToByteArrayTest00() throws Exception {
        assertArrayEquals(indexToByteArray(3, 2), new byte[]{1, 1});
    }

    @Test
    public void indexToByteArrayTest01() throws Exception {
        assertArrayEquals(indexToByteArray(3, 4), new byte[]{0, 0, 1, 1});
    }

    @Test
    public void indexToByteArrayTest02() throws Exception {
        assertArrayEquals(indexToByteArray(3, 6), new byte[]{0, 0, 0, 0, 1, 1});
    }


    @Test
    public void indexToByteArrayTest03() throws Exception {
        assertArrayEquals(indexToByteArray(-2, 32), new byte[]{
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
        });
    }

    @Test
    public void indexToByteArrayTest04() throws Exception {
        assertArrayEquals(indexToByteArray(-2, 16), new byte[]{
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
        });
    }

    @Test
    public void indexToByteArrayTest05() throws Exception {
        assertArrayEquals(indexToByteArray(19, 4), new byte[]{0, 0, 1, 1});
    }
}
