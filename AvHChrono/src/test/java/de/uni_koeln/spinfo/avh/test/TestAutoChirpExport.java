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
		List<DiaryEntry> importDiaryEntries = xtc.importDiaryEntries();
		//for (DiaryEntry diaryEntry : importDiaryEntries) {
		String content = AutoChirpExporter.generateAutoChirpExport(importDiaryEntries);
		File export = new File("output/AvHTweets.tsv");
		PrintWriter out = new PrintWriter(new FileWriter(export));
		out.println(content);
		out.flush();
		out.close();
		//}
	}

}
