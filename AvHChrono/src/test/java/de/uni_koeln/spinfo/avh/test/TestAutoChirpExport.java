package de.uni_koeln.spinfo.avh.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.ConvertBBAWData2Chirps;

public class TestAutoChirpExport {

	@Test
	public void test() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		ConvertBBAWData2Chirps.covert2Chirps("Chronologie_201207", "ChronoFull210525.tsv");
	}
		

}
