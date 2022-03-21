<%-- Withdrawal Amount R/O JSP Fragment --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<c:set scope="request" var="withAmtTypes" value="${withdrawalForm.lookupData['WITHDRAWAL_AMOUNT_TYPE']}"/>
<c:set scope="request" var="tpaFeeTypes" value="${withdrawalForm.lookupData['TPA_TRANSACTION_FEE_TYPE']}"/>
<c:set scope="request" var="unvestedOptions" value="${withdrawalForm.lookupData['OPTIONS_FOR_UNVESTED_AMOUNTS']}"/>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%-- Bean Definition for CMA Content --%>
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_WITHDRAWAL_AMOUNT_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalAmountSectionTitle"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_UNVESTED_MONEY_SECTION_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="unvestedMoneySectionContent"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_VESTING_INFO_LINK}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="vestingInformationText"/>

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_TAX_WITHHOLDING_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="taxWithholdingSectionTitle"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PARTIAL_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantHasPartialStatusText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_PRE_1987_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantHasPre1987MoneyTypeText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_WITHDRAWAL_REASON_IS_FULLY_VESTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalReasonIsFullyVestedText"/>

<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_2}" 
  beanName="step2PageBean" /> 
  
<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute beanName="withdrawalAmountSectionTitle" attribute="text"/></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
    
<%-- Show only on View Withdrawal Request page. --%>
<c:if test="${pageId == 'viewRequest'}">

   <c:if test="${empty param.printFriendly}">
    <c:if test="${withdrawalRequestUi.isRequestPending}">
  
    <script type="text/javascript">
      function doVestingInformation(empProfileId) {
      
        var contractId = "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.contractId}";
        var reason = "${withdrawalForm.withdrawalRequestUi.mappedReasonCodeForVesting}";
        var asOfDate = "<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.vestingInformationEventDate}" type="DATE" pattern="MM-dd-yyyy"/>";
        var printURL = "/do/census/vestingInformation/" + "?profileId=" + empProfileId + "&source=wd&printFriendly=true&contractId=" + contractId + "&asOfDate=" + asOfDate + "&wdReason=" + reason;
        window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
      }
    </script>
  
      <c:if test="${withdrawalForm.withdrawalRequestUi.showVestingInformationLink}">  
      <c:if test="${withdrawalForm.withdrawalRequestUi.hasVestingCalled}">  
        <a href="javascript:doVestingInformation(${withdrawalRequestUi.employeeProfileId})">
          <content:getAttribute beanName="vestingInformationText" attribute="text"/>
        </a>
      </c:if> 
      </c:if>
    </c:if>
  </c:if>

</c:if>
    
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500" id="withdrawalAmountTable">
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
  
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <tr class="datacell1" valign="top">
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <strong>Enter amount as</strong>&nbsp;
                  <ps:displayDescription collection="${withAmtTypes}" keyName="code" keyValue="description" 
                    key="${withdrawalRequest.amountTypeCode}"/>                  
                  
<c:if test="${withdrawalRequest.amountTypeCode == requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}">
                  <span style="padding-left:10px"/>
                    <strong>Amount</strong>&nbsp;
                    <fmt:formatNumber value="${withdrawalRequest.withdrawalAmount}" type="currency"/> 
                  </span>
</c:if>

                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr class="datacell1" valign="top">
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top">
                <td class="tablesubhead"><strong>Money type</strong></td>
                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="tablesubhead">
                  <strong>
                    Account balance<c:if test="${withdrawalForm.withdrawalRequestUi.hasPbaOrLoans}"><sup>^</sup></c:if>
                    &nbsp;($)
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
                
                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="tablesubhead"><strong>Vesting (%)</strong></td>

<c:if test="${withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="tablesubhead"><strong>Available amount ($)</strong></td>
</c:if>
                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="tablesubhead"><strong>Requested amount ($)</strong></td>
<c:if test="${withdrawalRequestUi.showRequestedWithdrawalAmountPercentColumn}">
                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="tablesubhead"><strong>Portion of available amount (%)</strong></td>
</c:if>
              </tr>
              
              <%-- Begin MoneyType loop --%>
<c:forEach items="${withdrawalRequest.moneyTypes}" var="moneyType" varStatus="moneyTypeStatus">
  <%-- Check if money type is pre-1987 --%>
  <c:if test="${moneyType.isPre1987MoneyType}">
    <c:set scope="page" var="hasPre1987MoneyType" value="true"/>
  </c:if>
  
              <tr class="${((moneyTypeStatus.count % 2) != 0 ) ? 'datacell1' : 'datacell2'}" valign="top">
                <td><c:out value="${moneyType.moneyTypeName}"/></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
                  <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                      value="${moneyType.totalBalance}" />
                </td>
                 <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
                 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
                  <c:if test = "${moneyType.moneyTypeId == 'EEDEF'}">
            <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.availableHarshipAmount}"/>
	         </c:if>
	         <c:if test = "${!(moneyType.moneyTypeId == 'EEDEF')}">
	          <fmt:formatNumber type="currency"
                               currencySymbol=""
                               minFractionDigits="2"
                               value="${moneyType.availableHarshipAmount}"/>
	         </c:if>
                </td>
                </c:if>
        
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
                  <fmt:formatNumber minFractionDigits="3" maxFractionDigits="3" 
                    value="${moneyType.vestingPercentage}" />
                </td>
                
  <c:if test="${withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
					 <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                    value="${moneyType.availableWithdrawalAmount}" />
					
                </td>
  </c:if>
                <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
				<c:if test="${!withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
                  <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                    value="${moneyType.withdrawalAmount}" />
					</c:if>
					  <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
		          <c:if test = "${moneyType.moneyTypeId == 'EEDEF'}">
				   <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                    value="${moneyType.withdrawalAmount}" />
					</c:if>
					<c:if test = "${!(moneyType.moneyTypeId == 'EEDEF')}">
					<fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                    value="${moneyType.withdrawalAmount}" />
					</c:if>
					</c:if>
                </td>
                
  <c:if test="${withdrawalRequestUi.showRequestedWithdrawalAmountPercentColumn}">
                  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                  <td align="right">
                  <%-- hardcoded 100.00 % for participant initiated reqeusts --%>
                  <c:if test="${withdrawalRequestUi.isParticipantInitiated}">100.00
                  </c:if>
                  <c:if test="${!withdrawalRequestUi.isParticipantInitiated}">
				<c:if test="${!withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
                    <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                      value="${moneyType.withdrawalPercentage}" />
				</c:if>
					  <c:if test="${withdrawalForm.withdrawalRequestUi.showAvailableforHardshipColumn}">
		          <c:if test = "${moneyType.moneyTypeId == 'EEDEF'}">
				   <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                    value="${moneyType.withdrawalPercentage}" />
					</c:if>
					<c:if test = "${!(moneyType.moneyTypeId == 'EEDEF')}">
					0.00
					</c:if>
					</c:if>
                  </c:if>
                  </td>
  </c:if>
                </tr>

</c:forEach>
<%-- End of MoneyType loop --%>
              <tr class="datacell1" valign="top">
                <td>&nbsp;</td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right"><strong>Total:</strong></td>
				<c:if test="${!withdrawalRequestUi.showAvailableforHardshipColumn}">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td>&nbsp;</td>
				</c:if>
				<c:if test="${withdrawalRequestUi.showAvailableforHardshipColumn}">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td>&nbsp;</td>
				<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td>&nbsp;</td>
				</c:if>
<c:if test="${withdrawalRequestUi.showAvailableWithdrawalAmountColumn}">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">
                  <span class="highlightBold">
                    <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                      value="${withdrawalRequestUi.totalAvailableWithdrawalAmount}"/>
                  </span>
                </td>
</c:if>


                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				
                <td align="right">
<c:if test="${withdrawalRequestUi.showTotalRequestedWithdrawalAmount}">
                  <span  class="highlightBold" id="totalRequestedAmount">
                    <fmt:formatNumber type="currency" currencySymbol="" minFractionDigits="2" maxFractionDigits="2"
                      value="${withdrawalRequestUi.totalRequestedWithdrawalAmount}"/>
                  </span>
</c:if>
                </td>
<c:if test="${withdrawalRequestUi.showRequestedWithdrawalAmountPercentColumn}">
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right">&nbsp;</td>
</c:if>
              </tr>
            </table>
          </td>
        </tr>
          					<%--MultiPayee Section --%>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo == requestConstants.PAYMENT_TO_MULTIPLE_DESTINATION}">
<c:choose>
<c:when test ="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode =='RE' || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode =='TE'}">
					 <jsp:include page="conformationMultiPayeeOption.jsp"/>   
					</c:when>
</c:choose>
</c:if> 
<!-- TPA transaction fee -->
<c:if test="${withdrawalRequest.contractInfo.hasATpaFirm or not empty withdrawalForm.withdrawalTransactionalFee.feeAmount}">
        <tr class="datacell1" valign="top">
          <td>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <!-- Begin Fee Loop -->
              <c:if test="${withdrawalRequest.contractInfo.hasATpaFirm }">
				  <c:forEach items="${withdrawalRequest.fees}" var="fee" varStatus="feeStatus">
				              <tr valign="top" class="datacell2">
				                <td class="sectionNameColumn">
				                  <strong>TPA withdrawal fee</strong>
				                </td>
				                <td class="indentedValueColumn">
				                  <c:choose>
				                    <c:when test="${not empty fee.value}">
									  <fmt:formatNumber minFractionDigits="2" maxFractionDigits="4"
				                      value="${fee.value}"/>
										<ps:displayDescription collection="${tpaFeeTypes}" 
				                                             keyName="code" 
				                                             keyValue="description" 
				                                             key="${fee.typeCode}"/>
				                    </c:when>
				                    <c:otherwise>
				                      None
				                    </c:otherwise>
				                  </c:choose>
				                </td>
				              </tr>
				   </c:forEach>
			  </c:if>
  
              <c:if test="${not empty withdrawalForm.withdrawalTransactionalFee.feeAmount}">
				  <tr valign="top" class="datacell2">
				                <td class="sectionNameColumn">
				                  <strong>Withdrawal processing fee</strong>
				                </td>
	                <td class="indentedValueColumn">
						 <fmt:formatNumber minFractionDigits="2" maxFractionDigits="4"
	                      value="${withdrawalForm.withdrawalTransactionalFee.feeAmount}"/>
							dollars
	                </td>
	              </tr>
              </c:if>
              <!-- End Fee Loop -->

              <!-- Fees static content -->
              <c:if test="${withdrawalRequestUi.showStaticContent}">
               <tr>
                 <td style="left-padding:4px;" colspan="2">
                   <ul>
                     <c:if test="${(((withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence ne 'PR') || (withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode ne 'PR'))
       	     &&(withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence ne 'CT'))
       	     }">
       	     				<content:getAttribute beanName="step2PageBean" attribute="body1Header"/>
       	     		 </c:if>
		      	     <c:if test="${(((withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'PR') 
       	     && (withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractIssuedStateCode eq 'PR'))
       	     || (withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'CT'))
       	    }">
		      	     	<content:getAttribute beanName="step2PageBean" attribute="body3Header"/>
		      	     </c:if>
                     <c:if test="${withdrawalForm.withdrawalRequestUi.participantHasPartialStatus}">
                       <content:getAttribute beanName="participantHasPartialStatusText" attribute="text"/>
                     </c:if>
                     <c:if test="${withdrawalForm.withdrawalRequestUi.hasPre1987MoneyTypes}">
                       <content:getAttribute beanName="participantHasPre1987MoneyTypeText" attribute="text"/>
                     </c:if>
                     <c:if test="${withdrawalForm.withdrawalRequestUi.showReasonIsFullyVestedFromPlanSpecialMessage}">
                       <content:getAttribute beanName="withdrawalReasonIsFullyVestedText" attribute="text"/>
                     </c:if>
                    </ul>
                  </td>
               </tr>
              </c:if>
              <c:if test="${withdrawalRequestUi.showStaticContent}">
                 <tr>
                  <td style="left-padding:4px;" colspan="2">
                    <ul>
                      <content:getAttribute beanName="step2PageBean" attribute="body1"/>
                    </ul>
                 </td>
               </tr>
             </c:if>
             <c:if test="${withdrawalForm.withdrawalRequestUi.showOptionForUnvestedAmount}"> 
              <tr class="datacell1">
                <td class="datadivider" colspan="2"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </tr>
              </c:if>
            </table>
          </td>
        </tr>
</c:if>
        
    <c:if test="${withdrawalForm.withdrawalRequestUi.showOptionForUnvestedAmount}">    
        <tr>
          <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr class="datacell1" valign="top">
                <td class="sectionNameColumn">
                  <strong>Option for unvested money</strong>
                </td>
                <td class="indentedValueColumn">
                  <ps:displayDescription collection="${unvestedOptions}" 
                                         keyName="code" 
                                         keyValue="description" 
                                         key="${withdrawalRequest.unvestedAmountOptionCode}"/>
                </td>
              </tr>
              <tr class="datacell1" valign="top">
                <td align="right" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </tr>
            </table>
          </td>
        </tr>
 </c:if>       
        <%-- Begin Tax Withholding Section --%>
<c:if test="${withdrawalRequestUi.payDirectlyTome !='PAAT'}">
<c:if test="${withdrawalRequestUi.showTaxWitholdingSection}">
        <tr>
          <td width="100%">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr>
                <td class="datadivider" colspan="2"><b><content:getAttribute beanName="taxWithholdingSectionTitle" attribute="text"/></b> </td>
              </tr>
              <tr class="datacell1" valign="top">
                <td style="padding-left:4px;" width="20%"><strong>Federal tax rate</strong></td>
                <td><c:out value="${withdrawalRequestUi.federalTaxPercent}"/>% of taxable withdrawal amount</td>
              </tr>
  <c:if test="${withdrawalRequestUi.stateTaxPercent != null}" >
              <tr class="datacell1" valign="top">
                <td style="padding-left:4px;" width="20%"><strong>State tax rate </strong></td>
                <td>
                  <c:if test="${withdrawalRequestUi.stateTaxType == 'W'}" >
                    <c:out value="${withdrawalRequestUi.stateTaxPercent}"/>% of taxable withdrawal amount
                  </c:if>
                  <c:if test="${withdrawalRequestUi.stateTaxType == 'F'}" >
                    <c:out value="${withdrawalRequestUi.stateTaxPercent}"/>% of federal tax
                  </c:if>
                </td>
              </tr>
  </c:if>
            </table>
          </td>
        </tr>
        </c:if>
 </c:if>


        <%-- End Tax Withholding section --%>
      
      </table>
    </td>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <tr>
    <td colspan="3">
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
    </td>
  </tr>
</table>
