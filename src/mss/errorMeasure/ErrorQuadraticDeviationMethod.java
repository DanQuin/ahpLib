package mss.errorMeasure;

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

import java.util.ArrayList;

public class ErrorQuadraticDeviationMethod extends AbstractErrorMeasureMethod {
    
    public ErrorQuadraticDeviationMethod(String name,
                                         ComparisonMatrix comparisonMatrix,
                                         AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        _type = FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION;
    }
    
    public ErrorQuadraticDeviationMethod(String name,
                                         String description,
                                         ComparisonMatrix comparisonMatrix,
                                         AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        _type = FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION;
    }
    
    private double computeError(double aij, double wi, double wj){
        return Math.pow(aij-(wi/wj), 2);
    }
    
    @Override public void compute(){
        ArrayList<Double> weightVector = _priorityMethod.getPriorityVector();
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        if(weightVector == null){
            throw new IllegalCallerException("Need to set up method (need weight vector)");
        }
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            double result = 0;
            double partialResult = 0;
            int rows = _matrix.getRowDimension();
            int columns = _matrix.getColumnDimension();
            for(int row = 0; row < rows; row++){
                partialResult = 0;
                for(int column = 0; column < columns; column++){
                    partialResult += computeError(_matrix.get(row, column), weightVector.get(row), weightVector.get(column));
                }
                result += Math.sqrt(partialResult);
            }
            _result = result;
        }
    }
}
