import java.util.*;

public class Face {

	//actual true integer value of the digit between 0 and 9
	private int isFace;
	//28x28 array of values for the pixels of this digit
	private int[][] pixelData;  
	
	/**
	 * Constructor
	 * @param pixelData
	 * @param trueValue
	 */
	public Face(int[][] pixelData, int trueValue){
		this.pixelData = deepCopy2dArray(pixelData); 
		this.isFace = trueValue;
	}
	
	/**
	 * Prints the digit value and pixel values
	 * @param bUseSpaces
	 */
	public void printFace(boolean bUseSpaces){
		System.out.println("TrueValue: " + this.isFace);
		System.out.println("PixelValues: ");
		for(int row = 0; row < 70; row++ ){
			for(int col = 0; col < 60; col++ ){
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
	
	public int getIsFace() {
		return isFace;
	}


	public int[][] getPixelData() {
		return pixelData;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
