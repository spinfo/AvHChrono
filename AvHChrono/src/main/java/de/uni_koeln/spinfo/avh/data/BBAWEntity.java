package de.uni_koeln.spinfo.avh.data;

public class BBAWEntity {
	
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
	
	
	

}
