package de.uni_koeln.spinfo.avh.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Class representing persons from the edition humboldt digital corpus
 * 
 * @author jhermes
 *
 */
public class Person extends BBAWEntity implements Serializable {

	private static Map<String, String> picUrls;
	private static String picUrlsFileName;
	
	
	static {
		picUrlsFileName = "output/picUrls.ser";
		try (FileInputStream fileIn = new FileInputStream(picUrlsFileName);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			picUrls = (Map<String, String>) in.readObject();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		}
		if (picUrls == null) {
			picUrls = new HashMap<String, String>();
			System.out.println("First Map!");
		}
	}

	private static final long serialVersionUID = 1458342242238769290L;

	private String pictureUrl;

	private String description;
	

	/**
	 * Creates a new person
	 * 
	 * @param name
	 * @param bbaw_id
	 */
	public Person(String name, String bbaw_id, String personFolderName) {
		super(name, bbaw_id);
		getPicUrl();
		this.description = extractPersonInfo(readXMLFile(personFolderName + "/"+bbaw_id.substring(bbaw_id.lastIndexOf('/') + 1)+".xml"));
	}

	/**
	 * Returns url of the persons pic from wikimedia commons
	 * 
	 * @return
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	private void getPicUrl() {
		String resource = this.getBbaw_id(); // "http://edition-humboldt.de/register/personen/detail.xql?id="+
												// this.getBbaw_id();
		if (picUrls.containsKey(resource)) {
			pictureUrl = picUrls.get(resource);
		
		} else {
			URL extractWikiCommonsImages = extractWikiCommonsImages(resource);
			if (extractWikiCommonsImages != null)
				pictureUrl = extractWikiCommonsImages.toExternalForm();
			picUrls.put(resource, pictureUrl);
			Person.serializeMap();
		}

	}

	private URL extractWikiCommonsImages(String url) {
		try {
			String wikiURL = Jsoup.connect(url).get().select("div.portrait a[href*=commons.wikimedia.org]").first()
					.attr("href");
			return new URL(Jsoup.connect(wikiURL).get().select("div#file a").first().attr("href"));
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public String toString() {
		String toReturn = this.getName() + "|" + this.getBbaw_id() + "|" + pictureUrl;
		return toReturn;
	}

	public static void serializeMap() {
		try (FileOutputStream fileOut = new FileOutputStream(picUrlsFileName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(picUrls);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return description;
	}
	
	public String readXMLFile(String filePath) {
        try {
            // Dateiinhalt als String lesen
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            return new String(encoded, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public  String extractPersonInfo(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(xmlContent.getBytes("UTF-8")));
            
            StringBuffer toReturn = new StringBuffer();
            try {
	            // Extrahiere Vorname und Nachname
	            NodeList forenameList = document.getElementsByTagNameNS("http://www.tei-c.org/ns/1.0", "forename");
	            NodeList surnameList = document.getElementsByTagNameNS("http://www.tei-c.org/ns/1.0", "surname");
	            
	            String forename = forenameList.item(0).getTextContent();
	            toReturn.append(forename);
	            String surname = surnameList.item(0).getTextContent();
	            toReturn.append(" " + surname);
            }
            catch(Exception e) {
            	
            }
           
            // Extrahiere Geburts- und Todesjahr
            try {
	            NodeList birthList = document.getElementsByTagNameNS("http://www.tei-c.org/ns/1.0", "birth");
	            NodeList deathList = document.getElementsByTagNameNS("http://www.tei-c.org/ns/1.0", "death");
	            
	            String birthYear = birthList.item(0).getTextContent();
	            String deathYear = deathList.item(0).getTextContent();
	            
	            toReturn.append(" (" + birthYear + "-" + deathYear + ")");
            }
            catch(Exception e) {
            	
            }
            
            try {
            	// Extrahiere die Notiz (biographische Informationen)
                NodeList noteList = document.getElementsByTagNameNS("http://www.tei-c.org/ns/1.0", "note");
                String note = noteList.item(0).getTextContent().trim().replaceAll("\\s{2,}", " ");
                toReturn.append(", ");
                toReturn.append(note);
                toReturn.append(".");
			} catch (Exception e) {
				
			}
           
            return toReturn.toString();
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("No item list found in " + xmlContent);
            return null;
        }
	}
}
