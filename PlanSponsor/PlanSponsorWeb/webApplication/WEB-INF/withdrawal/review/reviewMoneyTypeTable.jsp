<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<fmt:setLocale value="en_US"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="moneyTypeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />
<un:useConstants var="activityMoneyTypeConstants" className="com.manulife.pension.service.withdrawal.helper.MoneyTypeFieldDef" />

<%-- Due to a problem with how IE and Firefox render space for TD and Spans.  We have to dynamically
place the style class on the TD cell if we are using IE and on the internal span if not using IE to support
the dynamic behaviour of the money type table (in particular the show/suppress of the requested percentage --%>
<c:choose>
  <c:when test='${fn:containsIgnoreCase(header["user-agent"], "MSIE")}'>
    <c:set var="requestedPercentHeaderSeparatorTdClass" value="class=\"dataheaddivider\""/>
    <c:set var="requestedPercentHeaderSeparatorSpanClass" value=""/>
    <c:set var="requestedPercentHeaderTdClass" value="class=\"tablesubhead\""/>
    <c:set var="requestedPercentHeaderSpanClass" value=""/>
    <c:set var="requestedPercentSeparatorTdClass" value="class=\"datadivider\""/>
    <c:set var="requestedPercentSeparatorSpanClass" value=""/>
  </c:when>
  <c:otherwise>
    <c:set var="requestedPercentHeaderSeparatorTdClass" value=""/>
    <c:set var="requestedPercentHeaderSeparatorSpanClass" value="class=\"dataheaddivider\""/>
    <c:set var="requestedPercentHeaderTdClass" value=""/>
    <c:set var="requestedPercentHeaderSpanClass" value="class=\"tablesubhead\""/>
    <c:set var="requestedPercentSeparatorTdClass" value=""/>
    <c:set var="requestedPercentSeparatorSpanClass" value="class=\"datadivider\""/>
  </c:otherwise>
</c:choose> 

<tr class="datacell1" valign="top">
  <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="tablesubhead"><strong>Money type</strong></td>
        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td align="right" class="tablesubhead">
          <strong>
            Account balance
            <c:if test="${withdrawalForm.withdrawalRequestUi.hasPbaOrLoans}">
              <sup>^</sup>
            </c:if>
            &nbsp;($)
            <br/>
          </strong>
        </td>
         <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
         <td> &nbsp;</td>
         <td align="right" class="tablesubhead">
          <strong>
           Available for Hardship
            &nbsp;($)
            <br/>
          </strong>
        </td>
        </c:if>
        <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td align="right" class="tablesubhead"><strong>Vesting<br/>(%)</strong></td>
        <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" class="tablesubhead"><strong>Available amount&nbsp;($)</strong></td>
        </c:if>
        <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td align="right" class="tablesubhead"><strong>Requested amount&nbsp;($)</strong></td>
         <td id="requestedPercentageColHeaderSeparatorTdId" ${requestedPercentHeaderSeparatorTdClass}><span id="requestedPercentageColHeaderSeparatorId"  ${requestedPercentHeaderSeparatorSpanClass}><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></span></td>
        <td id="requestedPercentageColHeaderTdId" class="tablesubhead"  align="right" style="background:#CCCCCC;"  ${requestedPercentHeaderTdClass}>
          <span id="requestedPercentageColHeaderId" ${requestedPercentHeaderSpanClass}>
            <strong>Portion of available amount&nbsp;(%)</strong>
          </span>
        </td>
      </tr>
      <%-- Begin MoneyType loop --%>
      <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
                 var="moneyType"
                 varStatus="moneyTypeStatus">
        <%-- For the status count on the next line - note the index starts at 1, not 0 --%>
        <tr class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell1' : 'datacell2'}" valign="top">
          <td><c:out value="${moneyType.withdrawalRequestMoneyType.moneyTypeName}"/></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td align="right">
            <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.withdrawalRequestMoneyType.totalBalance}"/>
          </td>

            <%-- for Harship changes --%>
             <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
            <td> &nbsp;</td>
        <td align="right">
          <c:if test = "${moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF'}">
            <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.withdrawalRequestMoneyType.availableHarshipAmount}"/>
          </td>
           </c:if>
             <c:if test = "${!(moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF')}">
         <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.withdrawalRequestMoneyType.availableHarshipAmount}"/>
         </c:if>
         </c:if>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td align="right" nowrap>
           <c:choose>
             <c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
             <form:hidden path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].vestingPercentage" id="moneyTypeVestingPercentageId[${moneyTypeStatus.index}]"/>
<%-- <input type="hidden" name="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].vestingPercentage" id="moneyTypeVestingPercentageId[${moneyTypeStatus.index}]"/> --%>

             </c:when>
             <c:otherwise>
            <c:choose>
 
              <c:when test="${moneyType.withdrawalRequestMoneyType.vestingPercentageUpdateable and !withdrawalForm.withdrawalRequestUi.viewOnly}">
                <ps:fieldHilight name="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].vestingPercentage" singleDisplay="true">
					<ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_VESTING_PERCENT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/>
                </ps:fieldHilight>
<form:input path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].vestingPercentage" maxlength="7" onchange="return handleMoneyTypeVestingPercentageChanged(${moneyTypeStatus.index});" size="7" cssClass="mandatoryInputRight" id="moneyTypeVestingPercentageId[${moneyTypeStatus.index}]"/>

              </c:when>
 
              <c:otherwise>
                <fmt:formatNumber minFractionDigits="${moneyTypeConstants.VESTING_PERCENTAGE_SCALE}"
                                  maxFractionDigits="${moneyTypeConstants.VESTING_PERCENTAGE_SCALE}"
                                  value="${moneyType.withdrawalRequestMoneyType.vestingPercentage}"/>
<form:hidden path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].vestingPercentage" id="moneyTypeVestingPercentageId[${moneyTypeStatus.index}]"/>                                  

              </c:otherwise>
            </c:choose>
           </c:otherwise>
           </c:choose>
          </td>
          <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td align="right">
            <c:choose>
	             <c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
	          
	             </c:when>
	             <c:otherwise>
		              <fmt:formatNumber type="currency"
		                                currencySymbol=""
		                                minFractionDigits="2"
		                                value="${moneyType.withdrawalRequestMoneyType.availableWithdrawalAmount}"/>
	             </c:otherwise>
             </c:choose>
            </td>
          </c:if>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           <%-- Requested amount columns --%>
           <td align="right">
            <span id="requestedAmountColSelectRow[${moneyTypeStatus.index}]Id">&nbsp;</span>
            <span id="requestedAmountColSpecificAmountRow[${moneyTypeStatus.index}]Id">
            <ps:fieldHilight name="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalAmount" singleDisplay="true"><ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_WITHDRAWAL_AMOUNT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/></ps:fieldHilight>

            <c:choose>
            <c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
	             <fmt:formatNumber type="currency"
			                                currencySymbol=""
			                                minFractionDigits="2"
			                                value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.moneyTypes[moneyTypeStatus.index].withdrawalAmount}"/>
			                                <form:hidden path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalAmount" id="moneyTypeRequestedAmountId[${moneyTypeStatus.index}]"/>

             </c:when>
             <c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
             <form:hidden path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalAmount" id="moneyTypeRequestedAmountId[${moneyTypeStatus.index}]"/>

             </c:when>
             <c:otherwise>
<form:input path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalAmount" maxlength="20" onchange="handleMoneyTypeRequestedAmountChanged(${moneyTypeStatus.index});" onkeyup="updateTotalRequestedAmount();" size="15" cssClass="mandatoryInputRight" id="moneyTypeRequestedAmountId[${moneyTypeStatus.index}]"/>






            </c:otherwise>
           </c:choose>
            </span>
            <span id="requestedAmountColMaximumAvailableRow[${moneyTypeStatus.index}]Id">
              <c:choose>
                <c:when test="${withdrawalForm.withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
                	<c:choose>
             	     <c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
          
             		</c:when>
             		<c:otherwise>
	             	   <fmt:formatNumber type="currency"
	                                  currencySymbol=""
	                                minFractionDigits="2"
	                                value="${moneyType.withdrawalRequestMoneyType.availableWithdrawalAmount}"/>
                    </c:otherwise>
                   </c:choose>             
                                
            </c:when>
            <c:otherwise>
              &nbsp;
            </c:otherwise>
          </c:choose>
          	<c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
		    	 <c:if test = "${moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF'}">
            <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.withdrawalRequestMoneyType.availableWithdrawalAmount}"/>
	         </c:if>
	         <c:if test = "${!(moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF')}">
	         <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.withdrawalRequestMoneyType.availableWithdrawalAmount}"/>
	         </c:if>
	         </c:if>
            </span>
            <span id="requestedAmountColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id">
              <fmt:formatNumber type="currency"
                              currencySymbol=""
                              minFractionDigits="2"
                             value="${moneyType.withdrawalRequestMoneyType.withdrawalAmount}"/>
            </span>
           </td>
           <%-- Requested percent columns --%>
           <td id="requestedPercentageColSeparatorTdRow[${moneyTypeStatus.index}]Id" style="background:#E9E2C3;" ${requestedPercentSeparatorTdClass}>
            <span id="requestedPercentageColSeparatorRow[${moneyTypeStatus.index}]Id" ${requestedPercentSeparatorSpanClass}>
              <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
            </span>
           </td>
           <td id="requestedPercentageColTdRow[${moneyTypeStatus.index}]Id" align="right">
            <span id="requestedPercentageColSelectRow[${moneyTypeStatus.index}]Id">&nbsp;</span>
            <span id="requestedPercentageColMaximumAvailableRow[${moneyTypeStatus.index}]Id">
             <c:if test="${!(withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn)}">
             <ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_WITHDRAWAL_AMOUNT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/>
              100.00 
              </c:if>  
        
         
           <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
		          <c:if test = "${moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF'}">
		           <ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_WITHDRAWAL_AMOUNT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/>
              100.00 
              </c:if>
              	 <c:if test = "${!(moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF')}">
              	 <ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_WITHDRAWAL_AMOUNT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/>
              0.00
              </c:if>
              </c:if>
                </span>
            
              
		          
            <span id="requestedPercentageColPercentageMoneyTypeRow[${moneyTypeStatus.index}]Id">
             <ps:fieldHilight name="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalPercentage" singleDisplay="true">
			    <ps:activityHistory itemNumber="${activityConstants.DYN_MONEY_TYPE}" secondNumber="${activityMoneyTypeConstants.MT_WITHDRAWAL_AMOUNT.id}" secondName="${moneyType.withdrawalRequestMoneyType.moneyTypeId}"/>
			 </ps:fieldHilight>
			 <c:choose>
				 <c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
	                	${withdrawalForm.withdrawalRequestUi.withdrawalRequest.moneyTypes[moneyTypeStatus.index].withdrawalPercentage}
	             </c:when>
				 <c:otherwise>
<form:input path="withdrawalRequestUi.moneyTypes[${moneyTypeStatus.index}].withdrawalPercentage" maxlength="7" onchange="return handleMoneyTypeRequestedPercentageChanged(${moneyTypeStatus.index});" size="7" cssClass="mandatoryInputRight" id="moneyTypeRequestedPercentageId[${moneyTypeStatus.index}]"/>





	              </c:otherwise>
              </c:choose>
            </span>
           </td>
        </tr>
      </c:forEach>
      <%-- End of MoneyType loop --%>
      <tr class="datacell1" valign="top">
        <td>&nbsp;</td>
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td align="right"><strong>Total:</strong></td>
		<c:if test="${!(withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn)}">
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td>&nbsp;</td>
		</c:if>
		<c:if test="${(withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn)}">
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td>&nbsp;</td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td>&nbsp;</td>
		</c:if>
        <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td align="right">
            <span class="highlightBold">
              <c:choose>
              	<c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
          
             	</c:when>
             	<c:otherwise>
                	<fmt:formatNumber type="currency"
                                currencySymbol=""
                                minFractionDigits="2"
                                value="${withdrawalForm.withdrawalRequestUi.totalAvailableWithdrawalAmount}"/>
                </c:otherwise>
               </c:choose>
            </span>
          </td>
        </c:if>
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td align="right">
          <span class="highlightBold">
            <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.totalRequestedWithdrawalAmount" singleDisplay="true"/>
            <span id="totalRequestedAmountSpanSelectId"></span>
            <span id="totalRequestedAmountSpanSpecificAmountId"></span>
            <span id="totalRequestedAmountSpanMaximumAvailableId">
            <c:choose>
              	<c:when test="${withdrawalForm.withdrawalRequestUi.isPOWFirstTimeForReviewAndApproveForMT}">
          
             	</c:when>
             	<c:otherwise>
             	 <c:if test="${!(withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn)}"> 
	                <fmt:formatNumber type="currency"
	                                currencySymbol=""
	                                minFractionDigits="2"
	                                value="${withdrawalForm.withdrawalRequestUi.totalAvailableWithdrawalAmount}"/>
	                                </c:if>
	                                  <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
		           <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
		             var="moneyType"
		             varStatus="moneyTypeStatus">
		              <c:if test = "${moneyType.withdrawalRequestMoneyType.moneyTypeId == 'EEDEF'}">		         
		           <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${withdrawalForm.withdrawalRequestUi.totalRequestedWithdrawalAmount}"/>		         
		          </c:if>
		               </c:forEach> 
		               </c:if>       
	           </c:otherwise>
	       </c:choose>
            </span>
            <span id="totalRequestedAmountSpanPercentageMoneyTypeId">
              <fmt:formatNumber type="currency"
                                currencySymbol=""
                                minFractionDigits="2"
                                value="${withdrawalForm.withdrawalRequestUi.totalRequestedWithdrawalAmount}"/>
            </span>
          </span>
        </td>
        <td id="requestedPercentageColFooterSeparatorTdId" ${requestedPercentSeparatorTdClass}>
          <span id="requestedPercentageColFooterSeparatorId" ${requestedPercentSeparatorSpanClass}>
            <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
          </span>
        </td>
        <td id="requestedPercentageColFooterTdId" align="right">
          <span id="requestedPercentageColFooterId">&nbsp;</span>
        </td>
      </tr>
    </table>
  </td>
</tr> 
