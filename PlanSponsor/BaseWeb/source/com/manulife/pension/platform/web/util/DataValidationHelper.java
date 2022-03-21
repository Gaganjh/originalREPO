package com.manulife.pension.platform.web.util;

/**
 * Moved from PlanSponsorWeb
 * 
 * This class has utility methods 
 * 	1. to validate a String is null or blank
 *  2. to validate the String is integer
 *  3. to validate the value is in the given range
 *  
 * @author SAyyalusamy
 *
 */
public class DataValidationHelper {
	
	/**
	 * Validates the String is null or blank
	 * Returns TRUE, if the given string is null or blank
	 * Returns FALSE, if the given string is not null and not blank
	 * 
	 * @param text	String
	 * 
	 * @return boolean
	 */
	public static boolean isBlankOrNull(String text) {
		
		if (text == null || text.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Validates the String is an integer
	 * Returns TRUE, if the String can be converted to Integer
	 * Returns FALSE, if the String cannot be converted to Integer
	 * 
	 * @param text	String
	 * 
	 * @return boolean
	 */
	public static boolean isInt(String text) {

  	  	Integer result = null;
		try {
 	      	result = new Integer(text);
		} catch (NumberFormatException e) {
			// return null
		}
       return (result != null);
    }		

	/**
	 * Checks whether the integer value is within the maximum and minimum values
	 * Returns TRUE, if the given integer value is within the given maximum and minimum values
	 * Returns FALSE, if the given integer value is NOT within the given maximum and minimum values
	 * 
	 * @param value	int
	 * @param minValue	int 
	 * @param maxValue	int
	 * 
	 * @return boolean
	 */
	public static boolean isInRange(int value, int minValue, int maxValue) {
		
		if (value>=minValue && value<=maxValue) {
			return true;
		} 
		
		return false;
	}		

}

