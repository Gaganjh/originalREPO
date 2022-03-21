package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralUpdateDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;
/**
 * This action handles the creation of the ParticipantDeferralChangesReport. It will
 * also create the participant Deferral Changes download.
 *
 * @author Jan Barnes
 * @see ReportController for details
 */

@Controller

public final class ParticipantDeferralChangesReportController  extends ReportController{

	protected static final int DEFAULT_PAGE_SIZE = 35;
	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	public static final String FORM_STATE_UNSAVED_RETURN = "unsavedReturn";
	public static final String FORM_STATE_UNSAVED_SEARCH = "unsavedSearch";
	public static final String FORM_STATE_SEARCH_ONLY = "searchOnly";
	public static final String FORM_STATE_SAVE_ONLY = "saveOnly";
	public static final String FORM_STATE_INITIAL_SEARCH = "initialSearch";
	
	
	/**
	 * Constructor for ParticipantDeferralChangesReportAction
	 */
	public ParticipantDeferralChangesReportController() {
		super(ParticipantDeferralChangesReportController.class);
	}


	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		ParticipantDeferralChangesReportForm form = (ParticipantDeferralChangesReportForm) reportForm;

		//Display number of Participants with deferrals
		int numberParticipantsChanged = ((ParticipantDeferralChangesReportData) report)
				.getNumberParticipantsChanged();

		int rothCount = ((ParticipantDeferralChangesReportData) report)
		.getRothCount();
		
		//get dates for display
		Date fromDate = new Date();
		Date toDate = new Date();

		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
		
		try {
       		fromDate = format.parse(form.getFromDate());
       	} catch(ParseException pe) {
           	 if (logger.isDebugEnabled()) {
        		logger.debug("ParseException in fromDate getDownloadData() ParticipantDeferralChangesReportAction:", pe);
        	}
       	}    

		try {
       		toDate = format.parse(form.getToDate());
       	} catch(ParseException pe) {
           	 if (logger.isDebugEnabled()) {
        		logger.debug("ParseException in toDate getDownloadData() ParticipantDeferralChangesReportAction:", pe);
        	}
       	}   
		
				
		StringBuffer buffer = new StringBuffer();
		
        Contract currentContract = getUserProfile(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);
		
		// section one of the CSV report
		buffer.append("From date,To date");
		buffer.append(LINE_BREAK);
		buffer.append(DateRender.formatByPattern(fromDate, "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA).append(DateRender.formatByPattern(toDate, "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Number participants who submitted requests during the date range ").append(numberParticipantsChanged).append(LINE_BREAK);
		
		// section two of the CSV report
		buffer.append(LINE_BREAK);
		buffer.append(escapeField("For Excel users, you can reformat the 'Time of Request' column as a Date/Time. To do this, highlight the column, then select Format/Cells.  Under Category, select Date and then under Type, choose the format you want."));	
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		String typeOfPageLayout = form.getTypeOfPageLayout();
		
		//column headings - if full, if hide division, if hide payroll, and if hide both division and payroll
		if (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.FULL_COLUMNS)){
			if (rothCount > 0){
				buffer.append("Processed,Last name, First name, SSN,Date of request,Time of request, Payroll number, Division, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Roth 401(k)deferral requested ($),Roth 401(k)deferral requested (%),Contribution status");	
			} else {
				buffer.append("Processed,Last name, First name, SSN,Date of request,Time of request, Payroll number, Division, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Contribution status");					
			}
		} else if (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_DIVISION)){
			if (rothCount > 0){
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Payroll number, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Roth 401(k)deferral requested ($),Roth 401(k)deferral requested (%), Contribution status");	
			} else {
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Payroll number, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Contribution status");	
			}
			//buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Payroll number, New Deferral requested ($),New Deferral requested (%), Contribution status");	
		} else if (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_PAYROLL)){
			if (rothCount > 0){
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Division, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Roth 401(k)deferral requested ($),Roth 401(k)deferral requested (%), Contribution status");				
			} else {
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Division, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Contribution status");				
			}
		} else if (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_BOTH)){	
			if (rothCount > 0){
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Roth 401(k)deferral requested ($),Roth 401(k)deferral requested (%), Contribution status");						
			} else {
				buffer.append("Processed,Last name, First name,SSN,Date of request,Time of request, Traditional 401(k)deferral requested ($),Traditional 401(k)deferral requested (%), Contribution status");							
			}
		}		
		//SSE024, mask ssn if no download report full ssn permission
        boolean maskSSN = true;
		try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }	

		buffer.append(LINE_BREAK);
		
		//loop through details
				Iterator iterator = report.getDetails().iterator();
				while (iterator.hasNext()) {
					ParticipantDeferralChangesDetails theItem = 
						(ParticipantDeferralChangesDetails) iterator.next();
						//select only unprocessed records
							if (theItem.getProcessInd()){
								buffer.append("Yes").append(COMMA);
							} else {
								buffer.append("No").append(COMMA);
							}
							buffer.append(escapeField(theItem.getLastName()).trim()).append(COMMA);
							buffer.append(escapeField(theItem.getFirstName()).trim()).append(COMMA);
							//buffer.append(theItem.getSsn()).append(COMMA);
							buffer.append(SSNRender.format(theItem.getSsn(), "", maskSSN)).append(COMMA);


   							buffer.append(DateRender.formatByPattern(theItem.getChangeDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
 
				
							buffer.append(DateRender.formatByPattern(theItem.getChangeTS(), "", "yyyy-MM-dd HH:mm:ss.SSS")).append(COMMA);
							//if full columns, hide division column or not hide both - show the payroll column
							if (!typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_BOTH)){
								if ((typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.FULL_COLUMNS)) || (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_DIVISION))) {
									buffer.append(theItem.getEmployerDesignatedID()).append(COMMA);
								}
							}		
							//if full columns, hide payroll column or not hide both - show the Division column
							if (!typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_BOTH)){
								if ((typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.FULL_COLUMNS)) || (typeOfPageLayout.equals(ParticipantDeferralChangesReportForm.HIDE_PAYROLL))) {
									buffer.append(theItem.getOrganizationUnitID()).append(COMMA);
								}
							}	
					
							if (theItem.hasTradDeferral()){
								
								if (theItem.getContributionAmt() > 0){
									buffer.append(theItem.getContributionAmt());
								} else {
									buffer.append("0");
								}
							buffer.append(COMMA);
								if (theItem.getContributionPct() > 0){
							buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPct()),
                			"", "##"));
								} else {
									buffer.append(NumberRender.formatByPattern(new Double("0"),
									"", "##"));
								}									
							} else {
								buffer.append(NumberRender.formatByPattern(new Double("0"),
										"", "##"));
								buffer.append(COMMA);
								buffer.append(NumberRender.formatByPattern(new Double("0"),
								"", "##"));								
							}
							
							if (rothCount > 0){
								if (theItem.hasRothDeferral()){
									buffer.append(COMMA);
									if (theItem.getContributionAmtRoth() > 0){
										buffer.append(theItem.getContributionAmtRoth());
									}else {
										buffer.append("0");
									}
									buffer.append(COMMA);
									if (theItem.getContributionPctRoth() > 0 ){
										buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPctRoth()),"", "##"));	
									} else {
										buffer.append(NumberRender.formatByPattern(new Double("0"),
										"", "##"));
									}
								} else {
									buffer.append(COMMA);
									buffer.append(NumberRender.formatByPattern(new Double("0"),
									"", "##"));
									buffer.append(COMMA);
									buffer.append(NumberRender.formatByPattern(new Double("0"),
									"", "##"));
								}
							}	
	
							buffer.append(COMMA);
							buffer.append(theItem.getContributionStatus()).append(COMMA);

							buffer.append(LINE_BREAK);
				}


		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantDeferralChangesReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantDeferralChangesReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantDeferralChangesReportData.REPORT_NAME;
	}

    protected int getPageSize() {

        return DEFAULT_PAGE_SIZE;
    }



	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(ParticipantDeferralChangesReportData.FILTER_FIELD_1, Integer.toString(currentContract.getContractNumber()));

		
		ParticipantDeferralChangesReportForm psform = (ParticipantDeferralChangesReportForm) form;


		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

	
		//get the from Date
		if(!StringUtils.isEmpty(psform.getFromDate())) {
			try {
        		Date fromDate = format.parse(psform.getFromDate());
        		criteria.addFilter(ParticipantDeferralChangesReportData.FILTER_FIELD_2,fromDate);
        	} catch(ParseException pe) {
           	 	if (logger.isDebugEnabled()) {
        			logger.debug("ParseException in fromDate getDownloadData() ParticipantDeferralChangesReportAction:", pe);
        		}
        	}        	
		}

		//get the to Date
		if(!StringUtils.isEmpty(psform.getToDate())) {
			try {
        		Date toDate = format.parse(psform.getToDate());
        		criteria.addFilter(ParticipantDeferralChangesReportData.FILTER_FIELD_3,toDate);
        	} catch(ParseException pe) {
           	 	if (logger.isDebugEnabled()) {
        			logger.debug("ParseException in toDate getDownloadData() ParticipantDeferralChangesReportAction:", pe);
        		}
        	}   			
		}

		//if the unprocessed indicator check box is checked, add the criteria to select only unprocessed indicators 
		//
		String paramUnprocessed = request.getParameter("unprocessedIndOnly");

		if ("on".equals(paramUnprocessed)){
			criteria.addFilter(ParticipantDeferralChangesReportData.FILTER_FIELD_4,new Integer(ParticipantDeferralChangesReportData.UNPROCESSED_INDS_ONLY));
		} else {
			criteria.addFilter(ParticipantDeferralChangesReportData.FILTER_FIELD_4,new Integer(ParticipantDeferralChangesReportData.ALL_PROCESS_INDS));
		}		
		


		if (logger.isDebugEnabled()) {
			logger.debug("criteria= "+criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}
	public String doCommon( BaseReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
			throws SystemException {
		doValidate(  actionForm,request);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		UserRole role = principal.getRole();
		
		//all users except RM and TPA can update the DeferralReporting ind
		boolean userCanUpdateDeferralReportingInd = true;
		if (role != null) {
			if ((role instanceof RelationshipManager) 
          || (role instanceof BasicInternalUser) 
          || (role instanceof ThirdPartyAdministrator)){
							userCanUpdateDeferralReportingInd = false;	
			}
		}

		//only External users can update the Processed Indicators
		boolean userCanUpdateProcessedInds = false;
		if (role != null) {
			if (role instanceof ExternalUser){
				userCanUpdateProcessedInds = true;
			}
		}

		//get the current form
		ParticipantDeferralChangesReportForm form = (ParticipantDeferralChangesReportForm)actionForm;
		actionForm.setSortDirection(form.getSortDirection());
		actionForm.setSortField(form.getSortField());
		
		int pageNum = form.getPageNumber();
		
		boolean deferralReportingInd = form.getDeferralReportingInd();
		boolean baseDeferralReportingInd = form.getBaseDeferralReportingInd();
		boolean updateDeferralReportingInd = false;

		
		
		if (deferralReportingInd != baseDeferralReportingInd){
			updateDeferralReportingInd = true;
		}
		

		//temporarily store values from the current form
		String[] changedProcessedIndicators = null;
		String strArray = null;

		//default state
		String formState = FORM_STATE_SEARCH_ONLY;

		
		//DETERMINE IF INITIAL SEARCH			
		if (!form.getIsDirty()){	
			//if the isSearch is set to false in the form 
			if(!form.getIsSearch()){
				//if the isSave is set to false in the form
				if(!form.getIsSave()){
					//then we have unsaved data on the page, but are throwing away the changes and 
					//continuing with the search
					formState = FORM_STATE_INITIAL_SEARCH;
				}
			}
		}		
		

		//DETERMINE IF UNSAVED RETURN
		if (form.getUnsavedDataReturn()){
			if (form.getIsDirty()){
				//if the isSearch is set to true in the form 
				if(form.getIsSearch()){
					//if the isSave is set to false in the form
					if(!form.getIsSave()){
						//then we have unsaved data on the page, and are returning to the page to save
						formState = FORM_STATE_UNSAVED_RETURN;
					}
				}
			}	
		}


			
		//DETERMINE IF SAVE ONLY			
		if (form.getIsDirty()){
			//if the isSearch is set to true in the form 
			if(!form.getIsSearch()){
				//if the isSave is set to false in the form
				if(form.getIsSave()){
					//then we have a save only
					formState = FORM_STATE_SAVE_ONLY;
				}
			}
		}		

		//form is dirty because there is unsaved data on the page
		//either get the dirty process indicators from the request - if triggered by unsaved confirm box
		//or if a submit - save - get the dirty process indicators from the form

			if (formState.equals(FORM_STATE_UNSAVED_RETURN)){
				strArray = request.getParameter("processedIndCheckbox");
				if (strArray != null){
					StringTokenizer st=new StringTokenizer(strArray," :,;");
					int counter = 0;
					int tokenCount = st.countTokens();
					changedProcessedIndicators = new String[tokenCount];
					if (tokenCount > 0){
						while (st.hasMoreTokens()){
							String indId = st.nextToken();
							if (!indId.equals("na")){
								changedProcessedIndicators[counter] = indId;
								counter++;
							}	
						
						}
					}		
				}		
			}


			
			//if a Submit on a save - form will have values. The request will not include the values
			if (formState.equals(FORM_STATE_SAVE_ONLY)){
				changedProcessedIndicators = new String[form.getProcessedIndCheckbox().length];	
				for (int j = 0; j< form.getProcessedIndCheckbox().length; j++){
					if (!form.getProcessedIndCheckbox()[j].equals("na")){
						changedProcessedIndicators[j] = form.getProcessedIndCheckbox()[j];
					}
				}		
			}
		
			//create ArrayList to store items to be updated to CSDB
			ArrayList updateItems = new ArrayList();
			ParticipantDeferralUpdateDetails updateItem;
			Contract currentContract = getUserProfile(request).getCurrentContract();
			int contractNumber = currentContract.getContractNumber();
		

			if (formState.equals(FORM_STATE_SAVE_ONLY)){
				ParticipantDeferralChangesDetails item;
				//get the originalReport for the Save comparison - determine if the chk box indicators have changed
				ParticipantDeferralChangesReportData originalReport = (ParticipantDeferralChangesReportData)request.getSession().getAttribute("original_bean");		
			
				if (originalReport != null){
					ArrayList originalDetails = new ArrayList(originalReport.getDetails());
					if (originalDetails.size() > 0) {
					
			
						for (int i = 0;i < originalDetails.size(); i++){
							item = (ParticipantDeferralChangesDetails) originalDetails.get(i);
							String id = item.getUniqueId();
							boolean procInd = item.getProcessInd();
							boolean foundMatchForTrueRecord = false;
							boolean foundMatchForFalseRecord = false;

						
							//if original was true - determine when to save a false
							if (procInd) {
								//look for a match in the changed chk box array
								for (int j = 0; j< changedProcessedIndicators.length; j++){
									//if match is found then the original and changed chk box are both true - no save
									if (id.equals(changedProcessedIndicators[j])){
										//then set flag to true, so that nothing will be saved to the database
										foundMatchForTrueRecord = true;
									} else {
										//do nothing, continue to loop until list exhausted
									}
								}
								//else if there is no match - then the original true value is now false - save false
								if (!foundMatchForTrueRecord) {
									updateItem = new ParticipantDeferralUpdateDetails();
									updateItem.setChangeTS(item.getChangeTS());
									updateItem.setProfileId(item.getProfileId());
									updateItem.setProcessInd(false);
									updateItems.add(updateItem);
								}	
							}


						
						
							//if original was false - determine when to save a true
							if (procInd == false) {							
								//look for a match in the changed chk box array
								for (int j = 0; j< changedProcessedIndicators.length; j++){
									//if a match,set flag to true, so that a true will be saved to the database
									if (id.equals(changedProcessedIndicators[j])){
										foundMatchForFalseRecord = true;
									//if no match, then original was false and changed chk box is also false - no save	
									} else {
										//do nothing, continue to loop until list exhausted
									}
								}
								//if match is found then the original was false and changed chk box is true - save true								
								if (foundMatchForFalseRecord){
									updateItem = new ParticipantDeferralUpdateDetails();
									updateItem.setChangeTS(item.getChangeTS());
									updateItem.setProfileId(item.getProfileId());
									updateItem.setProcessInd(true);
									updateItems.add(updateItem);
								}						
							}
						//end of for loop
						}
					//end of if originalDetails.size() > 0
					}
				//end of if (originalReport != null)
				}
					
			}	


			//if External users (PA, PSEUM, TPA)
			//and we have update items to save
			if (userCanUpdateProcessedInds){
				if (updateItems.size() > 0){
					for (int i = 0; i< updateItems.size(); i++){
						updateItem = (ParticipantDeferralUpdateDetails)updateItems.get(i);
						ParticipantServiceDelegate.getInstance().updateDeferralProcessInd(contractNumber, updateItem.getProfileId(), updateItem.getChangeTS(), updateItem.getProcessInd());
					}	
				}
			}	
			
			
			if (userCanUpdateDeferralReportingInd){
				if (formState.equals(FORM_STATE_SAVE_ONLY)){
					if (updateDeferralReportingInd){
						ContractServiceDelegate.getInstance().updateDeferralReportingInd(contractNumber,deferralReportingInd);
					}
				}
			}	

			String forward = super.doCommon( actionForm, request,response);

		//get the Report object		
		ParticipantDeferralChangesReportData report = (ParticipantDeferralChangesReportData)request.getAttribute(Constants.REPORT_BEAN);		

		//get the report details		
		ArrayList details = new ArrayList(report.getDetails());
		//create ArrayList to store the uniqueIds for those details records that have a processInd of true
		ArrayList processedIndicators = new ArrayList();



 		//save was successful

 			if (formState.equals(FORM_STATE_SAVE_ONLY)){
								
 				form.setIsSave(false);
 				form.setIsDirty(false);
 				form.setIsSearch(false);
 				form.setIsRefresh(false);
 				form.clearArrays();
 			}

		//as long as there are details
		if (details.size() > 0) {
			form.setHasDetailRecords(true);
			//loop through all of the details
			for (int i = 0; i < details.size(); i++){
				ParticipantDeferralChangesDetails item = (ParticipantDeferralChangesDetails) details.get(i);
				//if the processInd is true, then get the uniqueId, and add to the ArrayList
				if (item.getProcessInd() == true){
					processedIndicators.add(item.getUniqueId());
				}
			}		
			
			//if the ArrayList has uniqueId's - there are records with a processInd of true
			if (processedIndicators.size() > 0){
				//call the initialize method on the form to initialize the processedIndCheckboxArray
				form.initializeProcessedIndCheckboxArray(processedIndicators);
				
			} else {
				form.clearArrays();
			}	 
		} else {
			form.setHasDetailRecords(false);
		}	


		//save the current changes into the form - unsaved changes

			if (formState.equals(FORM_STATE_UNSAVED_RETURN)){
				form.setChangedProcessedIndCheckbox(changedProcessedIndicators);
				form.setIsDirty(false);
				form.setIsSave(false);
				form.setIsSearch(false);
				form.setIsRefresh(true);
				form.setUnsavedDataReturn(false);
			}

			//default state
			if (formState.equals(FORM_STATE_SEARCH_ONLY)){
				form.setIsSearch(false);
				form.setIsDirty(false);
				form.setIsSave(false);
				form.setIsRefresh(false);
				form.setIsInitialSearch(false);
			}	


			if (formState.equals(FORM_STATE_INITIAL_SEARCH)){
				form.setIsInitialSearch(true);
			}
		


		//as long as there are details
		if (details.size() > 0) {
			if (report.getNonEmptyOrganizationUnitCount() > 0) {
				form.setHasDivisionFeature(true);
			} else {
				form.setHasDivisionFeature(false);
			}	
		
			if (report.getNonEmptyPayrollCount() > 0) {
				form.setHasPayrollNumberFeature(true);
			} else {
				form.setHasPayrollNumberFeature(false);
			}			
		}

		if (!formState.equals(FORM_STATE_UNSAVED_RETURN)){
			form.setDeferralReportingInd(report.getDeferralInstructionInd());
		}

		String task = getTask(request);

		form.setBaseFromDate(form.getFromDate());
		form.setBaseToDate(form.getToDate());
		form.setBaseUnprocessedIndOnly(form.getUnprocessedIndOnly());
		form.setBaseSortDirection(form.getSortDirection());
		form.setBaseSortField(form.getSortField());
		form.setBaseTask(task);
		form.setBasePageNumber(form.getPageNumber());
		if (!formState.equals(FORM_STATE_UNSAVED_RETURN)){
			form.setBaseDeferralReportingInd(form.getDeferralReportingInd());
		}	
		request.getSession().setAttribute("original_bean",report);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}

			return forward;

	}
	
	protected void populateReportForm(  BaseReportForm reportForm, HttpServletRequest request) 
		{

		
		super.populateReportForm( reportForm, request);

		String task = getTask(request);


		
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
	    if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }
		
		String task = getTask(request);

		

		Collection errors = super.doValidate( form, request);
		ParticipantDeferralChangesReportForm theForm = (ParticipantDeferralChangesReportForm) form;

if (!task.equals(DEFAULT_TASK)) {
		Calendar calToDate;
		Calendar calFromDate;
		Date fromDate = new Date();
		Date toDate = new Date();
		boolean validDates = false;
		boolean validFromDate = false;
		boolean validFromDateRange = false;

		//if only the from date is empty
		if((StringUtils.isEmpty(theForm.getFromDate()))  && (!StringUtils.isEmpty(theForm.getToDate()))) {
			errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
		}	

		//if both dates are empty		
		if ((StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
			errors.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
		}	


		//valid  To date or from date format
		if ((theForm.getToDate().trim().length() > 0) && (theForm.getFromDate().trim().length() > 0)){

			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
			format.setLenient(false);
			
			try {
        		toDate = new Date(format.parse(theForm.getToDate()).getTime());
        		fromDate = new Date(format.parse(theForm.getFromDate()).getTime());
       			validFromDate = true;
       			validDates = true;
        	} catch(Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validDates = false;
				validFromDate = false;
        	}        	
		}


		//empty From date, invalid to date
		if ((StringUtils.isEmpty(theForm.getFromDate())) && (theForm.getToDate().trim().length() > 0)){

			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
			format.setLenient(false);
			
			try {
        		toDate = new Date(format.parse(theForm.getToDate()).getTime());
       			validDates = true;
        	} catch(Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validDates = false;
        	}        	
		}


		//invalid  from date format and empty to date
		if ((theForm.getFromDate().trim().length() > 0) && (theForm.getToDate().trim().length() == 0)){

			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
			format.setLenient(false);
			
			try {
        		fromDate = new Date(format.parse(theForm.getFromDate()).getTime());
       			validFromDate = true;
        	} catch(Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validFromDate = false;
        	}        	
		}

		//if from date is populated and to date is empty - and from date is greater than toDate
		if (validFromDate){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				calToDate = Calendar.getInstance();
				calFromDate = Calendar.getInstance();
				calToDate.setTime(toDate);
				calFromDate.setTime(fromDate);
    			
    			
    			if (calFromDate.after(calToDate)) {
       				errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
       				validFromDateRange = false;
       			} else {
       				validFromDateRange = true;
    			}	
       			
			}
		}				


		//if from date is populated and to date is empty - and from date not within the last 24 months of today
		if (validFromDate){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				Calendar calEarliestDate = Calendar.getInstance();
				calEarliestDate.add(Calendar.MONTH, -24);
				calFromDate = Calendar.getInstance();
       			
       			calFromDate.setTime(fromDate);
 				
				  		
    			if (calFromDate.before(calEarliestDate)) {
       				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
       				validFromDateRange = false;
       			} else {
       				validFromDateRange = true;
    			}	
			}
		}


		//if from date is populated, the date range is corret, and to date is empty - then default the toDate to current date
		if (validFromDateRange){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				calToDate = Calendar.getInstance();
				Date dtToDate = calToDate.getTime();
				theForm.setToDate(DateRender.formatByPattern(dtToDate, "", FORMAT_DATE_SHORT_MDY));
			}
		}		
	
		//From date must be earlier than To date
		if (validDates){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {

				calToDate = Calendar.getInstance();
				calFromDate = Calendar.getInstance();
				calToDate.setTime(toDate);
				calFromDate.setTime(fromDate);
    
    			if (calFromDate.after(calToDate)) {
       				errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
       			}
			}	
		}	
		
		//From date must be within last 24 months of today
		if (validDates){
			if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
				Calendar calEarliestDate = Calendar.getInstance();
				calEarliestDate.add(Calendar.MONTH, -24);
				calFromDate = Calendar.getInstance();
       			
       			calFromDate.setTime(fromDate);
 			
				  		
    			if (calFromDate.before(calEarliestDate)) {
       				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
       			}
			}
		}		
}	

		if (errors.size() > 0) {
			theForm.setIsDirty(false);
			theForm.setIsRefresh(false);
			theForm.setHasDetailRecords(false);
			theForm.setIsInitialSearch(false);
			theForm.setBaseFromDate(theForm.getFromDate());
			theForm.setBaseToDate(theForm.getToDate());
			theForm.setBaseUnprocessedIndOnly(theForm.getUnprocessedIndOnly());
			populateReportForm( theForm, request);
			SessionHelper.setErrorsInSession(request, errors);
			ParticipantDeferralChangesReportData reportData = new ParticipantDeferralChangesReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			theForm.setErrorsOnPage(true);
		} else {
			theForm.setErrorsOnPage(false);
		}

	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }

		return errors;

	}


	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public String execute(@Valid @ModelAttribute("dynaForm") BaseReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
	
	
		String unprocessedIndOnly = null;
		String paramUnprocessed = request.getParameter("unprocessedIndOnly");

 
		if ("on".equals(paramUnprocessed)){
			unprocessedIndOnly = "on";
		} else {
			unprocessedIndOnly = "off";	
		}		

		String deferralReportingInd = null;
		String paramDefReportInd = request.getParameter("deferralReportingInd");

		if ("on".equals(paramDefReportInd)){
			deferralReportingInd = "on";
		} else {
			deferralReportingInd = "off";	
		}				
		

		
		if ("POST".equalsIgnoreCase(request.getMethod()) ) {
			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", 
					"/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request) + "&unprocessedIndOnly=" + unprocessedIndOnly + "&deferralReportingInd=" + deferralReportingInd, 
					true);
			
		    if(logger.isDebugEnabled()) {
			    logger.debug("forward = " + forward);
		    }
			return forward.getPath();
		}
		
		
		return null;
	}
	
	//Overside the doDefault and resetForm - to NOT refresh the form
	 @RequestMapping(value ="",  method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doDefault(@Valid @ModelAttribute("dynaForm") BaseReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 doValidate(  actionForm,request);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}

		actionForm = resetForm( actionForm, request);
		
		String forward = doCommon( actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forward;
	}


	protected BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		
		
		try {
			BaseReportForm blankForm = (BaseReportForm) reportForm
					.getClass().newInstance();
			PropertyUtils.copyProperties(reportForm, blankForm);
			
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"resetForm", "exception in resetting the form");
		}

		return reportForm;
	}
	
	private String escapeField(String field)
	{
		if(field.indexOf(",") != -1 )
		{
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		}
		else
		{
			return field;
		}
	}
}

