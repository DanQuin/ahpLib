package sty;

import clc.DecisionElement;
import clc.Utils;
import mtd.priority.FactoryPriorityMethod;
import problem.DecisionProblem;
import sty.RandomGenerators.RandomGenerator;
import sty.RandomGenerators.SimpleRandomGenerator;

import java.util.*;

// TODO: documentation

public class ProbabilisticSensitivityMethod extends AbstracSensitivityMethod {
    
    private final double QUARTILE1 = 0.25;
    private final double QUARTILE2 = 0.5;
    private final double QUARTILE3 = 0.75;
    
    private boolean _preserveRankOrder = false;
    private RandomGenerator _randomGenerator;
    private int _iterations = 10000;
    private ArrayList<DecisionElement> _nodes;
    private ArrayList<ComputeListener> _listeners;
    
    /**
     * Class to store the results of the simulation
     */
    public class Statistics{
        private double mean;
        private double median;
        private int min;
        private double quartile1;
        private double quartile2;
        private double quartile3;
        private int max;
        
        /**
         * Get the mean
         * @return the mean
         */
        public double getMean() {
            return mean;
        }
        
        /**
         * Get the median
         * @return the median
         */
        public double getMedian() {
            return median;
        }
        
        /**
         * Get the min
         * @return the min
         */
        public int getMin() {
            return min;
        }
        
        /**
         * Get the quartile1
         * @return the quartile1
         */
        public double getQuartile1() {
            return quartile1;
        }
        
        /**
         * Get the quartile2
         * @return the quartile2
         */
        public double getQuartile2() {
            return quartile2;
        }
        
        /**
         * Get the quartile3
         * @return the quartile3
         */
        public double getQuartile3() {
            return quartile3;
        }
        
        /**
         * Get the max
         * @return the max
         */
        public int getMax(){
            return max;
        }
    }
    
    public ProbabilisticSensitivityMethod(DecisionProblem decisionProblem,
                                          FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        super(decisionProblem, priorityMethodType);
        /* By default use uniform/simple random method */
        _randomGenerator = new SimpleRandomGenerator();
        _nodes = new ArrayList<>();
        addAllSimulationNodes(false);
    }
    
    public void setRandomGenerator(RandomGenerator randomGenerator){
        _randomGenerator = (randomGenerator != null)?
                randomGenerator:
                new SimpleRandomGenerator();
    }
    
    public RandomGenerator getRandomGenerator(){
        return _randomGenerator;
    }
    
    public int getIterations() {
        return _iterations;
    }
    
    public void setIterations(int iterations) {
        _iterations = iterations;
    }
    
    public boolean isPreserveRankOrder() {
        return _preserveRankOrder;
    }
    
    public void setPreserveRankOrder(boolean preserveRankOrder) {
        _preserveRankOrder = preserveRankOrder;
    }
    
    public ArrayList<Statistics> simulate(){
        if (_decisionProblem.getRanking(_priorityMethodType) == null){
            /* No data available to run simulation */
            return null;
        }
        
        /* Save original weights */
        Map backup = backupWeights();
        
        int rankingTable[][] = new int[_decisionProblem.getAlternativesCount()][_iterations];
        for(int iteration = 0; iteration < _iterations; iteration++){
            for(DecisionElement decisionElement: _nodes){
                randomizeNode(decisionElement);
            }
            
            ArrayList<Double> ranking = _decisionProblem.getRanking(_priorityMethodType);
            
            /* Sort ranking */
            ArrayList<Utils.IdxValue> items = Utils.IdxValue.sort(ranking);
            
            /* Assign ranking to alternatives for the iter-th iteration */
            for(int j = 0; j < items.size(); j++){
                rankingTable[items.get(j).getIndex()][iteration] = j + 1;
            }
            
            notifyListeners(iteration);
            
            /* Restore original weights before next iteration */
            restoreWeights(backup);
        }
        
        /* Summarize results */
        ArrayList<Statistics> results = summarizeResults(rankingTable);
        
        return results;
    }
    
    private ArrayList<Statistics> summarizeResults(int rankingTable[][]){
        ArrayList<Statistics> results = new ArrayList<>();// Statistics[rankingTable.length];
        for(int[] aRankingTable : rankingTable){
            /* Sort the array to easily find the quartiles, min and max (ascending order) */
            Arrays.sort(aRankingTable);
            Statistics statistics = new Statistics();
            statistics.quartile1 = quartile(aRankingTable, QUARTILE1);
            statistics.quartile2 = quartile(aRankingTable, QUARTILE2);
            statistics.quartile3 = quartile(aRankingTable, QUARTILE3);
            statistics.min = aRankingTable[0];
            statistics.max = aRankingTable[aRankingTable.length - 1];
            statistics.median = statistics.quartile2;
            statistics.mean = Utils.mean(aRankingTable);
            results.add(statistics);
        }
        return results;
    }
    
    private Map<DecisionElement, ArrayList<Double>> backupWeights(){
        Map<DecisionElement, ArrayList<Double>> backup = new HashMap<>();
        for(DecisionElement c: _nodes){
            backup.put(c, c.getPriorityVector(_priorityMethodType));
        }
        return backup;
    }
    
    private void restoreWeights(Map<DecisionElement, ArrayList<Double>> backup){
        for(DecisionElement decisionElement: _nodes){
            ArrayList<Double> weights = backup.get(decisionElement);
            decisionElement.setPriorityVector(_priorityMethodType, weights);
        }
    }
    
    private void randomizeNode(DecisionElement decisionElement){
        ArrayList<Double> originalWeights = decisionElement.getPriorityVector(_priorityMethodType);
        
        /* Generate random weights */
        ArrayList<Double> randomWeights = _randomGenerator.generateWeights(originalWeights);
    
        if (_preserveRankOrder){
            /* Weights must be ranked and then assigned */
            /* sorted from lowest ot highest */
            randomWeights.sort(null);
            /* sorted from highest to lowest */
            ArrayList<Utils.IdxValue> original = Utils.IdxValue.sort(originalWeights);
            ArrayList<Double> newWeights = new ArrayList<>();
            Utils.fillArray(newWeights, 0d, randomWeights.size());
            /* Put the weights in the same order */
            for(int i = 0; i < randomWeights.size(); i++){
                newWeights.set(original.get(i).getIndex(),
                               randomWeights.get(randomWeights.size() - i - 1));
            }
            randomWeights = newWeights;
        }
        /* Assign weights to criterion */
        decisionElement.setPriorityVector(_priorityMethodType, randomWeights);
    }
    
    private void collectNodes(DecisionElement decisionElement,
                              List<DecisionElement> decisionElementList,
                              boolean leaves){
        if (!(decisionElement.getSubCriteriaCount() == 0 && !leaves)){
            decisionElementList.add(decisionElement);
            for(DecisionElement decisionElement1: decisionElement.getSubcriteria()){
                collectNodes(decisionElement1, decisionElementList, leaves);
            }
        }
    }
    
    public final void addAllSimulationNodes(boolean includeAlternatives){
        _nodes.clear();
        collectNodes(_decisionProblem.getRoot(), _nodes, includeAlternatives);
    }
    
    public final void addAllSimulationNodes(){
        addAllSimulationNodes(true);
    }
    
    public void includeNodeToSimulation(DecisionElement decisionElement){
        if (!_nodes.contains(decisionElement)) _nodes.add(decisionElement);
    }
    
    public void removeSimulationNode(DecisionElement decisionElement){
        _nodes.remove(decisionElement);
    }
    
    public void removeAllSimulationNodes(){
        _nodes.clear();
    }
    
    public List<DecisionElement> getSimulationNodes(){
        return Collections.unmodifiableList(_nodes);
    }
    
    public boolean isSimulationNode(DecisionElement decisionElement){
        return _nodes.contains(decisionElement);
    }
    
    private static double quartile(int values[], double q){
        int n = values.length;
        /* Calculate the index */
        double x = (n - 1.0) * q;
        /* Int portion of index */
        int xInt = (int) x;
        /* Fraction part of index */
        double xFrac = x - xInt;
    
        /* Index out of bound */
        if (xInt < 0){
            return values[0];
        }
        else if (xInt >= n){
            return values[n - 1];
        }
        /* If index if int */
        if (xFrac == 0) {
            return values[xInt];
        }
        /* Calculate the geometric mean between two consecutive "intermediate" numbers */
        return ((values[xInt] + values[xInt + 1]) * 0.5);
    }
    
    public synchronized void addIterationListener(ComputeListener listener){
        if (listener != null){
            if (_listeners == null){
                _listeners = new ArrayList<>();
            }
            _listeners.add(listener);
        }
    }
    
    private void notifyListeners(int iteration){
        if (_listeners != null){
            for(ComputeListener l: _listeners){
                l.methodHasBeenComputed(null,
                                        null,
                                        null,
                                        iteration,
                                        _iterations);
            }
        }
    }
    
}
