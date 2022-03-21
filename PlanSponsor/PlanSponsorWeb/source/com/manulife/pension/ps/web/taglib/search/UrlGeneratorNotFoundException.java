package com.manulife.pension.ps.web.taglib.search;

import com.manulife.pension.reference.Reference;


/**
 * This exception is thrown by {@link UrlGeneratorFactory#getGenerator(Reference) getGenerator}
 * when it cannot find a {@link UrlGenerator UrlGenerator}
 * @author unascribed
 * @version 1.0
 */

public class UrlGeneratorNotFoundException extends Exception{

   public UrlGeneratorNotFoundException(String className, String methodName, String message) {
		super("className = "+ className+"\n" +
				"methodName = "+ methodName+"\n" +
				"message = "+ message+"\n");
   }

    
}