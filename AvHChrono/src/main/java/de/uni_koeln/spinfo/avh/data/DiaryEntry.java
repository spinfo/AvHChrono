package de.uni_koeln.spinfo.avh.data;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class DiaryEntry {
	
	private String id;
	private String date;
	private String text;
	private Set<String> locations;
	private Set<String> persons;
	
	public DiaryEntry(String id){
		this.id = id;
		this.locations = new TreeSet<String>();
		this.persons = new TreeSet<String>();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		
		this.text = text.replaceAll("\\s", " ");;
	}

	public String getId() {
		return id;
	}

	public Set<String> getLocations() {
		return locations;
	}
	
	public boolean addLocation(String location){
		return locations.add(location);
	}
	
	public boolean addPerson(String person){
		return persons.add(person);
	}
	
	public String toString(){
		return id + "\t" + date+ "\t" + text + "\t" + locations + "\t" +  persons;
	}

}
