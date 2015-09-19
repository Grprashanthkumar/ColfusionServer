/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.similarityMeasures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public class LevenshteinDistance implements SimilarityDistanceMeasure {

	static Logger logger = LogManager.getLogger(LevenshteinDistance.class.getName());

	//Taken from https://github.com/joewandy/BioinfoApp/blob/master/src/com/joewandy/bioinfoapp/model/stringDistance/LevenshteinDistance.java
	/**
	 * Computes the similiarity of the two strings <code>s1</code> and
	 * <code>s2</code> by the following formula: sim<sub>edit</sub>(x, y) = 1 /
	 * (1+editDist(x,y).
	 * 
	 * See also: {@link http://webdocs.cs.ualberta.ca/~lindek/papers/sim.pdf}
	 * 
	 * @return
	 */
	@Override
	public double computeSimilarity(final String value1, final String value2) {
		return 1 / (1 + computeDistance(value1, value2));
	}

	//Taken from https://github.com/joewandy/BioinfoApp/blob/master/src/com/joewandy/bioinfoapp/model/stringDistance/LevenshteinDistance.java
	/**
	 * Compute the Levenshtein distance between the two strings <code>s1</code>
	 * and <code>s2</code>. This is defined to be the minimum number of edits
	 * (insertion, deletion, substitution) that is required to transform
	 * <code>s1</code> to <code>s2</code>.
	 * 
	 * Adapted <code>computeEditDistance</code> to use less space, in the order
	 * of O(min(m, n)), instead of O(m*n). Also, the input strings
	 * <code>s1</code> and <code>s2</code> are normalized to all uppercases.
	 * 
	 * @return The edit distance between <code>s1</code> and <code>s2</code>
	 */
	@Override
	public double computeDistance(final String value1, final String value2) {
		
		String s1 = value1;
		String s2 = value2;
		
		// check preconditions
		int m = s1.length();
		int n = s2.length();
		if (m == 0) {
			return n; 			// some simple heuristics
		} else if (n == 0) {
			return m; 			// some simple heuristics
		} else if (m > n) {
			String tempString = s1; 	// swap m with n to get O(min(m, n)) space
			s1 = s2;
			s2 = tempString;
			int tempInt = m;
			m = n;
			n = tempInt;
		}

		// normalize case
		s1 = s1.toUpperCase();
		s2 = s2.toUpperCase();

		// Instead of a 2d array of space O(m*n) such as int d[][] = new int[m +
		// 1][n + 1], only the previous row and current row need to be stored at
		// any one time in prevD[] and currD[]. This reduces the space
		// complexity to O(min(m, n)).
		int prevD[] = new int[n + 1];
		int currD[] = new int[n + 1];
		int temp[]; // temporary pointer for swapping

		// the distance of any second string to an empty first string
		for (int j = 0; j < n + 1; j++) {
			prevD[j] = j;
		}

		// for each row in the distance matrix
		for (int i = 0; i < m; i++) {

			// the distance of any first string to an empty second string
			currD[0] = i + 1;
			char ch1 = s1.charAt(i);

			// for each column in the distance matrix
			for (int j = 1; j <= n; j++) {

				char ch2 = s2.charAt(j - 1);
				if (ch1 == ch2) {
					currD[j] = prevD[j - 1];
				} else {
					currD[j] = minOfThreeNumbers(prevD[j] + 1,
							currD[j - 1] + 1, prevD[j - 1] + 1);
				}

			}

			temp = prevD;
			prevD = currD;
			currD = temp;

		}

		// after swapping, the final answer is now in the last column of prevD
		return prevD[prevD.length - 1];
	}
	
	//Taken from https://github.com/joewandy/BioinfoApp/blob/master/src/com/joewandy/bioinfoapp/model/stringDistance/LevenshteinDistance.java
	/**
	 * Returns the minimum of three numbers
	 * 
	 * @param num1
	 *            The first number
	 * @param num2
	 *            The second number
	 * @param num3
	 *            The third number
	 * @return The minimum of first, second and third numbers
	 */
	private int minOfThreeNumbers(final int num1, final int num2, final int num3) {
		return Math.min(num1, Math.min(num2, num3));
	}

}
