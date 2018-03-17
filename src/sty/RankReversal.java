package sty;

import clc.DecisionElement;

// TODO: documentation

public class RankReversal{
    private DecisionElement _decisionElement;
    /* If _alternative = -1, mean that we refer to an alternative,
    other wise its a criterion */
    private int _alternative;
    private Double _weight;
    private Double _ranking;
    private int _alternative1;
    private int _alternative2;
    
    protected RankReversal(DecisionElement decisionElement,
                           int alternative,
                           double weight,
                           double ranking,
                           int alternative1,
                           int alternative2){
        _decisionElement = decisionElement;
        _alternative = alternative;
        _weight = weight;
        _ranking = ranking;
        _alternative1 = alternative1;
        _alternative2 = alternative2;
    }
    
    protected RankReversal(DecisionElement decisionElement,
                           double weight,
                           double ranking,
                           int alternative1,
                           int alternative2){
        this(decisionElement, -1, weight, ranking, alternative1, alternative2);
    }
    
    public DecisionElement getDecisionElement() {
        return _decisionElement;
    }
    
    public int getAlternative() {
        return _alternative;
    }
    
    public boolean isCriterionBased(){
        return _alternative == -1;
    }
    
    public double getWeight() {
        return _weight;
    }
    
    public double getRanking() {
        return _ranking;
    }
    
    public int getAlternative1() {
        return _alternative1;
    }
    
    public int getAlternative2() {
        return _alternative2;
    }
}