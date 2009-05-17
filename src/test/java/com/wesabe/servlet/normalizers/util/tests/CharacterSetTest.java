package com.wesabe.servlet.normalizers.util.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.util.CharacterSet;

@RunWith(Enclosed.class)
public class CharacterSetTest {
	public static class A_Set_Of_Chars {
		private final CharacterSet chars = CharacterSet.of("bcdf");
		
		@Test
		public void itContainsSomeCharacters() throws Exception {
			assertThat(chars.contains('c'), is(true));
		}
		
		@Test
		public void itDoesntContainCharactersBeforeTheBeginning() throws Exception {
			assertThat(chars.contains('a'), is(false));
		}
		
		@Test
		public void itDoesntContainCharactersAfterTheEnd() throws Exception {
			assertThat(chars.contains('z'), is(false));
		}
		
		@Test
		public void itDoesntContainCharactersInGaps() throws Exception {
			assertThat(chars.contains('e'), is(false));
		}
		
		@Test
		public void itTestsForComposition() throws Exception {
			assertThat(chars.composes("dddbbccdd"), is(true));
			assertThat(chars.composes("dddbb1ccdd"), is(false));
			assertThat(chars.composes(null), is(false));
		}
	}
}
