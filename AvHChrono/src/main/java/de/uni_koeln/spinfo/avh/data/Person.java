package de.uni_koeln.spinfo.avh.data;

import java.io.Serializable;
import java.net.URL;

import org.jsoup.Jsoup;

public class Person extends BBAWEntity implements Serializable{

	private static final long serialVersionUID = 1458342242238769290L;
	
	private String pictureUrl;
	
	public String getPictureUrl() {
		return pictureUrl;
	}

	public Person(String name, String bbaw_id) {
		super(name, bbaw_id);
		getPicUrl();
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
	
	public URL extractWikiCommonsImages(String url){
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
	
//	public URL extractWikiCommonsImages(String url) throws IOException, URISyntaxException{
//        String wikiURL = Jsoup.connect(url).get()
//                .select("div.portrait a[href*=commons.wikimedia.org]")
//                .first().attr("href");
//        return new URL(
//                Jsoup.connect(wikiURL).get()
//                .select("div#file a").first().attr("href")
//        );
//    } 


}
