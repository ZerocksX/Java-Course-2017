package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that puts integers into a ordered binary tree if there are no nodes with that value currently in the tree
 * @author Pavao Jerebić
 */
public class UniqueNumbers {

    /**
     * Class representing a node in a tree
     */
    public static class TreeNode {
        /**
         * left child of the current node
         */
        TreeNode left;

        /**
         * right child of the current node
         */
        TreeNode right;

        /**
         * value of the current node
         */
        int value;
    }

    /**
     * Method that adds new node to the tree if possible
     * Tree will always be sorted
     * @param head head of the tree
     * @param value value of new node
     * @return head of the tree
     */
    public static TreeNode addNode(TreeNode head, int value){
        if(head == null){
            TreeNode newNode = new TreeNode();
            newNode.left = null;
            newNode.right = null;
            newNode.value = value;
            return newNode;
        }
        if(head.value == value){
            return head;
        }
        if(head.value > value){
            head.left = addNode(head.left, value);
        }else{
            head.right = addNode(head.right, value);
        }
        return head;
    }

    /**
     * Method that will return true or false whether tree contains given value
     * @param head head of the tree
     * @param value searched value
     * @return whether tree contains the value or not
     */
    public static boolean containsValue(TreeNode head, int value) {
        return head != null && (head.value == value || containsValue(head.left, value) || containsValue(head.right, value));
    }

    /**
     * Method that will return the size of the tree
     * @param head head of the tree
     * @return size of the tree
     */
    public static int treeSize(TreeNode head){
        if(head == null) return 0;
        return 1 + treeSize(head.left) + treeSize(head.right);
    }

    /**
     * Prints the tree in order
     * @param head head of the tree
     */
    private static void printTreeInOrder(TreeNode head){
        if(head == null) return;
        printTreeInOrder(head.left);
        System.out.printf("%d ", head.value);
        printTreeInOrder(head.right);
    }

    /**
     * Prints the tree in reverse order
     * @param head head of the tree
     */
    private static void printTreeInReverseOrder(TreeNode head){
        if(head == null) return;
        printTreeInReverseOrder(head.right);
        System.out.printf("%d ", head.value);
        printTreeInReverseOrder(head.left);
    }

    /**
     * Method where program starts
     * Reads from standard input, if input is invalid program will output appropriate message and continue reading input
     * Method adds valid input into ordered binary tree
     * If a value already exists in the tree program will not add it and will provide appropriate message and continue
     * Program reads until user inputs 'kraj' then outputs all elements twice. First sorted ascending then descending, and finally exits.
     * @param args ignored
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        TreeNode head = null;

        while(true){
            System.out.printf("Unesite broj > ");
            String input = sc.next();
            if(input.equals("kraj")){
                break;
            }
            try{
                int value = Integer.parseInt(input);

                if(!containsValue(head,value)){
                    head = addNode(head,value);
                    System.out.println("Dodano.");
                }else{
                    System.out.println("Broj već postoji. Preskačem.");
                }
            }catch (NumberFormatException ex){
                System.out.printf("'%s' nije cijeli broj.%n", input);
            }
        }

        System.out.printf("Ispis od najmanjeg: ");
        printTreeInOrder(head);
        System.out.println();

        System.out.printf("Ispis od najvećeg: ");
        printTreeInReverseOrder(head);
        System.out.println();

        sc.close();
    }
}
