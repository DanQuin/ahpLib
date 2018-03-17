package clc;

// TODO documentation

import Jama.Matrix;

import java.util.ArrayList;

public class ComparisonMatrix extends Matrix {
    
    private Scale _scale;
    
    public ComparisonMatrix(int dimension) {
        this(dimension, dimension, false);
    }
    
    public ComparisonMatrix(int dimension, boolean identity) {
        this(dimension, dimension, identity);
    }
    
    private ComparisonMatrix(int rows, int columns) {
        this(rows, columns, false);
    }
    
    private ComparisonMatrix(int rows, int columns, boolean identity) {
        /* If rows or colums < 0 -> NegativeArraySizeException */
        super(rows, columns);
        /* Matrix must be square */
        if(rows != columns) throw new ExceptionInInitializerError();
        if(identity){
            this.fillIdentity(identity);
        }
    }
    
    public ComparisonMatrix(ArrayList<Double> weightVector) {
        this(weightVector.size(), weightVector.size());
        this.setFromVector(weightVector);
    }
    
    public final void setFromVector(ArrayList<Double> vector) {
        int n = vector.size();
        if(vector.contains(0d)) throw new ArithmeticException("Division by zero");
        if(Utils.sumArray(vector) != 1) throw new IllegalArgumentException("Vector's element must be sum 1");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                set(i, j, vector.get(i)/vector.get(j));
            }
        }
    }
    
    private void fillIdentity(boolean identity){
        for(int i = 0; i < getRowDimension(); i++){
            for(int j = 0; j < getColumnDimension(); j++){
                if (identity && i == j) set(i, j, 1);
                else setSuper(i, j, 0);
            }
        }
    }
    
    public ComparisonMatrix cloneMatrix(){
        ComparisonMatrix comparisonMatrix = new ComparisonMatrix(getRowDimension());
        comparisonMatrix.setScale(_scale);
        for(int i = 0; i < getRowDimension(); i++){
            for(int j = 0; j < getRowDimension(); j++){
                comparisonMatrix.setSuper(i, j, get(i, j));
            }
        }
        return comparisonMatrix;
    }
    
    public void swap(int row, int column) {
        if (((row >= 0) && (column >= 0) && (row < getRowDimension()) && (column < getColumnDimension()))) {
            double cij = get(row, column);
            set(row, column, get(column, row));
            set(column, row, cij);
        }
        else throw new IndexOutOfBoundsException("Swap index out of bound");
    }
    
    public void invert(int row, int column) {
        if (((row >= 0) && (column >= 0) && (row < getRowDimension()) && (column < getColumnDimension()))) {
            double cij = get(row, column);
            if(cij == 0) throw new ArithmeticException("Division by zero");
            set(row, column, 1/cij);
            set(column, row, cij);
        }
    }
    
    public Scale setScale(Scale scale) {
        return _scale = scale;
    }
    
    public Scale getScale() {
        return _scale;
    }
    
    public boolean isSquare(){
        return getRowDimension() == getColumnDimension();
    }
    
    public double getColumnSum(int column) {
        return Utils.sumArray(getColumn(column));
    }
    
    public double getRowSum(int row) {
        return Utils.sumArray(getRow(row));
    }
    
    public void normalizeByColumn(int column, double normalizedSum) {
        if(column < 0 || column > getColumnDimension()){
            throw new IndexOutOfBoundsException("Index out of range");
        }
        if (Double.isInfinite(normalizedSum)) {
            throw new IllegalArgumentException("Sum cannot be infinite");
        }
        if (Double.isNaN(normalizedSum)) {
            throw new IllegalArgumentException("Sum should be a number");
        }
        if (normalizedSum == 0) {
            throw new IllegalArgumentException("Sum cannot be 0");
        }
        double sum = getColumnSum(column);
        for(int row = 0; row < getRowDimension(); row++){
            double val = get(row, column) * normalizedSum / sum;
            /* Only set this cell, not set(column, row) */
            setSuper(row, column, val);
        }
    }
    
    public void normalizeByColumn(int column) {
        normalizeByColumn(column, 1);
    }
    
    public double[] getRow(int row){
        if(row < 0 || row > getRowDimension()){
            throw new IndexOutOfBoundsException("Index out of range");
        }
        int columns = getColumnDimension();
        double[] values = new double[getColumnDimension()];
        for(int column = 0; column < columns; column++){
            values[column] = get(row, column);
        }
        return values;
    }
    
    public double[] getColumn(int column) {
        if(column < 0 || column> getColumnDimension()){
            throw new IndexOutOfBoundsException("Index out of range");
        }
        int rows = getRowDimension();
        double[] values = new double[getRowDimension()];
        for(int row = 0; row < rows; row++){
            values[row] = get(row, column);
        }
        return values;
    }
    
    public void setSuper(int i, int j, double s){
        super.set(i, j, s);
    }
    
    @Override public void set(int i, int j, double s){
        if(s == 0) throw new IllegalArgumentException("Value to set in matrix cant be zero");
        if (!((i >= 0) && (j >= 0) && (i < getRowDimension()) && (j < getColumnDimension())))
            throw new IndexOutOfBoundsException("Set: index out of bound");
        //if(!_scale.contains(s)) throw new IllegalArgumentException("Value must be valid number from scale");
        super.set(i, j, s);
        super.set(j, i, 1/s);
    }
}
