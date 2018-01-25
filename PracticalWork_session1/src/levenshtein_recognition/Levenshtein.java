package levenshtein_recognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Levenshtein {
	private static double coutInsertion = 1.;
	private static double coutOmission = 1.;
	private static HashMap<Character, HashMap<Character, Double>> coutSubstitutionMap = null;

	private static double coutSubstitution(char c1, char c2) {
		if (coutSubstitutionMap == null) {
			return c1!=c2?1.:0.;
		}else
			return coutSubstitutionMap.get(c1).get(c2);

	}


	public static double levenshteinDistance(CharSequence c1, CharSequence c2, char[][] path) {
		double distance = D(c1,c2,c1.length()-1,c2.length()-1, path);

		return distance;
	}


	private static double D(CharSequence c1, CharSequence c2,int i, int j, char[][] path) {
		//		System.out.println("D called with aruments : "+c1+", "+ c2 + ", "+i+ ", "+ j);
		if (i<0 || j<0)
			return 0;

		double sub = D(c1,c2,i-1,j-1,path)+coutSubstitution(c1.charAt(i), c2.charAt(j));
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
		return (Character[]) optimalTransformation.toArray(new Character[optimalTransformation.size()]);
	}

	public static void printpathtable(char[][] path, CharSequence seq1, CharSequence seq2){
		//TODO: fix this display
		System.out.println("==============Path table START================ ");
		for (int i = 0; i < path.length; i++) {
			System.out.print(((i==seq1.length())?" ": seq1.charAt(seq1.length()-i-1)) + " | ");

			for (int j = 0; j < path[i].length; j++) {
				System.out.print(path[i][j] + " ");
			}
			System.out.println();
		}
		for (int j = 0; j < seq2.length()+1; j++) {
			System.out.print(((j==0)?"    ": "--"));
		}
		System.out.println();
		for (int j = 0; j < seq2.length()+1; j++) {
			System.out.print(((j==0)?"   ": seq2.charAt(j-1)) + " ");
		}
		System.out.println();
		System.out.println("==============Path table END================ ");


	}

	public static void main(String[] args) {
		CharSequence seq1 = "supaara";
		CharSequence seq2 = "suprraaa";

		char[][] path = new char[seq1.length()][seq2.length()];

		System.out.println("Distance between '"+seq1+"' and '"+seq2+"' : "+levenshteinDistance(seq1, seq2, path));

//		printpathtable(path, seq1, seq2);


		Character[] optimalTransformation = optimalTransformations(path);
		System.out.println(Arrays.deepToString(path));
		System.out.println("How to get from '"+seq1+"' to '"+seq2+"' : " + Arrays.deepToString(optimalTransformation) );


	}
}