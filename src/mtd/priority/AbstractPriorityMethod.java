package mtd.priority;

import clc.ComparisonMatrix;
import mtd.AbstractMethod;

import java.util.ArrayList;

// TODO documentation

public abstract class AbstractPriorityMethod extends AbstractMethod{
    
    // This contain the name and the priority associated to this decision element
    protected ArrayList<Double> _priorities;
    protected FactoryPriorityMethod.PriorityMethodEnum _type;
    
    public AbstractPriorityMethod(String name, String description,
                                  ComparisonMatrix comparisonMatrix){
        super(name, description, comparisonMatrix);
        _priorities = new ArrayList<Double>();
    }
    
    public AbstractPriorityMethod(String name, ComparisonMatrix comparisonMatrix){
        this(name, "", comparisonMatrix);
    }
    
    @Override public void recompute(){
        _priorities.clear();
        super.recompute();
    }
    
    @Override public void clearResults(){
        _priorities.clear();
        super.clearResults();
    }
    
    public ArrayList<Double> getPriorityVector() {
        if(!_hasBeenComputed){
            try{
                compute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return _priorities;
    }
    
    public void setPriorityVector(ArrayList<Double> weigths){
        if(weigths != null) _priorities = new ArrayList<>(weigths);
    }
    
    public FactoryPriorityMethod.PriorityMethodEnum getType(){
        return _type;
    }
    
}
