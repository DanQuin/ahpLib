package mss.consistency;

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

import java.util.ArrayList;

public class ConsistencyGeometricIndexMethod extends AbstractConsistencyMethod {
    
    public ConsistencyGeometricIndexMethod(String name, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX;
    }
    
    public ConsistencyGeometricIndexMethod(String name, String description, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX;
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
            int n = _matrix.getColumnDimension();
            /* Only compute if the are at least 3 elements */
            if(n > 2){
                double result = 0;
                for(int i = 1; i <= n - 1; i++){
                    for(int j = i + 1; j <= n; j++){
                        result += Math.pow(Math.log10(
                                _matrix.get(i - 1, j - 1) *
                                        (weightVector.get(j - 1) / (weightVector.get(i - 1)))), 2);
                    }
                }
                _result = (2./((n - 1)*(n - 2))) * result;
            }
            else {
                _result = 0;
            }
            
        }
    }
}
