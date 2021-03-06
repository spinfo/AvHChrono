package de.uni_koeln.spinfo.avh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.uni_koeln.spinfo.avh.ie.NamedEntityAggregator;

/**
 * Class to address several procedures for the Humboldt Diary Data.
 * @author jhermes
 *
 */
public class Workflow {

	/**
	 * Processes the data found in the specified input folder  
	 * @param inputFolderLocation
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ClassCastException
	 * @throws ClassNotFoundException
	 */
	public static void fullWorkflow(String inputFolderLocation) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassCastException, ClassNotFoundException{
		
		//Output file location
		String convertedDataFile = "output/AvHChrono_2212.csv";
		String processedDataFile = "output/AvHChronoTagged_2012.csv";
		
		//Read XML data and convert them to DiaryEntries / a csv file
		XMLtoCSVConverter conv = new XMLtoCSVConverter(inputFolderLocation, convertedDataFile);
		List<DiaryEntry> readEntries = conv.process();
		conv.writeCSVFile(readEntries);
		//Perform NER on converted data
		NamedEntityAggregator ner	 = new NamedEntityAggregator();
		ner.doNER(readEntries);
		
		//Write data to csv file
		PrintWriter out = new PrintWriter(new FileWriter(new File(processedDataFile)));
		out.println("ID \t Date \t Text \t I-LOC \t I-PER \t I-MISC & IORG");
		for (DiaryEntry diaryEntry : readEntries) {
			out.println(diaryEntry);
		}
		out.flush();
		out.close();
	}
	
	public static void readCSVwriteXML(String inputFolderLocation, String inputCSVlocation) throws IOException{
		XMLtoCSVConverter conv = new XMLtoCSVConverter(inputFolderLocation, inputCSVlocation);		
		List<DiaryEntry> importDiaryEntries = conv.importDiaryEntries();
		for (DiaryEntry diaryEntry : importDiaryEntries) {
			System.out.println(diaryEntry.getLocations());
		}
	}

	
}
