package mss.errorMeasure;

import clc.ComparisonMatrix;
import mtd.AbstractMethod;
import mtd.priority.AbstractPriorityMethod;
// TODO documentation

public abstract class AbstractErrorMeasureMethod extends AbstractMethod{
    double _result;
    protected AbstractPriorityMethod _priorityMethod;
    protected FactoryErrorMethod.ErrorMethodEnum _type;
    
    public AbstractErrorMeasureMethod(String name,
                                      String description,
                                      ComparisonMatrix comparisonMatrix,
                                      AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix);
        _priorityMethod = priorityMethod;
    }
    
    public AbstractErrorMeasureMethod(String name,
                                      ComparisonMatrix comparisonMatrix,
                                      AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix);
        _priorityMethod = priorityMethod;
    }
    
    public FactoryErrorMethod.ErrorMethodEnum getType(){
        return _type;
    }
    
    public double getResult(){
        if(!_hasBeenComputed){
            try{
                compute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return _result;
    }
}
