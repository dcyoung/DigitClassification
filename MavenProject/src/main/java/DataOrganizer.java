import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOrganizer {
	
	private ArrayList<Face> allFaces;
	private ArrayList<ArrayList<Face>> groupedFaces;
	
	//likelihoods that each pixel for each digit has a value of 1
	private ArrayList<double[][]> likelihoods;
	
	
	public DataOrganizer(ArrayList<Face> allDigits){
		this.allFaces = allDigits;
		
		this.groupedFaces = new ArrayList<ArrayList<Face>>(); 
		for(int i = 0; i < 2; i++ ){
			this.groupedFaces.add(new ArrayList<Face>());
		}
		groupDigits();
		
		this.likelihoods = new ArrayList<double[][]>();
		for(int i = 0; i < 2; i++ ){
			this.likelihoods.add(new double[70][60]);
		}
		calculateLikelihoods();
	}
	
	public void groupDigits(){
		int numDigits = this.allFaces.size();
		for(int i = 0; i < numDigits; i++ ){
			groupedFaces.get(this.allFaces.get(i).getIsFace()).add(this.allFaces.get(i));
		}
	}
	
	/**
	 * estimate the likelihoods P(Fij | class) for every pixel location (i,j) and for every digit class from 0 to 9
	 */
	public void calculateLikelihoods(){
		int sum;
		float likelihood;
		
		float k  = 1; //constant
		int V = 2; //number of possible values a feature can take (here binary 0,1  so 2 values)
		
		//for digit 0->9
		for(int d = 0; d < likelihoods.size(); d++ ){
			
			//for every pixel i,j
			for(int i = 0; i < 70; i++ ){
				for(int j = 0; j < 60; j++ ){
					//for each test img
					sum = 0;
					for(Face tempDig : this.getGroupedFaces().get(d)){
						sum += tempDig.getPixelData()[i][j];
					}
					//P(Fij = f | class) = (# of times pixel (i,j) has value f in training examples from this class) / (Total # of training examples from this class).
					//likelihood = (float) (1.0*sum/this.getSeparatedDigits().get(d).size());
					
					/*
					 * smooth the likelihoods to ensure that there are no zero counts 
					 * Laplace smoothing is a very simple method that increases the observation count of every value f 
					 * by some constant k. This corresponds to adding k to the numerator above, and k*V to the 
					 * denominator (where V is the number of possible values the feature can take on). 
					 * The higher the value of k, the stronger the smoothing 
					 */
					likelihood = (float) ((k+sum)/(k*V+this.getGroupedFaces().get(d).size()));
					likelihoods.get(d)[i][j] = likelihood;
				}
			}
		}
	}
	
	/**
	 * Print the values of a 28x28 pixel array 
	 * @param pixelData
	 * @param bUseSpaces
	 */
	public void printArray(float[][] pixelData, boolean bUseSpaces){
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMaximumFractionDigits(2);
		
		for(int row = 0; row < 70; row++ ){
			for(int col = 0; col < 60; col++ ){
				System.out.print(df.format(pixelData[row][col]));
				if(bUseSpaces)
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public ArrayList<Face> getAllFaces() {
		return allFaces;
	}

	public ArrayList<ArrayList<Face>> getGroupedFaces() {
		return groupedFaces;
	}

	public ArrayList<double[][]> getLikelihoods() {
		return likelihoods;
	}
	
	/**
	 * estimate the priors P(class) by the empirical frequencies of different classes in the training set
	 * @param digit: the class of digit (0-9)
	 * @return P(class) estimated by the empirical freq of different classes in the training set
	 */
	public float getProbabilityOfFaceClass(int digit){
		return (float) (1.0*this.groupedFaces.get(digit).size()/this.allFaces.size());
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
	 * @param face
	 * @return an array of posterior probabilities (up to scale) of each class given the digit
	 */
	public ArrayList<Double> getPosteriorProbabilities(Face face){
		ArrayList<Double> postProbs = new ArrayList<Double>();
		int[][] testImg = face.getPixelData();
		double tempProb;
		double PijGivenClass;
		
		//for each digClass 0->9
		for(int digClass = 0; digClass < 2; digClass++){
			tempProb = Math.log(getProbabilityOfFaceClass(digClass));
			//for each pixel in the testimg
			for(int i = 0; i < 70; i++ ){
				for(int j = 0; j < 60; j++ ){
					PijGivenClass = Math.log(getPixelLikelihood(digClass, i, j, testImg[i][j]));
					tempProb += PijGivenClass;
				}
			}
			postProbs.add(tempProb);
		}
		
		return postProbs;
	}
	
	
	public static void main(String[] args) {
		
		FileReader fr = new FileReader();
		String imgDataFilename = "facedata/facedatatrain";
		String labelFilename = "facedata/facedatatrainlabels";
		ArrayList<Face> test = fr.readFaceData(imgDataFilename, labelFilename);
		//test.get(0).printDigit(false);
		
		DataOrganizer dOrg = new DataOrganizer(test);
		
		for(int i = 0; i < 2; i++){
			System.out.println("number of " + i + "'s: " + dOrg.getGroupedFaces().get(i).size());
			System.out.println("P(Class = " + i + ") is " + dOrg.getProbabilityOfFaceClass(i));
			//dOrg.printArray(dOrg.getLikelihoods().get(i), true);
		}

		AccuracyStats stats = new AccuracyStats();
		for(int faceClass = 0; faceClass < 2; faceClass++){
			ArrayList<Integer> bestGuesses = new ArrayList<Integer>();
			for(int i = 0; i < dOrg.getGroupedFaces().get(faceClass).size(); i++){
				Face digit = dOrg.getGroupedFaces().get(faceClass).get(i);
				ArrayList<Double> postProbs = dOrg.getPosteriorProbabilities(digit);
				stats.addDatapoint(faceClass, postProbs.indexOf(Collections.max(postProbs)));
			}
		}
		
		double[] classificationRates = stats.getClassificationRates();
		System.out.println("Classification success rate for not faces: " + classificationRates[0]);
		System.out.println("Classification success rate for faces: " + classificationRates[1]);
		stats.printConfusionMatrix();
	}

}
