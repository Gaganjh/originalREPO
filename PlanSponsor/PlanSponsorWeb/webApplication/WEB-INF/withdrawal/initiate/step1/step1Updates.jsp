<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>


<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<%-- Defines page level update handlers --%>
<script type="text/javascript">
var lastReason = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)}";

function expandAllSections() {

  showParticipantInformationSection();
  showBasicInformationSection();
  showLoanSection();
}
function collapseAllSections() {
    
  hideParticipantInformationSection();
  hideBasicInformationSection();
  hideLoanSection();
}
 
/**
 * Master function that walks through the step 1 screen after a driver field is updated and ensure
 * the fields are in an appropriate state.
 */
  function updateStep1Page() {

    <%-- Basic Information Section --%>
    updateBasicInformationSection();

    <%-- Handle updates for loan section --%>
    <c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">
         updateLoanSection();
    </c:if>  
  }

/**
 * Master function that walks through the step 1 screen basic information section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
  var originalReason = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)}";
 
  function updateBasicInformationSection() {

    var reason = getSelectedValueById('withdrawalReasonId');
    updateReasonCode();
    <%-- Show/suppress WMSI Paychecks --%>
    if ('${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}' == reason) {
      showNodeById('basicIraServiceProviderCol1Id');
      showNodeById('basicIraServiceProviderCol2Id');
      showNodeById('basicIraServiceProviderCol3Id');
      
    } else {
      hideAndResetNodesById('basicIraServiceProviderCol1Id');
      hideAndResetNodesById('basicIraServiceProviderCol2Id');
      hideAndResetNodesById('basicIraServiceProviderCol3Id');
    }

    <%-- Update payment to --%>
    updatePaymentTo();

    <%-- Show/suppress hardship information --%>
    if ('${requestConstants.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE}' == reason) {
      showAndEnableNodesById('basicHardshipReasonCol1Id');
      showAndEnableNodesById('basicHardshipReasonCol2Id');
      showAndEnableNodesById('basicHardshipReasonCol3Id');
      showAndEnableNodesById('basicHardshipExplanationCol1Id');
      showAndEnableNodesById('basicHardshipExplanationCol2Id');
      showAndEnableNodesById('basicHardshipExplanationCol3Id');
    } else {
      hideResetAndDisableNodesById('basicHardshipReasonCol1Id');
      hideResetAndDisableNodesById('basicHardshipReasonCol2Id');
      hideResetAndDisableNodesById('basicHardshipReasonCol3Id');
      hideResetAndDisableNodesById('basicHardshipExplanationCol1Id');
      hideResetAndDisableNodesById('basicHardshipExplanationCol2Id');
      hideResetAndDisableNodesById('basicHardshipExplanationCol3Id');
    }
  
    <%-- Editability of participant leaving plan --%>
    updateParticipantLeavingPlan();
    
    if ('${requestConstants.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE}' == reason || '${requestConstants.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE}' == reason || '${requestConstants.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE}' == reason || '${requestConstants.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE}' == reason || '${requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}' == reason){
    	document.getElementById("leavingyes").disabled=true ;
    	document.getElementById("leavingyes").checked=false;
    	document.getElementById("leavingno").checked=true;
    	}
    else {
    	document.getElementById("leavingyes").disabled=false ;
   
    }

    <%-- Show/suppress Termination Date --%>
    if ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == reason
       || '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}' == reason) {
      showAndEnableNodesById('basicTerminationDateCol1Id');
      showAndEnableNodesById('basicTerminationDateCol2Id');
      showAndEnableNodesById('basicTerminationDateCol3Id');
    } else {
      hideAndDisableNodesById('basicTerminationDateCol1Id');
      hideAndDisableNodesById('basicTerminationDateCol2Id');
      hideAndDisableNodesById('basicTerminationDateCol3Id');
    }

    <%-- Show/suppress Retirement Date --%>
    if ('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == reason) {
      showAndEnableNodesById('basicRetirementDateCol1Id');
      showAndEnableNodesById('basicRetirementDateCol2Id');
      showAndEnableNodesById('basicRetirementDateCol3Id');
      
      <%-- Use default if retirement date is becoming visible --%>
      if (originalReason != reason) {
        document.getElementById('retirementDateId').value = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.defaultRetirementDate)}";
      }
    } else {
      hideResetAndDisableNodesById('basicRetirementDateCol1Id');
      hideResetAndDisableNodesById('basicRetirementDateCol2Id');
      hideResetAndDisableNodesById('basicRetirementDateCol3Id');
    }
    
    <%-- Show/suppress Disability Date --%>
    if ('${requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}' == reason) {
      showAndEnableNodesById('basicDisabilityDateCol1Id');
      showAndEnableNodesById('basicDisabilityDateCol2Id');
      showAndEnableNodesById('basicDisabilityDateCol3Id');
      
      <%-- Use default if disability date is becoming visible --%>
      if (originalReason != reason) {
        document.getElementById('disabilityDateId').value = '${withdrawalForm.withdrawalRequestUi.defaultDisabilityDate}';
      }
    } else {
      hideResetAndDisableNodesById('basicDisabilityDateCol1Id');
      hideResetAndDisableNodesById('basicDisabilityDateCol2Id');
      hideResetAndDisableNodesById('basicDisabilityDateCol3Id');
    }
    
    <%-- Show/suppress final contribution date --%>
    var wmsiPenchecks = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
    var isTotalWithdrawal = getCheckedValue(document.getElementsByName('withdrawalRequestUi.participantLeavingPlanInd'));
    if ((reason == '${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}')
        || (reason == '${requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}')
        || (reason == '${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}')
        || ((reason == '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}')
          && (wmsiPenchecks != '${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}')
          && (wmsiPenchecks != '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}'))
        || ((reason != '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}')
          && (isTotalWithdrawal == 'true'))) {
      showAndEnableNodesById('basicLastContributionPayrollEndingDateCol1Id');
      showAndEnableNodesById('basicLastContributionPayrollEndingDateCol2Id');
      showAndEnableNodesById('basicLastContributionPayrollEndingDateCol3Id');
      showNodeById('basicFinalContributionDateCol1Id');
      showNodeById('basicFinalContributionDateCol2Id');
      showNodeById('basicFinalContributionDateCol3Id');
      showAndEnableNodesById('basicFinalContributionDateCommentId');
    } else if ((reason == '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}')
          && ((wmsiPenchecks == '${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}')
          || (wmsiPenchecks == '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}'))) {

      <%-- Need to handle WMSI / PenChecks special default --%>
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol1Id');
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol2Id');
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol3Id');
      hideNodeById('basicFinalContributionDateCol1Id');
      hideNodeById('basicFinalContributionDateCol2Id');
      hideNodeById('basicFinalContributionDateCol3Id');
      hideAndDisableNodesById('basicFinalContributionDateCommentId');
      document.getElementById('finalContributionDateId').value =  '${withdrawalForm.withdrawalRequestUi.defaultFinalContributionDateForWmsiPenChecks}';
    
    } else { 
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol1Id');
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol2Id');
      hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol3Id');
      hideNodeById('basicFinalContributionDateCol1Id');
      hideNodeById('basicFinalContributionDateCol2Id');
      hideNodeById('basicFinalContributionDateCol3Id');
      hideAndDisableNodesById('basicFinalContributionDateCommentId');
    }
    
    lastReason = originalReason
    	
    <%-- Update original reason --%>
    originalReason = reason;
  }
function ReasonCodeTypes(code, description) {
		this.code=code;
		this.description=description;
}
var reasonCodeArray = new Array(); 
<c:forEach items="${withdrawalReasons}" var="reasonCodeTypes">
reasonCodeArray['${reasonCodeTypes.code}'] 
  = new ReasonCodeTypes('${reasonCodeTypes.code}',
                 '${reasonCodeTypes.description}');
</c:forEach>


function updateReasonCode() {
	
	 var age = null;
	 var birthDate = document.getElementById('birthDateId');
	 if(!!birthDate && !!birthDate.value){
		    age = getAge(birthDate.value);
	 		if ((!!age ) && (age >= 72|| age >71.000)){
	 			var reasonCodeSelect= document.getElementById('withdrawalReasonId');
	 			 var reasonCode = reasonCodeSelect.value;
	 			 reasonCodeSelect.options.length = 0;
	 			 reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option('-select-', '');
	 			 for (i in reasonCodeArray ){
		    		reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option(reasonCodeArray[i].description, reasonCodeArray[i].code);
		 		}   	
		 	}else if(!!age && age < 72) {
		 		var reasonCodeSelect= document.getElementById('withdrawalReasonId');
		 		 var reasonCode = reasonCodeSelect.value;
		 		 reasonCodeSelect.options.length = 0;
		 		 reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option('-select-', '');
		 		  for (i in reasonCodeArray ){
					 var code = reasonCodeArray[i].code;
				  	  if(!(code.trim() == 'MD')){
				  		  reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option(reasonCodeArray[i].description, reasonCodeArray[i].code);
				 		}
			 	  }
		    }
	 }else{
		 var reasonCodeSelect= document.getElementById('withdrawalReasonId');
		 var reasonCode = reasonCodeSelect.value;
		 reasonCodeSelect.options.length = 0;
		 reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option('-select-', '');
		 for (i in reasonCodeArray ){
     	 var code = reasonCodeArray[i].code;
		  	  if(!(code.trim() == 'MD')){
		  		 reasonCodeSelect.options[reasonCodeSelect.options.length] = new Option(reasonCodeArray[i].description, reasonCodeArray[i].code);
		 		}
	 	  } 
	 }
	if(!!reasonCodeSelect){	
   
	reasonCodeSelect.value = reasonCode;
	}
}


/**
 * Defines an object to hold payment to information.
 */ 
function PaymentTo(code, description) {
  this.code=code;
  this.description=description;
} 

/**
 * Collection of payment to information.
 */
var paymentToArray = new Array(); 
<c:forEach items="${paymentToTypes}" var="paymentTo">
  paymentToArray['${paymentTo.code}'] 
    = new PaymentTo('${paymentTo.code}',
                   '${paymentTo.description}');
</c:forEach>

/**
 * Handles updates for the payment to.
 */
  function updatePaymentTo() {

    <%-- Grab current value of WMSI / Paychecks --%>
    var wmsiPenchecks = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
    var paymentToSelect = document.getElementById('paymentToId');
    var paymentTo = paymentToSelect.value;

    <%-- Clear select --%>
    paymentToSelect.options.length = 0;

    <%-- If WMSI or Penchecks - only Rollover to IRA is present --%>
    if ((wmsiPenchecks == '${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}') || (wmsiPenchecks == '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}')) { 
    
      <%-- Add Rollover to IRA --%>
      paymentToSelect.options[paymentToSelect.options.length] = new Option(paymentToArray['${requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}'].description, '${requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}');;
      
      <%-- Set selected value --%>
      paymentToSelect.value = '${requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}';
      
    } else {
    
      <c:if test="${fn:length(paymentToTypes) > 1}">                           
        <%-- Add Select --%>
        paymentToSelect.options[paymentToSelect.options.length] = new Option('- select -', '');
      </c:if>
	
	var reason = getSelectedValueById('withdrawalReasonId')
	var isTotalWithdrawal = getCheckedValue(document.getElementsByName('withdrawalRequestUi.participantLeavingPlanInd'));
	if((('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == reason) 
	    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == reason)) && (isTotalWithdrawal == 'true')){
			
		 for (i in paymentToArray) {
			paymentToSelect.options[paymentToSelect.options.length] = new Option(paymentToArray[i].description, paymentToArray[i].code);
        }
	}else if(('${requestConstants.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE}' == reason) 
		    || ('${requestConstants.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE}' == reason)){
		for (i in paymentToArray) {
			  var code = paymentToArray[i].code;
			    if((code.trim() == 'PA')){
				paymentToSelect.options[paymentToSelect.options.length] = new Option(paymentToArray[i].description, paymentToArray[i].code);;
				}
	      }
		
	}else{	
      <%-- Add standard payment to options --%>
      for (i in paymentToArray) {
		  var code = paymentToArray[i].code;
		    if(!(code.trim() == 'M')){
			paymentToSelect.options[paymentToSelect.options.length] = new Option(paymentToArray[i].description, paymentToArray[i].code);;
			}
      }
	  if(paymentTo =='M'){
		  paymentTo = '';
	  }
	}
      <%-- Replace selected value --%>      
      paymentToSelect.value = paymentTo;
    }
  }

var originalWithdrawalReason = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)}";
function updateParticipantLeavingPlan() {
  var reason = getSelectedValueById('withdrawalReasonId');
  if (reason == '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}') {
    <%-- Handle change to mandatory termination - read only with participant leaving plan --%>
    showNodeById('participantLeavingPlanReadOnlySpanId');
    hideNodeById('participantLeavingPlanEditableSpanId');
    setRadioCheckedValue(document.getElementsByName("withdrawalRequestUi.participantLeavingPlanInd"), "true");  
  } else if (originalWithdrawalReason == '${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}') {
    <%-- Handle change from mandatory termination - editable with default participant not leaving plan --%>
    showNodeById('participantLeavingPlanEditableSpanId');
    hideNodeById('participantLeavingPlanReadOnlySpanId');
    setRadioCheckedValue(document.getElementsByName("withdrawalRequestUi.participantLeavingPlanInd"), "false");  
  } else {
    hideNodeById('participantLeavingPlanReadOnlySpanId');
    // Do nothing
  }

  <%-- Update flag --%> 
  originalWithdrawalReason = reason;
}

/**
 * Master function that walks through the step 1 screen loan section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
  function updateLoanSection() {

    updateLoanOption();
    updateIrsDistributionForLoan();
  }

/**
 * Handles updates for the IRS distribution code for loans.
 */
 
 
  function updateLoanOption() {

    <%-- Grab current value --%>
    var loanOptionSelect = document.getElementById('loanOptionId');
    var loanOption = loanOptionSelect.value;
    var participantLeavingPlan = getCheckedValue(document.getElementsByName('withdrawalRequestUi.participantLeavingPlanInd'));
    var paymentTo = getSelectedValueById('paymentToId'); 
	  var withdrawalReason = document.getElementById('withdrawalReasonId').value;
    <%-- Clear select --%>
    loanOptionSelect.options.length = 0;
    
    <%-- Add Closure (always present) --%>
    loanOptionSelect.options[loanOptionSelect.options.length] = new Option(loanOptionArray['${requestConstants.LOAN_CLOSURE_OPTION}'].description, '${requestConstants.LOAN_CLOSURE_OPTION}');
    
    <%-- Check if Rollover option should be suppressed or shown --%>
    if ((withdrawalReason != '') 
        && (!isSimpleWithdrawal() 
          && (participantLeavingPlan == 'false'
          || (participantLeavingPlan == 'true' 
            && (paymentTo != '${requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE}')
            && (paymentTo != '${requestConstants.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE}'))))) {
      <%-- Suppress Rollover (check selected value) --%>
      if (loanOption == '${requestConstants.LOAN_ROLLOVER_OPTION}') {
        loanOption = '';
      }
    } else {
      <%-- Show Rollover --%>
      loanOptionSelect.options[loanOptionSelect.options.length] = new Option(loanOptionArray['${requestConstants.LOAN_ROLLOVER_OPTION}'].description, '${requestConstants.LOAN_ROLLOVER_OPTION}');
    }
    
    <%-- Add Repay (always present) --%>
    loanOptionSelect.options[loanOptionSelect.options.length] = new Option(loanOptionArray['${requestConstants.LOAN_REPAY_OPTION}'].description, '${requestConstants.LOAN_REPAY_OPTION}');;

    <%-- Check if Keep option should be suppressed or shown --%>
    if ((withdrawalReason != '') 
        && !isSimpleWithdrawal()) { 
      <%-- Suppress Keep (check selected value) --%>
      if (loanOption == '${requestConstants.LOAN_KEEP_OPTION}') {
        loanOption = '';
      }
 
    } else {
    
      <%-- Show Keep --%>
      loanOptionSelect.options[loanOptionSelect.options.length] = new Option(loanOptionArray['${requestConstants.LOAN_KEEP_OPTION}'].description, '${requestConstants.LOAN_KEEP_OPTION}');
      if (isSimpleWithdrawal()) {
          loanOption = '';
        }
    }

  <%-- Update selected value if loan option blank --%>
  if (loanOption == '') {
    
    <%-- Default to keep if no withdrawal reason or is simple reason --%>
    if ((withdrawalReason == '') || isSimpleWithdrawal()) {
      loanOption = '${requestConstants.LOAN_KEEP_OPTION}';
    } else { 
      <%-- Otherwise default to close --%>
      loanOption = '${requestConstants.LOAN_CLOSURE_OPTION}';
    }
  }
  
  <%-- Update selected value --%>
  document.getElementById('loanOptionId').value = loanOption;     
}

/**
 * Defines an object to hold loan option information.
 */ 
function LoanOption(code, description) {
  this.code=code;
  this.description=description;
} 

/**
 * Collection of loan option information.
 */
var loanOptionArray = new Array(); 
<c:forEach items="${loanTypes}" var="loanOption">
  loanOptionArray['${loanOption.code}'] 
    = new LoanOption('${loanOption.code}',
                   '${loanOption.description}');
</c:forEach>

/**
 * Defines an object to hold loan option information.
 */ 
function IrsLoansTypes(code, description) {
  this.code=code;
  this.description=description;
}


var irsLoansArray = new Array();
 <c:forEach  items="${irsDistCodesLoansTypes}"  var="irsLoansTypes">
 irsLoansArray['${irsLoansTypes.code}'] 
    = new IrsLoansTypes('${irsLoansTypes.code}',
                   '${irsLoansTypes.description}');
 </c:forEach>
 
 
 var timer = null;


function lookForBirthDateChange()
{
	interval = 8000;
    timer = setInterval(loadingDropdowns, interval); 
}
 

function onclickLookForBirthDateChange()
{
	interval = 2000;
    timer = setInterval(loadingDropdowns, interval); 
}


 	
function loadingDropdowns()
{

     var newBirthDate = document.getElementById('birthDateId');
     updateReasonCode();
	<c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">	
         updateIrsDistributionForLoan();
    </c:if>  
   
	clearInterval(timer);
    timer = null;
	
}			 
 
/**
 * Handles updates for the IRS distribution code for loans.
 */
 function updateIrsDistributionForLoan() {
	var age = null;
    var loanOption = getSelectedValueById('loanOptionId');
    var paymentTo = getSelectedValueById('paymentToId');
	var reason = getSelectedValueById('withdrawalReasonId')
	var dropDownSelectedVal = '${withdrawalForm.withdrawalRequestUi.withdrawalRequest.irsDistributionCodeLoanClosure}';
	var birthDate = document.getElementById('birthDateId');
	 if(!!birthDate && !!birthDate.value){
	   age = getAge(birthDate.value);
	 }
	 if (('${requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE}' == paymentTo) && ('${requestConstants.LOAN_ROLLOVER_OPTION}' == loanOption)){
		 var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		 
		 var irsLoansType = loanIrsDistributionCodeIdSelect.value;
	     loanIrsDistributionCodeIdSelect.options.length = 0;
	     loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		  for (i in irsLoansArray) {
			  var code =irsLoansArray[i].code;
			  if(code.trim() == 'G' ){
			    loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			  }
		  }
      }else if((('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == reason) 
	    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == reason)) 
		  && ('${requestConstants.LOAN_CLOSURE_OPTION}' == loanOption)  && !!age &&  age < 59.5){
		  
		 var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		 var irsLoansType = loanIrsDistributionCodeIdSelect.value;
	     loanIrsDistributionCodeIdSelect.options.length = 0;
		 loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		  for (i in irsLoansArray) {
			  var code =irsLoansArray[i].code;
			  if((code.trim() == '1M' ||  code.trim() == '2M' )){
				loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			  }
      }
	    
	 }else if((('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == reason) 
	    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == reason))
		  && ('${requestConstants.LOAN_CLOSURE_OPTION}' == loanOption)  && !!age && age >= 59.5){
		  
		 var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		 var irsLoansType = loanIrsDistributionCodeIdSelect.value;
	     loanIrsDistributionCodeIdSelect.options.length = 0;
	     loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		  for (i in irsLoansArray) {
			  var code =irsLoansArray[i].code;
			  if((code.trim() == '7M')){
				loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			  }
      }
	    
	 }else if(('${requestConstants.LOAN_CLOSURE_OPTION}' == loanOption)){
		  
		 var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		 var irsLoansType = loanIrsDistributionCodeIdSelect.value;
	     loanIrsDistributionCodeIdSelect.options.length = 0;
		 loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		  for (i in irsLoansArray) {
			  var code = irsLoansArray[i].code;
			  if(!(code.trim() == 1 ||  code.trim() == 2 || code.trim() == 7)){
				loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			  }
      }
	    
	 }
    <%-- Check IRS suppression --%>
    if (('${requestConstants.LOAN_KEEP_OPTION}' == loanOption)  ||  (('${requestConstants.LOAN_REPAY_OPTION}' == loanOption)  
		&& (('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == reason) 
	    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == reason)))) {
      hideResetAndDisableNodesById('loanIrsDistributionCodeCol1Id');
      hideResetAndDisableNodesById('loanIrsDistributionCodeCol2Id');
      hideResetAndDisableNodesById('loanIrsDistributionCodeCol3Id');
    } else {
      showAndEnableNodesById('loanIrsDistributionCodeCol1Id');
      showAndEnableNodesById('loanIrsDistributionCodeCol2Id');
      showAndEnableNodesById('loanIrsDistributionCodeCol3Id');
    }
    if(!!dropDownSelectedVal){
 	   document.getElementById('loanIrsDistributionCodeId').value = dropDownSelectedVal;
   }
  }
  
/**
 * Shows the participant information section.
 */ 
 function showParticipantInformationSection() {
 
    <%-- Hide plus icon --%>
    hideNodeById('participantInformationShowIcon');
    
    <%-- Show minus icon --%>
    showNodeById('participantInformationHideIcon');
 		
 		<%-- Show participant info table --%>
 		showNodeById('participantInformationTable');
    
    <%-- Show participant info table footer --%>
    showNodeById('participantInformationFooter');
 }

/**
 * Hides the participant information section.
 */ 
 function hideParticipantInformationSection() {
 
    <%-- Show plus icon --%>
    showNodeById('participantInformationShowIcon');
    
    <%-- Hide minus icon --%>
    hideNodeById('participantInformationHideIcon');
    
 		
 		<%-- Hide participant info table --%>
 		hideNodeById('participantInformationTable');
    
    <%-- Hide participant info table footer --%>
    hideNodeById('participantInformationFooter');
 }
  
/**
 * Shows the basic information section.
 */ 
 function showBasicInformationSection() {
 
    <%-- Hide plus icon --%>
    hideNodeById('basicInformationShowIcon');
    
    <%-- Show minus icon --%>
    showNodeById('basicInformationHideIcon');
    
    <%-- Show basic info table --%>
    showNodeById('basicInformationTable');
    
    <%-- Show basic info footer --%>
    showNodeById('basicInformationFooter');
 }

/**
 * Hides the basic information section.
 */ 
 function hideBasicInformationSection() {
 
    <%-- Show plus icon --%>
    showNodeById('basicInformationShowIcon');
    
    <%-- Hide minus icon --%>
    hideNodeById('basicInformationHideIcon');
    
    <%-- Hide basic info table --%>
    hideNodeById('basicInformationTable');
    
    <%-- Hide basic info footer --%>
    hideNodeById('basicInformationFooter');
 }
  
/**
 * Shows the loan section.
 */ 
 function showLoanSection() {
 
    <%-- Hide plus icon --%>
    hideNodeById('loanShowIcon');
    
    <%-- Show minus icon --%>
    showNodeById('loanHideIcon');
    
    <%-- Show loan table --%>
    showNodeById('loanTable');
    
    <%-- Show loan footer --%>
    showNodeById('loanFooter');
 }

/**
 * Hides the loan section.
 */ 
 function hideLoanSection() {
 
    <%-- Show plus icon --%>
    showNodeById('loanShowIcon');
    
    <%-- Hide minus icon --%>
    hideNodeById('loanHideIcon');
    
    <%-- Hide loan footer --%>
    hideNodeById('loanTable');
    
    <%-- Hide loan footer --%>
    hideNodeById('loanFooter');
 }

 
 function getAge(dateString) {
  var now = new Date();
  var today = new Date(now.getYear(),now.getMonth(),now.getDate());

  var yearNow = now.getYear();
  var monthNow = now.getMonth();
  var dateNow = now.getDate();

  var dob = new Date(dateString.substring(6,10),
                     dateString.substring(0,2)-1,                   
                     dateString.substring(3,5)                  
                     );

  var yearDob = dob.getYear();
  var monthDob = dob.getMonth();
  var dateDob = dob.getDate();
  var age = {};
  var ageString = "";
  var yearString = "";
  var monthString = "";
  var dayString = "";
  var age = 0.0;

  yearAge = yearNow - yearDob;

  if (monthNow >= monthDob)
    var monthAge = monthNow - monthDob;
  else {
    yearAge--;
    var monthAge = 12 + monthNow -monthDob;
  }

  if (dateNow >= dateDob)
    var dateAge = dateNow - dateDob;
  else {
    monthAge--;
    var dateAge = 31 + dateNow - dateDob;

    if (monthAge < 0) {
      monthAge = 11;
      yearAge--;
    }
  }

  age = {
      years: yearAge,
      months: monthAge,
      days: dateAge
      };

  if ( age.years > 1 ) yearString = " years";
  else yearString = " year";
  if ( age.months> 1 ) monthString = " months";
  else monthString = " month";
  if ( age.days > 1 ) dayString = " days";
  else dayString = " day";
  

  if ( (age.years > 0) && (age.months > 0) && (age.days > 0) ){
	age =  age.years + age.months/12.00;
  }else if ( (age.years == 0) && (age.months == 0) && (age.days > 0) ){
	age =  age.years + age.months/12.00;
  }else if ( (age.years > 0) && (age.months == 0) && (age.days == 0) ){
	age =  age.years + age.months/12.00;
  }else if ( (age.years > 0) && (age.months > 0) && (age.days == 0) ){
	age =  age.years + age.months/12.00;
  }else if ( (age.years == 0) && (age.months > 0) && (age.days > 0) ){
	age =  age.years + age.months/12.00;
 }else if ( (age.years > 0) && (age.months == 0) && (age.days > 0) ){
	age =  age.years + age.months/12.00;
  }else if ( (age.years == 0) && (age.months > 0) && (age.days == 0) ){
	age =  age.years + age.months/12.00;
  }else ageString = "Oops! Could not calculate age!";
  return age;
}

 
</script>