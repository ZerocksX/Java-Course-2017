package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;


/**
 * @author Pavao Jerebić
 */
public class ExpressionTreePrinterTest {

    private VariableNode variableNodeA;
    private VariableNode variableNodeB;
    private UnaryOperatorNode unaryOperatorNode;
    private BinaryOperatorNode binaryOperatorNode;

    @Before
    public void setUp() throws Exception {
        variableNodeA = new VariableNode("A");
        variableNodeB = new VariableNode("B");
        unaryOperatorNode = new UnaryOperatorNode("not", variableNodeA, aBoolean -> !aBoolean);
        binaryOperatorNode = new BinaryOperatorNode("or", Arrays.asList(variableNodeA, variableNodeB, unaryOperatorNode), (aBoolean, aBoolean2) -> aBoolean | aBoolean2);
    }

    @Test
    public void test00() throws Exception {
        binaryOperatorNode.accept(new ExpressionTreePrinter());
    }
}