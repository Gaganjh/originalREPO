package com.manulife.pension.ps.web.pif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.pif.obs.ConcreateObservable;
import com.manulife.pension.ps.web.pif.obs.ContributionsObserver;
import com.manulife.pension.ps.web.pif.obs.EligibilityObserver;
import com.manulife.pension.ps.web.pif.obs.ForfeituresObserver;
import com.manulife.pension.ps.web.pif.obs.GeneralInfoObserver;
import com.manulife.pension.ps.web.pif.obs.LoansObserver;
import com.manulife.pension.ps.web.pif.obs.MoneyTypeObserver;
import com.manulife.pension.ps.web.pif.obs.OtherPlanObserver;
import com.manulife.pension.ps.web.pif.obs.VestingObserver;
import com.manulife.pension.ps.web.pif.obs.WithdrawalsObserver;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;


/**
 * This Action class handles plan information confirmation page related actions
 * 
 * @author Vivek Lingesan
 */
@Controller
@RequestMapping(value ="/contract")
@SessionAttributes({"pifDataForm"})

public class ConfirmPIFDataController extends BasePIFDataController {

	@ModelAttribute("pifDataForm")
	public PIFDataForm populateForm() 
	{
		return new PIFDataForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/pic/confirmPIFData.jsp"); 
		forwards.put("default", "/contract/pic/confirmPIFData.jsp");
		forwards.put("print", "/contract/pic/confirmPIFData.jsp");}

	
    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(ConfirmPIFDataController.class);

    /**
     * {@inheritDoc}
     */
    
    @RequestMapping(value ="/pic/confirm/", method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("pifDataForm") PIFDataForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get("input");//if input forward not //available, provided default
        	}
        }  
  

    	logger.debug("Entry -> doDefault");
	
    	//PIFDataForm form = (PIFDataForm) actionForm;
        PIFDataUi pifDataUi = actionform.getPifDataUi();
        actionform.setConfirmMode();
        // Convert the types
		//ConcreateObservable observable = createObservableObjects(form.getPlanInfoVO());
		//observable.notifyFromBeanObservers(pifDataUi,form.getPlanInfoVO());        

		logger.debug("Exit -> doDefault");
		return forwards.get(ACTION_FORWARD_DEFAULT);	
    	
    }
 
    /**
     * Create the observable and observer objects.
     * 
     * @param planinfoVO
     * @return ConcreateObservable
     */
	private ConcreateObservable createObservableObjects(PlanInfoVO planinfoVO){
		ConcreateObservable observable = new ConcreateObservable();
		List<PIFMoneyType> permittedMoneyTypes = new ArrayList<PIFMoneyType>();
		permittedMoneyTypes.addAll(planinfoVO.getPifMoneyType().getPermittedEmployeeMoneyTypes());
		permittedMoneyTypes.addAll(planinfoVO.getPifMoneyType().getPermittedEmployerMoneyTypes());
		
		//set all the initial values to Observers
		GeneralInfoObserver generalInfoObserver = new GeneralInfoObserver(planinfoVO.getGeneralInformations());
		MoneyTypeObserver moneyTypeObserver=new MoneyTypeObserver(planinfoVO.getPifMoneyType());
		ContributionsObserver contributionsObserver = new ContributionsObserver(planinfoVO.getContributions());
		LoansObserver loansObserver = new LoansObserver(planinfoVO.getLoans());
		EligibilityObserver eligibilityObserver = new EligibilityObserver(planinfoVO.getEligibility());
		ForfeituresObserver forfeituresObserver = new ForfeituresObserver(planinfoVO.getForfeitures());
		VestingObserver vestingObserver = new VestingObserver(planinfoVO.getVesting());
		WithdrawalsObserver withdrawalsObserver = new WithdrawalsObserver(planinfoVO.getWithdrawals());
		OtherPlanObserver otherPlanObserver = new OtherPlanObserver(planinfoVO.getOtherInformation());
		
		//Add observers to observable
        observable.addObserver(generalInfoObserver);
        observable.addObserver(moneyTypeObserver);
        observable.addObserver(contributionsObserver);
        observable.addObserver(loansObserver); 
        observable.addObserver(eligibilityObserver); 
        observable.addObserver(forfeituresObserver); 
        observable.addObserver(vestingObserver); 
        observable.addObserver(withdrawalsObserver); 
        observable.addObserver(otherPlanObserver); 
        
        // invoke the update method to update the values based on List<PIFMoneyType> list
        observable.update(permittedMoneyTypes);
                        
        // Once updated get all the values back and set to main VO
        planinfoVO.setGeneralInformations(generalInfoObserver.getGeneralInformations());
        planinfoVO.setPifMoneyType(moneyTypeObserver.getPlanInfoMoneyTypeVO());
        planinfoVO.setContributions(contributionsObserver.getContributions());
        planinfoVO.setEligibility(eligibilityObserver.getEligibility());
        planinfoVO.setVesting(vestingObserver.getPlanInfoVestingVO());
        planinfoVO.setForfeitures(forfeituresObserver.getForfeitures());
        planinfoVO.setWithdrawals(withdrawalsObserver.getPlanInfoWithdrawalsVO());
        planinfoVO.setLoans(loansObserver.getLoans());
        planinfoVO.setOtherInformation(otherPlanObserver.getPlanOtherInfoVO());	
        
        return observable;
	}    
	
	/* (non-Javadoc)
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}