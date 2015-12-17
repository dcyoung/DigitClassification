import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Organizes the training data by class
 * @author dcyoung
 *
 */
public class OrganizedDataset {
	
	private ArrayList<Digit> allDigits;
	private ArrayList<ArrayList<Digit>> groupedDigits;
	
	/**
	 * Constructor
	 * @param allDigits
	 */
	public OrganizedDataset(ArrayList<Digit> allDigits){
		this.allDigits = allDigits;
		
		this.groupedDigits = new ArrayList<ArrayList<Digit>>(); 
		for(int i = 0; i < 10; i++ ){
			this.groupedDigits.add(new ArrayList<Digit>());
		}
		groupDigits();
	}
	
	public void groupDigits(){
		int numDigits = this.allDigits.size();
		for(int i = 0; i < numDigits; i++ ){
			groupedDigits.get(this.allDigits.get(i).getTrueValue()).add(this.allDigits.get(i));
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

	
	public static void main(String[] args) {
		
	}

}
