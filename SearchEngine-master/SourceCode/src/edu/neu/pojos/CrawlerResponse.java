package edu.neu.pojos;

import java.io.Serializable;
import java.util.Set;

import org.jsoup.nodes.Document;

public class CrawlerResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> urlSet;
	private Document document;
	
	public Set<String> getUrlSet() {
		return urlSet;
	}
	public void setUrlSet(Set<String> urlSet) {
		this.urlSet = urlSet;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
}
