package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.QueryStringNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class QueryStringNormalizerTest {
	private static String normalize(String queryString) throws Exception {
		final QueryStringNormalizer normalizer = new QueryStringNormalizer();
		return normalizer.normalize(queryString);
	}
	
	private static void assertThrowsException(String queryString) {
		final QueryStringNormalizer normalizer = new QueryStringNormalizer();
		try {
			normalizer.normalize(queryString);
			fail("should have thrown a ValidationException but didn't");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}
	
	public static class Normalizing_A_Valid_Query_String {
		@Test
		public void itPassesThemThrough() throws Exception {
			assertEquals("q=food", normalize("q=food"));
			assertEquals("q=food&g=two", normalize("q=food&g=two"));
		}
	}
	
	public static class Normalizing_An_Encoded_Query_String {
		@Test
		public void itNormalizesTheEncodedCharacters() throws Exception {
			assertEquals("q=either%2For", normalize("q=either%2for"));
		}
	}
	
	public static class Normalizing_A_Double_Encoded_Query_String {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("q=either%252for");
			assertThrowsException("q%252fa=either");
		}
	}
	
	public static class Normalizing_A_Paramless_Query_String {
		@Test
		public void itPassesThemThrough() throws Exception {
			assertEquals("food", normalize("food"));
		}
	}
	
	public static class Normalizing_An_Encoded_Paramless_Query_String {
		@Test
		public void itNormalizesTheEncodedCharacters() throws Exception {
			assertEquals("either%2For", normalize("either%2for"));
		}
	}
}
