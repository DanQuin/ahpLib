package mss.errorMeasure;

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

import java.util.ArrayList;

public class ErrorPriorityViolationMethod extends AbstractErrorMeasureMethod {
    
    public ErrorPriorityViolationMethod(String name,
                                        ComparisonMatrix comparisonMatrix,
                                        AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        _type = FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION;
    }
    
    public ErrorPriorityViolationMethod(String name,
                                        String description,
                                        ComparisonMatrix comparisonMatrix,
                                        AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        _type = FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION;
    }
    
    private double piecewiseDecision(double aij, double wi, double wj){
        if(wi < wj && aij > 1) return 1;
        if((wi != wj && aij == 1) ||
                (wi == wj && aij != 1)) return 1d/2;
        return 0;
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
            double rows = _matrix.getRowDimension();
            double columns = _matrix.getColumnDimension();
            double result = 0;
            for(int row = 0; row < rows; row++){
                for(int column = row + 1; column < columns; column++){
                    result += piecewiseDecision(_matrix.get(row, column), weightVector.get(row), weightVector.get(column));
                }
            }
            _result = result;
        }
    }
}
