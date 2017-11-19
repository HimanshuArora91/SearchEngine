package edu.neu.queryExpansion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.assignment4.Task2_Retrieve;
import edu.neu.comparator.DocumentScoreComparator;
import edu.neu.download.DownloadPage;
import edu.neu.indexer.NGramsGenerator;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;
import edu.neu.utils.Utils;

public class QueryExpander {/*
	private static final Map<String, DocumentDetails> docMap = DownloadPage
			.getDocIDsMap();
	
	public static String expandQuery(String originalQuery) {
		String updatedQuery = originalQuery;
		Map<String, IndexEntry> indexMap = new HashMap<String, IndexEntry>();
		
		List<String> queryTerms = Arrays.asList(originalQuery.split("\\s+"));
		
		List<DocumentDetails> firstResultSet = Task2_Retrieve.getQueryResults(originalQuery);		
		List<DocumentDetails> topDocs = new ArrayList<DocumentDetails>(firstResultSet.subList(0, 50));
		
		
		
		
		for (DocumentDetails doc : DownloadPage.getDocIdFile()) {
			DownloadPage.getCurrentdocText(doc);
			NGramsGenerator.createNGrams(doc, 1, indexMap);
		}
		List<IndexEntry> list = new ArrayList<IndexEntry>(indexMap.values());
		Utils.calculateTotalFrequency(list);
		
		Double corpusSize = getTotalTokenCount(DownloadPage.getDocIdFile());
		for (DocumentDetails doc : topDocs) {
			
			calculateEachDocScore (doc,indexMap, queryTerms,corpusSize);
		}
		
		
		List<DocumentDetails> wordList = new ArrayList<DocumentDetails>();
		for (String eachWordInIndex : indexMap.keySet()) {
			Double wordScore = 0.0;
			DocumentDetails word = new DocumentDetails();
			for (DocumentDetails doc : topDocs) {
				
				Double probOfWordInDoc = getProbInDocument(eachWordInIndex,doc,indexMap);
				
				wordScore += probOfWordInDoc*doc.getScore(); 
			}
			
			
			word.setDocID(eachWordInIndex);
			word.setScore(wordScore);
			wordList.add(word);
		}
		
		Collections.sort(wordList, new DocumentScoreComparator());
		
		
		for (DocumentDetails d : wordList) {
			System.out.println(d.getDocID() + " " + d.getScore());
		}
	
		//Map<String, IndexEntry> indexMapTopDocs = new HashMap<String, IndexEntry>();
		for (DocumentDetails doc : firstResultSet) {
			
			NGramsGenerator.createNGrams(doc, 1, indexMap);
		}
		
		return updatedQuery;
	}
	
	private static Double getProbInDocument(String word, DocumentDetails doc,Map<String,IndexEntry> indexMap) {
		double prob = 0.0;
		int i = indexMap.get(word).getDocs().indexOf(doc);
		Double fd = getTermCountInDocument(word, doc, indexMap);
		
		prob = getTermCountInDocument(word, doc, indexMap)/docMap.get(doc.getDocID()).getTokenCount();
		return prob;
	}
	
	
	
	
	private static void calculateEachDocScore(DocumentDetails doc,Map<String, IndexEntry> indexMap, List<String> queryTerms,Double corpusSize) {
		
		//Double alpha = (1000.0/(docMap.get(doc.getDocID()).getTokenCount() + 1000.0));
		
		Double meu = 1000.0;
		Double score =1.0;
		
		for (String queryTerm : queryTerms) {
			
			
			
			Double D = (double)docMap.get(doc.getDocID()).getTokenCount();
			
			Double alpha = meu/(D+meu);
			Double fqi = getTermCountInDocument(queryTerm, doc, indexMap);
			Double cqi = indexMap.get(queryTerm)!=null?(double)indexMap.get(queryTerm).getTotFrequency():0.0;
			
			
			//score *= (fqi+meu*(cqi/corpusSize))/(D+meu);
			
			score*=  ((1-alpha)*(fqi/D) + alpha*cqi/corpusSize);
		}
		
		doc.setScore(score);
		
	}
	
	private static Double getTermCountInDocument(String queryTerm, DocumentDetails doc,Map<String,IndexEntry> indexMap) {
		
		Double count = 0.0;
		IndexEntry e = indexMap.get(queryTerm);
		if (e != null) {
			
			for (DocumentDetails d : e.getDocs()) {
				
				if (d.getDocID()!=null && doc.getDocID()!=null && d.equals(doc)) {
					
					count = (double)d.getTermFreq();
					return count;
				}
			}
		}
		
		
		return 0.0;
	}
	
	
	
	
	private static Double getTotalTokenCount(List<DocumentDetails> docs) {
		int count = 0;
		
		for (DocumentDetails d : docs) {
			
			if (docMap.get(d.getDocID())!=null) {
				count += docMap.get(d.getDocID()).getTokenCount();
			}
			
		}
		
		return count*1d;
	}

*/}
