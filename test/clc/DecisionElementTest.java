package clc;

import mss.consistency.FactoryConsistencyMethod;
import mss.errorMeasure.FactoryErrorMethod;
import mtd.priority.FactoryPriorityMethod;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DecisionElementTest {
    private DecisionElement decisionElement;
    private String testName = "Test element";
    private String testCriterionName = "Test criterion element";
    
    @Before public void setUp(){
        decisionElement = new DecisionElement(testName);
    }
    
    @Test public void getAndSetName(){
        assertEquals(testName, decisionElement.getName());
        
        decisionElement.setName(testCriterionName);
        assertEquals(testCriterionName, decisionElement.getName());
    }
    
    @Test public void getAndSetParent(){
        DecisionElement decisionElement2 = new DecisionElement(decisionElement.getName());
        assertEquals(null, decisionElement.getParent());
        
        decisionElement.setParent(decisionElement2);
        assertEquals(decisionElement2, decisionElement.getParent());
    }
    
    @Test public void equals(){
        DecisionElement decisionElement2 = new DecisionElement(decisionElement.getName());
        assertEquals(decisionElement, decisionElement2);
    }
    
    @Test public void addAndgetSubcriteria(){
        DecisionElement decisionElement2 = new DecisionElement(testCriterionName);
        decisionElement.addSubCriterion(decisionElement2, true);
        assertNotNull(decisionElement.getSubcriteria());
        assertEquals(1, decisionElement.getSubCriteriaCount(), 0);
        
        decisionElement.addSubCriterion(testCriterionName + testName, true);
        assertNotNull(decisionElement.getSubcriteria());
        assertEquals(2, decisionElement.getSubCriteriaCount(), 0);
        
        decisionElement.removeSubCriterion(decisionElement2, true);
        assertNotNull(decisionElement.getSubcriteria());
        assertEquals(1, decisionElement.getSubCriteriaCount(), 0);
        
        decisionElement.removeSubCriterion(testCriterionName + testName, true);
        assertNotNull(decisionElement.getSubcriteria());
        assertEquals(0, decisionElement.getSubCriteriaCount(), 0);
        
    }
    
    @Test public void isLeaf(){
        assertTrue(decisionElement.isLeaf());
        assertEquals(0, decisionElement.getSubCriteriaCount(), 0);
        assertFalse(decisionElement.hasSubCriterion());
        
        decisionElement.addSubCriterion(testCriterionName, true);
        assertFalse(decisionElement.isLeaf());
        assertEquals(1, decisionElement.getSubCriteriaCount(), 0);
        assertTrue(decisionElement.hasSubCriterion());
        
        decisionElement.addSubCriterion(testCriterionName, true);
        decisionElement.addSubCriterion(testName, true);
        assertFalse(decisionElement.isLeaf());
        assertEquals(2, decisionElement.getSubCriteriaCount(), 0);
        assertTrue(decisionElement.hasSubCriterion());
    }
    
    @Test public void getAndSetComparisonMatrix(){
        /* This example has been taken from thesis
        "DISEÑO E IMPLEMENTACIÓN DE UNA API AHP PARA LA TOMA DE DECISIONES CON MÚLTIPLES CRITERIOS"
        by Daniel Quinteros*/
        
        /* Root matrix */
        int dimension = 3;
        ComparisonMatrix comparisonMatrix = new ComparisonMatrix(dimension, true);
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/4);
        comparisonMatrix.set(1, 2, 1d/2);
        
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
    
        assertEquals(2, decisionElement.getComparisonMatrix().get(1, 0), 0);
        assertEquals(4, decisionElement.getComparisonMatrix().get(2, 0), 0);
        assertEquals(2, decisionElement.getComparisonMatrix().get(2, 1), 0);
    
        comparisonMatrix.set(0, 1, 1);
        comparisonMatrix.set(0, 2, 4);
        comparisonMatrix.set(1, 2, 4);
    
        String criterionC1 = "security";
        DecisionElement decisionElementC1 = new DecisionElement(criterionC1);
        decisionElementC1.setComparisonMatrix(comparisonMatrix, false);
        decisionElement.addSubCriterion(decisionElementC1, false);
    
        comparisonMatrix.set(0, 1, 2);
        comparisonMatrix.set(0, 2, 6);
        comparisonMatrix.set(1, 2, 3);
        
        String criterionC2 = "health";
        DecisionElement decisionElementC2 = new DecisionElement(criterionC2);
        decisionElementC2.setComparisonMatrix(comparisonMatrix, false);
        decisionElement.addSubCriterion(decisionElementC2, false);
    
        comparisonMatrix.set(0, 1, 1d/2);
        comparisonMatrix.set(0, 2, 1d/8);
        comparisonMatrix.set(1, 2, 1d/4);
        
        String criterionC3 = "transport";
        DecisionElement decisionElementC3 = new DecisionElement(criterionC3);
        decisionElementC3.setComparisonMatrix(comparisonMatrix, false);
        decisionElement.addSubCriterion(decisionElementC3, false);
    
        assertEquals(criterionC1, decisionElement.getSubCriterionByIndex(0).getName());
        assertEquals(criterionC2, decisionElement.getSubCriterionByIndex(1).getName());
        assertEquals(criterionC3, decisionElement.getSubCriterionByIndex(2).getName());
        
        double delta = 1e-5;
        
        /* Priority root */
        double[] priorityRootExpected = {1d/7, 2d/7, 4d/7};
        ArrayList<Double> arr = decisionElement.getPriorityVector(FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(priorityRootExpected[i], arr.get(i), delta);
        }
    
        /* Priority c1 */
        double[] priorityC1Expected = {4d/9, 4d/9, 1d/9};
        arr = decisionElement.getSubCriterionByName(criterionC1).getPriorityVector(
                FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(priorityC1Expected[i], arr.get(i), delta);
        }
    
        /* Priority c2 */
        double[] priorityC2Expected = {6d/10, 3d/10, 1d/10};
        arr = decisionElement.getSubCriterionByName(criterionC2).getPriorityVector(
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(priorityC2Expected[i], arr.get(i), delta);
        }
    
        /* Priority c3 */
        double[] priorityC3Expected = {1d/11, 2d/11, 8d/11};
        arr = decisionElement.getSubCriterionByName(criterionC3).getPriorityVector(
                FactoryPriorityMethod.PriorityMethodEnum.REVISED_GEOMETRIC_MEAN);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(priorityC3Expected[i], arr.get(i), delta);
        }
        
        /* Clear results */
        decisionElement.clearResultsRecursively(false);
        
        /* Rankings */
        double[] rankExpected = {142d/495, 877d/3465, 1594d/3465};
        
        arr = decisionElement.getRanking(FactoryPriorityMethod.PriorityMethodEnum.EIGENVECTOR);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(rankExpected[i], arr.get(i), delta);
        }
        
        arr = decisionElement.getRanking(FactoryPriorityMethod.PriorityMethodEnum.GEOMETRIC_MEAN);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(rankExpected[i], arr.get(i), delta);
        }
    
        arr = decisionElement.getRanking(FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(rankExpected[i], arr.get(i), delta);
        }
    
        arr = decisionElement.getRanking(FactoryPriorityMethod.PriorityMethodEnum.REVISED_GEOMETRIC_MEAN);
        for(int i = 0; i < arr.size(); i++){
            assertEquals(rankExpected[i], arr.get(i), delta);
        }
    
        /* Consistency */
        /* Perfect elicitation */
        comparisonMatrix.set(0, 1, 3);
        comparisonMatrix.set(0, 2, 6);
        comparisonMatrix.set(1, 2, 2);
    
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
        
        assertEquals(0, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    
        assertEquals(0, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_RATIO,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    
        assertEquals(0, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    
        assertEquals(0, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
        
        /* Errors in elicitation */
        comparisonMatrix.set(0, 1, 4);
        comparisonMatrix.set(0, 2, 2);
        comparisonMatrix.set(1, 2, 1);
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
        double consIndexExpected = 0.026810787939486547;
        double consRatioExpected = 0.03041151082065171;
        double consDetExpected = 0.5;
        double consGeomExpected = 0.03036037728554799;
        
        assertEquals(consIndexExpected, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    
        assertEquals(consRatioExpected, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.CONSISTENCY_RATIO,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
        
        assertEquals(consDetExpected, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.DETERMINANT_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    
        assertEquals(consGeomExpected, decisionElement.getConsistency(
                FactoryConsistencyMethod.ConsistencyMethodEnum.GEOMETRIC_INDEX,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
        
        /* Error measures */
        /* Perfect elicitation */
        comparisonMatrix.set(0, 1, 3);
        comparisonMatrix.set(0, 2, 6);
        comparisonMatrix.set(1, 2, 2);
        
        decisionElement.setComparisonMatrix(comparisonMatrix, true);
        decisionElement.removeAllSubCriteria();
        assertEquals(0, decisionElement.getErrorMeasure(
                FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), 0);
        assertEquals(0, decisionElement.getErrorMeasure(
                FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), 0);
        
        /* Errors in elicitation */
        comparisonMatrix.set(0, 1, 4);
        comparisonMatrix.set(0, 2, 2);
        comparisonMatrix.set(1, 2, 1);
    
        decisionElement.setComparisonMatrix(comparisonMatrix, false);
        decisionElement.removeAllSubCriteria();
    
        double errorPVExpected = 0.5;
        double errorQDExpected = 1.50027;
        assertEquals(errorPVExpected, decisionElement.getErrorMeasure(
                FactoryErrorMethod.ErrorMethodEnum.PRIORITY_VIOLATION,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
        assertEquals(errorQDExpected, decisionElement.getErrorMeasure(
                FactoryErrorMethod.ErrorMethodEnum.QUADRATIC_DEVIATION,
                FactoryPriorityMethod.PriorityMethodEnum.NORMALISED_COLUMN_SUM), delta);
    }
}