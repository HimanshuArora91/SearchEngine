package edu.neu.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndexEntry implements Serializable{
	
		
	private static final long serialVersionUID = 1L;
	
	private String indexName;
	private List <DocumentDetails> docs;
	private Long rank;
	private Integer documentFrequency;
	private Integer totFrequency;
	private Double idf;
	
	
	
	
	public IndexEntry(){
		this.indexName = "";
		docs = new ArrayList<DocumentDetails>();
		this.documentFrequency=0;
	}
	
	
	
	public Double getIdf() {
		return idf;
	}

	public void setIdf(Double idf) {
		this.idf = idf;
	}





	public Integer getTotFrequency() {
		return totFrequency;
	}

	public void setTotFrequency(Integer totFrequency) {
		this.totFrequency = totFrequency;
	}
	
	public Integer getDocumentFrequency() {
		return documentFrequency;
	}
	public void setDocumentFrequency(Integer documentFrequency) {
		this.documentFrequency = documentFrequency;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public List<DocumentDetails> getDocs() {
		return docs;
	}
	public void setDocs(List<DocumentDetails> docs) {
		this.docs = docs;
	}
		
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	@Override
	public boolean equals(Object obj) {
	
		if ((obj instanceof IndexEntry) && this.indexName.equals(((IndexEntry)obj).getIndexName())) {
			return true;
		}
		else {
			return false;
		}
	}
	public void addDocFrequencyByOne(){
		this.documentFrequency++;
	}
	
	@Override
	public String toString() {
		
		return this.indexName + "->" + printDocIds(); 
	}
	private String printDocIds() {
		StringBuilder docString= new StringBuilder("");
		if (this.getDocs() != null && !this.getDocs().isEmpty()) {
			int length = this.getDocs().size();
			int count=0;
			for (DocumentDetails d : this.getDocs()) {
				count++;
				docString.append("("+ d.getDocID() + "," + d.getTermFreq() + ")" + ((count==length)? "":","));
								
			}
		}
		return docString.toString();
	}
	
}
