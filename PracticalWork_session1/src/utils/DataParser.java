package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
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
		sb.deleteCharAt(sb.length());
		String phonesTestString = sb.toString();
		sb.setLength(0);
		for (String str : phonesReconnu)
			sb.append(str+" ");
		sb.deleteCharAt(sb.length());
		String phonesReconnuString = sb.toString();
		sb.setLength(0);
		for (String str : alignment)
			sb.append(str+" ");
		sb.deleteCharAt(sb.length());
		String alignmentString = sb.toString();
		sb.setLength(0);

		
		
		
		String logString = motTest + "\t["+phonesTestString+"]\t"+ motReconnu + "\t[" + phonesReconnuString + "]\t"+ cost+ "\t"+alignmentString;
		filewriter.write(logString);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
