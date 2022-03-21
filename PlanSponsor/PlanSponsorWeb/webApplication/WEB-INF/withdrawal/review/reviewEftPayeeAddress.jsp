<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />
<un:useConstants var="activityPayeeConstants" className="com.manulife.pension.service.withdrawal.helper.PayeeFieldDef" />

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
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine1" maxlength="30" onchange="return handleEftPayeeAddressLine1Changed(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeAddressLine1Id[${recipientIndex}][${payeeIndex}]"/>




				</c:otherwise>
           </c:choose>
 
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
  	 <strong>
  	 	 Address line 2
  	 </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
 	 <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.addressLine2" singleDisplay="true">
       <ps:activityHistory itemNumber="${activityConstants.DYN_PAYEE_TYPE}" secondNumber="${activityPayeeConstants.P_LINE2.id}" secondName="${payeeIndex+1}"/>
 	 </ps:fieldHilight>
 	 
 	       <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.addressLine2}
	           	</c:when>
	           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine2" maxlength="30" onchange="return handleEftPayeeAddressLine2Changed(${recipientIndex}, ${payeeIndex});" id="eftPayeeAddressLine2Id[${recipientIndex}][${payeeIndex}]"/>



				</c:otherwise>
           </c:choose>
 
  </td>
</tr>
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
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.city" maxlength="25" onchange="return handleEftPayeeCityChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeCityId[${recipientIndex}][${payeeIndex}]"/>




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
    <span id="eftPayeeStateDropdownSpanId[${recipientIndex}][${payeeIndex}]">
    
    	 <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.stateCode}
	           	</c:when>
	           	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" cssClass="mandatory" id="eftPayeeStateDropdownId[${recipientIndex}][${payeeIndex}]" onchange="return handleEftPayeeStateDropdownChanged(${recipientIndex}, ${payeeIndex});">



				        <form:option value="">- select -</form:option>
				        <form:options items="${states}" itemValue="code" itemLabel="code"/>
</form:select>
				</c:otherwise>
           </c:choose>
  
    </span>
    <span id="eftPayeeStateInputSpanId[${recipientIndex}][${payeeIndex}]">
    
        	 <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.stateCode}
	           	</c:when>
	           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" maxlength="2" onchange="return handleEftPayeeStateInputChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeStateInputId[${recipientIndex}][${payeeIndex}]"/>




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
    <span id="eftPayeeZipSingleSpanId[${recipientIndex}][${payeeIndex}]">
        	 <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode}
	           	</c:when>
	           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode" maxlength="9" onchange="return handleEftPayeeZipCodeChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeZipCodeId[${recipientIndex}][${payeeIndex}]"/>




                </c:otherwise>
           </c:choose>     

    </span>
    <span id="eftPayeeZipDoubleSpanId[${recipientIndex}][${payeeIndex}]">
    
              
        	 <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           	<form:hidden path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode1" id="eftPayeeZipCode1Id[${recipientIndex}][${payeeIndex}]"/>

	           		${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode1)}
	           	</c:when>
	           	<c:otherwise>
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode1" maxlength="5" onchange="return handleEftPayeeZipCode1Changed(${recipientIndex}, ${payeeIndex});" size="5" cssClass="mandatory" id="eftPayeeZipCode1Id[${recipientIndex}][${payeeIndex}]"/>





                </c:otherwise>
           </c:choose> 
            <c:choose>
	           	<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly or !payee.payeeAddressEditable}">
	           	<form:hidden path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode2" id="eftPayeeZipCode2Id[${recipientIndex}][${payeeIndex}]"/>

	           	    <c:if test= "${withdrawalForm.withdrawalRequestUi.withdrawalRequest.
                 			recipients[recipientIndex].payees[payeeIndex].address.zipCode2 != ''}">
                 		&ndash;	
                 	</c:if>
	           		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.zipCode2}
	           	</c:when>
	           	<c:otherwise>
	           		&ndash;
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode2" maxlength="4" onchange="return handleEftPayeeZipCode2Changed(${recipientIndex}, ${payeeIndex});" size="4" id="eftPayeeZipCode2Id[${recipientIndex}][${payeeIndex}]"/>




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
     	<form:hidden path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.countryCode" id="eftPayeeCountryId[${recipientIndex}][${payeeIndex}]"/>

     	
    		<ps:displayDescription collection="${countries}" keyName="value" keyValue="label" 
	                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].address.countryCode}"/>
        </c:when>
     	<c:otherwise>
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.countryCode" cssClass="mandatory" onchange="return handleEftPayeeCountryChanged(${recipientIndex}, ${payeeIndex});" id="eftPayeeCountryId[${recipientIndex}][${payeeIndex}]">



			      <form:option value="">- select -</form:option>
			      <form:options items="${countries}" itemValue="value" itemLabel="label"/>
</form:select>
     	
     	</c:otherwise>
     
     </c:choose>
 
  </td>
</tr>
