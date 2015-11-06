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
					for(Digit tempDig : this.getSeparatedDigits().get(d)){
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
					likelihood = (float) ((k+sum)/(k*V+this.getSeparatedDigits().get(d).size()));
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

	public ArrayList<ArrayList<Digit>> getSeparatedDigits() {
		return groupedDigits;
	}

	public ArrayList<float[][]> getLikelihoods() {
		return likelihoods;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> test = fr.readDigitData(imgDataFilename, labelFilename);
		//test.get(0).printDigit(false);
		
		DataOrganizer dOrg = new DataOrganizer(test);
		
		for(int i = 0; i < 10; i++){
			System.out.println("number of " + i + "'s: " + dOrg.getSeparatedDigits().get(i).size());
			dOrg.printArray(dOrg.getLikelihoods().get(i), true);
//			System.out.println();
			
		}
		
		
	}

}
