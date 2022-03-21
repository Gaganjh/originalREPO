package com.manulife.pension.ireports.util.propertymanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SystemPropertyType implements PropertyType {

	private Map values = new HashMap();
	private Set loadedKeys = new HashSet();
//	private Class staticClass = null;
	private String systemPropertyPrefix = null;

	public SystemPropertyType (String prefix) {
		systemPropertyPrefix = prefix;
	}

	public Object getObjectValue(String key) {
		if(key == null) return null;
		
		if(loadedKeys.contains(key)) {
			return values.get(key);
		}
	
		//we can only get Strings from the system
		Object value = System.getProperty(systemPropertyPrefix + key);
				
		if(value != null) {
			values.put(key,value);
		}
		loadedKeys.add(key);
		
		return value;
	}


	public Set getKeySet(String key) {
		Set matchingKeySet = new HashSet();
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

			//lazy load
			Properties props = System.getProperties();			
			Iterator itr = props.keySet().iterator();
			while(itr.hasNext()) {
				String loadedKey = (String)itr.next();
				if((key == null && loadedKey.startsWith(systemPropertyPrefix))
				|| (key !=null && loadedKey.startsWith(systemPropertyPrefix + key))) {
					matchingKeySet.add(loadedKey.substring(systemPropertyPrefix.length()));
				}
			}
		}
		return matchingKeySet;	
	}

}
