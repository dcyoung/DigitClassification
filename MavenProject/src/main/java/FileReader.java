import java.io.*;
import java.net.URISyntaxException;
import java.util.*;


public class FileReader {
	
	
	public FileReader(){
		
	}
	
	public ArrayList<Digit> readDigitData(String imgDataFilename, String labelFilename){
		
		ArrayList<Digit> digits = new ArrayList<Digit>();
		try {
			int fileLineCount = 1;
			Scanner imgDataLineScanner = new Scanner(new File(this.getClass().getResource( "/"+imgDataFilename).toURI()));
			Scanner labelsLineScanner = new Scanner(new File(this.getClass().getResource( "/"+labelFilename).toURI()));
			String tempLine;
			char tempPixelValue;
			int pixRow;
			int pixCol;
			int[][] digitPixelVals = new int[28][28];
			int trueDigitVal;
			
			//step through all the digits (each is 28 lines)
			while(imgDataLineScanner.hasNextLine()){
				//note the true value from the labels file
				if(labelsLineScanner.hasNextInt())
					trueDigitVal = labelsLineScanner.nextInt();
				else
					break;
				
				pixRow = 0;
				do{
					tempLine = imgDataLineScanner.nextLine();
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
