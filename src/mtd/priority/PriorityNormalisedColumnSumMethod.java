package mtd.priority;

import clc.ComparisonMatrix;

// TODO documentation
public class PriorityNormalisedColumnSumMethod extends AbstractPriorityMethod {
    private ComparisonMatrix _cMatrix;
    
    public PriorityNormalisedColumnSumMethod(String name, ComparisonMatrix comparisonMatrix){
        super(name, comparisonMatrix);
        _cMatrix = comparisonMatrix.cloneMatrix();
        _type = FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM;
    }
    
    public PriorityNormalisedColumnSumMethod(String name, String description, ComparisonMatrix comparisonMatrix){
        super(name, description, comparisonMatrix);
        _cMatrix = comparisonMatrix.cloneMatrix();
        _type = FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM;
    }
    
    @Override public void compute(){
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            int n = _cMatrix.getRowDimension();
            
            // Normalized matrix by column (toda la i-th columna se divide por la i-th suma)
            for(int column = 0; column < n; column++){
                _cMatrix.normalizeByColumn(column, 1);
            }
    
            // Se suma por fila y se divide en n
            double[] results = new double[n];
            for(int row = 0; row < n; row++){
                _priorities.add((_cMatrix.getRowSum(row)) / n);
            }
        }
    }
}
