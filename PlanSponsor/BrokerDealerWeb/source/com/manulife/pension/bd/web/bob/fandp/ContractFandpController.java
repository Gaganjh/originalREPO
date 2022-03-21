package com.manulife.pension.bd.web.bob.fandp;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.fap.BDFapBaseController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWSessionExpired;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.util.FapFilterUtility;

/**
 * Action class specific to the Contract Funds and Performance
 * 
 * @author SAyyalusamy
 *
 */
@Controller
@RequestMapping(value = "/bob")

public class ContractFandpController extends BDFapBaseController {
	
	@ModelAttribute("fapForm")
	public FapForm populateForm() {
		return new FapForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/fap/contractFandp.jsp");
		forwards.put("continue", "/fap/contract");
		forwards.put("sessionExpired","/WEB-INF/fap/fapFilterResults.jsp");

	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWSessionExpired bdValidatorFWSessionExpired;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWSessionExpired);
	}

	@RequestMapping(value = "/contractFandp", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("fapForm") FapForm actionForm, ModelMap model,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
												// //available, provided default
			}
		}
		request.getSession().setAttribute("FileName", "Contract_Fund&Performance");

		// populate the Base filter with default values
		return forwards.get(super.doDefault(actionForm, request, response));
	}

	public String preExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		FapForm fapForm = (FapForm) form;

		// set the contract mode
		fapForm.setContractMode(true);

		return super.preExecute(form, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.fap.FapAction#
	 * populateBaseFilterOptions(
	 * com.manulife.pension.platform.web.fap.FapForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateBaseFilterOptions(FapForm fapForm, HttpServletRequest request)
			throws SystemException {

		BobContext bobContext = BDSessionHelper.getBobContext(request);

		if (bobContext != null && bobContext.getCurrentContract() != null) {
			// set the company ID
			fapForm.setCompanyId(bobContext.getCurrentContract().getCompanyCode());
		} else {
			fapForm.setCompanyId(FapConstants.COMPANY_ID_US);
		}

		// set the mimic mode
		fapForm.setInMimic(isUserInMimic(request));

		// set the contract mode
		// fapForm.setContractMode(true);

		// default the base filter to contract funds
		fapForm.setBaseFilterSelect(FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY);

		// set the show NML option
		fapForm.setShowNML(false);

		// get the group by drop-down list
		fapForm.setGroupByList(FapFilterUtility.createBaseFilterGroupByList());

		if (StringUtils.isEmpty(fapForm.getTabSelected())) {
			fapForm.setTabSelected(FapConstants.FUND_INFORMATION_TAB_ID);
		}

		// set the contract number to the contract search text box
		// and the contract name to the form - bean
		String contractNumber = (String) request.getSession().getAttribute(FapConstants.ATTR_CONTRACT_NUMBER);
		String groupby = (String) request.getSession().getAttribute(FapConstants.ATTR_GROUP_BY);
		String tabSelected = (String) request.getSession().getAttribute(FapConstants.ATTR_TAB_SELECTED);
		if (contractNumber == null) {
			contractNumber = getCurrentContractNumber(request);
			fapForm.setSelectedContractName(getCurrentContractName(request));
			fapForm.setGroupBySelect(groupby);
			if (StringUtils.isNotEmpty(tabSelected)) {
				fapForm.setTabSelected(tabSelected);
			}
		} else {
			request.getSession().removeAttribute(FapConstants.ATTR_CONTRACT_NUMBER);
			request.getSession().removeAttribute(FapConstants.ATTR_GROUP_BY);
		}

		if (contractNumber != null) {
			fapForm.setContractSearchText(contractNumber);
			fapForm.setGroupBySelect(groupby);
			if (StringUtils.isNotEmpty(tabSelected)) {
				fapForm.setTabSelected(tabSelected);
			}
		}

		// get the options for the reports and down-loads drop-down
 		populateReportDropDownList(fapForm, request);
	}

	/**
	 * Returns the contract name
	 * 
	 * @param request
	 * @return contract name
	 */
	private String getCurrentContractName(HttpServletRequest request) {
		BobContext bobContext = BDSessionHelper.getBobContext(request);
		String contractName = null;

		if (bobContext != null && bobContext.getContractProfile() != null) {
			contractName = bobContext.getContractProfile().getContract().getCompanyName();
		}

		return contractName;
	}
}
