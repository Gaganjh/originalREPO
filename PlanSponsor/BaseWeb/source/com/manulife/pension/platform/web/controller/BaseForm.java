 package com.manulife.pension.platform.web.controller;

 import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.util.StaticHelperClass;

/**
 * This is the base for all action forms for PlanSponsor.
 * 
 * @author Ilker Celikyilmaz.
 */
public abstract class BaseForm implements ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String trimString(String value) {
		return value == null ? null : value.trim();
	}

	public String toString() {
		return StaticHelperClass.toString(this);
	}
	
	/**
	 * Safe trims a string
	 */
	public static String trim(String str) {
		return (str == null) ? str : str.trim();
	}
}