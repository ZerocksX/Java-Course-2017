package hr.fer.zemris.java.custom.collections;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayIndexedCollectionTest {
    @Test
    public void isEmptyTrue() throws Exception {
        assertEquals(true, collection.isEmpty());
    }

    @Test
    public void isEmptyFalse() throws Exception {
        collection.add(5);
        assertEquals(false, collection.isEmpty());
    }

    @Test
    public void sizeEmpty() throws Exception {
        assertEquals(0, collection.size());
    }


    private ArrayIndexedCollection collection;

    @Before
    public void setUp() throws Exception {
        collection = new ArrayIndexedCollection();

    }

    @Test
    public void sizeFourElements() throws Exception {
        collection.add(5);
        collection.add(3);
        collection.add(2);
        collection.add(1);

        assertEquals(4, collection.size());
    }

    @Test
    public void addOneElement() throws Exception {
        collection.add(2);

        assertEquals(2, collection.get(0));
    }

    @Test
    public void removeOneElement() throws Exception {
        collection.add(5);
        collection.add(1);
        collection.add(3);
        collection.add(4);

        collection.remove(new Integer(1));
        assertEquals(3, collection.size());
        assertEquals(3, collection.get(1));
    }

    @Test
    public void insertOneElement() throws Exception {
        collection.add(1);
        collection.add(2);
        collection.add(4);
        collection.add(5);
        collection.add(6);

        collection.insert(3, 2);
        assertEquals(3, collection.get(2));
        assertEquals(4, collection.get(3));

    }

    @Test
    public void add17Elements() throws Exception {
        for (int i = 0; i < 17; i++) {
            collection.add(i);
        }
        assertEquals(17, collection.size());
    }

    @Test
    public void addAllCollectionOf5() throws Exception {
        ArrayIndexedCollection other = new ArrayIndexedCollection() {{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
        }};
        collection.addAll(other);
        assertEquals(5, collection.size());

    }

    @Test
    public void clearAll() throws Exception {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.clear();
        assertEquals(true, collection.isEmpty());
    }

    @Test
    public void indexOfExists() throws Exception {
        collection.add(3);
        collection.add(1);
        assertEquals(1, collection.indexOf(1));

    }
}