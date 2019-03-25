package de.uni_koeln.spinfo.avh.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class TextAddSpace {
	
	@Test
	public void addSpace() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File("output/Chrono1810Full_3.tsv")));
		PrintWriter out = new PrintWriter(new FileWriter(new File("output/Chrono1903.tsv")));
		String line = in.readLine();
		while(line!=null) {
			line = line.replaceAll("#AvH250", "#AvH250 ");
			System.out.println(line);
			out.println(line);
			line = in.readLine();
		}
		out.flush();
		out.close();
		in.close();
	}

}
