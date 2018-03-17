package clc;

import java.util.Arrays;

public class Scale {
    
    // Internal constant and types
    // To make the scale from 1 to 9
    protected static int DEFAULT_LENGTH = 9;
    
    public enum TYPE {
        LINEAR,
        BALANCED
    }
    
    // ATTRIBUTES
    private Fraction[] _scale;
    
    // CONSTRUCTORS
    /**
     * Default constructor.
     * <p>
     *     Calls {@link #Scale(TYPE)} with linear type scale. By default use {@link Scale#DEFAULT_LENGTH} for length.
     * </p>
     * @see Scale#DEFAULT_LENGTH
     * @see Scale.TYPE#LINEAR
     */
    public Scale(){
        this(TYPE.LINEAR, DEFAULT_LENGTH);
    }
    
    /**
     * Constructor with type param.
     * <p>
     *     Use default length by default.
     * </p>
     * @param type scale type (from enum).
     * @see Scale#DEFAULT_LENGTH
     */
    public Scale(TYPE type){
        this(type, DEFAULT_LENGTH);
    }
    
    /**
     * Constructor with length param.
     * <p>
     *     Use linear scale by default.
     * </p>
     * @param length scale length.
     * @see Scale.TYPE#LINEAR
     */
    public Scale(int length){
        this(TYPE.LINEAR, length);
    }
    
    /**
     * Constructor with type and length params.
     * @param type      scale type (view available options from enum TYPE).
     * @param length    length of the generated scale.
     * @see   TYPE
     */
    public Scale(TYPE type, int length){
        if(length < 0){
            throw new NegativeArraySizeException("Length must be greater than 0");
        }
        _scale = new Fraction[length];
        int[] scale = new int[length];
        for(int i = 1; i < length + 1; i++){
            scale[i - 1] = i;
        }
        switch(type){
            case LINEAR:
                for(int i = 1; i < length + 1; i++){
                    _scale[i - 1] = new Fraction(scale[i - 1]);
                }
                break;
            case BALANCED:
                for(int i = 0; i < _scale.length; i++){
                    _scale[i] = new Fraction(scale[scale.length - 1], scale[i]);
                }
                break;
        }
    }
    
    /**
     * Copy constructor.
     * @param scale: array with scale numbers.
     */
    public Scale(int[] scale){
        _scale = new Fraction[scale.length];
        for(int i = 1; i < scale.length + 1; i++){
            _scale[i] = new Fraction(scale[i]);
        }
    }

    // ACCESSORS
    /**
     * Get the current scale
     * @return The current scale
     */
    public Fraction[] getScale(){
        return _scale;
    }
    
    /**
     * Get the current scale length
     * @return the current scale length.
     */
    public int getScaleLength(){
        return _scale.length;
    }
    
    @Override public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Scale scale = (Scale) o;
        return Arrays.equals(_scale, scale._scale);
    }
    
    public boolean contains(double s){
        for(Fraction fraction : _scale){
            if(fraction.doubleValue() == s) return true;
        }
        return false;
    }
}
