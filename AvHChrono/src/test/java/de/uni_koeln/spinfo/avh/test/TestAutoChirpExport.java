package de.uni_koeln.spinfo.avh.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestAutoChirpExport {

	
	
	//@Test
	public void testPersonMap() {
		String picUrlsFileName = "output/picUrls.ser";
		Map<String, String> picUrls;
		try (FileInputStream fileIn = new FileInputStream(picUrlsFileName);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			picUrls = (Map<String, String>) in.readObject();
			System.out.println(picUrls.size());
			Set<String> keySet = picUrls.keySet();
			int found = 0;
			for (String string : keySet) {
				String value = picUrls.get(string);
				if(value!=null) {
					System.out.println(value);
					found++;
				}
			}
			System.out.println(found);
			

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		}
	}
	
	@Test
	public void testReplacement() throws UnsupportedEncodingException {
		String decodedString = decodeUnicodeEscapes("H. reist an diesem oder am n\\u00E4chsten Tage von Wien \\u00FCber Li");
		 

		System.out.println(decodedString);

	}
	
	private String decodeUnicodeEscapes(String input) {
        Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
        Matcher matcher = pattern.matcher(input);
        StringBuffer decodedString = new StringBuffer(input.length());

        while (matcher.find()) {
            String unicodeChar = String.valueOf((char) Integer.parseInt(matcher.group(1), 16));
            matcher.appendReplacement(decodedString, unicodeChar);
        }
        matcher.appendTail(decodedString);
        return decodedString.toString();
    }
		

}
