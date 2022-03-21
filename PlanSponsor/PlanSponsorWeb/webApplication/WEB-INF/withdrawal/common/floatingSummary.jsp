<%@page buffer="none" autoFlush="true" isErrorPage="false" %> 

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<fmt:setLocale value="en_US"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="recipientConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<content:contentBean 
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_FLOATER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="withdrawalFloaterSectionTitle"/>
  
<style type="text/css">
<!--
.inputError {   color: #990000;
    font-weight:bold;
}

.infoRow { 
	height:18px; 
	vertical-align:top;
}

#scoreboard {
  position: absolute;
  left: 545px;
  top: 175px;
  width: 215px;
  height: 145px;
  z-index: 4;
  visibility: visible;
}
-->
</style>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/disbursements.css" type="text/css">

<div id="scoreboard" style="position: absolute; left: 545px; top: 175px; width: 215px; height: 145px; z-index: 4; visibility: visible; ">
  <table class="box" cellSpacing="0" cellPadding="0" width="215" border="0">
    <tr class="tablehead">
    	<td colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="withdrawalFloaterSectionTitle" attribute="text"/></b></td>
    </tr>
    <tr>
    	<td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td><img src="/assets/unmanaged/images/s.gif" height="10" width="1"></td>
      <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
    	<td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
      	<table width="100%" border="0" cellspacing="0" cellpadding="2">
        	<tr>
          	<td colspan="2" class="tablesubhead"><b>Participant information</b></td>
          </tr>
          <tr valign="top">
          	<td width="50%">Name</td>
            <td width="50%" class="highlight">
              <%-- Last name, and cif tag must be on same line or we get an extra space --%>
							<span id="wdReq_Name" class="infoRow">
              	${fn:trim(withdrawalForm.withdrawalRequestUi.withdrawalRequest.lastName)}<c:if test="${! empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.firstName}">,&nbsp;${fn:trim(withdrawalForm.withdrawalRequestUi.withdrawalRequest.firstName)}</c:if>
						  <span>
            </td>
          </tr>
          <tr valign="top">
          	<td>State of residence</td>
            <td class="highlight">
							<span id="floatingSummaryStateOfResidenceId" class="infoRow">
								<c:choose>
									<c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence == recipientConstants.STATE_OF_RESIDENCE_OUTSIDE_US}">
										Non-US
								  </c:when>
									<c:otherwise>
		              	${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence)}
		              </c:otherwise>
		            </c:choose>
						  </span>
            </td>
          </tr>
  	      <tr>
          	<td colspan="2" class="tablesubhead"><b>Basic information</b> </td>
          </tr>
          <tr valign="top">
          	<td>Type of withdrawal</td>
            <td class="highlight">
							<span id="wdReq_Reason" class="infoRow">
								${withdrawalForm.withdrawalReasonDescription}
						  </span>
						</td>
          </tr>
          <tr valign="top">
          	<td>Payment to </td>
            <td class="highlight">
							<c:set scope="request" var="paymentToCode" value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}" />
						  <span id="wdReq_PaymentTo" class="infoRow">
							  ${withdrawalForm.paymentToDescription}
							</span>
						</td>
          </tr>
			    <c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">
            <tr>
            	<td colspan="2" class="tablesubhead"><b>Loan details</b></td>
            </tr>
            <tr valign="top">
            	<td>Outstanding balance</td>
              <td class="highlight">
								<span id="wdReq_TotalLoanAmt" class="infoRow">
    	            <fmt:formatNumber type="currency" currencySymbol="$" value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.totalOutstandingLoanAmt}"/>
								</span>
            	</td>
            </tr>
            <c:if test="${withdrawalForm.withdrawalRequestUi.showLoanIrsDistributionCode}">
              <tr valign="top">
                <td id="floatingSummaryLoanIrsDistributionCodeCol1Id">IRS dist. code</td>
                <td id="floatingSummaryLoanIrsDistributionCodeCol2Id" class="highlight">
  							  <span id="floatingSummaryLoanIrsDistributionCodeId" class="infoRow">
                    <ps:displayDescription collection="${irsDistCodesLoansTypes}" keyName="code" keyValue="description" key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.irsDistributionCodeLoanClosure}" />
  	             </span>
  						  </td>
            	</tr>
            </c:if>
					</c:if>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.isPostDraft}">
            <tr>
              <td colspan="2" class="tablesubhead"><b>Amount details</b></td>
            </tr>
            <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
              <tr valign="top">
                <td>Total available</td>
                <td class="highlight">
                  <span class="infoRow">
                    <fmt:formatNumber type="currency" currencySymbol="$" 
                                      minFractionDigits="2"
                                      value="${withdrawalForm.withdrawalRequestUi.totalAvailableWithdrawalAmount}"/>
                 </span>
                </td>
              </tr>
            </c:if>
            <tr valign="top">
              <td>Requested amount</td>
              <td class="highlight">
                <span id="floatingSummaryTotalRequestedAmountSpanSpecificAmountId"></span>
                <span id="floatingSummaryTotalRequestedAmountSpanMaximumAvailableId">
                  <fmt:formatNumber type="currency" currencySymbol="$" 
                                    minFractionDigits="2"
                                    value="${withdrawalForm.withdrawalRequestUi.totalAvailableWithdrawalAmount}"/>
                </span>
                <span id="floatingSummaryTotalRequestedAmountSpanPercentageMoneyTypeId">
                  <fmt:formatNumber type="currency" currencySymbol="$" 
                                    minFractionDigits="2"
                                    value="${withdrawalForm.withdrawalRequestUi.totalRequestedWithdrawalAmount}"/>
                </span>
              </td>
            </tr>
            <c:if test="${withdrawalForm.withdrawalRequestUi.showLoanIrsDistributionCode}">
              <tr valign="top">
                <td>IRS dist. code</td>
                <td class="highlight">
                  <span id="floatingSummaryWithdrawalIrsDistributionCodeId" class="infoRow">
                    <ps:displayDescription collection="${irsWithdrawalDistributions}" keyName="code" keyValue="description" key="${withdrawalRequestUi.withdrawalRequest.recipients[0].payees[0].irsDistCode}" />
                 </span>
                </td>
              </tr>
            </c:if>
          </c:if>
      	</table>
      </td>
    	<td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
          <tr>
            <td width="1" class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
            <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
          	<td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
          </tr>
          <tr>
            <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          	<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        	</tr>
      	</table>
    	</td>
  	</tr>
	</table>
</div>

<%-- Defines page level update handlers --%>
<script type="text/javascript">

/**
 * Alternates between showing and suppressing the floating summary.
 */ 
 function handleShowSuppressFloatingSummaryClicked() {

    var node = document.getElementById('scoreboard');
    node.style.display = (node.style.display == 'none') ? 'inline' : 'none'; 
 }
</script>

