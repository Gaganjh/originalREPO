<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%@page import="com.manulife.pension.service.pif.valueobject.PIFAllowableMoneyTypesVO" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="pifConstants" className="com.manulife.pension.service.pif.util.PIFConstants" />


<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_LOANS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loansText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_JH_DOES_NOT_DO_RECORD_KEEPING_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="jhDoesNotDoRecordKeepingText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_LOAN_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noLoanMoneyTypesText"/>
  
<content:contentBean 
	contentId="80721" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="toUpdateGotoWithdrawalsTab" />
<content:contentBean
contentId="${contentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_LOANS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="applyFeeInformation404a5DisclosurePurposeForLoans" />

 
<script type="text/javascript" >

var maximumAmortizationPeriodGeneralPurpose = '${pifDataForm.planInfoVO.loans.maxAmortizationPeriodGeneral}';
var maximumAmortizationPeriodHardship = '${pifDataForm.planInfoVO.loans.maxAmortizationPeriodHardship}';
var maximumAmortizationPeriodPrimaryResidence = '${pifDataForm.planInfoVO.loans.maxAmortizationPeriodPrimaryResidence}';
var minimumLoanAmount = '${pifDataForm.planInfoVO.loans.minimumLoanAmount}';
var maximumLoanPercentage = '${pifDataForm.planInfoVO.loans.maximumLoanPercentage}';  
var maximumLoanAmount = '${pifDataForm.planInfoVO.loans.maximumLoanAmount}';
var maximumNumberOfOutstandingLoans = '${pifDataForm.pifDataUi.maximumNumberOfOutstandingLoans}';
var maxNumberOfOutstandingLoansIndicator = '${pifDataForm.planInfoVO.loans.maxNumberOfOutstandingLoansIndicator}';
var loanInterestAbovePrimeIndicator = '${pifDataForm.planInfoVO.loans.loanInterestAbovePrimeIndicator}';
var loanInterestRateAbovePrime = '${pifDataForm.planInfoVO.loans.loanInterestRateAbovePrime}';

 /**
  * Handles the click of the allow loans radio buttons.  
  *
  * @param allowedLoan The new selected feild.
  */
 function onLoansAllowedChanged(allowedLoan) {

   var generalPurposeInput = document.getElementById('maximumAmortizationPeriodGeneralPurposeTextId');
   var hardshipInput = document.getElementById('maximumAmortizationPeriodHardshipTextId');
   var primaryResidenceInput = document.getElementById('maximumAmortizationPeriodPrimaryResidenceTextId');
   var minLoanAmountInput = document.getElementById('minimumLoanAmountTextId');
   var maxLoanPercentInput = document.getElementById('maximumLoanPercentageTextId');
   var maxLoanAmountInput = document.getElementById('maximumLoanAmountTextId');
   var maxNumberOfOutstandingLoans = document.getElementById('loans_maxNumberOfOutstandingLoans');
   var maxNumberOfOutstandingLoansInput = document.getElementById('maximumNumberOfOutstandingLoansTextId');
   var maxNoOfOutstandLoansIndicatorInput  = document.getElementById('pifDataUi_planInfoVO_loans_maxNumberOfOutstandingLoansIndicator');
   var loanInterestAbovePrimeIndicatorInput = document.getElementById('pifDataUi_planInfoVO_loans_loanInterestAbovePrimeIndicator');
   var loanInterestRateAbovePrimeInput = document.getElementById('loanInterestRateAbovePrimeTextId');
   
	var maximumAmortizationPeriodGeneralPurposeHiddenId = "#maximumAmortizationPeriodGeneralPurposeHidden";
	var maximumAmortizationPeriodHardshipHiddenId = "#maximumAmortizationPeriodHardshipHidden";
	var maximumAmortizationPeriodPrimaryResidenceHiddenId = "#maximumAmortizationPeriodPrimaryResidenceHidden";
	var minimumLoanAmountHiddenId = "#minimumLoanAmountHidden";
	var maximumLoanPercentageHiddenId = "#maximumLoanPercentageHidden";
	var maximumLoanAmountHiddenId = "#maximumLoanAmountHidden";   
	var loanInterestRateAbovePrimeHidden = "#loans_loanInterestRateAbovePrime";
	var primeLoanIndicatorHidden = "#loans_loanInterestAbovePrimeIndicators";

   if (allowedLoan == '${planDataConstants.YES_CODE}') {
     <%-- Allow Loans - enable textfield, disable backing field and switch to value on record --%>
     generalPurposeInput.value = (maximumAmortizationPeriodGeneralPurpose == '') ? '' : maximumAmortizationPeriodGeneralPurpose;
     generalPurposeInput.disabled = false;
     hardshipInput.value = (maximumAmortizationPeriodHardship == '') ? '' : maximumAmortizationPeriodHardship;
     hardshipInput.disabled = false;
     primaryResidenceInput.value = (maximumAmortizationPeriodPrimaryResidence == '') ? '' : maximumAmortizationPeriodPrimaryResidence;
     primaryResidenceInput.disabled = false;
     minLoanAmountInput.value = (minimumLoanAmount == '') ? '' : minimumLoanAmount;
     minLoanAmountInput.disabled = false;
     maxLoanPercentInput.value = (maximumLoanPercentage == '') ? '' :maximumLoanPercentage;
     maxLoanPercentInput.disabled = false;
     maxLoanAmountInput.value = (maximumLoanAmount == '') ? '' : maximumLoanAmount;
     maxLoanAmountInput.disabled = false;
     maxNumberOfOutstandingLoansInput.disabled = false;
     maxNoOfOutstandLoansIndicatorInput.value = (maxNumberOfOutstandingLoansIndicator == '') ? 'false' : maxNumberOfOutstandingLoansIndicator;  	 
	 if(maxNumberOfOutstandingLoansIndicator == 'true')
     { 
		maxNoOfOutstandLoansIndicatorInput.checked='checked';
		maxNumberOfOutstandingLoansInput.value = '';
		maxNumberOfOutstandingLoansInput.disabled = true;
     }else{ 
		maxNoOfOutstandLoansIndicatorInput.checked='';
		maxNumberOfOutstandingLoansInput.value = '${pifDataForm.pifDataUi.maximumNumberOfOutstandingLoans}';	
		maxNumberOfOutstandingLoansInput.disabled = false;
     }
	 maxNoOfOutstandLoansIndicatorInput.disabled = false;
	 
	 loanInterestRateAbovePrimeInput.disabled = false;
     loanInterestAbovePrimeIndicatorInput.value = (loanInterestAbovePrimeIndicator == '') ? 'false' : loanInterestAbovePrimeIndicator;
     if(loanInterestAbovePrimeIndicator == 'true')
     {
		loanInterestAbovePrimeIndicatorInput.checked='checked';
		loanInterestRateAbovePrimeInput.value = '';
		loanInterestRateAbovePrimeInput.disabled = true;	
     }else{
		loanInterestAbovePrimeIndicatorInput.checked='';
		loanInterestRateAbovePrimeInput.value = '${pifDataForm.planInfoVO.loans.loanInterestRateAbovePrime}';
		loanInterestRateAbovePrimeInput.disabled = false;		
     }
	 loanInterestAbovePrimeIndicatorInput.disabled = false;	

   } else {
     <%-- No Loans - disable textfield, enable backing field and blank value --%>
     generalPurposeInput.value = '';
     generalPurposeInput.disabled=true;
     hardshipInput.value = '';
     hardshipInput.disabled=true;
     primaryResidenceInput.value = '';
     primaryResidenceInput.disabled=true;
     minLoanAmountInput.value = '';
     minLoanAmountInput.disabled=true;
     maxLoanPercentInput.value = '';
     maxLoanPercentInput.disabled=true;
     maxLoanAmountInput.value = '';
     maxLoanAmountInput.disabled=true;
     maxNumberOfOutstandingLoansInput.value = '';
     maxNumberOfOutstandingLoansInput.disabled=true;
     maxNoOfOutstandLoansIndicatorInput.value = 'false';
     maxNoOfOutstandLoansIndicatorInput.checked='';
     maxNoOfOutstandLoansIndicatorInput.disabled=true;
     loanInterestRateAbovePrimeInput.value = '';
     loanInterestRateAbovePrimeInput.disabled=true;	 
     loanInterestAbovePrimeIndicatorInput.value = 'false';
     loanInterestAbovePrimeIndicatorInput.checked='';
     loanInterestAbovePrimeIndicatorInput.disabled=true;

	 $(maximumAmortizationPeriodGeneralPurposeHiddenId).val("");
	 $(maximumAmortizationPeriodHardshipHiddenId).val("");
	 $(maximumAmortizationPeriodPrimaryResidenceHiddenId).val("");
	 $(minimumLoanAmountHiddenId).val("");
	 $(maximumLoanPercentageHiddenId).val("");
	 $(maximumLoanAmountHiddenId).val("");	 
	 $(maxNumberOfOutstandingLoans).val("0");
	 $(primeLoanIndicatorHidden).val("false");
	 $(loanInterestRateAbovePrimeHidden).val("");
	 
   }
   <c:if test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.loans.allowedMoneyTypesForLoans}" var="allowableMoneyTypeLoans" varStatus="count" >

<c:if test="${allowableMoneyTypeLoans.selectedMoneyType ==true}">
			var selectedIndicatorValue = '${allowableMoneyTypeLoans.selectedIndicator}';
			var selIndicatorInput = document.getElementById('pifDataUi_planInfoVO_loans_allowedMoneyTypesForLoans[${count.index}]_selectedIndicator');
		//	var selIndicatorHiddenInput = document.getElementById('loans_allowedMoneyTypesForLoans_selectedIndicator${count.index}');			
   			if (allowedLoan == '${planDataConstants.YES_CODE}') {
     				<%-- Allow Loans - enable textfield, disable backing field and switch to value on record --%>
     				//selIndicatorHiddenInput.value = (selectedIndicatorValue == '') ? 'N' : selectedIndicatorValue;
				if(selectedIndicatorValue == '${planDataConstants.YES_CODE}'){selIndicatorInput.checked='checked';}else{selIndicatorInput.checked='';}
				selIndicatorInput.disabled = false;
			}else{
     			//selIndicatorHiddenInput.value = 'N';
				selIndicatorInput.checked='';
     			selIndicatorInput.disabled=true;
			}
					
</c:if>
</c:forEach>
   </c:if>
 } 
 
function handleAllowedMoneyTypesForLoans(field,count){
	setDirtyFlag();
	allowedMoneyTypesForLoansHiddenId = document.getElementById("loans_allowedMoneyTypesForLoans_selectedIndicator"+count);
	if(field.checked){ 
		allowedMoneyTypesForLoansHiddenId.value = 'Y';
	}else{
		allowedMoneyTypesForLoansHiddenId.value = 'N';
	}
}
function validatePIFCurrencyField(field, min, max, invalidMsg, decimalMsg, minMsg, maxMsg) {
    if (field.value.length==0) {
       return true;
    }
	if (field.value!="0.00") {
	    if (field.value == "******") {
	      // masked currency field.
	      return true;
	    }
		var num = field.value.replace(/\$|\,/g,'');
		if (num.length == 0) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (isNaN(num)) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
			alert(decimalMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num < min) {
			alert(minMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num > max) {
			alert(maxMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else {
			field.value=parseCurrencyInput(num);
			return true;
		}
	}		 
}
  function validatePIFMinLoanAmount(field) {
    return validatePIFCurrencyField(field, 0, 999999999.99, ERR_INVALID_MIN_LOAN_AMOUNT, ERR_DECIMAL_2_minLoan, ERR_INVALID_MIN_LOAN_AMOUNT, ERR_INVALID_MIN_LOAN_AMOUNT);
  }

  function validatePIFMaxLoanAmount(field) {
    return validatePIFCurrencyField(field, 500, 50000.00, ERR_INVALID_MAX_LOAN_AMOUNT, ERR_DECIMAL_2_maxLoan, ERR_INVALID_MAX_LOAN_AMOUNT, ERR_INVALID_MAX_LOAN_AMOUNT);
  }
  function validatePIFMaxLoanPercent(field) {
		var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validate0to50), new Array(ERR_INVALID_MAX_LOAN_PERCENT, ERR_DECIMAL_3_maxLoanPerc, ERR_INVALID_MAX_LOAN_PERCENT), true);
		formatPercentage(field);
		return res;
  } 
  function validatePIFLoanInterestRate(field) {
		var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validate0to9), new Array(ERR_INVALID_LOAD_INTEREST_RATE, ERR_DECIMAL_3_loanInterestRate, ERR_INVALID_LOAD_INTEREST_RATE), true);
		formatPercentage(field);
		return res;
  }   

$(document).ready(function() { 
  <c:if test="${pifDataForm.editMode}">
    onLoansAllowedChanged("${e:forJavaScriptBlock(pifDataForm.planInfoVO.loans.allowLoans)}");  
  </c:if>
});

</script>  

<div id="loansTabDivId" class="borderedDataBox">
<HTML><BODY>
<SCRIPT language=JavaScript1.2 type=text/javascript>
<!--

var submitted=false;

function setButtonAndSubmit(button) {
	 
}

-->
</SCRIPT>
<!--start table content -->
	<table width="729" class="dataTable">
		<TR><TD class=subhead>	
			<DIV class=sectionTitle>
				<c:if test="${pifDataForm.confirmMode}">
					<content:getAttribute beanName="loansText" attribute="text"/>
				</c:if>
			</DIV>
		</TD></TR>
		<TR><TD class=subsubhead>General loan provisions</TD></TR>
	</table> 	  
      <DIV class=evenDataRow>
      <TABLE width="729" class=dataTable style="BORDER-BOTTOM: #cccccc 1px solid">
        <TBODY>
        <TR>
          <TD class=loansLabelColumn>Does the Plan allow loans? </TD>
          <TD class=dataColumn>
	          <c:choose>
	            <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="onLoansAllowedChanged('${planDataConstants.YES_CODE}');setDirtyFlag();" path="planInfoVO.loans.allowLoans" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}


<form:radiobutton disabled="${disableFields}" onclick="onLoansAllowedChanged('${planDataConstants.NO_CODE}');setDirtyFlag();" path="planInfoVO.loans.allowLoans" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



				</c:when>
	            <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.loans.allowLoans =='Y'}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.loans.allowLoans == 'N'}">

					No
</c:if>
	            </c:otherwise>
	          </c:choose>
		  </TD>
		</TR>
        <TR>
          <TD class=loansLabelColumn>Does the plan require spousal consent for distributions?</TD>
          <TD class=dataColumn>
	          <c:choose>
	            <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="true" onclick="setDirtyFlag();" path="planInfoVO.loans.requireSpousalConsent" id="loansRequireSpousalConsentYes" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}




<form:radiobutton disabled="true" onclick="setDirtyFlag();" path="planInfoVO.loans.requireSpousalConsent" id="loansRequireSpousalConsentNo" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}




					&nbsp;&nbsp;&nbsp;<content:getAttribute  beanName="toUpdateGotoWithdrawalsTab" attribute="text"/>
<form:hidden path="planInfoVO.loans.requireSpousalConsent" id="loansRequireSpousalConsentHidden" /><%-- input - name="pifDataForm" --%>

				</c:when>
	            <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.loans.requireSpousalConsent == 'Y'}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.loans.requireSpousalConsent == 'N'}">

					No
</c:if>
	            </c:otherwise>
	          </c:choose>
		  </TD>
		</TR>
        <TR>
          <TD class=loansLabelColumn vAlign=top rowSpan=3>Maximum amortization 
            period per loan type</TD>
          <TD class=dataColumn>
            <DIV class=amortizationLabelColumn>General purpose </DIV>
            <DIV class=amortizationDataColumn>
                <c:choose>
                  <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.loans.maxAmortizationPeriodGeneral" disabled="${disableFields}" maxlength="1" onblur="validateMaximumAmortizationGeneralPurpose(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="maximumAmortizationPeriodGeneralPurposeTextId"/>







<input type="hidden" name="planInfoVO.loans.maxAmortizationPeriodGeneral" id="maximumAmortizationPeriodGeneralPurposeHidden" /><%--  input - name="pifDataForm" --%>

                  </c:when>
                  <c:otherwise>
						${pifDataForm.planInfoVO.loans.maxAmortizationPeriodGeneral} 
                  </c:otherwise>
                </c:choose>
			</DIV>
            <DIV class=amortizationSuffixColumn>
                <c:if test="${pifDataForm.editMode or not empty pifDataForm.planInfoVO.loans.maxAmortizationPeriodGeneral}">
                  &nbsp;years
                </c:if>
			</DIV>
		  </TD>
		</TR>
        <TR>
          <TD class=dataColumn>
            <DIV class=amortizationLabelColumn>Hardship </DIV>
            <DIV class=amortizationDataColumn>
                <c:choose>
                  <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.loans.maxAmortizationPeriodHardship" disabled="${disableFields}" maxlength="1" onblur="validateMaximumAmortizationHardship(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="maximumAmortizationPeriodHardshipTextId"/>







<input type="hidden" name="planInfoVO.loans.maxAmortizationPeriodHardship" id="maximumAmortizationPeriodHardshipHidden" /><%--  input - name="pifDataForm" --%>

                  </c:when>
                  <c:otherwise>
						${pifDataForm.planInfoVO.loans.maxAmortizationPeriodHardship} 
                  </c:otherwise>
                </c:choose>
			</DIV>
            <DIV class=amortizationSuffixColumn>
                <c:if test="${pifDataForm.editMode or not empty pifDataForm.planInfoVO.loans.maxAmortizationPeriodHardship}">
                  &nbsp;years
                </c:if> 
			</DIV>
		  </TD>
		</TR>
        <TR>
          <TD class=dataColumn width="430px">
            <DIV class=amortizationLabelColumn>Purchase of primary residence 
            </DIV>
            <DIV></DIV>
            <DIV class=amortizationDataColumn>
                <c:choose>
                  <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.loans.maxAmortizationPeriodPrimaryResidence" disabled="${disableFields}" maxlength="2" onblur="validateMaximumAmortizationPrimaryResidence(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="maximumAmortizationPeriodPrimaryResidenceTextId"/>

<form:hidden path="planInfoVO.loans.maxAmortizationPeriodPrimaryResidence"  id="maximumAmortizationPeriodPrimaryResidenceHidden" /><%--  input - name="pifDataForm" --%>

                  </c:when>
                  <c:otherwise>
						${pifDataForm.planInfoVO.loans.maxAmortizationPeriodPrimaryResidence} 
                 </c:otherwise>
                </c:choose>
			
			</DIV>
            <DIV class=amortizationSuffixColumn>
                <c:if test="${pifDataForm.editMode or not empty pifDataForm.planInfoVO.loans.maxAmortizationPeriodPrimaryResidence}">
                  &nbsp;years
                </c:if>
			</DIV>
		  </TD>
		</TR>
        <DIV class=endDataRowAndClearFloats></DIV>
        </TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD class=loansLabelColumn style="BORDER-LEFT-WIDTH: 0px;">Minimum  amount for loan&nbsp;&nbsp;&nbsp;
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
$<form:input path="planInfoVO.loans.minimumLoanAmount" disabled="${disableFields}" maxlength="9" onblur="validatePIFMinLoanAmount(this)" onchange="setDirtyFlag();" size="14" cssClass="numericInput" id="minimumLoanAmountTextId"/>







<input type="hidden" name="planInfoVO.loans.minimumLoanAmount" id="minimumLoanAmountHidden" /><%--  input - name="pifDataForm" --%>

                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${pifDataForm.planInfoVO.loans.minimumLoanAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
		  </TD>
        </TR></TBODY></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD class=dataColumn style="BORDER-LEFT-WIDTH: 0px;">Participants may borrow up to 
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.loans.maximumLoanPercentage" disabled="${disableFields}" maxlength="7" onblur="validatePIFMaxLoanPercent(this)" onchange="setDirtyFlag();" size="7" cssClass="numericInput" id="maximumLoanPercentageTextId"/>







                  %
<input type="hidden" name="planInfoVO.loans.maximumLoanPercentage" id="maximumLoanPercentageHidden" /><%--  input - name="pifDataForm" --%>

                </c:when>
                <c:otherwise>
                  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${pifDataForm.planInfoVO.loans.maximumLoanPercentage}"/>
				  ${empty pifDataForm.planInfoVO.loans.maximumLoanPercentage ? '' : '%'}
                </c:otherwise>
              </c:choose> of their vested balance,  to a maximum of  
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
$<form:input path="planInfoVO.loans.maximumLoanAmount" disabled="${disableFields}" maxlength="9" onblur="validatePIFMaxLoanAmount(this)" onchange="setDirtyFlag();" size="10" cssClass="numericInput" id="maximumLoanAmountTextId"/>







<input type="hidden" name="planInfoVO.loans.maximumLoanAmount" id="maximumLoanAmountHidden" /><%--  input - name="pifDataForm" --%>

                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${pifDataForm.planInfoVO.loans.maximumLoanAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
		  </TD>
        </TR></TBODY></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD class=dataColumn style="BORDER-LEFT-WIDTH: 0px;">Maximum number of outstanding loans for a participant&nbsp;&nbsp;&nbsp;
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
<form:input path="pifDataUi.maximumNumberOfOutstandingLoans" 
							  id="maximumNumberOfOutstandingLoansTextId"					
							  size="10" 
							  maxlength="9" 
							  onblur="validateMaxNumberofOutstandingLoans(this)"
							  onchange="setDirtyFlag();"
							  cssClass="numericInput" 
							  disabled="${disableFields}"/>	
						<!-- <input type="hidden" id="loans_maxNumberOfOutstandingLoans"
						 name="pifDataUi.maximumNumberOfOutstandingLoans" /> -->	

		  &nbsp;&nbsp;&nbsp;or&nbsp;&nbsp;&nbsp;
						<script type="text/javascript">
						$(document).ready(function() {
							unlimitedLoansIndicatorId= "#pifDataUi_planInfoVO_loans_maxNumberOfOutstandingLoansIndicator";
							<c:if test="${disableFields}">
								$(unlimitedLoansIndicatorId).prop("disabled", true); 
							</c:if>

							$(unlimitedLoansIndicatorId).on("click", function() {
							    unlimitedLoansIndicatorHiddenId ="#loans_maxNumberOfOutstandingLoansIndicator"; 
								maximumNumberOfOutstandingLoansTextId = "#maximumNumberOfOutstandingLoansTextId";
								maximumNumberOfOutstandingLoans = "#loans_maxNumberOfOutstandingLoans";
								if($(unlimitedLoansIndicatorId).is(':checked')){ 
									$(unlimitedLoansIndicatorId).val('true');
									$(maximumNumberOfOutstandingLoansTextId).val('');
									$(maximumNumberOfOutstandingLoans).val('99');
									$(maximumNumberOfOutstandingLoansTextId).prop("disabled", true);
									$(unlimitedLoansIndicatorHiddenId).val('true');
								}else{
									$(unlimitedLoansIndicatorId).val('false');
									$(maximumNumberOfOutstandingLoansTextId).val('');
									$(maximumNumberOfOutstandingLoans).val('0');
									$(maximumNumberOfOutstandingLoansTextId).prop("disabled", false);
									$(unlimitedLoansIndicatorHiddenId).val('false');
								}
							});
						});
						</script>	
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_loans_maxNumberOfOutstandingLoansIndicator" 
							name="planInfoVO.loans.maxNumberOfOutstandingLoansIndicator" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${pifDataForm.planInfoVO.loans.maxNumberOfOutstandingLoansIndicator}">checked="checked" </c:if> />Unlimited
<form:hidden path="planInfoVO.loans.maxNumberOfOutstandingLoansIndicator" id="loans_maxNumberOfOutstandingLoansIndicator" /><%--  input - name="pifDataForm" --%>

				</c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${pifDataForm.planInfoVO.loans.maxNumberOfOutstandingLoansIndicator}">
                      ${planDataConstants.UNLIMITED}
                   </c:when>
                    <c:when test="${planDataConstants.UNSELECTED_LOANS_ALLOWED == pifDataForm.pifDataUi.maximumNumberOfOutstandingLoans}">
                      <%-- Unspecified --%>
                    </c:when>
                    <c:otherwise>
                      ${pifDataForm.pifDataUi.maximumNumberOfOutstandingLoans}
                    </c:otherwise>
                  </c:choose>
                </c:otherwise>
              </c:choose>
			</TD>
        </TR></TBODY></TABLE></DIV>      
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD class=loansLabelColumn>Loan interest rate 
              <c:choose>
                <c:when test="${pifDataForm.editMode}">	
						<script type="text/javascript">
						$(document).ready(function() {
							primeLoanIndicatorId= "#pifDataUi_planInfoVO_loans_loanInterestAbovePrimeIndicator";
							<c:if test="${disableFields}">
							$(primeLoanIndicatorId).prop("disabled", true); 
							</c:if>

							$(primeLoanIndicatorId).on("click", function() {
							primeLoanIndicatorHiddenId = "#loans_loanInterestAbovePrimeIndicators";
							loanInterestRateAbovePrimeHiddenId = "#loans_loanInterestRateAbovePrime";
							loanInterestRateAbovePrimeTextId = "#loanInterestRateAbovePrimeTextId";
								if($(primeLoanIndicatorId).is(':checked')){ 
									$(primeLoanIndicatorId).val('true');								
									$(loanInterestRateAbovePrimeHiddenId).val('0');
									$(loanInterestRateAbovePrimeTextId).val('');									
									$(loanInterestRateAbovePrimeTextId).prop("disabled", true);
									$(primeLoanIndicatorHiddenId).val('true');		
								}else{
									$(primeLoanIndicatorId).val('false');								
									$(loanInterestRateAbovePrimeHiddenId).val('');
									$(loanInterestRateAbovePrimeTextId).val('');									
									$(loanInterestRateAbovePrimeTextId).prop("disabled", false);
									$(primeLoanIndicatorHiddenId).val('false');									
								}
							});
						});
						</script>	
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_loans_loanInterestAbovePrimeIndicator" 
							name="planInfoVO.loans.loanInterestAbovePrimeIndicator" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${pifDataForm.planInfoVO.loans.loanInterestAbovePrimeIndicator}">checked="checked"</c:if> />prime&nbsp;&nbsp;&nbsp;or
<form:hidden path="planInfoVO.loans.loanInterestAbovePrimeIndicator" id="loans_loanInterestAbovePrimeIndicators" /><%--  input - name="pifDataForm" --%>

<form:input path="planInfoVO.loans.loanInterestRateAbovePrime" disabled="${disableFields}" maxlength="5" onblur="validatePIFLoanInterestRate(this)" onchange="setDirtyFlag();" size="5" cssClass="numericInput" id="loanInterestRateAbovePrimeTextId"/>	% above prime







<form:hidden path="planInfoVO.loans.loanInterestRateAbovePrime" id="loans_loanInterestRateAbovePrime" /><%--  input - name="pifDataForm" --%>

                </c:when>
                <c:otherwise>
					<c:if test="${pifDataForm.planInfoVO.loans.loanInterestAbovePrimeIndicator}">	prime </c:if>
					<c:if test="${!pifDataForm.planInfoVO.loans.loanInterestAbovePrimeIndicator && pifDataForm.planInfoVO.loans.loanInterestRateAbovePrime > 0}">	
						<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_INTEREST_RATE_SCALE}" value="${pifDataForm.planInfoVO.loans.loanInterestRateAbovePrime}"/>
						${empty pifDataForm.planInfoVO.loans.loanInterestRateAbovePrime ? '' : '% above prime'}
					</c:if>
                </c:otherwise>
              </c:choose>								  
		  </TD>
        </TR></TBODY></TABLE></DIV>   
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead>Allowable money types for loans</TD></TR>
		</table>
		 
				
     
      <DIV class=evenDataRow>
<c:forEach items="${pifDataForm.planInfoVO.loans.allowedMoneyTypesForLoans}" var="allowableMoneyTypeLoans" varStatus="count" >
<c:if test="${allowableMoneyTypeLoans.selectedMoneyType ==true}">
			  <div class="data">
			  <c:if test="${pifDataForm.confirmMode}"> <c:set var="disabledFlag" value="true"/> </c:if>
			  <c:if test="${!pifDataForm.confirmMode}"> <c:set var="disabledFlag" value="false"/> </c:if>
				<form:checkbox id="pifDataUi_planInfoVO_loans_allowedMoneyTypesForLoans[${count.index}]_selectedIndicator"
					path="planInfoVO.loans.allowedMoneyTypesForLoans[${count.index}].selectedIndicator" 
					value="Y" onclick="handleAllowedMoneyTypesForLoans(this,${count})" disabled="${disabledFlag}"/>

		
		
		 	<!-- substring money type 'EEAT1-403a' to 'EEAT1'-->
				<c:set var="allowableMTShortName" value="${fn:trim(allowableMoneyTypeLoans.moneyTypeShortName)}"/>
				<c:if test="${fn:contains( allowableMTShortName,pifConstants.EEAT_401A) || fn:contains( allowableMTShortName,pifConstants.EEAT_403A)}">
					<c:set var="tempStr" value="${fn:split(allowableMoneyTypeLoans.moneyTypeShortName,'-')}"/>
					<c:set var="allowableMTShortName" value="${tempStr[0]}"/>
				</c:if>
				${allowableMoneyTypeLoans.moneyTypeLongName}&nbsp;(${allowableMTShortName})
				</div>
				<c:if test="${count.index % 2 == 0}">
					<div class="endDataRowAndClearFloats"></div>
				</c:if>					
</c:if>
</c:forEach>
		<br><content:getAttribute id="applyFeeInformation404a5DisclosurePurposeForLoans" attribute="text"/>
		<div class="endDataRowAndClearFloats"></div>
	  </DIV>
<!--end table content -->
</BODY></HTML>

</div>
