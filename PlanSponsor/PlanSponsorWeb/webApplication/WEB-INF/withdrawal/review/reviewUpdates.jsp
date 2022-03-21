<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee" />
<un:useConstants var="recipientConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient" />
<un:useConstants var="addressConstants" className="com.manulife.pension.service.withdrawal.valueobject.Address" />
<un:useConstants var="stateTaxConstants" className="com.manulife.pension.service.environment.valueobject.StateTaxVO" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />
<un:useConstants var="stateTaxTypes" className="com.manulife.pension.service.environment.valueobject.StateTaxType" />

<%-- Defines page level update handlers --%>
<script type="text/javascript">
var section1onclickvalue = 'false';
$('.radioSelectpar').prop('disabled', true);
$('.radioSelectpa').prop('disabled', true);
$('.radioSelectpaat').prop('disabled', true);
$('.mandatory1').prop('disabled', true);
$('.mandatory2').prop('disabled', true);
$('.mandatory3').prop('disabled', true);
$('.rothfieldsSpecific1').prop('disabled', true);
$('.rothfieldsSpecific2').prop('disabled', true);
$('.nonrothfieldsSpecific1').prop('disabled', true);
$('.nonrothfieldsSpecific2').prop('disabled', true);
$('.nonrothfieldsSpecific3').prop('disabled', true);
$('.inputbox').attr('readonly', true);


$(".radioSelectpaat").click(function(){	
	section1onclickvalue = 'true';
    document.getElementById("taxsection").style.display = "none";
    document.getElementById("titlesec").style.display = "none";
    $(".nonrothfieldsSpecific").hide(); 
    $(".nonrothfieldsSpecific1").hide();
    $(".nonrothfieldsSpecific2").hide();
    $(".nonrothfieldsSpecific3").hide();
    $(".rothfieldsSpecific").show(); 
    $(".rothfieldsSpecific1").show();  
    $(".rothfieldsSpecific2").show();
    $(".inputbox").prop("disabled",true);
    $(".inputbox").val("");
});

$(".radioSelectpar").click(function(){
	 section1onclickvalue = 'true';
	 document.getElementById("taxsection").style.display = "block";
	 document.getElementById("titlesec").style.display = "block";
	 $(".nonrothfieldsSpecific").show();
	 $(".nonrothfieldsSpecific1").show();
     $(".nonrothfieldsSpecific2").show();
     $(".nonrothfieldsSpecific3").show();
     $(".rothfieldsSpecific1").hide();  
     $(".rothfieldsSpecific2").hide();
     $(".rothfieldsSpecific").hide();
     $(".inputbox").prop("disabled",true);
     $(".inputbox").val("");
});

$(".radioSelectpa").click(function(){
	 section1onclickvalue = 'true';
     document.getElementById("taxsection").style.display = "block";
     document.getElementById("titlesec").style.display = "block";
	 $(".nonrothfieldsSpecific").show(); 
	 $(".nonrothfieldsSpecific1").show();
     $(".nonrothfieldsSpecific2").show();
     $(".nonrothfieldsSpecific3").show();
     $(".rothfieldsSpecific").show();
     $(".rothfieldsSpecific1").show();  
     $(".rothfieldsSpecific2").show();
     $(".inputbox").prop("disabled",false);
});
if($('.radioSelectpaat').is(':checked')) {
	 section1onclickvalue = 'true';
    document.getElementById("taxsection").style.display = "none";
    document.getElementById("titlesec").style.display = "none";
	 $(".nonrothfieldsSpecific").hide();
	 $(".nonrothfieldsSpecific1").hide();
    $(".nonrothfieldsSpecific2").hide();
    $(".nonrothfieldsSpecific3").hide();
    $(".rothfieldsSpecific").show(); 
    $(".rothfieldsSpecific1").show();  
    $(".rothfieldsSpecific2").show();
    $(".inputbox").prop("disabled",true);
    $(".inputbox").val("");
}
if($('.radioSelectpar').is(':checked')) {	
	 section1onclickvalue = 'true';
	 document.getElementById("taxsection").style.display = "block";
	 document.getElementById("titlesec").style.display = "block";
	 $(".nonrothfieldsSpecific").show();
	 $(".nonrothfieldsSpecific1").show();
    $(".nonrothfieldsSpecific2").show();
    $(".nonrothfieldsSpecific3").show();
    $(".rothfieldsSpecific").hide();
    $(".rothfieldsSpecific1").hide();  
    $(".rothfieldsSpecific2").hide();
    $(".inputbox").prop("disabled",true);
    $(".inputbox").val("");
	}
	
if($('.radioSelectpa').is(':checked')) {
	 section1onclickvalue = 'true';
	 document.getElementById("taxsection").style.display = "block";
	 document.getElementById("titlesec").style.display = "block";
	 $(".nonrothfieldsSpecific").show();
	 $(".nonrothfieldsSpecific1").show();
    $(".nonrothfieldsSpecific2").show();
    $(".nonrothfieldsSpecific3").show();
    $(".rothfieldsSpecific").show();
    $(".rothfieldsSpecific1").show();  
    $(".rothfieldsSpecific2").show();
    $(".inputbox").prop("disabled",false);
}
if($('.mandatory1').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.mandatory2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.mandatory3').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.rothfieldsSpecific1').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.rothfieldsSpecific2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific1').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific3').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsection").style.display = "none";
		document.getElementById("titlesec").style.display = "none";
	}
}
$(".mandatory1").click(function(){
	 if(section1onclickvalue == "false"){ 
  document.getElementById("taxsection").style.display = "none";
  document.getElementById("titlesec").style.display = "none";
	 }
});

$(".mandatory2").click(function(){
	if(section1onclickvalue == "false"){ 
 document.getElementById("taxsection").style.display = "none";
 document.getElementById("titlesec").style.display = "none";
	 }
});

$(".mandatory3").click(function(){
	if(section1onclickvalue == "false"){  
  document.getElementById("taxsection").style.display = "none";
	document.getElementById("titlesec").style.display = "none";
	 }
});

$(".rothfieldsSpecific1").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsection").style.display = "none";
	  document.getElementById("titlesec").style.display = "none";
	 }
});

$(".rothfieldsSpecific2").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsection").style.display = "none";
	  document.getElementById("titlesec").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific1").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsection").style.display = "none";
	  document.getElementById("titlesec").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific2").click(function(){
		if(section1onclickvalue == "false"){ 
	 		 document.getElementById("taxsection").style.display = "none";
	 		 document.getElementById("titlesec").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific3").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsection").style.display = "none";
	  document.getElementById("titlesec").style.display = "none";
	 }
});
$(document).ready(function(){
  $(":submit,:submit").removeAttr("disabled");  
});



<%-- Define page variables --%>
var previousAmountType = 'INIT';

<c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
  <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
    var hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed = false;
    var oldPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index} = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].paymentMethodCode)}";

    function markPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed() {
      <%-- Radio buttons must use on click to capture changed value - so we need to filter out situations where user clicks on selected value --%>
      var newValue = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'));
      if (newValue != oldPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}) {
        hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed = true;
        // Set dirty flag
        onFieldChange(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'));
        // Update page
        updateReviewPage();
      }

      <%-- Update our copy of the old value --%>
      oldPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index} = newValue;
      
      if(!!document.getElementById("checkPayeeRolloverTypeId[0][0]")){
  	       document.getElementById("checkPayeeRolloverTypeId[0][0]").checked = true;
       }
      if(!!document.getElementById("eftPayeeRolloverTypeId[0][0]")){
  	     document.getElementById("eftPayeeRolloverTypeId[0][0]").checked = true;
  	   }  
    }
    function resetPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed() {
      hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed = false;
    }

  </c:forEach>
</c:forEach>

/**
 * Function that updates the recalculation required flag.
 */
 function setRecalculateRequired() {
    document.getElementById("recalculationRequiredId").value = 'true';
 }

function expandAllSections() {

  showParticipantInformationSection();
  showBasicInformationSection();
  showLoanSection();
  showWithdrawalAmountSection();
  <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
    <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
      showPayeeSection(${recipientStatus.index}, ${payeeStatus.index});
    </c:forEach>
  </c:forEach>
  <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
    showRecipientSection();
  </c:if>
  showNoteSection();
}
function collapseAllSections() {

  hideParticipantInformationSection();
  hideBasicInformationSection();
  hideLoanSection();
  hideWithdrawalAmountSection();
  <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
    <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
      hidePayeeSection(${recipientStatus.index}, ${payeeStatus.index});
    </c:forEach>
  </c:forEach>
  <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
    hideRecipientSection();
  </c:if>
  hideNoteSection();
}

/**
 * Master function that walks through the review screen after a driver field is updated and ensure
 * the fields are in an appropriate state.
 */
  function updateReviewPage() {

    <%-- Handle updates for participant section --%>
    updateReviewParticipantSection();  
    
    <%-- Handle updates for basic section --%>
    updateReviewBasicSection();  
    
    <%-- Handle updates for loan section --%>
    <c:if test ="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='HA' && withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='PD' &&withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='IR' &&withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='DI'}">
    <c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">
      updateReviewLoanSection();
    </c:if>  
    </c:if>
    
    <%-- Handle updates for specific amount section --%>
    updateSpecificAmount();
    
    <%-- Handle updates for withdrawal table --%>
    updateWithdrawalTable();
    
    <%-- Handle updates for tax section --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection}">
      updateStateTax();
    </c:if>
    
    <%-- Update previous federal tax flag after state tax update --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection}">
      updatePreviousFederalTax();
    </c:if>
    
    <%-- Handle updates for payee section --%>
    updateReviewPayeeSection();  

   <%-- Hide Recipient section if Payment To is Plan Trustee --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
      updateRecipientSection();
    </c:if>

    <%-- Handle update for US citizen --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
    	updateUsCitizenSection();
    </c:if>	
    
    <%-- Handle updates for declaration section --%>
    updateReviewDeclarationSection();  

    <%-- Update total --%>    
    updateTotalRequestedAmount();
  }

 /**
  * Updates the previous federal tax flag - we need to do this outside of the state tax
  * update functions as they might be disabled on the initial load.
  */
  var previousFederalTax = 'INIT';
  function updatePreviousFederalTax() {
    
    <%-- Update previous federal tax --%>
    var federalTaxField = document.getElementById("recipientFederalTaxId"); 
    if (federalTaxField != undefined) {
      previousFederalTax = federalTaxField.value;
    } // fi
  }
  
<%-- Hide Recipient section if Payment To is Plan Trustee --%>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
  /**
   * Function that walks through the Recipient section and updates the fields.
   */
  function updateRecipientSection() {

    <%-- Take care of Recipient state --%>
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
	 var countrySelected;
	  var countryElement = document.getElementById('recipientCountryId[${recipientStatus.index}]');
	  if(countryElement == 'select') {
	   	 countrySelected = getSelectedValueById('recipientCountryId[${recipientStatus.index}]');
	   	}else {
	   	  countrySelected = countryElement.value;
	   	}
     
     
      <%-- State dropdown and text boxes are dependant on selection of country --%>
      
      if ((countrySelected == '${addressConstants.USA_COUNTRY_CODE}') || (countrySelected == '')) {
        <%-- USA - show dropdown and default selection --%>
        showAndEnableNodesById("recipientStateDropdownSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientStateInputSpanId[${recipientStatus.index}]");
      } else {
        <%-- Non-USA - show text field and default input --%>
        showAndEnableNodesById("recipientStateInputSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientStateDropdownSpanId[${recipientStatus.index}]");
      }

      <%-- Zip code text boxes are dependant on the selection of country --%>
      if ((countrySelected == '${addressConstants.USA_COUNTRY_CODE}') || (countrySelected == '')) {
        <%-- USA - show two zip code textfields --%>
        showAndEnableNodesById("recipientZipDoubleSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientZipSingleSpanId[${recipientStatus.index}]");
      } else {
        <%-- Non-USA - show single zip code textfield --%>
        showAndEnableNodesById("recipientZipSingleSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientZipDoubleSpanId[${recipientStatus.index}]");
      }


    </c:forEach>
  }
</c:if>

<%-- Hide Recipient section if Payment To is Plan Trustee --%>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
  /**
   * Function that walks through the Recipient section and updates the fields.
   */
  function updateUsCitizenSection() {
   
      <%-- Take care of Recipient state --%>
      <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">

	      var suppressParticipantUsCitizen${recipientStatus.index} = true;

	      <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
	        	<%-- Check each payee --%>	        
	        	suppressParticipantUsCitizen${recipientStatus.index} &= getParticipantUsCitizenSuppression(${recipientStatus.index}, ${payeeStatus.index});
	      </c:forEach>
	      
	      <%-- Suppress if WMSI or Penchecks selected --%>
	      <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
	        if ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
	            || '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))) {
	          suppressParticipantUsCitizen${recipientStatus.index} = true;
	        }
	      </c:if>
	      
	      if (suppressParticipantUsCitizen${recipientStatus.index}) {
	          <%-- Hide Participant US Citizen --%>
	          hideAndDisableNodesById("participantUsCitizenCol1Id[${recipientStatus.index}]");
	          hideAndDisableNodesById("participantUsCitizenCol2Id[${recipientStatus.index}]");
	          hideAndDisableNodesById("participantUsCitizenCol3Id[${recipientStatus.index}]");
	      } else {
	          <%-- Show Participant US Citizen --%>
	          showAndEnableNodesById("participantUsCitizenCol1Id[${recipientStatus.index}]");
	          showAndEnableNodesById("participantUsCitizenCol2Id[${recipientStatus.index}]");
	          showAndEnableNodesById("participantUsCitizenCol3Id[${recipientStatus.index}]");
	      }
 
      </c:forEach>
  }
</c:if>
/**
 * Shows the withdrawal amount section.
 */ 
 function showWithdrawalAmountSection() {
 
    <%-- Hide plus icon --%>
    hideNodeById('moneyTypeShowIcon');
    
    <%-- Show minus icon --%>
    showNodeById('moneyTypeHideIcon');
    
    <%-- Show withdrawal amount table --%>
    showNodeById('withdrawalAmountTable');
    
    <%-- Show withdrawal amount footer --%>
    showNodeById('withdrawalAmountFooter');
 }

/**
 * Hides the withdrawal amount section.
 */ 
 function hideWithdrawalAmountSection() {
 
    <%-- Show plus icon --%>
    showNodeById('moneyTypeShowIcon');
    
    <%-- Hide minus icon --%>
    hideNodeById('moneyTypeHideIcon');
    
    <%-- Hide withdrawal amount table --%>
    hideNodeById('withdrawalAmountTable');
    
    <%-- Hide withdrawal amount footer --%>
    hideNodeById('withdrawalAmountFooter');
 }

/**
 * Master function that walks through the review screen basic section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
  function updateReviewParticipantSection() {
    if (document.getElementById("stateOfResidenceId").value == '${recipientConstants.STATE_OF_RESIDENCE_OUTSIDE_US}') {
      document.getElementById('floatingSummaryStateOfResidenceId').innerHTML = 'Non-US';
    } else {
      document.getElementById('floatingSummaryStateOfResidenceId').innerHTML = document.getElementById("stateOfResidenceId").value; 
    }
  }

/**
 * Master function that walks through the review screen basic section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
  function updateReviewBasicSection() {
    
    <c:if test="${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}">
      if ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
          || '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))) {
        hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol1Id');
        hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol2Id');
        hideAndDisableNodesById('basicLastContributionPayrollEndingDateCol3Id');
        hideNodeById('basicFinalContributionDateCol1Id');
        hideNodeById('basicFinalContributionDateCol2Id');
        hideNodeById('basicFinalContributionDateCol3Id');
        hideAndDisableNodesById('basicFinalContributionDateCommentId');
        document.getElementById('finalContributionDateId').value =  '${withdrawalForm.withdrawalRequestUi.defaultFinalContributionDateForWmsiPenChecks}';
      } else {
        showAndEnableNodesById('basicLastContributionPayrollEndingDateCol1Id');
        showAndEnableNodesById('basicLastContributionPayrollEndingDateCol2Id');
        showAndEnableNodesById('basicLastContributionPayrollEndingDateCol3Id');
        showNodeById('basicFinalContributionDateCol1Id');
        showNodeById('basicFinalContributionDateCol2Id');
        showNodeById('basicFinalContributionDateCol3Id');
        showAndEnableNodesById('basicFinalContributionDateCommentId');
      }  
    </c:if>
  }
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
    timer = setInterval(loadingIrsDistributionDropDown, interval); 
}
 

function onclickLookForBirthDateChange()
{
	interval = 2000;
    timer = setInterval(loadingIrsDistributionDropDown, interval); 
}
 
 
function loadingIrsDistributionDropDown()
{

     var newBirthDate = document.getElementById('birthDateId');


	<c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">
      updateReviewLoanSection();
    </c:if>  
     // do whatever you need to do
		updateReviewPayeeSection();
		

		

	clearInterval(timer);
    timer = null;
	
}			   
					   

   function DeCodeVO(code, description) {
  	  this.code=code;
  	  this.description=description;
  	}
	 var irsWithdrawalDistributionsArray = new Array();
  	 <c:forEach  items="${irsWithdrawalDistributions}"  var="deCodeVO">
	  irsWithdrawalDistributionsArray['${deCodeVO.code}'] 
  	    = new DeCodeVO('${deCodeVO.code}',
  	                   '${deCodeVO.description}');
  	 </c:forEach>
/**
 * Master function that walks through the review screen loan section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
 function updateReviewLoanSection() {
	  var varLoanOption;
	  var age = null;
	  var flag = true;
	  var dropDownSelectedVal = '${withdrawalForm.withdrawalRequestUi.withdrawalRequest.irsDistributionCodeLoanClosure}';
	  var loanIrsDistributionCodeIdValue1;
	  if(!! document.getElementById('loanIrsDistributionCodeId')){
		  loanIrsDistributionCodeIdValue1 = document.getElementById('loanIrsDistributionCodeId').value;
	  }
	  //var loanIrsDistributionCodeIdValue1 = document.getElementById('loanIrsDistributionCodeId').value;
	  var birthDate = document.getElementById('birthDateId');
		 if(!!birthDate && !!birthDate.value){
		   age = getAge(birthDate.value);
		 }
		 var loanOptionElement;
		 if(!! document.getElementById('loanOptionId')){
	   loanOptionElement = document.getElementById('loanOptionId');
	   }
	  if(loanOptionElement == 'select' && !!getSelectedValueById('loanOptionId')) {
	   	 varLoanOption = getSelectedValueById('loanOptionId');
	   	}else {
	   	  varLoanOption = loanOptionElement.value;
	   	}
	    <c:if test="${withdrawalForm.withdrawalRequestUi.showLoanIrsDistributionCode}">
	      if ('${requestConstants.LOAN_KEEP_OPTION}' == varLoanOption) {
	        hideResetAndDisableNodesById('loanIrsDistributionCodeCol1Id');
	        hideResetAndDisableNodesById('loanIrsDistributionCodeCol2Id');
	        hideResetAndDisableNodesById('loanIrsDistributionCodeCol3Id');
	        hideResetAndDisableNodesById('floatingSummaryLoanIrsDistributionCodeCol1Id');
	        hideResetAndDisableNodesById('floatingSummaryLoanIrsDistributionCodeCol2Id');
	      } else {
	        showAndEnableNodesById('loanIrsDistributionCodeCol1Id');
	        showAndEnableNodesById('loanIrsDistributionCodeCol2Id');
	        showAndEnableNodesById('loanIrsDistributionCodeCol3Id');
			<c:if test="${not withdrawalForm.withdrawalRequestUi.viewOnly}">
				<%-- Update floating summary if visible --%>
				if (document.getElementById("loanIrsDistributionCodeId").value == '') {
					document.getElementById('floatingSummaryLoanIrsDistributionCodeId').innerHTML = '';
				} else {
					document.getElementById('floatingSummaryLoanIrsDistributionCodeId').innerHTML = document.getElementById("loanIrsDistributionCodeId").options[document.getElementById("loanIrsDistributionCodeId").selectedIndex].text; 
				}
			</c:if>
			
	        showAndEnableNodesById('floatingSummaryLoanIrsDistributionCodeCol1Id');
	        showAndEnableNodesById('floatingSummaryLoanIrsDistributionCodeCol2Id');
	      }
	    </c:if>
		 
	    if ('${requestConstants.LOAN_REPAY_OPTION}' == varLoanOption  
				&& (('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
					    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}"))) {
	    	hideResetAndDisableNodesById('loanIrsDistributionCodeCol1Id');
	    	hideResetAndDisableNodesById('loanIrsDistributionCodeCol2Id');
	    	hideResetAndDisableNodesById('loanIrsDistributionCodeCol3Id'); 
	 	 }else{
		 	showAndEnableNodesById('loanIrsDistributionCodeCol1Id');
	      	showAndEnableNodesById('loanIrsDistributionCodeCol2Id');
	      	showAndEnableNodesById('loanIrsDistributionCodeCol3Id');
	  	}
	    
	   if (('${requestConstants.LOAN_CLOSURE_OPTION}' == varLoanOption ) 
				&& (('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
					    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")) && (!!age &&  age < 59.5)) {
		   if(!!document.getElementById('loanIrsDistributionCodeId')){
		   var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		    var irsLoansType = loanIrsDistributionCodeIdSelect.value;
		    flag = false
		    loanIrsDistributionCodeIdSelect.options.length = 0;
			loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			for (i in irsLoansArray) {
			  var code =irsLoansArray[i].code;
			  if((code.trim() == '1M' ||  code.trim() == '2M' )){
				loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			  }
	  	    }
			if (!!loanIrsDistributionCodeIdValue1 && (loanIrsDistributionCodeIdValue1.trim() == '1M' || loanIrsDistributionCodeIdValue1.trim() == '2M')) {
				document.getElementById('loanIrsDistributionCodeId').value = loanIrsDistributionCodeIdValue1;
		    }
	   }
	   }else if ((('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
					    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}"))
					    && ('${requestConstants.LOAN_CLOSURE_OPTION}' == varLoanOption) && (!!age &&  age >= 59.5)) {
		   if(!!document.getElementById('loanIrsDistributionCodeId')){
		   var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
		    var irsLoansType = loanIrsDistributionCodeIdSelect.value;
			flag = false
		    loanIrsDistributionCodeIdSelect.options.length = 0;
		    loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			for (i in irsLoansArray) {
				 var code =irsLoansArray[i].code;
				 if((code.trim() == '7M')){
					loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
			     }
		    }
		    if(!!loanIrsDistributionCodeIdValue1 && loanIrsDistributionCodeIdValue1.trim() == '7M') {
				   document.getElementById('loanIrsDistributionCodeId').value = loanIrsDistributionCodeIdValue1;
				 }
		   }
		}else if ((('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
					    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}"))
					    && ('${requestConstants.LOAN_ROLLOVER_OPTION}' == varLoanOption) && ('${requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}")){
		    if(!!document.getElementById('loanIrsDistributionCodeId')){
			var loanIrsDistributionCodeIdSelect = document.getElementById('loanIrsDistributionCodeId');
			 flag = false
			 var irsLoansType = loanIrsDistributionCodeIdSelect.value;
			 loanIrsDistributionCodeIdSelect.options.length = 0;
		     loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			 for (i in irsLoansArray) {
				  var code =irsLoansArray[i].code;
				  if((code.trim() == 'G')) {
					 loanIrsDistributionCodeIdSelect.options[loanIrsDistributionCodeIdSelect.options.length] = new Option(irsLoansArray[i].description, irsLoansArray[i].code);
				  }
			 }
			 if (!!loanIrsDistributionCodeIdValue1 && loanIrsDistributionCodeIdValue1.trim() =='G') {
					if(!!document.getElementById('loanIrsDistributionCodeId')){ 
				 	document.getElementById('loanIrsDistributionCodeId').value = loanIrsDistributionCodeIdValue1;
					}
				  }
		     }
		}
	     if( flag == true && !!dropDownSelectedVal && (('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
				    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")) ){
	 	  	 if(!!document.getElementById('loanIrsDistributionCodeId')){
	    	 document.getElementById('loanIrsDistributionCodeId').value = dropDownSelectedVal;
	    	 }
			 
	     } 
	    }
  
   function updatePayeeIrsDistributionCode() {
	 var varLoanOption;
     var age = null;
     var flag = true;
     var birthdayFlag;
     var birthDate = document.getElementById('birthDateId');
	 if(null!=birthDate && !!birthDate.value){
	   age = getAge(birthDate.value);
	   birthdayFlag = checkPptDOB(birthDate.value);
	 }
	 var payeeIrsDistributionCodeId = null;	 
	  if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
	      payeeIrsDistributionCodeId = document.getElementById('payeeIrsDistributionCodeId[0][0]').value;
	  }
	  
		 if(('${requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
				    || ('${requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")
				    || ('${requestConstants.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")
				    || ('${requestConstants.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")){
						
			if (('${requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}") ||
					('${requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}" 	)){
				 var irsDistributionCodeIdSelect = null ;
				 var irsLoansType = null ;
				if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
					
					irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]');
					irsLoansType = irsDistributionCodeIdSelect.value;
				
			 // var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
		     // var irsLoansType = irsDistributionCodeIdSelect.value;
		     irsDistributionCodeIdSelect.options.length = 0;
	         irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		     for (i in irsWithdrawalDistributionsArray) {
			  var code =irsWithdrawalDistributionsArray[i].code;
			  if((code.trim() == 'G')) {
				 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			  }
		     }
		     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == 'G')) {
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
	   		 }
				}
	      }
		   else if (('${requestConstants.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}"))
		  {
		
		if(!!age &&  age < 59.5){
		   var irsDistributionCodeIdSelect = null;
		   var irsLoansType =  null ;
		   if( !! document.getElementById('payeeIrsDistributionCodeId[0][0]')){
			   irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
			   var irsLoansType = irsDistributionCodeIdSelect.value;
		   
		// var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
		// var irsLoansType = irsDistributionCodeIdSelect.value;
		 irsDistributionCodeIdSelect.options.length = 0;
	     irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		 for (i in irsWithdrawalDistributionsArray) {
			  var code =irsWithdrawalDistributionsArray[i].code;
			  if((code.trim() == '1') ||(code.trim() == '2')) {
				 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			  }
		 } 
	     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '1' || payeeIrsDistributionCodeId.trim() == '2')) {
			  document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
		 }else{
			document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
			
		}}
		}
		else if (!!age && ( age >= 59.5 && birthdayFlag==false)){
			var irsDistributionCodeIdSelect = null;
			var irsLoansType = null;
			if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
		      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
		      irsLoansType = irsDistributionCodeIdSelect.value;
			
		      //var irsLoansType = irsDistributionCodeIdSelect.value;
		      irsDistributionCodeIdSelect.options.length = 0;
	          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		      for (i in irsWithdrawalDistributionsArray) {
			    var code =irsWithdrawalDistributionsArray[i].code;
			    if(code.trim() == '7') {
				    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			     }
		      }
			   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
	   		  }else{
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
			  }
		   }
	    }
		else if (!!age &&  birthdayFlag ==true){
			var irsDistributionCodeIdSelect = null;
			var irsLoansType = null;
			if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
		      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
		      irsLoansType = irsDistributionCodeIdSelect.value;
		      //var irsLoansType = irsDistributionCodeIdSelect.value;
		      irsDistributionCodeIdSelect.options.length = 0;
	          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		      for (i in irsWithdrawalDistributionsArray) {
			    var code =irsWithdrawalDistributionsArray[i].code;
			    if((code.trim() == '7A') ||(code.trim() == '7')) {
				    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			     }
		      }
			   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
	   		  }else{
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
			  }
		   }
	     }
		 } else if (('${requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}"))
		  {
				
				if(!!age &&  age < 59.5){
				   var irsDistributionCodeIdSelect = null;
				   var irsLoansType =  null ;
				   if( !! document.getElementById('payeeIrsDistributionCodeId[0][0]')){
					   irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
					   var irsLoansType = irsDistributionCodeIdSelect.value;
				   
				// var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
				// var irsLoansType = irsDistributionCodeIdSelect.value;
				 irsDistributionCodeIdSelect.options.length = 0;
			     irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				 for (i in irsWithdrawalDistributionsArray) {
					  var code =irsWithdrawalDistributionsArray[i].code;
					  if((code.trim() == '1') ||(code.trim() == '2')) {
						 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					  }
				 } 
			     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '1' || payeeIrsDistributionCodeId.trim() == '2')) {
					  document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
				 }else{
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					
				}}
				}
				else if (!!age && ( age >= 59.5)){
					var irsDistributionCodeIdSelect = null;
					var irsLoansType = null;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
				      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
				      irsLoansType = irsDistributionCodeIdSelect.value;
					
				      //var irsLoansType = irsDistributionCodeIdSelect.value;
				      irsDistributionCodeIdSelect.options.length = 0;
			          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				      for (i in irsWithdrawalDistributionsArray) {
					    var code =irsWithdrawalDistributionsArray[i].code;
					    if(code.trim() == '7') {
						    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					     }
				      }
					   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
			   		  }else{
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					  }
				   }
			    }
				 }
		 } else if(('${requestConstants.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}") 
				 ){
			 if(!!age && (age >= 72 && birthdayFlag ==false)){
				var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
			 if(!!irsDistributionCodeIdSelect){
			     var irsLoansType = irsDistributionCodeIdSelect.value;
			      irsDistributionCodeIdSelect.options.length = 0;
		          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			      for (i in irsWithdrawalDistributionsArray) {
				    var code =irsWithdrawalDistributionsArray[i].code;
				    if(code.trim() == '7') {
					    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
				     }
			      }
				   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7')) {
						document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
					}else{
						document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
				   }
			}
			 }else if (!!age &&  birthdayFlag ==true){
					var irsDistributionCodeIdSelect = null;
					var irsLoansType = null;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
				      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
				      irsLoansType = irsDistributionCodeIdSelect.value;
					
				      //var irsLoansType = irsDistributionCodeIdSelect.value;
				      irsDistributionCodeIdSelect.options.length = 0;
			          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				      for (i in irsWithdrawalDistributionsArray) {
					    var code =irsWithdrawalDistributionsArray[i].code;
					    if((code.trim() == '7A') ||(code.trim() == '7')) {
						    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					     }
				      }
					   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
			   		  }else{
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					  }
				   }
			    }
			 
		 }else if(('${requestConstants.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")
				|| ('${requestConstants.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}")){
			 if(!!age &&  age < 59.5){
				   var irsDistributionCodeIdSelect = null;
				   var irsLoansType =  null ;
				   if( !! document.getElementById('payeeIrsDistributionCodeId[0][0]')){
					   irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
					   var irsLoansType = irsDistributionCodeIdSelect.value;
				 irsDistributionCodeIdSelect.options.length = 0;
			     irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				 for (i in irsWithdrawalDistributionsArray) {
					  var code =irsWithdrawalDistributionsArray[i].code;
					  if((code.trim() == '1') ||(code.trim() == '2')) {
						 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					  }
				 } 
			     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '1' || payeeIrsDistributionCodeId.trim() == '2')) {
					  document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
				 }else{
					document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					
				}}
				}else if (!!age &&  (age >= 59.5 && birthdayFlag ==false)){
					var irsDistributionCodeIdSelect = null;
					var irsLoansType = null;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
				      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
				      irsLoansType = irsDistributionCodeIdSelect.value;
				      irsDistributionCodeIdSelect.options.length = 0;
			          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				      for (i in irsWithdrawalDistributionsArray) {
					    var code =irsWithdrawalDistributionsArray[i].code;
					    if(code.trim() == '7') {
						    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					     }
				      }
					   if (!!payeeIrsDistributionCodeId && payeeIrsDistributionCodeId.trim() == '7') {
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
			   		  }else{
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					  }
				   }
				}
				else if (!!age && birthdayFlag ==true ){
					var irsDistributionCodeIdSelect = null;
					var irsLoansType = null;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
				      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
				      irsLoansType = irsDistributionCodeIdSelect.value;
					
				      //var irsLoansType = irsDistributionCodeIdSelect.value;
				      irsDistributionCodeIdSelect.options.length = 0;
			          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
				      for (i in irsWithdrawalDistributionsArray) {
					    var code =irsWithdrawalDistributionsArray[i].code;
					    if((code.trim() == '7A') ||(code.trim() == '7')) {
						    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
					     }
				      }
					   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
			   		  }else{
							document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
					  }
				   }
			    }
			}else if('${requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}"){
				if (('${requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}") ||
						('${requestConstants.PAYMENT_TO_ROLLOVER_TO_IRA_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}" 	)){
					 var irsDistributionCodeIdSelect = null ;
					 var irsLoansType = null ;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
						
						irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]');
						irsLoansType = irsDistributionCodeIdSelect.value;
					
				 // var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
			     // var irsLoansType = irsDistributionCodeIdSelect.value;
			     irsDistributionCodeIdSelect.options.length = 0;
		         irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			     for (i in irsWithdrawalDistributionsArray) {
				  var code =irsWithdrawalDistributionsArray[i].code;
				  if((code.trim() == 'G')) {
					 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
				  }
			     }
			     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == 'G')) {
						document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
		   		 }
					}
		      }
			   else if (('${requestConstants.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}"))
			  {
				   var irsDistributionCodeIdSelect = null ;
					 var irsLoansType = null ;
					if(!!document.getElementById('payeeIrsDistributionCodeId[0][0]')){
						
						irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]');
						irsLoansType = irsDistributionCodeIdSelect.value;
					
				 // var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId[0][0]') ;
			     // var irsLoansType = irsDistributionCodeIdSelect.value;
			     irsDistributionCodeIdSelect.options.length = 0;
		         irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			     for (i in irsWithdrawalDistributionsArray) {
				  var code =irsWithdrawalDistributionsArray[i].code;
				  if((code.trim() == '3')) {
					 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
				  }
			     }
			     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '3')) {
						document.getElementById('payeeIrsDistributionCodeId[0][0]').value = payeeIrsDistributionCodeId;
		   		 }
					}
			} 
			}
   }
  function checkPptDOB(dob){
	  const pptDob = new Date(dob);
	  const preDob = new Date("01/01/1936");
	  if(Date.parse(preDob) > Date.parse(pptDob)){
		  return true;
	  }else if(Date.parse(preDob) == Date.parse(pptDob)){
		  return true;
	  }else {
		  return false;
	  }
  }
  
  
  /**
   * Function that updates the specific amount field.
   */
   function updateSpecificAmount() {
   	    var varAmtTypeCode;
	  	var amtTypeElement = document.getElementById('amountTypeCodeId');
	   	if(amtTypeElement == 'select') {
	  		varAmtTypeCode = getSelectedValueById('amountTypeCodeId');
	   	}else {
	   		varAmtTypeCode = amtTypeElement.value;
	   	}
	    if ('${requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}' == varAmtTypeCode) {
	     <%-- Show the specific amount entry. --%>
	     showAndEnableNodesById("specificAmountSpan");
	    } else {
	     <%-- Hide the specific amount entry. --%>
	     hideResetAndDisableNodesById("specificAmountSpan");
	    }
   }

  /**
   * Function that updates the withdrawal table.
   */
  function updateWithdrawalTable() {
  
    if (previousAmountType != document.getElementById("amountTypeCodeId").value) {
      if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}') {
        updateWithdrawalTableForSpecificAmount();
      } else if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE}') {
        updateWithdrawalTableForMaximumAvailable();
      } else if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE}') {
        updateWithdrawalTableForPercentMoneyType();
      }else if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE}') {
   	   updateWithdrawalTableForMaximumAvailable();
      } 
    }
    
    <%-- Update previous amount type --%>
    previousAmountType = document.getElementById("amountTypeCodeId").value;
  }

  /**
   * Function that updates the withdrawal table.
   */
  function updateWithdrawalTableForSpecificAmount() {
  
    <%-- Hide percentage column header --%>
    <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
      hideNodeById('requestedPercentageColHeaderSeparatorTdId');
      hideNodeById('requestedPercentageColHeaderTdId');
      hideNodeById('requestedPercentageColFooterSeparatorTdId');
      hideNodeById('requestedPercentageColFooterTdId');
    </c:if>
    hideNodeById('requestedPercentageColHeaderSeparatorId');
    hideNodeById('requestedPercentageColHeaderId');
    hideNodeById('requestedPercentageColFooterSeparatorId');
    hideNodeById('requestedPercentageColFooterId');
    
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
               var="moneyType"
               varStatus="moneyTypeStatus">
               
      <%-- Show specific amount amount column --%>
      showAndEnableNodesById('requestedAmountColSpecificAmountRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
  
      <%-- Hide all percentage columns --%>
      <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
        hideNodeById('requestedPercentageColSeparatorTdRow[${moneyTypeStatus.index}]Id');
        hideNodeById('requestedPercentageColTdRow[${moneyTypeStatus.index}]Id');
      </c:if>
      hideNodeById('requestedPercentageColSeparatorRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideResetAndDisableNodesById('requestedPercentageColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
    </c:forEach>
    
    <%-- Show select total footer --%>
    hideNodeById('totalRequestedAmountSpanSelectId');
    showNodeById('totalRequestedAmountSpanSpecificAmountId');
    hideNodeById('totalRequestedAmountSpanMaximumAvailableId');
    hideNodeById('totalRequestedAmountSpanPercentageMoneyTypeId');
    <%-- Show select total floating summary --%>
    showNodeById('floatingSummaryTotalRequestedAmountSpanSpecificAmountId');
    hideNodeById('floatingSummaryTotalRequestedAmountSpanMaximumAvailableId');
    hideNodeById('floatingSummaryTotalRequestedAmountSpanPercentageMoneyTypeId');
  }
  
  /**
   * Function that updates the withdrawal table when amount type is maximum available.
   */
  function updateWithdrawalTableForMaximumAvailable() {
    
    <%-- Show percentage column header --%>
    <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
      showNodeById('requestedPercentageColHeaderSeparatorTdId');
      showNodeById('requestedPercentageColHeaderTdId');
      showNodeById('requestedPercentageColFooterSeparatorTdId');
      showNodeById('requestedPercentageColFooterTdId');
    </c:if>
    showNodeById('requestedPercentageColHeaderSeparatorId');
    showNodeById('requestedPercentageColHeaderId');
    showNodeById('requestedPercentageColFooterSeparatorId');
    showNodeById('requestedPercentageColFooterId');
    
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
               var="moneyType"
               varStatus="moneyTypeStatus">
               
      <%-- Show maximum available amount column --%>
      hideResetAndDisableNodesById('requestedAmountColSpecificAmountRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColSelectRow[${moneyTypeStatus.index}]Id');
      showNodeById('requestedAmountColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
  
      <%-- Show maximum available percentage column --%>
      <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
        showNodeById('requestedPercentageColSeparatorTdRow[${moneyTypeStatus.index}]Id');
        showNodeById('requestedPercentageColTdRow[${moneyTypeStatus.index}]Id');
      </c:if>
      showNodeById('requestedPercentageColSeparatorRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColSelectRow[${moneyTypeStatus.index}]Id');
      showNodeById('requestedPercentageColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideResetAndDisableNodesById('requestedPercentageColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
    </c:forEach>
    
    <%-- Show select total footer --%>
    hideNodeById('totalRequestedAmountSpanSelectId');
    hideNodeById('totalRequestedAmountSpanSpecificAmountId');
    showNodeById('totalRequestedAmountSpanMaximumAvailableId');
    hideNodeById('totalRequestedAmountSpanPercentageMoneyTypeId');
    
    <%-- Show select total floating summary --%>
    hideNodeById('floatingSummaryTotalRequestedAmountSpanSpecificAmountId');
    showNodeById('floatingSummaryTotalRequestedAmountSpanMaximumAvailableId');
    hideNodeById('floatingSummaryTotalRequestedAmountSpanPercentageMoneyTypeId');
  }
  
  /**
   * Function that updates the withdrawal table when amount type is percent by money type.
   */
  function updateWithdrawalTableForPercentMoneyType() {
        
    <%-- Show percentage column header --%>
    <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
      showNodeById('requestedPercentageColHeaderSeparatorTdId');
      showNodeById('requestedPercentageColHeaderTdId');
      showNodeById('requestedPercentageColFooterSeparatorTdId');
      showNodeById('requestedPercentageColFooterTdId');
    </c:if>
    showNodeById('requestedPercentageColHeaderSeparatorId');
    showNodeById('requestedPercentageColHeaderId');
    showNodeById('requestedPercentageColFooterSeparatorId');
    showNodeById('requestedPercentageColFooterId');
    
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
               var="moneyType"
               varStatus="moneyTypeStatus">
               
      <%-- Show percentage by money type amount column --%>
      hideResetAndDisableNodesById('requestedAmountColSpecificAmountRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      showNodeById('requestedAmountColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
  
      <%-- Show percentage by money type percentage column --%>
      <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
        showNodeById('requestedPercentageColSeparatorTdRow[${moneyTypeStatus.index}]Id');
        showNodeById('requestedPercentageColTdRow[${moneyTypeStatus.index}]Id');
      </c:if>
      showNodeById('requestedPercentageColSeparatorRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      showAndEnableNodesById('requestedPercentageColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
    </c:forEach>
    
    <%-- Show select total footer --%>
    hideNodeById('totalRequestedAmountSpanSelectId');
    hideNodeById('totalRequestedAmountSpanSpecificAmountId');
    hideNodeById('totalRequestedAmountSpanMaximumAvailableId');
    showNodeById('totalRequestedAmountSpanPercentageMoneyTypeId');
    
    <%-- Show select total floating summary --%>
    hideNodeById('floatingSummaryTotalRequestedAmountSpanSpecificAmountId');
    hideNodeById('floatingSummaryTotalRequestedAmountSpanMaximumAvailableId');
    showNodeById('floatingSummaryTotalRequestedAmountSpanPercentageMoneyTypeId');
  }
  
  
 function updatePayeeIrsDistributionCodes(payeeType, recipientStatus,payeeStatus,irsDistCode) {
	 var payeeIrsDistributionCodeId = null;	
	 var age = null;
     var flag = true;
     var birthDate = document.getElementById('birthDateId');
	 if(null!=birthDate && !!birthDate.value){
	   age = getAge(birthDate.value);
	 }

	 if(!!document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']')){
	      payeeIrsDistributionCodeId = document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value;  
		if (payeeType == 'Roth IRA' || payeeType == 'Traditional IRA' 
	          || payeeType == 'Employer Sponsored Qualified Plan'){
			var irsDistributionCodeIdSelect = null ;
			var irsLoansType = null ;
            irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']');
			irsLoansType = irsDistributionCodeIdSelect.value;
		    irsDistributionCodeIdSelect.options.length = 0;
	        irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
			 irsWithdrawalDistributionsArray['G'] = new DeCodeVO('G', 'G  - Rollover');
		   for (i in irsWithdrawalDistributionsArray) {
			  var code =irsWithdrawalDistributionsArray[i].code;
			  if((code.trim() == 'G')) {
				 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			  }
		    }
		    if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == 'G')) {
				document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value = payeeIrsDistributionCodeId;
	   		 }
		    if(!!irsDistCode){
				document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value = 'G'; 
			 }
		}
		else if(('${requestConstants.PAYMENT_TO_MULTIPLE_DESTINATION}' == "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}" 	)){
		  if(!!age && payeeType == 'Participant directly' &&  age < 59.5){
		   var irsDistributionCodeIdSelect = null;
		   var irsLoansType =  null ;
			   irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']') ;
			   var irsLoansType = irsDistributionCodeIdSelect.value;
		   
		// var irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']') ;
		// var irsLoansType = irsDistributionCodeIdSelect.value;
		 irsDistributionCodeIdSelect.options.length = 0;
	     irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		 for (i in irsWithdrawalDistributionsArray) {
			  var code =irsWithdrawalDistributionsArray[i].code;
			  if((code.trim() == '1') ||(code.trim() == '2')) {
				 irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			  }
		 } 
	     if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '1' || payeeIrsDistributionCodeId.trim() == '2')) {
			  document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value = payeeIrsDistributionCodeId;
		 }else{
			document.getElementById('payeeIrsDistributionCodeId[0][0]').value = '';
			
		}
		}else if (!!age && payeeType == 'Participant directly'  && age >= 59.5){
			var irsDistributionCodeIdSelect = null;
			var irsLoansType = null;
		      irsDistributionCodeIdSelect = document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']') ;
		      irsLoansType = irsDistributionCodeIdSelect.value;
			
		      //var irsLoansType = irsDistributionCodeIdSelect.value;
		      irsDistributionCodeIdSelect.options.length = 0;
	          irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option('-select-', '');
		      for (i in irsWithdrawalDistributionsArray) {
			    var code =irsWithdrawalDistributionsArray[i].code;
			    if((code.trim() == '7A') ||(code.trim() == '7')) {
				    irsDistributionCodeIdSelect.options[irsDistributionCodeIdSelect.options.length] = new Option(irsWithdrawalDistributionsArray[i].description, irsWithdrawalDistributionsArray[i].code);
			     }
		      }
			   if (!!payeeIrsDistributionCodeId && (payeeIrsDistributionCodeId.trim() == '7A' || payeeIrsDistributionCodeId.trim() == '7')) {
					document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value = payeeIrsDistributionCodeId;
	   		  }else{
					document.getElementById('payeeIrsDistributionCodeId['+recipientStatus+']['+payeeStatus+']').value = '';
			  }
		}
	    }
	  }	 
   }
   
   
  
  
  /**
   * Function that walks through the Payee section and updates the fields.
   */
  function updateReviewPayeeSection() {
    updatePayeeIrsDistributionCode();
    <%-- Take care of Payee state --%>   
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
      <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
      
	  if(!!document.getElementById('payeeIrsDistributionCodeId[${recipientStatus.index}][${payeeStatus.index}]')){
		updatePayeeIrsDistributionCodes('${payee.paymentToDisplay}','${recipientStatus.index}','${payeeStatus.index}','${payee.withdrawalRequestPayee.irsDistCode}');
	   }
	  
        <%-- Update IRS Distribution code for payee 1 in floating summary --%>
        <c:if test="${!withdrawalForm.withdrawalRequestUi.viewOnly && withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
          <c:if test="${recipientStatus.index == 0 && payeeStatus.index == 0}">
            if (document.getElementById("payeeIrsDistributionCodeId[0][0]").value == '') {
              document.getElementById('floatingSummaryWithdrawalIrsDistributionCodeId').innerHTML = '';
            } else {          	
              		document.getElementById('floatingSummaryWithdrawalIrsDistributionCodeId').innerHTML = document.getElementById("payeeIrsDistributionCodeId[0][0]").options[document.getElementById("payeeIrsDistributionCodeId[0][0]").selectedIndex].text;
            }
          </c:if>        
        </c:if> 
      
        <c:if test="${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}">
          <%-- Check if payee sections should be suppressed --%>
          if ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
              || '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))) {
              
            <%-- Suppress payment method --%>
            hideResetAndDisableNodesById("reviewPayeePaymentMethodCol1Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("reviewPayeePaymentMethodCol2Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("reviewPayeePaymentMethodCol3Id[${recipientStatus.index}][${payeeStatus.index}]");
            <%-- Reset our payment method tracking flag --%>
            oldPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index} = '';
              
            <%-- Suppress bank account type --%>
            hideResetAndDisableNodesById("payeeBankAccountTypeCol1Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("payeeBankAccountTypeCol2Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("payeeBankAccountTypeCol3Id[${recipientStatus.index}][${payeeStatus.index}]");
            
          } else {             
          
            <%-- Show payment method --%>
            showAndEnableNodesById("reviewPayeePaymentMethodCol1Id[${recipientStatus.index}][${payeeStatus.index}]");
            showAndEnableNodesById("reviewPayeePaymentMethodCol2Id[${recipientStatus.index}][${payeeStatus.index}]");
            showAndEnableNodesById("reviewPayeePaymentMethodCol3Id[${recipientStatus.index}][${payeeStatus.index}]");
        </c:if>
  
           var testForAccountType;
           if(document.getElementById('BankAccountTypeId[${recipientStatus.index}][${payeeStatus.index}]')!= 'undefined' && document.getElementById('BankAccountTypeId[${recipientStatus.index}][${payeeStatus.index}]') != null) {
           	   	testForAccountType = document.getElementById('BankAccountTypeId[${recipientStatus.index}][${payeeStatus.index}]').value;
           }else {
           		testForAccountType = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'));
           }
         
         
          <%-- Handle bank account type --%>
          if ('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == testForAccountType) {
            <%-- Show bank account type --%>
            showAndEnableNodesById("payeeBankAccountTypeCol1Id[${recipientStatus.index}][${payeeStatus.index}]");
            showAndEnableNodesById("payeeBankAccountTypeCol2Id[${recipientStatus.index}][${payeeStatus.index}]");
            showAndEnableNodesById("payeeBankAccountTypeCol3Id[${recipientStatus.index}][${payeeStatus.index}]");
          } else {
            <%-- Hide bank account type --%>
            hideResetAndDisableNodesById("payeeBankAccountTypeCol1Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("payeeBankAccountTypeCol2Id[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("payeeBankAccountTypeCol3Id[${recipientStatus.index}][${payeeStatus.index}]");
          }
        <c:if test="${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}">
        }
        </c:if>
      
        <c:if test="${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}">
          <%-- Check if payee sections should be suppressed --%>
          if ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
              || '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))) {
            hideAndResetNodesById('reviewEftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]');
            hideAndResetNodesById('reviewCheckPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]');
          } else {
            showAndEnableNodesById('reviewEftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]');
            showAndEnableNodesById('reviewCheckPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]');
          }
        </c:if>

        <%-- Handle EFT and check payee sections --%>
        if (('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == testForAccountType)
            || ('${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == testForAccountType)) {

        	<%-- Security Enhancements - set payee address to participant address  --%>
        	if (hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed == true) {
        		setDefaultEftPayeeAddressRecipient${recipientStatus.index}Payee${payeeStatus.index}();
        	}        	
        	
          <%-- Show EFT payee subsection and hide check payee subsection --%>
          showAndEnableNodesById("eftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
          hideResetAndDisableNodesById("checkPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
        } else if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == testForAccountType) {
        	
        	<%-- Security Enhancements - set payee address to participant address  --%>
        	if (hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed == true) {
            	setDefaultCheckPayeeAddressRecipient${recipientStatus.index}Payee${payeeStatus.index}();
        	}        	

          <%-- Show Check payee subsection and hide EFT payee subsection --%>
          showAndEnableNodesById("checkPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
          hideResetAndDisableNodesById("eftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
        } else {
          <%-- Hide both subsections - no payment method selected --%>
          hideResetAndDisableNodesById("checkPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
          hideResetAndDisableNodesById("eftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
        }
  
        <%-- Handle check payee if visible --%>
        if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == testForAccountType) {
        
        	var checkPayeeCountry;
        	var chkPayeeCountryElement = document.getElementById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
        	if(chkPayeeCountryElement == 'select') {
        		checkPayeeCountry = getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
        	}else {
        		checkPayeeCountry = chkPayeeCountryElement.value;
        		
        	}
        		
        
           <%-- State dropdown and text are dependant on the selection of country --%>
          if ((checkPayeeCountry == '${addressConstants.USA_COUNTRY_CODE}') || (checkPayeeCountry == '')) {
            <%-- USA - show dropdown and default selection --%>
            showAndEnableNodesById("checkPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("checkPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
          } else {
            <%-- Non-USA - show text field and default input --%>
            showAndEnableNodesById("checkPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("checkPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
          }
          
          <%-- Zip code text boxes are dependant on the selection of country --%>
		  if (${payee.payeeAddressEditable} == true) {  
			  if ((checkPayeeCountry == '${addressConstants.USA_COUNTRY_CODE}') || (checkPayeeCountry == '')) {
				<%-- USA - show two zip code textfields --%>
				showAndEnableNodesById("checkPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show single zip code textfield --%>
				showAndEnableNodesById("checkPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }
		  } else {
            showAndEnableNodesById("checkPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("checkPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
		  }    
		  
    
        
		<%-- Security Enhancements - the send to address above was removed --%>
          <%-- Send to address above checkbox is driven off the hidden property --%>		  
<%--     <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.mailChequeToAddressIndicator}">
            if (document.getElementById('checkPayeeSendToAddressAboveId[${recipientStatus.index}][${payeeStatus.index}]').value == 'true') {
              document.getElementById('checkPayeeSendToAddressAboveCheckboxId[${recipientStatus.index}][${payeeStatus.index}]').checked = true;
            } else {
              document.getElementById('checkPayeeSendToAddressAboveCheckboxId[${recipientStatus.index}][${payeeStatus.index}]').checked = false;
            }
            <c:if test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
    			document.getElementById('checkPayeeSendToAddressAboveCheckboxId[${recipientStatus.index}][${payeeStatus.index}]').disabled = true;
    		</c:if>
          </c:if>  
		--%>  
        }
  
        <%-- Handle eft payee if visible --%>
        if (('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == testForAccountType)
            || ('${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == testForAccountType)) {
            
            	 var countryEftSelected;
				  var countryEftElement = document.getElementById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
				  if(countryEftElement == 'select') {
				   	 countryEftSelected = getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
				   	}else {
				   	  countryEftSelected = countryEftElement.value;
				   	}

          <%-- State dropdown and text are dependant on the selection of country --%>
          if ((countryEftSelected == '${addressConstants.USA_COUNTRY_CODE}') || (countryEftSelected == '')) {
            <%-- USA - show dropdown and default selection --%>
            showAndEnableNodesById("eftPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("eftPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
          } else {
            <%-- Non-USA - show text field and default input --%>
            showAndEnableNodesById("eftPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
            hideResetAndDisableNodesById("eftPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
          }
          <%-- Zip code text boxes are dependant on the selection of country --%>
		  if (${payee.payeeAddressEditable} == true) { 
			  if ((countryEftSelected == '${addressConstants.USA_COUNTRY_CODE}') || (countryEftSelected == '')) {
				<%-- USA - show two zip code textfields --%>
				showAndEnableNodesById("eftPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show single zip code textfield --%>
				showAndEnableNodesById("eftPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }
		  } else {
				<%-- Non-USA - show single zip code textfield --%>
				showAndEnableNodesById("eftPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");		  
		  }
        }
        <%-- Security Enhancements - reset --%>
		resetPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed();

      </c:forEach>
    </c:forEach>
  }

  /**
   * Function that determines if the participant US citizen row should be suppressed for the specified recipient and payee.
   */
   function getParticipantUsCitizenSuppression(recipientIndex, payeeIndex) {

      
      if( document.getElementById('BankAccountTypeId')!= 'undefined' && document.getElementById('BankAccountTypeId') != null ) {
      	paymentCode = document.getElementById('BankAccountTypeId').value;
      } else {
      	var paymentCode = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[' + recipientIndex + '].payees[' + payeeIndex + '].paymentMethodCode'));
      }
     
      var countryCode;
      var countryCodeElement;
      if (('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == paymentCode) || ('${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == paymentCode)) {
        countryCodeElement = document.getElementById('eftPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
        if(countryCodeElement == 'select') {
        	countryCode = getSelectedValueById('eftPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
        }else {
        	countryCode = countryCodeElement.value;
        }
      } else if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == paymentCode) {
      	countryCodeElement = document.getElementById('checkPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
        if(countryCodeElement == 'select') {
        	countryCode = getSelectedValueById('checkPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
        }else {
        	countryCode = countryCodeElement.value;
        }
     
      } else {
        <%-- Payment code not selected so suppress --%>
        return true;
      }

      <%-- Suppress if country is USA or blank --%>
      if (('${addressConstants.USA_COUNTRY_CODE}' == countryCode) || ('' == countryCode)) {
        return true;
      } else {
        return false;
      }
   }

 /**
   * Function that updates the default eft address
   */
<c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
  <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
    function setDefaultCheckPayeeAddressRecipient${recipientStatus.index}Payee${payeeStatus.index}() {

    	<%-- Security Enhancements --%> 
      <c:if test="${payee.checkPayeeNameEditable}">
	     document.getElementById("checkPayeeNameId[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].organizationName)}"; 
      </c:if>
    
	  <c:if test="${payee.payeeAddressEditable}">
		  document.getElementById("checkPayeeAddressLine1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine1)}';
		 if(!!document.getElementById("checkPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]")) {
			 document.getElementById("checkPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine2)}';
		 }
		  document.getElementById("checkPayeeCityId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedCity)}';
		  document.getElementById("checkPayeeStateDropdownId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
		  document.getElementById("checkPayeeStateInputId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
		  document.getElementById("checkPayeeZipCodeId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode)}';
		  document.getElementById("checkPayeeZipCode1Id[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode1)}";
		  document.getElementById("checkPayeeZipCode2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode2)}';
		  document.getElementById("checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.countryCode)}";
	  </c:if>
    }
  </c:forEach>
</c:forEach>

  /**
   * Function that updates the default eft address
   */
<c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
  <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
    function setDefaultEftPayeeAddressRecipient${recipientStatus.index}Payee${payeeStatus.index}() {

    	<%-- Security Enhancements --%>
      <c:if test="${payee.eftPayeeNameEditable}">
  		document.getElementById("eftPayeeFiNameId[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].organizationName)}";
      </c:if>
      
	  <c:if test="${payee.payeeAddressEditable}">
		  document.getElementById("eftPayeeAddressLine1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine1)}';
		 if(!!document.getElementById("eftPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]")) {
			 document.getElementById("eftPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine2)}';
		 }
		  document.getElementById("eftPayeeCityId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedCity)}';
		  document.getElementById("eftPayeeStateDropdownId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
		  document.getElementById("eftPayeeStateInputId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
		  document.getElementById("eftPayeeZipCodeId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode)}';
		  document.getElementById("eftPayeeZipCode1Id[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode1)}";
		  document.getElementById("eftPayeeZipCode2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode2)}';
		  document.getElementById("eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.countryCode)}";
		  ;
	   </c:if>
    }
  </c:forEach>
</c:forEach>
  
/**
 * Master function that walks through the review screen declaration section after a driver field is updated 
 * and ensure the fields are in an appropriate state.
 */
  function updateReviewDeclarationSection() {

    <c:if test="${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}">
      if ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
          || '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))) {
        showAndEnableNodesById('iraDeclarationHeaderCol1Id');
        showAndEnableNodesById('iraDeclarationHeaderCol2Id');
        showAndEnableNodesById('iraDeclarationHeaderCol3Id');
        showAndEnableNodesById('iraDeclarationCol1Id');
        showAndEnableNodesById('iraDeclarationCol2Id');
        showAndEnableNodesById('iraDeclarationCol3Id');
        <c:if test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
        	document.getElementById("wmsiDeclarationId").disabled = true;
        </c:if>
      } else {
        hideAndDisableNodesById('iraDeclarationHeaderCol1Id');
        hideAndDisableNodesById('iraDeclarationHeaderCol2Id');
        hideAndDisableNodesById('iraDeclarationHeaderCol3Id');
        hideAndDisableNodesById('iraDeclarationCol1Id');
        hideAndDisableNodesById('iraDeclarationCol2Id');
        hideAndDisableNodesById('iraDeclarationCol3Id');
      }
    </c:if>
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
    
    <%-- Show basic info table --%>
    hideNodeById('basicInformationTable');
    
    <%-- Show basic info footer --%>
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
  
/**
 * Shows the payee section.
 */ 
 function showPayeeSection(recipientIndex, payeeIndex) {
 
    <%-- Hide plus icon --%>
    hideNodeById('payeeShowIcon[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Show minus icon --%>
    showNodeById('payeeHideIcon[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Show payee table --%>
    showNodeById('paymentInstructionsTable[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Show payee footer --%>
    showNodeById('paymentInstructionsFooter[' + recipientIndex + '][' + payeeIndex + ']');
 }

/**
 * Hides the payee section.
 */ 
 function hidePayeeSection(recipientIndex, payeeIndex) {
 
    <%-- Show plus icon --%>
    showNodeById('payeeShowIcon[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Hide minus icon --%>
    hideNodeById('payeeHideIcon[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Hide payee table --%>
    hideNodeById('paymentInstructionsTable[' + recipientIndex + '][' + payeeIndex + ']');
    
    <%-- Hide payee footer --%>
    hideNodeById('paymentInstructionsFooter[' + recipientIndex + '][' + payeeIndex + ']');
 }
 
/**
 * Shows the Recipient section.
 */ 
 function showRecipientSection() {
 
    <%-- Hide plus icon --%>
    hideNodeById('recipientShowIcon');
    
    <%-- Show minus icon --%>
    showNodeById('recipientHideIcon');
    
    <%-- Show Recipient table --%>
    showNodeById('recipientTable');
    
    <%-- Show Recipient footer --%>
    showNodeById('recipientFooter');
 }

/**
 * Hides the Recipient section.
 */ 
 function hideRecipientSection() {
 
    <%-- Show plus icon --%>
    showNodeById('recipientShowIcon');
    
    <%-- Hide minus icon --%>
    hideNodeById('recipientHideIcon');
    
    <%-- Hide Recipient table --%>
    hideNodeById('recipientTable');
    
    <%-- Hide Recipient footer --%>
    hideNodeById('recipientFooter');
 }
 
/**
 * Shows the note section.
 */ 
 function showNoteSection() {
 
     <%-- Hide plus icon --%>
     hideNodeById('noteShowIcon');
     
     <%-- Show minus icon --%>
     showNodeById('noteHideIcon');
     
     <%-- Show note table --%>
     showNodeById('notesTable');
    
    <%-- Show note footer --%>
    showNodeById('notesFooter');
 }

/**
 * Hides the note section.
 */ 
 function hideNoteSection() {
 
     <%-- Show plus icon --%>
     showNodeById('noteShowIcon');
     
     <%-- Hide minus icon --%>
     hideNodeById('noteHideIcon');
     
     <%-- Hide note table --%>
     hideNodeById('notesTable');
    
     <%-- Hide note footer --%>
     hideNodeById('notesFooter');
 }
 
/**
 * Blanks the rollover plan name if first time focus.
 */ 
 function handleOnFocusRolloverPlanName(field) {
 
    if (isDefined(field)) {
      if (field.value == '${payeeConstants.DEFAULT_ROLLOVER_PLAN_NAME}') {
        field.value = '';
      }
    }
 }
 
/**
 * Function that sets the default send check to indicated address.
 */
 function setDefaultSendCheckToIndicatedAddress(recipientIndex, payeeIndex) {
   setRadioCheckedValue(document.getElementsByName("withdrawalRequestUi.withdrawalRequest.recipients[" + recipientIndex + "].payees[" + payeeIndex + "].mailCheckToAddress"), "false");
 }
 
/**
 * Defines an object to hold state tax information.
 */ 
function StateTax(stateCode, taxType, isTaxRequired, isPercentageFederal, isFreeForm, defaultTaxRate) {
  this.stateCode=stateCode;
  this.taxType=taxType;
  this.isTaxRequired=isTaxRequired;
  this.isPercentageFederal=isPercentageFederal;
  this.isFreeForm=isFreeForm;
  this.defaultTaxRate=defaultTaxRate;
} 

/**
 * Collection of state tax information.
 */
var stateTaxArray = new Array();

<%-- CL 1031784 Begin--%>
<c:set  var="contractIssuedStateCode" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode}' />
<%-- CL 1031784 end--%>

<c:forEach items="${withdrawalForm.stateTaxOptions}" var="stateTax">
  <c:if test="${stateTax.stateTaxType != stateTaxTypes.NONE}">
  var taxCode = '"N"';
  var stateTaxFreeFormYes = 'Y';
  	<c:if test = "${stateTax.taxTypeCode == 'F'}">
  		taxCode = '"Y"';
  	</c:if>
  	<c:if test="${stateTax.stateCode != 'PR' || contractIssuedStateCode != 'PR'}">
	    stateTaxArray['${stateTax.stateCode}'] 
	      = new StateTax('${stateTax.stateCode}',
	                     '${stateTax.stateTaxType}',
	                     ${stateTax.taxRequiredIndicator ? '"Y"' : '"N"'}, 
	                      taxCode,
	                     ${(stateTax.stateTaxType == stateTaxTypes.VOLUNTARY_FREE_FORM) ? '"Y"' : '"N"'},
	                     '${stateTax.defaultTaxRatePercentage}');
  	</c:if>
  	/* CL 131784:Condition for showing state tax as text box when state of residence is Puerto Rico */
  	<c:if test="${stateTax.stateCode == 'PR' && contractIssuedStateCode == 'PR'}">
	  	stateTaxArray['${stateTax.stateCode}'] 
	    = new StateTax('${stateTax.stateCode}',
	                   '${stateTax.stateTaxType}',
	                   ${stateTax.taxRequiredIndicator ? '"Y"' : '"N"'}, 
	                    taxCode,
	                    stateTaxFreeFormYes,
	                   '${stateTax.defaultTaxRatePercentage}');
  	</c:if>
  </c:if>

</c:forEach> 

/** 
 * Function that updates the state tax options.
 */
var previousStateOfResidence = 'INIT';
function updateStateTax() {

  var stateCode = document.getElementById("stateOfResidenceId").value;
  <%-- Need to use original and current state of res... use that to determine if we trigger default setting --%>

  <%-- If state code not present, we hide the state tax row --%>
  if (stateTaxArray[stateCode] == null) {
    hideResetAndDisableNodesById('stateTaxCol1Id');
    hideResetAndDisableNodesById('stateTaxCol2Id');
  } else {
    <%-- Show state tax row --%>
    showAndEnableNodesById('stateTaxCol1Id');  
    showAndEnableNodesById('stateTaxCol2Id');  

    <%-- Handle show suppress of other state tax components --%>
    updateStateTaxInput(stateCode);
    updateStateTaxPostLabel(stateCode);
  }   
  
  <%-- Update state of residence --%>
  previousStateOfResidence = stateCode;
}

/** 
 * Function that handles show-suppress for the state tax post label.
 */
function updateStateTaxInput(stateCode) {

  <%-- Determine if the state tax is freeform --%>
  if (stateTaxArray[stateCode].isFreeForm == 'Y') {
    showAndEnableNodesById('stateTaxTextInputSpanId');  
    
    <%-- Check if we should be redefaulting or not - don't bother if state hasn't changed or this is first time through --%>
    if ((previousStateOfResidence != stateCode) && (previousStateOfResidence != 'INIT')) {
      
      <%-- Check if previous state tax was suppressed --%>
      if (stateTaxArray[previousStateOfResidence] == null) {
        document.getElementById('recipientStateTaxInputId').value = stateTaxArray[stateCode].defaultTaxRate;
      } else if (stateTaxArray[previousStateOfResidence].isFreeForm == 'N') {
        <%-- Check if previous state tax was a dropdown (copy value) --%>
        document.getElementById('recipientStateTaxInputId').value = document.getElementById('recipientStateTaxDropdownId').value;
      }    
    }

    <%-- Hide and reset dropdown after defaulting as we may need selected value --%>    
    hideResetAndDisableNodesById('stateTaxSelectSpanId');
    
  } else {
 
    var previousStateTaxInput = document.getElementById('recipientStateTaxInputId').value;
 
   <c:if test="${not withdrawalForm.withdrawalRequestUi.isParticipantInitiated and not withdrawalForm.withdrawalRequestUi.viewOnly}">  
    var previousStateTaxDropdown = document.getElementById('recipientStateTaxDropdownId').value;
    if (stateTaxArray[previousStateOfResidence] == null) {
      var previousStateTax = '';  
    } else {
      var previousStateTax = (stateTaxArray[previousStateOfResidence].isFreeForm == 'N') ? previousStateTaxDropdown : previousStateTaxInput;
    }
   </c:if>
    showAndEnableNodesById('stateTaxSelectSpanId');  
    hideResetAndDisableNodesById('stateTaxTextInputSpanId');

    <%-- Update state option defaults if state has changed or federal tax has changed --%>
    var federalTax = document.getElementById("recipientFederalTaxId").value;
    if ((previousStateOfResidence != stateCode) || (previousFederalTax != federalTax)) {
  		if(document.getElementById('recipientStateTaxDropdownPOWId') != 'undefined' &&
  				document.getElementById('recipientStateTaxDropdownPOWId') != null) {
  			<%-- do nothing --%>
  		}else {

  		<c:set  var="participantStateOfResidenceCT" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
  		var participantStateOfResidenceCT = '<c:out value="${participantStateOfResidenceCT}"/>';
  				
        <%-- Clear select box --%>
        var field = document.getElementById('recipientStateTaxDropdownId');
        field.options.length = 0;
        
        <%-- Add zero option if tax is not required or federal tax is zero --%>
        if ((stateTaxArray[stateCode].isTaxRequired == 'N') || (federalTax == '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}' && participantStateOfResidenceCT != 'CT')) {
          field.options[0] = new Option('${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}', '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}');
        }else if ((stateTaxArray[stateCode].isTaxRequired == 'N') || (federalTax == '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}' && participantStateOfResidenceCT == 'CT')) {
            field.options[0] = new Option(stateTaxArray[stateCode].defaultTaxRate, stateTaxArray[stateCode].defaultTaxRate);
        }
	   
	   
	   	  <%-- CL 103133 Begin--%>
	  <c:set  var="participantStateOfResidence" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
	  <c:set  var="taxPercentageMinimum" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.taxPercentageMinimum}' />
	  <c:set  var="defaultTaxRatePercentage" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}' />
	  
	  <c:if test="${participantStateOfResidence == 'MS' && taxPercentageMinimum != defaultTaxRatePercentage}">
		field.options[field.options.length] = new Option('${taxPercentageMinimum}', '${taxPercentageMinimum}');
	  </c:if>
	  <%-- CL 103133 End--%>
	  
        
        <%-- Set state tax select defaults unless federal tax is zero --%>
        if (federalTax != '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}') {
          field.options[field.options.length] = new Option(stateTaxArray[stateCode].defaultTaxRate, stateTaxArray[stateCode].defaultTaxRate);
        }
  
        <%-- Check if we need to default unless first time in --%>
        if (previousFederalTax == 'INIT') {
        	if(participantStateOfResidenceCT != 'CT'){
                field.value = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.recipients[0].stateTaxPercent)}";
            }else{
                field.value ="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage)}"
            }
        } else if (stateTaxArray[stateCode].taxType == '${stateTaxTypes.MANDATORY}') {
          <%-- Mandatory should always be redefaulted as there is only one option --%>
          field.selectedIndex = 0;
        } else if ((stateTaxArray[stateCode].taxType == '${stateTaxTypes.VOLUNTARY_FIXED}') || (stateTaxArray[stateCode].taxType == '${stateTaxTypes.OPT_OUT}')) {
          <%-- Zero is always valid - set to first element --%>
          if (previousStateTax == '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}') {
            field.selectedIndex = 0;
          } else if (previousStateTax == stateTaxArray[stateCode].defaultTaxRate) {
            field.selectedIndex = field.options.length - 1;
          } else {
            if (stateTaxArray[stateCode].taxType == '${stateTaxTypes.VOLUNTARY_FIXED}') {
              field.selectedIndex = 0;
            } else {
              field.selectedIndex = field.options.length - 1;
            }
          }
        }
      }
    }
  }
}

/** 
 * Function that handles show-suppress for the state tax post label.
 */
function updateStateTaxPostLabel(stateCode) {

  <%-- Handle show-suppress of state tax post label --%>
  if (stateTaxArray[stateCode].isPercentageFederal == '"Y"') {
  
    showNodeById('stateTaxPercentFederalLabelSpanId');
    hideNodeById('stateTaxPercentWithdrawalLabelSpanId');
  } else {
    showNodeById('stateTaxPercentWithdrawalLabelSpanId');
    hideNodeById('stateTaxPercentFederalLabelSpanId');
     
  }
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