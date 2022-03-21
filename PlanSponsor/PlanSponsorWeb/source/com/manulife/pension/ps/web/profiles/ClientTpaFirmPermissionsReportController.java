/**
 * 
 */
package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.profiles.reporthandler.ClientTpaFirmPermissionsReportHandler;
import com.manulife.pension.ps.service.report.profiles.valueobject.ClientTpaFirmPermissionsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWClientTpa;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantAddressContractServiceFeature;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author marcest
 * 
 */
@Controller
@RequestMapping( value ="/profiles")

public class ClientTpaFirmPermissionsReportController extends ReportController {
	@ModelAttribute("clientTpaFirmPermissionsReportForm")
	public ReportForm populateForm() 
	{
		return new ReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("TPASummarytoTPAAdminXss","redirect:/do/contacts/thirdPartyAdministrator/");
	}

	private static final String QUOTE = "\"";
	private class ReportItem {

		private String permission;

		private String contractName;

		private String contractNumber;

		private String lastName;

		private String firstName;

        public ReportItem(String permission, String contractName, String contractNumber, String lastName, String firstName) {
			this.permission = permission;
			this.contractName = contractName;
			this.contractNumber = contractNumber;
			this.lastName = lastName;
			this.firstName = firstName;
		}

		public String getContractName() {
			return contractName;
		}

		public void setContractName(String contractName) {
			this.contractName = contractName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}

		public String getContractNumber() {
			return contractNumber;
		}

		public void setContractNumber(String contractNumber) {
			this.contractNumber = contractNumber;
		}

		public StringBuffer toReport() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(QUOTE).append(getPermission()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(getContractNumber()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(getContractName()).append(QUOTE).append(COMMA);
            buffer.append(QUOTE).append(lastName).append(QUOTE).append(COMMA);
            buffer.append(QUOTE).append(firstName).append(QUOTE);
			buffer.append(LINE_BREAK);
			return buffer;

		}
	}

	public static final String REPORT_TITLE = "Client Authorization of TPA Firm Permissions Report";
	public static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");

    // reporting
    private static String LABEL_DOWNLOAD_REPORTS = "Download Reports – Full SSN";
    // plan services
    private static String LABEL_SUBMIT_UPDATE_VESTING = "Submit / Update Vesting";
    // auto enrollment
    private static String LABEL_AUTO_ENROLL_ADMIN_EMAIL = "Auto Enrollment Administration emails";
    private static String LABEL_ANNUAL_REMINDER_EMAIL = "Annual Reminder Notification email";
    // payroll path
    private static String LABEL_PAYROLL_PATH_EMAIL = "Payroll Path Email";
    // submissions
    private static String LABEL_VIEW_SUBMISSIONS = "View Submissions";
    private static String LABEL_CREATE_SUBMISSIONS = "Create / Upload Submissions";
    private static String LABEL_VIEW_ALL_SUBMISSIONS = "View all users' submissions";
    private static String LABEL_CASH_ACCOUNT = "Cash Account";
    private static String LABEL_DIRECT_DEBIT = "Direct Debit";
    // i:withdrawals
    private static String LABEL_INITIATE_IWITHDRAWALS = "Initiate i:Withdrawals";
    private static String LABEL_REVIEW_IWITHDRAWALS = "Review i:Withdrawals";
    // Signing Authority : Introduced by LOANS project - will be used by both Loans & Withdrawals
    private static String LABEL_SIGNING_AUTHORITY = "Signing Authority";
    // census
    private static String LABEL_UPDATE_CENSUS_DATA = "Update Census Data";
    private static String LABEL_VIEW_SALARY = "View Salary";
    // participant services
    private static String LABEL_DEFERRAL_EMAIL = "Deferral Email";
    private static String LABEL_ENROLLMENT_EMAIL = "Enrollment Email";
    private static String LABEL_ADDRESS_CHANGE_EMAIL = "Address Change Email";
    
    // Loans permissions
    private static String LABEL_INITIATE_LOANS = "Initiate loans";
    private static String LABEL_REVIEW_LOANS = "Review loans";
    
    //XSS validation
    private static String TPA_SummarytoTPA_AdminXss = "TPASummarytoTPAAdminXss";


	 
    protected String doCommon(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {
		
		String returnForward = super.doCommon( reportForm, request, response);

		ReportData report = (ReportData) request.getAttribute(Constants.REPORT_BEAN);

		return returnForward;
	}

	
	
	
	@RequestMapping(value ="/clientTpaFirmPermissions/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
	       }
		}
	       String forward=super.doDefault( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/clientTpaFirmPermissions/" ,params={"task=filter"}  , method =  {RequestMethod.POST}) 
	public String doFilter (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/clientTpaFirmPermissions/", params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
	       }
		}
	       String forward=super.doPage( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/clientTpaFirmPermissions/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
	       }
		}
	       String forward=super.doSort( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/clientTpaFirmPermissions/",params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
			}
		}
		String forward=super.doDownload( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/clientTpaFirmPermissions/" ,params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
	public String doDownloadAll (@Valid @ModelAttribute("clientTpaFirmPermissionsReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	forwards.get("TPASummarytoTPAAdminXss");//if TPASummarytoTPAAdminXss forward not //available, provided default
	       }
		}
	       String forward=super.doDownloadAll( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	@Override
	protected String getDefaultSort() {
		return "sortByDefault";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.pension.ps.web.report.BaseReportForm,
     *      com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
	 */
	@Override
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

        com.manulife.pension.delegate.ContractServiceDelegate csd = com.manulife.pension.delegate.ContractServiceDelegate.getInstance();
        ArrayList<String> serviceFeatureList = new ArrayList<String>();
        serviceFeatureList.add(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);

		ClientTpaFirmPermissionsReportData data = (ClientTpaFirmPermissionsReportData) report;
		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		buffer.append("Report Name,").append(REPORT_TITLE).append(LINE_BREAK);
		buffer.append("Report Date,").append(DATE_FORMATTER.format(new Date()));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

        Map<Integer, Set<TPAFirmInfo>> firmInfoMap = new TreeMap<Integer, Set<TPAFirmInfo>>();
		for (TPAFirmInfo firm : data.getFirmInfos()) {
			if (firmInfoMap.get(firm.getId()) == null) {
                firmInfoMap.put(firm.getId(), new TreeSet<TPAFirmInfo>(new Comparator<TPAFirmInfo>() {
                    public int compare(TPAFirmInfo firm1, TPAFirmInfo firm2) {
                        Integer contractNumber1 = new Integer(firm1.getContractPermission().getContractNumber());
                        Integer contractNumber2 = new Integer(firm2.getContractPermission().getContractNumber());
                        return contractNumber1.compareTo(contractNumber2);
                    }
                }));
			}
			firmInfoMap.get(firm.getId()).add(firm);
		}

		for (Integer firmId : firmInfoMap.keySet()) {
            TPAFirmInfo aFirm = firmInfoMap.get(firmId).iterator().next();
			buffer.append("TPA Firm ID,").append(QUOTE).append(aFirm.getId()).append(QUOTE).append(LINE_BREAK);
            buffer.append("TPA Firm Name,").append(QUOTE).append(aFirm.getName()).append(QUOTE).append(LINE_BREAK).append(LINE_BREAK).append(LINE_BREAK);
			buffer.append("TPA Firm Permission,Contract Number,Contract Name,Last Name,First Name").append(LINE_BREAK);

            // reporting
            List<ReportItem> downloadReports = new ArrayList<ReportItem>();
            // plan services
            List<ReportItem> submitUpdateVesting = new ArrayList<ReportItem>();
            // auto enrollment
            List<ReportItem> autoEnrollAdminEmail = new ArrayList<ReportItem>();
            List<ReportItem> annualReminderEmail = new ArrayList<ReportItem>();
            // payroll path
            List<ReportItem> payrollPathEmail = new ArrayList<ReportItem>();
            // submissions
            List<ReportItem> viewSubmissions = new ArrayList<ReportItem>();
            List<ReportItem> createSubmissions = new ArrayList<ReportItem>();
            List<ReportItem> viewAllSubmissions = new ArrayList<ReportItem>();
            List<ReportItem> cashAccount = new ArrayList<ReportItem>();
            List<ReportItem> directDebit = new ArrayList<ReportItem>();
            // Loans permissions
            List<ReportItem> initiateLoans = new ArrayList<ReportItem>();
            List<ReportItem> reviewLoans = new ArrayList<ReportItem>();
            // i:withdrawals
            List<ReportItem> initiateIWithdrawals = new ArrayList<ReportItem>();
            List<ReportItem> reviewIWithdrawals = new ArrayList<ReportItem>();
            // Signing Authority : Introduced by LOANS project - will be used by both Loans & Withdrawals
            List<ReportItem> signingAuthority = new ArrayList<ReportItem>();
            // census
            List<ReportItem> updateCensusData = new ArrayList<ReportItem>();
            List<ReportItem> viewSalary = new ArrayList<ReportItem>();
            // participant services
            List<ReportItem> deferralEmail = new ArrayList<ReportItem>();
            List<ReportItem> enrollmentEmail = new ArrayList<ReportItem>();
            List<ReportItem> addressChangeEmail = new ArrayList<ReportItem>();

			for (TPAFirmInfo firm : firmInfoMap.get(firmId)) {

                ContractProfileVO contract = ContractServiceDelegate.getInstance().getContractProfileDetails(firm.getContractPermission().getContractNumber(), Environment.getInstance().getSiteLocation());
				String contractName = contract.getContractName();
				String contractId = Integer.toString(contract.getContractNumber());

                boolean hasAutoEnrollmentServiceFeature = false;
                boolean hasPayrollPathServiceFeature = false;
                boolean hasPlanDataServiceFeature = false;
                boolean hasWithdrawalsServiceFeature = false;
                boolean hasAddressServiceFeature = false;
                boolean hasLoansServiceFeature = false;
                boolean hasReviewWithdrawalCSF = false;
                
                try {
                    Map<String, ContractServiceFeature> serviceFeatureMap = csd.getContractServiceFeatures(contract.getContractNumber(), serviceFeatureList);
                    ContractServiceFeature autoEnrollmentServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
                    ContractServiceFeature payrollPathServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
                    ContractServiceFeature planDataServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
                    ContractServiceFeature withdrawalsServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
                    ContractServiceFeature addressServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
                    ContractServiceFeature loansServiceFeature = serviceFeatureMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
                    
                    if (autoEnrollmentServiceFeature != null) {
                        hasAutoEnrollmentServiceFeature = ContractServiceFeature.internalToBoolean(autoEnrollmentServiceFeature.getValue()).booleanValue();
                    }
                    if (payrollPathServiceFeature != null) {
                        hasPayrollPathServiceFeature = ContractServiceFeature.internalToBoolean(payrollPathServiceFeature.getValue()).booleanValue();
                    }
                    if (planDataServiceFeature != null) {
                        hasPlanDataServiceFeature = ContractServiceFeature.internalToBoolean(planDataServiceFeature.getValue()).booleanValue();
                    }
                    if (withdrawalsServiceFeature != null) {
                        hasWithdrawalsServiceFeature = ContractServiceFeature.internalToBoolean(withdrawalsServiceFeature.getValue()).booleanValue();
                        // Adding the boolean to determine whether the Review Withdrawal CSF is set
                        hasReviewWithdrawalCSF = ContractServiceFeature.internalToBoolean(withdrawalsServiceFeature.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW));
                    }
                    if (addressServiceFeature != null) {
                        ParticipantAddressContractServiceFeature pamServiceFeature = new ParticipantAddressContractServiceFeature(addressServiceFeature);
                        hasAddressServiceFeature = (pamServiceFeature.getValues().size() > 0);
                    }
                    if (loansServiceFeature != null) {
                    	hasLoansServiceFeature = ContractServiceFeature.internalToBoolean(loansServiceFeature.getValue()).booleanValue();
                    }
                    
                } catch (ApplicationException ae) {
                    throw new SystemException(ae, ClientTpaFirmPermissionsReportController.class.getName(), "getDownloadData", ae.getMessage());
                }

				if (firm.getContractPermission().isReportDownload()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isReportDownload())) {
                            downloadReports.add(new ReportItem(LABEL_DOWNLOAD_REPORTS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        downloadReports.add(new ReportItem(LABEL_DOWNLOAD_REPORTS, contractName, contractId, "", ""));
				}
                }
                if (hasPlanDataServiceFeature && firm.getContractPermission().isSubmitUpdateVesting()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isEditPlanData())) {
                            submitUpdateVesting.add(new ReportItem(LABEL_SUBMIT_UPDATE_VESTING, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        submitUpdateVesting.add(new ReportItem(LABEL_SUBMIT_UPDATE_VESTING, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isSubmissionAccess()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isSubmissionAccess())) {
                            viewSubmissions.add(new ReportItem(LABEL_VIEW_SUBMISSIONS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        viewSubmissions.add(new ReportItem(LABEL_VIEW_SUBMISSIONS, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isUploadSubmissions()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isUploadSubmissions())) {
                            createSubmissions.add(new ReportItem(LABEL_CREATE_SUBMISSIONS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        createSubmissions.add(new ReportItem(LABEL_CREATE_SUBMISSIONS, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isViewAllSubmissions()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isViewAllSubmissions())) {
                            viewAllSubmissions.add(new ReportItem(LABEL_VIEW_ALL_SUBMISSIONS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        viewAllSubmissions.add(new ReportItem(LABEL_VIEW_ALL_SUBMISSIONS, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isCashAccount()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isCashAccount())) {
                            cashAccount.add(new ReportItem(LABEL_CASH_ACCOUNT, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        cashAccount.add(new ReportItem(LABEL_CASH_ACCOUNT, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isDirectDebit()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isDirectDebit())) {
                            directDebit.add(new ReportItem(LABEL_DIRECT_DEBIT, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
                        }
						}
                    if (!userHasPermission) {
                        directDebit.add(new ReportItem(LABEL_DIRECT_DEBIT, contractName, contractId, "", ""));
					}
				}
				// Adding Loans permissions - allow online csf is set then allow the report to show initiate/review loans permissions
                if (hasLoansServiceFeature && firm.getContractPermission().isInitiateLoans()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isInitiateLoans())) {
                            initiateLoans.add(new ReportItem(LABEL_INITIATE_LOANS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
                        }
						}
                    if (!userHasPermission) {
                    	initiateLoans.add(new ReportItem(LABEL_INITIATE_LOANS, contractName, contractId, "", ""));
					}
				}
                if (hasLoansServiceFeature && firm.getContractPermission().isReviewLoans()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isReviewLoans())) {
                            reviewLoans.add(new ReportItem(LABEL_REVIEW_LOANS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                    	reviewLoans.add(new ReportItem(LABEL_REVIEW_LOANS, contractName, contractId, "", ""));
                    }
				}
                if (hasWithdrawalsServiceFeature && firm.getContractPermission().isInitiateWithdrawalsAndViewMine()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isInitiateWithdrawalsAndViewMine())) {
                            initiateIWithdrawals.add(new ReportItem(LABEL_INITIATE_IWITHDRAWALS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
                        }
						}
                    if (!userHasPermission) {
                        initiateIWithdrawals.add(new ReportItem(LABEL_INITIATE_IWITHDRAWALS, contractName, contractId, "", ""));
					}
				}
                // Adding an additional 'and' condition as per the SEC.61d of ManageProfiles DFS
                // show review permission only when the allow review for withdrawal csf attribute is turned on for the contract
                if (hasWithdrawalsServiceFeature && hasReviewWithdrawalCSF && firm.getContractPermission().isReviewIWithdrawals()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isReviewIWithdrawals())) {
                            reviewIWithdrawals.add(new ReportItem(LABEL_REVIEW_IWITHDRAWALS, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        reviewIWithdrawals.add(new ReportItem(LABEL_REVIEW_IWITHDRAWALS, contractName, contractId, "", ""));
                    }
				}
                // Change from LOANS project : Removing the dependency on withdrawal service feature for Signing Authority
                if (firm.getContractPermission().isSigningAuthority()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isSigningAuthority())) {
                            signingAuthority.add(new ReportItem(LABEL_SIGNING_AUTHORITY, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                    	signingAuthority.add(new ReportItem(LABEL_SIGNING_AUTHORITY, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isUpdateCensusData()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isUpdateCensusData())) {
                            updateCensusData.add(new ReportItem(LABEL_UPDATE_CENSUS_DATA, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        updateCensusData.add(new ReportItem(LABEL_UPDATE_CENSUS_DATA, contractName, contractId, "", ""));
                    }
				}
				if (firm.getContractPermission().isViewSalary()) {
                    boolean userHasPermission = false;
					for (UserInfo user : data.getUserInfos()) {
						TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
						if (user.isWebAccessInd() && (myInfo != null && myInfo.getContractPermission().isViewSalary())) {
                            viewSalary.add(new ReportItem(LABEL_VIEW_SALARY, contractName, contractId, user.getLastName(), user.getFirstName()));
                            userHasPermission = true;
						}
					}
                    if (!userHasPermission) {
                        viewSalary.add(new ReportItem(LABEL_VIEW_SALARY, contractName, contractId, "", ""));
                    }
				}
			}
            if (!downloadReports.isEmpty()) {
				buffer.append("Reporting").append(LINE_BREAK);
                for (ReportItem item : downloadReports)
					buffer.append(item.toReport());
			}
            if (!submitUpdateVesting.isEmpty()) {
				buffer.append("Plan Services").append(LINE_BREAK);
                for (ReportItem item : submitUpdateVesting)
					buffer.append(item.toReport());
			}
            if (!autoEnrollAdminEmail.isEmpty() || !annualReminderEmail.isEmpty() || !payrollPathEmail.isEmpty() || !viewSubmissions.isEmpty() || !createSubmissions.isEmpty()
                    || !viewAllSubmissions.isEmpty() || !cashAccount.isEmpty() || !directDebit.isEmpty() || !initiateIWithdrawals.isEmpty() || !reviewIWithdrawals.isEmpty()
                    || !signingAuthority.isEmpty() || !updateCensusData.isEmpty() || !viewSalary.isEmpty()) {
				buffer.append("Client Services").append(LINE_BREAK);

	               // LOANS PROJECT : Addition of the Signing Authority	
                if (!signingAuthority.isEmpty()) {
					buffer.append("Signing Authority").append(LINE_BREAK);
					for (ReportItem item : signingAuthority)
                        buffer.append(item.toReport());
                }
                
                if (!autoEnrollAdminEmail.isEmpty() || !annualReminderEmail.isEmpty()) {
					buffer.append("Auto Enrollment").append(LINE_BREAK);
                    for (ReportItem item : autoEnrollAdminEmail)
                        buffer.append(item.toReport());
                    for (ReportItem item : annualReminderEmail)
						buffer.append(item.toReport());
				}
                if (!payrollPathEmail.isEmpty()) {
					buffer.append("Payroll Path").append(LINE_BREAK);
                    for (ReportItem item : payrollPathEmail)
						buffer.append(item.toReport());
				}
                if (!viewSubmissions.isEmpty() || !createSubmissions.isEmpty() || !viewAllSubmissions.isEmpty() || !cashAccount.isEmpty() || !directDebit.isEmpty()) {
					buffer.append("Submissions").append(LINE_BREAK);
                    for (ReportItem item : viewSubmissions)
                        buffer.append(item.toReport());
                    for (ReportItem item : createSubmissions)
                        buffer.append(item.toReport());
                    for (ReportItem item : viewAllSubmissions)
                        buffer.append(item.toReport());
                    for (ReportItem item : cashAccount)
                        buffer.append(item.toReport());
                    for (ReportItem item : directDebit)
						buffer.append(item.toReport());
				}
 
                if (!initiateLoans.isEmpty() || !reviewLoans.isEmpty()) {
					buffer.append("Loans").append(LINE_BREAK);
                    for (ReportItem item : initiateLoans)
                        buffer.append(item.toReport());
                    for (ReportItem item : reviewLoans)
                        buffer.append(item.toReport());
				}
                
                if (!initiateIWithdrawals.isEmpty() || !reviewIWithdrawals.isEmpty()) {
					buffer.append("i:Withdrawals").append(LINE_BREAK);
                    for (ReportItem item : initiateIWithdrawals)
                        buffer.append(item.toReport());
                    for (ReportItem item : reviewIWithdrawals)
                        buffer.append(item.toReport());
				}
                
                if (!updateCensusData.isEmpty() || !viewSalary.isEmpty()) {
					buffer.append("Census Management").append(LINE_BREAK);
                    for (ReportItem item : updateCensusData)
                        buffer.append(item.toReport());
                    for (ReportItem item : viewSalary)
						buffer.append(item.toReport());
				}
			}
            if (!deferralEmail.isEmpty() || !enrollmentEmail.isEmpty() || !addressChangeEmail.isEmpty()) {
				buffer.append("Participant Services").append(LINE_BREAK);
                for (ReportItem item : deferralEmail)
                    buffer.append(item.toReport());
                for (ReportItem item : enrollmentEmail)
                    buffer.append(item.toReport());
                for (ReportItem item : addressChangeEmail)
					buffer.append(item.toReport());
			}

			buffer.append(LINE_BREAK).append(LINE_BREAK);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	@Override
	protected String getReportId() {
		return ClientTpaFirmPermissionsReportHandler.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	@Override
	protected String getReportName() {
		return ClientTpaFirmPermissionsReportHandler.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
     *      com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	@Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {

		UserProfile up = getUserProfile(request);
		List<Integer> contractIds = new ArrayList<Integer>();
		if (up.getRole().isInternalUser() && up.getCurrentContract() != null) {
			contractIds.add(up.getCurrentContract().getContractNumber());
		} else if (up.getRole().isTPA()) {
			UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());
			for (TPAFirmInfo firmInfo : userInfo.getTpaFirmsAsCollection()) {
				if (firmInfo.getContractPermission().isManageTpaUsers()) {
					contractIds.addAll(TPAServiceDelegate.getInstance().getContractsByFirm(firmInfo.getId()));
				}
			}
		}
		criteria.addFilter(ClientTpaFirmPermissionsReportData.FILTER_CONTRACT_IDS, contractIds);
	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */

   	 @Autowired
	   private PSValidatorFWClientTpa  psValidatorFWClientTpa;
   	 
   	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWClientTpa);
	}
}
