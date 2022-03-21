package com.manulife.pension.ps.web.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.security.exception.SecurityServiceException;

/**
 * 
 * @author guweigu
 * 
 * The decorate utility class that returns message counts for a list of
 * contracts
 * 
 */
@SuppressWarnings("unchecked")
public class MessageCountsDecorate {

	/**
	 * Return a total message counts (urgent/action/fyi) for one user within a site
	 * 
	 * @param servlet
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws SecurityServiceException 
	 */
	public static int[][] getTotalMessageCounts(ServletContext servlet,
			HttpServletRequest request) throws SystemException {
		UserProfile profile = SessionHelper.getUserProfile(request);
		if (profile.isInternalUser()&& !profile.isBundledGACAR()) {
			return null;
		} else {
			int[][] returnArray = new int[2][3];
			returnArray[0] = MessageServiceFacadeFactory.getInstance(servlet).getTotalMessageCountForUserContracts(
					profile);
			if ( profile.isTPA() ) {
				returnArray[1] =MessageServiceFacadeFactory.getInstance(servlet).getTotalMessageCountForUserFirms(
						profile); 
			}

			return returnArray;
		}

	}

	/**
	 * The returned map has key of contract, the value could be null (when the
	 * contract is Non-AE for Phase I), or an array of message counts (urgent,
	 * action, fyi)
	 * 
	 * @param servlet
	 * @param request
	 * @return
	 * @throws SystemException
	 * 
	 */

	public static Map<Integer, List<String>> getMessageCounts(ServletContext servlet,
			HttpServletRequest request) throws SystemException {
		UserProfile profile = SessionHelper.getUserProfile(request);
		if (profile.isInternalUser() && !profile.isBundledGACAR()) {
			return null;
		}
		SelectContractReportData reportData = (SelectContractReportData) request.getAttribute(Constants.REPORT_BEAN);
		Collection<SelectContract> items = reportData.getDetails();
		List<Integer> contractIds = new ArrayList<Integer>();
		List<String> blankArray = new ArrayList<String>();
		blankArray.add("");
		blankArray.add("");
		blankArray.add("");

		for (SelectContract contract : items) {
			contractIds.add(contract.getContractNumber());
		}
		Map<Integer, List<String>> results = new HashMap<Integer, List<String>>(0);
		if ( contractIds.size() > 0) {
			Map<Integer, int[]> tmp = MessageServiceFacadeFactory.getInstance(servlet).getUserMessageCounts(
					profile.getPrincipal().getProfileId(), contractIds);
			for (Integer i : tmp.keySet() ) {
				List<String> strings = new ArrayList<String>();
				for (int k = 0 ; k < tmp.get(i).length ; k++ ){
					strings.add(String.valueOf(tmp.get(i)[k]));
				}
				results.put(i,strings);
			}
		}

		for (int cid : contractIds) {
			if (results.get(cid) == null) {
				results.put(cid, blankArray);
			}
		}
		return results;
	}
}
