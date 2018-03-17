package mtd.priority;

import clc.ComparisonMatrix;

import java.util.LinkedHashMap;

// TODO documentation

public class FactoryPriorityMethod {
    
    public enum PriorityMethodEnum{
        EIGENVECTOR, GEOMETRIC_MEAN, NORMALISED_COLUMN_SUM, REVISED_GEOMETRIC_MEAN
    }
    
    private static LinkedHashMap<PriorityMethodEnum, String> _names = new LinkedHashMap<>();
    private static LinkedHashMap<PriorityMethodEnum, String> _descriptions = new LinkedHashMap<>();
    
    static {
        _names = new LinkedHashMap<>();
        _descriptions = new LinkedHashMap<>();
        // TODO add descriptions
        _names.put(PriorityMethodEnum.EIGENVECTOR, "Eigenvector");
        _names.put(PriorityMethodEnum.GEOMETRIC_MEAN, "Geometric mean");
        _names.put(PriorityMethodEnum.NORMALISED_COLUMN_SUM, "Normalised column sum");
        _names.put(PriorityMethodEnum.REVISED_GEOMETRIC_MEAN, "Revised geometric mean");
        _descriptions.put(PriorityMethodEnum.EIGENVECTOR, "");
        _descriptions.put(PriorityMethodEnum.GEOMETRIC_MEAN, "");
        _descriptions.put(PriorityMethodEnum.NORMALISED_COLUMN_SUM, "");
        _descriptions.put(PriorityMethodEnum.REVISED_GEOMETRIC_MEAN, "");
    }
    
    private FactoryPriorityMethod(){};

    public static AbstractPriorityMethod createMethod(PriorityMethodEnum type, ComparisonMatrix comparisonMatrix){
        return createMethod(type, getName(type), comparisonMatrix);
    }
    
    public static AbstractPriorityMethod createMethod(PriorityMethodEnum type, String name,
                                               ComparisonMatrix comparisonMatrix){
        return createMethod(type, name, getDescription(type), comparisonMatrix);
    }
    
    public static AbstractPriorityMethod createMethod(PriorityMethodEnum type,
                                               String name,
                                               String description,
                                               ComparisonMatrix comparisonMatrix){
        switch(type){
            case EIGENVECTOR:
                return new PriorityEigenvectorMethod(name, description, comparisonMatrix);
            case GEOMETRIC_MEAN:
                return new PriorityGeometricMeanMethod(name, description, comparisonMatrix);
            case NORMALISED_COLUMN_SUM:
                return new PriorityNormalisedColumnSumMethod(name, description, comparisonMatrix);
            case REVISED_GEOMETRIC_MEAN:
                return new PriorityRevisedGeomericMean(name, description, comparisonMatrix);
        }
        return null;
    }
    
    public static String getName(PriorityMethodEnum type){
        return _names.get(type);
    }
    
    public static String getDescription(PriorityMethodEnum type){
        return _descriptions.get(type);
    }
}
