package com.manulife.pension.bd.web.navigation.generation;

import java.util.List;

import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.navigation.UserMenuItem;

/**
 * The interface that generates an URL for a menu item
 * 
 * @author guweigu
 *
 */
public interface ParentMenuItemUrlGenerator {
	String getUrl(List<UserMenuItem>children, AuthorizationSubject subject);
}
