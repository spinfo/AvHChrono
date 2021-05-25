package de.uni_koeln.spinfo.avh.data;

import java.io.Serializable;
import java.net.URL;

import org.jsoup.Jsoup;

/**
 * Class representing persons from the edition humboldt digital corpus 
 * @author jhermes
 *
 */
public class Person extends BBAWEntity implements Serializable{

	private static final long serialVersionUID = 1458342242238769290L;
	
	private String pictureUrl;
	
	
	
	/** Creates a new person
	 * @param name
	 * @param bbaw_id
	 * @param pictureUrl
	 */
	public Person(String name, String bbaw_id, String pictureUrl) {
		super(name, bbaw_id);
		this.pictureUrl = pictureUrl;
	}

	/** Creates a new person
	 * @param name
	 * @param bbaw_id
	 */
	public Person(String name, String bbaw_id) {
		super(name, bbaw_id);
		getPicUrl();
	}

	
	/** Returns url of the persons pic from wikimedia commons
	 * @return
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}
	
	private void getPicUrl() {
		String resource = "http://edition-humboldt.de/register/personen/detail.xql?id="+ this.getBbaw_id();
		System.out.println(resource);
		URL extractWikiCommonsImages = extractWikiCommonsImages(resource);
		System.out.println("******************" + extractWikiCommonsImages(resource));
		
		if(extractWikiCommonsImages!=null)
			pictureUrl = extractWikiCommonsImages.toExternalForm();
		System.out.println("++++++++++++++++" + pictureUrl);
		
	}
	
	private URL extractWikiCommonsImages(String url){
        try {
			String wikiURL = Jsoup.connect(url).get()
			        .select("div.portrait a[href*=commons.wikimedia.org]")
			        .first().attr("href");
			return new URL(
			        Jsoup.connect(wikiURL).get()
			        .select("div#file a").first().attr("href")
			);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} 
    } 
	

	public String toString() {
		String toReturn = this.getName() + "|" + this.getBbaw_id() + "|"+  pictureUrl;
		return toReturn;
	}
	
}
