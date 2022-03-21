
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Common lookup Maps: US States, Countries constants --%>
<un:useConstants scope="request" var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache" />

<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%-- Withdrawal Request constants --%>
<un:useConstants scope="request" var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />

<%-- Render constants --%>
<un:useConstants scope="request" var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<c:set scope="request" var="withdrawalRequestUi" value="${withdrawalForm.withdrawalRequestUi}" />
<c:set scope="request" var="withdrawalRequest" value="${withdrawalRequestUi.withdrawalRequest}" />
<c:set scope="request" var="states" value="${withdrawalForm.lookupData[lookupConstants.USA_STATE_WITHOUT_MILITARY_TYPE]}" />
<c:set scope="request" var="countries" value="${withdrawalForm.lookupData[lookupConstants.COUNTRY_COLLECTION_TYPE]}" />
<c:set scope="request" var="loans"  value="${withdrawalRequest.loans}" />
<c:set scope="request" var="timeStampPattern" value="${renderConstants.LONG_MDY_SLASHED}"/>

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/disbursements.css" type="text/css">

<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V1}"
  beanName="confirmationText1"/>

<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V2}"
  beanName="confirmationText2"/>

<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_CONFIRMATION_TEXT_V3}"
  beanName="confirmationText3"/>
  
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
  <br/>
<content:errors scope="session"/>
<table border="0" cellpadding="0" cellspacing="0" width="760">
	<tr>
    <td><img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
		<td colspan="2">
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <colgroup>
          <col style="padding-left:10px;"></col>
        </colgroup>
    		<c:if test="${withdrawalRequestUi.isRequestSendForReview}">
    			<tr>
    		    <td><content:getAttribute attribute="text" beanName="confirmationText1" /></td>
	     	  </tr>
		    </c:if>
		    <c:if test="${withdrawalRequestUi.isRequestSendForApprove}">
			    <tr>
		        <td><content:getAttribute attribute="text" beanName="confirmationText2" /></td>
		      </tr>
		    </c:if>
		    <c:if test="${withdrawalRequestUi.isRequestApproved}">
		      <tr>
		        <td><content:getAttribute attribute="text" beanName="confirmationText3" /></td>
		      </tr>
		    </c:if>
		  </table>
		</td>
  </tr>
  <tr>
    <td/>
    <td width="500">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <colgroup>
          <col style="padding-left:10px;"></col>
          <col/>
        </colgroup>
        <tr>
          <td width="35%"><b>Submission number: </b></td>
          <td class="highlightBold" align="left"><b><c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.submissionId}" /></b></td>
        </tr>
        <c:if test="${withdrawalRequestUi.isRequestApproved}">
          <tr>
            <td width="35%"><b>Approved Date/Time: </b></td>
            <td class="highlightBold" align="left">
            	<b>
            		<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.approvedTimestamp}" type="DATE"  pattern="${timeStampPattern}" />
            	</b>
            </td>
          </tr>
        </c:if>
        <c:if test="${withdrawalRequestUi.showExpectedProcessingDate}">
          <tr>
    	      <td width="35%"><b>Expected processing date: </b></td>
    	      <td class="highlightBold" align="left"><b>
                <fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.expectedProcessingDate}" type="DATE" pattern="MM/dd/yyyy"/>
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
       	<jsp:include flush="true" page="viewLoanDetailsSection.jsp" />
        <br/>
      </c:if>
      <jsp:include flush="true" page="viewWithdrawalAmountSection.jsp" />
      <br/>
      <%-- Begin Recipient Loop --%>
    	<c:forEach items="${withdrawalRequestUi.recipients}" var="item" varStatus="itemStatus">
    	  <c:set var="recipientUi" scope="request" value="${item}" />
    	  <c:set var="recipientStatus" scope="request" value="${itemStatus}" />
    	  <%-- Synchronize the RecipientUi and Recipient objects --%>
    	  <c:set var="recipient" scope="request" value="${withdrawalRequest.recipients[recipientStatus.index]}" />
	      <jsp:include flush="true" page="viewPaymentInstructionsSection.jsp" />
	      <jsp:include flush="true" page="view1099RSection.jsp" />
	      <br/>
    	</c:forEach>
	    <%-- End Recipient Loop --%>
      <jsp:include flush="true" page="viewNotesSection.jsp" />
      <br/>
      <jsp:include flush="true" page="viewDeclarationsSection.jsp" />
      <br/>
      <ps:form method="POST" action="/do/withdrawal/confirmation/" modelAttribute="withdrawalForm" name="withdrawalForm" >
       
        <table width="500" border="0" cellspacing="0" cellpadding="0">
          <c:if test="${empty param.printFriendly}">  
            <tr>
          	  <td width="500"><content:getAttribute beanName="layoutPageBean" attribute="footer1"/></td>        
            </tr>
          </c:if>
          <tr>
            <td width="125" align="right">&nbsp;</td>
            <td width="125" align="right">&nbsp;</td>
            <td width="125" align="right">&nbsp;</td>
            <c:if test="${empty param.printFriendly}">  
              <td width="125" align="right">
<input type="submit" class="button100Lg" name="action" value="finished" />
              </td>
            </c:if>
          </tr>
        </table>
      </ps:form>
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
      			<c:if test="${withdrawalForm.withdrawalRequestUi.hasBothPbaAndLoans}">
      				<content:getAttribute beanName="accountBalanceFootnotePbaAndLoan" attribute="text" />
      			</c:if>
      			<c:if test="${withdrawalForm.withdrawalRequestUi.hasPbaOnly}">
      				<content:getAttribute beanName="accountBalanceFootnotePbaOnly" attribute="text" />
      			</c:if>
      			<c:if test="${withdrawalForm.withdrawalRequestUi.hasLoansOnly}">
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
