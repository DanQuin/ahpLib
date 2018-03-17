package sty.RandomGenerators;

import java.util.ArrayList;

// TODO: documentation

/**
 * Generates a random value from the gamma distribution
 * Algorithms taken from: http://mallet.cs.umass.edu/api/cc/mallet/util/Randoms.html
 */
public class GammaRandomGenerator implements RandomGenerator {
    
    private static final double ONE_OVER_E = Math.exp(-1);
    private Double _beta;
    
    public GammaRandomGenerator(){
        this(1d);
    }
    
    public GammaRandomGenerator(Double beta){
        super();
        _beta = beta;
    }
    
    /** Return a random double drawn from a Gamma distribution with
     * mean <code>alpha*beta</code> and variance <code>alpha*beta^2</code>. */
    public synchronized double nextGamma(double alpha, double beta) {
        return nextGamma(alpha, beta,0);
    }
    
    /** Return a random double drawn from a Gamma distribution
     *  with mean <code>alpha*beta+lambda</code> and variance <code>alpha*beta^2</code>.
     *  Note that this means the pdf is:
     *     <code>frac{ x^{alpha-1} exp(-x/beta) }{ beta^alpha Gamma(alpha) }</code>
     *  in other words, beta is a "scale" parameter. An alternative
     *  parameterization would use 1/beta, the "rate" parameter.
     */
    public synchronized double nextGamma(double alpha, double beta, double lambda) {
        // Random Uniform Generator
        java.util.Random rug = new java.util.Random();
        rug.setSeed(System.nanoTime());
        rug.nextDouble();
        
        double gamma = 0;
        if (alpha <= 0 || beta <= 0) {
            throw new IllegalArgumentException ("alpha and beta must be strictly positive.");
        }
        if (alpha < 1) {
            double b, p;
            boolean flag = false;
            
            b = 1 + alpha * ONE_OVER_E;
            
            while (!flag) {
                p = b * rug.nextDouble();
                if (p > 1) {
                    gamma = -Math.log((b - p) / alpha);
                    if (rug.nextDouble() <= Math.pow(gamma, alpha - 1)) {
                        flag = true;
                    }
                }
                else {
                    gamma = Math.pow(p, 1.0/alpha);
                    if (rug.nextDouble() <= Math.exp(-gamma)) {
                        flag = true;
                    }
                }
            }
        }
        else if (alpha == 1) {
            // Gamma(1) is equivalent to Exponential(1). We can
            // sample from an exponential by inverting the CDF:
            gamma = -Math.log (rug.nextDouble());
            // There is no known closed form for Gamma(alpha != 1)...
        }
        else {
            
            // This is Best's algorithm: see pg 410 of
            // Luc Devroye's "non-uniform random variate generation"
            // This algorithm is constant time for alpha > 1.
            
            double b = alpha - 1;
            double c = 3 * alpha - 0.75;
            
            double u, v;
            double w, y, z;
            
            boolean accept = false;
            
            while (! accept) {
                u = rug.nextDouble();
                v = rug.nextDouble();
                
                w = u * (1 - u);
                y = Math.sqrt( c / w ) * (u - 0.5);
                gamma = b + y;
                
                if (gamma >= 0.0) {
                    z = 64 * w * w * w * v * v;  // ie: 64 * w^3 v^2
                    
                    accept = z <= 1.0 - ((2 * y * y) / gamma);
                    
                    if (! accept) {
                        accept = (Math.log(z) <=
                                2 * (b * Math.log(gamma / b) - y));
                    }
                }
            }
			
			/* // Old version, uses time linear in alpha
			   double y = -Math.log (nextUniform ());
			   while (nextUniform () > Math.pow (y * Math.exp (1 - y), alpha - 1))
			   y = -Math.log (nextUniform ());
			   gamma = alpha * y;
			*/
        }
        return beta*gamma+lambda;
    }
    
    /**
     * Generates random weights from the Gamma distribution using the given weights as the alpha parameter of the distribution
     * @param weights The values to be used as the alpha parameter of the distribution
     * @return an array with the random weights
     */
    @Override
    public ArrayList<Double> generateWeights(ArrayList<Double> weights) {
        int quantity = weights.size();
        
        /* Generate quantity random numbers */
        ArrayList<Double> randomNumbers = new ArrayList<>();
        double sum = 0;
        for(int i = 0; i < quantity; i++){
            randomNumbers.add(nextGamma(weights.get(i), _beta));
            sum += randomNumbers.get(i);
        }
        
        /* Calculate random weights */
        ArrayList<Double> randomWeights = new ArrayList<>();
        for(int i = 0; i < quantity; i++){
            randomWeights.add(randomNumbers.get(i) / sum);
        }
        
        return randomWeights;
    }
    
    @Override
    public String toString(){
        return "Gamma Distribution";
    }
}
