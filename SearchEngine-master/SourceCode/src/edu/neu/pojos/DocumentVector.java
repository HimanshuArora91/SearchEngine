package edu.neu.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentVector implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DocumentDetails doc;
	private String term;
	private Double normalizedTermFreq;
	private Double idf;
	private Double tfIdf;
	//private List<Double> termWeights;
	private Map<String, Double> termWeightsMap = new HashMap<String, Double>();
	
	
	
	public Map<String, Double> getTermWeightsMap() {
		return termWeightsMap;
	}
	public void setTermWeightsMap(Map<String, Double> termWeightsMap) {
		this.termWeightsMap = termWeightsMap;
	}
	public DocumentDetails getDoc() {
		return doc;
	}
	public void setDoc(DocumentDetails doc) {
		this.doc = doc;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Double getNormalizedTermFreq() {
		return normalizedTermFreq;
	}
	public void setNormalizedTermFreq(Double normalizedTermFreq) {
		this.normalizedTermFreq = normalizedTermFreq;
	}
	public Double getIdf() {
		return idf;
	}
	public void setIdf(Double idf) {
		this.idf = idf;
	}
	public Double getTfIdf() {
		return tfIdf;
	}
	public void setTfIdf(Double tfIdf) {
		this.tfIdf = tfIdf;
	}
	
	

	
}
