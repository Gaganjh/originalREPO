package com.manulife.pension.bd.web.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WebResource {

	private String id;
	private String description;
	private WebResourceAuthorizationHandler authorizationHandler;
	private Set<URLPattern> urlSet = new HashSet<URLPattern>();
	private int order;
	
	public WebResource(String id, String description) {
		this.id = id;
		this.description = description;
	} 

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean contains(String url) {
		for (Iterator<URLPattern> it = urlSet.iterator(); it.hasNext(); ) {
			URLPattern pattern = it.next();
			if (pattern.isMatched(url)) {
				return true;
			}
		}
		return false;
	}

	public WebResourceAuthorizationHandler getAuthorizationHandler() {
		return authorizationHandler;
	}

	public void setAuthorizationHandler(WebResourceAuthorizationHandler handler) {
		this.authorizationHandler = handler;
	}

	public void addUrl(URLPattern url) {
		urlSet.add(url);
	}
	
	public Set<URLPattern> getUrlSet() {
		return urlSet;
	}

	public void setUrlSet(Set<URLPattern> urlSet) {
		this.urlSet = urlSet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
