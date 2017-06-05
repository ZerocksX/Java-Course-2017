package hr.fer.zemris.bf.parser;

import hr.fer.zemris.bf.utils.ExpressionTreePrinter;
import org.junit.Test;


/**
 * @author Pavao Jerebić
 */
public class ParserTest {
    @Test
    public void test00() throws Exception {
        Parser parser = new Parser("a or b or c and d and e");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }


    @Test
    public void test01() throws Exception {
        Parser parser = new Parser("not a or b or c and d and e");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test02() throws Exception {
        Parser parser = new Parser("(a or b)");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test03() throws Exception {
        Parser parser = new Parser("(a or b) and c or ( a xor d)");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test04() throws Exception {
        Parser parser = new Parser("(a or (b or (c or d) ) )");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test05() throws Exception {
        Parser parser = new Parser("(a or (b or (c or d) ) ) and (c or d) or (c and d)");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test06() throws Exception {
        Parser parser = new Parser("(a or (b or (c or d) ) ) and (c or d) or (c and d) or a or not (c xor d)");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test07() throws Exception {
        Parser parser = new Parser("a and b or c");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test08() throws Exception {
        Parser parser = new Parser("not not b");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test09() throws Exception {
        Parser parser = new Parser("a and b");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test10() throws Exception {
        Parser parser = new Parser("NOT A AND NOT B AND (NOT C OR D) OR A AND C");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test11() throws Exception {
        Parser parser = new Parser(" (A:+:B) * (NOT A * NOT B + A * B)");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

    @Test
    public void test12() throws Exception {
        Parser parser = new Parser("  !A* !B* !C + A*C + A*B* !C or !A* !B*C");

        parser.getExpression().accept(new ExpressionTreePrinter());
    }

}