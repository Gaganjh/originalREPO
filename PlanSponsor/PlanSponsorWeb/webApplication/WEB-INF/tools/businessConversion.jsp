<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext" %>  
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- import area -->
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>
<%@page import="com.manulife.pension.service.security.role.PayrollAdministrator"%>
<%@page import="com.manulife.pension.service.security.role.type.Auditor"%>
<%@page import="com.manulife.pension.service.security.role.type.Broker"%>
<%@page import="com.manulife.pension.service.security.role.type.RIA"%>
<%@page import="com.manulife.pension.ps.web.tools.BusinessConversionForm"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%
	String trustId=Trustee.ID;
    String trustName=new Trustee().getDisplayName();
    String authsignature=AuthorizedSignor.ID;
    String authsignaturename=new AuthorizedSignor().getDisplayName();
    String administrativecontact=AdministrativeContact.stringID;
    String administrativecontactname=new AdministrativeContact().getDisplayName();
    String intermediarycontact=IntermediaryContact.ID;
    String intermediarycontactname=new IntermediaryContact().getDisplayName();
    String payrolladministrator=PayrollAdministrator.ID;
    String payrolladministratorname=new PayrollAdministrator().getDisplayName();
    String auditorid=Auditor.id;
    String auditorName=Auditor.displayName;
    String broker=Broker.id;
    String brokername=Broker.displayName;
    String ria=RIA.id;
    String rianame=RIA.displayName;
    pageContext.setAttribute("trustId",trustId,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("trustName",trustName,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("authsignature",authsignature,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("authsignaturename",authsignaturename,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("administrativecontact",administrativecontact,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("administrativecontactname",administrativecontactname,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("intermediarycontact",intermediarycontact,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("intermediarycontactname",intermediarycontactname,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("payrolladministrator",payrolladministrator,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("payrolladministratorname",payrolladministratorname,PageContext.PAGE_SCOPE);
    
    pageContext.setAttribute("auditorid",auditorid,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("auditorName",auditorName,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("broker",broker,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("brokername",brokername,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("ria",ria,PageContext.PAGE_SCOPE);
    pageContext.setAttribute("rianame",rianame,PageContext.PAGE_SCOPE);
    
    
%>
<!-- content ids -->
<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>"  id="warningDiscardChanges" />
<content:contentBean contentId="<%=ContentConstants.WARNING_ATLEAST_ONE_TRUSTEE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>"  id="warningAtleastOneTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.NO_PLAN_SPONSOR_USERS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="noPlanSponsorUsers" />
<content:contentBean contentId="<%=ContentConstants.ROLE_IS_NOT_SELECTED%>"	type="<%=ContentConstants.TYPE_MESSAGE%>" id="roleIsNotSelected" />
<content:contentBean contentId="57775" type="<%=ContentConstants.TYPE_MESSAGE%>" id="contactTypeError" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_PRIMARY_CONTACT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOnePrimaryContact" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOneMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_TRUSTEE_MAIL_RECIPIENT_SEC_ROLE_CONV%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOneTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.MORE_THAN_ONE_PPT_STATEMENT_CONSULTANT_SEC_ROLE_CONV%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="moreThanOnePptStatementConsultant" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_TRUSTEE_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForTrusteeMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_MAIL_RECIPIENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForMailRecipient" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_SIGN_RECVD_AUTHORIZED_SIGNER%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForSignRecvdAuthorizedSigner" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_PRIMARY_CONTACT_PPY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForPrimaryContactPPY" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_PRIMARY_CONTACT_INC%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForPrimaryContactINC" />
<content:contentBean contentId="<%=ContentConstants.INVALID_ROLE_FOR_SIGN_RECVD_TRUSTEE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRoleForSignRecievedTrustee" />




 <%-- <jsp:useBean id="theForm" scope="request" type="com.manulife.pension.ps.web.tools.BusinessConversionForm" /> --%>
 <%
 BusinessConversionForm theForm = (BusinessConversionForm)session.getAttribute("businessConversionForm");
pageContext.setAttribute("theForm",theForm,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript" >
var contactTypeErrorMsg = '<content:getAttribute beanName="contactTypeError" attribute="text"  filter="true"/>';
var contactTypeError;

/**
 * This method retrieves the current selected value for the given dropdown. The input parameter for this
 * function is a NodeList of dropdowns, of which, we retrieve the first item.
 */
function getSelectedValue(dropDownNodeList) {
	var selectedValue;
	if (dropDownNodeList != null && dropDownNodeList.length > 0)  {
		var dropdownL = dropDownNodeList.item(0);
		selIndex = dropdownL.selectedIndex;
		selectedValue = dropdownL.options[selIndex].value;
		return selectedValue;
	}
}
/*
 * Back function trigger a server side action
 */
function doBack()
{
	document.businessConversionForm.action.value = 'back';
	document.businessConversionForm.submit();
}

/*
 * Cancel function with change tracker
 * Trigger a server side action for unlock profile
 */
function doCancel()
{
	if (discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text"  filter="true"/>"))
	{
		document.businessConversionForm.action.value = 'cancel';
		document.businessConversionForm.submit();

	} else {
		return false;
	}
}
/*
 * Check if fields are changed
 */
function discardChanges(warning)
{
	var formChanged = false;
	formChanged = changeTracker.hasChanged();

	//alert(changeTracker.hasChanged());
	if (formChanged)
	{
    	if (window.confirm(warning))
    	{
      		return true;
    	} else {
	      return false;
    	}
  	}
  return true;
}
/*
 * Search action
 */
function doSearch()
{
	if (checkSearchChange())
	{
		document.businessConversionForm.action.value = 'search';
		document.businessConversionForm.submit();
	} else {
		return false;
	}
}
/*
 * Check fields are changed before search action
 */
function checkSearchChange()
{
	var formChanged = false;
	formChanged = changeTracker.hasChanged();
	if (formChanged)
	{
    	if (confirm('Data is changed. Do you want to continue this process?'))
    	{
    		return true;
    	} else {
    		document.businessConversionForm.contractNumber.value='';
    		return false;
    	}
  	}
  return true;
}
/*
 * Save function
 */
function doSave()
{
	if (validatePage())
	{
		if (confirm('Are you sure?'))
		{
			enableAllFields();
			document.businessConversionForm.action.value = 'save';
			document.businessConversionForm.submit();
		}
	} else {
		return false;
	}

}
/*
 * Change role function. Disable fields when role is INC or PPY
 */
function changeRole(obj){
		var position1 = obj.name.indexOf('[');
		var position2 = obj.name.indexOf(']');
		var index = obj.name.substring(position1+1,position2);
		
		var contactTypeNameStr = "relatedProfiles[" + index +"]." + "contactType";
		//var webAccessStr = "relatedProfiles[" + index +"]." + "webAccess";
		var primaryContactNameStr= "relatedProfiles[" + index +"]." + "primaryContact";
		var mailRecipientNameStr = "relatedProfiles[" + index +"]." + "mailRecipient";
		var trusteeMailRecipientStr = "relatedProfiles[" + index +"]." + "trusteeMailRecipient";
		var pptStatementConsultantStr = "relatedProfiles[" + index +"]." + "pptStatementConsultant";
		var signReceivedTrusteeStr = "relatedProfiles[" + index +"]." + "signReceivedTrustee";
		var signReceivedAuthorizedSignorStr = "relatedProfiles[" + index +"]." + "signReceivedAuthorizedSignor";
	//select one will disable all fields
	if (obj.value=='select one')
	{
		disableDropdown(contactTypeNameStr);
		//disableDropdown(webAccessStr);
		disableDropdown(primaryContactNameStr);
		disableDropdown(mailRecipientNameStr);
		disableDropdown(trusteeMailRecipientStr);
		disableDropdown(pptStatementConsultantStr);
		disableDropdown(signReceivedTrusteeStr);
		disableDropdown(signReceivedAuthorizedSignorStr);
		return;

	}
	//trustee 
	if (obj.value=='TRT')
	{
		disableDropdownIfNotTrue(contactTypeNameStr, 'select one');
		disableDropdownIfNotTrue(signReceivedAuthorizedSignorStr, 'false');
		//enableDropdown(webAccessStr);
		enableDropdown(primaryContactNameStr);
		enableDropdown(mailRecipientNameStr);
		enableDropdown(trusteeMailRecipientStr);
		enableDropdown(pptStatementConsultantStr);
		enableDropdown(signReceivedTrusteeStr);
		return;

	}
	//authorized signor
	if (obj.value=='AUS' )
	{
		disableDropdownIfNotTrue(contactTypeNameStr, 'select one');
		disableDropdownIfNotTrue(trusteeMailRecipientStr, 'false');
		disableDropdownIfNotTrue(signReceivedTrusteeStr, 'false');
		//enableDropdown(webAccessStr);
		enableDropdown(primaryContactNameStr);
		enableDropdown(mailRecipientNameStr);
		enableDropdown(pptStatementConsultantStr);
		enableDropdown(signReceivedAuthorizedSignorStr);
		return;

	}
	//administrative contact
	if (obj.value=='ADC' )
	{
		disableDropdownIfNotTrue(contactTypeNameStr, 'select one');
		disableDropdownIfNotTrue(trusteeMailRecipientStr, 'false');
		disableDropdownIfNotTrue(signReceivedTrusteeStr, 'false');
		disableDropdownIfNotTrue(signReceivedAuthorizedSignorStr, 'false');
		//enableDropdown(webAccessStr);
		enableDropdown(primaryContactNameStr);
		enableDropdown(mailRecipientNameStr);
		enableDropdown(pptStatementConsultantStr);
		return;

	}
	//Payroll Administrator
	if (obj.value=='PPY')
	{
		disableDropdownIfNotTrue(contactTypeNameStr, 'select one');
		disableDropdownIfNotTrue(primaryContactNameStr, 'false');
		disableDropdownIfNotTrue(mailRecipientNameStr, 'false');
		disableDropdownIfNotTrue(trusteeMailRecipientStr, 'false');
		disableDropdownIfNotTrue(signReceivedTrusteeStr, 'false');
		disableDropdownIfNotTrue(signReceivedAuthorizedSignorStr, 'false');
		//enableDropdown(webAccessStr);
		enableDropdown(pptStatementConsultantStr);
		return;

	}

	if (obj.value=='INC' )
	{
		//disable fields
		disableDropdownIfNotTrue(primaryContactNameStr, 'false');
		disableDropdownIfNotTrue(mailRecipientNameStr, 'false');
		disableDropdownIfNotTrue(trusteeMailRecipientStr, 'false');
		disableDropdownIfNotTrue(signReceivedTrusteeStr, 'false');
		disableDropdownIfNotTrue(signReceivedAuthorizedSignorStr, 'false');
		//enableDropdown(webAccessStr);
		enableDropdown(contactTypeNameStr);
		enableDropdown(pptStatementConsultantStr);
		return;
	}
}

/**
 * Disable the dropdown. If the dropdown value in specialAttributesArray is present with value 'true', 
 * then, dont disable the dropdown. Instead, keep it enabled and set it to its default value.
 */
function disableDropdownIfNotTrue(fieldNameStr) {
	if (document.getElementsByName(fieldNameStr)[0].value == 'true') {
		document.getElementsByName(fieldNameStr)[0].disabled = false;
	} else {
		document.getElementsByName(fieldNameStr)[0].disabled = true;
	}
}

/**
 * Disable the dropdown and reset its value to its default value. 
 * If the value is present in the specialAttributesArray with its default value as 'true', then,
 * set its value to true, else, set its value to false.
 */
function disableDropdown(fieldNameStr) {
		document.getElementsByName(fieldNameStr)[0].value = 'false';
		document.getElementsByName(fieldNameStr)[0].disabled = true;
}

/**
 * Enables the given field.
 */
function enableDropdown(fieldNameStr) {
	document.getElementsByName(fieldNameStr)[0].disabled = false;
}

/*
 * Enable all fields before submit
 */
function enableAllFields(theForm)
{
	for(i=0; i<document.businessConversionForm.elements.length; i++)
	{
		if(document.businessConversionForm.elements[i].disabled==true)
		{
			document.businessConversionForm.elements[i].disabled=false;
		}
	}
}
/*
 * Validate page
 */
function validatePage(){
	//clean error div
	var warningMsg = '';
	var warnFlag = false;
	var errorFlag = false;
	var errorMsg = '';
	document.getElementById('errordivcs').innerHTML='';
	//valiate selected role
	if (!validateSelectedRole())
	{
		errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="roleIsNotSelected" attribute="text"/>&nbsp;&nbsp;[1072]</li></ul></td></tr></table>';
		errorFlag = true;
	}

	if (validateSelectedRole()) {
		//valiate contact type role
		if (!validateContactType())
		{
			errorMsg = errorMsg + contactTypeError;
			errorFlag = true;
		}


		//must not more than one primary contact
		if (validatePrimaryContact() > 1)
		{
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="moreThanOnePrimaryContact" attribute="text"/>&nbsp;&nbsp;[1073]</li></ul></td></tr></table>';
			//alert('There is a role is not selected at least!');
			errorFlag = true;
		}



		//must not more than one mail recipient
		if (validateMailRecipient() > 1)
		{
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="moreThanOneMailRecipient" attribute="text"/>&nbsp;&nbsp;[1074]</li></ul></td></tr></table>';
			errorFlag = true;
		}

		//must not more than one trustee mail recipient
		if (validateTrusteeMailRecipient() > 1)
		{
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="moreThanOneTrusteeMailRecipient" attribute="text"/>&nbsp;&nbsp;[1113]</li></ul></td></tr></table>';
			errorFlag = true;
		}

		//must not more than one trustee mail recipient
		if (validateParticipantStatementConsultant() > 1)
		{
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="moreThanOnePptStatementConsultant" attribute="text"/>&nbsp;&nbsp;[1114]</li></ul></td></tr></table>';
			errorFlag = true;
		}

		
		//at least has one primary contact
		if (validatePrimaryContact() < 1)
		{
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li>Contract must have at least one primary contact.</li></ul></td></tr></table>';
			errorFlag = true;
		}

		//at least has one mail recipient
		if (validateMailRecipient() < 1)
		{
			errorMsg = errorMsg +  '<table id="psErrors"><tr><td class="redText"><ul><li>Contract must have at least one mail recipient.</li></ul></td></tr></table>'
			errorFlag = true;
		}

		// atleast one trusee mail recipient
		if (validateTrusteeMailRecipient() < 1)
		{
			warningMsg = warningMsg + '<content:getAttribute beanName="warningAtleastOneTrusteeMailRecipient" attribute="text"/>';
			warnFlag = true;
		}

		// Validate that the correct role is selected for Mail recipient.
		if (validateRoleSelectedForMailRecipient()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForMailRecipient" attribute="text"/>&nbsp;&nbsp;[2871]</li></ul></td></tr></table>';
			errorFlag = true;
		}
		
		// Validate that the correct role is selected for Trustee Mail recipient.
		if (validateRoleSelectedForTrusteeMailRecipient()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForTrusteeMailRecipient" attribute="text"/>&nbsp;&nbsp;[1353]</li></ul></td></tr></table>';
			errorFlag = true;
		}
		
		// Validate that the correct role is selected for Signature Recieved Authorized Signer.
		if (validateRoleSelectedForSignRecvdAuthorizedSigner()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForSignRecvdAuthorizedSigner" attribute="text"/>&nbsp;&nbsp;[1354]</li></ul></td></tr></table>';
			errorFlag = true;
		}
		
		// Validate that the correct role is selected for Primary Contact.
		if (validateRoleSelectedForPrimaryContactPPY()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForPrimaryContactPPY" attribute="text"/>&nbsp;&nbsp;[1356]</li></ul></td></tr></table>';
			errorFlag = true;
		}
		
		// Validate that the correct role is selected for Primary Contact.
		if (validateRoleSelectedForPrimaryContactINC()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForPrimaryContactINC" attribute="text"/>&nbsp;&nbsp;[1356]</li></ul></td></tr></table>';
			errorFlag = true;
		}
		
		// Validate that the correct role is selected for Signature recieved Trustee.
		if (validateRoleSelectedForSignRecievedTrustee()) {
			errorMsg = errorMsg + '<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="invalidRoleForSignRecievedTrustee" attribute="text"/>&nbsp;&nbsp;[1355]</li></ul></td></tr></table>';
			errorFlag = true;
		}
	}

	//show error
	if(errorFlag){
		document.getElementById('errordivcs').innerHTML = errorMsg;
		return false;
	}
	//show warn
	if (warnFlag){
		if (!confirm(warningMsg)){
			return false;
		}
	}


	return true;

}
/**
 * validate selected role field
 * display an error if at least one user has a role is 'select one'
 */
function validateContactType()
{
	//clear error
	contactTypeError = "";
	var flag = true;
	var counts = eval(document.businessConversionForm.itemsSize.value);
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value=='INC' && document.getElementsByName('relatedProfiles[' + i + '].contactType')[0].value=='select one' )
		{
			var username = document.getElementsByName('relatedProfiles[' + i + '].userName')[0].value;
			//alert(username);
			contactTypeError = contactTypeError + '<table id="psErrors"><tr><td class="redText"><ul><li>' + contactTypeErrorMsg.replace("{0}", username) +'&nbsp;&nbsp;[2542]</li></ul></td></tr></table>'
			flag = false;
		}
	}
	return flag;
}

/**
 * validate contact type field
 * display an error if at least one user has a role is 'select one'
 */
function validateSelectedRole()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value=='select one')
		{
			return false;
		}
	}
	return true;
}

/**
 * Validate primary contact
 */
function validatePrimaryContact()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var primaryContactYesCounter = 0;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].primaryContact')[0].value=='true')
		{
			primaryContactYesCounter++
		}
	}
	return primaryContactYesCounter;
}

/**
 * Validate mail receipent
 */
function validateMailRecipient()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var mailRecipientYesCounter = 0;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].mailRecipient')[0].value=='true')
		{
			mailRecipientYesCounter++;
		}
	}
	return mailRecipientYesCounter;
}

/**
 * Validate trustee mail receipent
 */
function validateTrusteeMailRecipient()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var trusteeMailRecipientYesCounter = 0;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].trusteeMailRecipient')[0].value=='true')
		{
			trusteeMailRecipientYesCounter++;
		}
	}
	return trusteeMailRecipientYesCounter;
}

/**
 * Validate participant statement consultant.
 */
function validateParticipantStatementConsultant()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var pptStatementConsultantYesCounter = 0;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].pptStatementConsultant')[0].value=='true')
		{
			pptStatementConsultantYesCounter++;
		}
	}
	return pptStatementConsultantYesCounter;
}

/**
 * Validate that Client mail recipient is selected only for a valid Role.
 */
function validateRoleSelectedForMailRecipient()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].mailRecipient')[0].value == 'true'
			&& (document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value == 'INC'
			||  document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value == 'PPY'))
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

/**
 * Validate that trustee mail recipient is selected only for Trustee Role.
 */
function validateRoleSelectedForTrusteeMailRecipient()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].trusteeMailRecipient')[0].value=='true'
			&& document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value!='TRT')
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

/**
 * Validate that Signature Recieved Authorized Signer is selected only for Authorized Signer Role.
 */
function validateRoleSelectedForSignRecvdAuthorizedSigner()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].signReceivedAuthorizedSignor')[0].value=='true'
			&& document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value!='AUS')
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

/**
 * Validate that Primary Contact = 'yes' is not selected for a Payroll Administrator.
 */
function validateRoleSelectedForPrimaryContactPPY()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].primaryContact')[0].value=='true'
			&& document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value=='PPY')
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

/**
 * Validate that Primary Contact = 'yes' is not selected for a Intermediary Contact.
 */
function validateRoleSelectedForPrimaryContactINC()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].primaryContact')[0].value=='true'
			&& document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value=='INC')
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

/**
 * Validate that Primary Contact = 'yes' is not selected for a Intermediary Contact.
 */
function validateRoleSelectedForSignRecievedTrustee()
{
	var counts = eval(document.businessConversionForm.itemsSize.value);
	var incompatibleRoleSelected = false;
	for (i=0; i < counts; i++)
	{
		if (document.getElementsByName('relatedProfiles[' + i + '].signReceivedTrustee')[0].value=='true'
			&& !(document.getElementsByName('relatedProfiles[' + i + '].role.value')[0].value=='TRT'))
		{
			incompatibleRoleSelected = true;
		}
	}
	return incompatibleRoleSelected;
}

function isFormChanged()
{
	return changeTracker.hasChanged();
}
registerTrackChangesFunction(isFormChanged);

var keyCode;
document.onkeydown =
    function (evt) {
        keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
    }

function keyPressHandler(buttonName) { 
    if (keyCode == 13) {
		document.getElementsByName(buttonName)[0].click();
		return false;
    }
}
</script>
<%
	String actionStr = null;
	BusinessConversionForm formbean = (BusinessConversionForm)session.getAttribute("businessConversionForm");
	actionStr = formbean.getTargetAction();
%>

<ps:form cssClass="margin-bottom:0;" method="post" modelAttribute="businessConversionForm" name="businessConversionForm"	action="<%=actionStr%>">
<input type="hidden" name="action" value="search"/>
<input type="hidden" name="itemsSize"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
		<tr>
			<td valign="top" width="30"><img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
			<td valign="top" width="700">
			<table border="0" cellpadding="0" cellspacing="0" width="700">
				<tbody>
					<tr>
						<td width="350"><img src="/assets/unmanaged/images/s.gif" height="1" width="525"></td>
						<td width="20"><img	src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
						<td width="155"><img src="/assets/unmanaged/images/s.gif" height="1" width="155"></td>
					</tr>
					<tr>
						<td width="525"><img src="/assets/unmanaged/images/s.gif" height="1" width="525">
						<!-- error section -->
							<p><div id="errordivcs"><content:errors scope="session" /></div></p>
						</td>
						<td width="20"><img	src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
						<td width="155"><img src="/assets/unmanaged/images/s.gif" height="1" width="155"></td>
					</tr>
					<tr>
						<td height="47" colspan="3" valign="top">

						<table border="0" cellpadding="0" cellspacing="0" class="box" valign="top">
							<tbody>
								<tr class="tablehead">
								<td colspan="3" class="tableheadTD1"><b>Enter contract number</b></td>
								</tr>
								<tr>
									<td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td class="boxbody" width="212">
<form:input path="contractNumber" maxlength="10" onkeypress="return keyPressHandler('searchButton');" size="10"/>&nbsp;&nbsp;&nbsp;&nbsp;
									<input name="searchButton" class="button100Lg" value="search" type="button" onclick="return doSearch();">
									</td>
									<td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
								<tr>
									<td colspan="3">
									<table border="0" cellpadding="0" cellspacing="0" width="100%">
										<tbody>
											<tr>
												<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											</tr>
										</tbody>
									</table>
									</td>
								</tr>
							</tbody>
						</table>
						<Br>
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="200"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60">&nbsp;</td>
									<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
								<logicext:if name="theForm" property="itemsSize" op="equal" value="0">
								<logicext:then>
									<tr class="tablehead">
									<td class="tableheadTD1" colspan="24">
										&nbsp;
									</td>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1"	width="1"></td>
									</tr>
								</logicext:then>
								<logicext:else>
									<tr class="tablehead">
<td colspan="9" class="tableheadTD1"><b>${theForm.contractName}, ${theForm.contractNumber}</b></td>
<td colspan="5" class="tableheadTD"><B>Users 1-${theForm.itemsSize} of ${theForm.itemsSize}</B></td>
                        			 <td colspan="10" class="tableheadTD"><div align="right">Page <B>1</B> </div></td>
			                         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            			           </tr>
								</logicext:else>
								</logicext:if>
								<tr class="tablesubhead">
									<td class="databorder" height="28"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="200" valign="top"><strong> Name </strong></td>
									<td valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top"><div align="left"><strong>SSN</strong></div></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top"><strong>Profile	status </strong></td>
									<td valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top"><div align="left"><strong>Role</strong></div></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Contact type</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Web access</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Primary	contact</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Client mail recipient </strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Trustee mail recipient</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Participant Statement Consultant</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Signature received - trustee</strong></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top"><strong>Signature received - authorized signer</strong></td>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
								<logicext:if name="theForm" property="noData" op="equal" value="true">
								<logicext:then>
								<tr class="datacell1">
									<td class="databorder" height="28"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td valign="top" colspan="23"><content:getAttribute beanName="noPlanSponsorUsers" attribute="text"/></td>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
								</logicext:then>
								</logicext:if>
								<!-- data begin -->
<c:forEach items="${businessConversionForm.relatedProfiles}" var="relatedProfiles" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();%> %>
								<%
								
									if (Integer.parseInt(temp) % 2 == 0) {
								%>
									<tr class="datacell1">
								<%
									} else {
								%>

									<tr class="datacell2">
								<%
									}
								%>
									<td class="databorder" height="28"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="200" valign="top">
${relatedProfiles.userName}
<input type="hidden" name="userName" /><%--  input - indexed="true" name="relatedProfiles" --%>
									</td>
									<td valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top">
										<div align="left" nowrap="nowrap">
										<c:if test="${!empty relatedProfiles.ssn}">
											<render:ssn property="relatedProfiles.ssn" useMask="false" />
										</c:if>
										</div></td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top">
${relatedProfiles.profileStatus}
<input type="hidden" name="profileStatus" /><%-- input - indexed="true" name="relatedProfiles" --%>
									</td>
									<td valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="75" valign="top">
										<div align="left">
<form:select path="relatedProfiles.role.value" onchange="changeRole(this)" ><%--  indexed="true"  --%>
											
											<form:option value="select one">select one</form:option>
											<form:option value="${trustId}">${trustName}</form:option>
											<form:option value="${authsignature}">${authsignaturename}</form:option>
											<form:option value="${administrativecontact}">${administrativecontactname}</form:option>
											<form:option value="${intermediarycontact}">${intermediarycontactname}</form:option>
											<form:option value="${payrolladministrator}">${payrolladministratorname}</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="role.value" />
										</div>
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
										<div align="left">
<form:select path="relatedProfiles.contactType" disabled="true" ><%--   indexed="true"  --%>
											<form:option value="select one">select one</form:option>
											<form:option value="${auditorid}">${auditorName}</form:option>
											<form:option value="${broker}">${brokername}</form:option>
											<form:option value="${ria}">${rianame}</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="contactType" />
										</div>
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
										<c:if test="${!relatedProfiles.webAccess}">
											no
										</c:if >
										<c:if test="${relatedProfiles.webAccess}">
											Yes
										</c:if>
										
										
										
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="webAccess" />
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.primaryContact" disabled="true" ><%--  indexed="true"  --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="primaryContact" />
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.mailRecipient" disabled="true" ><%--  indexed="true"  --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="mailRecipient" />

									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.trusteeMailRecipient" disabled="true" ><%--  indexed="true" --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="trusteeMailRecipient" />
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.pptStatementConsultant" disabled="true" ><%-- indexed="true"  --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="pptStatementConsultant" />
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.signReceivedTrustee" disabled="true" ><%--  indexed="true"  --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="signReceivedTrustee" />
									</td>
									<td width="1" valign="top" class="dataheaddivider"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td width="60" valign="top">
<form:select path="relatedProfiles.signReceivedAuthorizedSignor" disabled="true" ><%--  indexed="true"  --%>
											<form:option value="false">no</form:option>
											<form:option value="true">yes</form:option>
</form:select>
										<ps:trackChanges name="businessConversionForm" indexPrefix="relatedProfiles" property="signReceivedAuthorizedSignor" />
									</td>
									<td class="databorder"><img	src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
</c:forEach>
								<tr>
									<td align="right" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td colspan="23" align="right" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="right" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
							</tbody>
						</table>
						<Br>
						<Br>
						<logicext:if name="theForm" property="itemsSize" op="equal" value="0">
						<logicext:then>
						<table width="578" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="177"><input name="search" class="button134" value="back" type="button" onclick="return doBack();"></td>
								<td width="431"><div align="right"></div></td>
							</tr>
						</table>
						</logicext:then>
						<logicext:else>
						<table width="850" border="0" cellspacing="0" cellpadding="0">
                     		<tr>
                       			<td width="177"><input name="cancelButton" class="button134" value="cancel" type="button" onclick="return doCancel();"></td>
                       			<td><div align="right">
                           				<input name="saveButton" class="button134" value="save" type="button" onclick="return doSave();">
                       				</div>
                       			</td>
                     		</tr>
                   		</table>
						</logicext:else>
						</logicext:if>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td valign="top" width="30"><br>
			<img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
		</tr>
	</tbody>
</table>
</td>
</tr>
</tbody>
</table>
</ps:form>

