package com.manulife.pension.ps.web.census;

import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DEATH;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DISABILITY;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.PRE_RETIREMENT;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.RETIREMENT;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.VestingServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.VestingExplanationRetriever;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWVestingInformation;
import com.manulife.pension.ps.web.withdrawal.WebConstants;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.employee.valueobject.ParticipantContractVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingException;

@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"vestingInformationForm"})

public class VestingInformationController extends PsController {

	@ModelAttribute("vestingInformationForm")
	public VestingInformationForm populateForm() {
		return new VestingInformationForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("vestingInformation","/census/viewVestingInformation.jsp");

	}

	public static final String ContractIdParamName = "contractId";

	public static final String ProfileIdParamName = "profileId";

	public static final String AsOfDateParamName = "asOfDate";

	public static final String SourceParamName = "source";

	public static final String WithdrawalReason = "wdReason";

	public static final String WithdrawalSource = "wd";

	public static final String AsOfDateFormat = "MM/dd/yyyy";
	public static final String WithdrawalAsOfDateFormat = "MM-dd-yyyy";

	// SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance(AsOfDateFormat);
	public static final FastDateFormat WITHDRAWALS_PARAMETER_DATE_FORMATTER = FastDateFormat
			.getInstance(WithdrawalAsOfDateFormat);

	protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();

	public VestingInformationController() {
		super(VestingInformationController.class);
	}

	@RequestMapping(value="/vestingInformation/", method = {RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			VestingInformation vi = (VestingInformation) request.getSession()
					.getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vi);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("vestingInformation");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String contractIdValue = request.getParameter(ContractIdParamName);
		String profileIdValue = request.getParameter(ProfileIdParamName);
		String asOfDateValue = request.getParameter(AsOfDateParamName);
		String withdrawalReasonValue = request.getParameter(WithdrawalReason);
		String source = request.getParameter(SourceParamName);

		if (StringUtils.isEmpty(contractIdValue)) {
			throw new SystemException("No contractId parameter");
		}

		if (StringUtils.isEmpty(profileIdValue)) {
			throw new SystemException("No profileId parameter");
		}

		int contractId = Integer.parseInt(contractIdValue);
		UserProfile userProfile = SessionHelper.getUserProfile(request);

		if (userProfile != null && userProfile.getCurrentContract() != null) {
			if (userProfile.getCurrentContract().getContractNumber() != contractId) {
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		} else {
			Integer tpaContractId = (Integer) request.getSession(false).getAttribute(Constants.TPA_CONTRACT_ID_KEY);
			if (tpaContractId == null || tpaContractId != contractId) {
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("VestingInformationAction: " + Constants.PROFILE_ID_PARAMETER + "[" + profileIdValue + "], "
					+ Constants.AS_OF_DATE_PARAMETER + "[" + asOfDateValue + "], " + Constants.SOURCE_PARAMETER + "["
					+ source + "]");
		} // fi

		long profileId = Long.parseLong(profileIdValue);
		Date asOfDate = null;
		if (!StringUtils.isEmpty(asOfDateValue)) {

			try {
				asOfDate = WITHDRAWALS_PARAMETER_DATE_FORMATTER.parse(asOfDateValue);
			} catch (ParseException e) {
				logger.error("An invalid date format :" + asOfDateValue);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("As Of Date to use: [" + asOfDate + "]");
		} // fi

		// if no date is passed, use the current calendar day
		if (asOfDate == null) {
			asOfDate = new Date();
		}

		request.setAttribute(Constants.AS_OF_DATE_PARAMETER, DATE_FORMATTER.format(asOfDate));

		Employee employee = new EmployeeServiceFacade().getEmployee(profileId, contractId, asOfDate, true);

		VestingInformation vi = new VestingInformation();

		checkPartialParticipantStatus(employee, vi);

		VestingServiceDelegate delegate = VestingServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		EmployeeDetailVO evo = employee.getEmployeeDetailVO();
		EmployeeVestingInformation evi = null;
		try {
			if (WithdrawalSource.equalsIgnoreCase(source)) {
				evi = delegate.getEmployeeWithdrawalVestingInformation(contractId, profileId, withdrawalReasonValue,
						asOfDate);
			} else {
				evi = delegate.getEmployeeVestingInformation(contractId, profileId, asOfDate);
			}
		} catch (VestingException e) {
			logger.error("Fail to get vesting information for contract id: " + contractId + " profile_id: " + profileId,
					e);
		}

		vi.setAsOfDate(asOfDate);
		vi.setEmployeeFirstName(evo.getFirstName());
		vi.setEmployeeMiddleInit(evo.getMiddleInitial());
		vi.setEmployeeLastName(evo.getLastName());
		vi.setEmployeeSSN(evo.getSocialSecurityNumber());
		vi.setEmployeeVestingInformation(evi);

		// final PlanData planData =
		// ContractServiceDelegate.getInstance().readPlanData(new Integer(contractId),
		// true);
		final PlanDataLite planDataLite = ContractServiceDelegate.getInstance()
				.getPlanDataLight(new Integer(contractId));
		Date planLastUpdatedDate = ContractServiceDelegate.getInstance().getPlanLastUpdatedDate(contractId,
				(Collection) actionForm.getPlanFieldNames());

		vi.setFullyVestedOnDeath(planDataLite.getWithdrawalReasons().contains(DEATH));
		vi.setFullyVestedOnRetirement(planDataLite.getWithdrawalReasons().contains(RETIREMENT));
		vi.setFullyVestedOnEarlyRetirement(planDataLite.getWithdrawalReasons().contains(PRE_RETIREMENT));
		vi.setFullyVestedOnDisability(planDataLite.getWithdrawalReasons().contains(DISABILITY));

		vi.setPlanHoursOfService(planDataLite.getHoursOfService());
		vi.setPlanLastUpdatedDate(planLastUpdatedDate);

		// store effective inputs to the vestingEffectiveInput map
		vi.storeEffectiveInputs(evi);

		// get the dynamic explanation
		List<Integer> cmaList = VestingExplanationRetriever.getInstance().retrieveExplanation(vi, employee, evi,
				withdrawalReasonValue, asOfDate);
		vi.setExplanation(cmaList);

		final Map<String, Collection> lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
		final List<MoneyTypeVO> list = (List<MoneyTypeVO>) lookupData.get(PlanConstants.MONEY_TYPES_BY_CONTRACT);
		Collections.sort(list, new VestingMoneyTypeComparator());
		vi.setMoneyTypes(list);

		final Collection<VestingSchedule> vestingSchedules = planDataLite.getVestingSchedules();
		vi.setVestingSchedules(vestingSchedules);

		// populate all param info
		EmployeeVestingInfo allParamInfo = serviceFacade.getEmployeeVestingInfo(new Long(profileId),
				userProfile.getCurrentContract().getContractNumber(), new Date(), VestingType.ALL, true, false, true);
		EmployeeVestingInfo filteredStatusInfo = actionForm.filterOutCanceledStatus(userProfile, allParamInfo);
		vi.getVestingParamInfo().put(VestingType.EMPLOYMENT_STATUS, filteredStatusInfo);
		vi.getVestingParamInfo().put(VestingType.VYOS, allParamInfo);
		if (allParamInfo.getHireDate() != null) {
			vi.setEmployeeHireDateInfo(allParamInfo.getHireDate());
		}

		// Retrieve fullyVestedIndicator again, but don't use any date so that we
		// retrieve whatever
		// value is in CSDB (could be future dated as well)
		vi.getVestingParamInfo().put(VestingType.FULLY_VESTED_IND,
				actionForm.getFullyVestedInfoIgnoringDate(serviceFacade, userProfile, allParamInfo));

		actionForm.populateForm(vi);

		request.setAttribute(CensusConstants.VESTING_INFO, vi);
		request.getSession().setAttribute(CensusConstants.VESTING_INFO, vi);
		return forwards.get("vestingInformation");
	}

	/**
	 * {@inheritDoc}
	 */
	protected String preExecute(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		// Get the existing value.
		final Object lastPage = request.getSession().getAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION);

		// This operation resets/clears the last active page location.
		final String forwardResult = super.preExecute(actionForm, request, response);

		// Put the previously the existing value back.
		request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, lastPage);

		return forwardResult;
	}

	private void checkPartialParticipantStatus(final Employee employee, final VestingInformation vestingExplanation) {

		final ParticipantContractVO participantContractVo = employee.getParticipantContract();
		if (participantContractVo != null) {
			final String participantStatusCode = participantContractVo.getParticipantStatusCode();

			if (CensusVestingDetails.isParticipantStatusPartial(participantStatusCode)) {
				// The status code is a partial code.
				vestingExplanation.setPartialParticipantStatus(Boolean.TRUE);
			} else {
				vestingExplanation.setPartialParticipantStatus(Boolean.FALSE);
			} // fi
		} // fi
	}
	
	@Autowired
	   private PSValidatorFWVestingInformation  psValidatorFWVestingInformation;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWVestingInformation);
	}
	
	
}
