package com.manulife.pension.bd.web.fap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.BdProperties;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.ChartDataBean;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.Footnotes;
import com.manulife.pension.util.content.helper.ContentUtility;

/**
 * This is the action class that handles the activities on the results page
 *
 * * @author SAyyalusamy
 */
@Controller
@RequestMapping( value = "/fap")
@SessionAttributes({"bdPerformanceChartInputForm"})

public class PerformanceChartResultsController extends BDController {
	@ModelAttribute("bdPerformanceChartInputForm") 
	public BDPerformanceChartInputForm populateForm() 
	{
		return new BDPerformanceChartInputForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("results","/investment/performanceChartResults.jsp");
		forwards.put("input","/investment/performanceChartInput.jsp");
		forwards.put("inputPenTest","/investment/performanceChartResults.jsp");
		forwards.put("inputFirst","/do/fap/performanceChartInput/");
		forwards.put("secureHome","/do/secured/");	
	}

    private static final String XSLT_FILE_KEY_NAME = "PerformanceChartingReport.XSLFile";
    private Environment env = Environment.getInstance(); 

    public PerformanceChartResultsController() {
        super(PerformanceChartResultsController.class);
    }

    /**
     * This method performs the tasks for 
     */
    @RequestMapping(value ="/performanceChartResult",  method ={RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("bdPerformanceChartInputForm") BDPerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get("input");//if input forward not //available, provided default
	       }
		}

        
		String forwardTo = "results";
        String button = actionForm.getButton();
        final String task = request.getParameter("task");

        if(task != null && task.equalsIgnoreCase("printPDF")) {
            doPrintPDF(null, request, response);
            return null;
        }
        
       /* if(aForm.getChartDataBean()==null){
        	forwardTo = "input";
        }else {
        	request.setAttribute(BDConstants.CHART_DATA_BEAN, aForm.getChartDataBean());
        	request.getSession(false).setAttribute(BDConstants.CHART_DATA_BEAN, aForm.getChartDataBean());
        	aForm.setChartDataBean(null);
        }*/
                      
        if (BDConstants.RESET.equals(button)) { 
        	actionForm.resetFundSelection();
        	actionForm.resetFundIds();
        	actionForm.setFundClass("B");
            request.getSession(false).removeAttribute(BDConstants.CHART_DATA_BEAN);
			forwardTo = "inputFirst";
        }
        if (BDConstants.NEXT.equals(button)) { 
        	actionForm.setHideDivSec(false);
			forwardTo = "input";
        }
        
        if(!actionForm.isFromInput() && !BDConstants.RESET.equals(button) && !BDConstants.NEXT.equals(button)){
        	actionForm.resetFundSelection();
        	actionForm.setFundClass("B");
            // request.getSession(false).removeAttribute(BDConstants.CHART_DATA_BEAN);
			forwardTo = "secureHome";
        }else{
        	actionForm.setFromInput(false);
        }
        
        actionForm.setButton("");

        return forwards.get(forwardTo);
    }

    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @SuppressWarnings("unchecked")
  
    public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException {
        
        PDFDocument doc = new PDFDocument();

        // Gets layout page for contributionTransactionReport.jsp
        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(
                            BDPdfConstants.PERFORMANCE_CHART_PATH, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();

        Element rootElement = doc.createRootElement(BDPdfConstants.PERFORMANCE_CHART);
        setLogoAndPageName(layoutPageBean, doc, rootElement);
        setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        ChartDataBean chartBean = (ChartDataBean) request.getSession(false).getAttribute(BDConstants.CHART_DATA_BEAN);
        doc.appendTextNode(rootElement, BDPdfConstants.FROM_DATE, chartBean.getStartDate());
        doc.appendTextNode(rootElement, BDPdfConstants.TO_DATE, chartBean.getEndDate());

        String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081" : System.getProperty("webcontainer.http.port");
        String baseURI = "http://localhost:" + portNumber;
        
        List unitValuesArr = chartBean.getUnitValues();
        List effectiveDatesArr = chartBean.getEffectiveDates();
        List fundNames = chartBean.getFundNames();

        List pct = chartBean.getPctValues();
        boolean pctExists = false;

        if ( pct != null && pct.size() > 0 ){
            pctExists = true;
        }
        /**
         * TODO The below set of code is present in FundPerformanceChart.java taglib also
         * Need to put them in common place.
         */
        // ... normalizing unit values
        PerformanceChartUtil.normalizeUnitValues( unitValuesArr );

        // add mock portfolio if needed
        double[] mockValues = null;
        int numValues = effectiveDatesArr.size();

        if ( pctExists ){
            double totalPct = 0;
            for (Iterator it=pct.iterator(); it.hasNext(); ){
                totalPct += ((Double)it.next()).doubleValue();

            }

            Double[] pctValues = (Double[])pct.toArray(new Double[0]);

            if ( totalPct == 100 ){
                mockValues = new double[numValues];
                for( int i=0; i<numValues; i++ ){
                    mockValues[i] = 0;
                    int j = 0;
                    for( Iterator it=unitValuesArr.iterator(); it.hasNext(); j++ ){
                        double[] values = (double[])it.next();
                        mockValues[i] += values[i] * pctValues[j].doubleValue() / 100.0;
                    }
                }
            }else{
                pctExists = false;
            }
        }

        int numFunds = (pctExists)?unitValuesArr.size()+1:unitValuesArr.size();
        double[][] unitValues = new double[ numFunds ][];
        int i = 0;
        for( Iterator it=unitValuesArr.iterator(); it.hasNext(); i++ ){
            unitValues[i] = (double[])it.next();
        }

        // add mock portfolio
        if ( pctExists ){
            unitValues[numFunds-1] = mockValues;
            if ( fundNames.size() < numFunds )
                fundNames.add( BdProperties.getMockPortfolioFundName() );
        }
        /**
         * TODO The above set of code is present in FundPerformanceChart.java taglib also
         * Need to put them in common place.
         */
        String params = PerformanceChartUtil.getChartImageUrlParams(effectiveDatesArr, unitValues, chartBean, 350, 700);
        doc.appendTextNode(rootElement, BDPdfConstants.LINE_CHART_URL, baseURI + "/LineChartServlet?" + params);
        setFundDetailXML(chartBean.getFundFootnotesByFund(), chartBean.getFundNames(), unitValues, doc, rootElement);
        Location location = ApplicationHelper.getRequestContentLocation(request);
        setFooterXMLElements(layoutPageBean, doc, rootElement, location);
        String[] fundFootnotes = chartBean.getFundFootnotes();
        Footnote[] sortedSymbolsArray = null;
        try {
            sortedSymbolsArray = Footnotes.getInstance().sortFootnotes(fundFootnotes);
        } catch (Exception exception) {
            logger.error("Exception occurred while getting and sorting fund footnotes", exception);
        }        

        /**
         * loop through the footnoteSymbolsArray, print the symbols in 
         * order - *'s, #'s, ^'s, +'s, and numbers 1 to 18 Text for 
         * footnotes currently hard-coded, waiting for getContent 
         * method to be developed
         */
        Element fundFootNotes = doc.createElement(BDPdfConstants.FUND_FOOTNOTES);
        for (i = 0; i < sortedSymbolsArray.length; i++){
            if (sortedSymbolsArray[i] != null){
                Element fundFootNote = doc.createElement(BDPdfConstants.FUND_FOOTNOTE);
                doc.appendTextNode(fundFootNote, BDPdfConstants.FOOTNOTE_TEXT, sortedSymbolsArray[i].getText());
                doc.appendTextNode(fundFootNote, BDPdfConstants.FOOTNOTE_SYMBOL, sortedSymbolsArray[i].getSymbol());
                doc.appendElement(fundFootNotes, fundFootNote);
            }
        }   
        doc.appendElement(rootElement, fundFootNotes);
        return doc.getDocument();
        
    }


    /**
     * This sets fund details XML elements
     * 
     * @param fundFootnotes
     * @param fundNames
     * @param unitValues
     * @param doc
     * @param rootElement
     */
    @SuppressWarnings("unchecked")
    private void setFundDetailXML(List fundFootnotes, List fundNames, double[][] unitValues, PDFDocument doc, Element rootElement) {
        int i = 0;
        String[] notes = null;
        Element fundDetailElement = null;
        for (Iterator it = fundNames.iterator(); it.hasNext(); i++) {
            String fundName = (String)it.next();
            fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
            /**
             * Get array of footnote symbols for fund (only uniques values)
             */
            doc.appendTextNode(fundDetailElement, BDPdfConstants.CHART_LINE_COLOR, BdProperties.getChartLineColor(i+1, true));
            doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_NAME, fundName);
            if ( fundFootnotes.size() > i && (notes=(String[])fundFootnotes.get(i)).length > 0 ){
                for (int k = 0; k < notes.length; k++){
                    if (notes[k] != null)
                        doc.appendTextNode(fundDetailElement, BDPdfConstants.FOOTNOTE, notes[k]);
                }
            }

            double startingValue = unitValues[i][0];
            double endingValue = unitValues[i][unitValues[i].length - 1];
            doc.appendTextNode(fundDetailElement, BDPdfConstants.START_VALUE, PerformanceChartUtil.formatDouble(startingValue, true));
            doc.appendTextNode(fundDetailElement, BDPdfConstants.END_VALUE, PerformanceChartUtil.formatDouble(endingValue, true));
            doc.appendTextNode(fundDetailElement, BDPdfConstants.PERCENT_CHANGE, PerformanceChartUtil.formatDouble(
                    100 * (endingValue - startingValue) / startingValue, true));
            doc.appendElement(rootElement, fundDetailElement);
        }
        
    }
    
    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /**
     * This code has been changed and added  to Validate form and request against penetration attack, 
     * prior to other validations as part of the CL#136970.
   	 */ 
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

}
