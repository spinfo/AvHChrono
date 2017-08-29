package de.uni_koeln.spinfo.avh.converters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import autoChirp.preProcessing.HeidelTimeWrapper;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

/**
 * Class to export DiaryEntries to a autoChirp import File.
 * @author jhermes
 *
 */
public class AutoChirpExporter {
	
	private File outputDir;
	
	/**
	 * Creates a new AutoChirpExporter on specified output directory
	 * @param outputDirectoryPath
	 */
	public AutoChirpExporter(String outputDirectoryPath){
		outputDir = new File(outputDirectoryPath);
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
	}

	/**
	 * Exports the specified DiaryEntries to a file with specified name in the output directory of this exporter.
	 * @param entries DiaryEnties to export
	 * @param filename Name of the output file (will be generated within the output dir)
	 * @throws IOException 
	 */
	public void generateAutoChirpExport(List<DiaryEntry> entries, String filename) throws IOException {
		HeidelTimeWrapper ht = initializeHeideltime();
		StringBuffer buff = new StringBuffer();
		for (DiaryEntry diaryEntry : entries) {
			String date = diaryEntry.getDate();
			String[] split = date.split("-");
			if(split.length<3){
				date += ("-01");
			}
			split = date.split("-");
			if(split.length<3){
				date += ("-01");
			}
			int year = Integer.parseInt(date.substring(0, 4));
			String text = diaryEntry.getText();
			String time = getTimeFromString(ht, text);
			if(time==null) time = "12:00";
			buff.append(date);
			buff.append("\t");
			buff.append(time);
			buff.append("\t");
			buff.append("["+ year + "] ");
			String fulltext = "http://edition-humboldt.de/"+diaryEntry.getId() + "\n" + text;
			fulltext = StringEscapeUtils.escapeJava(fulltext);
			buff.append(fulltext);
			buff.append("\n");
		}
		
		
		File outputFile = new File(outputDir, filename);
		PrintWriter out = new PrintWriter(new FileWriter(outputFile));
		String toAdd = buff.toString();
		out.println(toAdd);
		out.flush();
		out.close();
	}

	private String getTimeFromString(HeidelTimeWrapper ht, String toProcess) {
		String processed;
		processed = getTime(toProcess, ht);
		return processed;
	}

	private HeidelTimeWrapper initializeHeideltime() {
		HeidelTimeWrapper ht = new HeidelTimeWrapper(Language.GERMAN, DocumentType.NARRATIVES, OutputType.TIMEML,
				"/heideltime/config.props", POSTagger.TREETAGGER, false);
		return ht;
	}

	public String getTime(String text, HeidelTimeWrapper ht) {
		String timeml;
		try {
			timeml = ht.process(text);
		} catch (DocumentCreationTimeMissingException e) {
			e.printStackTrace();
			return null;
		}

		
		String toReturn = parseTime(timeml);
		if(toReturn==null) return null;
		if(toReturn.equals("MO")) return "08:00";
		if(toReturn.equals("EV")) return "20:00";
		if(toReturn.equals("AF")) return "16:00";
		if(toReturn.equals("NI")) return "23:00";
		if(toReturn.equals("DT")) return "12:00";
		return toReturn;

	}

	private String parseTime(String string) {
		String regex = "type=\"TIME\" value=\"[0-9|X]{4}-month-dayT([0-9|A-Z|:]+)\">";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			// times.put(matcher.group(1), string);
			System.out.println(matcher.group(1));
			return matcher.group(1);
		}

		regex = "type=\"TIME\" value=\"[0-9|X]{4}-[0-9|X]{2}-[0-9|X]{2}T([0-9|A-Z|:]+)\">";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(string);

		if (matcher.find()) {
			// times.put(matcher.group(1),string);
			System.out.println(matcher.group(1));
			return matcher.group(1);
		}

		return null;
	}

}
