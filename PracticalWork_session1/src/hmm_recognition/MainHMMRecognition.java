package hmm_recognition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import levenshtein_recognition.Levenshtein;

import java.util.AbstractMap.SimpleEntry;

import utils.DataParser;

public class MainHMMRecognition {

	
	
	public static void reco_HMM_discret(File lex,File modeleHMM, File test) throws IOException{
		HashMap<String,String[]> LexicPhonemsHashmap  = DataParser.lex_or_test_to_HashMap(lex);
		
		HashMap[] HMMMaps  = DataParser.load_HMMMaps(modeleHMM);
		
		
		HashMap<String, Double> PSIOLogs = HMMMaps[0];
		Double PsubLog = PSIOLogs.get("PsubLog");
		Double PinsLog = PSIOLogs.get("PinsLog");
		Double PomiLog = PSIOLogs.get("PomiLog");		
		HashMap<String, HashMap<String, Double>> SubstitutionLogsMap = HMMMaps[1];
		HashMap<String, Double> InsertionLogsMap = HMMMaps[2];
		
		HashMap<Object, Double> coutInsertionMap = new HashMap<>();
		HashMap<Object, HashMap<Object, Double>> coutSubstitutionMap = new HashMap<>();
		HashMap<Object, Double> coutOmissionMap = new HashMap<>();
		
		for (String phonem : InsertionLogsMap.keySet()) {
			coutInsertionMap.put(phonem, -PsubLog-InsertionLogsMap.get(phonem));
			coutOmissionMap.put(phonem, -PomiLog);
			HashMap<Object, Double> cs = new HashMap<>();
			for (String phonem2 : InsertionLogsMap.keySet()) {
				cs.put(phonem2, -PinsLog-SubstitutionLogsMap.get(phonem).get(phonem2));
			}
			coutSubstitutionMap.put(phonem, cs);
		}
		
		Levenshtein.coutInsertionMap=coutInsertionMap;
		Levenshtein.coutOmissionMap=coutOmissionMap;
		Levenshtein.coutSubstitutionMap=coutSubstitutionMap;
		

		String line;	
		BufferedReader br = new BufferedReader(new FileReader(test));
		BufferedWriter bw = new BufferedWriter(new FileWriter(test+".HMMDISCRET__testLog"));
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
		
		File modeleHMM = new File("resources/Master-Audition-TD-2018.01-data-v1.0/modele_discret_initialise.dat");

//		===================1syll================
		System.out.println("===================1syll================");

		File lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-0100words.lex");
		File testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-0100words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-0500words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-1syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-1syll-1000words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		
//		===================2syll================
		System.out.println("===================2syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-0100words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-0500words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-2syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-2syll-1000words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
//		===================3syll================
		System.out.println("===================3syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-0100words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-0500words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-3syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-3syll-1000words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
//		===================4+syll================
		System.out.println("===================4+syll================");

		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-0100words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-0100words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-0500words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-0500words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
		
		lexpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/lexicon-4+syll-1000words.lex");
		testpath = new File("resources/Master-Audition-TD-2018.01-data-v1.0/test-4+syll-1000words.test");
		reco_HMM_discret(lexpath,modeleHMM,testpath);
	}

}
