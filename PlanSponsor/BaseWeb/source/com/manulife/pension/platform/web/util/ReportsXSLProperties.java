package com.manulife.pension.platform.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * Class to retrieve ReportsXSL.properties file and get value for the given key.
 * 
 * @author
 * 
 */
public class ReportsXSLProperties {
    private static Properties props = new Properties();

    private ReportsXSLProperties()
    {
    }

    static
    {
    	InputStream propertyFileStream = null;
        try
        {
            propertyFileStream = Class.forName(ReportsXSLProperties.class.getName()).
                                getClassLoader().getResourceAsStream(CommonConstants.PROPERTIES_FILE_NAME);
            props.load(propertyFileStream);
        }
        catch (Throwable e)
        {
            SystemException se = new SystemException(e, ReportsXSLProperties.class.getName(),
                                "static", "Static block failed for ReportsXSL.properties");
            throw ExceptionHandlerUtility.wrap(se);
        }finally{
        	try {
        		if(propertyFileStream!= null)
        			propertyFileStream.close();
			} catch (IOException e) {
				SystemException se = new SystemException(e, ReportsXSLProperties.class.getName(),
                        "static", "Static block failed for ReportsXSL.properties");
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
