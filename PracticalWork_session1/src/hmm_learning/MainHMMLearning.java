package hmm_learning;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import levenshtein_recognition.Levenshtein;

import utils.DataParser;

public class MainHMMLearning {
	
	
	private static int[][] n_align_matrix; //Matrix to count the number of times the phoneme of reference is aligned to the phoneme test
	private static int[] n_ins_vector; 	   // Vector containing the number of times the test phoneme is inserted into a sequence
	
	private static int n_substitutions = 0; //Global number of substitutions
	private static int n_omissions = 0;     //Global number of omissions
	private static int n_insertions= 0;		//Global number of insertions
	
	
	public static void parse_and_update_on_levenshtein_results(String[] levenshtein_result){
		
		for (String suggested_transformation : levenshtein_result){
			if (suggested_transformation.startsWith("(")){
				Matcher m = Pattern.compile("\\((.*)=>(.*)\\)").matcher(suggested_transformation);
				if (m.matches()){
					System.out.println("First term : "+m.group(1));
					System.out.println("Second term : "+m.group(2));
					
					// If the first term is nothing, we have an insertion
					if (m.group(1).equals("")){
						n_insertions++;
						System.out.println("An insertion was made !");
					}
					else if (m.group(2).equals("")){
						n_omissions++;
						System.out.println("An omission was made !");
					}
					else{
						n_substitutions++;
						System.out.println("A substitution was made !");
					}
				}
				else
					System.err.println("In Levenshtein's result there is a string beginning with '(' but not matching '(. => .)' !!");
			}
			else{
				//Update in the diagonal
			}
		}
		
	}
	
	public static void apprentissage_HMM_discret(File modeleHMM_init, File donnees_app, String modl_appris_output_filename) throws IOException{

		// The list of phonem hashmaps parsed from the initial model
		HashMap[] HMMMaps_init_model  = DataParser.load_HMMMaps(modeleHMM_init);
		
		// A hashmap containing (reference,test) phonems list
		HashMap<String[],String[]> trainPhonemsHashmap  = DataParser.train_to_Hashmap(donnees_app);
		
		// For each example in train data
		for (String[] ref_phonemes : trainPhonemsHashmap.keySet()){
			String[] test_phonemes = trainPhonemsHashmap.get(ref_phonemes);
			char[][] tempPath = new char[test_phonemes.length+1][ref_phonemes.length+1];
			double tempCost = Levenshtein.levenshteinDistance(test_phonemes, ref_phonemes, tempPath);
			
			parse_and_update_on_levenshtein_results(Levenshtein.optimalTransformationsDisplayableStringArray(tempPath, test_phonemes, ref_phonemes));
			
		}
		
		
	}
		
	public static void main(String[] args) {
		
		File trainData = new File("resources/Master-Audition-TD-2018.01-data-v1.0/train-01000items.train");
		File modeleHMM_init = new File("resources/Master-Audition-TD-2018.01-data-v1.0/modele_discret_initialise.dat");
		
		try {
			apprentissage_HMM_discret(modeleHMM_init, trainData, "resources/learning_trained/model_trained_1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
