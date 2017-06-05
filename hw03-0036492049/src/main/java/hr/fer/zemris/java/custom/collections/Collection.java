package hr.fer.zemris.java.custom.collections;

/**
 * Collection of objects
 *
 * @author Pavao Jerebić
 */
public class Collection {

    /**
     * Returns true or false if collection is empty
     *
     * @return boolean representing if collection is empty
     */
    public boolean isEmpty() {
        return size() <= 0;
    }

    /**
     * Returns size of the collection
     *
     * @return size
     */
    public int size() {
        return 0;
    }

    /**
     * Adds object value into collection
     *
     * @param value inserted object
     */
    public void add(Object value) {
    }

    /**
     * Returns true of false if collection contains the object
     *
     * @param value value that we are searching for
     * @return boolean representing if collection contains object value
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Removes object value if collection contains it
     *
     * @param value value that is to be removed
     * @return true if success, false if failure
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Returns collection as array
     *
     * @return collection as array
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Processes {@link Processor}'s process method on all elements of collection
     *
     * @param processor processor whose process method will be used on all elements of collection
     */
    public void forEach(Processor processor) {

    }

    /**
     * Adds all elements from other collection to this
     *
     * @param other other collection
     */
    public void addAll(Collection other) {

        /**
         * Local class that overrides original process to add values into collection
         * @author Pavao Jerebić
         */
        class AddProcessor extends Processor {
            @Override
            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new AddProcessor());

    }

    /**
     * Removes all elements from collection
     */
    public void clear() {

    }
}
