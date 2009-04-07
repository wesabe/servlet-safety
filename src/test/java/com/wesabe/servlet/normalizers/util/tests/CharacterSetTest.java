package com.wesabe.servlet.normalizers.util.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.util.CharacterSet;

@RunWith(Enclosed.class)
public class CharacterSetTest {
	public static class A_Set_From_An_Array_Of_Chars {
		private final CharacterSet chars = CharacterSet.of('a', 'b', 'c', 'd');
		
		@Test
		public void itContainsSomeCharacters() throws Exception {
			assertTrue(chars.contains(Character.valueOf('c')));
		}
		
		@Test
		public void itDoesntContainSomeCharacters() throws Exception {
			assertFalse(chars.contains(Character.valueOf('z')));
		}
		
		@Test
		public void itCanHandlePrimitives() throws Exception {
			assertTrue(chars.contains('c'));
			assertFalse(chars.contains('D'));
		}
		
		@Test
		public void itTestsForComposition() throws Exception {
			assertTrue(chars.composes("dddbbccdd"));
			assertFalse(chars.composes("dddbb1ccdd"));
		}
	}
	
	public static class A_Set_From_A_String {
		private final CharacterSet chars = CharacterSet.of("acbd");
		
		@Test
		public void itContainsSomeCharacters() throws Exception {
			assertTrue(chars.contains(Character.valueOf('c')));
		}
		
		@Test
		public void itDoesntContainSomeCharacters() throws Exception {
			assertFalse(chars.contains(Character.valueOf('z')));
		}
		
		@Test
		public void itCanHandlePrimitives() throws Exception {
			assertTrue(chars.contains('c'));
			assertFalse(chars.contains('D'));
		}
	}
}
