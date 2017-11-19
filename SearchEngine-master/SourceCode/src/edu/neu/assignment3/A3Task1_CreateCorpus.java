package edu.neu.assignment3;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.neu.corpusgenerate.ConvertStemCorpus;
import edu.neu.corpusgenerate.GenerateCorpus;
import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.URLRecord;

public class A3Task1_CreateCorpus {
	 
	public static void main(String args[]) throws IOException {
		new PropertyFileManager();
		Date t1 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 3 Task 1 - CREATE CORPUS Started ----------");
		System.out.println("------- GENERATING CORPUS : Please wait ----------");
		
		
				String stemmingFlag = PropertyFileManager.getProperty("STEMMING");						
		
				if ("Y".equals(stemmingFlag)) {
					ConvertStemCorpus cs = new ConvertStemCorpus();					
					cs.addToMap();
				List<URLRecord> docList = cs.writeToFile();
				if (docList!=null && !docList.isEmpty()) {
					DownloadPage.createDocIDsFile(docList);	
				}
				
					
				}
				else {
					GenerateCorpus g = new GenerateCorpus();
					g.generateCorpus();		
				}
			
		System.out.println("------- Assignment 3 Task 1 End ----------");
		
		Date t2 = Calendar.getInstance().getTime();
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
		

	}
}
