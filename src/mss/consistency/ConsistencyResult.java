package mss.consistency;

import mtd.priority.AbstractPriorityMethod;
import mtd.priority.FactoryPriorityMethod;

public class ConsistencyResult {
    private AbstractConsistencyMethod _consistencyMethod;
    private AbstractPriorityMethod _priorityMethod;
    
    public ConsistencyResult(AbstractConsistencyMethod consistencyMethod, AbstractPriorityMethod priorityMethod){
        _consistencyMethod = consistencyMethod;
        _priorityMethod = priorityMethod;
    }
    
    public double getResult() throws Exception{
        return _consistencyMethod.getResult();
    }
    
    public boolean isType(
            FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethod,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        return (_consistencyMethod.getType() == consistencyMethod) &&
                (_priorityMethod.getType() == priorityMethodType);
    }
    public void clearResult(){
        _consistencyMethod.clearResults();
        _priorityMethod.clearResults();
    }
    public AbstractConsistencyMethod getMethod(){
        return _consistencyMethod;
    }
    public AbstractPriorityMethod getPriorityMethod(){
        return _priorityMethod;
    }
}
