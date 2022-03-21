package com.manulife.pension.ireports.util.cache;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom McGrady
 *
 */
public class ReflectionCacheManager {

	private static Map reflectionMap = new HashMap();
	private static Map accessorMap = new HashMap();
	private static Map mutatorMap = new HashMap();

	private static final String MUTATOR = "set";
	private static final String ACCESSOR = "get";

	static{
		reflectionMap.put(MUTATOR, mutatorMap);
		reflectionMap.put(ACCESSOR, accessorMap);
	}

	private ReflectionCacheManager() {
	}

	public static Map getMutatorMap(String className) {
		Class klass = null;
		try {
			klass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return getMutatorMap(klass);
	}

	public static Map getMutatorMap(Object object) {
		return getMutatorMap(object == null ? null : object.getClass());
	}
	
	public static Map getMutatorMap(Class klass) {
		Map map = null;
		if (klass != null) {
			map = getMap(klass, MUTATOR);					
		}
		return map;
	}

	public static Map getAccessorMap(String className) {
		Class klass = null;
		try {
			klass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return getAccessorMap(klass);
	}

	public static Map getAccessorMap(Object object) {
		return getAccessorMap(object == null ? null : object.getClass());
	}
	
	public static Map getAccessorMap(Class klass) {
		Map map = null;
		if (klass != null) {
			map = getMap(klass, ACCESSOR);					
		}
		return map;
	}

	private static Map getMap(Class klass, String type) {
		Map methodMap = (Map) reflectionMap.get(type);
		SoftReference softMap = (SoftReference) methodMap.get(klass.getName());
		Map outMap = (softMap == null ? null : (Map) softMap.get());

		if (outMap == null) {				
			Map getMap = new HashMap();
			Map setMap = new HashMap();
			
			//find all setters and getters
			Method[] methods = klass.getMethods();
			for(int i = 0 ; methods != null && i < methods.length ; i++) {
				Method method = methods[i];
				if (method.getName().length() > 3 ) {
					if (method.getName().startsWith(ACCESSOR) && method.getParameterTypes().length == 0) {
						storeMethod(method, getMap);
					} else if (method.getName().startsWith(MUTATOR) && method.getParameterTypes().length == 1) {
						storeMethod(method, setMap);
					}
				}
			}

			synchronized(methodMap) {
				((Map) reflectionMap.get(ACCESSOR)).put(klass.getName(), new SoftReference(getMap));
				((Map) reflectionMap.get(MUTATOR)).put(klass.getName(), new SoftReference(setMap));
			}

			outMap = (MUTATOR.equals(type) ? setMap : getMap);
		}
		
		return outMap;
	}

	private static void storeMethod(Method method, Map map) {
		String fieldName = method.getName().substring(3);
		
		//store Method under "java" name (aaaBbbCcc) ...
		map.put(fieldName.substring(0,1).toLowerCase()+fieldName.substring(1), method);
		
		//... and store Method under "column" name (aaa_bbb_ccc)
		StringBuffer buff = new StringBuffer();
		for(int j = 0; j < fieldName.length(); j++) {						
			char c = fieldName.charAt(j);
			if(Character.isUpperCase(c) && j > 0) {
				buff.append("_");
			}
			buff.append(Character.toLowerCase(c));						
		}
		map.put(buff.toString(), method);
	}

	public static synchronized void dirtyCache(Object object) {
		dirtyCache(object.getClass());
	}
	
	public static synchronized void dirtyCache(Class clazz) {
		dirtyCache(clazz.getName());
	}
	
	public static synchronized void dirtyCache(String clazzName) {
		mutatorMap.remove(clazzName);
		accessorMap.remove(clazzName);
	}
	
	public static synchronized void dirtyCache() {
		mutatorMap.clear();
		accessorMap.clear();
	}
}
