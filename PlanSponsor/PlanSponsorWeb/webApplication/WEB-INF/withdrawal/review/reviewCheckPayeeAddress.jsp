<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />
<un:useConstants var="activityPayeeConstants" className="com.manulife.pension.service.withdrawal.helper.PayeeFieldDef" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
      Address line 1
    </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.addressLine1" singleDisplay="true">
      	      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_LINE1.id}" secondName="${payeeIndex+1}"/>
      </ps:fieldHilight>
      <c:choose>
      	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
      		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.addressLine1}
      	</c:when>
      	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine1" maxlength="30" onchange="return handleCheckPayeeAddressLine1Changed(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="checkPayeeAddressLine1Id[${recipientIndex}][${payeeIndex}]"/>




      	</c:otherwise>
      </c:choose>

  </td>
</tr>
<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE && payee.paymentToDisplay !='Employer Sponsored Qualified Plan'}">
<c:choose>
<c:when test ="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='RE' || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode !='TE'}">
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
      Address line 2
    </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.addressLine2" singleDisplay="true">
	      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_LINE2.id}" secondName="${payeeIndex+1}"/>
	  </ps:fieldHilight>
	  
	  <c:choose>
      	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
      		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.addressLine2}
      	</c:when>
      	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine2" maxlength="30" onchange="return handleCheckPayeeAddressLine2Changed(${recipientIndex}, ${payeeIndex});" id="checkPayeeAddressLine2Id[${recipientIndex}][${payeeIndex}]"/>



      	</c:otherwise>
      </c:choose>
  
  </td>
</tr>
</c:when>
</c:choose>
</c:if>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
      City
    </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.city" singleDisplay="true">
      	      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_CITY.id}" secondName="${payeeIndex+1}"/>
      </ps:fieldHilight>
      <c:choose>
      	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
      		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.city}
      	</c:when>
      	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.city" maxlength="25" onchange="return handleCheckPayeeCityChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="checkPayeeCityId[${recipientIndex}][${payeeIndex}]"/>




      	</c:otherwise>
      </c:choose>
 
   </td>
 </tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
     State
   </strong>
 </td>
 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
 <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.stateCode" singleDisplay="true">
	      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_STATE.id}" secondName="${payeeIndex+1}"/>
	  </ps:fieldHilight>
   <span id="checkPayeeStateDropdownSpanId[${recipientIndex}][${payeeIndex}]">
   
      <c:choose>
      	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
      		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.stateCode}
      	</c:when>
      	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" cssClass="mandatory" id="checkPayeeStateDropdownId[${recipientIndex}][${payeeIndex}]" onchange="return handleCheckPayeeStateDropdownChanged(${recipientIndex}, ${payeeIndex});">



       			<form:option value="">- select -</form:option>
       			<form:options items="${states}" itemValue="code" itemLabel="code"/>
</form:select>
      	</c:otherwise>
      </c:choose>

   </span>
   <span id="checkPayeeStateInputSpanId[${recipientIndex}][${payeeIndex}]">
   
         <c:choose>
      	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
      		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.stateCode}
      	</c:when>
      	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" maxlength="2" onchange="return handleCheckPayeeStateInputChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="checkPayeeStateInputId[${recipientIndex}][${payeeIndex}]"/>




      	</c:otherwise>
      </c:choose>
 
    </span>
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
     Zip Code
   </strong>
 </td>
 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
 <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.zipCode" singleDisplay="true">
      	<ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_ZIP.id}" secondName="${payeeIndex+1}"/>
      </ps:fieldHilight>
   <span id="checkPayeeZipSingleSpanId[${recipientIndex}][${payeeIndex}]">
   
    	 <c:choose>
           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode}
           	</c:when>
           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode" maxlength="9" onchange="return handleCheckPayeeZipCodeChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="checkPayeeZipCodeId[${recipientIndex}][${payeeIndex}]"/>




            </c:otherwise>
       </c:choose>     

   </span>
   <span id="checkPayeeZipDoubleSpanId[${recipientIndex}][${payeeIndex}]">
   
                
        	 <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           		${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode1)}
	           	</c:when>
	           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode1" maxlength="5" onchange="return handleCheckPayeeZipCode1Changed(${recipientIndex}, ${payeeIndex});" size="5" cssClass="mandatory" id="checkPayeeZipCode1Id[${recipientIndex}][${payeeIndex}]"/>





                </c:otherwise>
           </c:choose>     
           <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           	    <c:if test= "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.
                 			recipients[recipientIndex].payees[payeeIndex].address.zipCode2 != ''}">
                 		&ndash;	
                 	</c:if>
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode2}
	           	</c:when>
	           	<c:otherwise>
	           		&ndash;
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode2" maxlength="4" onchange="return handleCheckPayeeZipCode2Changed(${recipientIndex}, ${payeeIndex});" size="4" id="checkPayeeZipCode2Id[${recipientIndex}][${payeeIndex}]"/>




                </c:otherwise>
           </c:choose>

    </span>
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
     Country
   </strong>
 </td>
 <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
 <td class="indentedValueColumn">
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.countryCode" singleDisplay="true">
	      <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_COUNTRY.id}" secondName="${payeeIndex+1}"/>
	  </ps:fieldHilight>
	  
	       <c:choose>
     	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
     	<form:hidden path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.countryCode" id="checkPayeeCountryId[${recipientIndex}][${payeeIndex}]"/>

     	
    		<ps:displayDescription collection="${countries}" keyName="value" keyValue="label" 
	                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.countryCode}"/>
        </c:when>
     	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.countryCode" cssClass="mandatory" id="checkPayeeCountryId[${recipientIndex}][${payeeIndex}]" onchange="return handleCheckPayeeCountryChanged(${recipientIndex}, ${payeeIndex});">



		     <form:option value="">- select -</form:option>
		      <form:options items="${countries}" itemValue="value" itemLabel="label"/>
</form:select>
     	
     	</c:otherwise>
     
     </c:choose>

  </td>
</tr>
