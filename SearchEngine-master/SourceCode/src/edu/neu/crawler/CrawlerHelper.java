package edu.neu.crawler;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.neu.download.DownloadPage;
import edu.neu.pojos.CrawlerResponse;

public class CrawlerHelper {
	 private static final String USER_AGENT =
	            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	

	public CrawlerResponse crawlWebPage(String webPageUrl, String keyword) {
		System.out.println(webPageUrl);
		Document doc = null;
		Set<String> urlSet = new LinkedHashSet<String>();
		CrawlerResponse res = new CrawlerResponse();
		try {
			

			Connection con = Jsoup.connect(webPageUrl).userAgent(USER_AGENT);
	
				doc = con.get();
				for (Element url : doc.select("a[href]")) {
					String urlString = url.attr("href");
					String urlLabel = url.text();
					
					//Test This once 
					if (urlString.contains("#")) {
						urlString = urlString.substring(0,urlString.indexOf("#"));
					}

					if (urlString != null
							&& !urlString.contains(":")
							&& urlString.startsWith("/wiki/")
							&& !urlString.equalsIgnoreCase("/wiki/Main_Page"))
						{
						
						
						

						if (keyword!=null && !keyword.isEmpty()){
							if (urlString.toLowerCase().contains(keyword.toLowerCase())
									||
									(urlLabel!=null && !urlLabel.isEmpty() && urlLabel.toLowerCase().contains(keyword.toLowerCase()))) {
								
								urlSet.add("https://en.wikipedia.org" + urlString);
								
								continue;
							}
							
						}
						else {
							urlSet.add("https://en.wikipedia.org" + urlString);
						}
						

					}

				}

			//}

			res.setDocument(doc);
			res.setUrlSet(urlSet);

		//	System.out.println(urlSet);
		//	System.out.println(urlSet.size());

			return res;

			// System.out.println(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
			System.out.println("IO Exception Occured");
			//	e.printStackTrace();
		}
		

		return null;
	}

}
