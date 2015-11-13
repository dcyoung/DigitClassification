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
	public ArrayList<Face> readFaceData(String imgDataFilename, String labelFilename){
		
		int numPixRows = 70;
		int numPixCols = 60;
		ArrayList<Face> faces = new ArrayList<Face>();
		try {
			
			Scanner imgDataLineScanner = new Scanner(new File(this.getClass().getResource( "/"+imgDataFilename).toURI()));
			Scanner labelsLineScanner = new Scanner(new File(this.getClass().getResource( "/"+labelFilename).toURI()));
			String tempLine;
			char tempPixelValue; //the data file uses + for edges and # for the body
			int pixRow, pixCol, faceLabelVal;
			int[][] facePixelVals = new int[numPixRows][numPixCols]; //the 70 lines associated with each digit will be read into this temporary 2d array
			int fileLineCount = 1;
			
			//step through all the digits (each is 70 lines)
			while(imgDataLineScanner.hasNextLine()){
				//note the true value from the labels file
				if(labelsLineScanner.hasNextInt())
					faceLabelVal = labelsLineScanner.nextInt();
				else
					break;
				
				pixRow = 0;
				//populate the 2d 70x61 array array noting the value of each pixel 
				do{
					tempLine = imgDataLineScanner.nextLine();
					//look at every pixel in this row
					for(pixCol = 0; pixCol < numPixCols; pixCol++){
						switch(tempLine.charAt(pixCol)){
							case '#': //body
								facePixelVals[pixRow][pixCol] = 1;
								break;
							case '+': //edges
								facePixelVals[pixRow][pixCol] = 1;
								break;
							default:
								facePixelVals[pixRow][pixCol] = 0;
								break;
						}
					}
					pixRow++;
					fileLineCount++;
				}while(imgDataLineScanner.hasNextLine() && fileLineCount%numPixRows!=0);
				
				//add to the arraylist of digits a new digit using the read pixel values and true digit value
				faces.add(new Face(facePixelVals, faceLabelVal));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found.");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}	
		return faces;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader();
		String imgDataFilename = "facedata/facedatatrain";
		String labelFilename = "facedata/facedatatrainlabels";
		ArrayList<Face> test = fr.readFaceData(imgDataFilename, labelFilename); 
		test.get(0).printFace(false);
	}

}
