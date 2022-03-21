package com.manulife.pension.bd.web.mimic;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.tracking.BDMimicTracker;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;

/**
 * Helper class that start and end mimic session.
 * Stores the internal user's session to a map and clear it out from Session when mimic
 * is started. And recover's internal user's session attributes int HTTPSession
 * 
 * @author guweigu
 *
 */
public class MimicSession {
	private static final String ATTR_MIMICKING_SESSION = "bd.mimicSession";

	@SuppressWarnings("unchecked")
	public void createMimicSession(HttpServletRequest request,
			BDUserProfile mimickingUser, BDUserProfile mimickedUser) {
		HttpSession session = request.getSession();
		Map<String, Object> mimickingUserSession = new HashMap<String, Object>();
		Enumeration<String> names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String n = names.nextElement();
			if (!StringUtils.equals(BDConstants.BD_LOGIN_TRACK, n)) {
				mimickingUserSession.put(n, session.getAttribute(n));
				session.removeAttribute(n);
			}
		}
		session.setAttribute(ATTR_MIMICKING_SESSION, mimickingUserSession);
		session.setAttribute(BDConstants.BD_MIMIC_TRACK, new BDMimicTracker(
				mimickingUser.getBDPrincipal(), mimickedUser.getBDPrincipal()
						.getProfileId()));
		session.setAttribute(BDConstants.LOGIN_USER_PRINCIPAL,mimickingUser.getBDPrincipal()); 

		ApplicationHelper.setUserProfile(request, mimickedUser);
		return;
	}

	@SuppressWarnings("unchecked")
	public void endMimicSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> mimickingUserSession = (Map<String, Object>) session
				.getAttribute(ATTR_MIMICKING_SESSION);
		Enumeration<String> names = session.getAttributeNames();
		// first remove all the attributes
		while (names.hasMoreElements()) {
			String n = names.nextElement();
			if (!StringUtils.equals(BDConstants.BD_LOGIN_TRACK, n)) {
				session.removeAttribute(n);
			}
		}
		// recover all the attributes
		for (String n : mimickingUserSession.keySet()) {
			session.setAttribute(n, mimickingUserSession.get(n));
		}
		session.removeAttribute(ATTR_MIMICKING_SESSION);
		return;
	}
}
