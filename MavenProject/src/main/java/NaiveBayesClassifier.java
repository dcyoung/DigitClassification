import java.util.ArrayList;
import java.util.Collections;


/**
 * Trains a simple naive Bayes Classifier on a training Dataset
 * by calculating probability info necessary for flag detection
 * @author dcyoung
 */
public class NaiveBayesClassifier {
	private OrganizedDataset trainingData;
	
	//likelihoods that each pixel for each digit has a value of 1
	private ArrayList<double[][]> likelihoods;
	
	public NaiveBayesClassifier(OrganizedDataset trainingData){
		this.trainingData = trainingData;
		this.likelihoods = new ArrayList<double[][]>();
		int numClasses = trainingData.getGroupedDigits().size();
		for(int i = 0; i < numClasses; i++ ){
			this.likelihoods.add(new double[28][28]);
		}
		calculateLikelihoods();
	}
	
	/**
	 * estimate the likelihoods P(Fij | class) for every pixel location (i,j) and for every digit class from 0 to 9
	 */
	public void calculateLikelihoods(){
		int sum;
		float likelihood;
		
		float k  = 1; //constant
		int V = 2; //number of possible values a feature can take (here binary 0,1  so 2 values)
		
		//for each class (digit 0->9)
		for(int d = 0; d < likelihoods.size(); d++ ){
			//for every pixel i,j
			for(int i = 0; i < 28; i++ ){
				for(int j = 0; j < 28; j++ ){
					sum = 0;
					//for each training example from this class
					for(Digit tempDig : this.trainingData.getGroupedDigits().get(d)){
						sum += tempDig.getPixelData()[i][j];
					}
					//P(Fij = f | class) = (# of times pixel (i,j) has value f in training examples from this class) / (Total # of training examples from this class).
					/*
					 * smooth the likelihoods to ensure that there are no zero counts 
					 * Laplace smoothing is a very simple method that increases the observation count of every value f 
					 * by some constant k. This corresponds to adding k to the numerator above, and k*V to the 
					 * denominator (where V is the number of possible values the feature can take on). 
					 * The higher the value of k, the stronger the smoothing 
					 */
					int totalNumExamplesInClass = this.trainingData.getGroupedDigits().get(d).size();
					likelihood = (float) ((k+sum)/(k*V+totalNumExamplesInClass));
					likelihoods.get(d)[i][j] = likelihood;
				}
			}
		}
	}
	
	
	/**
	 * estimate the priors P(class) by the empirical frequencies of different classes in the training set
	 * @param digClass: the class of digit (0-9)
	 * @return P(class) estimated by the empirical freq of different classes in the training set
	 */
	public float getProbabilityOfDigitClass(int digClass){
		int numExamplesFromDesiredClass = this.trainingData.getGroupedDigits().get(digClass).size();
		int numExamplesFromAllClasses = this.trainingData.getAllDigits().size();
		return (float) (1.0*numExamplesFromDesiredClass/numExamplesFromAllClasses);
	}
	
	/**
	 * 
	 * @param digClass
	 * @param pixRow
	 * @param pixCol
	 * @param featureVal
	 * @return P(f_i,j | class)
	 */
	public double getPixelLikelihood(int digClass, int pixRow, int pixCol, int featureVal){
		double likelihoodPixelIsOne = this.getLikelihoods().get(digClass)[pixRow][pixCol];
		if(featureVal == 1){
			return likelihoodPixelIsOne;
		}
		else{
			return 1-likelihoodPixelIsOne;
		}
	}
	
	/**
	 * 
	 * @param digitExample
	 * @return an array of posterior probabilities (or proportional representations) 
	 * of each digit class given the example digit... ie: an array of entries P(Class_i | Example)
	 */
	public ArrayList<Double> getPosteriorProbabilities(Digit digitExample){
		ArrayList<Double> postProbs = new ArrayList<Double>();
		int[][] testImg = digitExample.getPixelData();
		double tempProb;
		double PijGivenClass;
		
		//for each digClass 0->9
		for(int digClass = 0; digClass < 10; digClass++){
			tempProb = Math.log(getProbabilityOfDigitClass(digClass));
			//for each pixel in the testimg
			for(int i = 0; i < 28; i++ ){
				for(int j = 0; j < 28; j++ ){
					PijGivenClass = Math.log(getPixelLikelihood(digClass, i, j, testImg[i][j]));
					tempProb += PijGivenClass;
				}
			}
			postProbs.add(tempProb);
		}
		
		return postProbs;
	}
	
	
	
	public ArrayList<double[][]> getLikelihoods() {
		return likelihoods;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> test = fr.readDigitData(imgDataFilename, labelFilename);
		//test.get(0).printDigit(false);
		
		OrganizedDataset trainingData = new OrganizedDataset(test);
		NaiveBayesClassifier classifier = new NaiveBayesClassifier(trainingData);
		
		for(int i = 0; i < 10; i++){
			System.out.println("number of " + i + "'s: " + trainingData.getGroupedDigits().get(i).size());
			System.out.println("P(Class = " + i + ") is " + classifier.getProbabilityOfDigitClass(i));
			//dOrg.printArray(dOrg.getLikelihoods().get(i), true);
		}

		AccuracyStats stats = new AccuracyStats();
		for(int digClass = 0; digClass < 10; digClass++){
			ArrayList<Integer> bestGuesses = new ArrayList<Integer>();
			for(int i = 0; i < trainingData.getGroupedDigits().get(digClass).size(); i++){
				Digit digit = trainingData.getGroupedDigits().get(digClass).get(i);
				ArrayList<Double> postProbs = classifier.getPosteriorProbabilities(digit);
				stats.addDatapoint(digClass, postProbs.indexOf(Collections.max(postProbs)));
			}
		}
		
		System.out.println(stats.getClassificationRates());
		stats.printConfusionMatrix();
	}

}
