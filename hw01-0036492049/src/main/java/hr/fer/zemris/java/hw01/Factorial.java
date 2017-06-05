package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that takes a integer [1..20] from console and prints nth factorial of that number.
 * If the user has provided a number which is out of range, or if input is not an integer, then the user is
 * asked to provide a correct integer
 * Program will end when user has provided 'kraj' as input
 * @author Pavao Jerebić
 */
public class Factorial {
    /**
     * Method that takes n as input and returns its factorial
     * @param n integer n
     * @return n!
     */
    public static long nthFactorial(int n){
        long sol = 1;
        while(n > 1){
            sol *= n--;
        }
        return sol;
    }

    /**
     * Method where program starts
     * Reads input from standard and depending form input gives appropriate output
     * @param args ignored
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("Unesite broj > ");
            String input = sc.next();
            if(input.equals("kraj")){
                System.out.println("Doviđenja.");
                break;
            }
            try {
                int n = Integer.parseInt(input);
                if( n < 1 || n > 20){
                    System.out.format("'%d' nije broj u dozvoljenom rasponu.%n", n);
                }else{
                    System.out.format("%d! = %d%n", n, nthFactorial(n));
                }
            }catch (NumberFormatException ex){
                System.out.format("'%s' nije cijeli broj.%n", input);
            }
        }
        sc.close();
    }
}
