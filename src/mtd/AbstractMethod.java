package mtd;

import clc.ComparisonMatrix;

public abstract class AbstractMethod {
    private String _name;
    private String _description;
    protected boolean _hasBeenComputed;
    protected ComparisonMatrix _matrix;
    
    
    public void recompute(){
        clearResults();
        try{
            compute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void clearResults(){
        _hasBeenComputed = false;
    }
    
    public abstract void compute();
    
    public AbstractMethod(String name, String description,
                          ComparisonMatrix comparisonMatrix){
        _name = name;
        _description = description;
        _hasBeenComputed = false;
        _matrix = comparisonMatrix;
    }
    
    public AbstractMethod(String name, ComparisonMatrix comparisonMatrix){
        this(name, "", comparisonMatrix);
    }
    
    public String getName(){
        return _name;
    }
    
    public String getDescription(){
        return _description;
    }
    
    public void setDescription(String description){
        _description = description;
    }
    
    public void setName(String name){
        _name = name;
    }
    
}
