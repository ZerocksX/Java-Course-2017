package hr.fer.zemris.java.hw01;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Scanner;

/**
 * Program that calculates area and perimeter of a rectangle
 * Input is given either as console argument or in standard input
 * Rectangle's width and height must be non-negative rational numbers
 * If given number of arguments isn't 0 or 2 then program exits
 * @author Pavao Jerebić
 */
public class Rectangle {

    /**
     * Returns area of the rectangle with given width and height
     * @param w width
     * @param h height
     * @return area
     */
    public static double area(double w, double h){
        return w*h;
    }

    /**
     * Returns perimeter of the rectangle with given width and height
     * @param w width
     * @param h height
     * @return perimeter
     */
    public static double perimeter(double w, double h){
        return 2.0*(w+h);
    }


    /**
     * Method which uses NumberFormat class to parse string to double considering locale
     * @param input string that is parsed
     * @return input parsed as double
     * @throws ParseException This exception is thrown if number can't be parsed or if parsed length doesn't match input string's length
     */
    public static double parseDouble(String input) throws ParseException{
        NumberFormat numberFormat = NumberFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);

        Number number = numberFormat.parse(input, parsePosition);

        if(parsePosition.getIndex() != input.length()){
            throw new ParseException("Invalid input", parsePosition.getIndex());
        }

        return number.doubleValue();
    }

    /**
     * Reads input with given scanner as double while providing text 'Unesite %s > " where %s is given name
     * if input is invalid then user is given appropriate message
     * @param name name for input
     * @param sc scanner
     * @return valid input as double
     */
    private static double readDoubleWithNameFromScanner(String name, Scanner sc){
        double data;
        while(true){
            System.out.printf("Unesite %s > ", name);
            String input = sc.next();
            try{
                data = parseDouble(input);
                if(data <= 0){
                    System.out.printf("Unijeli ste negativnu vrijednost.%n");
                }else{
                    break;
                }
            }catch (ParseException ex){
                System.out.printf("'%s' se ne može protumačiti kao broj.%n", input);
            }
        }
        return data;

    }

    /**
     * Starting method
     * If console arguments are valid then takes input from there, if none are given reads input from standard input
     * If console arguments are invalid program exits
     * If program is reading from standard input then program will continue reading until valid input is given
     * Displays rectangles area and perimeter
     * @param args must be either 0 or 2
     */
    public static void main(String[] args) {
        if(args.length == 1 || args.length > 2){
            System.out.println("Unijeli ste neispravne argumente!");
        }else{
            if(args.length == 2){
                try{
                    double w = parseDouble(args[0]), h = parseDouble(args[1]);
                    System.out.printf("Pravokutnik širine %f i visine %f ima površinu %f te opseg %f.%n", w, h, area(w, h), perimeter(w, h));
                }catch (ParseException ex){
                    System.out.println("Unijeli ste neispravne argumente!");
                }

            }else{
                Scanner sc = new Scanner(System.in);
                double w, h;
                w = readDoubleWithNameFromScanner("širinu", sc);
                h = readDoubleWithNameFromScanner("visinu", sc);
                System.out.printf("Pravokutnik širine %f i visine %f ima površinu %f te opseg %f.%n", w, h, area(w, h), perimeter(w, h));
            }
        }
    }

}
