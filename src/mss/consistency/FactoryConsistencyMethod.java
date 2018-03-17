package mss.consistency;

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

import java.util.LinkedHashMap;

public class FactoryConsistencyMethod {
    
    public enum ConsistencyMethodEnum {
        CONSISTENCY_INDEX, CONSISTENCY_RATIO, DETERMINANT_INDEX, GEOMETRIC_INDEX
    }
    
    private static LinkedHashMap<ConsistencyMethodEnum, String> _names;
    private static LinkedHashMap<ConsistencyMethodEnum, String> _descriptions;
    
    static {
        _names = new LinkedHashMap<>();
        _descriptions = new LinkedHashMap<>();
        // TODO add descriptions
        _names.put(ConsistencyMethodEnum.CONSISTENCY_INDEX, "Consistency index");
        _names.put(ConsistencyMethodEnum.CONSISTENCY_RATIO, "Consistency ratio");
        _names.put(ConsistencyMethodEnum.DETERMINANT_INDEX, "Determinant index");
        _names.put(ConsistencyMethodEnum.GEOMETRIC_INDEX, "Geometric index");
        _descriptions.put(ConsistencyMethodEnum.CONSISTENCY_INDEX, "");
        _descriptions.put(ConsistencyMethodEnum.CONSISTENCY_RATIO, "");
        _descriptions.put(ConsistencyMethodEnum.DETERMINANT_INDEX, "");
        _descriptions.put(ConsistencyMethodEnum.GEOMETRIC_INDEX, "");
    }
    
    private FactoryConsistencyMethod(){}
    
    public static AbstractConsistencyMethod createMethod(ConsistencyMethodEnum type,
                                                  ComparisonMatrix comparisonMatrix,
                                                  AbstractPriorityMethod priorityMethod){
        return createMethod(type, getName(type), comparisonMatrix, priorityMethod);
    }
    
    public static AbstractConsistencyMethod createMethod(ConsistencyMethodEnum type,
                                                  String name,
                                                  ComparisonMatrix comparisonMatrix,
                                                  AbstractPriorityMethod priorityMethod){
        return createMethod(type, name, getDescription(type), comparisonMatrix, priorityMethod);
    }
    
    public static AbstractConsistencyMethod createMethod(ConsistencyMethodEnum type,
                                                  String name,
                                                  String description,
                                                  ComparisonMatrix comparisonMatrix,
                                                  AbstractPriorityMethod priorityMethod){
        switch(type){
            case CONSISTENCY_INDEX:
                return new ConsistencyIndexMethod(name, description, comparisonMatrix, priorityMethod);
            case CONSISTENCY_RATIO:
                return new ConsistencyRatioMethod(name, description, comparisonMatrix, priorityMethod);
            case DETERMINANT_INDEX:
                return new ConsistencyDeterminantIndexMethod(name, description, comparisonMatrix, priorityMethod);
            case GEOMETRIC_INDEX:
                return new ConsistencyGeometricIndexMethod(name, description, comparisonMatrix, priorityMethod);
        }
        return null;
    }
    
    public static String getName(ConsistencyMethodEnum type){
        return _names.get(type);
    }
    
    public static String getDescription(ConsistencyMethodEnum type){
        return _descriptions.get(type);
    }
    
}
