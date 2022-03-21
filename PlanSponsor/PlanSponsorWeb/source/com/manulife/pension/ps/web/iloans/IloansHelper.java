package com.manulife.pension.ps.web.iloans;

import javax.servlet.http.HttpSession;

import com.manulife.pension.ps.web.Constants;

public class IloansHelper {
	public static String PROFILE_ID_PARM = "iloansProfileId";

	public static String CONTRACT_NUMBER_PARM = "iloansContractNumber";

	public static String LOAN_REQUEST_ID_PARM = "iloansRequestId";


	public IloansHelper() {
	}

	public static String getDisplayStatus(String status) {

		String displayStatusText = "unknown";

		if (status.equalsIgnoreCase(Constants.ILOANS_STATUS_CODE_APPROVED)) {
			displayStatusText = Constants.ILOANS_STATUS_TEXT_APPROVED;

		} else if (status.equalsIgnoreCase(Constants.ILOANS_STATUS_CODE_DENIED)) {
			displayStatusText = Constants.ILOANS_STATUS_TEXT_DENIED;

		} else if (status
				.equalsIgnoreCase(Constants.ILOANS_STATUS_CODE_PENDING)) {
			displayStatusText = Constants.ILOANS_STATUS_TEXT_PENDING;

		} else if (status.equalsIgnoreCase(Constants.ILOANS_STATUS_CODE_REVIEW)) {
			displayStatusText = Constants.ILOANS_STATUS_TEXT_REVIEW;
		}

		return displayStatusText;
	}

	public static void removeSessionAttributes(HttpSession session) {
		if (session.getAttribute(PROFILE_ID_PARM) != null)
			session.removeAttribute(PROFILE_ID_PARM);
		if (session.getAttribute(CONTRACT_NUMBER_PARM) != null)
			session.removeAttribute(CONTRACT_NUMBER_PARM);
		if (session.getAttribute(LOAN_REQUEST_ID_PARM) != null)
			session.removeAttribute(LOAN_REQUEST_ID_PARM);
	}

}