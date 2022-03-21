package com.manulife.pension.platform.web.fap.tabs;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.fap.FapController;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.service.fund.fandp.valueobject.FundBaseInformation;

/**
 * Action class, to enable the Fund Information tab. It gets the 
 * value object for his tab and sets in request.
 * 
 * @author ayyalsa
 *
 */
public class FapByFundInformationController extends FapController {
	
	@Override
	public String doDefault(
			AutoForm actionForm,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
	
		//return doFilter(mapping, actionForm, request, response);
		return doFilter( actionForm, request, response);
	}
	
	@Override
	public String doFilter(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
	
		FapForm fapForm = (FapForm) actionForm;
		
		if (!fapForm.isDisplayOnlyHeaders()) {
			// Retrieve the data from the session
			LinkedHashMap<String, List<? extends FundBaseInformation>> currentTabValueObject =
		        	(LinkedHashMap<String, List<? extends FundBaseInformation>>)
		        	getTabValueObject(request, fapForm, FapConstants.FUND_INFORMATION_TAB_ID, false);
			
			// do default sorting
			sortFunds(null, currentTabValueObject);
	
			// Set the default tab object to the form-bean
	    	request.setAttribute(FapConstants.VO_CURRENT_TAB, currentTabValueObject);
		}
		
    	// Get the columns info for the tab
    	request.setAttribute(FapConstants.COLUMNS_INFO_OBJECT, 
    			FapTabUtility.createFundInformationTabColumns());
		
		//return mapping.findForward(FapConstants.FORWARD_DISPLAY_TABS);
    	return FapConstants.FORWARD_DISPLAY_TABS;
	}

}
