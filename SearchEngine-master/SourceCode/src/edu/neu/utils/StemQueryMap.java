package edu.neu.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.neu.download.PropertyFileManager;

public class StemQueryMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		StemQueryMap sm = new StemQueryMap();
		
		Map<String , String> queryMap1 = sm.addQueryToMap();
		System.out.println(queryMap1);
	}

	
	public Map<String , String> addQueryToMap()
	{
		
		Map<String , String> queryMap = new HashMap<String , String>();
		
		//String path = "C:/Users/Himanshu/Desktop/IR Project/cacm_stem.query.txt";
		
		String path= PropertyFileManager
				.getProperty("STEMMED_QUERY_FILE");
		
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(path));
			 int queryCounter = 1;
			 while((line = br.readLine())!= null )
			 {
				 
				 String stoppingFlag= PropertyFileManager.getProperty("STOPPING");
					
				 if ("Y".equals(stoppingFlag)) {
					 //Call StopList function
					 line = StopWordsRemoval.removeStopWords(line);
				 }
				 
				queryMap.put(Integer.toString(queryCounter) , line);
				queryCounter++; 
			 }
			 
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		{
			if(br!= null)
			{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		return queryMap;
	}
}
