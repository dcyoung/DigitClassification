import java.io.*;
import java.net.URISyntaxException;
import java.util.*;


public class FileReader {
	
	
	public FileReader(){
		
	}
	
	/**
	 * Reads digit data from a formatted file of image data and a file of true integer values for those digits
	 * @param imgDataFilename
	 * @param labelFilename
	 * @return
	 */
	public ArrayList<Digit> readDigitData(String imgDataFilename, String labelFilename){
		
		ArrayList<Digit> digits = new ArrayList<Digit>();
		try {
			
			Scanner imgDataLineScanner = new Scanner(new File(this.getClass().getResource( "/"+imgDataFilename).toURI()));
			Scanner labelsLineScanner = new Scanner(new File(this.getClass().getResource( "/"+labelFilename).toURI()));
			String tempLine;
			char tempPixelValue; //the data file uses + for edges and # for the body
			int pixRow, pixCol, trueDigitVal;
			int[][] digitPixelVals = new int[28][28]; //the 28 lines associated with each digit will be read into this temporary 2d array
			int fileLineCount = 1;
			
			//step through all the digits (each is 28 lines)
			while(imgDataLineScanner.hasNextLine()){
				//note the true value from the labels file
				if(labelsLineScanner.hasNextInt())
					trueDigitVal = labelsLineScanner.nextInt();
				else
					break;
				
				pixRow = 0;
				//populate the 2d 28x28 array array noting the value of each pixel 
				do{
					tempLine = imgDataLineScanner.nextLine();
					//look at every pixel in this row
					for(pixCol = 0; pixCol < 28; pixCol++){
						switch(tempLine.charAt(pixCol)){
							case '#': //body
								digitPixelVals[pixRow][pixCol] = 1;
								break;
							case '+': //edges
								digitPixelVals[pixRow][pixCol] = 1;
								break;
							default:
								digitPixelVals[pixRow][pixCol] = 0;
								break;
						}
					}
					pixRow++;
					fileLineCount++;
				}while(imgDataLineScanner.hasNextLine() && fileLineCount%28!=0);
				
				//add to the arraylist of digits a new digit using the read pixel values and true digit value
				digits.add(new Digit(digitPixelVals, trueDigitVal));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found.");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return digits;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader();
		String imgDataFilename = "digitdata/trainingimages";
		String labelFilename = "digitdata/traininglabels";
		ArrayList<Digit> test = fr.readDigitData(imgDataFilename, labelFilename); 
		test.get(0).printDigit(false);
	}

}
