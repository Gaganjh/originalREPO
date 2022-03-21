package com.manulife.pension.bd.web.fundcheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.manulife.pension.bd.service.fundCheck.dao.FundCheckDAO;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.fundcheck.model.FundCheckDocFileRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocFileResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocListRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocListResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocumentInfo;
import com.manulife.pension.lp.bos.ereports.common.ContractDocumentNotFoundException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.content.GenericException;

/**
 * This class  is used for level1 version of fundcheck page.
 * @author Vanidha Rajendhiran
 *
 */
@Controller
@RequestMapping(value ="/fundcheck")
@SessionAttributes({"bdFundCheckLevel1Form"})

public class BDFundCheckLevel1Controller extends BDFundCheckBaseController {
	@ModelAttribute("bdFundCheckLevel1Form")
	public BDFundCheckLevel1Form populateForm() {
		return new BDFundCheckLevel1Form();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static final String DEFAULT = "default";
    private static final String SEARCHPDF = "searchPDF";
	private static final String SECWINDOWERROR = "secondaryWindowError";
    private static final String INPUT = "input";
    EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
    private static final String CONTRACT_NAME_TEXT="contractName";
    private static final String CONTRACT_NUMBER_TEXT="contractNumber";
	static {
		forwards.put(INPUT, "/fundcheck/fundCheckL1.jsp");
		forwards.put(DEFAULT, "/fundcheck/fundCheckL1.jsp");
		forwards.put(SEARCHPDF, "/fundcheck/fundCheckL1.jsp");
		forwards.put(SECWINDOWERROR, "/global/secondaryWindowError.jsp");
	}
    static final Comparator<FundCheckDocumentInfo> PDF_ORDER = new Comparator<FundCheckDocumentInfo>() {
//TODO SSA I don't particularly like how this comparison is written, it may be better to compare individually the year, season etc. One could think of a scenario where year is "", season is "2010FFL" for one and year is "2010" and season is "FFL" for this other. This case would result in a successfull compare even thought the values are different. This scenario is plausible as both year and season are strings.
//TODO SSA it is better to have this compare part of the FundCheckDocumentInfo object, in its equals method     
        public int compare(FundCheckDocumentInfo e1, FundCheckDocumentInfo e2) {
            String e1Info = "";
            String e2Info = "";
            if(e1 != null){
                e1Info = (e1.getYear() != null ? e1.getYear(): "")
                        +(e1.getSeason()!= null ? e1.getSeason(): "")
                        +(e1.getProducerCode() != null ? e1.getProducerCode() : "");
            }
            if(e2 != null){
                e2Info = (e2.getYear() != null ? e2.getYear(): "")
                        +(e2.getSeason()!= null ? e2.getSeason(): "")
                        +(e2.getProducerCode() != null ? e2.getProducerCode() : "");
            }
            return e1Info.compareTo(e2Info);
        }
    };
    
    static final Comparator<FundCheckDocumentInfo> PDF_YEAR_DESC_ORDER = new Comparator<FundCheckDocumentInfo>() {
                public int compare(FundCheckDocumentInfo e1, FundCheckDocumentInfo e2) {
                    String e1Info = "";
                    String e2Info = "";
                    if(e1 != null){
                        e1Info = (e1.getYear() != null ? e1.getYear(): "");
                    }
                    if(e2 != null){
                        e2Info = (e2.getYear() != null ? e2.getYear(): "");
                    }
                    return e2Info.compareTo(e1Info);
                }
            };    
 
   
    /**
     * This is invoked when loading the fundcheck page for the first time or when no other method is specified.
     */
    /**
     * @param actionForm
     * @param bindingResult
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/fundCheckL1", method =  RequestMethod.GET) 
    public String doDefault(@Valid @ModelAttribute("bdFundCheckLevel1Form") BDFundCheckLevel1Form actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
        if (logger.isDebugEnabled())
            logger.debug("entry -> doDefault");
        BDFundCheckLevel1Form fundCheckL1Form = (BDFundCheckLevel1Form) actionForm;
        fundCheckL1Form.setSearchType("");
        fundCheckL1Form.setSearchInput("");
        fundCheckL1Form.setFundCheckPDFAvailableForSearch(true);
        fundCheckL1Form.setShowSearchResults(false);
        fundCheckL1Form.setNoSearchResults(false);
        fundCheckL1Form.setAddMoreFilters(false);
        fundCheckL1Form.setNameMap(null);
        fundCheckL1Form.setShowCompanyId(false);
        //try {
        //    
        //    List resultProducerCodes = getProducerCodes(fundCheckL1Form, request);
        //    Iterator iterator = resultProducerCodes.iterator();
        //    List producerCodes = new ArrayList();
        //    while (iterator.hasNext()) {
        //        Long id = (Long) iterator.next();
        //        producerCodes.add(String.valueOf(id));
        //    }           
        //    setFundCheckInfo(mapping, fundCheckL1Form, request,producerCodes);
            BDFundCheckHelper bdFundCheckHelper = new BDFundCheckHelper();
            fundCheckL1Form.setSearchTypeMap(bdFundCheckHelper.getLevel2SearchTypeMap());
        /*} catch (ServiceUnavailableException sue) {
            logger.error("EReports service not available.", sue);
            request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, true);
            return mapping.findForward(fundCheckL1Form.getAction());
        } catch (SecurityServiceException sse) {
            List errors = new ArrayList();
            logger.error("Security service exception", sse);
            errors.add(new GenericException(Integer.parseInt(sse.getErrorCode())));
            setErrorsInRequest(request, errors);
            return mapping.findForward(fundCheckL1Form.getAction());
        }*/
        String forward=forwards.get(fundCheckL1Form.getAction());
        if (logger.isDebugEnabled())
            logger.debug("exit -> doDefault");
        return forward;
    }

    /**
     * This method retreives the fundcheck pdfs for the user entered search criteria.(contract number/contract name)
     * @param actionForm
     * @param bindingResult
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/fundCheckL1" ,params="action=searchPDF"   , method =  RequestMethod.POST) 
    public String doSearchPDF (@Valid @ModelAttribute("bdFundCheckLevel1Form") BDFundCheckLevel1Form actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
        if (logger.isDebugEnabled())
            logger.debug("entry -> doSearchPDF");
        List<GenericException> errors = new ArrayList<GenericException>();
        Map<String,String> nameMap = null;
       
        actionForm.setFundCheckPDFAvailableForSearch(true);
        actionForm.setShowSearchResults(false);
        actionForm.setNoSearchResults(false);
        actionForm.setAddMoreFilters(false);
        actionForm.setNameMap(null);
        
        //Basic validation on the form elements and if validation success then proceed for search.
        if(doValidateSearch(actionForm,request)){
            BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
            boolean searchNameList = false;
            String contractId = null;
            boolean searchResult = false;
            try{            
                String searchType = actionForm.getSearchType();
                String searchInput = actionForm.getSearchInput();
                String selectedId = actionForm.getSelectedId();
                searchNameList = actionForm.isSearchNameList();
                /** when searchNameList is true and searchType is contractName than it will search for 
                 * contract nameList, when searchNameList is false and searchType is contractName 
                 * than it will set the searchInput to contractName.
                 */
                if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY) && searchNameList){
                    if (logger.isDebugEnabled()){               
                        logger.debug("entry -> Get Contract Name List by using the serch input");
                    }
                    //Get the contract names and respective contract id.
                    String userRoleCode = userProfile.getBDPrincipal().getBDUserRole().getRoleType().getUserRoleCode();
                    String asstUserProfileId = null;
                    if(userRoleCode != null && userRoleCode.equalsIgnoreCase("BRKAST")){
                        asstUserProfileId = FundCheckDAO.getAssistantProfileIdForLevel2User(userProfile.getBDPrincipal().getProfileId());
                        nameMap = FundCheckDAO.getContractNamesForLevel2User(searchInput,Long.parseLong(asstUserProfileId));
                    }else{
                        nameMap = FundCheckDAO.getContractNamesForLevel2User(searchInput,userProfile.getBDPrincipal().getProfileId());
                    }
                    
                    /** This block will set the nameMap into the formBean when the nameMap is not null 
                     * and it consists of less than 35 names.And when the nameMap is not null and having 
                     * names greateer than 35 than set the addMoreFilter to true.
                     * when nameMap is null set nosearchResults to true. 
                    */
                    if(nameMap != null){
                        int nameMapSize = nameMap.size();                   
                        if(nameMapSize > 0 && nameMapSize <= BDConstants.NUMBER_MAX_RESULT){
                        	actionForm.setNameMap(nameMap);      
                            if(nameMapSize == 1){
                                //Search pdf by contractName
                                Set<String> selectedNameId = nameMap.keySet();
                                selectedId = (String) (selectedNameId.toArray())[0];
                                contractId = selectedId;
                                actionForm.setNameMap(null);
                                actionForm.setSearchInput(nameMap.get(selectedId));
                            }else if(nameMapSize <= BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY){
                            	actionForm.setNameListSize(nameMap.size());
                            }else{
                            	actionForm.setNameListSize(BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY);
                            }                       
                        }else if(nameMapSize > BDConstants.NUMBER_MAX_RESULT){
                        	actionForm.setAddMoreFilters(true);
                        	actionForm.setNameMap(null);
                        }else if(nameMapSize <= 0){
                        	actionForm.setNoSearchResults(true); 
                        	actionForm.setNameMap(null);
                        }
                    }
                }else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY)){
                    //Get Pdf by ContractNumber
                    if (logger.isDebugEnabled()){
                        logger.debug("entry -> Get Pdf by ContractNumber");
                    }
                    //Check for User Existance in the DB.
                    String userRoleCode = userProfile.getBDPrincipal().getBDUserRole().getRoleType().getUserRoleCode();
                    long userProfileId = userProfile.getBDPrincipal().getProfileId();
                    String asstUserProfileId = null;
                    if(userRoleCode != null && userRoleCode.equalsIgnoreCase("BRKAST")){
                        asstUserProfileId = FundCheckDAO.getAssistantProfileIdForLevel2User(userProfileId);
                        searchResult = FundCheckDAO.checkUserExistForLevel2User(actionForm.getSearchType(),actionForm.getSearchInput(),Long.parseLong(asstUserProfileId));
                    }else{
                        searchResult = FundCheckDAO.checkUserExistForLevel2User(actionForm.getSearchType(),actionForm.getSearchInput(),userProfileId);
                    }
                    if(searchResult){
                        contractId  = searchInput;
                    }

                }else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY) && !searchNameList){
                    //Get Pdf by ContractName
                    if (logger.isDebugEnabled()){
                        logger.debug("entry -> Get Pdf by ContractName");
                    }
                    contractId = selectedId;
                    actionForm.setNameMap(null);                       
                }       
                
                /** Search for the fund check document list based on the contractId.
                 * And when contractId is null than set no serach result 
                 */
                if (contractId != null) {
                    FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
                    FundCheckDocListResponse fundCheckResponse = null;
                    //Get the fund check document list.
                    fundCheckRequest.setContractNumber(contractId); 
                    fundCheckResponse = delegate.getContractFundCheckDocList(fundCheckRequest);             
                    
                    //Check for the FundCheck PDF Availabillity
                    FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse
                            .getFundCheckDocList();
                    if (fundCheckArray.length == 0) {
                    	actionForm.setFundCheckPDFAvailableForSearch(false);
                    } else {
                    	actionForm.setShowSearchResults(true);
                        List<FundCheckDocumentInfo> listRow = new ArrayList<FundCheckDocumentInfo>();
                        for (int i = 1; i <= fundCheckArray.length; i++) {
                            listRow.add(fundCheckArray[i - 1]);
                        }
                        actionForm.setSearchResultFundCheckInfo(listRow);  
                    }
                }else if(!searchNameList){
                    //When ContractId is null and searchNameList is false than set no result found.
                    if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY)){
                    	actionForm.setFundCheckPDFAvailableForSearch(false);
                    }else{
                    	actionForm.setNoSearchResults(true);
                    }
                }
                if(errors != null && errors.size()>0){
                    setErrorsInRequest(request, errors);                    
                }
                    
            } catch (ServiceUnavailableException sue) {
                logger.error("EReports service not available.", sue);
                errors.add(new GenericException(BDErrorCodes.REPORT_SERVICE_UNAVAILABLE));
                setErrorsInRequest(request, errors);
                request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, true);             
            } 
        }else{
            // set no search found when validation fails.
            if(actionForm.isNoSearchResults() != true){
            	actionForm.setNoSearchResults(false);
            }            
            actionForm.setNameMap(null);
        }
        String forward=forwards.get(actionForm.getAction());
        if (logger.isDebugEnabled())
            logger.debug("exit -> doSearchPDF");
        return forward;
    }
    
    /**
     * @param actionForm
     * @param bindingResult
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/fundCheckL1", params="action=openPDF" , method =  RequestMethod.POST) 
    public String doOpenPDF (@Valid @ModelAttribute("bdFundCheckLevel1Form") BDFundCheckLevel1Form actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
		String forward = super.doOpenPDF(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    /**
     * This method validates the search input given by the user.
     * @param actionForm
     * @param request
     * @return
     */
    private boolean doValidateSearch(AutoForm actionForm, 
            HttpServletRequest request){
        if (logger.isDebugEnabled()){
            logger.debug("entry -> doValidateSearch");
        }
        boolean isValidSearchCriteria=true;
        
        BDFundCheckLevel1Form fundCheckL1Form = (BDFundCheckLevel1Form) actionForm;
        fundCheckL1Form.setNoSearchResults(false);
        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

        if(StringUtils.isEmpty(fundCheckL1Form.getSearchType()) || StringUtils.isEmpty(fundCheckL1Form.getSearchInput())){
            /**
             * 1. Validate the Search Type and Search Input field values are not empty.
             */
            GenericException exception = new GenericException(BDErrorCodes.NO_SEARCH_TEXT_ENTERED);
            errorMessages.add(exception);
        }else if(CONTRACT_NAME_TEXT.equalsIgnoreCase(fundCheckL1Form.getSearchType())){
            /**
             * 2. Validate the Contract Name filter value is not numeric 
             * and not less than 3 Characters.
             */
            if (StringUtils.trimToNull(fundCheckL1Form.getSearchInput()) != null
                    && (StringUtils.trimToNull(fundCheckL1Form.getSearchInput()).length() < 3)) {
                GenericException exception = new GenericException(
                        BDErrorCodes.ENTER_ATLEAST_THREE_CHARS);
                errorMessages.add(exception);
            }
        }else if(CONTRACT_NUMBER_TEXT.equalsIgnoreCase(fundCheckL1Form.getSearchType())){
            if(!StringUtils.isNumeric(fundCheckL1Form.getSearchInput())){
                fundCheckL1Form.setNoSearchResults(true);
            }
        }
        if(errorMessages.size() > 0){   
            setErrorsInRequest(request, errorMessages);
            isValidSearchCriteria=false;
        }else if(fundCheckL1Form.isNoSearchResults() == true){
            isValidSearchCriteria=false;
        }
        if (logger.isDebugEnabled()){
            logger.debug("exit -> doValidateSearch");
        }
        return isValidSearchCriteria;
    }
    /**
     * This method sets the FundCheck PDF info to the form. This will be
     * executed when fundcheck page is loaded for the first time.
     * 
     * @param mapping
     * @param form
     * @param request
     * @throws ServiceUnavailableException
     * @throws SystemException
     */
    private void setFundCheckInfo(
            BDFundCheckLevel1Form fundCheckL1Form, HttpServletRequest request,List<String> producerCodes)
            throws ServiceUnavailableException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> setFundCheckInfo");
        }
        if (producerCodes != null && producerCodes.size() != 0) {
            String[] producerArray=producerCodes.toArray(new String[0]);
            FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
            fundCheckRequest.setProducerCode(producerArray);        
            FundCheckDocListResponse fundCheckResponse = delegate
                    .getProducerFundCheckDocList(fundCheckRequest);
            FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse
                    .getFundCheckDocList();
            if (fundCheckArray.length > 0) {
                fundCheckL1Form.setFundCheckPDFAvailable(true);
                Arrays.sort(fundCheckArray, PDF_ORDER);
                Arrays.sort(fundCheckArray,PDF_YEAR_DESC_ORDER);
                //Block of code added to check for multiple producer code 
                if(producerCodes.size() >= 2){
                    fundCheckL1Form.setShowProducerCode(true);
                }
                //Block of code added to check for different version of PDF files,using company code 
                if (fundCheckArray.length >= 2) {
                    String companyId = null;
                    boolean USAFlag = false;
                    boolean NYFlag = false;
                    for (int i = 0; i < fundCheckArray.length; i++) {
                        companyId = fundCheckArray[i].getCompanyId().trim();
                        if(companyId.equalsIgnoreCase("USA")){
                            USAFlag = true;
                        }else if(companyId.equalsIgnoreCase("NY")){
                            NYFlag = true;
                        }
                        if(USAFlag && NYFlag){
                            fundCheckL1Form.setShowCompanyId(true);
                            break;
                        }
                    }
                }
                //create Rows Of FundCheckInfo and set in formBean
                fundCheckL1Form.setFundCheckInfo(createRowsOfFundCheckInfo(fundCheckArray));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> setFundCheckInfo");
        }
    }

    /**
     * This method returns a list of associated producer codes with the broker
     * 
     * @param actionForm
     * @param request
     * @return
     * @throws SystemException
     * @throws SecurityServiceException
     */
    @SuppressWarnings("unchecked")
    private List getProducerCodes(BDFundCheckLevel1Form actionForm,
            HttpServletRequest request) throws SystemException,
            SecurityServiceException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> getProducerCodes");
        BDPrincipal user = BDSessionHelper.getUserProfile(request)
                .getBDPrincipal();
        if(user != null) {
            ExtendedBDExtUserProfile profile = BDUserSecurityServiceDelegate
                    .getInstance().getExtendedBDExtUserProfile(user);
            List producerCodes = null;
            ExtendedBrokerUserProfile brokerUserProfile = null;
            if (profile!=null && (profile.getRoleType().compareTo(BDUserRoleType.FirmRep) == 0)) {
                brokerUserProfile = (ExtendedBrokerUserProfile) profile;
            }
            if (profile!=null && (profile.getRoleType().compareTo(BDUserRoleType.FinancialRepAssistant) == 0)) {
                ExtendedBrokerAssistantUserProfile brokerAssistantUserProfile = (ExtendedBrokerAssistantUserProfile) profile;
                brokerUserProfile = brokerAssistantUserProfile.getParentBroker();
            }
            if( brokerUserProfile!=null){
                List<BrokerEntityAssoc> brokerEntities = brokerUserProfile
                    .getActiveBrokerEntities();
                if (brokerEntities != null) {
                    BDFundCheckHelper bdFundCheckHelper = new BDFundCheckHelper();
                    producerCodes = bdFundCheckHelper
                            .getAssociatedProducerCodes(brokerEntities);
                }
            }else{
                logger.error("It wasn't invoked from the right context.");
                throw new SystemException("Error - It wasn't invoked from the right context."); 
            }
            if (logger.isDebugEnabled())
                logger.debug("exit -> getProducerCodes");
            return producerCodes;
        }else{
            logger.error("Error in retrieving userProfile info.");
            throw new SystemException("Error in retrieving userProfile info.");         
        }
    }

    /**
     * This method retrieves the fundcheck pdf from eReports.
     * 
     * @param mapping
     * @param form
     * @param request
     * @return a FundCheckDocFileResponse
     * @throws ServiceUnavailableException
     * @throws SystemException
     * @throws ContractDocumentNotFoundException
     */
    protected FundCheckDocFileResponse getFundCheckPDF(
            AutoForm form, HttpServletRequest request)
            throws ServiceUnavailableException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getFundCheckPDF");
        }
        BDFundCheckLevel1Form fundCheckL1Form = (BDFundCheckLevel1Form) form;
        FundCheckDocumentInfo info = new FundCheckDocumentInfo();       
        String selectedPDF = fundCheckL1Form.getSelectedPDF();
        String selectedPDFCode = StringUtils.trim(String.valueOf(fundCheckL1Form
                .getSelectedPDFCode()));
        String season = StringUtils.trim(fundCheckL1Form.getSelectedSeason());
        String year = StringUtils.trim(fundCheckL1Form.getSelectedYear());
        String companyId = StringUtils.trim(fundCheckL1Form.getSelectedCompanyId());
		String language = StringUtils.trim(fundCheckL1Form.getSelectedLanguage());
		String participantInd = StringUtils.trim(fundCheckL1Form.getParticipantNoticeInd());
        info.setSeason(season);
        info.setYear(year);
        info.setCompanyId(companyId);
		info.setLanguage(language);
		info.setParticipantNoticeInd(participantInd);
        FundCheckDocFileRequest fundCheckFileRequest = new FundCheckDocFileRequest();
        FundCheckDocFileResponse fundCheckDocFileResponse = null;
        
        if(StringUtils.isNotBlank(fundCheckL1Form.getSelectedSeason()) 
				&& StringUtils.isNotBlank(fundCheckL1Form.getSelectedYear())) {
	        //Get the Contract or Producer PDF file that the user has requested.
	        if(selectedPDF.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY)){
	            info.setContractNumber(selectedPDFCode);
	            fundCheckFileRequest.setContractNumber(selectedPDFCode);
	            fundCheckFileRequest.setReportKeys(new FundCheckDocumentInfo[] { info });
	            if (validateFileName(selectedPDFCode, season, year, true)) {
	            	fundCheckDocFileResponse = delegate.getContractFundCheckDocFile(fundCheckFileRequest);    
	            }
	        }else if(selectedPDF.equals(BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_KEY)){            
	            info.setProducerCode(selectedPDFCode);
	            fundCheckFileRequest.setProducerCode(selectedPDFCode);
	            fundCheckFileRequest.setReportKeys(new FundCheckDocumentInfo[] { info });
	            if (validateFileName(selectedPDFCode, season, year, false)) {
	            	fundCheckDocFileResponse = delegate.getProducerFundCheckDocFile(fundCheckFileRequest);
	            }
	        }   
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getFundCheckPDF");
        }
        return fundCheckDocFileResponse;
    }
    
    private boolean validateFileName(String inputNumber, String season,
			String year, boolean isContractNumber) throws ServiceUnavailableException, SystemException {
		// TODO Auto-generated method stub
		
		boolean validFile = false;
		FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
		FundCheckDocListResponse fundCheckResponse = null;
		if (isContractNumber) {
			fundCheckRequest.setContractNumber(inputNumber);
			fundCheckResponse = delegate
					.getContractFundCheckDocList(fundCheckRequest);
		} else {
			String[] producerCode = {inputNumber};
			fundCheckRequest.setProducerCode(producerCode);
			fundCheckResponse = delegate.getProducerFundCheckDocList(fundCheckRequest);
		}
		
		FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse
				.getFundCheckDocList();
		if (fundCheckArray.length > 0) {
			for (int i = 0; i < fundCheckArray.length; i++) {
				FundCheckDocumentInfo info = fundCheckArray[i];
				if(info.getSeason().trim().equalsIgnoreCase(season) && info.getYear().trim().equals(year)){
					validFile = true;
					break;
				}
			}
		}
		return validFile;
	}
    
    /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */ 
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

	
}
