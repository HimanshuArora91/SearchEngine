package edu.neu.assignment3;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.corpusgenerate.GenerateCorpus;
import edu.neu.download.PropertyFileManager;
import edu.neu.indexer.IndexCreaterNGrams;

public class CreateInvertedIndex1_2_3_Grams {
	private static final int MAXTHREADS = 5;
	
	public static void main (String ...args) {
		new PropertyFileManager();
		
		System.out.println("------- Assignment 3 Task 2 N Grams Started ----------");
		Date t1 = Calendar.getInstance().getTime();
	
	
		/*for (int i = 1; i <= 3; i++) {
			Runnable indexer = new IndexCreaterNGrams(i);
			indexer.run();
		}*/

		Runnable indexer = new IndexCreaterNGrams(1);
		indexer.run();
		
		System.out.println("\n Finished all threads");
		Date t2 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 3 Task 2 N Grams Ended ----------");
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
	}

}

