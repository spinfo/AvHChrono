package de.uni_koeln.spinfo.avh.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

/**
 * Converts TEI-xml files of diary entries (BBAW-format) to DiaryEntry objects and to a tsv export format.
 * @author jhermes
 *
 */
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
	 * The export function: Writes the specified DiaryEntries to a tsv file at this converters destination file.
	 * @param diaryEntries
	 * @throws IOException
	 */
	public void writeCSVFile(List<DiaryEntry> diaryEntries) throws IOException{
		
		PrintWriter out = new PrintWriter(new FileWriter(destinationFile));
		for (DiaryEntry diaryEntry : diaryEntries) {
			out.println(diaryEntry);
		}
		
		out.flush();
		out.close();
	}
	
	/**
	 * The import function: Reads a list of DiaryEntries from this converters destFile.
	 * @return
	 * @throws IOException
	 */
	public List<DiaryEntry> importDiaryEntries() throws IOException{
		List<DiaryEntry> toReturn = new ArrayList<DiaryEntry>();
		BufferedReader in = new BufferedReader(new FileReader(destinationFile));
		String line = in.readLine(); //first line is headline
		line = in.readLine();
		while(line!=null){
			
			String[] parts = line.split("\t");
			DiaryEntry de = new DiaryEntry(parts[0]);
			de.setDate(parts[1]);
			System.out.println(parts[1]);
			de.setText(parts[2]);
			de.setLocations(getSetFromString(parts[3]));
			de.setPersons(getSetFromString(parts[4]));
			//de.setUnspecified(getSetFromString(parts[5]));
			toReturn.add(de);
			line = in.readLine();
		}		
		return toReturn;
	}
	
	private Set<String> getSetFromString(String str){
		str = str.substring(1, str.length()-1);
		String[] elements = str.split(",");
		
		Set<String> set = new TreeSet<String>();
		
		for (String element : elements) {
			set.add(element.trim());
		}
		
		return set;
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
			System.out.println(file.getName());
			 entries.addAll(readFile(file));
		}
		
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
        	
          	System.out.println(id);
             //System.out.println(nodes.item(i).getNodeValue());
         	String date = nodes.item(i).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1).getAttributes().item(0).getTextContent();
         	System.out.println(date);
         	//String text = nodes.item(i).getChildNodes().item(2).getNodeValue();
         	String text = nodes.item(i).getChildNodes().item(3).getTextContent();
         	text = text.replaceAll("[\\s]+", " ");
//         	System.out.print(id + "\t");
//         	System.out.print(text.length() + "\t");
//         	text = text.replaceAll("-LRB- ", "");
//         	text = text.replaceAll("-LSB- ", "");
//         	text = text.replaceAll("-RSB- ", "");
//         	text = text.replaceAll("-RRB- ", "");
//         	System.out.println(text.length());
         	NodeList childNodes = nodes.item(i).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getChildNodes();
         	for(int j=0; j <childNodes.getLength();j++){
         		//System.out.println(childNodes.item(j).getNodeName());
         		if(childNodes.item(j).getNodeName().equals("placeName")){         		
         			String location = childNodes.item(j).getTextContent();
         			location = location.replaceAll("[\\s]+", " ");
         			System.out.println("Place: " + location);
         			newDE.addLocation(location);
         		}
         		if(childNodes.item(j).getNodeName().equals("persName")){
         			String person = childNodes.item(j).getTextContent();
         			person = person.replaceAll("[\\s]+", " ");
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
