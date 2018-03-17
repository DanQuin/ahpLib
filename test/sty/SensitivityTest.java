package sty;

import clc.ComparisonMatrix;
import clc.DecisionElement;
import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.FactoryPriorityMethod;
import org.junit.Test;
import problem.DecisionProblem;
import problem.DecisionProblemSolver;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SensitivityTest {
    
    private DecisionProblem problem;
    private DecisionProblemSolver solver;
    
    @Test public void decisionProblem(){
        solver = new DecisionProblemSolver();
        assertNotNull(solver);
    
        for(FactoryPriorityMethod.PriorityMethodEnum enume:
                FactoryPriorityMethod.PriorityMethodEnum.values()){
            solver.addPriorityMethod(enume);
        }
    
        for(FactoryConsistencyMethod.ConsistencyMethodEnum enume:
                FactoryConsistencyMethod.ConsistencyMethodEnum.values()){
            solver.addConsistencyMethod(enume);
        }
    
        for(FactoryErrorMethod.ErrorMethodEnum enume:
                FactoryErrorMethod.ErrorMethodEnum.values()){
            solver.addErrorMeasureMethod(enume);
        }
        
        /* This example has been taken from thesis
        "DISEÑO E IMPLEMENTACIÓN DE UNA API AHP PARA LA TOMA DE DECISIONES CON MÚLTIPLES CRITERIOS"
        by Daniel Quinteros
        Results from Expert Choice*/
        
        problem = new DecisionProblem("Best city");
        String alternatives[] = {"London", "Madrid", "Santiago"};
        problem.setAlternatives(alternatives);
        
        String testName = "Top criterion";
        DecisionElement decisionElement = new DecisionElement(testName);
    
        /* Root matrix */
        int dimension = 3;
        ComparisonMatrix comparisonMatrix = new ComparisonMatrix(dimension, true);
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/4);
        comparisonMatrix.set(1, 2, 1d/2);
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
    
        problem.setRootNode(decisionElement);
        
        comparisonMatrix.set(0, 1, 1);
        comparisonMatrix.set(0, 2, 4);
        comparisonMatrix.set(1, 2, 4);
    
        String criterionC1 = "security";
        DecisionElement decisionElementC1 = new DecisionElement(criterionC1);
        decisionElementC1.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC1, false);
        
        comparisonMatrix.set(0, 1, 2);
        comparisonMatrix.set(0, 2, 6);
        comparisonMatrix.set(1, 2, 3);
    
        String criterionC2 = "health";
        DecisionElement decisionElementC2 = new DecisionElement(criterionC2);
        decisionElementC2.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC2, problem.getRoot(), false);
        
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/8);
        comparisonMatrix.set(1, 2, 1d/4);
    
        String criterionC3 = "transport";
        DecisionElement decisionElementC3 = new DecisionElement(criterionC3);
        decisionElementC3.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC3, false);
        
        solver.computeResults(problem, false);
    
        NumercialSensitivityMethod sensitivityMethod =
                new NumercialSensitivityMethod(problem,
                                               FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM);
        ArrayList<RankReversal> rank = sensitivityMethod.getRankReversals(problem.getRoot(), 2);
    
        
        double delta = 1e-2;
        double weightExpected[] = {0.68, 0.41, 0.30};
        double alternativeExpected[][] = {{0, 1},{0, 2}, {1, 2}};
    
        for(int i = 0; i < rank.size(); i++){
            assertEquals(weightExpected[i], rank.get(i).getWeight(), delta);
            assertEquals(alternativeExpected[i][0], rank.get(i).getAlternative1(), delta);
            assertEquals(alternativeExpected[i][1], rank.get(i).getAlternative2(), delta);
        }
        
        /*ProbabilisticSensitivityMethod prob = new ProbabilisticSensitivityMethod(problem, FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM);
        prob.setPreserveRankOrder(true);
        prob.addAllSimulationNodes(false);
        ArrayList<Double> testse = prob.getRanking();
        System.out.println("hdjaskf");*/
    }
}