package sty.RandomGenerators;

import java.util.ArrayList;
import java.util.Collections;

// TODO: documentation

public class SimpleRandomGenerator extends java.util.Random implements RandomGenerator{
    
    private ArrayList<Double> generateRandom(int quantity){
        
        ArrayList<Double> result = new ArrayList<>();
        for(int i = 0; i < quantity; i++){
            /* This method use the default random generator number implementation*/
            result.add(nextDouble());
        }
        return result;
    }
    
    @Override
    public ArrayList<Double> generateWeights(ArrayList<Double> weights) {
        
        ArrayList<Double> randomWeights = new ArrayList<>();
        
        int quantity = weights.size();
        if (quantity == 1){
            randomWeights.add(1d);
            return randomWeights;
        }
        
        /* Generate quantity-1 random numbers */
        ArrayList<Double> randomNumbers = generateRandom(quantity - 1);
        
        /* sort numbers in ascending order*/
        Collections.sort(randomNumbers);
        
        /* Calculate random weights */
        /* This is a geometric sum (x0 + (x1-x0) + (x2-x1) + ... + (x{n-1}+xn) + (1-xn)
        * when we sum all terms -> 1*/
        randomWeights.add(randomNumbers.get(0));
        for(int i = 1; i < quantity - 1; i++){
            randomWeights.add(randomNumbers.get(i) - randomNumbers.get(i - 1));
        }
        randomWeights.add(1 - randomNumbers.get(quantity - 2));
        return randomWeights;
    }
    
    @Override
    public String toString(){
        return "Simple distribution";
    }
}
