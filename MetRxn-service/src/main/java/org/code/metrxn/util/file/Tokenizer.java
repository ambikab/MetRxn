package org.code.metrxn.util.file;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Splits the tokens from a given line
 * @author ambika_b
 *
 */
public class Tokenizer {

	/**
	 * Fetches the token in a single line
	 * @return
	 */
	public static List<String> readTokens(String delimitter, String inputLine) {
		List<String> tokens = new ArrayList<String>();
		StringTokenizer splitter = new StringTokenizer(inputLine, delimitter);
		while (splitter.hasMoreTokens()) {
			tokens.add(splitter.nextToken());
		}
		return tokens;
	}

}
