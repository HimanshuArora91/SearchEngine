package edu.neu.corpusgenerate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.neu.download.DownloadPage;
import edu.neu.download.PropertyFileManager;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.URLRecord;
import edu.neu.textProcessor.FileProcessor;

public class GenerateCorpus {
	List<URLRecord> urlsList = null;
	private List <DocumentDetails> listDoc = new ArrayList<DocumentDetails>();
	public void generateCorpus() {

		// getURLMappingFile();asd
		getAllFileNames();
		int count = 1;
		for (URLRecord item : urlsList) {
			/*File input = new File(
					PropertyFileManager.getProperty("HTML_FILE_PATH")
							+ item.getRecordId() + ".txt");*/
			
			File input = new File(
					PropertyFileManager.getProperty("CACM_HTML_FILES")
							+ item.getDocId());

			try {
				
				// String  rawText = FileProcessor.processFile(input);
				String  rawText = FileProcessor.processCACMFile(input);
				
				item.setDocId(item.getDocId().substring(0,item.getDocId().lastIndexOf(".")));
				String fileName = DownloadPage.createCorpusFile(rawText, item);
				item.setDocName(fileName);
				item.setTokenCount(rawText.split("\\s+").length);
				
			} catch (IOException e) {

				System.out.println("Error in reading the HTML File" + item.getRecordId()+".txt");
			} catch (Exception e) {

				System.out.println("SOME EXCEPTION HAS OCCURED");
			}

		}
		
		DownloadPage.createDocIDsFile(urlsList);

	}

	public void getURLMappingFile() {
		urlsList = DownloadPage.getURLFileNamesFromMappingFile();

	}
	
	public void getAllFileNames() {
		urlsList = DownloadPage.getAllHTMLFileNames();

	}

}
