package problem;

import clc.DecisionElement;
import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.FactoryPriorityMethod;
import sty.ComputeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO documentation

public class DecisionProblemSolver {
    /** List of methods to use for solving the problem */
    private ArrayList<FactoryPriorityMethod.PriorityMethodEnum> _priorityMethods;
    private ArrayList<FactoryConsistencyMethod.ConsistencyMethodEnum> _consistencyMethods;
    private ArrayList<FactoryErrorMethod.ErrorMethodEnum> _errorMeasureMethod;
    
    /** Listener objects that will be notified of the progress */
    private ArrayList<ComputeListener> _computeListeners;
    
    /** Current number of criterion been elicited */
    private int _criteriaElicited;
    
    /** Total number of criteria to be elicited */
    private int _criteriaToBeElicited;
    
    /** Reference to the problem */
    private DecisionProblem _decisionProblem;
    
    public DecisionProblemSolver(){
        _priorityMethods = new ArrayList<>();
        _consistencyMethods = new ArrayList<>();
        _errorMeasureMethod = new ArrayList<>();
        _computeListeners = new ArrayList<>();
    }
    
    public void addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum type){
        if(!_priorityMethods.contains(type)) _priorityMethods.add(type);
    }
    
    public void removePriorityMethod(int index){
        _priorityMethods.remove(index);
    }
    
    public void removePriorityMethod(FactoryPriorityMethod.PriorityMethodEnum type){
        _priorityMethods.remove(type);
    }
    
    public void addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum type){
        if(!_consistencyMethods.contains(type)) _consistencyMethods.add(type);
    }
    
    public void removeConsistencyMethod(int index){
        _consistencyMethods.remove(index);
    }
    
    public void removeConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum type){
        _consistencyMethods.remove(type);
    }
    
    public void addErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum type){
        if(!_errorMeasureMethod.contains(type)) _errorMeasureMethod.add(type);
    }
    
    public void removeErrorMeasureMethod(int index){
        _errorMeasureMethod.remove(index);
    }
    
    public void removeErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum type){
        _errorMeasureMethod.remove(type);
    }
    
    public int getPriorityMethodsCount(){
        return _priorityMethods.size();
    }
    
    public int getConsistencyMethodsCount(){
        return _consistencyMethods.size();
    }
    
    public int getErrorMeasureMethodsCount(){
        return _errorMeasureMethod.size();
    }
    
    public String getPriorityMethodName(int index){
        return FactoryPriorityMethod.getName(_priorityMethods.get(index));
    }
    
    public String getPriorityMethodName(FactoryPriorityMethod.PriorityMethodEnum type){
        return FactoryPriorityMethod.getName(type);
    }
    
    private List<FactoryPriorityMethod.PriorityMethodEnum> getPriorityMethods(){
        return Collections.unmodifiableList(_priorityMethods);
    }
    
    public String getConsistencyMethodName(int index){
        return FactoryConsistencyMethod.getName(_consistencyMethods.get(index));
    }
    
    public String getConsistencyMethodName(FactoryConsistencyMethod.ConsistencyMethodEnum type){
        return FactoryConsistencyMethod.getName(type);
    }
    
    private List<FactoryConsistencyMethod.ConsistencyMethodEnum> getConsistencyMethods(){
        return Collections.unmodifiableList(_consistencyMethods);
    }
    
    public String getErrorMethodName(int index){
        return FactoryErrorMethod.getName(_errorMeasureMethod.get(index));
    }
    
    public String getErrorMethodName(FactoryErrorMethod.ErrorMethodEnum type){
        return FactoryErrorMethod.getName(type);
    }
    
    private List<FactoryErrorMethod.ErrorMethodEnum> getErrorMeasureMethods(){
        return Collections.unmodifiableList(_errorMeasureMethod);
    }
    
    public void computeResults(DecisionProblem decisionProblem, boolean verbose){
        /* Only method2 can be null */
        if (verbose) addExecutionListener(((method1,
                                            method2,
                                            decisionElement,
                                            current,
                                            total)
                                                   -> System.out
                .println("compute priority " +
                                 "(" + method1 + ")" +
                                 decisionElement.getName() + ": " +
                                 current + " of " + total)));
    
        _decisionProblem = decisionProblem;
        _criteriaElicited = 0;
        _criteriaToBeElicited = getPriorityMethodsCount() * decisionProblem.getNodeCount();
        
        /* Solve every ComparisonMatrix in each node of the decisionProblem */
        /* First compute weight vector in each node for each method */
        for(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType: _priorityMethods){
            /* This is not necessary because vectors are calculated automatically
            when getRanking or getWeightVector are called */
            calculatePriorityVector(priorityMethodType, decisionProblem.getRoot());
        }
        _computeListeners.clear();
    
        /* method2 cant be null, current and total yes*/
        if (verbose) addExecutionListener((method1,
                                            method2,
                                            decisionElement,
                                            current,
                                            total)
                                                   -> System.out
                .println("compute method " +
                                 method1 + " " +
                                 "(" + method2 + ")" +
                                 decisionElement.getName()));
        
        /* Second compute consistency and error for each method */
        for(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType: _priorityMethods){
            /* Calculate the rankings for every method */
            /* Get ranking from root node (global ranking) */
            decisionProblem.getRanking(priorityMethodType);
            
            /* Compute all consistency and error measures */
            for(FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType: _consistencyMethods){
                calculateConsistency(consistencyMethodType, priorityMethodType, decisionProblem.getRoot());
            }
            
            for(FactoryErrorMethod.ErrorMethodEnum errorMeasureMethodType: _errorMeasureMethod){
                calculateErrorMeasure(errorMeasureMethodType, priorityMethodType, decisionProblem.getRoot());
            }
        }
    }
    
    private void calculateConsistency(
            FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
            DecisionElement decisionElement){
        
        decisionElement.getConsistency(consistencyMethodType,
                                       priorityMethodType);
    
        /* Notify listeners*/
        notifyListeners(FactoryConsistencyMethod.getName(consistencyMethodType),
                        FactoryPriorityMethod.getName(priorityMethodType),
                        decisionElement);
        
        for(DecisionElement child: decisionElement.getSubcriteria()){
            calculateConsistency(consistencyMethodType, priorityMethodType, child);
        }
    }
    
    private void calculateErrorMeasure(
            FactoryErrorMethod.ErrorMethodEnum errorMeasureMethodType,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
            DecisionElement decisionElement){
        
        decisionElement.getErrorMeasure(errorMeasureMethodType,
                                        priorityMethodType);
        
        /* Notify listeners*/
        notifyListeners(FactoryErrorMethod.getName(errorMeasureMethodType),
                        FactoryPriorityMethod.getName(priorityMethodType),
                        decisionElement);
        
        for(DecisionElement child: decisionElement.getSubcriteria()){
            calculateErrorMeasure(errorMeasureMethodType, priorityMethodType, child);
        }
    }
    
    
    /* Compute the weight vector for each node */
    private void calculatePriorityVector(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
                                         DecisionElement decisionElement) {
        /* Node and comparison matrix cant be null */
        if (decisionElement == null) {
            throw new IllegalArgumentException("Node cannot be null (" +
                                                       decisionElement.getName() +
                                                       ")");
        }
        if(decisionElement.getComparisonMatrix() == null ||
                decisionElement.getComparisonMatrix().getRowDimension() == 0){
            throw new IllegalArgumentException("ComparisonMatrix cannot be null (" +
                                                       decisionElement.getName() +
                                                       ")");
        }
        /* Comparison matrix must be according the alternatives number */
        if ((!decisionElement.isLeaf() &&
                decisionElement.getComparisonMatrix().getRowDimension() != decisionElement.getSubCriteriaCount()) ||
                (decisionElement.isLeaf() &&
                        decisionElement.getComparisonMatrix().getRowDimension() != _decisionProblem.getAlternativesCount())){
            throw new IllegalArgumentException("ComparisonMatrix size not equals to children size (" +
                                                       decisionElement.getName() + ")");
        }
    
        decisionElement.getPriorityVector(priorityMethodType);
        _criteriaElicited++;
        
        /* Notify listeners*/
        notifyListeners(FactoryPriorityMethod.getName(priorityMethodType),
                        null,
                        decisionElement);
        
        for(DecisionElement child: decisionElement.getSubcriteria()){
            calculatePriorityVector(priorityMethodType, child);
        }
    }
    
    private synchronized void addExecutionListener(ComputeListener listener){
        if (listener == null){
            return;
        }
        if (_computeListeners == null){
            _computeListeners = new ArrayList<>();
        }
        _computeListeners.add(listener);
    }
    
    private void notifyListeners(String abstractMethod1, String abstractMethod2, DecisionElement decisionElement){
        if (_computeListeners == null){
            return;
        }
        for(ComputeListener l: _computeListeners){
            l.methodHasBeenComputed(abstractMethod1,
                                    abstractMethod2,
                                    decisionElement,
                                    _criteriaElicited,
                                    _criteriaToBeElicited);
        }
    }
    public FactoryPriorityMethod.PriorityMethodEnum getPriorityMethodType(int i){
        return _priorityMethods.get(i);
    }
    
    public FactoryConsistencyMethod.ConsistencyMethodEnum getConsistencyMethodType(int i){
        return _consistencyMethods.get(i);
    }
    
    public FactoryErrorMethod.ErrorMethodEnum getErrorMethodType(int i){
        return _errorMeasureMethod.get(i);
    }
}
