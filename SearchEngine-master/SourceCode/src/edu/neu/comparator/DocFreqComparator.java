package edu.neu.comparator;

import java.util.Comparator;

import edu.neu.pojos.IndexEntry;

public class DocFreqComparator implements Comparator<IndexEntry> {

	@Override
	public int compare(IndexEntry s1, IndexEntry s2) {
		if (s1!=null 
				&& s2!=null 
				&& s1.getIndexName()!=null 
				&& s2.getIndexName()!=null) {
			return (s1.getIndexName().compareToIgnoreCase(s2.getIndexName()));
		}
		return 0;
	}

}
