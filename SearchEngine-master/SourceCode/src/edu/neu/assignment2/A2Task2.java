package edu.neu.assignment2;

import java.util.Calendar;
import java.util.Date;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pagerank.PageRankCalculator;

public class A2Task2 {
	
	public static void main(String args[]) {
		new PropertyFileManager();
		
		Date t1 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 2 Task 2 - Calculation of Page Rank -- Started ----------");
	
		
	   PageRankCalculator p = new PageRankCalculator();
	   p.calculatePageRanks(DownloadPage.getGraphList());
	   
	   
	   Date t2 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 2 Task 2 - Calculation of Page Rank -- Completed ----------");
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
	}

}
