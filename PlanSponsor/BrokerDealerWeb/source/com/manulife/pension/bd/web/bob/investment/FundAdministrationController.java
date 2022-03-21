package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * 
 * This is the controller class for BOB Fund Administration Page
 * @author dasaran
 *
 */

@Controller
@RequestMapping(value = "/bob")
public class FundAdministrationController {
	
	public static Map<String,String> forwards = new HashMap<>();
	 public static final String  IS_LIVESITE_VARIABLE = "isLiveSite";
	 public static final String  IS_FCP_LIVE_VARIABLE = "isFcpLive";
	static{
		forwards.put("input","/fundAdministration/fundAdministration.jsp");
		forwards.put("default","/fundAdministration/fundAdministration.jsp");
		}
	
	@RequestMapping(value ="/fundAdministration", method =  {RequestMethod.GET}) 
	
	 public String doDefault(@Valid @ModelAttribute("fapForm") FapForm actionForm,
	            HttpServletRequest request, HttpServletResponse response) throws IOException,
	            ServletException, SystemException {

	        FapForm fapForm = (FapForm) actionForm;

	        // get the report's as of date
	        Date reportAsOfDate = FundServiceDelegate.getInstance().getReportAsOfDate();
	        if (reportAsOfDate != null) {
	            String formattedValue = DateRender.formatByPattern(reportAsOfDate, null,
	                    RenderConstants.MEDIUM_MDY_SLASHED);
	            fapForm.setAsOfDate(formattedValue);
	        } else {
	            fapForm.setAsOfDate(StringUtils.EMPTY);
	        }
	        
	      //DOL_FCP changes
	    	BaseEnvironment environment = new BaseEnvironment();

			boolean isLiveNaming = Boolean.parseBoolean(environment
					.getNamingVariable(IS_LIVESITE_VARIABLE,null));
			boolean isFcpLiveNaming = Boolean.parseBoolean(environment
					.getNamingVariable(IS_FCP_LIVE_VARIABLE,null));
			
			if(!isLiveNaming || isFcpLiveNaming) {
				fapForm.setFcpContent(true);
			}
	        
	        return forwards.get("default");
	    }

}
