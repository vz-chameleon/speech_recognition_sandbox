package levenshtein_recognition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import utils.DataParser;

public class MainLevenshteinRecognition {	
	
	public static void reco_dist_levenshtein(String lex, String test) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(lex));
		HashMap<String,String[]> LexicPhonemsHashmap  = DataParser.tex_or_test_to_HashMap(lex);
		
		String line;	
		br.close();
		br = new BufferedReader(new FileReader(test));
		BufferedWriter bw = new BufferedWriter(new FileWriter(test+".testLog"));
		while ((line = br.readLine()) != null) {
	
			String split[]= line.split("\t");
			String testword = split[0];
			String[] testphonems = split[1].split(" ");
			
//			Call function to test out testword's phonem with our LexicPhonemsAList
			SimpleEntry<String, SimpleEntry<String[],Double>> result  = getrecognisedWordwithAlignmentAndCost(testphonems,LexicPhonemsHashmap);
			String recognisedWord = result.getKey();
			String[] recognisedWordphonems = LexicPhonemsHashmap.get(recognisedWord);
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
