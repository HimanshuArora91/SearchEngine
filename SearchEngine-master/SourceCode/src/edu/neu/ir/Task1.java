package edu.neu.ir;

import edu.neu.crawler.WebCrawler;
import edu.neu.download.PropertyFileManager;;

public class Task1 {

	//private static final String SEEDURL = "https://en.wikipedia.org/wiki/Sustainable_energy";
	//private static final String SEEDURL = "https://en.wikipedia.org/wiki/Greenpower";
	public static void main (String args[]) {
		new PropertyFileManager();
		WebCrawler w = new WebCrawler();
		System.out.println("------- Task 1 Started ----------");
		
		w.executeTask1(PropertyFileManager.getProperty("seed_url"));
		
		System.out.println("------- Task 1 Completed ----------");
		
		
	}
}
