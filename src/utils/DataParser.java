package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class DataParser {
	
	/**
	 * Loads a .lex or a .test file into a <{@code HashMap<String,String[]>} 
	 * @param lex : the file to load into a HashMap
	 * @return a {@code HashMap<String,String[]>} linking each word (the {@code String}) of the file (<b>lex</b>) and the corresponding array of phonemes ({@code String[]}
	 * @throws IOException when fails to parse the file properly
	 */
	public static List<Entry<String,String[]>> lex_or_test_to_HashMap(File lex) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(lex));
		List<Entry<String,String[]>> WordPhonemsHashmap = new  ArrayList<Entry<String,String[]>>();
		
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String word = split[0];
			String[] phonems = split[1].split(" ");
			WordPhonemsHashmap.add(new AbstractMap.SimpleEntry(word,phonems));
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

	
	// ==================================================================================================================
	// ============================ Parsers and other utils for learning and traind data ================================
	// ==================================================================================================================
	
	/**
	 * A function to parse train data files into Hashmaps representing the reference phoneme list paired to the tested phoneme list
	 * @param train_data : the file containing training data
	 * @return HashMap<String[],String[]> : pairs of (reference, test) phonemes lists
	 * @throws IOException
	 */
	public static List<Entry<String[],String[]>> train_to_List(File train_data) throws IOException {		
		// Load train data file
		BufferedReader br = new BufferedReader(new FileReader(train_data));
		
		// For train data, there is no need to keep the word as a key, here the key is the expected list of phonems (reference) and value is the test list of phonems
		List<Entry<String[],String[]>> Ref_test_PhonemsSet = new ArrayList<Entry<String[],String[]>>();
		
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String word = split[0]; // Not used here
			String[] ref_phonems = split[1].replace("[", "").replace("]", "").split(" ");
			String[] test_phonems = split[2].replace("[", "").replace("]", "").split(" ");
			Ref_test_PhonemsSet.add(new AbstractMap.SimpleEntry(ref_phonems,test_phonems));
		}
		br.close(); 
		
		return Ref_test_PhonemsSet;
	}
	
	/**
	 * A function to create an empty hashmap of phoneme occurrences. All available phonemes are loaded from the symbol list file
	 * @return HashMap<String, Integer> - A hashmap with all the possible phoneme symbols, associated to a frequency of occurrence at 0
	 * @throws IOException
	 */
	public static <V> HashMap<String, V> create_empty_hashmap_for_phoneme_occurrence(V value) throws IOException{
		HashMap<String, V> phonem_occurrence = new HashMap<>();
		
		// Load symbols list data file
		BufferedReader br = new BufferedReader(new FileReader(new File("resources/Master-Audition-TD-2018.01-data-v1.0/liste_symboles.dat")));
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String phonem = split[0]; 
			phonem_occurrence.put(phonem, value);
		}
		br.close();
		
		return phonem_occurrence;
	}
	
	/**
	 * A function to create an empty hashmap of hashmaps for phonemes alignement. 
	 * Key : the phoneme's symbol, Value : result of function 'create_empty_hashmap_for_phoneme_occurrence()'
	 * @return
	 * @throws IOException
	 */
	
	public static <V> HashMap<String, HashMap<String, V>> create_empty_hashmap_matrix_of_phoneme_alignment(V value) throws IOException{
		HashMap<String, HashMap<String, V>> phonem_occurrence = new HashMap<>();
		
		// Load symbols list data file
		BufferedReader br = new BufferedReader(new FileReader(new File("resources/Master-Audition-TD-2018.01-data-v1.0/liste_symboles.dat")));
		String line;
		while ((line = br.readLine()) != null) {
			String split[]= line.split("\t");
			String phonem = split[0]; 
			phonem_occurrence.put(phonem, create_empty_hashmap_for_phoneme_occurrence(value)); 
		}
		br.close();
		
		return phonem_occurrence;
	}
	
	public static String[] load_phoneme_symbols_as_list() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File("resources/Master-Audition-TD-2018.01-data-v1.0/liste_symboles.dat")));
		String line;
		int line_nb=0;
		while ((line = br.readLine()) != null) {
			line_nb++;
		}
		br.close();
		
		String[] phoneme_list = new String[line_nb];
		int i=0;
		br = new BufferedReader(new FileReader(new File("resources/Master-Audition-TD-2018.01-data-v1.0/liste_symboles.dat")));
		String line2;
		while ((line2 = br.readLine()) != null) {
			String split[]= line2.split("\t");
			phoneme_list[i]=split[0]; 
			i++;
		}
		br.close();
		
		return phoneme_list;
	}
	
	public static void export_trained_model_to_file(Double p_sub, Double p_ins, Double p_omi, HashMap<String, Double> p_insertion, HashMap<String, HashMap<String, Double>> p_alignment,String output_path) throws IOException{
		Writer filewriter = new BufferedWriter(new FileWriter(output_path));
		filewriter.write("Psub;Pins;Pomi \n");
		filewriter.write(""+p_sub+";"+p_ins+";"+p_omi+"\n");
		filewriter.write("#Une ligne par symbole de reference; une colonne par symbole de test \n");
		
		StringBuilder sb = new StringBuilder("  ");
		for (String symbol : DataParser.load_phoneme_symbols_as_list())
			sb.append(";"+symbol);
		
		filewriter.write(sb.toString()+"\n");
		
		String[] phoneme_symbol_list = DataParser.load_phoneme_symbols_as_list();
		
		for (String symbol : phoneme_symbol_list){
			sb = new StringBuilder(symbol);
			
			HashMap<String, Double> alignment_map_for_ref_phoneme = p_alignment.get(symbol);
			for (String test_phoneme_symbol : phoneme_symbol_list)	
				sb.append(";"+alignment_map_for_ref_phoneme.get(test_phoneme_symbol));
		
			filewriter.write(sb.toString()+"\n");
			
		}
		
		filewriter.write("Proba insertions...\n");
		sb = new StringBuilder("<ins>");
		for (String symbol : phoneme_symbol_list)
			sb.append(";"+p_insertion.get(symbol));
		
		filewriter.write(sb.toString());
		filewriter.flush();
		filewriter.close();
	}
	
	
	// ==================================================================================================================


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

	}
}
