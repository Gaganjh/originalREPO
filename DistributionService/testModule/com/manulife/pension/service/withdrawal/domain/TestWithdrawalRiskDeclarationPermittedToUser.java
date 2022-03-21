package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;


import org.junit.Before;
import org.junit.Test;



import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;


/**
 * Tests the validations for the Risk Declaration presented to the user.
 * 
 * @author kuthiha
 */
public class TestWithdrawalRiskDeclarationPermittedToUser {

       
    private ContractInfo contractInfo = new ContractInfo();
    
    @Before
    public void setUpConverters() throws Exception {

    }
    
    private void populateContractInfo(Boolean bool) {
    	contractInfo.setHasApprovePermission(bool);
    }

    /**
     * Tests the logic to see if the Risk Declaration
     * is permitted for the user.
     *
     */
    @Test
    public void testIsAtRiskDeclarationPermittedForUser() throws Exception {
    	final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    	populateContractInfo(Boolean.TRUE);
    	withdrawalRequest.setContractInfo(contractInfo);
    	withdrawalRequest.setRequestRiskIndicator(true);
    	withdrawalRequest.setIsParticipantCreated(true);
    	withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
    	boolean bool = withdrawalRequest.isAtRiskDeclarationPermittedForUser();
    	assertEquals(bool, true);
    }
    
    /**
     * Tests the logic to see if the Risk Declaration
     * is permitted for the user.
     *
     */
    @Test
    public void testIsAtRiskDeclarationPermittedForUserPermissionNoApprove() throws Exception {
    	final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    	populateContractInfo(null);
    	withdrawalRequest.setContractInfo(contractInfo);
    	withdrawalRequest.setRequestRiskIndicator(true);
    	withdrawalRequest.setIsParticipantCreated(true);
    	withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
    	boolean bool = withdrawalRequest.isAtRiskDeclarationPermittedForUser();
    	assertEquals(bool, false);
    }
    
    /**
     * Tests the logic to see if the Risk Declaration
     * is permitted for the user.
     *
     */
    @Test
    public void testIsAtRiskDeclarationPermittedForUserDraftStatus() throws Exception {
    	final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    	populateContractInfo(null);
    	withdrawalRequest.setContractInfo(contractInfo);
    	withdrawalRequest.setRequestRiskIndicator(true);
    	withdrawalRequest.setIsParticipantCreated(true);
    	withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
    	boolean bool = withdrawalRequest.isAtRiskDeclarationPermittedForUser();
    	assertEquals(bool, false);
    }
    
    /**
     * Tests the logic to see if the Risk Declaration
     * is permitted for the user.
     *
     */
    @Test
    public void testIsAtRiskDeclarationPermittedForUserRiskIndNo() throws Exception {
    	final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    	populateContractInfo(Boolean.TRUE);
    	withdrawalRequest.setContractInfo(contractInfo);
    	withdrawalRequest.setRequestRiskIndicator(false);
    	withdrawalRequest.setIsParticipantCreated(true);
    	withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
    	boolean bool = withdrawalRequest.isAtRiskDeclarationPermittedForUser();
    	assertEquals(bool, false);
    }
    
    /**
     * Tests the logic to see if the Risk Declaration
     * is permitted for the user.
     *
     */
    @Test
    public void testIsAtRiskDeclarationPermittedForUserNotPow() throws Exception {
    	final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    	populateContractInfo(Boolean.TRUE);
    	withdrawalRequest.setContractInfo(contractInfo);
    	withdrawalRequest.setRequestRiskIndicator(false);
    	withdrawalRequest.setIsParticipantCreated(false);
    	withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
    	boolean bool = withdrawalRequest.isAtRiskDeclarationPermittedForUser();
    	assertEquals(bool, false);
    }
    
    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWithdrawalRiskDeclarationPermittedToUser.class);
    }
    

}
