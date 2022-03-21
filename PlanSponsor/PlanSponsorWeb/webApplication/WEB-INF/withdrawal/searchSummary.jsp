<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="report" uri="manulife/tags/report"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@ taglib prefix="render" uri="manulife/tags/render"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<jsp:useBean id="searchWithdrawalRequestForm" scope="request"
	class="com.manulife.pension.ps.web.withdrawal.SearchParticipantRequestWithdrawalForm" />

<un:useConstants var="webConstants"
	className="com.manulife.pension.ps.web.Constants" />
<un:useConstants var="formConstants"
	className="com.manulife.pension.ps.web.withdrawal.SearchParticipantRequestWithdrawalForm" />
<un:useConstants var="withdrawalWebConstants"
	className="com.manulife.pension.ps.web.withdrawal.WebConstants" />
<un:useConstants var="globalConstants"
	className="com.manulife.pension.common.GlobalConstants" />



<c:set var="theReport" scope="page"
	value="${requestScope[webConstants.REPORT_BEAN]}" />
<c:set var="userProfile" scope="page"
	value="${sessionScope[webConstants.USERPROFILE_KEY]}" />

<script type="text/javascript" >
/**
 * Resets the contract number field to their default.
 */
function resetLastName() {
  document.getElementById('lastNameId').value = '';
}

/**
 * Resets the SSN fields to their default.
 */
function resetSsn() {
  document.getElementById('ssnOneId').value = '';
  document.getElementById('ssnTwoId').value = '';
  document.getElementById('ssnThreeId').value = '';
}

/**
 * Resets the last name field to their default.
 */
function resetLastName() {
  document.getElementById('lastNameId').value = '';
}

/**
 * Handler for when a input is detected for the SSN field.
 */
function handleSsnChanged(field, fieldLength, event) {

  // Determine if there is content in the SSN field
  if (field.value.length > 0) {
  
    // Content clear last name
    resetLastName();
    
    // Check for auto tab
    autoTab(field, fieldLength, event);
  }
}

/**
 * Handler for when a input is detected for the last name field.
 */
function handleLastNameChanged(field, event) {
  // Determine if there is content in the last name - clear SSN fields if so
  if (field.value.length > 0) {
    resetSsn();
  }
}

/**
 * Handler for when a key input is detected for the SSN 1 field.
 */
function handleSsn1KeyUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_ONE_LENGTH}, event);
}

/**
 * Handler for when a key input is detected for the SSN 2 field.
 */
function handleSsn2KeyUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_TWO_LENGTH}, event);
}

/**
 * Handler for when a key input is detected for the SSN 3 field.
 */
function handleSsn3KeyUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_THREE_LENGTH}, event);
}

/**
 * Handler for when a key input is detected for the last name field.
 */
function handleLastNameKeyUp(field, event) {
  handleLastNameChanged(field, event);
}

/**
 * Handler for when a mouse input is detected for the SSN 1 field.
 */
function handleSsn1MouseUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_ONE_LENGTH}, event);
}

/**
 * Handler for when a mouse input is detected for the SSN 2 field.
 */
function handleSsn2MouseUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_TWO_LENGTH}, event);
}

/**
 * Handler for when a mouse input is detected for the SSN 3 field.
 */
function handleSsn3MouseUp(field, event) {
  handleSsnChanged(field, ${formConstants.SSN_THREE_LENGTH}, event);
}

/**
 * Handler for when a mouse input is detected for the last name field.
 */
function handleLastNameMouseUp(field, event) {
  handleLastNameChanged(field, event);
}

function pagingSubmit(pageNumber){
	if (document.forms['searchWithdrawalRequestForm']) {

		document.forms['searchWithdrawalRequestForm'].elements['task'].value = "page";
		document.forms['searchWithdrawalRequestForm'].elements['pageNumber'].value = pageNumber;
		if (document.forms['searchWithdrawalRequestForm'].csfType.value == "LOAN") {			
			document.forms['searchWithdrawalRequestForm'].action="/do/loan/searchSummary/";
		}else {
			document.forms['searchWithdrawalRequestForm'].action="/do/withdrawal/searchSummary/";
		}
		if (document.forms['searchWithdrawalRequestForm']) {
			document.forms['searchWithdrawalRequestForm'].submit();
		} else {
			document.forms.searchWithdrawalRequestForm.submit();
		}
	}
}


function doSubmit(button){
	if (document.forms['searchWithdrawalRequestForm']) {
		if (document.forms['searchWithdrawalRequestForm'].csfType.value == "LOAN") {
			document.forms['searchWithdrawalRequestForm'].action="/do/loan/searchSummary/";
		}else {
			document.forms['searchWithdrawalRequestForm'].action="/do/withdrawal/searchSummary/";
		}
		document.forms['searchWithdrawalRequestForm'].elements['task'].value = button;
		if (document.forms['searchWithdrawalRequestForm']) {			
			document.forms['searchWithdrawalRequestForm'].submit();
		} else {						
			document.forms.searchWithdrawalRequestForm.submit();
		}
	}
}
</script>

<%--onSubmit is modified to take 'filter' as default value [It is an impact of the logic implemented to diffentiate  loan and withdrawal search options] --%>
<ps:form modelAttribute ="searchWithdrawalRequestForm" method="POST" action="/do/withdrawal/searchSummary/" name="searchWithdrawalRequestForm"
	onsubmit="doSubmit('filter'); return false;">
<form:hidden path="csfType"/>
<input type="hidden" name="task" value="filter"/>
<form:hidden path="pageNumber"/>

	<%-- Messages section --%>
	<div style="margin-left: 30px; margin-top: 10px; margin-bottom: 10px;">
	<ps:messages scope="session" maxHeight="100px" width="500px" /></div>

	<%-- Determine if TPA and has selected a contract --%>
	<c:set var="isTpaPsw" scope="page" value="false" />
	<ps:isTpa name="${webConstants.USERPROFILE_KEY}" property="role">
		<c:if test="${empty userProfile.currentContract}">
			<c:set var="isTpaPsw" scope="page" value="true" />
		</c:if>
	</ps:isTpa>

	<div style="margin-left: 30px;">
	<table border="0" cellpadding="0" cellspacing="0" width="500px">
		<tr>
			<td>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr class="tablehead">
					<td class="tableheadTD1"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body1Header" /></b></td>
					<td class="tableheadTDinfo"><b> <report:recordCounter						report="theReport" label="Participants" /> </b></td>
					<td class="tableheadTDinfo" align="right"><report:pageCounterViaSubmit report="theReport" formName="searchWithdrawalRequestForm"/></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="2" width="100%">
				<c:if test="${isTpaPsw}">
					<tr>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
						<td><strong> Contract number </strong></td>
<td><form:input path="filterContractId" maxlength="${formConstants.CONTRACT_NUMBER_LENGTH_MAXIMUM}" id="contractNumberId" /></td>



						<td colspan="3" />
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
					</tr>
				</c:if>
				<tr>
					<td class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
					<td><strong> Last name </strong></td>
<td><form:input path="filterParticipantLastName" maxlength="${globalConstants.LAST_NAME_LENGTH_MAXIMUM}" onchange="return handleLastNameChanged(this, event);" onkeyup="return handleLastNameKeyUp(this, event);" id="lastNameId" /></td>





					<td><strong> SSN </strong></td>
					<td>
					<form:password path="ssnOne" value="${searchWithdrawalRequestForm.ssnOne}"
					 maxlength="${formConstants.SSN_ONE_LENGTH}"
						size="${formConstants.SSN_ONE_LENGTH}"
						onkeyup="return handleSsn1KeyUp(this, event);"
						onchange="return handleSsnChanged(this, ${formConstants.SSN_ONE_LENGTH}, event);"
						styleClass="inputField" styleId="ssnOneId" />
					<form:password path="ssnTwo"  value="${searchWithdrawalRequestForm.ssnTwo}"
					maxlength="${formConstants.SSN_TWO_LENGTH}"
						size="${formConstants.SSN_TWO_LENGTH}"
						onkeyup="return handleSsn2KeyUp(this, event);"
						onchange="return handleSsnChanged(this, ${formConstants.SSN_TWO_LENGTH}, event);"
						styleClass="inputField" styleId="ssnTwoId" />
						<form:input path="ssnThree" autocomplete="off" maxlength="${formConstants.SSN_THREE_LENGTH}"
						size="${formConstants.SSN_THREE_LENGTH}"
						onkeyup="return handleSsn3KeyUp(this, event);"
						onchange="return handleSsnChanged(this, ${formConstants.SSN_THREE_LENGTH}, event);"
						styleClass="inputField" styleId="ssnThreeId" /></td>
					<td><input name="action" type="submit" value="search">
					</td>
					<td class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
				<%-- the header --%>
				<tr class="tablesubhead">
					<td class="databorder" width="1"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
					<td valign="top" class="tablesubhead"><b>Name/SSN</b></td>
					<td valign="top" class="dataheaddivider" width="1"><img
						src="/assets/unmanaged/images/spacer.gif" height="1" width="1" /></td>
					<td valign="top" class="tablesubhead"><b>Date of birth</b></td>
					<td valign="top" class="dataheaddivider" width="1"><img
						src="/assets/unmanaged/images/spacer.gif" height="1" width="1" /></td>
					<td valign="top" class="tablesubhead"><b>Date of hire</b></td>
					<td valign="top" class="dataheaddivider" width="1"><img
						src="/assets/unmanaged/images/spacer.gif" height="1" width="1" /></td>
					<td valign="top" class="tablesubhead" />
					<td class="databorder" width="1"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
				</tr>
				<%-- new dynamic content --%>
				<c:choose>
					<c:when test="${fn:length(theReport.details) > 0}">
						<%-- The contract number and contract name --%>
						<ps:isTpa name="${webConstants.USERPROFILE_KEY}" property="role">
							<c:if test="${empty userProfile.currentContract}">
								<tr class="datacell2">
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="7"><span>Contract number:</span> <span
										style="left-pad: 4px;">${searchWithdrawalRequestForm.selectedContractId}</span>
									<span style="left-pad: 10px;">Contract name:</span> <span
										style="left-pad: 4px;">${searchWithdrawalRequestForm.selectedContractName}</span>
									</td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
							</c:if>
						</ps:isTpa>
						<c:forEach var="participant" items="${theReport.details}"
							varStatus="status">
							<tr
								class="${((status.index % 2) == 0 ) ? 'datacell1' : 'datacell2'}"
								valign="top">
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
								<td>
								<div>${participant.participantFullName}</div>
								<div><render:ssn>${participant.ssn}</render:ssn></div>
								</td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
								<td valign="middle"><fmt:formatDate
									value="${participant.birthDate}" pattern="MM/dd/yyyy" /></td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
								<td valign="middle"><fmt:formatDate
									value="${participant.hireDate}" pattern="MM/dd/yyyy" /></td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
								<td>
								<c:if test="${empty searchWithdrawalRequestForm.csfType}">
															
<a href="/do/withdrawal/beforeProceedingGatewayInit/?${withdrawalWebConstants.PROFILE_ID_PARAMETER}=${participant.profileId}&${withdrawalWebConstants.CONTRACT_ID_PARAMETER}=${participant.contractNumber}&${withdrawalWebConstants.ORIGINATOR_PARAMETER}=${withdrawalWebConstants.SEARCH_PARTICIPANT_ORIGINATOR}" title="Create Request">


                       				 Create Withdrawal Request
</a>
								</c:if> 
								<c:if test="${not empty searchWithdrawalRequestForm.csfType}">
<a href="/do/onlineloans/initiate/?participantProfileId=${participant.profileId}&${withdrawalWebConstants.ORIGINATOR_PARAMETER}=${withdrawalWebConstants.SEARCH_PARTICIPANT_ORIGINATOR}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${participant.contractNumber}">Create Loan Request

</a>
								</c:if>
								</td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="datacell1">
							<td class="databorder" valign="top"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
							<td valign="top" colspan="7"><b> 
							 <c:if test="${searchWithdrawalRequestForm.showOldILoansLink  }">
						Contract has not converted to on-line loans
						<a href="/do/iloans/viewLoanRequests/"> &lt;Click here to get
								to the i:loans request page&gt; </a>
							</c:if>
						 <c:if test="${not searchWithdrawalRequestForm.showOldILoansLink  }">
						
								<c:choose>
									<c:when
										test="${searchWithdrawalRequestForm.showNoSearchDataMessage}">
                          Please enter criteria above and push Search.
                        </c:when>
									<c:otherwise>
                          There are no records for this contract for the search criteria specified.
                        </c:otherwise>
								</c:choose>
							</c:if> </b></td>
							<td class="databorder" valign="top"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
				<%-- end new dynamic content --%>
				<tr>
					<td class="databorder" colspan="9"><img
						src="/assets/unmanaged/images/s.gif" height="1"></td>
				</tr>
				<%-- end table border --%>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="9" align="right"><report:pageCounterViaSubmit 
				report="theReport" arrowColor="black" name="parameterMap" formName="searchWithdrawalRequestForm" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>

		<tr>
			<td align="right">
<div style="padding-bottom: 5px; padding-top: 5px;"><input type="submit" class="button100Lg" onclick="doSubmit('cancel'); return false;" value="cancel"/></div>


			</td>
		</tr>
		<tr>
			<td><content:getAttribute beanName="layoutPageBean"
				attribute="footer1" /></td>
		</tr>
		<c:forEach items="${layoutPageBean.footnotes}" var="footnote"
			varStatus="footnoteStatus">
			<tr>
				<td><content:getAttribute beanName="footnote" attribute="text" /></td>
			</tr>
		</c:forEach>
		<c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer"
			varStatus="disclaimerStatus">
			<tr>
				<td><content:getAttribute beanName="disclaimer"
					attribute="text" /></td>
			</tr>
		</c:forEach>
	</table>
	</div>
</ps:form>
