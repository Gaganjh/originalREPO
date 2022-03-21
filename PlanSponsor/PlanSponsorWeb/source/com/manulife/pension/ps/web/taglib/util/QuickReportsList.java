package com.manulife.pension.ps.web.taglib.util;

import java.io.IOException;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.PilotHelper;
import com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.report.reporthandler.SystematicWithdrawReportHandler;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.PilotCAR;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import org.apache.log4j.Logger;
/**
 * This class represents the quick report list. It reads the reports.xml
 * configuration file during initialization. Once initialized, the getReports()
 * method returns the list of reports the given user have access to.
 * 
 * @author Charles Chan
 */
public class QuickReportsList {

	//private static final String PERFORMANCE_CHARTING = "PERFORMANCE_CHARTING";

	//private static final String CONTRACT_FUNDS = "CONTRACT_FUNDS";

	private static final String CURRENT_LOAN_SUMMARY = "CURRENT_LOAN_SUMMARY";
	
	private static final String CONTRACT_STATEMENTS_REPORTS ="CONTRACT_STATEMENTS_REPORTS";

	//private static final String CENSUS_SUMMARY ="CENSUS_SUMMARY";
    
    //private static final String ELIGIBILITY ="ELIGIBILITY";
    
    private static final String VESTING ="VESTING";
    
    private static final String CONTRACT_SERVICE_FEATURE ="CONTRACT_SERVICE_FEATURE";
    
    private static final String CONTRACT_SERVICE_FEATURE_DB ="CONTRACT_SERVICE_FEATURE_DB";

    private static final String CONTRACT_PLAN_INFORMATION = "CONTRACT_PLAN_INFORMATION";
    
    private static final String STATEMENTS = "STATEMENTS";
    
    private static final String UNCASHED_CHECKS = "UNCASHED_CHECKS";
    
    private static final String IPSMANAGER = "IPSMANAGER";
    
    private static final String WILSHIRE321_REVIEW_REPORTS = "WILSHIRE321_REVIEW_REPORTS";
    
    private static final String NOTICE_MANAGER = "NOTICE_MANAGER";
    
    private static final String SEND_SERVICE = "SEND_SERVICE";
    
    private static final String SYS_WITHDRAW_QL = "SYS_WITHDRAW_QL";
    
    private static final String PAYROLL_SELF_SERVICE = "PAYROLL_SELF_SERVICE";

    private static final String DOCUMENT_TYPE_COFID = "CFD";
	private static final String DOCUMENT_SUB_TYPE_321 = "321";
	private static final String DOCUMENT_SUB_TYPE_338 = "338";

	private static final String REPORTS_XML = "./quickreportslist.xml";

	private static final String REPORTS_RULES_XML = "./quickreportslist-rules.xml";
	
	private static final Logger logger = Logger.getLogger(QuickReportsList.class);

	private static QuickReportsList instance = new QuickReportsList();

	private List reports = null;

	/**
	 * Constructor.
	 */
	private QuickReportsList() {
		super();
		initialize();
	}

	/**
	 * Returns the singleton ReportList.
	 * 
	 * @return The singleton ReportList.
	 */
	public static QuickReportsList getInstance() {
		return instance;
	}

	/**
	 * Returns a list of reports that the given user can access.
	 * 
	 * @param user
	 *            The user to check against.
	 * @return The list of reports that the given user can access.
	 */
	@SuppressWarnings("unchecked")
	public List getReports(UserProfile user) throws SystemException {
		List cloneReports = new ArrayList();
		boolean showWithdrawalToolLink;
		/*
		 * SPR.108 Non-Allocated Contract shows only Contract Funds and
		 * Performance Charting.
		 */
		/*if (user.getCurrentContract() != null
				&& !user.getCurrentContract().isContractAllocated()) {

			
			 * Add the "Select report" label and the following separator.
			 
			Iterator it = reports.iterator();
			cloneReports.add(it.next());
			cloneReports.add(it.next());

			for (; it.hasNext();) {
				Object obj = it.next();
				if (obj instanceof QuickReportItem) {
					QuickReportItem report = (QuickReportItem) obj;
					String id = report.getId();
					if (id.equals(CONTRACT_FUNDS)
							|| id.equals(PERFORMANCE_CHARTING)) {
						populateContent(report);
						cloneReports.add(report);
					}
				}
			}
		} else */{
			String typeOfWithdrawalToolLink =WithdrawalWebUtil.getTypeOfRequest(user);
	        // Get permission for withdrawals tool link
	        final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().getContractInfo(
	                user.getCurrentContract().getContractNumber(), user.getPrincipal());
	        WithdrawalRequestUi.populatePermissions(contractInfo, user.getPrincipal());
	        showWithdrawalToolLink= contractInfo.getShowWithdrawalToolsLink();

			/*
			 * Makes a copy of the original report list.
			 */
			cloneReports.addAll(reports);

			/*
			 * Check with security manager to make sure user can view the
			 * report. Remove those that user cannot see.
			 */
			com.manulife.pension.ps.web.controller.SecurityManager sm = com.manulife.pension.ps.web.controller.SecurityManager
					.getInstance();
			for (Iterator it = cloneReports.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj instanceof QuickReportItem) {
					
					if (!sm.isUserAuthorized(user, ((QuickReportItem) obj)
							.getUrl())) {
						it.remove();
					} else {
						QuickReportItem item = (QuickReportItem) obj;
						/*
						 * SPR.88.	If a contract does not have Loans as a
						 * product feature associated with the contract and
						 * there are no outstanding loans,  the following page
						 * elements will not be displayed (i.e. show them if
						 * the contract has the feature or there are
						 * outstanding loans)
						 * �	Loans outstanding label
						 * �	Number of loans
						 * �	Loan Amount
						 * �	Loan Summary Link
						 */
						if (item.getId().equals(CURRENT_LOAN_SUMMARY)) {
							if (!user.getCurrentContract().isLoanFeature()
									&& user.getCurrentContract()
											.getLoansTotalAmount()
											.doubleValue() == 0) {
								it.remove();
								continue;
							}
						}
						// do not display Contract Statements if the user does not have access
						if (item.getId().equals(CONTRACT_STATEMENTS_REPORTS)){
 							if (!user.isAllowedContractStatements()) {
 								it.remove();
 								continue;
 							}
						}
                        
						//do not display if the contract is  a Defined Benefit contract
                        if (item.getId().equals(CONTRACT_SERVICE_FEATURE)){
                            if (user.getCurrentContract().isDefinedBenefitContract()) {
                                it.remove();
                                continue;
                            }
                        }
                        
                        // do not display the "Uncashed checks" dropdown value
						// for Intermediary Contact users and  
						if (item.getId().equals(UNCASHED_CHECKS)) {
							boolean isInPilot = PilotHelper.isInPilot(user
									.getCurrentContract().getContractNumber(),
							"Uncashed Checks");
							if (!isInPilot) {
								it.remove();
								continue;
							}
						}
                            
                        //display Contract service feature link for Defined Benefit contract
                        if (item.getId().equals(CONTRACT_SERVICE_FEATURE_DB)){
                        	//do not display if the Defined Benefit contract is old i.e. Effective date < Consent CSF launch date
    						if (user.getCurrentContract().isDefinedBenefitContract()) {
                                BusinessParameterValueObject businessParameterObject = PartyServiceDelegate.getInstance().getBuisnessParameterValueObject();
                                Date effectiveDate = user.getCurrentContract().getEffectiveDate();
                                if (businessParameterObject != null){
                                    Date consentImplementationDate = businessParameterObject.getCsfContentWebLaunchDate();
                                
                                    if((effectiveDate != null) && (consentImplementationDate != null)){
                                        if (effectiveDate.before(consentImplementationDate)) {
                                            it.remove();
                                            continue;
                                        }
                                    }
                                }
                            //do not display if contract is other than Defined Benefit                                
                            } else { 
                                it.remove();
                                continue;
                            }
                        }

//						// do not display Eligibility report if contract AutoEnrollment feature is off
//                        if (item.getId().equals(ELIGIBILITY)){
//                            if (!CensusUtils.isAutoEnrollmentEnabled(user
//                                    .getCurrentContract().getContractNumber())) {
//                                it.remove();
//                                continue;
//                            }
//                        }

                        // do not display Vesting report if Vesting not enabled or 
                        // contract is a Defined Benefit contract or
                        // contract is not allocated
                        if (item.getId().equals(VESTING)){
                            if (!CensusUtils.isVestingEnabled(user.getCurrentContract().getContractNumber()) || 
                                user.getCurrentContract().isDefinedBenefitContract()/* ||
                                !user.getCurrentContract().isContractAllocated()*/) {
                                it.remove();
                                continue;
                            }
                        }
                        
                        // If IPS option is off dont show the IPS assist link
						if (IPSMANAGER.equals(item.getId())) {
							InvestmentPolicyStatementVO investmentPolicyStatementVO = ContractServiceDelegate
									.getInstance().getIpsBaseData(
											user.getCurrentContract()
													.getContractNumber());
							if (investmentPolicyStatementVO == null) {
								it.remove();
								continue;
							}
						}
						
						// If the contract is CoFid contract and service provider has past 24 months documents then show the
						// Wilshire 3(21) Review Reports link
						if (WILSHIRE321_REVIEW_REPORTS.equals(item.getId())) {

							ContractDocumentInfo[] contractDocInfo = null;

							try {
								contractDocInfo = com.manulife.pension.platform.web.contract.ContractDocumentsHelper
										.getContractDocuments(user.getCurrentContract());
							} catch (SystemException e) {
								logger.error("Service Provider doesn't have past 24 months documents "+ e.getMessage());

							} catch (ServiceUnavailableException e1) {

								logger.error("Report Service isn't available "+ e1.getMessage());
							}

							if (contractDocInfo != null) {

								for (int i = 0; i < contractDocInfo.length; i++) {

									if (contractDocInfo[i].getDocumentType() != null
											&& contractDocInfo[i].getDocumentSubType() != null
											&& (StringUtils
													.equalsIgnoreCase(contractDocInfo[i].getDocumentType().trim(),
															DOCUMENT_TYPE_COFID))
											&& ((StringUtils.equalsIgnoreCase(
													contractDocInfo[i].getDocumentSubType().trim(),
													DOCUMENT_SUB_TYPE_321)
													|| (StringUtils.equalsIgnoreCase(
															contractDocInfo[i].getDocumentSubType().trim(),
															DOCUMENT_SUB_TYPE_338))))) {
										if (contractDocInfo[i].getDocumentCreatedDate()
												.after(DataUtility.currentDateMinus24Months())
												|| user.getCurrentContract().getContractDates().getAsOfDate()
														.equals(DataUtility.currentDateMinus24Months())) {
											user.getCurrentContract().setServiceProviderHasPast24MonthsDocuments(true);
											break;
										}
									}
								}
							}

							boolean isCoFidContract = ContractServiceDelegate.getInstance()
									.checkCoFidContractIndicator(user.getCurrentContract().getContractNumber());
							if (/*
								 * !isCoFidContract &&
								 */ !user.getCurrentContract().isServiceProviderHasPast24MonthsDocuments()) {
								it.remove();
								continue;
							}
						}
						
                        // do not display plan information if 
                        // contract is a defined benefit one or
                        // external user with a CA contract
                        if (item.getId().equals(CONTRACT_PLAN_INFORMATION)){
                            String contractStatus = user.getCurrentContract().getStatus();
                            
                            boolean isDefineBenefit = user.getCurrentContract().isDefinedBenefitContract();
                            boolean isUserContractOK = (!user.isInternalUser() && !(PlanConstants.CONTRACT_STATUS_EXTERNAL_USER_ACCESS.contains(contractStatus))) ? false : true;
                            boolean isContractOK = (PlanConstants.CONTRACT_STATUS_NOT_FOR_QUICK_REPORT.contains(contractStatus)) ? false : true;
                            
                            if (isDefineBenefit || !isUserContractOK || !isContractOK) {
                                it.remove();
                                continue;
                            }
                        }
                        //  dynamically display �Loans and Withdrawals� or �Loans� or �Withdrawals� 
                        if (item.getId().equalsIgnoreCase(WithdrawalWebUtil.LOANANDWITHDRAWAL)){
                        	if (showWithdrawalToolLink && WithdrawalWebUtil.LOANANDWITHDRAWAL.equalsIgnoreCase(typeOfWithdrawalToolLink)){
                        		// display the Loans and Withdrawals  tool link
                        	}
                        	else{
                        		it.remove();
                        	}
                        }
                        if (item.getId().equalsIgnoreCase(WithdrawalWebUtil.WITHDRAWAL_ONLY)){
                        	if (showWithdrawalToolLink && WithdrawalWebUtil.WITHDRAWAL_ONLY.equalsIgnoreCase(typeOfWithdrawalToolLink)){
                        		// display Withdrawals tool link
                        	}
                        	else{
                        		it.remove();
                        	}
                        }
                        if (item.getId().equalsIgnoreCase(WithdrawalWebUtil.LOAN_ONLY)){
                        	if (showWithdrawalToolLink && WithdrawalWebUtil.LOAN_ONLY.equalsIgnoreCase(typeOfWithdrawalToolLink)){
                        		// display Loans tool link
                        	}
                        	else{
                        		it.remove();
                        	}
                        }
                        if (item.getId().equalsIgnoreCase(STATEMENTS)){
                        		item.setUrl("/do/participant/participantStatements");
                        }
						populateContent((QuickReportItem) obj);

					
					/* Notice Manager Display - Start */
						if (item.getId().equalsIgnoreCase(NOTICE_MANAGER)) {
							
							boolean isUserProfileOK = (user.getRole() instanceof InternalServicesCAR || user.getRole() instanceof PilotCAR || user.getRole() instanceof InternalUserManager ) ? true : false; // To restrict Pilot CAR and IS CAR (IUM)

							Contract contract = user.getCurrentContract();
							
							boolean contractRestriction;
							try {
								contractRestriction = NoticeManagerUtility.validateContractRestriction(contract);
							} catch (ContractDoesNotExistException e) {
								throw new SystemException(e,"Contract Details Not available");
							}
							boolean productRestriction = NoticeManagerUtility.validateProductRestriction(contract);
							boolean diContractStatus = NoticeManagerUtility.validateDIStatus(contract, user.getRole());
							
							if(isUserProfileOK || contractRestriction || productRestriction || diContractStatus)
							{
								it.remove();
                                continue;
							}

						}
						/* Notice Manager Display - End */
						
						/*Send Service display- start*/
							if (item.getId().equalsIgnoreCase(SEND_SERVICE)) {
							
							boolean isUserProfileOK = (user.getRole() instanceof InternalServicesCAR || user.getRole() instanceof PilotCAR || user.getRole() instanceof InternalUserManager ) ? true : false; // To restrict Pilot CAR and IS CAR (IUM)

							Contract contract = user.getCurrentContract();
							
							boolean contractRestriction;
							try {
								contractRestriction = NoticeManagerUtility.validateContractRestriction(contract);
							} catch (ContractDoesNotExistException e) {
								throw new SystemException(e,"Contract Details Not available");
							}
							boolean productRestriction = NoticeManagerUtility.validateProductRestriction(contract);
							boolean diContractStatus = NoticeManagerUtility.validateDIStatus(contract, user.getRole());
							
							if(isUserProfileOK || contractRestriction || productRestriction || diContractStatus || !user.isSendServiceAccessible())
							{
								it.remove();
                                continue;
							}

						}
						/*Send Service display- end*/
							
						/*Systematic Withdrawals display- start*/
						if (item.getId().equalsIgnoreCase(SYS_WITHDRAW_QL)) {
							Contract contract = user.getCurrentContract();
							int contractNumber = user.getCurrentContract()
									.getContractNumber();
							boolean sysLinkEnable = false;
							boolean reportData = false;

							boolean contractStatus = validateContractStatus(contract);

							boolean featurecode = ContractServiceDelegate
									.getInstance()
									.isSystematicWithdrawalFeatureON(contractNumber);

							if (contractStatus) {
								if (featurecode) {
									sysLinkEnable = true;
								} else { //using replica table instead of calling apollo stored proc
									reportData = getPlaDataWithdrawalStatusReasonCode(contractNumber);
									if (reportData) {
										sysLinkEnable = true;
									}
								}
							}
							if (!sysLinkEnable) {
								it.remove();
								continue;
							}
						}
					/* Systematic Withdrawals Display - End */
						
					
						/* Payroll Self Service link display - start */
						if (item.getId().equalsIgnoreCase(PAYROLL_SELF_SERVICE)) {
							int contractNumber = user.getCurrentContract().getContractNumber();
							boolean payrollFeedbackServiceEnabled = false;
							try {
								payrollFeedbackServiceEnabled = Boolean.TRUE.equals(ContractServiceFeatureUtil.hasContractServiceFeature(
										ContractServiceDelegate
											.getInstance()
											.getContractServiceFeatures(contractNumber),
										ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE,
										new HashSet<>(Collections.singletonList(ServiceFeatureConstants.YES))
										));
							} catch(ApplicationException | SystemException e) {
								throw new IllegalStateException("Failed while checking Payroll Feedback Service Feature flag for the contract ID [" + contractNumber + "]!", e);
							}
							
							if (!payrollFeedbackServiceEnabled) {
								it.remove();
								continue;
							}
						}
						/* Payroll Self Service link Display - End */
						
				}
			}
		}

        removeRedundantSeparators(cloneReports);
        
		return cloneReports;
		}
	}
	

	/*
	 * Obtains titles from CMA. These items cannot be cached because we need
	 * different contents for NY and US site.
	 */
	private void populateContent(QuickReportItem report) throws SystemException {
		String contentId = report.getContentId();
		if (contentId != null) {
			try {
				Content reportContent = ContentCacheManager.getInstance()
						.getContentById(Integer.parseInt(contentId),
								ContentTypeManager.instance().MISCELLANEOUS);
				if (reportContent != null) {
					report.setTitle(ContentUtility.getContentAttribute(
							reportContent, "title"));
				}
			} catch (ContentException e) {
				throw new SystemException(e, getClass().getName(),
						"populateContent", "Cannot obtain content for ["
								+ contentId + "]");
			}
		}
	}

    /**
     * Sometimes there are 2 separtors running together when
     * all the reports in the section have been removed because
     * of security.  This routine removes the redundant separators.
     * 
     */
    private void removeRedundantSeparators(List cloneReports) {
        
        int separatorIndex = -1;
        Iterator it = cloneReports.iterator();
        for (int i = 0; i < cloneReports.size(); i++) {
            Object obj = it.next();
            if (obj instanceof QuickReportSeparator) {
                if (separatorIndex == i - 1) {
                    it.remove();
                }
                separatorIndex = i;
             }
        }
    }

	/**
	 * Gets a digester using the XML rules file.
	 * 
	 * @return A digster object with rules from the XML rules file.
	 */
	private Digester getDigester() {

		URL rules = getClass().getClassLoader().getResource(REPORTS_RULES_XML);

		if (rules == null) {
			throw new IllegalStateException("Rules [" + REPORTS_RULES_XML
					+ "] not found.");
		}

		/*
		 * Creates a digester which the rules XML file.
		 */
		Digester digester = DigesterLoader.createDigester(rules);
        digester.setUseContextClassLoader(true);
        return digester;
	}

	/**
	 * Uses the xml digester to read the property file conaining list of
	 * reports.
	 */
	private void initialize() {

		Digester digester = getDigester();

		try {
			/*
			 * Parses the data file using the created digester.
			 */
			URL reportsXml = getClass().getClassLoader().getResource(
					REPORTS_XML);

			if (reportsXml == null) {
				throw new IllegalStateException("Reports XML [" + REPORTS_XML
						+ "] not found.");
			}

			InputStream is = reportsXml.openStream();

			reports = (ArrayList) digester.parse(is);

			is.close();

		} catch (SAXException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
	  public Boolean getPlaDataWithdrawalStatusReasonCode(Integer contractNumber) throws SystemException {
	    	Boolean withdrawalStatusReasonCode=false;
	    	List<SystematicWithdrawDataItem> reportItems= ContractServiceDelegate.getInstance().getSystematicWithdrawalData(contractNumber, null);
				if(null!=reportItems && reportItems.size()>0){
					withdrawalStatusReasonCode = true;
				}
			
			 return withdrawalStatusReasonCode;
		}
	  /**
	     * Method used to validate the contract status (AC,CF,DI).
	     * 
	     * @param userProfile
	     * @return boolean
	     * @throws SystemException
	     */
	    public static boolean validateContractStatus(Contract contract) throws SystemException {
	        String contractStatus = contract.getStatus();

	        if (Contract.STATUS_PROPOSAL_SIGNED.equalsIgnoreCase(contractStatus)
	                || Contract.STATUS_ACTIVE_CONTRACT.equalsIgnoreCase(contractStatus)
	                || Contract.STATUS_CONTRACT_FROZEN.equalsIgnoreCase(contractStatus)
	                || Contract.STATUS_CONTRACT_DISCONTINUED.equalsIgnoreCase(contractStatus)) {
	            return true;
	        } else {
	            return false;
	        }
	    }
}