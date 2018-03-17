package mss.consistency;

import clc.ComparisonMatrix;
import mtd.AbstractMethod;
import mtd.priority.AbstractPriorityMethod;

public abstract class AbstractConsistencyMethod extends AbstractMethod{
    double _result;
    AbstractPriorityMethod _priorityMethod;
    protected FactoryConsistencyMethod.ConsistencyMethodEnum _type;
    
    public FactoryConsistencyMethod.ConsistencyMethodEnum getType(){
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
    
    public AbstractConsistencyMethod(String name, String description, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, description, comparisonMatrix);
        _priorityMethod = priorityMethod;
    }
    
    public AbstractConsistencyMethod(String name, ComparisonMatrix comparisonMatrix, AbstractPriorityMethod priorityMethod){
        super(name, comparisonMatrix);
        _priorityMethod = priorityMethod;
    }
    
    @Override public void recompute() {
        _hasBeenComputed = false;
        try{
            compute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override public void clearResults(){
        _hasBeenComputed = false;
    }
    
}
