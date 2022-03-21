package com.manulife.pension.bd.web.controller;

/**
 * A URL pattern.  Subclass can implement different matching algorithm
 * (i.e. regular expression or simple string compare)
 * 
 * @author guweigu
 *
 */
public interface URLPattern {
	/**
	 * Returns wheter the specified url matches this pattern
	 * 
	 * @param url
	 * @return
	 */
	public boolean isMatched(String url);
}
