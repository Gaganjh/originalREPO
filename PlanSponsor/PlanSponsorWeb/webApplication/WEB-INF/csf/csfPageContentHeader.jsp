<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>


<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges"/>
<content:contentBean contentId="<%=ContentConstants.ACI_SAVE_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="aciAreYouSure"/>
<content:contentBean contentId="<%=ContentConstants.ONLINE_LOANS_WARNING_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="onlineLoansYes"/>

<script language="javascript">

var isPageSubmitted = false;

function doGoPage(theForm, pageUrl) {
	res = discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>");
	if (res == true) {
		this.location.href = pageUrl;
	}
}

function isFormChanged() {
	return changeTracker.hasChanged();
}

function doCancel() {
	document.csfForm.action.value = 'Cancel';
	if (confirm("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>")){
		document.csfForm.submit();
	}
	return false;
}
function doContinueEdit() {
	document.csfForm.action.value = 'continueEdit';
	document.csfForm.submit();
}
function doAccept() {
	document.csfForm.action.value = 'accept';
	submitForm();
}

function doSave() {
	var save = true;
	if (isFormChanged()) {
		if (save && (elementHasChanged("deferralType") || 
				elementHasChanged("deferralLimitPercent")||
				elementHasChanged("deferralLimitDollars")||
				elementHasChanged("deferralMaxLimitPercent")||
				elementHasChanged("deferralMaxLimitDollars")||
				elementHasChanged("payrollCutoff")||
				elementHasChanged("autoContributionIncrease")||
				elementHasChanged("aciAnniversaryYear")||
				elementHasChanged("increaseAnniversary"))){
				if (!confirm("<content:getAttribute beanName="aciAreYouSure" attribute="text" filter="true"/>")){
					save = false;
				}
		}
		if ( save ) {
			document.csfForm.action.value = 'save';
			submitForm();
		}
	}else {
		document.csfForm.action.value = 'save';
		submitForm();
	}
}

function submitForm() {
	if (isPageSubmitted) {
		window.status = "Transaction already in progress.  Please wait.";
	} else {
		isPageSubmitted = true;
		document.csfForm.submit();
	}
}

function elementHasChanged(name) {
	var hasChanged = false;
	var elements = document.getElementsByName(name);
	if (elements != null && elements.length > 0) {
		var oldValue = changeTracker.getOldValue(name);
		
		var newValue = elements[0].value;
		if (elements[0].type == "radio" && !elements[0].checked && !elements[0].disabled) {
			newValue = "";
			for(i=1; i < elements.length; i++) {
				var elem = elements[i];
				if(elem != null && elem.checked && !elem.disabled)
					newValue = elem.value;
			}
		}			
		if (elements[0].type == "checkbox") {
		    // types differ
		    if (oldValue == "false" && elements[0].checked == true) hasChanged = true;
		    if (oldValue == "true" && elements[0].checked == false) hasChanged = true;
		} 
		if (newValue != oldValue && !(elements[0].type == "checkbox") && !elements[0].disabled) {
			if(!(oldValue == null && newValue == ""))
				hasChanged = true;
		}
	}
	return hasChanged;
}
</script>
