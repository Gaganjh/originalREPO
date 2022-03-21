package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

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
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeDetailVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.ps.web.transaction.LoanRepaymentDetailsReportForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.SvgifFund;


@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountMoneyTypeDetailsController extends ParticipantAccountCommonController {

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
		forwards.put("input","/participant/participantAccountMoneyTypeDetails.jsp");
		forwards.put("participantAccountMoneyTypeDetails","/participant/participantAccountMoneyTypeDetails.jsp");}

	public ParticipantAccountMoneyTypeDetailsController()
	{
		super(ParticipantAccountMoneyTypeDetailsController.class);
	}

	@RequestMapping(value ="/participantAccountMoneyTypeDetails/",  method =  {RequestMethod.GET}) 
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
	
	
	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDetailedDownloadData");
		}

		DecimalFormat unitsHeldFormatter = new DecimalFormat("#0.000000");
		DecimalFormat percentFormatter = new DecimalFormat("#0.00");
		DecimalFormat unitValueFormatter = new DecimalFormat("#0.00");
		
		List<SvgifFund> svgifFundList = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
		
		StringBuffer buff = new StringBuffer();

		buff.append("Money Type Details").append(LINE_BREAK).append(LINE_BREAK);		
		
		// Section 4 Money Type Details collection for the report
		buff.append("Investment option,Number of units,Unit value($) / interest rate,Balance Employee subtotal($), Balance Employer subtotal($),Balance($),Percentage of total(%)").append(LINE_BREAK);
	
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
				
				buff.append(summVO.getEmployeeBalance()).append(COMMA).append(summVO.getEmployerBalance()).append(COMMA);
				buff.append(summVO.getFundTotalBalance()).append(COMMA);
				buff.append(percentFormatter.format(summVO.getFundTotalPercentageOfTotal()*100.0d)).append(LINE_BREAK);
				
				boolean isSVGIFFund = svgifFundList.stream().anyMatch(o -> o.getFundId().equals(summVO.getFundId().trim()));				
				
				// next line is the Money Type Details level
				ParticipantFundMoneyTypeDetailVO[] moneyTypeDetails = summVO.getFundMoneyTypeDetails();
				
				for (int k=0; k < moneyTypeDetails.length; k++)
				{
					buff.append(moneyTypeDetails[k].getMoneyTypeName()).append(COMMA);
					
					if (summVO.getFundTotalNumberOfUnitsHeld()==0)
					{
						if(isSVGIFFund)
						{
							buff.append(COMMA);
							buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
						}
						else if (summVO.getFundTotalCompositeRate() > 0) 
							buff.append(COMMA).append(COMMA);
						else 
							buff.append(unitValueFormatter.format(moneyTypeDetails[k].getNumberOfUnitsHeld())).append(COMMA);
					} else {
						buff.append(unitsHeldFormatter.format(moneyTypeDetails[k].getNumberOfUnitsHeld())).append(COMMA);
						buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
					}
					
					if ( moneyTypeDetails[k].getMoneyType().equals("EE") )
						buff.append(moneyTypeDetails[k].getBalance()).append(COMMA);
					else
						buff.append(COMMA);
					
					if ( moneyTypeDetails[k].getMoneyType().equals("ER") )
						buff.append(moneyTypeDetails[k].getBalance()).append(COMMA);
					else
						buff.append(COMMA);
					
					buff.append(LINE_BREAK);
				}
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
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}



