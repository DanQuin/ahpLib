package mss.errorMeasure;

import mtd.priority.AbstractPriorityMethod;

public class ErrorResult {
    private AbstractErrorMeasureMethod _errorMethod;
    private AbstractPriorityMethod _priorityMethod;
    
    public ErrorResult(AbstractErrorMeasureMethod errorMethod, AbstractPriorityMethod priorityMethod){
        _errorMethod = errorMethod;
        _priorityMethod = priorityMethod;
    }
    
    public double getResult(){
        return _errorMethod.getResult();
    }
    public void clearResult(){
        _errorMethod.clearResults();
        _priorityMethod.clearResults();
    }
    public AbstractErrorMeasureMethod getMethod(){
        return _errorMethod;
    }
    public AbstractPriorityMethod getPriorityMethod(){
        return _priorityMethod;
    }
}
