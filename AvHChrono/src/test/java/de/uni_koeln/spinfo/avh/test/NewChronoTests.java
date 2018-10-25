package de.uni_koeln.spinfo.avh.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class NewChronoTests {

	@Test
	public void test() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		XMLtoCSVConverter converter = new XMLtoCSVConverter("AVHChronoShort", "output/export1810.csv");
		List<DiaryEntry> entries = converter.process();
		converter.writeCSVFile(entries);
	}

}
