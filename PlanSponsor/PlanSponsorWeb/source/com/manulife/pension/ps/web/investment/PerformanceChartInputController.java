package com.manulife.pension.ps.web.investment;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
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

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.PerformanceChartInputForm;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.investment.validators.CheckDates;
import com.manulife.pension.ps.web.investment.validators.CheckFunds;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.entity.FundOffering;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.FundUnitValueHistorySummary;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.pension.util.content.GenericException;

/**
 * Action class for the Performance Charting.  It gets the data from the view funds database.
 *
 * @author   Chris Shin
 * @version  CS1.0  (April 14, 2004)
 **/
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"performanceChartInputForm"})

public class PerformanceChartInputController extends PsController {

	@ModelAttribute("performanceChartInputForm") 
	public  PerformanceChartInputForm populateForm()
	{
		return new  PerformanceChartInputForm();
	}
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/investment/performanceChartInput.jsp");
		forwards.put("results","redirect:/do/investment/performanceChartResult/" );
			}
	

	public static String SPLITTER_TEXT = "-------------- Market Indices --------------";

    public PerformanceChartInputController() {
        super(PerformanceChartInputController.class);
    }

    @RequestMapping(value ="/performanceChartInput/", method = {RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException, ServletException,SystemException {
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
      	// Reset in all the other cases: RESET or no action specified
        	actionForm.resetFundSelection();
            UserProfile userProfile = getUserProfile(request);
            CustomerServicePrincipal principal = CheckDates.hackAValidPrincipal(userProfile);

            appGetFundUnitValueHistorySummaries(principal, userProfile,
                request.getSession(false));

            return forwards.get("input");
        
    }
    
    @RequestMapping(value ="/performanceChartInput/", params={"button=view"}, method = {RequestMethod.GET}) 
    public String doView(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        return forwards.get("results"); 
    }
    
    @RequestMapping(value ="/performanceChartInput/", params={"button=reset"}, method = {RequestMethod.GET}) 
    public String doReset(@Valid @ModelAttribute("performanceChartInputForm") PerformanceChartInputForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}
    	actionForm.resetFundSelection();
        UserProfile userProfile = getUserProfile(request);
        CustomerServicePrincipal principal = CheckDates.hackAValidPrincipal(userProfile);

        appGetFundUnitValueHistorySummaries(principal, userProfile,
            request.getSession(false));

        return forwards.get("input"); 
    }

    public Collection doValidate(PerformanceChartInputForm inputForm, HttpServletRequest request) {

		Collection allErrors = new ArrayList();
		Collection dateErrors = new ArrayList();

        if (!Constants.VIEW.equals(inputForm.getButton())) {
	    	return allErrors;
        }

        allErrors = CheckFunds.checkFunds(inputForm, request);

		/**
		 * Validate the date fields
		 */
        if (!isValidDate(inputForm.getStartDate())) {
            dateErrors.add(new GenericException(ErrorCodes.CHART_START_DATE));
        }

        if (!isValidDate(inputForm.getEndDate())) {
            dateErrors.add(new GenericException(ErrorCodes.CHART_END_DATE));
        }

	   	if (dateErrors.size() > 0) {
			allErrors.addAll(dateErrors);
         	return allErrors;
	   	}

        dateErrors = CheckDates.checkDates(inputForm, request);

		if (dateErrors.size() >0 ) {
			allErrors.addAll(dateErrors);
		}
        return allErrors;
    }
	
    private boolean isValidDate(String dateString) {

        boolean ret = true;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.CHART_DATE_PATTERN);
		sdf.setLenient(false);

        if (DataValidationHelper.isBlankOrNull(dateString)) {
            ret = false;
        }
        else {
            try {
                if (sdf.parse(dateString) == null) {
                    ret = false;
                }
                else {
                    ret = true;
                }
            }
            catch (ParseException e) {
                ret = false;
            }
        }

        return ret;
    }
    
    @Autowired
	private PSValidatorFWInput  psValidatorFWInput;

    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	/**
     *
     * Retrieves the presentation value object for funds information from the ezk service
     *
	 * @param principal
	 * 		the CustomerServicePrincipal
	 *
	 * @param userProfile
	 * 		the userProfile
     *
	 * @param session
	 * 		the HTTPSession object
     *
     * @return
     * 		FundUnitValueHistorySummary[] object
     *
     * @exception SystemException
     * 		cannot get the FundUnitBalueHistorySummary object due to a problem from the service call
     *
     */
    public FundUnitValueHistorySummary[] appGetFundUnitValueHistorySummaries(
        CustomerServicePrincipal principal, UserProfile userProfile, HttpSession session)
        throws SystemException {

        FundUnitValueHistorySummary[] fundUnitValueHistorySummary =
            (FundUnitValueHistorySummary[]) session.getAttribute(Constants.VIEW_FUNDS);


		String performanceChartContract = (String) session.getAttribute(Constants.PERFORMANCE_CHART_CONTRACT);
		String contractNumber = Integer.toString(userProfile.getCurrentContract().getContractNumber());

        if (fundUnitValueHistorySummary != null &&
        	contractNumber.equals(performanceChartContract)) {
            return fundUnitValueHistorySummary;
        }
        List <String> svgFundList = new ArrayList<>();
        //Retrieving SVGIF Funds
        List <SvgifFund>svgFunds = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
        if(svgFunds != null){
        for(SvgifFund svgFund:svgFunds) {
        	svgFundList.add(svgFund.getFundId());
        }
        }
        //drop-downs
        try {
	        fundUnitValueHistorySummary = AccountServiceDelegate.getInstance().
 	           getFundsForPerformanceChart(principal, contractNumber,
               	 null, true);
        } catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"appGetFundUnitValueHistorySummaries", "Exception occurred calling service.  Contract= " + userProfile.getCurrentContract().getContractNumber());
			throw se;
        }

        if (fundUnitValueHistorySummary != null) {
            //sort by id
            Arrays.sort(fundUnitValueHistorySummary, new Comparator() {
                public int compare(Object obj1, Object obj2) {
                    String id1 = ((FundUnitValueHistorySummary) obj1).getId();
                    String id2 = ((FundUnitValueHistorySummary) obj2).getId();
                    return id1.compareTo(id2);
                }
            });
            session.setAttribute(Constants.VIEW_FUNDS_ORIGINAL, fundUnitValueHistorySummary);
            session.setAttribute(Constants.PERFORMANCE_CHART_CONTRACT, contractNumber);
        }
        FundUnitValueHistorySummary[] fundUnitValueHistorySummary2 =
            new FundUnitValueHistorySummary[fundUnitValueHistorySummary.length];
        System.arraycopy(fundUnitValueHistorySummary, 0, fundUnitValueHistorySummary2,
            0, fundUnitValueHistorySummary.length);

        Arrays.sort(fundUnitValueHistorySummary2, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                String name1 = ((FundUnitValueHistorySummary) obj1).getName();
                String name2 = ((FundUnitValueHistorySummary) obj2).getName();
                return name1.compareTo(name2);
            }
        });

        List funds = new Vector();
        List indices = new Vector();

        FundUnitValueHistorySummary noSel = new FundUnitValueHistorySummary();
		noSel.setId(" ");
        noSel.setName("Select investment option or index");
        funds.add(noSel);

        for (int i = 0; i < fundUnitValueHistorySummary2.length; i++) {
            FundUnitValueHistorySummary fund = fundUnitValueHistorySummary2[i];
            if (FundOffering.FUND_TYPE_MARKET_INDEX.equals(fund.getType()))
                indices.add(fund);
            else
            	//Removing SVGIF Funds from Funds List
            	if(!svgFundList.contains(fund.getId())) {
                funds.add(fund);
            	}
        }
        FundUnitValueHistorySummary splitter = new FundUnitValueHistorySummary();
        splitter.setName(SPLITTER_TEXT);
        splitter.setId(""); //ensure this will not be selected in the dropdown.
        funds.add(splitter);
        funds.addAll(indices);
        FundUnitValueHistorySummary[] arr = new FundUnitValueHistorySummary[
            fundUnitValueHistorySummary.length + 1];

        if (funds != null) {
            session.setAttribute(Constants.VIEW_FUNDS, funds.toArray(arr));
            session.setAttribute(Constants.PERFORMANCE_CHART_CONTRACT, contractNumber);
        }

        return fundUnitValueHistorySummary;
    }
}
