package com.wesabe.servlet.normalizers.util;

/**
 * A set of characters, designed exclusively for speed of member checking.
 * 
 * @author coda
 */
public class CharacterSet {
	/**
	 * Creates a character set from an array of {@code char}.
	 * 
	 * @param chars
	 *            the elements of the character set
	 * @return a {@link CharacterSet} containing {@code chars}
	 */
	public static CharacterSet of(char... chars) {
		return of(new String(chars));
	}

	/**
	 * Creates a character set from a string.
	 * 
	 * @param chars
	 *            a string containing the elements of the character set
	 * @return a {@link CharacterSet} containing the characters in {@code chars}
	 */
	public static CharacterSet of(String chars) {
		char minChar = Character.MAX_VALUE;
		char maxChar = Character.MIN_VALUE;
		for (int i = 0; i < chars.length(); i++) {
			final char c = chars.charAt(i);
			if (c < minChar) {
				minChar = c;
			}
			if (c > maxChar) {
				maxChar = c;
			}
		}
		
		final boolean[] characters = new boolean[maxChar - minChar + 1];
		for (char i = minChar; i <= maxChar; i++) {
			characters[i - minChar] = chars.indexOf(i) >= 0;
		}
		
		return new CharacterSet(characters, minChar, maxChar);
	}

	private final boolean[] characters;
	private final char minChar, maxChar;

	private CharacterSet(boolean[] characters, char minChar, char maxChar) {
		this.characters = characters;
		this.minChar = minChar;
		this.maxChar = maxChar;
	}

	/**
	 * Returns {@code true} if {@code c} exists in the set.
	 */
	public boolean contains(char c) {
		return (c >= minChar) && (c <= maxChar) && characters[c - minChar];
	}
	
	/**
	 * Returns {@code true} if {@code s} is composed exclusively of the
	 * characters in {@code this}.
	 */
	public boolean composes(String s) {
		if (s == null) {
			return false;
		}
		
		for (int i = 0; i < s.length(); i++) {
			if (!contains(s.charAt(i))) {
				return false;
			}
		}
		
		return true;
	}
}
