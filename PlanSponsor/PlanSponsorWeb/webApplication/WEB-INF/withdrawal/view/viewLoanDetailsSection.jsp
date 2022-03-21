<%-- Withdrawal Loan Details R/O JSP Fragment --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<c:set scope="request" var="loanTypes" value="${withdrawalForm.lookupData['LOAN_OPTION_TYPE']}"/>
<c:set scope="request" var="irsDistCodesLoansTypes" value="${withdrawalForm.lookupData['IRS_CODE_LOAN']}"/>

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_LOAN_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="loanInformation"/>

<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1}" 
  beanName="step1PageBean" /> 

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="loanInformation" /></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
           
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <tr class="datacell1" valign="top">
          <td class="tablesubhead"><b>Loan number</b></td>
          <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="tablesubhead"><b>Outstanding balance ($)</b></td>
        </tr>
        
        
        <c:forEach items="${withdrawalRequest.loans}" var="loan" varStatus="loanStatus">
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn">
          
	<c:if test="${empty param.printFriendly}"> 
  		<c:choose>
    		<c:when test="${withdrawalRequestUi.isRequestPending && param.pageTypeView}">
            	<a href="javascript:doLoanDetail(<c:out value="${loan.loanNo}"/>, <c:out value="${withdrawalRequest.employeeProfileId}"/>, <c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractId}"/>)">
              Loan #<c:out value="${loan.loanNo}"/>
            </a>
    		</c:when>
    	<c:otherwise>
            Loan #<c:out value="${loan.loanNo}"/>
   		 </c:otherwise>
  		</c:choose>
  	</c:if>



	<c:if test="${param.printFriendly}">
              Loan #<c:out value="${loan.loanNo}"/>
     </c:if>


          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <fmt:formatNumber type="currency" currencySymbol="" value="${loan.outstandingLoanAmount}" />
          </td>
       </tr>
       </c:forEach>
         
<!--
       <tr class="datacell1" valign="top">
         <td height="1" colspan="5" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       </tr>
-->
       <tr class="datacell1" valign="top" >
         <td class="sectionNameColumn"><strong>Total</strong></td>
         <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         <td class="indentedValueColumn">
           <fmt:formatNumber type="currency" currencySymbol="" value="${withdrawalRequest.totalOutstandingLoanAmt}" />
         </td>
       </tr>
       <tr class="datacell1" valign="top">
         <td height="1" colspan="5" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       </tr>
       <tr class="datacell1" valign="top" >
         <td class="sectionNameColumn"><strong>What should be done with all outstanding loans?</strong></td>
         <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         <td class="indentedValueColumn">
           <ps:displayDescription collection="${loanTypes}" keyName="code" keyValue="description" 
             key="${withdrawalRequest.loanOption}"/>
         </td>
       </tr>
       
      <c:if test="${withdrawalRequestUi.showIrsLoanDistribution}">
       <tr class="datacell1" valign="top">
         <td class="sectionNameColumn"><strong>IRS distribution code for loans</strong></td>
         <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         <td class="indentedValueColumn">
           <ps:displayDescription collection="${irsDistCodesLoansTypes}" 
                                  keyName="code" 
                                  keyValue="description" 
                                  key="${withdrawalRequest.irsDistributionCodeLoanClosure}"/>
         </td>
       </tr>
       </c:if>
        <tr class="datacell1" valign="top">
          <td height="1" colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>             
  <c:if test="${withdrawalRequestUi.showStaticContent}">
        <tr class="datacell1" valign="top">
          <td colspan="3">
            <content:getAttribute beanName="step1PageBean" attribute="body3"/>
          </td>
        </tr>
  </c:if>
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
