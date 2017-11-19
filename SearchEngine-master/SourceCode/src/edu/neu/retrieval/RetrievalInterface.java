package edu.neu.retrieval;

import java.util.List;

import edu.neu.pojos.DocumentDetails;

public interface RetrievalInterface {

	
	
	public List<DocumentDetails> generateRelevantDocList(List<String> queryTerms, String queryId, int flow);
	
}
