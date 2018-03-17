package clc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class FractionTest {
    private Fraction fractionA, fractionB;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test public void divide(){
        fractionA = new Fraction(6);
        fractionB = new Fraction(4);
        fractionA.divide(fractionB);
        assertEquals(1.5d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(7);
        fractionB = new Fraction(1);
        fractionA.divide(fractionB);
        assertEquals(7d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(0);
        fractionB = new Fraction(1);
        fractionA.divide(fractionB);
        assertEquals(0d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(-3);
        fractionB = new Fraction(2);
        fractionA.divide(fractionB);
        assertEquals(-1.5d, fractionA.doubleValue(), 0);
        
        fractionA = new Fraction(3);
        fractionB = new Fraction(0);
        exception.expect(ArithmeticException.class);
        fractionA.divide(fractionB);
    }
    
    @Test public void divide2(){
        fractionA = new Fraction(0);
        fractionB = new Fraction(0);
        exception.expect(ArithmeticException.class);
        fractionA.divide(fractionB);
    }
    
    @Test public void add(){
        fractionA = new Fraction(-3);
        fractionB = new Fraction(0);
        fractionA.add(fractionB);
        assertEquals(-3d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(3);
        fractionB = new Fraction(0);
        fractionA.add(fractionB);
        assertEquals(3d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(1);
        fractionB = new Fraction(2);
        fractionA.add(fractionB);
        assertEquals(3d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(-9, 3);
        fractionB = new Fraction(2);
        fractionA.add(fractionB);
        assertEquals(-1d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(3);
        fractionB = new Fraction(-2);
        fractionA.add(fractionB);
        assertEquals(1d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(-3);
        fractionB = new Fraction(3);
        fractionA.add(fractionB);
        assertEquals(0d, fractionA.doubleValue(), 0);
    }
    
    @Test public void minus(){
        fractionA = new Fraction(1);
        fractionA.minus(0);
        assertEquals(1d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(1);
        fractionA.minus(1);
        assertEquals(0d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(14,7);
        fractionA.minus(3);
        assertEquals(-1d, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(0);
        fractionA.minus(-1);
        assertEquals(1, fractionA.doubleValue(), 0);
        
    }
    
    @Test public void multiply(){
        fractionA = new Fraction(0);
        fractionB = new Fraction(1);
        fractionA.multiply(fractionB);
        assertEquals(0, fractionA.doubleValue(), 0);
        
        fractionA = new Fraction(11);
        fractionB = new Fraction(2);
        fractionA.multiply(fractionB);
        assertEquals(22, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(-1);
        fractionB = new Fraction(0);
        fractionA.multiply(fractionB);
        assertEquals(0, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(1);
        fractionB = new Fraction(0);
        fractionA.multiply(fractionB);
        assertEquals(0, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(0);
        fractionB = new Fraction(0);
        fractionA.multiply(fractionB);
        assertEquals(0, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(-4,2);
        fractionB = new Fraction(27,9);
        fractionA.multiply(fractionB);
        assertEquals(-6, fractionA.doubleValue(), 0);
    }
    
    @Test public void invert(){
        fractionA = new Fraction(2,1);
        fractionB = new Fraction(1,2);
        fractionA.invert();
        assertEquals(0, fractionA.compareTo(fractionB), 0);
    
        fractionA = new Fraction(2,2);
        fractionB = new Fraction(1);
        fractionA.invert();
        assertEquals(0, fractionA.compareTo(fractionB), 0);
    
        fractionA = new Fraction(1,3);
        fractionB = new Fraction(3,1);
        fractionA.invert();
        assertEquals(0, fractionA.compareTo(fractionB), 0);
    
        fractionA = new Fraction(-1,3);
        fractionB = new Fraction(3,-1);
        fractionA.invert();
        assertEquals(0, fractionA.compareTo(fractionB), 0);
    
        fractionA = new Fraction(-1,3);
        fractionB = new Fraction(-3,1);
        fractionA.invert();
        assertEquals(0, fractionA.compareTo(fractionB), 0);
    
        fractionA = new Fraction(0,1);
        exception.expect(ArithmeticException.class);
        fractionA.invert();
    }
    
    @Test public void getInvert(){
        fractionA = new Fraction(2,1);
        fractionB = new Fraction(1,2);
        assertEquals(0, fractionB.compareTo(fractionA.getInvert()), 0);
    
        fractionA = new Fraction(2,2);
        fractionB = new Fraction(1);
        assertEquals(0, fractionB.compareTo(fractionA.getInvert()), 0);
    
        fractionA = new Fraction(1,3);
        fractionB = new Fraction(3,1);
        assertEquals(0, fractionB.compareTo(fractionA.getInvert()), 0);
    
        fractionA = new Fraction(-1,3);
        fractionB = new Fraction(3,-1);
        assertEquals(0, fractionB.compareTo(fractionA.getInvert()), 0);
    
        fractionA = new Fraction(-1,3);
        fractionB = new Fraction(-3,1);
        assertEquals(0, fractionB.compareTo(fractionA.getInvert()), 0);
    
        fractionA = new Fraction(0,1);
        exception.expect(ArithmeticException.class);
        fractionB.compareTo(fractionA.getInvert());
    }
    
    @Test public void compareTo(){
        fractionA = new Fraction(2,1);
        fractionB = fractionA;
        assertEquals(0, fractionB.compareTo(fractionB), 0);
    
        fractionA = new Fraction(21,-7);
        fractionB = new Fraction(9,-3);
        assertEquals(0, fractionB.compareTo(fractionA), 0);
    
        fractionA = new Fraction(-21,7);
        fractionB = new Fraction(9,-3);
        assertEquals(0, fractionB.compareTo(fractionA), 0);
    
        fractionA = new Fraction(-22,11);
        fractionB = new Fraction(-2,1);
        assertEquals(0, fractionB.compareTo(fractionA), 0);
    
        fractionA = new Fraction(-22,-11);
        fractionB = new Fraction(-2,-1);
        assertEquals(0, fractionB.compareTo(fractionA), 0);
    
        fractionA = new Fraction(-2);
        fractionB = new Fraction(-2);
        assertEquals(0, fractionB.compareTo(fractionA), 0);
    }
    
    @Test public void doubleValue(){
        fractionA = new Fraction(2,1);
        assertEquals(2, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(3,27);
        assertEquals(3d/27, fractionA.doubleValue(), 0);
    
        fractionA = new Fraction(3,27);
        assertEquals(1d/9, fractionA.doubleValue(), 0);
    }
}