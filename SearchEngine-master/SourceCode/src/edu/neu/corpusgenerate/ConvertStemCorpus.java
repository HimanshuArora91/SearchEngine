package edu.neu.corpusgenerate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.URLRecord;

public class ConvertStemCorpus {

	Map<String , String> docText= new HashMap<String , String>();
	public static void main(String[] args) {
		
		ConvertStemCorpus cs = new ConvertStemCorpus();
		
		cs.addToMap();
		cs.writeToFile();
	}

	
	public List<URLRecord> writeToFile()
	{
		
		Set<String> docKeys = docText.keySet();
		
		FileWriter finalqueryres = null;
		String path = PropertyFileManager
				.getProperty("STEMMED_CORPUS_LOCATION");
		
		//String path =  "C:/Users/Himanshu/Desktop/IR Project/Stemmed_Corpus/";
		new File(path).mkdir();
		
		List<URLRecord> list = new ArrayList<URLRecord>();
		
		
		for(String doc : docKeys)
		{
			
			String docID = ("0000" + doc).substring(doc.length());
			BufferedWriter br = null;
			try 
			{
				finalqueryres = new FileWriter(path+"CACM-"+docID+".txt");
				 br = new BufferedWriter(finalqueryres);
				 br.write(docText.get(doc));
				 
				 URLRecord rec = new URLRecord();
				 rec.setDocId("CACM-"+docID);
				 rec.setDocName("CACM-"+docID);
				 rec.setTokenCount(docText.get(doc).split("\\s+").length);
				 list.add(rec);		
				 
				 
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally
			{
				if(br != null)
				{
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return list;
		
	}
	public void addToMap()
	{
		//String path = "C:/Users/Himanshu/Desktop/IR Project/cacm_stem.txt";
		String path = PropertyFileManager
				.getProperty("STEMMED_CORPUS_SINGLE_FILE");
		
		String content ="";
		try {
			File corpus = new File(path);
			FileInputStream inp = new FileInputStream(corpus);
			byte[] bcorpus = new byte[(int)corpus.length()];
			inp.read(bcorpus);
			content = new String(bcorpus, "UTF-8");
		
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	
		String temp []  = content.split("#");
		for(String text : temp)
		{
			text = text.trim();
			if(!text.equals(""))
			{
				int index = text.indexOf("\n");
				String a = text.substring(0, index);
				docText.put(a, text.substring(index).trim());	
			}
		}
	//	System.out.println(docText.size());
	//	System.out.println(docText);
	
	}


}
