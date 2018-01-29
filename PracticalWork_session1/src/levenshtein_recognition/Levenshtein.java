package levenshtein_recognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class Levenshtein {
	public static double coutInsertion = 1.;
	public static double coutOmission = 1.;
	public static HashMap<Object, HashMap<Object, Double>> coutSubstitutionMap = null;
	public static HashMap<Object, Double> coutInsertionMap = null;
	public static HashMap<Object, Double> coutOmissionMap = null;

	private static double  coutSubstitution(Object[] seq1, Object[] seq2, int i, int j) {
		if (i<0 || j<0)
			return Double.MAX_VALUE;
		Object c1 = seq1[i];
		Object c2 = seq2[j];		
		if (coutSubstitutionMap == null) {
			return (c1.equals(c2))?0.:1.;
		}else
			return coutSubstitutionMap.get(c1).get(c2);

	}
	
	private static double  coutInsertion(Object[] seq, int i) {
		if (i<0)
			return Double.MAX_VALUE;
		Object c = seq[i];		
		if (coutSubstitutionMap == null) {
			return 1.;
		}else
			return coutInsertionMap.get(c);

	}
	
	private static double  coutOmission(Object[] seq, int i) {
		if (i<0)
			return Double.MAX_VALUE;
		Object c = seq[i];
		if (coutSubstitutionMap == null) {
			return 1.;
		}else
			return coutOmissionMap.get(c);

	}


	public static double levenshteinDistance(Object[] c1, Object[] c2, char[][] path) {
		double distance = D(c1,c2,c1.length-1,c2.length-1, path);

		return distance;
	}


	private static double D(Object[] c1, Object[] c2,int i, int j, char[][] path) {
		//		System.out.println("D called with aruments : "+c1+", "+ c2 + ", "+i+ ", "+ j);
		if (i==-1 && j==-1)
			return 0;
		else if (i==-2 || j==-2)
			return Double.MAX_VALUE;


		double sub = D(c1,c2,i-1,j-1,path)+coutSubstitution(c1, c2, i, j);
		double ins = D(c1, c2, i-1,j,path)+coutInsertion(c1,i);
		double omi = D(c1, c2, i,j-1,path)+coutOmission(c2,j);

		double min = Math.min(Math.min(sub, ins), omi);

		if (sub == min)
			path[i+1][j+1]='s';
		else if (ins == min)
			path[i+1][j+1]='i';
		else 
			path[i+1][j+1]='o';
		return min;
	}

	public static Character[] optimalTransformations(char[][] path) {
		int i = path.length-1;
		int j = path[0].length-1;
		ArrayList<Character> optimalTransformation = new ArrayList<>();
		boolean loopCondition = true;
		while (loopCondition) {
			loopCondition = (i!=0 || j != 0);
			switch (path[i][j]) {
			case 's':
				optimalTransformation.add('s');
				System.out.println("i,j = "+i+","+j+"   ...... 's'");
				i-=1;j-=1;
				
				if (i==0 & j==-1)
					j++;
				else if (j==0 & i==-1)
					i++;
				break;
			case 'i':
				optimalTransformation.add('i');
				System.out.println("i,j = "+i+","+j+"   ...... 'i'");
				i-=1;
				break;
			case 'o':
				optimalTransformation.add('o');
				System.out.println("i,j = "+i+","+j+"   ...... 'o'");
				j-=1;
				break;
			}
			if (i>0 & j==-1) {
				i--;
				j++;
			}else if (j>0 & i==-1) {
				j--;
				i++;
			}
		}
		Collections.reverse(optimalTransformation);
		return (Character[]) optimalTransformation.toArray(new Character[optimalTransformation.size()]);
	}

	public static void printpathtable(char[][] path, Object[] seq1, Object[] seq2){
		//TODO: fix this display
		System.out.println("==============Path table START================ ");
		for (int i = 0; i < path.length; i++) {
			System.out.print(((i==seq1.length)?" ": seq1[seq1.length-i-1]) + " | ");

			for (int j = 0; j < path[i].length; j++) {
				System.out.print(path[i][j] + " ");
			}
			System.out.println();
		}
		for (int j = 0; j < seq2.length+1; j++) {
			System.out.print(((j==0)?"    ": "--"));
		}
		System.out.println();
		for (int j = 0; j < seq2.length+1; j++) {
			System.out.print(((j==0)?"   ": seq2[j-1]) + " ");
		}
		System.out.println();
		System.out.println("==============Path table END================ ");


	}

	public static void main(String[] args) {

		String[] seq1 = "abaarbolesww".split("(?!^)");
		String[] seq2 = "baarbresrw".split("(?!^)");

		char[][] path = new char[seq1.length+1][seq2.length+1];

		System.out.println("Distance between '"+Arrays.deepToString(seq1)+"' and '"+Arrays.deepToString(seq2)+"' : "+levenshteinDistance(seq1, seq2, path));

		printpathtable(path, seq1, seq2);


		Character[] optimalTransformation = optimalTransformations(path);
		System.out.println(Arrays.deepToString(path));
		System.out.println("How to get from '"+Arrays.deepToString(seq1)+"' to '"+Arrays.deepToString(seq2)+"' : " + Arrays.deepToString(optimalTransformation) );
		System.out.println(Arrays.deepToString(optimalTransformationsDisplayableStringArray(path, seq1, seq2)));

	}


	public static String[] optimalTransformationsDisplayableStringArray(char[][] path, Object[] seq1,
			Object[] seq2) {

		int i = path.length-1;
		int j = path[0].length-1;
		ArrayList<String> optimalTransformationString = new ArrayList<>();

		boolean loopCondition = true;
		while (loopCondition) {
			loopCondition = (i!=0 || j != 0);

			switch (path[i][j]) {
			case 's':
				if (seq1[i-1].equals(seq2[j-1]))
					optimalTransformationString.add(seq1[i-1].toString());
				else
					optimalTransformationString.add("("+seq2[j-1]+"=>"+seq1[i-1]+")");
				i-=1;j-=1;
				break;
			case 'i':
				optimalTransformationString.add("(=>"+seq1[i-1]+")");
				i-=1;
				break;
			case 'o':
				optimalTransformationString.add("("+seq2[j-1]+"=>)");
				j-=1;
				break;
			}
		}
		Collections.reverse(optimalTransformationString);
		return (String[]) optimalTransformationString.toArray(new String[optimalTransformationString.size()]);
	}
	
	public static SimpleEntry<String, SimpleEntry<String[], Double>> getrecognisedWordwithAlignmentAndCost(String[] testphonems,HashMap<String, String[]> LexicPhonemsAList){		
		double cost = Double.MAX_VALUE;
		String closestWord = null;
		String[] closestAlignment= null;
		for (Entry<String, String[]> word_phonems_entry : LexicPhonemsAList.entrySet()) {
			String[] lexphonems = word_phonems_entry.getValue();
			char[][] tempPath = new char[testphonems.length+1][lexphonems.length+1];
			double tempCost = Levenshtein.levenshteinDistance(testphonems, lexphonems, tempPath);
			
			if (cost>tempCost) {
				cost = tempCost;
				closestWord = word_phonems_entry.getKey();
				closestAlignment = Levenshtein.optimalTransformationsDisplayableStringArray(tempPath, testphonems, lexphonems);
			}
		}	
		return new SimpleEntry<String, SimpleEntry<String[],Double>>(closestWord, new SimpleEntry<>(closestAlignment, cost));
		
	}
}