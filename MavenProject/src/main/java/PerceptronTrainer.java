/**
 * PerceptronTrainer:
 *	An individual trainer that can be used to conduct a single data 
 *  training on a perceptron. A TrainingManager is used to create 
 *  and manage many PerceptronTrainers.
 *  
 *  (for this project, this could be replaced functinally by just using
 *  a Digit class, but this class was created to dedicate the functionality
 *  of a sinlge train on a perceptron to a class)
 * @author dcyoung
 *
 */
public class PerceptronTrainer {
	
	
	private double[] inputs;
	private int trueValue;
	
	/**
	 * Constructor:
	 * @param features - 2D int array of features (pixel values)
	 * @param trueValue - true value or answer for the features
	 */
	public PerceptronTrainer(int[][] features, int trueValue){
		
		//populate the inputs array given the features
		int numFeatureRows = features.length;
		int numFeatureCols = features[0].length;
		
		//number of inputs will be 1 for each feature + 1 for the bias
		int numInputs = numFeatureRows*numFeatureCols +1;
		
		this.inputs = new double[numInputs];
		for(int row = 0; row < numFeatureRows; row++ ){
			for(int col = 0; col < numFeatureCols; col++ ){
				this.inputs[row*numFeatureCols + col] = features[row][col];
			}
		}
		this.inputs[numInputs-1] = 1;
		
		//note the trueValue which will be used during training
		this.trueValue = trueValue;
	}
	
	
	public void printInputs(){
		System.out.println();
		for(int i = 0 ; i < inputs.length; i++){
			System.out.print(inputs[i] + ", ");
		}
		System.out.println();
	}
	
	
	public double[] getInputs() {
		return inputs;
	}


	public int getTrueValue() {
		return trueValue;
	}


	public static void main(String[] args) {
		int[][] features = new int[28][28];
		PerceptronTrainer pT = new PerceptronTrainer(features, 4);
		pT.printInputs();
	}

}
