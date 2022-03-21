package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.text.DecimalFormat;
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
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWParticipantInput;

@Controller
@RequestMapping(value="/db")
@SessionAttributes({"participantAccountForm"})

public class DefinedBenefitAccountController extends DefinedBenefitAccountCommonController {
	@ModelAttribute("participantAccountForm") 
	public  ParticipantAccountForm populateForm()
	{
		return new  ParticipantAccountForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/definedBenefitAccount.jsp"); 
		forwards.put("definedBenefitAccount","/participant/definedBenefitAccount.jsp");}

	
	private static String TAB_NAME = "DefinedBenefitAccount";

	public DefinedBenefitAccountController()
	{
		super(DefinedBenefitAccountController.class);
	}
	@RequestMapping(value ="/definedBenefitAccount/", method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
	       }
		}
		forward=super.doExecute( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }

	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> DefinedBenefitAccountAction");
		}

		DecimalFormat unitsHeldFormatter = new DecimalFormat("#0.000000");
		DecimalFormat percentFormatter = new DecimalFormat("#0.00");
		DecimalFormat unitValueFormatter = new DecimalFormat("#0.00");

		StringBuffer buff = new StringBuffer();

		// Section 4 fund summary collection for the report
		buff.append("Investment option,Number of units,Unit value($),Balance($),Percentage of total(%),Ongoing contributions(%)").append(LINE_BREAK);
		InvestmentOptionVO [] options = participantAccountVO.getParticipantFundsByRisk();
		for(int i=0;i<options.length;i++) {
			InvestmentOptionVO optionVO = options[i];
			if(optionVO.getParticipantFundSummaryArray().length==0) continue;

			buff.append(LINE_BREAK).append(optionVO.getCategory().getCategoryDesc()).append(LINE_BREAK);
			ParticipantFundSummaryVO [] summaries = optionVO.getParticipantFundSummaryArray();

			for(int j=0;j<summaries.length;j++) {
				ParticipantFundSummaryVO summVO = summaries[j];

				// first line is the fund aggregate level
				buff.append(summVO.getFundName()).append(COMMA);
				if(summVO.getFundTotalNumberOfUnitsHeld()==0) {
					buff.append(COMMA);
					if (summVO.getFundTotalCompositeRate() > 0) {
						buff.append(percentFormatter.format(summVO.getFundTotalCompositeRate())).append(COMMA);
					} else {
						buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
					}
				} else {
					buff.append(unitsHeldFormatter.format(summVO.getFundTotalNumberOfUnitsHeld())).append(COMMA);
					buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
				}
				buff.append(summVO.getFundTotalBalance()).append(COMMA);
				buff.append(percentFormatter.format(summVO.getFundTotalPercentageOfTotal()*100.0d)).append(COMMA);
//				buff.append(percentFormatter.format(summVO.getEmployeeOngoingContributions()*100.0d)).append(COMMA);
				buff.append(percentFormatter.format(summVO.getEmployerOngoingContributions()*100.0d)).append(LINE_BREAK);

/*
				// next line is the Employee level
				buff.append("Employee assets").append(COMMA);
				if(summVO.getFundTotalNumberOfUnitsHeld()==0) {
					buff.append(COMMA);
					if (summVO.getEmployeeCompositeRate() > 0) {
						buff.append(percentFormatter.format(summVO.getEmployeeCompositeRate())).append(COMMA);
					} else {
						buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
					}
				} else {
					buff.append(unitsHeldFormatter.format(summVO.getEmployeeNumberOfUnitsHeld())).append(COMMA);
					buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
				}
				buff.append(summVO.getEmployeeBalance()).append(LINE_BREAK);

				// next line is the Employer level
				buff.append("Employer assets").append(COMMA);
				if(summVO.getFundTotalNumberOfUnitsHeld()==0) {
					buff.append(COMMA);
					if (summVO.getEmployerCompositeRate() > 0) {
						buff.append(percentFormatter.format(summVO.getEmployerCompositeRate())).append(COMMA);
					} else {
						buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
					}
				} else {
					buff.append(unitsHeldFormatter.format(summVO.getEmployerNumberOfUnitsHeld())).append(COMMA);
					buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
				}
				buff.append(summVO.getEmployerBalance()).append(LINE_BREAK);
*/
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- DefinedBenefitAccountAction");
		}
		return buff.toString();
	}
	
	protected String getTabName(){
		return TAB_NAME;
	}
	
	 /*@Autowired
	    private PSValidatorFWParticipantInput psValidatorFWParticipantInput;  

	    @InitBinder
	    protected void initBinder(HttpServletRequest request,
	    			ServletRequestDataBinder  binder) {
	    	binder.bind(request);
	    	binder.addValidators(psValidatorFWParticipantInput);
	    }*/
	
	
}



