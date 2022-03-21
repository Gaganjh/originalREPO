package com.manulife.pension.service.loan.valueobject.document;

import org.jdom.output.*;
/**
 * The singleton class that represents the features of XMLOutputter of 
 * JDOM XMLOutputter. This class is used for generating XMLs for LoanDocuments
 *  
 * @author DODDAAN
 */

public class LoanDocXMLOutputter extends XMLOutputter {
		
		/**
		 * represents the singleton instance of the outputter class
		 */
		private static LoanDocXMLOutputter outputter; 
		
		/**
		 * construct that starts constructing the XML output
		 */
		public LoanDocXMLOutputter() { 
			super(Format.getPrettyFormat());
		} 
	
		/**
		 * the method that ensures the creation of single instance
		 * @return LoanDocXMLOutputter
		 */
		public static LoanDocXMLOutputter getInstance(){ 
			if (outputter ==null) 
				outputter = new LoanDocXMLOutputter(); 
			return outputter; 
		} 
}
