package de.uni_koeln.spinfo.avh.ie;

import java.util.Comparator;

public class StringLengthComparator<T> implements Comparator<String> {

	public int compare(String o1, String o2) {
		int result = o2.length()-o1.length();
		return result;
	}

}
