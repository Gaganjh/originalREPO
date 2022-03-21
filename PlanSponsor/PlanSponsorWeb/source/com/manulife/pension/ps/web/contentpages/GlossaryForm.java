
package com.manulife.pension.ps.web.contentpages;


import java.io.Serializable;

import com.manulife.pension.ps.web.controller.PsForm;

/**
 * GlossaryForm submitted by the user and contains letter selected for glossary terms.
 * @author 	Mabel Au
 */

 public class GlossaryForm extends PsForm implements Serializable{
	
	protected int letterSelected = 0;


/**
 * Default Constructor
 */
	public GlossaryForm() {
		super();	       			
	}
			

/* getter methods */	
/**
 * Get letterSelected
 * @return int
 */	
	public int getLetterSelected() {
		return letterSelected;
	}	
		
	
/* setter methods */
/**
 * Set letterSelected
 * @param letterSelected - int
 */	
	public void setLetterSelected (int letterSelected) {
		this.letterSelected = letterSelected; 
	}	
}

