package edu.neu.textProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.URLRecord;

public class StringProcessor {
	
	
	public static void main (String ...s) {
		new PropertyFileManager();
		
		convertToDocList("(Asim,1),(taran,2)");
	}
	
	public static String removeAllSpecialCharacters(String str) {
		
		
	        Pattern p = Pattern.compile("[^a-zA-Z0-9-]");
	        Matcher match= p.matcher(str);
	        while(match.find())
	        {
	            String s= match.group();
	            str=str.replaceAll("\\"+s, "");
	        }
	        
		return str;
	}
	
	public static String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http|https):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr;
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
