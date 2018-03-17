package mss.consistency;

// TODO documentation

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

public class ConsistencyRatioMethod extends AbstractConsistencyMethod {
    
    private ConsistencyIndexMethod consistencyIndexMethod;
    
    // TODO calculate more values
    private final double[] RandomIndex = {1, 1, 0.5247, 0.8816, 1.1086, 1.2479,
                                          1.3417, 1.4057, 1.4499, 1.4854, 1.5140,
                                          1.5365, 1.5551, 1.5713, 1.5838};
    
    public ConsistencyRatioMethod(String name, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        consistencyIndexMethod  = new ConsistencyIndexMethod(getName(), comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_RATIO;
    }
    
    public ConsistencyRatioMethod(String name, String description, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        consistencyIndexMethod  = new ConsistencyIndexMethod(getName(), comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_RATIO;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        // TODO calculate more values
        if(_matrix.getRowDimension() > RandomIndex.length){
            throw new IllegalCallerException("Dimension matrix too big (maximum: " + RandomIndex.length + ")");
        }
        if(!_hasBeenComputed){
            double result = consistencyIndexMethod.getResult();
            
            _result = result / RandomIndex[_matrix.getRowDimension()];
            _hasBeenComputed = true;
        }
    }
}
