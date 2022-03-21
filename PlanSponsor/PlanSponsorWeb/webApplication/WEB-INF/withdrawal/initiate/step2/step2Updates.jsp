<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Define static constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="declarationConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration" />
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="addressConstants" className="com.manulife.pension.service.withdrawal.valueobject.Address" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />
<un:useConstants var="stateTaxTypes" className="com.manulife.pension.service.environment.valueobject.StateTaxType" />

<%-- Defines page level update handlers --%>
<script type="text/javascript">
//enable disable Rollover remaining balance screen
var section1onclickvalue = 'false';
$(".inputbox").prop("disabled",true);
$(".radioSelectpaat").click(function(){	
	section1onclickvalue = 'true';
    document.getElementById("taxsec").style.display = "none";
    document.getElementById("titlesec1").style.display = "none";
    $(".nonrothfieldsSpecific").hide(); 
    $(".nonrothfieldsSpecific1").hide();
    $(".nonrothfieldsSpecific2").hide();
    $(".nonrothfieldsSpecific3").hide();
    $(".rothfieldsSpecific").show(); 
    $(".rothfieldsSpecific1").show();  
    $(".rothfieldsSpecific2").show();
    $(".inputbox").prop("disabled",true);
    $(".inputbox").val("");
    $(".amounthide").hide();
});

$(".radioSelectpar").click(function(){
	 section1onclickvalue = 'true';
	 document.getElementById("taxsec").style.display = "block";
	 document.getElementById("titlesec1").style.display = "block";
	 $(".nonrothfieldsSpecific").show();
	 $(".nonrothfieldsSpecific1").show();
     $(".nonrothfieldsSpecific2").show();
     $(".nonrothfieldsSpecific3").show();
     $(".rothfieldsSpecific1").hide();  
     $(".rothfieldsSpecific2").hide();
     $(".rothfieldsSpecific").hide();
     $(".inputbox").prop("disabled",true);
     $(".inputbox").val("");
     $(".amounthide").hide();
});

$(".radioSelectpa").click(function(){
	 section1onclickvalue = 'true';
     document.getElementById("taxsec").style.display = "block";
     document.getElementById("titlesec1").style.display = "block";
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
     document.getElementById("taxsec").style.display = "none";
     document.getElementById("titlesec1").style.display = "none";
	 $(".nonrothfieldsSpecific").hide();
	 $(".nonrothfieldsSpecific1").hide();
     $(".nonrothfieldsSpecific2").hide();
     $(".nonrothfieldsSpecific3").hide();
     $(".rothfieldsSpecific").show(); 
     $(".rothfieldsSpecific1").show();  
     $(".rothfieldsSpecific2").show();
     $(".inputbox").prop("disabled",true);
     $(".inputbox").val("");
     $(".amounthide").hide();
}

if($('.radioSelectpar').is(':checked')) {	
	 section1onclickvalue = 'true';
	 document.getElementById("taxsec").style.display = "block";
	 document.getElementById("titlesec1").style.display = "block";
	 $(".nonrothfieldsSpecific").show();
	 $(".nonrothfieldsSpecific1").show();
     $(".nonrothfieldsSpecific2").show();
     $(".nonrothfieldsSpecific3").show();
     $(".rothfieldsSpecific").hide();
     $(".rothfieldsSpecific1").hide();  
     $(".rothfieldsSpecific2").hide();
     $(".inputbox").prop("disabled",true);
     $(".inputbox").val("");
     $(".amounthide").hide();
	}
	
if($('.radioSelectpa').is(':checked')) {
	 section1onclickvalue = 'true';
	 document.getElementById("taxsec").style.display = "block";
	 document.getElementById("titlesec1").style.display = "block";
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
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.mandatory2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.mandatory3').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.rothfieldsSpecific1').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.rothfieldsSpecific2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific1').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific2').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
if($('.nonrothfieldsSpecific3').is(':checked')) {
	 if(section1onclickvalue == "false"){ 
		document.getElementById("taxsec").style.display = "none";
		document.getElementById("titlesec1").style.display = "none";
	}
}
$(".mandatory1").click(function(){
	 if(section1onclickvalue == "false"){ 
   document.getElementById("taxsec").style.display = "none";
   document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".mandatory2").click(function(){
	if(section1onclickvalue == "false"){ 
  document.getElementById("taxsec").style.display = "none";
  document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".mandatory3").click(function(){
	if(section1onclickvalue == "false"){  
   document.getElementById("taxsec").style.display = "none";
	document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".rothfieldsSpecific1").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsec").style.display = "none";
	  document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".rothfieldsSpecific2").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsec").style.display = "none";
	  document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific1").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsec").style.display = "none";
	  document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific2").click(function(){
		if(section1onclickvalue == "false"){ 
	 		 document.getElementById("taxsec").style.display = "none";
	 		 document.getElementById("titlesec1").style.display = "none";
	 }
});

$(".nonrothfieldsSpecific3").click(function(){
	if(section1onclickvalue == "false"){ 
	  document.getElementById("taxsec").style.display = "none";
	  document.getElementById("titlesec1").style.display = "none";
	 }
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
        updateStep2Page();
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
 * Master function that walks through the step 2 screen after a driver field is updated and ensure
 * the fields are in an appropriate state.
 */
  function updateStep2Page() {

    updateSpecificAmount();
    
    updateWithdrawalTable();

    <%-- Handle updates for tax section --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.showTaxWitholdingSection && withdrawalForm.withdrawalRequestUi.recipients[0].showStateTax && not withdrawalForm.withdrawalRequestUi.recipients[0].useFreeFormStateTax}">
      updateStateTax();
    </c:if>

   <%-- Hide Recipient section if Payment To is Plan Trustee --%>
   <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
    updateRecipientSection();
    </c:if>

    <%-- Hide payment instruction section if WMSI or Penchecks was selected --%>
    <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
      updatePayeeInstructionSection();
    </c:if>

    updateTotalRequestedAmount();
  }

/** 
 * Function that updates the state tax options.
 */
var previousFederalTax = 'INIT';
function updateStateTax() {

  <%-- Update state option if federal tax has changed --%>
  var federalTax = document.getElementById("recipientFederalTaxId").value;
  <c:set  var="participantStateOfResidenceCT" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
  var participantStateOfResidenceCT = '<c:out value="${participantStateOfResidenceCT}"/>';
		  
  
  if (previousFederalTax != federalTax) {

    <%-- Clear select box --%>
    var field = document.getElementById('recipientStateTaxDropdownId');
    var currentIndex = field.selectedIndex;
    field.options.length = 0;

    <%-- Add zero option if federal tax is zero --%>
    if (federalTax == '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}' && participantStateOfResidenceCT != 'CT') {
      field.options[0] = new Option('${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}', '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}');
    } else {
      <c:if test="${not withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.taxRequiredIndicator}">
        field.options[0] = new Option('${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}', '${requestUiConstants.TAX_PERCENTAGE_ZERO_OPTION}');
      </c:if>
	  <%-- CL 103133 Begin--%>
	  <c:set  var="participantStateOfResidence" value='${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}' />
	  <c:set  var="taxPercentageMinimum" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.taxPercentageMinimum}' />
	  <c:set  var="defaultTaxRatePercentage" value='${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}' />
	  
	  <c:if test="${participantStateOfResidence == 'MS' && taxPercentageMinimum != defaultTaxRatePercentage}">
		field.options[field.options.length] = new Option('${taxPercentageMinimum}', '${taxPercentageMinimum}');
	  </c:if>
	  <c:if test="${participantStateOfResidence == 'CT' && taxPercentageMinimum != defaultTaxRatePercentage}">
		field.options[field.options.length] = new Option('${taxPercentageMinimum}', '${taxPercentageMinimum}');
	  </c:if>
	  <%-- CL 103133 End--%>	  
      field.options[field.options.length] = new Option('${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}', '${withdrawalForm.withdrawalRequestUi.recipients[0].withdrawalRequestRecipient.stateTaxVo.defaultTaxRatePercentage}');
    }
  
    <%-- Set state tax selected unless we are initializing --%>
    if (previousFederalTax == 'INIT') {
      field.selectedIndex = currentIndex;
    } else {
      <c:choose>
        <c:when test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].withdrawalRequestRecipient.stateTaxVo.stateTaxType == stateTaxTypes.VOLUNTARY_FIXED}">
          field.selectedIndex = 0;
        </c:when>
        <c:otherwise>
          field.selectedIndex = field.options.length - 1;
        </c:otherwise>
      </c:choose>
    }
  
    <%-- Update previous federal tax --%>
    previousFederalTax = federalTax;
  }
}

<%-- Hide Recipient section if Payment To is Plan Trustee --%>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
  /**
   * Function that walks through the Recipient section and updates the fields.
   */
  function updateRecipientSection() {

    <%-- Take care of Recipient state --%>
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">

      <%-- State dropdown and text boxes are dependant on selection of country --%>
      if ((getSelectedValueById('recipientCountryId[${recipientStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
       || (getSelectedValueById('recipientCountryId[${recipientStatus.index}]') == '')) {
        <%-- USA - show dropdown and default selection --%>
        showAndEnableNodesById("recipientStateDropdownSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientStateInputSpanId[${recipientStatus.index}]");
      } else {
        <%-- Non-USA - show text field and default input --%>
        showAndEnableNodesById("recipientStateInputSpanId[${recipientStatus.index}]");
        hideResetAndDisableNodesById("recipientStateDropdownSpanId[${recipientStatus.index}]");
      }

      <%-- Zip code text boxes are dependant on the selection of country --%>
      if ((getSelectedValueById('recipientCountryId[${recipientStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
       || (getSelectedValueById('recipientCountryId[${recipientStatus.index}]') == '')) {
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
	 

 function updatePayeeIrsDistributionCode(payeeType, recipientStatus,payeeStatus,irsDistCode) {
	 var payeeIrsDistributionCodeId = null;	
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
	  }	 
   }


<%-- Hide payment instruction section if WMSI or Penchecks was selected --%>
<c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
  /**
   * Function that walks through the Payee section and updates the fields.
   */
  function updatePayeeInstructionSection() {
    <%-- Take care of Payee state --%>
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
      <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
	  
	    if(!!document.getElementById('payeeIrsDistributionCodeId[${recipientStatus.index}][${payeeStatus.index}]')){
		updatePayeeIrsDistributionCode('${payee.paymentToDisplay}','${recipientStatus.index}','${payeeStatus.index}','${payee.withdrawalRequestPayee.irsDistCode}');
	   }

        <%-- Handle bank account type --%>
        if ('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
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

        <%-- Handle EFT and check payee sections --%>
        if ('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))
            || '${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
          <%-- Set the default eft payee address for this payee --%>
          if (hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed == true) {
        	
        	<%-- Security Enhancements - set payee address to participan address  --%> 
        	setDefaultEftPayeeAddressRecipient${recipientStatus.index}Payee${payeeStatus.index}();

            <%-- Set USA as default country --%>
  <%--          document.getElementById("eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]").value='${addressConstants.USA_COUNTRY_CODE}';  --%>
          }
          <%-- Show EFT payee subsection and hide check payee subsection --%>
          showAndEnableNodesById("eftPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
          hideResetAndDisableNodesById("checkPayeeSectionId[${recipientStatus.index}][${payeeStatus.index}]");
        } else if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
          <%-- Set the default check payee address for this payee --%>
          if (hasPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed == true) {
                <%-- Security Enhancements --%>
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

        <%-- Reset payment method flag --%>
        resetPaymentMethodForRecipient${recipientStatus.index}Payee${payeeStatus.index}Changed();

        <%-- Handle check payee if visible --%>
        if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
			
		  if (${payee.payeeAddressEditable} == true) { 
			
			  <%-- State dropdown and text are dependant on the selection of country --%>
			  if ((getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
			   || (getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '')) {
				<%-- USA - show dropdown and default selection --%>
				showAndEnableNodesById("checkPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show text field and default input --%>
				showAndEnableNodesById("checkPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }
			  
			  <%-- Zip code text boxes are dependant on the selection of country --%>
			  if ((getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
			   || (getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '')) {
				<%-- USA - show two zip code textfields --%>
				showAndEnableNodesById("checkPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show single zip code textfield --%>
				showAndEnableNodesById("checkPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("checkPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }		  
		  }
     
          <%-- Security Enhancements - remove Send to address above checkbox --%>
        }

        <%-- Handle eft payee if visible --%>
        if ('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))
            || '${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {

			if (${payee.payeeAddressEditable} == true) { 
			  <%-- State dropdown and text are dependant on the selection of country --%>
			  if ((getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
			   || (getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '')) {
				<%-- USA - show dropdown and default selection --%>
				showAndEnableNodesById("eftPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show text field and default input --%>
				showAndEnableNodesById("eftPayeeStateInputSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeStateDropdownSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }
			  <%-- Zip code text boxes are dependant on the selection of country --%>
			  if ((getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '${addressConstants.USA_COUNTRY_CODE}')
			   || (getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]') == '')) {
				<%-- USA - show two zip code textfields --%>
				showAndEnableNodesById("eftPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  } else {
				<%-- Non-USA - show single zip code textfield --%>
				showAndEnableNodesById("eftPayeeZipSingleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
				hideResetAndDisableNodesById("eftPayeeZipDoubleSpanId[${recipientStatus.index}][${payeeStatus.index}]");
			  }
			}
        }
      </c:forEach>

      <%-- Participant US Citizen check must be done after payee manipulation --%>
      <c:if test="${withdrawalForm.withdrawalRequestUi.recipients[recipientStatus.index].showParticipantUsCitizenRow}">
      
        <%-- Participant U.S. Citizen radio button is dependant on the selection of country --%>
        var suppressParticipantUsCitizen${recipientStatus.index} = true;
        <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
          <%-- Check each payee --%>
          suppressParticipantUsCitizen${recipientStatus.index} &= getParticipantUsCitizenSuppression(${recipientStatus.index}, ${payeeStatus.index});
        </c:forEach>
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
      </c:if>
    </c:forEach>
  }
</c:if>


  /**
   * Function that updates the specific amount field.
   */
   function updateSpecificAmount() {
      if ('${requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}' == getSelectedValueById('amountTypeCodeId')) {
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
      } else if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE}') {
   	   updateWithdrawalTableForMaximumAvailable();
      }  
      else {
        updateWithdrawalTableForSelectOption();
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
  }
  
  /**
   * Function that updates the withdrawal table when amount type is not selected.
   */
  function updateWithdrawalTableForSelectOption() {

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
               
      <%-- Show select amount column --%>
      hideResetAndDisableNodesById('requestedAmountColSpecificAmountRow[${moneyTypeStatus.index}]Id');
      showNodeById('requestedAmountColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedAmountColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
  
      <%-- Show select percentage column --%>
      <c:if test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
        showNodeById('requestedPercentageColSeparatorTdRow[${moneyTypeStatus.index}]Id');
        showNodeById('requestedPercentageColTdRow[${moneyTypeStatus.index}]Id');
      </c:if>
      showNodeById('requestedPercentageColSeparatorRow[${moneyTypeStatus.index}]Id');
      showNodeById('requestedPercentageColSelectRow[${moneyTypeStatus.index}]Id');
      hideNodeById('requestedPercentageColMaximumAvailableRow[${moneyTypeStatus.index}]Id');
      hideResetAndDisableNodesById('requestedPercentageColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id');
    </c:forEach>
    
    <%-- Show select total footer --%>
    showNodeById('totalRequestedAmountSpanSelectId');
    hideNodeById('totalRequestedAmountSpanSpecificAmountId');
    hideNodeById('totalRequestedAmountSpanMaximumAvailableId');
    hideNodeById('totalRequestedAmountSpanPercentageMoneyTypeId');
  }

  /**
   * Function that determines if the participant US citizen row should be suppressed for the specified recipient and payee.
   */
   function getParticipantUsCitizenSuppression(recipientIndex, payeeIndex) {

      var paymentCode = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[' + recipientIndex + '].payees[' + payeeIndex + '].paymentMethodCode'));
      var countryCode;
      if (('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == paymentCode) || ('${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == paymentCode)) {
        countryCode = getSelectedValueById('eftPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
      } else if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == paymentCode) {
        countryCode = getSelectedValueById('checkPayeeCountryId[' + recipientIndex + '][' + payeeIndex + ']');
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

      <%-- Security Enhancements --%>
      <c:if test="${payee.payeeAddressEditable}">    
	      document.getElementById("checkPayeeAddressLine1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine1)}';
	     if(!!document.getElementById("checkPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]")) {
			 document.getElementById("checkPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine2)}';
	     } document.getElementById("checkPayeeCityId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedCity)}';
	      document.getElementById("checkPayeeStateDropdownId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
	      document.getElementById("checkPayeeStateInputId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
	      document.getElementById("checkPayeeZipCodeId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode)}';
	      document.getElementById("checkPayeeZipCode1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode1)}';
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
      
      <%-- Security Enhancements --%>
      <c:if test="${payee.payeeAddressEditable}">    
	      document.getElementById("eftPayeeAddressLine1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine1)}';
		 if(!!document.getElementById("eftPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]")) {
			 document.getElementById("eftPayeeAddressLine2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedAddressLine2)}';
	     } document.getElementById("eftPayeeCityId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.escapedCity)}';
	      document.getElementById("eftPayeeStateDropdownId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
	      document.getElementById("eftPayeeStateInputId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.stateCode)}';
	      document.getElementById("eftPayeeZipCodeId[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode)}';
	      document.getElementById("eftPayeeZipCode1Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode1)}';
	      document.getElementById("eftPayeeZipCode2Id[${recipientStatus.index}][${payeeStatus.index}]").value='${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.zipCode2)}';
	      document.getElementById("eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]").value="${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientStatus.index].payees[payeeStatus.index].defaultAddress.countryCode)}";
      </c:if>
    }
  </c:forEach>
</c:forEach>

/**
 * Function that updates the recalculation required flag.
 */
 function setRecalculateRequired() {
    document.getElementById("recalculationRequiredId").value = 'true';
 }

function expandAllPage2Sections() {

  showWithdrawalAmountSection();
  <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
    <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
      showPayeeSection(${recipientStatus.index}, ${payeeStatus.index});
    </c:forEach>
  </c:forEach>
  showRecipientSection();
  showNoteSection();
}
function collapseAllPage2Sections() {
  hideWithdrawalAmountSection();
  <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}" var="recipient" varStatus="recipientStatus">
    <c:forEach items="${recipient.payees}" var="payee" varStatus="payeeStatus">
      hidePayeeSection(${recipientStatus.index}, ${payeeStatus.index});
    </c:forEach>
  </c:forEach>
  hideRecipientSection();
  hideNoteSection();
}

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
    
    <%-- Show recipient table --%>
    showNodeById('recipientTable');
    
    <%-- Show recipient footer --%>
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
    
    <%-- Hide recipient table --%>
    hideNodeById('recipientTable');
    
    <%-- Hide recipient footer --%>
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
 
 

 
</script>