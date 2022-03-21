<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Define static constants --%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<!--  Load content beans required for this page -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_RECALCULATE_REQUIRED_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="recalculateRequiredText"/>

<%-- Define collections we will need --%>
<c:set var="states" scope="request" value="${withdrawalForm.lookupData['USA_STATE_WITH_MILITARY_TYPE']}" />
<c:set var="withdrawalAmountTypes" scope="request" value="${withdrawalForm.lookupData['WITHDRAWAL_AMOUNT_TYPE']}" />
<c:set var="countries" scope="request" value="${withdrawalForm.lookupData['COUNTRY_COLLECTION_TYPE']}" />
<c:set var="tpaTransactionFeeTypes" scope="request" value="${withdrawalForm.lookupData['TPA_TRANSACTION_FEE_TYPE']}" />
<c:set var="unvestedAmountOptions" scope="request" value="${withdrawalForm.lookupData['OPTIONS_FOR_UNVESTED_AMOUNTS']}" />
<c:set var="courierCompanies" scope="request" value="${withdrawalForm.lookupData['COURIER_COMPANY']}" />
<c:set var="irsWithdrawalDistributions" scope="request" value="${withdrawalForm.lookupData['IRS_CODE_WD']}" />
<c:set var="paymentToTypes" scope="request" value="${withdrawalForm.lookupData['PAYMENT_TO_TYPE']}" />
<c:set var="irsDistCodesLoansTypes" scope="request" value="${withdrawalForm.lookupData['IRS_CODE_LOAN']}"/>

<%-- page definition --%>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/disbursements.css" type="text/css">

<c:set scope="request" var="useFloatingSummary" value="true"/>

<%-- Flag for any browser dependant rendering --%>
<c:choose>
  <c:when test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
    <c:set var="isIE" value="true" scope="request"/>
  </c:when>
  <c:otherwise>
    <c:set var="isIE" value="false" scope="request"/>
  </c:otherwise>
</c:choose>

<ps:form modelAttribute ="withdrawalForm" method="POST" action="/do/withdrawal/entryStep2/" name="withdrawalForm">

  <jsp:include flush="true" page="../../common/floatingSummary.jsp"></jsp:include>
  <form:hidden path="action"  disabled="true"/>
<form:hidden path="withdrawalRequestUi.withdrawalRequest.ignoreWarnings" value="true" disabled="true"/>
<form:hidden path="withdrawalRequestUi.withdrawalRequest.recalculationRequired" id="recalculationRequiredId"/>
<form:hidden path="withdrawalRequestUi.withdrawalRequest.isLegaleseConfirmed" id="legaleseConfirmFlagId" value="false" />
<form:hidden path="dirty" />
	<table border="0" cellpadding="0" cellspacing="0" width="760">
	  <tr>
	    <td>
	      <table border="0" cellpadding="0" cellspacing="0" width="100%">
	        <tr>
	          <td>
	            <img src="/assets/unmanaged/images/s.gif" height="1" width="30">
	            <br>
	            <img src="/assets/unmanaged/images/s.gif" height="1">
	          </td>
	          <td width="500">
							<%-- Introduction Section --%>
							<jsp:include page="step2IntroductionSection.jsp"/>
						  <%-- Begin Recipient Loop --%>
	  					<c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}"
	            					 var="recipient"
	             					 varStatus="recipientStatus">
								<c:set var="recipientIndex" scope="request" value="${recipientStatus.index}"/>             					 
								<c:set var="recipient" scope="request" value="${recipient}"/>             					 
								<%-- Money Type Section --%>
								<jsp:include page="step2MoneyTypeSection.jsp"/>
						    <%-- Begin Payee Loop --%>
						    <c:forEach items="${recipient.payees}"
						               var="payee"
						               varStatus="payeeStatus">
									<c:set var="payeeIndex" scope="request" value="${payeeStatus.index}"/>             					 
									<c:set var="payee" scope="request" value="${payee}"/>             					 
									<jsp:include page="step2PayeeSection.jsp"/>
						    </c:forEach>
						    <%-- End Payee Loop --%>
						    <%-- Recipient Section - hidden if Payment To is Plan Trustee --%>
						    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
									<jsp:include page="step2RecipientSection.jsp"/>
						    </c:if>
						  </c:forEach>
						  <%-- End Recipient Loop --%>
							<%-- Notes Section --%>
							<jsp:include page="step2NotesSection.jsp"/>
							<%-- Declaration Section --%>
							<jsp:include page="step2DeclarationSection.jsp"/>
              <%-- Footer Section --%>
							<jsp:include page="step2FooterSection.jsp"/>
              <%-- Actions Section --%>
							<jsp:include page="step2Buttons.jsp"/>
						</td>
				    <td width="260">&nbsp;</td>
			    <!-- end main content table -->
  				</tr>
				  <tr>
				    <td>&nbsp;</td>
				    <td colspan="2">&nbsp;</td>
				  </tr>
				  <tr>
				  	<td>&nbsp;</td>
				  	<td colspan="2">
							<jsp:include page="step2FootnotesSection.jsp"/>
				    </td>
  				</tr>
				</table>
				<!-- footer table -->
				<script type="text/javascript">
				<!--
					g_FloatPanel.init('scoreboard');
				//-->
				</script>
			</td>
		</tr>
	</table>
	<jsp:include flush="true" page="../../common/legalese.jsp"></jsp:include>
	<jsp:include flush="true" page="withdrawalBankLookUp.jsp"></jsp:include>
	
</ps:form>
<%-- note to developers - this script tag MUST appear after all tooltips.
Best to put it before the closing body tag  - Shane Delorme Jun 14, 2006--%>
<script  type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<jsp:include flush="true" page="../../common/withdrawalCommonUtil.jsp"></jsp:include>
<jsp:include flush="true" page="step2Validations.jsp"></jsp:include>
<jsp:include flush="true" page="step2Updates.jsp"></jsp:include>
<jsp:include flush="true" page="step2Handlers.jsp"></jsp:include>
<script type="text/javascript">

function initWithdrawalPage2() {
	expandAllPage2Sections();

  <%-- Initialize page --%>
  updateStep2Page();
  
  <%-- Register our dirty page query --%>
  
  registerTrackChangesFunction(isFormDirty);
  
	protectLinks();
  
  handleFocus();
}

  <%-- Updates the total requested withdrawal amount --%>
  function updateTotalRequestedAmount() {

		if (document.getElementById("amountTypeCodeId").value == '${requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}') {
		
		  document.getElementById('totalRequestedAmountSpanSpecificAmountId').innerHTML = formatAmount(getColumnTotal("withdrawalRequestUi.moneyTypes", ["withdrawalAmount"], []), false, false);
	  }
  }
  
  <%-- Checks if recalculate is required and displays an alert --%>
  function checkRecalculateRequired() {
  
  	if (document.getElementById("recalculationRequiredId").value == "true") {

      document.getElementById('amountTypeCodeId').focus();
			alert('<content:getAttribute beanName="recalculateRequiredText" attribute="text" escapeJavaScript="true"/>');
      document.getElementById('recalculateButtonId').focus();
			return false;
		}
		
		return true;
  }

function doVestingInformation(empProfileId) {

  var contractId = "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.contractId}";
  var reason = "${withdrawalForm.withdrawalRequestUi.mappedReasonCodeForVesting}";
  var asOfDate = "<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.vestingInformationEventDate}" type="DATE" pattern="MM-dd-yyyy"/>";
  var printURL = "/do/census/vestingInformation/" + "?profileId=" + empProfileId + "&source=wd&printFriendly=true&contractId=" + contractId + "&asOfDate=" + asOfDate + "&wdReason=" + reason;
  window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
}

function clearOnPasteBankName(){
	document.getElementById("eftPayeeBankNameId[0][0]").value = '';
 }
 
function clearSelectBankName(evt,recipientIndex,payeeIndex){
	document.getElementById("eftPayeeBankNameId[" + recipientIndex + "][" + payeeIndex + "]").value = '';
	
 }
 
function doBankNameList(routingNumber,recipientIndex, payeeIndex) {
	
   if (routingNumber == 0 || routingNumber == "" || routingNumber == null) {
    return false;
   }
   
  $("#eftPayeeBankName").val('');
  $("#bankNameOtherInput").val('');
  $("bankNameOtherRadio").removeAttr("checked");
  $("#bankSelectionTable").empty();
  $("#bankSelectionErrorMsg").html('');
    var paymentMethodCode = '';
	var ele = document.getElementsByName("withdrawalRequestUi.withdrawalRequest.recipients["+recipientIndex+"].payees["+payeeIndex+"].paymentMethodCode");
     for(i = 0; i < ele.length; i++) {
       if(ele[i].checked)
               paymentMethodCode   = ele[i].value;
     }
  var jsonItem = JSON.stringify({abaRoutingNumber : routingNumber,paymentMethodCode : paymentMethodCode});
  var http = new XMLHttpRequest();
  var csrfToken = '${_csrf.token}';
  var url = "/do/withdrawal/bankNamesList/ajax/?"+"_csrf="+csrfToken;
  
  $.ajax({
    url: url,
    data: jsonItem,
    type: "POST",
    dataType: "json",
	contentType: 'application/json',
	mimeType: 'application/json',
	cache: false,
    async: false,
    success: function(data){		
      console.log(data);
	  callbackBankLookupPopUp(data,recipientIndex,payeeIndex);
    },
    error: function(jqxhr, textStatus, errorThrown) {
      console.log("jqXHR: ", jqxhr);
      console.log("textStatus: ", textStatus);
      console.log("errorThrown: ", errorThrown);
    }

  });
  
 }

function showPopupGuide() {
  var popupURL = new URL("/do/withdrawal/beforeProceedingGatewayInit/");
  popupURL.setParameter("action", "print");
  popupURL.setParameter("printFriendly", "true");
  window.open(popupURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

  <%-- Checks if recalculate was pressed to determine where focus should be sent --%>
  function handleFocus() {
    if (document.getElementById('action').value == 'calculate') {
      if (${withdrawalForm.errorsExist}) {
        document.getElementById('messagesSectionId').focus();  
        
      } else {
        document.getElementById('withdrawalAmountTable').focus();
      }
    }
  }
</script>
<script type="text/javascript">initWithdrawalPage2();</script>
