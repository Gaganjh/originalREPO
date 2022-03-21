package com.manulife.pension.bd.web.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manulife.pension.exception.SystemException;

/**
 * The SecurityManager checks the user's authorization for a specified url.
 * 
 * 1. It first checks all the non authentication required WebResource, if it
 * finds a match, the corresponding WebResourceAuthorizationHandler determines
 * the authorization
 * 
 * 2. If (1) fails, it then checks the permission based resources, 3. If (2)
 * fails, it checks the role-base web resources.
 * 
 * The Authorization policy for each WebResource (and its url patterns) are
 * defined by the WebResourceAuthorizationHandler.
 * 
 * @author guweigu
 * 
 */
public class SecurityManager {
	/**
	 * A non role bases WebResource lists
	 */
	private List<WebResource> nonAuthenticationBasedResources;

	private List<WebResource> permissionBasedResources;
	/**
	 * A Map whose key is the user role name, and the value is a list of
	 * WebResource.
	 */
	private Map<String, List<WebResource>> roleBasedResources;

	public boolean isUserAuthorized(AuthorizationSubject subject, String url)
			throws SystemException {
		// 1. Try to find a matching WebResource that contains the url
		// in non-authentication based WebResourceList
		WebResourceAuthorizationHandler handler = getAuthorizationHandler(
				nonAuthenticationBasedResources, url);

		if (handler != null) {
			return handler.isUserAuthorized(subject, url);
		}

		if (subject == null || subject.getUserProfile() == null) {
			return false;
		}

		// 2. Try to find the url in permission based authorization handler
		// if found and the handler allows the access, return true
		// otherwise, continue with role based web resources
		handler = getAuthorizationHandler(permissionBasedResources, url);
		if (handler != null && handler.isUserAuthorized(subject, url)) {
			return true;
		}

		// 3. let the handler decide if the user is authorized.
		List<WebResource> resources = roleBasedResources.get(subject
				.getUserProfile().getAbstractPrincipal().getAbstractUserRole()
				.getRoleId());
		return resources != null && isUserAuthorized(resources, subject, url);
	}

	private boolean isUserAuthorized(List<WebResource> resources,
			AuthorizationSubject subject, String url) throws SystemException {
		for (Iterator<WebResource> it = resources.iterator(); it.hasNext();) {
			WebResource resource = it.next();
			if (resource.contains(url)) {
				if (resource.getAuthorizationHandler().isUserAuthorized(
						subject, url)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Find a matching WebSource for the <code>url</code> and returns its
	 * AuthorizationHandler. It returns null if no matching WebResource is
	 * found.
	 * 
	 * @param resources
	 * @param userProfile
	 * @param url
	 * @return
	 */
	private WebResourceAuthorizationHandler getAuthorizationHandler(
			List<WebResource> resources, String url) {
		for (Iterator<WebResource> it = resources.iterator(); it.hasNext();) {
			WebResource resource = it.next();
			if (resource.contains(url)) {
				return resource.getAuthorizationHandler();
			}
		}
		return null;
	}

	public List<WebResource> getNonAuthenticationBasedResources() {
		return nonAuthenticationBasedResources;
	}

	public void setNonAuthenticationBasedResources(
			List<WebResource> nonAuthenticationBasedResources) {
		Collections.sort(nonAuthenticationBasedResources,
				new Comparator<WebResource>() {
					public int compare(WebResource w1, WebResource w2) {
						int order1 = w1.getOrder();
						int order2 = w2.getOrder();
						if (order1 < order2)
							return -1;
						if (order1 > order2)
							return 1;
						return 0;
					}
				});
		this.nonAuthenticationBasedResources = nonAuthenticationBasedResources;
	}

	public Map<String, List<WebResource>> getRoleBasedResources() {
		return roleBasedResources;
	}

	public void setRoleBasedResources(
			Map<String, List<WebResource>> roleBasedResources) {
		this.roleBasedResources = roleBasedResources;
	}

	public List<WebResource> getPermissionBasedResources() {
		return permissionBasedResources;
	}

	public void setPermissionBasedResources(
			List<WebResource> permissionBasedResources) {
		this.permissionBasedResources = permissionBasedResources;
	}
}
