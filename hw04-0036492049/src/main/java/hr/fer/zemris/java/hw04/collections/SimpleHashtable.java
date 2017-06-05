package hr.fer.zemris.java.hw04.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of a hash table<br/>
 * Has all basic functionalities of a hash table<br/>
 * If 75% of capacity is reached then it is resized to a double the amount
 *
 * @author Pavao Jerebi&#x107;
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    /**
     * Array representing the table
     */
    private TableEntry<K, V>[] table;
    /**
     * Size
     */
    private int size;
    /**
     * Number of modifications
     */
    private int modificationCount = 0;

    /**
     * Basic constructor that sets initial capacity to 16
     */
    public SimpleHashtable() {
        this(16);
    }

    /**
     * Constructor that sets capacity to first power of 2 greater or equal to given size
     *
     * @param size size
     * @throws IllegalArgumentException if size is less than 1
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than 1\n Given size was: " + size);
        }
        this.table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, capacity(size));
        this.size = 0;
    }

    /**
     * Helping method to calculate what should initial capacity be
     *
     * @param n given capacity
     * @return first power of 2 >= n
     */
    private int capacity(int n) {
        int i = 1;
        for (; 1 << i < n; i++) ;
        return 1 << i;
    }

    /**
     * Puts (key, value) into map
     *
     * @param key   key
     * @param value value
     * @throws IllegalArgumentException if key is null
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        int position = Math.abs(key.hashCode()) % table.length;

        if (!containsKey(key)) {
            if (1.0 * (size + 1) >= 0.75 * table.length) {
                resize(this);
            }
        }

        if (table[position] == null) {
            table[position] = new TableEntry<>(key, value);
            size++;
            modificationCount++;
            return;
        }

        for (TableEntry<K, V> entry = table[position]; ; entry = entry.next) {
            if (entry.key.equals(key)) {
                entry.setValue(value);
                return;
            }

            if (entry.next == null) {
                entry.next = new TableEntry<>(key, value);
                size++;
                modificationCount++;
                return;
            }
        }
    }

    /**
     * Resizes when 75% capacity is reached
     *
     * @param hashtable given hashtable
     */
    @SuppressWarnings("unchecked")
    private void resize(SimpleHashtable<K, V> hashtable) {
        SimpleHashtable<K, V> temp = new SimpleHashtable<>(table.length);

        for (SimpleHashtable.TableEntry<K, V> entry : hashtable) {
            temp.put(entry.getKey(), entry.getValue());
        }

        hashtable.clear();
        hashtable.table = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, table.length * 2);

        for (SimpleHashtable.TableEntry<K, V> entry : temp) {
            hashtable.put(entry.getKey(), entry.getValue());
        }

    }

    /**
     * Returns value of a pair with given key
     *
     * @param key key
     * @return value of element in table
     */
    public V get(Object key) {
//        if (!containsKey(key)) return null;
        if (key == null) return null;

        int position = Math.abs(key.hashCode()) % table.length;

        for (TableEntry<K, V> entry = table[position]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                return entry.getValue();
            }
        }

        return null;

    }

    /**
     * Getter for size
     *
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if table contains the key
     *
     * @param key key
     * @return true if key is in table
     */
    public boolean containsKey(Object key) {
        if (key == null) return false;

        int position = Math.abs(key.hashCode()) % table.length;

        for (TableEntry<K, V> entry = table[position]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                return true;
            }
        }

        return false;

    }

    /**
     * Checks if table contains value
     *
     * @param value value
     * @return true if value is in table
     */
    public boolean containsValue(Object value) {

        for (TableEntry<K, V> aTable : table) {
            for (TableEntry<K, V> entry = aTable; entry != null; entry = entry.next) {
                if (entry.value == value || entry.value.equals(value)) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Removes element in table with given key
     *
     * @param key key
     */
    public void remove(Object key) {
        if (key == null || isEmpty()) return;

        int position = Math.abs(key.hashCode()) % table.length;

        if (table[position].key.equals(key)) {
            table[position] = table[position].next;
            size--;
            modificationCount++;
            return;
        }

        for (TableEntry<K, V> entry = table[position]; entry != null && entry.next != null; entry = entry.next) {
            if (entry.next.key.equals(key)) {
                entry.next = entry.next.next;
                size--;
                modificationCount++;
                return;
            }
        }
    }

    /**
     * Returns true if table is empty
     *
     * @return true if size is <=0
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Clears whole table of elements, capacity stays the sames
     */
    public void clear() {
        for (int i = 0; i < table.length; ++i) {
            clearFromEnd(table[i]);
            table[i] = null;
        }
        size = 0;
        modificationCount++;
    }

    /**
     * Helping method to clear all elements of a linked list
     *
     * @param entry starting point
     */
    private void clearFromEnd(TableEntry<K, V> entry) {
        if (entry == null) return;

        clearFromEnd(entry.next);

        entry.next = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (TableEntry<K, V> aTable : table) {
            for (TableEntry<K, V> entry = aTable; entry != null; entry = entry.next) {
                sb.append(entry.toString()).append(",");
            }
        }
        sb.setLength(Math.max(sb.length() - 1, 1));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new SimpleIterator();
    }

    /**
     * Implementation of {@link Iterator}
     */
    private class SimpleIterator implements Iterator<TableEntry<K, V>> {

        /**
         * Elements processed so far
         */
        private int count;
        /**
         * Current row in the table
         */
        private int currentRow;
        /**
         * Current entry
         */
        private TableEntry<K, V> entry;
        /**
         * Number of modifications
         */
        private int modificationCount;
        /**
         * Can remove be called
         */
        private boolean canRemove;
        /**
         * Size of the table
         */
        private int size;

        /**
         * Base constructor
         */
        public SimpleIterator() {
            modificationCount = SimpleHashtable.this.modificationCount;
            this.size = SimpleHashtable.this.size();
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (SimpleHashtable.this.modificationCount != modificationCount) {
                throw new ConcurrentModificationException("Table has changed");
            }
            return count < size;
        }

        @Override
        public TableEntry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }

            if (entry == null) {
                while (table[currentRow] == null) currentRow++;
                entry = table[currentRow];
                count++;
            } else if (entry.next == null) {
                currentRow++;
                while (table[currentRow] == null) currentRow++;
                entry = table[currentRow];
                count++;
            } else {
                entry = entry.next;
                count++;
            }

            canRemove = true;
            return entry;

        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Method next has not yet been called or remove method has already been called after call to the next method");
            }
            SimpleHashtable.this.remove(entry.key);
            canRemove = false;
            modificationCount++;
        }
    }

    /**
     * Table entry class
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class TableEntry<K, V> {
        /**
         * Key
         */
        private K key;
        /**
         * Value
         */
        private V value;
        /**
         * Next entry in list
         */
        private TableEntry<K, V> next;

        /**
         * Constructor that sets key and value
         *
         * @param key   key
         * @param value value
         * @throws IllegalArgumentException if key is null
         */
        public TableEntry(K key, V value) {
            if (key == null) {
                throw new IllegalArgumentException("key cant be null");
            }
            this.key = key;
            this.value = value;
        }

        /**
         * Setter for value
         * @param value value
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Getter for key
         * @return key
         */
        public K getKey() {
            return key;
        }

        /**
         * Getter for value
         * @return value
         */
        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

    }


    /**
     * Demo
     * @param args ignored
     */
    public static void main(String[] args) {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        System.out.println(Arrays.toString(examMarks.table) + " " + examMarks.table.length);
        examMarks.put("Ante", 2);
        System.out.println(Arrays.toString(examMarks.table) + " " + examMarks.table.length);
        examMarks.put("Jasna", 2);
        System.out.println(Arrays.toString(examMarks.table) + " " + examMarks.table.length);
        examMarks.put("Kristina", 5);
        System.out.println(Arrays.toString(examMarks.table) + " " + examMarks.table.length);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        // query collection:
        Integer kristinaGrade = examMarks.get("Kristina");
        System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
        // What is collection's size? Must be four!
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4
        System.out.println(examMarks);
        System.out.println(Arrays.toString(examMarks.table) + " " + examMarks.table.length);

        examMarks.remove("Ante");
        System.out.println(examMarks);

        examMarks.clear();
        System.out.println(examMarks);

        // create collection:
        //SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        System.out.println(examMarks);
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }

        for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
            for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
                System.out.printf(
                        "(%s => %d) - (%s => %d)%n",
                        pair1.getKey(), pair1.getValue(),
                        pair2.getKey(), pair2.getValue()
                );
            }
        }


        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
        while (iter.hasNext()) {
            SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());


    }
}
