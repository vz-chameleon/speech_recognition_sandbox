package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;;

public class DataParser {

	
	public static void reco_dist_levenshtein(String lex, String test) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(lex));
		HashMap<String,String[]> LexicPhonemsAList = new  HashMap<String,String[]>();
		
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String word = split[0];
			String[] phonems = split[1].split(" ");
			LexicPhonemsAList.put(word,phonems);
		}
		br.close();
		
		
		br = new BufferedReader(new FileReader(test));
		BufferedWriter bw = new BufferedWriter(new FileWriter(test+".testLog"));
		while ((line = br.readLine()) != null) {
	
			String split[]= line.split("\t");
			String testword = split[0];
			String[] testphonems = split[1].split(" ");
			
//			Call function to test out testword's phonem with our LexicPhonemsAList
			SimpleEntry<String, SimpleEntry<String[],Double>> result  = getrecognisedWordwithAlignmentAndCost(testphonems,LexicPhonemsAList);
			String recognisedWord = result.getKey();
			String[] recognisedWordphonems = LexicPhonemsAList.get(recognisedWord);
			String[] agitlignment = result.getValue().getKey();
			double cost = result.getValue().getValue();
			
//			log this to file
//			logtofile(bw, testword, testphonems, recognisedWord, recognisedWordphonems, cost);
		}
		br.close();
		bw.close();
	}
	
	public static SimpleEntry<String, SimpleEntry<String[], Double>> getrecognisedWordwithAlignmentAndCost(String[] testphonems,HashMap<String, String[]> LexicPhonemsAList){
		//TODO
		return null;
		
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
	public void logtofile(Writer filewriter, String motTest, String[] phonesTest, String motReconnu, String[] phonesReconnu, double cost) throws IOException{
		String logString = motTest + " ["+phonesTest+"] "+ motReconnu + " [" + phonesReconnu + "] "+ cost;
		filewriter.write(logString);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
