package hr.fer.zemris.java.console.parser;

import hr.fer.zemris.java.console.Expression;
import hr.fer.zemris.java.console.Variable;
import hr.fer.zemris.java.console.lexer.Lexer;
import hr.fer.zemris.java.console.lexer.Token;
import hr.fer.zemris.java.console.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Pavao Jerebić
 */
public class Parser {
    private char[] data;
    private List<Expression> expressions;
    private List<Variable> labels;
    private List<Variable> expandees;
    private Map<String, Variable> variableNames = new HashMap<>();
    private int endPoint;

    public Parser(char[] data) {
        this.data = data;
        expressions = new ArrayList<>();
        labels = new ArrayList<>();
        expandees = new ArrayList<>();
        parse();
    }

    private void parse() {
        Lexer lexer = new Lexer(data);
        Token token = lexer.nextToken();
        while (token.getType() == TokenType.DIRECTIVE) {

            String directive = token.getValue();
            if (directive.equals("#EXPR")) {
                token = lexer.nextToken();
                String main = token.getValue().substring(1);
                token = lexer.nextToken();
                token = lexer.nextToken();
                String first = token.getValue().substring(1);
                token = lexer.nextToken();
                BiFunction<Double, Double, Double> operation = null;
                switch (token.getValue()) {
                    case "*":
                        operation = (a, b) -> a * b;
                        break;
                    case "/":
                        operation = (a, b) -> a / b;
                        break;
                    case "+":
                        operation = (a, b) -> a + b;
                        break;
                    case "-":
                        operation = (a, b) -> a - b;
                        break;
                }
                token = lexer.nextToken();
                String second = token.getValue().substring(1);
                expressions.add(new Expression(main, first, second, operation));
                token = lexer.nextToken();
            } else if (directive.equals("#LABELS")) {
                token = lexer.nextToken();
                String name = token.getValue();
                token = lexer.nextToken();
                while (token.getType() == TokenType.COLON) {
                    labels.add(new Variable(name, null));
                    token = lexer.nextToken();
                    if (token.getType() != TokenType.NAME) {
                        break;
                    }
                    name = token.getValue();
                    token = lexer.nextToken();
                }
                labels.add(new Variable(name, null));

            } else if (directive.equals("#EXPAND")) {
                token = lexer.nextToken();
                String name = token.getValue();
                token = lexer.nextToken();
                while (token.getType() == TokenType.COLON) {
                    expandees.add(new Variable(name, null));

                    token = lexer.nextToken();
                    if (token.getType() != TokenType.NAME) {
                        break;
                    }
                    name = token.getValue();
                    token = lexer.nextToken();
                }
                expandees.add(new Variable(name, null));


            }
        }
        if (lexer.getToken().getType() == TokenType.EOF) {
            endPoint = Integer.parseInt(lexer.getToken().getValue());
        }
    }

    public int getEndPoint() {
        return endPoint;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public List<Variable> getLabels() {
        return labels;
    }

    public List<Variable> getExpandees() {
        return expandees;
    }

    public String calculate(String line) {
        variableNames = new HashMap<>();
        String[] data = line.split("[;]");
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setValue(Double.parseDouble(data[i]));
            variableNames.put(labels.get(i).getName(), labels.get(i));
        }
//        System.out.println(labels.toString());
        for (Expression expression : expressions) {
            if (!variableNames.containsKey(expression.getMain())) {
                variableNames.put(expression.getMain(), new Variable(expression.getMain(), null));
            }
            if (!variableNames.containsKey(expression.getSecond())) {
                variableNames.put(expression.getSecond(), new Variable(expression.getSecond(), null));
            }
            if (!variableNames.containsKey(expression.getFirst())) {
                variableNames.put(expression.getFirst(), new Variable(expression.getFirst(), null));
            }
        }
        while (!allClear()) {

            for (Expression expression : expressions) {
                Variable main = variableNames.get(expression.getMain());
                Variable first = variableNames.get(expression.getFirst());
                Variable second = variableNames.get(expression.getSecond());
                if (first.getValue() == null || second.getValue() == null) {
                    continue;
                }
                main.setValue(expression.getOperation().apply(first.getValue(), second.getValue()));

            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = labels.size(); i < n; i++) {
            sb.append(variableNames.get(labels.get(i).getName()).getValue());
            if (i != n - 1) {
                sb.append(";");
            }
        }
        for (int i = 0, n = expandees.size(); i < n; i++) {
            sb.append(";");
            sb.append(variableNames.get(expandees.get(i).getName()).getValue());
        }
        return sb.toString();
    }

    private boolean allClear() {
        for (String name : variableNames.keySet()) {
            if (variableNames.get(name).getValue() == null) {
                return false;
            }
        }
        return true;
    }
}
