package com.manulife.pension.platform.web.fap.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.owasp.encoder.Encode;
import org.w3c.dom.Element;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.cache.FootnoteCacheImpl;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.ColumnsInfoBean;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.fund.fandp.valueobject.Fi360ScoreQuartiles;
import com.manulife.pension.service.fund.fandp.valueobject.FundBaseInformation;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;
import com.manulife.pension.service.fund.util.Constants;
import com.manulife.pension.util.content.Footnotes;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Utility class that supports the reports the reports and download filter options
 * 
 * @author ayyalsa
 * 
 */
public class FapReportsUtility {

    private enum DataType {
        String, Date, Float, Double, BigDecimal, DEFAULT
    };

    public static final String COMMA = ",";

    public static final String LINE_BREAK = System.getProperty("line.separator");

    private FapReportsUtility() {

    }

    /**
     * Returns back the value of the attribute from the object by calling its getter
     * 
     * @param attribute String
     * @param useBooleanMethodPrefix
     * @param vo FapSummaryVO
     * @return Object
     * 
     * @throws SystemException
     */
    public static Object getValueForAttribute(String attribute, Object object,
            boolean useBooleanMethodPrefix) throws SystemException {
        try {

            Class<?> thisClass = object.getClass();
            Method actionMethod = null;
            String methodPrefix = "";
            if (useBooleanMethodPrefix) {
                methodPrefix = "is";
            } else {
                methodPrefix = "get";
            }
            String methodName = methodPrefix + attribute.substring(0, 1).toUpperCase()
                    + attribute.substring(1);
            try {
                actionMethod = thisClass.getDeclaredMethod(methodName, new Class[] {});
            } catch (NoSuchMethodException e) {
                thisClass = thisClass.getSuperclass();
            }
            if (thisClass == null) {
                throw new NoSuchMethodException("No such method: " + methodName);
            }

            try {
                actionMethod = thisClass.getDeclaredMethod(methodName, new Class[] {});
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodException("No such method: " + methodName);
            }

            Object value = actionMethod.invoke(object, new Object[] {});

            if (value == null) {
                return "";
            }

            return value;

        } catch (Throwable e) {
            Throwable causingException = ExceptionHandlerUtility
                    .getSystemExceptionOrCausedByException(e);
            if (causingException instanceof SystemException) {
                throw (SystemException) causingException;
            } else {
                throw new SystemException(causingException, "Exception calling getValue");
            }
        }
    }

	/**
	 * Returns back the headers list based on the tab selected
	 * 
	 * @param tabName
	 * @param vo FapSummaryVO
	 * @param format
	 * 
	 * @return Object
	 * 
	 * @throws SystemException
	 */
	public static Object getHeaderValueObject(String tabName, Class<?> utility,
			String format)
            throws SystemException {
        try {

            Method actionMethod = null;
            String methodName = "create" + tabName.substring(0, 1).toUpperCase()
                    + tabName.substring(1) + "TabColumns";
            try {
				if (FapConstants.MORNINGSTAR_TAB_ID.equals(tabName)) {
					actionMethod = utility.getDeclaredMethod(methodName,
							new Class[] { String.class });
				} else {
					actionMethod = utility.getDeclaredMethod(methodName,
							new Class[] {});
				}
            } catch (NoSuchMethodException e) {
                utility = utility.getSuperclass();
            }
            if (utility == null) {
                throw new NoSuchMethodException("No such method: " + methodName);
            }
			Object value = null;
			if (FapConstants.MORNINGSTAR_TAB_ID.equals(tabName)) {
				value = actionMethod.invoke(utility, new Object[] { format });
			} else {
				value = actionMethod.invoke(utility, new Object[] {});
			}
            return value;
        } catch (Throwable e) {
            Throwable causingException = ExceptionHandlerUtility
                    .getSystemExceptionOrCausedByException(e);
            if (causingException instanceof SystemException) {
                throw (SystemException) causingException;
            } else {
                throw new SystemException(causingException, "Exception calling getValue");
            }
        }
    }
	
	/**
	 * Returns back the headers list based on the tab selected
	 * 
	 * @param tabName
	 * @param vo FapSummaryVO
	 * @param format
	 * 
	 * @return Object
	 * 
	 * @throws SystemException
	 */
	public static Object getHeaderValueObject(String tabName, Class<?> utility,
			FundScoreCardMetricsSelection fundScoreCardMetricsSelection)
            throws SystemException {
        try {

            Method actionMethod = null;
            String methodName = "create" + tabName.substring(0, 1).toUpperCase()
                    + tabName.substring(1) + "TabColumns";
            try {
					actionMethod = utility.getDeclaredMethod(methodName,
							new Class[] { FundScoreCardMetricsSelection.class });
            } catch (NoSuchMethodException e) {
                utility = utility.getSuperclass();
            }
            if (utility == null) {
                throw new NoSuchMethodException("No such method: " + methodName);
            }
			Object value = null;
			value = actionMethod.invoke(utility, new Object[] { fundScoreCardMetricsSelection });
            return value;
        } catch (Throwable e) {
            Throwable causingException = ExceptionHandlerUtility
                    .getSystemExceptionOrCausedByException(e);
            if (causingException instanceof SystemException) {
                throw (SystemException) causingException;
            } else {
                throw new SystemException(causingException, "Exception calling getValue");
            }
        }
    }

    /**
     * Returns the formatted value based on the datatype
     * 
     * @param type DataType
     * @param value Object
     * 
     * @return Object
     */
    public static Object getFormattedValue(Object value) {

        DataType dataType = DataType.DEFAULT;

        if (value instanceof Date) {
            dataType = DataType.Date;
        } else if (value instanceof String) {
            dataType = DataType.String;
        } else if (value instanceof Float) {
            dataType = DataType.Float;
        } else if (value instanceof Double) {
            dataType = DataType.Double;
        }else if (value instanceof BigDecimal) {
            dataType = DataType.BigDecimal;
        }

        Object formattedValue = null;
        switch (dataType) {
            case Date:
                formattedValue = DateRender.formatByPattern(value, null,
                        RenderConstants.MEDIUM_MDY_SLASHED);
                break;
            case Float:
            case Double:
            case BigDecimal:
                NumberFormat numberFormat = DecimalFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                numberFormat.setMinimumFractionDigits(2);
                formattedValue = numberFormat.format(value);
                break;
            case String:
            case DEFAULT:
                formattedValue = value;
                break;
        }

        return formattedValue;
    }

    /**
     * returns a map of all the tab headers
     * 
     * @param siteLocation
     * 
     * @return Map<String, String>
     */
    public static Map<String, String> getAllTabHeaders(Location siteLocation) {
    	
		Map<String, String> headersForAllTab = new HashMap<String, String>();

        headersForAllTab.put(FapConstants.FUND_INFORMATION_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.FUND_INFORMATION_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.PRICES_YTD_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.PRICES_AND_YTD_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.PERFORMANCE_FEES_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.PERFORMANCE_DISCLOSURE,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.STANDARD_DEVIATION_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.STANDARD_DEVIATION_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.FUND_CHAR_I_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.FUND_CHARACTERISTICS_1_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.FUND_CHAR_II_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.FUND_CHARACTERISTICS_2_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.MORNINGSTAR_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.MORNINGSTAR_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.FUNDSCORECARD_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.JH_SIGNATURE_FUNDSCORECARD_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.FAP_GENERIC_VIEW_ID, ContentHelper.getContentText(
                CommonContentConstants.FAP_GENERIC_VIEW_DISCLOSURE,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));
        headersForAllTab.put(FapConstants.FAP_CONTRACT_VIEW_ID, ContentHelper.getContentText(
                CommonContentConstants.FAP_CONTRACT_VIEW_DISCLOSURE,
                ContentTypeManager.instance().MISCELLANEOUS, siteLocation));

        return headersForAllTab;
    }

    /**
     * returns a map of all the tab headers
     * 
     * @return Map<String, String>
     */
    public static Map<String, String> getAllTabHeadersForTpa() {
        Map<String, String> headersForAllTabTpa = new HashMap<String, String>();

        headersForAllTabTpa.put(FapConstants.FUND_INFORMATION_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_FUND_INFORMATION_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.PRICES_YTD_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_PRICES_AND_YTD_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.PERFORMANCE_FEES_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_PERFORMANCE_AND_FEES_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.STANDARD_DEVIATION_TAB_ID, ContentHelper
                .getContentText(CommonContentConstants.TPA_STANDARD_DEVIATION_TAB,
                        ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.FUND_CHAR_I_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_FUND_CHARACTERISTICS_1_TAB, ContentTypeManager
                        .instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.FUND_CHAR_II_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_FUND_CHARACTERISTICS_2_TAB, ContentTypeManager
                        .instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.MORNINGSTAR_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_MORNINGSTAR_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.FUNDSCORECARD_TAB_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_FUNDSCORECARD_TAB,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        headersForAllTabTpa.put(FapConstants.FAP_GENERIC_VIEW_ID, ContentHelper.getContentText(
                CommonContentConstants.TPA_FAP_GENERIC_VIEW_DISCLOSURE,
                ContentTypeManager.instance().MISCELLANEOUS, null));
        return headersForAllTabTpa;
    }

    /**
     * Gets the CSV header
     * 
     * @param tabName
     * @param layoutPageBean
     * @param asOfDate
     * @param tabHeaders
     * @param mspAsOfDate
     * 
     * @return String

     * @throws ContentException 
     * @throws NumberFormatException 
     */
	public static String getCsvHeader(String tabName,
			LayoutPage layoutPageBean, String asOfDate,
			Map<String, String> tabHeaders, Date mspAsOfDate, boolean isMerrillLynchAdvisor, boolean contractFundsFlag)
			throws NumberFormatException, ContentException {
        StringBuffer header = new StringBuffer();
        if (FapConstants.ALL_TABS.equals(tabName)) {
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.FUND_INFORMATION_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.PRICES_YTD_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.PERFORMANCE_FEES_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.STANDARD_DEVIATION_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.FUND_CHAR_I_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.FUND_CHAR_II_TAB_ID)))).append(LINE_BREAK);
            header.append(
                    ContentUtility.filterCMAContentForCSV(escapeField(tabHeaders
                            .get(FapConstants.MORNINGSTAR_TAB_ID)))).append(LINE_BREAK);
        } else {
            header.append(escapeField(ContentUtility
                    .filterCMAContentForCSV(tabHeaders.get(tabName))));
        }
        StringBuffer buffer = new StringBuffer();
        String intro1Text = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.INTRO1_TEXT, null);
        String intro2Text = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.INTRO2_TEXT, null);
        String name = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.PAGE_NAME, null);
                
        buffer.append(name).append(" : ").append(Encode.forHtmlContent(asOfDate));
        buffer.append(LINE_BREAK);
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(tabName)
				|| FapConstants.ALL_TABS.equals(tabName)) {
			String formattedValue = DateRender.formatByPattern(mspAsOfDate,
					null, RenderConstants.MEDIUM_MDY_SLASHED);
			buffer.append("Morningstar Information As Of ").append(
					formattedValue);
			buffer.append(LINE_BREAK);
		}
        buffer.append(LINE_BREAK);
        if (!StringUtils.isEmpty(intro1Text)) {
            intro1Text = ContentUtility.filterCMAContentForCSV(intro1Text);
            buffer.append(escapeField(intro1Text)).append(LINE_BREAK);
        }
        if (!StringUtils.isEmpty(intro2Text)) {
            intro2Text = ContentUtility.filterCMAContentForCSV(intro2Text);
            buffer.append(escapeField(intro2Text)).append(LINE_BREAK);
        }
        buffer.append(header.toString());
        buffer.append(LINE_BREAK);

        //Added for Funds and Performance FWI
        if( FapConstants.PRICES_YTD_TAB_ID.equals(tabName) || 
        		FapConstants.PERFORMANCE_FEES_TAB_ID.equals(tabName) || 
        		FapConstants.FUNDSCORECARD_TAB_ID.equals(tabName) || 
        		FapConstants.MORNINGSTAR_TAB_ID.equals(tabName)  || FapConstants.ALL_TABS.equals(tabName) ){
	        String feeWaiverDisclosure =  ContentHelper.getContentText(CommonContentConstants.FEE_WAIVER_DISCLOSURE_TEXT,
	        		ContentTypeManager.instance().DISCLAIMER, null);
	        buffer.append(LINE_BREAK);
	        if (feeWaiverDisclosure != null) {	
	        	feeWaiverDisclosure = StringEscapeUtils.unescapeHtml(feeWaiverDisclosure);
				addContentsAsCSV(buffer, feeWaiverDisclosure);			
	        }
        }
        
        if(isMerrillLynchAdvisor && (FapConstants.FUND_INFORMATION_TAB_ID.equals(tabName) || 
        		FapConstants.PRICES_YTD_TAB_ID.equals(tabName) || 
        		FapConstants.PERFORMANCE_FEES_TAB_ID.equals(tabName) || 
        		FapConstants.STANDARD_DEVIATION_TAB_ID.equals(tabName) || 
        		FapConstants.FUND_CHAR_I_TAB_ID.equals(tabName) || 
        		FapConstants.FUND_CHAR_II_TAB_ID.equals(tabName) || 
        		FapConstants.MORNINGSTAR_TAB_ID.equals(tabName)  || FapConstants.ALL_TABS.equals(tabName) )){
	        String restrictedFundsDisclosure =  ContentHelper.getContentText(CommonContentConstants.MERRILL_RESRICTED_FUNDS_CONTENT,
	        		ContentTypeManager.instance().DISCLAIMER, null);
	        buffer.append(LINE_BREAK);
	        if (restrictedFundsDisclosure != null) {	
	        	restrictedFundsDisclosure = StringEscapeUtils.unescapeHtml(restrictedFundsDisclosure);
				addContentsAsCSV(buffer, restrictedFundsDisclosure);			
	        }
        }
        
        if( FapConstants.FUNDSCORECARD_TAB_ID.equals(tabName)){
	        String fundScorecardDisclosure =  ContentHelper.getContentText(CommonContentConstants.FUND_SCORECARD_DISCLOSURE_TEXT,
	        		ContentTypeManager.instance().DISCLAIMER, null);
	        buffer.append(LINE_BREAK);
	        if (fundScorecardDisclosure != null) {	
	        	fundScorecardDisclosure = StringEscapeUtils.unescapeHtml(fundScorecardDisclosure);
				addContentsAsCSV(buffer, fundScorecardDisclosure);			
	        }
        }
        
        String genericOrContractViewDisclosure = null;
        if(contractFundsFlag) {
            genericOrContractViewDisclosure = ContentHelper.getContentText(CommonContentConstants.FAP_CONTRACT_VIEW_DISCLOSURE,
            		ContentTypeManager.instance().DISCLAIMER, null);
        }else {
            genericOrContractViewDisclosure = ContentHelper.getContentText(CommonContentConstants.FAP_GENERIC_VIEW_DISCLOSURE,
            		ContentTypeManager.instance().DISCLAIMER, null);
        }
        buffer.append(LINE_BREAK);
        if (genericOrContractViewDisclosure != null) {  
            genericOrContractViewDisclosure = StringEscapeUtils.unescapeHtml(genericOrContractViewDisclosure);
            addContentsAsCSV(buffer, genericOrContractViewDisclosure);          
        }

        buffer.append(LINE_BREAK);
        return buffer.toString();
    }

	/**
	 * Gets the CSV footer
	 * 
	 * @param layoutPageBean
	 * @param request
	 * @param location
	 * @param data
	 * @param selectedTab
	 * 
	 * @return String
	 * 
	 * @throws SystemException
	 * @throws ContentException
	 * @throws NumberFormatException
	 */
	public static String getCsvFooter(LayoutPage layoutPageBean,
			HttpServletRequest request, Location location,
			HashMap<String, List<? extends FundBaseInformation>> data,
			String selectedTab, int globalDisclosureCMAKey) throws SystemException, NumberFormatException,
			ContentException {

        StringBuffer buffer = new StringBuffer();
        buffer.append(LINE_BREAK);
        
		String modifiedLineUpDisclaimer = getModifiedLineupDisclaimer(request);
		if (modifiedLineUpDisclaimer != null) {
			buffer.append(LINE_BREAK);
			addContentsAsCSV(buffer, modifiedLineUpDisclaimer);
		}
        
        // FootNotes - start
        String footer = ContentUtility.getPageFooter(layoutPageBean, new String[] {});
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab)) {
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			String morningstarFootNotes = getContent(
					CommonContentConstants.MORNINGSTAR_FOOTNOTE,
					ContentTypeManager.instance().MISCELLANEOUS,
					PdfConstants.TEXT, null, null);
			if (morningstarFootNotes != null) {
				addContentsAsCSV(buffer, morningstarFootNotes);
			}
		}
        String footnotes = ContentUtility.getPageFootnotes(layoutPageBean, new String[] {}, -1);
        String disclaimer = ContentUtility.getPageDisclaimer(layoutPageBean, new String[] {}, -1);
        String globalDisclosureText = ContentHelper.getContentText(
        		globalDisclosureCMAKey,
                ContentTypeManager.instance().MISCELLANEOUS, location);

        if (footer != null) {
            addContentsAsCSV(buffer, footer);
        }

        if (footnotes != null) {
            addContentsAsCSV(buffer, footnotes);
        }
        
        buffer.append(LINE_BREAK);
        
        if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
			String fundscorecardMorningstarFootnotes = getContent(
					CommonContentConstants.MORNINGSTAR_FOOTNOTE,
					ContentTypeManager.instance().MISCELLANEOUS,
					PdfConstants.TEXT, null, null);
			if (fundscorecardMorningstarFootnotes != null) {
				addContentsAsCSV(buffer, fundscorecardMorningstarFootnotes);
			}
			buffer.append(LINE_BREAK);
		}
        
        // FootNotes for Morningstar
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab) || 
				FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
			Set<String> keys = data.keySet();
			for (String key : keys) {
				List<? extends FundBaseInformation> result = data.get(key);
				for (FundBaseInformation vo : result) {
					int id = 0;
					//Getting the content id for each fund
					id = ((Integer) FapReportsUtility.getValueForAttribute(
							FapConstants.MORNINGSTAR_FOOTNOTE_CMA_ID, vo, false)).intValue();
					if (id != 0) {
						String morningstarFootNotes = setMorningstarFootNotes(id, vo);
						addContentsAsCSV(buffer, morningstarFootNotes);
					}
				}
			}
			buffer.append(LINE_BREAK);
		}

        String[] footnoteSymbolsArray = null;

        request.getSession().getAttribute("symbolsArray");
        if (request.getAttribute("symbolsArray") != null)
            footnoteSymbolsArray = (String[]) request.getAttribute("symbolsArray");
        else if (request.getSession().getAttribute("symbolsArray") != null)
            footnoteSymbolsArray = (String[]) request.getSession().getAttribute("symbolsArray");

		final String companyId = Location.NEW_YORK.equals(location) ? CommonConstants.COMPANY_ID_NY
				: CommonConstants.COMPANY_ID_US;

        if (footnoteSymbolsArray != null) {
            Footnote[] sortedSymbolsArray = new Footnote[] {};
            try {
				sortedSymbolsArray = FootnoteCacheImpl.getInstance()
						.sortFootnotes(footnoteSymbolsArray, companyId);
            } catch (Exception e) {
                throw new SystemException(e, "Exception getting the fund footnotes");
            }

            /**
             * loop through the footnoteSymbolsArray, print the symbols in order - *'s, #'s, ^'s,
             * +'s, and numbers 1 to 18 Text for footnotes currently hard-coded, waiting for
             * getContent method to be developed
             */
            for (int i = 0; i < sortedSymbolsArray.length; i++) {
                if (sortedSymbolsArray[i] != null) {
                    String returnText = "";
                    if (sortedSymbolsArray[i].getText() != null) {
                        returnText = ContentUtility.jsEsc(sortedSymbolsArray[i].getText());
                        returnText = ContentUtility.filterCMAContentForCSV(returnText);
                    }
                    String returnSymbol = sortedSymbolsArray[i].getSymbol();
                    String footnone = returnSymbol + " " + returnText;
                    buffer.append(escapeField(footnone));
                    buffer.append(LINE_BREAK);
                }
            }
			buffer.append(LINE_BREAK);
		}

		if (disclaimer != null) {
			addContentsAsCSV(buffer, disclaimer);
		}

		if (globalDisclosureText != null) {
			addContentsAsCSV(buffer, globalDisclosureText);
        }

		return buffer.toString();
	}

	/**
	 * Gets the All Tab CSV footer
	 * 
	 * @param layoutPageBean
	 * @param request
	 * @param location
	 * @param fundsAndPerformance
	 * @param data
	 * @param listOfHeader
	 * 
	 * @return String
	 * 
	 * @throws SystemException
	 * @throws ContentException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("unchecked")
	public static String getAllTabCsvFooter(LayoutPage layoutPageBean,
			HttpServletRequest request, Location location,
			FundsAndPerformance fundsAndPerformance,
			HashMap<String, List<? extends FundBaseInformation>> data,
			HashMap<List<ColumnsInfoBean>, String> listOfHeader)
			throws SystemException, NumberFormatException, ContentException {

		StringBuffer buffer = new StringBuffer();
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		String modifiedLineUpDisclaimer = getModifiedLineupDisclaimer(request);
		if (modifiedLineUpDisclaimer != null) {
			addContentsAsCSV(buffer, modifiedLineUpDisclaimer);
			buffer.append(LINE_BREAK);
		}
		
		// FootNotes - start
		String footer = ContentUtility.getPageFooter(layoutPageBean,
				new String[] {});
		String morningstarFooter = getContent(
				CommonContentConstants.MORNINGSTAR_FOOTNOTE,
				ContentTypeManager.instance().MISCELLANEOUS, PdfConstants.TEXT,
				null, null);
		if (morningstarFooter != null) {
			addContentsAsCSV(buffer, morningstarFooter);
		}
		String footnotes = ContentUtility.getPageFootnotes(layoutPageBean,
				new String[] {}, -1);
		String disclaimer = ContentUtility.getPageDisclaimer(layoutPageBean,
				new String[] {}, -1);
		String globalDisclosureText = ContentHelper.getContentText(
				CommonContentConstants.BD_GLOBAL_DISCLOSURE, ContentTypeManager
						.instance().MISCELLANEOUS, location);

		if (footer != null) {
			addContentsAsCSV(buffer, footer);
		}

		if (footnotes != null) {
			addContentsAsCSV(buffer, footnotes);
		}
		
		buffer.append(LINE_BREAK);
		
		// FootNotes for Morningstar
		Set<List<ColumnsInfoBean>> headers = listOfHeader.keySet();
		Set<String> keys = data.keySet();
		for (String key : keys) {
			List<? extends FundBaseInformation> result = data.get(key);
			int size = result.size();
			for (int rowCount = 0; rowCount < size; rowCount++) {
				for (List<ColumnsInfoBean> tablist : headers) {
					String valueOject = listOfHeader.get(tablist);
					HashMap<String, List<? extends FundBaseInformation>> actualVo = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
							.getValueForAttribute(valueOject,
									fundsAndPerformance, false);
					List<? extends FundBaseInformation> resultVO = actualVo
							.get(key);
					if(resultVO != null && resultVO.size() > rowCount) {
						FundBaseInformation vo = resultVO.get(rowCount);
						int id = 0;
						//Getting the content id for each fund
						id = ((Integer) FapReportsUtility.getValueForAttribute(
								FapConstants.MORNINGSTAR_FOOTNOTE_CMA_ID, vo, false)).intValue();
						if (id != 0) {
							String morningstarFootNotes = setMorningstarFootNotes(id, vo);
							addContentsAsCSV(buffer, morningstarFootNotes);
						}
					}
				}
			}
		}
		
		buffer.append(LINE_BREAK);

		String[] footnoteSymbolsArray = null;

		request.getSession().getAttribute("symbolsArray");
		if (request.getAttribute("symbolsArray") != null)
			footnoteSymbolsArray = (String[]) request
					.getAttribute("symbolsArray");
		else if (request.getSession().getAttribute("symbolsArray") != null)
			footnoteSymbolsArray = (String[]) request.getSession()
					.getAttribute("symbolsArray");

		if (footnoteSymbolsArray != null) {
			Footnote[] sortedSymbolsArray = new Footnote[] {};
			try {
				sortedSymbolsArray = Footnotes.getInstance().sortFootnotes(
						footnoteSymbolsArray);
			} catch (Exception e) {
				throw new SystemException(e,
						"Exception getting the fund footnotes");
			}

			/**
			 * loop through the footnoteSymbolsArray, print the symbols in order
			 * - *'s, #'s, ^'s, +'s, and numbers 1 to 18 Text for footnotes
			 * currently hard-coded, waiting for getContent method to be
			 * developed
			 */
			for (int i = 0; i < sortedSymbolsArray.length; i++) {
				if (sortedSymbolsArray[i] != null) {
					String returnText = "";
					if (sortedSymbolsArray[i].getText() != null) {
						returnText = ContentUtility.jsEsc(sortedSymbolsArray[i]
								.getText());
						returnText = ContentUtility
								.filterCMAContentForCSV(returnText);
					}
					String returnSymbol = sortedSymbolsArray[i].getSymbol();
					String footnone = returnSymbol + " " + returnText;
					buffer.append(escapeField(footnone));
					buffer.append(LINE_BREAK);
				}
			}
			buffer.append(LINE_BREAK);
		}

		buffer.append(LINE_BREAK);

        if (disclaimer != null) {
            addContentsAsCSV(buffer, disclaimer);
        }
        
        if(globalDisclosureText != null) {
            addContentsAsCSV(buffer, globalDisclosureText);
        }

        return buffer.toString();
    }

    /**
     * don't want excel to think the , is the next field
     * 
     * @param field
     * @return String
     */
    public static String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(
                    CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }

    /**
     * Returns the values to be shown in down-loaded All Tab CSV in bytes[]
     * 
     * @param level1Tab
     * @param listOfHeader
     * @param request
     * @param layoutPageBean
     * @param asOfDate
     * @param tabHeaders
     * @param location
     * 
     * @return byte[]
     * 
     * @throws SystemException
     * @throws ContentException 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
	public static byte[] getDownloadedDataForCsvAll(
			List<List<ColumnsInfoBean>> level1Tab,
			HashMap<List<ColumnsInfoBean>, String> listOfHeader,
			HashMap<String, List<? extends FundBaseInformation>> currentTabData,
			HttpServletRequest request, LayoutPage layoutPageBean,
			String asOfDate, Map<String, String> tabHeaders, Location location,
			HashMap<String, List<? extends FundBaseInformation>> sortedData, boolean isMerrillLynchAdvisor, boolean contractFundsFlag)
			throws SystemException, NumberFormatException, ContentException {

        StringBuffer buffer = new StringBuffer();
        
		FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request
				.getSession().getAttribute(
						FapConstants.VO_FUNDS_AND_PERFORMANCE);
        
        // get the page headers
		buffer.append(FapReportsUtility.getCsvHeader(FapConstants.ALL_TABS,
				layoutPageBean, asOfDate, tabHeaders, fundsAndPerformance
				.getAsOfDates().get(FapConstants.CONTEXT_MSP), isMerrillLynchAdvisor, contractFundsFlag));

        buffer.append(getFilterOptions(request, null));

        Set<List<ColumnsInfoBean>> headers = listOfHeader.keySet();

        // first level of table headers
        StringBuffer secondRow = new StringBuffer();
        if (level1Tab != null && !level1Tab.isEmpty()) {
        	int lastRow = level1Tab.size();
            for (List<ColumnsInfoBean> tab : level1Tab) {
                for (ColumnsInfoBean row : tab) {

                    String[] values = stripHtmlTags(row.getName());
                    String value = "";
                    for (String name : values) {
                        value = value + "" + name;
                    }

                    buffer.append(value).append(COMMA);
                    if (row.getRowSpan() != null) {
                        secondRow.append(COMMA);
                    }
                    if (row.getColSpan() != null) {
                        int colspan = Integer.parseInt(row.getColSpan());
                        while (colspan > 1) {
                            if (row.getRowSpan() != null) {
                                secondRow.append(COMMA);
                            }
                            buffer.append(COMMA);
                            colspan--;
                        }
                    }
                }
                lastRow--;
                if(lastRow > 0) {
                	 buffer.append(LINE_BREAK);
                	 buffer.append(secondRow.toString());
                }
            }
        }

        buffer.append(LINE_BREAK);

        // 2nd level of table headers
        for (List<ColumnsInfoBean> tablist : headers) {
            for (ColumnsInfoBean tab : tablist) {

                String[] names = stripHtmlTags(tab.getName());
                String tabName = "";
                for (String name : names) {
                    tabName = tabName + "" + name;
                }

                buffer.append(tabName).append(COMMA);
                if (tab.getColSpan() != null) {
                    int colspan = Integer.parseInt(tab.getColSpan());
                    while (colspan > 1) {
                        buffer.append(COMMA);
                        colspan--;
                    }
                }
            }
        }

        buffer.append(LINE_BREAK);

        String tab = "";
        Collection<String> tabKey = listOfHeader.values();
        for (String value : tabKey) {
            tab = value;
            break;
        }

        String marketIndexRow = null;

        // get the table values
        HashMap<String, List<? extends FundBaseInformation>> firstVO = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(tab, fundsAndPerformance, false);
        Set<String> keys = firstVO.keySet();
        for (String key : keys) {
            buffer.append(key).append(LINE_BREAK);
            List<? extends FundBaseInformation> result = firstVO.get(key);
            int size = result.size();
            for (int rowCount = 0; rowCount < size; rowCount++) {
                for (List<ColumnsInfoBean> tablist : headers) {
                    String valueOject = listOfHeader.get(tablist);
                    for (ColumnsInfoBean tab1 : tablist) {
                        HashMap<String, List<? extends FundBaseInformation>> actualVo = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                                .getValueForAttribute(valueOject, fundsAndPerformance, false);
                        List<? extends FundBaseInformation> resultVO = actualVo.get(key);
                        List<? extends FundBaseInformation> sortedFunds = currentTabData.get(key);
                        
                        Object resultValue = null;
                        
                        FundBaseInformation vo = getFundBaseInformationAtRow(resultVO, getSortedFunds(sortedData.get(key), sortedFunds), rowCount);
                        //FundBaseInformation vo = resultVO.get(rowCount);
                        String tabHeaderKey = tab1.getKeyForCsv() != null ? tab1.getKeyForCsv()
                                : tab1.getKey();

                        if ((vo == null) ||
                        		("dateIntroduced".equals(tabHeaderKey) && (vo.isMarketIndexFund() || vo.isGuaranteedFund()))) {
                            resultValue = "-";
                        } else {
                            resultValue = FapReportsUtility.getValueForAttribute(tabHeaderKey, vo,
                                    false);
                        }

                       if (vo != null && vo.getFundDisclosureText() != null) {
                            marketIndexRow = vo.getFundDisclosureText();
                        }

                        resultValue = FapReportsUtility.getFormattedValue(resultValue);
                        Object originalValue = resultValue;
                        
                        
                        //Added for F & P FWI
                        if ("fundName".equals(tabHeaderKey) && (isMerrillLynchAdvisor && vo != null && vo.isRestrictedFund())  ){	                     
                     		resultValue =  "#" + resultValue;	                        
                        }
                        if ("fundName".equals(tabHeaderKey) && (vo != null && vo.isFeeWaiverFund())  ){	                     
	                     		resultValue =  "•" + resultValue;	                        
                        }
                    
                        if(resultValue == null || "".equals(resultValue)) {
                            resultValue ="\t-";
                        }

                        // to escape commas that might be there in the string
                        if (resultValue instanceof String) {
                            resultValue = FapReportsUtility.escapeField((String) resultValue);

                            String[] values = stripHtmlTags((String) resultValue);
                            resultValue = "";
                            for (String name : values) {
                                resultValue = resultValue + "" + name;
                            }

                        }
                        
                        if(vo != null) {
                        	if (originalValue == null || "".equals(originalValue)) {
    							if (FapConstants.Fi360.equals(tab1.getValueRenderingType())) {
    								resultValue = "NS";
    							}
    						} else {
    							if (isPercetageValue(tab1.getFormat())) {
    								resultValue = resultValue + "%";
    							}
    						}
                            
                            if(tab1.getSecondaryKey() != null) {
                            	Object secondaryValue = FapReportsUtility.getValueForAttribute(tab1.getSecondaryKey(), vo,
                                         false);
                            	secondaryValue = FapReportsUtility.getFormattedValue(secondaryValue);
                            	if(secondaryValue == null || "".equals(secondaryValue)) {
                            		secondaryValue ="-";
                            	}
                            	resultValue = resultValue + " ( " +  secondaryValue + " )";
                            }
                        }
						
                        
                        buffer.append(resultValue).append(COMMA);
                    }
                }

                if (marketIndexRow != null) {
                    buffer.append(LINE_BREAK).append(marketIndexRow);
                }
                marketIndexRow = null;
                buffer.append(LINE_BREAK);
            }
        }

        // gets the footers
		buffer.append(FapReportsUtility.getAllTabCsvFooter(layoutPageBean,
				request, location, fundsAndPerformance, firstVO, listOfHeader));

        return buffer.toString().getBytes();
    }
    
	private static List<FundBaseInformation> getSortedFunds(
			List<? extends FundBaseInformation> defaultSortFunds,
			List<? extends FundBaseInformation> sortedFunds) {
		
		int defaultFundsSize = defaultSortFunds.size();
	    int sortFundSize = sortedFunds == null ? 0 :	sortedFunds.size();		
				
		List<FundBaseInformation> funds = new ArrayList<FundBaseInformation>();
		if(sortFundSize > 0) {
			funds.addAll(sortedFunds);
		}
		
		if(sortFundSize != defaultFundsSize) {
			for (FundBaseInformation base : defaultSortFunds) {
				int count = 0;
				if(sortedFunds != null) {
					for (FundBaseInformation sort : sortedFunds) {
						if (StringUtils.equals(base.getFundId(), sort.getFundId())) {
							break;
						}
						count++;
					}
				}
				if (count == sortFundSize) {
					funds.add(base);
				}
			}
		}
		
   		return funds;
	}

    /**
     * Based on the sortedFund's order to get the the fund in rowCount
     * @param resultVO
     * @param sortedFunds
     * @param rowCount
     * @return
     */
    private static FundBaseInformation getFundBaseInformationAtRow(
			List<? extends FundBaseInformation> fundList,
			List<? extends FundBaseInformation> sortedFunds, int rowNum) {
    	
    	if(fundList == null) {
    		return null;
    	}
    	
    	if(sortedFunds == null) {
    		return fundList.get(rowNum);
    	}
    	
    	FundBaseInformation fund = sortedFunds.get(rowNum);
    	String selectedFundKey = fund.getFundKey();
    	
    	for (FundBaseInformation f : fundList) {
    		if (StringUtils.equals(f.getFundKey(), selectedFundKey)) {
    			return f;
    		}
    	}
		return null;
	}

	/**
     * Returns the values to be shown in down-loaded CSV in bytes[]
     * 
     * @param selectedTab
     * @param columns
     * @param data
     * @param request
     * @param layoutPageBean
     * @param asOfDate
     * @param tabHeaders
     * @param location
     * 
     * @return byte[]
     * 
     * @throws SystemException
     * @throws ContentException 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
	public static byte[] getDownloadedData(String selectedTab,
			HashMap<String, List> columns,
			HashMap<String, List<? extends FundBaseInformation>> data,
			HttpServletRequest request, LayoutPage layoutPageBean,
			String asOfDate, Map<String, String> tabHeaders, Location location,
			FundScoreCardMetricsSelection fundScoreCardMetricsSelection, int globalDisclosureCMAKey, 
			boolean isMerrillLynchAdvisor, boolean contractFundsFlag)
			throws SystemException, NumberFormatException, ContentException {

        StringBuffer buffer = new StringBuffer();

		FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request
				.getSession().getAttribute(
						FapConstants.VO_FUNDS_AND_PERFORMANCE);
        
        // get the page headers
		buffer.append(FapReportsUtility.getCsvHeader(selectedTab,
				layoutPageBean, asOfDate, tabHeaders, fundsAndPerformance
						.getAsOfDates().get(FapConstants.CONTEXT_MSP), isMerrillLynchAdvisor, contractFundsFlag));

        buffer.append(getFilterOptions(request, selectedTab));

        List<List<ColumnsInfoBean>> levelOneTabs = columns
                .get(FapConstants.COLUMN_HEADINGS_LEVEL_1);
        StringBuffer secondRow = new StringBuffer();

        // 1st level of table headers
        if (levelOneTabs != null && !levelOneTabs.isEmpty()) {
            for (List<ColumnsInfoBean> tab : levelOneTabs) {

                if (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)) {
                    tab.addAll(FapTabUtility.getLevel1HeadersForPricesAndYtd());
                }

                for (ColumnsInfoBean row : tab) {

                    String[] names = stripHtmlTags(row.getName());
                    String tabName = "";
                    for (String name : names) {
                        tabName = tabName + "" + name;
                    }

                    buffer.append(tabName).append(COMMA);
                    if (row.getRowSpan() != null) {
                        secondRow.append(COMMA);
                    }
                    if (row.getColSpan() != null) {
                        int colspan = Integer.parseInt(row.getColSpan());
                        while (colspan > 1) {
                            if (row.getRowSpan() != null) {
                                secondRow.append(COMMA);
                            }
                            buffer.append(COMMA);
                            colspan--;
                        }
                    }
                }
                buffer.append(LINE_BREAK);
                buffer.append(secondRow.toString());
            }
        }
        buffer.append(LINE_BREAK);
        
        if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
        	buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	if(fundScoreCardMetricsSelection.isShowMorningstarScorecardMetrics()) {
        		buffer.append("MORNINGSTAR").append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	}
        	if(fundScoreCardMetricsSelection.isShowFi360ScorecardMetrics()) {
        		buffer.append("FI360 *56").append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	}
        	if(fundScoreCardMetricsSelection.isShowRpagScorecardMetrics()) {
        		buffer.append("RPAG *57").append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	}
        	buffer.append(LINE_BREAK);
        	
        	buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	if(fundScoreCardMetricsSelection.isShowMorningstarScorecardMetrics()) {
        		buffer.append("Total Return (% Rank in category)*55").append(COMMA)
        		.append(COMMA).append(COMMA).append(COMMA).append("Overall Rating* (# of peers)").append(COMMA);
        	}
        	if(fundScoreCardMetricsSelection.isShowFi360ScorecardMetrics()) {
        		buffer.append("Fi360 Fiduciary Score ® (# of peers)").append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	}
        	if(fundScoreCardMetricsSelection.isShowRpagScorecardMetrics()) {
        		buffer.append("RPAG Score (out of 10)").append(COMMA).append(COMMA).append(COMMA).append(COMMA);
        	}
        	buffer.append(LINE_BREAK);
        }

        // 2nd level of table headers
        List<ColumnsInfoBean> levelTwoTabs = columns.get(FapConstants.COLUMN_HEADINGS_LEVEL_2);

        for (ColumnsInfoBean tab : levelTwoTabs) {

            String[] names = stripHtmlTags(tab.getName());
            String tabName = "";
            for (String name : names) {
                tabName = tabName + "" + name;
            }
            
            if(!tab.isShowHeader()) {
            	tabName = StringUtils.EMPTY;
        	}

            buffer.append(tabName).append(COMMA);
        }
        buffer.append(LINE_BREAK);

        // get the table values
        Set<String> keys = data.keySet();
        for (String key : keys) {
            buffer.append(key).append(LINE_BREAK);
            List<? extends FundBaseInformation> result = data.get(key);
            for (FundBaseInformation vo : result) {
                for (ColumnsInfoBean tab : levelTwoTabs) {
                                	
                	Object resultValue = null;

                    if ("dateIntroduced".equals(tab.getKey()) && (vo.isMarketIndexFund() || vo.isGuaranteedFund())) {
                        resultValue = "-";
                    } else {
                        resultValue = FapReportsUtility.getValueForAttribute(
                                tab.getKeyForCsv() != null ? tab.getKeyForCsv() : tab.getKey(), vo,
                                false);
                    }

                    resultValue = FapReportsUtility.getFormattedValue(resultValue);
                    Object originalValue = resultValue;
                    
                    if(resultValue == null || "".equals(resultValue)) {
                        resultValue ="\t-";
                    }
                    
                    //Added for F & P FWI     
                    if(isMerrillLynchAdvisor && isRestrictedColumnIncluded(selectedTab, tab.getKey())){
        				if (vo.isRestrictedFund()){	                     
                     		resultValue =  "#" + resultValue;	                        
                        }
        			}
                	if (isFeeWaiverColumnIncluded(selectedTab, tab.getKey())) {
        				if(vo.isFeeWaiverFund()) {
        					resultValue =  "•" + resultValue;
        				}        				
        			}
                
                    // to escape commas that might be there in the string
                    if (resultValue instanceof String) {
                        resultValue = FapReportsUtility.escapeField((String) resultValue);

                        String[] values = stripHtmlTags((String) resultValue);
                        resultValue = "";
                        for (String name : values) {
                            resultValue = resultValue + "" + name;
                        }

                    }   
                    
					if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
						if (originalValue == null || "".equals(originalValue)) {
							if (FapConstants.Fi360.equals(tab.getValueRenderingType())) {
								resultValue = "NS";
							}
						} else {
							if (isPercetageValue(tab.getFormat())) {
								resultValue = resultValue + "%";
							}
						}
					}
                    
                    
                    if(tab.getSecondaryKey() != null) {
                    	Object secondaryValue = FapReportsUtility.getValueForAttribute(tab.getSecondaryKey(), vo,
                                 false);
                    	secondaryValue = FapReportsUtility.getFormattedValue(secondaryValue);
                    	if(secondaryValue == null || "".equals(secondaryValue)) {
                    		secondaryValue ="-";
                    	}
                    	resultValue = resultValue + " ( " +  secondaryValue + " )";
                    }
                    
                    
                    buffer.append(resultValue).append(COMMA);
                }
                if (vo.getFundDisclosureText() != null) {
                    buffer.append(LINE_BREAK).append(vo.getFundDisclosureText());
                }
                buffer.append(LINE_BREAK);
            }
        }

        // get the footers
		buffer.append(FapReportsUtility.getCsvFooter(layoutPageBean, request,
				location, data, selectedTab, globalDisclosureCMAKey));
        return buffer.toString().getBytes();
    }

    /**
     * This method strips the html tags if there is a super tag then a array of two elements is
     * return if there is no super tag then a array of one elements is return
     * 
     * @param value
     * @return String[]
     */
    public static String[] stripHtmlTags(String value) {
        boolean hasSuperTags = false;
        if (value.indexOf("sup") != -1 || value.indexOf("SUP") != -1) {
            hasSuperTags = true;
        }
        Pattern pattern = Pattern.compile("<.*?>|&nbsp;");
        String[] result = pattern.split(value);
        String name = "";
        int length = result.length;
        String superValue = "";
        if (hasSuperTags) {
            superValue = result[length - 1];
            for (int count1 = 0; count1 < length - 1; count1++) {
                name = name + result[count1];
            }
            name = name.replaceAll("&amp;", "&");
            return new String[] { name, superValue };
        } else {
            for (int count1 = 0; count1 < length; count1++) {
                name = name + result[count1];
            }
            name = name.replaceAll("&amp;", "&");
            return new String[] { name };
        }
    }

    /**
     * This method sets the Logo and the Page Name.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    public static String getImageUrl(String path, String imageName) {
        StringBuffer imagePath = new StringBuffer();
        imagePath.append(FapReportsUtility.class.getResource(path + imageName));
        return imagePath.toString();
    }

    /**
     * This method sets the Logo and the Page Name.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setLogoAndPageName(LayoutPage layoutPageBean, PDFDocument pdfDoc,
            Element rootElement, String logoFileName) {
        StringBuffer logoPath = new StringBuffer();
        // Logo will be the same for both US and NY because, they both see the same page
        // with the same logo.
        logoPath.append(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
                + logoFileName));
        pdfDoc.appendTextNode(rootElement, PdfConstants.JH_LOGO_PATH, logoPath.toString());

        String pageName = ContentUtility.getContentAttributeText(layoutPageBean,
                CommonContentConstants.PAGE_NAME, null);
        pdfDoc.appendTextNode(rootElement, PdfConstants.PAGE_NAME, pageName);
    }

    /**
     * This method sets Level 2 header elements
     * 
     * @param doc PDFDocument
     * @param rootElement Element
     * @param levelTwoTabs List<ColumnsInfoBean>
     * @param selectedTab String
     */
    public static void setLevel2HeaderElements(PDFDocument doc, Element rootElement,
    		 List<ColumnsInfoBean> levelTwoTabs, String selectedTab, boolean isMerrillLynchAdvisor, boolean isMerrillLynchContract, String currentView) {

        Element tableHeader3 = doc.createElement(PdfConstants.HEADER3);

        // for P&YTD tab get the extra header names
        if (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)) {
            levelTwoTabs.addAll(FapTabUtility.getLevel2columnsMap().get(
                    FapConstants.PRICES_YTD_LEVEL2COLUMNS_CSV));
        }

        // populalate the level 2 header elements
        for (ColumnsInfoBean tab : levelTwoTabs) {
        	
        	// skip if header is not to be displayed
        	if(!tab.isShowHeader()) {
        		continue;
        	}
        	
            String[] strippedValues = {};
            strippedValues = FapReportsUtility.stripHtmlTags(tab.getName());
            Element tableHeader = doc.createElement(PdfConstants.HEADER);
            doc.appendElement(tableHeader3, tableHeader);
            
            // get the text-align for the column
            String align = getTextAlignForHeaders(tab.getColumnClass());
            doc.appendTextNode(tableHeader, "align", align);
            int length = strippedValues.length;
            doc.appendTextNode(tableHeader, PdfConstants.NAME, strippedValues[0]);
            if (length > 1) {
                doc.appendTextNode(tableHeader, PdfConstants.SUPER, strippedValues[1]);
            }
            
			if (isFeeWaiverColumnIncluded(selectedTab, tab.getKey())) {
		        PdfHelper.setFeeWaiverDisclosure(doc, rootElement);
				doc.appendTextNode(tableHeader, "colSpan", "2");
			}

			
			if ((isMerrillLynchAdvisor && isRestrictedColumnIncluded(selectedTab, tab.getKey())) 
					|| (isMerrillLynchContract && isRestrictedColumnIncluded(selectedTab, tab.getKey()))) {
		        PdfHelper.setRestrictedFundsDisclosure(doc, rootElement);
				doc.appendTextNode(tableHeader, "colSpan", "2");
			}			

			if(tab.getKey().equals("fundName")) {
				if (currentView.contains("Contract Funds") || currentView.startsWith("Contract Funds")) {
					PdfHelper.setContractFundDisclosure(doc, rootElement);
		        } else {
					PdfHelper.setGenericFundDisclosure(doc, rootElement);
		        }
			}
			
			if (isRestrictedColumnIncluded(selectedTab, tab.getKey())) {
				doc.appendTextNode(tableHeader, "colSpan", "2");
			}

        }
        doc.appendElement(rootElement, tableHeader3);
    }

	/**
	 * This method sets the fund footnotes elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param request
	 * @param bean
	 * @param site
	 * @param reportData
	 * @param selectedTab
	 * 
	 * @throws SystemException
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	@SuppressWarnings("unchecked")
	public static void setFootNotesElement(PDFDocument doc,
			Element rootElement, HttpServletRequest request, LayoutPage bean,
			Location site,
			HashMap<String, List<? extends FundBaseInformation>> reportData,
			String selectedTab, int globalDisclosureCMAKey) throws SystemException, NumberFormatException,
			ContentException {
		String[] footnoteSymbolsArray = null;

        Element tabFootnotesElement;
        Element moriningstarTabFootnotesElement;

        request.getSession().getAttribute("symbolsArray");
        if (request.getAttribute("symbolsArray") != null)
            footnoteSymbolsArray = (String[]) request.getAttribute("symbolsArray");
        else if (request.getSession().getAttribute("symbolsArray") != null)
            footnoteSymbolsArray = (String[]) request.getSession().getAttribute("symbolsArray");

        // FootNotes - start
        String footer = ContentUtility.getPageFooter(bean, new String[] {});
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab)) {
			String morningstarFootNotes = getContent(
					CommonContentConstants.MORNINGSTAR_FOOTNOTE,
					ContentTypeManager.instance().MISCELLANEOUS,
					PdfConstants.TEXT, null, null);
			footer = morningstarFootNotes + "\n" + footer;
		}
		
		String modifiedLineUpDisclaimer = getModifiedLineupDisclaimer(request);
		if (modifiedLineUpDisclaimer != null) {
			footer = modifiedLineUpDisclaimer + "\n" + footer;
		}
		
        String footnotes = ContentUtility.getPageFootnotes(bean, new String[] {}, -1);
        String disclaimer = ContentUtility.getPageDisclaimer(bean, new String[] {}, -1);
        
        
        if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
			footnotes = footnotes + "\n" +  getContent(
					CommonContentConstants.MORNINGSTAR_FOOTNOTE,
					ContentTypeManager.instance().MISCELLANEOUS,
					PdfConstants.TEXT, null, null);;
		}

        PdfHelper.convertIntoDOM(PdfConstants.FOOTER, rootElement, doc, footer);
        PdfHelper.convertIntoDOM(PdfConstants.FOOTNOTES, rootElement, doc, footnotes);
        
        // FootNotes for Morningstar
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab)
				|| FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
			int id = 0;
			Set<String> keys = reportData.keySet();
			for (String key : keys) {
				List<? extends FundBaseInformation> result = reportData
						.get(key);
				for (FundBaseInformation vo : result) {
					//Getting the content id for each fund
					id = ((Integer) FapReportsUtility.getValueForAttribute(
							FapConstants.MORNINGSTAR_FOOTNOTE_CMA_ID, vo, false)).intValue();
					if (id != 0) {
						moriningstarTabFootnotesElement = doc
								.createElement(PdfConstants.MORNINGSTAR_TAB_FOOTNOTES);
						doc.appendElement(rootElement,
								moriningstarTabFootnotesElement);

						String morningstarFootNotes = setMorningstarFootNotes(id, vo);

						PdfHelper.convertIntoDOM(
								PdfConstants.MORNINGSTAR_TAB_FOOTNOTES,
								moriningstarTabFootnotesElement, doc,
								morningstarFootNotes);
					}
				}
			}
		}

		final String companyId = Location.NEW_YORK.equals(site) ? CommonConstants.COMPANY_ID_NY
				: CommonConstants.COMPANY_ID_US;

        if (footnoteSymbolsArray != null) {
            Footnote[] sortedSymbolsArray = new Footnote[] {};
            try {
				sortedSymbolsArray = FootnoteCacheImpl.getInstance()
						.sortFootnotes(footnoteSymbolsArray, companyId);
            } catch (Exception e) {
                throw new SystemException(e, "Exception getting the fund footnotes");
            }

            /**
             * loop through the footnoteSymbolsArray, print the symbols in order - *'s, #'s, ^'s,
             * +'s, and numbers 1 to 18 Text for footnotes currently hard-coded, waiting for
             * getContent method to be developed
             */
            for (int i = 0; i < sortedSymbolsArray.length; i++) {
                if (sortedSymbolsArray[i] != null) {
                    tabFootnotesElement = doc.createElement(PdfConstants.TAB_FOOTNOTES);
                    doc.appendElement(rootElement, tabFootnotesElement);
                    String returnText = "";
                    if (sortedSymbolsArray[i].getText() != null) {
                        returnText = ContentUtility.jsEsc(sortedSymbolsArray[i].getText());
                        returnText = ContentUtility.filterCMAContentForPDF(returnText);
                    }
                    String returnSymbol = sortedSymbolsArray[i].getSymbol();
                    doc.appendTextNode(tabFootnotesElement, PdfConstants.SUPER, returnSymbol);
                    PdfHelper.convertIntoDOM(PdfConstants.VALUE, tabFootnotesElement, doc, returnText);
                }
            }
        }

        PdfHelper.convertIntoDOM(PdfConstants.DISCLAIMER, rootElement, doc, disclaimer);

        PdfHelper.setGlobalDisclosureXMLElement(doc, rootElement, site, globalDisclosureCMAKey);
        
        if(FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
    		PdfHelper.setFundScorecardDisclosure(doc, rootElement);
    	}
    }

    /**
     * This method sets Level 1 header elements
     * 
     * @param doc PDFDocument
     * @param rootElement Element
     * @param levelOneTabs List<List<ColumnsInfoBean>>
     * @param selectedTab String
     */
    public static void setLevel1HeaderElements(PDFDocument doc, Element rootElement,
            List<List<ColumnsInfoBean>> levelOneTabs, String selectedTab) {

        Element tableHeader = null;
        if (levelOneTabs != null && !levelOneTabs.isEmpty()) {
            int count = 0;
            List<ColumnsInfoBean> tab = null;

            // if size is 2 then there are 2 rows of level 1 header
            if (levelOneTabs.size() == 2) {
                Element tableHeader1 = doc.createElement(PdfConstants.HEADER1);
                tab = levelOneTabs.get(count++);
                for (ColumnsInfoBean row : tab) {
                    String[] strippedValues = {};
                    strippedValues = FapReportsUtility.stripHtmlTags(row.getName());
                    tableHeader = doc.createElement(PdfConstants.HEADER);
                    doc.appendElement(tableHeader1, tableHeader);
                    int length = strippedValues.length;
                    doc.appendTextNode(tableHeader, PdfConstants.NAME, strippedValues[0]);
                    if (length > 1) {
                        doc.appendTextNode(tableHeader, PdfConstants.SUPER, strippedValues[1]);
                    }
                    if (row.getColSpanForPdf() != null) {
                        doc.appendTextNode(tableHeader, PdfConstants.COL_SPAN, row.getColSpanForPdf());
                    } else {
                        doc.appendTextNode(tableHeader, PdfConstants.COL_SPAN, "1");
                    }
                    if (row.getRowSpan() != null) {
                        doc.appendTextNode(tableHeader, PdfConstants.ROW_SPAN, row.getRowSpan());
                    }
                }
                doc.appendElement(rootElement, tableHeader1);
            }

            tab = levelOneTabs.get(count++);

            if (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)) {
                tab.addAll(FapTabUtility.getLevel1HeadersForPricesAndYtd());
            }

            Element tableHeader2 = doc.createElement(PdfConstants.HEADER2);
            for (ColumnsInfoBean row : tab) {
                String[] strippedValues = {};
                strippedValues = FapReportsUtility.stripHtmlTags(row.getName());
                tableHeader = doc.createElement(PdfConstants.HEADER);
                doc.appendElement(tableHeader2, tableHeader);
                int length = strippedValues.length;
                doc.appendTextNode(tableHeader, PdfConstants.NAME, strippedValues[0]);

                if (length > 1) {
                    doc.appendTextNode(tableHeader, PdfConstants.SUPER, strippedValues[1]);
                }
                if (row.getColSpanForPdf() != null) {
                    doc.appendTextNode(tableHeader, PdfConstants.COL_SPAN, row.getColSpanForPdf());
                } else {
                    doc.appendTextNode(tableHeader, PdfConstants.COL_SPAN, "1");
                }
            }
            doc.appendElement(rootElement, tableHeader2);
        }
        
        if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
        	 Element imagePath = doc.createElement("imagePath");
             String imagePathStr = getImagePath();
             imagePath.setTextContent(imagePathStr);
             doc.appendElement(rootElement, imagePath);
        }
       
    }
    
    /**
     * Sets dynamic deployed path to image files
     * @return String
     */
    private static String getImagePath() {
        StringBuffer imagePath = new StringBuffer();
        imagePath.append(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX));
        return imagePath.toString();
    }

    /**
     * This method sets the fund details pdf elements
     * 
     * @param doc
     * @param rootElement
     * @param levelTwoTabs
     * @param reportData
     * @param selectedTab
     * @param fundCheckAsOfDate
     * 
     * @throws SystemException
     */
    public static void setFundDeatilsElements(PDFDocument doc, Element rootElement,
            List<ColumnsInfoBean> levelTwoTabs,
            HashMap<String, List<? extends FundBaseInformation>> reportData, String selectedTab,
            String fundCheckAsOfDate, boolean isMerrillLynchAdvisor, boolean isMerrillLynchContract) throws SystemException {
        Element reportDetailsElement = doc.createElement(PdfConstants.REPORT_DETAILS);
        Element reportDetailElement = null;
        Set<String> keys = reportData.keySet();
        for (String key : keys) {
            Element fundDetailElement;
            reportDetailElement = doc.createElement(PdfConstants.REPORT_DETAIL);
            doc.appendTextNode(reportDetailElement, PdfConstants.FUND_CATEGORY, key);

            List<? extends FundBaseInformation> result = reportData.get(key);
            
            int index = 0;

            for (FundBaseInformation vo : result) {
            	
                fundDetailElement = doc.createElement(PdfConstants.FUND_DETAIL);
                doc.appendElement(reportDetailElement, fundDetailElement);

                boolean isSelectedByContract = vo.isSelectedBycontract();
               
                if (isSelectedByContract) {
                    doc.appendTextNode(fundDetailElement, "rowColor", "#FFFFDB");
                } else {
                	if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
                   	 if(index ++ % 2 == 1) {
                        	doc.appendTextNode(fundDetailElement, "rowColor", "#fafaf8");
                        } 
                   }
                }

                boolean isClosedToNB = vo.isClosedToNB();
                if (isClosedToNB) {
                    doc.appendTextNode(fundDetailElement, "rowFontColor", "#767676");
                }
                
                if (vo.getRowSpan() > 1) {
                    doc.appendTextNode(fundDetailElement, "rowSpan", String.valueOf(vo.getRowSpan()));
                }

                for (ColumnsInfoBean tab : levelTwoTabs) {
                	
        			if (isFeeWaiverColumnIncluded(selectedTab, tab.getKey())) {
        				if(vo.isFeeWaiverFund()) {
        					doc.appendTextNode(fundDetailElement, "showFeeWaiverSymbol", "true");
        				}
        				if (vo.getRowSpan() == 2) {
        					doc.appendTextNode(fundDetailElement, "showFeeWaiverSymbolRowSpan", "2");
        				}
        				doc.appendTextNode(fundDetailElement, "includeFeeWaiverColumn", "true");
        			}
        			
        			if (isRestrictedColumnIncluded(selectedTab, tab.getKey())) {
        				if(vo.isRestrictedFund() && (isMerrillLynchAdvisor || isMerrillLynchContract)) {
        					doc.appendTextNode(fundDetailElement, "showRestrictedFundSymbol", "true");
        				}
        				doc.appendTextNode(fundDetailElement, "includeRestrictedFund", "true");
        			}
                    Object value = null;

                    if ("dateIntroduced".equals(tab.getKey()) && (vo.isMarketIndexFund() || vo.isGuaranteedFund())) {
                        value = "-";
                    } else {
                        value = FapReportsUtility.getValueForAttribute(tab.getKey(), vo, false);
                    }

                    String align = getTextAlign(tab.getColumnClass());
                    
                    // check if the tab is supposed to show a image
                    if ("IMAGE".equals(tab.getValueRenderingType())
                            && StringUtils.isNotEmpty((String) value)) {

                    	 Element valueElement = doc.createElement(PdfConstants.VALUE);
                         doc.appendElement(fundDetailElement, valueElement);
                         doc.appendTextNode(valueElement, PdfConstants.ALIGN, align);
                    	if(StringUtils.equals("-", (String) value)) {
                    		doc.appendTextNode(valueElement, PdfConstants.NAME,(String) value);
                    	}
                    	else{
                    		doc.appendTextNode(valueElement, PdfConstants.IMAGE, 
                    				FapReportsUtility.getImageUrl(
                    						CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX, 
                    						(String) value));
                    	}
                        continue;
                    }

                    
                    // format the value
                    value = FapReportsUtility.getFormattedValue(value);
                    
                    // if the object value is NULL or BLANK, then set the values as "-"
                    // the Object can be a String or BigDecimal
                    if(value == null || "".equals(value)) {
                        value ="-";
                    } else if(isPercetageValue(tab.getFormat())) {
                    	value = value + "%";
                    }

                    // strip the html tags
                    String[] strippedValues = {};
                    if (value instanceof String) {
                        strippedValues = FapReportsUtility.stripHtmlTags((String) value);
                    }

                    Element valueElement = doc.createElement(PdfConstants.VALUE);
                    doc.appendElement(fundDetailElement, valueElement);

                    if (tab.isHypoLogicApplicable() && vo.getHypotheticalInfo() != null) {
                        Boolean isValueHyporthical = (Boolean) getValueForAttribute(tab.getKey(),
                                vo.getHypotheticalInfo(), true);
                        if (isValueHyporthical) {
                            doc.appendTextNode(valueElement, "bold", "true");
                        }
                    }
                    
                    if("Fi360".equals(tab.getValueRenderingType())) {
                    	if(StringUtils.isNumeric(String.valueOf(value))) {
                    		String style = Fi360ScoreQuartiles.getFi360Style((Integer)value);
                        	doc.appendTextNode(valueElement, PdfConstants.Fi360Style, style);
                        	doc.appendTextNode(valueElement, "bold", "true");
                    	} else {
                    		value = "NS";
                    	}
                    }
                    
                    if(isTextBold(tab.getColumnClass())) {
                    	doc.appendTextNode(valueElement, "boldScore", "true");
                    }

                    // if stripped array has two elements
                    // then the value has super tags
                    if (strippedValues.length == 2) {
                        doc.appendElement(fundDetailElement, valueElement);
                        doc.appendTextNode(valueElement, PdfConstants.NAME, strippedValues[0]);
                      //Defect #2330 Added Extra spaces for the alignment issue
                        strippedValues[1] = StringUtils.replace(strippedValues[1], ",", ", ");
                        doc.appendTextNode(valueElement, PdfConstants.SUPER, strippedValues[1]);
                    } else {
                        doc.appendTextNode(valueElement, PdfConstants.NAME, "" + value);
                    }

                    doc.appendTextNode(valueElement, PdfConstants.ALIGN, align);
                    
                    if(tab.getSecondaryKey() != null) {
                    	 Object secValue = FapReportsUtility.getValueForAttribute(tab.getSecondaryKey(), vo, false);
                         secValue = FapReportsUtility.getFormattedValue(secValue);
                         if(secValue == null || "".equals(secValue)) {
                         	secValue ="-";
                         }
                         doc.appendTextNode(valueElement, PdfConstants.SECONDARY_VALUE, "" + secValue);
                    }
                    
                }

                if (vo.getFundDisclosureText() != null) {
                    doc.appendTextNode(fundDetailElement, "disclosureText", vo
                            .getFundDisclosureText());
                }

            }
            doc.appendElement(reportDetailsElement, reportDetailElement);
        }
        doc.appendElement(rootElement, reportDetailsElement);
    }

    /**
     * Get Filter Options
     * 
     * @param request
     * @return String
     * @throws SystemException
     */
    private static String getFilterOptions(HttpServletRequest request, String selectedTab) throws SystemException {

        StringBuffer buffer = new StringBuffer();

        String modifiedLineUp = (String) request.getAttribute("modifiedLineUp");
        String currentView = (String) request.getSession().getAttribute("View");
        String currentContract = (String) request.getSession().getAttribute("Contract");
        String currentClass = (String) request.getSession().getAttribute("Class");
        String currentGroupByOption = (String) request.getSession().getAttribute("GroupBy");
        String fundMenu = (String) request.getSession().getAttribute(
                FapConstants.FUND_MENU);

        if(!"All Funds".equals(fundMenu)) {
            currentView = fundMenu;
        } else {
            if (FapConstants.CONTRACT_FUNDS.equals(currentView)) {
                currentView = "Contract Funds";
            } else {
                currentView = "All Funds";
            } 
        }

        if ("true".equals(modifiedLineUp)) {
        	 if ("Contract Funds".equals(currentView)) {
            	 currentView = currentView + " - Modified Lineup^^";
            } else {
            	 currentView = currentView + " - Modified Lineup^";
            } 
        }

        if (currentClass != null) {
            currentClass = FundClassUtility.getInstance().getFundClassName(currentClass);
        }

        if ("filterRiskCategoryFunds".equals(currentGroupByOption)) {
            currentGroupByOption = "Risk/Return Category";
        } else {
            currentGroupByOption = "Asset Class";
        }

        buffer.append(LINE_BREAK);
        buffer.append("View").append(COMMA).append(currentView).append(LINE_BREAK);
        if (currentContract != null) {
            buffer.append("Contract").append(COMMA).append(currentContract).append(LINE_BREAK);
        }
        if (!FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab) && currentClass != null) {
            buffer.append("Class").append(COMMA).append(currentClass).append(LINE_BREAK);
        }
        buffer.append("Group by").append(COMMA).append(currentGroupByOption).append(LINE_BREAK);

        return buffer.toString();
    }

    /**
     * Strips out the HTML tags from the buffer
     * 
     * @param buffer
     * @param content
     */
    private static void addContentsAsCSV(StringBuffer buffer, String content) {

        // replace double quotes with single quotes
        if (content.contains("\"")) {
            content = content.replaceAll("\"", "\'");
        }

        // split the <BR> tags to separate sections
        String[] contentArray = content.split("<br/>|<BR/>|<br>|<BR>|</p>");

        for (String value : contentArray) {
            value = ContentUtility.filterCMAContentForCSV(value);
            buffer.append(escapeField(value)).append(LINE_BREAK);
        }
    }

    /**
     * Returns the text-align for the PDFs.
     *  
     * @param columnClass
     * @return right for the numeric columns and left for other columns
     */
    private static String getTextAlign(String columnClass) {
    	// default is left aligned
        String align = "left";
        
        if (StringUtils.contains(columnClass, "cur") ||
        		StringUtils.contains(columnClass, "name")) {
        	align = "right";
        } else if (StringUtils.contains(columnClass, "date")) {
        	align = "center";
        }else if (StringUtils.contains(columnClass, "fcalign")) {
        	align = "center";
        }else if (StringUtils.contains(columnClass, "eralign")) {
        	align = "center";
        } else if (StringUtils.contains(columnClass, "score")) {
        	align = "center";
        }
        
        return align;
    }
    
    /**
     * Returns the text-align for the PDFs.
     *  
     * @param columnClass
     * @return right for the numeric columns and left for other columns
     */
    private static boolean isTextBold(String columnClass) {
        if (StringUtils.contains(columnClass, "score_bold")) {
        	return true;
        }
        return false;
    }
    
    /**
     * Returns the text-align for the PDFs.
     *  
     * @param columnClass
     * @return right for the numeric columns and left for other columns
     */
    private static boolean isPercetageValue(String format) {
        if (StringUtils.contains(format, "%")) {
        	return true;
        }
        return false;
    }
    
    /**
     * Returns the text-align for column headers.
     * Used for PDF
     *  
     * @param columnClass
     * @return right for the numeric columns and left for other columns
     */
    private static String getTextAlignForHeaders(String columnClass) {
    	// default is left aligned
        String align = "left";
        
        if (StringUtils.contains(columnClass, "cur") || 
        		StringUtils.contains(columnClass, "date") ||
        		StringUtils.contains(columnClass, "fcalign") ||
        		StringUtils.contains(columnClass, "eralign") ||
        		StringUtils.contains(columnClass, "name") ||
        		StringUtils.contains(columnClass, "score")) {
        	align = "center";
        }  
        
        return align;
    }

	/**
	 * Method to get the Content
	 * 
	 * @param contentId
	 * @param contentType
	 * @param attribute
	 * @param member
	 * @param params
	 * 
	 * @return String
	 * 
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	public static String getContent(String contentId, ContentType contentType,
			String attribute, String member, String[] params)
			throws NumberFormatException, ContentException {
		Content content = ContentCacheManager.getInstance().getContentById(
				Integer.parseInt(contentId), contentType);
		String contentText = "";
		if (params != null && params.length != 0) {
			contentText = ContentUtility.getContentAttribute(content,
					attribute, member, params);
		} else {
			contentText = ContentUtility
					.getContentAttribute(content, attribute);
		}

		return contentText;
	}
	
	private static boolean isFeeWaiverColumnIncluded(String selectedTab,
			String columnKey) {
		if (columnKey.equals("fundName")
				&& (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)
						|| FapConstants.PERFORMANCE_FEES_TAB_ID.equals(selectedTab)
						|| FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab)
						|| FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab))) {
			return true;
		}
		return false;
	}

	private static boolean isRestrictedColumnIncluded(String selectedTab,
			String columnKey) {
		if (columnKey.equals("fundName")
				&& (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)
						|| FapConstants.PERFORMANCE_FEES_TAB_ID.equals(selectedTab)
						|| FapConstants.MORNINGSTAR_TAB_ID.equals(selectedTab)
						|| FapConstants.FUND_CHAR_I_TAB_ID.equals(selectedTab)
						|| FapConstants.FUND_INFORMATION_TAB_ID.equals(selectedTab)
						|| FapConstants.FUND_CHAR_II_TAB_ID.equals(selectedTab)
						|| FapConstants.STANDARD_DEVIATION_TAB_ID.equals(selectedTab))) {
			return true;
		}
		return false;
	}
	/**
	 * Sets Morningstar Footnotes for pdf, csv
	 * 
	 * @param id
	 * @param vo
	 * 
	 * @return String
	 * 
	 * @throws SystemException
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	@SuppressWarnings("unchecked")
	private static String setMorningstarFootNotes(int id, FundBaseInformation vo)
			throws SystemException, NumberFormatException, ContentException {
		String morningstarFootNotes = null;
		
		//Getting the List of FootNote params for substituting it in Content
		List<String> morningstarFootNoteList = (List<String>) FapReportsUtility
				.getValueForAttribute(FapConstants.MORNINGSTAR_FOOTNOTE_LIST,
						vo, false);
		String[] params = (String[]) morningstarFootNoteList
				.toArray(new String[0]);
		/**
		 * Based on the ratings available for the 3Yr, 5Yr, and 10 Yr periods
		 * the content is built with corresponding content id
		 */
		if (id == FapConstants.MORNINGSTAR_3_5_10_YR_FOOTNOTE) {
			morningstarFootNotes = getContent(
					CommonContentConstants.MORNINGSTAR_3_5_10_YR_FOOTNOTE,
					ContentTypeManager.instance().PAGE_FOOTNOTE,
					PdfConstants.TEXT, PdfConstants.TEXT, params);
		} else if (id == FapConstants.MORNINGSTAR_3_5_YR_FOOTNOTE) {
			morningstarFootNotes = getContent(
					CommonContentConstants.MORNINGSTAR_3_5_YR_FOOTNOTE,
					ContentTypeManager.instance().PAGE_FOOTNOTE,
					PdfConstants.TEXT, PdfConstants.TEXT, params);
		} else if (id == FapConstants.MORNINGSTAR_3_YR_FOOTNOTE) {
			morningstarFootNotes = getContent(
					CommonContentConstants.MORNINGSTAR_3_YR_FOOTNOTE,
					ContentTypeManager.instance().PAGE_FOOTNOTE,
					PdfConstants.TEXT, PdfConstants.TEXT, params);
		}

		return morningstarFootNotes;
	}

	
	private static String getModifiedLineupDisclaimer(HttpServletRequest request) throws NumberFormatException, ContentException {
		String modifiedLineUpDisclaimer = null;
		String modifiedLineUp = (String) request.getAttribute("modifiedLineUp");
        if("true".equals(modifiedLineUp)) {
        	String currentView = (String) request.getSession().getAttribute("View");
        	if(FapConstants.CONTRACT_FUNDS.equals(currentView)) {
        		modifiedLineUpDisclaimer = getContent(
    					String.valueOf(CommonContentConstants.MODIFIED_LINE_UP_DISCLAIMER_CONTRACT_FUNDS),
    					ContentTypeManager.instance().MISCELLANEOUS,
    					PdfConstants.TEXT, null, null);
        	} else {
        		modifiedLineUpDisclaimer = getContent(
        				String.valueOf(CommonContentConstants.MODIFIED_LINE_UP_DISCLAIMER_ALL_FUNDS),
    					ContentTypeManager.instance().MISCELLANEOUS,
    					PdfConstants.TEXT, null, null);
        	}
        }
		return modifiedLineUpDisclaimer;
	}
}
