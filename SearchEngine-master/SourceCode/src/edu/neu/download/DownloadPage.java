package edu.neu.download; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.neu.comparator.InlinksComparator;
import edu.neu.comparator.MapValueComparator;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;
import edu.neu.pojos.URLRecord;
import edu.neu.textProcessor.StringProcessor;

public class DownloadPage {

	private static BufferedWriter bw = null;
	private static FileWriter fw;
	private static BufferedReader br = null;

	public static boolean saveDocument(int fileNumber, String docString,
			String filepath) {

		try {

			File file = new File(filepath + fileNumber + ".txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(docString);
			bw.flush();
			return true;

		} catch (FileNotFoundException fnf) {
			System.out.println("File Not Found");

		} catch (IOException ioe) {
			System.out.println("Exception ocured in saving the corpus.");

		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here

			}
		}
		return false;
	}

	public static void saveUrlMappings(List<URLRecord> urlListVisited,
			String fileLocation) {
		try {

			/*
			 * File file = new File( fileLocation+"task1_mapping.txt");
			 */

			File file = new File(fileLocation);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");
			for (URLRecord rec : urlListVisited) {
				line.append(rec.getRecordId()).append("|")
						.append(rec.getUrlString()).append("|")
						.append(rec.getDepth()).append("\n");
			}
			bw.write(line.toString());
			bw.flush();

		} catch (FileNotFoundException fnf) {
			System.out.println("File Not Found while save URL mappings");

		} catch (IOException ioe) {
			System.out.println("Exception ocured in saving the URL Document.");

		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}
	}

	public static List<URLRecord> getURLSFromMappingFile() {
		List<URLRecord> urls = new LinkedList<URLRecord>();

		try {

			String line = null;
			
			String file= PropertyFileManager.getProperty("GRAPH_URL_MAPPING_FILE");
			br = new BufferedReader(
					new FileReader(file));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String s[] = line.split("\\|");
				URLRecord r = new URLRecord();
				r.setUrlString(s[1]);
				r.setUrlLabel(s[1].replaceFirst(
						"https://en.wikipedia.org/wiki/", ""));
				
				
				urls.add(r);
			}
		} catch (FileNotFoundException e) {

			System.out
					.println("URL Mapping File Not found. Please check if it is placed in the correct location");
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

		return urls;
	}

	// Create Graph File

	public static void createGraph(Map<String, List<String>> urlGraph) {
		try {

			String graphFile = PropertyFileManager.getProperty("GRAPH_FILE");
			File file = new File(graphFile);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");

			for (String mapKey : urlGraph.keySet()) {
				line.append(mapKey).append(" ");
				for (String urlItem : urlGraph.get(mapKey)) {
					// if (!urlItem.equals(mapKey)) {
					line.append(urlItem).append(" ");
					// }

				}
				line.append("\n");
			}

			bw.write(line.toString());
			bw.flush();

		} catch (FileNotFoundException fnf) {
			System.out.println("File Not Found while saving the LINK GRAPH");

		} catch (IOException ioe) {
			System.out.println("Exception ocured in saving the URL Document.");

		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}
	}

	public static Map<String, List<String>> getGraphList() {
		Map<String, List<String>> urlGraph = new HashMap<String, List<String>>();
		String line = null;
		try {
			String graphFile = PropertyFileManager.getProperty("GRAPH_FILE");

			br = new BufferedReader(new FileReader(graphFile));

			while ((line = br.readLine()) != null) {

				String s[] = line.split(" ");
				//List<String> list = ;
				List<String> l = new ArrayList<String>(Arrays.asList(s));
				//l.addAll(list);
				l.remove(s[0]);
				urlGraph.put(s[0], l);

			}
		} catch (FileNotFoundException e) {

			System.out
					.println("Graph File Not found. Please check if it is placed in the correct location");
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

		return urlGraph;
	}

	public static void printAllPerplexityValues(List<Double> p) {

		try {
			String fileLocation = PropertyFileManager
					.getProperty("PERPLEXITY_FILE_LOCATION");

			File file = new File(fileLocation+"\\perplexity.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");
			int count = 1;
			line.append("Initial Perplexity :> ").append(p.get(0)).append("\n\n");
			p.remove(0);
			for (Double rec : p) {

				line.append("Perplexity after iteration ").append(count)
						.append(" :> ").append(rec.toString()).append("\n");
				count++;

			}
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("Perplexity File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	}
	
	public static void printSortedPageRank(Map<String, Double> pageRankMap, String fileName) {

		try {
			String fileLocation = PropertyFileManager
					.getProperty("INLINK_PAGE_RANK_FILE_LOCATION");

			File file = new File(fileLocation + fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			// Sorting the Map
			TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(new MapValueComparator(pageRankMap));
			
			
			sortedMap.putAll(pageRankMap);
			
			
			StringBuilder line = new StringBuilder("");
			Double d = 0d;
			for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
				
				line.append(entry.getKey() +" :>  "+entry.getValue()).append("\n");
				d += (entry.getValue());
			}
			line.append("Total:"+d.toString());
			
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("Perplexity File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	}
	
	public static void printSortedInlinks(Map<String,List<String>> pageMap, String fileName) {

		try {
			String fileLocation = PropertyFileManager
					.getProperty("INLINK_PAGE_RANK_FILE_LOCATION");

			File file = new File(fileLocation + fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			for (Map.Entry<String, List<String>> entry: pageMap.entrySet()) {
				
				map.put(entry.getKey(), entry.getValue().size());
			}
			
			// Sorting the Map
			TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(new InlinksComparator(map));
			
			
			sortedMap.putAll(map);
			
			
			StringBuilder line = new StringBuilder("");
			Double d = 0d;
			for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
				
				line.append(entry.getKey() +" :>  "+entry.getValue()).append("\n");
				d += (entry.getValue().doubleValue());
			}
			line.append("Total:"+d.toString());
			
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("Perplexity File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	}
	public static List<URLRecord>  getAllHTMLFileNames() {
		List<URLRecord> urls = new LinkedList<URLRecord>();
		
		File folder = new File(PropertyFileManager.getProperty("CACM_HTML_FILES"));
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("HTML File :" + listOfFiles[i].getName());
		        
		        URLRecord r = new URLRecord();
		        r.setDocId(listOfFiles[i].getName());
		        r.setDocName(listOfFiles[i].getName());
		        urls.add(r);
		        
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		
		return urls;
	}
	// Assignment 3
	public static List<URLRecord> getURLFileNamesFromMappingFile() {
		List<URLRecord> urls = new LinkedList<URLRecord>();

		try {

			String line = null;
			
			String file= PropertyFileManager.getProperty("URL_MAPPING_FILE");
			br = new BufferedReader(
					new FileReader(file));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String s[] = line.split("\\|");
				URLRecord r = new URLRecord();
				r.setUrlString(s[1]);
				r.setUrlLabel(s[1].replaceFirst(
						"https://en.wikipedia.org/wiki/", ""));
				r.setRecordId(Integer.parseInt(s[0]));
				
				
				urls.add(r);
			}
		} catch (FileNotFoundException e) {

			System.out
					.println("URL Mapping File Not found. Please check if it is placed in the correct location");
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

		return urls;
	}
	
	
	/**
	 * Assignment 3
	 * @param rawText
	 * @param record
	 */
	//static Map<String, String> m = new LinkedHashMap<String, String>();
	public static String createCorpusFile(String rawText,URLRecord record) {
		String fileName ="";

		try {
			String fileLocation = PropertyFileManager
					.getProperty("CORPUS_LOCATION");
			 //fileName = StringProcessor.removeAllSpecialCharacters(record.getUrlLabel()) + record.getRecordId();
			 fileName = record.getDocId();
			 File file = new File(fileLocation + fileName + ".txt");

			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(rawText);
			bw.flush();
			System.out.println("Corpus File Created with DOC NAME : "+ fileName);
		} catch (FileNotFoundException e) {

			System.out
					.println("File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}
		return fileName;
	}
	
	
	
	public static void createDocIDsFile(List<URLRecord> records) {

		try {
			String txtFile= PropertyFileManager
					.getProperty("DOC_ID_FILE");

			File file = new File(txtFile);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");
			
			for (URLRecord rec : records) {

				line.append(rec.getDocId() + "|" + rec.getDocName() + "|" + rec.getTokenCount() + "\n");
			
			}
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	}
	
	// create doc ID Map
	
	public static Map <String , DocumentDetails> getDocIDsMap() {
		Map <String , DocumentDetails> docs = new HashMap <String , DocumentDetails>();

		try {

			String line = null;
			
			String file= PropertyFileManager.getProperty("DOC_ID_FILE");
			br = new BufferedReader(
					new FileReader(file));

			while ((line = br.readLine()) != null) {
				DocumentDetails doc= new DocumentDetails();
				String[] s = line.split("\\|");
				doc.setDocID(s[0]);
				doc.setDocName(s[1]);
				// token count
				doc.setTokenCount(Integer.parseInt(s[2]));
				//doc.setDocText(getCurrentdocText());
				docs.put(s[0],doc);
				
			}
		} catch (FileNotFoundException e) {

			System.out
					.println("DOC ID File for creating the map Not Found. Please check if it is placed in the correct location");
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

		return docs;
	}
	
	
	
	
	
	
	public static List<DocumentDetails> getDocIdFile() {
		List<DocumentDetails> docs = new LinkedList<DocumentDetails>();

		try {

			String line = null;
			
			String file= PropertyFileManager.getProperty("DOC_ID_FILE");
			br = new BufferedReader(
					new FileReader(file));

			while ((line = br.readLine()) != null) {
				DocumentDetails doc= new DocumentDetails();
				String[] s = line.split("\\|");
				doc.setDocID(s[0]);
				doc.setDocName(s[1]);
				// token count
				doc.setTokenCount(Integer.parseInt(s[2]));
				//doc.setDocText(getCurrentdocText());
				docs.add(doc);
				
			}
		} catch (FileNotFoundException e) {

			System.out
					.println("DOC ID File Not Found. Please check if it is placed in the correct location");
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

		return docs;
	}
	public static void getCurrentdocText (DocumentDetails doc) {
		try {

			String line = null;
			StringBuilder text = new StringBuilder("");
			
			String fileLocation = "";
			
			
			String stemmingFlag = PropertyFileManager.getProperty("STEMMING");	
			
			if ("Y".equals(stemmingFlag)) {
				 fileLocation = PropertyFileManager
						.getProperty("STEMMED_CORPUS_LOCATION");
				
			}else {
				 fileLocation = PropertyFileManager
						.getProperty("CORPUS_LOCATION");
			}
			
			br = new BufferedReader(
					new FileReader(fileLocation+doc.getDocName() + ".txt"));

			while ((line = br.readLine()) != null) {
				text.append(line + "\n");
				}
			
			doc.setDocText(text.toString());
			
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
	
	public static void createIndexFile (Map<String,IndexEntry> indexMap, String fileName) {
		
		try {
			String fileLocation= PropertyFileManager
					.getProperty("INDEX_FILE_LOCATION");

			File file = new File(fileLocation + fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");
			//line.append("TERM->(DOC_ID,Term_Frequency)...\n\n\n");
			
			for (Map.Entry<String, IndexEntry> item : indexMap.entrySet()) {

				line.append(item.getValue() + "\n");
			
			}
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	}
	
	public static Map <String, IndexEntry> getIndexData(int gram) {
		
		String fileName = null;
		switch (gram) {
		case 1 : fileName = "Index_One_Gram.txt";
		break;
		case 2 : fileName = "Index_Two_Gram.txt";
		break;
		case 3 : fileName = "Index_Three_Gram.txt";
		break;
		default : fileName = "Index_One_Gram.txt";
		}
		
		Map <String, IndexEntry> indexMap = new HashMap<String, IndexEntry>();

		try {

			String line = null;
			
			String file= PropertyFileManager.getProperty("INDEX_FILE_LOCATION");
			br = new BufferedReader(
					new FileReader(file + fileName));

			while ((line = br.readLine()) != null) {
				String[] index = line.split("->");
				IndexEntry e = new IndexEntry();
				e.setIndexName(index[0].trim());
				e.setDocs(StringProcessor.convertToDocList(index[1].trim()));
				e.setDocumentFrequency(e.getDocs().size());
				indexMap.put(index[0].trim(), e);
			}
		} catch (FileNotFoundException e) {

			System.out
					.println("INDEX File Not Found. Please check if it is placed in the correct location");
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
				
			}
		}

		return indexMap;
		
	}
	
	public static void generateQueryResultsFile (Map<String,List<DocumentDetails>> resultMap, String retrievalModel, int noOfDocsToPrint) {

		
		try {
			String fileName= PropertyFileManager
					.getProperty("QUERY_RESULTS_FILE");

			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			StringBuilder line = new StringBuilder("");
			//line.append("TERM->(DOC_ID,Term_Frequency)...\n\n\n");
			
			for (Map.Entry<String, List<DocumentDetails>> eachResult : resultMap.entrySet()) {
				
				List<DocumentDetails> results = eachResult.getValue();
				int rank=1;
				
				for (DocumentDetails d : results) {

					line.append(eachResult.getKey() + "\t" + "Q0" + "\t"+ d.getDocID() + "\t" + (rank++) + "\t"+ d.getScore() + "\t" + retrievalModel + "\n");
					if (rank==noOfDocsToPrint+1) {
						break;
					}
				
				}
				//line.append("\n---------------------------------------------------------------------------------------------------------------- \n\n");
			}
			
			
			
			
			
			
			
			bw.write(line.toString());
			bw.flush();
		} catch (FileNotFoundException e) {

			System.out
					.println("File not found");
		} catch (IOException e) {

			System.out.println("Error Occured in reading the file");
		} catch (Exception e) {

			System.out.println("Some Exception has occured");
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException ioe) {
				System.out.println("Error occured in closing the Stream");
				// nothing to see here
			}
		}

	
	}

}
