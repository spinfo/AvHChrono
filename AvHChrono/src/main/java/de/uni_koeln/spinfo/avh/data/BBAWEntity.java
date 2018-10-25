package de.uni_koeln.spinfo.avh.data;

public class BBAWEntity implements Comparable{
	
	private String name;
	private String bbaw_id;
		
	public BBAWEntity(String name, String bbaw_id) {
		super();
		this.name = name;
		this.bbaw_id = bbaw_id;
	}

	public String getName() {
		return name;
	}

	public String getBbaw_id() {
		return bbaw_id;
	}

	public int compareTo(Object o) {
		return this.name.compareTo(((BBAWEntity)o).getName());
	}
	
	
	

}
