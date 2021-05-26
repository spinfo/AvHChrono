package de.uni_koeln.spinfo.avh;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.converters.AutoChirpExporter;
import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class ConvertBBAWData2Chirps {

	/** Method to convert edition humboldt digital corpus to autoChirp innput format
	 * @param inputFolderName Folder with edition humboldt digital corpus in TEI-XML
	 * @param outputFileName name of file that should be generated in the output folder
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static void covert2Chirps(String inputFolderName, String outputFileName) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		
		System.out.println("Parsing BBAW input, generating diary");
		
		XMLtoCSVConverter xtc = new XMLtoCSVConverter(inputFolderName, "output/temp.csv");
		List<DiaryEntry> importedDiaryEntries = xtc.process();
		
		System.out.println("Size of processed entries: " + importedDiaryEntries.size());
		System.out.println("Coverting diary to autoChirp input file");
		
		AutoChirpExporter ace = new AutoChirpExporter("output");
		ace.generateAutoChirpExport(importedDiaryEntries, outputFileName);
		
	}

}
