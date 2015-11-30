import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
/**
 * Provides a single entry point to execute the training and testing of a perceptron. 
 * @author dcyoung
 *
 */
public class TestRunner {
	
	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> allTrainingDigits = fr.readDigitData(imgDataFilename, labelFilename);
		
		imgDataFilename = "digitdata/testimages";
		labelFilename = "digitdata/testlabels";
		ArrayList<Digit> allTestingDigits = fr.readDigitData(imgDataFilename, labelFilename);
		
		OrganizedDataSet trainingDataset = new OrganizedDataSet(allTrainingDigits);
		OrganizedDataSet testingDataset = new OrganizedDataSet(allTestingDigits);
		
		TrainingManager trainingManager = new TrainingManager(trainingDataset);
		
		MultiClassPerceptron perceptron = new MultiClassPerceptron(10, (28*28+1), true, 1);
		trainingManager.trainAllDataRandomly(perceptron);
		AccuracyStats stats = trainingManager.testTrainedPerceptron(perceptron, testingDataset);
		
		System.out.println("Average Classification Rate:");
		System.out.println(stats.getAverageClassificationRate());
		System.out.println();
		System.out.println("Confusion Matrix:");
		stats.printConfusionMatrix();
        
        //rodney added this coment
//		System.out.println();
//		System.out.println("Classification Rates by digit:");
//		double[] classRates = stats.getClassificationRates();
//		for(int i = 0; i < 10; i++){
//			System.out.println("Digit " + i + ": " + df.format(classRates[i]));
//		}
//		System.out.println("Average Classification Rate Across all Digit Classes: \n" + df.format(stats.getAverageClassificationRate()));
//
//		try {
//			HeatMapGenerator hmg = new HeatMapGenerator(stats, trainingData.getLikelihoods());
//		} catch (IOException e) {
//			System.out.println("Failed to create/save heat maps");
//			e.printStackTrace();
//		}
	}

}
