package edu.neu.assignment2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pagerank.PageRankCalculator;


public class A2Task2B {

	/**
	 * @param args
	 */
	public static void main(String ...args) {
		new PropertyFileManager();
		 Map<String,List<String>> m = DownloadPage.getGraphList();
		
		Date t1 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 2 Task B Started ----------");
		
		
		
		System.out.println("Calculating pages with no in-links and pages with no sinks");
		
	    PageRankCalculator p = new PageRankCalculator();
	    Set<String> SINK_NODES = p.getAllSinkNodes(m);
	    System.out.println("Proportion of pages with SINKS to the total no. of pages :> \n");
	    System.out.println(SINK_NODES.size() +"/" + m.keySet().size());
	    
	    
	    // Pages with no inlinks
	   
	    
	    Set<String> noInLinkSet = p.getPagesWithNoInlinks(m);
	    
	    
	  
	    
	    System.out.println("Proportion of pages with NO IN LINKS  to the total no. of pages :> \n");
	    System.out.println(noInLinkSet.size() +"/" + m.keySet().size());
	    
	    
	   
	   Date t2 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 2 Task B Ended ----------");
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
		
	}
}
