package org.woped.tests.allTests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.tests.editor.TestEditorEventProcessor;
import org.woped.tests.translationTests.TestMessages;


@RunWith(Suite.class)
@Suite.SuiteClasses( {TestMessages.class, TestEditorEventProcessor.class  })

public class AllTests {

	public static Test suite () {
		TestSuite suite= new TestSuite("Test for the whole WoPeD Suite");
		//$JUnit-BEGIN$
		
		//$JUnit-END$
		return suite;
	}
}