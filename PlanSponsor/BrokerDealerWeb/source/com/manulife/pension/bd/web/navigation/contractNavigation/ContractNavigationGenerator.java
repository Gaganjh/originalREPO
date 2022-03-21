package com.manulife.pension.bd.web.navigation.contractNavigation;

import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.encoder.Encode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.investment.FeeDisclosureUtility;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.BDAuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.navigation.UserMenu;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.navigation.UserNavigation;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.report.reporthandler.SystematicWithdrawReportHandler;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem;
import com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This class will generate the Contract Navigation Menu Items to be displayed in the contract
 * specific report pages.
 * 
 * @author HArlomte
 * 
 */
public class ContractNavigationGenerator {
    
    private static ContractNavigationGenerator instance;
    
    public static final String MODIFIED_PARTICIPANT_SUMMARY_TITLE = "Participant List";
    public static final String  IS_LIVESITE_VARIABLE = "isLiveSite";
	 public static final String  IS_FCP_LIVE_VARIABLE = "isFcpLive";

    private SecurityManager securityManager;
    private UserMenuItem userMenuItems;
    private static final String DOCUMENT_TYPE_COFID = "CFD";
	private static final String DOCUMENT_SUB_TYPE_321 = "321";
	private static final String DOCUMENT_SUB_TYPE_338 = "338";
    private static final Logger logger = Logger.getLogger(ContractNavigationGenerator.class);

    /**
     * returns an instance of ContractNavigationGenerator
     * 
     * @param request
     * @return ContractNavigationGenerator
     * 
     * @throws SystemException
     */
    public static ContractNavigationGenerator getInstance(HttpServletRequest request)
            throws SystemException {
        if (instance == null) {
            synchronized (ContractNavigationGenerator.class) {
                if (instance == null) {
                    instance = new ContractNavigationGenerator(request);
                }
            }
        }
        return instance;
    }

    private ContractNavigationGenerator(HttpServletRequest request) throws SystemException {
        ServletContext context = request.getSession().getServletContext();
        securityManager = ApplicationHelper.getSecurityManager(context);
        userMenuItems = getUserMenuItemDetails();
    }

    /**
     * This method creates a UserNavigation object containing all the Contract Navigation Menu Items
     * to be displayed in the given contract specific page.
     * 
     * @param request - The HttpServletRequest object.
     * @return - UserNavigation object containing the UserMenuItems to be displayed.
     * @throws SystemException
     */
    public UserNavigation generateUserNavigation(BDUserProfile userProfile, BobContext bobContext)
            throws SystemException {
        UserNavigation userNavigation = new UserNavigation();

        AuthorizationSubject subject = createAuthorizationSubject(userProfile, bobContext);
        UserMenu userMenu = new UserMenu();
        userMenu = generateMainMenu(userMenuItems, subject);
        userNavigation.setUserMenu(userMenu);

        return userNavigation;
    }

    /**
     * geneartes the main menu
     * 
     * @param systemMenu
     * @param subject
     * 
     * @return UserMenu
     * 
     * @throws SystemException
     */
    private UserMenu generateMainMenu(UserMenuItem userMenu, AuthorizationSubject subject)
            throws SystemException {
        UserMenu menu = new UserMenu();
        UserMenuItem top = generateMenu(userMenu, subject);
        if (top != null) {
            for (UserMenuItem item : top.getSubMenuItems()) {
                menu.addLevelOneUserMenuItem(item);
            }
        }
        return menu;
    }

    /**
     * Generates the lower level menu items
     * 
     * @param menuItem
     * @param subject
     * @return UserMenuItem
     * @throws SystemException
     */
    private UserMenuItem generateMenu(UserMenuItem menuItem, AuthorizationSubject subject)
            throws SystemException {
        if (!menuItem.hasChildren()) { // leaf menu item
        	
        	/**
        	 * Production fix for not to show planReviewReport Link on live
        	 * 
        	 * <bean parent="menuItem" p:id="planReviewReports" p:title="Plan Review Reports"
								p:actionURL="/do/bob/contract/planReview/" />
        	 * 
        	 */
        	
        	if(StringUtils.equalsIgnoreCase("planReviewReports", menuItem.getId())){
        		
            	if(!PlanReviewReportUtils.isPlanReviewLaunched()) {
    	        	if(!PlanReviewReportUtils.isPlanReviewFunctionalityAvailable()) {
    	        		
    	        		//  if the  plan review launched is 'false' and 
    	        		//  if the plan review available is false 
    	        		// -- >  Plan Review Reports link will suppressed
    	        		
    	        		return null;
    	        	}
            	}
			}	
        	
            if (securityManager.isUserAuthorized(subject, menuItem.getActionURL())) {
                return getSubMenuItem(menuItem, subject);
            } else {
                return null;
            }
        } else {
            List<UserMenuItem> sChildren = menuItem.getSubMenuItems();
            List<UserMenuItem> uChildren = new ArrayList<UserMenuItem>(10);
            for (UserMenuItem c : sChildren) {
                // if the list already contains the same id, then ignore it
                if (!containsId(uChildren, c.getId())) {
                    UserMenuItem newItem = generateMenu(c, subject);
                    if (newItem != null) {
                        uChildren.add(newItem);
                    }
                }
            }
            if (uChildren.size() > 0) {
                UserMenuItem menu = new UserMenuItem(menuItem.getId(), menuItem.getTitle(),
                        uChildren.get(0).getActionURL());
                for (UserMenuItem sub : uChildren) {
                    menu.addSubMenuItem(sub);
                }
                return menu;
            } else {
                return null;
            }
        }
    }

    /**
     * Check if the list of UserMenuItem already contains the <code>id</code>
     * 
     * @param children
     * @param id
     * @return
     */
    private boolean containsId(List<UserMenuItem> children, String id) {
        for (UserMenuItem item : children) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the sub menu items based on the the navigation rules
     * 
     * @param menuItem
     * @param subject
     * 
     * @return UserMenuItem
     * 
     * @throws SystemException
     * @throws ContentException 
     */
    private UserMenuItem getSubMenuItem(UserMenuItem menuItem, AuthorizationSubject subject)
            throws SystemException {

        BobContext bob = ((BDAuthorizationSubject) subject).getBobContext();
        
        BaseEnvironment environment = new BaseEnvironment();

		boolean isLiveNaming = Boolean.parseBoolean(environment
				.getNamingVariable(IS_LIVESITE_VARIABLE,null));
		
		boolean isFcpLiveNaming = Boolean.parseBoolean(environment
				.getNamingVariable(IS_FCP_LIVE_VARIABLE,null));
        
        boolean isDB06OrDBNY06Product = false;
        
        if("DB06".equalsIgnoreCase( bob.getContractProfile().getContract().getProductId()) ||
        		"DBNY06".equalsIgnoreCase( bob.getContractProfile().getContract().getProductId()))
        	isDB06OrDBNY06Product = true;

        if (BDConstants.CONTRACT_DOCUMENTS_ID.equals(menuItem.getId())) {
            if (isContractDocumentLinkEnabled(subject)) {
                return getContractUserMenuItem(menuItem);
            } else {
                return null;
            }
        } else if (BDConstants.CURRENT_LOAN_SUMMARY.equals(menuItem.getId())) {
            if (isContractLoanEnabled(subject)) {
                return getContractUserMenuItem(menuItem);
            } else {
                return null;
            }
        } else if (BDConstants.PARTICIPANT_ACCOUNT.equals(menuItem.getId())
                || BDConstants.PARTICIPANT_TXN_HISTORY.equals(menuItem.getId())) {
            if (bob.getPptProfileId() != null) {
                return getParticipantUserMenuItem(menuItem, bob.getPptProfileId());
            } else {
                return null;
            }
        } else if (BDConstants.ROR_CALCULATOR.equals(menuItem.getId())) {
        	if(isDB06OrDBNY06Product)
        		return null;
            if (bob.getPptProfileId() != null) {
                return getParticipantUserMenuItem(menuItem, bob.getPptProfileId());
            } else {
            	return null;
            }            
        }else if (BDConstants.PARTICIPANT_STATEMENTS_RESULTS.equals(menuItem.getId())) {
            if (bob.getPptProfileId() != null) {
                return getParticipantStatementUserMenuItem(menuItem, bob.getPptProfileId());
            } else {
                return getContractUserMenuItem(menuItem);
            }
        } else if (BDConstants.PARTICIPANT_SUMMARY.equals(menuItem.getId())) {
            if (bob.getPptProfileId() != null) {
                return getParticipantSummaryUserMenuItem(menuItem);
            } else {
                return getContractUserMenuItem(menuItem);
            }
        } else if (BDConstants.PARTICIPANT_BENEFIT_BASE_INFORMATION.equals(menuItem.getId())) {
            if (isBenefitBasePageAccessible(bob)) {
				return getParticipantUserMenuItem(menuItem, bob
						.getPptProfileId());
			} else {
				return null;
			}
        } else if (BDConstants.IPS_MANAGER_ID.equals(menuItem.getId())) {
            if (isIPSManagerAvailable(bob)) {
           	 return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		} else if (BDConstants.COFID321_ID.equals(menuItem.getId())) {
			if (isCoFidContract(bob)) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		} else if (BDConstants.COFID_FUND_RECOMMENDATION_ID.equals(menuItem.getId())) {
			if (isCofidFundRecommendationAvailable(bob)) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		} else if (BDConstants.REGULATORY_DISCLOSURES.equals(menuItem.getId())) {
			if (isRegulatoryDisclosuresAvailable(bob)) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		}else if (BDConstants.COFID_FUND_RECOMMENDATION_ID.equals(menuItem.getId())) {
			if (isCofidFundRecommendationAvailable(bob)) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		}else if (BDConstants.SYSTEMATIC_WITHDRAWAL_REPORT_ID.equals(menuItem.getId())) {
			if (isSYWContract(bob)) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		}else if (BDConstants.FUND_ADMIN_ID.equals(menuItem.getId())) {  //Fund Administration Tab changes
			if (!isLiveNaming || isFcpLiveNaming) {
				return getContractUserMenuItem(menuItem);
			} else {
				return null;
			}
		}else {
			return getContractUserMenuItem(menuItem);
		}
    }

    /**
     * returns contract menu item
     * 
     * @param menuItem
     * @return UserMenuItem
     */
    private UserMenuItem getContractUserMenuItem(UserMenuItem menuItem) {
        return new UserMenuItem(menuItem.getId(), menuItem.getTitle(), menuItem.getActionURL());
    }

    /**
     * returns the menu item with action url appeneded with profile id
     * 
     * @param menuItem
     * @param profileId
     * @return UserMenuItem
     */
    private UserMenuItem getParticipantUserMenuItem(UserMenuItem menuItem, String profileId) {
        return new UserMenuItem(menuItem.getId(), menuItem.getTitle(), menuItem.getActionURL()
                + "?profileId=" + Encode.forHtmlContent(profileId));
    }
    
    /**
     * returns the menu item with action url appeneded with profile id
     * 
     * @param menuItem
     * @param profileId
     * @return UserMenuItem
     */
    private UserMenuItem getParticipantStatementUserMenuItem(UserMenuItem menuItem, String profileId) {
        return new UserMenuItem(menuItem.getId(), menuItem.getTitle(), "/do/bob/participant/participantStatementResults/?task=fetchStatements"
                + "&profileId=" + Encode.forHtmlContent(profileId));
    }
    
    /**
     * returns the menu item with renamed ppt summary title
     * 
     * @param menuItem
     * @param profileId
     * @return UserMenuItem
     */
    private UserMenuItem getParticipantSummaryUserMenuItem(UserMenuItem menuItem) {
        return new UserMenuItem(menuItem.getId(), MODIFIED_PARTICIPANT_SUMMARY_TITLE , menuItem.getActionURL());
    }

    /**
     * This method checks to see if the "Contract Documents" Link should be displayed or not.
     * 
     * @param request - The HttpServletRequest object.
     * @return - true, if the "Contract Documents" link should be displayed, else, returns false.
     * @throws SystemException
     */
    public boolean isContractDocumentLinkEnabled(AuthorizationSubject subject)
            throws SystemException {
        Contract contract = ((BDAuthorizationSubject) subject).getBobContext().getCurrentContract();

        boolean showContractDocumentLink = false;

        if (contract != null) {
            ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
            boolean isPaperConsentGiven = false;
            boolean isContractDocPresent = false;
            try {
                isContractDocPresent = contractServiceDelegate
                        .checkContractHasAmendmentOrContractDocuments((new Integer(contract.getContractNumber()))
                                .toString());
                isPaperConsentGiven = contractServiceDelegate.checkContractConsent(contract
                        .getContractNumber());
            } catch (SystemException e) {
                isPaperConsentGiven = false;
            } catch (ApplicationException e) {
                isPaperConsentGiven = false;
            }
            if (isContractDocPresent) {
                if (((BDUserProfile) subject.getUserProfile()).isInternalUser()) {
                    showContractDocumentLink = true;
                } else if (isPaperConsentGiven) {
                    showContractDocumentLink = true;
                }
            }
        }
        return showContractDocumentLink;
    }
    
    
    /**
     * This method returns true if the current contract has either: 1. the Loan Feature turned ON or
     * 2. There is at least one current outstanding loan.
     * 
     * @param bobContext - The BobContext object.
     * @return - "true" if the contract has the loan enabled or there is at least one outstanding
     *         loan, else, returns "false"
     */
    public boolean isContractLoanEnabled(AuthorizationSubject subject) {
        boolean contractLoanEnabled = false;

        Contract currentContract = ((BDAuthorizationSubject) subject).getBobContext()
                .getCurrentContract();

        if (currentContract != null) {
            if (currentContract.isLoanFeature()
                    || (currentContract.getLoansTotalAmount() != null && currentContract
                            .getLoansTotalAmount().doubleValue() != 0)) {
                contractLoanEnabled = true;
            }
        }
        return contractLoanEnabled;
    }

    /**
     * This method helps in reading the "ContractNavigationMenu.xml" file. This file contains the
     * information regarding the Menu Items to be displayed in Contract navigation Menu bar.
     * 
     * @return - UserMenuItem that contains all the information present in
     *         ContractNavigationMenu.xml file.
     * @throws SystemException - is thrown if unable to get the xml file.
     */
    public static UserMenuItem getUserMenuItemDetails() throws SystemException {

        URL navigationMenuFileURL = ContractNavigationGenerator.class.getClassLoader().getResource(
                BDConstants.CONTRACT_NAVIG_XML_FILE);

        if (navigationMenuFileURL == null) {
            throw new SystemException("Unable to get the ContractNavigationMenu.xml file.");
        }

        String navigationMenuFile = navigationMenuFileURL.getPath();

        ApplicationContext filterCtx = new FileSystemXmlApplicationContext(navigationMenuFile);

        UserMenuItem userMenuItems = (UserMenuItem) filterCtx
                .getBean(BDConstants.CONTRACT_NAVIG_XML_BEAN_NAME);

        return userMenuItems;
    }

    /**
     * Creates the Authorization Subject
     * 
     * @param userProfile
     * @param bobContext
     * @return AuthorizationSubject
     */
    private AuthorizationSubject createAuthorizationSubject(BDUserProfile userProfile,
            BobContext bobContext) {
        BDAuthorizationSubject subject = new BDAuthorizationSubject();
        subject.setUserProfile(userProfile);
        subject.setBobContext(bobContext);
        return subject;
    }

    /**
     * returns the securityManager
     * 
     * @return securityManager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * sets the securityManager
     * 
     * @param securityManager
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }
    
    /**
     * Returns a flag to indicate whether the benefit base link should be
     * made available or not.
     * 
     * @param bob
     * @return boolean
     */
    private boolean isBenefitBasePageAccessible(BobContext bob) {
    	return (bob.getCurrentContract() != null
		&& bob.getCurrentContract().getHasContractGatewayInd()
		&& bob.getPptProfileId() != null
		&& StringUtils.isNotEmpty(bob.getParticipantGiflInd()));
    }
    
    
	/**
	 * This method checks to see if the "IPSManager" Link should be displayed or not.
	 * 
	 * @param BobContext - The BobContext object.
	 * @return - true, if IPSManager link should be displayed, else, returns false.
	 * @throws SystemException
	 */
	public boolean isIPSManagerAvailable(BobContext bob) throws SystemException {
		Contract contract = bob.getCurrentContract();
		boolean isIPSManagerAvailable = false;
		if (contract != null) {
			InvestmentPolicyStatementVO investmentPolicyStatementVO = ContractServiceDelegate
					.getInstance().getIpsBaseData(contract.getContractNumber());
			if (investmentPolicyStatementVO != null) {
				isIPSManagerAvailable = true;
			}
		}
		return isIPSManagerAvailable;
	}
    
	 
    
	/**
	 * This method checks to see if the "Wilshire 3(21) Adviser Service Report" Link should be displayed or not.
	 * 
	 * @param BobContext - The BobContext object.
	 * @return - true, if Wilshire 3(21) Adviser Service and Service provider has past 24 Months Reports then link should be displayed, else, returns false.
	 * @throws SystemException
	 */
	public boolean isCoFidContract(BobContext bob) throws SystemException {
		Contract contract = bob.getCurrentContract();
		
		ContractDocumentInfo[] contractDocInfo = null;
		try {
			contractDocInfo = com.manulife.pension.platform.web.contract.ContractDocumentsHelper
					.getContractDocuments(bob.getCurrentContract());
		} catch (SystemException e) {
			
			logger.error("Service Provider doesn't have past 24 months documents "+ e.getMessage());
			
		} catch (ServiceUnavailableException e1) {
		
			logger.error("Report Service isn't available "+ e1.getMessage());
		}

		if (contractDocInfo != null) {

			for (int i = 0; i < contractDocInfo.length; i++) {
				if (contractDocInfo[i].getDocumentType() != null && contractDocInfo[i].getDocumentSubType() != null
						&& StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentType(), DOCUMENT_TYPE_COFID)
						&& ((StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(),
								DOCUMENT_SUB_TYPE_321)
								|| (StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(),
										DOCUMENT_SUB_TYPE_338))))) {
					if (contractDocInfo[i].getDocumentCreatedDate().after(DataUtility.currentDateMinus24Months())
							|| bob.getCurrentContract().getContractDates().getAsOfDate()
									.equals(DataUtility.currentDateMinus24Months())) {
						bob.getCurrentContract().setServiceProviderHasPast24MonthsDocuments(true);
						break;
					}
				}
			}
		}
		
		if (contract != null) {
			boolean isCoFidContract = ContractServiceDelegate
					.getInstance().checkCoFidContractIndicator(contract.getContractNumber());
			if (/*isCoFidContract*/contract.isServiceProviderHasPast24MonthsDocuments()) {
					return true;
				
			}
		}
		return false;
	}
	
	/**
	 * This method checks to see if the "Cofid-Fund Recommendation Review " Link should be displayed or not.
	 * 
	 * @param BobContext - The BobContext object.
	 * @return - true, if Cofid-Fund Recommendation Review link should be displayed, else, returns false.
	 * @throws SystemException
	 */
	public boolean isCofidFundRecommendationAvailable(BobContext bob) throws SystemException {
		Contract contract = bob.getCurrentContract();
		if (contract != null) {
			boolean isCofidFundRecommendationAvailable = ContractServiceDelegate
					.getInstance().checkFundRecommendationFileAndAutoExecuteStatus(contract.getContractNumber());
			if (isCofidFundRecommendationAvailable) {
				return true;
			}
		}
		return false;
	}

	public boolean isRegulatoryDisclosuresAvailable(BobContext bob) throws SystemException {
		boolean isRegulatoryDisclosuresAvailable = false;
		Contract contract = bob.getCurrentContract();
		if (contract != null) {
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
			
			boolean hasStableValueFund = contractServiceDelegate.hasStableValueFund(contract.getContractNumber(), new Date());
			
			Map<String, String> mtaMap = contractServiceDelegate.getMtaContractDetails(contract.getContractNumber());
			Boolean isGovernmentPlan = null;
			try {
				isGovernmentPlan = contractServiceDelegate.isGovernmentPlan(contract.getContractNumber());
			} catch (ContractDoesNotExistException e1) {
				// not a contract
			}
			
			 //404 section
		      if((contract.getStatus().equals(BDConstants.ACTIVE)||(contract.getStatus().equals(BDConstants.FROZEN)))
		    		  && !contract.isDefinedBenefitContract()
		    		  && (mtaMap != null && (!StringUtils.equals(mtaMap.get(BDConstants.DISTRIBUTION_CHANNEL), BDConstants.MTA) 
		    			  && !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25270)
		    			  && !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25280)))
		    		  && (isGovernmentPlan != null && !isGovernmentPlan)){
		    	  isRegulatoryDisclosuresAvailable = true; 
		       //408b2 section 
		      }else if(!hasStableValueFund || (contract.getStatus().equals(BDConstants.DISCONTINUED_STATUS)
		    		  || contract.isMta()
		    		  || (mtaMap != null 
		    				  && (StringUtils.equals(mtaMap.get(BDConstants.DISTRIBUTION_CHANNEL), BDConstants.MTA)
		    						  || StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25270)
		    						  || StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25280)))
		    		  || (isGovernmentPlan != null && isGovernmentPlan) )){
		    	  isRegulatoryDisclosuresAvailable = false; 
				}else{
				  isRegulatoryDisclosuresAvailable = true;
				}
		}
		return isRegulatoryDisclosuresAvailable;
	}
	
	/**
	 * This method checks to see if the "Systematic withdrawal" Link should be displayed or not.
	 * 
	 * @param BobContext - The BobContext object.
	 * @return - true, if systematic withdrawal should be displayed, else, returns false.
	 * @throws SystemException
	 */
	public boolean isSYWContract(BobContext bob) throws SystemException {
		
		Contract contract = bob.getCurrentContract();
		boolean contractStatus = validateContractStatus(contract);
		if (contract != null && contractStatus) {
			boolean sywIndicator = ContractServiceDelegate
					.getInstance()
					.isSystematicWithdrawalFeatureON(contract.getContractNumber());
			if (sywIndicator
					) {
				return true;
			} else {// using replica table instead of calling apollo stored proc
				Boolean reportData = getPlaDataWithdrawalStatusReasonCode(contract
						.getContractNumber());
				if (reportData) {
					return true;
				}
			}

		}
		return false;
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
    public Boolean getPlaDataWithdrawalStatusReasonCode(Integer contractNumber) throws SystemException {
    	Boolean withdrawalStatusReasonCode=false;
			List<SystematicWithdrawDataItem> reportItems=ContractServiceDelegate.getInstance().getSystematicWithdrawalData(contractNumber, null);
			if(null!=reportItems && reportItems.size()>0){
				withdrawalStatusReasonCode = true;
			}		
		 return withdrawalStatusReasonCode;
	}
}

