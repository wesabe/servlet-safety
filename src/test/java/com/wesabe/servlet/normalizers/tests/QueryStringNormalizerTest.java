package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
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
	
	public static class Normalizing_A_Valid_Null_String {
		@Test
		public void itPassesItThrough() throws Exception {
			assertThat(normalize(null), is(nullValue()));
		}
	}
	
	public static class Normalizing_A_Valid_Query_String {
		@Test
		public void itPassesItThrough() throws Exception {
			assertThat(normalize("q=food"), is("q=food"));
			assertThat(normalize("q=food&g=two"), is("q=food&g=two"));
		}
	}
	
	public static class Normalizing_An_Encoded_Query_String {
		@Test
		public void itNormalizesTheEncodedCharacters() throws Exception {
			assertThat(normalize("q=either%2for"), is("q=either%2For"));
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
		public void itPassesItThrough() throws Exception {
			assertThat(normalize("food"), is("food"));
		}
	}
	
	public static class Normalizing_An_Encoded_Paramless_Query_String {
		@Test
		public void itNormalizesTheEncodedCharacters() throws Exception {
			assertThat(normalize("either%2for"), is("either%2For"));
		}
	}
}
