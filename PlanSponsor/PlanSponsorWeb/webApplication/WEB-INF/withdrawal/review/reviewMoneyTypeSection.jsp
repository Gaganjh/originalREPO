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
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_WITHDRAWAL_AMOUNT_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalAmountSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_VESTING_INFO_LINK}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingInformationText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_RECALCULATE_BUTTON_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="recalculateRolloverText"/>

<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="3">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
          <tr>
            <td class="tableheadTD1">
              <div style="padding-top:5px;padding-bottom:5px">
                <span style="padding-right:2px" id="moneyTypeShowIcon" onclick="showWithdrawalAmountSection();">
                  <img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
                </span>
                <span style="padding-right:2px" id="moneyTypeHideIcon" onclick="hideWithdrawalAmountSection();">
                  <img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
                </span>
                <b><content:getAttribute beanName="withdrawalAmountSectionTitle" attribute="text"/></b>
              </div>
            </td>
            <c:if test="${withdrawalForm.withdrawalRequestUi.showVestingInformationLink}"> 
             <c:if test="${withdrawalForm.withdrawalRequestUi.hasVestingCalled}">
              <td class="tablehead" style="text-align:right" nowrap>
                <b>
                  <a href="javascript:doVestingInformation(${withdrawalForm.withdrawalRequestUi.employeeProfileId})">
                    <content:getAttribute beanName="vestingInformationText" attribute="text"/>
                  </a>
                </b>
              </td>
            </c:if>
            </c:if>  
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="withdrawalAmountTable">
          <tr class="datacell1" valign="top">
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td style="sectionName">
                    <strong>
						Enter amount as 
                    </strong>
                    <span style="padding-left:5px"/>
                    	<ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.amountTypeCode" singleDisplay="true">
                    		<ps:activityHistory itemNumber="${activityConstants.AMOUNT_TYPE_CODE.id}"/>
                    	</ps:fieldHilight>
                    	<c:choose>
                    	
                    		<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
    <form:hidden path="withdrawalRequestUi.withdrawalRequest.amountTypeCode" id="amountTypeCodeId"/>                		


				                <ps:displayDescription collection="${withdrawalAmountTypes}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.amountTypeCode}"/>
				                  
                    		</c:when>
                    		<c:otherwise>
                    		
 <form:select path="withdrawalRequestUi.withdrawalRequest.amountTypeCode" id="amountTypeCodeId" cssClass="mandatory" onchange="updateReviewPage();setRecalculateRequired();">



		                        <form:options items="${withdrawalAmountTypes}" itemValue="code" itemLabel="description"/>
</form:select>
		                      
		                    </c:otherwise>
		               </c:choose>
                    </span>
                    </td>
                    <td>
                    <span id="specificAmountSpan" style="padding-left:10px"/>
                      <strong>
                        Dollar amount 
                      </strong>
                      <ps:fieldHilight name="withdrawalRequestUi.withdrawalAmount" singleDisplay="true"><ps:activityHistory itemNumber="${activityConstants.AMOUNT_VALUE.id}"/></ps:fieldHilight>
                      <c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<fmt:formatNumber type="currency"
		                                currencySymbol=""
		                                minFractionDigits="2"
		                                value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.withdrawalAmount}"/>
                	</c:when>
			    	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalAmount" maxlength="20" onchange="return handleWithdrawalAmountChanged();" size="17" cssClass="mandatoryInputRight" id="withdrawalAmountId"/>





                   </c:otherwise>
			   </c:choose>
                    </span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <%-- Money Type Table --%>
          <jsp:include page="reviewMoneyTypeTable.jsp"/> 
          					<%--MultiPayee Section --%>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo == requestConstants.PAYMENT_TO_MULTIPLE_DESTINATION}">
<c:choose>
<c:when test ="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode =='RE' || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode =='TE'}">
					 <jsp:include page="reviewMultiPayeeOption.jsp"/>   
					</c:when>
</c:choose>
</c:if>                  
          <%-- Fee Section --%>
          <jsp:include page="reviewFeeSection.jsp"/>                        
          <c:if test="${withdrawalForm.withdrawalRequestUi.showOptionForUnvestedAmount}">
          <tr>
            <td>
              <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="top">
                  <td class="datacell1">
                    <span class="sectionName">
                     <strong>
                        Option for unvested money
                      </strong>
                      <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.unvestedAmountOptionCode" singleDisplay="true">
	                      <ps:activityHistory itemNumber="${activityConstants.OPTION_FOR_UNVESTED_AMOUNTS.id}"/>
                      </ps:fieldHilight>
                      <c:choose>
                      <c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<ps:displayDescription collection="${unvestedAmountOptions}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.unvestedAmountOptionCode}"/>
                	</c:when>
			    	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.unvestedAmountOptionCode" id="optionForUnvestedMoneyId" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" onchange="return handleOptionForUnvestedMoneyChanged();">



                        <form:option value="">- select -</form:option>
                        <form:options items="${unvestedAmountOptions}" itemValue="code" itemLabel="description"/>
</form:select>
                      </c:otherwise>
                      </c:choose>
                    </span>
                  </td>
                </tr>
                <tr class="datacell1" valign="top">
                  <td align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                </tr>
              </table>
            </td>
          </tr>
          </c:if>
          <%-- Begin Tax Withholding Section --%>
          <jsp:include page="reviewTaxWithholdingSection.jsp"/>
          <tr class="datacell1" valign="top">
            <td align="right">
              <c:set var="recalculateMouseOverText">
                Tip('<content:getAttribute beanName="recalculateRolloverText" attribute="text" escapeJavaScript="true"/>')
              </c:set>
<input type="submit" id="recalculateButtonId" class="button100Lg" name="action" onclick="return handleRecalculateButtonClicked();" onmouseout="UnTip()" onmouseover="${recalculateMouseOverText}" value="calculate" disabled="disabled"/>






              <span style="padding-right:10px"/>
            </td>
          </tr>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="withdrawalAmountFooter">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
              <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
            </tr>
            <tr>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
  </table>
</div>
              
              
