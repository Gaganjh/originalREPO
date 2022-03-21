<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_SSN_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="ssnText" />

<%-- 
   This tag is used to display SSN and Tax Id
--%>

<script type="text/javascript">
<%-- 
This method is used to automatically tab from one field to 
another once the required length of characters entered.
--%> 
function autoTab(elem, e) {
	if(e.keyCode) {
		code = e.keyCode;
	}
	else if(e.which) {
		code = e.which;
	}
	if(code != 9 && code != 16) {
		if(elem.id == "ssn1" && elem.value.length == 3) {
			document.getElementById("ssn2").focus();
		}
		else if(elem.id == "ssn2" && elem.value.length == 2) {
			document.getElementById("ssn3").focus();
		}
		else if(elem.id == "taxId1" && elem.value.length == 2) {
			document.getElementById("taxId2").focus();
		}
	}
}

String.prototype.startsWith = function(str)
	{return (this.match("^"+str)==str)};
	
<%-- 
Either SSN or Tax Id should be entered. This method clears the other fields
when SSN or Tax Id is entered.
--%>
function clearFields(elem) {
	if(elem.id.startsWith("ssn") && elem.value.length > 0) {
		document.getElementById("taxId1").value = "";
		document.getElementById("taxId2").value = "";
	}
	else if(elem.id.startsWith("taxId") && elem.value.length > 0) {
		document.getElementById("ssn1").value = "";
		document.getElementById("ssn2").value = "";
		document.getElementById("ssn3").value = "";
	}
}
</script>

<div class="label">* SSN:</div>
<div class="inputText">
	<label><form:password id="ssn1" cssClass="input" path="ssn.ssn1" size="3" maxlength="3" onkeyup="clearFields(this)" /></label>
	<label><form:password id="ssn2" cssClass="input" path="ssn.ssn2" size="2" maxlength="2" onkeyup="clearFields(this)" /></label>
	<label><form:password id="ssn3" cssClass="input" path="ssn.ssn3" size="4" maxlength="4" onkeyup="clearFields(this)"/></label>
	<br/>
		<content:getAttribute attribute="text" beanName="ssnText"/>
	<br/>
</div>

<div class="label">* TAX ID:</div>
<div class="inputText">
	<label><form:input id="taxId1" cssClass="input" path="taxId.taxId1" size="2" maxlength="2" onkeyup="autoTab(this, event);clearFields(this)"/></label>
	<label><form:input id="taxId2" cssClass="input" path="taxId.taxId2" size="7" maxlength="7" onkeyup="clearFields(this)"/></label>
</div>
