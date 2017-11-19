package edu.neu.snippetGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.neu.download.PropertyFileManager;

public class SnippetGeneration {

	private static final Map<String , Integer> stopList = new HashMap<String , Integer>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		Map<String,String> queryMap = createQueryList();
		
		for(String id : queryMap.keySet())
		{
			String query = queryMap.get(id);
			SnippetGeneration sg = new SnippetGeneration();
			String doc = sg.readDoc();
			String docText = doc;
			String title = doc.substring(0 , doc.indexOf('\n')); 
			doc = doc.substring(doc.indexOf('\n'));
			
			String processedtitle = processString(title);
			String processedDoc = processString(doc);
			
			
			addStopList();
			ArrayList<String> snippet = sg.createSnippet(query,  processedtitle , processedDoc , doc);
			
			snippet.addAll(Arrays.asList(query.split("\\s+")));
			String temp [] = docText.split("\n");
			
			StringBuffer s = new StringBuffer("");
			for(String line: temp)
			{
				String temp1 [] = line.split("\\s+");
				for(String a : temp1)
				{
					
					if(snippet.contains(a))
					{
						s.append(a.toUpperCase() + " ");
					}
					else
					{
						s.append(a + " ");
					}
				}
				
				s.append("\n");

				
			}
					
			
			
			StringBuffer finalSnip = new StringBuffer("");
			String finalSnippet = s.toString();
			
			String snipps [] = finalSnippet.split("\n");
		
			for(String a : snipps)
			{
				int flag = 0;
				String x [] = a.split("\\s+");
				for(String m : x)
				{
					if(snippet.contains(m.toLowerCase()))
					{
						flag = 1;
					}
				}
				
				if(flag == 1)
				{
					finalSnip.append(a + "\n");
				}
			}
			
			System.out.println(finalSnip);
			
		}
		

	}

	public ArrayList<String> createSnippet(String query ,  String title , String processedDoc , String doc)
	{
		StringBuffer snippet = new StringBuffer("");
		
		ArrayList<String> significantWords = new ArrayList<String>();
		snippet.append(title);
		
		String tempQuery [] = query.split("\\s+");
		List<String> queryTerms = Arrays.asList(tempQuery);
		System.out.println(queryTerms);
		String temp [] = doc.split("\n");
		int noOfsentences = temp.length -2;
		
		String docTerms[] = processedDoc.split("\\s+");
		
		List<String> terms = Arrays.asList(docTerms);
	
		for(String term : docTerms)
		{
			if(!stopList.containsKey(term))
			{
				int freq = Collections.frequency(terms, term);
				
				double significantFactor = 0.0;
				if(noOfsentences < 25)
				{
					significantFactor = 3 - (0.1 * (25 - noOfsentences));
					
				}
				else if(noOfsentences >= 25 && noOfsentences <= 40)
				{
					significantFactor = 3;
				}
				else
				{
					significantFactor = 3 + (0.1 * (noOfsentences - 40));
				}
				
				if(freq >= significantFactor || queryTerms.contains(term))
				{
					if(!significantWords.contains(term))
					{
					significantWords.add(term);
					}
					
				}
			}
		}
		
		
		System.out.println(significantWords);
		return significantWords;
		
	}
	
	private static void addStopList()
	{
		
		File path = new File(
				PropertyFileManager.getProperty("STOP_WORDS_FILE"));
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(path));
			 while((line = br.readLine())!= null )
			 {
				 
				 stopList.put(line, 1);
				 
			 }
			 
			 br.close();
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String readDoc()
	{
		String path = "C:/Users/Himanshu/Desktop/doc.txt";
	
	
		String content ="";
		try {
			File corpus = new File(path);
			FileInputStream inp = new FileInputStream(corpus);
			byte[] bcorpus = new byte[(int)corpus.length()];
			inp.read(bcorpus);
			content = new String(bcorpus, "UTF-8");
			
			inp.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return content;
	}
	
public static Map<String,String> createQueryList() {
		
		Map<String,String> map = new LinkedHashMap<String, String>();
	    File queryFile = new File("C:/Users/Himanshu/Desktop/IR Project/cacm-query.query");
		org.jsoup.nodes.Document doc;
		try {
			doc = Jsoup.parse(queryFile, "UTF-8");
			Elements queryList = doc.select("DOC");
			
			
			for (Element e : queryList) {
				
				String qId = e.select("DOCNO").text();
				String t = e.text();
				String processedQuery = processString(t.substring(t.indexOf(" ")+1));
				
				map.put(qId, processedQuery);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;
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
