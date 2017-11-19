package edu.neu.indexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.download.DownloadPage;
import edu.neu.pojos.DocumentDetails;
import edu.neu.pojos.IndexEntry;
import edu.neu.pojos.URLRecord;

public class IndexCreaterNGrams implements Runnable {

	private final Integer n_gram;
	private String fileName = null;
	private Map<String, IndexEntry> indexMap = new HashMap<String, IndexEntry>();

	public IndexCreaterNGrams(Integer inputGram) {
		this.n_gram = inputGram;
	}

	@Override
	public void run() {
		System.out.println(this.n_gram);
		this.createIndex();
	}

	public void createIndex() {

		switch (n_gram) {
		case 1:
			fileName = "Index_One_Gram.txt";
			break;
		case 2:
			fileName = "Index_Two_Gram.txt";
			break;
		case 3:
			fileName = "Index_Three_Gram.txt";
			break;
		}
		List<DocumentDetails> docFiles = null;

		docFiles = DownloadPage.getDocIdFile();

		// List<DocumentDetails> docFiles = DownloadPage.getDocIdFile();
		if (docFiles != null && !docFiles.isEmpty()) {
			for (DocumentDetails doc : docFiles) {

				DownloadPage.getCurrentdocText(doc);
				NGramsGenerator.createNGrams(doc, n_gram, indexMap);
			}

		}

		DownloadPage.createIndexFile(indexMap, fileName);

		System.out.println("Thread executed");
	}

}
