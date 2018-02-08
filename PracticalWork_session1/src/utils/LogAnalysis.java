package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogAnalysis {

	/**
	 * Prints an analysis of a <b>logfile</b> given as an argument. <br>
	 * More specifically, it prints :<br>
	 * -the number of tested values<br>
	 * -the number and proportion of correctly matched values<br>
	 * -the accumulated and averaged cost of correctly matching values<br>
	 * -the accumulated and averaged cost of incorrectly matching values<br>
	 * 
	 * @param logFile : File object indicating the log file to analyze
	 * @throws IOException Error during the parsing of the file may throw IOExceptions
	 */
	public static void printStats(File logFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(logFile));
		int tested = 0;
		int correctMatch = 0;
		double accumulatedCorrect_cost=0;
		double accumulatedError_cost=0;

		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String word = split[0];
			String[] phonems = split[1].replaceAll("\\[|\\]", "").split(" ");
			String match = split[2];
			String[] matchphonems = split[3].replaceAll("\\[|\\]", "").split(" ");
			double cost = Double.parseDouble(split[4]);


			tested++;
			if (word.equals(match)) {
				correctMatch++;
				accumulatedCorrect_cost+=cost;
			}else {
				accumulatedError_cost+=cost;
			}
		}
		br.close();

		System.out.println("================== Stats for "+logFile.getName()+ "==================");

		if (tested==0)
			System.out.println("|| Error reading file ? There were no tests found in it");
		else {
			System.out.println("|| Tested : "+tested);
			System.out.println("|| Number of correct matches : "+correctMatch);
			System.out.println("|| Proportion of correct matches : "+((double)correctMatch)/tested);

			System.out.println("|| Cumulated cost of correct matching : "+accumulatedCorrect_cost);
			System.out.println("|| Cumulated cost of incorrect matching : "+accumulatedError_cost);
			if (correctMatch==0)
				System.out.println("|| 0 correct matches : cannot calculate *Average cost of correct matching* ");
			else
				System.out.println("|| Average cost of correct matching : "+((double)accumulatedCorrect_cost)/correctMatch);
			
			if (correctMatch==tested)
				System.out.println("|| 0 incorrect matches : cannot calculate *Average cost of incorrect matching* ");
			else
				System.out.println("|| Average cost of incorrect matching : "+((double)accumulatedError_cost)/(tested-correctMatch));
			System.out.println("===============================================================================================\n");

		}

	}
	
	
	/**
	 * Prints an analysis of all the files of extension {@code logType} situated in the folder {@code pathToLogs}
	 * @param logType : extension of the files to analyze
	 * @param pathToLogs : folder into which to look to find files with <b>logType</b> extension
	 * @throws Exception  An exception may be thrown during the analysis of a file. It will indicate which file caused the exception.
	 */
	public static void analyseAllLogs(String logType, String pathToLogs) throws Exception {
		File folder = new File(pathToLogs);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  String filename = listOfFiles[i].getName();
		    	  int index = filename.lastIndexOf('.');
		    	  String extension = filename.substring(index+1);
		    	  if (extension.equals(logType))
		    		  try {
		    			  printStats(listOfFiles[i]);
		    		  }catch (Exception e) {
						throw new Exception("Error during processing of"+listOfFiles[i], e);
					}
		      }
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		File logfile2 = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-0100words.test.HMMDISCRET__testLog");
		printStats(logfile2);
		
		analyseAllLogs("HMMDISCRET__testLog","resources/Master-Audition-TD-2018.01-data-v1.0");
		
		analyseAllLogs("LEVENSHTEIN__testLog","resources/Master-Audition-TD-2018.01-data-v1.0");

	}

}
