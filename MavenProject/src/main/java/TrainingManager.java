import java.util.ArrayList;
/**
 * TrainingManager: 
 * 	Creates and manages many PerceptronTrainers to effectively train a 
 * 	perceptron on a training dataset. Also provides methods for testing 
 * 	the performance of a trained peceptron. 
 * @author dcyoung
 *
 */
public class TrainingManager {

	//the organized training data
	private OrganizedDataSet trainingData;
	
	/**
	 * Constructor
	 * @param dataset
	 */
	public TrainingManager(OrganizedDataSet dataset){
		this.trainingData = dataset;
	}
	
	/**
	 * Trains a perceptron on all the data in the organized dataset, in the order the
	 * data was originally read in... before any organization or separation into groups.
	 * @param perceptron
	 */
	public void trainAllDataRandomly(MultiClassPerceptron perceptron){
		trainPerceptron(perceptron, this.trainingData.getAllDigits());
	}
	
	/**
	 * Trains a perceptron on all the data in the organized dataset by going through
	 * each class sequentially and training the perceptron on every example from that class.
	 * @param perceptron
	 */
	public void trainAllClassesSequentially(MultiClassPerceptron perceptron){
		for(int digClass = 0; digClass < this.trainingData.getGroupedDigits().size(); digClass++){
			trainPerceptron(perceptron, this.trainingData.getGroupedDigits().get(digClass));
		}
	}
	
	/**
	 * Train a perceptron on a specifiable list of training data.
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
	
	/**
	 * Test a perceptron on a set of test data.
	 * @param perceptron
	 * @param testData
	 * @return accuracy statistics about the classification performance of the perceptron on the testing data.
	 */
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
