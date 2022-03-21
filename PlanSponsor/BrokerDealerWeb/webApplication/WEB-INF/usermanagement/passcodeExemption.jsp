<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page
	import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<script language=javascript type='text/javascript'>
function limitTextArea(field, maxSize) {
	if (field.value.length > maxSize) {
		field.value = field.value.substring(0, maxSize);
		return false;
	} else {
	    return true;
	}
}

</script>
<div id="contentFull">

	<h1>
		<content:getAttribute attribute="name" beanName="layoutPageBean" />
	</h1>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
			<content:getAttribute attribute="subHeader" beanName="layoutPageBean" />
		</div>
	</div>

	<c:if test="${not empty layoutBean.layoutPageBean.introduction1}">
		<p>
			<content:getAttribute attribute="introduction1"
				beanName="layoutPageBean" />
		</p>
	</c:if>
	<c:if test="${not empty layoutBean.layoutPageBean.introduction2}">
		<p>
			<content:getAttribute attribute="introduction2"
				beanName="layoutPageBean" />
		</p>
	</c:if>


	<report:formatMessages scope="request" />
	<bd:form action="/do/usermanagement/passcodeExemption" modelAttribute="passcodeExemptionForm" name="passcodeExemptionForm">
		

		<div>&nbsp;</div>
		<div class="label">First Name:</div>
		<div class="inputText">
			${e:forHtmlContent(requestScope.passcodeExemptionForm.firstName)} &nbsp;</div>
		<div class="label">Last Name:</div>
		<div class="inputText">
			${e:forHtmlContent(requestScope.passcodeExemptionForm.lastName)} &nbsp;</div>
		<div class="label">Email Address:</div>
		<div class="inputText">
			${e:forHtmlContent(requestScope.passcodeExemptionForm.email)} &nbsp;</div>

		<div class="label">User Role:</div>
		<div class="inputText">${e:forHtmlContent(requestScope.passcodeExemptionForm.userRole)} </div>

		<div class="label">* Exemption Reason:</div>
		<div class="inputText">
			<form:textarea path="exemptionReason"
				onkeyup="return limitTextArea(this, 140)"
				onkeydown="return limitTextArea(this, 140)"
				onblur="return limitTextArea(this, 140)" rows="3" cols="32"
				disabled="${e:forHtmlContent(requestScope.passcodeExemptionForm.exemptionReason)}" cssStyle="margin: 0px; width: 237px; height: 59px;" />
			<br />

		</div>

		<div class="label">* Exemption Requested By:</div>
		<div class="inputText">
			<form:input path="exemptRequestedName" size="30" maxlength="30"
				disabled="${e:forHtmlContent(requestScope.passcodeExemptionForm.exemptRequestedName)}" />
			<br />

		</div>
		<div class="label">* PPM Ticket Number:</div>
		<div class="inputText">
			<form:input path="ppmTicket" size="30" maxlength="20"
				disabled="${e:forHtmlContent(requestScope.passcodeExemptionForm.ppmTicket)}" />
			<br />

		</div>

		<form:hidden path="userName" />
		<form:hidden path="firstName" />
		<form:hidden path="lastName" />
		<form:hidden path="email" />
		<form:hidden path="userRole" />

		<br class="clearFloat" />
		<div id="content">

			<div class="formButton">
				<input type="button" class="blue-btn next"
					onmouseover="this.className +=' btn-hover'"
					onmouseout="this.className='blue-btn next'" name="save"
					value="Exempt"
					onclick="return doProtectedSubmitBtn(document.passcodeExemptionForm, 'ExemptPasscode', this)">
			</div>

			<div class="formButton">
				<input type="button" class="grey-btn back"
					onmouseover="this.className +=' btn-hover'"
					onmouseout="this.className='grey-btn back'" name="cancel"
					value="Back"
					onclick="return doProtectedSubmitBtn(document.passcodeExemptionForm, 'cancel', this)">
			</div>
			<input type="hidden" name="action">
		</div>
	</bd:form>
</div>

<layout:pageFooter />