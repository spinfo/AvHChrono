package de.uni_koeln.spinfo.avh.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uni_koeln.spinfo.avh.data.DiaryEntry;

public class XMLtoCSVConverter {
	
	private File inputfolder;
	private File destinationFile;
	
	
	/**
	 * Creates a new XMLtoCSVConverter for the specified input folder and destination file.
	 * @param inputfolder
	 * @param destinationFile
	 */
	public XMLtoCSVConverter(String inputfolder, String destinationFile){
		this.inputfolder = new File(inputfolder);
		this.destinationFile = new File(destinationFile);
	}
	
	
	/**
	 * Processes the input file, writes results to the destination file.
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public List<DiaryEntry> process() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		List<DiaryEntry> entries = new ArrayList<DiaryEntry>();
		
		File[] list = inputfolder.listFiles();
		for (File file : list) {
			 entries.addAll(readFile(file));
		}
		PrintWriter out = new PrintWriter(new FileWriter(destinationFile));
		for (DiaryEntry diaryEntry : entries) {
			out.println(diaryEntry);
		}
		
		out.flush();
		out.close();
		return entries;
	}
	
	/**
	 * Reads the specified file and generates a DiaryEntry object for each diary entry within this file.
	 * @param file File with TEI-diary entries
	 * @return List of DiaryEntry|s found within the File
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public List<DiaryEntry> readFile(File file) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		List<DiaryEntry> toReturn = new ArrayList<DiaryEntry>();
		
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder;
         Document doc = null;
         XPathExpression expr = null;
         builder = factory.newDocumentBuilder();
         //doc = builder.parse("testdata/people.xml");
         doc = builder.parse(file);

         // create an XPathFactory
         XPathFactory xFactory = XPathFactory.newInstance();

         // create an XPath object
         XPath xpath = xFactory.newXPath();

         // compile the XPath expression
         //expr = xpath.compile("//TEI/text/body/p/placeName/text()");
         //expr = xpath.compile("//TEI/teiHeader/fileDesc/titleStmt/title/date/@when");
         //expr = xpath.compile("//*[local-name()='TEI']/@*[name()='xml:id']");
         expr = xpath.compile("//*[local-name()='TEI']");
         // run the query and get a nodeset
         Object result = expr.evaluate(doc, XPathConstants.NODESET);

         // cast the result to a DOM NodeList
         NodeList nodes = (NodeList) result;
         for (int i=0; i<nodes.getLength();i++){
         	
        	String id = nodes.item(i).getAttributes().item(0).getNodeValue();
          	DiaryEntry newDE = new DiaryEntry(id);
        	 
             //System.out.println(nodes.item(i).getNodeValue());
         	String date = nodes.item(i).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getAttributes().item(0).getTextContent();
         	
         	//String text = nodes.item(i).getChildNodes().item(2).getNodeValue();
         	String text = nodes.item(i).getChildNodes().item(3).getTextContent();
         	NodeList childNodes = nodes.item(i).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getChildNodes();
         	for(int j=0; j <childNodes.getLength();j++){
         		//System.out.println(childNodes.item(j).getNodeName());
         		if(childNodes.item(j).getNodeName().equals("placeName")){         		
         			String location = childNodes.item(j).getTextContent();
         			System.out.println("Place: " + location);
         			newDE.addLocation(location);
         		}
         		if(childNodes.item(j).getNodeName().equals("persName")){
         			String person = childNodes.item(j).getTextContent();
         			System.out.println("Person: " + person);
         			newDE.addPerson(person);
         		}
         	}
         	
         	
         	newDE.setDate(date);
         	newDE.setText(text.trim());
         	
         	//System.out.println(newDE);
         	
         	toReturn.add(newDE);
         }
		
		return toReturn;
	}

}
