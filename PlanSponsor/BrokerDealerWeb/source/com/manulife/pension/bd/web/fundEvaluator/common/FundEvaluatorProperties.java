package com.manulife.pension.bd.web.fundEvaluator.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;

/**
 * Class to retrieve FundEvaluator.properties file and get value for the given key.
 * @author PWakode
 */
public class FundEvaluatorProperties implements CoreToolConstants{
    private static Properties properties = new Properties();

    //Below static block to initialize property file 
    static{
    	InputStream propertyFileStream = null;
        try{
            propertyFileStream = Class.forName(FundEvaluatorProperties.class.getName()).
                                getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            properties.load(propertyFileStream);
        }
        catch (Throwable e){
            SystemException se = new SystemException(e, 
                                "FundEvaluatorProperties - static: Static block failed for FundEvaluatorProperties.properties");
            throw ExceptionHandlerUtility.wrap(se);
        }finally{
        	try {
        		if(propertyFileStream != null){
    				propertyFileStream.close();
            		}
			} catch (IOException e) {
				SystemException se = new SystemException(e, 
                        "FundEvaluatorProperties - static: Static block failed for FundEvaluatorProperties.properties");
               throw ExceptionHandlerUtility.wrap(se);
			}
        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue){
        return properties.getProperty(key, defaultValue);
    }
}
