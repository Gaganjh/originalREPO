/**
 * 
 */
package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.manulife.pension.ps.service.report.profiles.reporthandler.TpaUserSummaryReportHandler;
import com.manulife.pension.ps.service.report.profiles.valueobject.TpaUserSummaryReportData;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpaUser;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author marcest
 * 
 */
@Controller
@RequestMapping( value ="/profiles")


public class TpaUserSummaryReportController extends ReportController {

	@ModelAttribute("tpaUserSummaryReportForm") 
	public ReportForm populateForm() 
	{
		return new ReportForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("TPASummarytoTPAAdminXss","redirect:/do/contacts/thirdPartyAdministrator/");
	}

	public static final String REPORT_TITLE = "TPA User Management Summary";

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");

	//XSS validation
	public static final String TPA_SummarytoTPA_Admin_Xss = "TPASummarytoTPAAdminXss";

	 
	/*public String doCommon (ReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		
			String returnForward = super.doCommon( actionForm, request, response);

			ReportData report = (ReportData) request.getAttribute(Constants.REPORT_BEAN);

			return returnForward;
		}*/
		@RequestMapping(value ="/tpaUserManagementSummary", method = {RequestMethod.POST,RequestMethod.GET})
		public String doDefault (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doDefault( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		@RequestMapping(value ="/tpaUserManagementSummary/" ,params={"task=filter"}  , method =  {RequestMethod.POST}) 
		public String doFilter (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return	forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}

		@RequestMapping(value ="/tpaUserManagementSummary/", params={"task=page"}, method =  {RequestMethod.GET}) 
		public String doPage (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doPage( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		@RequestMapping(value ="/tpaUserManagementSummary/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
		public String doSort (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doSort( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		@RequestMapping(value ="/tpaUserManagementSummary/",params={"task=download"}  , method =  {RequestMethod.GET}) 
		public String doDownload (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doDownload( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		@RequestMapping(value ="/tpaUserManagementSummary/" ,params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
		public String doDownloadAll (@Valid @ModelAttribute("tpaUserSummaryReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if(errDirect!=null){
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return	forwards.get("TPASummarytoTPAAdminXss");//if input forward not //available, provided default
				}
			}
			String forward=super.doDownloadAll( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
		 */
		@Override
		protected String getDefaultSort() {
			return "sortByLastName";
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
		 *      com.manulife.pension.service.report.valueobject.ReportData,
		 *      javax.servlet.http.HttpServletRequest)
		 */
		@Override
		protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
				HttpServletRequest request) throws SystemException {


			if (logger.isDebugEnabled()) {
				logger.debug("entry -> populateDownloadData");
			}

			ArrayList<String> serviceFeatureList = new ArrayList<String>();
			serviceFeatureList.add(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
			serviceFeatureList.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
			serviceFeatureList.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);

			List<TPAFirmInfo> firms = (List<TPAFirmInfo>)report.getReportCriteria().getFilterValue(TpaUserSummaryReportData.FILTER_FIRM_IDS);
			Collection<UserInfo> userInfos =(Collection<UserInfo>)report.getDetails();
			// find the contract sort code

			StringBuffer buffer = new StringBuffer();

			buffer.append("Report Name:,").append(REPORT_TITLE).append(LINE_BREAK);
			buffer.append("Report Date:,").append(DATE_FORMATTER.format(new Date()));
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);

			for(TPAFirmInfo firm : firms ) {
				boolean showIWithdrawals = false;
				boolean showPlanData = false;
				boolean showLoans = false;

				List<Integer> contractNumbers = TPAServiceDelegate.getInstance().getContractsByFirm(firm.getId());
				for (Integer contractNumber : contractNumbers) {
					try {
						Map<String, ContractServiceFeature> serviceFeatureMap = ContractServiceDelegate.getInstance().getContractServiceFeatures(contractNumber.intValue(),
								serviceFeatureList);
						if (!showIWithdrawals) {
							ContractServiceFeature withdrawalsCSF = serviceFeatureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
							if (withdrawalsCSF != null && ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue()).booleanValue()) {
								showIWithdrawals = true;
							}
						}
						if (firm.getContractPermission().isEditPlanData() && !showPlanData) {
							showPlanData = true;
						}
						if (!showLoans) {
							ContractServiceFeature loansCSF = serviceFeatureMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
							if (loansCSF != null && ContractServiceFeature.internalToBoolean(loansCSF.getValue()).booleanValue()) {
								showLoans = true;
							}
						}
						if (showLoans && showIWithdrawals && showPlanData) {
							break;
						}
					} catch (ApplicationException ae) {
						throw new SystemException(ae, TpaUserSummaryReportController.class.getName(), "getDownloadData", ae.getMessage());
					}
				}

				buffer.append("TPA ID:,").append(firm.getId()).append(LINE_BREAK).append("TPA Name:,").append(QUOTE).append(firm.getName()).append(QUOTE).append(LINE_BREAK);
				buffer.append("General Information,,,,Permissions").append(LINE_BREAK);
				buffer.append("TPA Firm ID,Last Name,First Name,Email Address,Manage Users,Create/View Submissions,TPA Staff Plan Access,Download Reports with Full SSN");
				if (showIWithdrawals) {
					buffer.append(",Initiate/Review i:withdrawals");
				}
				if (showLoans) {
					buffer.append(",Initiate/Review Loans");
				}
				buffer.append(",Update Census Data,View Salary");

				buffer.append(LINE_BREAK);
				for ( UserInfo user : userInfos) {
					TPAFirmInfo myInfo = user.getTpaFirm(firm.getId());
					if ( myInfo == null ||  !user.isWebAccessInd()) continue;
					buffer.append(firm.getId()).append(COMMA);
					buffer.append(QUOTE).append(user.getLastName()).append(QUOTE).append(COMMA);
					buffer.append(QUOTE).append(user.getFirstName()).append(QUOTE).append(COMMA);
					buffer.append(user.getEmail()).append(COMMA);
					ContractPermission cp = myInfo.getContractPermission();
					buffer.append(cp.isManageTpaUsers() ? "Yes" : "No").append(COMMA);
					buffer.append(cp.isViewAllSubmissions() ? "Yes" : "No").append(COMMA);
					buffer.append(cp.isTpaStaffPlanAccess() ? "Yes" : "No").append(COMMA);
					buffer.append(cp.isReportDownload()  ? "Yes" : "No").append(COMMA);
					if (showIWithdrawals) {
						buffer.append(cp.isReviewIWithdrawals() ? "Yes" : "No").append(COMMA);
					}
					if (showLoans) {
						buffer.append(cp.isReviewLoans() ? "Yes" : "No").append(COMMA);
					}
					buffer.append(cp.isUpdateCensusData() ? "Yes" : "No").append(COMMA);
					buffer.append(cp.isViewSalary() ? "Yes" : "No").append(COMMA);

					buffer.append(LINE_BREAK);

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
			return TpaUserSummaryReportHandler.REPORT_ID;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
		 */
		@Override
		protected String getReportName() {
			return TpaUserSummaryReportHandler.REPORT_NAME;
		}



		/*
		 * (non-Javadoc)
		 * 
		 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
		 *      com.manulife.pension.ps.web.report.BaseReportForm,
		 *      javax.servlet.http.HttpServletRequest)
		 */
		@Override
		protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
				HttpServletRequest request) throws SystemException {

			UserProfile up = getUserProfile(request);
			List<TPAFirmInfo> firmIds = new ArrayList<TPAFirmInfo>();
			if (up.getRole().isInternalUser() && up.getCurrentContract() != null) {
				TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(
						up.getCurrentContract().getContractNumber());

				if (firmInfo != null)
				{
					firmIds.add(firmInfo);
				}

			} else if (up.getRole().isTPA()) {
				UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());
				for (TPAFirmInfo firmInfo : userInfo.getTpaFirmsAsCollection()) {
					if (firmInfo != null && firmInfo.getContractPermission().isManageTpaUsers()) {
						firmIds.add(firmInfo);
					}
				}
			}
			criteria.addFilter(TpaUserSummaryReportData.FILTER_FIRM_IDS, firmIds);
		}

		/**This code has been changed and added  to 
		 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
		 */
		/*@SuppressWarnings("rawtypes")
	public Collection doValidate( Form form, HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form, mapping, request, TPA_SummarytoTPA_Admin_Xss);
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			return penErrors;
		}
		return super.doValidate( form, request);
	}*/
		@Autowired
		private PSValidatorFWTpaUser  psValidatorFWTpaUser;

		@InitBinder
		public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
			binder.bind( request);
			binder.addValidators(psValidatorFWTpaUser);
		}

	}
