/**
 * 
 */
package com.manulife.pension.service.withdrawal.common;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import com.intware.dao.DAOException;
import com.manulife.pension.service.party.dao.EmployeeEmailAddressDAO;
import com.manulife.pension.service.party.dao.EmployeeEmailMessageDAO;
import com.manulife.pension.service.party.valueobject.EmployeeEmailAddressValueObject;
import com.manulife.pension.service.party.valueobject.EmployeeEmailMessageValueObject;
import com.manulife.pension.service.withdrawal.exception.WithdrawalEmailException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalEmailVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalEmailHandler
 *
 * @see com.manulife.pension.service.withdrawal.common.WithdrawalEmailHandler
 * @author patelpo
 */
@PrepareForTest({ WithdrawalEmailHandler.class })
@RunWith(PowerMockRunner.class)
public class WithdrawalEmailHandlerTest {

	/**
	 * Parasoft Jtest UTA: Test for sendEmailToParticipant(WithdrawalEmailVO, String)
	 *
	 * @see com.manulife.pension.service.withdrawal.common.WithdrawalEmailHandler#sendEmailToParticipant(WithdrawalEmailVO, String)
	 * @author patelpo
	 */
	@Test
	public void testSendEmailToParticipant() throws Throwable {
		EmployeeEmailAddressDAO newEmployeeEmailAddressDAOResult = mock(EmployeeEmailAddressDAO.class); // UTA: default value
		whenNew(EmployeeEmailAddressDAO.class).withAnyArguments().thenReturn(newEmployeeEmailAddressDAOResult);
		EmployeeEmailAddressValueObject employeeEmailAddressValueObject = new EmployeeEmailAddressValueObject();
		when(newEmployeeEmailAddressDAOResult.selectCurrentEmployeeEmailAddress(any(BigDecimal.class))).thenReturn(employeeEmailAddressValueObject);
		EmployeeEmailMessageDAO employeeEmailMessageDAO = mock(EmployeeEmailMessageDAO.class); // UTA: default value
		whenNew(EmployeeEmailMessageDAO.class).withAnyArguments().thenReturn(employeeEmailMessageDAO);
		PowerMockito.doNothing().when(employeeEmailMessageDAO).create(any(EmployeeEmailMessageValueObject.class));

		
		// Given
		WithdrawalEmailHandler underTest = new WithdrawalEmailHandler();

		// When
		WithdrawalEmailVO withdrawalEmailVO = mockWithdrawalEmailVO();
		
	
		String eventId = "1"; // UTA: default value
		underTest.sendEmailToParticipant(withdrawalEmailVO, eventId);
		

	}
	
	@Test(expected = WithdrawalEmailException.class)
	public void testSendEmailToParticipant_Exception() throws Throwable {
		EmployeeEmailAddressDAO newEmployeeEmailAddressDAOResult = mock(EmployeeEmailAddressDAO.class); // UTA: default value
		whenNew(EmployeeEmailAddressDAO.class).withAnyArguments().thenReturn(newEmployeeEmailAddressDAOResult);
		EmployeeEmailAddressValueObject employeeEmailAddressValueObject = new EmployeeEmailAddressValueObject();
		when(newEmployeeEmailAddressDAOResult.selectCurrentEmployeeEmailAddress(any(BigDecimal.class))).thenReturn(employeeEmailAddressValueObject);
		EmployeeEmailMessageDAO employeeEmailMessageDAO = mock(EmployeeEmailMessageDAO.class); // UTA: default value
		whenNew(EmployeeEmailMessageDAO.class).withAnyArguments().thenReturn(employeeEmailMessageDAO);
		PowerMockito.doThrow(new DAOException()).when(employeeEmailMessageDAO).create(any(EmployeeEmailMessageValueObject.class));

		
		// Given
		WithdrawalEmailHandler underTest = new WithdrawalEmailHandler();

		// When
		WithdrawalEmailVO withdrawalEmailVO = mockWithdrawalEmailVO();
		
	
		String eventId = "1"; // UTA: default value
		underTest.sendEmailToParticipant(withdrawalEmailVO, eventId);
		

	}
	@Test(expected = WithdrawalEmailException.class)
	public void testSendEmailToParticipant_Exception1() throws Throwable {
		EmployeeEmailAddressDAO newEmployeeEmailAddressDAOResult = mock(EmployeeEmailAddressDAO.class); // UTA: default value
		
		// Given
		WithdrawalEmailHandler underTest = new WithdrawalEmailHandler();
		
		// When
		WithdrawalEmailVO withdrawalEmailVO = mockWithdrawalEmailVO();
		
		
		String eventId = "1"; // UTA: default value
		underTest.sendEmailToParticipant(withdrawalEmailVO, eventId);
		
		
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalEmailVO
	 */
	private static WithdrawalEmailVO mockWithdrawalEmailVO() throws Throwable {
		WithdrawalEmailVO withdrawalEmailVO = mock(WithdrawalEmailVO.class);
		Integer getContractIdResult = 10; // UTA: default value
		when(withdrawalEmailVO.getContractId()).thenReturn(getContractIdResult);

		int getEmployeeProfileIdResult = 1; // UTA: default value
		when(withdrawalEmailVO.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Integer getSubmissionIdResult = 10; // UTA: default value
		when(withdrawalEmailVO.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return withdrawalEmailVO;
	}
}