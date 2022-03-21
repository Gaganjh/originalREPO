package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.web.transaction.LoanRepaymentDetailsReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountNetDeferralController extends ParticipantAccountCommonController {

	@ModelAttribute("participantAccountForm") 
	public ParticipantAccountForm populateForm()
	{
		return new ParticipantAccountForm();
		}
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateFormLoan() {
		return new LoanRepaymentDetailsReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/participant/participantAccountNetDeferral.jsp");
		forwards.put("participantAccountNetDeferral", "/participant/participantAccountNetDeferral.jsp" );}

	public ParticipantAccountNetDeferralController()
	{
		super(ParticipantAccountNetDeferralController.class);
	}

	
	@RequestMapping(value ="/participantAccountNetDeferral/",  method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
	       }
		}
	       String forward=super.doExecute( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDetailedDownloadData");
		}
		StringBuffer buff =new StringBuffer();
		
		if (participantAccountVO.getParticipantAccountDetailsVO().isNetEEDeferralContributionsAvailable()){
			buff.append ("Net employee contribution").append(COMMA);
			buff.append(participantAccountVO.getParticipantAccountDetailsVO().getNetEEDeferralContributions()).append(LINE_BREAK);
		}
		else{
			buff.append("Apollo error");
		}
		buff.append ("Maximum hardship amount").append(COMMA);
		buff.append(participantAccountVO.getParticipantAccountDetailsVO().getMaximumHardshipAmount()).append(LINE_BREAK);
		
		ParticipantDeferralVO deferralVO = participantAccountVO.getParticipantAccountDetailsVO().getParticipantDeferralVO();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);
		
		// similar logic to jsp output (participantAccountNetDeferral.jsp)
		if (deferralVO.isAuto() || deferralVO.isSignUp()) {
		   if (!deferralVO.isParticipantACIOn()) {
			   buff.append("Scheduled deferral increase").append(COMMA).append("Off").append(LINE_BREAK);
		   } else {
			  buff.append("Scheduled deferral increase").append(LINE_BREAK);
			  buff.append("Date of next increase").append(COMMA).append(dateFormatter.format(deferralVO.getDateOfNextIncreaseAsDate())).append(LINE_BREAK);
			  buff.append("Next increase").append(COMMA).append(form.getNextIncreaseValue()).append(LINE_BREAK);
			  buff.append("Personal rate limit").append(COMMA).append(form.getPersonalRateLimit()).append(LINE_BREAK);
		  }
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDetailedDownloadData");
		}
		return buff.toString();		
	}
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}
}




