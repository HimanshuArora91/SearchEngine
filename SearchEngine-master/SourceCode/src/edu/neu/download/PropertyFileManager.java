package edu.neu.download;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileManager {
	private static Properties prop = new Properties();

	public PropertyFileManager()  {
		//InputStream is = getClass().getClassLoader().getResourceAsStream(
		//		"crawler.properties");
		
		InputStream is;
		try {
			String filePath = System.getProperty("propertyFile");
			is = new FileInputStream(filePath);
			
			prop.load(is);
			
		} catch (FileNotFoundException e1) {
			System.out.println("Error occured in loading properties file");
			//e1.printStackTrace();
		}
		catch (IOException e1) {
			System.out.println("Error occured in loading properties file");
			//e1.printStackTrace();
		}
		catch (NullPointerException e1) {
			System.out.println("Error occured in loading properties file");
			//e1.printStackTrace();
		}
		
		
	}

	public void loadPropertiesFile() {

	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

}
