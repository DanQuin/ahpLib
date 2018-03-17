package sty;


import clc.DecisionElement;
import clc.Utils;
import mtd.priority.FactoryPriorityMethod;
import problem.DecisionProblem;

import java.awt.*;
import java.util.ArrayList;

// TODO: documentation

public class NumercialSensitivityMethod extends AbstracSensitivityMethod{
    /* The decisionElement to be analyzed */
    private DecisionElement _decisionElement;
    
    /* The subcriterion (or alternative) to be analysed */
    private int _child;
    
    public NumercialSensitivityMethod(DecisionProblem problem,
                                      FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        super(problem, priorityMethodType);
    }
    
    public void setCriterion(DecisionElement decisionElement, int child){
        _decisionElement = decisionElement;
        _child = child;
    }
    
    public DecisionElement getDecisionElement(){
        return _decisionElement;
    }
    
    public ArrayList<Double> getRanking(double weight) {
        return getRanking(_decisionElement, _child, weight);
    }
    
    public ArrayList<RankReversal> getRankReversals() {
        return getRankReversals(_decisionElement, _child);
    }
    
    public ArrayList<RankReversal> getRankReversals(DecisionElement decisionElement, int child) {
        ArrayList<RankReversal> result = new ArrayList<>();
        
        ArrayList<Double> rankingAt0 = getRanking(decisionElement, child, 0);
        ArrayList<Double> rankingAt1 = getRanking(decisionElement, child, 1);
        
        if (rankingAt0 != null && rankingAt1 != null){
            for(int i = 0; i < rankingAt0.size()-1; i++){
                for(int j = i + 1; j < rankingAt0.size(); j++){
                    Point.Double intersectionPoint = Utils.lineIntersection(0d, rankingAt0.get(i),
                                                                            1d, rankingAt1.get(i),
                                                                            0d, rankingAt0.get(j),
                                                                            1d, rankingAt1.get(j));
                    if (intersectionPoint != null){
                        DecisionElement aDecisionElement = decisionElement;
                        int alternative = child;
                        if (decisionElement.getSubCriteriaCount() > 0){
                            aDecisionElement = decisionElement.getSubcriteria().get(child);
                            alternative = -1;
                        }
                        RankReversal reversal = new RankReversal(aDecisionElement,
                                                                 alternative,
                                                                 intersectionPoint.getX(),
                                                                 intersectionPoint.getY(),
                                                                 i, j);
                        result.add(reversal);
                    }
                }
            }
        }
        return result;
    }
    
    public ArrayList<RankReversal> getAllRankReversals(){
        ArrayList<RankReversal> list = new ArrayList<>();
        for(int i = 0; i < _decisionProblem.getRoot().getSubCriteriaCount(); i++){
            collectRankReversals(_decisionProblem.getRoot(), i, list);
        }
        return list;
    }
    
    private void collectRankReversals(DecisionElement decisionElement, int child, ArrayList<RankReversal> rankLis){
        rankLis.addAll(getRankReversals(decisionElement, child));
        if (decisionElement.getSubCriteriaCount() > 0){
            DecisionElement subCriterion = decisionElement.getSubcriteria().get(child);
            int n = (subCriterion.getSubCriteriaCount() == 0) ?
                    _decisionProblem.getAlternativesCount() :
                    subCriterion.getSubCriteriaCount();
            for(int i = 0; i < n; i++){
                collectRankReversals(subCriterion, i, rankLis);
            }
        }
    }
}