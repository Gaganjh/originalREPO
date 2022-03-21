package com.manulife.pension.ps.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.Role;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.AccessLevelUtility;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class will act as the Manager to
 * verify if the access to the public or secure url
 * is allowed. It is implemented as singleton and
 * it loads the "securityinfo.xml" whic has all the
 * definitions to webresources and roles.
 *
 * @author Ilker Celikyilmaz
 */
public class SecurityManager {
	public final static String WEBRESOURCE_ELEMENT_NAME = "web-resource-name";
	public final static String WEBRESOURCE_ID_ATTRIBUTE = "id";
	public final static String WEBRESOURCE_DESCRIPTION_ATTRIBUTE = "description";
	public final static String WEBRESOURCE_URL_ELEMENT_NAME = "url-pattern";
	public final static String WEBRESOURCE_ALLOWED_PRODUCT_TYPES_ATTRIBUTE = "productypes";
	public final static String WEBRESOURCE_ALLOWED_CONTRACT_STATUSES_ATTRIBUTE = "contractstatuses";
	public final static String WEBRESOURCE_CONTRACT_REQUIRED_ATTRIBUTE = "contractrequired";
	public final static String WEBRESOURCE_CONTRACT_PERMISSIONS_REQUIRED_ATTRIBUTE = "contractpermissionsrequired";
	public final static String WEBRESOURCE_ALLOWED_SITES_ATTRIBUTE = "sites";
	public final static String WEBRESOURCE_PERMISSIONS_ELEMENT_NAME = "permissions";
	public final static String WEBRESOURCE_SELECT_ACCESS_CHECK_REQUIRED_ATTRIBUTE = "selectedaccesscheckrequired";

	public final static String ROLE_ELEMENT_NAME = "role";
	public final static String ROLE_ID_ATTRIBUTE = "id";
	public final static String ROLE_DESCRIPTION_ATTRIBUTE = "description";
	public final static String ROLE_AVAILABLESERVICE_ELEMENT_NAME = "availableresource";
	public final static String ROLE_TIMEOUT_ATTRIBUTE = "timeout";
	public final static String PUBLIC_SERVICE = "public";
	public final static String INACTIVE_SERVICE = "inactive";
	public static final String MANAGE_INTERNAL ="MANAGE_INTERNAL";
	public static final String MANAGE_EXTERNAL ="MANAGE_EXTERNAL";
	public static final String MANAGE_TPA ="MANAGE_TPA";

	private Logger logger = null;

	private static SecurityManager instance = null;
	static {
		try {
			instance = new SecurityManager();
		} catch (SystemException e) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
		}
	}
	private InputStream xmlFileStream = getClass().getClassLoader().getResourceAsStream("./securityinfo.xml");
	private Map<String, WebResource> webResources = new HashMap<String, WebResource>();
	private Map<String, Role> roles = new HashMap<String, Role>();
	private Map<String, Set<URLPattern>> availableURLsForRoles = new HashMap<String, Set<URLPattern>>();

	private SecurityManager() throws SystemException
	{
		logger = Logger.getLogger(this.getClass());
		loadData();
	}

	public static SecurityManager getInstance()
	{
		return instance;
	}
	/**
	 * This method is used to to verify if user is authorized to access the requested url. If a url
     * is defined for a role, only the first entry for each url is processed, subsequent entries
     * for the same role are discarded.
	 *
	 * @param user
	 * 		Reference to the current user that makes the request
	 * @param reuestedUrl
	 * 		The requested url that needs to be authenticated.
	 * @return
	 * 		Returns true if the requestedUrl is allowed otherwise false.
	 */
	public boolean isUserAuthorized(UserProfile user, String requestedUrl)
	{
		// First of all, if the URL is public, we can access it
		if (isPublicUrl(requestedUrl)) {
			return true;
		}

		if ( requestedUrl == null || requestedUrl.trim().length() == 0 || user.getPrincipal().getRole() == null )
			return false;

			String roleId = user.getPrincipal().getRole().toString();
			Contract contract = user.getCurrentContract();

			if(logger.isDebugEnabled())
			{
				logger.debug("User role= "+user.getPrincipal().getRole()+" and contract= "+contract);
			}

			// First let see if its permission based
			URLPattern aURLPattern = getURLPattern(requestedUrl);
			boolean hasPermissionToAccess = aURLPattern!=null && user.getRole().hasPermissionExist( aURLPattern.getAllowedPermissions() );
			if (hasPermissionToAccess == true) {
				// Permission based may still require a contract and statuses may still be required,
				// if so, verify that we have access to it.
				// Only return if contract is not required
				// or if contract is required and our access is auhtorized to it
				if (aURLPattern == null || !aURLPattern.isContractRequired() ||
						(aURLPattern.isContractRequired() && isUserAuthorized(user, aURLPattern))) {
					return true;
				}
			}

			// Continue on roles based authorization

			// We have to go over the url's that available to the role
			// and make left comparison
			Object role = availableURLsForRoles.get(roleId);
			if ( role != null )
			{
				Set availableUrls = (Set)role;
				for (Iterator it=availableUrls.iterator(); it.hasNext();)
				{
					URLPattern urlPattern = (URLPattern)it.next();
					if ( isRequestedURLMatch(urlPattern.getUrl(), requestedUrl) )
					{
						boolean authorized = isUserAuthorized(user, urlPattern);
						if (authorized)
							return true;
					}
				}

		}


		return false;
	}

	/**
	 * Returns URLPattern that corresponds to requested Url
	 *
	 * @param requestedUrl
	 * @param roleId
	 * @return
	 */
	private URLPattern getURLPattern( String requestedUrl ) {
		//Iterate over the webresources defined
		Iterator webResourcesIterator = webResources.entrySet().iterator();
		while (webResourcesIterator.hasNext()) {
			Map.Entry item = (Map.Entry)webResourcesIterator.next();
			WebResource aWebResource = (WebResource)  item.getValue();
			// only go deeper if this set has permissions assigned at all
			if (aWebResource.hasPermissions()) {
				// If this resource is permission based iterate over patterns to find out requestedUrl
				Iterator urlPatternIterator = aWebResource.getAssociatedURLs().iterator();
				while (urlPatternIterator.hasNext()) {
					URLPattern urlPattern = (URLPattern)urlPatternIterator.next();
					if ( isRequestedURLMatch(urlPattern.getUrl(), requestedUrl) ) {
						return urlPattern;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Verifies whether the user can access the specific URL, including contract statuses checking
	 * @param user
	 * @param contract
	 * @param urlPattern
	 */
	private boolean isUserAuthorized(UserProfile user, URLPattern urlPattern) {

		if (urlPattern.isSelectedAccessCheckRequired() && user.getRole().hasPermission(PermissionType.SELECTED_ACCESS))
		{
			return false;
		}

		//unallocated contracts do not have status
		Contract contract = user.getCurrentContract();
		if ( urlPattern.isContractRequired() &&
			 contract != null 
			  &&
             urlPattern.isProductTypeAllowed(/*contract.isContractAllocated(), */contract.isDefinedBenefitContract()) &&             
			 (   (urlPattern.isContractStatusAllowed(contract.getStatus())/* &&
			      contract.isContractAllocated()) ||
			      !contract.isContractAllocated() */ ) &&
			 isRequiredContractPermissionExists(user, urlPattern) &&
			 urlPattern.isSiteAllowed(Environment.getInstance().getSiteLocation()) ))   {
			if (urlPattern.isPermissionRequired()) {
				return user.getRole().hasPermissionExist( urlPattern.getAllowedPermissions() );
			} else {
				return true;
			}
		} else if ( !urlPattern.isContractRequired() &&
		 		   urlPattern.isSiteAllowed(Environment.getInstance().getSiteLocation()) ) {
			if (urlPattern.isPermissionRequired()) {
				return user.getRole().hasPermissionExist( urlPattern.getAllowedPermissions() );
			} else {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method is verify if the requested url is exists in public service.
	 *
	 * @param reuestedUrl
	 * 		The requested url that needs to be verified if it is public.
	 * @return
	 * 		Returns true if the requestedUrl is public. If requested url is
	 * null, empty, or is not public returns false.
	 */
	public boolean isPublicUrl(String requestedUrl)
	{
		if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
			return false;

		// We have to go over the public url's and make left compare
		WebResource publicService = (WebResource)webResources.get(PUBLIC_SERVICE);
		for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
		{
			URLPattern publicUrlPattern = (URLPattern)it.next();
			if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
				return true;
		}

		return false;
	}

    public boolean isSecondStepUrl(String requestedUrl)
    {
        if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
            return false;

        // We have to go over the public url's and make left compare
        WebResource publicService = (WebResource)webResources.get("secondStep");
        for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
        {
            URLPattern publicUrlPattern = (URLPattern)it.next();
            if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
                return true;
        }

        return false;
    }
    
    public boolean isSecondStepTransitionUrl(String requestedUrl)
    {
        if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
            return false;

        // We have to go over the public url's and make left compare
        WebResource publicService = (WebResource)webResources.get("secondStepTransition");
        for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
        {
            URLPattern publicUrlPattern = (URLPattern)it.next();
            if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
                return true;
        }

        return false;
    }
    
    public boolean isPhoneCollectionEditmyprofileUrl(String requestedUrl)
    {
        if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
            return false;

        // We have to go over the public url's and make left compare
        WebResource publicService = (WebResource)webResources.get("phoneCollectionEdit");
        for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
        {
            URLPattern publicUrlPattern = (URLPattern)it.next();
            if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
                return true;
        }
        
        return false;
    }
    
    public boolean isPhoneCollectionUrl(String requestedUrl)
    {
        if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
            return false;

        // We have to go over the public url's and make left compare
        WebResource publicService = (WebResource)webResources.get("phoneCollection");
        for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
        {
            URLPattern publicUrlPattern = (URLPattern)it.next();
            if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
                return true;
        }
        
        return false;
    }
    
    public boolean isSignOutUrl(String requestedUrl)
    {
        if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
            return false;

        // We have to go over the public url's and make left compare
        WebResource publicService = (WebResource)webResources.get("signOut");
        for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
        {
            URLPattern publicUrlPattern = (URLPattern)it.next();
            if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
                return true;
        }
        
        return false;
    }

	/**
	 * Returns true if the given URL is inactive; otherwise false. If requested
	 * URL is null or empty
	 *
	 * @param requestedUrl
	 * 		The requested url that needs to be verified if it is inactive.
	 * @return
	 * 		Returns true if the requestedUrl is inactive; otherwise false.
	 */
	public boolean isInactive(String requestedUrl)
	{
		if ( requestedUrl == null || requestedUrl.trim().length() == 0 )
			return false;

		WebResource publicService = (WebResource)webResources.get(INACTIVE_SERVICE);
		for (Iterator it=publicService.getAssociatedURLs().iterator(); it.hasNext();)
		{
			URLPattern publicUrlPattern = (URLPattern)it.next();
			if ( isRequestedURLMatch(publicUrlPattern.getUrl(), requestedUrl) )
				return true;
		}

		return false;
	}

	/**
	 * This method loads the security data from securityinfo.xml
	 *
	 * @throws SystemException (IOException if secuirtyinfo.xml does not exist and
	 * SAXException if the xml is not well formatted)
	 */
	private void loadData() throws SystemException
	{
		DOMParser parser = new DOMParser();

        try {
            parser.parse(new InputSource(xmlFileStream));
            Document document = parser.getDocument();

			loadWebResources(document);
			loadRoles(document);

			populateAvailableURLsForRoles();

        } catch (IOException e) {
        	e.printStackTrace();
  			throw new SystemException(e, this.getClass().getName(), "loadData" , "IOException occurred.");
        } catch (SAXException e) {
        	e.printStackTrace();
  			throw new SystemException(e, this.getClass().getName(), "loadData" , "SAXException occurred.");
        }

	}

	private void loadWebResources(Document document) throws SystemException
	{

        NodeList list = document.getElementsByTagName(WEBRESOURCE_ELEMENT_NAME);
        int len = list.getLength();
        for (int i=0; i < len; i++)
        {
        	Element webResource = (Element)list.item(i);
        	String webResourceId = webResource.getAttribute(WEBRESOURCE_ID_ATTRIBUTE);
        	String description = webResource.getAttribute(WEBRESOURCE_DESCRIPTION_ATTRIBUTE);
        	String contractRequired = webResource.getAttribute(WEBRESOURCE_CONTRACT_REQUIRED_ATTRIBUTE);
        	String productType = webResource.getAttribute(WEBRESOURCE_ALLOWED_PRODUCT_TYPES_ATTRIBUTE);
        	String contractStatuses = webResource.getAttribute(WEBRESOURCE_ALLOWED_CONTRACT_STATUSES_ATTRIBUTE);
        	String contractPermissionsRequired = webResource.getAttribute(WEBRESOURCE_CONTRACT_PERMISSIONS_REQUIRED_ATTRIBUTE);
        	String allowedSites = webResource.getAttribute(WEBRESOURCE_ALLOWED_SITES_ATTRIBUTE);
        	String permissionsAllowed = webResource.getAttribute(WEBRESOURCE_PERMISSIONS_ELEMENT_NAME);
        	String selectedAccessCheckRequired = webResource.getAttribute(WEBRESOURCE_SELECT_ACCESS_CHECK_REQUIRED_ATTRIBUTE);

			boolean boolContractRequired = Boolean.valueOf(contractRequired).booleanValue();
			boolean boolSelectedAccessCheckRequired = Boolean.valueOf(selectedAccessCheckRequired).booleanValue();
			if ( boolContractRequired && (productType.trim().length() == 0 || contractStatuses.trim().length() == 0 ) )
				throw new SystemException(new
					Exception("SecurityManager->loadWebResources: contract is required for this webresource and allowed "

+
						"contract statuses and allowed product types should specified. webresource: " +

webResourceId), this.getClass().getName(), "loadWebResources" ,"Contract is required");

        	NodeList urls = webResource.getElementsByTagName(WEBRESOURCE_URL_ELEMENT_NAME);
        	int lenUrls = urls.getLength();
        	//List serviceList = new ArrayList(lenUrls);
			WebResource webResourceObj = new WebResource(webResourceId,description);
			webResourceObj.setPermissions( StringUtils.isNotBlank(permissionsAllowed) );

        	for (int j=0; j < lenUrls; j++)
        	{
        		Element urlElement =  (Element)urls.item(j);
        		URLPattern urlPattern = new URLPattern(urlElement.getFirstChild().getNodeValue(), boolContractRequired, boolSelectedAccessCheckRequired);

  				String productTypeForUrl = urlElement.getAttribute(WEBRESOURCE_ALLOWED_PRODUCT_TYPES_ATTRIBUTE);
  				if ( productTypeForUrl != null && productTypeForUrl.trim().length() > 0)
	  				urlPattern.addAllowedProductTypes(productTypeForUrl);
	  			else
		  			urlPattern.addAllowedProductTypes(productType);

  				String allowedSitesForUrl = urlElement.getAttribute(WEBRESOURCE_ALLOWED_SITES_ATTRIBUTE);
  				if ( allowedSitesForUrl != null && allowedSitesForUrl.trim().length() > 0)
	  				urlPattern.addAllowedSites(allowedSitesForUrl);
	  			else
		  			urlPattern.addAllowedSites(allowedSites);


        		urlPattern.addAllowedContractStatuses(contractStatuses);
        		urlPattern.addAllowedPermissions(permissionsAllowed);
        		urlPattern.addContractPermissionsRequired(contractPermissionsRequired);

        		webResourceObj.addURL(urlPattern);
        		//webResourceObj.addURL( ((Element)urls.item(j)).getFirstChild().getNodeValue() );
        	}

			webResources.put(webResourceId, webResourceObj);
        }

		if ( logger.isDebugEnabled() )
			logger.debug("webservices="+webResources);
	}


	private void loadRoles(Document document) throws SystemException
	{
        NodeList list = document.getElementsByTagName(ROLE_ELEMENT_NAME);
        int len = list.getLength();
        for (int i=0; i < len; i++)
        {
        	Element role = (Element)list.item(i);
        	String roleId = role.getAttribute(ROLE_ID_ATTRIBUTE);
        	String description = role.getAttribute(ROLE_DESCRIPTION_ATTRIBUTE);
        	String timeout = role.getAttribute(ROLE_TIMEOUT_ATTRIBUTE);

        	NodeList availableServices = role.getElementsByTagName(ROLE_AVAILABLESERVICE_ELEMENT_NAME);
        	int lenAvailableServices = availableServices.getLength();
			Role roleObj = new Role(roleId, description,timeout);
        	for (int j=0; j < lenAvailableServices; j++)
        		roleObj.addService( ((Element)availableServices.item(j)).getFirstChild().getNodeValue() );

			roles.put(roleId, roleObj);
        }

        if ( logger.isDebugEnabled() )
			logger.debug("roles="+roles);
	}

	/**
	 * This is to populate the availableURLsForRole which will speed up the
	 * lookup. This is basically associating the roles with url patterns.
	 *
     * Note: This method will only hold the first 'url', if they are defined more than once for a
     * particular role, all after the first 'url' are discarded.
	 */
	private void populateAvailableURLsForRoles()
	{
		for (Iterator itRole=roles.keySet().iterator(); itRole.hasNext();)
		{
			Role role = (Role)roles.get(itRole.next());
			Set<URLPattern> urls = new TreeSet<URLPattern>();
			for (Iterator itServices=role.getAssociatedServices();itServices.hasNext();)
			{
				WebResource webResource = (WebResource)webResources.get(itServices.next());
				//urls.addAll( webResource.getAssociatedURLs() );
				if (webResource != null && webResource.getAssociatedURLs() != null) {
					for (Iterator itAssociatedUrls=webResource.getAssociatedURLs().iterator();itAssociatedUrls.hasNext();)
					{
						URLPattern urlPattern = (URLPattern)itAssociatedUrls.next();
						urls.add(urlPattern);
					}
				}
			}
			availableURLsForRoles.put(role.getId(), urls);
		}

        if ( logger.isDebugEnabled() )
			logger.debug("availableURLsForRoles="+availableURLsForRoles);

	}
	/**
	 * This is a helper methos that check if the allowed url is
	 * part of the requested url.
	 *
	 * @param allowedUrl
	 * 		Allowed url forn the service.
	 * @param requestedUrl
	 * 		url that is going to be authenticated.
	 * @return
	 * 		If requested url's length is longer or equals to allowed url and requested url
	 * starts with allowed url
	 */
	private boolean isRequestedURLMatch(String allowedUrl, String requestedUrl)
	{
		boolean retVal = false;

		if ( allowedUrl.length() <= requestedUrl.length() &&
			requestedUrl.startsWith(allowedUrl) )
			retVal = true;

		return retVal;
	}

	public synchronized void clearCache() throws SystemException
	{
		webResources.clear();
		roles.clear();
		availableURLsForRoles.clear();

		loadData();
	}


	public static Collection getProfileAccessLevel(UserRole userRole, boolean allocated)
	{
		return AccessLevelUtility.getProfileAccessLevel(userRole, allocated);
	}

	public static boolean canManageExternal(UserRole currentRole)
	{
		return AccessLevelUtility.canManageExternal(currentRole);
	}

	public static boolean canManageTPA(UserRole currentRole)
	{
		return AccessLevelUtility.canManageTPA(currentRole);
	}

	public static boolean canManageInternal(UserRole currentRole)
	{
		return AccessLevelUtility.canManageInternal(currentRole);
	}

	public static boolean canManage(Principal managingPrincipal, UserRole roleToBeManaged)
	{
		return AccessLevelUtility.canManageThisUser(managingPrincipal, roleToBeManaged);
	}

	private boolean isRequiredContractPermissionExists(UserProfile user, URLPattern urlPattern)
	{
		String[] permissions = urlPattern.getContractPermissionsRequired();
		int len = permissions.length;
		boolean isPermissionsExists = true;

		try {
			for (int i=0; i < len; i++)
			{
				if ( !((Boolean)PropertyUtils.getProperty(user, permissions[i])).booleanValue() )
				{
					isPermissionsExists = false;
					break;
				}
			}
		}
		catch(NoSuchMethodException e)
		{
			isPermissionsExists = false;
			SystemException se = new SystemException(e, this.getClass().getName(), "isRequiredPermissionExists" , "NoSuchMethodException occurred.");
			LogUtility.log(se);
		}
		catch(IllegalAccessException e)
		{
			isPermissionsExists = false;
			SystemException se = new SystemException(e, this.getClass().getName(), "isRequiredPermissionExists" , "IllegalAccessException occurred.");
			LogUtility.log(se);
		}
		catch(java.lang.reflect.InvocationTargetException e)
		{
			isPermissionsExists = false;
			SystemException se = new SystemException(e, this.getClass().getName(), "isRequiredPermissionExists" , "InvocationTargetException occurred.");
			LogUtility.log(se);
		}

		return isPermissionsExists;
	}

	public int getTimeout(Principal principal)
	{
		Object obj = roles.get(principal.getRole().toString());
		if ( obj != null )
			return ((Role)obj).getTimeout();
		else // This is set to default value incase there is a no timeout has been set
			return 600;
	}
}



