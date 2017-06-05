package hr.fer.zemris.java.hw02;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexNumberTest {
    private static final double doubleDelta = 0.0000001;

    @Test
    public void fromReal() throws Exception {
        assertEquals(5.0, ComplexNumber.fromReal(5).getReal(), doubleDelta);
    }

    @Test
    public void fromImaginary() throws Exception {
        assertEquals(5.0, ComplexNumber.fromImaginary(5.0).getImaginary(), doubleDelta);
    }

    @Test
    public void fromMagnitudeAndAngle() throws Exception {
        assertEquals(1, ComplexNumber.parse("1i").getMagnitude(), doubleDelta);
    }

    @Test
    public void parse() throws Exception {
        assertEquals(-4, ComplexNumber.parse("-4+3i").getReal(), doubleDelta);
    }

    @Test
    public void getReal() throws Exception {
        assertEquals(5, new ComplexNumber(5, 0).getReal(), doubleDelta);
    }

    @Test
    public void getImaginary() throws Exception {
        assertEquals(5, new ComplexNumber(0, 5).getImaginary(), doubleDelta);
    }

    @Test
    public void getMagnitude() throws Exception {
        assertEquals(5, new ComplexNumber(0,5).getMagnitude(), doubleDelta);
    }

    @Test
    public void getAngle() throws Exception {
        assertEquals(0, new ComplexNumber(5,0).getAngle(), doubleDelta);

    }

    @Test
    public void add() throws Exception {
        assertEquals(2,(ComplexNumber.parse("2+3i").add(ComplexNumber.parse("i"))).getReal(),doubleDelta);
    }

    @Test
    public void sub() throws Exception {
        assertEquals(2,(ComplexNumber.parse("2+3i").sub(ComplexNumber.parse("i"))).getReal(),doubleDelta);
    }

    @Test
    public void mul() throws Exception {
        assertEquals(9,(ComplexNumber.parse("2+3i").mul(ComplexNumber.parse("3-i"))).getReal(),doubleDelta);
    }

    @Test
    public void div() throws Exception {
        assertEquals(2.5,(ComplexNumber.parse("2+3i").div(ComplexNumber.parse("1+i"))).getReal(),doubleDelta);
    }

    @Test
    public void power() throws Exception {
        assertEquals(-5,(ComplexNumber.parse("2+3i").power(2)).getReal(),doubleDelta);
    }

    @Test
    public void root() throws Exception {

        assertEquals(1.674149228,(ComplexNumber.parse("2+3i").root(2))[0].getReal(),doubleDelta);
    }

}