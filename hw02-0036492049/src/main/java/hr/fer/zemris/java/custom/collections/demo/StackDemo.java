package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Application that takes input from command line and calculates the expression in postfix representation with spaces between
 * Application only takes one argument
 *
 * @author Pavao Jerebić
 */
public class StackDemo {

    /**
     * Main method of application
     *
     * @param args postfix expression to calculate
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments");
            return;
        }

        ObjectStack stack = new ObjectStack();

        String[] elements = args[0].split(" ");

        for (String element : elements) {
            try {
                int value = Integer.parseInt(element);
                stack.push(value);
            } catch (NumberFormatException ex) {
                int a, b;
                try{
                    b = (int) stack.pop();
                    a = (int) stack.pop();
                }catch (EmptyStackException e){
                    System.out.println("Invalid argument");
                    return;
                }
                switch (element) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "%":
                        stack.push(a % b);
                        break;
                    default:
                        System.out.println("Invalid argument");
                        return;
                }
            }
        }
        System.out.printf("Expression evaluates to %d.%n", (int) stack.pop());
    }
}
