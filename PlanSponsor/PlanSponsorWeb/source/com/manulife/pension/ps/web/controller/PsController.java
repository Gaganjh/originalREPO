package com.manulife.pension.ps.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * This is the base action for all Action that is going to be implemented in
 * PlanSponsor.
 * 
 * @author Ilker Celikyilmaz
 * 
 */
public abstract class PsController extends BaseController {

	public PsController(Class clazz) {
		super(clazz);
	}

	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static UserProfile getUserProfile(final HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}
}