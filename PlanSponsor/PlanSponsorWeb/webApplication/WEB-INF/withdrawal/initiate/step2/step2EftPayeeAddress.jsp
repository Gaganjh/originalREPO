<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>




<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
  	 <strong>
  	 	 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.addressLine1" singleDisplay="true"/>
  	 	 Address line 1
  	 </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
	<c:choose>
	     <c:when test="${payee.payeeAddressEditable}">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine1" maxlength="30" onchange="return handleEftPayeeAddressLine1Changed(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeAddressLine1Id[${recipientIndex}][${payeeIndex}]"/>




	     </c:when>
	     <c:otherwise>
		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.addressLine1}
	     </c:otherwise>
	</c:choose>	
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
  	 <strong>
  	 	 <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.addressLine2" singleDisplay="true"/>
  	 	 Address line 2
  	 </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
	<c:choose>
	     <c:when test="${payee.payeeAddressEditable}">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.addressLine2" maxlength="30" onchange="return handleEftPayeeAddressLine2Changed(${recipientIndex}, ${payeeIndex});" id="eftPayeeAddressLine2Id[${recipientIndex}][${payeeIndex}]"/>



             </c:when>
	     <c:otherwise>
		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.addressLine2}
	     </c:otherwise>
	</c:choose>	               
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
  	 <strong>
  	 	 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.city" singleDisplay="true"/>
  	 	 City
  	 </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
	<c:choose>
	     <c:when test="${payee.payeeAddressEditable}">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.city" maxlength="25" onchange="return handleEftPayeeCityChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeCityId[${recipientIndex}][${payeeIndex}]"/>




             </c:when>
	     <c:otherwise>
		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.city}
	     </c:otherwise>
	</c:choose>	                    
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
  	 <strong>
  	   <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.stateCode" singleDisplay="true"/>
  	   State
  	 </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
    	<c:choose>
    	     <c:when test="${payee.payeeAddressEditable}">    	     
		    <span id="eftPayeeStateDropdownSpanId[${recipientIndex}][${payeeIndex}]">
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" cssClass="mandatory" id="eftPayeeStateDropdownId[${recipientIndex}][${payeeIndex}]" onchange="return handleEftPayeeStateDropdownChanged(${recipientIndex}, ${payeeIndex});">



			<form:option value="">- select -</form:option>
			<form:options items="${states}" itemValue="code" itemLabel="code"/>
</form:select>
		    </span>
		    <span id="eftPayeeStateInputSpanId[${recipientIndex}][${payeeIndex}]">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.stateCode" maxlength="2" onchange="return handleEftPayeeStateInputChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeStateInputId[${recipientIndex}][${payeeIndex}]"/>




		    </span>             
             </c:when>
    	     <c:otherwise>
    		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.stateCode}
    	     </c:otherwise>
	</c:choose>
    </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.zipCode" singleDisplay="true"/>
      Zip Code
    </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
    	<c:choose>
    	     <c:when test="${payee.payeeAddressEditable}">
		    <span id="eftPayeeZipSingleSpanId[${recipientIndex}][${payeeIndex}]">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode" maxlength="9" onchange="return handleEftPayeeZipCodeChanged(${recipientIndex}, ${payeeIndex});" cssClass="mandatory" id="eftPayeeZipCodeId[${recipientIndex}][${payeeIndex}]"/>




		    </span>
		    <span id="eftPayeeZipDoubleSpanId[${recipientIndex}][${payeeIndex}]">
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode1" maxlength="5" onchange="return handleEftPayeeZipCode1Changed(${recipientIndex}, ${payeeIndex});" size="5" cssClass="mandatory" id="eftPayeeZipCode1Id[${recipientIndex}][${payeeIndex}]"/>





		      &ndash;
<form:input path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.zipCode2" maxlength="4" onchange="return handleEftPayeeZipCode2Changed(${recipientIndex}, ${payeeIndex});" size="4" id="eftPayeeZipCode2Id[${recipientIndex}][${payeeIndex}]"/>




		    </span>
             </c:when>
    	     <c:otherwise>
	         <c:choose>
		     <c:when test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.zipCode1 
		     		&& not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.zipCode2 
				&& withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.countryCode == 'USA'}" >
		       <c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.zipCode1}}"/>-
		       <c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.zipCode2}"/>
		     </c:when>
		     <c:otherwise>
		       <c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.zipCode}"/>
		     </c:otherwise>
                 </c:choose>
	     </c:otherwise>
	 </c:choose>	     
  </td>
</tr>
<tr class="datacell1" valign="top">
  <td class="subsectionNameColumn">
    <strong>
      <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].payees[${payeeIndex}].addressUi.address.countryCode" singleDisplay="true"/>
      Country
    </strong>
  </td>
  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  <td class="indentedValueColumn">
    	<c:choose>
    	     <c:when test="${payee.payeeAddressEditable}">
 <form:select path="withdrawalRequestUi.withdrawalRequest.recipients[${recipientIndex}].payees[${payeeIndex}].address.countryCode" cssClass="mandatory" onchange="return handleEftPayeeCountryChanged(${recipientIndex}, ${payeeIndex});" id="eftPayeeCountryId[${recipientIndex}][${payeeIndex}]">



		     <form:option value="">- select -</form:option>
		      <form:options items="${countries}" itemValue="value" itemLabel="label"/>
</form:select>
    	     </c:when>
    	     <c:otherwise>
    		${withdrawalForm.withdrawalRequestUi.withdrawalRequest.recipients[recipientIndex].payees[payeeIndex].defaultAddress.countryCode}
    	     </c:otherwise>
	</c:choose>	
  </td>
</tr>
