package problem;

// TODO documentation

import clc.ComparisonMatrix;
import clc.DecisionElement;
import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.FactoryPriorityMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DecisionProblem {
    private String _goal;
    private ArrayList<String> _alternatives;
    private ArrayList<DecisionElement> _decisionElements;
    private DecisionElement _root;
    private String _title;
    private String _description;
    
    public DecisionProblem(){
        this("Goal");
    }
    
    public DecisionProblem(String goal){
        _goal = goal;
        _alternatives = new ArrayList<>();
        _decisionElements = new ArrayList<>();
    }
    
    public DecisionProblem(String goal, String title){
        this(goal);
        _title = title;
    }
    
    public String getGoal(){
        return _goal;
    }
    
    public void setGoal(String goal){
        _goal = goal;
        _root.setName(_goal);
    }
    
    public void addAlternative(String alternative){
        if (_alternatives.contains(alternative))
            throw new IllegalArgumentException("Duplicate alternative: " + alternative);
        _alternatives.add(alternative);
    }
    
    public void setAlternatives(String[] alternatives){
        removeAllAlternatives();
        _alternatives.addAll(Arrays.asList(Arrays.copyOf(alternatives, alternatives.length)));
    }
    
    // TODO do this to return list
    public List<String> getAlternatives(){
        return Collections.unmodifiableList(_alternatives);
    }
    
    public int getAlternativesCount(){
        return _alternatives.size();
    }
    
    public String getAlternative(int index){
        return _alternatives.get(index);
    }
    
    public void setAlternative(String name, int index){
        _alternatives.set(index, name);
    }
    
    public void removeAlternative(String alternative){
        _alternatives.remove(alternative);
    }
    
    public void removeAlternative(int index){
        _alternatives.remove(index);
    }
    
    public void removeAllAlternatives(){
        _alternatives.clear();
    }
    
    public void addSubCriterion(DecisionElement node, DecisionElement parentNode, boolean compute){
        if(node == null || parentNode == null) throw new NullPointerException("Node cant be null");
        if(!_decisionElements.contains(parentNode)) throw new IllegalArgumentException("Node parent doesn't exist");
        parentNode.addSubCriterion(node, compute);
        _decisionElements.add(node);
    }
    
    public void addSubCriterion(DecisionElement node, boolean compute){
        addSubCriterion(node, _root, compute);
    }
    
    public void removeSubCriteria(DecisionElement node){
        for(DecisionElement child : node.getSubcriteria()){
            _decisionElements.remove(child);
            removeSubCriteria(child);
        }
        _decisionElements.remove(node);
        node.removeAllSubCriteria();
    }
    
    public DecisionElement getDecisionElement(String name){
        for(DecisionElement decisionElement : _decisionElements){
            if(decisionElement.getName().compareTo(name) == 0) return decisionElement;
        }
        return null;
    }
    
    public DecisionElement getRoot(){
        return _root;
    }
    
    public void setRootNode(DecisionElement root){
        if (_root != null) removeSubCriteria(_root);
        _root = root;
        _decisionElements.add(_root);
    }
    
    private void setComparisonMatrix(ComparisonMatrix matrix, boolean recompute) {
        _root.setComparisonMatrix(matrix, recompute);
    }
    
    public ArrayList<Double> getRanking(FactoryPriorityMethod.PriorityMethodEnum method){
        return _root.getRanking(method);
    }
    
    public Double getErrorMeasure(FactoryErrorMethod.ErrorMethodEnum errorMethod,
                                  FactoryPriorityMethod.PriorityMethodEnum priorityMethod){
        return _root.getErrorMeasure(errorMethod, priorityMethod);
    }
    
    public Double getConsistency(FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType,
                                  FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
                                 DecisionElement decisionElement) {
        return decisionElement.getConsistency(consistencyMethodType, priorityMethodType);
    }
    
    public String getTitle() {
        return _title;
    }
    
    public void setTitle(String title) {
        _title = title;
    }
    
    public String getDescripttion() {
        return _description;
    }
    
    public void setDescripttion(String description) {
        _description = description;
    }
    
    public int getNodeCount(){
        return getNodeCount(_root);
    }
    
    private int getNodeCount(DecisionElement node){
        // Current DecisionElement
        int count = 1;
        if(node.getSubCriteriaCount() > 0){
            List<DecisionElement> children = node.getSubcriteria();
            for (DecisionElement child: children){
                count += getNodeCount(child);
            }
        }
        return count;
    }
}

