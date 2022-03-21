package com.manulife.pension.ps.web.tools.util;

import com.manulife.pension.service.security.role.CAR;
import com.manulife.pension.service.security.role.SuperCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.UserRole;

public class ContractPinGenHelper {
	public static String getActionSourceCode(UserRole role) {
		return (role instanceof SuperCAR || role instanceof CAR || role instanceof TeamLead) ? "C"
				: "L";
	}
}
