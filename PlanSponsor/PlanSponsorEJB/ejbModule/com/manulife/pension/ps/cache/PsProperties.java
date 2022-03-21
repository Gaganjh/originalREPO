package com.manulife.pension.ps.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;

/**
 * A singleton utility to read propeties from ps properties
 */
public class PsProperties
{
   	private static Properties props = new Properties();

	private PsProperties()
	{
	}

   	static
   	{
   		InputStream propertyFileStream = null;
   		try
   		{
	   		propertyFileStream = Class.forName("com.manulife.pension.ps.cache.PsProperties").getClassLoader().getResourceAsStream("./ps.properties");
			props.load(propertyFileStream);
   		}
   		catch (Throwable e)
   		{
   			SystemException se = new SystemException(e, "com.manulife.pension.ps.cache.PsProperties","static","Static block failed for ps.properties");
			throw ExceptionHandlerUtility.wrap(se);
   		}finally{
   			try {
   				if(propertyFileStream!= null)
   					propertyFileStream.close();
			} catch (IOException e) {
				SystemException se = new SystemException(e, "com.manulife.pension.ps.cache.PsProperties","static","Static block failed for ps.properties");
				throw ExceptionHandlerUtility.wrap(se);
			}
   		}

   	}


	public static String get(String key)
	{
		return props.getProperty(key);
	}

	public static String get(String key, String defaultValue)
	{
		return props.getProperty(key, defaultValue);
	}
}

