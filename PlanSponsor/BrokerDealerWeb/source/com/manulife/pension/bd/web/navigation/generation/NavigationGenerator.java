package com.manulife.pension.bd.web.navigation.generation;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.NavigationHeader;
import com.manulife.pension.bd.web.navigation.UserMenu;
import com.manulife.pension.exception.SystemException;

/**
 * The interface to generate various navigation artifacts (Menu, tabs etc..)
 * 
 * @author guweigu
 *
 */
public interface NavigationGenerator {
	/**
	 * Generate the main navigation menu
	 * 
	 * @param systemMenu
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	UserMenu generateMainMenu(SystemMenuItem systemMenu, AuthorizationSubject subject) throws SystemException ;
	
	/**
	 * Generate the navigation header
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	NavigationHeader generateNavigationHeader(AuthorizationSubject subject) throws SystemException;
}
