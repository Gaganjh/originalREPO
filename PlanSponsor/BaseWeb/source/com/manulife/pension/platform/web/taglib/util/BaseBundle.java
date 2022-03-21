package com.manulife.pension.platform.web.taglib.util;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class BaseBundle {
	
	private static Properties properties = null; 
	
	private static HashMap<String, String> cacheMap = new HashMap<String, String> ();
	
	BaseBundle() { 
		loadProperties(properties); 
	}
	
	private static void loadProperties(Properties properties) {
		try {
			
			InputStream is = Thread.currentThread()
					
					.getContextClassLoader()
					
					.getResourceAsStream("com/manulife/pension/ps/web/MyBundle.properties");
			
			properties.load(is);
			
			Set<Entry<Object, Object>> entries = properties.entrySet();
			
			for(Entry<Object, Object> entry: entries) {
				
				cacheMap.put((String)entry.getKey(), (String)entry.getValue());
				
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String getMessage(String key) {
		return cacheMap.get(key);
	}

	public static String getMessage(String key, String arg0) {
		String pattern = cacheMap.get(key);
		return MessageFormat.format(pattern, arg0);
	}

	public static String getMessage(String key, String arg0, String arg1) {
		String pattern = cacheMap.get(key);
		return MessageFormat.format(pattern, arg0, arg1);
	}

}
