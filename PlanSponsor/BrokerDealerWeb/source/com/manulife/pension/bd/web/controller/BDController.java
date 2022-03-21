package com.manulife.pension.bd.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.platform.web.report.BasePdfController;

abstract public class BDController extends BasePdfController {

	@SuppressWarnings("unchecked")
	public BDController(Class clazz) {
		super(clazz);
	}
	
	static public BDUserProfile getUserProfile(HttpServletRequest request) {
		return BDSessionHelper.getUserProfile(request);
	}
	
	/**
	 * Returns the BOB Context associated with the given request by
	 * using BDSessionHelper
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	static public BobContext getBobContext(HttpServletRequest request) {
		return BDSessionHelper.getBobContext(request);
	}

}
