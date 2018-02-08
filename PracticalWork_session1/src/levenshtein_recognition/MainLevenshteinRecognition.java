package levenshtein_recognition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import utils.DataParser;

public class MainLevenshteinRecognition {	
	
	public static void reco_dist_levenshtein(File lex, File test) throws IOException{
		HashMap<String,String[]> LexicPhonemsHashmap  = DataParser.lex_or_test_to_HashMap(lex);
		
		String line;	
		BufferedReader br = new BufferedReader(new FileReader(test));
		BufferedWriter bw = new BufferedWriter(new FileWriter(test+".LEVENSHTEIN__testLog"));
		while ((line = br.readLine()) != null) {
	
			String split[]= line.split("\t");
//			System.out.println(Arrays.deepToString(split));
			String testword = split[0];
			String[] testphonems = (split.length==2)?split[1].split(" "): new String[] {"unknown"};
			
//			Call function to test out testword's phonem with our LexicPhonemsAList
			SimpleEntry<String, SimpleEntry<String[],Double>> result  = Levenshtein.getrecognisedWordwithAlignmentAndCost(testphonems,LexicPhonemsHashmap);
			String recognisedWord = result.getKey();
			String[] recognisedWordphonems = LexicPhonemsHashmap.get(recognisedWord);
			String[] alignment = result.getValue().getKey();
			double cost = result.getValue().getValue();
			
//			log this to file
			DataParser.logtofile(bw, testword, testphonems, recognisedWord, recognisedWordphonems, cost, alignment);
		}
		br.close();
		bw.close();
	}
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		===================1syll================
		System.out.println("===================1syll================");

		File lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-0100words.lex");
		File testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-0100words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-0500words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-1000words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		
//		===================2syll================
		System.out.println("===================2syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-0100words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-0500words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-1000words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
//		===================3syll================
		System.out.println("===================3syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-0100words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-0500words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-1000words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
//		===================4+syll================
		System.out.println("===================4+syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-0100words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-0500words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-1000words.test");
		reco_dist_levenshtein(lexpath,testpath);
		
	}

}
