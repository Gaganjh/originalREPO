package com.manulife.pension.bd.web.navigation.generation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.owasp.encoder.Encode;

import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.navigation.NavigationHeader;
import com.manulife.pension.bd.web.navigation.NavigationHeaderConstants;
import com.manulife.pension.bd.web.navigation.UserMenu;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDPrincipal;

/**
 * Note: This is designed as a thread-safe stateless class.
 * 
 * @author guweigu
 * 
 */
public class NavigationGeneratorImpl implements NavigationGenerator {
	private SecurityManager securityManager;

	private final static String AdvisorViewText = "Advisor View for:";
	private final static String WelcomeText = "Welcome,";

	public NavigationGeneratorImpl(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public UserMenu generateMainMenu(SystemMenuItem systemMenu,
			AuthorizationSubject subject) throws SystemException {
		UserMenu menu = new UserMenu();
		UserMenuItem top = generateMenu(systemMenu, subject);
		if (top != null) {
			for (UserMenuItem item : top.getSubMenuItems()) {
				menu.addLevelOneUserMenuItem(item);
				// if there is only one child menu item, remove it
				// the parent one will work for it
				if (item.getChildrenSize() == 1) {
					item.removeChild(0);
				}
			}
		}
		return menu;
	}

	private UserMenuItem generateMenu(SystemMenuItem menuItem,
			AuthorizationSubject subject) throws SystemException {
		if (!menuItem.hasChildren()) { // leaf menu item
			
			
			/**
        	 * Production fix for not to show planReviewReport Link on live
        	 * 
        	 *  <bean parent="menuItem" p:id="planReviewReports" p:title="Plan Review Reports"
                                p:actionURL="/do/bob/planReview/" />
        	 * 
        	 */
        	
			if(StringUtils.equalsIgnoreCase("planReviewReports", menuItem.getId())){
        		
            	if(!PlanReviewReportUtils.isPlanReviewLaunched()) {
    	        	if(!PlanReviewReportUtils.isPlanReviewFunctionalityAvailable()) {
    	        		
    	        		//  if the  plan review launched is 'false' and 
    	        		//  if the plan review available is 'false' 
    	        		// -- >  Plan Review Reports link will suppressed
    	        		
    	        		return null;
    	        	}
            	}
			}
			
			if(StringUtils.equalsIgnoreCase("orderACR", menuItem.getId())){
        		
            	if(PlanReviewReportUtils.isPlanReviewLaunched()) {
    	        		
    	        		//  if the  plan review launched is 'true' 
    	        		// -- >  orderACR Link should be suppressed
    	        		
    	        		return null;
    	        	
            	}
			}
			
			// to hide the navigation header for Partnering ,Prime and literature when logged in as an RIA user
			if(subject != null){
				BDUserProfile userProfile = (BDUserProfile) subject.getUserProfile();
				String userRole = StringUtils.EMPTY;
				if(userProfile != null && userProfile.getAbstractPrincipal() != null && userProfile.getAbstractPrincipal().getAbstractUserRole() != null){
					
					userRole = userProfile.getAbstractPrincipal().getAbstractUserRole().getRoleId();
					
					if("RIAUser".equalsIgnoreCase(userRole)) {
						if(StringUtils.equalsIgnoreCase("partnering", menuItem.getId())){
							return null;
						}
						if(StringUtils.equalsIgnoreCase("prime", menuItem.getId())){
							return null;
						}
						if(StringUtils.equalsIgnoreCase("literature", menuItem.getId())){
							return null;
						}
					}
				}
			}
			
			
			if (securityManager.isUserAuthorized(subject, menuItem
					.getActionURL())) {
				return new UserMenuItem(menuItem.getId(), menuItem.getTitle(),
						menuItem.getActionURL());
			} else {
				// This is checked specifically for mimic mode that if an URL
				// can't be accessed by mimic only, then show it with URL
				if (subject != null) {
					BDUserProfile up = (BDUserProfile) subject.getUserProfile();
					if (up != null && up.isInMimic()) {
						AuthorizationSubject tmp = new AuthorizationSubject();
						tmp.setUserProfile(up);
						tmp.setIgnoreMimic(true);
						if (securityManager.isUserAuthorized(tmp, menuItem
								.getActionURL())) {
							UserMenuItem item = new UserMenuItem(menuItem
									.getId(), menuItem.getTitle(), "#");
							return item;
						}
					}
				}
				return null;
			}
		} else {
			List<SystemMenuItem> sChildren = menuItem.getSubMenuItems();
			List<UserMenuItem> uChildren = new ArrayList<UserMenuItem>(10);
			for (SystemMenuItem c : sChildren) {
				// if the list already contains the same id, then ignore it
				if (!containsId(uChildren, c.getId())) {
					UserMenuItem newItem = generateMenu(c, subject);
					if (newItem != null) {
						uChildren.add(newItem);
					}
				}
			}
			if (uChildren.size() > 0) {
				UserMenuItem menu = new UserMenuItem(menuItem.getId(), menuItem
						.getTitle(), menuItem.getUrlGenerator().getUrl(
						uChildren, subject));
				
				for (UserMenuItem sub : uChildren) {
					menu.addSubMenuItem(sub);
				}
				return menu;
			} else {
				return null;
			}
		}
	}

	/**
	 * Check if the list of UserMenuItem already contains the <code>id</code>
	 * 
	 * @param children
	 * @param id
	 * @return
	 */
	private boolean containsId(List<UserMenuItem> children, String id) {
		for (UserMenuItem item : children) {
			if (item.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public NavigationHeader generateNavigationHeader(
			AuthorizationSubject subject) throws SystemException {
		if (subject == null) {
			return null;
		}
		BDUserProfile user = (BDUserProfile) subject.getUserProfile();
		NavigationHeader navHeader = new NavigationHeader();
		BDPrincipal p = user.getBDPrincipal();
		String nameText = Encode.forHtmlContent(p.getFirstName()) + " " + Encode.forHtmlContent(p.getLastName());
		navHeader.setUserNameText(nameText);
		List<UserMenuItem> links = new ArrayList<UserMenuItem>(10);
		navHeader.setLinks(links);
		if (user.isInMimic()) {
			navHeader.setExitMenu(NavigationHeaderConstants.exitMimic);
			navHeader.setWelcomeText(AdvisorViewText);
			navHeader.setRoleText(BDUserRoleDisplayNameUtil.getInstance()
					.getDisplayName(p.getBDUserRole().getRoleType()));
			addLink(subject, links, NavigationHeaderConstants.ContactUs);
			links.add(NavigationHeaderConstants.disabledMyProfile);
			links.add(NavigationHeaderConstants.myProfileHelp);
			addLink(subject, links, NavigationHeaderConstants.userManagement);
		} else {
			addLink(subject, links, NavigationHeaderConstants.ContactUs);
			// none or one of the internal/external my profile will be picked
			addLink(subject, links, NavigationHeaderConstants.myProfileExternal);
			addLink(subject, links, NavigationHeaderConstants.myProfileInternal);
			addLink(subject, links, NavigationHeaderConstants.userManagement);
			addLink(subject, links, NavigationHeaderConstants.contentManagement);
			addLink(subject, links, NavigationHeaderConstants.globalMessages);
			navHeader.setExitMenu(NavigationHeaderConstants.logOut);
			navHeader.setWelcomeText(WelcomeText);
		}
		return navHeader;
	}

	private void addLink(AuthorizationSubject subject, List<UserMenuItem> list,
			UserMenuItem item) throws SystemException {
		if (securityManager.isUserAuthorized(subject, item.getActionURL())) {
			list.add(item);
		}
		return;
	}
}
