package hr.fer.zemris.java.console;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Pavao Jerebić
 */
public class Expression {
    private String main;
    private String first;
    private String second;
    private BiFunction<Double, Double, Double> operation;

    public Expression(String main, String first, String second, BiFunction<Double, Double, Double> operation) {
        this.main = main;
        this.first = first;
        this.second = second;
        this.operation = operation;
    }

    public String getMain() {
        return main;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public BiFunction<Double, Double, Double> getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "main='" + main + '\'' +
                ", first='" + first + '\'' +
                ", second='" + second + '\'' +
                '}';
    }
}
