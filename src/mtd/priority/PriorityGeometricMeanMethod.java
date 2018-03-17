package mtd.priority;

import clc.ComparisonMatrix;
import clc.Utils;

// TODO documentation
public class PriorityGeometricMeanMethod extends AbstractPriorityMethod {
    
    public PriorityGeometricMeanMethod(String name, ComparisonMatrix comparisonMatrix){
        super(name, comparisonMatrix);
        _type = FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN;
    }
    
    public PriorityGeometricMeanMethod(String name, String description, ComparisonMatrix comparisonMatrix){
        super(name, description, comparisonMatrix);
        _type = FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            int rows = _matrix.getRowDimension();
            double power = 1.0 / rows;
            double results[] = new double[rows];
            double mult;
            for (int row = 0; row < rows; row++) {
                mult = Utils.multiplyArray(_matrix.getRow(row));
                results[row] = Math.pow(mult, power);
            }
            results = Utils.normalizeArray(results);
            for(double result: results){
                _priorities.add(result);
            }
        }
    }
    
}
