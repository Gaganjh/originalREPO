<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- ################### Participant Services Section Content Keys ##################### --%>

<%--  Participant Services Section Title --%>
<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantServicesSectionTitle"/>

<%--  Payroll Support Services sub-section Title --%>
<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_PAYROLL_SUPPORT_SERVICES_SUB_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="payrollSupportServicesSubSectionTitle"/>
                     
<%--  Participant online address changes are permitted for --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantOnlineAddressChanges}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantOnlineAddressChanges"/>

<%-- Participants can specify deferral amounts as --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantDeferrelAmtType}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantDeferrelAmtType"/>		
                     			 
<%-- Participants are allowed to change their deferrals online - First sentence --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantDeferrelsOnline1}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantDeferrelsOnline1"/>	

<%-- Participants are allowed to change their deferrals online - Second sentence --%>					 
<content:contentBean contentId="${csfForm.participantServicesData.participantDeferrelsOnline2}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantDeferrelsOnline2"/>						 

<%-- Participants are allowed to enrolled online --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantsAreAllowedToEnrolledOnline}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsAreAllowedToEnrolledOnline"/>					

<content:contentBean contentId="<%=ContentConstants.OUR_STANDARD_SERVICE_OFFERING_HAS_BEEN_CUSTOMIZED_YOU_HAVE_REQUESTED%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="customizedLabel"/>

<%-- Default deferral scheduled increase - amount and maximum --%>					 
<content:contentBean contentId="${csfForm.participantServicesData.defaultDeferralScheduledIncreaseAmtAndMax}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="defaultDeferralScheduledIncreaseAmtAndMax"/>					 

<%-- Payroll cut off for online deferral and auto enrollment changes --%>
<content:contentBean contentId="${csfForm.participantServicesData.payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges"/>						 

<%--  Financial Transactions sub-section Title --%>
<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_FINANCIAL_TRANSACTIONS_SUB_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="financialTransactionsSubSectionTitle"/>
                     
<%-- Participants can initiate Inter-account transfers online --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantsCanInitiateInterAccountTransfersOnline}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsCanInitiateInterAccountTransfersOnline"/>	

<%-- Participants can initiate online loan requests --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantsCanInitiateOnlineLoanRequests}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsCanInitiateOnlineLoanRequests"/>

<%-- Participants can initiate withdrawal requests --%>
<content:contentBean contentId="${csfForm.participantServicesData.participantsCanInitiateWithdrawalRequests}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsCanInitiateWithdrawalRequests"/>	
 
 <%--  PlanSponser Section Content Keys --%>
 
 <content:contentBean contentId="<%=ContentConstants.PALN_SPONSPER_SERVICES_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planSponsorServicesSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.PLAN_SUPPORT_SERVICES_SUB_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planSupportServicesSubSectionTitle"/>
                     
<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_MONEY_TYPES_LABEL%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="eligibilityMoneyTypesLabel"/>
  
<content:contentBean contentId="<%=ContentConstants.JH_EZINCREASE_OUR_STANDARD_OFFERINGS_LABEL%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="jhEzIncreaseLabel"/>
				 
<content:contentBean contentId="<%=ContentConstants.ELECTRONIC_TRANSACTIONS_SUB_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="electronicTransactionsSubSectionTitle"/>					 

<content:contentBean contentId="${csfForm.planSponsorServicesData.summaryPlanHighlightAvailable}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planHighlights"/>	
                     
<c:if test="${csfForm.planSponsorServicesData.summaryPlanHighlightReviewed !=0}">
<content:contentBean contentId="${csfForm.planSponsorServicesData.summaryPlanHighlightReviewed}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planHighlightsReviewed"/>	
</c:if>
<%-- Notices EDelivery CMA keys: Start--%> 
<content:contentBean contentId="<%=ContentConstants.ELECTRONIC_DELIVERY_FOR_PLAN_NOTICES_AND_STATEMENTS_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="electronicDeliveryForPlanNoticesAndStatementsText"/> 
                     
<content:contentBean contentId="<%=ContentConstants.NOTICES_EDELIVERY_WAW_ON%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="edyWAWOn"/>
                     
<content:contentBean contentId="<%=ContentConstants.NOTICES_EDELIVERY_NOIA_ON%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="edyNIAOn"/>
                     
<content:contentBean contentId="<%=ContentConstants.NOTICES_EDELIVERY_OFF%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="edyOff"/> 
<%-- Notices EDelivery CMA keys: End--%>   
<%-- SEND Service CMA keys: Start--%> 
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_OFF%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sendServiceOff"/>
                     
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ON_LABEL%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sendServiceOn"/>
                     
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ON_EFF_DT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sendServiceOnEffDt"/>
                     
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ON_NOTICES_SELECTED_MSG%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sendServiceOnNoticeTypeSelectedMsg"/>
<%-- SEND Service CMA keys: End--%>                                          
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.eligibilityCalculationService}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="eligibilityCalculationService"/>						 

<content:contentBean contentId="${csfForm.planSponsorServicesData.jhEZstart}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="jhEZstart"/>					

<content:contentBean contentId="${csfForm.planSponsorServicesData.initialEnrollmentDate}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="initialEnrollmentDate"/>
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.directMailEnrollment}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="directMailEnrollment"/>					 

<content:contentBean contentId="${csfForm.planSponsorServicesData.jhEZincrease}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="jhEZincrease"/>	
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.firstScheduledIncrease}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="firstScheduledIncrease"/>						 

<content:contentBean contentId="${csfForm.planSponsorServicesData.initialIncreaseAnniversaryDate}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="initialIncreaseAnniversaryDate"/>	
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.vesting}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="vesting"/>
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.reportVestingPercentages}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="reportVestingPercentages"/>	
                     
<content:contentBean contentId="${csfForm.planSponsorServicesData.payrollFrequency}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="payrollFrequency"/>	                     
                     
<content:contentBean contentId="${csfForm.planSponsorServicesData.allowPayrollPathSubmissions}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="allowPayrollPathSubmissions"/>					 

<content:contentBean contentId="${csfForm.planSponsorServicesData.consentSubsequentAmendments}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="consentSubsequentAmendments"/>	
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.onlineLoans}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="onlineLoans"/>						 

<content:contentBean contentId="${csfForm.planSponsorServicesData.loanChecksMailed}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="loanChecksMailed"/>	
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.loanApprover}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="loanApprover"/>
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.loanPackagesGenerated}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="loanPackagesGenerated"/>
                     
                     					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.loanPriorApproval}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="loanPriorApproval"/>						 

<content:contentBean contentId="${csfForm.planSponsorServicesData.iWithdrawals}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="iWithdrawals"/>	
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.withdrawalIRSSpecialTaxNotices}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="withdrawalIRSSpecialTaxNotices"/>
					 
<content:contentBean contentId="${csfForm.planSponsorServicesData.withdrawalChecksMailed}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="withdrawalChecksMailed"/>	
                     
<content:contentBean contentId="${csfForm.planSponsorServicesData.withdrawalPriorApproval}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="withdrawalPriorApproval"/>					 

<content:contentBean contentId="${csfForm.planSponsorServicesData.withdrawalApproval}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="withdrawalApproval"/>	
                     
<content:contentBean contentId="<%=ContentConstants.INVESTMENT_POLICY_STATEMENT_SERVICE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="investmentPolicyStatementService"/>                                                                             

<content:contentBean contentId="<%=ContentConstants.ANNUAL_REVIEW_SERVICE_PROCESSING_DATE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="annualReviewServiceProcessingDate"/>
                     
<content:contentBean contentId="<%=ContentConstants.ONLINE_BENEFICIARY_DESIGNATION_SERVICE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="onlineBeneficiaryDesignationService"/>
                     
<content:contentBean contentId="<%=ContentConstants.PARTICIPANTS_ALLOWED_ONLINE_BENEFICIARY_DESIGNATION_SERVICE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsAllowedOnlineBeneficiaryDesignation"/>

<content:contentBean contentId="<%=ContentConstants.PARTICIPANTS_NOT_ALLOWED_ONLINE_BENEFICIARY_DESIGNATION_SERVICE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantsNotAllowedOnlineBeneficiaryDesignation"/>

<content:contentBean contentId="<%=ContentConstants.CO_FIDUCIARY_FEATURE_VIEW%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="coFiduciaryViewPage"/> 
                                                                                                                       
<content:contentBean contentId="<%=ContentConstants.COFID_WILSHIRE_ADVISER_SERVICE_VIEW%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="coFidWilshireAdviserServiceViewPage"/>  
                     
<content:contentBean contentId="${csfForm.planSponsorServicesData.payrollFeedbackServiceContent}"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="payrollFeedbackServiceContent"/>                       
                    
<!-- Managed Account sub-section --> 
<content:contentBean contentId="<%=ContentConstants.MANAGED_ACCOUNTS_SUB_SECTION_TITLE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="managedAccountsSubSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.PLAN_HAS_MANAGED_ACCOUNT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planHasManagedAccount"/> 
                                                                                                                       
<content:contentBean contentId="<%=ContentConstants.PLAN_HAS_NO_MANAGED_ACCOUNT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planHasNoManagedAccount"/>
                    
<content:contentBean contentId="<%=ContentConstants.PLAN_HAS_MANAGED_ACCOUNT_AS_OF_DATE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="managedAccountAsOfDate"/>

<content:contentBean contentId="<%=ContentConstants.PARTICIPANTS_CAN_OPT_IN_MA%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="partCanOptInMA"/>
