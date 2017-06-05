package hr.fer.zemris.bf.demo;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.qmc.Minimizer;
import hr.fer.zemris.bf.utils.ExpressionTreePrinter;
import hr.fer.zemris.bf.utils.Util;

import java.util.*;

/**
 * Program that takes input from the console<br/>
 * Input is f( variables split by "," ) = E ( | E)<br/>
 * E stands for expression. It is a boolean expression or indexes of minterms in format [ indexes separated with ","]<br/>
 * Don't cares can be given by ending first expression with "|". Same rules apply for this expression
 *
 * @author Pavao Jerebić
 */
public class QMC {
    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line.trim().toLowerCase().equals("quit")) {
                break;
            }
            try {
                String[] splits = splitExpression(line);
                String[] variables = parseVariables(splits[0]);
                Integer[] minterms = parseSumOfMintermsFromExpression(splits[1], variables);
                Integer[] dontCares = new Integer[0];
                if (splits.length == 3) {
                    dontCares = parseSumOfMintermsFromExpression(splits[2], variables);
                }

                Minimizer minimizer = new Minimizer(new HashSet<>(Arrays.asList(minterms)), new HashSet<>(Arrays.asList(dontCares)), Arrays.asList(variables));

                List<String> forms = minimizer.getMinimalFormsAsString();

                for (String form : forms) {
                    System.out.println(form);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input");
            }
        }

        sc.close();


    }

    /**
     * Parses expression and produces a sum of minterms
     *
     * @param string    string
     * @param variables variables
     * @return minterms
     * @throws IllegalArgumentException if string is malformed
     */
    private static Integer[] parseSumOfMintermsFromExpression(String string, String[] variables) {
        string = string.trim();
        try {
            if (string.startsWith("[") && string.endsWith("]")) {
                string = string.substring(1, string.length() - 1);
                List<Integer> minterms = new ArrayList<>();
                for (String s : string.split(",")) {
                    minterms.add(Integer.parseInt(s.trim()));
                }
                Integer[] array = new Integer[minterms.size()];
                return minterms.toArray(array);
            } else {
                Parser parser = new Parser(string);
                Set<Integer> minterms = Util.toSumOfMinterms(Arrays.asList(variables), parser.getExpression());
                Integer[] array = new Integer[minterms.size()];
                return minterms.toArray(array);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

    }


    /**
     * Parses variables from f('Variables')
     *
     * @param split string
     * @return variables
     * @throws IllegalArgumentException if string is malformed
     */
    private static String[] parseVariables(String split) {
        try {
            split = split.trim();
            String[] variableArray;
            if (split.substring(0, 2).toLowerCase().equals("f(")) {
                if (split.endsWith(")")) {
                    String variable = split.substring(2, split.length() - 1);
                    variableArray = variable.split(",");
                    return variableArray;
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Splits expression
     *
     * @param line line
     * @return array of expressions
     * @throws IllegalArgumentException if line is malformed
     */
    private static String[] splitExpression(String line) {
        String[] array1 = line.split("=");
        if (array1.length != 2) {
            throw new IllegalArgumentException();
        }

        String[] array2 = array1[1].split("\\|");
        String[] result;
        if (array2.length == 2) {
            result = new String[]{array1[0], array2[0], array2[1]};
        } else if (array2.length == 1) {
            result = new String[]{array1[0], array2[0]};
        } else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * Helping method used when testing
     */
    private static void test() {
        Set<Integer> minterms = new HashSet<>(Arrays.asList(0, 1, 3, 10, 11, 14, 15));
        Set<Integer> dontcares = new HashSet<>(Arrays.asList(4, 6));
        Minimizer m = new Minimizer(minterms, dontcares, Arrays.asList("A", "B", "C", "D"));
        for (Node node : m.getMinimalFormsAsExpressions()) {
            node.accept(new ExpressionTreePrinter());
        }
        System.out.println(m.getMinimalFormsAsString());

        Minimizer m1 = new Minimizer(new HashSet<>(Arrays.asList(0, 1)), new HashSet<>(), Collections.singletonList("A"));
        for (Node node : m1.getMinimalFormsAsExpressions()) {
            node.accept(new ExpressionTreePrinter());
        }
        System.out.println(m1.getMinimalFormsAsString());

        Minimizer m2 = new Minimizer(new HashSet<>(), new HashSet<>(), Collections.singletonList("A"));
        for (Node node : m2.getMinimalFormsAsExpressions()) {
            node.accept(new ExpressionTreePrinter());
        }
        System.out.println(m2.getMinimalFormsAsString());

        Minimizer m3 = new Minimizer(new HashSet<>(Arrays.asList(2, 3)), new HashSet<>(), Arrays.asList("A", "B"));
        for (Node node : m3.getMinimalFormsAsExpressions()) {
            node.accept(new ExpressionTreePrinter());
        }
        System.out.println(m3.getMinimalFormsAsString());
    }
}
