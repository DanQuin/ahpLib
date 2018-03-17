package mtd.priority;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import clc.ComparisonMatrix;
import clc.Utils;

// TODO documentation

public class PriorityEigenvectorMethod extends AbstractPriorityMethod {
    
    public PriorityEigenvectorMethod(String name, ComparisonMatrix comparisonMatrix){
        super(name, comparisonMatrix);
        _type = FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR;
    }
    
    public PriorityEigenvectorMethod(String name, String description, ComparisonMatrix comparisonMatrix){
        super(name, description, comparisonMatrix);
        _type = FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalArgumentException("Need to set up method (need matrix)");
        }
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            EigenvalueDecomposition ev = _matrix.eig();
            Matrix vec = ev.getV();
            int columns = _matrix.getColumnDimension();
            int lambdaIdx = Utils.findPerronFrobeniusLambda(_matrix).get(Utils.PerronEnum.LAMBDA_IDX).intValue();
            double[] results = new double[columns];
            for(int row = 0; row < columns; row++){
                results[row] = vec.get(row, lambdaIdx);
            }
            results = Utils.normalizeArray(results);
            for(double result: results){
                _priorities.add(result);
            }
        }
    }
}
