package com.aflac.aims.tph.web.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileHandler {

	protected static Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
	public List<String> readFile(String errorFileName){
		 List<String> str_data=new ArrayList<String>();;   
		try
			{
			  
   	    	 Map<String, String> env = System.getenv();
   	         String AIMSDirHome=env.get("AIMS_DIR_HOME");
   	         String Instance=env.get("AIMS_INSTANCE");
   	         
   	       // Open the file that is the first 
				// command line parameter
   	         String ErrorFile=AIMSDirHome+"/data/SITE/"+Instance+"/ftpout/"+errorFileName;
				logger.info("Fetching error from file: " + ErrorFile );
   	         FileInputStream fstream = new 
					FileInputStream(ErrorFile);
				// Convert our input stream to a
				// DataInputStream
				DataInputStream in = 
					new DataInputStream(fstream);
				// Continue to read lines while 
				// there are still some left to read
				while (in.available() !=0)
				{
					// Print file line to screen
					str_data.add(in.readLine());
				}
				in.close();
				return str_data;
			} 
   	    catch(FileNotFoundException e){
   	    	str_data.add("Error File not found");
   	    	return str_data;
   	    }
			catch (Exception e)
			{
		    	return null;
			}
	}
}
