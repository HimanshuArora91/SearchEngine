package edu.neu.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.comparator.DocFreqComparator;
import edu.neu.comparator.TermFrequencyComparator;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;
import edu.neu.utils.Utils;

public class DocumentFrequencyTableGenerator {

	private static BufferedWriter bw = null;
	private static FileWriter fw;
	private static BufferedReader br = null;
	private int ngrams;
	public DocumentFrequencyTableGenerator(int ngrams) {
		this.ngrams = ngrams;
	}
	public void generateDocFrequencyTable (Map<String, IndexEntry> indexMap) {


		
		List<IndexEntry> list = new ArrayList<IndexEntry>(indexMap.values());
		Utils.calculateTotalFrequency(list);
		Collections.sort(list,new DocFreqComparator());
	
		String fileName = null;
		switch (this.ngrams) {
		case 1 : fileName = "DF_TABLE_One_Gram.txt";
		break;
		case 2 : fileName = "DF_TABLE_Two_Grams.txt";
		break;
		case 3 : fileName = "DF_TABLE_Three_Grams.txt";
		break;
		default : fileName = "DF_TABLE_One_Gram.txt";
		}
		
			
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
				line.append("TERM->DOCUMENT ID\tDOCUMENT FREQUENCY\n");
				for (IndexEntry item : list) {

					line.append(item.getIndexName() + "->" + docIDString(item) + "\t"+ item.getDocs().size() + "\n");
				
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

					if (br != null)
						br.close();
				} catch (IOException ioe) {
					System.out.println("Error occured in closing the Stream");
					// nothing to see here
				}
			}

		
	}
	
	private String docIDString(IndexEntry i) {

		StringBuilder docString= new StringBuilder("");
		if (i!=null && i.getDocs() != null && !i.getDocs().isEmpty()) {
			int length = i.getDocs().size();
			int count=0;
			for (DocumentDetails d : i.getDocs()) {
				count++;
				docString.append("("+ d.getDocID()+ ")" + ((count==length)? "":","));
				
			}
		}
		return docString.toString();
	
		
	}
}
