package com.manulife.pension.ps.web.taglib.csf;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.web.contract.csf.CsfForm;
import com.manulife.pension.ps.web.contract.csf.util.CsfUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;

/** The tag determins if DN 52 days warning should be displayed
 * The tag calls CsfUtility for calculation of the next PED date
 * DM CSF331 
 * author Ludmila Stern
 * If does the plan want direct mail of enrollment materials
 * is changed to no and the current date within 52 days of the plan’s next applicable plan entry date, 
 * issue a warning message when ‘Save’ is clicked 
 * author Ludmila Stern
 **/
public class DmPEDWarningTag  extends TagSupport {
	    private static final long serialVersionUID = -1L;
	    private static final Logger logger = Logger.getLogger(DmPEDWarningTag.class);
	    private String formName = "actionForm";
	    /**
	     * Constructor.
	     */
	    public DmPEDWarningTag () {
	        super();
	    }
	    
	    
	    public String getFormName() {
			return formName;
		}


		public void setFormName(String formName) {
			this.formName = formName;
		}


		/**
	     * @return Returns the csf code.
	     */
	    public int doStartTag() throws JspTagException, JspException {
	    	boolean displayWarning = condition();
	    	try{
	    	if(displayWarning)
	    	pageContext.getOut().write("true");
	    	else
	    	pageContext.getOut().write("false");
	    		
	    	}catch (Exception e){
	    		logger.error("Ignored error");
	    	}
	    		
			//pageContext.setAttribute(var, new Boolean(displayWarning));
			return SKIP_BODY;
	    }

		/**
		 * @return
		 */
		protected boolean condition() {
	        boolean displayWarning = false;
	        UserProfile userProfile = (UserProfile) SessionHelper.getUserProfile((HttpServletRequest) pageContext.getRequest());
	        int contractNumber =userProfile.getCurrentContract().getContractNumber();
	        Date basePlanEntryDate = null;
	        String planFrequency =null;
	        String initialEnrollmentDate =null;
	        String InitialEnrollmentDate =null;
	        try {
	        	CsfForm form = (CsfForm) BaseTagHelper.getSpringForm(pageContext,formName);
	        	if (form!=null)
	        	{       	
	        	basePlanEntryDate =  null/*form.getBasePlanEntryDate()*/;
	        	initialEnrollmentDate =form.getInitialEnrollmentDate();
	        	//have to map the freaquency to ContractService notation
	        	if(basePlanEntryDate !=null )
	        		planFrequency = (String)CsfUtil.FREQUENCYMAP.get(form.getPlanFrequency().trim());
	            if (basePlanEntryDate !=null && planFrequency != null &&initialEnrollmentDate !=null && initialEnrollmentDate.length()>0)
	            	displayWarning = CsfUtil.isDM52Warning(basePlanEntryDate, planFrequency, initialEnrollmentDate);
	        	}
	        } catch (Exception e) {
	            logger.error("Exception calculating nextPED for contract " + contractNumber+ ", initialEnrollmentDate " + initialEnrollmentDate
	                    + " basePlanEntryDate " + basePlanEntryDate +  e);
	        }
			return displayWarning;
		}

		public int doEndTag() throws JspTagException {
	        return EVAL_PAGE;
	    }

	}

