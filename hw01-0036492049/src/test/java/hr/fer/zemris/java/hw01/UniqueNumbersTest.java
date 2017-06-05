package hr.fer.zemris.java.hw01;

import org.junit.Test;

import static hr.fer.zemris.java.hw01.UniqueNumbers.*;
import static org.junit.Assert.*;
/**
 * Tests UniqueNumbers class' functionality
 * @author Pavao Jerebić
 */
public class UniqueNumbersTest {

    /**
     * tests methods addNode and treeSize with one node
     * @throws Exception if needed
     */
    @Test
    public void addOneNode() throws Exception {
        TreeNode head = null;
        head = addNode(head, 41);
        assertEquals(1, treeSize(head));
    }

    /**
     * tests methods addNode and treeSize with four nodes with multiple instances of same value
     * @throws Exception if needed
     */
    @Test
    public void addFourNodesWithMultipleInstances() throws Exception {
        TreeNode head = null;
        head = addNode(head, 42);
        head = addNode(head, 76);
        head = addNode(head, 21);
        head = addNode(head, 76);
        head = addNode(head, 35);
        assertEquals(4, treeSize(head));
    }

    /**
     * tests method containsValue when tree contains it
     * @throws Exception if needed
     */
    @Test
    public void containsValueTrue() throws Exception {
        TreeNode head = null;
        head = addNode(head, 42);
        head = addNode(head, 76);
        head = addNode(head, 21);
        head = addNode(head, 76);
        head = addNode(head, 35);
        assertEquals(true,containsValue(head,76));
    }

    /**
     * tests method containsValue when tree does not contain it
     * @throws Exception if needed
     */
    @Test
    public void containsValueFalse() throws Exception {
        TreeNode head = null;
        head = addNode(head, 35);
        assertEquals(false,containsValue(head,1));
    }

}