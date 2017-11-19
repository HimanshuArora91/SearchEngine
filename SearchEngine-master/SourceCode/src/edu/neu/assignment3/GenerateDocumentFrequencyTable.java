package edu.neu.assignment3;

import java.util.Calendar;
import java.util.Date;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.indexer.DocumentFrequencyTableGenerator;
import edu.neu.indexer.FrequencyTableGenerator;

public class GenerateDocumentFrequencyTable {

	public static void main(String args[]) {
		new PropertyFileManager();

		System.out
				.println("------- Assignment 3 Task 3 Generate Doc Frequency Table Started ----------");
		Date t1 = Calendar.getInstance().getTime();

		
		for (int i=1; i<=3 ; i++) {
			DocumentFrequencyTableGenerator d = new DocumentFrequencyTableGenerator(i);
			d.generateDocFrequencyTable(DownloadPage.getIndexData(i));
		}
		
		Date t2 = Calendar.getInstance().getTime();
		System.out.println("------- Assignment 3 Task 3 Doc Term Frequency Table Ended ----------");
		System.out.println("Time Taken --> " + (t2.getTime()-t1.getTime()));
	}
}
