package com.manulife.pension.ps.web.participant;


import java.io.IOException;
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
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeTotalsVO;
import com.manulife.pension.ps.web.transaction.LoanRepaymentDetailsReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWParticipantInput;


@Controller
@RequestMapping(value="/db")
@SessionAttributes({"participantAccountForm"})

public class DefinedBenefitAccountMoneyTypeSummaryController extends DefinedBenefitAccountCommonController {
	
	@ModelAttribute("participantAccountForm") 
	public  ParticipantAccountForm  populateForm() 
	{
		return new  ParticipantAccountForm();
		}
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateFormLoan() {
		return new LoanRepaymentDetailsReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantAccountMoneyTypeSummary.jsp");
		forwards.put("definedBenefitAccountMoneyTypeSummary","/participant/definedBenefitAccountMoneyTypeSummary.jsp");} 

	
	private String TAB_NAME = "DefinedBenefitAccountMoneyTypeSummary";

	public DefinedBenefitAccountMoneyTypeSummaryController()
	{
		super(DefinedBenefitAccountMoneyTypeSummaryController.class);
	}

	@RequestMapping(value ="/definedBenefitAccountMoneyTypeSummary/", method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);//if input forward not //available, provided default
	       }
		}
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
			
		 forward =super.doExecute( actionForm, request, response);
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	/**
	 * Custom versino for defined benefit that removes some data in the base version.
	 */
	
	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> DefinedBenefitAccountMoneyTypeSummaryAction");
		}

//		DecimalFormat unitsHeldFormatter = new DecimalFormat("#0.000000");
//		DecimalFormat percentFormatter = new DecimalFormat("#0.00");
//		DecimalFormat unitValueFormatter = new DecimalFormat("#0.00");


		boolean showLoans = true;
		if ( participantAccountVO.getParticipantAccountDetailsVO().getLoanAssets()==0 )
			showLoans = false;

		StringBuffer buff = new StringBuffer();

		buff.append("Money Type Summary").append(LINE_BREAK).append(LINE_BREAK);

		// Section 4 money type summary for the report
		buff.append("Money Types");
		if ( showLoans )
			buff.append(COMMA).append(" Total assets excluding loans($),Loan assets($)");
		else
			buff.append(COMMA).append(" Total assets($)");

		buff.append(LINE_BREAK).append(LINE_BREAK);

/*
		// Employee contributions
		buff.append("Employee contributions").append(COMMA);
		buff.append(participantAccountVO.getTotalEmployeeContributionsAssets());
		if ( showLoans )
			buff.append(COMMA).append(participantAccountVO.getTotalEmployeeContributionsLoanAssets());

		buff.append(LINE_BREAK);

		ParticipantFundMoneyTypeTotalsVO[] employeeMoneyTypeTotals = participantAccountVO.getEmployeeMoneyTypeTotals();

		for (int i=0; i<employeeMoneyTypeTotals.length; i++) {
			buff.append(employeeMoneyTypeTotals[i].getMoneyTypeName()).append(COMMA);
			buff.append(employeeMoneyTypeTotals[i].getBalance());
			if ( showLoans )
				buff.append(COMMA).append(employeeMoneyTypeTotals[i].getLoanBalance());

			buff.append(LINE_BREAK);
		}

		buff.append(LINE_BREAK);
*/

		// Employer contributions
		buff.append("Employer contributions").append(COMMA);
		buff.append(participantAccountVO.getTotalEmployerContributionsAssets());
		if ( showLoans )
			buff.append(COMMA).append(participantAccountVO.getTotalEmployerContributionsLoanAssets());

		buff.append(LINE_BREAK);

		ParticipantFundMoneyTypeTotalsVO[] employerMoneyTypeTotals = participantAccountVO.getEmployerMoneyTypeTotals();

		for (int i=0; i<employerMoneyTypeTotals.length; i++) {
			buff.append(employerMoneyTypeTotals[i].getMoneyTypeName()).append(COMMA);
			buff.append(employerMoneyTypeTotals[i].getBalance());
			if ( showLoans )
				buff.append(COMMA).append(employerMoneyTypeTotals[i].getLoanBalance());

			buff.append(LINE_BREAK);
		}

		buff.append(LINE_BREAK);

		// Total contributions
		buff.append("Total").append(COMMA);
		buff.append(participantAccountVO.getTotalContributionsAssets());
		if ( showLoans )
			buff.append(COMMA).append(participantAccountVO.getTotalContributionsLoanAssets());

		buff.append(LINE_BREAK);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- DefinedBenefitAccountMoneyTypeSummaryAction");
		}
		return buff.toString();
	}
	
	protected String getTabName(){
		return TAB_NAME;
	}

	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}

}


