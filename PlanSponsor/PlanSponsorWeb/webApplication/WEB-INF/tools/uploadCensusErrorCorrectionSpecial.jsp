<%@ page import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationErrorCode"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm.SpecialPageType"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib prefix="uploadCensus" tagdir="/WEB-INF/tags/tools/uploadCensus"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<%
String similarssn=SpecialPageType.similarSsn;
String duplicateSubmittedSSN=SpecialPageType.duplicateSubmittedSSN;
String multipleDuplicateEmployeeId=SpecialPageType.multipleDuplicateEmployeeIdSortOptionNotEE;
String duplicateEmployeeId=SpecialPageType.duplicateEmployeeIdAccountHolderSortOptionNotEE;
String empidnonaccount=SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionEE;
String nonaccountduplicateoption=SpecialPageType.duplicateEmployeeIdNonAccountHolderSortOptionNotEE;
String duplicateemail=SpecialPageType.duplicateEmail;
String duplicatesubemial=SpecialPageType.duplicateSubmittedEmail;
%>
<un:useConstants var="content"
  className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_GENERAL_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_GENERAL_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_WARNING_ONLY_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningOnlyHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_WARNING_ONLY_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningOnlyIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_ERROR_ONLY_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorOnlyIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_SUBMITTED_SSN_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DuplicateSubmittedSSNActionText" />
	
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_SUBMITTED_EMPID_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DuplicateSubmittedEmpIdActionText" />
	
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_SIMILIAR_SSN_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="SimilarSsnActionText" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_EMPID_NON_ACCOUNT_HOLDER_EE_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DuplicateEmpIdNonAccountHolderEEActionText" />
	
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_EMPID_NON_ACCOUNT_HOLDER_NOT_EE_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DuplicateEmpIdNonAccountHolderNotEEActionText" />
	
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DISCARD_RECORD_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DiscardRecordActionText" />	
	
<content:contentBean contentId = "<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_SUBMITTED_EMAIL_ACTION_REQUIRED_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="duplicateSDBEmailAddressText" />	
	
<content:contentBean contentId = "<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_DUPLICATE_SUBMITTED_CSDB_EMAIL_ACTION_REQUIRED_TEXT%>"  
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="duplicateCSDBEmailAddressText" />	


<style type="text/css">
DIV.scrollDiv {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 130px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}

</style>

<script type="text/javascript">
<!--
function doConfirm(msg) {
  return confirm(msg);
}

function showEmployeeRecord(profileId) {
  printURL = "/do/census/viewEmployeeSnapshot/?profileId=" + profileId + "&printFriendly=true";      
  window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
}

function gotoSubmissionDetailsPage(warning) {
  if (discardChanges(warning)) {
    parent.location.href='/do/tools/viewCensusDetails/';
  }
  return false;
}
function discardChanges1(warning) {
	if (discardChanges(warning)) {
		document.getElementById("nextRecord").value="NextRecord";
	  }
	}

//-->
</script>

<ps:form method="post" action="/do/tools/uploadCensusErrorCorrection/"
  cssStyle="margin-bottom: 0pt;" modelAttribute="errorCorrectionForm" name="errorCorrectionForm">

  <table border="0" cellpadding="0" cellspacing="0" width="700">
    <tbody>
      <tr>
        <td width="15"><img src="/assets/unmanaged/images/spacer.gif" border="0"
          height="1" width="30"></td>
        <td width="609" valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="500">
              <tbody>
                <tr>
                  <td width="1"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                  <td width="225"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="225"></td>
                  <td width="1"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                  <td width="175"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="175"></td>
                  <td width="1"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                  <td width="215"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="175"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="175"></td>
                  <td width="1"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                </tr>

                <tr class="tablehead">
                  <td colspan="6" class="tableheadTD1">
	                  <strong>
	                  Errors
	                  </strong>
                  </td>
                  <td width="1" class="databorder"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                </tr>
                <tr class="tablesubhead">
                  <td width="1" height="38" valign="top"
                    class="databorder"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                  <td width="615" colspan="5" class="datacell1">
                  <c:choose>
                    <%-- Edit 13 --%>
                    <c:when test="${specialError.errorCode eq 'SimilarSSN'}">
	                     <div class="scrollDiv">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ol>
	                        <li>
	                        <ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                        <br/>
	                        <br/>
	                        <strong>Similar records: </strong> <br>
	                        <c:forEach var="employee"
	                          items="${specialError.employees}">
	                          <c:out value="${employee.lastName}" /> ,<c:out
	                            value="${employee.firstName}" />
	                          <c:out value="${employee.middleInit}" />
	                          ${fn:substring(employee.ssn, 0, 3)}-${fn:substring(employee.ssn, 3, 5)}-${fn:substring(employee.ssn, 5, -1)}
	                          <a
	                            href="javascript:showEmployeeRecord(<c:out value='${employee.profileId}'/>)">
	                          View similar record </a>
	                          <br/>
	                        </c:forEach>
	                        </li>
	                     </ol>
	                     <br/>
	                     <br/>
	                     <content:getAttribute beanName="SimilarSsnActionText" attribute="text"/>
<input type="hidden" name="specialPageType" value="${similarssn}"/>
	                     </div>
                    </c:when>
                    <%-- Edit 89 --%>
                    <c:when
	                     test="${specialError.errorCode eq 'DuplicateSubmittedSSN'}">
	                     <div class="scrollDiv">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ol>
	                        <li><ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                        <br/>
	                        <br/>
	                        <content:getAttribute id="DuplicateSubmittedSSNActionText" attribute="text"/>
	                        <br/>
	                        <c:forEach var="employee" 
	                          items="${specialError.employees}">
	                          Row&nbsp;<c:out value="${employee.sourceRecordNo}" />
	                          <br/>
	                        </c:forEach>
	                        </li>
	                  	 </ol>
<input type="hidden" name="specialPageType" value="${duplicateSubmittedSSN}"/>
	                     </div>
                    </c:when>
                    <%-- Edit 16 --%>
                    <c:when
	                     test="${specialError.errorCode eq 'DuplicateSubmittedEmployeeId'}">
	                     <div class="scrollDiv">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ol>
	                        <li><ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                        <br/>
	                        <br/>
	                        <content:getAttribute id="DuplicateSubmittedEmpIdActionText" attribute="text"/>
	                        <br/>
	                        <c:forEach var="employee" 
	                          items="${specialError.employees}">
	                          Row&nbsp;<c:out value="${employee.sourceRecordNo}" />
	                          <br/>
	                        </c:forEach>
	                        </li>
	                  	 </ol>
<input type="hidden" name="specialPageType"/>
	                     </div>
                    </c:when>
                    <%-- Edit 211 --%>
                    <c:when
	                     test="${specialError.errorCode eq 'MultipleDuplicateEmployeeIdSortOptionNotEE'}">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                     <br/>
	                     <br/>
<input type="hidden" name="specialPageType" value="${multipleDuplicateEmployeeId}"/>
                    </c:when>
                    <%-- Edit 209 --%>
                    <c:when
	                     test="${specialError.errorCode eq 'DuplicateEmployeeIdAccountHolderSortOptionNotEE'}">
                         <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
                         <br/>
                         <br/>
	                     <ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                     <br/>
	                     <br/>
<input type="hidden" name="specialPageType" value="${duplicateEmployeeId}"/>
                    </c:when>
                    <%-- Edit 206 --%>  
                    <c:when
	                     test="${specialError.errorCode eq 'DuplicateEmployeeIdNonAccountHolderSortOptionEE'}">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <img alt='Error' src='/assets/unmanaged/images/error.gif'>&nbsp;
	                     <c:forEach var="employee" begin="0" end="0" items="${specialError.employees}">
	                        <content:getAttribute id="DuplicateEmpIdNonAccountHolderEEActionText" attribute="text">
	                           <content:param><c:out value="${employee.employeeNumber}"/></content:param>
	                           <content:param><c:out value="${employee.lastName}"/></content:param>
	                           <content:param><c:out value="${employee.firstName}"/></content:param>
	                           <content:param><render:ssn property="employee.ssn" defaultValue=""/></content:param>
	                           <content:param><c:out value="${specialError.params[0]}"/></content:param>
						    </content:getAttribute>
						 </c:forEach>
						 &nbsp;&nbsp;[${specialErrorMsg.errorCode}]
	                     <br/>
	                     <br/>
<input type="hidden" name="specialPageType" value="${empidnonaccount}"/>
                    </c:when>
                    <%-- Edit 207 --%>
                    <c:when
	                     test="${specialError.errorCode eq 'DuplicateEmployeeIdNonAccountHolderSortOptionNotEE'}">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <img alt='Error' src='/assets/unmanaged/images/error.gif'>&nbsp;
	                     <c:forEach var="employee" begin="0" end="0" items="${specialError.employees}">
	                        <content:getAttribute id="DuplicateEmpIdNonAccountHolderNotEEActionText" attribute="text">
	                           <content:param><c:out value="${employee.employeeNumber}"/></content:param>
	                           <content:param><c:out value="${employee.lastName}"/></content:param>
	                           <content:param><c:out value="${employee.firstName}"/></content:param>
	                           <content:param><render:ssn property="employee.ssn" defaultValue=""/></content:param>
	                           <content:param>blank</content:param>
						    </content:getAttribute>
						 </c:forEach>
						 &nbsp;&nbsp;[${specialErrorMsg.errorCode}]
						 <br/>
	                     <br/>
<input type="hidden" name="specialPageType" value="${nonaccountduplicateoption}"/>
                    </c:when>
                    <%-- Edits 11, 137, 14, 136, 15, 210, 208, 17, 31, 30 --%>
                    <c:when test="${specialError.errorCode eq 'InvalidSSN' ||
                    				specialError.errorCode eq 'InvalidEmployeeIdSortOptionEE' ||
                    				specialError.errorCode eq 'MissingSSN' ||
                    				specialError.errorCode eq 'EmployeeIdSSNMismatch' ||
                    				specialError.errorCode eq 'MissingEmployeeId' ||
                    				specialError.errorCode eq 'MultipleDuplicateEmployeeIdSortOptionEE' || 
                    				specialError.errorCode eq 'DuplicateEmployeeIdAccountHolderSortOptionEE' ||
                                    specialError.errorCode eq 'MissingSSNNewEmployeeEE' ||
                                    specialError.errorCode eq 'CancelledParticipant' ||
                                    specialError.errorCode eq 'CancelledEmployee'}">
                         <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
                         <br/>
                         <br/>
	                     <ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                     <br/>
	                     <br/>
	                     <content:getAttribute beanName="DiscardRecordActionText" attribute="text"/>
<input type="hidden" name="specialPageType"/>
                    </c:when>
					<%-- Edit 214  --%>
					<c:when test="${specialError.errorCode eq 'DuplicateEmailAddress'}">
	                     <div class="scrollDiv">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ol>
	                        <li>
	                        <ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                        <br/>
	                        <br/>
							<content:getAttribute beanName="duplicateCSDBEmailAddressText" attribute="text">
	                        <c:forEach var="employee"
	                          items="${specialError.employees}">
	                          <c:out value="${employee.lastName}" /> ,<c:out
	                            value="${employee.firstName}" />
	                          <c:out value="${employee.middleInit}" />
	                          ${fn:substring(employee.ssn, 0, 3)}-${fn:substring(employee.ssn, 3, 5)}-${fn:substring(employee.ssn, 5, -1)}
	                          <content:param>javascript:showEmployeeRecord(<c:out value='${employee.profileId}'/>)</content:param>
	                          <br/>
	                        </c:forEach>
							</content:getAttribute>
	                        </li>
	                     </ol>
	                     <br/>
	                     <br/>
						 
<input type="hidden" name="specialPageType" value="${duplicateemail}"/>
	                     </div>
                    </c:when>
					<%-- Edit 213 --%>
					<c:when
	                     test="${specialError.errorCode eq 'DuplicateSubmittedEmailAddress'}">
	                     <div class="scrollDiv">
					     <content:getAttribute beanName="ErrorOnlyIntro" attribute="text"/>
					     <br/>
					     <br/>
	                     <ol>
	                        <li><ps:employeeSnapshotErrorMsg name="specialErrorMsg" />
	                        <br/>
	                        <br/>
	                        <content:getAttribute id="duplicateSDBEmailAddressText" attribute="text">
	                        <br/>
							<c:set var="rowList" value=""/>
							<% int count = 0; 
								%>

	                        <c:forEach var="employee" 
	                          items="${specialError.employees}">
							  <c:set var="rowList" value="${rowList}Row ${employee.sourceRecordNo} <br>"/>
							  <% count++; 
							  	if (count >= 10) { %>
								<c:set var="rowList" value="${rowList}There are more than 10 rows with the same Employer Provided Email Address"/>
								<%
								break;}  
								%>
								
	                        </c:forEach>
							<content:param>${rowList}</content:param>
							</content:getAttribute>
	                        </li>
	                  	 </ol>
<input type="hidden" name="specialPageType" value="${duplicatesubemial}"/>
	                     </div>
                    </c:when>
					
					
                    <c:otherwise>
                    	 Unknown error code [${specialError.errorCode}]
<input type="hidden" name="specialPageType"/>
                    </c:otherwise>
                  </c:choose>
                  </div>
                  </td>
                  <td width="1" valign="top" class="databorder"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                </tr>

                <tr>
                  <td width="1" height="1" class="databorder"><img
                    src="/assets/unmanaged/images/s.gif" height="1"
                    width="1"></td>

                  <td height="1" colspan="6" class="databorder"><img
                    src="/assets/unmanaged/images/spacer.gif" border="0"
                    height="1" width="1"></td>
                </tr>
              </tbody>
            </table>
        
        <br />

        <jsp:include page="uploadCensusErrorCorrectionErrorSummary.jsp"
          flush="true" />
          
        <br />

          <table border="0" cellpadding="0" cellspacing="0" width="605">
            <tbody>
              <tr valign="top">
                <td>
                <div align="center">
<input type="submit" class="button134" onclick="return doConfirm('Do you wish to remove this record permanently from the census file?')" name="action" value="Discard" /><%--  - property="actionLabel" --%>



                  &nbsp;
                </div>
                </td>
                <td>
                <div align="center">
                  &nbsp;
<input type="button" onclick="return gotoSubmissionDetailsPage('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')" name="action" class="button134" value="Submission details"/>



                  &nbsp;
                </div>
                </td>
                <%-- Edits 13, 89, 206, 207 --%>
                <c:if test="${specialError.errorCode eq 'SimilarSSN' or 
                			specialError.errorCode eq 'DuplicateSubmittedSSN' or
                			specialError.errorCode eq 'DuplicateEmployeeIdNonAccountHolderSortOptionEE' or
                			specialError.errorCode eq 'DuplicateEmployeeIdNonAccountHolderSortOptionNotEE'}"> 
                <td>
                <div align="center">
                  &nbsp;
<input type="submit" class="button134" value="Accept" name="action" /><%--  - property="actionLabel" --%>
                  &nbsp;
                </div>
                </td>
                </c:if>
                <%-- Edit 211, 209, 207 --%>
                <c:if test="${specialError.errorCode eq 'MultipleDuplicateEmployeeIdSortOptionNotEE' or 
                            specialError.errorCode eq 'DuplicateEmployeeIdAccountHolderSortOptionNotEE' or
                			specialError.errorCode eq 'DuplicateEmployeeIdNonAccountHolderSortOptionNotEE' or
                			specialError.errorCode eq 'DuplicateEmailAddress' or
                			specialError.errorCode eq 'DuplicateSubmittedEmailAddress'}"> 
                <td>
                <div align="center">
                  &nbsp;
<input type="submit" class="button134" value="Save" name="action" /><%--  - property="actionLabel" --%>
                  &nbsp;
                </div>
                </td>
                </c:if>
                <td>
                <div align="center">
                  &nbsp;
<input type="submit" class="button134" id="nextRecord"  onclick="return discardChanges1('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')"  name="action" value="Next record (${errorCorrectionForm.numberOfErrorsInSubmission})" /><%--  - property="actionLabel" --%>



                  &nbsp;
                </div>
                </td>
              </tr>
            </tbody>
          </table>

        </td>
      </tr>
      
    </tbody>
  </table>
</ps:form>

<!-- footer table -->
<br />
<table height="25" cellSpacing="0" cellPadding="0" width="760"
  border="0">
  <tr>
    <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <!--deleted 1-->
      <tr>
      </tr>
      <tr>
        <td width="1%"><img src="/assets/unmanaged/images/s.gif"
          height="1"></td>
        <td width="99%"><br>
        <p><content:pageFooter beanName="layoutPageBean" /></p>
        <p class="footnote"><content:pageFootnotes
          beanName="layoutPageBean" /></p>
        <p class="disclaimer"><content:pageDisclaimer
          beanName="layoutPageBean" index="-1" /></p>
        </td>
      </tr>
    </table>
    </td>
    <td width="3%"><img src="/assets/unmanaged/images/s.gif"
      height="1"></td>
    <td width="15%"><img src="/assets/unmanaged/images/s.gif"
      height="1"></td>
  </tr>
</table>

