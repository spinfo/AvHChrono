package de.uni_koeln.spinfo.avh.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.junit.Test;

public class GrabPicTest {

	@Test
	public void test() throws IOException, URISyntaxException {
		System.out.println(extractWikiCommonsImages("http://edition-humboldt.de/register/personen/detail.xql?id=H0012068"));
		System.out.println(extractWikiCommonsImages("http://edition-humboldt.de/register/personen/detail.xql?id=H0006329"));
	}
	
	public URL extractWikiCommonsImages(String url) throws IOException, URISyntaxException{
        String wikiURL = Jsoup.connect(url).get()
                .select("div.portrait a[href*=commons.wikimedia.org]")
                .first().attr("href");
        return new URL(
                Jsoup.connect(wikiURL).get()
                .select("div#file a").first().attr("href")
        );
    } 

}
