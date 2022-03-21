package com.manulife.pension.ps.web.census.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.census.CensusSummaryReportForm;
import com.manulife.pension.ps.web.census.CensusVestingReportForm;
import com.manulife.pension.ps.web.census.DeferralReportForm;
import com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm;
import com.manulife.pension.ps.web.participant.ParticipantAddressesReportForm;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Helper class for maintaining filter criterias across the tabs
 * 
 * @author jthangad
 */
public class CensusInfoFilterCriteriaHelper {

	private static final String DOWNLOAD_TASK = "download";

	private static final String DEFAULT_TASK = "default";
	
	private static final String FILTER_TASK = "filter";
	
	private static final String SAVE_TASK = "save";
	
	private static final String RESET_TASK = "reset";
	
	private static final String CANCEL_TASK = "cancel";
	
	private static final String PAGE_TASK = "page";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
    private static final SimpleDateFormat SP_DATE_FORMAT = new SimpleDateFormat("MMM d yyyy");
    
	/**
	 * Populates the filter criteria for Eligibility tab
	 * 
	 * @param task
	 * @param filterCriteriaVo
	 * @param psform
	 * @param criteria
	 */
	public static void populateEligibilityTabFilterCriteria(String task,
			FilterCriteriaVo filterCriteriaVo,
			EmployeeEnrollmentSummaryReportForm psform, ReportCriteria criteria) {
		
		if (!(task.equals(FILTER_TASK)) && !(task.equals(RESET_TASK)) && !(task.equals(DOWNLOAD_TASK))) {

			// Populate "criteria" object from filterCriteriaVo and also copy it to psform
			if (!StringUtils.isEmpty(filterCriteriaVo.getEnrollmentStatus())) {
				criteria.addFilter(EmployeeEnrollmentSummaryReportData.FILTER_STATUS,
						filterCriteriaVo.getEnrollmentStatus());

				psform.setStatus(filterCriteriaVo.getEnrollmentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getEmploymentStatus())) {
				if (!("All".equals(filterCriteriaVo.getEmploymentStatus()))) {
					criteria.addFilter(	EmployeeEnrollmentSummaryReportData.FILTER_EMP_STATUS,
									filterCriteriaVo.getEmploymentStatus());

					psform.setEmpStatus(filterCriteriaVo.getEmploymentStatus());
				}
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getLastName())) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());
				psform.setNamePhrase(filterCriteriaVo.getLastName());
			}

			if (!(filterCriteriaVo.getSsn().isEmpty()
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
				psform.setSsnOne(filterCriteriaVo.getSsnOne());
				psform.setSsnTwo(filterCriteriaVo.getSsnTwo());
				psform.setSsnThree(filterCriteriaVo.getSsnThree());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSegment())) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
				psform.setSegment(filterCriteriaVo.getSegment());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getDivision())) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
				psform.setDivision(filterCriteriaVo.getDivision());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getEnrolledFrom())) {
				criteria.addFilter(
								EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_START,
								filterCriteriaVo.getEnrolledFrom());
				psform.setEnrolledFrom(filterCriteriaVo.getEnrolledFrom());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getEnrolledTo())) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_END,
						filterCriteriaVo.getEnrolledTo());
				psform.setEnrolledTo(filterCriteriaVo.getEnrolledTo());
			}
			
			if (!StringUtils.isEmpty(filterCriteriaVo.getFromPED())) {
				criteria.addFilter(
								EmployeeEnrollmentSummaryReportData.FILTER_FROM_PED,
								filterCriteriaVo.getFromPED());
				psform.setFromPED(filterCriteriaVo.getFromPED());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getToPED())) {
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_TO_PED,
						filterCriteriaVo.getToPED());
				psform.setToPED(filterCriteriaVo.getToPED());
			}
			if(psform.getMoneyTypes()!= null && psform.getMoneyTypes().size() > 0){
				String moneyTypes = "";
				
					for(int index = 0; index < psform.getMoneyTypes().size(); index++ ){
						if(index == 0){
							moneyTypes = moneyTypes + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
						}else{
							moneyTypes = moneyTypes + ", " + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
						}
					}
				
				filterCriteriaVo.setMoneyTypeSelected(moneyTypes);
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
						filterCriteriaVo.getMoneyTypeSelected());
				
			}else{
				filterCriteriaVo.setMoneyTypeSelected("''");
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
						filterCriteriaVo.getMoneyTypeSelected());
			}

		}

		//If the request is for filter then populate the session object from form bean
		if (task.equals(FILTER_TASK)|| task.equals(DOWNLOAD_TASK)) {

			// Sync the filterCriteriaVo with form bean values and set the criterias
			if (!StringUtils.isEmpty(psform.getStatus())) {
				filterCriteriaVo.setEnrollmentStatus(psform.getStatus());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_STATUS,
						filterCriteriaVo.getEnrollmentStatus());
			} else {
				filterCriteriaVo.setEnrollmentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getEmpStatus())) {
				filterCriteriaVo.setEmploymentStatus(psform.getEmpStatus());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_EMP_STATUS,
						filterCriteriaVo.getEmploymentStatus());
			} else {
				filterCriteriaVo.setEmploymentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getNamePhrase())) {
				filterCriteriaVo.setLastName(psform.getNamePhrase());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());
			} else {
				filterCriteriaVo.setLastName(null);
			}

			if (!StringUtils.isEmpty(psform.getSegment())) {
				filterCriteriaVo.setSegment(psform.getSegment());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
			} else {
				filterCriteriaVo.setSegment(null);
			}

			if (!StringUtils.isEmpty(psform.getDivision())) {
				filterCriteriaVo.setDivision(psform.getDivision());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
			} else {
				filterCriteriaVo.setDivision(null);
			}

			if (!(psform.getSsn().isEmpty()
					&& StringUtils.isEmpty(psform.getSsnOne())
					&& StringUtils.isEmpty(psform.getSsnTwo())
					&& StringUtils.isEmpty(psform.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(psform.getSsnOne());
				filterCriteriaVo.setSsnTwo(psform.getSsnTwo());
				filterCriteriaVo.setSsnThree(psform.getSsnThree());
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}

			
				if (!StringUtils.isEmpty(psform.getEnrolledFrom()) && !StringUtils.isEmpty(psform.getEnrolledTo())) {
					
					filterCriteriaVo.setEnrolledFrom(psform.getEnrolledFrom());
					criteria.addFilter(
									EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_START,
									filterCriteriaVo.getEnrolledFrom());
					
					filterCriteriaVo.setEnrolledTo(psform.getEnrolledTo());
					criteria.addFilter(
									EmployeeEnrollmentSummaryReportData.FILTER_ENROLL_END,
									filterCriteriaVo.getEnrolledTo());
				} else {
					filterCriteriaVo.setEnrolledFrom(null);
					filterCriteriaVo.setEnrolledTo(null);
				}

				if (!StringUtils.isEmpty(psform.getFromPED()) && !StringUtils.isEmpty(psform.getToPED())) {
					
					filterCriteriaVo.setFromPED(psform.getFromPED());
					criteria.addFilter(
									EmployeeEnrollmentSummaryReportData.FILTER_FROM_PED,
									filterCriteriaVo.getFromPED());
					
					filterCriteriaVo.setToPED(psform.getToPED());
					criteria.addFilter(
									EmployeeEnrollmentSummaryReportData.FILTER_TO_PED,
									filterCriteriaVo.getToPED());
				} else {
					filterCriteriaVo.setFromPED(null);
					filterCriteriaVo.setToPED(null);
				}

				if(psform.getMoneyTypes()!= null && psform.getMoneyTypes().size() > 0){
					String moneyTypes = "";
					if(psform.getMoneyTypeSelected()!= null && !"All".equalsIgnoreCase(psform.getMoneyTypeSelected())){
						moneyTypes = "'"+psform.getMoneyTypeSelected()+"'";
						filterCriteriaVo.setMoneyTypeFilter("Y");
					}else{
						for(int index = 0; index < psform.getMoneyTypes().size(); index++ ){
							if(index == 0){
								moneyTypes = moneyTypes + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
							}else{
								moneyTypes = moneyTypes + ", " + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
							}
						}
						filterCriteriaVo.setMoneyTypeFilter("N");
					}
					
					filterCriteriaVo.setMoneyTypeSelected(moneyTypes);
					criteria.addFilter(
							EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
							filterCriteriaVo.getMoneyTypeSelected());
					criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPE_SELECTED,
						filterCriteriaVo.getMoneyTypeFilter());
					
				}else{
					filterCriteriaVo.setMoneyTypeSelected("''");
					criteria.addFilter(
							EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
							filterCriteriaVo.getMoneyTypeSelected());
					filterCriteriaVo.setMoneyTypeFilter("N");
					criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPE_SELECTED,
						filterCriteriaVo.getMoneyTypeFilter());
				}
			
			
		}
		if (task.equals(RESET_TASK)) {
			if(psform.getMoneyTypes()!= null && psform.getMoneyTypes().size() > 0){
				String moneyTypes = "";
				
					for(int index = 0; index < psform.getMoneyTypes().size(); index++ ){
						if(index == 0){
							moneyTypes = moneyTypes + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
						}else{
							moneyTypes = moneyTypes + ", " + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
						}
					}
				
				filterCriteriaVo.setMoneyTypeSelected(moneyTypes);
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
						filterCriteriaVo.getMoneyTypeSelected());
				
			}else{
				filterCriteriaVo.setMoneyTypeSelected("''");
				criteria.addFilter(
						EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
						filterCriteriaVo.getMoneyTypeSelected());
			}
		}
	}
	
	/**
	 * Populates the filter criteria for Census Summary tab
	 * 
	 * @param task
	 * @param filterCriteriaVo
	 * @param psform
	 * @param criteria
	 */
	public static void populateCensusSummaryTabFilterCriteria(String task,
			FilterCriteriaVo filterCriteriaVo, CensusSummaryReportForm psform,
			ReportCriteria criteria) {
		
		// If task is default then load the filter criterias from session
		// object. This has been done to remember the filter criterias across the tabs.
		if (!(task.equals(FILTER_TASK)) && !(task.equals(RESET_TASK))) {
			
			if (!StringUtils.isEmpty(filterCriteriaVo.getEmploymentStatus())) {
				criteria.addFilter(CensusSummaryReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());

				psform.setStatus(filterCriteriaVo.getEmploymentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getLastName())) {
				criteria.addFilter(CensusSummaryReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());

				psform.setNamePhrase(filterCriteriaVo.getLastName());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSegment())) {
				criteria.addFilter(CensusSummaryReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());

				psform.setSegment(filterCriteriaVo.getSegment());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getDivision())) {
				criteria.addFilter(CensusSummaryReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());

				psform.setDivision(filterCriteriaVo.getDivision());
			}

			if (!(filterCriteriaVo.getSsn().isEmpty()
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
				criteria.addFilter(CensusSummaryReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
				psform.setSsnOne(filterCriteriaVo.getSsnOne());
				psform.setSsnTwo(filterCriteriaVo.getSsnTwo());
				psform.setSsnThree(filterCriteriaVo.getSsnThree());
			}
		}

		// If the request is for filter then populate the session object from
		// form bean and populate the filter criterias.
		if (task.equals(FILTER_TASK)) {

			// Sync the filterCriteriaVo with form bean values
			if (!StringUtils.isEmpty(psform.getStatus())) {
				filterCriteriaVo.setEmploymentStatus(psform.getStatus());
				criteria.addFilter(CensusSummaryReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());
			} else {
				filterCriteriaVo.setEmploymentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getNamePhrase())) {
				filterCriteriaVo.setLastName(psform.getNamePhrase());
				criteria.addFilter(CensusSummaryReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());
			} else {
				filterCriteriaVo.setLastName(null);
			}

			if (!StringUtils.isEmpty(psform.getSegment())) {
				filterCriteriaVo.setSegment(psform.getSegment());
				criteria.addFilter(CensusSummaryReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
			} else {
				filterCriteriaVo.setSegment(null);
			}

			if (!StringUtils.isEmpty(psform.getDivision())) {
				filterCriteriaVo.setDivision(psform.getDivision());
				criteria.addFilter(CensusSummaryReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
			} else {
				filterCriteriaVo.setDivision(null);
			}

			if (!(psform.getSsn().isEmpty()
					&& StringUtils.isEmpty(psform.getSsnOne())
					&& StringUtils.isEmpty(psform.getSsnTwo())
					&& StringUtils.isEmpty(psform.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(psform.getSsnOne());
				filterCriteriaVo.setSsnTwo(psform.getSsnTwo());
				filterCriteriaVo.setSsnThree(psform.getSsnThree());
				criteria.addFilter(CensusSummaryReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}
		}
	}
	
	/**
	 * Populates the filter criteria for addresses tab
	 * 
	 * @param task
	 * @param filterCriteriaVo
	 * @param psform
	 * @param criteria
	 */
	public static void populateAddressesTabFilterCriteria(String task,
			FilterCriteriaVo filterCriteriaVo,
			ParticipantAddressesReportForm psform, ReportCriteria criteria) {
		
		// If task is default then load the filter criterias from session
		// object. This has been done to remember the filter criterias across the tabs.
		if (!(task.equals(FILTER_TASK)) && !(task.equals(RESET_TASK))) {

			if (!StringUtils.isEmpty(filterCriteriaVo.getEmploymentStatus())) {
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());

				psform.setStatus(filterCriteriaVo.getEmploymentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getLastName())) {
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());

				psform.setNamePhrase(filterCriteriaVo.getLastName());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSegment())) {
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());

				psform.setSegment(filterCriteriaVo.getSegment());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getDivision())) {
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());

				psform.setDivision(filterCriteriaVo.getDivision());
			}

			if (!(filterCriteriaVo.getSsn().isEmpty()
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
				criteria.addFilter(ParticipantAddressesReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
				
				psform.setSsnOne(filterCriteriaVo.getSsnOne());
				psform.setSsnTwo(filterCriteriaVo.getSsnTwo());
				psform.setSsnThree(filterCriteriaVo.getSsnThree());
			}
		}

		// If the task is filter then then set the filters from form bean and
		// load them to the session object and populate the filter criterias.
		if (task.equals(FILTER_TASK)) {

			// Sync the filterCriteriaVo with form bean values
			if (!StringUtils.isEmpty(psform.getStatus())) {
				filterCriteriaVo.setEmploymentStatus(psform.getStatus());
				
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());
			} else {
				filterCriteriaVo.setEmploymentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getNamePhrase())) {
				filterCriteriaVo.setLastName(psform.getNamePhrase());
				
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());
			} else {
				filterCriteriaVo.setLastName(null);
			}

			if (!StringUtils.isEmpty(psform.getSegment())) {
				filterCriteriaVo.setSegment(psform.getSegment());
				
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
			} else {
				filterCriteriaVo.setSegment(null);
			}

			if (!StringUtils.isEmpty(psform.getDivision())) {
				filterCriteriaVo.setDivision(psform.getDivision());
				
				criteria.addFilter(
						ParticipantAddressesReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
			} else {
				filterCriteriaVo.setDivision(null);
			}

			if (!(psform.getSsn().isEmpty()
					&& StringUtils.isEmpty(psform.getSsnOne())
					&& StringUtils.isEmpty(psform.getSsnTwo())
					&& StringUtils.isEmpty(psform.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(psform.getSsnOne());
				filterCriteriaVo.setSsnTwo(psform.getSsnTwo());
				filterCriteriaVo.setSsnThree(psform.getSsnThree());
				
				criteria.addFilter(ParticipantAddressesReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}
		}
	}
	
	/**
	 * Populates the filter criteria for Defferal tab
	 * 
	 * @param task
	 * @param filterCriteriaVo
	 * @param psform
	 * @param criteria
	 */
	public static void populateDefferelTabFilterCriteria(String task,
			FilterCriteriaVo filterCriteriaVo, DeferralReportForm psform,
			ReportCriteria criteria) {

		// If task is default then load the filter criterias from session
		// object. This has been done to remember the filter criterias across the tabs.
		if (!(task.equals(FILTER_TASK)) && !(task.equals(RESET_TASK))) {
			if (!StringUtils.isEmpty(filterCriteriaVo.getEmploymentStatus())) {
				criteria.addFilter(DeferralReportData.FILTER_EMPLOYMENT_STATUS,
						filterCriteriaVo.getEmploymentStatus());

				psform.setEmploymentStatus(filterCriteriaVo
						.getEmploymentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getEnrollmentStatus())) {
				criteria.addFilter(DeferralReportData.FILTER_ENROLLMENT_STATUS,
						filterCriteriaVo.getEnrollmentStatus());

				psform.setEnrollmentStatus(filterCriteriaVo
						.getEnrollmentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getLastName())) {
				criteria.addFilter(DeferralReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());

				psform.setNamePhrase(filterCriteriaVo.getLastName());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSegment())) {
				criteria.addFilter(DeferralReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());

				psform.setSegment(filterCriteriaVo.getSegment());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getDivision())) {
				criteria.addFilter(DeferralReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());

				psform.setDivision(filterCriteriaVo.getDivision());
			}

			if (!(filterCriteriaVo.getSsn().isEmpty()
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
				criteria.addFilter(ParticipantAddressesReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
				psform.setSsnOne(filterCriteriaVo.getSsnOne());
				psform.setSsnTwo(filterCriteriaVo.getSsnTwo());
				psform.setSsnThree(filterCriteriaVo.getSsnThree());
			}
		}

		// If the request is for filter then populate the session object from
		// form bean and populate the filter criterias.
		if (task.equals(FILTER_TASK)) {

			// Sync the filterCriteriaVo with form bean values
			if (!StringUtils.isEmpty(psform.getEmploymentStatus())) {
				filterCriteriaVo.setEmploymentStatus(psform
						.getEmploymentStatus());
				criteria.addFilter(DeferralReportData.FILTER_EMPLOYMENT_STATUS,
						filterCriteriaVo.getEmploymentStatus());
			} else {
				filterCriteriaVo.setEmploymentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getEnrollmentStatus())) {
				filterCriteriaVo.setEnrollmentStatus(psform
						.getEnrollmentStatus());
				criteria.addFilter(DeferralReportData.FILTER_ENROLLMENT_STATUS,
						filterCriteriaVo.getEnrollmentStatus());
			} else {
				filterCriteriaVo.setEnrollmentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getNamePhrase())) {
				filterCriteriaVo.setLastName(psform.getNamePhrase());
				criteria.addFilter(DeferralReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());

			} else {
				filterCriteriaVo.setLastName(null);
			}

			if (!StringUtils.isEmpty(psform.getSegment())) {
				filterCriteriaVo.setSegment(psform.getSegment());
				criteria.addFilter(DeferralReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
			} else {
				filterCriteriaVo.setSegment(null);
			}

			if (!StringUtils.isEmpty(psform.getDivision())) {
				filterCriteriaVo.setDivision(psform.getDivision());
				criteria.addFilter(DeferralReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
			} else {
				filterCriteriaVo.setDivision(null);
			}

			if (!(psform.getSsn().isEmpty()
					&& StringUtils.isEmpty(psform.getSsnOne())
					&& StringUtils.isEmpty(psform.getSsnTwo())
					&& StringUtils.isEmpty(psform.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(psform.getSsnOne());
				filterCriteriaVo.setSsnTwo(psform.getSsnTwo());
				filterCriteriaVo.setSsnThree(psform.getSsnThree());
				criteria.addFilter(DeferralReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}
		}
	}
	
	/**
	 * Populates the filter criteria for vesting tab
	 * 
	 * @param task
	 * @param filterCriteriaVo
	 * @param psform
	 * @param criteria
	 */
	public static void populateVestingTabFilterCriteria(String task,
			FilterCriteriaVo filterCriteriaVo, CensusVestingReportForm psform,
			ReportCriteria criteria) {
		
		//If task is default then load the filter criterias from session object. This has been done
		//to remember the filter criterias across the tabs.
		if (!(task.equals(FILTER_TASK)) && !(task.equals(RESET_TASK))) {
			if (!StringUtils.isEmpty(filterCriteriaVo.getEmploymentStatus())) {
				criteria.addFilter(CensusVestingReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());

				psform.setStatus(filterCriteriaVo.getEmploymentStatus());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getLastName())) {
				criteria.addFilter(CensusVestingReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());

				psform.setNamePhrase(filterCriteriaVo.getLastName());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getSegment())) {
				criteria.addFilter(CensusVestingReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());

				psform.setSegment(filterCriteriaVo.getSegment());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getDivision())) {
				criteria.addFilter(CensusVestingReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());

				psform.setDivision(filterCriteriaVo.getDivision());
			}

			if (!(filterCriteriaVo.getSsn().isEmpty()
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnOne())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnTwo())
					&& StringUtils.isEmpty(filterCriteriaVo.getSsnThree()))) {
				criteria.addFilter(CensusVestingReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
				psform.setSsnOne(filterCriteriaVo.getSsnOne());
				psform.setSsnTwo(filterCriteriaVo.getSsnTwo());
				psform.setSsnThree(filterCriteriaVo.getSsnThree());
			}

			if (!StringUtils.isEmpty(filterCriteriaVo.getAsOfDate())) {
				criteria.addFilter(CensusVestingReportData.FILTER_ASOFDATE,
						filterCriteriaVo.getAsOfDate());
				psform.setAsOfDate(filterCriteriaVo.getAsOfDate());
			}

		}

		// If the request is for filter then populate the session object from
		// form bean and populate the filter criterias.
		if (task.equals(FILTER_TASK)) {

			// Sync the filterCriteriaVo with form bean values
			if (!StringUtils.isEmpty(psform.getStatus())) {
				filterCriteriaVo.setEmploymentStatus(psform.getStatus());
				criteria.addFilter(CensusVestingReportData.FILTER_STATUS,
						filterCriteriaVo.getEmploymentStatus());
			} else {
				filterCriteriaVo.setEmploymentStatus(null);
			}

			if (!StringUtils.isEmpty(psform.getNamePhrase())) {
				filterCriteriaVo.setLastName(psform.getNamePhrase());
				criteria.addFilter(CensusVestingReportData.FILTER_LAST_NAME,
						filterCriteriaVo.getLastName());
			} else {
				filterCriteriaVo.setLastName(null);
			}

			if (!StringUtils.isEmpty(psform.getSegment())) {
				filterCriteriaVo.setSegment(psform.getSegment());
				criteria.addFilter(CensusVestingReportData.FILTER_SEGMENT,
						filterCriteriaVo.getSegment());
			} else {
				filterCriteriaVo.setSegment(null);
			}

			if (!StringUtils.isEmpty(psform.getDivision())) {
				filterCriteriaVo.setDivision(psform.getDivision());
				criteria.addFilter(CensusVestingReportData.FILTER_DIVISION,
						filterCriteriaVo.getDivision());
			} else {
				filterCriteriaVo.setDivision(null);
			}

			if (!(psform.getSsn().isEmpty()
					&& StringUtils.isEmpty(psform.getSsnOne())
					&& StringUtils.isEmpty(psform.getSsnTwo())
					&& StringUtils.isEmpty(psform.getSsnThree()))) {
				filterCriteriaVo.setSsnOne(psform.getSsnOne());
				filterCriteriaVo.setSsnTwo(psform.getSsnTwo());
				filterCriteriaVo.setSsnThree(psform.getSsnThree());
				criteria.addFilter(CensusVestingReportData.FILTER_SSN,
						filterCriteriaVo.getSsn().toString());
			} else {
				filterCriteriaVo.setSsnOne(null);
				filterCriteriaVo.setSsnTwo(null);
				filterCriteriaVo.setSsnThree(null);
			}

			if (!StringUtils.isEmpty(psform.getAsOfDate())) {
				filterCriteriaVo.setAsOfDate(psform.getAsOfDate());
				criteria.addFilter(CensusVestingReportData.FILTER_ASOFDATE,
						filterCriteriaVo.getAsOfDate());
			} else {
				filterCriteriaVo.setAsOfDate(null);
			}
		}
	}
}
