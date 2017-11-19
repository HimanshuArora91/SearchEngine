package edu.neu.ir;

import edu.neu.crawler.WebCrawler;
import edu.neu.download.PropertyFileManager;

public class Task3 {

	public static void main (String args[]) {
		
		new PropertyFileManager();
		
		WebCrawler w = new WebCrawler();
		System.out.println("------- Task 3 Started ----------");
		w.executeTask2DFS(PropertyFileManager.getProperty("seed_url"),PropertyFileManager.getProperty("keyword"));
		System.out.println("------- Task 3 Completed ----------");
	}
	
	
}
