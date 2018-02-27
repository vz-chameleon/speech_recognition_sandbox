package hmm_learning;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import levenshtein_recognition.Levenshtein;

import utils.DataParser;

public class MainHMMLearning {
	
	
	private static HashMap<String, HashMap<String, Integer>> n_align_matrix; //Matrix to count the number of times the phoneme of reference is aligned to the phoneme test
	private static HashMap<String, Integer> n_ins_vector; 				   // Vector containing the number of times the test phoneme is inserted into a sequence
	
	private static int n_substitutions = 0; //Global number of substitutions
	private static int n_omissions = 0;     //Global number of omissions
	private static int n_insertions= 0;		//Global number of insertions
	
	private static HashMap<String, HashMap<String, Double>> p_testPhoneme_knowing_refPhoneme;
	private static HashMap<String, Double> p_testPhoneme_knowing_insertion;
	private static double p_substitution ; 
	private static double p_insertion ;
	private static double p_omission; 
	
	private static double cumulated_global_cost = 0;
	
	
	public static void parse_and_update_on_levenshtein_results(String[] levenshtein_result){
				
		for (String suggested_transformation : levenshtein_result){
			if (suggested_transformation.startsWith("(")){
				Matcher m = Pattern.compile("\\((.*)=>(.*)\\)").matcher(suggested_transformation);
				if (m.matches()){
//					System.out.println("First term : "+m.group(1));
//					System.out.println("Second term : "+m.group(2));
					
					// If the first term is nothing, we have an insertion
					if (m.group(1).equals("")){
						
						//Update global number of insertions
						n_insertions++; 
						
						// Update insertion counter for inserted phoneme
						int count = n_ins_vector.get(m.group(2));
						n_ins_vector.put(m.group(2), count+1);
						
//						System.out.println("An insertion was made !");
					}
					else if (m.group(2).equals("")){
						
						//Update global number of omissions
						n_omissions++;						
//						System.out.println("An omission was made !");
					}
					else{
						//Update global number of substitutions
						n_substitutions++;
						
						//Update alignment matrix : (a => b)
						
						// Get hashmap for phoneme 'a', the get the value of alignment for phoneme 'b' in that map
						int count = n_align_matrix.get(m.group(1)).get(m.group(2)); 
						n_align_matrix.get(m.group(1)).put(m.group(2), count+1);
						
//						System.out.println("A substitution was made !");
					}
				}
				else
					System.err.println("In Levenshtein's result there is a string beginning with '(' but not matching '(. => .)' !!");
			}
			else{
				//Update global number of substitutions
				n_substitutions++;
				
				int count = n_align_matrix.get(suggested_transformation).get(suggested_transformation); 
				n_align_matrix.get(suggested_transformation).put(suggested_transformation, count+1);
				
//				System.out.println("It's the same phoneme !");
			}
		}
		
	}
	
	public static void apprentissage_HMM_discret(File modeleHMM_init, File donnees_app, String modl_appris_output_filename) throws IOException{
		cumulated_global_cost=0;
		//Initialise Levenshtein's distance costs using the transition porbabilities of the initial HMM model
		Levenshtein.initialise_with_HMM_model_costs(modeleHMM_init);
		
		//Initialize the structures for counting occurrences of insertions and phonem alignments
		n_ins_vector = DataParser.create_empty_hashmap_for_phoneme_occurrence((int)0);
		n_align_matrix = DataParser.create_empty_hashmap_matrix_of_phoneme_alignment((int)0);
		
		//Initialize the structures for model transition probability estimation
		p_substitution = 0.0;
		p_testPhoneme_knowing_insertion = DataParser.create_empty_hashmap_for_phoneme_occurrence((double)0);
		p_testPhoneme_knowing_refPhoneme= DataParser.create_empty_hashmap_matrix_of_phoneme_alignment((double)0);
				
		// A hashmap containing (reference,test) phonems list
		List<Entry<String[],String[]>> trainPhonemsSet  = DataParser.train_to_List(donnees_app);
		
		
		// For each example in train data
		for (Entry<String[], String[]> ref_test_pair : trainPhonemsSet) {
			String[] ref_phonemes = ref_test_pair.getKey();
			String[] test_phonemes = ref_test_pair.getValue();
			
//			System.out.println("Reference phonemes : "+Arrays.deepToString(ref_phonemes) + " | test_phonemes : "+Arrays.deepToString(test_phonemes));
						
			char[][] tempPath = new char[test_phonemes.length+1][ref_phonemes.length+1];
			double tempCost = Levenshtein.levenshteinDistance(test_phonemes, ref_phonemes, tempPath);
			cumulated_global_cost+=tempCost;
			parse_and_update_on_levenshtein_results(Levenshtein.optimalTransformationsDisplayableStringArray(tempPath, test_phonemes, ref_phonemes));
		}
		
		// ===== Estimation of the substitution, insertion and omission probabilities =====
		p_substitution = ((double)n_substitutions + 1)/(n_substitutions + n_insertions + n_omissions + 3);
		p_insertion= ((double)n_insertions + 1)/(n_substitutions + n_insertions + n_omissions + 3);
		p_omission = ((double)n_omissions + 1)/(n_substitutions + n_insertions + n_omissions + 3);
		
		// ===== Estimation of having a given test phonem given an insertion =====
		// --- Calculate insertions occurrences total ---
		int insertion_occ_sum=0;
		
		for (String test_phoneme : n_ins_vector.keySet())
			insertion_occ_sum += (n_ins_vector.get(test_phoneme)+1);
		
		// --- Estimate probability of insertion ---
		for (String test_phoneme : p_testPhoneme_knowing_insertion.keySet()){
			Double test_ins_proba = ((double)n_ins_vector.get(test_phoneme)+1.0)/insertion_occ_sum;
			p_testPhoneme_knowing_insertion.put(test_phoneme, test_ins_proba);
		}
			
		// ===== Estimation of having a given test phoneme given a reference phoneme =====
		
		// For each reference phoneme in train data
		for (String reference_phoneme : n_align_matrix.keySet()){
			
			// --- Calculate total number of alignments for the given reference phoneme (ie for each test_phoneme)
			double align_occ_sum = 0;
			HashMap<String, Integer> test_phoneme_map = n_align_matrix.get(reference_phoneme);
			
			for (String test_phoneme : test_phoneme_map.keySet())
				align_occ_sum += ((double)test_phoneme_map.get(test_phoneme)+1);
			
			// --- Estimate probability of alignment between reference_phoneme and test_phoneme ---
			HashMap<String, Double> test_phoneme_knowing_ref_mapProba = p_testPhoneme_knowing_refPhoneme.get(reference_phoneme); 

			for (String test_phoneme : test_phoneme_knowing_ref_mapProba.keySet()){
				Double ref_test_align_proba = ((double)test_phoneme_map.get(test_phoneme)+1.0)/align_occ_sum;
				test_phoneme_knowing_ref_mapProba.put(test_phoneme, ref_test_align_proba);
			}
			
			p_testPhoneme_knowing_refPhoneme.put(reference_phoneme, test_phoneme_knowing_ref_mapProba);
		}
		
		//Export model to outpul file
		DataParser.export_trained_model_to_file(p_substitution, p_insertion, p_omission, p_testPhoneme_knowing_insertion, p_testPhoneme_knowing_refPhoneme, modl_appris_output_filename);
	}
		
		
		
	
		
	public static void main(String[] args) {
		
		File trainData = new File("resources/Master-Audition-TD-2018.01-data-v1.0/train-01000items.train");
		File modeleHMM_init = new File("resources/Master-Audition-TD-2018.01-data-v1.0/modele_discret_initialise.dat");
		
		try {
			apprentissage_HMM_discret(modeleHMM_init, trainData, "resources/learning_trained/model_trained_1.dat");
			System.out.println("Coût Global Cumulé : "+MainHMMLearning.cumulated_global_cost);

			apprentissage_HMM_discret(new File("resources/learning_trained/model_trained_1.dat"), trainData, "resources/learning_trained/model_trained_2.dat");
			System.out.println("Coût Global Cumulé : "+MainHMMLearning.cumulated_global_cost);
		
			apprentissage_HMM_discret(new File("resources/learning_trained/model_trained_2.dat"), trainData, "resources/learning_trained/model_trained_3.dat");
			System.out.println("Coût Global Cumulé : "+MainHMMLearning.cumulated_global_cost);
		
			apprentissage_HMM_discret(new File("resources/learning_trained/model_trained_3.dat"), trainData, "resources/learning_trained/model_trained_4.dat");
			System.out.println("Coût Global Cumulé : "+MainHMMLearning.cumulated_global_cost);
		
			apprentissage_HMM_discret(new File("resources/learning_trained/model_trained_4.dat"), trainData, "resources/learning_trained/model_trained_5.dat");
			System.out.println("Coût Global Cumulé : "+MainHMMLearning.cumulated_global_cost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
