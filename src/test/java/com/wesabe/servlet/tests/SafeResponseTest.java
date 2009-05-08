package com.wesabe.servlet.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.BadResponseException;
import com.wesabe.servlet.SafeResponse;

@RunWith(Enclosed.class)
public class SafeResponseTest {
	private static abstract class Context {
		protected SafeResponse response;
		protected HttpServletResponse servletResponse;
		
		public void setup() throws Exception {
			this.servletResponse = mock(HttpServletResponse.class);
			this.response = new SafeResponse(servletResponse);
		}
	}
	
	public static class Adding_A_Valid_Cookie extends Context {
		private Cookie cookie;
		
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
			
			this.cookie = new Cookie("sessionid", "blorp");
		}
		
		@Test
		public void itAddsTheCookie() throws Exception {
			response.addCookie(cookie);
			
			verify(servletResponse).addCookie(argThat(new BaseMatcher<Cookie>() {
				@Override
				public boolean matches(Object obj) {
					if (obj instanceof Cookie) {
						final Cookie cookie = (Cookie) obj;
						return cookie.getName().equals("sessionid") && cookie.getValue().equals("blorp");
					}
					return false;
				}

				@Override
				public void describeTo(Description desc) {
					desc.appendText("a normalized cookie");
				}
			}));
		}
	}
	
	public static class Adding_An_Invalid_Cookie extends Context {
		private Cookie cookie;
		
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
			this.cookie = mock(Cookie.class);
			when(cookie.getName()).thenReturn("\0\0\0\0DEATH");
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.addCookie(cookie);
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).addCookie(cookie);
		}
	}
	
	public static class Adding_A_Valid_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itAddsTheHeader() throws Exception {
			response.addHeader("Server", "Dingomatic v4.2");
			
			verify(servletResponse).addHeader("Server", "Dingomatic v4.2");
		}
	}
	
	public static class Adding_A_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.addHeader("Se\0rver", "Dingomatic v4.2");
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).addHeader("Se\0rver", "Dingomatic v4.2");
		}
	}
	
	public static class Adding_A_Header_With_An_Invalid_Value extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.addHeader("Server", "Ding\0omatic v4.2");
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).addHeader("Server", "Ding\0omatic v4.2");
		}
	}
	
	public static class Setting_A_Valid_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itSetsTheHeader() throws Exception {
			response.setHeader("Server", "Dingomatic v4.2");
			
			verify(servletResponse).setHeader("Server", "Dingomatic v4.2");
		}
	}
	
	public static class Setting_A_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.setHeader("Se\0rver", "Dingomatic v4.2");
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).setHeader("Se\0rver", "Dingomatic v4.2");
		}
	}
	
	public static class Setting_A_Header_With_An_Invalid_Value extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.setHeader("Server", "Ding\0omatic v4.2");
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).setHeader("Server", "Ding\0omatic v4.2");
		}
	}
	
	public static class Adding_A_Valid_Date_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itAddsTheHeader() throws Exception {
			response.addDateHeader("Last-Modified-At", 200L);
			
			verify(servletResponse).addDateHeader("Last-Modified-At", 200L);
		}
	}
	
	public static class Adding_A_Date_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.addDateHeader("Last-\0Modified-At", 200L);
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).addDateHeader("Last-\0Modified-At", 200L);
		}
	}
	
	public static class Setting_A_Valid_Date_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itSetsTheHeader() throws Exception {
			response.setDateHeader("Last-Modified-At", 200L);
			
			verify(servletResponse).setDateHeader("Last-Modified-At", 200L);
		}
	}
	
	public static class Setting_A_Date_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.setDateHeader("Last-\0Modified-At", 200L);
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).setDateHeader("Last-\0Modified-At", 200L);
		}
	}
	
	public static class Adding_A_Valid_Integer_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itAddsTheHeader() throws Exception {
			response.addIntHeader("X-Fun-Times", 200);
			
			verify(servletResponse).addIntHeader("X-Fun-Times", 200);
		}
	}
	
	public static class Adding_An_Integer_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.addIntHeader("X\0-Fun-Times", 200);
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).addIntHeader("X\0-Fun-Times", 200);
		}
	}
	
	public static class Setting_A_Valid_Integer_Header extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itSetsTheHeader() throws Exception {
			response.setIntHeader("X-Fun-Times", 200);
			
			verify(servletResponse).setIntHeader("X-Fun-Times", 200);
		}
	}
	
	public static class Setting_An_Integer_Header_With_An_Invalid_Name extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itThrowsABadResponseException() throws Exception {
			try {
				response.setIntHeader("X\0-Fun-Times", 200);
				fail("should have thrown a BadResponseException but didn't");
			} catch (BadResponseException e) {
				assertThat(e.getBadResponse(), is(sameInstance(servletResponse)));
			}
			
			verify(servletResponse, never()).setIntHeader("X\0-Fun-Times", 200);
		}
	}
	
	public static class Encoding_URLs extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itPassesThemStraightThrough() throws Exception {
			final String url = "url";
			
			assertThat(response.encodeRedirectUrl(url), is(sameInstance(url)));
			assertThat(response.encodeRedirectURL(url), is(sameInstance(url)));
			assertThat(response.encodeUrl(url), is(sameInstance(url)));
			assertThat(response.encodeURL(url), is(sameInstance(url)));
		}
	}
	
	public static class Getting_A_Human_Readable_Representation extends Context {
		@Before
		@Override
		public void setup() throws Exception {
			super.setup();
		}
		
		@Test
		public void itPassesThrough() throws Exception {
			assertThat(response.toString(), is(servletResponse.toString()));
		}
	}
}
