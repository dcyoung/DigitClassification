import java.util.ArrayList;

public class TrainingManager {

	
	private OrganizedDataSet trainingData;
	
	/**
	 * Constructor
	 * @param dataset
	 */
	public TrainingManager(OrganizedDataSet dataset){
		this.trainingData = dataset;
	}
	
	
	public void trainAllDataRandomly(MultiClassPerceptron perceptron){
		trainPerceptron(perceptron, this.trainingData.getAllDigits());
	}
	
	/**
	 * 
	 * @param perceptron
	 */
	public void trainAllClassesSequentially(MultiClassPerceptron perceptron){
		for(int digClass = 0; digClass < this.trainingData.getGroupedDigits().size(); digClass++){
			trainPerceptron(perceptron, this.trainingData.getGroupedDigits().get(digClass));
		}
	}
	
	/**
	 * 
	 * @param perceptron
	 * @param trainingData
	 */
	public void trainPerceptron(MultiClassPerceptron perceptron, ArrayList<Digit> trainingData){
		PerceptronTrainer[] trainer = new PerceptronTrainer[trainingData.size()];
		for(int i = 0; i < trainer.length; i++){
			Digit trainingDigit = trainingData.get(i);
			int actualDigClass = trainingDigit.getTrueValue();
			trainer[i] = new PerceptronTrainer(trainingDigit.getPixelData(), actualDigClass);
		}
		
		for(int i = 0; i < trainer.length; i++){
			perceptron.train(trainer[i].getInputs(), trainer[i].getTrueValue());
		}
	}
	
	
	public AccuracyStats testTrainedPerceptron(MultiClassPerceptron perceptron, OrganizedDataSet testData){
		AccuracyStats stats = new AccuracyStats();
		
		for(int digClass = 0; digClass < testData.getGroupedDigits().size(); digClass++){
			for(int i = 0; i < testData.getGroupedDigits().get(digClass).size(); i++){
				Digit digit = testData.getGroupedDigits().get(digClass).get(i);
				
				int digClassGuess = perceptron.feedForward(digit.generateInputsWithBias());
				stats.addDatapoint(digClass, digClassGuess);
			}
		}
		return stats;
	}
	
	
	public static void main(String[] args) {
		
	}

}
