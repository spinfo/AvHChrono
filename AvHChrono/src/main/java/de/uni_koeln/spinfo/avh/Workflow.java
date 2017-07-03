package de.uni_koeln.spinfo.avh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.uni_koeln.spinfo.avh.ie.NamedEntityAggregator;
import de.uni_koeln.spinfo.avh.xml.XMLtoCSVConverter;

public class Workflow {

	public static void process(String inputFolderLocation) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassCastException, ClassNotFoundException{
		
		String convertedDataFile = "output/AvHChrono.csv";
		String processedDataFile = "output/AvHChronoTagged.csv";
		XMLtoCSVConverter conv = new XMLtoCSVConverter(inputFolderLocation, convertedDataFile);
		List<DiaryEntry> readEntries = conv.process();
		
		NamedEntityAggregator ner = new NamedEntityAggregator();
		ner.doNER(readEntries);
		
		PrintWriter out = new PrintWriter(new FileWriter(new File(processedDataFile)));
		
		out.println("ID \t Date \t Text \t I-LOC \t I-PER \t I-MISC & IORG");
		for (DiaryEntry diaryEntry : readEntries) {
			out.println(diaryEntry);
		}
		out.flush();
		out.close();
	}

	
}
