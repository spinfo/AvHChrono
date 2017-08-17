package de.uni_koeln.spinfo.avh.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

import de.uni_koeln.spinfo.avh.converters.AutoChirpExporter;
import de.uni_koeln.spinfo.avh.converters.XMLtoCSVConverter;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class TestAutoChirpExport {

	@Test
	public void test() throws IOException {
		XMLtoCSVConverter xtc = new XMLtoCSVConverter("AvHChronologieNeu", "output/AvHChronoNeu.csv");
		List<DiaryEntry> importedDiaryEntries = xtc.importDiaryEntries();
		//for (DiaryEntry diaryEntry : importDiaryEntries) {
		AutoChirpExporter ace = new AutoChirpExporter("output");
		ace.generateAutoChirpExport(importedDiaryEntries, "AvHDiaryTweets.tsv");
		
	}

}
