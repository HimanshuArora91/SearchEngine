package edu.neu.queryExpansion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.neu.assignment4.Task2_Retrieve;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;
import edu.neu.utils.StopWordsRemoval;

public class PseudoRelevanceRocchio {

	
	Map<String, HashMap<String, Integer>> docMap = new HashMap<String, HashMap<String,Integer>>();
	
	Map<String, HashMap<String, Integer>> docMapIndex = new HashMap<String, HashMap<String,Integer>>();
	
	Map<String,  Double> docMapFreq = new HashMap<String,Double>();
	
	Map<String, HashMap <String , Double>> TfIdf = new HashMap<String, HashMap <String , Double>>();
	
  //  Map<String , ArrayList<String>> TopSet = new HashMap<String , ArrayList<String>>();
    
    public static Map<String , List<String>> relevantDocs = new HashMap<String , List<String>>();
    
	
	public String calc(ArrayList <String> queryTermList , Set<String> vocabList , String qID)
	{
		
		Map<String , Double> termScores = new HashMap<String , Double>();
		
		double alpha = 8.0;
		double beta = 16.0;
		double gamma = 4.0;
		
		for(String term : vocabList)
		{
			
			double termCount = Collections.frequency(queryTermList, term);
			double termWeight = termCount/queryTermList.size();
			double relevantSum = 0.0;
			
			double relCount = relevantDocs.get(qID).size();
			for(String relDocs : relevantDocs.get(qID))
			{
				
				if(TfIdf.get(relDocs).containsKey(term))
				{
					relevantSum += TfIdf.get(relDocs).get(term);
				}
				
			}
			
			Set<String> nonRelevantDocs  = new HashSet<String>(docMapIndex.keySet());
			nonRelevantDocs.removeAll(relevantDocs.get(qID));
			
			double nonRelCount = 3204.0 - relevantDocs.keySet().size()*1d;
			double nonRelevantSum = 0.0;
			
			
			for(String nonRelDocs : nonRelevantDocs)
			{
				if(TfIdf.get(nonRelDocs).containsKey(term))
				{
				nonRelevantSum += TfIdf.get(nonRelDocs).get(term);
				}
			}
			
			double finaltermScore = (alpha * termCount) + ((beta * (1.0/relCount)) * relevantSum) - ((gamma * (1.0/nonRelCount)) * nonRelevantSum );
			termScores.put(term, finaltermScore);
			
		}
		//System.out.println(termScores);
		Comparator<String> comparator = new ValueComparator4(termScores);
	    TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
		result.putAll(termScores);
		//zSystem.out.println(result);
		
		Set<String> expandedNewItems = StopWordsRemoval.removeStopWords(result.keySet());
		expandedNewItems.removeAll(new ArrayList<String>(queryTermList));
		
		//System.out.println(expandedNewItems);
	
		queryTermList.addAll(new ArrayList<String>(expandedNewItems).subList(0, 20));
		
		StringBuffer b = new StringBuffer("");
		
		for (String t : queryTermList) {
			b.append(t + " ");
		}
		
		return b.toString().trim();
			
	}
	public Set<String> createVocab(String qId,List<DocumentDetails> topResults)
	{
		//relevantDocs.clear();
		Set<String> vocabList = new HashSet<String>();
		
		List<String> queryDocs = new ArrayList<String>();
		
		for (DocumentDetails d : topResults) {
			
			queryDocs.add(d.getDocID());
		}
		
		
		
		relevantDocs.put(qId, queryDocs);
		
		for(String doc : queryDocs)
			
		{
			//String doc = queryDocs.get(i);
			//System.out.println(doc);
			
			vocabList.addAll(docMapIndex.get(doc).keySet());
			
		}
		
		//System.out.println(vocabList.size());
		
		return vocabList;
		
	}
	
	public List<DocumentDetails> getTopResult(String originalQuery, String qid)
	{
		
		
		List<DocumentDetails> firstResultSet = Task2_Retrieve.getQueryResults(originalQuery, qid,0);		
		List<DocumentDetails> topDocs = new ArrayList<DocumentDetails>(firstResultSet.subList(0, 50));
		
	
		return topDocs;
	
		
	}
	
	
	// dik page 242
	public void calcDocVector()
	{
		
			for(String d: docMapIndex.keySet())
			{
				
					Set <String> termDoc = docMapIndex.get(d).keySet();
					for(String docterm : termDoc)
					{
						int nk = docMap.get(docterm).keySet().size();
						HashMap <String , Double> al = new HashMap<String , Double>();
						
						int termFreq = docMap.get(docterm).get(d);
						double tf = Math.log(termFreq) + 1;
						
						double idf = Math.log(3204.0/nk);
						
						double finalValue = (tf * idf) /Math.sqrt(docMapFreq.get(d));
						
						al.put(docterm, finalValue);
						if(TfIdf.containsKey(d))
						{
							TfIdf.get(d).put(docterm, finalValue);
						}
						else
						{
							TfIdf.put(d, al);
						}
						
						
					}
					
			}
		
	}
	
	public void retrieveIndex()
	{
		//String path = "C:/Users/Himanshu/Desktop/IR Project/Index_One_Gram.txt";
		
		String file= PropertyFileManager.getProperty("INDEX_FILE_LOCATION");
		/*br = new BufferedReader(
				new FileReader(file + "Index_One_Gram.txt"));*/
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(file + "Index_One_Gram.txt"));
			 while((line = br.readLine())!= null )
			 {
				 String temp []= line.split("->");
				 String temp1[] = temp[1].split(",");
				
				 HashMap<String , Integer> h = new HashMap<String , Integer>();
				 
				 HashMap<String , Integer> h2 = new HashMap<String , Integer>();
				 
				 
				 String key = temp1[0].substring(1);
				 int val = Integer.parseInt(temp1[1].substring(0, temp1[1].indexOf(')')));
			
				 h.put(key,val);
				 
				 String indKey = temp[0];
				 h2.put(indKey, val);

				 if(docMapIndex.containsKey(key))
				 {
					 docMapIndex.get(key).put(indKey, val);
				 }
				 else 
				 {
					 docMapIndex.put(key, h2);
				 }
				 docMap.put(temp[0],h);
				 
				 int nk = temp1.length/2;
				 double idf = Math.log(3204.0/nk);
				 
				 if(docMapFreq.containsKey(key))
				 {
					 double fre = docMapFreq.get(key);
					 double logValue = Math.log(val) + 1;
					 double finalValue = fre+Math.pow(logValue*idf, 2);
					 docMapFreq.put(key, finalValue);
				 }
				 else
				 {
					 double logVal = Math.log(val) + 1;
					 docMapFreq.put(key,  Math.pow(logVal * idf, 2));
				 }
				 
				 for(int i = 2 ; i < temp1.length ;i+=2)
				 {
					 HashMap<String , Integer> h1 = new HashMap<String , Integer>();
					 HashMap<String , Integer> h3 = new HashMap<String , Integer>();
					 
					 String key1 = temp1[i].substring(1);
					 int val1 = Integer.parseInt(temp1[i+1].substring(0, temp1[i+1].indexOf(')')));
					 h1.put(key1,val1);
					 docMap.get(temp[0]).put(key1, val1);
					 
					 h3.put(indKey, val1);
					 
					 if(docMapIndex.containsKey(key1))
					 {
						 docMapIndex.get(key1).put(indKey, val1);
					 }
					 else
					 {
						 docMapIndex.put(key1, h3);
					 }


					 if(docMapFreq.containsKey(key1))
					 {
						 double fre1 = docMapFreq.get(key1);
						 double logValue1 = Math.log(val1) + 1;
						 double finalVal = fre1+Math.pow(logValue1*idf, 2);
						 docMapFreq.put(key1, finalVal);
					 }
					 else
					 {
						 double logVal1 = Math.log(val1) + 1;
						 docMapFreq.put(key1,  Math.pow(logVal1 * idf, 2));
					 } 
				 }
			 }
			 
			 //System.out.println(docMapIndex); // Non-inverted index //doc -> (term1 , fre1) ..
			 //System.out.println(docMap); //Inverted index
			 //System.out.println(docMapFreq); //square of denominator
			 
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
	
	

    
}


class ValueComparator4 implements Comparator<String>{
	 
	HashMap<String, Double> map = new HashMap<String, Double>();
 
	public ValueComparator4(Map<String, Double> map){
		this.map.putAll(map);
	}
	
	public int compare(String s1, String s2) {
		if(map.get(s1) > map.get(s2)){
			return -1;
		}
		else{
			return 1;
		}	
	}
}