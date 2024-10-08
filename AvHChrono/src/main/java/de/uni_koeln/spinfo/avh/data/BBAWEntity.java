package de.uni_koeln.spinfo.avh.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Entity of the edition humboldt digital corpus
 * 
 * @author jhermes
 *
 */
public class BBAWEntity implements Comparable<BBAWEntity> {

	private String name;
	private String bbaw_id;

	private static Map<String, String> titleUrls;
	private static String titleUrlsFileName;
	static {
		titleUrlsFileName = "output/titleUrls.ser";
		try (FileInputStream fileIn = new FileInputStream(titleUrlsFileName);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			titleUrls = (Map<String, String>) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		}
		if (titleUrls == null) {
			titleUrls = new HashMap<String, String>();
		}
	}

	public static void serializeMap() {
		try (FileOutputStream fileOut = new FileOutputStream(titleUrlsFileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(titleUrls);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates an entity for specified id. Tries to verify the specified name from the edition humboldt webpage with specified id http://edition-humboldt.de/register/personen/  
	 * Replaces name, if another is found there.
	 * @param name 
	 * @param bbaw_id
	 */
	public BBAWEntity(String name, String bbaw_id) {
		super();
		this.name = name;
		this.bbaw_id = bbaw_id;
		String titleString = "http://edition-humboldt.de/register/personen/detail.xql?id="+ bbaw_id;
		String bbaw_name;
		// this.getBbaw_id();
		if (titleUrls.containsKey(titleString)) {
				 bbaw_name = titleUrls.get(titleString);
		} 
		else {
		  bbaw_name = extractTitleString(titleString);
		  titleUrls.put(titleString, bbaw_name);
		  BBAWEntity.serializeMap();
		}
		if(bbaw_name!=null) {
			this.name = bbaw_name;
		}
		
	}

	/**
	 * Returns name of the entity
	 * 
	 * @return name of the entity
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns id of the entity
	 * 
	 * @return id of the entity
	 */
	public String getBbaw_id() {
		return bbaw_id;
	}

	public int compareTo(BBAWEntity o) {
		return this.name.compareTo(((BBAWEntity) o).getName());
	}

	private String extractTitleString(String url) {
		Document document;
		try {
			// Get Document object after parsing the html from given url.
			document = Jsoup.connect(url).get();

			// Get title from document object.
			String title = document.select("H1").text();
			title = title.replace("edition humboldt digital ", "");
			if (title.trim() == "") {
				return null;
			}
			return title;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
