package mss.consistency;

import clc.ComparisonMatrix;
import clc.Utils;
import mtd.priority.AbstractPriorityMethod;

public class ConsistencyDeterminantIndexMethod extends AbstractConsistencyMethod {
    
    public ConsistencyDeterminantIndexMethod(String name, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX;
    }
    
    public ConsistencyDeterminantIndexMethod(String name, String description, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix, priorityMethod);
        _type = FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            int n = _matrix.getRowDimension();
            double result = 0;
            for(int i = 1; i <= n - 2; i++){
                for(int j = i + 1; j <= n - 1; j++){
                    for(int k = j + 1; k <= n; k++){
                        result = result +
                                ((_matrix.get(i - 1, k - 1))
                                        / (_matrix.get(i - 1, j - 1) * _matrix.get(j - 1, k - 1)))
                                +
                                ((_matrix.get(i - 1, j - 1) * _matrix.get(j - 1, k - 1))
                                        / (_matrix.get(i - 1, k - 1))) - 2;
                    }
                }
            }
            _result = result / Utils.binomial(n, 3);
        }
    }
}
