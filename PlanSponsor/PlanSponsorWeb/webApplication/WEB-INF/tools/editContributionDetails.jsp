<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionPaymentForm" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditContributionDetailsForm" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<jsp:useBean id="editContributionDetailsForm" scope="session" class="com.manulife.pension.ps.web.tools.EditContributionDetailsForm"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_NEGATIVE_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNegativeAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_EFFECTIVE_DATE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidDate"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_PAYMENT_INFO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messagePaymentInfo"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_CASH_VALUE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxCashValue"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_CASH_CONT_ONLY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageCashContOnly"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_CASH_FUTURE_DATED%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageFutureDated"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_BILL_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxBillAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_TEMP_CREDIT_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxTempCreditAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_GENERATE_STMT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidGenerateStmt"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PAYMENT_AUTH%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoPaymentAuth"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_PAYMENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNoPaymentInstructionsIncluded"/>
<content:contentBean contentId="<%=ContentConstants.NO_CHANGES_MADE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoChangesMade"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NOT_AUTHORIZED_CREATE_CONTRIB%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageContractStatusNotAuth"/>


<%

EditContributionDetailsForm requestForm = (EditContributionDetailsForm)session.getAttribute("editContributionDetailsForm");
pageContext.setAttribute("requestForm",requestForm,PageContext.REQUEST_SCOPE);
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
ContributionDetailItem contributionData=null;
if(theReport!=null){
 contributionData = theReport.getContributionData();}
pageContext.setAttribute("submissionItem",contributionData,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String paymentTableHeight =String.valueOf(SubmissionPaymentForm.MAX_PAYMENT_TABLE_HEIGHT);
pageContext.setAttribute("paymentTableHeight",paymentTableHeight,PageContext.PAGE_SCOPE);
%>

<c:set var="theReport" value="${editContributionDetailsForm.theReport}" scope="page" />

<c:if test="${editContributionDetailsForm.noPermission !=true}">
<c:set var="submissionItem" value="${theReport.contributionData}"/>
<c:set var="ifileConfig" value="${userProfile.contractProfile.ifileConfig}"/>
</c:if>
<c:set var="contract" value="${userProfile.currentContract}" scope="request" />
<c:set var="trackChanges" value="true" scope="request" />
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_PAYMENT_TEXT%>"
    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
    beanName="fileUploadPaymentNote"/>


<style type="text/css">
<!--
div.scroll {
	height: 234px;
	width: 225px;
	overflow: auto;
	border-style: none;
	background-color: #fff;
	padding: 8px;}
div.paymentScroll {
	height: ${editContributionDetailsForm.paymentTableHeight}px;
	width: 100%;
	<c:if test="${editContributionDetailsForm.paymentTableHeight == paymentTableHeight}">
	overflow: auto;
	</c:if>
	border-style: none;
	background-color: #fff;
	padding: 0px;}
div.inline {
	display: inline; }
-->
</style>

<script type="text/javascript" >

	//AG: formName == editContributionDetailsForm

	function isFormChanged() {
<c:if test="${editContributionDetailsForm.hasChanged ==true}">
			return true;
</c:if>
<c:if test="${form.hasChanged !=true}">
			return changeTracker.hasChanged();
</c:if>
	}

	// common messsages
	var warningDiscardChanges = '<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>';
	var messageNegativeAmount = '<content:getAttribute beanName="messageNegativeAmount" attribute="text" filter="true"/>';
	var messageMaxAmount = '<content:getAttribute beanName="messageMaxAmount" attribute="text" filter="true"/>';
	var messageValidDate = '<content:getAttribute beanName="messageValidDate" attribute="text" filter="true"/>';
	var messagePaymentInfo = '<content:getAttribute beanName="messagePaymentInfo" attribute="text" filter="true"/>';
	var messageMaxCashValue = '<content:getAttribute beanName="messageMaxCashValue" attribute="text" filter="true"/>';
	var messageCashContOnly = '<content:getAttribute beanName="messageCashContOnly" attribute="text" filter="true"/>';
	var messageFutureDated = '<content:getAttribute beanName="messageFutureDated" attribute="text" filter="true"/>';
	var messageMaxBillAmount = '<content:getAttribute beanName="messageMaxBillAmount" attribute="text" filter="true"/>';
	var messageMaxTempCreditAmount = '<content:getAttribute beanName="messageMaxTempCreditAmount" attribute="text" filter="true"/>';
var messageDataCheckerWarnings = "${editContributionDetailsForm.dialogWarningMessages}";
	var messageValidGenerateStmt = '<content:getAttribute beanName="messageValidGenerateStmt" attribute="text" filter="true"/>';
	var messageNoChangesMade = '<content:getAttribute beanName="messageNoChangesMade" attribute="text" filter="true"/>';

<c:if test="${editContributionDetailsForm.noPermission !=true}">
var numberOfParticipants = ${submissionItem.numberOfParticipants};
</c:if>

	<% if (editContributionDetailsForm.isDisplayPaymentInstructionSection() && editContributionDetailsForm.getAccounts() != null && editContributionDetailsForm.getAccounts().size() > 0) {%>
		var isPaymentSectionShown=true;
	<%} else {%>
		var isPaymentSectionShown=false;
	<%}%>

	<% if (editContributionDetailsForm.isCashAccountPresent()) {%>
		var isCashAccountPresent=true;
	<%} else {%>
		var isCashAccountPresent=false;
	<%}%>

var isInternalUser=${userProfile.internalUser};
<c:if test="${editContributionDetailsForm.noPermission !=true}">
var systemStatus=${submissionItem.systemStatus};
</c:if>
<c:if test="${editContributionDetailsForm.noPermission ==true}">
		var systemStatus="";
</c:if>
var contributionFields = ${editContributionDetailsForm.paymentContributionInputObjectsNamesForJavascript} <%-- filter="false" --%>;
var billFields = ${editContributionDetailsForm.paymentBillInputObjectsNamesForJavascript} <%-- filter="false" --%>;
var creditFields = ${editContributionDetailsForm.paymentCreditInputObjectsNamesForJavascript} <%-- filter="false" --%>;

	var cashAccountRow;
<c:forEach items="${editContributionDetailsForm.accountsRowsObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var accountsRow${theIndex.index} = ${theItem};
	// the last account is the cash account if its present
cashAccountRow = accountsRow${theIndex.index};
</c:forEach>



<c:forEach items="${editContributionDetailsForm.contributionColumnsInputObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var moneyFieldsCol${theIndex.index} = ${theItem};
</c:forEach>

<c:forEach items="${editContributionDetailsForm.loanColumnsInputObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var loanFieldsCol${theIndex.index} = ${theItem};
</c:forEach>

<c:if test="${editContributionDetailsForm.noPermission !=true}">
var moneyFieldsPageTotals = ${editContributionDetailsForm.moneyFieldsPageTotalsForJavascript} <%-- filter="false" --%>;
var loanFieldsPageTotals = ${editContributionDetailsForm.loanFieldsPageTotalsForJavascript} <%-- filter="false" --%>;

var moneyFieldsColumnTotals = ${editContributionDetailsForm.moneyFieldsColumnTotalsForJavascript} <%-- filter="false" --%>;
var loanFieldsColumnTotals = ${editContributionDetailsForm.loanFieldsColumnTotalsForJavascript} <%-- filter="false" --%>;
</c:if>

<c:forEach items="${editContributionDetailsForm.participantFieldsObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var participantFieldsRow${theIndex.index} =${theItem};
</c:forEach>

<c:if test="${editContributionDetailsForm.noPermission !=true}">
	// payroll effective valid dates
var payRollValidDates = ${editContributionDetailsForm.allowedPayrollDatesJavaScript} <%-- filter="false" --%>;
	// payment effective valid dates
var validDates = ${editContributionDetailsForm.allowedPaymentDatesJavaScript} <%-- filter="false" --%>;
</c:if>

<c:if test="${not empty editContributionDetailsForm.paymentInfo}">
var outstandingBillPayment = ${editContributionDetailsForm.paymentInfo.outstandingBillPayment};
var outstandingTemporaryCredit = ${editContributionDetailsForm.paymentInfo.outstandingTemporaryCredit};
var cashAccountAvailableBalance = ${editContributionDetailsForm.paymentInfo.cashAccountAvailableBalance};
var cashAccountTotalBalance = ${editContributionDetailsForm.paymentInfo.cashAccountTotalBalance};
</c:if>
<c:if test="${not empty editContributionDetailsForm.paymentDetails}">
	var paymentsExist = true;
</c:if>
<c:if test="${empty editContributionDetailsForm.paymentDetails}">
	var paymentsExist = false;
</c:if>
	
var isBillPaymentSectionShown = ${editContributionDetailsForm.displayBillPaymentSection};
var isTemporaryCreditSectionShown = ${editContributionDetailsForm.displayTemporaryCreditSection};
	var currentDate=<%=editContributionDetailsForm.getCurrentDateJavascriptObject()%>;
	var defaultEffectiveDate=<%=editContributionDetailsForm.getDefaultEffectiveDateJavascriptObject()%>;

	registerTrackChangesFunction(isFormChanged);
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}

</SCRIPT>
<content:errors scope="session"/>
<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

          <tr>
				<td width="16" class="big">&nbsp;</td>
				<td width="500" height="20">
					<ps:form action="/do/tools/editContribution/" method="POST" onsubmit="return confirmSend();" modelAttribute="editContributionDetailsForm" name="editContributionDetailsForm">
					
  			    	
<form:hidden path="subNo"/>
<form:hidden path="showConfirmDialog"/>
<form:hidden path="ignoreDataCheckWarnings"/>
<form:hidden path="forwardFromSave"/>
<c:if test="${editContributionDetailsForm.noPermission !=true}">
						<jsp:include page="contractEnterSection.jsp" flush="true" />
</c:if>
<c:if test="${editContributionDetailsForm.noPermission ==true}">
						<table width="500" border="0" cellpadding="0" cellspacing="0">
						<tr>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="99" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="98" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="98" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="tablehead" height="25">
						  <td class="tableheadTD1" colspan="9"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
						</tr>
						<tr class="datacell1">
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
							<td class="datacell1" colspan="7" >
								<span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
										<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
										You are not authorized to make submissions.
</c:if>
								</span>
							</td>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="lastrow" colspan="7"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
						  <td class="lastrow" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						  <td class="databorder" colspan="7"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						</table>
</c:if>
					<br/>
					<br/>

<c:if test="${editContributionDetailsForm.noPermission !=true}">
					  <table width="500" border="0" cellpadding="0" cellspacing="0">
						<tr>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="420" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="tablehead" height="25">
						  <td class="tableheadTD1" colspan="5"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
						</tr>
						<tr class="whiteborder">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td colspan="3" >
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
<c:set var="lastRowStyle" value="datacell1" scope="request" />
<c:if test="${userProfile.internalUser ==true}">
<c:if test="${editContributionDetailsForm.displayPaymentInstructionSection ==true}">
									<jsp:include page="paymentInternalUserEditSection.jsp" flush="true" />
</c:if>
<c:if test="${editContributionDetailsForm.displayPaymentInstructionSection !=true}">
									<tr>
										<td class="datacell1"><span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
											<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
											<content:getAttribute beanName="messageNoPaymentAuth" attribute="text"/>
</c:if>
										</span></td>
									</tr>
</c:if>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
							<%if (editContributionDetailsForm.isDisplayPaymentInstructionSection()) {%>
								<% if (editContributionDetailsForm.getAccounts() != null && editContributionDetailsForm.getAccounts().size() > 0) { %>
									<tr>
										<td class="datacell1"><span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body2"/></span></td>
									</tr>
									<jsp:include page="paymentEnterSection.jsp" flush="true" />
								<%} else {%>
								<tr>
									<td class="datacell1">
										<span class="content">You do not have any bank accounts eligible for payment instructions.</span>
									</td>
								</tr>
								<%}%>

							<%} else {%>
								<tr>
							  		<td class="datacell1"><span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
										<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
										<content:getAttribute beanName="messageNoPaymentAuth" attribute="text"/>
</c:if>
									</span></td>
								</tr>
							<%}%>
</c:if>
						  </table>
						  </td>

						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
<tr class="${lastRowStyle}">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
<tr class="${lastRowStyle}">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
						  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						  <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>

                </table>
</c:if>
                </td>

				<!--// end column 2 -->


				<!-- column 3 column gap -->
				<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1" border="0"></td>
				<!--// emd column 3 -->

				<!-- column 4 HELPFUL HINT -->
				<td align="left" valign="top">
<c:if test="${editContributionDetailsForm.noPermission !=true}">
						<c:if test="${empty param.printFriendly}" >
							<report:submissionErrors errors="<%=theReport%>" contributionDetailsErrors="true" warnings="<%=editContributionDetailsForm.getWarningMessage()%>" width="230" printFriendly="false" forceView="true"/>
						</c:if>
</c:if>

					<logicext:if name="userProfile" property="allowedCashAccount" op="equal" value="true">
						<logicext:or name="userProfile" property="allowedDirectDebit" op="equal" value="true"/>

						<logicext:then>
						   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
						   <!-- file upload helpful hint -->
						   <content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />
						</logicext:then>

					</logicext:if>

				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" />
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer3" beanName="layoutPageBean" />
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer4" beanName="layoutPageBean" />
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				</td>

				<!--// end column 4 -->
			</tr>
          <tr>
				<td width="16" class="big">&nbsp;</td>
				<td colspan="3">

					<%-- START new contribution details table --%>
<c:if test="${editContributionDetailsForm.noPermission !=true}">
						<c:if test="${not empty param.printFriendly }" >
							<br/>
							<br/>
							<report:submissionErrors errors="<%=theReport%>" contributionDetailsErrors="true" warnings="<%=editContributionDetailsForm.getWarningMessage()%>"  width="505" printFriendly="true" forceView="true"/>
						</c:if>
						<br/>
						<br/>
						 <jsp:include page="participantEnterSection.jsp" flush="true" />
								<br/><br/>
								<table width="760" border="0" cellpadding="0" cellspacing="0">
									<tbody>
									  <tr>
										<td witdh="25%" align="center">
											<span class="content">
<input type="submit" class="button134" onclick="return doCancelWithValue(this);" value="undo" name="task" />
											</span>
										</td>
										<td witdh="25%" align="center">
											<span class="content">
<input type="submit" class="button134" onclick="return doCancelWithValue(this);" value="cancel" name="task" />
											</span>
										</td>
										<td witdh="25%" align="center">
											<span class="content">
<input type="submit" id="saveButton" class="button134" onclick="return doSaveWithValue(this);" value="save" name="task" />
												 <script type="text/javascript" >
                          							var onenter = new OnEnterSubmit('task', 'save');
                          							onenter.install();
                         						</script>
											</span>
										</td>
										<td witdh="25%" align="center">
											<span class="content">
											<% if (editContributionDetailsForm.getTheReport().getContributionData().getSystemStatus()!= null) {
												if (editContributionDetailsForm.getTheReport().getContributionData().getSystemStatus().equals("14") || editContributionDetailsForm.getTheReport().getContributionData().getSystemStatus().equals("97")) { %>
<input type="submit" id="submitButton" class="button134" onclick="return doSaveWithValue(this);" value="submit" name="task" />
												<% } else { %>
<input type="submit" id="submitButton" class="button134" onclick="return doSaveWithValue(this);" value="re-submit" name="task" />
												<% } %>
											<% } %>
											</span>
										</td>
									  </tr>
									</tbody>
								</table>
</c:if>
						</ps:form>
<c:if test="${editContributionDetailsForm.noPermission !=true}">
							<script type="text/javascript" >
							<!-- // create calendar object(s) just after form tag closed
								 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
								 // note: you can have as many calendar objects as you need for your application
var dt_start = new Date("${editContributionDetailsForm.payrollCalendarStartDate}");
var dt_end = new Date("${editContributionDetailsForm.payrollCalendarEndDate}");
var dt_defaultPayroll = new Date("${editContributionDetailsForm.payrollEffectiveDate}");
								if (dt_defaultPayroll.getYear() < 2000) {
									if (dt_defaultPayroll.getYear() > 1899) {
										dt_defaultPayroll.setYear(dt_defaultPayroll.getYear() + 100);
									} else if (dt_defaultPayroll.getYear() < 100) {
										dt_defaultPayroll.setYear(dt_defaultPayroll.getYear() + 2000);
									}		
								}	
								dt_end.setHours(23);
								dt_end.setMinutes(59);
								dt_end.setSeconds(59);
								dt_end.setMilliseconds(999);
								var payrollCal = new calendar(document.forms['editContributionDetailsForm'].elements['payrollEffectiveDate'],dt_start.valueOf(),dt_end.valueOf(),payRollValidDates,dt_defaultPayroll);
								payrollCal.year_scroll = false;
								payrollCal.time_comp = false;
							//-->
							</script>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
<c:if test="${editContributionDetailsForm.displayPaymentInstructionSection ==true}">
								<% if (editContributionDetailsForm.getAccounts() != null && editContributionDetailsForm.getAccounts().size() > 0) { %>
								<script type="text/javascript" >
								<!-- // create calendar object(s) just after form tag closed
									 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
									 // note: you can have as many calendar objects as you need for your application
var dt_start = new Date("${editContributionDetailsForm.calendarStartDate}");
var dt_end = new Date("${editContributionDetailsForm.calendarEndDate}");
var dt_default = new Date("${editContributionDetailsForm.requestEffectiveDate}");
									if (dt_default.getYear() < 2000) {
										if (dt_default.getYear() > 1899) {
											dt_default.setYear(dt_default.getYear() + 100);
										} else if (dt_default.getYear() < 100) {
											dt_default.setYear(dt_default.getYear() + 2000);
										}		
									}	
									dt_end.setHours(23);
									dt_end.setMinutes(59);
									dt_end.setSeconds(59);
									dt_end.setMilliseconds(999);
									var cal = new calendar(document.forms['editContributionDetailsForm'].elements['requestEffectiveDate'],dt_start.valueOf(),dt_end.valueOf(),validDates,dt_default);
									cal.year_scroll = false;
									cal.time_comp = false;
								//-->
								</script>
								<% } %>
</c:if>
</c:if>

					<%-- END new contribution details table --%>
					<br>
				</td>
			</tr>
			<tr>
				<td class="big">&nbsp;</td>
				<td colspan="3">&nbsp;</td>
			</tr>
	        
			<tr>
               <td height="20" width="30">&nbsp;</td>
               <td colspan="3" width="730">		 	   
					<p><content:pageFooter id="layoutPageBean"/></p>
			   </td>
 			</tr>

			<%if (!editContributionDetailsForm.isNoPermission() && editContributionDetailsForm.isDisplayPaymentInstructionSection() && editContributionDetailsForm.getAccounts() != null && editContributionDetailsForm.getAccounts().size() > 0) { %>
 			<tr>
               <td height="20" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="20"></td>
               <td width="730" colspan="3">		 	   
					<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
				</td>
 			</tr>
 			<% } %>

<c:if test="${editContributionDetailsForm.noPermission !=true}">
<c:if test="${ifileConfig.directDebitAccountPresent ==true}">
<c:if test="${editContributionDetailsForm.displayMoreSections ==true}">
             <tr>
                <td height="20">&nbsp;</td>
               <td>		 	   
			  	  	<span class="disclaimer"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/></span>
			   </td>
             </tr>
			
</c:if>
</c:if>
</c:if>
			</table>	        

<script>
	<!-- //
	initPage();
	//-->
</script>
