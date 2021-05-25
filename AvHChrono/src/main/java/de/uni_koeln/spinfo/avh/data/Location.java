package de.uni_koeln.spinfo.avh.data;

/**
 * Class representing locations from the editionn humboldt digital corpus
 * @author jhermes
 *
 */
public class Location extends BBAWEntity {

	public Location(String name, String bbaw_id) {
		super(name, bbaw_id);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String toReturn = this.getName() + "|" + this.getBbaw_id();
		return toReturn;
	}
}
