package com.manulife.pension.bd.web.fap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ContractSearchUtility;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWSessionExpired;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.fap.FapController;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.fap.util.FapFilterUtility;
import com.manulife.pension.platform.web.fap.util.FapReportsUtility;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.util.content.GenericException;

/**
 * Action Class is specific to the Broker Dealer Module - Funds & Performance
 * pages.
 * 
 * The contract search is implemented in this action class.
 * 
 * @author ayyalsa
 *
 */
@Controller
@RequestMapping(value = "/fap")

public class BDFapController extends BDFapBaseController {

	@ModelAttribute("fapForm")
	public FapForm populateForm() {
		return new FapForm();
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

	/**
	 * Action method to perform validations on the contract search text and
	 * retrieve the contract list matching the contract search text.
	 * 
	 * @param mapping
	 * @param actionform
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=contractSerach" }, method = { RequestMethod.POST })
	public String doContractSerach(@Valid @ModelAttribute("fapForm") FapForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		Collection<GenericException> errorMessages = new ArrayList<GenericException>();

		List<ContractVO> contractVOList = null;
		actionForm.setFilteredFundIds(null);

		String contractSearchText = actionForm.getContractSearchText();
		int contractSearchTextLength = StringUtils.deleteWhitespace(contractSearchText).length();
		boolean includeIAStatus = false;

		List<String> contractStatusList = new ArrayList<String>();
		contractStatusList.add("AC");
		contractStatusList.add("DI");

		/*
		 * If the search text is null or empty or less than 3 in length then
		 * display the error message.
		 */
		if (contractSearchText == null || contractSearchTextLength < 3) {

			errorMessages
					.add(new GenericException(CommonErrorCodes.CONTRACT_NAME_SEARCH_REQUIRES_MINIMUN_3_CHARARACTERS));

			/*
			 * If the search text length is between 5 to 7 and all are digits
			 * then consider the search text as contract number
			 */
		} else if (contractSearchTextLength >= FapConstants.CONTRACT_NUMBER_MIN_LENGTH
				&& contractSearchTextLength <= FapConstants.CONTRACT_NUMBER_MAX_LENGTH
				&& StringUtils.isNumeric(StringUtils.deleteWhitespace(contractSearchText))) {

			actionForm.setContractSearchText(StringUtils.deleteWhitespace(contractSearchText));

			contractVOList = ContractSearchUtility.searchByContractNumber(request, userProfile,
					StringUtils.deleteWhitespace(contractSearchText), contractStatusList, includeIAStatus);

			// then display a error message
			if (contractVOList == null || contractVOList.isEmpty()) {

				errorMessages.add(new GenericException(CommonErrorCodes.NO_MATCH_FOR_CONTRACT_NUMBER));

			} else if (contractVOList.size() == 1) {
				actionForm.setSelectedCompanyName(contractVOList.get(0).getCompanyName());
				actionForm.setSelectedContractName(contractVOList.get(0).getContractName());
				forward = doApplyContract(actionForm, bindingResult, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}
			// the search text should be contract name
		} else {

			contractVOList = ContractSearchUtility.searchByContractName(request, userProfile, contractSearchText,
					contractStatusList, includeIAStatus);

		}

		if (errorMessages.isEmpty()) {
			// no match found for the entered contract name/number
			// then display a error message
			if (contractVOList == null || contractVOList.isEmpty()) {

				errorMessages.add(new GenericException(CommonErrorCodes.NO_MATCH_FOR_CONTRACT_NAME));

				// IF there are no errors then format the results
			} else {
				actionForm.setFilterResultsId(FapConstants.CONTRACT_SERACH_RESULTS);

				// If there are more than 35 results, display only the first 35
				if (contractVOList.size() > 35) {
					actionForm.setContractResultsList(
							FapFilterUtility.createContractResultsList(contractVOList.subList(0, 35)));
				} else {
					actionForm.setContractResultsList(FapFilterUtility.createContractResultsList(contractVOList));
				}

				// set the results size to the form. this would be used to
				// display
				// the messages in the contract search dialog
				actionForm.setContractResultsSize(contractVOList.size());
			}
		}

		if (!errorMessages.isEmpty()) {
			setErrorsInRequest(request, errorMessages);
			actionForm.setMessagesExist(true);
		}

		return forwards.get("continue");
	}

	/**
	 * CASE 1: The action is triggered from Contract Funds & Performance IF: the
	 * contract number is equal to the bobContext contract number THEN do the
	 * filter for this contract. ELSE: Create the bobContext for the new
	 * contract and reload the page
	 * 
	 * CASE 2: The action is triggered from the Generic Funds & Performance IF:
	 * the company name matches with the currently selected contract THEN do the
	 * filter for this contract. ELSE: reload the page which matches to the
	 * contract's company name
	 * 
	 * @param mapping
	 * @param actionform
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=applyContract" }, method = { RequestMethod.POST })
	public String doApplyContract(@Valid @ModelAttribute("fapForm") FapForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}

		if (actionForm.isContractMode()) {
			String currentContractNumber = getCurrentContractNumber(request);
			if (StringUtils.equals(currentContractNumber, actionForm.getContractSearchText())) {
				 forward =super.doFilter(actionForm, request, response);
				return  StringUtils.contains(forward, '/') ? forward : forwards.get(forward);

			} else {
				setAttributesInSession(request, FapConstants.ATTR_CONTRACT_NUMBER, actionForm.getContractSearchText());
				setAttributesInSession(request, FapConstants.ATTR_GROUP_BY, actionForm.getGroupBySelect());
				if (StringUtils.isNotEmpty(actionForm.getTabSelected())) {
					setAttributesInSession(request, FapConstants.ATTR_TAB_SELECTED, actionForm.getTabSelected());
				}
				BobContextUtils.setUpBobContext(actionForm.getContractSearchText(), request);

				BobContext bob = BDSessionHelper.getBobContext(request);
				if (bob == null || bob.getCurrentContract() == null) {
					// return mapping.findForward(BDConstants.BOB_PAGE_FORWARD);
					return BDConstants.BOB_PAGE_FORWARD;
				}

				// set the content location for CMA
				setContentLocation(request, bob.getCurrentContract().getCompanyCode());

				actionForm.setFilterResultsId("reloadPage");

				if (actionForm.isAdvanceFilterEnabled()) {
					setAttributesInSession(request, "advanceFilterEnabled", true);
				}

				return forwards.get(FapConstants.FORWARD_CONTINUE);
			}
		} else {
			if (doesCompanyNameMatch(actionForm)) {
				 forward =super.doFilter(actionForm, request, response);
				return  StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			} else {
				actionForm.setFilterResultsId("toggleURL");

				setAttributesInSession(request, FapConstants.ATTR_CONTRACT_NUMBER, actionForm.getContractSearchText());
				setAttributesInSession(request, FapConstants.ATTR_GROUP_BY, actionForm.getGroupBySelect());
				if (StringUtils.isNotEmpty(actionForm.getTabSelected())) {
					setAttributesInSession(request, FapConstants.ATTR_TAB_SELECTED, actionForm.getTabSelected());
				}

				return forwards.get(FapConstants.FORWARD_CONTINUE);
			}
		}
	}

	/**
	 * Gets the Columns Value Object and the Tab's value Object from the request
	 * and sets it to the form
	 * 
	 * @param mapping
	 * @param actionform
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=displayTabs" }, method = { RequestMethod.POST })
	public String doDisplayTabs(@Valid @ModelAttribute("fapForm") FapForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}

		actionForm.setColumnsInfo((HashMap<String, List>) request.getAttribute(FapConstants.COLUMNS_INFO_OBJECT));

		actionForm.setFundcheckAsOfDate((String) request.getAttribute(FapConstants.FUND_CHECK_ASOFDATE));

		return forwards.get(actionForm.getTabSelected());
	}

	/**
	 * Removes the fundsAndPerformance value object from the session. This
	 * method will be triggered when there are contract related error messages.
	 * In this case only the column headers for the report table to should be
	 * displayed
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=displayHeaders" }, method = { RequestMethod.POST })
	public String doDisplayHeaders(@Valid @ModelAttribute("fapForm") FapForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}

		String tabSelected = actionForm.getTabSelected();

		if (StringUtils.isBlank(tabSelected)) {
			tabSelected = FapConstants.FUND_INFORMATION_TAB_ID;
		} else {
			tabSelected = getTabName(actionForm);
		}

		// Remove the value object and the footnotes symbols from the session
		removeAttributesFromSession(request, FapConstants.VO_FUNDS_AND_PERFORMANCE);
		removeAttributesFromSession(request, "symbolsArray");

		synchronized (FapController.class) {
			FapTabUtility.asOfDates = new HashMap<String, Date>();
		}

		FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request
				.getSession().getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

		HashMap<String, List> currentTabColumns = new HashMap<String, List>();
		if (FapConstants.FUNDSCORECARD_TAB_ID.equals(tabSelected)) {
			currentTabColumns = (HashMap<String, List>) FapReportsUtility.getHeaderValueObject(tabSelected,
					FapTabUtility.class, fundScoreCardMetricsSelection);
		} else {
			currentTabColumns = (HashMap<String, List>) FapReportsUtility.getHeaderValueObject(tabSelected,
					FapTabUtility.class, FapConstants.WEB_FORMAT);
		}

		// create the columns info for the tab
		actionForm.setColumnsInfo(currentTabColumns);

		if (FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY.equals(actionForm.getBaseFilterSelect())) {
			actionForm.setContractSearchText(actionForm.getContractSelect());
		}

		return forwards.get(tabSelected);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=enableCustomQuery" }, method = { RequestMethod.POST })
	public String doEnableCustomQuery(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}
		forward = super.doEnableCustomQuery(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=InsertOrRemoveRows" }, method = { RequestMethod.POST })
	public String doInsertOrRemoveRows(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}
		forward = super.doInsertOrRemoveRows(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=toggleValue" }, method = { RequestMethod.POST })
	public String doToggleValue(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}
		 forward = super.doToggleValue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=SaveCustomQuery" }, method = { RequestMethod.POST })
	public String doSaveCustomQuery(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}
		 forward = super.doSaveCustomQuery(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=CustomQueryFilter" }, method = { RequestMethod.POST })
	public String doCustomQueryFilter(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}
		forward = super.doCustomQueryFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=customQueryEvents" }, method = { RequestMethod.POST })
	public String doCustomQueryEvents(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doCustomQueryEvents(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=advanceFilter" }, method = { RequestMethod.POST })
	public String doAdvanceFilter(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doAdvanceFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=InnerFilterOption" }, method = { RequestMethod.POST })
	public String doInnerFilterOption(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doInnerFilterOption(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=FilterFundIds" }, method = { RequestMethod.POST })
	public String doFilterFundIds(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		 forward = super.doFilterFundIds(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=SortForSloshBoxes" }, method = { RequestMethod.POST })
	public String doSortForSloshBoxes(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doSortForSloshBoxes(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = {"action=displayAssetClass"}, method = { RequestMethod.GET })
	public String doDisplayAssetClass(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doDisplayAssetClass(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, method = { RequestMethod.POST })
	public String doDefault(@Valid @ModelAttribute("fapForm") FapForm form, 
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=sort" }, method = { RequestMethod.POST })
	public String doSort(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=reportsAndDownload" }, method = { RequestMethod.POST })
	public String doReportsAndDownload(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doReportsAndDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=changeDropDownList" }, method = { RequestMethod.POST })
	public String doChangeDropDownList(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doChangeDropDownList(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, 
			params = {"action=filterFundScorecardMetrics" }, method = { RequestMethod.POST })
	public String doFilterFundScorecardMetrics(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doFilterFundScorecardMetrics(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=storeFapFormInSession" }, method = { RequestMethod.POST })
	public String doStoreFapFormInSession(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doStoreFapFormInSession(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=EditCriteria" }, method = { RequestMethod.POST })
	public String doEditCriteria(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doEditCriteria(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=SaveFundList" }, method = { RequestMethod.POST })
	public String doSaveFundList(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doSaveFundList(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=downloadCsv" }, method = { RequestMethod.GET })
	public String doDownloadCsv(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doDownloadCsv(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=downloadCsvAll" }, method = { RequestMethod.GET })
	public String doDownloadCsvAll(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doDownloadCsvAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=printPdf" }, method = { RequestMethod.GET })
	public String doPrintPdf(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doPrintPdf(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = { "/fapFilterAction/", "/tabs/" }, params = { "action=generateIReport" }, method = { RequestMethod.GET })
	public String doGenerateIReport(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		forward = super.doGenerateIReport(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}
