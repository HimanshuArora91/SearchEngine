package edu.neu.utils;

import java.util.List;

import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;

public class Utils {
	
	public static int getTotalFrequency(IndexEntry i) {
		
		int count = 0;
		if (i !=null && i.getDocs()!=null && !i.getDocs().isEmpty()) {
			
			for (DocumentDetails d : i.getDocs()) {
				count += d.getTermFreq();
			}
		}
			
			i.setTotFrequency(count);
			return count;
		
	} 
	public static void calculateTotalFrequency(List<IndexEntry> list) {
	
		
		for (IndexEntry item : list) {
			item.setTotFrequency(getTotalFrequency(item));
		}

	}
}

	

