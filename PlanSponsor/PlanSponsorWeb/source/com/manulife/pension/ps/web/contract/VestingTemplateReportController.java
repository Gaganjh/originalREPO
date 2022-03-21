package com.manulife.pension.ps.web.contract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWVestTemp;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.employee.EmployeeConstants;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.util.render.SSNRender;

/**
 * This class models the action of retrieving and presenting a vesting template
 * for a contract.
 * 
 * @author Diana Macean
 */
@Controller
@RequestMapping( value = "/contract")
public class VestingTemplateReportController extends ReportController {
	
	
	@ModelAttribute("vestingTemplateForm") 
	public  VestingTemplateReportForm populateForm()
	{
		return new  VestingTemplateReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("vesttemp", "/tools/toolsMenu.jsp");
		}

	public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("00");
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			"MMddyyyy");

	public static final String EMPLOYEE_ID_INDICATOR = "EE";

	/**
	 * @see ReportController#populateReportCriteria(ReportCriteria,
	 *      BaseReportForm, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(CensusVestingReportData.FILTER_CONTRACT_NUMBER,
				Integer.toString(userProfile.getCurrentContract()
						.getContractNumber()));
		
		criteria.addFilter(CensusVestingReportData.FILTER_PRODUCTID,
				userProfile.getCurrentContract()
						.getProductId());
		
		// if external user, don't display Cancelled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * @see ReportController#populateSortCriteria(ReportCriteria, BaseReportForm)
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// sort first by lastName, in the DAO if lastName is one of the sort
		// items found
		// the data will also be sorted by firstName and middleInitial
		criteria.insertSort(getDefaultSort(), getDefaultSortDirection());

		// if Contract sort option code is "EE", sort by employeeId, else sort
		// by ssn
		VestingTemplateReportForm vestingTemplateForm = (VestingTemplateReportForm) form;
		if (StringUtils.equals(vestingTemplateForm.getContractSortOptionCode(),
				EMPLOYEE_ID_INDICATOR)) {
			criteria.insertSort(CensusVestingReportData.SORT_EMPLOYEE_ID,
					getDefaultSortDirection());
		} else {
			criteria.insertSort(CensusVestingReportData.SORT_SSN,
					getDefaultSortDirection());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateSortCriteria");
		}
	}

	/**
	 * @see ReportController#populateReportForm(ActionMapping,
	 *      BaseReportForm, HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		// find and store the contract sort option code
		UserProfile user = getUserProfile(request);
		Contract currentContract = user.getCurrentContract();
		String sortCode = currentContract.getParticipantSortOptionCode();

		VestingTemplateReportForm vestingTemplateForm = (VestingTemplateReportForm) reportForm;
		vestingTemplateForm.setContractSortOptionCode(sortCode);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return CensusVestingReportData.TEMPLATE_REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return CensusVestingReportData.TEMPLATE_REPORT_NAME;
	}

	protected String getDefaultSort() {
		return CensusVestingReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getFileName()
	 */
	protected String getFileName(HttpServletRequest request) {
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}
		return "Vesting_Template_for_"
				+ getUserProfile(request).getCurrentContract()
						.getContractNumber() + "_for_" + dateString
				+ CSV_EXTENSION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		StringBuffer buffer = new StringBuffer();
		CensusVestingReportData vestingData = (CensusVestingReportData) report;

		UserProfile user = getUserProfile(request);
		Contract currentContract = user.getCurrentContract();

      //  FunctionalLogger.INSTANCE.log("Download Vesting File Template", user, getClass(), getMethodName(null, reportForm, request));
        
		// find the contract sort code
		String sortCode = currentContract.getParticipantSortOptionCode();

		// Fill in the header
		Iterator columnLabels = vestingData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}

		if (!getUserProfile(request).isSelectedAccess()) {

			Iterator items = report.getDetails().iterator();
			while (items.hasNext()) {
				buffer.append(LINE_BREAK);
				buffer.append(vestingData.getTransactionNumber()).append(COMMA);
				buffer.append(vestingData.getContractNumber()).append(COMMA);

				CensusVestingDetails vesting = (CensusVestingDetails) items
						.next();
				buffer.append(SSNRender.format(vesting.getSsn(), null, false))
						.append(COMMA);
				buffer.append(escapeField(vesting.getFirstName()))
						.append(COMMA);
				buffer.append(escapeField(vesting.getLastName().trim()))
						.append(COMMA);

				if (vesting.getMiddleInitial() != null)
					buffer
							.append(escapeField(vesting.getMiddleInitial()
									.trim()));
				buffer.append(COMMA);

				if (vesting.getEmployeeNumber() != null
						&& vesting.getEmployeeNumber().trim().length() > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < (9 - vesting.getEmployeeNumber().trim()
							.length()); i++) {
						sb.append("0");
					}
					buffer.append(sb.toString());
					buffer.append(vesting.getEmployeeNumber().trim());
				}
				buffer.append(COMMA);

				if (VestingConstants.VestingServiceFeature.CALCULATION
						.equals(vestingData.getVestingServiceFeature())) {

					// if crediting method is Hour of Service
					if (VestingConstants.CreditingMethod.HOURS_OF_SERVICE
							.equals(vesting.getCalculationFact()
									.getCreditingMethod())) {
						if(!Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId())){
							// adding apply LTPT Creditings
							buffer.append(vesting.getApplyLTPTCrediting());
						}
						VestingRetrievalDetails fact = vesting
								.getCalculationFact();
						VestingInputDescription info = fact
								.getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE);
						if (info != null) {

							// new requirement to not display vyos and vyos
							// date, SSE project - Dec 17, 2007
							// buffer.append(DateRender.formatByPattern(info.
							// getEffectiveDate(), "",
							//RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA)
							// ;
							buffer.append(COMMA);

							//buffer.append(escapeField(NumberRender.formatByType
							// (info.getValue(), "",
							// RenderConstants.DECIMAL_TYPE))).append(COMMA);
							buffer.append(COMMA);

						} else {
							buffer.append(COMMA);
							buffer.append(COMMA);
						}

					}

					// if creditting method is Elapsed Time
					if (VestingConstants.CreditingMethod.ELAPSED_TIME
							.equals(vesting.getCalculationFact()
									.getCreditingMethod())) {
						buffer.append(COMMA);
						buffer.append(COMMA);
					}

				} else {

					// For TPA Provided, vesting engine returns vesting
					// effective date as asOfDate
					// (today) if no percentages were provided by TPA. For the
					// template vesting
					// effective date has to be blank if no percentages were
					// provided by TPA.
					// We have to override the vesting date returned by the
					// vesting engine
					// with the latest vesting effective date found in the map.
					Date effectiveDate = null;
					for (Iterator it = vesting.getPercentages().values()
							.iterator(); it.hasNext();) {
						MoneyTypeVestingPercentage mvp = (MoneyTypeVestingPercentage) it
								.next();
						if (EmployeeConstants.MoneyTypeGroup.MONEY_TYPE_ER
								.equals(mvp.getMoneyTypeGroup())
								&& !mvp.isMoneyTypeFullyVested()) {
							Date d = mvp.getEffectiveDate();
							if (effectiveDate == null
									|| (d != null && d.after(effectiveDate))) {
								effectiveDate = d;
							}
						}
					}

					/*
					 * new requirement to not display vesting effective date and
					 * percentages, SSE project - Dec 17, 2007
					 */
					// buffer.append(DateRender.formatByPattern(effectiveDate,
					// "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
					buffer.append(COMMA);

					/* sort according to the rule defined in the comparator */
					List contractMoneyTypes = vestingData.getMoneyTypes();
					Collections.sort(contractMoneyTypes,
							new VestingMoneyTypeComparator());
					for (int i = 0; i < contractMoneyTypes.size(); i++) {
						MoneyTypeVO vo = (MoneyTypeVO) contractMoneyTypes
								.get(i);
						MoneyTypeVestingPercentage mtvp = (MoneyTypeVestingPercentage) vesting
								.getPercentages().get(vo.getId());
						/*
						 * if (mtvp != null) { BigDecimal perc =
						 * mtvp.getPercentage();
						 * buffer.append(NumberRender.formatByPattern(perc, "",
						 * "###.###", 3, BigDecimal.ROUND_HALF_DOWN)); }
						 */
						buffer.append(COMMA);
					}

				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
	
	
	@RequestMapping(value ="/vestingTemplate",params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("vestingTemplateForm") VestingTemplateReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		UserProfile userProfile = getUserProfile(request);
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		try {
    				request.setAttribute(Constants.REQUEST_TYPE,WithdrawalWebUtil.getTypeOfRequest(userProfile));
    			} catch (SystemException e) {
    				e.printStackTrace();
    			}
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forwards.get("vesttemp");//if input forward not //available, provided default
        	}
        }
		String forward=super.doDownload( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	/**PSValidatorFWVestTemp
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations
	 */
	
	 @Autowired
	   private PSValidatorFWVestTemp  psValidatorFWVestTemp;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWVestTemp);
	}
}