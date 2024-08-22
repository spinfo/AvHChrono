package de.uni_koeln.spinfo.avh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import de.uni_koeln.spinfo.avh.converters.AutodoneExporter;
import de.uni_koeln.spinfo.avh.converters.TEItoEntryConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

/**
 * Class to convert Humboldt diary #v10 to Mastodon toots.
 * @author jhermes
 *
 */
public class BuildV10Toots {

	
	
	public static void main(String[] args) {
		String pathToEditionHumboldtDigitalFolder = args[0];
		
		buildV10Toots(pathToEditionHumboldtDigitalFolder);
		
		
	}
	
	public static void buildV10Toots(String inputFolderName){
		String chronologyFolderName = inputFolderName + "/data/chronology";
		String personFolderName = inputFolderName + "/data/index/person";
		
		
		File inputFolder = new File(chronologyFolderName);
		System.out.println(inputFolder.getAbsolutePath());
		List<File> files = Arrays.asList(inputFolder.listFiles());
		
		List<DiaryEntry> entries = new ArrayList<DiaryEntry>();

		System.out.println("Files in chronology: " + files.size());
		try {
			TEItoEntryConverter ttec = new TEItoEntryConverter(personFolderName);
			for (File file : files) {
				entries.add(ttec.process(file));
			}
			
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(entries.size());
		try {
			PrintWriter out = new PrintWriter(new FileWriter(new File("entriesList.tsv")));
			for (DiaryEntry diaryEntry : entries) {
				out.println(diaryEntry.toString());
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AutodoneExporter ade = new AutodoneExporter("output");
		try {
			ade.generateAutoChirpExport(entries, "ehd_v10_Toots_ref.tsv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	
}
