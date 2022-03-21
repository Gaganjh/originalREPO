<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.iloans.IloansHelper"%>
<%@ page import="com.manulife.pension.ps.service.report.iloans.valueobject.LoanRequestReportData"%> 
<script language="JavaScript1.2" type="text/javascript">
<!--
	var submitted = false;

	function setButtonAndSubmit(button) {

	}

	-->
</script>
<%
	boolean errors = false;
%>
<%
LoanRequestReportData theReport = (LoanRequestReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>

<%
	String iloansStatusCodeReview = (String)session.getAttribute(Constants.ILOANS_STATUS_CODE_REVIEW);
	pageContext.setAttribute("iloansStatusCodeReview",iloansStatusCodeReview,PageContext.PAGE_SCOPE);
	
	String iloansStatusCodePending = (String)session.getAttribute(Constants.ILOANS_STATUS_CODE_PENDING);
	pageContext.setAttribute("iloansStatusCodePending",iloansStatusCodePending,PageContext.PAGE_SCOPE);
	
	String iloansStatusCodeApproved = (String)session.getAttribute(Constants.ILOANS_STATUS_CODE_APPROVED);
	pageContext.setAttribute("iloansStatusCodeApproved",iloansStatusCodeApproved,PageContext.PAGE_SCOPE);
	
	String iloansStatusCodeDenied = (String)session.getAttribute(Constants.ILOANS_STATUS_CODE_DENIED);
	pageContext.setAttribute("iloansStatusCodeDenied",iloansStatusCodeDenied,PageContext.PAGE_SCOPE);

%>


<%@ page
	import="com.manulife.pension.ps.web.iloans.ViewLoanRequestsForm"%>
<content:getAttribute beanName="layoutPageBean" attribute="body1" />
<br>
<P>
	<strong>Legend: <img
		src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12">
	</strong>Edit <img src="/assets/unmanaged/images/view_icon.gif" width="12"
		height="12"> View
</P>
<p>
	<c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="true" scope="page" />

		<div id="errordivcs">
			<content:errors scope="session" />
		</div>
		<%
			errors = true;
		%>
	</c:if>
</p>
<table width="715" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="25">
			<!--DWLayoutEmptyCell-->&nbsp;
		</td>
		<td width="130"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="75"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="100"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="100"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="105"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="100"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="68"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
		<td width="4"><img src="/assets/unmanaged/images/spacer.gif"
			width="4" height="1" border="0" /></td>
		<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
			width="1" height="1" border="0" /></td>
	</tr>

	<ps:form method="POST" modelAttribute="viewLoanRequestsForm" name="viewLoanRequestsForm" action="/do/iloans/viewLoanRequests/">

		

		<tr class="tablehead">
			<td class="tableheadTD1" colspan="9"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body2Header" /></b></td>
			<td colspan="5" class="tableheadTD">
				<!--DWLayoutEmptyCell-->&nbsp;
			</td>
			<td colspan="3" align="right" class="tableheadTDinfo"><report:pageCounter
					report="theReport" /></td>
		</tr>

		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td valign="top" class="tablesubhead">
				<!--DWLayoutEmptyCell-->&nbsp;
			</td>
			<td valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="contractName" direction="desc">Contract
	name </report:sort></strong></td>
			<td width="1" align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="contractNumber" direction="desc">Contract
      number </report:sort></strong></td>
			<td align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td align="left" valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="participantName" direction="desc">Participant
      name </report:sort></strong></td>
			<td align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td align="left" valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="ssn" direction="desc">SSN</report:sort></strong></td>
			<td align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td align="left" valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="requestDate" direction="desc">Date
      of request </report:sort></strong></td>
			<td align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td align="left" valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="status" direction="desc">Status</report:sort></strong></td>
			<td align="left" valign="top" class="dataheaddivider"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
				border="0" /></td>
			<td colspan="2" align="left" valign="top" class="tablesubhead"><strong><report:sort formName="viewLoanRequestsForm"
						field="initiatedBy" direction="desc">Initiated by</report:sort></strong></td>
			<td width="1" valign="top" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
		</tr>


		<%
			String rowClass = "datacell1";
		%>
		<%
			String dataDivider = "dataheaddivider";
		%>
<c:if test="${empty theReport.details}">
			<tr class="datacell1">
				<td class="databorder"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				<td colspan="15" class="<%=rowClass%>">There are no unexpired
					i:loans requests.</td>
				<td class="databorder"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
</c:if>
<c:forEach items="${theReport.details}" var="theLoanRequest" varStatus="theLoanRequestsIndex" >
<c:set var="theLoanRequestsIndex" value="${theLoanRequestsIndex.index}"/> 
<%
String temp = pageContext.getAttribute("theLoanRequestsIndex").toString();
%>


			<%
				if (Integer.parseInt(temp) % 2 == 0) {
							rowClass = "datacell1";
							dataDivider = "datadivider";
						} else {
							rowClass = "datacell2";
							dataDivider = "datadivider";
						}
			%>

			<tr class="<%=rowClass%>">
				<td class="databorder"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				<td align="center"><ps:map id="parameterMap">
						<ps:param name="iloansProfileId" valueBeanName="theLoanRequest"
							valueBeanProperty="profileId" />
						<ps:param name="iloansContractNumber"
							valueBeanName="theLoanRequest" valueBeanProperty="contractNumber" />
						<ps:param name="iloansRequestId" valueBeanName="theLoanRequest"
							valueBeanProperty="loanRequestId" />
						<ps:param name="viewAndEdit" valueBeanName="theLoanRequest"
							valueBeanProperty="loanRequestStatusCode" />

</ps:map> <c:if test="${theLoanRequest.loanRequestStatusCode ==iloansStatusCodeReview}">


						<ps:link action="/iloans/viewLoanRequests/" name="parameterMap">
							<img src="/assets/unmanaged/images/edit_icon.gif" width="12"
								height="12" border="0">
						</ps:link>
</c:if> <c:if test="${theLoanRequest.loanRequestStatusCode ==iloansStatusCodePending}" >


						<ps:link action="/iloans/viewLoanRequests/" name="parameterMap">
							<img src="/assets/unmanaged/images/edit_icon.gif" width="12"
								height="12" border="0">
						</ps:link>
</c:if> <c:if test="${theLoanRequest.loanRequestStatusCode ==iloansStatusCodeApproved}" >


						<ps:link action="/iloans/viewLoanRequests/" name="parameterMap">
							<img src="/assets/unmanaged/images/view_icon.gif" width="12"
								height="12" border="0">
						</ps:link>
</c:if> <c:if test="${theLoanRequest.loanRequestStatusCode ==iloansStatusCodeDenied}">


						<ps:link action="/iloans/viewLoanRequests/" name="parameterMap">
							<img src="/assets/unmanaged/images/view_icon.gif" width="12"
								height="12" border="0">
						</ps:link>
</c:if></td>
<td align="left">${theLoanRequest.contractName} <%-- filter="false" --%></td>

				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
<td align="left">${theLoanRequest.contractNumber}</td>

				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
<td align="left" nowrap="nowrap" class="<%=rowClass%>">${theLoanRequest.participantName}</td>

				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
				<td align="left" nowrap="nowrap"><render:ssn
						property="theLoanRequest.participantSSN" /></td>
				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
				<td align="left" class="<%=rowClass%>"><render:date
						property="theLoanRequest.reqDate" defaultValue=""
						patternOut="MM/dd/yyyy" /></td>
				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
<td align="left">${theLoanRequest.requestStatus}</td>

				<td align="left" class="<%=dataDivider%>"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1"
					border="0" /></td>
<td colspan="2" align="left">${theLoanRequest.initiatedBy}</td>

				<td valign="top" class="databorder"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
			</tr>

</c:forEach>

		<%
			String emptyRowColor = "white";
				if (rowClass.equals("datacell1")) {
					emptyRowColor = "white";
				} else {
					emptyRowColor = "beige";
				}
		%>



		<ps:roundedCorner numberOfColumns="17"
			emptyRowColor="<%=emptyRowColor%>" />

		<tr>

			<%
				if (errors == false) {
			%>
			<td colspan="16" align="right"><report:pageCounter
					report="theReport" arrowColor="black" /></td>
			<td width="1"><img src="/assets/unmanaged/images/spacer.gif"
				width="1" height="1" border="0" /></td>
			<%
				}
			%>
		</tr>
		<tr>
			<td colspan="16" align="right">&nbsp;</td>
		</tr>
		<c:if test="${empty param.printFriendly }" >
			<tr>
<td colspan="16" align="right"><input type="submit" class="button100Lg" name="actionInitiate" value="create i:loan" /></td>


			</tr>
		</c:if>
		<tr>
			<td colspan="17"><br>
				<p>
					<content:pageFooter beanName="layoutPageBean" />
				</p>
				<p class="footnote">
					<content:pageFootnotes beanName="layoutPageBean" />
				</p>
				<p class="disclaimer">
					<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
				</p></td>
		</tr>

	</ps:form>
</table>
<!--
<script>
	setFocusOnFirstInputField("viewLoanRequestsForm");
</script>
-->
<br>
