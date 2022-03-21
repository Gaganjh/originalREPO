<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="manulife/tags/content" prefix="content"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.ps.web.Constants.FirstClientContactFeatureValue"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>


<c:set var="statesList" value="${editContractContactAddressForm.states}" scope="page"/>

<content:contentBean
	contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges" />
<content:contentBean
	contentId="<%=ContentConstants.APPLY_TO_ALL_ADDRESS%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="applyToAllAddress" />
<content:contentBean
	contentId="<%=ContentConstants.PASSIVE_TRUSTEE_TEXT%>"
	type="<%=ContentConstants.TYPE_FEEDISCLOSURE%>"
	id="passiveTrusteeWording" />

<script  type="text/javascript">
	var submitted = false;
	
	function doSubmit(actionType) {
		
		if (!submitted) {
			if(actionType == 'save') {
				if(!validateLegalAddress()) {
					return false;
				}
			}
			submitted = true;
			
			document.forms['editContractContactAddressForm'].action.value = actionType; 
			document.forms['editContractContactAddressForm'].submit();
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}

	function doCancelChanges(theForm) {
		if (!submitted) {
			if(doCancel(theForm)) {
				doSubmit('cancel');
			}
			else {
				return false;
			}
		 } else {
			 window.status = "Transaction already in progress.  Please wait.";
			 return false;
		 }
	}

		
	function doCancel(theForm) {
		return discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>");
	}

	function isFormChanged() {
		var form = document.forms["editContractContactAddressForm"];
		return changed(form);
	}

	function changed(theForm){
		var indexes = new Array(4);
		var isChanged = false;		 
		<c:forEach items="${editContractContactAddressForm.contactAddresses}" var="contactAddress" varStatus="addressIndex" >
		if("${e:forJavaScriptBlock(contactAddress.addressTypeCode)}" == 'L') {
			indexes[0] = ${addressIndex.index};
		}
		else if("${e:forJavaScriptBlock(contactAddress.addressTypeCode)}" == 'M') {
			indexes[1] = ${addressIndex.index};
		}
		else if("${e:forJavaScriptBlock(contactAddress.addressTypeCode)}" == 'C') {
			indexes[2] = ${addressIndex.index};
		}
		else if("${e:forJavaScriptBlock(contactAddress.addressTypeCode)}" == 'T') {
			indexes[3] = ${addressIndex.index};
		}
		</c:forEach>

		for(var i in indexes){
			var addressLine1 = document.getElementById("addressLine1" + indexes[i]);
			var addressLine2 = document.getElementById("addressLine2" + indexes[i]);
			var city = document.getElementById("city" + indexes[i]);
			var stateCode = document.getElementById("contactAddresses["+i+"].stateCode").value;
			var zipCode1 = document.getElementById("zipCode1" + indexes[i]);
			var zipCode2 = document.getElementById("zipCode2" + indexes[i]);
			
			if(addressLine1.defaultValue != addressLine1.value 
				|| addressLine2.defaultValue != addressLine2.value
				|| city.defaultValue != city.value
				|| stateCode.defaultValue != stateCode.value
				|| zipCode1.defaultValue != zipCode1.value
				|| zipCode2.defaultValue != zipCode2.value
				|| '${editContractContactAddressForm.applyToAllAddresses}' != String(document.editContractContactAddressForm.applyToAllAddresses.checked)) {
					isChanged = true;
					break;
			}		
		}
		return isChanged;
	}
		
	
	function validateLegalAddress() {
		var index;
		var warningString = "";
		<c:forEach items="${editContractContactAddressForm.contactAddresses}" var="contactAddress" varStatus="addressIndex" >
			if("${e:forJavaScriptBlock(contactAddress.addressTypeCode)}" == 'L') {
				 index = ${addressIndex.index};
			}
		</c:forEach>
		
		var addressLine1Legal = String(trim(document.getElementById("addressLine1" + index).value));
		var addressLine2Legal = String(trim(document.getElementById("addressLine2" + index).value));
		var cityLegal = String(trim(document.getElementById("city" + index).value));
		var stateCodeLegal = String(trim(document.getElementById("contactAddresses["+index+"].stateCode" ).value));
		var zipCode1Legal = String(trim(document.getElementById("zipCode1" + index).value));
		
		if(addressLine1Legal.length <= 0 && addressLine2Legal.length <= 0 && cityLegal.length <= 0 &&				
			stateCodeLegal.length <=0  && zipCode1Legal.length <= 0) {
			warningString += "Warning! Legal address is missing - Please complete and save.\n";
		}

		//adding the warning for apply to all address check box
		if(document.editContractContactAddressForm.applyToAllAddresses.checked) {
			var warMsg = '<content:getAttribute beanName="applyToAllAddress" attribute="text"/>';
			warningString += "Warning! " + warMsg + "\n";
		}
		if(warningString != ""){
			return window.confirm(warningString); 
		}
		else {			
			return true;
		}	
	}
	
	function trim(str) {
		return str.replace(/^\s*/, "").replace(/\s*$/, "");
	}
	// Register the event.
	if (window.addEventListener) {	
		window.addEventListener('load', protectLinksFromCancel, false);
	} else if (window.attachEvent) {	
		window.attachEvent('onload', protectLinksFromCancel);
	}
	registerTrackChangesFunction(isFormChanged, "<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>");
	
    function onFirstClientContactChange() {
		var disabled = false;
var firstClientContactOtherElementValue = "${editContractContactAddressForm.firstPointOfContact.firstClientContactOther}";
var firstClientContactOtherTypeElementValue = "${editContractContactAddressForm.firstPointOfContact.firstClientContactOtherType}";
		
		var firstClientContactElement = document.getElementsByName("firstPointOfContact.firstClientContact")[0];
		var firstClientContactOtherElement = document.getElementsByName("firstPointOfContact.firstClientContactOther")[0];
		var firstClientContactOtherTypeElement = document.getElementsByName("firstPointOfContact.firstClientContactOtherType")[0];
		
		if ( firstClientContactOtherElement != null && firstClientContactOtherElement.value == '' ) {
			firstClientContactOtherElementValue = "";
		}
		 
		if ( firstClientContactOtherTypeElement != null && firstClientContactOtherTypeElement.value == '' ) {
			firstClientContactOtherTypeElementValue = "";
		}
		
		if (firstClientContactElement != null && firstClientContactElement.value != "<%=FirstClientContactFeatureValue.CLIENT_AND_OTHER
							.getValue()%>") {
			disabled = true;
			firstClientContactOtherElementValue = "";
			firstClientContactOtherTypeElementValue = "";
		}
		firstClientContactOtherElement.value = firstClientContactOtherElementValue;
		firstClientContactOtherTypeElement.value = firstClientContactOtherTypeElementValue;	
		firstClientContactOtherElement.disabled = disabled;
		firstClientContactOtherTypeElement.disabled = disabled;
    }
    
	function initPage() {
		onFirstClientContactChange();
	}

	if (window.addEventListener) {
		window.addEventListener('load', initPage, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', initPage);
	}    
</script>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
%>
<jsp:useBean class="com.manulife.pension.ps.web.contacts.EditContractContactAddressForm" id="editContractContactAddressForm" scope="session"/>
<ps:form  method="POST"
	action="/do/editContractAddress/" name="editContractContactAddressForm"  modelAttribute="editContractContactAddressForm">

<input type="hidden" name="action" /><%--  input - name="editContractContactAddressForm" --%>

<input type="hidden" name="legalAddressLine1" /><%--  input - name="editContractContactAddressForm" --%>


	<DIV id="errordivcs"><content:errors scope="request" /></DIV>
	<br>

	<%-- 		OB3 T3 PSW_contact changes start --%>
	<%
		int theIndex = 0;
	%>
<c:if test="${userProfile.internalUser ==true}">
		<table border="0" cellpadding="0" cellspacing="0" width="720">
			<tbody>
				<tr>
					<td height="25" colspan="3" class="tablesubhead"><b>First
					point of contact</b></td>
				</tr>
				<%
					if (theIndex++ % 2 == 0) {
				%>
				<tr class="datacell2">
					<%
						} else {
					%>
				
				<tr class="datacell1">
					<%
						}
					%>
					<td width="402"><span id="label_firstClientContact">Which
					party is the first point of contact?</span></td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
<td width="265"><form:select path="firstPointOfContact.firstClientContact" onchange="onFirstClientContactChange()" >



						<c:forEach
							items="${editContractContactAddressForm.firstPointOfContact.firstClientContactValues}"
							var="fccEntry">
							<form:option value="${fccEntry.value}">${fccEntry.displayValue}</form:option>
						</c:forEach>
</form:select></td>
				</tr>
				<%
					if (theIndex++ % 2 == 0) {
				%>
				<tr class="datacell2">
					<%
						} else {
					%>
				
				<tr class="datacell1">
					<%
						}
					%>
					<td width="402"><span id="label_firstClientContactOther">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What
					other party will be contacted for selected issues?</span></td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
<td width="265"><form:select path="firstPointOfContact.firstClientContactOther" disabled="true" >


						<c:forEach
							items="${editContractContactAddressForm.firstPointOfContact.firstClientContactOtherAttributeValues}"
							var="fccoEntry">
							<form:option value="${fccoEntry.value}">${fccoEntry.displayValue}</form:option>
						</c:forEach>
</form:select></td>
				</tr>
				<%
					if (theIndex++ % 2 == 0) {
				%>
				<tr class="datacell2">
					<%
						} else {
					%>
				
				<tr class="datacell1">
					<%
						}
					%>

					<td width="402"><span id="label_firstClientContactOtherType">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What
					selected issues will the other party be contacted for?</span></td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
<td width="265"><form:select path="firstPointOfContact.firstClientContactOtherType" disabled="true" >


						<c:forEach
							items="${editContractContactAddressForm.firstPointOfContact.firstClientContactOtherTypeAttributeValues}"
							var="fccotEntry">
							<form:option value="${fccotEntry.value}">${fccotEntry.displayValue}</form:option>
						</c:forEach>
</form:select></td>
				</tr>
			</tbody>
		</table>
		<br />
		<br />



		<table border="0" cellpadding="0" cellspacing="0" width="720">
			<tbody>
				<tr>
					<td height="25" colspan="3" class="tablesubhead"><b><content:getAttribute
						beanName="passiveTrusteeWording" attribute="text" /></b></td>
				</tr>
				<tr class="datacell1">
					<td width="402"><span id="label_firstClientContact">Matrix Trust Company Passive Trustee Service</span></td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
<td width="265"><c:if test="${userProfile.internalServicesCAR ==true}">

<form:select path="passiveTrustee" >

							<c:forEach
								items="${editContractContactAddressForm.passiveTrusteeOptions}"
								var="trusteeOptions">
								<form:option value="${trusteeOptions.key}">${trusteeOptions.value}</form:option>
							</c:forEach>
</form:select>
</c:if> <c:if test="${userProfile.internalServicesCAR !=true}">

<form:select path="passiveTrustee" disabled="true" >

							<c:forEach
								items="${editContractContactAddressForm.passiveTrusteeOptions}"
								var="trusteeOptions">
								<form:option value="${trusteeOptions.key}">${trusteeOptions.value}</form:option>
							</c:forEach>
</form:select>
</c:if></td>
				</tr>
			</tbody>
		</table>
		<br />
		<br />

</c:if>


	<%-- 		OB3 T3 PSW_contact changes End --%>
	<table border="0" cellpadding="0" cellspacing="0" width="720">
		<tbody>
			<tr>
				<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td height="25" colspan="9" class="tablesubhead"><b> <content:getAttribute
								beanName="layoutPageBean" attribute="body1Header" /> </b></td>
						</tr>
						<tr>
							<td width="70" class="pgNumBack">&nbsp;</td>
							<td width="1" class="greyborder"><img
								src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
							</td>
							<td width="135" class="pgNumBack"><b>Legal </b></td>
							<td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif"
								border="0" height="1" width="1"></td>
							<td width="175" class="pgNumBack"><b>Mailing </b></td>
							<td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif"
								border="0" height="1" width="1"></td>
							<td width="175" class="pgNumBack"><b>Courier </b></td>
							<td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif"
								border="0" height="1" width="1"></td>
							<td width="175" class="pgNumBack"><B>Trustee </B></td>
						</tr>
						<tr>
							<td colspan="9">
							<table class="addressTable">
								<tbody>
									<c:set var="maxfieldsLengthForAddr" value="6" />
									<c:set var="startIndexForAddr" value="0" />

									<c:set var="disableAddressEdits"  value ="false"  />

									<c:if test = "${userProfile.internalUser == 'true' && userProfile.internalServicesCAR == 'false'}">
										<c:set var="disableAddressEdits"  value ="true"  />
									</c:if>

									<tr class="datacell1">
										<td class="addressLabelColumn">Line 1</td>
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											<form:hidden path="contactAddresses[${addressStatus.index}].addressTypeCode"/>
										</c:forEach>
										<c:set var="startIndexForAddr"
											value="${startIndexForAddr + 1}" />
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											<td
												style="border-right-width: ${addressStatus.last ? '0' : '1px'}">
<form:input value="${contactAddress.addressLine1}" path="contactAddresses[${addressStatus.index}].addressLine1" disabled="${disableAddressEdits}" maxlength="30" id="addressLine1${addressStatus.index}" tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" /><%--  form:input - indexed="true" name="contactAddress" --%>




											</td>
										</c:forEach>
									</tr>
									<tr class="datacell2">
										<td class="addressLabelColumn">Line 2</td>
										<c:set var="startIndexForAddr"
											value="${startIndexForAddr + 1}" />
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											<td
												style="border-right-width: ${addressStatus.last ? '0' : '1px'}">
<form:input value="${contactAddress.addressLine2}" path="contactAddresses[${addressStatus.index}].addressLine2" disabled="${disableAddressEdits}" maxlength="30" id="addressLine2${addressStatus.index}" tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" /><%--  form:input - indexed="true" name="contactAddress" --%>




											</td>
										</c:forEach>
									</tr>

									<tr class="datacell1">
										<td class="addressLabelColumn">City</td>
										<c:set var="startIndexForAddr"
											value="${startIndexForAddr + 1}" />
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											<td
												style="border-right-width: ${addressStatus.last ? '0' : '1px'}">
<form:input value="${contactAddress.city}" path="contactAddresses[${addressStatus.index}].city" disabled="${disableAddressEdits}" maxlength="30" id="city${addressStatus.index}" tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" /><%-- form:input - indexed="true" name="contactAddress" --%>




											</td>
										</c:forEach>

									</tr>

									<tr class="datacell2">
										<td class="addressLabelColumn">State</td>
										<c:set var="startIndexForAddr"
											value="${startIndexForAddr + 1}" />
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											 
											<td
												style="border-right-width: ${addressStatus.last ? '0' : '1px'}">
										
							<form:select path="contactAddresses[${addressStatus.index}].stateCode" disabled="${disableAddressEdits}" id="contactAddresses[${addressStatus.index}].stateCode" itemValue="${contactAddress.stateCode}"  tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" indexed="true" styleId="stateCode${addressStatus.index}" >

								<form:option value=""></form:option>
								<form:options items="${statesList}" itemLabel="label" itemValue="value"/>
											
							</form:select></td>
										</c:forEach>
									</tr>


									<tr class="datacell1">
										<td class="addressLabelColumn">Zip code</td>
										<c:set var="startIndexForAddr"
											value="${startIndexForAddr + 1}" />
										<c:forEach
											items="${editContractContactAddressForm.contactAddresses}"
											var="contactAddress" varStatus="addressStatus">
											<td
												style="border-right-width: ${addressStatus.last ? '0' : '1px'}">
<form:input value="${contactAddress.zipCode1}" path="contactAddresses[${addressStatus.index}].zipCode1" disabled="${disableAddressEdits}" maxlength="5" size="5" id="zipCode1${addressStatus.index}" tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" /><%--  form:input - indexed="true" name="contactAddress" --%>




<form:input value="${contactAddress.zipCode2}" path="contactAddresses[${addressStatus.index}].zipCode2" disabled="${disableAddressEdits}" maxlength="4" size="4" id="zipCode2${addressStatus.index}" tabindex="${(addressStatus.index * maxfieldsLengthForAddr) + startIndexForAddr}" /><%--  form:input - indexed="true" name="contactAddress" --%>




											</td>
										</c:forEach>
									</tr>

									<tr class="datacell2">
										<td class="addressLabelColumn">Apply to all <br />
										addresses <br />
										</td>
										<td class="addressLabelColumn"><c:set
											var="startIndexForAddr" value="${startIndexForAddr + 1}" />
<form:checkbox value="${contactAddress.applyToAllAddresses}" path="applyToAllAddresses" disabled="${disableAddressEdits}" tabindex="${((fn:length(editContractContactAddressForm.contactAddresses) - 1) * maxfieldsLengthForAddr)+ startIndexForAddr}" />



										</td>
										<td colspan="7"></td>
									</tr>
								</tbody>
							</table>
							</td>
						</tr>
					</tbody>
				</table>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<table border="0" cellpadding="0" cellspacing="0" width="92%">
		<tbody>
			<tr>
				<td align="right" width="80%"><c:set var="startIndexForAddr"
					value="${startIndexForAddr + 1}" /> 
					<input type="button"
					class="button100Lg" onclick="return doCancelChanges('editContractContactAddressForm');"
					value="cancel" tabindex="${((fn:length(editContractContactAddressForm.contactAddresses) - 1) * maxfieldsLengthForAddr)+ startIndexForAddr}"
					 />
					 
				</td>
				<td align="right" width="20%"><c:set var="startIndexForAddr"
					value="${startIndexForAddr + 1}" /> <input type="button"
					class="button100Lg" onclick="return doSubmit('save')" value="save"
					tabindex="${((fn:length(editContractContactAddressForm.contactAddresses) - 1) * maxfieldsLengthForAddr)+ startIndexForAddr}" />
				</td>
			</tr>
		</tbody>
	</table>

</ps:form>
