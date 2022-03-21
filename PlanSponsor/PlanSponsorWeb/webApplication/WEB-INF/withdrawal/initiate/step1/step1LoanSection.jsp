<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>


<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<c:set scope="request" var="loans"  value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}" />
<c:forEach var="irsLoan" items="${irsDistCodesLoansTypes}">
   </c:forEach>
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_LOAN_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanSectionTitle"/>

<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="3" class="tableheadTD1">
        <div style="padding-top:5px;padding-bottom:5px">
          <span style="padding-right:2px" id="loanShowIcon" onclick="showLoanSection();">
            <img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
          </span>
          <span style="padding-right:2px" id="loanHideIcon" onclick="hideLoanSection();">
            <img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
          </span>
          <b><content:getAttribute beanName="loanSectionTitle" attribute="text"/></b>
        </div>
      </td>
    </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="loanTable">
          <tr class="datacell1" valign="top">
            <td class="tablesubhead"><b>Loan number</b></td>
            <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="tablesubhead"><b>Outstanding balance($)</b></td>
          </tr>
          <c:forEach var="loan" 
                     items="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.loans}">
            <tr class="datacell1" valign="top">
              <td class="shortSectionNameColumn">
                <a href="javascript:doLoanDetail(${loan.loanNo}, ${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.employeeProfileId)}, ${withdrawalForm.withdrawalRequestUi.contractId})">
                  Loan #${loan.loanNo}
                </a>
              </td>
              <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="longIndentedValueColumn">
                <span style="width: 130; text-align: right; display: block;">
                  <fmt:formatNumber type="currency"
                                    currencySymbol=""
                                    minFractionDigits="2"
                                    value="${loan.outstandingLoanAmount}"/>
                </span>              
              </td>
            </tr>
          </c:forEach>
          <tr class="datacell1" valign="top" >
            <td class="shortSectionNameColumn"><strong>Total</strong></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="longIndentedValueColumn">
              <span class="highlightBold" style="width: 130; text-align: right; display: block;">
                <fmt:formatNumber type="currency"
                                  currencySymbol=""
                                  minFractionDigits="2"
                                  value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.totalOutstandingLoanAmt}"/>
              </span>              
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          </tr>
          <tr class="datacell1" valign="top">
            <td class="shortSectionNameColumn">
              <strong>
                <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.loanOption"/>
                What should be done with all outstanding loans?
              </strong>
            </td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="longIndentedValueColumn">
 <form:select id="loanOptionId" onchange="return handleLoanOptionChanged();" path="withdrawalRequestUi.withdrawalRequest.loanOption">


                <form:options items="${loanTypes}" itemValue="code" itemLabel="description"/>               
</form:select>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td>
              <span id="loanIrsDistributionCodeCol1Id" class="shortSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
              <strong>
                <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.irsDistributionCodeLoanClosure"/>
                IRS distribution code for loans
              </strong>
              </span>
            </td>
            <td class="datadivider">
              <span id="loanIrsDistributionCodeCol2Id"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></span>
            </td>
            <td>
              <span id="loanIrsDistributionCodeCol3Id" class="longIndentedValueColumn">
              <form:select id="loanIrsDistributionCodeId"
              			   cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" 
                           onchange="return handleLoanIrsDistributionCodeChanged();" 
                           path="withdrawalRequestUi.withdrawalRequest.irsDistributionCodeLoanClosure">
                <form:option value="">- select -</form:option>
                <c:forEach var="irsLoansTypes" items="${irsDistCodesLoansTypes}">
                <c:if test="${fn:trim(irsLoansTypes.code) ne '1' and fn:trim(irsLoansTypes.code) ne '2' and fn:trim(irsLoansTypes.code) ne '7'}">
														
															<form:option  value="${irsLoansTypes.code}">
															<c:out value="${irsLoansTypes.description}"/>
															</form:option>
															</c:if>
															</c:forEach>
      
              </form:select>
              </span>
            </td>
          </tr>
          <tr class="datacell1" valign="top">
            <td colspan="3" class="indentedValue">
              <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
            </td>
          </tr>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="loanFooter">
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
                  
