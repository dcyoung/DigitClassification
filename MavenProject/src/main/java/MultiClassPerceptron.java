import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * MultiClassPerceptron: 
 * A peceptron that can operate on inputs to produce a classification 
 * estimate considering multiple classes. 
 * @author dcyoung
 *
 */
public class MultiClassPerceptron {

	//weight vector for each class
	private ArrayList<double[]> weights;
	//learningRate
	private double alpha;
	//whether or not this perceptron will use a bias
	private boolean bUseBias;
	
	private Random randomGenerator = new Random();
	
	
	
	/**
	 * Constructor
	 * @param numClasses - number of classes considered for classification (for digit = 10)
	 * @param numInputs - number of inputs to this peceptron 
	 * @param bInitRandWeights - true if the perceptron should be initialized
	 * with random weights, false if the weights should be initialized to 0
	 * @param learningEpoch
	 */
	public MultiClassPerceptron(int numClasses, int numInputs, boolean bInitRandWeights, boolean bUseBias, int epoch){
		
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
		//whether or not the perceptron should use a bias input
		this.bUseBias = bUseBias;
		//calculate the learning rate (alpha)
		this.alpha = 1000.0/(1000+epoch);
	}
	
	/**
	 * feeds the input through the perceptron 
	 * @param inputs: any features representable by a double array (digit pixel values for example)
	 * @return the perceptron's best guess at a classification (digit class)
	 */
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
	
	/**
	 * Trains the perceptron by running the input through the perceptron, comparing
	 * the perceptron's best classification to the true value and updating the weights 
	 * if the classification was innacurate.
	 * @param inputs: any features representable by a double array (digit pixel values for example)
	 * @param expectedClass: the correct classification
	 */
	void train(double[] inputs, int expectedClass){
		//let the perceptron classify the given inputs
		int bestClassGuess = feedForward(inputs);
		//compare the classification to the expected value
		if(bestClassGuess == expectedClass){
			//do nothing
		}
		else{
			//if the classification was incorrect
			//update the weight vectors of the expected and incorrectly chosen classes
			updateWeightVec(expectedClass, inputs, this.alpha);
			updateWeightVec(bestClassGuess, inputs, -this.alpha);
		}
	}
	
	void updateLearningRate(int epoch){
		this.alpha = 1000.0/(1000+epoch);
	}
	
	public boolean getUseBias() {
		return this.bUseBias;
	}

	public static void main(String[] args) {
		
	}


}
