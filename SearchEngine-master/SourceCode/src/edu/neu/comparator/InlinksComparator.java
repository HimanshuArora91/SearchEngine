package edu.neu.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InlinksComparator implements Comparator<String> {

	Map<String, Integer> localMap = new HashMap<String, Integer >();

	public InlinksComparator(Map<String, Integer> map) {
		this.localMap.putAll(map);
	}

	@Override
	public int compare(String s1, String s2) {
		if (localMap.get(s1) >= localMap.get(s2)) {
			return -1;
		} else {
			return 1;
		}
	}

}
