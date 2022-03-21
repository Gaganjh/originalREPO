<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_CHEQUE_PAYEE_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="chequePayeeSectionTitle"/>

<div id="checkPayeeSectionId[${recipientIndex}][${payeeIndex}]">
   <table border="0" cellpadding="0" cellspacing="0" width="498">
     <tr valign="top" class="tablesubhead">
       <td colspan="3"><strong><content:getAttribute beanName="chequePayeeSectionTitle" attribute="text"/></strong></td>
     </tr>
      <tr class="datacell1" valign="top">
       <td class="subsectionTitleColumn">
      	   <strong>${payee.checkPayeeSubSubHeader}</strong>
       </td>
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td/>
     </tr>
     <c:if test="${fn:length(paymentToTypes) > 1}">
	<c:forEach items="${paymentToTypes}" var="entry">
    <c:if test="${(entry.code) == 'M'}">
       <c:if test="${ ((requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) || (requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)
	   ||  (requestConstants.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) || (requestConstants.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) || (requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)) && (payee.paymentToDisplay == 'Rollover all to IRA')}">

    <tr><td><br></td></tr>
    <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
      	 <strong>
      	  <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.rolloverType" singleDisplay="true" />
      	  Rollover  Type 
      	 </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
	  <form:radiobutton path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].checkRolloverType"  id="checkPayeeRolloverTypeId[${recipientIndex}][${payeeIndex}]"   onchange="return handleCheckPayeeRolloverTypeChanged(${recipientIndex}, ${payeeIndex});"  cssClass="mandatory" value="Traditional IRA"/> Traditional IRA      
	  </td>
    </tr>
   <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
      	 <strong>
      	 </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
	  <form:radiobutton path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].checkRolloverType"  id="checkPayeeRolloverTypeId[${recipientIndex}][${payeeIndex}]"   onchange="return handleCheckPayeeRolloverTypeChanged(${recipientIndex}, ${payeeIndex});"  cssClass="mandatory" value="Roth IRA"/> Roth IRA      
	  </td>
    </tr>
    <tr><td><br></td></tr>
   </c:if>
   </c:if>
   </c:forEach>
   </c:if>
     <tr class="datacell1" valign="top">
       <td class="subsectionNameColumn">
         <strong>
           <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].organizationName" singleDisplay="true"/>
           ${payee.subSectionNameColumn}
         </strong>
       </td>
       <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td class="indentedValueColumn">
         <c:choose>
           <c:when test="${payee.checkPayeeNameEditable}">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].checkOrganizationName" maxlength="70" onchange="return handleCheckPayeeNameChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="checkPayeeNameId[${recipientIndex}][${payeeIndex}]"/>




           </c:when>
           <c:otherwise>
             <c:out value="${payee.checkOrganizationName}"/>
           </c:otherwise>
         </c:choose>
        </td>
      </tr>
			<jsp:include page="step2CheckPayeeAddress.jsp"/>
     

  </table>
</div>
