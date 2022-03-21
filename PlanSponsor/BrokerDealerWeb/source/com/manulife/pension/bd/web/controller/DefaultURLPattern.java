package com.manulife.pension.bd.web.controller;


/**
 * Default implementation of URLPattern. If the url matches the pattern
 * as a prefix, it regard the url matches the pattern.
 * 
 * Note: default behavior for psw before refactoring 
 * @author guweigu
 *
 */
public class DefaultURLPattern implements URLPattern {
	@Override
	public int hashCode() {
		return pattern.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof DefaultURLPattern) {
			return pattern.equals(((DefaultURLPattern)o).pattern);
		} else {
			return false;
		}
	}

	private String pattern = "";

	public DefaultURLPattern() {		
	}
	
	public DefaultURLPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public boolean isMatched(String url) {
		return url != null && pattern.length() <= url.length()
				&& url.startsWith(pattern);
	}
}
