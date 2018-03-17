package mtd.priority;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import clc.ComparisonMatrix;
import clc.Utils;

// TODO documentation

public class PriorityRevisedGeomericMean extends AbstractPriorityMethod {
    private ComparisonMatrix _cMatrix;
    
    public PriorityRevisedGeomericMean(String name, ComparisonMatrix comparisonMatrix){
        super(name, comparisonMatrix);
        _cMatrix = new ComparisonMatrix(comparisonMatrix.getRowDimension(), false);
        _type = FactoryPriorityMethod.PriorityMethodEnum.REVISED_GEOMETRIC_MEAN;
    }
    
    public PriorityRevisedGeomericMean(String name, String description, ComparisonMatrix comparisonMatrix){
        super(name, description, comparisonMatrix);
        _cMatrix = new ComparisonMatrix(comparisonMatrix.getRowDimension(), false);
        _type = FactoryPriorityMethod.PriorityMethodEnum.REVISED_GEOMETRIC_MEAN;
    }
    
    @Override public void compute(){
        if(_matrix == null){
            throw new IllegalCallerException("Need to set up method (need matrix)");
        }
        if(!_hasBeenComputed){
            _hasBeenComputed = true;
            // Count unknowns values per row
            int rows = _matrix.getRowDimension(), columns = _matrix.getColumnDimension();
            int[] unknownPerRow = new int[rows];
            for(int row = 0; row < rows; row++){
                unknownPerRow[row] = Utils.countNoFiniteNumbers(_matrix.getRow(row));
            }
            double matrixValue, newValue;
            for(int row = 0; row < rows; row++){
                for(int column = 0; column < columns; column++){
                    matrixValue = _matrix.get(row, column);
                    if(row == column){
                        newValue = 1 + unknownPerRow[row];
                    }
                    else if(Double.isFinite(matrixValue)){
                        newValue = matrixValue;
                    }
                    else {
                        newValue = 0;
                    }
                    _cMatrix.setSuper(row, column, newValue);
                }
            }
            
            // Compute max lambda
            int lambdaMaxIdx = (int) Math.abs(Utils.findPerronFrobeniusLambda(_cMatrix).get(Utils.PerronEnum.LAMBDA_IDX));
            EigenvalueDecomposition ev = _cMatrix.eig();
            Matrix vec = ev.getV();
            double[] results = new double[columns];
            for(int row = 0; row < columns; row++){
                results[row] = vec.get(row, lambdaMaxIdx);
            }
            results = Utils.normalizeArray(results);
            for(double result: results){
                _priorities.add(result);
            }
        }
    }
}
