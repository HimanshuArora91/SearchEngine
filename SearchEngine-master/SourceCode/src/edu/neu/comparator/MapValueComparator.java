package edu.neu.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapValueComparator implements Comparator<String> {

	Map<String, Double> localMap = new HashMap<String,  Double>();

	public MapValueComparator(Map<String, Double> map) {
		this.localMap.putAll(map);
	}

	@Override
	public int compare(String s1, String s2) {
		if (localMap.get(s1) >= localMap.get(s2)) {
			// To return reverse list
			return -1;
		} else {
			return 1;
		}
	}

}
