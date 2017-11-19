package edu.neu.graph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.neu.crawler.CrawlerHelper;
import edu.neu.download.DownloadPage;
import edu.neu.pojos.CrawlerResponse;
import edu.neu.pojos.URLRecord;

public class GraphCreater {

	private Map<String, List<String>> urlGraph = new LinkedHashMap<String, List<String>>();

	public void createGraph() {
		List<URLRecord> urlRecords = DownloadPage.getURLSFromMappingFile();
		System.out.println("URL Records : " + urlRecords);

		// Creating Empty Graph
		for (URLRecord item : urlRecords) {
			urlGraph.put(item.getUrlLabel(), new ArrayList<String>());
		}
		CrawlerHelper helper = new CrawlerHelper();

		// Crawl each URL page and put the in bound entries in the Map

		for (URLRecord item : urlRecords) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			CrawlerResponse res = helper.crawlWebPage(item.getUrlString(), null);
			if (res != null && res.getUrlSet() != null
					& !res.getUrlSet().isEmpty()) {
				
				for (String curUrl : res.getUrlSet()) {
					String urlKey = curUrl.replaceFirst(
							"https://en.wikipedia.org/wiki/", "");
					if (urlGraph.containsKey(urlKey)) {
						// Add the incoming label in the Current Node
						urlGraph.get(urlKey).add(item.getUrlLabel());
					}

				}
			} else {
				continue;
			}

		}

		DownloadPage.createGraph(urlGraph);

	}

}
