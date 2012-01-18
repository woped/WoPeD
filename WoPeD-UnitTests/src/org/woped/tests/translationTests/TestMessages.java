package org.woped.tests.translationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.woped.translations.Messages;

public class TestMessages {
	
	@Test
	public void testGerman(){
		assertTrue(Messages.exists("Init.ConfigError"));
		assertEquals("German translation", "Initialisierungsfehler", Messages.getString("Init.ConfigError"));
	}
}
