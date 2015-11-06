import java.text.DecimalFormat;
import java.util.ArrayList;

public class DataOrganizer {
	
	private ArrayList<Digit> allDigits;
	private ArrayList<ArrayList<Digit>> groupedDigits;
	
	//likelihoods that each pixel for each digit has a value of 1
	private ArrayList<float[][]> likelihoods;
	
	
	public DataOrganizer(ArrayList<Digit> allDigits){
		this.allDigits = allDigits;
		
		this.groupedDigits = new ArrayList<ArrayList<Digit>>(); 
		for(int i = 0; i < 10; i++ ){
			this.groupedDigits.add(new ArrayList<Digit>());
		}
		groupDigits();
		
		this.likelihoods = new ArrayList<float[][]>();
		for(int i = 0; i < 10; i++ ){
			this.likelihoods.add(new float[28][28]);
		}
		calculateLikelihoods();
	}
	
	public void groupDigits(){
		int numDigits = this.allDigits.size();
		for(int i = 0; i < numDigits; i++ ){
			groupedDigits.get(this.allDigits.get(i).getTrueValue()).add(this.allDigits.get(i));
		}
	}
	
	/**
	 * estimate the likelihoods P(Fij | class) for every pixel location (i,j) and for every digit class from 0 to 9
	 */
	public void calculateLikelihoods(){
		int sum;
		float likelihood;
		
		float k  = 25; //constant
		int V = 2; //number of possible values a feature can take (here binary 0,1  so 2 values)
		
		//for digit 0->9
		for(int d = 0; d < likelihoods.size(); d++ ){
			
			//for every pixel i,j
			for(int i = 0; i < 28; i++ ){
				for(int j = 0; j < 28; j++ ){
					//for each test img
					sum = 0;
					for(Digit tempDig : this.getGroupedDigits().get(d)){
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
					likelihood = (float) ((k+sum)/(k*V+this.getGroupedDigits().get(d).size()));
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
		
		for(int row = 0; row < 28; row++ ){
			for(int col = 0; col < 28; col++ ){
				System.out.print(df.format(pixelData[row][col]));
				if(bUseSpaces)
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public ArrayList<Digit> getAllDigits() {
		return allDigits;
	}

	public ArrayList<ArrayList<Digit>> getGroupedDigits() {
		return groupedDigits;
	}

	public ArrayList<float[][]> getLikelihoods() {
		return likelihoods;
	}
	
	/**
	 * estimate the priors P(class) by the empirical frequencies of different classes in the training set
	 * @param digit: the class of digit (0-9)
	 * @return P(class) estimated by the empirical freq of different classes in the training set
	 */
	public float getProbabilityOfDigitClass(int digit){
		return (float) (1.0*this.groupedDigits.get(digit).size()/this.allDigits.size());
	}
	
	/**
	 * 
	 * @param digClass
	 * @param pixRow
	 * @param pixCol
	 * @param featureVal
	 * @return P(f_i,j | class)
	 */
	public float getPixelLikelihood(int digClass, int pixRow, int pixCol, int featureVal){
		float likelihoodPixelIsOne = this.getLikelihoods().get(digClass)[pixRow][pixCol];
		if(featureVal == 1){
			return likelihoodPixelIsOne;
		}
		else{
			return 1-likelihoodPixelIsOne;
		}
	}
	
	/**
	 * 
	 * @param digit
	 * @return an array of posterior probabilities (up to scale) of each class given the digit
	 */
	public ArrayList<Double> getPosteriorProbabilities(Digit digit){
		ArrayList<Double> postProbs = new ArrayList<Double>();
		int[][] testImg = digit.getPixelData();
		double tempProb;
		double PijGivenClass;
		
		//for each digClass 0->9
		for(int digClass = 0; digClass < 10; digClass++){
			tempProb = Math.log(getProbabilityOfDigitClass(digClass));
			//for each pixel in the testimg
			for(int i = 0; i < 28; i++ ){
				for(int j = 0; j < 28; j++ ){
					PijGivenClass = Math.log(getPixelLikelihood(digClass, i, j, testImg[i][j]));
					tempProb = tempProb*PijGivenClass;
				}
			}
			postProbs.add(tempProb);
		}
		
		return postProbs;
	}
	
	
	
	
	public static void main(String[] args) {
		
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> test = fr.readDigitData(imgDataFilename, labelFilename);
		//test.get(0).printDigit(false);
		
		DataOrganizer dOrg = new DataOrganizer(test);
		
		for(int i = 0; i < 10; i++){
			System.out.println("number of " + i + "'s: " + dOrg.getGroupedDigits().get(i).size());
			System.out.println("P(Class = " + i + ") is " + dOrg.getProbabilityOfDigitClass(i));
			//dOrg.printArray(dOrg.getLikelihoods().get(i), true);
		}
		
		Digit digit = dOrg.getGroupedDigits().get(5).get(0);
		ArrayList<Double> postProbs = dOrg.getPosteriorProbabilities(digit);
		System.out.println(postProbs);
		
	}

}
