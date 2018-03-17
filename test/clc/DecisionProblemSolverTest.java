package clc;

import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.FactoryPriorityMethod;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import problem.DecisionProblem;
import problem.DecisionProblemSolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DecisionProblemSolverTest {
    
    private double delta;
    private double rankingExpected[];
    /* Root node */
    private double consIdxExpected;
    private double consRatExpected;
    
    private DecisionProblem problem;
    private DecisionProblemSolver solver;
    private String JUnitTestName;
    
    @Rule public TestName name = new TestName();
    
    @After public void asserts(){
        System.out.println(JUnitTestName);
        for(int i = 0; i < solver.getPriorityMethodsCount(); i++){
            for(int j = 0; j < rankingExpected.length; j++){
                assertEquals(rankingExpected[j],
                             problem.getRanking(solver.getPriorityMethodType(i)).get(j),
                             delta);
            }
            assertEquals(consIdxExpected,
                         problem.getConsistency(FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX,
                                                solver.getPriorityMethodType(i),
                                                problem.getRoot()),
                         delta);
            assertEquals(consRatExpected,
                         problem.getConsistency(FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_RATIO,
                                                solver.getPriorityMethodType(i),
                                                problem.getRoot()),
                         delta);
        }
    }
    
    @Test public void decisionProblem1(){
        solver = new DecisionProblemSolver();
        assertNotNull(solver);
    
        solver.addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM);
        solver.addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR);
        solver.addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN);
        solver.addPriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR);
        assertEquals(3, solver.getPriorityMethodsCount(), 0);
        solver.removePriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN);
        assertEquals(2, solver.getPriorityMethodsCount(), 0);
        solver.removePriorityMethod(FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN);
        assertEquals(2, solver.getPriorityMethodsCount(), 0);
    
        for(FactoryPriorityMethod.PriorityMethodEnum enume:
        FactoryPriorityMethod.PriorityMethodEnum.values()){
            solver.addPriorityMethod(enume);
        }
    
        solver.addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX);
        solver.addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX);
        solver.addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX);
        solver.addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX);
        assertEquals(3, solver.getConsistencyMethodsCount(), 0);
        solver.removeConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX);
        assertEquals(2, solver.getConsistencyMethodsCount(), 0);
        solver.removeConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX);
        assertEquals(2, solver.getConsistencyMethodsCount(), 0);
        solver.removeConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX);
        assertEquals(1, solver.getConsistencyMethodsCount(), 0);
        solver.addConsistencyMethod(FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX);
        assertEquals(2, solver.getConsistencyMethodsCount(), 0);
    
        for(FactoryConsistencyMethod.ConsistencyMethodEnum enume:
                FactoryConsistencyMethod.ConsistencyMethodEnum.values()){
            solver.addConsistencyMethod(enume);
        }
        
        solver.addErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION);
        solver.addErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION);
        assertEquals(1, solver.getErrorMeasureMethodsCount(), 0);
        solver.addErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION);
        assertEquals(2, solver.getErrorMeasureMethodsCount(), 0);
        solver.removeErrorMeasureMethod(FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION);
        assertEquals(1, solver.getErrorMeasureMethodsCount(), 0);
    
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
        for(String alternative : alternatives){
            problem.addAlternative(alternative);
        }
        try{
            problem.addAlternative(alternatives[1]);
        } catch (Exception e){
            assertEquals("Duplicate alternative: " + alternatives[1], e.getMessage());
        }
        assertEquals(alternatives.length, problem.getAlternativesCount(), 0);
        problem.removeAlternative(alternatives[0]);
        assertEquals(alternatives.length - 1, problem.getAlternativesCount(), 0);
        problem.addAlternative(alternatives[0]);
        assertEquals(alternatives.length, problem.getAlternativesCount(), 0);
        
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
        assertEquals(1, problem.getNodeCount(), 0);
        
        comparisonMatrix.set(0, 1, 1);
        comparisonMatrix.set(0, 2, 4);
        comparisonMatrix.set(1, 2, 4);
    
        String criterionC1 = "security";
        DecisionElement decisionElementC1 = new DecisionElement(criterionC1);
        decisionElementC1.setComparisonMatrix(comparisonMatrix, false);
        
        problem.addSubCriterion(decisionElementC1, false);
        assertEquals(2, problem.getNodeCount(), 0);
    
        comparisonMatrix.set(0, 1, 2);
        comparisonMatrix.set(0, 2, 6);
        comparisonMatrix.set(1, 2, 3);
    
        String criterionC2 = "health";
        DecisionElement decisionElementC2 = new DecisionElement(criterionC2);
        decisionElementC2.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC2, problem.getRoot(), false);
        assertEquals(3, problem.getNodeCount(), 0);
        
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/8);
        comparisonMatrix.set(1, 2, 1d/4);
    
        String criterionC3 = "transport";
        DecisionElement decisionElementC3 = new DecisionElement(criterionC3);
        decisionElementC3.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC3, false);
        assertEquals(4, problem.getNodeCount(), 0);
        
        assertEquals(criterionC1, decisionElement.getSubCriterionByIndex(0).getName());
        assertEquals(criterionC2, decisionElement.getSubCriterionByIndex(1).getName());
        assertEquals(criterionC3, decisionElement.getSubCriterionByIndex(2).getName());
        
        solver.computeResults(problem, false);
    
        delta = 1e-2;
        rankingExpected = new double[]{0.28, 0.25, 0.46};
        /* Root node */
        consIdxExpected = 0;
        consRatExpected = 0;
        JUnitTestName = name.getMethodName();
    }
    
    @Test public void decisionProblem2(){
        solver = new DecisionProblemSolver();
        assertNotNull(solver);
    
        for(FactoryPriorityMethod.PriorityMethodEnum enume:
                FactoryPriorityMethod.PriorityMethodEnum.values()){
            solver.addPriorityMethod(enume);
        }
        assertEquals(FactoryPriorityMethod.PriorityMethodEnum.values().length,
                     solver.getPriorityMethodsCount(),
                     0);
    
        for(FactoryConsistencyMethod.ConsistencyMethodEnum enume:
                FactoryConsistencyMethod.ConsistencyMethodEnum.values()){
            solver.addConsistencyMethod(enume);
        }
        assertEquals(FactoryConsistencyMethod.ConsistencyMethodEnum.values().length,
                     solver.getConsistencyMethodsCount(),
                     0);
    
        for(FactoryErrorMethod.ErrorMethodEnum enume:
                FactoryErrorMethod.ErrorMethodEnum.values()){
            solver.addErrorMeasureMethod(enume);
        }
        assertEquals(FactoryErrorMethod.ErrorMethodEnum.values().length,
                     solver.getErrorMeasureMethodsCount(),
                     0);
        
        /* This example has been taken from article
        "Investigación de Operaciones II"
        by María Cristina Riff
        Results from Expert Choice*/
        
        problem = new DecisionProblem("Best job");
        String alternatives[] = {"J1", "J2", "J3"};
        
        problem.setAlternatives(alternatives);
        
        assertEquals(alternatives.length, problem.getAlternativesCount(), 0);
        problem.removeAlternative(alternatives[0]);
        assertEquals(alternatives.length - 1, problem.getAlternativesCount(), 0);
        problem.addAlternative(alternatives[0]);
        assertEquals(alternatives.length, problem.getAlternativesCount(), 0);
    
        String testName = "Objectives";
        DecisionElement decisionElement = new DecisionElement(testName);
    
        /* Root matrix */
        int dimension = 4;
        ComparisonMatrix comparisonMatrix = new ComparisonMatrix(dimension, true);
        comparisonMatrix.set(0, 1, 5d);
        comparisonMatrix.set(0, 2, 2d);
        comparisonMatrix.set(0, 3, 4d);
        comparisonMatrix.set(1, 2, 1d/2);
        comparisonMatrix.set(1, 3, 1d/2);
        comparisonMatrix.set(2, 3, 2d);
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
    
        problem.setRootNode(decisionElement);
        assertEquals(1, problem.getNodeCount(), 0);
    
        dimension = 3;
        comparisonMatrix = new ComparisonMatrix(dimension, true);
    
        comparisonMatrix.set(0, 1, 2);
        comparisonMatrix.set(0, 2, 4);
        comparisonMatrix.set(1, 2, 2);
    
        String criterionC1 = "Salary";
        DecisionElement decisionElementC1 = new DecisionElement(criterionC1);
        decisionElementC1.setComparisonMatrix(comparisonMatrix, false);
    
        problem.addSubCriterion(decisionElementC1, false);
        assertEquals(2, problem.getNodeCount(), 0);
    
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/3);
        comparisonMatrix.set(1, 2, 1d/3);
    
        String criterionC2 = "Life";
        DecisionElement decisionElementC2 = new DecisionElement(criterionC2);
        decisionElementC2.setComparisonMatrix(comparisonMatrix, false);
        
        problem.addSubCriterion(decisionElementC2, problem.getRoot(), false);
        assertEquals(3, problem.getNodeCount(), 0);
    
        comparisonMatrix.set(0, 1, 1d/7);
        comparisonMatrix.set(0, 2, 1d/3);
        comparisonMatrix.set(1, 2, 3d);
    
        String criterionC3 = "Interest";
        DecisionElement decisionElementC3 = new DecisionElement(criterionC3);
        decisionElementC3.setComparisonMatrix(comparisonMatrix, false);
        
        problem.addSubCriterion(decisionElementC3, false);
        assertEquals(4, problem.getNodeCount(), 0);
    
        comparisonMatrix.set(0, 1, 1d/4);
        comparisonMatrix.set(0, 2, 1d/7);
        comparisonMatrix.set(1, 2, 2d);
    
        String criterionC4 = "Place";
        DecisionElement decisionElementC4 = new DecisionElement(criterionC4);
        decisionElementC4.setComparisonMatrix(comparisonMatrix, false);
        
        problem.addSubCriterion(decisionElementC4, false);
        assertEquals(5, problem.getNodeCount(), 0);
    
        assertEquals(criterionC1, decisionElement.getSubCriterionByIndex(0).getName());
        assertEquals(criterionC2, decisionElement.getSubCriterionByIndex(1).getName());
        assertEquals(criterionC3, decisionElement.getSubCriterionByIndex(2).getName());
        assertEquals(criterionC4, decisionElement.getSubCriterionByIndex(3).getName());
    
        solver.computeResults(problem, false);
    
        delta = 1e-2;
        rankingExpected = new double[]{0.34, 0.41, 0.24};
        /* Root node */
        consIdxExpected = 0.015;
        consRatExpected = 0.014;
        JUnitTestName = name.getMethodName();
    }
}