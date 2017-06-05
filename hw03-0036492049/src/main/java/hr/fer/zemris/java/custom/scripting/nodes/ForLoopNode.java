package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct
 *
 * @author Pavao Jerebić
 */
public class ForLoopNode extends Node {
    /**
     * Variable
     */
    private ElementVariable variable;
    /**
     * Start expression
     */
    private Element startExpression;
    /**
     * End expression
     */
    private Element endExpression;
    /**
     * Step expression
     */
    private Element stepExpression;

    /**
     * Constructor
     *
     * @param variable        variable
     * @param startExpression start expression
     * @param endExpression   end expression
     * @param stepExpression  step expression
     * @throws IllegalArgumentException if any argument except stepExpression is null
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        if (variable == null
                || startExpression == null
                || endExpression == null) {
            throw new IllegalArgumentException("Argument/s can not be null");
        }
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    /**
     * Getter for variable
     *
     * @return variable
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Getter for start expression
     *
     * @return start expression
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Getter for end expression
     *
     * @return end expression
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Getter for step expression
     *
     * @return step expression
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public String toString() {
        String s =  "ForLoopNode{" +
                "variable=" + variable.asText() +
                ", startExpression=" + startExpression.asText() +
                ", endExpression=" + endExpression.asText();
        if(stepExpression != null)s+=", stepExpression=" + stepExpression.asText();
        return  s + "}";
    }
}
