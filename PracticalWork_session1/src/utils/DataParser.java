package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class DataParser {
	
	public static HashMap<String,String[]> tex_or_test_to_HashMap(File lex) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(lex));
		HashMap<String,String[]> WordPhonemsHashmap = new  HashMap<String,String[]>();
		
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String word = split[0];
			String[] phonems = split[1].split(" ");
			WordPhonemsHashmap.put(word,phonems);
		}
		br.close();
		
		return WordPhonemsHashmap;
	}
	
	/***
	 * 
	 * @param modeleHMM
	 * @return An array of hashmaps, so that array[0] is PSIOLogs, array[2] is SubstitutionLogsMap, array[3] is InsertionLogsMap, 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static HashMap[] load_HMMMaps(File modeleHMM) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(modeleHMM));
		HashMap<Object, HashMap<Object, Double>> SubstitutionLogsMap = new HashMap<>();
		br.readLine();
		//loading substitution data on line 2 ???
		String[] line2split = br.readLine().split(";");
		String Psub = line2split[0],Pins = line2split[1] ,Pomi = line2split[2];
		
		HashMap<String, Double> PSIOLogs = new HashMap<>(3);
		PSIOLogs.put("PsubLog", Math.log(Double.parseDouble(Psub)));
		PSIOLogs.put("PinsLog", Math.log(Double.parseDouble(Pins)));
		PSIOLogs.put("PomiLog", Math.log(Double.parseDouble(Pomi)));
		
		//loading rest of substitution data ??
		br.readLine();
		String line = br.readLine();
		
		String[] phonems = line.split(";");
		for (int substitutionline = 1; substitutionline<phonems.length; substitutionline++){
			line = br.readLine();
			String split[]= line.split(";");
			String phonem = split[0];
			HashMap<Object, Double> cs = new HashMap<>();
			for (int i = 1; i< phonems.length; i++)
				cs.put(phonems[i], Math.log(Double.valueOf(split[i])+Double.MIN_NORMAL));
			SubstitutionLogsMap.put(phonem, cs);
		}
		br.readLine();
		line = br.readLine();
		
		HashMap<Object, Double> InsertionLogsMap = new HashMap<>();
		String[] insertionProbabilities = line.split(";");
		for (int i = 1; i< phonems.length; i++)
			InsertionLogsMap.put(phonems[i], Math.log(Double.valueOf(insertionProbabilities[i])+Double.MIN_NORMAL));		
		
		return new HashMap[] {PSIOLogs, SubstitutionLogsMap, InsertionLogsMap} ;
	}



	/**
	 * log this to file : <mot test> [phones test]  <mot reconnu> [phones ref] ok/err cout align
	 * 
	 * @param filewriter Object to write to log file
	 * @param motTest The tested word
	 * @param phonesTest The tested word's recorded pronunciation
	 * @param motReconnu The recognized word from those sounds
	 * @param phonesReconnu The recognized word true pronunciation
	 * @param cost The Levenshtein cost to go from phonesTest to phonesReconnu
	 * @throws IOException
	 */
	public static void logtofile(Writer filewriter, String motTest, String[] phonesTest, String motReconnu, String[] phonesReconnu, double cost, String[] alignment) throws IOException{
		StringBuilder sb = new StringBuilder();
		for (String str : phonesTest)
			sb.append(str+" ");
		sb.deleteCharAt(sb.length()-1);
		String phonesTestString = sb.toString();
		sb.setLength(0);
		for (String str : phonesReconnu)
			sb.append(str+" ");
		sb.deleteCharAt(sb.length()-1);
		String phonesReconnuString = sb.toString();
		sb.setLength(0);
		for (String str : alignment)
			sb.append(str+" ");
		sb.deleteCharAt(sb.length()-1);
		String alignmentString = sb.toString();
		sb.setLength(0);

		
		
		
		String logString = motTest + "\t["+phonesTestString+"]\t"+ motReconnu + "\t[" + phonesReconnuString + "]\t"+ cost+ "\t"+alignmentString+"\n";
		filewriter.write(logString);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
