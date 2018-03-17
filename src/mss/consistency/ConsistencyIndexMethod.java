package mss.consistency;

import clc.ComparisonMatrix;
import clc.Utils;
import mtd.priority.AbstractPriorityMethod;

public class ConsistencyIndexMethod extends AbstractConsistencyMethod {
    
    public ConsistencyIndexMethod(String name, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX;
    }
    
    public ConsistencyIndexMethod(String name, String description, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        if(!_hasBeenComputed){
            double rows = _matrix.getRowDimension();
            _result =  Math.abs(Utils.findPerronFrobeniusLambda(_matrix).get(Utils.PerronEnum.LAMBDA_MAX) -  rows)
                    / (rows - 1);
            _hasBeenComputed = true;
        }
    }
}
