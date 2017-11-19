package edu.neu.retrieval.cosinesimliarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import edu.neu.comparator.DocumentScoreComparator;
import edu.neu.download.DownloadPage;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.DocumentVector;
import edu.neu.pojos.IndexEntry;
import edu.neu.retrieval.RetrievalInterface;

public class CosineSimilarityCalculator implements RetrievalInterface{
	private static final Map<String, DocumentDetails> docMap = DownloadPage
			.getDocIDsMap();

	public List<DocumentDetails> generateRelevantDocList(List<String> queryTerms, String queryId, int flow) {

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

			for (String item : indexMap.keySet()) {
				IndexEntry e = indexMap.get(item);

				// calculated normalised tf
				calculateNormalisedTF(e);

				// Inverse document frequency
				calculateIDF(e);

				//calculatetfIdf(e);
			}

			for (String item : indexMap.keySet()) {
				IndexEntry e = indexMap.get(item);

				// Calculate TF*IDF
				calculatetfIdf(e);
			}
			
			//List<DocumentVector> queryVector = new ArrayList<DocumentVector>();
			
			DocumentVector queryVector = new DocumentVector();			
			for (String queryTerm : queryTerms) {
								
				if (indexMapOfQueryEntries.get(queryTerm)!=null) {
					Double normalizedTermFreq = (1.0 * getTermCount(queryTerms, queryTerm)/queryTerms.size());
					Double queryIdf =indexMapOfQueryEntries.get(queryTerm).getIdf();
					//queryVector.setTfIdf(dv.getNormalizedTermFreq() * dv.getIdf());
					//queryVector.add(dv);
					queryVector.getTermWeightsMap().put(queryTerm, normalizedTermFreq*queryIdf);
				}
				
			}
			
			Map<String, DocumentVector> allDocsVector = createDocumentVectors (indexMap);
			
			
			resultantDocs = calculateCosineSimilarityOfQueryAndDocs(queryVector, allDocsVector);
			
		
			Collections.sort(resultantDocs, new DocumentScoreComparator());
			
			
			for (DocumentDetails d : resultantDocs) {
				System.out.println(d.getDocID() + " " + d.getDocName() + " " + d.getScore());
			}
			return resultantDocs;
		}
		
		System.out.println("Please enter a proper query");
		
		return resultantDocs;
	}
	
	
	private Map<String, DocumentVector> createDocumentVectors (Map<String, IndexEntry> indexMapOfQueryEntries) {
		//List<DocumentVector> docVector = new ArrayList<DocumentVector>();
		
		Map<String, DocumentVector> docVectorMap = new HashMap<String, DocumentVector>();
		
		for (IndexEntry e : indexMapOfQueryEntries.values()) {
			
			for (DocumentDetails dItem : e.getDocs()) {
				
				if (docVectorMap.containsKey(dItem.getDocID())) {
					DocumentVector v = docVectorMap.get(dItem.getDocID());
			
					/*if (v.getTermWeightsMap().containsKey(e.getIndexName())) {
						
					}*/
					v.getTermWeightsMap().put(e.getIndexName(), dItem.getTfIdf());
					
					
					
				}
				else {
					DocumentVector doctemp = new DocumentVector();
					DocumentDetails d = new DocumentDetails();
					d.setDocID(dItem.getDocID());
					//d.setDocName(docMap.get(dItem.getDocID()).getDocName());
					d.setDocName(dItem.getDocID());
					doctemp.setDoc(d);					
					doctemp.getTermWeightsMap().put(e.getIndexName(), dItem.getTfIdf());
					docVectorMap.put(dItem.getDocID(), doctemp);
				}
			}
			
			
		}
				
		return docVectorMap;
	}
	
	private List<DocumentDetails> calculateCosineSimilarityOfQueryAndDocs(DocumentVector queryVector, Map<String, DocumentVector> allDocsVector) {
		List<DocumentDetails> results = new ArrayList<DocumentDetails>();
		
		for (Map.Entry<String, DocumentVector> docVectorEntry : allDocsVector.entrySet()) {
			
			Double dotProduct = 0d;
			for (String queryTerm : queryVector.getTermWeightsMap().keySet()) {
				
				Double docTermWeight = docVectorEntry.getValue().getTermWeightsMap().get(queryTerm);
				dotProduct += docTermWeight!=null ? docTermWeight * queryVector.getTermWeightsMap().get(queryTerm) : 0d ;
						
				
			}
			Double normalizationFactor = Math.sqrt(sumOfSquares(queryVector) * sumOfSquares(docVectorEntry.getValue()));
			Double score = dotProduct/normalizationFactor;
			DocumentDetails d = new DocumentDetails();
			d.setDocID(docVectorEntry.getValue().getDoc().getDocID());
			d.setDocName(docVectorEntry.getValue().getDoc().getDocName());
			d.setScore(score);
			if (d.getScore()>0.0){
				results.add(d);	
			}
			 
		}
		
		
		
		return results;
	}
	
	
	private double sumOfSquares(DocumentVector d) {
		Double nFactor = 0d;
		if (d!=null && d.getTermWeightsMap()!=null && d.getTermWeightsMap().size()>0) {
			for (Map.Entry<String, Double> termWeight : d.getTermWeightsMap().entrySet()) {
				nFactor += Math.pow(termWeight.getValue(), 2);
			}
			
			return nFactor;
			
		}
		else 
			return 1d;
		
		
	}
		
	private Integer getTermCount(List<String> queryTerms,String term) {
		int count = 0;
		for (String item : queryTerms) {
			if (item.toLowerCase().equals(term.toLowerCase())) {
				count ++;
			}
		}
		return count;
	}

	private void calculateNormalisedTF(IndexEntry e) {

		if (e != null && e.getDocs() != null && !e.getDocs().isEmpty()) {

			for (DocumentDetails doc : e.getDocs()) {
				doc.setNormallisedTermFreq(doc.getTermFreq().doubleValue()
						/ docMap.get(doc.getDocID()).getTokenCount());
			}
		}
	}

	private void calculateIDF(IndexEntry e) {
		if (e != null && e.getDocs() != null && !e.getDocs().isEmpty()) {
			e.setIdf(Math.log((1D * docMap.keySet().size()) 
					/ e.getDocs().size()));
		}

	}

	private void calculatetfIdf(IndexEntry e) {
		if (e != null && e.getDocs() != null && !e.getDocs().isEmpty()) {

			for (DocumentDetails doc : e.getDocs()) {
				doc.setTfIdf(doc.getNormallisedTermFreq() * e.getIdf());
			}
		}
	}
	
	
}
