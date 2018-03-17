package sty;

import clc.DecisionElement;
import clc.Utils;
import mtd.priority.FactoryPriorityMethod;
import problem.DecisionProblem;

import java.util.ArrayList;

// TODO: documentation

public class AbstracSensitivityMethod {
    
    DecisionProblem _decisionProblem;
    FactoryPriorityMethod.PriorityMethodEnum _priorityMethodType;
    
    AbstracSensitivityMethod(
            DecisionProblem problem, FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        _decisionProblem = problem;
        _priorityMethodType = priorityMethodType;
    }
    
    public DecisionProblem getProblem(){
        return _decisionProblem;
    }
    
    public ArrayList<Double> getRanking(){
        return _decisionProblem.getRanking(_priorityMethodType);
    }
    
    public synchronized ArrayList<Double> getRanking(DecisionElement decisionElement,
                                            ArrayList<Double> newWeights) {
        /* Get original weights */
        ArrayList<Double> weights = decisionElement.getPriorityVector(_priorityMethodType);
        
        if (weights == null){
            return null;
        }
        
        /* Calculate proportional weights, in case if there is a -1 in the newWeights array */
        newWeights = proportionalWeights(weights, newWeights);
        
        /* Calculate ranking using new weights */
        //decisionElement.getRanking(newWeights, _priorityMethodType);
        //ArrayList<Double> ranking = decisionElement.getRanking(newWeights, _priorityMethodType); //.getRanking(_priorityMethodType);
        
        /* Restore original weights */
        //decisionElement.setWeights(weigths);
        return decisionElement.getRanking(newWeights, _priorityMethodType);
    }
    
    public ArrayList<Double> getRanking(DecisionElement decisionElement,
                                        int child,
                                        double weight) {
        ArrayList<Double> newWeights = new ArrayList<>();
        Utils.fillArray(newWeights, -1, (decisionElement.hasSubCriterion()?
                                                decisionElement.getSubCriteriaCount():
                                                _decisionProblem.getAlternativesCount()));
        newWeights.set(child, weight);
        return getRanking(decisionElement, newWeights);
    }
    
    
    public ArrayList<Double> getRanking(DecisionElement decisionElement, double weight){
        DecisionElement parent = decisionElement.getParent();
        if(parent == null) throw new IllegalArgumentException("The element to change weigh can't be the root");
        return getRanking(parent, parent.getSubCriterionIndex(decisionElement), weight);
    }
    
    
    public FactoryPriorityMethod.PriorityMethodEnum getMethod() {
        return _priorityMethodType;
    }
    
    public void setMethod(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType) {
        if (_decisionProblem != null) {
            throw new IllegalArgumentException("Method " +
                                                FactoryPriorityMethod.getName(priorityMethodType) +
                                                " not found in problem results");
        }
        _decisionProblem.getRanking(priorityMethodType);
        _priorityMethodType = priorityMethodType;
    }
    
    protected ArrayList<Double> proportionalWeights(ArrayList<Double> weights,
                                                    int index,
                                                    double weight){
        ArrayList<Double> newWeights = new ArrayList<>();
        Utils.fillArray(newWeights, -1, weights.size());
        newWeights.set(index, weight);
        return proportionalWeights(weights, newWeights);
    }
    
    private ArrayList<Double> proportionalWeights(
            ArrayList<Double> weights, ArrayList<Double> newWeights){
        ArrayList<Double> w = new ArrayList<>();
        double sum0 = 0;
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0; i < newWeights.size(); i++){
            if (Double.compare(newWeights.get(i),-1) != 0){
                sum0 += newWeights.get(i);
                sum1 += newWeights.get(i) - weights.get(i);
            }
            else
                sum2 += weights.get(i);
        }
        
        if (sum0 == 1){
            for(int i = 0; i < weights.size(); i++){
                w.add((Double.compare(newWeights.get(i),-1) == 0) ?
                              0d :
                              newWeights.get(i));
            }
        }
        else{
            for(int i = 0; i < weights.size(); i++){
                w.add((Double.compare(newWeights.get(i),-1) == 0) ?
                              weights.get(i) - sum1 * weights.get(i) / sum2:
                              newWeights.get(i));
            }
        }
        return w;
    }
}
