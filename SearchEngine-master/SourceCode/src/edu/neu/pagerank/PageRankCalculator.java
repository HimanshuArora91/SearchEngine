


package edu.neu.pagerank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;

public class PageRankCalculator {

	// Total no. of Nodes in the corpus. In our case it will be 1000
	private Integer N;
	private double damping_factor = 0.85d;
	private Map<String, List<String>> pageMap = null;
	// Sink Nodes
	private Set<String> SINK_NODES;
	private Map<String,List<String>>  outLinksMap = new HashMap<String, List<String>>();

	// for Calculating L
	// private Map<String, Integer> outLinksMap = new HashMap<String, Integer>();
	
	// For calculating page rank in any current Iteration
	private Map<String, Double> pageRank_PR = new LinkedHashMap<String, Double>();

	
	
	/**
	 * @param pageMap
	 */
	public void calculatePageRanks(Map<String, List<String>> inputPageMap) {

		System.out.println("---- Calculating page Rank ----");
		
		if (inputPageMap!=null) {
		this.pageMap = inputPageMap;
		N = pageMap.size();
		SINK_NODES = getAllSinkNodes(pageMap);
		Set<String> P = pageMap.keySet();
	
		double PR = 0;

		for (String p : P) {
			PR = 1.0 / N;
			pageRank_PR.put(p, PR);
		}

		// Calculate L before the loop
		//calculateL();
		calculateL();
		List<Double> perplexity = new ArrayList<Double>();
		
		System.out.println("Calculating current perplexity");
		double currentPerplexity = perplexityCalculator();
		System.out.println("Initial Perplexity : " + currentPerplexity);
		
		perplexity.add(currentPerplexity);
		int convergenceCount = 0;
		int iterCount = 1;
		while (true) {
			System.out.println("Iteration-->" + iterCount);
			double sinkPR = 0.0;
			for (String p : SINK_NODES) {
				sinkPR += pageRank_PR.get(p);
			}
			Map<String, Double> pageRank_NEW_PR = new HashMap<String, Double>();
			for (String p : P) {
				Double newPR = (1 - damping_factor) / N;
				newPR += (damping_factor * sinkPR) / N;
				// Traversing through M(p)

				for (String q : M(p)) {
					newPR += damping_factor * (pageRank_PR.get(q) / L(q));
				}

				pageRank_NEW_PR.put(p, newPR);
			}

			//System.out.println("Page Ranks in Iteration(" + iterCount + ") : "
			//		+ pageRank_NEW_PR);

			for (String p : P) {
				pageRank_PR.put(p, pageRank_NEW_PR.get(p));
			}

			currentPerplexity = perplexityCalculator();
			System.out.println("Perplexity in Iteration : (" + iterCount
					+ ") =>" + currentPerplexity);
			if (perplexity.size() > 1
					&& (Math.abs((currentPerplexity - perplexity.get(perplexity
							.size() - 1))) < 1)) {
				convergenceCount++;
				if (convergenceCount == 4) {
					perplexity.add(currentPerplexity);
					break;
				}
			} else {
				convergenceCount = 0;
			}

			perplexity.add(currentPerplexity);
			iterCount++;
		}

		//System.out.println(pageRank_PR);

		// Generating Perplexity File
		DownloadPage.printAllPerplexityValues(perplexity);
		
		// Generating pageRank File where page Rank is in sorted order
		DownloadPage.printSortedPageRank(pageRank_PR,PropertyFileManager.getProperty("PAGE_RANK_FILE"));
		
		// Print pages with inlinks
		
	
		
	   DownloadPage.printSortedInlinks(pageMap,PropertyFileManager.getProperty("IN_LINKS_FILE"));
		
		
	}
	else {
		System.out.println("ERROR!! Input graph is null or empty. Please check the configuration...");
	}
	}

	private List<String> M(String p) {
		return this.pageMap.get(p);
	}

	/*private int L(String q) {
		return outLinksMap.get(q);

	}*/
	
	private int L(String q) {
		return outLinksMap.get(q).size();

	}

	/*private void calculateL() {
	
		List<String> temp = new ArrayList<String>();

		Set<String> firstColumnPagesSet = new HashSet<String>(pageMap.keySet());
		
		firstColumnPagesSet.removeAll(SINK_NODES);

		for (List<String> l : this.pageMap.values()) {
			temp.addAll(l);
		}

		for (String q : firstColumnPagesSet) {
			outLinksMap.put(q, Collections.frequency(temp, q));
		}
		
		
		
		System.out.println("L() calculation completes for all the nodes");
		

	}*/
	
	private void calculateL() {

		// firstColumnPagesSet.removeAll(SINK_NODES);

		for (String mapKey : pageMap.keySet()) {

			for (String eachListItem : pageMap.get(mapKey)) {

				if (outLinksMap.get(eachListItem) == null) {
					outLinksMap.put(eachListItem, new ArrayList<String>());
					outLinksMap.get(eachListItem).add(mapKey);
				} else {
					outLinksMap.get(eachListItem).add(mapKey);
				}

			}
		}

		System.out.println("L2() calculation completes for all the nodes");

	}

	public Set<String> getAllSinkNodes(Map<String, List<String>> pageMap) {
		// Set<String> s = new LinkedHashSet<String>();

		Set<String> firstColumnPagesSet = new HashSet<String>(pageMap.keySet());

		//firstColumnPagesSet.addAll(pageMap.keySet());
		Set<String> allInboundPagesInGraph = new HashSet<String>();
		
		for (String i : firstColumnPagesSet) {
			allInboundPagesInGraph.addAll(pageMap.get(i));
		}

		System.out.println(firstColumnPagesSet
				.removeAll(allInboundPagesInGraph));
		System.out.println("SINK NODES :> " + firstColumnPagesSet+ "\n");

		// System.out.println("SINK NODES : " + s);
		return firstColumnPagesSet;
	}

	private double perplexityCalculator() {
		return Math.pow(2.0, entropyCalculator());
	}

	private double entropyCalculator() {

		double sum = 0;
		for (String p : pageRank_PR.keySet()) {
			sum += pageRank_PR.get(p) * logBase2(pageRank_PR.get(p));
		}

		return -1.0 * sum;
	}

	private double logBase2(double p) {

		return (Math.log(p)) / (Math.log(2.0));
	}
	
	public Set<String> getPagesWithNoInlinks(Map<String, List<String>> inputPageMap) {
		Set<String> noInLinkSet = new HashSet<String>();
		
		for (String p : inputPageMap.keySet()) {
			
			if (inputPageMap.get(p) == null || inputPageMap.get(p).isEmpty()){
				noInLinkSet.add(p);
			}
		}
		System.out.println("\n\nPages with NO IN LINKS :> " + noInLinkSet + "\n\n");
		return noInLinkSet;
		
	}
}
