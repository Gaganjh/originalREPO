package com.manulife.pension.bd.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.Role;

@SuppressWarnings("unchecked")
public class SecurityManagerConfigurator {
	private static final Logger logger = Logger
			.getLogger(SecurityManagerConfigurator.class);

	public final static String WEBRESOURCE_ELEMENT_NAME = "web-resource-name";
	public final static String WEBRESOURCE_ID_ATTRIBUTE = "id";
	public final static String WEBRESOURCE_DESCRIPTION_ATTRIBUTE = "description";
	public final static String WEBRESOURCE_URL_ELEMENT_NAME = "url-pattern";

	public final static String WEBRESOURE_AUTHORIZATION_HANDLER = "authorizationHandler";
	public final static String WEBRESOURE_AUTHORIZATION_CLASS_ATTRIBUTE = "class";
	public final static String WEBRESOURE_AUTHORIZATION_PROPERTY = "property";

	public final static String ROLE_ELEMENT_NAME = "role";
	public final static String ROLE_ID_ATTRIBUTE = "id";
	public final static String ROLE_DESCRIPTION_ATTRIBUTE = "description";
	public final static String ROLE_AVAILABLESERVICE_ELEMENT_NAME = "availableresource";
	public final static String ROLE_TIMEOUT_ATTRIBUTE = "timeout";
	public final static String PUBLIC_SERVICE = "public";
	public final static String INACTIVE_SERVICE = "inactive";

	public SecurityManagerConfigurator() {

	}

	public SecurityManager getSecurityManager(String fileName)
			throws SystemException {
		DOMParser parser = new DOMParser();
		InputStream xmlFileStream = null;
		try {
			xmlFileStream = getClass().getClassLoader()
					.getResourceAsStream(fileName);
			parser.parse(new InputSource(xmlFileStream));
			Document document = parser.getDocument();

			Map<String, WebResource> webResources = loadWebResources(document);
			Map<String, Role> roles = loadRoles(document);

			SecurityManager securityManager = new SecurityManager();
			List<WebResource> nonAuthenticationBased = new ArrayList<WebResource>();
			List<WebResource> permissionBased = new ArrayList<WebResource>();
			Map<String, List<WebResource>> roleBased = new HashMap<String, List<WebResource>>();

			for (Iterator<WebResource> it = webResources.values().iterator(); it
					.hasNext();) {
				WebResource r = it.next();
				WebResourceAuthorizationHandler h = r.getAuthorizationHandler();
				if (!h.needsAuthentication()) {
					nonAuthenticationBased.add(r);
				} else if (h.hasPermission()) {
					permissionBased.add(r);
				}
			}

			// remove the duplicate url pattern in the webresource
			Set<URLPattern> urls = new HashSet<URLPattern>();
			for (Iterator<String> it = roles.keySet().iterator(); it.hasNext();) {
				String roleName = it.next();
				Role role = roles.get(roleName);
				List<WebResource> list = new ArrayList<WebResource>();
				roleBased.put(roleName, list);
				urls.clear();
				for (Iterator<String> sIt = role.getAssociatedServices(); sIt
						.hasNext();) {
					WebResource resource = webResources.get(sIt.next());
					WebResource clone = new WebResource(resource.getId(), resource.getDescription());
					clone.setAuthorizationHandler(resource.getAuthorizationHandler());
					for (Iterator<URLPattern> urlIt = resource.getUrlSet().iterator(); urlIt.hasNext();) {
						URLPattern url = urlIt.next();
						if (!urls.contains(url)) {
							clone.addUrl(url);
							urls.add(url);
						}
					}
					list.add(clone);
				}
			}

			securityManager
					.setNonAuthenticationBasedResources(nonAuthenticationBased);
			securityManager.setPermissionBasedResources(permissionBased);
			securityManager.setRoleBasedResources(roleBased);
			return securityManager;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemException(e, this.getClass().getName(), "loadData",
					"IOException occurred.");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new SystemException(e, this.getClass().getName(), "loadData",
					"SAXException occurred.");
		}finally{
			try {
				if(xmlFileStream!= null)
					xmlFileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new SystemException(e, this.getClass().getName(), "loadData",
						"SAXException occurred.");
			}
		}

	}

	private Map<String, WebResource> loadWebResources(Document document)
			throws SystemException {
		Map<String, WebResource> webResources = new HashMap<String, WebResource>();
		NodeList list = document.getElementsByTagName(WEBRESOURCE_ELEMENT_NAME);
		int len = list.getLength();
		for (int i = 0; i < len; i++) {
			Element webResource = (Element) list.item(i);
			String webResourceId = webResource
					.getAttribute(WEBRESOURCE_ID_ATTRIBUTE);
			String description = webResource
					.getAttribute(WEBRESOURCE_DESCRIPTION_ATTRIBUTE);

			String order = webResource.getAttribute("order");
			NodeList handlers = webResource
					.getElementsByTagName(WEBRESOURE_AUTHORIZATION_HANDLER);
			if (handlers.getLength() != 1) {
				throw new SystemException("Can only have one handler");
			}
			Element handler = (Element) handlers.item(0);

			// List serviceList = new ArrayList(lenUrls);
			WebResource webResourceObj = new WebResource(webResourceId,
					description);
			webResourceObj
					.setAuthorizationHandler(createAuthorizationHandler(handler));
			if (order != null && order.length() > 0) {
				webResourceObj.setOrder(Integer.parseInt(order.trim()));
			}
			
			NodeList urls = webResource
					.getElementsByTagName(WEBRESOURCE_URL_ELEMENT_NAME);
			int lenUrls = urls.getLength();

			for (int j = 0; j < lenUrls; j++) {
				Element urlElement = (Element) urls.item(j);
				URLPattern urlPattern = new DefaultURLPattern(urlElement
						.getFirstChild().getNodeValue());
				webResourceObj.addUrl(urlPattern);
			}

			webResources.put(webResourceId, webResourceObj);
		}

		if (logger.isDebugEnabled())
			logger.debug("webservices=" + webResources);
		return webResources;
	}

	private WebResourceAuthorizationHandler createAuthorizationHandler(
			Element handler) throws SystemException {
		String className = handler
				.getAttribute(WEBRESOURE_AUTHORIZATION_CLASS_ATTRIBUTE);
		try {
			WebResourceAuthorizationHandler handlerObj = (WebResourceAuthorizationHandler) Class
					.forName(className).newInstance();
			NodeList nodes = handler
					.getElementsByTagName(WEBRESOURE_AUTHORIZATION_PROPERTY);
			for (int i = 0, len = nodes.getLength(); i < len; i++) {
				Element property = (Element) nodes.item(i);
				String pName = property.getAttribute("name");
				String pValue = property.getAttribute("value");
				BeanUtils.setProperty(handlerObj, pName, pValue);
			}
			return handlerObj;
		} catch (Exception e) {
			logger.error("Fail to create AuthorizationHandler class = "
					+ className, e);
			throw new SystemException(e,
					"Fail to create AuthorizationHandler class = " + className);
		}
	}

	private Map<String, Role> loadRoles(Document document)
			throws SystemException {
		Map<String, Role> roles = new HashMap<String, Role>();

		NodeList list = document.getElementsByTagName(ROLE_ELEMENT_NAME);
		int len = list.getLength();
		for (int i = 0; i < len; i++) {
			Element role = (Element) list.item(i);
			String roleId = role.getAttribute(ROLE_ID_ATTRIBUTE);
			String description = role.getAttribute(ROLE_DESCRIPTION_ATTRIBUTE);
			String timeout = role.getAttribute(ROLE_TIMEOUT_ATTRIBUTE);

			NodeList availableServices = role
					.getElementsByTagName(ROLE_AVAILABLESERVICE_ELEMENT_NAME);
			int lenAvailableServices = availableServices.getLength();
			Role roleObj = new Role(roleId, description, timeout);
			for (int j = 0; j < lenAvailableServices; j++)
				roleObj.addService(((Element) availableServices.item(j))
						.getFirstChild().getNodeValue());

			roles.put(roleId, roleObj);
		}

		if (logger.isDebugEnabled())
			logger.debug("roles=" + roles);
		return roles;
	}
}
