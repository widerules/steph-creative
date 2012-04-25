package com.droid.gamedev.util;

public class StringUtil {

	/**
	 * Format a string in multiple lines with a certain number of characters
	 * @param src : input string
	 * @param len : minimum number of characters per line
	 * @return output string 
	 */
	public static String formatLines(String src, int len) {
		String res = null;
		char[] sAr = src.toCharArray();
		String sub;
		int n = sAr.length;

		int start = 0;
		for (int i = 0; i < n; i++) {
			if ((sAr[i] == ' ' && (i - start) > len) || i == (n - 1)) {

				sub = (i == n - 1) ? src.substring(start, i + 1) : src
						.substring(start, i);

				if (res == null) {
					res = sub;
				} 
				else {
					res += "\n" + sub;
				}
				start = i + 1;
			}
		}

		return res;

	}

}
