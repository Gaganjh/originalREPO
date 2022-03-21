package com.manulife.pension.ps.web.fandp;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.customquery.CustomQueryController;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.ColumnsInfoBean;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.fap.util.FapReportsUtility;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.fund.fandp.valueobject.FundBaseInformation;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

public class PSTPAFandpBaseController extends CustomQueryController {

	/**
	 * The preExecute() method was overridden to solve the back button problem.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws SystemException
	 */
	@Override
	public String preExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		FapForm fapForm = (FapForm) form;

		HttpSession session = request.getSession(false);

		if ((session == null) || (SessionHelper.getUserProfile(request) == null)) {
			// set the results ID
			fapForm.setFilterResultsId(FapConstants.SESSION_EXPIRED);
			return forwards.get(FapConstants.FORWARD_SESSION_EXPIRED);

		}

		return null;
	}

	@Override
	protected long getProfileId(HttpServletRequest request) {
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		long profileId = userProfile.getPrincipal().getProfileId();

		if (userProfile.getRole().isInternalUser()) {
			UserInfo tpaUserInfo = (UserInfo) request.getSession(false).getAttribute(Constants.TPA_USER_INFO);

			profileId = tpaUserInfo.getProfileId();
		}

		return profileId;
	}

	@Override
	protected boolean userAllowedToViewSavedData(HttpServletRequest request) {
		return true;
	}

	@SuppressWarnings("unchecked")
	protected void setWarningsInRequest(HttpServletRequest request, Collection warnings) {
		request.setAttribute(Constants.PS_WARNINGS, warnings);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setInformationMessagesInRequest(HttpServletRequest request, Collection warnings) {
		request.setAttribute(Constants.PS_INFO_MESSAGES, warnings);
	}

	@Override
	protected boolean includeNMLFunds(HttpServletRequest request) throws SystemException {
		return false;
	}

	@Override
	protected boolean includeOnlyMerrillCoveredFunds(HttpServletRequest request, int contractNumber)
			throws SystemException {
		return false;
	}

	@Override
	protected boolean includeContractFundsOption(HttpServletRequest request) throws SystemException {
		return false;
	}

	/**
	 * This method gets layout page for the given layout id.
	 * 
	 * @param path
	 * @return LayoutPage
	 */
	protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {

		LayoutBean layoutBean = LayoutBeanRepository.getInstance().getPageBean("/fap/fap.jsp");
		LayoutPage layoutPageBean = (LayoutPage) layoutBean.getLayoutPageBean();
		return layoutPageBean;
	}

	/**
	 * This method needs to be overridden by any Report that needs PDF
	 * Generation functionality. This method would generate the XML file.
	 * 
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return Object
	 * @throws ParserConfigurationException
	 * @throws SystemException
	 * @throws ContentException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Document prepareXMLFromReport(AutoForm form, HashMap<String, List> columns,
			HashMap<String, List<? extends FundBaseInformation>> reportData, HttpServletRequest request)
			throws ParserConfigurationException, SystemException, NumberFormatException, ContentException {

		PDFDocument doc = new PDFDocument();
		Element rootElement = doc.createRootElement(PdfConstants.FUNDS_AND_PERFORMANCE);

		String selectedTab = ((FapForm) form).getTabSelected();

		String modifiedLineUp = (String) request.getAttribute(FapConstants.MODIFIED_LINEUP);
		String currentClass = (String) request.getSession().getAttribute("Class");
		String currentGroupByOption = (String) request.getSession().getAttribute("GroupBy");
		String fundMenu = (String) request.getSession().getAttribute(FapConstants.FUND_MENU);
		 String currentContract = (String) request.getSession().getAttribute(
	                FapConstants.CONTRACT_NUMBER);
	        
	        FapForm fapForm = new FapForm();
	        int contractNumber = 0;
			String contractSearchText = fapForm.getContractSearchText();
			
			// check if the search text is a contract number
			if (StringUtils.isNotBlank(contractSearchText) && 
					StringUtils.isNumeric(contractSearchText)) {
				contractNumber = Integer.parseInt(
						fapForm.getContractSearchText());
			}
			boolean isMerrillLynchAdvisor = includeOnlyMerrillCoveredFunds(request, contractNumber);
	        boolean isMerrillLynchContract = false;
	        if(currentContract != null){
	        	isMerrillLynchContract = includeOnlyMerrillCoveredFunds(request, Integer.parseInt(currentContract));
	        }
		String currentView = "All Funds";
		if (!currentView.equals(fundMenu)) {
			currentView = fundMenu;
		}

		if ("true".equals(modifiedLineUp)) {
			currentView = currentView + " - Modified Lineup";
		}

		if (currentClass != null) {
			currentClass = FundClassUtility.getInstance().getFundClassName(currentClass);
		}

		if ("filterRiskCategoryFunds".equals(currentGroupByOption)) {
			currentGroupByOption = "Risk/Return Category";
		} else {
			currentGroupByOption = "Asset Class";
		}

		doc.appendTextNode(rootElement, PdfConstants.TAB_NAME, selectedTab);

		FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request
				.getSession().getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

		if (fundScoreCardMetricsSelection != null) {
			doc.appendTextNode(rootElement, PdfConstants.SHOW_MORNIGSTAR_SCORECARD,
					String.valueOf(fundScoreCardMetricsSelection.isShowMorningstarScorecardMetrics()));
			doc.appendTextNode(rootElement, PdfConstants.SHOW_FI360_SCORECARD,
					String.valueOf(fundScoreCardMetricsSelection.isShowFi360ScorecardMetrics()));
			doc.appendTextNode(rootElement, PdfConstants.SHOW_RPAG_SCORECARD,
					String.valueOf(fundScoreCardMetricsSelection.isShowRpagScorecardMetrics()));
		}

		if (FapConstants.FUNDSCORECARD_TAB_ID.equals(((FapForm) form).getTabSelected())) {
			Date scorecardDate = FapTabUtility.asOfDates.get("FUND_SCORECARD_EFFECTIVE_DATE_KEY");
			String formattedValue = DateRender.formatByPattern(scorecardDate, null, RenderConstants.MEDIUM_MDY_SLASHED);
			doc.appendTextNode(rootElement, PdfConstants.JHSCORECARD_AS_OF_DATE, formattedValue);
		}

		String logoFileName = null;
		logoFileName = Constants.SITEMODE_USA.equals(Environment.getInstance().getSiteLocation())
				? "JH_blue_resized.gif" : "JH_blue_resized_NY.gif";

		FapReportsUtility.setLogoAndPageName(getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH, request), doc,
				rootElement, logoFileName);

		doc.appendTextNode(rootElement, PdfConstants.ASOF_DATE, ((FapForm) form).getAsOfDate());

		// setting into1 and intro2 elements
		PdfHelper.setIntro1Intro2XMLElements(getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH, request), doc,
				rootElement);

		// set headers specific to tab
		Map<String, String> tabHeaders = getAllTabHeaders((FapForm) form);
		String tabHeader = tabHeaders.get(selectedTab);
		PdfHelper.convertIntoDOM(PdfConstants.TAB_HEADER, rootElement, doc, tabHeader);

		// set headers for generic view disclosure for TPAs
		//tabHeader = tabHeaders.get(FapConstants.FAP_GENERIC_VIEW_ID);
		//PdfHelper.convertIntoDOM(PdfConstants.TAB_HEADER, rootElement, doc, tabHeader);
		
		Element filters = doc.createElement("filters");
		doc.appendElement(rootElement, filters);

		doc.appendTextNode(filters, "currentView", currentView);

		if (currentClass != null) {
			doc.appendTextNode(filters, "currentClass", currentClass);
		}
		if (currentGroupByOption != null) {
			doc.appendTextNode(filters, "currentGroupByOption", currentGroupByOption);
		}

		/*
		 * Level 1 Header section
		 */
		List<List<ColumnsInfoBean>> levelOneTabs = columns.get(FapConstants.COLUMN_HEADINGS_LEVEL_1);
		FapReportsUtility.setLevel1HeaderElements(doc, rootElement, levelOneTabs, selectedTab);

		/*
		 * Level 2 Header section
		 */
		List<ColumnsInfoBean> levelTwoTabs = columns.get(FapConstants.COLUMN_HEADINGS_LEVEL_2);
		 FapReportsUtility.setLevel2HeaderElements(doc, rootElement, levelTwoTabs, selectedTab, isMerrillLynchAdvisor, isMerrillLynchContract, currentView);
		/*
		 * Table values section
		 */
		 FapReportsUtility.setFundDeatilsElements(doc, rootElement, levelTwoTabs, reportData,
	                selectedTab, ((FapForm) form).getFundcheckAsOfDate(), isMerrillLynchAdvisor,isMerrillLynchContract);

		Location site = null;
		if (CommonConstants.SITEMODE_USA.equals(((FapForm) form).getSiteLocation())) {
			site = Location.USA;
		} else {
			site = Location.NEW_YORK;
		}

		/*
		 * set footnotes section
		 */
		FapReportsUtility.setFootNotesElement(doc, rootElement, request,
				getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH, request), site, reportData, selectedTab,
				getGlobalDisclosureCMAKey(selectedTab));

		return doc.getDocument();
	}

	/**
	 * 
	 * @return Map<String, String>
	 */
	protected Map<String, String> getAllTabHeaders(FapForm form) {
		return FapReportsUtility.getAllTabHeadersForTpa();
	}

	@Override
	protected void postExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		String path = "/fap/fap.jsp";

		LayoutBean bean = LayoutBeanRepository.getInstance().getPageBean(path);

		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).
		if (bean != null) {
			request.setAttribute("layoutPageBean", bean.getLayoutPageBean());
		}
	}

	/**
	 * Since TPA F&P page does not have contract view we always return true. so
	 * that GIFL category will always be displayed for asset class and risk
	 * return.
	 * 
	 * return boolean - always return true because we need to display the GIFL
	 * category for TPA F&P page.
	 */
	@Override
	protected boolean includeGIFL(FapForm fapForm, HttpServletRequest request) throws SystemException {
		return true;
	}

	@Override
	protected void populateAdvisorNameFields(FapForm fapForm, HttpServletRequest request) {
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if (userProfile.isInternalUser()) {
			fapForm.setAdvisorNameDisplayed(true);
		} else {
			fapForm.setAdvisorNameDisplayed(false);
			fapForm.setAdvisorName(
					userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName());
		}
	}

}
