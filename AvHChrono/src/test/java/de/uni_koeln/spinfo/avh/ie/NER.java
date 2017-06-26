package de.uni_koeln.spinfo.avh.ie;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class NER {
	
	private AbstractSequenceClassifier<CoreLabel> classifier1;
	private AbstractSequenceClassifier<CoreLabel> classifier2;
	
	public NER() throws ClassCastException, ClassNotFoundException, IOException{
		String serializedClassifier1 = "StanfordNER/classifiers/dewac_175m_600.crf.ser.gz";
		String serializedClassifier2 = "StanfordNER/classifiers/hgc_175m_600.crf.ser.gz";
		classifier1 = CRFClassifier.getClassifier(serializedClassifier1);
		classifier2 = CRFClassifier.getClassifier(serializedClassifier2);
	}

	public List<DiaryEntry> doNER(List<DiaryEntry> entries) {
		for (DiaryEntry diaryEntry : entries) {
			String text = diaryEntry.getText();
			System.out.print(classifier1.classifyToString(text, "tabbedEntities", false));
			System.out.print(classifier2.classifyToString(text, "tabbedEntities", false));
		}
		return null;
	}
	
	private Map<String, List<String>> extractEntities(){
		Map<String, List<String>> entities = new TreeMap<String, List<String>>();
		
		return entities;
	}

}
