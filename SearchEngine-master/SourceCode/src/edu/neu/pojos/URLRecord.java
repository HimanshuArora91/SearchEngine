package edu.neu.pojos;

import java.io.Serializable;


public class URLRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String urlString;
	//HTML File name is also the same
	private Integer recordId;
	private Integer depth;
	private String urlLabel;
	private double pageRank;
	private String docId;
	private String docName;
	private Integer tokenCount;
	
	
		
	public Integer getTokenCount() {
		return tokenCount;
	}
	public void setTokenCount(Integer tokenCount) {
		this.tokenCount = tokenCount;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	// Getters and Seters
	public String getUrlString() {
		return urlString;
	}
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	public String getUrlLabel() {
		return urlLabel;
	}
	public void setUrlLabel(String urlLabel) {
		this.urlLabel = urlLabel;
	}
	public double getPageRank() {
		return pageRank;
	}
	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}
	
@Override
public String toString() {
	return urlLabel + "||" + urlString ;
}
}
