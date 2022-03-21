package com.manulife.pension.ps.web.ipitool;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.list.GrowthList;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWIPiTool;
import com.manulife.pension.service.fee.util.DiscontinuanceContractYearCalcuator;
import com.manulife.pension.service.fee.valueobject.DiscontuanceContractYearParam;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.util.ArrayUtility;
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping(value="/fee")
@SessionAttributes({"ipiToolForm"})
public class IpiToolController extends PsAutoController {

	@ModelAttribute("ipiToolForm")
	public IpiToolForm populateForm() {
		return new IpiToolForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("submission","/fee/hypotheticalTool.jsp");
		forwards.put("summary","/fee/ipiHypotheticalToolSummary.jsp");
		forwards.put("confirmation","/fee/ipiHypotheticalToolConfirmation.jsp");
		forwards.put("regulatoryDisclosure","redirect:/do/fee/disclosure/");
	}

	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	private static final String SUBMISSION = "submission";
	private static final String SUMMARY = "summary";
	private static final String IPITOOL = "ipiTool";
	private static final String STATUS_CODE = "statusCode";
	private static final String ERROR_CODE = "errorCode";
	private static final String GENERATE = "generate";
	private static final String ROWCOUNT = "rowCount";
	private static final String DCVALUES = "dcValues";
	private static final String BACVALUES = "bacValues";
	private static final String CONFIRMATION = "confirmation";
	private static final String IS_DI_FEE_CHANGED = "isDiFeeChanged";
	private static final String DEFAULT_BAND_STARTS = "0.00";
	private static final BigDecimal EMPTY = BigDecimal.ZERO;
	private static final double MAXLIMIT = 999999999.99;
	private static final int MAXDECIMALS = 3;
	private static final Set<String> PREACTIVE_STATUS = Collections
			.unmodifiableSet(ArrayUtility.toUnsortedSet("PS", "DC", "PC", "CA"));

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/hypotheticalTool/", method = {RequestMethod.GET, RequestMethod.POST})
	public String doDefault(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}

		if (getUserProfile(request).getAccess404a5()
				.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL) == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		// IpiToolForm form = (IpiToolForm) actionForm;

		boolean isDIFeeChangedForCurrentandFutureYears = false;

		UserProfile userProfile = getUserProfile(request);
		if (userProfile == null || userProfile.getCurrentContract() == null) {
			// MRL logging
			logger.error("User Profile Object is null");
			throw new SystemException("Not a valid contract");
		}

		if (userProfile.getCurrentContract().getTotalAssets() == null) {
			// MRL logging
			logger.error("Contract Total Assets for the contract [ "
					+ userProfile.getCurrentContract().getContractNumber() + " ] are null");
			throw new SystemException("Contract Total Assets for the contract [ "
					+ userProfile.getCurrentContract().getContractNumber() + " ] are null");
		}

		IpiToolView view = IpiTool.INSTANCE.process(userProfile, form.getCurrentFieldValueMap(),
				form.getInputFieldValueMap(), form.getCurrentBacScale(), form.getInputBacScale(),
				form.getCurrentDiScale(), form.getInputDiScale(), form.getMyAction(), form.isBacChanged(),
				form.isDiChanged());

		if (form.getMyAction().equals(GENERATE)) {
			request.setAttribute(ERROR_CODE, view.getErrorCode().get(ERROR_CODE));
			request.setAttribute(STATUS_CODE, view.getErrorCode().get(STATUS_CODE));
			return forwards.get(view.getForwardUrl());

		}

		if (view.getErrorKeys() != null) {

			Collection errors = super.doValidate(form, request);

			for (int errorFieldFormName : view.getErrorKeys()) {
				errors.add(new GenericException(errorFieldFormName));
			}

			final ArrayList<String> errorFieldNames = new ArrayList<String>();

			if (view.getErrorFields() != null) {
				for (String errorFieldFormName : view.getErrorFields()) {
					errorFieldNames.add("\"" + errorFieldFormName + "\"");
				}
			}
			if (errorFieldNames.isEmpty()) {
				errorFieldNames.add("0"); // required by JSP
			}
			errorFieldNames.trimToSize();
			request.setAttribute(IPITOOL, view);
			SessionHelper.setErrorsInSession(request, errors);
			request.setAttribute(Constants.ERROR_KEYS, errorFieldNames);

			return forwards.get(view.getForwardUrl());

		}

		int index = 0;

		// Set current BAC Values to Form to retain for summary page
		for (BasicAssetChargeLine basicAssetChargeLine : view.getCurrentBacScale()) {
			form.setCurrentBacScaleLine(index, basicAssetChargeLine);
			index++;
		}

		index = 0;

		// Set current DI Values to Form to retain for summary page

		for (String currentDiScaleLine : view.getCurrentDiScale()) {
			form.setCurrentDiScaleLine(index, currentDiScaleLine);
			index++;
		}

		// Set current current fees to Form to retain for summary page

		for (SectionView sectionView : view.getFieldAttributeValue()) {
			for (FieldView fieldView : sectionView.getFields()) {
				form.setCurrentFieldValue(fieldView.getFormName(), fieldView.getCurrentValue());
			}
		}

		if (view.getForwardUrl().equals(SUMMARY)) {

			DiscontuanceContractYearParam discParam = FeeServiceDelegate
					.getInstance(Environment.getInstance().getAppId())
					.getContractDiscontinuanceParameters(userProfile.getCurrentContract().getContractNumber());

			Integer dcYear = new Integer(
					DiscontinuanceContractYearCalcuator.getDiscontinunaceContractYear(discParam, new Date()));

			if (view.getInputDiScale().size() > 0 && StringUtils.isNotBlank((String) view.getInputDiScale().get(0))) {

				isDIFeeChangedForCurrentandFutureYears = isDIFeeChangedForCurrentandFutureYears(dcYear,
						view.getCurrentDiScale(), view.getInputDiScale());
			} else {
				isDIFeeChangedForCurrentandFutureYears = isDIFeeChangedForCurrentandFutureYears(dcYear,
						view.getCurrentDiScale(), view.getCurrentDiScale());
			}

			form.setMyAction(view.getForwardUrl());

		}

		request.setAttribute(IPITOOL, view);
		request.setAttribute(Constants.ERROR_KEYS, "0"); // required by JSP
		request.setAttribute(IS_DI_FEE_CHANGED, isDIFeeChangedForCurrentandFutureYears);
		return forwards.get(view.getForwardUrl());

	}

	 /* @RequestMapping(value="/hypotheticalTool/", params = { "task=printPDF" }, method = { RequestMethod.POST })
	public String doPrintPDF(@Valid @ModelAttribute("ipiToolForm") IpiToolForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		String forward = super.doPrintPDF(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	} */
	
	@RequestMapping(value = "/hypotheticalTool/", params = { "action=previous" }, method = {RequestMethod.POST})
	public String doPrevious(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}

		// IpiToolForm form = (IpiToolForm) actionForm;
		UserProfile userProfile = getUserProfile(request);

		IpiToolView view = IpiTool.INSTANCE.process(userProfile, form.getCurrentFieldValueMap(),
				form.getInputFieldValueMap(), form.getCurrentBacScale(), form.getInputBacScale(),
				form.getCurrentDiScale(), form.getInputDiScale(), form.getMyAction(), false, false);

		request.setAttribute(Constants.ERROR_KEYS, "0"); // required by JSP
		request.setAttribute(IPITOOL, view);
		return forwards.get(SUBMISSION);

	}

	@RequestMapping(value="/hypotheticalTool/", params = { "action=calculateBACharge" }, method = { RequestMethod.POST })
	public String doCalculateBACharge(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		// IpiToolForm form = (IpiToolForm) actionForm;
		BasicAssetChargeLine basicAssetChargeLine;
		String[] bacValues = new String[100];
		StringBuffer bacErrors = null;
		String[] inputBAC = null;
		int index = 0;
		UserProfile userProfile = getUserProfile(request);
		PrintWriter out = response.getWriter();
		bacValues = request.getParameterValues(BACVALUES);

		try {

			bacErrors = IpiToolActionHelper.bacValidator(bacValues);

			if (bacValues.length > 0 && bacErrors.length() == 0) {

				for (String value : bacValues) {

					basicAssetChargeLine = new BasicAssetChargeLine();
					inputBAC = value.split(",");
					String charge = String.valueOf(EMPTY);
					String band = String.valueOf(EMPTY);
					if (inputBAC.length > 0) {
						band = StringUtils.isBlank(inputBAC[0]) ? String.valueOf(EMPTY) : inputBAC[0];
						if (inputBAC.length == 2) {
							charge = StringUtils.isBlank(inputBAC[1]) ? String.valueOf(EMPTY)
									: inputBAC[1].equals("0") ? "0.00" : inputBAC[1];
						} else {
							charge = String.valueOf(EMPTY);
						}

						basicAssetChargeLine.setBandEnd(band);

						if (index == 0) {
							basicAssetChargeLine.setBandStart(DEFAULT_BAND_STARTS);
						} else if (StringUtils.isNotBlank(form.getInputBacScaleLine(index - 1).getBandEnd())
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) > 0
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) < MAXLIMIT) {
							basicAssetChargeLine.setBandStart(String.valueOf(
									Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) + 0.01));
						} else {
							basicAssetChargeLine.setBandStart(String.valueOf(EMPTY));
						}

						basicAssetChargeLine.setCharge(charge);
						form.setInputBacScaleLine(index, basicAssetChargeLine);

						if (form.getCurrentBacScaleLine(index) == null) {
							basicAssetChargeLine.setBandEnd(String.valueOf(EMPTY));
							basicAssetChargeLine.setBandStart(String.valueOf(EMPTY));
							basicAssetChargeLine.setCharge(String.valueOf(EMPTY));
							form.setCurrentBacScaleLine(index, basicAssetChargeLine);
						}

					} else if (form.getCurrentBacScaleLine(index) != null) {
						basicAssetChargeLine.setBandEnd(String.valueOf(EMPTY));

						String bandEnds = index > 0
								&& StringUtils.isNotBlank(form.getInputBacScaleLine(index - 1).getBandEnd())
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) > 0
										? form.getInputBacScaleLine(index - 1).getBandEnd() : String.valueOf(EMPTY);

						basicAssetChargeLine.setBandStart(String.valueOf(Double.parseDouble(bandEnds) > 0
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) < MAXLIMIT
										? Double.parseDouble(bandEnds) + 0.01 : String.valueOf(EMPTY)));

						basicAssetChargeLine.setCharge(String.valueOf(EMPTY));
						form.setInputBacScaleLine(index, basicAssetChargeLine);
					}

					index++;
				}

				index = 0;

				List<BasicAssetChargeLine> finalBacScale = LazyList.decorate(new GrowthList(), new BacFactory());

				List<BasicAssetChargeLine> bacScale = form.getCurrentBacScale();
				BasicAssetChargeLine basicAssetChargeScale;

				if (form.getInputBacScaleLine(0).getBandEnd().equals(String.valueOf(EMPTY))
						&& form.getInputBacScaleLine(0).getCharge().equals(String.valueOf(EMPTY))) {
					finalBacScale = form.getCurrentBacScale();

				} else if (form.getInputBacScaleLine(0).getBandEnd().equals(String.valueOf(EMPTY))
						&& !form.getInputBacScaleLine(0).getCharge().equals(String.valueOf(EMPTY))) {
					for (BasicAssetChargeLine basicAssetCharge : bacScale) {
						basicAssetChargeScale = new BasicAssetChargeLine();
						basicAssetChargeScale.setBandStart(basicAssetCharge.getBandStart());
						basicAssetChargeScale.setBandEnd(basicAssetCharge.getBandEnd());
						basicAssetChargeScale.setCharge(form.getInputBacScaleLine(index).getCharge());
						finalBacScale.add(index, basicAssetChargeScale);
						index++;

					}
				} else if (!form.getInputBacScaleLine(0).getBandEnd().equals(String.valueOf(EMPTY))
						&& form.getInputBacScaleLine(0).getCharge().equals(String.valueOf(EMPTY))) {
					for (BasicAssetChargeLine basicAssetCharge : bacScale) {
						basicAssetChargeScale = new BasicAssetChargeLine();
						basicAssetChargeScale.setBandStart(form.getInputBacScaleLine(index).getBandStart());
						basicAssetChargeScale.setBandEnd(form.getInputBacScaleLine(index).getBandEnd());
						basicAssetChargeScale.setCharge(basicAssetCharge.getCharge());
						finalBacScale.add(index, basicAssetChargeScale);
						index++;

					}

				} else {
					finalBacScale = form.getInputBacScale();

				}

				while (!finalBacScale.get(finalBacScale.size() - 1).getBandEnd().equals("999999999.99")) {
					finalBacScale.remove(finalBacScale.size() - 1);
				}

				BigDecimal totalAssets = userProfile.getCurrentContract().getTotalAssets();
				BigDecimal bacCharge = BigDecimal.ZERO;

				if (PREACTIVE_STATUS.contains(userProfile.getCurrentContract().getStatus())) {
					bacCharge = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).calculateSpaBAC(
							userProfile.getCurrentContract().getContractNumber(),
							BaseAssetTransformer.INSTANCE.transform(finalBacScale),
							(totalAssets == null || totalAssets.signum() == 0) ? BigDecimal.ONE : totalAssets,
							new Date());

				} else {
					bacCharge = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
							.calculateBlendedAssetCharge(userProfile.getCurrentContract().getContractNumber(),
									BaseAssetTransformer.INSTANCE.transform(finalBacScale),
									(totalAssets == null || totalAssets.signum() == 0) ? BigDecimal.ONE : totalAssets);
				}

				bacCharge = bacCharge.setScale(MAXDECIMALS, BigDecimal.ROUND_HALF_UP);
				form.setBacChanged(false);
				form.setBlendedAssetCharge(String.valueOf(bacCharge));
				out.print(bacCharge);
				out.flush();

			} else {
				out.print("{ " + bacErrors + " }");
				out.flush();
			}

		} catch (Exception exception) {
			// TODO Need to handle this
			out.print(Constants.FAILURE);
			out.flush();
		}

		return null;

	}

	@RequestMapping(value="/hypotheticalTool/", params = { "action=calculateDICharge"}, method = {RequestMethod.POST})
	public String doCalculateDICharge(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		// IpiToolForm form = (IpiToolForm) actionForm;
		UserProfile userProfile = getUserProfile(request);
		String[] dcValues = new String[100];
		PrintWriter out = response.getWriter();
		int index = 0, rowCount = 0;
		dcValues = request.getParameterValues(DCVALUES);
		rowCount = Integer.parseInt(request.getParameter(ROWCOUNT));
		try {

			StringBuffer diErrors = IpiToolActionHelper.diValidator(dcValues, rowCount);
			if (diErrors.length() == 0) {
				for (String value : dcValues) {
					value = value.length() >= 0 ? value : StringUtils.EMPTY;
					form.setInputDiScaleLine(index, value);
					if (form.getCurrentDiScale().size() < index + 1 && index < rowCount) {
						form.setCurrentDiScaleLine(index, StringUtils.EMPTY);
					}
					index++;
				}

				if (form.getInputDiScale().size() > rowCount) {
					int m = form.getCurrentDiScale().size();
					int n = form.getInputDiScale().size();
					for (int i = m; i > rowCount; i--) {
						form.getCurrentDiScale().remove(i - 1);
					}
					for (int i = n; i > rowCount; i--) {
						form.getInputDiScale().remove(i - 1);
					}
				}

				DiscontuanceContractYearParam discParam = FeeServiceDelegate
						.getInstance(Environment.getInstance().getAppId())
						.getContractDiscontinuanceParameters(userProfile.getCurrentContract().getContractNumber());

				Integer dcYear = new Integer(
						DiscontinuanceContractYearCalcuator.getDiscontinunaceContractYear(discParam, new Date()));

				String dcVal;

				if (StringUtils.isNotEmpty(form.getInputDiScaleLine(0))) {
					dcVal = dcValues.length >= dcYear && StringUtils.isNotBlank(dcValues[dcYear - 1])
							? ((dcValues[dcYear - 1])) : Constants.DECIMAL_PATTERN;
				} else {
					dcVal = form.getCurrentFieldValue("discontinuanceFee");
				}

				form.setDiscontinuanceFee(
						String.valueOf(new BigDecimal(dcVal).setScale(MAXDECIMALS, BigDecimal.ROUND_HALF_UP)));
				form.setDiChanged(false);
				out.print(new BigDecimal(dcVal).setScale(MAXDECIMALS, BigDecimal.ROUND_HALF_UP));
				out.flush();
			} else {
				// request.setAttribute("diErrors", diErrors );

				out.print("{ " + diErrors + " }");
				out.flush();
				out.flush();
			}

		} catch (Exception exception) {
			// TODO Need to handle this
			out.print(Constants.FAILURE);
			out.flush();
		}

		return null;

	}

	@RequestMapping(value="/hypotheticalTool/", params = { "action=clearBaCharge" }, method = {RequestMethod.POST})
	public String doClearBaCharge(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		// IpiToolForm form = (IpiToolForm) actionForm;
		BasicAssetChargeLine basicAssetChargeLine = new BasicAssetChargeLine();
		basicAssetChargeLine.setBandStart(String.valueOf(EMPTY));
		basicAssetChargeLine.setBandEnd(String.valueOf(EMPTY));
		basicAssetChargeLine.setCharge(String.valueOf(EMPTY));
		PrintWriter out = response.getWriter();
		try {
			for (int index = 0; index < form.getCurrentBacScale().size(); index++) {
				form.setInputBacScaleLine(index, basicAssetChargeLine);
			}
			String bacCharge = StringUtils.EMPTY;
			form.setBlendedAssetCharge(bacCharge);
			form.setBacChanged(false);
			out.print(bacCharge);
			out.flush();
		} catch (Exception exception) {
			// TODO Need to handle this
			out.print(Constants.FAILURE);
			out.flush();
		}

		return null;

	}

	@RequestMapping(value="/hypotheticalTool/", params = { "action=clearDiCharge" }, method = {RequestMethod.POST})
	public String doClearDiCharge(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		// IpiToolForm form = (IpiToolForm) actionForm;
		PrintWriter out = response.getWriter();
		int rowCount = Integer.parseInt(request.getParameter(ROWCOUNT));

		try {
			for (int index = 0; index < rowCount; index++) {
				form.setInputDiScaleLine(index, StringUtils.EMPTY);
			}

			String dcVal = StringUtils.EMPTY;
			form.setDiscontinuanceFee(dcVal);
			form.setDiChanged(false);
			out.print(dcVal);
			out.flush();

		} catch (Exception exception) {
			// TODO Need to handle this
			out.print(Constants.FAILURE);
			out.flush();
		}

		return null;

	}

	@RequestMapping(value="/hypotheticalTool/", params={"action=compareBAC_DC"}, method = {RequestMethod.POST})
	public String doCompareBAC_DC(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}

		// Comparing current and previous BAC Scale values

		// IpiToolForm form = (IpiToolForm) actionForm;
		String[] bacValues = new String[100];
		String[] inputBAC = null;
		boolean isBacChanged = form.isBacChanged();
		BasicAssetChargeLine basicAssetChargeLine;
		int index = 0;
		PrintWriter out = response.getWriter();
		bacValues = request.getParameterValues(BACVALUES);
		boolean isInputMapEmpty = form.getInputBacScale().size() == 0;

		try {

			if (bacValues.length > 0) {

				for (String value : bacValues) {

					inputBAC = value.split(",");
					String charge = String.valueOf(EMPTY);
					String band = String.valueOf(EMPTY);
					basicAssetChargeLine = new BasicAssetChargeLine();
					if (inputBAC.length > 0) {
						band = StringUtils.isBlank(inputBAC[0]) ? String.valueOf(EMPTY) : inputBAC[0];
						if (inputBAC.length == 2) {
							charge = StringUtils.isBlank(inputBAC[1]) ? String.valueOf(EMPTY)
									: inputBAC[1].equals("0") ? "0.00" : inputBAC[1];
						} else {
							charge = String.valueOf(EMPTY);
						}

						if (form.getInputBacScale().size() > index) {
							isBacChanged = StringUtils.isNotBlank(form.getInputBacScaleLine(index).getBandEnd())
									&& StringUtils.isNotBlank(form.getInputBacScaleLine(index).getCharge())
									&& (!band.equals(form.getInputBacScaleLine(index).getBandEnd())
											|| !isValidDecimal(band) || !isValidDecimal(charge)
											|| !charge.equals(form.getInputBacScaleLine(index).getCharge())) ? true
													: false;
							if (isBacChanged) {
								form.setBacChanged(isBacChanged);
							}
						} else if (isInputMapEmpty && !isBacChanged
								&& (!band.equals(String.valueOf(EMPTY)) || !charge.equals(String.valueOf(EMPTY)))) {
							isBacChanged = true;
							form.setBacChanged(isBacChanged);
						}
						basicAssetChargeLine.setBandEnd(band);

						if (index == 0) {
							basicAssetChargeLine.setBandStart(DEFAULT_BAND_STARTS);
						} else if (StringUtils.isNotBlank(form.getInputBacScaleLine(index - 1).getBandEnd())
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) > 0
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) < MAXLIMIT) {
							basicAssetChargeLine.setBandStart(String.valueOf(
									Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) + 0.01));
						} else {
							basicAssetChargeLine.setBandStart(String.valueOf(EMPTY));
						}

						basicAssetChargeLine.setCharge(charge);
						form.setInputBacScaleLine(index, basicAssetChargeLine);

						if (form.getCurrentBacScaleLine(index) == null) {
							basicAssetChargeLine.setBandEnd(String.valueOf(EMPTY));
							basicAssetChargeLine.setBandStart(String.valueOf(EMPTY));
							basicAssetChargeLine.setCharge(String.valueOf(EMPTY));
							form.setCurrentBacScaleLine(index, basicAssetChargeLine);
						}

					} else if (form.getCurrentBacScaleLine(index) != null) {
						basicAssetChargeLine.setBandEnd(String.valueOf(EMPTY));

						String bandEnds = index > 0
								&& StringUtils.isNotBlank(form.getInputBacScaleLine(index - 1).getBandEnd())
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) > 0
										? form.getInputBacScaleLine(index - 1).getBandEnd() : String.valueOf(EMPTY);

						basicAssetChargeLine.setBandStart(String.valueOf(Double.parseDouble(bandEnds) > 0
								&& Double.parseDouble(form.getInputBacScaleLine(index - 1).getBandEnd()) < MAXLIMIT
										? Double.parseDouble(bandEnds) + 0.01 : String.valueOf(EMPTY)));

						basicAssetChargeLine.setCharge(String.valueOf(EMPTY));
						form.setInputBacScaleLine(index, basicAssetChargeLine);
					}
					index++;
				}
				out.print(form.getBlendedAssetCharge());
				out.flush();

			}

		} catch (Exception exception) {

			if (exception instanceof NumberFormatException) {
				form.setBacChanged(true);
			}
			out.print(Constants.FAILURE);
			out.flush();
		}

		// Comparing current and previous DC Scale values

		String DCRowCount = request.getParameter("DcRowCount");
		String[] dcValues = new String[100];
		boolean isDiChanged = form.isDiChanged();
		index = 0;
		dcValues = request.getParameterValues(DCVALUES);
		try {
			if (form.getInputDiScale().size() > 0 && Integer.parseInt(DCRowCount) == form.getInputDiScale().size()) {

				for (String value : dcValues) {
					value = value.length() >= 0 ? value : StringUtils.EMPTY;
					isDiChanged = !value.equals(form.getInputDiScaleLine(index)) ? true : false;
					if (isDiChanged) {
						form.setDiChanged(isDiChanged);
					}
					form.setInputDiScaleLine(index, value);
					if (form.getCurrentDiScale().size() < index + 1 && index < Integer.parseInt(DCRowCount)) {
						form.setCurrentDiScaleLine(index, StringUtils.EMPTY);
					}
					index++;
				}
			} else {
				for (String value : dcValues) {
					isDiChanged = StringUtils.isNotBlank(value) ? true : false;
					if (isDiChanged) {
						form.setDiChanged(isDiChanged);
					}
					value = value.length() >= 0 ? value : StringUtils.EMPTY;
					form.setInputDiScaleLine(index, value);
					if (form.getCurrentDiScale().size() < index + 1 && index < Integer.parseInt(DCRowCount)) {
						form.setCurrentDiScaleLine(index, StringUtils.EMPTY);
					}
					index++;
				}

			}

			if (form.getInputDiScale().size() > Integer.parseInt(DCRowCount)) {

				int m = form.getCurrentDiScale().size();
				int n = form.getInputDiScale().size();
				int rowCount = Integer.parseInt(DCRowCount);
				for (int i = m; i > rowCount; i--) {
					form.getCurrentDiScale().remove(i - 1);
				}
				for (int i = n; i > rowCount; i--) {
					form.getInputDiScale().remove(i - 1);
				}
			}
			out.print(form.getDiscontinuanceFee());
			out.flush();

		} catch (Exception exception) {

			if (exception instanceof NumberFormatException) {
				form.setDiChanged(true);
			}
			out.print(Constants.FAILURE);
			out.flush();
		}
		
		return null;

	}

	private static boolean isValidDecimal(String value) {

		if (!value.contains(".")) {
			return StringUtils.isNumeric(value);

		} else {
			return value.matches("[0-9]*\\d\\.\\d\\d?\\d?\\d?\\d?\\d?");

		}

	}

	private boolean isDIFeeChangedForCurrentandFutureYears(int contractYear, List<String> currentDiScale,
			List<String> inputDiScale) throws SystemException {

		boolean changed = false;
		int currentDISize = currentDiScale.size();
		int inputDISize = inputDiScale.size();

		if (!ListUtils.isEqualList(currentDiScale, inputDiScale)) {

			if (inputDISize >= contractYear) {

				if (currentDISize >= contractYear) {

					if (inputDISize <= currentDISize) {

						for (int i = contractYear - 1; i < currentDISize; i++) {
							if (!StringUtils.equalsIgnoreCase(currentDiScale.get(i), inputDiScale.get(i))) {
								changed = true;
								break;
							}
						}
					} else {
						changed = true;
					}

				} else {
					changed = true;
				}

			}
		}

		return changed;
	}

	@RequestMapping(value = "/hypotheticalTool/", params = { "action=feeCalculationError"}, method = { RequestMethod.POST })
	public String doFeeCalculationError(@Valid @ModelAttribute("ipiToolForm") IpiToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("submission");// if input forward not available,provided default
			}
		}
		List<GenericException> errors = new ArrayList<GenericException>();
		errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		setErrorsInRequest(request, errors);
		request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository.getInstance().getPageBean(EMPTY_LAYOUT_ID));
		return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);

	}

	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */

	@Autowired
	private PSValidatorFWIPiTool psValidatorFWIPiTool;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWIPiTool);
	}

}
