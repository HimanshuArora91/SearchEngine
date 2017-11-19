package edu.neu.assignment4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.evaluation.PrecisionRecall;
import edu.neu.evaluation.RelevantSet;
import edu.neu.pojos.DocumentDetails;
import edu.neu.retrieval.RetrievalInterface;
import edu.neu.retrieval.bm25.BM25RetrievalAlgo;
import edu.neu.retrieval.cosinesimliarity.CosineSimilarityCalculator;
import edu.neu.retrieval.tfIdf.TFIdfRetrieval;
import edu.neu.utils.GetAllQueries;
import edu.neu.utils.StemQueryMap;

public class Task2_Retrieve {

	private static String retrievalModel = null;
	public static void main(String args[]) {

		new PropertyFileManager();
		Date t1 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 4 Task 2 - Retrieved Scored Documents Started ----------");
		
		 retrievalModel = PropertyFileManager
				.getProperty("RETRIEVAL_SYSTEM");
		
		// Initialising the class once to load class variables
		
	
		
		 // These are processed queries
		Map<String, String> queryMap = null;
		String stemmingFlag = PropertyFileManager
		.getProperty("STEMMING");
		
		if ("Y".equals(stemmingFlag)) {
				StemQueryMap sm = new StemQueryMap();
			
			 queryMap = sm.addQueryToMap();
		}
		else {
			
			// here we'll do the query EXPANSION if needed
			 queryMap = GetAllQueries.createQueryList();
		}
	
			Map<String,List<DocumentDetails>> resultMap = new LinkedHashMap<String, List<DocumentDetails>>();
			
			for (Map.Entry<String, String> queryEntry : queryMap.entrySet()) {
				
				
				String queryString = queryEntry.getValue();
				System.out.println("Query : " + queryEntry.getKey());
				
				
				//Query ExpansionCode
				
				//List<DocumentDetails> results = c.generateRelevantDocList(Arrays.asList(queryString.split("\\s+")));
				List<DocumentDetails> results = getQueryResults (queryString, queryEntry.getKey(),1);
				if (results!=null && !results.isEmpty()) {
					
					resultMap.put(queryEntry.getKey(), results);
					
				}
				else {
					System.out.println("No Result Found. Please enter valid query");
				}
				
			}
			
			if (resultMap!=null && resultMap.keySet()!=null && !resultMap.keySet().isEmpty()) {
				DownloadPage.generateQueryResultsFile(resultMap, retrievalModel,100);
				
			}
		
			
			// Evaluation start
			
			
			RelevantSet rs = new RelevantSet();
			
			Map <String , List<String>> relevantDocSet = rs.retrieveRelevantSet();
			
			PrecisionRecall pr = new PrecisionRecall();
			Map <String , ArrayList<String>> retrievedDocSet = pr.readRetrievedocs();
			
			pr.calcPrecisionRecall(relevantDocSet , retrievedDocSet, resultMap);
			
			// Evaluation End
			
		System.out.println("------- Assignment 4 Task 2 - Retrieved Scored Documents End ----------");
		
		Date t2 = Calendar.getInstance().getTime();
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
		

	
	}
	
	public static List<DocumentDetails> getQueryResults (String queryString, String queryId, int flow) {
				
		RetrievalInterface c =null;
		
		switch(retrievalModel) {
		case "BM25" : 	c = new BM25RetrievalAlgo();
						break;
		
		case "COSINE" : c = new CosineSimilarityCalculator(); 
						break;
		
		case "TFIDF" :  c= new TFIdfRetrieval();
			break;
		
		default : c = new BM25RetrievalAlgo();  
					  break;
		}
		
		List<DocumentDetails> results = c.generateRelevantDocList(Arrays.asList(queryString.split("\\s+")),queryId, flow);
		
		return results;
	}
}
