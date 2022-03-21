package com.manulife.pension.ps.web.participant;

import static com.manulife.pension.platform.web.CommonConstants.DOLLAR_SIGN;
import static com.manulife.pension.platform.web.CommonConstants.PERCENTAGE_SIGN;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountAssetsByRiskVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.RenderConstants;


/**
 *  Custom version of ParticipantAccountCommonAction for DefinedBenefit support.
 *  DefinedBenefit needs to hide/rename some fields on the screen/download report, so 
 *  we have a custom version. This is in support of the jsp pages DefinedBenefitAccount*.jsp
 * 
 * 
 * @author Glen Lalonde
 *
 */
public abstract class DefinedBenefitAccountCommonController extends ParticipantAccountCommonController {
    
    protected static final double HUNDRED = 100;

	public DefinedBenefitAccountCommonController(Class clazz) {
		super(clazz);
	}


	/*
	 * Method that produces the first(top) part of the download data, specific
	 * screens implement the populateDetailedDownloadData method.
	 * 
	 * Based on populateDownloadData in ParticipantAccountCommonAction
	 * 
	 */
	protected void populateDownloadData(
			ParticipantAccountVO participantAccountVO,
			ParticipantAccountForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		UserProfile userProfile = getUserProfile(request);

		boolean showLoans = form.getShowLoans();
		boolean showPBA = userProfile.getCurrentContract().isPBA();
		StringBuffer buff = new StringBuffer(255);

		Contract currentContract = getUserProfile(request).getCurrentContract();
        buff.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		SimpleDateFormat dateFormatter = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);
		
		Date asOfDate = new Date(Long.parseLong(form.getSelectedAsOfDate()));

		// Section 1 as of date for the report
		buff.append("As of,").append(dateFormatter.format(asOfDate)).append(LINE_BREAK).append(LINE_BREAK);

		// Section 2 participant details for the report
		buff.append("Name,Status,");
		buff.append(LINE_BREAK);

		ParticipantAccountDetailsVO detailsVO = participantAccountVO.getParticipantAccountDetailsVO();

		buff.append(QUOTE).append(detailsVO.getFirstName()+" ").append(detailsVO.getLastName()).append(QUOTE).append(COMMA);
		buff.append(detailsVO.getEmployeeStatus()).append(COMMA);
		
		buff.append(LINE_BREAK).append(LINE_BREAK).append(LINE_BREAK);
					
		buff.append("Total assets,");
		buff.append(detailsVO.getTotalAssets()).append(LINE_BREAK);
		buff.append("Allocated assets,");
		buff.append(detailsVO.getAllocatedAssets());

		if(showLoans) {
			buff.append(LINE_BREAK);
			buff.append("Loan assets,");
			buff.append(detailsVO.getLoanAssets());
		}	
		
		if(showPBA) {
			buff.append(LINE_BREAK);
			buff.append("Personal brokerage account,");
			buff.append(detailsVO.getPersonalBrokerageAccount());
		}
			
//		if(isAsOfDateCurrent) {
			buff.append(LINE_BREAK);
			//CL 103590 fix - Defined Benefit contract report csv problem - start
//			buff.append("Default investment indicator,");
//			buff.append(detailsVO.getDefaultInvestmentIndicator()).append(LINE_BREAK);
			//CL 103590 fix - Defined Benefit contract report csv problem - end
			buff.append("Last contribution date,");
			if (detailsVO.getLastContributionDate() != null) {
				buff.append(dateFormatter.format(detailsVO.getLastContributionDate())).append(LINE_BREAK);
			} else {
				buff.append("none").append(LINE_BREAK);
			}
			//CL 103590 fix - Defined Benefit contract report csv problem - start
//			buff.append("Automatic rebalance indicator,");
//			buff.append(detailsVO.getAutomaticRebalanceIndicator()).append(LINE_BREAK);;
			//CL 103590 fix - Defined Benefit contract report csv problem - end
			if (detailsVO.getRothFirstDepositYear() != 9999){
				buff.append("Year of first Roth contribution,");
				buff.append(detailsVO.getRothFirstDepositYear()); 
			}
//		}
		buff.append(LINE_BREAK);

		// Section 3 allocated assets for the report
		ParticipantAccountAssetsByRiskVO assetsByRiskVO = participantAccountVO.getAssetsByRisk();
		
		if (assetsByRiskVO != null) {

            buff.append(LINE_BREAK).append(LINE_BREAK);

            if (userProfile.getCurrentContract().getHasLifecycle()
                    || assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_LIFECYCLE) > 0) {
                buff.append("Target Date,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_LIFECYCLE)).append(COMMA)
                        .append(
                                Math.round(HUNDRED
                                        * assetsByRiskVO
                                        .getPercentageTotalByRisk(FundVO.RISK_LIFECYCLE)))
                        .append(PERCENTAGE_SIGN).append(LINE_BREAK);
            }

            buff.append("Aggressive,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_AGGRESIVE)).append(COMMA)
                    .append(
                            Math.round(HUNDRED
                                    * assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_AGGRESIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_GROWTH)).append(COMMA).append(
                            Math.round(HUNDRED
                            * assetsByRiskVO.getPercentageTotalByRisk(FundVO.RISK_GROWTH))).append(
                    PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth & income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_GROWTH_INCOME)).append(COMMA)
                    .append(
                            Math.round(HUNDRED
                                    * assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_GROWTH_INCOME)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_INCOME)).append(COMMA).append(
                            Math.round(HUNDRED
                            * assetsByRiskVO.getPercentageTotalByRisk(FundVO.RISK_INCOME))).append(
                    PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Conservative,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_CONSERVATIVE)).append(COMMA)
                    .append(
                            Math.round(HUNDRED
                                    * assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_CONSERVATIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            if (showPBA) {
                buff.append("Personal brokerage account,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_PBA)).append(COMMA).append(
                                Math.round(HUNDRED
                                * assetsByRiskVO.getPercentageTotalByRisk(FundVO.RISK_PBA)))
                        .append(PERCENTAGE_SIGN).append(LINE_BREAK);
            }

            buff.append(LINE_BREAK);
        }

		buff.append(populateDetailedDownloadData(participantAccountVO, form));
		
		byte[] downloadData = buff.toString().getBytes();

        ReportController.streamDownloadData(request, response, ReportController.CSV_TEXT,
                getTabName() + ReportController.CSV_EXTENSION, downloadData);

		
		/*response.setContentType(ReportAction.CSV_TEXT);

		try {
			response.getOutputStream().println(buff.toString());
		} catch (IOException ioe) {
			SystemException se = new SystemException(ioe, this.getClass().getName(), 
					"populateDownloadData", "populateDownloadData caught Exception writing csv data to ouput stream.");
			throw se;
		}*/

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}
	}

	
	/**
	 * We need to jump through some hoops here to find the one participant's profile ID for
	 * a given contract. There should be a one to one relationship between the contract
	 * and (one fake)participant.
	 */
	public void findProfileIdByContract(ActionForm form, HttpServletRequest request) 
	         throws SystemException, ReportServiceException {
		
		// run a query simulating the ACCOUNT SUMMARY page 
		ReportCriteria criteria = new ReportCriteria(ParticipantSummaryReportData.REPORT_ID);		 
		criteria.insertSort(ParticipantSummaryReportData.DEFAULT_SORT,ReportSort.ASC_DIRECTION); // populateSortCriteria(criteria);
		criteria.setPageNumber(1); // Start at 1 not 0 I think.
		criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
		
//		populateReportCriteria(criteria, request);
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));
		
		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		ReportData bean = service.getReportData(criteria);
		
		if (bean.getDetails().size() != 1) {
			// FIXME: generate an error once we have some REAL db contracts to work with
		}
		
		ParticipantSummaryDetails theItem = (ParticipantSummaryDetails) bean.getDetails().iterator().next();
		String profileId = theItem.getProfileId();
		
		ParticipantAccountForm accountForm = (ParticipantAccountForm) form;
		accountForm.setDefinedBenefitContract(true);
		accountForm.setProfileId(profileId); // ParticipantAccountCommonAction(our base class)
	}


//	private void populateReportCriteria(ReportCriteria criteria,
//			HttpServletRequest request) {
//
//		// default sort criteria
//		// this is already set in the super
//
//		UserProfile userProfile = getUserProfile(request);
//		Contract currentContract = userProfile.getCurrentContract();
//
//		criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));
//
//		ParticipantSummaryReportForm psform = (ParticipantSummaryReportForm) form;
//
//		if(!StringUtils.isEmpty(psform.getAsOfDate())) {
//        	criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_2,psform.getAsOfDate());
//		}
//
//		if(!StringUtils.isEmpty(psform.getStatus()) && psform.isAsOfDateCurrent()) {
//	        criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_3,
//	            ((ParticipantSummaryReportForm) form).getStatus());
//		}
//
//	}

	
	 protected String preExecute( ActionForm form,
	            HttpServletRequest request, HttpServletResponse response) throws ServletException,
	            IOException, SystemException {
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
                
        // check if contract is discontinued, DBA5, DBA6, DBA7
        if (userProfile.getCurrentContract().isDiscontinued()) {
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }

        try {
        	findProfileIdByContract(form, request); 
		} catch (ReportServiceException e) { // this is how ReportAction handles this exception 
			logger.error("Received a Report service exception: ", e);
			List errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			setErrorsInRequest(request, errors);
			//TODO
			//return mapping.getInputForward();
			return "input";
		}

		return null; // normal return 
	}
	 /**
	     * Returns the tab name for the .csv file name 
	     * 
	     * @return String - tab name
	     */
	    protected abstract String getTabName();
}

