import java.util.*;

public class Digit {

	//actual true integer value of the digit between 0 and 9
	private int trueValue;
	//28x28 array of values for the pixels of this digit
	private int[][] pixelData;  
	
	/**
	 * Constructor
	 * @param pixelData
	 * @param trueValue
	 */
	public Digit(int[][] pixelData, int trueValue){
		this.pixelData = deepCopy2dArray(pixelData); 
		this.trueValue = trueValue;
	}
	
	/**
	 * Prints the digit value and pixel values
	 * @param bUseSpaces
	 */
	public void printDigit(boolean bUseSpaces){
		System.out.println("TrueValue: " + this.trueValue);
		System.out.println("PixelValues: ");
		for(int row = 0; row < 28; row++ ){
			for(int col = 0; col < 28; col++ ){
				System.out.print(this.pixelData[row][col]);
				if(bUseSpaces)
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Helper function to deep copy a 2d array
	 * @param input
	 * @return
	 */
	private int[][] deepCopy2dArray(int[][] input){
		int [][] copy = new int[input.length][];
		for(int i = 0; i < input.length; i++)
		{
		  int[] aMatrix = input[i];
		  int   aLength = aMatrix.length;
		  copy[i] = new int[aLength];
		  System.arraycopy(aMatrix, 0, copy[i], 0, aLength);
		}
		return copy;
	}
	
	
	public double[] generateInputsWithBias(){
		int[][] features = this.pixelData;
		double[] inputs;
		
		//populate the inputs array given the features
		int numFeatureRows = features.length;
		int numFeatureCols = features[0].length;
		
		//number of inputs will be 1 for each feature + 1 for the bias
		int numInputs = numFeatureRows*numFeatureCols +1;
		
		inputs = new double[numInputs];
		for(int row = 0; row < numFeatureRows; row++ ){
			for(int col = 0; col < numFeatureCols; col++ ){
				inputs[row*numFeatureCols + col] = (double) features[row][col];
			}
		}
		inputs[numInputs-1] = 1;
		return inputs;
	}
	
	
	
	public int getTrueValue() {
		return trueValue;
	}


	public int[][] getPixelData() {
		return pixelData;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> allTrainingDigits = fr.readDigitData(imgDataFilename, labelFilename);
		
		Digit firstDigit = allTrainingDigits.get(0);
		double[] inputs = firstDigit.generateInputsWithBias();
		for(int i = 0; i < inputs.length; i++){
			System.out.print(inputs[i] + ", ");
		}
		System.out.println();
		System.out.println(firstDigit.generateInputsWithBias().length);
	}

}
