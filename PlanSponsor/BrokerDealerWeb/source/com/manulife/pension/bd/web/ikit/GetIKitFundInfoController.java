package com.manulife.pension.bd.web.ikit;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.cache.FootnoteCacheImpl;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.service.contract.ContractConstants.ContractStatus;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.fandp.valueobject.FandpHypotheticalInfo;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;
import com.manulife.pension.service.fund.fandp.valueobject.Morningstar;
import com.manulife.pension.service.fund.fandp.valueobject.PerformanceAndFees;
import com.manulife.pension.service.fund.fandp.valueobject.PricesAndYTD;
import com.manulife.pension.service.fund.fandp.valueobject.StandardDeviation;
import com.manulife.pension.service.fund.valueobject.AssetClassVO;
import com.manulife.pension.service.fund.valueobject.ContractFund;
import com.manulife.pension.service.fund.valueobject.GARates;
import com.manulife.pension.service.fund.valueobject.RiskCategoryVO;
import com.manulife.pension.util.content.helper.ContentUtility;

/**
 * This Action will be called by iKIT application to fetch the Fund Information
 * for Contract Selected Funds. This Action class gets input as Contract Number,
 * Contract Access Code based on which it gets the Fund Information for Contract
 * Selected Funds.
 * 
 * This Action class produces response as XML Stream containing the Fund
 * Information for contract selected funds.
 * 
 * @author harlomte
 * 
 */
@Controller
@RequestMapping( value ="/iKit")

public class GetIKitFundInfoController extends BaseController {

	@ModelAttribute("getIKitFundInfoForm") 
	public GetIKitFundInfoForm populateForm() 
	{
		return new GetIKitFundInfoForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("fail","/do/home/");
	}
    private static final Logger logger= Logger.getLogger(GetIKitFundInfoController.class);
    
	/**
	 * Constructor class.
	 */
	public GetIKitFundInfoController() {
		super(GetIKitFundInfoController.class);
	}

	/**
	 * This method will fetch the contract selected funds information and create
	 * a XML out of it.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return - IKitFundInfo VO holding the information to be placed in
	 *         the XML file.
	 */
	@RequestMapping(value ="/getFundInfo/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doExecute( @ModelAttribute("getIKitFundInfoForm") GetIKitFundInfoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.doExecute() method -> start");
		}
		
		IKitFundInfo fundInfo = fetchFundInformation((GetIKitFundInfoForm)actionForm, request);

		PrintWriter printWriter = response.getWriter();
		response.setContentType(IKitConstants.XML_CONTENT_TYPE);

		Document doc = null;

		String xmlContent = null;
		if (fundInfo != null) {
			// start creating the XML file..
			doc = generateXMLDocument(fundInfo);
			try {
				xmlContent = GetIKitFundInfoUtils.convertDocumentObjToString(doc);
			} catch (TransformerException te) {
				logger
						.error("Transformer Exception occurred during XML generation in GetIKitFundInfoAction.");
			}								  
			printWriter.println(xmlContent);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.doExecute() method -> end");
		}
		
		return null;
	}

	/**
	 * This method fetches the Contract-Selected Funds Information.
	 * 
	 * This method calls the F&P backend to fetch the Funds Information. The
	 * fund information retrieved is the same that is shown in F&P page under
	 * "Performance & Fees" tab, "Prices & YTD" Tab, "Morningstar" tab.
	 * 
	 * @param form
	 * @param request
	 * @return - IKitFundInfo VO holding the information to be placed in
	 *         the XML file.
	 */
	private IKitFundInfo fetchFundInformation(GetIKitFundInfoForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchFundInformation() method -> start");
		}
		
		boolean keepProcessing = true;

		IKitFundInfo iKitFundInfo = new IKitFundInfo();
		iKitFundInfo.setCreatedTs(new Date());

		String contractNumber = form.getContractNumber().trim();
		String contractAccessCode = form.getContractAccessCode().trim();
		Contract contract = null;
		ContractProfileVO contractProfileVO = null;
		Integer contractId = null;

		try {
			contractId = Integer.valueOf(contractNumber);
		} catch (NumberFormatException ne) {
			iKitFundInfo.getErrors().put(
					IKitErrorCodes.CONTRACT_ID_NON_NUMERIC_CODE,
					IKitErrorCodes.CONTRACT_ID_NON_NUMERIC_MSG);
			logger.error(IKitErrorCodes.CONTRACT_ID_NON_NUMERIC_MSG);
			keepProcessing = false;
		}

		if (keepProcessing) {
			try {
				contract = ContractServiceDelegate.getInstance()
						.getContractDetails(contractId, IKitConstants.DI_DURATION_6_MTHS);

				if (contract == null) {
					iKitFundInfo.getErrors().put(
							IKitErrorCodes.CONTRACT_ID_NOT_FOUND_CODE,
							IKitErrorCodes.CONTRACT_ID_NOT_FOUND_MSG);
					logger.error(IKitErrorCodes.CONTRACT_ID_NOT_FOUND_MSG);
					keepProcessing = false;
				}
			} catch (ContractNotExistException cne) {
				iKitFundInfo.getErrors().put(
						IKitErrorCodes.CONTRACT_ID_NOT_FOUND_CODE,
						IKitErrorCodes.CONTRACT_ID_NOT_FOUND_MSG);
				logger.error(IKitErrorCodes.CONTRACT_ID_NOT_FOUND_MSG);
				keepProcessing = false;
			} catch (SystemException se) {
				recordSystemError(iKitFundInfo, se,
						IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_CODE,
						IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_MSG);
				logger
						.error(IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_MSG);
				keepProcessing = false;
			}
		}

		if (keepProcessing) {
			// Validate the Contract Details
			try {
				contractProfileVO = ContractServiceDelegate.getInstance()
						.getContractProfileDetails(
					Integer.valueOf(contractNumber),contract.getCompanyCode() );
		}
			catch(SystemException se){
				recordSystemError(iKitFundInfo, se,
						IKitErrorCodes.CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_CODE,
						IKitErrorCodes.CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_MSG);
				logger
						.error(IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_MSG);
				keepProcessing = false;
			}
		}
		if (keepProcessing) {
			keepProcessing = validateContractDetails(contractAccessCode, contractProfileVO, 
						iKitFundInfo, contract);
		}
		if (keepProcessing) {
			try {
				String companyCode = contract.getCompanyCode();

				// Set the location..
				if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equals(contract.getCompanyCode())) {
					ApplicationHelper.setRequestContentLocation(request, Location.NEW_YORK);
				}

				// Get Funds Data sorted by Asset Class.
				FundsAndPerformance fundsAndPerformance = getFundsAndPerformance(
						contractNumber, companyCode);
				
				if (fundsAndPerformance != null) {
					// Set the F&P report as of date.
					iKitFundInfo.setReportAsOfDate(getReportAsOfDate());

					// Set the as of dates map.
					iKitFundInfo.setAsOfDates(fundsAndPerformance.getAsOfDates());

					// Set the sorted Fund Footnotes.
					iKitFundInfo.setSortedFootnotes(getFundFootnoteSymbolsArray(
							fundsAndPerformance, companyCode));

					// Retrieve the fund Details, Market Index Fund Details, and
					// Funds sorted by Asset Class.
					retrieveFundDetails(contractId, fundsAndPerformance, iKitFundInfo);

				}

				// Get Fund ID's sorted by Investment Category.
				Map<String, List<String>> fundIdsForRiskReturn = getFundIdsForRiskReturn(
						contractNumber, companyCode);

				if (fundIdsForRiskReturn != null) {
					List<AssetClassOrRiskReturn> fundsByInvCategories = new ArrayList<AssetClassOrRiskReturn>();

					Set<Entry<String, List<String>>> fundIdsForRiskReturnEntrySet = fundIdsForRiskReturn
							.entrySet();

					for (Entry<String, List<String>> fundIdsForRiskReturnEntry : fundIdsForRiskReturnEntrySet) {
						AssetClassOrRiskReturn fundsByInvCategory = new AssetClassOrRiskReturn(
								getInvCategoryId(fundIdsForRiskReturnEntry.getKey()), 
										fundIdsForRiskReturnEntry.getKey(), 
										fundIdsForRiskReturnEntry.getValue());

						fundsByInvCategories.add(fundsByInvCategory);
					}

					iKitFundInfo.setFundsByInvestmentCategories(fundsByInvCategories);
				}
				
				iKitFundInfo.setFeeWaiverFunds(FundServiceDelegate.getInstance().getFundFeeWaiverIndicator());
				
				// Get Footer, footnote, disclaimer..
				LayoutPage bean = getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH, request);			
		        String pageFooter = ContentUtility.getPageFooter(bean, new String[] {});
		        String pageFootnotes = ContentUtility.getPageFootnotes(bean, new String[] {}, -1);
		        String pageDisclaimer = ContentUtility.getPageDisclaimer(bean, new String[] {}, -1);
		        
				String feeWaiverDisclaimer = ContentHelper.getContentText(
						BDContentConstants.FEE_WAIVER_DISCLOSURE_TEXT,
						ContentTypeManager.instance().DISCLAIMER, null);

				pageFooter = GetIKitFundInfoUtils.removeXMLInvalidCharacters(pageFooter);
				pageFootnotes = GetIKitFundInfoUtils.removeXMLInvalidCharacters(pageFootnotes);
				pageDisclaimer = GetIKitFundInfoUtils.removeXMLInvalidCharacters(pageDisclaimer);
				feeWaiverDisclaimer = GetIKitFundInfoUtils.removeXMLInvalidCharacters(feeWaiverDisclaimer);

				iKitFundInfo.setPageFooter(pageFooter);
				iKitFundInfo.setPageFootnotes(pageFootnotes);
				iKitFundInfo.setPageDisclaimer(pageDisclaimer);
				iKitFundInfo.setFeeWaiverDisclaimer(feeWaiverDisclaimer);

			} catch (SystemException se) {
				String errorCode = se.getMessage();
				if (StringUtils.isBlank(errorCode)) {
					errorCode = IKitErrorCodes.UNKNOWN_SYSTEM_EXCEPTION_CODE;
				}

				recordSystemError(iKitFundInfo, se, errorCode, GetIKitFundInfoUtils
						.getErrorMessage(errorCode));
				logger.error(GetIKitFundInfoUtils.getErrorMessage(errorCode));
				keepProcessing = false;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchFundInformation() method -> end");
		}
		
		return iKitFundInfo;

	}

	/**
	 * Get the Report as of date.
	 * 
	 * @return - report as of date.
	 * @throws SystemException
	 */
	private Date getReportAsOfDate() throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getReportAsOfDate() method -> start");
		}

		Date reportAsOfDate = null;
		try {
			reportAsOfDate = FundServiceDelegate.getInstance().getReportAsOfDate();
		} catch (SystemException se) {
			logger.error(IKitErrorCodes.FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_CODE);
			throw new SystemException(se,
					IKitErrorCodes.FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_CODE);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getReportAsOfDate() method -> end");
		}

		return reportAsOfDate;
	}


    /**
     * This method gets layout page for the given layout id.
     * 
     * @param id
     * @param request
     * @return LayoutPage
     */
    protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getLayoutPage() method -> start");
		}

        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(id, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getLayoutPage() method -> end");
		}

        return layoutPageBean;
    }

	/**
	 * This method returns the FundsAndPerformance object containing all the
	 * Fund Information based on the filter criteria. In this case, we are
	 * creating a filter criteria to fetch Contract-Selected Funds only for a
	 * particular contract number, grouped by Asset Class.
	 * 
	 * @param contractNumber
	 *            - contract number for which we need to get the contract
	 *            selected funds
	 * @param companyCode
	 * @return - FundsAndPerformance object containing all the Fund Information
	 * @throws SystemException
	 */
	private FundsAndPerformance getFundsAndPerformance(String contractNumber,
			String companyCode) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundsAndPerformance() method -> start");
		}
		
		FundsAndPerformance fundsAndPerformance = null;
		
		FandpFilterCriteria filterCriteria = new FandpFilterCriteria(
				FandpFilterCriteria.CONTRACT_FUNDS,
				FandpFilterCriteria.ASSET_CLASS_FILTER_KEY,
				IKitConstants.DEFAULT_CLASS_SELECTED, Integer.valueOf(contractNumber),
				companyCode);
		filterCriteria.setLoadContractSelectedFundsOnly(true);

		try {
			fundsAndPerformance = FundServiceDelegate.getInstance()
					.getFundsAndPerformance(filterCriteria);
		} catch (SystemException se) {
			logger.error(IKitErrorCodes.FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_CODE);
			throw new SystemException(se,
					IKitErrorCodes.FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_CODE);
		}
		
		// Do the Default sorting on Funds.
		if (fundsAndPerformance != null) {
			sortFunds(fundsAndPerformance.getPerformanceAndFees());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundsAndPerformance() method -> end");
		}
		
		return fundsAndPerformance;
	}

	/**
	 * This method returns the FundsAndPerformance object containing all the
	 * Fund Information based on the filter criteria. In this case, we are
	 * creating a filter criteria to fetch Contract-Selected Funds only for a
	 * particular contract number, grouped by Investment Category.
	 * 
	 * @param contractNumber
	 *            - contract number for which we need to get the contract
	 *            selected funds
	 * @param companyCode
	 * @return - FundsAndPerformance object containing all the Fund Information
	 * @throws SystemException
	 */
	private Map<String, List<String>> getFundIdsForRiskReturn(
			String contractNumber, String companyCode) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundIdsForRiskReturn() method -> start");
		}
		
		FandpFilterCriteria filterCriteria = new FandpFilterCriteria(
				FandpFilterCriteria.CONTRACT_FUNDS,
				FandpFilterCriteria.RISK_CATEGORY_FILTER_KEY,
				IKitConstants.DEFAULT_CLASS_SELECTED, Integer.valueOf(contractNumber),
				companyCode);
		filterCriteria.setLoadContractSelectedFundsOnly(true);

		FundsAndPerformance fundsAndPerformance = null;
		try {
			fundsAndPerformance = FundServiceDelegate
					.getInstance().getFundsAndPerformance(filterCriteria);
		} catch (SystemException se) {
			logger.error(IKitErrorCodes.FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_CODE);
			throw new SystemException(se,
					IKitErrorCodes.FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_CODE);
		}

		Map<String, List<String>> fundIdsForRiskReturnMap = new LinkedHashMap<String, List<String>>();

		if (fundsAndPerformance != null) {
			// Do the Default sorting on Funds.
			sortFunds(fundsAndPerformance.getPerformanceAndFees());

			Map<String, List<PerformanceAndFees>> performanceAndFeesMap = fundsAndPerformance
					.getPerformanceAndFees();
			
			if (performanceAndFeesMap != null) {
				for (Entry<String, List<PerformanceAndFees>> perfAndFeesEntry : performanceAndFeesMap.entrySet()) {
					String invCategoryName = perfAndFeesEntry.getKey();
					List<String> fundIds = new ArrayList<String>();
					for (PerformanceAndFees perfAndFees : perfAndFeesEntry
							.getValue()) {
						fundIds.add(perfAndFees.getFundId());
					}
					fundIdsForRiskReturnMap.put(invCategoryName, fundIds);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundIdsForRiskReturn() method -> end");
		}
		
		return fundIdsForRiskReturnMap;
	}

	/**
	 * This method returns a sorted footnotes symbols array.
	 * 
	 * @param fundsAndPerformance
	 * @param companyCode
	 * @return - sorted footnotes symbols array.
	 */
	private List<Footnote> getFundFootnoteSymbolsArray(
			FundsAndPerformance fundsAndPerformance, String companyCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundFootnoteSymbolsArray() method -> start");
		}

		String[] fundFootnotesSymbolsArray = getFootNotesSymbols(fundsAndPerformance);

		Footnote[] sortedSymbolsArray = FootnoteCacheImpl.getInstance()
				.sortFootnotes(fundFootnotesSymbolsArray, companyCode);
		
		ArrayList<Footnote> sortedFootnotesList = new ArrayList<Footnote>();
		if (sortedSymbolsArray != null) {
			for (Footnote footnote : sortedSymbolsArray) {
				String footnoteText = footnote.getText();
				// Remove the XML invalid characters from the footnote text.
				footnoteText = GetIKitFundInfoUtils.removeXMLInvalidCharacters(footnoteText);
				footnote.setText(footnoteText);
				sortedFootnotesList.add(footnote);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundFootnoteSymbolsArray() method -> end");
		}

		return sortedFootnotesList;

	}

	/**
	 * Returns a footNotes symbol array
	 * 
	 * @param fundsAndPerformance
	 * @return
	 */
	private String[] getFootNotesSymbols(FundsAndPerformance fundsAndPerformance) {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFootNotesSymbols() method -> start");
		}

		String allSymbols = new String();

		for (String symbol : fundsAndPerformance.getFundFootNotes()) {
			if (StringUtils.isNotBlank(allSymbols)) {
				allSymbols += IKitConstants.COMMA_SYM;
			}
			allSymbols += symbol;
		}

		String[] allSymbolsArray = StringUtils.split(allSymbols, ',');

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFootNotesSymbols() method -> end");
		}

		return allSymbolsArray;
	}

	/**
	 * This method retrieves a list of Fund Id's.
	 * 
	 * @param perfAndFeesList
	 * @return - a list of Fund Id's.
	 */
	private List<String> getFundIds(List<PerformanceAndFees> perfAndFeesList) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("GetFundInformationAction.getFundIds() method -> start");
		}

		List<String> fundIdsList = new ArrayList<String>();

		if (perfAndFeesList != null) {
			for (PerformanceAndFees perfAndFee : perfAndFeesList) {
				fundIdsList.add(perfAndFee.getFundId());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getFundIds() method -> end");
		}

		return fundIdsList;
	}

	/**
	 * Does the sorting on the Tab's value object
	 * 
	 * @param currentTabObject
	 */
	@SuppressWarnings("unchecked")
	protected void sortFunds(Map<String, List<PerformanceAndFees>> currentTabObject) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("GetFundInformationAction.sortFunds() method -> start");
		}

		if (currentTabObject == null) {
			return;
		}

		BeanComparator comparator = new BeanComparator(IKitConstants.SORT_NUMBER);

		Set<String> keySet = currentTabObject.keySet();
		Iterator<String> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			Collections.sort(currentTabObject.get(iterator.next()), comparator);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.sortFunds() method -> end");
		}
	}

	/**
	 * This method retrieves all the Fund Information and populates the
	 * ReturnsAndFeesVO object specific to each fund.
	 * 
	 * @param contractNumber
	 * @param fundsAndPerformance
	 * @param fundInfo
	 * @throws SystemException
	 */
	private void retrieveFundDetails(Integer contractNumber,
			FundsAndPerformance fundsAndPerformance, IKitFundInfo fundInfo)
			throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.retrieveFundDetails() method -> start");
		}
		
		List<AssetClassOrRiskReturn> fundsByAssetClasses = new ArrayList<AssetClassOrRiskReturn>();
		Map<String, ReturnsAndFeesVO> fundDetailsMap = new LinkedHashMap<String, ReturnsAndFeesVO>();
		Map<String, ReturnsAndFeesVO> marketIndexFundDetailsMap = new LinkedHashMap<String, ReturnsAndFeesVO>();
		List<PerformanceAndFees> guaranteedAccountFundDetailsList = new ArrayList<PerformanceAndFees>();

		// Retrieve the Fund details from PerformanceAndFeesVO and set it in ReturnsAndFeesVO.
		// Also, retrieve the Guaranteed Account Funds.
		fetchPerformanceAndFees(fundsAndPerformance, fundDetailsMap,
				marketIndexFundDetailsMap, guaranteedAccountFundDetailsList,
				fundsByAssetClasses);
		
		// Retrieve the ror_1month, ror_3month, ror_ytd fund values from PricesAndYTD VO
		// and set it in ReturnsAndFeesVO.
		fetchRorInfoFromPricesAndYtd(fundsAndPerformance, fundDetailsMap, marketIndexFundDetailsMap);
		
		// Retrieve the morningstar category fund information from Morningstar VO
		// and set it in ReturnsAndFeesVO.
		fetchMorningStarInfo(fundsAndPerformance, fundDetailsMap, marketIndexFundDetailsMap);
		
		// Fetch the Guaranteed Account Funds Information.
		GARateVO gaRateVO = fetchGuaranteedAccountFunds(contractNumber, guaranteedAccountFundDetailsList);
		
	    fetchStandardDeviationValues(fundsAndPerformance, fundDetailsMap, marketIndexFundDetailsMap);
		
		fundInfo.setFundsByAssetClasses(fundsByAssetClasses);
		fundInfo.setFundDetailsList(new ArrayList<ReturnsAndFeesVO>(fundDetailsMap.values()));
		fundInfo.setMarketIndexFundDetailsList(new ArrayList<ReturnsAndFeesVO>(marketIndexFundDetailsMap.values()));
		fundInfo.setGaRateVO(gaRateVO);
		
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.retrieveFundDetails() method -> end");
		}
		
	}
	
	private void fetchStandardDeviationValues(
			FundsAndPerformance fundsAndPerformance,
			Map<String, ReturnsAndFeesVO> fundDetailsMap,
			Map<String, ReturnsAndFeesVO> marketIndexFundDetailsMap) {


		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchStandardDeviationValues() method -> start");
		}
		
		Map<String, List<StandardDeviation>> standardDeviationMap = fundsAndPerformance.getStandardDeviation();
		if (standardDeviationMap != null) {
			Collection<List<StandardDeviation>> standardDeviationListOfList = standardDeviationMap.values();

			for (List<StandardDeviation> standardDeviationList : standardDeviationListOfList) {
				for (StandardDeviation standardDeviation : standardDeviationList) {
					if (standardDeviation != null) {
						String fundId = standardDeviation.getFundId();
						if (fundId != null) {
							if (standardDeviation.isMarketIndexFund()) {
								ReturnsAndFeesVO returnsAndFeesVO = marketIndexFundDetailsMap.get(fundId);
								if (returnsAndFeesVO != null) {
									returnsAndFeesVO.setThreeYearSDAsOfQuaterEnd(standardDeviation.getThreeYearSDAsOfQuaterEnd());
									returnsAndFeesVO.setFiveYearSDAsOfQuaterEnd(standardDeviation.getFiveYearSDAsOfQuaterEnd());
									returnsAndFeesVO.setTenYearSDAsOfQuaterEnd(standardDeviation.getTenYearSDAsOfQuaterEnd());
									
									marketIndexFundDetailsMap.put(fundId, returnsAndFeesVO);
								}
							} else {
								ReturnsAndFeesVO returnsAndFeesVO = fundDetailsMap.get(fundId);
								if (returnsAndFeesVO != null) {
									returnsAndFeesVO.setThreeYearSDAsOfQuaterEnd(standardDeviation.getThreeYearSDAsOfQuaterEnd());
									returnsAndFeesVO.setFiveYearSDAsOfQuaterEnd(standardDeviation.getFiveYearSDAsOfQuaterEnd());
									returnsAndFeesVO.setTenYearSDAsOfQuaterEnd(standardDeviation.getTenYearSDAsOfQuaterEnd());
									
									fundDetailsMap.put(fundId, returnsAndFeesVO);
								}
							}
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchStandardDeviationValues() method -> end");
		}
	
	}

	/**
	 * This method goes thru the list of PerformanceAndFees object and populates
	 * the RiskAndReturnVO with PerformanceAndFees object. For each asset class,
	 * it creates the AssetClassOrRiskReturn VO.
	 * 
	 * @param fundsAndPerformance
	 * @param fundDetailsMap
	 * @param marketIndexFundDetailsMap
	 * @param guaranteedAccountFundDetailsList
	 * @param fundsByAssetClasses
	 * @throws SystemException
	 */
	private void fetchPerformanceAndFees(
			FundsAndPerformance fundsAndPerformance,
			Map<String, ReturnsAndFeesVO> fundDetailsMap,
			Map<String, ReturnsAndFeesVO> marketIndexFundDetailsMap,
			List<PerformanceAndFees> guaranteedAccountFundDetailsList,
			List<AssetClassOrRiskReturn> fundsByAssetClasses)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchPerformanceAndFees() method -> start");
		}
		
		Map<String, List<PerformanceAndFees>> performanceAndFees = fundsAndPerformance.getPerformanceAndFees();
		// Map<String, List<PerformanceAndFees>> - key = asset class id

		if (performanceAndFees != null) {
			Set<Entry<String, List<PerformanceAndFees>>> performanceAndFeesEntrySet = performanceAndFees.entrySet();

			for (Entry<String, List<PerformanceAndFees>> perfAndFeeEntry : performanceAndFeesEntrySet) {
				// 1. Add the List of Performance And Fees into a global list.
				List<PerformanceAndFees> perfAndFeesList = perfAndFeeEntry.getValue();
				for (PerformanceAndFees perfAndFeesVO : perfAndFeesList) {
					ReturnsAndFeesVO returnsAndFeesVO = new ReturnsAndFeesVO(perfAndFeesVO);
					if (perfAndFeesVO.isMarketIndexFund()) {
						if (perfAndFeesVO.getFundId() != null) {
							marketIndexFundDetailsMap.put(perfAndFeesVO.getFundId(), returnsAndFeesVO);
						}
					} else if (perfAndFeesVO.isGuaranteedFund()) {
						guaranteedAccountFundDetailsList.add(perfAndFeesVO);
					} else {
						if (perfAndFeesVO.getFundId() != null) {
							fundDetailsMap.put(perfAndFeesVO.getFundId(),returnsAndFeesVO);
						}
					}
				}

				if (IKitConstants.GUARANTEED_ACCOUNT.equals(perfAndFeeEntry.getKey())) {
					// Don't add the AssetClassOrRiskReturn object for
					// Guaranteed Account.
				} else {
					// 2. Create AssetClassOrRiskReturn object.
					AssetClassOrRiskReturn fundsByAssetClass = new AssetClassOrRiskReturn(
							getAssetClassId(perfAndFeeEntry.getKey()),
						perfAndFeeEntry.getKey(), getFundIds(perfAndFeesList));

					fundsByAssetClasses.add(fundsByAssetClass);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchPerformanceAndFees() method -> end");
		}
		
	}
	
	/**
	 * This method goes thru the list of PricesAndYTD object and populates the
	 * RiskAndReturnVO with ror details from PricesAndYTD object.
	 * 
	 * @param fundsAndPerformance
	 * @param fundDetailsMap
	 * @param marketIndexFundDetailsMap
	 */
	private void fetchRorInfoFromPricesAndYtd(
			FundsAndPerformance fundsAndPerformance,
			Map<String, ReturnsAndFeesVO> fundDetailsMap,
			Map<String, ReturnsAndFeesVO> marketIndexFundDetailsMap) {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchRorInfoFromPricesAndYtd() method -> start");
		}
		
		Map<String, List<PricesAndYTD>> pricesAndYtdMap = fundsAndPerformance.getPricesAndYTD();
		if (pricesAndYtdMap != null) {
			Collection<List<PricesAndYTD>>pricesAndYtdListOfList = pricesAndYtdMap.values();

			for (List<PricesAndYTD> pricesAndYtdList : pricesAndYtdListOfList) {
				for (PricesAndYTD pricesAndYtd : pricesAndYtdList) {
					if (pricesAndYtd != null) {
						String fundId = pricesAndYtd.getFundId();
						if (fundId != null) {
							if (pricesAndYtd.isMarketIndexFund()) {
								ReturnsAndFeesVO returnsAndFeesVO = marketIndexFundDetailsMap.get(fundId);
								if (returnsAndFeesVO != null) {
									returnsAndFeesVO.setOneMonthRORAsOfMonthEnd(pricesAndYtd.getOneMonthRORAsOfMonthEnd());
									returnsAndFeesVO.setThreeMonthRORAsOfMonthEnd(pricesAndYtd.getThreeMonthRORAsOfMonthEnd());
									returnsAndFeesVO.setYtdRORAsOfMonthEnd(pricesAndYtd.getYtdRORAsOfMonthEnd());
									
									marketIndexFundDetailsMap.put(fundId, returnsAndFeesVO);
								}
							} else {
								ReturnsAndFeesVO returnsAndFeesVO = fundDetailsMap.get(fundId);
								if (returnsAndFeesVO != null) {
									returnsAndFeesVO.setOneMonthRORAsOfMonthEnd(pricesAndYtd.getOneMonthRORAsOfMonthEnd());
									returnsAndFeesVO.setThreeMonthRORAsOfMonthEnd(pricesAndYtd.getThreeMonthRORAsOfMonthEnd());
									returnsAndFeesVO.setYtdRORAsOfMonthEnd(pricesAndYtd.getYtdRORAsOfMonthEnd());
									
									fundDetailsMap.put(fundId, returnsAndFeesVO);
								}
							}
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchRorInfoFromPricesAndYtd() method -> end");
		}
	}
	
	/**
	 * This method goes thru the list of Morningstar object and populates the
	 * RiskAndReturnVO with morningstar category from Morningstar object.
	 * 
	 * @param fundsAndPerformance
	 * @param fundDetailsMap
	 * @param marketIndexFundDetailsMap
	 */
	private void fetchMorningStarInfo(FundsAndPerformance fundsAndPerformance,
			Map<String, ReturnsAndFeesVO> fundDetailsMap,
			Map<String, ReturnsAndFeesVO> marketIndexFundDetailsMap) {

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchMorningStarInfo() method -> start");
		}
		
		Map<String, List<Morningstar>> morningstarMap = fundsAndPerformance.getMorningstar();
		if (morningstarMap != null) {
			Collection<List<Morningstar>> morningstarListOfList = morningstarMap.values();
			for (List<Morningstar> morningstarList : morningstarListOfList) {
				for (Morningstar morningstar : morningstarList) {
					String fundId = morningstar.getFundId();
					if (fundId != null) {
						if (morningstar.isMarketIndexFund()) {
							ReturnsAndFeesVO returnsAndFeesVO = marketIndexFundDetailsMap.get(fundId);
							if (returnsAndFeesVO != null) {
								returnsAndFeesVO.setMorningstarCategory(morningstar.getCategory());
							}
							marketIndexFundDetailsMap.put(fundId, returnsAndFeesVO);
						} else {
							ReturnsAndFeesVO returnsAndFeesVO = fundDetailsMap.get(fundId);
							if (returnsAndFeesVO != null) {
								returnsAndFeesVO.setMorningstarCategory(morningstar.getCategory());
							}
							fundDetailsMap.put(fundId, returnsAndFeesVO);
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchMorningStarInfo() method -> end");
		}
	}

	/**
	 * This method fetches the Guaranteed Account Fund details for
	 * contract-selected guaranteed account funds.
	 * 
	 * @param contractNumber
	 * @param guaranteedAccountFundDetailsList
	 * @return - Guaranteed Account Fund details for contract-selected
	 *         guaranteed account funds.
	 * @throws SystemException
	 */
	private GARateVO fetchGuaranteedAccountFunds(Integer contractNumber,
			List<PerformanceAndFees> guaranteedAccountFundDetailsList)
			throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchGuaranteedAccountFunds() method -> start");
		}
		
		GARateVO gaRateVO = null;
		if (guaranteedAccountFundDetailsList != null
				&& !guaranteedAccountFundDetailsList.isEmpty()) {
			List<GARates> selectedGaRatesList = getSelectedGuaranteedFunds(contractNumber);

			Date currentEffectiveDate = null;
			Date previousEffectiveDate = null;

			if (selectedGaRatesList != null && !selectedGaRatesList.isEmpty()) {
				for (GARates gaRates : selectedGaRatesList) {
					currentEffectiveDate = gaRates.getCurrenteffectivedate();
					previousEffectiveDate = gaRates.getPreviouseffectivedate();
					break;
				}
			}

			String[] footnoteSymbolArray = null;
			if (guaranteedAccountFundDetailsList.get(0) != null) {
				String footnoteSymbols = guaranteedAccountFundDetailsList
						.get(0).getFootNoteSymbols();
				if (!StringUtils.isBlank(footnoteSymbols)) {
					footnoteSymbolArray = StringUtils.split(footnoteSymbols,
							IKitConstants.COMMA_SYM);
				}

			}

			SimpleDateFormat DATE_FORMAT_ONLY_MONTH = new SimpleDateFormat(
					"MMMM");

			gaRateVO = new GARateVO();
			gaRateVO.setCurrentEffectiveDate(currentEffectiveDate);
			gaRateVO.setPreviousEffectiveDate(previousEffectiveDate);

			String currentFundName = IKitConstants.GA_STRING + "("
					+ DATE_FORMAT_ONLY_MONTH.format(currentEffectiveDate) + ")";
			String previousFundName = IKitConstants.GA_STRING + "("
					+ DATE_FORMAT_ONLY_MONTH.format(previousEffectiveDate)
					+ ")";
			gaRateVO.setCurrentMonthEndName(currentFundName);
			gaRateVO.setPreviousMonthEndName(previousFundName);

			gaRateVO.setGaRatesList(selectedGaRatesList);
			if (footnoteSymbolArray != null) {
				gaRateVO.setCurrentMonthEndFootnoteSymbols(Arrays
						.asList(footnoteSymbolArray));
				gaRateVO.setPreviousMonthEndFootnoteSymbols(Arrays
						.asList(footnoteSymbolArray));
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.fetchGuaranteedAccountFunds() method -> end");
		}
		
		return gaRateVO;
	}

	/**
	 * This method returns the asset class ID, when the asset class Name is
	 * given as input.
	 * 
	 * @param assetClassName - asset class Name
	 * @return - asset class ID
	 * @throws SystemException
	 */
	private String getAssetClassId(String assetClassName) throws SystemException  {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getAssetClassId() method -> start");
		}

		String assetClassId = null;

		if (assetClassName != null) {
			List<AssetClassVO> assetClassesList = null;
			try {
				assetClassesList = FundServiceDelegate.getInstance().getAssetClassesList();
			} catch (SystemException se) {
				logger.error(IKitErrorCodes.ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_CODE);
				throw new SystemException(se,
						IKitErrorCodes.ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_CODE);
			}
			if (assetClassesList != null) {
				for (AssetClassVO assetClassVO : assetClassesList) {
					if (assetClassName.equals(assetClassVO.getAssetClassDesc())) {
						assetClassId = assetClassVO.getAssetClass();
						break;
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getAssetClassId() method -> end");
		}

		return assetClassId;
	}

	/**
	 * This method returns the investment category ID, when the investment category Name is
	 * given as input.
	 * 
	 * @param invCategoryName - investment category Name
	 * @return - investment category ID
	 * @throws SystemException
	 */
	private String getInvCategoryId(String invCategoryName) throws SystemException  {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getInvCategoryId() method -> start");
		}
		String invCategoryId = null;

		if (invCategoryName != null) {
			RiskCategoryVO[] invCategoriesList = null;
			try {
				invCategoriesList = FundServiceDelegate.getInstance().getInvestmentCategoriesList();
			} catch (SystemException se) {
				logger.error(IKitErrorCodes.INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_CODE);
				throw new SystemException(se,
						IKitErrorCodes.INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_CODE);
			}
			if (invCategoriesList != null) {
				for (RiskCategoryVO invCategoryVO : invCategoriesList) {
					if (invCategoryName.equals(invCategoryVO.getRiskCategoryName())) {
						invCategoryId = invCategoryVO.getOrder();
						break;
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getInvCategoryId() method -> end");
		}
		
		return invCategoryId;
	}

	/**
	 * This method returns the Guaranteed Account Funds information for those
	 * funds that were selected by contract.
	 * 
	 * @param contractNumber
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	private List<GARates> getSelectedGuaranteedFunds(Integer contractNumber) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getSelectedGuaranteedFunds() method -> start");
		}
		
		Hashtable<String, ContractFund> contractFundsMap = null;
		try {
			contractFundsMap = FundServiceDelegate.getInstance()
					.getContractFunds(contractNumber);
			// Key is the Fund ID.
		} catch (SystemException se) {
			logger.error(IKitErrorCodes.CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_CODE);
			throw new SystemException(se,
					IKitErrorCodes.CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_CODE);
		}
		
		List<GARates> gaRatesList = new ArrayList<GARates>();

		if (contractFundsMap != null) {
			Map<String, GARates> gaRatesMap = null;
			try {
				gaRatesMap = FundServiceDelegate.getInstance()
						.getGARatesForContract(String.valueOf(contractNumber));
				// Key is the Fund ID.
			} catch (SystemException se) {
				logger.error(IKitErrorCodes.CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_CODE);
				throw new SystemException(se,
						IKitErrorCodes.CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_CODE);
			}

			if (gaRatesMap != null) {
				for (Entry<String, GARates> gaRatesMapEntry : gaRatesMap.entrySet()) {
					String fundId = gaRatesMapEntry.getKey(); // 3YC / 5YC / 10YC
					if (contractFundsMap.containsKey(fundId) && contractFundsMap.get(fundId).isSelected()) {
						gaRatesList.add(gaRatesMapEntry.getValue());
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetFundInformationAction.getSelectedGuaranteedFunds() method -> end");
		}
		/*
		 * The Fund id will be coming from DB as 5YC, 3YC and 10YC.
		 * But while displaying we need to display as 3YC, 5YC and 10YC.
		 * So we are sorting it manually. Used anonymous class.
		 */
		Collections.sort(gaRatesList, new Comparator () {
				public int compare(Object object1, Object object2) {
					if (object1 instanceof GARates && object2 instanceof GARates) {
						GARates gaRates1 = (GARates) object1;
						GARates gaRates2 = (GARates) object2;
						if (gaRates1.getFundid() != null && gaRates2.getFundid() != null) {
							Integer fundIdOrder1 = GetIKitFundInfoUtils.getGuarateedAccountFundsOrder(gaRates1.getFundid());
							Integer fundIdOrder2 = GetIKitFundInfoUtils.getGuarateedAccountFundsOrder(gaRates2.getFundid());
							return fundIdOrder1.compareTo(fundIdOrder2);
						}
					}
					return 0;
				}
			} // anonymous class end here
		); // method closed here.
		return gaRatesList;
	}

	/**
	 * This method is a helper method to record the System Exceptions.
	 * 
	 * @param iKitFundInfoVO
	 * @param e
	 * @param errorCode
	 * @param errorMsg
	 */
	private void recordSystemError(IKitFundInfo iKitFundInfoVO, Exception e,
			String errorCode, String errorMsg) {
		String stackTrace = ExceptionUtils.getStackTrace(e).trim();
		iKitFundInfoVO.getErrors().put(errorCode,
						errorMsg + stackTrace.substring(0,
						stackTrace.length() <= IKitConstants.maxStackTraceBytesToReturn ? stackTrace.length()
							: IKitConstants.maxStackTraceBytesToReturn));
	}

	/**
	 * This method will validate the access code and contract status.
	 * @param contractAccessCode
	 * @param contractprofileVO
	 * @param fundInfo
	 * @param contract
	 * @return keepProcessing - return true if access code and contract status code is valid else return false
	 */
	private boolean validateContractDetails(String contractAccessCode, ContractProfileVO contractprofileVO,
			IKitFundInfo fundInfo, Contract contract){

		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.validateContractDetails() method -> start");
		}
		boolean keepProcessing = true; 
		
		if (contract.isDefinedBenefitContract()) {
			fundInfo.getErrors().put(
					IKitErrorCodes.DEFINED_BENEFIT_CONTRACT_NOT_SUPPORTED_CODE,
					IKitErrorCodes.DEFINED_BENEFIT_CONTRACT_NOT_SUPPORTED_MSG);
			keepProcessing = false;
			return keepProcessing;
		}
		
		if (StringUtils.isNotBlank(contractAccessCode)) {
			if (!contractAccessCode.equals(contractprofileVO.getContractAccessCode())) { 
				fundInfo.getErrors().put(
						IKitErrorCodes.CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_CODE,
						IKitErrorCodes.CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_MSG);
				keepProcessing = false;
			}

		} else {
			fundInfo.getErrors().put(
					IKitErrorCodes.CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_CODE,
					IKitErrorCodes.CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_MSG);
			keepProcessing = false;

		}
		// Check the contract status code. Proceed if the status is AC or CF. 
		if (!ContractStatus.ACTIVE.equals(contract.getStatus())
				&& !ContractStatus.FROZEN.equals(contract.getStatus())
				&& !ContractStatus.APPROVED.equals(contract.getStatus())) {
			fundInfo.getErrors().put(
					IKitErrorCodes.CONTRACT_STATUS_NOT_SUPPORTED_CODE,
					IKitErrorCodes.CONTRACT_STATUS_NOT_SUPPORTED_MSG + 
					contract.getStatus());
			keepProcessing = false;
		}

// 2010-03-05: i:enrollment edit removed at the request of the business
//		if (JdbcHelper.INDICATOR_NO.equals(getIEnrollmentAllowedInd(contract))) {
//			fundInfo.getErrors().put(
//					IKitErrorCodes.IENROLLMENT_NOT_ALLOWED_CODE,
//					IKitErrorCodes.IENROLLMENT_NOT_ALLOWED_MSG);
//			keepProcessing = false;
//		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.validateContractDetails() method -> end");
		}

		return keepProcessing;

	}
	/**
	 * This method will validate whether given contract has access for iEnrollment.
	 * @param contract
	 * @return String - contain the iEnrollment indicator 
	 */
	private String getIEnrollmentAllowedInd(Contract contract) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.getIEnrollmentAllowedInd() method -> start");
		}

		String iEnrollmentAllowed = "N";
		Map<String, ContractServiceFeature> csfMap  = contract.getServiceFeatureMap();

		ContractServiceFeature managingDeferralsCSF = csfMap.get(
				ServiceFeatureConstants.MANAGING_DEFERRALS);

		if (managingDeferralsCSF.getAttributeValue(
				ServiceFeatureConstants.MD_ENROLL_ONLINE) != null) {
			iEnrollmentAllowed = managingDeferralsCSF.getAttributeValue(
					ServiceFeatureConstants.MD_ENROLL_ONLINE).trim();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.getIEnrollmentAllowedInd() method -> end");
		}
		return iEnrollmentAllowed;

	}

	/**
	 * This method will add the error code and message.
	 * 
	 * @param error
	 * @param document
	 * @param errors
	 */
	private void addErrorElements(Map<String, String> error, PDFDocument document, Element errors){		

		Set<Entry<String, String>> keys = error.entrySet();
		if (keys != null) {
			for (Entry<String, String> errorDetails : keys) {
				Element err = document.createElement(IKitConstants.ERROR);
				document.appendElement(errors, err);
				document.appendTextNode(err, IKitConstants.CODE, errorDetails.getKey());
				document.appendTextNode(err, IKitConstants.MESSAGE,errorDetails.getValue());
			}
		}
	}

	/**
	 * This method add the as of date elements
	 * @param asOfDates
	 * @param document
	 * @param funds_asofdates
	 */
	private void addAsOfDatesElements(Map<String, Date> asOfDates,
			PDFDocument document, Element funds_asofdates) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addAsOfDatesElements() method -> start");
		}

		document.appendTextNode(funds_asofdates, IKitConstants.ASOFDATE_ROR,
				GetIKitFundInfoUtils.formatValue((Date) asOfDates
						.get(IKitConstants.GET_ROR)));
		document.appendTextNode(funds_asofdates, IKitConstants.ASOFDATE_ROR_QE,
				GetIKitFundInfoUtils.formatValue((Date) asOfDates
						.get(IKitConstants.GET_RORQE)));
		document.appendTextNode(funds_asofdates, IKitConstants.ASOFDATE_EXPENSE_RATIO,
				GetIKitFundInfoUtils
						.formatValue((Date) asOfDates
								.get(IKitConstants.GET_FER)));		
		document.appendTextNode(funds_asofdates, IKitConstants.ASOFDATE_STANDARD_DEVIATION_QE,
				GetIKitFundInfoUtils
						.formatValue((Date) asOfDates
								.get(IKitConstants.GET_STDDEV_QE)));		

		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addAsOfDatesElements() method -> end");
		}

	}

	/**
	 * This method will add the asset class elements
	 * @param fundsByAssetClasses
	 * @param document
	 * @param asset_classes
	 */
	private void addAssetClassElements(List<AssetClassOrRiskReturn> fundsByAssetClasses, 
			PDFDocument document, Element asset_classes) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addAssetClassElements() method -> start");
		}

		for (AssetClassOrRiskReturn assetClassOrRiskReturn : fundsByAssetClasses) {

			Element asset_class = document.createElement(IKitConstants.ASSET_CLASS);
			document.appendElement(asset_classes, asset_class);

			asset_class.setAttribute(IKitConstants.ID, GetIKitFundInfoUtils
					.formatValue(assetClassOrRiskReturn.getId()));
			asset_class.setAttribute(IKitConstants.NAME, GetIKitFundInfoUtils
					.formatValue(assetClassOrRiskReturn.getName()));

			Element funds = document.createElement(IKitConstants.FUNDS);
			document.appendElement(asset_class, funds);

			List<String> fundList = assetClassOrRiskReturn.getFundIds();

			if (fundList != null) {
				for (String fund : fundList) {
					if(!StringUtils.isBlank(fund)){
						document.appendTextNode(funds, IKitConstants.FUND, fund);
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addAssetClassElements() method -> end");
		}

	}

	/**
	 * This method will add element in Investment categories
	 * @param fundsByInvestmentCategories
	 * @param document
	 * @param investment_categories
	 */
	private void addInvestmentCategories(List<AssetClassOrRiskReturn> fundsByInvestmentCategories,
			PDFDocument document, Element investment_categories) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addInvestmentCategories() method -> start");
		}

		for (AssetClassOrRiskReturn assetClassOrRiskReturn : fundsByInvestmentCategories) {

			Element investment_category = document.createElement(IKitConstants.INVESTMENT_CATEGORY);
			document.appendElement(investment_categories, investment_category);

			investment_category.setAttribute(IKitConstants.ID,
					GetIKitFundInfoUtils.formatValue(assetClassOrRiskReturn
							.getId()));
			investment_category.setAttribute(IKitConstants.NAME,
					GetIKitFundInfoUtils.formatValue(assetClassOrRiskReturn
							.getName()));

			Element funds = document.createElement(IKitConstants.FUNDS);
			document.appendElement(investment_category, funds);

			List<String> fundList = assetClassOrRiskReturn.getFundIds();
			if (fundList != null) {
				for (String fund : fundList) {
					if(!StringUtils.isBlank(fund)){
						document.appendTextNode(funds, IKitConstants.FUND, fund);	
					}

				}
			}

		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addInvestmentCategories() method -> end");
		}
	}

	/**
	 * This method will add the attribute to the fund elements
	 * ( common to fund details and market index fund)
	 * @param fund
	 * @param performanceAndFees
	 */
	private void addAttributeForFund(Element fund, PerformanceAndFees performanceAndFees, 
			boolean isMarketIndexFund){	

		fund.setAttribute(IKitConstants.ID, GetIKitFundInfoUtils
				.formatValue(performanceAndFees.getFundId()));
		fund.setAttribute(IKitConstants.CLASS, GetIKitFundInfoUtils
				.formatValue(performanceAndFees.getFundClassShortName()));
		fund.setAttribute(IKitConstants.NAME, GetIKitFundInfoUtils.removeSupElement(
				GetIKitFundInfoUtils
						.formatValue(performanceAndFees.getFundName())));
		fund.setAttribute(IKitConstants.MANAGER_NAME, GetIKitFundInfoUtils
				.formatValue(performanceAndFees.getManagerName()));
		// set the inception date to "" for market index fund.
		if(isMarketIndexFund){
			fund.setAttribute(IKitConstants.INCEPTION_DATE, "");
		}
		else{
			fund.setAttribute(IKitConstants.INCEPTION_DATE, GetIKitFundInfoUtils
					.formatValue(performanceAndFees.getDateIntroduced()));	
		}		
			
		fund.setAttribute(IKitConstants.FUND_CODE, GetIKitFundInfoUtils
						.formatValue(performanceAndFees.getFundCode()));
		fund.setAttribute(IKitConstants.MORNINGSTAR_SECURITY_ID, GetIKitFundInfoUtils
						.formatValue(performanceAndFees.getMorningstarSecurityId()));
	}

	/**
	 * This will add element for ror. ( common to fund details and market index fund)
	 * @param performanceAndFees
	 * @param returnsAndFeesVO
	 * @param document
	 * @param ror
	 */
	private void addRORElements(PerformanceAndFees performanceAndFees,
			ReturnsAndFeesVO returnsAndFeesVO, PDFDocument document, Element ror){		

		FandpHypotheticalInfo  fandpHypotheticalInfo = performanceAndFees.getHypotheticalInfo();
		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror,
				IKitConstants.ROR_1MONTH,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isOneMonthRORAsOfMonthEnd(),
				returnsAndFeesVO.getOneMonthRORAsOfMonthEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror,
				IKitConstants.ROR_3MONTH,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isThreeMonthRORAsOfMonthEnd(),
				returnsAndFeesVO.getThreeMonthRORAsOfMonthEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror,
				IKitConstants.ROR_YTD,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isYtdRORAsOfMonthEnd(),
				returnsAndFeesVO.getYtdRORAsOfMonthEnd());
	}
	/**
	 * This will add the ROR quarter End elements
	 * @param performanceAndFees
	 * @param returnsAndFeesVO
	 * @param document
	 * @param ror_qe
	 */
	private void addRORQuarterEndElements(PerformanceAndFees performanceAndFees, 
			ReturnsAndFeesVO returnsAndFeesVO, PDFDocument document, Element ror_qe){		

		FandpHypotheticalInfo  fandpHypotheticalInfo = performanceAndFees.getHypotheticalInfo();
		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror_qe,
				IKitConstants.ROR_1YR_QE, 
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isRor1YrAsOfQuaterEnd(),
				performanceAndFees.getRor1YrAsOfQuaterEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror_qe,
				IKitConstants.ROR_3YR_QE,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isRor3YrAsOfQuaterEnd(),
				performanceAndFees.getRor3YrAsOfQuaterEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror_qe,
				IKitConstants.ROR_5YR_QE,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isRor5YrAsOfQuaterEnd(),
				performanceAndFees.getRor5YrAsOfQuaterEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror_qe,
				IKitConstants.ROR_10YR_QE,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isRor10YrAsOfQuaterEnd(),
				performanceAndFees.getRor10YrAsOfQuaterEnd());

		GetIKitFundInfoUtils.formatValueWithAttribute(document, ror_qe,
				IKitConstants.ROR_SINCE_INCEPTION_QE,
				fandpHypotheticalInfo == null? false : fandpHypotheticalInfo.isRorSinceInceptionQuaterEnd(),
				performanceAndFees.getRorSinceInceptionQuaterEnd());	
	}

	/**
	 * This will add the fund_expensees elements
	 * @param performanceAndFees
	 * @param document
	 * @param expense_ratio_values
	 */
	private void addExpensesRatioElements(PerformanceAndFees performanceAndFees,
			PDFDocument document, Element expense_ratio_values){

		document.appendTextNode(expense_ratio_values, IKitConstants.FUND_EXPENSE_RATIO,
				GetIKitFundInfoUtils
								.formatValue(performanceAndFees
										.getFerAsOfQuarterEnd()));
		document.appendTextNode(expense_ratio_values, IKitConstants.ANNUAL_MAINTENANCE_CHARGE,
				GetIKitFundInfoUtils.formatValue(performanceAndFees
								.getAmcAsOfQuarterEnd()));
		document.appendTextNode(expense_ratio_values, IKitConstants.SALES_AND_SERVICE_FEE,
				performanceAndFees.getSalesAndServiceFeeAsOfQuarterEnd());
		document.appendTextNode(expense_ratio_values, IKitConstants.EXPENSE_RATIO,
				GetIKitFundInfoUtils
						.formatValue(performanceAndFees
								.getExpenseRatioAsOfQuarterEnd()));			
	}

	/**
	 * This method will add morningstar_category element
	 * (this is common method for fund_deatils and Market index fund elements)
	 * @param returnsAndFeesVO
	 * @param document
	 * @param fund_metrics
	 */
	private void addMornigStarCategory(ReturnsAndFeesVO returnsAndFeesVO, PDFDocument document, 
			Element fund_metrics){		

		document.appendTextNode(fund_metrics, IKitConstants.MORNINGSTAR_CATEGORY,
				GetIKitFundInfoUtils
				.formatValue(returnsAndFeesVO.getMorningstarCategory()));

	}

	/**
	 * This method will add the fund details (this is common method for fund_deatils and Market index fund)
	 * @param fundDetails
	 * @param document
	 * @param map 
	 * @param fund_details
	 */
	private void addFundDetails(List<ReturnsAndFeesVO> fundDetails,
			PDFDocument document, Element funds, boolean isMarketIndexFund,
			List<String> feeWaiverFunds, Map<String, Date> asOfDates) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addFundDetails() method -> start");
		}

		for (ReturnsAndFeesVO returnsAndFeesVO : fundDetails) {

			if (returnsAndFeesVO != null) {

				PerformanceAndFees performanceAndFees =	returnsAndFeesVO.getPerformanceAndFees();

				if (performanceAndFees != null) {

					Element fund = document.createElement(IKitConstants.FUND);
					document.appendElement(funds, fund);

					// add the attribute for fund
					addAttributeForFund(fund, performanceAndFees, isMarketIndexFund);

					Element fund_metrics = document.createElement(IKitConstants.FUND_METRICS);
					document.appendElement(fund, fund_metrics);

					/* creating ROR elements */
					Element ror = document.createElement(IKitConstants.ROR);
					document.appendElement(fund_metrics, ror);

					addRORElements(performanceAndFees, returnsAndFeesVO, document, ror);	

					/* creating ROR QA elements */			
					Element ror_qe = document.createElement(IKitConstants.ROR_QE);
					document.appendElement(fund_metrics, ror_qe);	
					addRORQuarterEndElements(performanceAndFees, returnsAndFeesVO, document, ror_qe);	

					/* creating expense_ratio_values elements */

					Element expense_ratio_values = document.createElement(IKitConstants.EXPENSE_RATIO_VALUES);
					document.appendElement(fund_metrics, expense_ratio_values);

					addExpensesRatioElements(performanceAndFees, document, expense_ratio_values);

					addMornigStarCategory(returnsAndFeesVO, document, fund_metrics);
					
					/* creating Standard Deviation Quarter End elements */
					Element standardDeviationsQe = document.createElement(IKitConstants.STANDARD_DEIATIONS_QE);
					document.appendElement(fund_metrics, standardDeviationsQe);
					standardDeviationsQe.setAttribute(IKitConstants.AS_OF_DATE, GetIKitFundInfoUtils
							.formatValue((Date) asOfDates
									.get(IKitConstants.GET_STDDEV_QE)));
					addStandardDeviationsQuarterEndValues(returnsAndFeesVO, document, standardDeviationsQe);

					/* creating footnotes */
					Element footNote = document.createElement(IKitConstants.FOOTNOTES);
					document.appendElement(fund, footNote);

					String footNoteSymbol = performanceAndFees.getFootNoteSymbols();

					if (footNoteSymbol != null) {
						addFootNoteSymbol(footNoteSymbol, document, footNote);
					}
					
					if(feeWaiverFunds.contains(performanceAndFees.getFundId())) {
						addFootNoteSymbol(IKitConstants.FWI_FUND_FOOTNOTE_SYMBOL, document, footNote);
					}
					
					if (fund != null
							&& IKitConstants.MONEY_MARKET_FUND.equals(performanceAndFees.getFundFamilyCategoryCode())) {						
						document.appendTextNode(fund,
								IKitConstants.MONEYMARKET7DAYYIELD,
								performanceAndFees.getFundDisclosureText());
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addFundDetails() method -> end");
		}

	}

	private void addStandardDeviationsQuarterEndValues(
			ReturnsAndFeesVO returnsAndFeesVO, PDFDocument document,
			Element standardDeviationsQe) {
		
		Element standardDeviationQe = document.createElement(IKitConstants.STANDARD_DEIATION_QE);
		standardDeviationQe.setAttribute(IKitConstants.PERIOD, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR_3));
		standardDeviationQe.setAttribute(IKitConstants.PERIOD_TYPE, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR));
		standardDeviationQe.setAttribute(IKitConstants.VALUE, GetIKitFundInfoUtils
				.formatValue(returnsAndFeesVO.getThreeYearSDAsOfQuaterEnd()));
		document.appendElement(standardDeviationsQe, standardDeviationQe);

		standardDeviationQe = document.createElement(IKitConstants.STANDARD_DEIATION_QE);
		standardDeviationQe.setAttribute(IKitConstants.PERIOD, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR_5));
		standardDeviationQe.setAttribute(IKitConstants.PERIOD_TYPE, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR));
		standardDeviationQe.setAttribute(IKitConstants.VALUE, GetIKitFundInfoUtils
				.formatValue(returnsAndFeesVO.getFiveYearSDAsOfQuaterEnd()));
		document.appendElement(standardDeviationsQe, standardDeviationQe);
		
		standardDeviationQe = document.createElement(IKitConstants.STANDARD_DEIATION_QE);
		standardDeviationQe.setAttribute(IKitConstants.PERIOD, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR_10));
		standardDeviationQe.setAttribute(IKitConstants.PERIOD_TYPE, GetIKitFundInfoUtils
				.formatValue(IKitConstants.YEAR));
		standardDeviationQe.setAttribute(IKitConstants.VALUE, GetIKitFundInfoUtils
				.formatValue(returnsAndFeesVO.getTenYearSDAsOfQuaterEnd()));
		document.appendElement(standardDeviationsQe, standardDeviationQe);
	}

	/**
	 * This method receive the "," separated footnote symbols. it split the foot
	 * notes and add it in to XML
	 * 
	 * @param footNoteSymbol
	 * @param document
	 * @param footNote
	 */
	private void addFootNoteSymbol(String footNoteSymbol , PDFDocument document , Element footNote){		

		String[] individualFootNote = StringUtils.split(footNoteSymbol, ',');

		if (individualFootNote != null) {
			for (String message : individualFootNote) {
				document.appendTextNode(footNote, IKitConstants.FOOTNOTE_SYMBOL,
						message);
			}
		}
	}

	/**
	 * This method receive the footnote symbols in list object. each of the foot
	 * note symbol from the list is added in to the XML
	 * 
	 * @param footNoteSymbol
	 * @param document
	 * @param footNote
	 */
	private void addFootNoteSymbol(List<String> footNoteSymbol, PDFDocument document, Element footNote){		

		for (String individualFootNote : footNoteSymbol) {
			document.appendTextNode(footNote, IKitConstants.FOOTNOTE_SYMBOL,
					individualFootNote);
		}
	}

	/**
	 * This method will add the values for guaranteed_fund element for current month 
	 * @param gaRatesList
	 * @param document
	 * @param guaranteed_account_fund
	 * @param guaranteedAccountRateVo
	 */
	private void addGuaranteedCurrentFund(List<GARates> gaRatesList, PDFDocument document,
			Element guaranteed_account_fund, GARateVO guaranteedAccountRateVo ){
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addGuaranteedCurrentFund() method -> start");
		}

		for (GARates gaRates : gaRatesList) {

			Element guaranteed_fund = document.createElement(IKitConstants.GUARANTEED_FUND);
			document.appendElement(guaranteed_account_fund, guaranteed_fund);

			guaranteed_fund.setAttribute(IKitConstants.ID, GetIKitFundInfoUtils
					.formatValue(gaRates.getFundid()));
			guaranteed_fund.setAttribute(IKitConstants.INTEREST_RATE,
					GetIKitFundInfoUtils.formatValue(gaRates
							.getCurrentinterestrate()));
		}

		Element footNote = document.createElement(IKitConstants.FOOTNOTES);
		document.appendElement(guaranteed_account_fund, footNote);

		List<String> footNoteSymbol = guaranteedAccountRateVo.getCurrentMonthEndFootnoteSymbols();

		if (footNoteSymbol != null) {
			addFootNoteSymbol(footNoteSymbol, document, footNote);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addGuaranteedCurrentFund() method -> end");
		}

	}

	/**
	 * This method will add the values for guaranteed_fund element for previous month  
	 * @param gaRatesListPrevious
	 * @param document
	 * @param guaranteed_account_fund_previous
	 * @param guaranteedAccountRateVo
	 */
	private void addGuaranteedPreviousFund(List<GARates> gaRatesListPrevious, PDFDocument document,
			Element guaranteed_account_fund_previous, GARateVO guaranteedAccountRateVo ){
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addGuaranteedPreviousFund() method -> start");
		}

		for (GARates gaRates : gaRatesListPrevious) {

			Element guaranteed_fund = document.createElement(IKitConstants.GUARANTEED_FUND);
			document.appendElement(guaranteed_account_fund_previous, guaranteed_fund);

			guaranteed_fund.setAttribute(IKitConstants.ID, GetIKitFundInfoUtils
					.formatValue(gaRates.getFundid()));
			guaranteed_fund.setAttribute(IKitConstants.INTEREST_RATE,
					GetIKitFundInfoUtils.formatValue(gaRates
							.getPreviousinterestrate()));
		}

		Element footNotePrevious = document.createElement(IKitConstants.FOOTNOTES);
		document.appendElement(guaranteed_account_fund_previous, footNotePrevious);

		List<String> footNoteSymbolPrevious = guaranteedAccountRateVo.getPreviousMonthEndFootnoteSymbols(); // call the method to get the foot note symbol
		if (footNoteSymbolPrevious != null) {
			addFootNoteSymbol(footNoteSymbolPrevious, document, footNotePrevious);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.addGuaranteedPreviousFund() method -> end");
		}
	}

	/**
	 * This method will generate the XML document with valid data.
	 * @param fundInfo
	 * @return Document - it contain the values that need to be converted into XML format.
	 */
	private Document generateXMLDocument(IKitFundInfo fundInfo) {
		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.generateXMLDocument() method -> start");
		}

		PDFDocument document = null;	
		boolean isMarketIndexFund = false;
		try {

			document = new PDFDocument();

			// Creating the root elements
			Element rootElement = document.createRootElement(IKitConstants.FUND_INFO);

			Map<String, String> error = fundInfo.getErrors();

			// adding the attribute for root elements

			rootElement.setAttribute(IKitConstants.XML_GENERATED_TS, fundInfo.getCreatedTs() == null ? "":
				GetIKitFundInfoUtils.formatDate(IKitConstants.TIME_STAMP_FORMAT, fundInfo.getCreatedTs()));
			rootElement.setAttribute(IKitConstants.ERROR_COUNT, error == null ? IKitConstants.DEFAULT_ERROR_SIZE :
				new Integer(error.size()).toString());

			// create errors elements
			Element errors = document.createElement(IKitConstants.ERRORS);
			document.appendElement(rootElement, errors);

			// Add the error code and message if any error exist
			if(error != null && error.size() > 0){
				addErrorElements(error, document, errors);
			}
			else{ // If no error exist proceed to create the elements			

				// Create elements for report_asofdates
				document.appendTextNode(rootElement, IKitConstants.REPORT_ASOFDATE,
						GetIKitFundInfoUtils
								.formatValue(fundInfo.getReportAsOfDate()));

				// Create elements for funds_asofdates
				Element funds_asofdates = document.createElement(IKitConstants.FUNDS_ASOFDATES);
				document.appendElement(rootElement, funds_asofdates);

				Map<String, Date> asOfDates = fundInfo.getAsOfDates();
				if (asOfDates != null) {
					addAsOfDatesElements(asOfDates, document, funds_asofdates);
				}
				// funds_asofdates element ends here

				// Creating elements for funds by assets class
				Element funds_by_asset_class = document.createElement(IKitConstants.FUNDS_BY_ASSET_CLASS);
				document.appendElement(rootElement, funds_by_asset_class);

				Element asset_classes = document.createElement(IKitConstants.ASSET_CLASSES);
				document.appendElement(funds_by_asset_class, asset_classes);

				List<AssetClassOrRiskReturn> fundsByAssetClasses = fundInfo.getFundsByAssetClasses();

				if (fundsByAssetClasses != null) {
					addAssetClassElements(fundsByAssetClasses, document, asset_classes);
				}
				// funds by assets class element ends here

				// creating elements for investment category
				Element funds_by_investment_category = document.createElement(IKitConstants.FUNDS_BY_INVESTMENT_CATEGORY);
				document.appendElement(rootElement, funds_by_investment_category);

				Element investment_categories = document.createElement(IKitConstants.INVESTMENT_CATEGORIES);
				document.appendElement(funds_by_investment_category, investment_categories);

				List<AssetClassOrRiskReturn> fundsByInvestmentCategories = fundInfo.getFundsByInvestmentCategories();

				if (fundsByInvestmentCategories != null) {
					addInvestmentCategories(fundsByInvestmentCategories, document, investment_categories);
				}
				// investment category element ends here

				// Creating element for fund_details
				Element fund_details = document.createElement(IKitConstants.FUND_DETAILS);
				document.appendElement(rootElement, fund_details);

				Element funds = document.createElement(IKitConstants.FUNDS);
				document.appendElement(fund_details, funds);

				List<ReturnsAndFeesVO> fundDetails = fundInfo.getFundDetailsList();

				if (fundDetails != null) {
					addFundDetails(fundDetails, document, funds, isMarketIndexFund, 
							fundInfo.getFeeWaiverFunds(), fundInfo.getAsOfDates());
				}
				// fund_details element ends here

				// creating Guaranteed accounts element for current month
				GARateVO guaranteedAccountRateVo = fundInfo.getGaRateVO();
				Element guaranteed_account_funds = document.createElement(IKitConstants.GUARANTEED_ACCOUNT_FUNDS);
				document.appendElement(fund_details, guaranteed_account_funds);

				Element guaranteed_account_fund = document.createElement(IKitConstants.GUARANTEED_ACCOUNT_FUND);
				document.appendElement(guaranteed_account_funds, guaranteed_account_fund);

				if (guaranteedAccountRateVo != null) {

					// Setting the attribute to guaranteed_account_fund
					guaranteed_account_fund.setAttribute(IKitConstants.AS_OF_DATE,
							GetIKitFundInfoUtils
									.formatValue(guaranteedAccountRateVo
											.getCurrentEffectiveDate()));
					guaranteed_account_fund.setAttribute(IKitConstants.NAME,
							GetIKitFundInfoUtils
									.formatValue(guaranteedAccountRateVo
											.getCurrentMonthEndName()));


					List<GARates> gaRatesList = guaranteedAccountRateVo.getGaRatesList();

					if (gaRatesList != null) {
						addGuaranteedCurrentFund(gaRatesList, document, guaranteed_account_fund , guaranteedAccountRateVo);
					}

					// Guaranteed accounts element for current month ends here

					// creating Guaranteed accounts element for previous month

					Element guaranteed_account_fund_previous = document.createElement(IKitConstants.GUARANTEED_ACCOUNT_FUND);
					document.appendElement(guaranteed_account_funds, guaranteed_account_fund_previous);

					guaranteed_account_fund_previous.setAttribute(IKitConstants.AS_OF_DATE,
							GetIKitFundInfoUtils
									.formatValue(guaranteedAccountRateVo
											.getPreviousEffectiveDate()));
					guaranteed_account_fund_previous.setAttribute(IKitConstants.NAME,
							GetIKitFundInfoUtils
									.formatValue(guaranteedAccountRateVo
											.getPreviousMonthEndName()));			

					List<GARates> gaRatesListPrevious = guaranteedAccountRateVo.getGaRatesList();

					if (gaRatesListPrevious != null) {
						addGuaranteedPreviousFund(gaRatesListPrevious, document,
								guaranteed_account_fund_previous, guaranteedAccountRateVo);
					}
				}
				// Guaranteed accounts element for previous month ends here

				// Creating MarketIndexFundDetailsList element
				Element market_index_funds = document.createElement(IKitConstants.MARKET_INDEX_FUNDS);
				document.appendElement(fund_details, market_index_funds);

				List<ReturnsAndFeesVO> marketIndexFund  = fundInfo.getMarketIndexFundDetailsList();

				if (marketIndexFund != null) {
					isMarketIndexFund = true;
					addFundDetails(marketIndexFund, document, market_index_funds, isMarketIndexFund,
							fundInfo.getFeeWaiverFunds(), fundInfo.getAsOfDates());
				}
				// MarketIndexFundDetailsList element ends here

				Element footnote_details = document.createElement(IKitConstants.FOOTNOTE_DETAILS);
				document.appendElement(rootElement, footnote_details);
				addFootNoteDetails(fundInfo, document, footnote_details);
			}
		}
		catch(ParserConfigurationException ex){
			logger.error(IKitErrorCodes.PARSER_CONFIGURATION_EXCEPTION_MSG);

			fundInfo.getErrors().put(
					IKitErrorCodes.PARSER_CONFIGURATION_EXCEPTION_CODE,
					IKitErrorCodes.PARSER_CONFIGURATION_EXCEPTION_MSG);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("GetIKitFundInfoAction.generateXMLDocument() method -> end");
		}
		return document.getDocument();
	}

	/**
	 * This method will add the Foot note element details
	 * @param fundInfo
	 * @param document
	 * @param footnote_details
	 */
	private void addFootNoteDetails(IKitFundInfo fundInfo,
			PDFDocument document, Element footnote_details) {

		document.appendTextNode(footnote_details, IKitConstants.PAGE_FOOTER,
				fundInfo.getPageFooter());
		document.appendTextNode(footnote_details, IKitConstants.PAGE_FOOTNOTES,
				fundInfo.getPageFootnotes()); 

		Element fund_footnotes = document.createElement(IKitConstants.FUND_FOOTNOTES);
		document.appendElement(footnote_details, fund_footnotes);

		List<Footnote> footNoteArray = fundInfo.getSortedFootnotes();
		if (footNoteArray != null) {
			for (Footnote footNoteElements : footNoteArray) {
				if (footNoteElements != null) {
					Element fund_footnote = document.createElement(IKitConstants.FUND_FOOTNOTE);
					document.appendElement(fund_footnotes, fund_footnote);

					document.appendTextNode(fund_footnote, IKitConstants.FUND_FOOTNOTE_SYMBOL,
							footNoteElements.getSymbol());

					document.appendTextNode(fund_footnote, IKitConstants.FUND_FOOTNOTE_TEXT,
							footNoteElements.getText());
				}
			}
		}
		
		Element fund_footnote = document.createElement(IKitConstants.FUND_FOOTNOTE);
		document.appendElement(fund_footnotes, fund_footnote);
		document.appendTextNode(fund_footnote, IKitConstants.FUND_FOOTNOTE_SYMBOL, IKitConstants.FWI_FUND_FOOTNOTE_SYMBOL);
		document.appendTextNode(fund_footnote, IKitConstants.FUND_FOOTNOTE_TEXT,
				fundInfo.getFeeWaiverDisclaimer());
		
		
		document.appendTextNode(footnote_details, IKitConstants.PAGE_DISCLAIMER,
				fundInfo.getPageDisclaimer());

	}

}
