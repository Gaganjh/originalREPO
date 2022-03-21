package com.manulife.pension.bd.web.util;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.userprofile.BDUserProfile;

public class ValidatorUtil {

	static public BDUserProfile getUserProfile(HttpServletRequest request) {
		return BDSessionHelper.getUserProfile(request);
	}
	
	
	/**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }
	
	  protected static final String TASK_KEY = "task";
    protected static final String DEFAULT_TASK = "default";	
	
	
}
