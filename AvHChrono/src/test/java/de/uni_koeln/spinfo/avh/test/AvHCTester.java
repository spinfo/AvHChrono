package de.uni_koeln.spinfo.avh.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.xml.XMLtoCSVConverter;

public class AvHCTester {

	@Test
	public void test() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		XMLtoCSVConverter conv = new XMLtoCSVConverter("AvHChronologie", "AvHChrono.csv");
		conv.process();
	}

}
