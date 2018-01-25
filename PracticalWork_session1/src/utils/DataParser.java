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

	public DataParser(){

	}

	public ArrayList<SimpleEntry<String,String[]>> loadLexicFile(String filepath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		ArrayList<SimpleEntry<String,String[]>> LexicPhonemsAList = new ArrayList<>();
		
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t"); // split[1] = "Mozart," so you may need to do a little more work there
			String word = split[0];
			String[] phonems = split[1].split(" ");
			LexicPhonemsAList.add(new SimpleEntry<String,String[]>(word,phonems));
		}
		br.close();
		return LexicPhonemsAList;
	}
	
	public void checkTestFile(String filepath, ArrayList<SimpleEntry<String,String[]>> LexicPhonemsAList) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		BufferedWriter bw = new BufferedWriter(new FileWriter(filepath+".testLog"));
		
		String line;
		while ((line = br.readLine()) != null) {
	
			String split[]= line.split("\t");
			String testword = split[0];
			String[] testphonems = split[1].split(" ");
			
//			Call function to test out testword's phonem with our LexicPhonemsAList
//			String recognisedWord, String[] recognisedWordphonems, in/double cost = getrecognisedWordwithPhonemsAndCost(testphonems,LexicPhonemsAList)
//			log this to file
//			logtofile(bw, testword, testphonems, recognisedWord, recognisedWordphonems, cost);
		}
		br.close();
		bw.close();
	}
	
	public void logtofile(Writer filewriter, String motTest, String[] phonesTest, String motReconnu){
//		log this to file : <mot test> [phones test]  <mot reconnu> [phones ref] ok/err cout align
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
