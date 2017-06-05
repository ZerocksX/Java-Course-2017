package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * List model that produces a list of prime numbers by calling method {@link #next()}
 *
 * @author Pavao Jerebić
 */
public class PrimListModel implements ListModel<String> {

    /**
     * List of primes
     */
    private List<String> prims;
    /**
     * List of listeners
     */
    private List<ListDataListener> listeners;

    /**
     * Basic constructor
     */
    public PrimListModel() {
        this.prims = new ArrayList<>();
        prims.add("1");
        this.listeners = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return prims.size();
    }

    @Override
    public String getElementAt(int i) {
        return prims.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {
        listeners.add(listDataListener);
    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {
        listeners.remove(listDataListener);
    }

    /**
     * Adds next prime number to the list and notifies listeners
     */
    public void next() {
        int position = getSize();
        prims.add(nextPrim());
        ListDataEvent event = new ListDataEvent(
                this,
                ListDataEvent.INTERVAL_ADDED,
                position,
                position
        );
        listeners.forEach(listDataListener ->
                listDataListener.intervalAdded(event)
        );
    }

    /**
     * Helping method that produces a next prime number
     *
     * @return string representation of a next prime number
     */
    private String nextPrim() {
        Long last = Long.parseLong(prims.get(getSize() - 1));

        for (long i = last + 1; i < Long.MAX_VALUE; i++) {
            boolean isPrim = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrim = false;
                    break;
                }
            }
            if (isPrim) {
                return Long.toString(i);
            }
        }
        return "1";
    }

    /**
     * Only for testing purposes(reason fro package-private scope)
     *
     * @return listeners
     */
    List<ListDataListener> getListeners() {
        return listeners;
    }
}
