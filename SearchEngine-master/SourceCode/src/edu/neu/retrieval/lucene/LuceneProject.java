package edu.neu.retrieval.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.neu.download.PropertyFileManager;

/**
 * To create Apache Lucene index in a folder and add files into this index based
 * on the input of the user.
 */
public class LuceneProject {
    //private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
    //private static Analyzer sAnalyzer = new SimpleAnalyzer(Version.LUCENE_47);
    private static Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_47);

    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();

    public static void main(String[] args) throws IOException {
    	
    	new PropertyFileManager();
	System.out
		.println("Enter the FULL path where the index will be created: (e.g. /Usr/index or c:\\temp\\index)");

	String indexLocation = null;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String s = br.readLine();

	LuceneProject indexer = null;
	try {
	    indexLocation = s;
	    indexer = new LuceneProject(s);
	} catch (Exception ex) {
	    System.out.println("Cannot create index..." + ex.getMessage());
	    System.exit(-1);
	}

	// ===================================================
	// read input from user until he enters q for quit
	// ===================================================
	String bkppath = null;
	while (!s.equalsIgnoreCase("q")) {
	    try {
		System.out
			.println("Enter the FULL path to add into the index (q=quit): (e.g. /home/mydir/docs or c:\\Users\\mydir\\docs)");
		System.out
			.println("[Acceptable file types: .xml, .html, .html, .txt]");
		s = br.readLine();
		if (s.equalsIgnoreCase("q")) {
		    break;
		}

		 bkppath = s;
		// try to add file into the index
		indexer.indexFileOrDirectory(s);
	    } catch (Exception e) {
		System.out.println("Error indexing " + s + " : "
			+ e.getMessage());
	    }
	}

	// ===================================================
	// after adding, we always have to call the
	// closeIndex, otherwise the index is not created
	// ===================================================
	indexer.closeIndex();

	// =========================================================
	// Now search
	// =========================================================
	
	Map <String ,String> queryMap = createQueryList();
	
	
	s = "";
	int ind = 0;
	FileWriter finalqueryres = null;
	
	try {
		//todo
	
		String path= PropertyFileManager
				.getProperty("INDEX_FILE_LOCATION");
		finalqueryres = new FileWriter(path);
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	BufferedWriter br5 = new BufferedWriter(finalqueryres);
	
	for(String  queryID : queryMap.keySet())
	{
		String s1 = queryMap.get(queryID);
		System.out.println(s1);
		try {
		
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
					indexLocation)));
				IndexSearcher searcher = new IndexSearcher(reader);
				TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
				
		Query q = new QueryParser(Version.LUCENE_47, "contents",
			analyzer).parse(s1);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		
		
		for (int i = 0; i < hits.length; ++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    String temp = d.get("path");
		    temp = temp.replace(bkppath, "");
		    temp = temp.substring(1);
		    temp = temp.replace(".html" , "");
		    temp = temp.replace(".txt" , "");
		    System.out.println((i + 1) + ". " + d.get("path")
			    + " score=" + hits[i].score);
		    br5.write(queryID + "\t" + "Q0 " + temp + "\t" + (i + 1) + "\t" + hits[i].score + "\t" + "LUCENE" + "\r\n");
		}
		ind++;
		
		
		// 5. term stats --> watch out for which "version" of the term
		// must be checked here instead!
		Term termInstance = new Term("contents", s1);
		long termFreq = reader.totalTermFreq(termInstance);
		long docCount = reader.docFreq(termInstance);
		System.out.println(s1 + " Term Frequency " + termFreq
			+ " - Document Frequency " + docCount);

	    } catch (Exception e) {
		System.out.println("Error searching " + s1 + " : "
			+ e.getMessage());
		break;
	    }

	}
	
	try {
		br5.flush();
		br5.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

    }

    /**
     * Constructor
     * 
     * @param indexDir
     *            the name of the folder in which the index should be created
     * @throws java.io.IOException
     *             when exception creating index.
     */
    LuceneProject(String indexDir) throws IOException {

	FSDirectory dir = FSDirectory.open(new File(indexDir));

	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,
		analyzer);

	writer = new IndexWriter(dir, config);
    }

    /**
     * Indexes a file or directory
     * 
     * @param fileName
     *            the name of a text file or a folder we wish to add to the
     *            index
     * @throws java.io.IOException
     *             when exception
     *             
     */
    
    
public static Map<String,String> createQueryList() {
		
		Map<String,String> map = new LinkedHashMap<String, String>();
		//CACM Query Files
	  //  File queryFile = new File("C:/Users/Himanshu/Desktop/IR Project/cacm-query.query");
	    
	    File queryFile = new File(
				PropertyFileManager.getProperty("QUERY_FILE_LOCATION"));
		org.jsoup.nodes.Document doc;
		try {
			doc = Jsoup.parse(queryFile, "UTF-8");
			Elements queryList = doc.select("DOC");
			
			
			for (Element e : queryList) {
				
				String qId = e.select("DOCNO").text();
				String t = e.text();
				String processedQuery = processString(t.substring(t.indexOf(" ")+1));
				
				map.put(qId, processedQuery);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;
	}

    public static String processString(String str) {
		
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\t", " ");
		str = str.toLowerCase();

		StringBuilder s = new StringBuilder("");
		String pattern = "(\\b(\\d*)?[a-zA-Z]+(\\d+)?(\\-[a-zA-Z]+|\\-\\d+)?(\\d*|[a-zA-Z]*)?\\b|\\d+((\\.|\\,)?\\d+)?|([a-zA-Z]+\\-[a-zA-Z]+))";
		Matcher m = Pattern.compile(pattern).matcher(str);
		while (m.find()) {
			s.append(m.group(1) + " ");
		}

		return s.toString().trim();
	}

    public void indexFileOrDirectory(String fileName) throws IOException {
	// ===================================================
	// gets the list of files in a folder (if user has submitted
	// the name of a folder) or gets a single file name (is user
	// has submitted only the file name)
	// ===================================================
	addFiles(new File(fileName));

	int originalNumDocs = writer.numDocs();
	for (File f : queue) {
	    FileReader fr = null;
	    try {
		Document doc = new Document();

		// ===================================================
		// add contents of file
		// ===================================================
		fr = new FileReader(f);
		doc.add(new TextField("contents", fr));
		doc.add(new StringField("path", f.getPath(), Field.Store.YES));
		doc.add(new StringField("filename", f.getName(),
			Field.Store.YES));

		writer.addDocument(doc);
		System.out.println("Added: " + f);
	    } catch (Exception e) {
		System.out.println("Could not add: " + f);
	    } finally {
		fr.close();
	    }
	}

	int newNumDocs = writer.numDocs();
	System.out.println("");
	System.out.println("************************");
	System.out
		.println((newNumDocs - originalNumDocs) + " documents added.");
	System.out.println("************************");

	queue.clear();
    }

    private void addFiles(File file) {

	if (!file.exists()) {
	    System.out.println(file + " does not exist.");
	}
	if (file.isDirectory()) {
	    for (File f : file.listFiles()) {
		addFiles(f);
	    }
	} else {
	    String filename = file.getName().toLowerCase();
	    // ===================================================
	    // Only index text files
	    // ===================================================
	    if (filename.endsWith(".htm") || filename.endsWith(".html")
		    || filename.endsWith(".xml") || filename.endsWith(".txt")) {
		queue.add(file);
	    } else {
		System.out.println("Skipped " + filename);
	    }
	}
    }

    /**
     * Close the index.
     * 
     * @throws java.io.IOException
     *             when exception closing
     */
    public void closeIndex() throws IOException {
	writer.close();
    }
}