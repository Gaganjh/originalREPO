package com.manulife.pension.ps.service.participant;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.ManagedAccountParticipantServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.participant.dao.ParticipantAccountDAO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.service.report.participant.dao.ParticipantDeferralChangesDAO;
import com.manulife.pension.ps.service.report.transaction.dao.CareActsLoanReAmortizationDAO;
import com.manulife.pension.service.account.dao.ParticipantBenefitBaseDAO;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.managedaccount.enums.ParticipantManagedAccountServiceType;
import com.manulife.pension.service.managedaccount.enums.ParticipantManagedAccountStatus;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.AbstractPrincipal;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;

/**
 * Bean implementation class for Enterprise Bean: ParticipantService
 */
public class ParticipantServiceBean implements SessionBean {
	/**
	 * Default Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(ParticipantServiceBean.class);

	private static final String SOURCE_WEB_SITE = "PS";
	private SessionContext mySessionCtx;

	public static final String AUTO_ENROLLMENT = "AE";
    public static final String OPT_OUT = "OOD";
	public static final String MANAGING_DEFERRALS = "MD";
	public static final String medDeferralType = "DFT";
	public static final String AUTO_CONTRIBUTION = "ACI";
    public static final String aciDefaultAnnualIncreaseByPercent = "AIP";
    public static final String aciDefaultAnnualIncreaseByAmount = "AID";
    public static final String aciDefaultDeferralLimitByPercent = "DLP";
    public static final String aciDefaultDeferralLimitByAmount = "DLD";
    public static final String ACI_DEFAULTED_TO_YES="DY";
    public static final String aciAnniversaryDate = "ADT";
    
    private static final String MANAGED_ACCOUNT_NO_INDICATOR = "No";
	private static final String MANAGED_ACCOUNT_YES_INDICATOR = "Yes";
    private static final String MANAGED_ACCOUNT_PENDING_INDICATOR = "Pending";
    
    //GIFL 1C
    public static final String GIFL_DESELECTED_DATE = "9999-12-31";


	/**
	 * getSessionContext
	 */
	public SessionContext getSessionContext() {
		return mySessionCtx;
	}

	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}

	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}

	public String getProfileIdByParticipantId(String participantId,
			int contractNumber) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getProfileIdByParticipantId");

		String profileId = null;

		try {
			profileId = ParticipantAccountDAO.getProfileIdByParticipantId(
					participantId, contractNumber);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,
			        "Unchecked exception occurred. Input Paramereters are "
							+ "participantId:" + participantId
							+ ", contractNumber:" + contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getProfileIdByParticipantId");

		return profileId;
	}

	public ParticipantAccountVO getParticipantAccount(AbstractPrincipal principal,int contractNumber,
			String productId, String profileId, Date asOfDate, boolean retrieveNetEEDeferralContibutions, boolean organizeFundsByAssetClass) throws RemoteException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantAccount");

		ParticipantAccountVO vo = null;

		try {
			vo = ParticipantAccountDAO.getParticipantAccountDetails(
					contractNumber, productId, profileId, asOfDate, organizeFundsByAssetClass);

			// If the netEEDeferralContibutions are going to be retrieved
			// and the user is either Internal User or TPA/TPAUM
			// thenm retrieve the NetEEDeferralContributions
			if ( vo.getParticipantAccountDetailsVO().getSsn() != null && // if the record could be retrieved from CSDB
					retrieveNetEEDeferralContibutions &&
					(principal.getAbstractUserRole() instanceof InternalUser ||
					principal.getAbstractUserRole() instanceof ThirdPartyAdministrator ||
					principal.getAbstractUserRole() instanceof BDUserRole) ) {

				try {
					//	get the Net employee Contribution
					double netEEDeferralContributions = ParticipantAccountDAO.getParticipantNetEEDeferralContributions(
							contractNumber, vo.getParticipantAccountDetailsVO().getSsn(), asOfDate);
					

					ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();           
			        String eedefInd = delegate.getEEDEFEarningsFlagInd(contractNumber);
			        //US32157 Add Maximum Hardship Field to PSW EE Deferrals Tab
			        //  code cleanup done after Jan 1, 2019 - for US32361 Hardship Post-January 1st Code Cleanup - eComm
			       
			        if("Y".equalsIgnoreCase(eedefInd)) {
			        	vo.getParticipantAccountDetailsVO().setMaximumHardshipAmount(vo.getParticipantAccountDetailsVO().getNetEEDeferralContributions());
			        }else {
			        	if (  netEEDeferralContributions < vo.getParticipantAccountDetailsVO().getNetEEDeferralContributions() ) {
			        		vo.getParticipantAccountDetailsVO().setMaximumHardshipAmount(netEEDeferralContributions);
			        	}else {
			        		vo.getParticipantAccountDetailsVO().setMaximumHardshipAmount(vo.getParticipantAccountDetailsVO().getNetEEDeferralContributions());
			        	}
			        }
					// Ths following code added in order to PPR.424
					// We need to display the lesser amount of the CSDB EEDEF value
					// or Apollo STP result
					if (  netEEDeferralContributions < vo.getParticipantAccountDetailsVO().getNetEEDeferralContributions() )
						vo.getParticipantAccountDetailsVO().setNetEEDeferralContributions(netEEDeferralContributions);

					vo.getParticipantAccountDetailsVO().setNetEEDeferralContributionsAvailable(true);

					// get data for the updated[ACI project] net EE deferrals tab
					ParticipantDeferralVO deferralVO = getParticipantDeferralData(contractNumber, profileId);
					vo.getParticipantAccountDetailsVO().setParticipantDeferralVO(deferralVO);
					
					
				} catch (SystemException e) {
				    // process if batch running;  re-throw otherwise
				    if (e.getCause() instanceof SQLException) {
				        SQLException sqle = (SQLException) e.getCause();
				        if (ParticipantAccountDAO.ERROR_APOLLO_SP_BATCH.equals(sqle.getMessage())) {
		                    // set the new Employee Contribution N/A
		                    vo.getParticipantAccountDetailsVO().setNetEEDeferralContributionsAvailable(false);
		                    vo.getParticipantAccountDetailsVO().setNetEEDeferralContributions(0.0);	
		                    vo.getParticipantAccountDetailsVO().setMaximumHardshipAmount(0.0);	
				        }
				    } else {
				        throw e;
				    }
				}
			}
			
			ContractServiceDelegate delegate = ContractServiceDelegate
					.getInstance();
			vo.getParticipantAccountDetailsVO().setLiaDetailsVO(
					delegate.getLIADetailsByProfileId(profileId));
			
			updateManagedAccountParticipantStatus(contractNumber, Long.valueOf(profileId), vo.getParticipantAccountDetailsVO());

		} catch (ApplicationException ae) {
			 ExceptionHandlerUtility.handleApplicationException(ae);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,
			        "Unchecked exception occurred. Input Paramereters are "
							+ "contractNumber:" + contractNumber
							+ ", profileId:" + profileId + ", asOfDate:"
							+ asOfDate);
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantAccount");

		return vo;

	}

	
	private void updateManagedAccountParticipantStatus(int contractNumber, Long profileId,
			ParticipantAccountDetailsVO participantAccountDetailsVO) throws SystemException, IllegalArgumentException {
		ManagedAccountServiceFeatureLite managedAccountService = ContractServiceDelegate.getInstance()
				.getContractSelectedManagedAccountServiceLite(contractNumber);
		if (managedAccountService != null) {
			ParticipantManagedAccountServiceType managedAccountServiceType = ParticipantManagedAccountServiceType
					.get(managedAccountService.getServiceCode());
			ParticipantManagedAccountStatus participantManagedAccountStatus = ManagedAccountParticipantServiceDelegate
					.getInstance()
					.getParticipantManagedAccountStatus(contractNumber, profileId, managedAccountServiceType);
			participantAccountDetailsVO.setManagedAccountStatusValue(getManagedAccountStatusValue(participantManagedAccountStatus));
		}
	}

	private String getManagedAccountStatusValue(ParticipantManagedAccountStatus participantManagedAccountStatus) {
		switch (participantManagedAccountStatus) {
		case OPTED_IN:
			return MANAGED_ACCOUNT_YES_INDICATOR;
		case OPT_IN_PENDING:
			return MANAGED_ACCOUNT_PENDING_INDICATOR;
		default:
			return MANAGED_ACCOUNT_NO_INDICATOR;
		}
	}
	
	
    // TODO: check on ApplicationException throwing
	private ParticipantDeferralVO getParticipantDeferralData(int contractNumber, String profileId) throws ApplicationException, SystemException  {
		// call stored proc for db items.
		ParticipantDeferralVO deferralVO = ParticipantAccountDAO.getParticipantNetEEDeferralData(contractNumber, profileId);
				
        ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();           
        ContractServiceFeature csf = delegate.getContractServiceFeature(contractNumber, MANAGING_DEFERRALS);
        
        if (csf != null) {
            int optOutNumberOfDays = 0;
            String optOutDays = csf.getAttributeValue(OPT_OUT);
            try {
                optOutNumberOfDays = Integer.parseInt(optOutDays);
                
                deferralVO.setOptOutDays(optOutNumberOfDays);
            } catch (NumberFormatException e) {
                // It should be un-reacheable
            }
        }
                
        String planDeferralType = null;
        if (csf != null) {
            planDeferralType = csf.getAttributeValue(medDeferralType);            	
        	deferralVO.setContractDeferralType(planDeferralType);
        }
        
        PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(contractNumber);
        deferralVO.setAnnualApplyDate(planData.getAciAnnualApplyDate());
        deferralVO.setPlanYearEndDate(planData.getPlanYearEnd());
        
        ContractServiceFeature aciCSF = delegate.getContractServiceFeature(contractNumber, AUTO_CONTRIBUTION);

        if (aciCSF != null) {
        	String autoOrSignup = ContractServiceDelegate.getInstance().determineSignUpMethod(contractNumber);
        	deferralVO.setAuto(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoOrSignup));
        	deferralVO.setSignUp(ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoOrSignup));
        	if (deferralVO.isAuto()) {
        		deferralVO.setContractDefaultIncreaseAmount(aciCSF.getAttributeValue(aciDefaultAnnualIncreaseByAmount));
        		deferralVO.setContractDefaultLimitAmount(aciCSF.getAttributeValue(aciDefaultDeferralLimitByAmount));
        		deferralVO.setContractDefaultIncreasePercent(String.valueOf(planData.getAciDefaultIncreasePercent().intValue()));
        		deferralVO.setContractDefaultLimitPercent(String.valueOf(planData.getAciDefaultAutoIncreaseMaxPercent().intValue()));
        	} else if (deferralVO.isSignUp()) {
        		deferralVO.setContractDefaultIncreasePercent(aciCSF.getAttributeValue(aciDefaultAnnualIncreaseByPercent));
        		deferralVO.setContractDefaultIncreaseAmount(aciCSF.getAttributeValue(aciDefaultAnnualIncreaseByAmount));
        		deferralVO.setContractDefaultLimitPercent(aciCSF.getAttributeValue(aciDefaultDeferralLimitByPercent));
        		deferralVO.setContractDefaultLimitAmount(aciCSF.getAttributeValue(aciDefaultDeferralLimitByAmount));
        	}
        	deferralVO.setContractAciAnniversaryDate(delegate.getContractAnniversaryDate(contractNumber));
        	if (aciCSF !=null &&ContractServiceFeature.internalToBoolean(aciCSF.getValue()).booleanValue()) {
        		deferralVO.setCSFACIValue(true);
        	} else {
        		deferralVO.setCSFACIValue(false);
        	}	        
        }
 
        deferralVO.calculateDerrivedFields();
                        
		return deferralVO;
	}
		

	
	public void updateDeferralProcessInd(int contractNumber, double profileId, Timestamp createTS, boolean processInd)	{
		if ( logger.isDebugEnabled() )
			logger.debug("entry -> updateDeferralProcessInd");


		try {
			ParticipantDeferralChangesDAO.updateDeferralIndicator(contractNumber, profileId, createTS, processInd);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		catch (RuntimeException e) {
			SystemException se = new SystemException(e,
					"Unchecked exception occurred. Input Paramereters are "+
					"contractNumber:"+contractNumber+
					", profileId:"+profileId+
					", createTS:"+createTS+
					", processInd:"+processInd);
			throw ExceptionHandlerUtility.wrap(se);
		}

		if ( logger.isDebugEnabled() )
			logger.debug("exit <- updateDeferralProcessInd");

	}


    // placed here to get the two updates to happen in one transaction. [8.11.5]
    public void declineACIRequest(String contractNumber, String profileId, 
            long createdTS, int instructionNo, String remarks, 
            Long userId, Boolean isInternal, Long processedTimestamp,
            boolean isADHocRequest) throws SystemException {

    	
    	EmployeeServiceDelegate esDelegate = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
    	esDelegate.processACIRequest(contractNumber, profileId, createdTS, instructionNo, userId,
    			  isInternal, processedTimestamp, SOURCE_WEB_SITE, remarks, false);
 
    }
    
    // not needed to be here, but want to be consistent with the decline(above) which must be here
    public void approveACIRequest(String contractNumber, String profileId,
    		long createdTS, int instructionNo, Long userId,  
    		Boolean isInternal, Long processedTimestamp) throws SystemException {
    	
    	EmployeeServiceDelegate esDelegate = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
        esDelegate.processACIRequest(contractNumber, profileId, createdTS, instructionNo, 
        		 userId, isInternal, processedTimestamp, SOURCE_WEB_SITE, null, true);
    }

    //Gateway Phase1 starts
    /**
     * Retrieves the participantId form profileId
     * @param profileId long
     * @param contractNumber int
     * @throws SystemException
     * @throws RuntimeException 
     */
    
    public int getParticipantIdByProfileId(long profileId, int contractNumber) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantIdByProfileId");

		int  participantId=0 ;

		try {
			participantId = ParticipantAccountDAO.getParticipantIdByProfileId(profileId, contractNumber);
			} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,
					"Unchecked exception occurred. Input Paramereters are "
							+ "profileId:" + profileId
							+ ", contractNumber:" + contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantIdByProfileId");

		return participantId;
	}
    //Gateway Phase1C starts
    /**
     * Retrieves the ParticipantGIFLStatus based on participantID
     * @param participantID long
     * @param contractNumber int
     * @throws SystemException
     * @throws RuntimeException 
     */

    public String getParticipantGIFLStatus(String participantId, String contractNumber) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantGIFLStatus");

		String  giflDeselectionStatus = "";

		try {
			//giflDeselectionStatus = ParticipantAccountDAO.getParticipantGIFLStatus(profileId, contractNumber);
			ParticipantBenefitBaseDAO participantBenefitBaseDAO = new ParticipantBenefitBaseDAO();
			ParticipantGiflData giflData = participantBenefitBaseDAO.
											getParticipantGiflDataByParticipantIdAndContractId(contractNumber, participantId);
			giflDeselectionStatus =  getParticipantGIFLStatusAsSelect(giflData == null ? null 
																					:giflData.getDisplayGiflDeselectionDate());
			} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,
					"Unchecked exception occurred. Input Paramereters are "
							+ "participantId:" + participantId
							+ ", contractNumber:" + contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantIdByProfileId");

		return giflDeselectionStatus;
	}
    //This method returns the GIFL status as boolean.
    public boolean getParticipantGIFLStatusAsBoolean(String participantId, String contractNumber) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantGIFLStatusAsBoolean");
		String giflDeselectionStatus = getParticipantGIFLStatus(participantId, contractNumber);
		if(giflDeselectionStatus != null && !giflDeselectionStatus.equals("")){
			return true;
		}

			if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantGIFLStatusAsBoolean");

		return false;   	
    }
//  This method returns the GIFL status as Selected/Deselected/empty.
    private String getParticipantGIFLStatusAsSelect(String giflDeselectionDate){
    	String  giflDeselectionStatus = "";
	 	if ( giflDeselectionDate != null && giflDeselectionDate.toString().equals(GIFL_DESELECTED_DATE)) {
	 		return "Selected";
	 	}else if(giflDeselectionDate != null && !giflDeselectionDate.equals(GIFL_DESELECTED_DATE)){
	 		return "DeSelected";
	 	}
	 	return giflDeselectionStatus;
    }
    
    public void updateLoanDetails(String[] loanDetails,ArrayList<String> unSeletedLoans, int contractNumber, String Tpaid,String tpaProfileName) throws SystemException {

		try {
			CareActsLoanReAmortizationDAO.updateLoanDetails(loanDetails,unSeletedLoans, contractNumber, Tpaid,tpaProfileName);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, "Unchecked exception occurred. Input Paramereters are "
					+ "profileId:" + Tpaid + ", contractNumber:" + contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}

	}

	public List<String> getExistingParticipantIDS(int contractNumber) throws SystemException {
		List<String> existingParticipnatIDs;
		try {
			existingParticipnatIDs = CareActsLoanReAmortizationDAO.getExistingParticipantIDS(contractNumber);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e,
					"Unchecked exception occurred. Input Paramereters are " + " contractNumber:" + contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return existingParticipnatIDs;
	}
}
