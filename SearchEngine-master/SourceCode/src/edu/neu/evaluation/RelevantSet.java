package edu.neu.evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import edu.neu.download.PropertyFileManager;

public class RelevantSet {

	public static void main(String[] args) {
		
		
		RelevantSet rs = new RelevantSet();
		rs.retrieveRelevantSet();

	}
	
	public Map<String , List<String>>  retrieveRelevantSet()
	{
		Map<String ,List<String>> relevantSet = new LinkedHashMap<String , List<String>>();
		
		//String path = "C:/Users/Himanshu/Desktop/IR Project/cacm.txt";
		String path = PropertyFileManager
				.getProperty("RELEVANCE_JUDGEMENT_FILE");
		
		
		
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(path));
			 while((line = br.readLine())!= null )
			 {
				 
				 String  temp [] = line.split("\\s+");
				 
				 if(relevantSet.containsKey(temp[0]))
				 {
					 List<String> al = new ArrayList<String>();
					 al = relevantSet.get(temp[0]);
					 al.add(temp[2]);
					 relevantSet.put(temp[0],al);
				 }
				 else
				 {
					 ArrayList<String> al = new ArrayList<String>();
					 al.add(temp[2]);
					 relevantSet.put(temp[0], al);
					 
				 }
				 
				 
			 }
			 
			// br.close();
			// System.out.println(relevantSet.size());
			// System.out.println(relevantSet);
			 
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			try {
				
				if (br != null)
					br.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		
		}
		
		return relevantSet;

		
		
	}
	

}
