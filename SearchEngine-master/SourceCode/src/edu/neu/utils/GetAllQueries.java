package edu.neu.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;
import edu.neu.queryExpansion.PseudoRelevanceRocchio;
import edu.neu.queryExpansion.QueryExpander;
import edu.neu.textProcessor.FileProcessor;

public class GetAllQueries {
	
	private static BufferedWriter bw = null;
	private static FileWriter fw;
	private static BufferedReader br = null;
	
	
	public static Map<String,String> createQueryList() {
		
		Map<String,String> map = new LinkedHashMap<String, String>();
	    File queryFile = new File(
			PropertyFileManager.getProperty("QUERY_FILE_LOCATION"));
		Document doc;
		try {
			doc = Jsoup.parse(queryFile, "UTF-8");
			Elements queryList = doc.select("DOC");
			
			
			PseudoRelevanceRocchio rm = new PseudoRelevanceRocchio();
			rm.retrieveIndex();
			rm.calcDocVector();	
			
			for (Element e : queryList) {
				
				String qId = e.select("DOCNO").text();
				String t = e.text();
				String processedQuery = FileProcessor.processString(t.substring(t.indexOf(" ")+1));
				
				 String stoppingFlag= PropertyFileManager.getProperty("STOPPING");
				
				 if ("Y".equals(stoppingFlag)) {
					 //Call StopList function
					 processedQuery = StopWordsRemoval.removeStopWords(processedQuery);
				 }
				 
				 // Query Expansion to be done here
				 String queryExpansionFlag= PropertyFileManager.getProperty("QUERY_EXPANSION");
					
				 if ("Y".equals(queryExpansionFlag)) {
					
					 //KL
					 //processedQuery = QueryExpander.expandQuery(processedQuery);
					 
					 
					 //ROCCHIO
											
						List<DocumentDetails> topResult = rm.getTopResult(processedQuery, qId);
						
						Set<String> vocabList = rm.createVocab(qId,topResult);
						ArrayList <String> queryTermList = new ArrayList<String>();
						
						String tempQueryList [] = processedQuery.split("\\s+");
						for(String term : tempQueryList)
						{
							queryTermList.add(term);
						}
						
						processedQuery = rm.calc(queryTermList , vocabList ,qId);
				 }
				 
				
				map.put(qId, processedQuery);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;
	}
	

}
