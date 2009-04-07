package com.wesabe.servlet.normalizers.util.tests;

import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import org.junit.runner.RunWith;

import com.wesabe.servlet.normalizers.util.EmptyEnumeration;

@RunWith(Enclosed.class)
public class EmptyEnumerationTest {
	public static class An_Empty_Enumeration {
		private final Enumeration<Integer> empty = EmptyEnumeration.build();
		
		@Test
		public void itHasNoNextElement() throws Exception {
			assertFalse(empty.hasMoreElements());
		}
		
		@Test
		public void itSeriouslyHasNoElements() throws Exception {
			try {
				empty.nextElement();
				fail("wasn't supposed to have any elements");
			} catch (NoSuchElementException e) {
				assertTrue(true);
			}
		}
	}
}
