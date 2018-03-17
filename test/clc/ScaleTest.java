package clc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ScaleTest {
    private Scale scale;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test public void getScale(){
        // Linear Scale (with out parameter)
        int scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale();
        Fraction[] scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 5;
        scale = new Scale(scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 2;
        scale = new Scale(scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        // Linear Scale (with parameter)
        scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale(Scale.TYPE.LINEAR);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 5;
        scale = new Scale(Scale.TYPE.LINEAR, scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 2;
        scale = new Scale(Scale.TYPE.LINEAR, scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        // Balanced Scale
        scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale(Scale.TYPE.BALANCED);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(scaleLength,i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 5;
        scale = new Scale(Scale.TYPE.BALANCED, scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(scaleLength,i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
    
        scaleLength = 2;
        scale = new Scale(Scale.TYPE.BALANCED, scaleLength);
        scaleAux = new Fraction[scaleLength];
        for(int i = 0; i < scaleLength; i++){
            scaleAux[i] = new Fraction(scaleLength,i + 1);
        }
        assertArrayEquals(scaleAux, scale.getScale());
        
    }
    
    @Test public void getScaleLength(){
        
        int scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale();
        assertEquals(scaleLength, scale.getScaleLength(), 0);
    
        scaleLength = 9;
        scale = new Scale(scaleLength);
        assertEquals(scaleLength, scale.getScaleLength(), 0);
    
        scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale(Scale.TYPE.LINEAR);
        assertEquals(scaleLength, scale.getScaleLength(), 0);
    
        scaleLength = Scale.DEFAULT_LENGTH;
        scale = new Scale(Scale.TYPE.BALANCED);
        assertEquals(scaleLength, scale.getScaleLength(), 0);
    
        scaleLength = 7;
        scale = new Scale(Scale.TYPE.LINEAR, scaleLength);
        assertEquals(scaleLength, scale.getScaleLength(), 0);
    
        scaleLength = 100;
        scale = new Scale(Scale.TYPE.BALANCED, scaleLength);
        assertEquals(scaleLength, scale.getScaleLength(), 0);
        
        scaleLength = -1;
        exception.expect(NegativeArraySizeException.class);
        scale = new Scale(scaleLength);
    }
}