<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="webConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants"/>
<un:useConstants var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache"/>
<un:useConstants var="reportConstants" className="com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalReportData"/>
<un:useConstants var="formConstants" className="com.manulife.pension.ps.web.withdrawal.LoanAndWithdrawalRequestsForm"/>
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest"/>
<un:useConstants var="withdrawalWebConstants" className="com.manulife.pension.ps.web.withdrawal.WebConstants"/>
<un:useConstants var="globalConstants" className="com.manulife.pension.common.GlobalConstants"/>

<c:set var="theReport" scope="page" value="${requestScope[webConstants.REPORT_BEAN]}"/>
<c:set var="userProfile" scope="page" value="${sessionScope[webConstants.USERPROFILE_KEY]}"/>  

<%-- Define collections we will need --%>
<c:set var="reasonStatusCode" scope="page" value="${loanAndWithdrawalRequestsForm.reasonStatusCode}"/>
<c:set var="requestStatusCode" scope="page" value="${loanAndWithdrawalRequestsForm.requestStatusCode}"/>
<c:set var="filterReasons" scope="request" value="${loanAndWithdrawalRequestsForm.lookupData[reasonStatusCode]}"/>
<c:set var="withdrawalRequestStatuses" scope="request" value="${loanAndWithdrawalRequestsForm.lookupData[requestStatusCode]}"/>
<%-- Define content we will be using --%>
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_LOAN_AND_WITHDRAWAL_LEGEND}" type="${contentConstants.TYPE_MISCELLANEOUS}" id="legend"/>
<content:contentBean contentId="${contentConstants.CREATE_LOAN_REQUEST_LINK}" type="${contentConstants.TYPE_MISCELLANEOUS}" id="loanLink"/>

<script  type="text/javascript">
function sortSubmit(sortfield, sortDirection){
  document.forms['loanAndWithdrawalRequestsForm'].elements['task'].value = "sort";
  document.forms['loanAndWithdrawalRequestsForm'].elements['sortField'].value = sortfield;
  document.forms['loanAndWithdrawalRequestsForm'].elements['sortDirection'].value = sortDirection;
  document.forms['loanAndWithdrawalRequestsForm'].submit();
}

function pagingSubmit(pageNumber){
  if (document.forms['loanAndWithdrawalRequestsForm']) {
    document.forms['loanAndWithdrawalRequestsForm'].elements['task'].value = "page";
    document.forms['loanAndWithdrawalRequestsForm'].elements['pageNumber'].value = pageNumber;
    if (document.forms['loanAndWithdrawalRequestsForm']) {
      document.forms['loanAndWithdrawalRequestsForm'].submit();
    } else {
      document.forms.loanAndWithdrawalRequestsForm.submit();
    }
  }
}

/**
 * Resets the contract number field to their default.
 */
function resetContractNumber() {
  document.getElementById('contractNumberId').value = '';
}

/**
 * Resets the contract name field to their default.
 */
function resetContractName() {
  document.getElementById('contractNameId').value = '';
}

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
 * Handler for when a input is detected for the contract name field.
 */
function handleContractNameChanged(field, event) {
  // Determine if there is content in the contract name - clear contract number field if so
  if (field.value.length > 0) {
    resetContractNumber();
  }
}

/**
 * Handler for when a input is detected for the contract number field.
 */
function handleContractNumberChanged(field, event) {
  // Determine if there is content in the last name - clear contract name field if so
  if (field.value.length > 0) {
    resetContractName();
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
 * Handler for when a key input is detected for the contract name field.
 */
function handleContractNameKeyUp(field, event) {
  handleContractNameChanged(field, event);
}

/**
 * Handler for when a key input is detected for the contract number field.
 */
function handleContractNumberKeyUp(field, event) {
  handleContractNumberChanged(field, event);
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

/**
 * Handler for when a mouse input is detected for the contract name field.
 */
function handleContractNameMouseUp(field, event) {
  handleContractNameChanged(field, event);
}

/**
 * Handler for when a mouse input is detected for the contract number field.
 */
function handleContractNumberMouseUp(field, event) {
  handleContractNumberChanged(field, event);
}

/**
 *
 */
function resetFilters() {
  document.forms['loanAndWithdrawalRequestsForm'].elements['task'].value  = "resetFilters";
  document.forms['loanAndWithdrawalRequestsForm'].submit();
}

function participantSearch(task) {
  document.forms['loanAndWithdrawalRequestsForm'].elements['task'].value  = task;
  document.forms['loanAndWithdrawalRequestsForm'].submit();
}

function viewRequest(profileId, contractId, reqStatus, submissionId) {
  document.forms['loanAndWithdrawalRequestsForm'].elements['task'].value  = "viewItem";
  document.forms['loanAndWithdrawalRequestsForm'].elements['submissionId'].value  = submissionId;
  document.forms['loanAndWithdrawalRequestsForm'].elements['profileId'].value  = profileId;
  document.forms['loanAndWithdrawalRequestsForm'].elements['contractId'].value  = contractId;
  document.forms['loanAndWithdrawalRequestsForm'].elements['requestStatus'].value  = reqStatus;
  document.forms['loanAndWithdrawalRequestsForm'].submit();
}

</script>
  <table width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
      <td width="30" valign="top">
      	  <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </td>
	  <td>
		<ps:form method="POST" action="/do/withdrawal/searchSummary/" name="searchWithdrawalRequestForm" modelAttribute="searchWithdrawalRequestForm" >
	
	<input type="hidden" name="contractId" value="${userProfile.currentContract.contractNumber}" />
		  <input type="hidden" name="userType" value="withdrawal" />
		  <input type="hidden" name="task" value="participantSearch" />
		</ps:form>
		
	<ps:form method="POST" action="/do/withdrawal/loanAndWithdrawalRequests/" name="loanAndWithdrawalRequestsForm" modelAttribute="loanAndWithdrawalRequestsForm" >
	  <input type="hidden" name="task" value="filter" />
	<form:hidden path="pageNumber"/>
	<form:hidden path="sortField"/>
	<form:hidden path="sortDirection"/>
	<form:hidden path="profileId"/>
	<form:hidden path="submissionId"/>
	<form:hidden path="contractId"/>
	<form:hidden path="requestStatus"/>
		
		  <%-- Determine if TPA and has selected a contract --%>
		  <c:set var="isTpaPsw" scope="page" value="false"/>

          <c:set var="isBgaContractAndInternalTpa" scope="page" value="false"/>
          <c:if test="${userProfile.currentContract.bundledGaIndicator && userProfile.internalUser}">
            <c:set var="isBgaContractAndInternalTpa" scope="page" value="true"/>
          </c:if>           
          
          
		  <ps:isTpa name="${webConstants.USERPROFILE_KEY}" property="role" >
		    <c:if test="${empty userProfile.currentContract}">
		      <c:set var="isTpaPsw" scope="page" value="true"/>
		    </c:if>           
		  </ps:isTpa>  
		
		  <%-- Create withdrawal link --%>
		  <c:if test="${empty param.printFriendly}">
		    <c:if test="${loanAndWithdrawalRequestsForm.showSearchLink}">
		      <div style="padding-top: 5px; padding-bottom: 5px;">
		        <ps:withdrawalLink contractId = "${isTpaPsw ? '' : userProfile.currentContract.contractNumber}" 
		                           linkType="${isTpaPsw ? webConstants.LINKTYPE_CREATE_TPA : webConstants.LINKTYPE_CREATE_PSW}">
		          <span style="padding-left: 5px;">
                    <a href="javascript:participantSearch('participantSearch');">Create withdrawal request</a>
		          </span>
		        </ps:withdrawalLink>
		      </div>
		    </c:if>
		  </c:if>
 		<%-- Create Loan link --%>
		 <c:if test="${empty param.printFriendly}">
		    <c:if test="${loanAndWithdrawalRequestsForm.showLoanCreateLink}">
		      <div style="padding-top: 5px; padding-bottom: 5px;">
		        <ps:withdrawalLink contractId = "${isTpaPsw ? '' : userProfile.currentContract.contractNumber}" 
		                           linkType="${isTpaPsw ? webConstants.LINKTYPE_CREATE_LOAN_TPA : webConstants.LINKTYPE_CREATE_LOAN_PSW}">
		          <span style="padding-left: 5px;">
  	                <content:getAttribute beanName="loanLink" attribute="text">
		              <content:param>javascript:participantSearch('loanParticipantSearch');</content:param>
		            </content:getAttribute>
		          </span> 
		        </ps:withdrawalLink>
		      </div>
		    </c:if>
		  </c:if>		
		  <%-- Messages section --%>
		  <div style="margin-left: 5px;">
		    <ps:messages scope="session" maxHeight="100px" width="725px" />
		  </div>
		  <div style="margin-left: 5px;">
		    <table border="0" cellpadding="0" cellspacing="0" width="725px">
		      <tr>
		        <td>
		          <div style="padding-top: 5px; padding-bottom: 5px;">
		            <content:getAttribute attribute="text" id="legend"/>
		          </div>
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <table border="0" cellpadding="0" cellspacing="0" width="100%">
		            <tr class="tablehead">
		              <td class="tableheadTD1">
		                <b>
		                  <c:choose>
		                    <c:when test="${fn:length(theReport.details) > 0}">
		                    <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
		                      <report:recordCounter report="theReport" label=""/>
		                      
		                    </c:when>
		                    <c:otherwise>
		                    &nbsp;
		                      <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>            
		                    </c:otherwise>
		                  </c:choose>
		                </b>
		              </td>
		              <td align="right" class="tableheadTDinfo">
		                <report:pageCounterViaSubmit name="loanAndWithdrawalRequestsForm" formName="loanAndWithdrawalRequestsForm" report="theReport" arrowColor="white"/>
		              </td>
		            </tr>
		          </table>
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <table border="0" cellspacing="0" cellpadding="2" width="100%"  style="background-color:white">
		            <tr>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		              <td nowrap="nowrap">
		                <strong>Requests from</strong>
		              </td>
		              <td nowrap="nowrap">
<form:input path="filterFromDate" disabled="${param.printFriendly}" maxlength="${formConstants.DATE_FIELD_LENGTH}" size="${formConstants.DATE_FIELD_LENGTH}" cssClass="inputAmount" id="filterFromDateId" />






		                <c:if test="${empty param.printFriendly}">                           
		                  <img onclick="return handleDateIconClicked(event, 'filterFromDateId', false);" src="/assets/unmanaged/images/cal.gif" alt="Use the Calendar to pick the date">
		                </c:if>
		                <span style="padding-left: 4px;">
		                  <strong>to</strong>
		                </span>
		              </td>
		              <td nowrap="nowrap">
<form:input path="filterToDate" disabled="${param.printFriendly}" maxlength="${formConstants.DATE_FIELD_LENGTH}" size="${formConstants.DATE_FIELD_LENGTH}" cssClass="inputAmount" id="filterToDateId" />






		                <c:if test="${empty param.printFriendly}">                           
		                  <img onclick="return handleDateIconClicked(event, 'filterToDateId', false);" src="/assets/unmanaged/images/cal.gif" alt="Use the Calendar to pick the date">
		                </c:if>
		              </td>
		              <td nowrap="nowrap"><strong>Request status </strong></td>
		              <td>
<form:select path="filterRequestStatus" disabled="${param.printFriendly}" >
		                  <form:option value="-1">All</form:option>
		                  <form:options items="${withdrawalRequestStatuses}" itemValue="code" itemLabel="description"/>
</form:select>
		              </td>
		         
		              <td>
		                <c:if test="${isTpaPsw}">
		                  <strong>Contract name</strong>
		                </c:if>
		              </td>
		              <td>
		                <c:if test="${isTpaPsw}">
<form:input path="filterContractName" disabled="${param.printFriendly}" maxlength="${formConstants.CONTRACT_NAME_LENGTH}" onchange="return handleContractNameChanged(this, event);" onkeyup="return handleContractNameKeyUp(this, event);" id="contractNameId" />






		                </c:if>
		              </td>
		 
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		            </tr>
		            <tr>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		              <td/>
		              <td><strong>(mm/dd/yyyy)</strong></td>
		              <td><strong>(mm/dd/yyyy)</strong></td>
		              <td><strong>Participant</strong></td>
		              <td/>
		              
		               <td>
		                <c:if test="${isTpaPsw}">
		                  <strong>Contract number</strong>
			            </c:if>
		              </td>
		              
		              <td>
		                <c:if test="${isTpaPsw}">
<form:input path="filterContractNumber" disabled="${param.printFriendly}" maxlength="${formConstants.CONTRACT_NUMBER_LENGTH_MAXIMUM}" onchange="return handleContractNumberChanged(this, event);" onkeyup="return handleContractNumberKeyUp(this, event);" id="contractNumberId" />






		                </c:if>
		              </td>
		   
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		            </tr>
		            <tr>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		              <td/>
		              <td/>
		              <td/>
		              <td>
		                <span style="padding-left:10">
		                  <strong>Last name</strong>
		                </span>
		              </td>
		              <td>
<form:input path="filterParticipantLastName" disabled="${param.printFriendly}" maxlength="${globalConstants.LAST_NAME_LENGTH_MAXIMUM}" onchange="return handleLastNameChanged(this, event);" onkeyup="return handleLastNameKeyUp(this, event);" id="lastNameId" />






		              </td>
		              <td/>
		              <td/>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		            </tr>
		            <tr>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		              <td><strong>Type of Request</strong></td>
		              <td colspan="2">
<form:select path="filterRequestReason" disabled="${param.printFriendly}" >
		                  <c:if test="${loanAndWithdrawalRequestsForm.typeOfRequest != 'loanOnly'}">	
								<form:option value="-1">All</form:option>
											
							</c:if>
		                  <form:options items="${filterReasons}" itemValue="code" itemLabel="description"/>
</form:select>
		              </td>
		              <td>
		                <span style="padding-left:10">
		                  <strong>SSN</strong>
		                </span>
		              </td>
		              <td>
		              <form:password path="ssnOne"
		               maxlength="${formConstants.SSN_ONE_LENGTH}" 
		                               size="${formConstants.SSN_ONE_LENGTH}"
		                               disabled="${param.printFriendly}"
		                               onkeyup="return handleSsn1KeyUp(this, event);" 
		                               onchange="return handleSsnChanged(this, ${formConstants.SSN_ONE_LENGTH}, event);"
		                               styleClass="inputField"
		                               styleId="ssnOneId"/>
		                 <form:password path="ssnTwo"
		                 maxlength="${formConstants.SSN_TWO_LENGTH}" 
		                               size="${formConstants.SSN_TWO_LENGTH}"
		                               disabled="${param.printFriendly}"
		                               onkeyup="return handleSsn2KeyUp(this, event);" 
		                               onchange="return handleSsnChanged(this, ${formConstants.SSN_TWO_LENGTH}, event);"
		                               styleClass="inputField"
		                               styleId="ssnTwoId"/>
		               <form:input path="ssnThree"
		               autocomplete="off" 
		                           maxlength="${formConstants.SSN_THREE_LENGTH}" 
		                           size="${formConstants.SSN_THREE_LENGTH}"
		                           disabled="${param.printFriendly}"
		                           onkeyup="return handleSsn3KeyUp(this, event);" 
		                           onchange="return handleSsnChanged(this, ${formConstants.SSN_THREE_LENGTH}, event);"
		                           styleClass="inputField"
		                           styleId="ssnThreeId"/>
		              </td>
		              <td>
		                <c:if test="${empty param.printFriendly}">
		                	<input type="submit" name="Submit" value="search">
		                </c:if>
		              </td>
		              <td>
		                <c:if test="${empty param.printFriendly}">
		                  <a href="javascript:resetFilters();">Reset filters</a>
		                </c:if>
		              </td>
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		            </tr>
		          </table>
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <table border="0" cellspacing="0" cellpadding="0" width="100%">
		            <%-- the header with sorting columns --%>
		            <tr class="tablesubhead">
		              <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		              <td valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="24"/></td>
		              <td class="dataheaddivider" valign="top" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		              <td valign="top" class="tablesubhead">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_REFERENCE_NUMBER}" formName="loanAndWithdrawalRequestsForm" direction="asc">Submission number</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <td valign="top" class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		              <td valign="top" class="tablesubhead">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_REQUEST_DATE}" formName="loanAndWithdrawalRequestsForm" direction="asc">Date of request</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <td valign="top" class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		              <td valign="top" class="tablesubhead">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_REQUEST_REASON}" formName="loanAndWithdrawalRequestsForm" direction="asc">Type of Request</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <c:if test="${isTpaPsw}">
		                <td class="dataheaddivider" valign="top" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		                <td valign="top" class="tablesubhead">
		                  <b>
		                    <report:sortLinkViaSubmit field="${reportConstants.SORT_CONTRACT_NAME}" formName="loanAndWithdrawalRequestsForm" direction="asc">Contract name</report:sortLinkViaSubmit>
		                  </b>
		                </td>
		                <td class="dataheaddivider" valign="top" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		                <td valign="top" class="tablesubhead">
		                  <b>
		                    <report:sortLinkViaSubmit field="${reportConstants.SORT_CONTRACT_NUMBER}" formName="loanAndWithdrawalRequestsForm" direction="asc">Contract number</report:sortLinkViaSubmit>
		                  </b>
		                </td>
		              </c:if>
		              <td valign="top" class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		              <td valign="top" class="tablesubhead">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_PARTICIPANT_NAME}" formName="loanAndWithdrawalRequestsForm" direction="asc">Participant name / SSN</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <td valign="top" class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		              <td valign="top" class="tablesubhead">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_STATUS}" formName="loanAndWithdrawalRequestsForm" direction="asc">Status</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <td valign="top" class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		              <td valign="top">
		                <b>
		                  <report:sortLinkViaSubmit field="${reportConstants.SORT_INITIATED_BY}" formName="loanAndWithdrawalRequestsForm" direction="asc">Initiated by</report:sortLinkViaSubmit>
		                </b>
		              </td>
		              <td class="databorder" valign="top"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		            </tr>
		            <%-- new dynamic content --%>
		            <c:choose>
		              <c:when test="${fn:length(theReport.details) > 0}">
		                <c:forEach var="withdrawalRequest" items="${theReport.details}" varStatus="status">
		                  <tr class="${((status.index % 2) == 0 ) ? 'datacell1' : 'datacell2'}" valign="top">
		                    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		                    <td style="text-align: center;">
	                      <c:if test="${empty param.printFriendly}">
	                      <!-- edit and view button display for "LOAN request"  START-->		                       
		                      <c:if test="${withdrawalRequest.requestReason == webConstants.REQUEST_TYPE_LOAN}">
		                        <c:if test="${withdrawalRequest.showEditLoanRequestLink}">
		                      		<c:if test="${withdrawalRequest.statusCode == webConstants.LOAN_REQUEST_STATUS_DRAFT}">		                      		
<a href="/do/onlineloans/draft/?${withdrawalWebConstants.SUBMISSION_ID_LOAN_PARAMETER}=${withdrawalRequest.referenceNumber}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${withdrawalRequest.contractNumber}" id="editLoan_${withdrawalRequest.referenceNumber}" title="Edit Request">
		                           			 <img src="/assets/unmanaged/images/edit_icon.gif" 
		                               		  height="12" 
		                               		 width="12"
		                               		  border="0">
</a>
		                          	</c:if>
		                          	<c:if test="${withdrawalRequest.statusCode == webConstants.LOAN_REQUEST_STATUS_PENDINGREVIEW}">
<a href="/do/onlineloans/review/?${withdrawalWebConstants.SUBMISSION_ID_LOAN_PARAMETER}=${withdrawalRequest.referenceNumber}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${withdrawalRequest.contractNumber}" id="editLoan_${withdrawalRequest.referenceNumber}" title="Edit Request">
		                           			 <img src="/assets/unmanaged/images/edit_icon.gif" 
		                               		  height="12" 
		                               		 width="12"
		                               		  border="0">
</a>
		                          	</c:if>
		                          	<c:if test="${withdrawalRequest.statusCode == webConstants.LOAN_REQUEST_STATUS_PENDINGAPPROVAL}">
<a href="/do/onlineloans/approve/?${withdrawalWebConstants.SUBMISSION_ID_LOAN_PARAMETER}=${withdrawalRequest.referenceNumber}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${withdrawalRequest.contractNumber}" id="editLoan_${withdrawalRequest.referenceNumber}" title="Edit Request">
		                           			 <img src="/assets/unmanaged/images/edit_icon.gif" 
		                               		  height="12" 
		                               		 width="12"
		                               		  border="0">
</a>
		                          	</c:if>
		                       	</c:if>   		                        
		                        <c:if test="${withdrawalRequest.showViewLoanRequestLink}">
<a href="/do/onlineloans/view/?${withdrawalWebConstants.SUBMISSION_ID_LOAN_PARAMETER}=${withdrawalRequest.referenceNumber}&${withdrawalWebConstants.CONTRACT_ID_LOAN_PARAMETER}=${withdrawalRequest.contractNumber }" id="viewLoan_${withdrawalRequest.referenceNumber}" title="View Request">
                                    <img src="/assets/unmanaged/images/view_icon.gif" 
                                         height="12" 
                                         width="12"
                                         border="0">
</a>
		                        </c:if>
		                      </c:if>
		                   <!-- edit and view button display for "LOAN request"  END--> 
		                        <!-- view and edit button display for withdrawal request START -->
		                        <c:if test="${withdrawalRequest.requestReason != webConstants.REQUEST_TYPE_LOAN}">
		                         <c:if test="${withdrawalRequest.showEditRequestLink}">
<a href="/do/withdrawal/beforeProceedingGateway${(withdrawalRequest.statusCode == requestConstants.WITHDRAWAL_STATUS_DRAFT_CODE) ? 'Init' : 'Review'}/?${withdrawalWebConstants.PROFILE_ID_PARAMETER}=${withdrawalRequest.profileId}&${withdrawalWebConstants.CONTRACT_ID_PARAMETER}=${withdrawalRequest.contractNumber}&${withdrawalWebConstants.WITHDRAWAL_STATUS_CODE_PARAMETER}=${withdrawalRequest.statusCode}&${withdrawalWebConstants.ORIGINATOR_PARAMETER}=${withdrawalWebConstants.WITHDRAWAL_LIST_ORIGINATOR}&${withdrawalWebConstants.SUBMISSION_ID_PARAMETER}=${withdrawalRequest.referenceNumber}&${withdrawalWebConstants.WITHDRAWAL_INITIATED_BY_PARAMETER}=${withdrawalRequest.initiatedBy}" id="editWithdrawal_${withdrawalRequest.referenceNumber}" title="Edit Request">

		                            <img src="/assets/unmanaged/images/edit_icon.gif" 
		                                 height="12" 
		                                 width="12"
		                                 border="0">
</a>
		                        </c:if>
		                        <c:if test="${withdrawalRequest.showViewRequestLink}">
<a href="javascript:viewRequest('${withdrawalRequest.profileId}', '${withdrawalRequest.contractNumber}', '${withdrawalRequest.statusCode}', '${withdrawalRequest.referenceNumber}')" id="viewWithdrawal_${withdrawalRequest.referenceNumber}" title="View Request">


                                    <img src="/assets/unmanaged/images/view_icon.gif" 
                                         height="12" 
                                         width="12"
                                         border="0">
</a>
		                        </c:if>
		                        </c:if>
		                        <!-- view and edit button display for withdrawal request END -->
		                      </c:if>
		                    </td>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                    <td>${withdrawalRequest.referenceNumber}</td>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                    <td><fmt:formatDate value="${withdrawalRequest.requestDate}" pattern="MM/dd/yyyy"/></td>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                    <td>${withdrawalRequest.requestReason}</td>
		                    <c:if test="${isTpaPsw}">
		                      <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                      <td>${withdrawalRequest.contractName}</td>
		                      <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                      <td>${withdrawalRequest.contractNumber}</td>
		                    </c:if>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"/></td>
		                    <td nowrap="nowrap">
		                      <div>
		                        ${withdrawalRequest.participantName}
		                      </div>
		                      <div>
		                        <render:ssn property="withdrawalRequest.ssn"/>
		                      </div>
		                    </td>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                    <td>${withdrawalRequest.status}</td>
		                    <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                    <td>${withdrawalRequest.initiatedBy}</td>
		                    <td valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
		                  </tr>
		                </c:forEach>
		              </c:when>
		              <c:otherwise>
		                <tr class="datacell1">
		                  <td class="databorder" valign="top"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		                  <td valign="top" colspan="${isTpaPsw ? '17' : '13'}">
		                  	<c:choose>
		                  	 	<c:when test = "${loanAndWithdrawalRequestsForm.loanTypeILoan}">
		                  	 		<b>Contract has not converted to online loans
									<a href="/do/iloans/viewLoanRequests/"> 
									Click here to get to the i:loans request page
									</a></b>
		                  	 	</c:when>
		                  	 <c:otherwise>
		                  	 	<b>There are no requests available to display for the above criteria.</b>
		                  	 </c:otherwise>
		                  	</c:choose>	                  	
		                  </td>
		                  <td class="databorder" valign="top"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"/></td>
		                </tr>
		              </c:otherwise>
		            </c:choose>
		            <%-- end new dynamic content --%>
		            <tr>
		              <td class="databorder" colspan="${isTpaPsw ? '19' : '15'}"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		            </tr>
		            <%-- end table border --%>    
		            <tr>
		              <td colspan="${isTpaPsw ? '19 ' : '15'}" align="right">
		                <report:pageCounterViaSubmit name="loanAndWithdrawalRequestsForm" formName="loanAndWithdrawalRequestsForm"  report="theReport" arrowColor="black"/>
		              </td>
		            </tr>
		            <%-- end page count --%>    
		          </table>
		        </td>
		      </tr>
		    </table>
		  </div>
		</ps:form>
		
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		       <tr>
		        <td><content:getAttribute beanName="layoutPageBean" attribute="footer1"/></td>
		      </tr>
		    <c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
		        <tr>
		          <td><content:getAttribute beanName="footnote" attribute="text"/></td>
		        </tr>
		      </c:forEach>
		      <c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
		        <tr>
		          <td><content:getAttribute beanName="disclaimer" attribute="text"/></td>
		        </tr>
		      </c:forEach>
		  </table>
		<c:if test="${param.printFriendly}">
		  <content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}" type="${contentConstants.TYPE_MISCELLANEOUS}" id="globalDisclosure"/>
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr>
		      <td><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		    </tr>
		   </table>
		</c:if>
	</td>
	</tr>
</table>
