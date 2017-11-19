package edu.neu.crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.CrawlerResponse;
import edu.neu.pojos.URLRecord;

public class WebCrawler {

	private List<URLRecord> urlListVisited = null;
	private Queue<String> urlListToVisit = new LinkedList<String>();
	private final int MAXCOUNT = 1000;
	private final int MAXDEPTH = 5;
	private int depthCounter = 1;
	private int count = 1;
	private String filePath = null;

	// Map <Integer,Set> map = new HashMap<Integer,Set>();

	public void executeTask1(String seedURL) {

		urlListVisited = new LinkedList<URLRecord>();

		// map.put(depthCounter++, value)
		crawlSeedPage(seedURL, null);
		crawlPagesFromList(null);

		System.out.println("List size---->" + urlListVisited.size());
		DownloadPage.saveUrlMappings(urlListVisited, PropertyFileManager
				.getProperty("task1_url_mapping_file_location"));

	}

	private void crawlSeedPage(String url, String keyword) {

		if (!isURLAlreadyAdded(url)) {
			CrawlerHelper helper = new CrawlerHelper();
			CrawlerResponse res = helper.crawlWebPage(url, keyword);

			String corpusFlag = PropertyFileManager
					.getProperty("create_corpus");
			if (corpusFlag != null && "Y".equals(corpusFlag)) {
				if (keyword != null && !keyword.isEmpty()) {
					createCorpus("TASK2A", res);
				} else {
					createCorpus("TASK1", res);
				}

			}
			addURLToFinalList(count++, url, depthCounter);

			urlListToVisit.addAll(res.getUrlSet());

			depthCounter++;

		}
	}

	private void crawlPagesFromList(String keyword) {

		if (depthCounter <= MAXDEPTH) {

			Queue<String> tempList = new LinkedList<String>();
			while (urlListToVisit != null && !urlListToVisit.isEmpty()
					&& urlListVisited.size() < MAXCOUNT) {

				String curUrl = urlListToVisit.remove();
				if (!isURLAlreadyAdded(curUrl)) {

					CrawlerHelper helper = new CrawlerHelper();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
						System.out.println("Some error occured");
						//e.printStackTrace();
					}

					CrawlerResponse res = helper.crawlWebPage(curUrl, keyword);
					String corpusFlag = PropertyFileManager
							.getProperty("create_corpus");
					if (corpusFlag != null && "Y".equals(corpusFlag)) {
						if (keyword != null && !keyword.isEmpty()) {
							createCorpus("TASK2A", res);
						} else {
							createCorpus("TASK1", res);
						}

					}

					addURLToFinalList(count++, curUrl, depthCounter);
					tempList.addAll(res.getUrlSet());
				}
			}
			System.out.println("TempList Size at depth" + depthCounter + "::"
					+ tempList.size());
			depthCounter++;
			if (!tempList.isEmpty() && depthCounter <= MAXDEPTH) {
				urlListToVisit.addAll(tempList);

				if (!urlListToVisit.isEmpty()) {
					crawlPagesFromList(keyword);
				}

			}

		}

	}

	public void executeTask2BFS(String seedURL, String keyword) {

		urlListVisited = new LinkedList<URLRecord>();

		crawlSeedPage(seedURL, keyword);
		crawlPagesFromList(keyword);

		System.out.println("List size---->" + urlListVisited.size());
		DownloadPage
				.saveUrlMappings(
						urlListVisited,
						"task2A__BFS_url_mapping_file_location");

	}

	public void executeTask2DFS(String seedURL, String keyword) {

		urlListVisited = new LinkedList<URLRecord>();

		// crawlSeedPage(seedURL, keyword);
		crawlPagesDFS(seedURL, keyword, depthCounter);

		DownloadPage.saveUrlMappings(urlListVisited, PropertyFileManager
				.getProperty("task2B_DFS_url_mapping_file_location"));
	}

	private int dfsCount = 0;

	private void crawlPagesDFS (String curUrl, String keyword, int depth) {
						
		if (!isURLAlreadyAdded(curUrl)) {
			Queue<String> tempList = new LinkedList<String>();
			System.out.println("At Depth : " + depth);
			System.out.println("Currently Crawling :  " + curUrl);
			CrawlerHelper helper = new CrawlerHelper();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
				System.out.println("Interrrupted Exception");
				//e.printStackTrace();
			}

			CrawlerResponse res = helper.crawlWebPage(curUrl, keyword);			
			String corpusFlag = PropertyFileManager.getProperty("create_corpus");
			if (corpusFlag!=null && "Y".equals(corpusFlag)) {
				createCorpus("TASK2B", res); 
			}
	
			addURLToFinalList(count++, curUrl, depth);
			tempList.addAll(res.getUrlSet());
			
			while (depth < MAXDEPTH && tempList != null 
					&& !tempList.isEmpty() 
					&& urlListVisited.size() < MAXCOUNT) {
				
					crawlPagesDFS(tempList.remove(), keyword, depth+1);
		}
	}
	}

	private boolean addURLToFinalList(int recordID, String urlString,
			Integer depth) {

		if (urlListVisited != null && urlListVisited.size() < MAXCOUNT) {
			URLRecord rec = new URLRecord();
			rec.setRecordId(recordID);
			rec.setUrlString(urlString);
			// rec.setUrlLabelOnPage(urlLabel);
			rec.setDepth(depth);
			urlListVisited.add(rec);
			return true;
		}
		return false;

	}

	private Boolean createCorpus(String task, CrawlerResponse res) {

		if (res != null) {

			switch (task) {
			case "TASK1":
				filePath = PropertyFileManager
						.getProperty("task1_corpus_location");
				break;
			case "TASK2A":
				filePath = PropertyFileManager
						.getProperty("task2A_BFS_corpus_location");
				break;
			case "TASK2B":
				filePath = PropertyFileManager
						.getProperty("task2B_DFS_corpus_location");
				break;
			}

			return DownloadPage.saveDocument(count, res.getDocument()
					.toString(), filePath);
		}
		return false;

	}

	private boolean isURLAlreadyAdded(String url) {
		if (urlListVisited != null && !urlListVisited.isEmpty()) {
			for (URLRecord rec : urlListVisited) {
				if (url.equals(rec.getUrlString())) {
					return true;
				}
			}
		}

		return false;

	}

}
