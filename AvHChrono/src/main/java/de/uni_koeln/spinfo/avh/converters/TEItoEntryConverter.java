package de.uni_koeln.spinfo.avh.converters;

import java.io.File;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;
import de.uni_koeln.spinfo.avh.data.Person;

/**
 * Class to parse Edition Humboldt Digital data from TEI format to DiaryEntries
 */
public class TEItoEntryConverter {

    private DocumentBuilder builder;
    
    private XPath xpath;
    
    private XPathExpression dateExpr, paragraphExpr, personExpr;
    
    private String personFolderName;
    
    public TEItoEntryConverter(String personFolderName) throws ParserConfigurationException, XPathExpressionException {
    	this.personFolderName = personFolderName;
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // Important to handle namespaces
        builder = factory.newDocumentBuilder();
        
        XPathFactory xPathfactory = XPathFactory.newInstance();
        xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (prefix == null) throw new NullPointerException("Null prefix");
                else if ("tei".equals(prefix)) return "http://www.tei-c.org/ns/1.0";
                else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
                return XMLConstants.NULL_NS_URI;
            }
            @Override
            public String getPrefix(String uri) {
                throw new UnsupportedOperationException();
            }
            @Override
            public Iterator getPrefixes(String uri) {
                throw new UnsupportedOperationException();
            }
        });
        dateExpr = xpath.compile("//tei:teiHeader/tei:fileDesc/tei:titleStmt/tei:title/tei:date/@when");
        paragraphExpr = xpath.compile("//tei:text/tei:body/tei:p");
        personExpr = xpath.compile("//tei:text/tei:body/tei:p/tei:persName");

	}



	public DiaryEntry process(File xmlFile) {
    	
    	DiaryEntry toReturn = new DiaryEntry(xmlFile.getName().replaceAll(".*/(.*)\\.xml", "$1"));
    	System.out.println(toReturn.getId());
        
        try {
            // Parse the XML file
            
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
           

            // Extract the date
            String date = (String) dateExpr.evaluate(document, XPathConstants.STRING);
            toReturn.setDate(date);

            // Extract the paragraph content
            String paragraph = (String) paragraphExpr.evaluate(document, XPathConstants.STRING);
            paragraph = paragraph.replaceAll("\\s+", " ").trim();
            toReturn.setText(paragraph);
           
            NodeList personNodes = (NodeList) personExpr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < personNodes.getLength(); i++) {
                Element personElement = (Element) personNodes.item(i);
                String name = personElement.getTextContent().trim();
                String ref = personElement.getAttribute("ref").split(" ")[0];
                Person person = new Person(name, ref, personFolderName);
                toReturn.addPerson(person);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return toReturn;
   
    }
    public static String extractID(String input) {
        // Find the position of the last '/' and the last '.'
        int lastSlashIndex = input.lastIndexOf('/');
        int lastDotIndex = input.lastIndexOf('.');

        // Extract the substring between the last '/' and the last '.'
        if (lastSlashIndex != -1 && lastDotIndex != -1 && lastSlashIndex < lastDotIndex) {
            return input.substring(lastSlashIndex + 1, lastDotIndex);
        } else {
            // If the input format is unexpected, return an empty string or handle the error as needed
            return "";
        }
    }
}
