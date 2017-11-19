package edu.neu.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.neu.pojos.DocumentDetails;

public class TEst {

	public static void main(String args[]) throws IOException {

		new PropertyFileManager();

		// Connection con =
		// Jsoup.connect("https://en.wikipedia.org/wiki/Sustainable_energy").userAgent(USER_AGENT);
		// Document doc = con.get();
		File input = new File(PropertyFileManager.getProperty("HTML_FILE_PATH")
				+ "1.txt");

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

		
		//String pattern = "\\[|\\]|\\(|\\)|~|!|@|#|\\$|%|\\^|&|\\*|_|\\||\\+|\\=|\\{|\\}|\"|\'|;|:|\\<|\\>|/|\\?|\\b,";
		/*finalString = finalString.replaceAll(pattern, " ");
			//	.replace(" - ", " ").replace(" , ", " ")
		finalString = 	finalString.replace(" . ", " ");
		finalString = 	finalString.replace(" , ", " ");
		finalString = 	finalString.replace(", ", " ");
		finalString = 	finalString.replace("?", " ");*/
		
		StringBuilder s = new StringBuilder("");
		//pattern = "(\\b\\w+\\b)|(\\d+\\.\\d+)";
		//pattern ="(\\d+\\.\\d+)";
		//pattern ="(\\d+,\\d+)";
		//pattern ="([a-zA-Z]+\\-[a-zA-Z]+)";(\\.|\\,)?
		String pattern= "(\\b(\\d*)?[a-zA-Z]+(\\d+)?(\\-[a-zA-Z]+|\\-\\d+)?(\\d*|[a-zA-Z]*)?\\b|\\d+((\\.|\\,)?\\d+)?|([a-zA-Z]+\\-[a-zA-Z]+))";
		Matcher m = Pattern.compile(pattern).matcher(finalString);
		while (m.find()) {


		}
		
		System.out.println(s.toString());
		//System.out.println(finalString.replace("?", ""));

		// System.out.println(finalString.replaceAll(pattern, " "));

	}
	
	public static  List<DocumentDetails> convertToDocList(String text) {
		List<DocumentDetails> docList = new ArrayList<DocumentDetails>();
		Matcher m = Pattern.compile("\\((.*?)\\)").matcher(text);
		while(m.find()) {
		    //System.out.println(m.group(1));
		    DocumentDetails doc = new DocumentDetails();
		    String[] str = m.group(1).split(",");
		    doc.setDocID(str[0].trim());
		    doc.setTermFreq(Integer.parseInt(str[1].trim()));
		    docList.add(doc);
		}
		return docList;
		
	}

}
