package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.Node;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Utilities class
 *
 * @author Pavao Jerebić
 */
public class Util {

    /**
     * Creates all combination of given variables and calls {@link Consumer#accept(Object)} on that combination<br/>
     * Can create up to 2^63 combinations
     *
     * @param variables variables
     * @param consumer  consumer
     * @throws IllegalArgumentException if number of variables is greater than 63
     */
    public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
        long mask = 0;
        if (variables.size() > 63) {
            throw new IllegalArgumentException("Too many variables");
        }
        for (int i = 0, n = 1 << variables.size(); i < n; i++) {

            boolean[] values = new boolean[variables.size()];
            for (int j = 0, m = variables.size(); j < m; j++) {
                values[m - 1 - j] = ((mask & (1 << j)) != 0);
            }

            consumer.accept(values);
            mask++;
        }
    }

    /**
     * For a given list of variables, creates all combinations of their values and than calculates its' expression<br/>
     * If result mathces expressionValue it puts them in a Set in order of appearance
     *
     * @param variables       list of variables
     * @param expression      expression
     * @param expressionValue expression value to filter for
     * @return set of variable values combinations
     */
    public static Set<boolean[]> filterAssignments(List<String> variables, Node expression, boolean expressionValue) {
        Set<boolean[]> assignments = new LinkedHashSet<>();

        ExpressionEvaluator eval = new ExpressionEvaluator(variables);

        Util.forEach(
                variables,
                values -> {
                    eval.setValues(values);
                    expression.accept(eval);
                    if (eval.getResult() == expressionValue) {
                        assignments.add(values);
                    }
                }
        );

        return assignments;
    }

    /**
     * Creates a int from a given boolean array, [1, 0, 0] will be 4
     *
     * @param values boolean array
     * @return int representation of given boolean array
     */
    public static int booleanArrayToInt(boolean[] values) {
        if (values.length > 31) {
            throw new IllegalArgumentException("Number is too big");
        }

        int number = 0;

        for (int i = 0, n = values.length; i < n; i++) {
            if (values[n - 1 - i]) {
                number += 1 << i;
            }
        }

        return number;
    }


    /**
     * Creates a set of minterms in given expression with its variables
     *
     * @param variables  variables
     * @param expression expression
     * @return all minterms
     */
    public static Set<Integer> toSumOfMinterms(List<String> variables, Node expression) {
        Set<boolean[]> filtered = filterAssignments(variables, expression, true);
        Set<Integer> result = new LinkedHashSet<>();
        for (boolean[] values : filtered) {
            result.add(booleanArrayToInt(values));
        }

        return result;

    }

    /**
     * Creates a set of maxterms in given expression with its variables
     *
     * @param variables  variables
     * @param expression expression
     * @return all maxterms
     */
    public static Set<Integer> toProductOfMaxterms(List<String> variables, Node expression) {
        Set<boolean[]> filtered = filterAssignments(variables, expression, false);
        Set<Integer> result = new LinkedHashSet<>();
        for (boolean[] values : filtered) {
            result.add(booleanArrayToInt(values));
        }

        return result;
    }


    /**
     * Method that takes an integer and transforms it to a byte array with a given size.<br/>
     * Most significant bit is in the first position
     * @param x integer
     * @param n size
     * @return byte array representation of a given integer
     */
    public static byte[] indexToByteArray(int x, int n) {

        byte[] index = new byte[n];

        for (int i = 0; i < n; i++) {
            index[n - (1 + i)] = ((x & 1 << i) != 0) ? (byte) 1 : (byte) 0;
        }

        return index;
    }

}
