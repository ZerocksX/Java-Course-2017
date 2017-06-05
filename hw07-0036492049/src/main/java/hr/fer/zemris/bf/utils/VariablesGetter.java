package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;

import java.util.*;

/**
 * Creates lexicographical ordered list of variables in given expression
 *
 * @author Pavao Jerebić
 */
public class VariablesGetter implements NodeVisitor {

    /**
     * Variables
     */
    private Set<String> variables;


    /**
     * Basic constructor
     */
    public VariablesGetter() {
        variables = new HashSet<>();
    }

    @Override
    public void visit(ConstantNode node) {

    }

    @Override
    public void visit(VariableNode node) {
        variables.add(node.getName());
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        node.getChild().accept(this);
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        node.getChildren().forEach(child -> child.accept(this));
    }

    /**
     * Returns ordered list of variables
     * @return variables
     */
    public List<String> getVariables() {
        List<String> var = new ArrayList<>();
        var.addAll(variables);
        Collections.sort(var);
        return var;
    }
}
