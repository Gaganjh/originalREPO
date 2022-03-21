package com.manulife.pension.service.loan.util;

import junit.framework.Assert;

import com.manulife.pension.datahelper.DataHelper;
import com.manulife.pension.datahelper.EmployeeDataHelper;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.employee.EmployeeConstants.ParticipantStatus;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.util.Pair;

public class TestLoanDataHelper extends
		DistributionContainerEnvironment {

	private DataHelper.Session session;

	public void setUp() throws Exception {
		super.setUp();
		session = DataHelper.newSession();
	}

	public void tearDown() throws Exception {
		session.cleanUp();
		super.tearDown();
	}

	public void testGetLoanParticipantData() throws Exception {
		EmployeeDataHelper employeeDataHelper = new EmployeeDataHelper(
				DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE);
		Long profileId = employeeDataHelper.newEmployee(session, (Pair[]) null);
		Integer contractId = 70300;
		String firstName = "TestFirstName";
		String participantStatusCode = ParticipantStatus.PARTIAL_DEATH;
		employeeDataHelper
				.newEmployeeContract(session, profileId, contractId,
						new Pair[] { new Pair<String, String>("FIRST_NAME",
								firstName) });
		Long participantId = employeeDataHelper.newParticipantContract(session,
				profileId, contractId, new Pair[] { new Pair<String, String>(
						"PARTICIPANT_STATUS_CODE", participantStatusCode) });

		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		LoanParticipantData loanParticipantData = loanDataHelper
				.getLoanParticipantData(contractId, profileId.intValue());

		Assert.assertEquals(firstName, loanParticipantData.getFirstName());
		Assert.assertEquals(participantStatusCode, loanParticipantData
				.getParticipantStatusCode());

	}

}
