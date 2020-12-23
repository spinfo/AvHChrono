package de.uni_koeln.spinfo.avh.test;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.converters.AutoChirpExporter;
import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class TestAutoChirpExport {

	@Test
	public void test() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		XMLtoCSVConverter xtc = new XMLtoCSVConverter("Chronologie_2012", "output/export2012.csv");
		//xtc = new XMLtoCSVConverter("AVHChronoShort", "output/export1810_2.csv");
		List<DiaryEntry> importedDiaryEntries = xtc.process();
		xtc.writeCSVFile(importedDiaryEntries);
		System.out.println("List size: " + importedDiaryEntries.size());
		
		AutoChirpExporter ace = new AutoChirpExporter("output");
		ace.generateAutoChirpExport(importedDiaryEntries, "Chrono_ehd-v6.tsv");
		
	}
		

}
