package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
import com.manulife.pension.ps.service.participant.valueobject.ParticipantNetContribEarningsVO;
import com.manulife.pension.ps.web.transaction.LoanRepaymentDetailsReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;



@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountNetContribEarningsController extends ParticipantAccountCommonController {

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
	forwards.put("input","/participant/participantAccountNetContribEarnings.jsp");
	forwards.put("participantAccountNetContribEarnings", "/participant/participantAccountNetContribEarnings.jsp" );
	}
	@RequestMapping(value ="/participantAccountNetContribEarnings/", method =  {RequestMethod.POST,RequestMethod.GET}) 
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
		
		
	

	public ParticipantAccountNetContribEarningsController()
	{
		super(ParticipantAccountNetContribEarningsController.class);
	}

	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateNetContribEarningsDownloadData");
		}

		StringBuffer buff = new StringBuffer();
		Collection netContribEarningsDetails = participantAccountVO.getNetContribEarningsDetailsCollection();
		
		buff.append("After tax money").append(LINE_BREAK).append(LINE_BREAK);
		
		buff.append("After tax money types");
		buff.append(COMMA).append("Net contributions($)");
		buff.append(COMMA).append("Earnings($)");
		buff.append(LINE_BREAK).append(LINE_BREAK);
		
		if(form.isShowNonRothHeader()){
			buff.append("Non-Roth").append(LINE_BREAK);
			Iterator netContribEarningsDetailsIt = netContribEarningsDetails.iterator();
			while (netContribEarningsDetailsIt.hasNext()){
				ParticipantNetContribEarningsVO netContribEarningsItem = (ParticipantNetContribEarningsVO)netContribEarningsDetailsIt.next();
				if(netContribEarningsItem.isNonRothMoneyTypeInd()){
					buff.append(netContribEarningsItem.getMoneyTypeName()).append(COMMA);
					buff.append(netContribEarningsItem.getNetContributions()).append(COMMA);
					buff.append(netContribEarningsItem.getEarnings()).append(LINE_BREAK);
				}
			}
		}
		
		if(form.isShowRothHeader()){
			buff.append("Roth").append(LINE_BREAK);
			Iterator netContribEarningsDetailsIt = netContribEarningsDetails.iterator();
			while (netContribEarningsDetailsIt.hasNext()){
				ParticipantNetContribEarningsVO netContribEarningsItem = (ParticipantNetContribEarningsVO)netContribEarningsDetailsIt.next();
				if(netContribEarningsItem.isRothMoneyTypeInd()){
					buff.append(netContribEarningsItem.getMoneyTypeName()).append(COMMA);
					buff.append(netContribEarningsItem.getNetContributions()).append(COMMA);
					buff.append(netContribEarningsItem.getEarnings()).append(LINE_BREAK);
				}
			}
		}

		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateNetContribEarningsDownloadData");
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




