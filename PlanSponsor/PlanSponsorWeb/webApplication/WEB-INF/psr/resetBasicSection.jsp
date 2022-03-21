<%@ taglib uri="manulife/tags/ps" prefix="ps"%>
<%@ taglib uri="manulife/tags/render" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<c:set var="userProfile" value="${sessionScope.USER_KEY}" scope="request" />
<c:set var="contractStatus" value="${userProfile.currentContract.status}" />
		


<table>
	<tr>
		<td colspan="2">
			<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
			<ps:messages scope="session"
				maxHeight="${param.printFriendly ? '1000px' : '100px'}"
				suppressDuplicateMessages="true" showOnlyWarningContent="true" /></div>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>

<table width="505" border="0" cellpadding="0" cellspacing="0" class="box">
	<tr class="tablehead">
		<td class="tableheadTD1" colspan="3"><b>Participant summary</b></td>
	</tr>
	<tr>
		<td class="boxborder">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		</td>
		<td class="boxbody">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="40%">First name</td>
				<td width="10%">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>
				<td width="50%">
${passwordResetForm.firstName}
				</td>
			</tr>
			<tr>
				<td width="40%">Last name</td>
				<td width="10%">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>
				<td width="50%">
${passwordResetForm.lastName}
				</td>
			</tr>
			<tr>
				<td width="40%">SSN</td>
				<td width="10%">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>
				<td width="50%">
					<render:ssn property="passwordResetForm.ssn" defaultValue="" />
				</td>
			</tr>
			<tr>
				<td width="40%">Date of birth</td>
				<td width="10%" align="right">
					<ps:fieldHilight name="birthDate" />
				</td>
				<td width="50%">
					<render:date patternOut="MM/dd/yyyy" property="passwordResetForm.birthDate" />
				</td>
			</tr>
			<tr>
				<td width="40%">Employer provided email address</td>
				<td width="10%" align="right">
					<ps:fieldHilight name="employerProvidedEmailAddress" singleDisplay="true" className="errorIcon" displayToolTip="true" />
				</td>
				<td width="">
<c:if test="${not empty passwordResetForm.employerProvidedEmailAddress}">
<input type ="text" value="${passwordResetForm.employerProvidedEmailAddress}" readonly="readonly" />
</c:if>
				</td>
			</tr>
			<tr>
				<td width="40%">Web registered</td>
				<td width="10%">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>
				<td width="50%">
					<c:out value="${passwordResetForm.pptWebRegisStatus}"></c:out>
				</td>
			</tr>
			</table>
		</td>
		<td class="boxborder">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		</td>
	</tr>
	<tr>
		<td class="boxborder" colspan="3">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		</td>
	</tr>
</table>
<BR/>
