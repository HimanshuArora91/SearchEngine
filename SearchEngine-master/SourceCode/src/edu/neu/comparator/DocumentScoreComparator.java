package edu.neu.comparator;

import java.util.Comparator;

import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;

public class DocumentScoreComparator implements Comparator<DocumentDetails> {

	@Override
	public int compare(DocumentDetails d1, DocumentDetails d2) {
		if (d1!=null && d2!=null) {
			//return (-1 * d1.getScore().compareTo(d2.getScore()));
			
			if (d1.getScore() > d2.getScore()) {
				return -1;
			}
			else if (d1.getScore() < d2.getScore()) {
				return 1;
			}
			else {
				return 0;
			}
		}
		return 0;
	}

}
