package com.manulife.pension.platform.web.fap.customquery.util.propertymanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class FilePropertyType implements PropertyType {

	private Map values = new TreeMap();
	private String propertyFilename = null;

	public FilePropertyType (String propertyFilename) throws FileNotFoundException, IOException {
		this.propertyFilename = propertyFilename;
		
		loadProperties();
	}
	
	private void loadProperties() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		//try classpath first
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(this.propertyFilename);
		try {
			if(in == null) {
				//try a filename
				File propFile = new File(propertyFilename);
				in = new FileInputStream(propFile);				
			}
			props.load(in);
			values.putAll(props);
		} finally {
			if(in != null) in.close();
		}
	}

	public Object getObjectValue(String key) {
		if(key == null) return null;
		
		
		if(values.containsKey(key)) {
			return values.get(key);
		}
		return null;	
	}


	public Set getKeySet(String key) {
		Set matchingKeySet = new TreeSet();
		synchronized(this.values) {		
			//overrides
			Set currentKeySet = this.values.keySet();
			if(currentKeySet != null) {
				Iterator itr = currentKeySet.iterator();
				while(itr.hasNext()) {
					String currentKey = (String)itr.next();
					if(key == null || currentKey.startsWith(key)) {
						matchingKeySet.add(currentKey);
					}
				}
			}

		}
		return matchingKeySet;	
	}


}

