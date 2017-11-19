package edu.neu.pojos;

import java.io.Serializable;

public class DocumentDetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docID;
	private Integer termFreq;
	private Integer tokenCount = 0;
	private String docText;
	private String docName;
	private Double normallisedTermFreq;
	private Double tfIdf;
	private Double score;
	
	public DocumentDetails(){
	this.docID = "";
	this.termFreq = 0;
	this.tokenCount = 0;	
	this.docText="";
	this.tokenCount = 0;
	this.normallisedTermFreq=0d;
	this.tfIdf=0d;
	}
	
	
	
	public Double getScore() {
		if (score==null)
			return 0d;
		return score;
	}


	public void setScore(Double score) {
		this.score = score;
	}
		
	public Double getTfIdf() {
		return tfIdf;
	}


	public void setTfIdf(Double tfIdf) {
		this.tfIdf = tfIdf;
	}

	public Double getNormallisedTermFreq() {
		return normallisedTermFreq;
	}





	public void setNormallisedTermFreq(Double normallisedTermFreq) {
		this.normallisedTermFreq = normallisedTermFreq;
	}





	public String getDocName() {
		return docName;
	}


	public void setDocName(String docName) {
		this.docName = docName;
	}


	public String getDocText() {
		return docText;
	}


	public void setDocText(String docText) {
		this.docText = docText;
	}


	public Integer getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(Integer tokenCount) {
		this.tokenCount = tokenCount;
	}

	public String getDocID() {
		return docID;
	}
	public void setDocID(String docID) {
		this.docID = docID;
	}
	public Integer getTermFreq() {
		return termFreq;
	}
	public void setTermFreq(Integer termFreq) {
		this.termFreq = termFreq;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if ((obj instanceof DocumentDetails) && this.docID.equals(((DocumentDetails)obj).getDocID())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addTermFrequencyByOne() {
		this.termFreq++;
	}

}
