package edu.neu.comparator;

import java.util.Comparator;

import edu.neu.pojos.IndexEntry;
import edu.neu.utils.Utils;

public class TermFrequencyComparator implements Comparator<IndexEntry> {

	@Override
	public int compare(IndexEntry s1, IndexEntry s2) {
		if (s1.getTotFrequency() > s2.getTotFrequency()) {
			// To return reverse list
			return -1;
		} else if (s1.getTotFrequency() < s2.getTotFrequency()){
			return 1;
		}
		else if (s1.getTotFrequency() == s2.getTotFrequency()) {
			return 0;
		}
		return 0;
	}

}
