package clc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class ComparisonMatrixTest {
    
    private int length;
    private ArrayList<Double> vector;
    private ComparisonMatrix comparisonMatrix;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Before public void setUp(){
        length = 5;
        Random random = new Random(0);
        double[] doubles = new double[length];
        vector = new ArrayList<>();
        for(int i = 0; i < length; i++){
            doubles[i] = random.nextInt(10) + 1;
        }
        try{
            doubles = Utils.normalizeArray(doubles);
        }catch(Exception e){
            e.printStackTrace();
        }
    
        for(int i = 0; i < length; i++){
            vector.add(doubles[i]);
        }
    }
    
    @Test public void constructors(){
        /* No fill matrix */
        comparisonMatrix = new ComparisonMatrix(2);
        assertNotNull(comparisonMatrix);
    
        comparisonMatrix = new ComparisonMatrix(99);
        assertNotNull(comparisonMatrix);
    
        assertEquals(0, comparisonMatrix.get(0,0), 0);
        assertEquals(0, comparisonMatrix.get(99 - 1, 99 - 1), 0);
        
        comparisonMatrix = new ComparisonMatrix(vector);
        assertNotNull(comparisonMatrix);
    
        assertEquals(1, comparisonMatrix.get(0,0), 0);
        assertEquals(1, comparisonMatrix.get(length - 1, length - 1), 0);
        
        /* Identity matrix */
        comparisonMatrix = new ComparisonMatrix(2,true);
        assertNotNull(comparisonMatrix);
    
        comparisonMatrix = new ComparisonMatrix(99, true);
        assertNotNull(comparisonMatrix);
    
        assertEquals(1, comparisonMatrix.get(0,0), 0);
        assertEquals(1, comparisonMatrix.get(length - 1, length - 1), 0);
    
        exception.expect(NegativeArraySizeException.class);
        comparisonMatrix = new ComparisonMatrix(-1);
    }
    
    @Test public void setFromVector(){
        comparisonMatrix = new ComparisonMatrix(length);
        comparisonMatrix.setFromVector(vector);
    }
    
    @Test public void cloneMatrix(){
        comparisonMatrix = new ComparisonMatrix(length);
        comparisonMatrix.setFromVector(vector);
        comparisonMatrix.setScale(new Scale());
        ComparisonMatrix comparisonMatrix2 = comparisonMatrix.cloneMatrix();
        assertNotNull(comparisonMatrix2);
        assertTrue(comparisonMatrix.isSquare() == comparisonMatrix2.isSquare());
        for(int i = 0; i < length; i++){
            assertTrue(Arrays.equals(comparisonMatrix.getRow(i), comparisonMatrix2.getRow(i)));
            assertEquals(length, comparisonMatrix.getRow(i).length, 0);
        }
        assertTrue(comparisonMatrix.getScale().equals(comparisonMatrix2.getScale()));
    
        
        int column = 0;
        comparisonMatrix.normalizeByColumn(column);
        assertEquals(1, comparisonMatrix.getColumnSum(column), 0);
    
        column = 1;
        comparisonMatrix2.normalizeByColumn(column, 10);
        assertEquals(10, comparisonMatrix2.getColumnSum(column), 0);
    }
}