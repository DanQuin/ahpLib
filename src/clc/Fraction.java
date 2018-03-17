package clc;

import java.math.BigInteger;

// TODO documentation
public class Fraction extends Number implements Comparable<Fraction> {
    
    // LOCAL TYPES CONSTANTS
    static final int ONE_INT = 1;
    static final int ZERO_INT = 0;
    public static final String DIVIDE_BY_ZERO = "Divide by zero.";
    
    // CONSTRUCTORS
    
    public Fraction(){
        this(ZERO_INT);
    }
    
    public Fraction(int numerator) {
        this(numerator, ONE_INT);
    }
    
    public Fraction(int numerator, int denominator) {
        if(ZERO_INT == denominator){
            throw new ArithmeticException(DIVIDE_BY_ZERO);
        }
        
        /* Only numerator should be negative. */
        if(denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        
        // Create a reduced fraction
        int gcd = gcd(numerator, denominator);
        _numerator = numerator/gcd;
        _denominator = denominator/gcd;
    }
    
    public Fraction(Fraction numerator, Fraction denominator){
        this(numerator._numerator * denominator._denominator,
             numerator._denominator * denominator._numerator);
    }
    
    // ATTRIBUTES
    private int _numerator;
    private int _denominator;
    
    // METHODS
    private static int gcd(int a, int b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }
    
    // Utility function
    private void divide(Fraction numerator, Fraction denominator){
        Fraction f = new Fraction(numerator, denominator);
        this._numerator = f._numerator;
        this._denominator = f._denominator;
    }
    
    public void divide(Fraction denominator){
        this.divide(this, denominator);
    }
    
    public void add(Fraction operator){
        Fraction f = new Fraction(this._numerator * operator._denominator +
                this._denominator * operator._numerator, this._denominator * operator._denominator);
        this._numerator = f._numerator;
        this._denominator = f._denominator;
    }
    
    public void minus(int i){
        add(new Fraction(-i));
    }
    
    public void multiply(Fraction operator){
        if (operator.doubleValue() == 0 || this.doubleValue() == 0){
            Fraction fraction = new Fraction(0);
            _numerator = fraction._numerator;
            _denominator = fraction._denominator;
        }
        else{
            this.divide(operator.getInvert());
        }
    }
    
    public void invert(){
        Fraction fraction = new Fraction(_denominator, _numerator);
        _numerator = fraction._numerator;
        _denominator = fraction._denominator;
    }
    
    public Fraction getInvert(){
        return new Fraction(_denominator, _numerator);
    }
    
    // OVERRIDES
    @Override public int compareTo(Fraction fraction){
        return  (_numerator * fraction._denominator) - (fraction._numerator * _denominator);
    }
    @Override public int intValue(){
        return _numerator/_denominator;
    }
    @Override public long longValue(){
        return _numerator/ (long) _denominator;
    }
    @Override public float floatValue(){
        return _numerator/ (float) _denominator;
    }
    @Override public double doubleValue(){
        if (_denominator == 0d){
            return Double.NaN;
        }
        return _numerator / (double) _denominator;
    }
    @Override public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return (fraction.doubleValue() == doubleValue()) ||
                (_numerator == fraction._numerator && _denominator == fraction._denominator);
    }
}
