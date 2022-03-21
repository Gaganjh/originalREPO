 /**
 * 
 */
package com.manulife.pension.service.withdrawal.util;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for WithdrawalRequestHelper
 *
 * @see com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper
 * @author patelpo
 */
public class WithdrawalRequestHelperTest {

	/**
	 * Parasoft Jtest UTA: Test for populateDefaultRecipient(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper#populateDefaultRecipient(WithdrawalRequest)
	 * @author patelpo
	 */
/*	@Test
	public void testPopulateDefaultRecipient() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest();
		
		WithdrawalRequestHelper underTest = mock(WithdrawalRequestHelper.class);
		underTest.populateDefaultRecipient(request);

	}
*/
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		boolean isBlankResult = true; // UTA: default value
		when(getParticipantAddressResult.isBlank()).thenReturn(isBlankResult);
		return getParticipantAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getFirstNameResult = ""; // UTA: default value
		when(request.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(request.getLastName()).thenReturn(getLastNameResult);

		DistributionAddress getParticipantAddressResult = mockDistributionAddress();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "PP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		return request;
	}

	/**
	 * Parasoft Jtest UTA: Test for updateRecipients(WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper#updateRecipients(WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testUpdateRecipients() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients1() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest21();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_1() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_1();
		WithdrawalRequestHelper.updateRecipients(request);

	}

	
	  @Test 
	  public void testUpdateRecipients_2() throws Throwable { 
	  WithdrawalRequest request = mockWithdrawalRequest2_2();
	  WithdrawalRequestHelper.updateRecipients(request);
	  
	  }
	 
/*	  @Test 
	  public void testUpdateRecipients_21() throws Throwable { 
		  WithdrawalRequest request = mockWithdrawalRequest2_21();
		  WithdrawalRequestHelper.updateRecipients(request);
		  
	  }
 */ 
/*	  @Test 
	  public void testUpdateRecipients_22() throws Throwable { 
		  WithdrawalRequest request = mockWithdrawalRequest2_22();
		  WithdrawalRequestHelper.updateRecipients(request);
		  
	  }
*/
	  @Test 
	  public void testUpdateRecipients_23() throws Throwable { 
		  WithdrawalRequest request = mockWithdrawalRequest2_23();
		  WithdrawalRequestHelper.updateRecipients(request);
		  
	  }
	  
	@Test
	public void testUpdateRecipients_3() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_3();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_31() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_31();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_32() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_32();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_33() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_33();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_34() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_34();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_35() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_35();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_351() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_351();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_352() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_352();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_3Exception() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_311();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_4() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_4();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_41() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_41();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
/*	@Test
	public void testUpdateRecipients_42() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_42();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
*/
	@Test
	public void testUpdateRecipients_43() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_43();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_44() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_44();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_4Exception() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_411();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_5() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_5();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_6() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_6();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_61() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_61();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_62() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_62();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_63() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_63();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_64() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_64();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_65() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_65();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_6Exception() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_611();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_7() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_7();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_8() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_8();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	
	@Test
	public void testUpdateRecipients_9() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_9();
		WithdrawalRequestHelper.updateRecipients(request);

	}
	@Test
	public void testUpdateRecipients_91() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_91();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_92() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_92();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test
	public void testUpdateRecipients_93() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_93();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	@Test(expected = NestableRuntimeException.class)
	public void testUpdateRecipients_10() throws Throwable {
		// When
		WithdrawalRequest request = mockWithdrawalRequest2_10();
		WithdrawalRequestHelper.updateRecipients(request);
		
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress2() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		boolean isBlankResult = false; // UTA: default value
		when(getParticipantAddressResult.isBlank()).thenReturn(isBlankResult);
		when(getParticipantAddressResult.getAddressLine1()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getAddressLine2()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getCity()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getZipCode()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getCountryCode()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getDistributionTypeCode()).thenReturn("ADDR1");
		when(getParticipantAddressResult.getPayeeNo()).thenReturn(10);
		when(getParticipantAddressResult.getRecipientNo()).thenReturn(10);
		
		
		return getParticipantAddressResult;
	}
	private static DistributionAddress mockDistributionAddress21() throws Throwable {
		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		when(getParticipantAddressResult.isBlank()).thenReturn(true);
		
		
		return getParticipantAddressResult;
	}
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getTrusteeNameResult = ""; // UTA: default value
		when(getParticipantInfoResult.getTrusteeName()).thenReturn(getTrusteeNameResult);

		String toStringResult = ""; // UTA: default value
		when(getParticipantInfoResult.toString()).thenReturn(toStringResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress() throws Throwable {
		Address cloneAddressResult2 = mock(Address.class);
		Address cloneAddressResult3 = mock(Address.class);
		when(cloneAddressResult2.cloneAddress()).thenReturn(cloneAddressResult3);

		boolean isBlankResult2 = false; // UTA: default value
		when(cloneAddressResult2.isBlank()).thenReturn(isBlankResult2);

		String toStringResult2 = ""; // UTA: default value
		when(cloneAddressResult2.toString()).thenReturn(toStringResult2);
		return cloneAddressResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress2() throws Throwable {
		Address cloneAddressResult = mock(Address.class);
		Address cloneAddressResult2 = mockAddress();
		when(cloneAddressResult.cloneAddress()).thenReturn(cloneAddressResult2);

		boolean isBlankResult3 = false; // UTA: default value
		when(cloneAddressResult.isBlank()).thenReturn(isBlankResult3);

		String toStringResult3 = ""; // UTA: default value
		when(cloneAddressResult.toString()).thenReturn(toStringResult3);
		return cloneAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Address
	 */
	private static Address mockAddress3() throws Throwable {
		Address getTrusteeAddressResult = mock(Address.class);
		Address cloneAddressResult = mockAddress2();
		when(getTrusteeAddressResult.cloneAddress()).thenReturn(cloneAddressResult);

		boolean isBlankResult4 = false; // UTA: default value
		when(getTrusteeAddressResult.isBlank()).thenReturn(isBlankResult4);

		String toStringResult4 = ""; // UTA: default value
		when(getTrusteeAddressResult.toString()).thenReturn(toStringResult4);
		return getTrusteeAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "PA"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest21() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "AB"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PA"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	
	private static WithdrawalRequest mockWithdrawalRequest2_1() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}

	
	  private static WithdrawalRequest mockWithdrawalRequest2_2() throws Throwable{ 
		  
	  WithdrawalRequest request = mock(WithdrawalRequest.class); String
	  getOriginalPaymentToResult = "RI"; // UTA: default value
	  when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
	  
	  //DistributionAddress getParticipantAddressResult =
	  mockDistributionAddress2(); Address getParticipantAddressResult=new
	  Address();
	  when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult)
	  ;
	  
	  ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
	  when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
	  
	  String getParticipantNameResult = ""; // UTA: default value
	  when(request.getParticipantName()).thenReturn(getParticipantNameResult);
	  
	  String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
	  when(request.getParticipantStateOfResidence()).thenReturn(
	  getParticipantStateOfResidenceResult);
	  
	  String getPaymentToResult = "PA"; // UTA: default value
	  when(request.getPaymentTo()).thenReturn(getPaymentToResult);
	  
	  Payee payee=mock(LoanPayee.class); 
	  DistributionAddress distributionAddress = mock(DistributionAddress.class);
	  when(distributionAddress.isBlank()).thenReturn(true);
	  when(payee.getDefaultAddress()).thenReturn(distributionAddress);
	  when(payee.getCourierCompanyCode()).thenReturn("COMP");
	  
	  
	  Collection<Payee> payees = new ArrayList<Payee>(); 
	  
	  payees.add(payee);
	  
	  Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); 
	  Recipient recipient = new WithdrawalRequestRecipient();
	  recipient.setPayees(payees); 
	  getRecipientsResult.add(recipient);
	  doReturn(getRecipientsResult).when(request).getRecipients();
	  
	  Address getTrusteeAddressResult = mockAddress3();
	  when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
	  
	  boolean hasPaymentToChangedResult = false; // UTA: default value
	  when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
	  
	  String toStringResult5 = ""; // UTA: default value
	  when(request.toString()).thenReturn(toStringResult5); 
	  return request; 
	  }
	  private static WithdrawalRequest mockWithdrawalRequest2_21() throws Throwable{ 
		  
		  WithdrawalRequest request = mock(WithdrawalRequest.class); String
		  getOriginalPaymentToResult = "RI"; // UTA: default value
		  when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		  
		  DistributionAddress getParticipantAddressResult = mockDistributionAddress21(); 
		  when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		  
		  ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		  when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		  
		  String getParticipantNameResult = ""; // UTA: default value
		  when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		  
		  String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		  when(request.getParticipantStateOfResidence()).thenReturn(
				  getParticipantStateOfResidenceResult);
		  
		  String getPaymentToResult = "PI"; // UTA: default value
		  when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		  
		  Payee payee=mock(LoanPayee.class); 
		  DistributionAddress distributionAddress = mock(DistributionAddress.class);
		  when(distributionAddress.isBlank()).thenReturn(true);
		  when(payee.getDefaultAddress()).thenReturn(distributionAddress);
		  when(payee.getCourierCompanyCode()).thenReturn("COMP");
		  
		  
		  Collection<Payee> payees = new ArrayList<Payee>(); 
		  
		  payees.add(payee);
		  
		  Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); 
		  Recipient recipient = new WithdrawalRequestRecipient();
		  recipient.setPayees(payees); 
		  getRecipientsResult.add(recipient);
		  doReturn(getRecipientsResult).when(request).getRecipients();
		  
		  Address getTrusteeAddressResult = mockAddress3();
		  when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		  
		  boolean hasPaymentToChangedResult = false; // UTA: default value
		  when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		  
		  String toStringResult5 = ""; // UTA: default value
		  when(request.toString()).thenReturn(toStringResult5); 
		  return request; 
	  }
	  private static WithdrawalRequest mockWithdrawalRequest2_22() throws Throwable{ 
		  
		  WithdrawalRequest request = mock(WithdrawalRequest.class); String
		  getOriginalPaymentToResult = "RI"; // UTA: default value
		  when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		  
		  //DistributionAddress getParticipantAddressResult =
		  mockDistributionAddress2(); Address getParticipantAddressResult=new
				  Address();
		  when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult)
		  ;
		  
		  ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		  when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		  
		  String getParticipantNameResult = ""; // UTA: default value
		  when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		  
		  String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		  when(request.getParticipantStateOfResidence()).thenReturn(
				  getParticipantStateOfResidenceResult);
		  
		  String getPaymentToResult = "PP"; // UTA: default value
		  when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		  
		  Payee payee=new LoanPayee(); payee.setCourierCompanyCode("COMP");
		  Collection<Payee> payees = new ArrayList<Payee>(); payees.add(payee);
		  
		  Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); 
		  Recipient recipient = new WithdrawalRequestRecipient();
		  recipient.setPayees(payees); 
		  getRecipientsResult.add(recipient);
		  doReturn(getRecipientsResult).when(request).getRecipients();
		  
		  Address getTrusteeAddressResult = mockAddress3();
		  when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		  
		  boolean hasPaymentToChangedResult = false; // UTA: default value
		  when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		  
		  String toStringResult5 = ""; // UTA: default value
		  when(request.toString()).thenReturn(toStringResult5); 
		  return request; 
	  }
	  private static WithdrawalRequest mockWithdrawalRequest2_23() throws Throwable{ 
		  
		  WithdrawalRequest request = mock(WithdrawalRequest.class); String
		  getOriginalPaymentToResult = "RI"; // UTA: default value
		  when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		  
		  //DistributionAddress getParticipantAddressResult =
		  mockDistributionAddress2(); Address getParticipantAddressResult=new
				  Address();
		  when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult)
		  ;
		  
		  ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		  when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		  
		  String getParticipantNameResult = ""; // UTA: default value
		  when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		  
		  String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		  when(request.getParticipantStateOfResidence()).thenReturn(
				  getParticipantStateOfResidenceResult);
		  
		  String getPaymentToResult = "TR"; // UTA: default value
		  when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		  
		  Payee payee=new LoanPayee(); payee.setCourierCompanyCode("COMP");
		  Collection<Payee> payees = new ArrayList<Payee>(); payees.add(payee);
		  
		  Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); 
		  Recipient recipient = new WithdrawalRequestRecipient();
		  recipient.setPayees(payees); getRecipientsResult.add(recipient);
		  doReturn(getRecipientsResult).when(request).getRecipients();
		  
		  Address getTrusteeAddressResult = mockAddress3();
		  when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		  
		  boolean hasPaymentToChangedResult = false; // UTA: default value
		  when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		  
		  String toStringResult5 = ""; // UTA: default value
		  when(request.toString()).thenReturn(toStringResult5); 
		  return request; 
	  }
	 
	private static WithdrawalRequest mockWithdrawalRequest2_3() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PA"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_31() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_32() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_33() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "TR"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_34() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = ""; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_35() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("CH");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_351() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("AC");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_352() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("WT");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_311() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "AB"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("WT");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_4() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_41() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "TR"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_42() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("CH");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_43() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PA"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_44() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_411() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "AB"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "PI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Address address=new Address();
		payee.setAddress(address);
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_5() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "PP"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_6() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PA"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_61() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_62() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_63() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_64() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_65() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = ""; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_611() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "AB"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "TR"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_7() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "RI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "RI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_8() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "PA"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_9() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);

		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);

		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = "RI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();

		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);

		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_91() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("CH");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_92() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("AC");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_93() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PI"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "RI"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		payee.setPaymentMethodCode("WT");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	private static WithdrawalRequest mockWithdrawalRequest2_10() throws Throwable {
		WithdrawalRequest request = mock(WithdrawalRequest.class);
		String getOriginalPaymentToResult = "PP"; // UTA: default value
		when(request.getOriginalPaymentTo()).thenReturn(getOriginalPaymentToResult);
		
		//DistributionAddress getParticipantAddressResult = mockDistributionAddress2();
		Address getParticipantAddressResult=new Address();
		
		when(request.getParticipantAddress()).thenReturn(getParticipantAddressResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(request.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		String getParticipantNameResult = ""; // UTA: default value
		when(request.getParticipantName()).thenReturn(getParticipantNameResult);
		
		String getParticipantStateOfResidenceResult = "Test"; // UTA: default value
		when(request.getParticipantStateOfResidence()).thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = "AB"; // UTA: default value
		when(request.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Payee payee=new LoanPayee();
		payee.setCourierCompanyCode("COMP");
		Collection<Payee> payees = new ArrayList<Payee>();
		payees.add(payee);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new WithdrawalRequestRecipient();
		recipient.setPayees(payees);
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(request).getRecipients();
		
		Address getTrusteeAddressResult = mockAddress3();
		when(request.getTrusteeAddress()).thenReturn(getTrusteeAddressResult);
		
		boolean hasPaymentToChangedResult = true; // UTA: default value
		when(request.hasPaymentToChanged()).thenReturn(hasPaymentToChangedResult);
		
		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	
}