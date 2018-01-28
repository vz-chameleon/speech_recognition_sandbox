package levenshtein_recognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Levenshtein {
	private static double coutInsertion = 1.;
	private static double coutOmission = 1.;
	private static HashMap<Object, HashMap<Object, Double>> coutSubstitutionMap = null;

	private static double  coutSubstitution(Object c1, Object c2) {
		if (coutSubstitutionMap == null) {
			return (!c1.equals(c2))?1.:0.;
		}else
			return coutSubstitutionMap.get(c1).get(c2);

	}


	public static double levenshteinDistance(Object[] c1, Object[] c2, char[][] path) {
		double distance = D(c1,c2,c1.length-1,c2.length-1, path);

		return distance;
	}


	private static double D(Object[] c1, Object[] c2,int i, int j, char[][] path) {
		//		System.out.println("D called with aruments : "+c1+", "+ c2 + ", "+i+ ", "+ j);
		if (i<0 || j<0)
			return 0;

		double sub = D(c1,c2,i-1,j-1,path)+coutSubstitution(c1[i], c2[j]);
		double ins = D(c1, c2, i-1,j,path)+coutInsertion;
		double omi = D(c1, c2, i,j-1,path)+coutOmission;

		double min = Math.min(Math.min(sub, ins), omi);

		if (sub == min)
			path[i][j]='s';
		else if (ins == min)
			path[i][j]='i';
		else 
			path[i][j]='o';
		return min;
	}

	public static Character[] optimalTransformations(char[][] path) {
		int i = path.length-1;
		int j = path[0].length-1;
		ArrayList<Character> optimalTransformation = new ArrayList<>();

		while (i>=0 & j>=0) {
			switch (path[i][j]) {
			case 's':
				optimalTransformation.add(0,'s');
//				System.out.println("i,j = "+i+","+j+"   ...... 's'");
				i-=1;j-=1;
				break;
			case 'i':
				optimalTransformation.add(0,'i');
//				System.out.println("i,j = "+i+","+j+"   ...... 'i'");
				i-=1;
				break;
			case 'o':
				optimalTransformation.add(0,'o');
//				System.out.println("i,j = "+i+","+j+"   ...... 'o'");
				j-=1;
				break;
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

		String[] seq1 = "animal".split("(?!^)");
		String[] seq2 = "animel".split("(?!^)");

		char[][] path = new char[seq1.length][seq2.length];

		System.out.println("Distance between '"+Arrays.deepToString(seq1)+"' and '"+Arrays.deepToString(seq2)+"' : "+levenshteinDistance(seq1, seq2, path));

		printpathtable(path, seq1, seq2);


		Character[] optimalTransformation = optimalTransformations(path);
		System.out.println(Arrays.deepToString(path));
		System.out.println("How to get from '"+Arrays.deepToString(seq1)+"' to '"+Arrays.deepToString(seq2)+"' : " + Arrays.deepToString(optimalTransformation) );
		System.out.println(Arrays.deepToString(optimalTransformationsDisplayableStringArray(path, seq1, seq2)));

	}


	public static String[] optimalTransformationsDisplayableStringArray(char[][] path, Object[] seq1,
			Object[] seq2) {

		int i = seq1.length-1;
		int j = seq2.length-1;
		ArrayList<String> optimalTransformationString = new ArrayList<>();

		while (i>=0 & j>=0) {
			switch (path[i][j]) {
			case 's':
				if (seq1[i].equals(seq2[j]))
					optimalTransformationString.add(seq1[i].toString());
				else
					optimalTransformationString.add("("+seq1[i]+"=>"+seq2[j]+")");
				i-=1;j-=1;
				break;
			case 'i':
				optimalTransformationString.add("(=>"+seq2[j]+")");
				i-=1;
				break;
			case 'o':
				optimalTransformationString.add("("+seq1[i]+"=>)");
				j-=1;
				break;
			}
		}
		Collections.reverse(optimalTransformationString);
		return (String[]) optimalTransformationString.toArray(new String[optimalTransformationString.size()]);
	}
}