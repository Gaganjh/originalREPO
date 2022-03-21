package com.manulife.pension.ps.web.pilot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.PilotException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.home.SelectContractController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractPilotList;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.content.GenericException;

/**
 * Pilot contract action
 * 
 * @author Steven Wang @ ManuLife
 */

@Controller
@RequestMapping(value ="/pilot")
public class PilotContractController extends PsController {
	@ModelAttribute("pilotContractForm")
	public PilotContractForm populateForm() {
		return new PilotContractForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/pilot/pilotContractPage.jsp");
		forwards.put("pilotContractPage","/pilot/pilotContractNoNavHeaderPage.jsp");
		forwards.put("cancel","redirect:/do/home/ChangeContract/");
	}

    /**
     * pilot contract jsp page
     */
    private final static String PILOT_CONTRACT_PAGE = "pilotContractPage";

    private final static String PILOT_CONTRACT_SEARCH = "search";

    private final static String PILOT_CONTRACT_CANCEL = "cancel";

    private final static String PILOT_CONTRACT_SAVE = "save";

    private final static String PILOT_CONTRACT_VIEW = "view";

    private final static String PILOT_CONTRACT_BACK = "back";

    public static final String FROM_SEARCH_CONTRACT_PAGE_ACTION = "/do/pilot/pilotContractNoNavHeader/";

    public static final String FROM_MANAGER_USER_PAGE_ACTION = "/do/pilot/pilotContractStandardHeader/";

    public PilotContractController() {
        super(SelectContractController.class);
    }

   
   @RequestMapping(value = {"/pilotContractNoNavHeader/","/pilotContractStandardHeader"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doExecute(@Valid @ModelAttribute("pilotContractForm") PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
	   
	   if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
       		
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       	}
       }

        String dispatchAction = null;
        if (request.getParameter("searchBtn") != null
                && request.getParameter("searchBtn").length() > 0) {

            dispatchAction = request.getParameter("searchBtn").trim();
        } else if (request.getParameter("cancelBtn") != null
                && request.getParameter("cancelBtn").length() > 0) {

            dispatchAction = request.getParameter("cancelBtn").trim();
        } else if (request.getParameter("saveBtn") != null
                && request.getParameter("saveBtn").length() > 0) {

            dispatchAction = request.getParameter("saveBtn").trim();

        } else if (request.getParameter("backBtn") != null
                && request.getParameter("backBtn").length() > 0) {

            dispatchAction = request.getParameter("backBtn").trim();

        } else {
            dispatchAction = PILOT_CONTRACT_VIEW;
        }

        if (PILOT_CONTRACT_VIEW.equals(dispatchAction)) {
            return viewAction( actionForm,bindingResult, request, response);
        }
        if (PILOT_CONTRACT_SEARCH.equals(dispatchAction)) {
            return searchAction( actionForm,bindingResult, request, response);
        }
        if (PILOT_CONTRACT_CANCEL.equals(dispatchAction)) {
            return cancelAction( actionForm,bindingResult, request, response);
        }
        if (PILOT_CONTRACT_SAVE.equals(dispatchAction)) {

            return saveAction( actionForm,bindingResult, request, response);
        }
        if (PILOT_CONTRACT_BACK.equals(dispatchAction)) {

            return cancelAction( actionForm, bindingResult, request, response);
        }
        return null;
    }

   
  /* @RequestMapping(value = {"/pilotContractNoNavHeader/","/pilotContractStandardHeader"},params={"action=back"}, method =  {RequestMethod.POST}) 
   public String doBack (@Valid @ModelAttribute("pilotContractForm") PilotContractForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
   	
   	if(bindingResult.hasErrors()){
   		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   		if(errDirect!=null){
   			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
   		}
   	}
       return forwards.get("cancel");
   }*/
   
    /**
     * Get pilot names form back end and show on the page
     */
     
    private String viewAction(PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

      //  PilotContractForm pform = (PilotContractForm) form;
        buildPilotNamesWithoutValues(actionForm);
        request.setAttribute("pilotViewBean", actionForm);
        setTargetAction(request);
        return forwards.get("pilotContractPage");
    }

    /**
     * search pilot contract action
     */
     
    private String searchAction(PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    /*@RequestMapping(value = {"/pilotContractNoNavHeader/","/pilotContractStandardHeader"},params={"action=search"}, method =  {RequestMethod.POST}) 
    public String searchAction (@Valid @ModelAttribute("pilotContractForm") PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)*/
    throws IOException,ServletException, SystemException {
   

        HttpSession session = request.getSession(false);
        Collection errors = new ArrayList();
       // PilotContractForm pilotForm = (PilotContractForm) form;

        // checking contract
        if ((actionForm.getContractNumber() == null)
                || (actionForm.getContractNumber().length() == 0)
                || !isValidContractNumber(session, actionForm.getContractNumber())) {
            // bad contract
            if ((actionForm.getContractNumber() == null)
                    || (actionForm.getContractNumber().length() == 0))
            {

                errors.add(new GenericException(6001));
            } else if (!isValidContractNumber(session, actionForm.getContractNumber())) {

                errors.add(new GenericException(1043));
            }
            request.setAttribute(Environment.getInstance().getErrorKey(), errors);
            buildPilotNamesWithoutValues(actionForm);

        } else {
            // good contract
            int contractNumber = Integer.parseInt(actionForm.getContractNumber());
            try {
                if (!ContractNumberNoMandatoryRule.getInstance().validate("contract number",
                        errors, actionForm.getContractNumber())) {
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                    buildPilotNamesWithoutValues(actionForm);

                } else if (!isBusinessConvertedContract(getContract(contractNumber))) {
                    errors.add(new GenericException(6003));
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                    buildPilotNamesWithoutValues(actionForm);
                } else {
                    ContractPilotList pilotContract = getContractServiceDelegate()
                            .getContractPilotList(contractNumber);
                    actionForm.setContractName(pilotContract.getContractName());
                    actionForm.setGoodContractNumber("good");
                    actionForm.setPilotTable(pilotContract.getPilotEnabledMap());
                }

            } catch (ContractNotExistException ce){
                errors.add(new GenericException(1043));
                request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                buildPilotNamesWithoutValues(actionForm);

            } catch (ApplicationException ae) {

                errors.add(new GenericException(Integer.parseInt(ae.getErrorCode())));
                request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                buildPilotNamesWithoutValues(actionForm);

            } catch (SystemException se) {
                if (!(se.getCause() instanceof ContractDoesNotExistException)) {
                    errors.add(new GenericException(1043));
                    request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                    buildPilotNamesWithoutValues(actionForm);
                } else {
                    throw se;
                }
            }
        }
        request.setAttribute("pilotViewBean", actionForm);
        setTargetAction(request);
        return forwards.get(PILOT_CONTRACT_PAGE);
    }

    /**
     * save pilot contract action
     */
    
    private String saveAction(PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
       // PilotContractForm pilotForm = (PilotContractForm) form;
        Collection errors = new ArrayList();
        // Get real model.Currently it is dummy data
        // buildPilotPageView(pilotForm);
        int contractNumber = Integer.parseInt(actionForm.getContractNumber());
        Map<String, Boolean> viewPilotValues = null;
        Map<String, Boolean> modelPilotValues = null;
        ContractPilotList pilotList = null;
        viewPilotValues = getPilotValuesFromRequest(request, actionForm.getPilotNames());
        try {
            pilotList = getContractServiceDelegate().getContractPilotList(contractNumber);
            modelPilotValues = pilotList.getPilotEnabledMap();
            viewPilotValues = getPilotValuesFromRequest(request, pilotList.getPilotNames());
            if (isPilotValuesChanged(viewPilotValues, modelPilotValues)) {
                updatePilotList(viewPilotValues, pilotList, request);
                getContractServiceDelegate().updateContractPilotList(pilotList);
                buildPilotNamesWithoutValues(actionForm);
                actionForm.setContractNumber(null);
                request.setAttribute("pilotViewBean", actionForm);
            } else {
                // data is not changed - error
                errors = new ArrayList();
                // errors.add(new GenericException(51613));
                errors.add(new GenericException(2157));// testing
                request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                actionForm.setGoodContractNumber("good");
                actionForm.setContractName(pilotList.getContractName());
                actionForm.setPilotTable(modelPilotValues);
                request.setAttribute("pilotViewBean", actionForm);
            }
        } catch (ApplicationException ae) {
            PilotException pe = (PilotException) ae;
            for (String pilotName : pe.getPilotNames()) {
                String[] params = new String[] { pilotName };
                errors.add(new GenericException(Integer.parseInt(ae.getErrorCode()), params));
            }
            request.setAttribute(Environment.getInstance().getErrorKey(), errors);
            // buildPilotNamesWithoutValues(pilotForm);
            actionForm.setGoodContractNumber("good");
            actionForm.setContractName(pilotList.getContractName());
            actionForm.setPilotTable(viewPilotValues);
            request.setAttribute("pilotViewBean", actionForm);
        }
        setTargetAction(request);
        return forwards.get(PILOT_CONTRACT_PAGE);
    }

    /**
     * cancel pilot contract action
     */
     
    private String cancelAction(PilotContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
        return forwards.get("cancel");

    }

    private Map<String, Boolean> getPilotValuesFromRequest(HttpServletRequest req,
            Collection<String> pilotNames) {
        Map<String, Boolean> pagePilotValues = new HashMap<String, Boolean>();
        for (String pname : pilotNames) {
            if (req.getParameter(pname) != null) {
                // checkbox is checked

                pagePilotValues.put(pname, Boolean.TRUE);

            } else {
                // checkbox is not checked

                pagePilotValues.put(pname, Boolean.FALSE);
            }

        }
        setTargetAction(req);
        return pagePilotValues;
    }

    private boolean isValidContractNumber(HttpSession session, String contractStr) {
        int contractNumber = 0;
        try {
            contractNumber = Integer.parseInt(contractStr);
        } catch (NumberFormatException ne) {
            return false;
        }
        return true;

    }

    private boolean isPilotValuesChanged(Map<String, Boolean> viewMap, Map<String, Boolean> modelMap) {

        Collection<String> pnames = modelMap.keySet();
        for (String pname : pnames) {
            if (!modelMap.get(pname).equals(viewMap.get(pname))) {
                return true;
            }

        }
        return false;

    }

    private void updatePilotList(Map<String, Boolean> viewMap, ContractPilotList pilotList,
            HttpServletRequest req) {
        Collection<String> pnames = pilotList.getPilotNames();
        Principal principal = getUserProfile(req).getPrincipal();

        pilotList.setUserName(principal.getUserName());
        pilotList.setPrincipalProfileId(principal.getProfileId());
        pilotList.setPrincipalFirstName(principal.getFirstName());
        pilotList.setPrincipalLastName(principal.getLastName());
        for (String pname : pnames) {

            pilotList.setPilotEnabled(pname, viewMap.get(pname));
        }

    }

    private ContractServiceDelegate getContractServiceDelegate() {
        return ContractServiceDelegate.getInstance();

    }

    private void buildPilotNamesWithoutValues(PilotContractForm pform) throws SystemException {
        pform.setGoodContractNumber(null);
        for (String pilotName : getContractServiceDelegate().getPilotList()) {
            pform.addPilotTableItem(pilotName, Boolean.FALSE);
        }
        ;

    }

    private Contract getContract(int contractNumber) throws ContractNotExistException, SystemException {
        Contract contract = null;
        contract = ContractServiceDelegate.getInstance().getContractDetails(contractNumber, 6);
        if (contract != null && contract.getCompanyName() == null) {
            contract = null;
        }
        return contract;
    }

    private boolean isBusinessConvertedContract(Contract contract) throws SystemException {
        return contract.isBusinessConverted();
    }

    /**
     * 
     * @param request
     */
    private void setTargetAction(HttpServletRequest request) {
        if (request.getParameter("from") != null && request.getParameter("from").length() > 0) {
            String fromPage = request.getParameter("from");
            if (Constants.SEARCH_CONTRACT_PAGE.equalsIgnoreCase(fromPage))
                request.setAttribute("targetAction", FROM_SEARCH_CONTRACT_PAGE_ACTION);
            else
                request.setAttribute("targetAction", FROM_MANAGER_USER_PAGE_ACTION);
        }
    }

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	//@Autowired
	//private PilotContractReportValidator pilotContractReportValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		//binder.addValidators(pilotContractReportValidator);
	}
}
