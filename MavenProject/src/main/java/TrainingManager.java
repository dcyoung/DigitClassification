import java.util.ArrayList;

public class TrainingManager {

	
	private DataOrganizer dataset;
	private MultiClassPerceptron perceptron;
	
	public TrainingManager(MultiClassPerceptron perceptron, DataOrganizer dataset){
		this.perceptron = perceptron;
		this.dataset = dataset;
	}
	
	
	/**
	 * 
	 * @param perceptron
	 */
	public void trainAllClassesSequentially(MultiClassPerceptron perceptron){
		for(int digClass = 0; digClass < this.dataset.getGroupedDigits().size(); digClass++){
			trainPerceptron(perceptron, this.dataset.getGroupedDigits().get(digClass));
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
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
