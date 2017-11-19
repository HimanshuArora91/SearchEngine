package edu.neu.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;

public class PrecisionRecall {


	
	public void calcPrecisionRecall(Map <String , List<String>> relevantDocSet , Map <String , ArrayList<String>> retrievedDocSet,Map<String,List<DocumentDetails>> resultMap)
	{
		
			Set<String> querySet = retrievedDocSet.keySet();
			
			Map<String , Double>  avgPrecision = new LinkedHashMap<String, Double>();
			Map<String , Double>  reciprocalrank = new LinkedHashMap <String , Double>();
			Map<String , ArrayList<Double>>  precisionSet = new LinkedHashMap <String , ArrayList<Double>>();
			Map<String , Double>  precisionAt5 = new LinkedHashMap <String , Double>();
			Map<String , Double>  precisionAt20 = new LinkedHashMap <String , Double>();
			Map<String , ArrayList<Double>>  recallSet = new LinkedHashMap <String , ArrayList<Double>>();
			
			double reciprocalRankSum = 0.0;
			for(String query : querySet)
			{
				if (relevantDocSet.get(query)!=null) {
					
				
				ArrayList<Double> tempPrecision = new ArrayList<Double>();
				ArrayList<Double> tempRecall = new ArrayList<Double>();
				
				int noOfRelevantDocs = relevantDocSet.get(query).size();
				
				ArrayList<String> curQueryDocs = new ArrayList<String>();
				ArrayList<String> curQueryRelevantDocs = new ArrayList<String>();
				
				curQueryDocs.addAll(retrievedDocSet.get(query));
				curQueryRelevantDocs.addAll(relevantDocSet.get(query));
				
			
				precisionSet.put(query, tempPrecision);
				recallSet.put(query, tempRecall);
				
				double relCounter = 0.0;
				double docCounter = 1.0;
				double precision = 0.0;
				double recall = 0.0;
				double totalrelprecision = 0.0;
				int flag = 0;
				for(String doc : curQueryDocs)
				{
					
					if(curQueryRelevantDocs.contains(doc))
					{
					
						relCounter++;
					    precision = relCounter/docCounter;
					    recall = relCounter/noOfRelevantDocs;
					    totalrelprecision += precision;
					    if(flag == 0)
					    {
					    	reciprocalrank.put(query, 1/docCounter);
					    	reciprocalRankSum += 1/docCounter; 
					    	flag = 1;
					    }
					}
					else
					{
						precision = relCounter/docCounter;
					}
					
					if(docCounter == 5)
					{
						precisionAt5.put(query, precision);
					}
					
					if(docCounter == 20)
					{
						precisionAt20.put(query, precision);
					}
							
					tempPrecision.add(precision);
					tempRecall.add(recall);
					
					precisionSet.put(query, tempPrecision);
					recallSet.put(query, tempRecall);
					
					
					docCounter++;
				}
				
				avgPrecision.put(query, totalrelprecision/noOfRelevantDocs);
			}
			}
			
			
			
			createResultsWithPrecisionAndRecall(precisionSet,recallSet,resultMap);
			createMeanFile(avgPrecision , reciprocalrank , reciprocalRankSum ,precisionAt5 , precisionAt20);
			
		
			System.out.println(precisionSet);
			System.out.println(recallSet);
			
			System.out.println(avgPrecision);
			System.out.println(reciprocalrank);
			System.out.println(reciprocalRankSum/reciprocalrank.size());
			System.out.println(precisionAt5);
			System.out.println(precisionAt20);
	}
	
	
	
	private void createMeanFile(Map<String , Double>  avgPrecision ,Map<String , Double>  recipocalrank ,double recprocsum  ,Map<String , Double>  precisionAt5,Map<String , Double>  precisionAt20)
	{
		
		StringBuffer s = new StringBuffer("");
		
		String line = null;
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		
			 
			 try
			 {
			 //write to new file
			 
				 String precicionFilepath = PropertyFileManager
							.getProperty("QUERY_RESULTS_FILE_METRICS");
	
					File file = new File(precicionFilepath);
					if (!file.exists()) {
						file.createNewFile();
					}
	
					fw = new FileWriter(file.getAbsoluteFile());
					bw = new BufferedWriter(fw);
				
					 List<String> li = new ArrayList<String>(avgPrecision.keySet());
					// Collections.sort(li);
					 for (String i : li) {
								 
						s.append(i).append("\t").append(avgPrecision.get(i)).append("\t").append(recipocalrank.get(i)).append("\t").append(precisionAt5.get(i)).append("\t").append(precisionAt20.get(i)).append("\n");
						
					 }
					 
					 double sumPrecision = 0.0;
					 double MAP = 0.0;
					 double MRR = 0.0;
					 
					 for(String query: avgPrecision.keySet() )
					 {
						 sumPrecision += avgPrecision.get(query);
						 
					 }
					 
					 MAP = sumPrecision/avgPrecision.keySet().size();
					 MRR = recprocsum/avgPrecision.keySet().size();
					 
					 s.append("Mean average Precision(MAP) =" + MAP).append("\n").append("Mean Reciprocal Rank(MRR) =" + MRR).append("\n");
					 bw.write(s.toString());
					 bw.flush();
						 
				}
			
				
			
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				
				if (br != null)
					br.close();
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here

			}
		}
		
		
		
	}
	
	private void createResultsWithPrecisionAndRecall(Map<String , ArrayList<Double>> precisionSet, Map<String , ArrayList<Double>> recallSet,Map<String,List<DocumentDetails>> resultMap) {

		BufferedWriter bw = null;
		FileWriter fw = null;
		StringBuffer line = new StringBuffer("");
		String retrievalModel = PropertyFileManager
				.getProperty("RETRIEVAL_SYSTEM");
		for (Map.Entry<String , ArrayList<Double>> e : precisionSet.entrySet()) {
			
			List<DocumentDetails> listDoc = resultMap.get(e.getKey());
			List<Double> recallList = recallSet.get(e.getKey());
			int rank=1;
			for (int i=0;i<e.getValue().size();i++) {
				line.append(e.getKey() + "\t" + "Q0" + "\t"+ listDoc.get(i).getDocID() + "\t" + (rank++) + "\t"+ listDoc.get(i).getScore() + "\t" + retrievalModel)
				.append("\t").append(e.getValue().get(i)).append("\t").append(recallList.get(i)).append("\n");
			}
		
			
			
		}		
		try {		
			 //write to new file			 
			 String precisionFilepath = PropertyFileManager
						.getProperty("QUERY_RESULTS_FILE_PREC_RECALL");

				File file = new File(precisionFilepath);
				if (!file.exists()) {
					file.createNewFile();
				}

				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(line.toString());
				bw.flush();
				
			// br.close();
			 
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here

			}
		}
		
	

	
	}
	
	public Map<String , ArrayList<String>>  readRetrievedocs()
	{
		Map<String , ArrayList<String>> docSet = new LinkedHashMap<String , ArrayList<String>>();
		
		
		// String path = "C:/Users/Himanshu/Desktop/IR Project/retres.txt";
		
		String system = PropertyFileManager
				.getProperty("RETRIEVAL_SYSTEM");
		
		
		String path ="";
		if ("LUCENE".equals(system)) {
			path = PropertyFileManager
					.getProperty("LUCENE_RESULTS");
		}
		else {
			path = PropertyFileManager
					.getProperty("QUERY_RESULTS_FILE");
		}
		
		
	
		
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(path));
			 while((line = br.readLine())!= null )
			 {
				 String  temp [] = line.split("\\s+");
				 
				 if(docSet.containsKey(temp[0]))
				 {
					 ArrayList<String> al = new ArrayList<String>();
					 al = docSet.get(temp[0]);
					 al.add(temp[2]);
					 docSet.put(temp[0],al);
				 }
				 else
				 {
					 ArrayList<String> al = new ArrayList<String>();
					 al.add(temp[2]);
					 docSet.put(temp[0], al);
					 
				 }
				 
			 }
			 
			// br.close();
			 
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return docSet;

	}


}
