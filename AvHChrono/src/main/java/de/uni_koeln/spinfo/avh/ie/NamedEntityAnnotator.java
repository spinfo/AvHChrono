package de.uni_koeln.spinfo.avh.ie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class NamedEntityAnnotator {
	
	public void annotateDiaryEntries(List<DiaryEntry> toAnnotate){
		for (DiaryEntry diaryEntry : toAnnotate) {
			String text = diaryEntry.getText();
			Set<String> locations = diaryEntry.getLocations();
			List<String> nerList = new ArrayList<String>(locations);
			nerList.addAll(diaryEntry.getPersons());
			Collections.sort(nerList, new StringLengthComparator<String>());
		
			System.out.println(nerList);
		}
	}

}
