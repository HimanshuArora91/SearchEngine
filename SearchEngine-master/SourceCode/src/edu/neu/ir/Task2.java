package edu.neu.ir;

import edu.neu.crawler.WebCrawler;
import edu.neu.download.PropertyFileManager;

public class Task2 {
	
	private static final String SEEDURL = "https://en.wikipedia.org/wiki/Sustainable_energy";
	
	public static void main(String args[]) {
		new PropertyFileManager();
		
		WebCrawler w = new WebCrawler();
		System.out.println("------- Task 2 Started ----------");
		w.executeTask2BFS(PropertyFileManager.getProperty("seed_url"),PropertyFileManager.getProperty("keyword"));
		System.out.println("------- Task 2 Completed ----------");
		
	}

}
