package clc;

// TODO documentation

import mss.consistency.AbstractConsistencyMethod;
import mss.consistency.ConsistencyResult;
import mss.consistency.ConsistencyResultList;
import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.AbstractErrorMeasureMethod;
import mss.errorMeasure.ErrorResult;
import mss.errorMeasure.ErrorResultList;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.AbstractPriorityMethod;
import mtd.priority.FactoryPriorityMethod;
import mtd.priority.PriorityResult;
import mtd.priority.PriorityResultList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DecisionElement {
    /** Name of the criterion */
    private String _name;
    
    /** Parent of this node, if null this is the top-most node */
    private DecisionElement _parent;
    
    /** ArrayList of sub-criteria, if empty this is a leaf node */
    private ArrayList<DecisionElement> _subCriteria;
    
    /** Matrix with the pairwise comparison of its children */
    private ComparisonMatrix _comparisonMatrix;
    
    /** Priority method used for pairwise*/
    private PriorityResultList _priorities;
    
    /** Elicitation results (error measures) for each method that was used to computeResults the problem */
    private ConsistencyResultList _consistencies;
    
    private ErrorResultList _errorMeasures;
    
    /**
     * Creates a new instance with the given name. The list of children the matrix is set to null.
     * @param name The name of the criterion
     */
    public DecisionElement(String name){
        _name = name;
        _subCriteria = new ArrayList<>();
        _priorities = new PriorityResultList();
        _consistencies = new ConsistencyResultList();
        _errorMeasures = new ErrorResultList();
    }
    
    /**
     * Returns the name of the criterion
     * @return A String with the name
     */
    public String getName(){
        return _name;
    }
    
    /**
     * Sets the name of the criterion
     * @param name The new name for the criterion
     */
    public void setName(String name){
        _name = name;
    }
    
    /**
     * Returns the parent of this criterion.
     * @return The parent criterion, or null if the criterion has no parent
     */
    public DecisionElement getParent(){
        return _parent;
    }
    
    /**
     * Sets the parent node of this criterion
     * @param parent The parent object
     */
    public void setParent(DecisionElement parent){
        _parent = parent;
    }
    
    @Override
    public boolean equals(Object obj){
        if(this == obj) {
             return true;
        }
        if (!(obj instanceof DecisionElement)) {
               return false;
        }
        return _name.equals(((DecisionElement)obj)._name);
    }

    @Override
    public int hashCode() {
        return _name.hashCode();
    }
    
    /**
     * Adds a decisionElement as a child (sub-decisionElement) of this decisionElement
     * @param decisionElement The child decisionElement
     * @return The decisionElement that has been added, null otherwise
     */
    public DecisionElement addSubCriterion(DecisionElement decisionElement, boolean recompute) {
        if (!_subCriteria.contains(decisionElement)){
            _subCriteria.add(decisionElement);
            decisionElement.setParent(this);
            try{
                clearResults(recompute);
            }catch(Exception e){
                e.printStackTrace();
            }
            return decisionElement;
        }
        return null;
    }
    
    /**
     * Adds a new criterion with the given name as a child (sub-criterion) of this criterion
     * @param name The name of the child criterion
     * @return The criterion that has been added
     */
    public DecisionElement addSubCriterion(String name, boolean recompute) {
        DecisionElement decisionElement = new DecisionElement(name);
        return addSubCriterion(decisionElement, recompute);
    }
    
    /**
     * Returns a list with the children (sub criteria)
     * @return A list with the child criteria
     */
    public ArrayList<DecisionElement> getSubcriteria() {
        return _subCriteria;
    }
    
    /**
     * Returns the child criterion at index <code>index</code>
     * @param index The index of the child criterion
     * @return The child criterion, or IndexOutOfBoundsException if the index is out of range
     */
    public DecisionElement getSubCriterionByIndex(int index){
        return _subCriteria.get(index);
    }
    
    public DecisionElement getSubCriterionByName(String name){
        return (name == null)?
                null :
                getSubCriterionByIndex(getSubCriterionIndex(name));
    }
    
    private int getSubCriterionIndex(String name){
        for(int i = 0; i < getSubCriteriaCount(); i++){
            if(getSubCriterionByIndex(i).getName().compareTo(name) == 0) return i;
        }
        return -1;
    }
    
    /**
     * Returns the number of children
     * @return The number of child elements
     */
    public int getSubCriteriaCount(){
        if(_subCriteria != null){
            return _subCriteria.size();
        }
        return -1;
    }
    
    /**
     * Test if the actual DecisionElement is a leaf node
     * @return true if the number of children is equal to 0 (leaf node)
     */
    public boolean isLeaf(){
        return getSubCriteriaCount() == 0;
    }
    
    /**
     * Returns true if this criterion has at least one sub-criterion
     * @return true if this criterion has at least one sub-criterion
     */
    public boolean hasSubCriterion(){
        return (getSubCriteriaCount() > 0);
    }
    
    /**
     * Removes the child (sub-criterion) at index <code>index</code>
     * @param index The index to be removed
     * @return true if the element in that index has been removed
     */
    public boolean removeSubCriterion(int index, boolean recompute) {
        // indexOf in removeSubCriteria can be -1
        if (index >= 0 && index < getSubCriteriaCount()){
            _subCriteria.get(index).setParent(null);
            _subCriteria.remove(index);
            try{
                clearResults(recompute);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
    public boolean removeSubCriterion(String name, boolean recompute) {
        return removeSubCriterion(getSubCriterionIndex(name), recompute);
    }
    
    /**
     * Removes the sub-decisionElement decisionElement
     * @param decisionElement The decisionElement to be removed
     * @return true if the element in that index has been removed
     */
    public boolean removeSubCriterion(DecisionElement decisionElement, boolean recompute){
        return removeSubCriterion(getSubCriterionIndex(decisionElement), recompute);
    }
    
    /**
     * Removes all children (sub-criteria) from this criterion
     */
    public void removeAllSubCriteria(){
        for(int i = 0; i < getSubCriteriaCount(); i++){
            removeSubCriterion(i, false);
        }
        _subCriteria.clear();
    }
    
    /**
     * Returns the index of the first occurrence of the specified decisionElement.
     * @param decisionElement The decisionElement to search for
     * @return The index of the first occurrence, or -1 if the decisionElement is not found
     */
    public int getSubCriterionIndex(DecisionElement decisionElement){
        return _subCriteria.indexOf(decisionElement);
    }
    
    /**
     * Sets the comparisonMatrix with the pairwise comparisons values. The size of the comparisonMatrix
     * must match with the number of children (sub-criteria) or an IllegalArgumentException will be thrown.
     * If the criterion has no children, then the size of the comparisonMatrix is ignored.
     * The comparisonMatrix is cloned, that is, a new instance is created when setting the internal field.
     * To get the actual instance, after setting the comparisonMatrix call <code>getComparisonMatrix</code>.
     * @param comparisonMatrix the pairwise comparison <code>ComparisonMatrix</code>
     */
    public void setComparisonMatrix(ComparisonMatrix comparisonMatrix, boolean recompute) {
        /*
         * comparisonMatrix must be square
         * Accept the comparisonMatrix if the number of child-criteria is equals to comparisonMatrix size,
         * or if there are no children (in this case it is a comparisonMatrix for the alternatives)
         */
        if (comparisonMatrix.isSquare() &&
                (isLeaf() || comparisonMatrix.getRowDimension() == getSubCriteriaCount())){
            _comparisonMatrix = comparisonMatrix.cloneMatrix();
        }
        else{
            throw new IllegalArgumentException("Matrix size must match with sub-criteria number");
        }
        clearResults(recompute);
    }
    
    /**
     * Returns the matrix with the pairwise comparisons.
     * @return the PCMatrix object
     */
    public ComparisonMatrix getComparisonMatrix() {
        return _comparisonMatrix;
    }
    
    /*
     * Sets the results for the specified method.
     * @param method The method used for getting the results.
     * @param results The results obtained using the specified method
     
    public void setResult(AbstractMethod method, ResultList results) {
        _results.put(method, results);
    }
     */
    
    /*
     * Returns the results associated with the specified method, or null if
     * there are no results for the method
     * @param method The method to get the results for. If method is null, the results for the first registered method are returned.
     * @return the results associated with the specified method, or null if there are no results for the method
     
    public ResultList getResult(AbstractMethod method) {
        if (this.results.isEmpty()){
            return null;
        }
        
        if (method == null){
            return this.results.values().iterator().next();
        }
        return this.results.get(method);
    }
     */
    
    /**
     * Returns a map with all the results of this criterion.
     * @return an instance of <code>Map&lt;AbstractMethod, ResultList&gt;</code> with all results.
     
    public Map<AbstractMethod, ResultList> getResults() {
        return results;
    }*/
    
    /**
     * Clear all results of this criterion.
     * */
    public void clearResults(boolean recompute) {
        clearPriorityResults(recompute);
        clearConsistencyResults(recompute);
        clearErrorResults(recompute);
    }
    
    /**
     * Clear all error results of this criterion.
     * */
    private void clearErrorResults(boolean recompute) {
        _errorMeasures.clear();
        /*for(ErrorResult result : _errorMeasures){
            if (recompute){
                result.getMethod().recompute();
            }
            else result.getMethod().clearResults();
        }*/
        /*for(Map.Entry<AbstractErrorMeasureMethod, AbstractPriorityMethod> entry: _errorMeasures.entrySet()){
            entry.getKey().clearResults();
        }*/
        
        /*
        for(Map.Entry<FactoryErrorMethod.ErrorMethodEnum,
                LinkedHashMap<FactoryPriorityMethod.PriorityMethodEnum,
                        AbstractErrorMeasureMethod>> entry : _errorMeasures.entrySet()){
            for(Map.Entry<FactoryPriorityMethod.PriorityMethodEnum,
                    AbstractErrorMeasureMethod> entry2 : entry.getValue().entrySet()){
                if(recompute){
                    entry2.getValue().recompute();
                }
                else entry2.getValue().clearResults();
            }
        }*/
    }
    
    /**
     * Clear all consistency results of this criterion.
     * */
    private void clearConsistencyResults(boolean recompute) {
        _consistencies.clear();/*
        for(ConsistencyResult result : _consistencies){
            if (recompute){
                result.getMethod().recompute();
            }
            else result.getMethod().clearResults();
        }*/
        
        /*for(Map.Entry<FactoryConsistencyMethod.ConsistencyMethodEnum, LinkedHashMap<FactoryPriorityMethod.PriorityMethodEnum, AbstractConsistencyMethod>> entry1 :
                _consistencies.entrySet()){
            for(Map.Entry<FactoryPriorityMethod.PriorityMethodEnum, AbstractConsistencyMethod> entry2 :
                    entry1.getValue().entrySet()){
                if (recompute){
                    entry2.getValue().recompute();
                }
                else entry2.getValue().clearResults();
            }
        }*/
    }
    
    /**
     * Clear all priority results of this criterion.
     * */
    private void clearPriorityResults(boolean recompute) {
        _priorities.clear();
        /*for(PriorityResult result : _priorities){
            if (recompute){
                result.getMethod().recompute();
            }
            else result.getMethod().clearResults();
        }*/
        /*
        for(Map.Entry<FactoryPriorityMethod.PriorityMethodEnum, AbstractPriorityMethod> entry :
                _priorities.entrySet()){
            if (recompute){
                entry.getValue().recompute();
            }
            else entry.getValue().clearResults();
        }*/
    }
    
    
    /**
     * Clear all results of this criterion and results of sub-criteria recursively
     */
    public void clearResultsRecursively(boolean recompute) {
        clearResults(recompute);
        for(DecisionElement child: getSubcriteria()){
            child.clearResultsRecursively(recompute);
        }
    }
    
    /**
     * Returns the priorities of the alternatives using <code>priorityMethod</code>
     * @return An <code>LinkedHashMap</code> with the priority vector for this decision element.
     */
    public ArrayList<Double> getPriorityVector(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType) {
        AbstractPriorityMethod priorityMethod = _priorities.getForType(priorityMethodType);
        if(priorityMethod == null){
            return addPriorityMethod(priorityMethodType, true).getPriorityVector();
        }
        return priorityMethod.getPriorityVector();
    }
    
    private AbstractPriorityMethod addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum type, boolean compute) {
        AbstractPriorityMethod priorityMethod = _priorities.getForType(type);
        if(priorityMethod != null){
            return priorityMethod;
        }
        PriorityResult priorityResult = new PriorityResult(
                FactoryPriorityMethod.createMethod(type, _comparisonMatrix));
        _priorities.add(priorityResult);
        if (compute){
            try{
                (_priorities.getForType(type)).compute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return priorityResult.getMethod();
    }
    
    private AbstractConsistencyMethod addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType,
                                                           FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
                                                           boolean compute) {
        
        AbstractConsistencyMethod consistencyMethod = _consistencies.getForType(consistencyMethodType,
                                                                                     priorityMethodType);
        if(consistencyMethod != null){
            return consistencyMethod;
        }
        AbstractPriorityMethod priorityMethod = addPriorityMethod(priorityMethodType, compute);
        ConsistencyResult consistencyResult = new ConsistencyResult(FactoryConsistencyMethod.createMethod(consistencyMethodType,
                                                                                                     _comparisonMatrix,
                                                                                                     priorityMethod),
                                                                    priorityMethod);
        _consistencies.add(consistencyResult);
        
        return consistencyResult.getMethod();
    }
    
    
    private AbstractErrorMeasureMethod addErrorMethod(
            FactoryErrorMethod.ErrorMethodEnum errorMethodType,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
            boolean compute) {
        AbstractErrorMeasureMethod errorMeasureMethod = _errorMeasures.getForType(errorMethodType,
                                                                                  priorityMethodType);
        if(errorMeasureMethod != null){
            return errorMeasureMethod;
        }
        AbstractPriorityMethod priorityMethod = addPriorityMethod(priorityMethodType, compute);
        ErrorResult errorResult = new ErrorResult(FactoryErrorMethod.createMethod(errorMethodType,
                                                                                    _comparisonMatrix,
                                                                                    priorityMethod),
                                                                    priorityMethod);
        _errorMeasures.add(errorResult);
        return errorResult.getMethod();
    }
    
    /**
     * Returns a map with the ranking of alternatives for each registered elicitation method
     * @return an instance of <code>Map&lt;AbstractMethod, Map&lt;String, Fraction&gt;&gt;</code> with the ranking of alternatives for each method (with the alternative name and his ranking)
     */
    public Map<AbstractPriorityMethod, ArrayList<Double>> getPrioritiesVectors() {
        LinkedHashMap<AbstractPriorityMethod, ArrayList<Double>> priorities = new LinkedHashMap<>();
        ArrayList<Double> priority;
        for(PriorityResult method: _priorities){
            priority = method.getPriorityVector();
            priorities.put(method.getMethod(), priority);
        }
        return priorities;
    }
    
    /**
     * Returns the ranking of alternatives calculated using one elicitation method
     * @param priorityMethodType Method for which to return the ranking. If its null <code>NORMALISED_COLUMN_SUM</code> is used.
     * @return Array with ranking of alternatives or null if the node has not been elicited
     */
    public ArrayList<Double> getRanking(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType) {
        if (priorityMethodType == null){
            priorityMethodType = FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM;
        }
        return getRanking(getPriorityVector(priorityMethodType), priorityMethodType);
        
        /*// Its leaf node
        if (isLeaf()){
            ArrayList<Double> ranking = new ArrayList<>(getPriorityVector(priorityMethod).size());
            for(double fraction : getPriorityVector(priorityMethod)){
                ranking.add(fraction);
            }
            return ranking;
        }
        else{
            // It is a middle node = criteria, calculate the cumulative weights
            ArrayList<Double> cumulativeRanking = new ArrayList<Double>();
            // Auxiliar element
            double newElement;
            for(int childIdx = 0; childIdx < getSubCriteriaCount(); childIdx++){
                DecisionElement subDecisionElement = getChildrenByIndex(childIdx);
    
                // Prioridad que tiene ese hijo en el actual decisionElement
                double subDecisionElementWeight = (getPriorityVector(priorityMethod).get(childIdx)).doubleValue();
    
                // Get the weight vector of the child
                ArrayList<Double> subDecisionElementRank = subDecisionElement.getRanking(priorityMethod);
                
                for(int i = 0; i < subDecisionElementRank.size(); i++){
                    newElement = subDecisionElementWeight * (subDecisionElementRank.get(i));
                    // Todavía no se han creado todos los hijos
                    if (cumulativeRanking.size() != getSubCriteriaCount()){
                        cumulativeRanking.add(newElement);
                    } else {
                        cumulativeRanking.set(i, cumulativeRanking.get(i) + newElement);
                    }
                }
            }
            return cumulativeRanking;
        }*/
    }
    
    public Double getErrorMeasure(FactoryErrorMethod.ErrorMethodEnum errorMethod,
                                  FactoryPriorityMethod.PriorityMethodEnum priorityMethod){
        addErrorMethod(errorMethod, priorityMethod, true);
        if(isLeaf()){
            return _errorMeasures.getForType(errorMethod, priorityMethod).getResult();
        }
        // It is middle node
        double result = 0;
        List<DecisionElement> children = getSubcriteria();
        ArrayList<Double> weights = getPriorityVector(priorityMethod);
        for(int i = 0; i < children.size(); i++){
            DecisionElement child = children.get(i);
            double childTD = child.getErrorMeasure(errorMethod, priorityMethod);
            result += childTD * weights.get(i);
        }
        return result;
    }
    
    public Double getConsistency(FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType,
                                 FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        addConsistencyMethod(consistencyMethodType, priorityMethodType, true);
    
        return _consistencies.getForType(consistencyMethodType, priorityMethodType).getResult();
        /*if(isLeaf()){
            return _consistencies.getForType(consistencyMethodType, priorityMethodType).getResult();
        }
        
        // It is middle node
        double result = 0;
        List<DecisionElement> children = getSubcriteria();
        ArrayList<Double> weights = getPriorityVector(priorityMethodType);
        for(int i = 0; i < children.size(); i++){
            DecisionElement child = children.get(i);
            double childTD = child.getConsistency(consistencyMethodType, priorityMethodType);
            result += childTD * weights.get(i);
        }
        return result;*/
    }
    
    public ArrayList<Double> getRanking(ArrayList<Double> weights,
                                        FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        // Its leaf node
        if (isLeaf()){
            return weights;
        }
        else{
            // It is a middle node = criteria, calculate the cumulative weights
            ArrayList<Double> cumulativeRanking = new ArrayList<>();
            boolean filledArray = false;
            // Auxiliary element
            double newElement;
            for(int childIdx = 0; childIdx < getSubCriteriaCount(); childIdx++){
                DecisionElement subDecisionElement = getSubCriterionByIndex(childIdx);
            
                // Prioridad que tiene ese hijo en el actual decisionElement
                double subDecisionElementWeight = weights.get(childIdx);
            
                // Get the weight vector of the child
                ArrayList<Double> subDecisionElementRank = subDecisionElement.getRanking(priorityMethodType);
            
                if(!filledArray){
                    filledArray = true;
                    Utils.fillArray(cumulativeRanking, 0, subDecisionElementRank.size());
                }
                
                for(int i = 0; i < subDecisionElementRank.size(); i++){
                    newElement = subDecisionElementWeight * (subDecisionElementRank.get(i));
                    cumulativeRanking.set(i, cumulativeRanking.get(i) + newElement);
                }
            }
            return cumulativeRanking;
        }
    }
    public void setPriorityVector(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType,
                                  ArrayList<Double> weights){
        _priorities.getForType(priorityMethodType).setPriorityVector(weights);
    }
}
