package de.uni_koeln.spinfo.avh.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.Workflow;

public class AvHCTester {

	@Test
	public void test() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassCastException, ClassNotFoundException {
		Workflow.process("AvHChronologie");
	}

}
