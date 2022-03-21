package com.manulife.pension.bd.web.fap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.fap.validators.CheckDates;
import com.manulife.pension.bd.web.fap.validators.CheckFunds;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.ChartDataBean;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.util.content.GenericException;

/**
 * Action class for the Performance Charting. It gets the data from the view
 * funds database.
 *
 * @author SAyyalusamy
 **/
@Controller
@RequestMapping(value = "/fap")
@SessionAttributes({"bdPerformanceChartInputForm"})

public class PerformanceChartInputController extends BDController {
	
	@ModelAttribute("bdPerformanceChartInputForm")
	public BDPerformanceChartInputForm populateForm() {
		return new BDPerformanceChartInputForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/investment/performanceChartInput.jsp");
		forwards.put("results","redirect:/do/fap/performanceChartResult/");
	}

	/**
	 * This method used to validate the form inputs like dates, funds. To
	 * validate dates and funds it calls the classes
	 * CheckDates.java,CheckFunds.java.
	 * 
	 */

	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

	// splitter text
	public static String SPLITTER_TEXT = "-------------- Market Indices --------------";
	public static String ON = "on";
	public static String OFF = "off";
	public static String FILTER = "filter";
	public static String B = "B";
	public static String N = "N";
	public static String Y = "Y";
	public static String CLASS1 = "CL5";
	public static String RESULTS = "results";
	public static String SELECT_OPTIONS = "Select investment option or index";
	public static String POOLED_FUNDS = "POOLED";
	public static String INDEX_FUNDS = "INDEX";
	public static String FUND_CLASS_LIST = "fundClassList";
	public static String INDEX_FUNDS_LIST = "indexFunds";

	// Default Constructor
	public PerformanceChartInputController() {
		super(PerformanceChartInputController.class);
	}

	/**
	 * This method performs the operations like getting funds, viewing the chart
	 * for given input criteria.
	 */
	@RequestMapping(value = "/performanceChartInput", method = { RequestMethod.GET })
	public String doExecute(
			@Valid @ModelAttribute("bdPerformanceChartInputForm") BDPerformanceChartInputForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		
		Collection errors = doValidate(actionForm, request);
    	if(!errors.isEmpty()){
    		setErrorsInRequest(request, errors);
    		return forwards.get("input");
    	}
		BDUserProfile userProfile = (BDUserProfile) BDSessionHelper.getUserProfile(request);
		
			// Reset in all the other cases: RESET or no action specified
			actionForm.resetFundSelection();
			actionForm.setHideDivSec(true);
			if (userProfile.getDefaultFundListing() != null) {
				actionForm.setUserPreference(userProfile.getDefaultFundListing().getCode());
			} else {
				actionForm.setUserPreference(UserSiteInfoValueObject.SiteLocation.USA.getCode());
			}

			actionForm.setCheckBoxValue(N);
			actionForm.setNMLSelection(false);
			actionForm.setFundClass(B);
			actionForm.setFundType("filterAllAvailableFunds");
			appGetFundUnitValueHistorySummaries(request, request.getSession(false), actionForm, userProfile);

			List<DeCodeVO> classList = getFundClassesList();
			if (classList != null) {
				request.getSession(false).setAttribute(FUND_CLASS_LIST, classList);
			}
			if (request.getAttribute(BdBaseController.ERROR_KEY) != null) {
				request.removeAttribute(BdBaseController.ERROR_KEY);
			}
			return forwards.get("input");
		
	}
	
	@RequestMapping(value ="/performanceChartInput/", params={"button=view"}, method = {RequestMethod.GET}) 
    public String doView(@Valid @ModelAttribute("bdPerformanceChartInputForm") BDPerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}
    	Collection errors = doValidate(actionForm, request);
    	if(!errors.isEmpty()){
    		setErrorsInRequest(request, errors);
    		return forwards.get("input");
    	}

		actionForm.setButton("");
		if (BDConstants.COMPANY_NAME_NY.equals(actionForm.getUserPreference())) {
			ApplicationHelper.setRequestContentLocation(request, Location.NEW_YORK);
		}
		actionForm.setFromInput(true);
		actionForm.setChartDataBean((ChartDataBean) request.getAttribute(BDConstants.CHART_DATA_BEAN));
		return forwards.get(RESULTS);
	
    }
    
    @RequestMapping(value ="/performanceChartInput/", params={"button=reset"}, method = {RequestMethod.GET}) 
    public String doReset(@Valid @ModelAttribute("bdPerformanceChartInputForm") BDPerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}
    	BDUserProfile userProfile = (BDUserProfile) BDSessionHelper.getUserProfile(request);
		// Reset in all the other cases: RESET or no action specified

		actionForm.resetFundSelection();
		actionForm.setHideDivSec(true);
		if (userProfile.getDefaultFundListing() != null) {
			actionForm.setUserPreference(userProfile.getDefaultFundListing().getCode());
		} else {
			actionForm.setUserPreference(UserSiteInfoValueObject.SiteLocation.USA.getCode());
		}

		actionForm.setCheckBoxValue(N);
		actionForm.setNMLSelection(false);
		actionForm.setFundClass(B);
		actionForm.setFundType("filterAllAvailableFunds");
		appGetFundUnitValueHistorySummaries(request, request.getSession(false), actionForm, userProfile);

		List<DeCodeVO> classList = getFundClassesList();
		if (classList != null) {
			request.getSession(false).setAttribute(FUND_CLASS_LIST, classList);
		}
		if (request.getAttribute(BdBaseController.ERROR_KEY) != null) {
			request.removeAttribute(BdBaseController.ERROR_KEY);
		}
		return forwards.get("input");
	
    }

	/**
	 * This method is used to get the funds for default selection.
	 * 
	 * @param session
	 * @param form
	 * @throws SystemException
	 */
	private void appGetFundUnitValueHistorySummaries(HttpServletRequest request, HttpSession session,
			BDPerformanceChartInputForm form, BDUserProfile userProfile) throws SystemException {
		FundServiceDelegate delegate = FundServiceDelegate.getInstance();
		String companyCode = "";
		if (BDConstants.COMPANY_NAME_US.equals(form.getUserPreference())) {
			companyCode = BDConstants.COMPANY_ID_US;
		} else if (BDConstants.COMPANY_NAME_NY.equals(form.getUserPreference())) {
			companyCode = BDConstants.COMPANY_ID_NY;
		}
		// FandpFilterCriteria filterCriteria =new
		// FandpFilterCriteria(null,null,CLASS1,0,companyCode);
		// Collection<Fund> allFunds =
		// delegate.getFundsForPerformanceChart(filterCriteria);
		
		List<Fund> jhiFundsList = new Vector<>();
		Collection<String>jhiFunds = delegate.getJHIFunds();
		Collection<String>svgFunds = new ArrayList<>();
		Collection<Fund> allFunds = delegate.getAllFunds();
		//Retrieving SVGIF Funds
		Collection<SvgifFund> svgifFunds = delegate.getSVGIFDefaultFunds();
		if(svgifFunds != null){
		for (SvgifFund svgifFund :svgifFunds) {
			svgFunds.add(svgifFund.getFundId());
		}
		}
		for(Fund funds :allFunds){
		if (jhiFunds.contains(funds.getFundId())){
			jhiFundsList.add(funds);
		}
		}
		
		allFunds = removeLSPSFunds(allFunds);
		Collection<Fund> indexFunds = delegate.getMarketIndexFunds();

		Fund[] fundArray = new Fund[allFunds.size()];
		fundArray = allFunds.toArray(fundArray);

		Fund[] fundArray2 = new Fund[allFunds.size()];
		System.arraycopy(fundArray, 0, fundArray2, 0, fundArray.length);

		Arrays.sort(fundArray2, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				String name1 = ((Fund) obj1).getFundName();
				String name2 = ((Fund) obj2).getFundName();
				return name1.compareTo(name2);
			}
		});

		Fund[] indexFundArray = new Fund[indexFunds.size()];
		indexFundArray = indexFunds.toArray(indexFundArray);

		Fund[] indexFundArray2 = new Fund[indexFunds.size()];
		System.arraycopy(indexFundArray, 0, indexFundArray2, 0, indexFundArray.length);

		Arrays.sort(indexFundArray2, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				String name1 = ((Fund) obj1).getFundName();
				String name2 = ((Fund) obj2).getFundName();
				return name1.compareTo(name2);
			}
		});

		Fund[] allFundsInclude = new Fund[allFunds.size() + indexFunds.size()];
		System.arraycopy(fundArray2, 0, allFundsInclude, 0, fundArray2.length);
		System.arraycopy(indexFundArray2, 0, allFundsInclude, allFundsInclude.length - indexFundArray2.length,
				indexFundArray2.length);
		session.setAttribute(BDConstants.VIEW_FUNDS_ORIGINAL, allFundsInclude);
		List<Fund> funds = new Vector<Fund>();
		List<Fund> indices = new Vector<Fund>();

		Fund noSel = new Fund(" ", SELECT_OPTIONS);
		funds.add(noSel);

		boolean ind1 = ((userProfile.getRole() instanceof BDInternalUser) && !userProfile.isInMimic());
		boolean ind2 = showNMLOption(request);
		form.setUserNMLAssociated(ind2);

		form.resetFundIds();

		for (Fund fund : fundArray2) {
			//Removing SVGIF Funds From  Funds list
			if(!svgFunds.contains(fund.getFundId())) {
			funds.add(fund);
			}
		}

		for (Fund fund : indexFundArray2) {
			indices.add(fund);
		}
		Fund splitter = new Fund("", SPLITTER_TEXT);
		funds.add(splitter);
		funds.addAll(indices);
		if (indices != null) {
			session.setAttribute(INDEX_FUNDS_LIST, indices);
		}

		if (funds != null)
			session.setAttribute(BDConstants.VIEW_FUNDS, funds);
		
		if(jhiFundsList != null && jhiFundsList.size()>0){
			session.setAttribute(BDConstants.JHI_FUNDS, jhiFundsList);
		}	

	}

	/**
	 * This method used to validate the form inputs like dates, funds. To
	 * validate dates and funds it calls the classes
	 * CheckDates.java,CheckFunds.java.
	 * 
	 */
	public Collection<GenericException> doValidate(ActionForm form, HttpServletRequest request) {
		 //This code has been changed and added  to 
		 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
		 
		Collection<GenericException> allErrors = new ArrayList<GenericException>();
		Collection<GenericException> dateErrors = new ArrayList<GenericException>();

		BDPerformanceChartInputForm inputForm = (BDPerformanceChartInputForm) form;

		if (!BDConstants.VIEW.equals(inputForm.getButton())) {
			return allErrors;
		}
		if (!inputForm.isFromInput()) {
			inputForm.resetFundSelection();
			inputForm.setButton("");
			return allErrors;
		} else {
			inputForm.setFromInput(false);
		}

		allErrors = CheckFunds.checkFunds(inputForm, request);

		if (B.equals(inputForm.getFundClass())) {
			dateErrors.add(new GenericException(
					BDErrorCodes.CHART_MISSING_FUND_CLASS));
		}

		/**
		 * Validate the date fields
		 */
		if (!isValidDate(inputForm.getStartDate())) {
			dateErrors.add(new GenericException(BDErrorCodes.CHART_START_DATE));
		}

		if (!isValidDate(inputForm.getEndDate())) {
			dateErrors.add(new GenericException(BDErrorCodes.CHART_END_DATE));
		}

		if (dateErrors.size() > 0) {
			allErrors.addAll(dateErrors);
			return allErrors;
		}

		dateErrors = CheckDates.checkDates(inputForm, request);

		if (dateErrors.size() > 0) {
			allErrors.addAll(dateErrors);
		}
		return allErrors;

	}
	/**
	 * This method is used to validate the given date string.
	 * 
	 * @param dateString
	 * @return
	 */
	private boolean isValidDate(String dateString) {

		boolean ret = true;
		SimpleDateFormat sdf = new SimpleDateFormat(BDConstants.CHART_DATE_PATTERN);
		sdf.setLenient(false);

		if (DataValidationHelper.isBlankOrNull(dateString) || (dateString.trim().length() != 7)) {
			ret = false;
		} else {
			try {
				if (sdf.parse(dateString) == null) {
					ret = false;
				} else {
					ret = true;
				}
			} catch (ParseException e) {
				ret = false;
			}
		}

		return ret;
	}

	private boolean showNMLOption(HttpServletRequest request) throws SystemException {

		boolean enableNML = false;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		if (BDUserProfileHelper.associatedWithApprovingFirm(userProfile, NFACodeConstants.NML)
				&& (BDUserProfileHelper.isFirmRep(userProfile) || BDUserProfileHelper.isFinancialRep(userProfile)
						|| BDUserProfileHelper.isFinancialRepAssistant(userProfile))) {

			enableNML = true;
		}

		return enableNML;
	}

	/**
	 * This method gets a list of Fund Classes available in Database.
	 * 
	 * @return - List of Fund Classes.
	 * @throws SystemException
	 */
	public static List<DeCodeVO> getFundClassesList() throws SystemException {

		List<DeCodeVO> classList = new ArrayList<DeCodeVO>();
		// DeCodeVO has CLASS_ID, CLASS_NAME of each class, ordered by
		// CLASS_ORDER_NO.
		Collection<DeCodeVO> tempClassList = FundServiceDelegate.getInstance().getAllFundsClassList();
		for (DeCodeVO classInfo : tempClassList) {
			String className = FundClassUtility.getInstance().getFundClassName(classInfo.getCode());
				classList.add(new DeCodeVO(classInfo.getCode(), className));
		}

		return classList;
	}

	/**
	 * Removes LSPS funds from the collection
	 * 
	 * @param Collection
	 *            of funds, from which the LSPS funds are to be removed
	 * @return Collection of Funds, where the LSPS funds are removed
	 */
	private Collection<Fund> removeLSPSFunds(Collection<Fund> funds) {

		Collection<Fund> filteredFunds = new ArrayList<Fund>();
		for (Fund fund : funds) {
			if (!BDConstants.LSPS_FUNDS.contains(fund.getFundId())) {
				filteredFunds.add(fund);
			}
		}
		return filteredFunds;
	}

}
