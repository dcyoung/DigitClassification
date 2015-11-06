import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TestRunner {
	
	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> trainingDataDigits = fr.readDigitData(imgDataFilename, labelFilename);
		
		imgDataFilename = "digitdata/testimages";
		labelFilename = "digitdata/testlabels";
		ArrayList<Digit> testDataDigits = fr.readDigitData(imgDataFilename, labelFilename);
		
		DataOrganizer trainingData = new DataOrganizer(trainingDataDigits);
		DataOrganizer testData = new DataOrganizer(testDataDigits);
		
		AccuracyStats stats = new AccuracyStats();
		for(int digClass = 0; digClass < 10; digClass++){
			for(int i = 0; i < testData.getGroupedDigits().get(digClass).size(); i++){
				Digit digit = testData.getGroupedDigits().get(digClass).get(i);
				ArrayList<Double> postProbs = trainingData.getPosteriorProbabilities(digit);
				stats.addDatapoint(digClass, postProbs.indexOf(Collections.max(postProbs)));
			}
		}
		
		System.out.println("Confusion Matrix:");
		stats.printConfusionMatrix();
		
		System.out.println();
		System.out.println("Classification Rates by digit:");
		double[] classRates = stats.getClassificationRates();
		for(int i = 0; i < 10; i++){
			System.out.println("Digit " + i + ": " + df.format(classRates[i]));
		}
		System.out.println("Average Classification Rate Across all Digit Classes: \n" + df.format(stats.getAverageClassificationRate()));
	}

}
