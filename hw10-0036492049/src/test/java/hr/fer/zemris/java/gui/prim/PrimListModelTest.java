package hr.fer.zemris.java.gui.prim;

import org.junit.Test;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static org.junit.Assert.*;

/**
 * @author Pavao Jerebić
 */
public class PrimListModelTest {

    private static ListDataListener l = new ListDataListener() {
        @Override
        public void intervalAdded(ListDataEvent listDataEvent) {

        }

        @Override
        public void intervalRemoved(ListDataEvent listDataEvent) {

        }

        @Override
        public void contentsChanged(ListDataEvent listDataEvent) {

        }
    };

    @Test
    public void testAddListener() throws Exception {
        PrimListModel model = new PrimListModel();
        model.addListDataListener(l);
        assertEquals(1, model.getListeners().size());
    }

    @Test
    public void testRemoveListener() throws Exception {
        PrimListModel model = new PrimListModel();
        model.addListDataListener(l);
        assertEquals(1, model.getListeners().size());
        model.removeListDataListener(l);
        assertEquals(0, model.getListeners().size());
    }

    @Test
    public void testNext() throws Exception {
        PrimListModel model = new PrimListModel();
        JList<String> jList1 = new JList<>(model);
        JList<String> jList2 = new JList<>(model);
        assertEquals("1",
                jList1.getModel().getElementAt(0));
        assertEquals("1",
                jList2.getModel().getElementAt(0));
        assertEquals(1,
                jList1.getModel().getSize());
        assertEquals(1,
                jList1.getModel().getSize());

        model.next();

        assertEquals("2",
                jList1.getModel().getElementAt(1));
        assertEquals(2,
                jList1.getModel().getSize());
        assertEquals("2",
                jList2.getModel().getElementAt(1));
        assertEquals(2,
                jList2.getModel().getSize());

        model.next();
        model.next();

        assertEquals("5",
                jList1.getModel().getElementAt(3));
        assertEquals(4,
                jList1.getModel().getSize());
        assertEquals("5",
                jList2.getModel().getElementAt(3));
        assertEquals(4,
                jList2.getModel().getSize());

    }
}