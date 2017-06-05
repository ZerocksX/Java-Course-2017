package hr.fer.zemris.bf.qmc;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavao Jerebić
 */
public class MaskTest {

    @Test
    public void testToString01() throws Exception {
        Mask mask1 = new Mask(1, 2, false);
        assertEquals("01 .   [1]", mask1.toString());
    }

    @Test
    public void testToString02() throws Exception {
        Mask mask1 = new Mask(new byte[]{0, 2, 1, 0},
                new HashSet<Integer>() {{
                    add(6);
                    add(2);
                }},
                true);
        assertEquals("0-10 D * [2, 6]", mask1.toString());
    }

    @Test
    public void testCountOfOnes() throws Exception {
        Mask mask1 = new Mask(1, 2, false);
        assertEquals(1, mask1.countOfOnes());
    }

    @Test
    public void testGetValueAt() throws Exception {
        Mask mask1 = new Mask(1, 2, false);
        assertEquals(0, mask1.getValueAt(0));
    }

    @Test
    public void testGetIndexes() throws Exception {
        Mask mask1 = new Mask(new byte[]{0, 2, 1, 0},
                new HashSet<Integer>() {{
                    add(6);
                    add(2);
                }},
                true);
        assertEquals("[2, 6]", mask1.getIndexes().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombineWith01() throws Exception {
        Mask mask1 = new Mask(new byte[]{0, 2, 1, 0},
                new HashSet<Integer>() {{
                    add(6);
                    add(2);
                }},
                true);
        Mask mask2 = new Mask(1, 2, false);

        mask1.combineWith(mask2);
    }

    @Test
    public void testCombineWith02() throws Exception {
        Mask mask1 = new Mask(new byte[]{0, 0, 1, 0},
                new HashSet<Integer>() {{
                    add(2);
                }},
                true);
        Mask mask2 = new Mask(3, 4, false);

        Mask mask3 = mask1.combineWith(mask2).get();

        assertEquals("0010 D   [2]", mask1.toString());
        assertEquals("0011 .   [3]", mask2.toString());
        assertEquals("001- . * [2, 3]", mask3.toString());
    }

    @Test
    public void testCombineWith03() throws Exception {
        Mask mask1 = new Mask(new byte[]{0, 2, 1, 0},
                new HashSet<Integer>() {{
                    add(2);
                    add(6);
                }},
                true);
        Mask mask2 = new Mask(new byte[]{1, 2, 1, 0},
                new HashSet<Integer>() {{
                    add(10);
                    add(14);
                }},
                false);

        Mask mask3 = mask1.combineWith(mask2).get();

        assertEquals("0-10 D   [2, 6]", mask1.toString());
        assertEquals("1-10 .   [10, 14]", mask2.toString());
        assertEquals("--10 .   [2, 6, 10, 14]", mask3.toString());
    }
}