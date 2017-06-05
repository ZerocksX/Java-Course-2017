package hr.fer.zemris.bf.demo;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

import java.util.Arrays;
import java.util.List;

/**
 * {@link Util#filterAssignments(List, Node, boolean)} demo
 */
public class UtilDemo1 {

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        Node expression = new Parser("A and b or C").getExpression();

        VariablesGetter getter = new VariablesGetter();
        expression.accept(getter);

        List<String> variables = getter.getVariables();
        for (boolean[] values : Util.filterAssignments(variables, expression, true)) {
            System.out.println(Arrays.toString(values).replaceAll("true", "1").replaceAll("false", "0"));
        }
    }

}
