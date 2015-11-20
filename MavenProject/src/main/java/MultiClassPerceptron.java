import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MultiClassPerceptron {

	//weight vector for each class
	private ArrayList<double[]> weights;
	//learningRate
	private double alpha; 
	private Random randomGenerator = new Random();

	
	
	/**
	 * Constructor
	 * @param numClasses - number of classes considered for classification (for digit = 10)
	 * @param numInputs - number of inputs to this peceptron 
	 * @param bInitRandWeights - true if the perceptron should be initialized
	 * with random weights, false if the weights should be initialized to 0
	 * @param learningEpoch
	 */
	public MultiClassPerceptron(int numClasses, int numInputs, boolean bInitRandWeights, int epoch){
		
		//create weight vectors for each class (initialize all weights to 0)
		this.weights = new ArrayList<double[]>();
		for(int i = 0; i < numClasses; i++){
			double[] weightVec = new double[numInputs];
			this.weights.add(weightVec);
		}
		
		if(bInitRandWeights){
			//initialize all the weights to random values between -1:1
			for(int c = 0; c < numClasses; c++){
				for(int i = 0; i < numInputs; i++){
					weights.get(c)[i] = randomGenerator.nextDouble()-1;
				}
			}
		}
		
		//calculate the learning rate (alpha)
		this.alpha = 1000.0/(1000+epoch);
	}
	
	
	public int feedForward(double[] inputs){
		
		//create a container to store the calculated sum for each class
		ArrayList<Double> sumsByClass = new ArrayList<Double>();
		
		for(int c = 0; c < this.weights.size(); c++ ){
			double sum = 0;
			for(int i = 0; i < this.weights.get(c).length; i++){
				sum += inputs[i]*weights.get(c)[i];
			}
			sumsByClass.add(sum);
		}
		
		int bestClassGuess = sumsByClass.indexOf(Collections.max(sumsByClass));
		return bestClassGuess;
	}
	
	/**
	 * updateWeightVec: updates a specified weight vector as part of training
	 * @param targetClass: the class who's weight vec must be updated
	 * @param inputs: 
	 * @param updateFactor: either positive or negative learning factor
	 */
	private void updateWeightVec(int targetClass, double[] inputs, double updateFactor) {
		for(int i = 0; i < this.weights.get(targetClass).length; i++){
			this.weights.get(targetClass)[i] += updateFactor*inputs[i];
		}
	}
	
	void train(double[] inputs, int desiredClass){
		int bestClassGuess = feedForward(inputs);
		if(bestClassGuess == desiredClass){
			//do nothing
		}
		else{
			updateWeightVec(desiredClass, inputs, this.alpha);
			updateWeightVec(bestClassGuess, inputs, -this.alpha);
		}
	}
	

	public static void main(String[] args) {
		ArrayList<double[]> weights;
	}

}
