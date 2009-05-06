package com.wesabe.servlet.normalizers.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.CookieNormalizer;
import com.wesabe.servlet.normalizers.ValidationException;

@RunWith(Enclosed.class)
public class CookieNormalizerTest {
	public static class Normalizing_A_Valid_Cookie {
		private CookieNormalizer normalizer;
		private Cookie cookie;
		
		@Before
		public void setup() throws Exception {
			this.normalizer = new CookieNormalizer();
			
			this.cookie = new Cookie("sessionid", "DEADBEEF");
			cookie.setDomain("example.com");
			cookie.setMaxAge(3000);
			cookie.setPath("/");
			cookie.setVersion(1);
			cookie.setSecure(false);
		}
		
		@Test
		public void itReturnsAnEquivalentCookie() throws Exception {
			final Cookie normalizedCookie = normalizer.normalize(cookie);
			assertThat(normalizedCookie.getValue(), is(cookie.getValue()));
			assertThat(normalizedCookie.getDomain(), is(cookie.getDomain()));
			assertThat(normalizedCookie.getMaxAge(), is(cookie.getMaxAge()));
			assertThat(normalizedCookie.getPath(), is(cookie.getPath()));
			assertThat(normalizedCookie.getVersion(), is(cookie.getVersion()));
			assertThat(normalizedCookie.getSecure(), is(cookie.getSecure()));
		}
		
	}
	
	public static class Normalizing_A_Cookie_With_An_Invalid_Name {
		private CookieNormalizer normalizer;
		private Cookie cookie;
		
		@Before
		public void setup() throws Exception {
			this.normalizer = new CookieNormalizer();
			
			this.cookie = mock(Cookie.class);
			when(cookie.getName()).thenReturn("sessi\0nid");
		}
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(cookie);
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: sessi\0nid (not a valid cookie name)"));
			}
		}
		
	}
	
	public static class Normalizing_A_Cookie_With_An_Invalid_Value {
		private CookieNormalizer normalizer;
		private Cookie cookie;
		
		@Before
		public void setup() throws Exception {
			this.normalizer = new CookieNormalizer();
			
			this.cookie = mock(Cookie.class);
			when(cookie.getName()).thenReturn("sessionid");
			when(cookie.getValue()).thenReturn("DEADBEE\0F");
		}
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(cookie);
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: DEADBEE\0F (not a valid cookie value)"));
			}
		}
	}
	
	public static class Normalizing_A_Cookie_With_An_Invalid_Domain {
		private CookieNormalizer normalizer;
		private Cookie cookie;
		
		@Before
		public void setup() throws Exception {
			this.normalizer = new CookieNormalizer();
			
			this.cookie = mock(Cookie.class);
			when(cookie.getName()).thenReturn("sessionid");
			when(cookie.getValue()).thenReturn("DEADBEEF");
			when(cookie.getDomain()).thenReturn("ex\0ample.com");
		}
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(cookie);
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: ex\0ample.com (not a valid hostname)"));
			}
		}
	}
	
	public static class Normalizing_A_Cookie_With_An_Invalid_Path {
		private CookieNormalizer normalizer;
		private Cookie cookie;
		
		@Before
		public void setup() throws Exception {
			this.normalizer = new CookieNormalizer();
			
			this.cookie = mock(Cookie.class);
			when(cookie.getName()).thenReturn("sessionid");
			when(cookie.getValue()).thenReturn("DEADBEEF");
			when(cookie.getDomain()).thenReturn("example.com");
			when(cookie.getPath()).thenReturn("/\0");
		}
		
		@Test
		public void itThrowsAValidationException() throws Exception {
			try {
				normalizer.normalize(cookie);
				fail("should have thrown a ValidationException but didn't");
			} catch (ValidationException e) {
				assertThat(e.getMessage(), is("Invalid value: /\0 (not a valid cookie path)"));
			}
		}
	}
}