package de.uni_koeln.spinfo.avh.converters;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autoChirp.preProcessing.HeidelTimeWrapper;
import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class AutoChirpExporter {

	public static String generateAutoChirpExport(List<DiaryEntry> entries) {
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
			buff.append(text);
			buff.append("\n");
		}

		return buff.toString();

	}

	private static String getTimeFromString(HeidelTimeWrapper ht, String toProcess) {
		String processed;
		processed = getTime(toProcess, ht);
		return processed;
	}

	/**
	 * returns a list of TimeML-annotated sentences
	 *
	 * @param document
	 * @return list of tagged sentences
	 */
	private static HeidelTimeWrapper initializeHeideltime() {
		HeidelTimeWrapper ht = new HeidelTimeWrapper(Language.GERMAN, DocumentType.NARRATIVES, OutputType.TIMEML,
				"/heideltime/config.props", POSTagger.TREETAGGER, false);
		return ht;
	}

	public static String getTime(String text, HeidelTimeWrapper ht) {
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

	private static String parseTime(String string) {
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
