package mtd.priority;

import java.util.ArrayList;

public class PriorityResult {
    private AbstractPriorityMethod _priorityMethod;
    
    public PriorityResult(AbstractPriorityMethod priorityMethod){
        _priorityMethod = priorityMethod;
    }
    
    public ArrayList<Double> getPriorityVector(){
        return _priorityMethod.getPriorityVector();
    }
    public void clearResult(){
        _priorityMethod.clearResults();
    }
    
    public FactoryPriorityMethod.PriorityMethodEnum getType(){
        return _priorityMethod.getType();
    }
    
    public AbstractPriorityMethod getMethod(){
        return _priorityMethod;
    }
}
