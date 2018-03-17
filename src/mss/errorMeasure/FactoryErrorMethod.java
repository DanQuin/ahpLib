package mss.errorMeasure;

import clc.ComparisonMatrix;
import mtd.priority.AbstractPriorityMethod;

import java.util.LinkedHashMap;

public class FactoryErrorMethod {
    public enum ErrorMethodEnum {
        QUADRATIC_DEVIATION, PRIORITY_VIOLATION
    }
    
    private static LinkedHashMap<ErrorMethodEnum, String> _names;
    private static LinkedHashMap<ErrorMethodEnum, String> _descriptions;
    
    static{
        _names = new LinkedHashMap<>();
        _descriptions = new LinkedHashMap<>();
        _names.put(ErrorMethodEnum.QUADRATIC_DEVIATION, "Quadratic deviation");
        _names.put(ErrorMethodEnum.PRIORITY_VIOLATION, "Priority violation");
        // TODO add descriptions
        _descriptions.put(ErrorMethodEnum.QUADRATIC_DEVIATION, "Quadratic deviation description");
        _descriptions.put(ErrorMethodEnum.PRIORITY_VIOLATION, "Priority violation description");
    }
    
    private FactoryErrorMethod(){}
    
    public static AbstractErrorMeasureMethod createMethod(ErrorMethodEnum type,
                                                   ComparisonMatrix comparisonMatrix,
                                                   AbstractPriorityMethod abstractPriorityMethod){
        return createMethod(type, getName(type), comparisonMatrix, abstractPriorityMethod);
    }
    
    public static AbstractErrorMeasureMethod createMethod(ErrorMethodEnum type,
                                                   String name,
                                                   ComparisonMatrix comparisonMatrix,
                                                   AbstractPriorityMethod abstractPriorityMethod){
        return createMethod(type, name, getDescription(type), comparisonMatrix, abstractPriorityMethod);
    }
    
    public static AbstractErrorMeasureMethod createMethod(ErrorMethodEnum type,
                                                   String name,
                                                   String description,
                                                   ComparisonMatrix comparisonMatrix,
                                                   AbstractPriorityMethod abstractPriorityMethod){
        switch(type){
            case PRIORITY_VIOLATION:
                return new ErrorPriorityViolationMethod(name,
                                                        description,
                                                        comparisonMatrix,
                                                        abstractPriorityMethod);
            case QUADRATIC_DEVIATION:
                return new ErrorQuadraticDeviationMethod(name,
                                                         description,
                                                         comparisonMatrix,
                                                         abstractPriorityMethod);
        }
        return null;
    }
    
    public static String getName(ErrorMethodEnum type){
        return _names.get(type);
    }
    
    public static String getDescription(ErrorMethodEnum type){
        return _descriptions.get(type);
    }
}
