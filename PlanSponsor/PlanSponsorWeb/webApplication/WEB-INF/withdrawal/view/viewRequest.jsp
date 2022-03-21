<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<%-- Common lookup Maps: US States, Countries constants --%>
<un:useConstants scope="request" var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache" />

<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%-- Withdrawal Request constants --%>
<un:useConstants scope="request" var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<%-- Render constants --%>
<un:useConstants scope="request" var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<%-- Identify this JSP as a View Withdrawal Request page. --%>
<c:set scope="request" var="pageId" value="viewRequest" />

<c:set scope="request" var="permissions" value="${withdrawalPermissionsKey}"/>
<c:set scope="request" var="withdrawalRequestUi" value="${withdrawalForm.withdrawalRequestUi}" />
<c:set scope="request" var="withdrawalRequest" value="${withdrawalRequestUi.withdrawalRequest}" />
<c:set scope="request" var="states" value="${withdrawalForm.lookupData['USA_STATE_WITHOUT_MILITARY_TYPE']}"/>
<c:set scope="request" var="countries" value="${withdrawalForm.lookupData[lookupConstants.COUNTRY_COLLECTION_TYPE]}" />
<c:set scope="request" var="requestStatuses" value="${withdrawalForm.lookupData[lookupConstants.WITHDRAWAL_REQUEST_STATUS_ORDERED]}" />
<c:set scope="request" var="loans"  value="${withdrawalRequest.loans}" />
<c:set var="timeStampPattern" scope="request" value="${renderConstants.LONG_TIMESTAMP_MDY_SLASHED}"/>

<%-- page definition --%>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/disbursements.css" type="text/css">

<script type="text/javascript">
  function handleDeleteClicked() { return confirm('Are you sure?'); }
function showActivityHistory() {
	var popupURL = new URL("/do/withdrawal/activityHistory/");
	popupURL.setParameter("submissionId", "<c:out value="${withdrawalRequest.submissionId}"/>");
	popupURL.setParameter("printFriendly", "true");
	window.open(popupURL.encodeURL(),"","width=720,height=480,resizable,toolbar=no,scrollbars,menubar=no");
}  
</script>

<%-- Bean Definition for CMA Content --%>
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_ACTIVITY_HISTORY}"
  beanName="activityHistory"/>

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_AND_LOAN_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnotePbaAndLoan"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnotePbaOnly"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_LOAN_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnoteLoanOnly"/>

<table border="0" cellpadding="0" cellspacing="0" width="760">
  <tr>
    <td><img src="/assets/unmanaged/images/s.gif" height="1" width="30"><br>
        <img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="500">

    <div style="padding-top: 10px; padding-bottom: 10px;">        
      <ps:messages scope="session" maxHeight="100px"/>
    </div> 
	<%-- GIFL 1C Start --%>
		<% 
			boolean giflInd = ((Boolean)request.getAttribute("isParticipantGIFLEnabled")).booleanValue();
			if(giflInd)
			{
		%>
		<c:if test="${withdrawalRequest.statusCode == 'W5' || withdrawalRequest.statusCode == 'W6' }">
			<c:choose>
				<c:when test="${withdrawalRequest.isParticipantCreated}">
					<content:contentBean
					  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_PARTICIPANT_MESSAGE_TEXT}"
					  type="${contentConstants.TYPE_MISCELLANEOUS}"
					  id="participantInitiatedRequestMessage"/>
          			<content:getAttribute beanName="participantInitiatedRequestMessage" attribute="text" /><br/><br/>
	
				</c:when>
				<c:otherwise>
					<content:contentBean
					  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_VIEW_PS_TPA_MESSAGE_TEXT}"
					  type="${contentConstants.TYPE_MISCELLANEOUS}"
					  id="psTPAInitiatedRequestMessage"/>
          			<content:getAttribute beanName="psTPAInitiatedRequestMessage" attribute="text" /><br/><br/>		
				</c:otherwise>
			</c:choose>
		</c:if>
		<%
		}
		%>
	<%-- GIFL 1C end --%>

    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.activityHistoryEnabled}">
      <c:if test="${empty param.printFriendly}">
        <c:if test="${withdrawalRequestUi.showActivityHistory}">
          <a href="javascript:showActivityHistory()">
            <content:getAttribute attribute="text" beanName="activityHistory" />
          </a>
          <br>
        </c:if>
      </c:if>
    </c:if>
      
      <br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="35%"><b>Submission number</b></td>
          <td class="highlightBold" align="left">
            <b><c:out value="${withdrawalRequest.submissionId}" /></b>
          </td>
        </tr>
        <tr>
          <td width="35%"><b>Status</b></td>
          <td class="highlightBold" align="left">
            <b>
                  <ps:displayDescription collection="${requestStatuses}" keyName="code" keyValue="description" 
              key="${withdrawalRequest.statusCode}"/>
            </b>
          </td>
        </tr>

        <c:if test="${withdrawalRequestUi.isRequestApproved}">
        <tr>
          <td><b>Approved date/time</b></td>
          <td class="highlightBold" align="left">
            <b>
            <fmt:formatDate 
              value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.approvedTimestamp}" 
              type="DATE"  
              pattern="${timeStampPattern}"/>
            </b>
          </td>
        </tr>
        </c:if>
        
        <c:if test="${withdrawalRequestUi.showExpectedProcessingDate}">
        <tr>
          <td><b>Expected processing date</b></td>
          <td class="highlightBold" align="left">
            <b>
            <fmt:formatDate 
              value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.expectedProcessingDate}" 
              type="DATE" 
              pattern="MM/dd/yyyy"/>
            </b>
          </td>
        </tr>
        </c:if>

      </table>
            
      <br>
      <jsp:include flush="true" page="viewParticipantInformationSection.jsp" />
      <br>
      <jsp:include flush="true" page="viewWithdrawalBasicsSection.jsp" />
      <br>
      <c:if test="${fn:length(loans) > 0}">
        <jsp:include flush="true" page="viewLoanDetailsSection.jsp" >
			<jsp:param name="pageTypeView" value="true" />
	    </jsp:include>
        <br>
      </c:if>
      <jsp:include flush="true" page="viewWithdrawalAmountSection.jsp" />
      <br>

<%-- Begin Recipient Loop --%>
<c:forEach items="${withdrawalRequestUi.recipients}"
  var="item" varStatus="itemStatus">
  <c:set scope="request" var="recipientUi" value="${item}" />
  <c:set scope="request" var="recipientStatus" value="${itemStatus}" />
  <%-- Synchronize the RecipientUi and Recipient objects --%>
  <c:set scope="request" var="recipient" value="${withdrawalRequest.recipients[recipientStatus.index]}" />

      <jsp:include flush="true" page="viewPaymentInstructionsSection.jsp" />
      <jsp:include flush="true" page="view1099RSection.jsp" />
      <br>
</c:forEach>
<%-- End Recipient Loop --%>

      <jsp:include flush="true" page="viewNotesSection.jsp" />
      <br>
      <jsp:include flush="true" page="viewDeclarationsSection.jsp" />
      <br>
      

 <c:if test="${empty param.printFriendly}">
  <ps:form method="POST" action="/do/withdrawal/viewRequest/" >
           
      <table width="500" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500"><content:getAttribute beanName="layoutPageBean" attribute="footer1"/></td>
        </tr>
        <tr>
          <td width="125" align="right">&nbsp;</td>
          <td width="125" align="right">&nbsp;</td>
          <td width="125" align="right">&nbsp;
    <c:if test="${permissions.delete}">
<input type="submit" class="button100Lg" onclick="return handleDeleteClicked();" name="action" value="delete" />

    </c:if>
          </td>
          <td width="125" align="right">
<input type="submit" class="button100Lg" name="action" value="finished" />
          </td>
        </tr>
      </table>

  </ps:form>
  </c:if>


    </td>
    <td width="260">&nbsp;</td>

    <!-- end main content table --> 
  
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  
  <tr>
    <td>&nbsp;</td>
    <td colspan="2">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
<c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
      <tr>
        <td><content:getAttribute beanName="footnote" attribute="text"/></td>
      </tr>
</c:forEach>
      <tr>
        <td>
<c:if test="${withdrawalRequestUi.hasBothPbaAndLoans}">
          <content:getAttribute beanName="accountBalanceFootnotePbaAndLoan" attribute="text" />
</c:if>
<c:if test="${withdrawalRequestUi.hasPbaOnly}">
          <content:getAttribute beanName="accountBalanceFootnotePbaOnly" attribute="text" />
</c:if>
<c:if test="${withdrawalRequestUi.hasLoansOnly}">
          <content:getAttribute beanName="accountBalanceFootnoteLoanOnly" attribute="text" />
</c:if>
        </td>
      </tr>
<c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
      <tr>
        <td><content:getAttribute beanName="disclaimer" attribute="text"/></td>
      </tr>
</c:forEach>

    </table>
  </td>
</tr>
  
</table>
     
<!-- footer table -->
