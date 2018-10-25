package de.uni_koeln.spinfo.avh.ie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.uni_koeln.spinfo.avh.data.Location;
import de.uni_koeln.spinfo.avh.data.Person;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Class to search Named Entities in texts of DiaryEntries and save them to the specified NE-sets.
 * @author jhermes
 *
 */
public class NamedEntityAggregator {
	
	private AbstractSequenceClassifier<CoreLabel> classifier1;
	private AbstractSequenceClassifier<CoreLabel> classifier2;
	
	/**
	 * Initializes a new NamedEntityAggregator by using two different models for German.
	 * @throws ClassCastException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public NamedEntityAggregator() throws ClassCastException, ClassNotFoundException, IOException{
		String serializedClassifier1 = "StanfordNER/classifiers/dewac_175m_600.crf.ser.gz";
		String serializedClassifier2 = "StanfordNER/classifiers/hgc_175m_600.crf.ser.gz";
		classifier1 = CRFClassifier.getClassifier(serializedClassifier1);
		classifier2 = CRFClassifier.getClassifier(serializedClassifier2);
	}

	/**
	 * Performes NER on specified DiaryEntries and saves them to the specified NE-sets
	 * @param entries 
	 */
	public void doNER(List<DiaryEntry> entries) {
		for (DiaryEntry diaryEntry : entries) {
			String text = diaryEntry.getText();
			System.out.println(diaryEntry);
			addEntitiesToEntry(diaryEntry, extractEntities(classifier1.classifyToString(text, "tabbedEntities", false)));
			addEntitiesToEntry(diaryEntry, extractEntities(classifier2.classifyToString(text, "tabbedEntities", false)));
			//System.out.println(diaryEntry);
		}
	}
	
	private Map<String, List<String>> extractEntities(String tabbedEntities){
		Map<String, List<String>> entities = new TreeMap<String, List<String>>();
		String[] lines = tabbedEntities.split("\n");
		for (String line : lines) {
			String[] elements = line.split("\t");
			if(elements.length > 1 && elements[0].trim()!=""){
				String tag = elements[1];
				String text = elements[0];
				List<String> texts = entities.get(tag);
				if(texts == null){
					texts = new ArrayList<String>();
				}
				texts.add(text);
				entities.put(tag, texts);
			}
		}		
 		return entities;
	}
	
	private void addEntitiesToEntry(DiaryEntry entry, Map<String, List<String>> extractedEntities){
		List<String> locList = extractedEntities.get("I-LOC");
		if(locList!=null){
			for (String loc : locList) {
				entry.addLocation(new Location(loc, loc));
			}
		}
		List<String> perList = extractedEntities.get("I-PER");
		if(perList!=null){
			for (String per : perList) {
				entry.addPerson(new Person(per,per));
			}
		}
		List<String> miscList = extractedEntities.get("I-MISC");
		if(miscList==null){
			miscList = new ArrayList<String>();
		}
		List<String> orgList = extractedEntities.get("I-ORG");
		if(orgList!=null){
			miscList.addAll(orgList);
		}
		for (String misc : miscList) {
			entry.addUnspecified(misc);
		}
	}

}
