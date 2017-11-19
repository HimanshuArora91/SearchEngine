package edu.neu.retrieval.bm25;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.comparator.DocumentScoreComparator;
import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.evaluation.RelevantSet;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;
import edu.neu.queryExpansion.PseudoRelevanceRocchio;
import edu.neu.retrieval.RetrievalInterface;

public class BM25RetrievalAlgo implements RetrievalInterface{

	private Double k1 = 1.2d;
	private Double k2 = 100d;
	private Double b = 0.75d;
	private String qId=null;
	private int flow=1;
	private static final Map<String, DocumentDetails> docMap = DownloadPage
			.getDocIDsMap();

	public List<DocumentDetails> generateRelevantDocList(List<String> queryTerms,  String queryId, int flow) {
		this.qId=queryId;
		this.flow = flow;
		// creating the normalised term frequency
		List<DocumentDetails> resultantDocs = null;

		if (queryTerms != null && !queryTerms.isEmpty()) {
			Map<String, IndexEntry> indexMap = DownloadPage.getIndexData(1);
			Map<String, IndexEntry> indexMapOfQueryEntries = new HashMap<String, IndexEntry>();

			// get related documents
			for (String item : queryTerms) {
				if (indexMap.containsKey(item)) {
					indexMapOfQueryEntries.put(item, indexMap.get(item));

				}
			}

			resultantDocs = calculateBM25Score(indexMapOfQueryEntries, queryTerms);

			Collections.sort(resultantDocs, new DocumentScoreComparator());

			/*for (DocumentDetails d : resultantDocs) {
				System.out.println(d.getDocID() + " " + d.getDocName() + " "
						+ d.getScore());
			}*/
			return resultantDocs;
		}

		System.out.println("Please enter a proper query");

		return resultantDocs;
	}

	private List<DocumentDetails> calculateBM25Score(
			Map<String, IndexEntry> indexMapOfQueryEntries, List<String> queryTerms) {

		
		Set<DocumentDetails> relevantDocSet = new HashSet<DocumentDetails>();
		Double avdl = averageDocLength();

		for (Map.Entry<String, IndexEntry> item : indexMapOfQueryEntries
				.entrySet()) {

			relevantDocSet.addAll(item.getValue().getDocs());

		}
		
		// creating relevant Docs Map
		Map<String,DocumentDetails> relevantDocMap = new HashMap<String, DocumentDetails>();
		for (DocumentDetails d : relevantDocSet) {
			DocumentDetails dEntry = new DocumentDetails();
			dEntry.setDocID(d.getDocID());			
			dEntry.setDocName(d.getDocID());
			dEntry.setTokenCount(d.getTokenCount());
			d.setTermFreq(d.getTermFreq());
			relevantDocMap.put(d.getDocID(), dEntry);
		}
		

		for (Map.Entry<String, IndexEntry> item : indexMapOfQueryEntries
				.entrySet()) {

			Double idf = idf(item.getValue());
			for (DocumentDetails d : item.getValue().getDocs()) {
				
				if (relevantDocMap.get(d.getDocID())!=null) {
					
					int qf = qf(item.getKey(), queryTerms);
					Double curScore =  Math.log(idf) * 
							(d.getTermFreq()*(k1+1)/(d.getTermFreq() + calculateK(docMap.get(d.getDocID()).getTokenCount(), avdl))) *
							(((k2+1)*qf)*1d/(k2+qf));
					
					relevantDocMap.get(d.getDocID()).setScore(relevantDocMap.get(d.getDocID()).getScore() + curScore);
				}
			}
		}

		List<DocumentDetails> resultantDocs = new ArrayList<DocumentDetails>(relevantDocMap.values());
		return resultantDocs;
	}
	
	private int qf(String term, List<String> queryTerms) {

		return Collections.frequency(queryTerms, term); 
		
		
		
	}
	
	private Double idf(IndexEntry index) {
		Double result = 0.0;
		
		RelevantSet rs = new RelevantSet();
		 String queryExpansionFlag= PropertyFileManager.getProperty("QUERY_EXPANSION");
		 if ("N".equals(queryExpansionFlag)) {
			 flow=0;
		 }
		 
		Map <String , List<String>> relevantDocSet = (flow==0)?rs.retrieveRelevantSet():PseudoRelevanceRocchio.relevantDocs;
		
		
		if (relevantDocSet.containsKey(qId)) {
			Double R = (double) relevantDocSet.get(qId).size();
			Double ri = calculateRI(relevantDocSet.get(qId),index);
			
			result =((ri+0.5)/(R-ri+0.5)) / ((index.getDocs().size() - ri + 0.5)/(docMap.size() - index.getDocs().size() - R + ri + 0.5));
			
			
		}else {
			result = ((docMap.size() - index.getDocs().size() + 0.5) / (index.getDocs().size() + 0.5));
		}
		return ((docMap.size() - index.getDocs().size() + 0.5) / (index.getDocs().size() + 0.5));
	}
	
	private Double calculateRI(List<String> docs, IndexEntry index) {
		Double count = 0.0;
		for (String doc : docs) {
			if (index.getDocs().contains(doc)) {
				count = count + 1;
			}
		}
		
		return count;
		
	}

	private double calculateK(Integer dl, Double avdl) {

		return k1 * 1.0 * (1 - b + b * (dl / (1.0 * avdl)));
	}

	private Double averageDocLength() {

		Double avdl = 0d;

		int wordCount = 0;

		for (Map.Entry<String, DocumentDetails> item : docMap.entrySet()) {

			wordCount += item.getValue().getTokenCount();
		}
		avdl = (1d * wordCount) / docMap.size();
		return avdl;
	}
}
