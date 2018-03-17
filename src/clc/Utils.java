package clc;

// TODO documentation

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.awt.*;
import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Utils {
    
    public static long binomial(int n, int k) {
        if(k > n - k){
            k = n - k;
        }
        
        long b = 1;
        for(int i = 1, m = n; i <= k; i++, m--){
            b = b * m / i;
        }
        return b;
    }
    
    public static double[] normalizeArray(double[] values, double normalizedSum){
        if (Double.isInfinite(normalizedSum)) {
            throw new InvalidParameterException("Sum cannot be infinite");
        }
        if (Double.isNaN(normalizedSum)) {
            throw new InvalidParameterException("Sum should be a number");
        }
        if (normalizedSum == 0) {
            throw new InvalidParameterException("Sum cannot be 0");
        }
        double sum = sumArray(values);
        if (sum == 0) {
            throw new ArithmeticException("All elements in array cannot be zero");
        }
        double result[] = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            if (Double.isNaN(values[i])) {
                result[i] = Double.NaN;
            } else {
                result[i] = values[i] * normalizedSum / sum;
            }
        }
        return result;
    }
    
    public static double[] normalizeArray(double[] values){
        return normalizeArray(values, 1);
    }
    
    public static double multiplyArray(double[] values){
        double result = 1;
        final int size = values.length;
        for (double value : values) {
            if (Double.isInfinite(value)) {
                throw new ArithmeticException("Elements cannot be infinite");
            }
            if (!Double.isNaN(value)) {
                result *= value;
            }
        }
        return result;
    }
    
    private static double sumArray(int[] values){
        double[] valuesDouble = new double[values.length];
        for(int idx = 0; idx < values.length; idx++){
            valuesDouble[idx] = values[idx];
        }
        return sumArray(valuesDouble);
    }
    
    public static double sumArray(double[] values) {
        double sum = 0;
        for (double value : values) {
            if (Double.isInfinite(value)) {
                throw new ArithmeticException("Elements cannot be infinite");
            }
            if (!Double.isNaN(value)) {
                sum += value;
            }
        }
        return sum;
    }
    
    public static int countNoFiniteNumbers(double[] values){
        int result = 0;
        for(double value: values){
            if (!Double.isFinite(value)) {
                result++;
            }
        }
        return result;
    }
    public static void fillArray(ArrayList<Double> array, double newVal, int length){
        for(int i = 0; i < length; i++){
            array.add(newVal);
        }
    }
    public static double mean(int[] values) {
        if(values.length == 0) throw new ArithmeticException("Division by zero");
        return (sumArray(values) / values.length);
    }
    public static double sumArray(ArrayList<Double> vector){
        double result = 0d;
        for(Double aVector : vector){
            result += aVector;
        }
        return result;
    }
    
    public enum PerronEnum {
        LAMBDA_IDX, LAMBDA_MAX
    }
    
    public static LinkedHashMap<PerronEnum, Double> findPerronFrobeniusLambda(ComparisonMatrix matrix){
    
        EigenvalueDecomposition ev = matrix.eig();
        Matrix evd = ev.getD();
    
        int columns = matrix.getColumnDimension();
        int lambdaColumn = 0;
        // Suppose that the max lambda is the first one (0,0)
        double lambdaMax = evd.get(lambdaColumn, lambdaColumn);
        double d;
        for (int i = 1; i < columns; i++) {
            d = evd.get(i, i);
            if (d > lambdaMax) {
                lambdaMax = d;
                lambdaColumn = i;
            }
        }
        
        LinkedHashMap<PerronEnum, Double> result = new LinkedHashMap<>();
        result.put(PerronEnum.LAMBDA_IDX, (double) lambdaColumn);
        result.put(PerronEnum.LAMBDA_MAX, lambdaMax);
        return result;
    }
    
    public static Point.Double lineIntersection(Double x11, Double y11, Double x12, Double y12,
                                           Double x21, Double y21, Double x22, Double y22){
        Double m1 =  Double.sum(y11, -y12) / Double.sum(x11, -x12);
        Double b1 =  Double.sum(x11*y12, -x12*y11) / Double.sum(x11, -x12);
        Double m2 =  Double.sum(y21, -y22) / Double.sum(x21, -x22);
        Double b2 =  Double.sum(x21*y22, -x22*y21) / Double.sum(x21, -x22);
        Double xItpt = Double.sum(b2, -b1) / Double.sum(m1, -m2);
        Double yItpt = Double.sum(m1 * xItpt, b1);
        if(xItpt.isNaN() || yItpt.isNaN()||
                xItpt.isInfinite() || yItpt.isInfinite()){
            return null;
        }
        return new Point2D.Double(xItpt, yItpt);
    }
    
    public static class IdxValue implements Comparable{
        int _index;
        double _value;
        
        public IdxValue(int index, double value){
            _index = index;
            _value = value;
        }
        
        public int compareTo(Object o) {
            return Double.compare(_value, ((IdxValue)o)._value);
        }
        
        public int getIndex(){
            return _index;
        }
        
        public double getValue(){
            return _value;
        }
        
        public static ArrayList<IdxValue> sort(ArrayList<Double> values){
            ArrayList<IdxValue> items = new ArrayList<>();
            for(int i = 0; i < values.size(); i++){
                items.add(new IdxValue(i, values.get(i)));
            }
            /* Sort in descending order (by value) */
            items.sort(null);
            Collections.reverse(items);
            return items;
        }
    }
    
}
