package com.wesabe.servlet.normalizers.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.UriNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class UriNormalizerTest {
	private static String normalize(String uri) throws Exception {
		final UriNormalizer normalizer = new UriNormalizer();
		return normalizer.normalize(uri);
	}
	
	private static void assertThrowsException(String uri) {
		final UriNormalizer normalizer = new UriNormalizer();
		try {
			normalizer.normalize(uri);
			fail("should have thrown a ValidationException but didn't");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}
	
	public static class Normalizing_URIs_With_Invalid_Characters {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/^^^^^^¨î!");
		}
	}

	public static class Normalizing_URIs_With_Malformed_Hex_Characters {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/dingo/%FV");
		}
	}
	
	public static class Normalizing_URIs_With_Malformed_Double_Encoded_Hex_Characters {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/dingo/%25FV");
		}
	}

	public static class Normalizing_URIs_With_Encoded_Paths {
		@Test
		public void itPassesThemThrough() throws Exception {
			assertEquals("/tags/either%2For", normalize("/tags/either%2for"));
		}
	}
	
	public static class Normalizing_URIs {
		@Test
		public void itPassesThemThrough() throws Exception {
			assertEquals("/tags/food", normalize("/tags/food"));
		}
	}
	
	public static class Normalizing_URIs_With_Trailing_Slashes {
		@Test
		public void itPassesThemThrough() throws Exception {
			assertEquals("/tags/food/", normalize("/tags/food/"));
		}
	}
	
	public static class Normalizing_URIs_With_Double_Encoded_Paths {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/tags/either%252for");
		}
	}
	
	public static class Normalizing_URIs_With_Triple_Encoded_Paths {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/tags/either%25252for");
		}
	}
	
	public static class Normalizing_URIs_With_Unnecessarily_Encoded_Characters {
		@Test
		public void itReducesThemToAMinimallyEncodedForm() throws Exception {
			assertEquals("/needless", normalize("/%6e%65%65%64%6c%65%73%73"));
		}
	}
	
	public static class Normalizing_URIs_With_Long_UTF8_Characters {
		@Test
		public void itNormalizesThemToStandardEncodings() throws Exception {
			assertEquals("/..%2F..%2F..%2F..%2Fetc/shadow", normalize("/%C0%AE%C0%AE%C0%AF%C0%AE%C0%AE%C0%AF%C0%AE%C0%AE%C0%AF%C0%AE%C0%AE%C0%AFetc/shadow"));
		}
	}
	
	public static class Normalizing_URIs_With_Malformed_UTF8 {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException("/tags/%dfo%ee1");
		}
	}
	
	public static class Normalizing_URIs_Which_Are_Too_Long {
		@Test
		public void itThrowsAnException() throws Exception {
			assertThrowsException(buildHugeURI());
		}

		private String buildHugeURI() {
			final StringBuilder builder = new StringBuilder();
			builder.append("/wh");
			for (int i = 1; i <= 10000; i++) {
				builder.append('e');
			}
			return builder.toString();
		}
	}
}
