package com.manulife.pension.service.loan.valueobject.document;

import org.jdom.*;

/**
 * The interface that contains the behavior to make a 
 * value object to be generated as an XML 
 * @author DODDAAN
 */
public interface XMLValueObject {

		/**
		 * generates the XML for a value object
		 * @return
		 */
		public String toXML();
		
		/**
		 * returns specific element
		 * @return
		 */
		public Element getElement(); 

}
