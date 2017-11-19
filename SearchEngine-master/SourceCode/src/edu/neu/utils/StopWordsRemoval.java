package edu.neu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.neu.download.PropertyFileManager;


public class StopWordsRemoval {

	private static final Map<String , Integer> stopList = new HashMap<String , Integer>();
	
	static {
		
		addStopList();
		
		
	}

	
	public static String removeStopWords(String text)
	{
		StringBuffer buffText = new StringBuffer("");
		
		String tokens [] = text.split("\\s+");
		
		for(String token : tokens)
		{
			if(!stopList.containsKey(token))
			{
				buffText.append(token + " ");
			}
		}
		
		return buffText.toString().trim();
		
		
	}
	
	public static Set<String> removeStopWords(Set<String> wordList)
	{
		// wordList.removeAll(new ArrayList<String>(stopList.keySet()));
		
		Set<String> list = new LinkedHashSet<String>();
		
		 for (String word : wordList) {
			 if (!stopList.containsKey(word)) {
				 list.add(word);
			 }
		 }
		 
		 
		Set<String> wordWithoutNumber = new LinkedHashSet<String>(list);
		 for (String word : list) {
			 String pattern = "(\\d+((\\.|\\,)?\\d+)?)";
			 Matcher m = Pattern.compile(pattern).matcher(word);
			 if (m.find()){
				 wordWithoutNumber.remove(word);
			 }
			 
		 }
		 
		 
		 return wordWithoutNumber;
		/*List<String> updatedList = new ArrayList<String>();
		
		for(String token : wordList)
		{
			if(!stopList.containsKey(token))
			{
				updatedList.add(token);
			}
		}
		
		return updatedList;*/
		
		
	}
	
	public static void addStopList()
	{
		
		//String path = "C:/Users/Himanshu/Desktop/IR Project/common_words.txt";
		
		File path = new File(
					PropertyFileManager.getProperty("STOP_WORDS_FILE"));
		
		
		
		String line = null;
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader (new FileReader(path));
			 while((line = br.readLine())!= null )
			 {
				 
				 stopList.put(line, 1);
				 
			 }
			 
			 br.close();
		}
			 
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}
