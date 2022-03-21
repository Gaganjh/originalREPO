package com.manulife.pension.ireports.report.utility;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.manulife.pension.ireports.util.propertymanager.FilePropertyType;
import com.manulife.pension.ireports.util.propertymanager.PropertyType;
import com.manulife.pension.ireports.util.propertymanager.SystemPropertyType;





/**
 * 
 */

public class PropertyManager {

	public static final String FILE_PROPERTY_FILENAME_SYSTEM_PROPERTY_KEY = "fileproperty.filename";
    public static final String PDF_REPORTS_PROP_FILE_KEY = "pdf_reports.filename";
	private static final String PDF_REPORTS_PROP_FILE = "pdf_reports.properties";	
    
	private static PropertyManager propertyManager = null;
//	private final Map propertyTypes = new HashMap();
	private final List types = new ArrayList();
	private final Map mutableProps = new HashMap();

	/** Private constructor. Use as singleton
	 */
	protected PropertyManager() {
		super();
	}
	
	/** This method creates a new instance of the PropertyManager if not already instantiated.
	 * @return instance of property manager
	 */
	public static PropertyManager getInstance() {
		if (propertyManager == null) {			
			synchronized(PropertyManager.class) {
				if(propertyManager == null) {
					propertyManager = new PropertyManager();		
					//load up defaults
					propertyManager.resetDefaults();							
				}
			}
		}
		return propertyManager;
	}
	
	/** private instance version of resetting properties
	 */
	public void resetDefaults() {		
		mutableProps.clear();
		types.clear();
			
		//load up the non database properties first		
		types.add(new SystemPropertyType(""));		

		String filePropertyFile = (String)getObjectValue(FILE_PROPERTY_FILENAME_SYSTEM_PROPERTY_KEY, null);
		if(filePropertyFile != null) {
			try {
				types.add(new FilePropertyType(filePropertyFile));					
			} catch(IOException x) {
				throw new IllegalArgumentException("PropertyManager. Problems loading File properties:" + filePropertyFile + " Exception:" + x.getMessage());
			}
		}
		
		
		//load pdf_reports.properties
		String pdfReportsPropertyFile = (String)getObjectValue(PDF_REPORTS_PROP_FILE_KEY, PDF_REPORTS_PROP_FILE);
		try {		
			types.add(new FilePropertyType(pdfReportsPropertyFile));
		} catch(IOException x) {
			throw new IllegalArgumentException("PropertyManager. Problems loading File properties:" + pdfReportsPropertyFile + " Exception:" + x.getMessage());
		}		
	}
	
	/** Sets a int value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setInt(String key, int value) {
		getInstance().setObjectValue(key,new Integer(value));
	}
	/** Sets a long value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setLong(String key, long value) {
		getInstance().setObjectValue(key,new Long(value));
	}
	/** Sets a boolean value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setBoolean(String key, boolean value) {
		getInstance().setObjectValue(key,new Boolean(value));
	}
	
	/** Sets a String value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setString(String key, String value) {
		getInstance().setObjectValue(key,value);
	}
	
	/** Sets a BigDecimal value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setBigDecimal(String key, BigDecimal value) {
		getInstance().setObjectValue(key,value);
	}
	
	/** Sets an Object value as a property
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	public static void setObject(String key, Object value) {
		getInstance().setObjectValue(key,value);
	}
	
	/** Private instance specific object setter.
	 * Will throw an IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param value
	 */
	private void setObjectValue(String key, Object value) {
		if (value == null) mutableProps.remove(key);
		else mutableProps.put(key, value);
	}
	
	private static Object convertObject(String key, Class targetClass, Object defaultObject) {
		Object obj = getInstance().getObjectValue(key, defaultObject);
		if(obj==null) return null;
		Object value=null;
		if(targetClass.isAssignableFrom(obj.getClass())) {
			value = obj;
		} else {
			Constructor constructor = null;
			Object[] ctorParms = new Object[1];
			try {
				//attempt to look for a ctor that takes the targetClass				
				Class[] param = {targetClass};
				constructor = targetClass.getConstructor(param);
				ctorParms[0] = obj;
				
			} catch(NoSuchMethodException nsmx) {
				//as a last resort try for a ctor that takes a string
				
				try {
					Class[] strParam = {String.class};
					constructor = targetClass.getConstructor(strParam);
					ctorParms[0] = obj.toString();
				} catch(Exception x) {
					throw new IllegalStateException("Cannot convert Property to an " + targetClass.getName() +". "
							+" Property:" + key + " Object:"+ obj.getClass().getName() 
							+ " Exception:" + x.getMessage());
				}
				
			}
			try {
				value = constructor.newInstance(ctorParms);
			} catch(Exception x) {
				throw new IllegalStateException("Cannot convert Property to an " + targetClass.getName() +". "
						+" Property:" + key + " Object:"+ obj.getClass().getName() 
						+ " Exception:" + x.getMessage());
				
			}
		}
		return value;
	}

	
	/** Gets an int value 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static int getInt(String key) {
		Integer i = (Integer)convertObject(key, Integer.class, null);
		if (i == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return i.intValue();				
	}
	
	/** Gets a long value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static int getInt(String key, int defaultValue) {
		Integer i = (Integer)convertObject(key, Integer.class, new Integer(defaultValue));
		return i.intValue();				
	}
	
	/** Gets a long value 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static long getLong(String key) {
		Long l = (Long)convertObject(key, Long.class, null);
		if (l == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return l.longValue();				
	}
	/** Gets an int value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static long getLong(String key, long defaultValue) {
		Long l = (Long)convertObject(key, Long.class, new Long(defaultValue));
		return l.longValue();				
	}
		
	/** Gets a boolean value 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static boolean getBoolean(String key) {
		Boolean b = (Boolean)convertObject(key, Boolean.class, null);
		if (b == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return b.booleanValue();				
	}
	/** Gets an boolean value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		Boolean b = (Boolean)convertObject(key, Boolean.class, new Boolean(defaultValue));
		return b.booleanValue();				
	}

	/** Gets a String value 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static String getString(String key) {
		String s = (String)convertObject(key, String.class, null);
		if (s == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return s;						
	}	
	/** Gets a String value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static String getString(String key, String defaultValue) {
		String s = (String)convertObject(key, String.class, defaultValue);
		return s;						
	}
	
	/** Gets a BigDecimal value 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static BigDecimal getBigDecimal(String key) {
		BigDecimal b = (BigDecimal)convertObject(key, BigDecimal.class, null);
		if (b == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return b;								
	}	
	/** Gets a BigDecimal value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		BigDecimal b = (BigDecimal)convertObject(key, BigDecimal.class, defaultValue);
		return b;								
	}
	
	/** Gets an Object 
	 * Will throw an IllegalStateExceptioin if int not found. IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @return int value
	 */
	public static Object getObject(String key) {
		Object value = getInstance().getObjectValue(key, null);
		if (value == null) throw new IllegalStateException("PropertyManager: Property not found for key:"+key);
		return value;
	}
	
	/** Gets an Object value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	public static Object getObject(String key, Object defaultValue) {
		return getInstance().getObjectValue(key, defaultValue);
	}
	
	
	/** Private: Gets an Object value. If not found returns default value
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param key
	 * @param default
	 * @return int value
	 */
	private Object getObjectValue(String key, Object defaultValue) {
		Object value = mutableProps.get(key);
		
		for (Iterator i = types.iterator(); value == null && i.hasNext();) {
			PropertyType type = (PropertyType)i.next();
			value = type.getObjectValue(key);
		}
		
		if (value == null) {
			value = defaultValue;
		}
		
		value = replaceEmbeddedProperties(value);
		
		return value;
	} 
	
	/**
	 * Allow for replaceable tokens in property values.
	 * @param value value to look for embedded properties in. 
	 * @return value with embedded properties replaced.
	 */
	private Object replaceEmbeddedProperties(Object value) {
		if (!(value instanceof String)) {
			return value;
		}
		String valueString = (String)value;
		
		StringBuffer buff=new StringBuffer();
		String prefix="?{";
		String suffix="}";
		int pos=0;
		int i=valueString.indexOf(prefix,pos);
		while(i>=0) {
			if(pos<i) {
				buff.append(valueString.substring(pos,i));
			}
			int endindex = valueString.indexOf(suffix,i+prefix.length());
			String embeddedProperty = valueString.substring(i+prefix.length(), endindex);
			buff.append(PropertyManager.getString(embeddedProperty));
			pos = i + prefix.length() + embeddedProperty.length() + suffix.length();
			i = valueString.indexOf(prefix, pos);
		}
		if(pos < valueString.length()) {
			buff.append(valueString.substring(pos));
		}
		return buff.toString();		
	}

	/** Gets a list of keys matching the key prefix
	 * Will throw a IllegalArgumentException if key not associated with Property Type
	 * @param keyPrefix
	 * @return Set
	 */
	public static Set getKeySet(String key) {
		return getInstance().getKeySetMatchingPrefix(key);
	}

	public static Properties getProperties(String prefix) {
		Set set = getKeySet(prefix);
		Properties props = new Properties();
		for (Iterator i = set.iterator(); i.hasNext(); ) {
			String name = (String)i.next();
			Object value = getObject(name, null);
			if (value != null)
				props.setProperty(name, value.toString());
		}
		return props;
	}

	private Set getKeySetMatchingPrefix(String keyPrefix) {
		Set keySet = new TreeSet();
		Set mutableKeys = mutableProps.keySet();
		for (Iterator i = mutableKeys.iterator(); i.hasNext(); ) {
			String key = (String)i.next();
			if (key!=null && (keyPrefix == null || key.startsWith(keyPrefix)))
				keySet.add(key);
		}
		for (Iterator i = types.iterator(); i.hasNext(); ) {
			PropertyType type = (PropertyType)i.next();
			keySet.addAll(type.getKeySet(keyPrefix));
		}
		return keySet;		
	}

	/** Gets a map of all current properties
	 * @return Map
	 */
	public static Map getAllProperties() {
		return getInstance().getAllPropertyValues();
	}

	private Map getAllPropertyValues() {
		Map map = new TreeMap();
		Collection keys = getKeySetMatchingPrefix(null);
		for(Iterator itr = keys.iterator(); itr.hasNext(); ) {
			String key = (String)itr.next();
			Object value = PropertyManager.getObject(key);
			map.put(key, value);
		}
		return map;
	}
}