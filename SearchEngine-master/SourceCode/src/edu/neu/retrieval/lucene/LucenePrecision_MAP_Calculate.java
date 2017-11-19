package edu.neu.retrieval.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.neu.download.PropertyFileManager;
import edu.neu.evaluation.PrecisionRecall;
import edu.neu.evaluation.RelevantSet;
import edu.neu.pojos.DocumentDetails;

public class LucenePrecision_MAP_Calculate {
	
	private static BufferedWriter bw = null;
	private static FileWriter fw;
	private static BufferedReader br = null;

	public static void main (String args[]) {
		
		new PropertyFileManager();
		
		String path = PropertyFileManager
		.getProperty("LUCENE_RESULTS");
		try {
			
		String line = null;
		StringBuilder text = new StringBuilder("");
		
		
		br = new BufferedReader(
				new FileReader(path));

		Integer count = 1;
		Map<String,List<DocumentDetails>> resultMap = new LinkedHashMap<String, List<DocumentDetails>>();
		while ((line = br.readLine()) != null) {
			
			
			String s[] = line.split("\\s+");
			DocumentDetails d = new DocumentDetails();
			if (!String.valueOf(count).equals(s[0])) {
				count++;
			}
			d.setDocID(s[2]);
			d.setScore(Double.parseDouble(s[4]));
			
			if (resultMap.get(count.toString())==null) {
				List<DocumentDetails> list = new ArrayList<DocumentDetails>();
				list.add(d);
				resultMap.put(count.toString(), list);
			}
			else {
				resultMap.get(count.toString()).add(d);
			}
			
			
			text.append(line + "\n");
			
			}
		
		System.out.println(resultMap);
		
		
	
		
		
		
		RelevantSet rs = new RelevantSet();
		
		Map <String , List<String>> relevantDocSet = rs.retrieveRelevantSet();
		
		PrecisionRecall pr = new PrecisionRecall();
		Map <String , ArrayList<String>> retrievedDocSet = pr.readRetrievedocs();
		
		pr.calcPrecisionRecall(relevantDocSet , retrievedDocSet, resultMap);
		
		
		} catch (FileNotFoundException e) {

			System.out
					.println("CORPUS DOC File Not Found. Please check if it is placed in the correct location");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {

				if (br != null)
					br.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}
		
		
	}
	
	
	

}
