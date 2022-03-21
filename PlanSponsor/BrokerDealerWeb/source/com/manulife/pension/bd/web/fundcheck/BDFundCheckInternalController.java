package com.manulife.pension.bd.web.fundcheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping( value = "/fundcheck")
@SessionAttributes({"bdFundCheckInternalForm"})

public class BDFundCheckInternalController extends BDFundCheckBaseController {
	@ModelAttribute("bdFundCheckInternalForm") 
	public BDFundCheckInternalForm populateForm() 
	{
		return new BDFundCheckInternalForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	private static final String DEFAULT = "default";
    private static final String INPUT = "input";
	static{
		forwards.put(INPUT,"/fundcheck/fundCheckInternal.jsp");
		forwards.put(DEFAULT,"/fundcheck/fundCheckInternal.jsp");
	}
	EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
	private static final String CONTRACT_NAME_TEXT="contractName";
	private static final String CONTRACT_NUMBER_TEXT="contractNumber";
//TODO SSA I don't particularly like how this comparison is written, it may be better to compare individually the year, season etc. One could think of a scenario where year is "", season is "2010FFL" for one and year is "2010" and season is "FFL" for this other. This case would result in a successfull compare even thought the values are different. This scenario is plausible as both year and season are strings.
//TODO SSA it better to have this compare part of the FundCheckDocumentInfo object, in its equals method
	static final Comparator<FundCheckDocumentInfo> PDF_ORDER_BY_YEAR = new Comparator<FundCheckDocumentInfo>() {
    	public int compare(FundCheckDocumentInfo firstFundCheckDoc, FundCheckDocumentInfo secondFundCheckDoc) {
    		String firstFundCheckDocInfo = "";
    		String secondFundCheckDocInfo = "";
    		
    		if(firstFundCheckDoc != null){
	    		firstFundCheckDocInfo = (firstFundCheckDoc.getYear() != null ? firstFundCheckDoc.getYear(): "");
    		}
    		if(secondFundCheckDoc != null){
    			secondFundCheckDocInfo = (secondFundCheckDoc.getYear() != null ? secondFundCheckDoc.getYear(): "");
    		}
           
    		return secondFundCheckDocInfo.compareTo(firstFundCheckDocInfo);
    	}
    };
	
    static final Comparator<FundCheckDocumentInfo> PDF_ORDER_BY_SEASON = new Comparator<FundCheckDocumentInfo>() {
    	public int compare(FundCheckDocumentInfo firstFundCheckDoc, FundCheckDocumentInfo secondFundCheckDoc) {
    		String firstFundCheckDocInfo = "";
    		String secondFundCheckDocInfo = "";
    		
    		if(firstFundCheckDoc != null){
	    		firstFundCheckDocInfo =(firstFundCheckDoc.getSeason()!= null ? firstFundCheckDoc.getSeason(): "");
    		}
    		if(secondFundCheckDoc != null){
    			secondFundCheckDocInfo =(secondFundCheckDoc.getSeason()!= null ? secondFundCheckDoc.getSeason(): "");
    		}
           
    		return firstFundCheckDocInfo.compareTo(secondFundCheckDocInfo);
    	}
    };
    
   @RequestMapping(value = "/fundCheckInternal/",  method = RequestMethod.GET) 
    public String doDefault(@Valid @ModelAttribute( "bdFundCheckInternalForm") BDFundCheckInternalForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
		if (logger.isDebugEnabled())
			logger.debug("entry -> doDefault");
		
		actionForm.setSearchType("");
		actionForm.setSearchInput("");
		actionForm.setShowSearchResults(false);
    	actionForm.setFundCheckPDFAvailable(true);
    	actionForm.setNoSearchResults(false);
    	actionForm.setAddMoreFilters(false);
    	actionForm.setNameMap(null);
		BDFundCheckHelper bdFundCheckHelper = new BDFundCheckHelper();
		actionForm.setSearchTypeMap(bdFundCheckHelper.getInternalSearchTypeMap());
		actionForm.setSearchTypeMapL2(bdFundCheckHelper.getLevel2SearchTypeMap());
		if (logger.isDebugEnabled())
			logger.debug("exit -> doDefault");
		return forwards.get(actionForm.getAction());
	}
	
	/**
	 * 
	 * This method is used to process the Search for the FundCheck PDF
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */	
  @RequestMapping(value ="/fundCheckInternal/" ,params="action=pdfSearch"  , method =  RequestMethod.POST) 
   public String doPdfSearch (@Valid @ModelAttribute("bdFundCheckInternalForm") BDFundCheckInternalForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws  SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(INPUT);
			}
		}
		
		if (logger.isDebugEnabled()){
			logger.debug("entry -> doPdfSearch");
		}
		
		List<GenericException> errors = new ArrayList<GenericException>(); 
    	String producerCode = null;
    	String contractId = null;
    	Map<String,String> nameMap = null;
    	boolean searchNameList;
    	boolean searchResult = false;
    	actionForm.setSearchInternalIndicator(false);
    	actionForm.setShowSearchResults(false);
    	actionForm.setFundCheckPDFAvailable(true);
    	actionForm.setNoSearchResults(false);
    	actionForm.setAddMoreFilters(false);
    	actionForm.setNameMap(null);
    	actionForm.setShowCompanyId(false);
    	
		//Basic validation on form elements and if validation success then proceed for search.
		if(doValidateSearch(actionForm,request)){    
            try{
            	String searchType = actionForm.getSearchType();
            	String searchInput = actionForm.getSearchInput();
            	String selectedId = actionForm.getSelectedId();
            	searchNameList = actionForm.isSearchNameList();
		    	/** when searchNameList is true and searchType is contractName than it will search for 
		    	 * contract nameList, when searchNameList is true and searchType is finRepName 
		    	 * than it will search for FR nameList,when searchNameList is false and searchType 
		    	 * is contractName than it will set the searchInput to contractName, when searchNameList 
		    	 * is false and searchType is finRepName than it will set the searchInput to producerCode.
		    	 */
            	if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY) && searchNameList){
            		if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get contractName List");
            		}
            		//Get the Contract names and respective contract id
            		nameMap = FundCheckDAO.getContractNamesForInternalUser(searchInput);
 
        			/** This block will set the nameMap into the formBean when the nameMap is not null 
        			 * and it consists of less than 35 names.And when the nameMap is not null and having 
        			 * names greateer than 35 than set the addMoreFilter to true.
        			 * when nameMap is null set nosearchResults to true. 
            		*/
            		if(nameMap != null && (nameMap.size() > 0 && nameMap.size() <= BDConstants.NUMBER_MAX_RESULT)){
            			actionForm.setNameMap(nameMap);    
            			actionForm.setNoSearchResults(false);
            			if(nameMap.size() == 1){
                    		//Search pdf by contractName
            				Set<String> selectedNameId = nameMap.keySet();
            				searchInput = (String) (selectedNameId.toArray())[0];
                			contractId  = searchInput;
                			actionForm.setNameMap(null);
                			actionForm.setSearchInput(nameMap.get(searchInput));
		    			}else if(nameMap.size() <= BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY){
            				actionForm.setNameListSize(nameMap.size());
            			}else{
            				actionForm.setNameListSize(BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY);
            			}
            		}else if(nameMap != null && (nameMap.size() > BDConstants.NUMBER_MAX_RESULT)){
            			actionForm.setAddMoreFilters(true);
            			actionForm.setNameMap(null);
            		}else if(nameMap != null && nameMap.size() <= 0){
            			actionForm.setNoSearchResults(true);
            			actionForm.setNameMap(null);
            		}
            		actionForm.setShowSearchResults(false);
            	}else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_FR_NAME_KEY) && searchNameList){
    		    	if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get finRepName List");
            		}
            		//Get the FR names and respective producer code in map
            		nameMap = FundCheckDAO.getFRNamesForInternalUser(searchInput);
            		
            		if(nameMap != null && (nameMap.size() > 0 && nameMap.size() <= BDConstants.NUMBER_MAX_RESULT)){
            			actionForm.setNameMap(nameMap);  
            			actionForm.setNoSearchResults(false);
            			if(nameMap.size() == 1){
            				//Search pdf by finRepName
            				Set<String> selectedNameId = nameMap.keySet();
            				searchInput = (String) (selectedNameId.toArray())[0];
                			producerCode=searchInput; 
                			actionForm.setNameMap(null);
                			actionForm.setSearchInput(nameMap.get(searchInput));
		    			}else if(nameMap.size() <= BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY){
            				actionForm.setNameListSize(nameMap.size());
            			}else{
            				actionForm.setNameListSize(BDConstants.NUMBER_MAX_RESULT_TO_DISPLAY);
            			}
            		}else if(nameMap != null && (nameMap.size() > BDConstants.NUMBER_MAX_RESULT)){
            			actionForm.setAddMoreFilters(true);
            			actionForm.setNoSearchResults(false);
            			actionForm.setNameMap(null);
            		}else if(nameMap != null && nameMap.size() <= 0){
            			actionForm.setNoSearchResults(true); 
            			actionForm.setAddMoreFilters(false);
            			actionForm.setNameMap(null);
            		}
            		actionForm.setShowSearchResults(false);
            	}else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_KEY)){
            		if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get pdf by producerCode");
            		}
            		//Check for User Existance for the Internal User.
            		searchResult = FundCheckDAO.checkUserExistForInternalUser(searchType, searchInput);
            		if(searchResult){
            			producerCode=searchInput;
            		}
            	}else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_FR_NAME_KEY) && !searchNameList){
            		if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get pdf by finRepName");
            		}
        			searchInput = selectedId;
        			actionForm.setNameMap(null);
        			producerCode=searchInput; 
            	}else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY)){
            		if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get pdf by contractNumber");
            		}
            		//Check User Existance for Internal User.
            		searchResult = FundCheckDAO.checkUserExistForInternalUser(searchType, searchInput);
            		if(searchResult){
            			contractId  = searchInput;
            		}
            	}else if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY) && !searchNameList){
            		//Search pdf by contractName
            		if (logger.isDebugEnabled()){
            			logger.debug("entry -> Get pdf by contractName");
            		}
        			searchInput = selectedId;
        			actionForm.setNameMap(null);
        			contractId  = searchInput;
            	}
            	
            	/** Search for the fund check document list based on the input 
            	 * i.e., either by producerCode or by contractId.
            	 * And when producerCode or contractId is null than set no serach result 
            	 */
            	if (producerCode != null  || contractId != null) {            		
        			FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
        			FundCheckDocListResponse fundCheckResponse = null;
        			//Get the fund check document list.
            		if ((producerCode != null && producerCode.length() > 0)){
            			//Get pdf by producer_code
            			String[] producerArray= {producerCode};
            			fundCheckRequest.setProducerCode(producerArray);   
            			fundCheckResponse = delegate.getProducerFundCheckDocList(fundCheckRequest);
            		}else { 
            			//Get pdf by contractId
            			fundCheckRequest.setContractNumber(contractId); 
                		fundCheckResponse = delegate.getContractFundCheckDocList(fundCheckRequest);
            		}
        			
            		//Check for the FundCheck PDF Availabillity
        			FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse.getFundCheckDocList();
        			if (fundCheckArray.length == 0) {
        				actionForm.setShowSearchResults(false);
        				actionForm.setFundCheckPDFAvailable(false);
        			} else {
        				actionForm.setShowSearchResults(true);
        				actionForm.setNoSearchResults(false);
        				actionForm.setFundCheckPDFAvailable(true);
        				
        				if (producerCode != null){
            				//Sort the fundCheckArray based on pdf display order.
        					Arrays.sort(fundCheckArray, PDF_ORDER_BY_SEASON);
        					Arrays.sort(fundCheckArray, PDF_ORDER_BY_YEAR);
	        		   	}
        				
        				//create Rows Of FundCheckInfo and set in formBean
        				actionForm.setSearchResultFundCheckInfo(createRowsOfFundCheckInfo(fundCheckArray));
        				//Block of code added to check for different version of PDF files,using company code
        				if (fundCheckArray.length >= 2) {
        					Set<String> theSet = new HashSet<String>();
	    					for (int i = 1; i <= fundCheckArray.length; i++) {
	    						theSet.add(fundCheckArray[i - 1].getCompanyId());
	    					}
    						if(theSet.size() >= 2)
    						{
    							actionForm.setShowCompanyId(true);
    						}					

        				}        				
        			}
        		}else if(!searchNameList){
        			//When producerCode or ContractId is null than set no result found.
        			actionForm.setShowSearchResults(false);
        			if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY) || searchType.equals(BDConstants.FUNDCHECK_INPUT_FR_NAME_KEY)){
        				actionForm.setFundCheckPDFAvailable(false);
        			}else{
            			actionForm.setNoSearchResults(true);
        			}
        		}            	
            	if(errors != null && errors.size()>0){
            		setErrorsInRequest(request, errors);
            		actionForm.setShowSearchResults(false);
            	}
            	
            }catch(ServiceUnavailableException e){
                logger.error("Error occured during doPdfSearch()",e);
       			errors.add(new GenericException(BDErrorCodes.REPORT_FILE_NOT_FOUND));
                setErrorsInRequest(request, errors);
            }catch(Exception exception){
                logger.error("Error occured during doPdfSearch()",exception);
    			throw new SystemException(exception,
				"Error in retrieving FundCheck documents info.");
            } 
        }else{
        	actionForm.setShowSearchResults(false);
        	actionForm.setNameMap(null);
        }
		String forward = forwards.get(DEFAULT);
		if (logger.isDebugEnabled()){
			logger.debug("exit -> doPdfSearch");
		}
		return forward;		
	}
	
	

	/**
	 * This method retreives the fundcheck external pdfs for the user entered search criteria.(contract number/contract name)
	 * @param actionForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
 @RequestMapping(value ="/fundCheckInternal", params="action=searchPDF" , method =  RequestMethod.POST) 
  public String doSearchPDF (@Valid @ModelAttribute("bdFundCheckInternalForm") BDFundCheckInternalForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
  throws SystemException {
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
    	
    	actionForm.setSearchInternalIndicator(true);
    	actionForm.setShowSearchResults(false);
    	actionForm.setFundCheckPDFAvailable(true);
    	actionForm.setNoSearchResults(false);
    	actionForm.setAddMoreFilters(false);
    	actionForm.setNameMap(null);
    	actionForm.setShowCompanyId(false);
    	
    	
    	//Basic validation on the form elements and if validation success then proceed for search.
		if(doValidateSearchPdf(actionForm, request)){
			BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
	    	boolean searchNameList = false;
	    	String contractId = null;
	    	boolean searchResult = false;
			try{			
		    	String searchType = actionForm.getSearchTypeL2();
		    	String searchInput = actionForm.getSearchInputL2();
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
            		nameMap = FundCheckDAO.getContractNamesForInternalUser(searchInput);
            		
		    		
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
            		searchResult = FundCheckDAO.checkUserExistForInternalUser(actionForm.getSearchTypeL2(),actionForm.getSearchInputL2());
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
						actionForm.setSearchResultFundCheckInfoL2(listRow);	
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
		String forward=forwards.get(DEFAULT);
		if (logger.isDebugEnabled())
			logger.debug("exit -> doSearchPDF");
		return forward;
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
		BDFundCheckInternalForm internalFundCheckForm = (BDFundCheckInternalForm) form;
		FundCheckDocumentInfo info = new FundCheckDocumentInfo();
		String searchType = "";
		FundCheckDocFileResponse fundCheckDocFileResponse = null;
		/*Search Internal Indicator decides which search 
		 * menu returned result out of two search types 
		 * for fund check available in the page. A False 
		 * Value implies the same was searched from the 
		 * right side menu. CL #133770*/
		boolean searchInternal = internalFundCheckForm.isSearchInternalIndicator();
		if(internalFundCheckForm != null){
			if (searchInternal) {
				searchType = internalFundCheckForm.getSearchTypeL2();
			} else {
				searchType = internalFundCheckForm.getSearchType();
			}
			String selectedPDF = StringUtils.trim(String.valueOf(internalFundCheckForm
					.getSelectedPDF()));
			String season = StringUtils.trim(internalFundCheckForm.getSelectedSeason());
			String year = StringUtils.trim(internalFundCheckForm.getSelectedYear());
			String companyId = StringUtils.trim(internalFundCheckForm.getSelectedCompanyId());
			String language = StringUtils.trim(internalFundCheckForm.getSelectedLanguage());
			String participantInd = StringUtils.trim(internalFundCheckForm.getParticipantNoticeInd());
			info.setSeason(season);
			info.setYear(year);
			info.setCompanyId(companyId);
			info.setLanguage(language);
			info.setParticipantNoticeInd(participantInd);
			FundCheckDocFileRequest fundCheckFileRequest = new FundCheckDocFileRequest();
		
			if(StringUtils.isNotBlank(internalFundCheckForm.getSelectedSeason()) 
					&& StringUtils.isNotBlank(internalFundCheckForm.getSelectedYear())) {
				//Get the Contract or Producer PDF file that the user has requested.
				if(searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY) || searchType.equals(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY)){
					info.setContractNumber(selectedPDF);
					fundCheckFileRequest.setContractNumber(selectedPDF);
					fundCheckFileRequest.setReportKeys(new FundCheckDocumentInfo[] { info });
					if (validateFileName(selectedPDF, season, year, true)) {
						 fundCheckDocFileResponse = delegate.getContractFundCheckDocFile(fundCheckFileRequest);
					}
				}else{			
					info.setProducerCode(selectedPDF);
					fundCheckFileRequest.setProducerCode(selectedPDF);
					fundCheckFileRequest.setReportKeys(new FundCheckDocumentInfo[] { info });
					if (validateFileName(selectedPDF, season, year, false)) {
						fundCheckDocFileResponse = delegate.getProducerFundCheckDocFile(fundCheckFileRequest);
					}
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
	
	/**
	 *
	 * This method validates the mandatory fields before processing the search. 
	 * @param actionForm
	 * @param request
	 * @return
	 */
//TODO SSA this method looks awfully similar to the one in BDFundCheckLevel2Action. Can they be unified in a superclass of this and BDFundCheckLevel2Action?	
	private boolean doValidateSearch(BDFundCheckInternalForm internalFundCheckForm, 
			HttpServletRequest request){
		if (logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidateSearch");
		}		
		boolean isValidSearchCriteria=true;
		internalFundCheckForm.setNoSearchResults(false);

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		if(StringUtils.isEmpty(internalFundCheckForm.getSearchType()) ||
				StringUtils.isEmpty(internalFundCheckForm.getSearchInput())){
	        /**
	         * 1. Validate the Search Type and Search Input field filter value are not empty.
	         */
			errorMessages.add(new GenericException(BDErrorCodes.NO_SEARCH_TEXT_ENTERED));
			isValidSearchCriteria =false;
		}else if((BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY.equalsIgnoreCase(internalFundCheckForm.getSearchType()))
				||(BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_KEY.equalsIgnoreCase(internalFundCheckForm.getSearchType()))){
	        /**
	         * 2. Validate the Contract Number and Producer Code filter value is numeric 
	         */
			if (!StringUtils.isNumeric(internalFundCheckForm.getSearchInput())) {
				internalFundCheckForm.setNoSearchResults(true);				
			}
		}else if(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY.equalsIgnoreCase(internalFundCheckForm.getSearchType())){
         /**
          * Display Error Message if Contract Name < 3 characters.
          */
			if (StringUtils.trimToNull(internalFundCheckForm.getSearchInput()).length() < 3) {
				errorMessages.add(new GenericException(BDErrorCodes.ENTER_ATLEAST_THREE_CHARS));
				isValidSearchCriteria=false;
			}
		}else if(BDConstants.FUNDCHECK_INPUT_FR_NAME_KEY.equalsIgnoreCase(internalFundCheckForm.getSearchType())){
         /**
          * Display Error Message if Financial Rep Name / Org Name < 3 characters.
          */
			if (StringUtils.trimToNull(internalFundCheckForm.getSearchInput()).length() < 3) {
				errorMessages.add(new GenericException(BDErrorCodes.ENTER_ATLEAST_THREE_CHARS));
				isValidSearchCriteria=false;
			}
		}
		if(!isValidSearchCriteria){	
			setErrorsInRequest(request, errorMessages);
		}else if(internalFundCheckForm.isNoSearchResults() == true){
			isValidSearchCriteria=false;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doValidateSearch");
		}		
		return isValidSearchCriteria;
	}   
	
	/**
	 * This method validates the search input given by the user.
	 * @param actionForm
	 * @param request
	 * @return
	 */
	private boolean doValidateSearchPdf(BDFundCheckInternalForm internalFundCheckForm, 
			HttpServletRequest request){
		if (logger.isDebugEnabled()){
			logger.debug("entry -> doValidateSearch");
		}
		boolean isValidSearchCriteria=true;
		
		internalFundCheckForm.setNoSearchResults(false);
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		if(StringUtils.isEmpty(internalFundCheckForm.getSearchTypeL2()) || StringUtils.isEmpty(internalFundCheckForm.getSearchInputL2())){
	        /**
	         * 1. Validate the Search Type and Search Input field values are not empty.
	         */
			GenericException exception = new GenericException(BDErrorCodes.NO_SEARCH_TEXT_ENTERED);
			errorMessages.add(exception);
		}else if(CONTRACT_NAME_TEXT.equalsIgnoreCase(internalFundCheckForm.getSearchTypeL2())){
	        /**
	         * 2. Validate the Contract Name filter value is not numeric 
	         * and not less than 3 Characters.
	         */
			if (StringUtils.trimToNull(internalFundCheckForm.getSearchInputL2()) != null
	                && (StringUtils.trimToNull(internalFundCheckForm.getSearchInputL2()).length() < 3)) {
	            GenericException exception = new GenericException(
	                    BDErrorCodes.ENTER_ATLEAST_THREE_CHARS);
	            errorMessages.add(exception);
			}
		}else if(CONTRACT_NUMBER_TEXT.equalsIgnoreCase(internalFundCheckForm.getSearchTypeL2())){
			if(!StringUtils.isNumeric(internalFundCheckForm.getSearchInputL2())){
				internalFundCheckForm.setNoSearchResults(true);
			}
		}
		if(errorMessages.size() > 0){	
			setErrorsInRequest(request, errorMessages);
			isValidSearchCriteria=false;
	   	}else if(internalFundCheckForm.isNoSearchResults() == true){
	   		isValidSearchCriteria=false;
	   	}
		if (logger.isDebugEnabled()){
			logger.debug("exit -> doValidateSearch");
		}
		return isValidSearchCriteria;
	}
	
	 @RequestMapping(value = "/fundCheckInternal/", params="action=openPDF" , method =  RequestMethod.POST) 
	    public String doOpenPDF (@Valid @ModelAttribute("bdFundCheckInternalForm") BDFundCheckInternalForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
			 }
		 }
		 String forward=super.doOpenPDF( actionForm, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	
	 /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */ 
	 @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

	
}
