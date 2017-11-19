package edu.neu.assignment3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TEst {

	
	
	public static void main (String ...s) {
		
		
		StringBuffer ss = new StringBuffer("");
		String pattern = "(\\d+((\\.|\\,)?\\d+)?)";
		Matcher m = Pattern.compile(pattern).matcher("1.2222 123 123,123 123.123");
		
		int count =0;
		while (m.find()) {
			System.out.println(count++);
		}
		
		System.out.println(ss);
	}
}
