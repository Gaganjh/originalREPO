package com.manulife.pension.ps.web.preferences;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;

/**
 * @author Charles Chan
 */
@Controller
@RequestMapping( value ="/preferences")
public class SetReportPageSizeController extends PsController implements CommonConstants{

	
	@ModelAttribute(" setReportPageSizeForm ")
	public  SetReportPageSizeForm  populateForm() 
	{
		return new  SetReportPageSizeForm ();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("reloadPageResetPageNumber","/WEB-INF/global/reloadPageResetPageNumber.jsp");
	}

	
	
    /**
     * Constructor.
     * 
     * @param clazz
     */
    public SetReportPageSizeController() {
        super(SetReportPageSizeController.class);
    }

    /**
     * @see com.manulife.pension.ps.web.controller.PsController#doExecute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value ="/setReportPageSize/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("setReportPageSizeForm") SetReportPageSizeForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

    

        //SetReportPageSizeForm actionForm = (SetReportPageSizeForm) form;

        UserProfile profile = getUserProfile(request);

        Integer newPageSize = actionForm.getNewPageSize();
        if (newPageSize == null || newPageSize.intValue() == 0) {
            return null;
        }

        profile.getPreferences().put(UserPreferenceKeys.REPORT_PAGE_SIZE,
                String.valueOf(actionForm.getNewPageSize()));

        SecurityServiceDelegate.getInstance().updateUserPreference(
                profile.getPrincipal(), UserPreferenceKeys.REPORT_PAGE_SIZE,
                String.valueOf(actionForm.getNewPageSize()));

        return forwards.get(Constants.RELOAD_PAGE_RESET_PAGE_NUMBER_FORWARD);
    }
}