package de.uni_koeln.spinfo.avh.data;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class to model diary entries.
 * @author jhermes
 */
public class DiaryEntry {
	
	private String id;
	private String date;
	private String text;
	private Map<String, Location> locations;
	private Map<String, Person> persons;
	private Set<String> unspecified;
	
	/**
	 * Creates diary entry with specified id
	 * @param id
	 */
	public DiaryEntry(String id){
		this.id = id;
		this.locations = new TreeMap<String,Location>();
		this.persons = new TreeMap<String,Person>();
		this.unspecified = new TreeSet<String>();
	}

	/**
	 * Returns the date of the diary entry
	 * @return date of the diary entry
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date of the diary entry
	 * @param date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the content of the diary entry
	 * @return content of the diary entry
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the content of the diary entry
	 * @param text to set
	 */
	public void setText(String text) {
		
		this.text = text.replaceAll("\\s", " ");;
	}

	/**
	 * Returns the id of the diary entry
	 * @return id of the diary entry
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the locations mentioned in the diary entry
	 * @return Set of locations
	 */
	public Set<Location> getLocations() {
		return new TreeSet<Location>(locations.values());
	}
	
	/**
	 * Returns the persons mentioned in the diary entry
	 * @return Set of persons
	 */
	public Set<Person> getPersons() {
		if(this.persons.values()==null) {
			return new TreeSet<Person>();
		}
		return new TreeSet<Person>(this.persons.values());
	}
	
	
	/**
	 * Returns the unspecified information mentioned in the diary entry
	 * @return Set of unspecified information
	 */
	public Set<String> getUnspecified() {
		return unspecified;
	}

	/**
	 * Adds a location 
	 * @param location
	 */
	public void addLocation(Location location){
		locations.put(location.getBbaw_id(), location);
	
	}
	
	/**
	 * Adds a person 
	 * @param person
	 * @return true, if person is new to the diary entry, false otherwise
	 */
	public void addPerson(Person person){
		persons.put(person.getBbaw_id(), person);
	}
	
	/**
	 * Adds a unspecified info string 
	 * @param unspecified info string 
	 * @return true, if unspecified info is new to the diary entry, false otherwise
	 */
	public boolean addUnspecified(String unspec){
		return unspecified.add(unspec);
	}
	
	
	
	public void setLocations(Map<String,Location> locations) {
		this.locations = locations;
	}

	public void setPersons(Map<String,Person> persons) {
		this.persons = persons;
	}

	public void setUnspecified(Set<String> unspecified) {
		this.unspecified = unspecified;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return id + "\t" + date+ "\t" + text + "\t" + locations + "\t" +  persons + "\t" + unspecified;
	}

}
