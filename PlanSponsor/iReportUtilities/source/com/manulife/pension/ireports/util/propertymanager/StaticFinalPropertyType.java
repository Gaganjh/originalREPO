package com.manulife.pension.ireports.util.propertymanager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StaticFinalPropertyType implements PropertyType {

	private Map values = new HashMap();
	private Set loadedKeys = new HashSet();
	private Class staticClass = null;

	public StaticFinalPropertyType (Class staticClass) {
		this.staticClass = staticClass;
	}
	
	public Object getObjectValue(String key) {
		if(key == null) return null;
		
		
		if(loadedKeys.contains(key)) {
			return values.get(key);
		}
		
		//lazy load
		String newKey = key.trim().toUpperCase();
		newKey = newKey.replace('.','_');
		
		Object value = null;
		try {
			Field field = staticClass.getDeclaredField(newKey);
			
			Class fieldClass = field.getType();			
			if(fieldClass.equals(int.class)) {
				value = new Integer(field.getInt(staticClass));
			} else if(fieldClass.equals(long.class)) {
				value = new Long(field.getLong(staticClass));
			} else if(fieldClass.equals(float.class)) {
				value = new Float(field.getFloat(staticClass));
			} else if(fieldClass.equals(double.class)) {
				value = new Double(field.getDouble(staticClass));
			} else if(fieldClass.equals(boolean.class)) {
				value = new Boolean(field.getBoolean(staticClass));
			} else {
				value = field.get(staticClass);
			}
		} catch(IllegalAccessException e) {			
		} catch(NoSuchFieldException e) {			
		}
		
		if(value!=null) {
			values.put(key,value);
		}
		loadedKeys.add(key);
		
		return value;
	}

	public Set getKeySet(String key) {
		Set matchingKeySet = new HashSet();
		synchronized(this.values) {		
			//find all the overrides
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
			
			// because of lazy load
			String newKey = null;
			if(key !=null) {
				newKey = key.trim().toUpperCase();
				newKey = newKey.replace('.','_');
			}
		
			try {
				Field fields[] = staticClass.getDeclaredFields();
				if(fields != null) {
					for(int i = 0; i < fields.length; i++) {
						if(newKey == null || fields[i].getName().startsWith(newKey)) {
							String matchedKey = fields[i].getName().toLowerCase().replace('_', '.');
							matchingKeySet.add(matchedKey);							
						}
					}
					
				}
			} catch(Exception e) {			
			}
		}
		return matchingKeySet;	
	}

}
