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
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_EFT_PAYEE_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="eftPayeeSectionTitle"/>

<div id="eftPayeeSectionId[${recipientIndex}][${payeeIndex}]">
 <table border="0" cellpadding="0" cellspacing="0" width="498">
    <tr valign="top" class="tablesubhead">
     <td colspan="3"><strong><content:getAttribute beanName="eftPayeeSectionTitle" attribute="text"/></strong></td>
    </tr>
    <tr class="datacell1" valign="top">
    	 <td class="subsectionTitleColumn">
    	   <strong>${payee.eftPayeeSubSubHeader}</strong>
    	 </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td/>
    </tr>
    <c:if test="${fn:length(paymentToTypes) > 1}">
	<c:forEach items="${paymentToTypes}" var="entry">
    <c:if test="${(entry.code) == 'M'}">
    <c:if test="${ ((requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) || (requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) ||  (requestConstants.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)
    || (requestConstants.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode) || (requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE == withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode)) && (payee.paymentToDisplay == 'Rollover all to IRA')}">
	
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
      <form:radiobutton path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].eftRolloverType"  cssClass="mandatory" value="Traditional IRA" id="eftPayeeRolloverTypeId[${recipientIndex}][${payeeIndex}]"   onchange="return handlePayeeRolloverTypeChanged(${recipientIndex}, ${payeeIndex});"/> Traditional IRA
      </td>
    </tr>
	
   <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
      	 <strong>
      	 
      	 </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
	  <form:radiobutton path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].eftRolloverType"   cssClass="mandatory" value="Roth IRA" id="eftPayeeRolloverTypeId[${recipientIndex}][${payeeIndex}]"   onchange="return handlePayeeRolloverTypeChanged(${recipientIndex}, ${payeeIndex});"/> Roth IRA			 
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
      	   <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.organizationName" singleDisplay="true"/>
      	   ${payee.subSectionNameColumn}
      	 </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
		   
	  <c:choose>
          <c:when test="${payee.eftPayeeNameEditable}">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].eftOrganizationName" maxlength="43" onchange="return handleEftPayeeFiNameChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeFiNameId[${recipientIndex}][${payeeIndex}]"/>




          </c:when>
          <c:otherwise>
             ${payee.withdrawalRequestPayee.organizationName}
          </c:otherwise>
        </c:choose>				   
      </td>
    </tr>
		<jsp:include page="step2EftPayeeAddress.jsp"/>
    <tr class="datacell1" valign="top">
    	 <td class="subsectionTitleColumn">
    	   <strong>Bank details</strong>
    	 </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td/>
    </tr>
    <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
        <strong>
          <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].bankTransitNumber" singleDisplay="true"/>
          ABA / Routing number
        </strong>		 
      </td>
	 
	  
	  


                

      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].bankTransitNumber" maxlength="9" onchange="return handleEftPayeeBankAbaNumberChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" onkeypress="return clearSelectBankName(event,${recipientIndex}, ${payeeIndex})"  id="eftPayeeBankAbaNumberId[${recipientIndex}][${payeeIndex}]"/>

 <a href="javascript:doBankNameList(document.getElementById('eftPayeeBankAbaNumberId[${recipientIndex}][${payeeIndex}]').value,${recipientIndex}, ${payeeIndex})" class="show_form">
                    Select bank name
                  </a>


      </td>
    </tr>
	  <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
        <strong>
          <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.bankName" singleDisplay="true"/>
          Bank / Branch name
        </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">


<form:input  path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankName" size="50" onchange="return handleEftPayeeBankNameChanged(${recipientIndex}, ${payeeIndex});" readonly="true"  id="eftPayeeBankNameId[${recipientIndex}][${payeeIndex}]" style = "border: 1px solid #fff9f2;"/>

      </td>
    </tr>
    <tr class="datacell1" valign="top">
      <td class="subsectionNameColumn">
        <strong>
          <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].withdrawalRequestPayee.bankAccountNumber" singleDisplay="true"/>
          Account number
        </strong>
      </td>
      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].paymentInstruction.bankAccountNumber" maxlength="17" onchange="return handleEftPayeeBankAccountNumberChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeBankAccountNumberId[${recipientIndex}][${payeeIndex}]"/>




      </td>
    </tr>

  </table>
</div>
