package edu.neu.textProcessor;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FileProcessor {

	public static String processFile(File input) throws IOException {

		Document doc = Jsoup.parse(input, "UTF-8", "https://en.wikipedia.org");

		String finalString = "";
		finalString += (doc.select("h1.firstHeading").text() + " ");

		Element bodyContent = doc.getElementById("bodyContent");
		for (Element curElement : bodyContent.select("p,h1,h2,h3,h4,h5,h6")) {
			finalString += (curElement.text() + " ");
		}

		finalString = finalString.replaceAll("\n", " ");
		finalString = finalString.replaceAll("\t", " ");
		finalString = finalString.toLowerCase();

		StringBuilder s = new StringBuilder("");
		String pattern = "(\\b(\\d*)?[a-zA-Z]+(\\d+)?(\\-[a-zA-Z]+|\\-\\d+)?(\\d*|[a-zA-Z]*)?\\b|\\d+((\\.|\\,)?\\d+)?|([a-zA-Z]+\\-[a-zA-Z]+))";
		Matcher m = Pattern.compile(pattern).matcher(finalString);
		while (m.find()) {
			s.append(m.group(1) + " ");
		}

		return s.toString().trim();

	}
	
	
	public static String processCACMFile(File input) throws IOException {

		Document doc = Jsoup.parse(input, "UTF-8");		
		String finalString = doc.select("pre").text();
		finalString = processString(finalString);
		return finalString;

	}
	
	public static String processString(String str) {
		
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\t", " ");
		str = str.toLowerCase();

		StringBuilder s = new StringBuilder("");
		String pattern = "(\\b(\\d*)?[a-zA-Z]+(\\d+)?(\\-[a-zA-Z]+|\\-\\d+)?(\\d*|[a-zA-Z]*)?\\b|\\d+((\\.|\\,)?\\d+)?|([a-zA-Z]+\\-[a-zA-Z]+))";
		Matcher m = Pattern.compile(pattern).matcher(str);
		while (m.find()) {
			s.append(m.group(1) + " ");
		}

		return s.toString().trim();
	}

}
