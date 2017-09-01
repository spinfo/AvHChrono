package de.uni_koeln.spinfo.avh.test;

import java.io.IOException;
import java.nio.channels.NetworkChannel;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.Workflow;
import de.uni_koeln.spinfo.avh.converters.AutoChirpExporter;
import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class AvHCTester {

	@Test
	public void test() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassCastException, ClassNotFoundException {
		Workflow.fullWorkflow("AVHChronologieNeu");
	}

	@Test
	public void testImporter() throws IOException{
		
		Workflow.readCSVwriteXML("AvHChronologieNeu","output/AvHChronoNeu.csv");
		XMLtoCSVConverter xtc = new XMLtoCSVConverter("AvHChronologieNeu", "output/AvHChronoTagged.csv");
		List<DiaryEntry> importDiaryEntries = xtc.importDiaryEntries();
		for (DiaryEntry diaryEntry : importDiaryEntries) {
			System.out.println(diaryEntry.getPersons());
		}
	}
	
	@Test
	public void testBBAWEntities() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		XMLtoCSVConverter conv = new XMLtoCSVConverter("AvHChronologieNeu", "output/TweetsWithPics.csv");
		List<DiaryEntry> process = conv.process();
		AutoChirpExporter ace = new AutoChirpExporter("output", conv.getPersons());
		ace.generateAutoChirpExport(process, "AvHDiaryTweetsPics.tsv");
	}
}
