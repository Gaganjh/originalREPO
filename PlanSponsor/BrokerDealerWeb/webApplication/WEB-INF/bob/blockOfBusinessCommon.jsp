<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm"%>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentProperties" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentCacheConstants" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="blockOfBusinessForm" scope="session" type="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm" />




<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);


BlockOfBusinessReportData theReport =(BlockOfBusinessReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>





<c:if test="${not empty theReport}">

	
	<%-- Summary Information --%>
	<div id="summaryBox">
		<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
		<c:if test="${blockOfBusinessForm.internalUserAndNotInMimickModeInd}">
			User Name: <strong>${blockOfBusinessForm.internalUserName}</strong><br/>
		</c:if>
		<c:if test="${blockOfBusinessForm.firmRepUserInd}">
			<table> 
				<tr>
					<td valign="top" style="padding-top:4px" nowrap="nowrap">Firm Name(s):</td>
					<td>
						<table>
							<c:forEach var="firmName" items="${blockOfBusinessForm.associatedFirmNames}">
								<tr><td style="padding-top:0px"><strong>${firmName}</strong></td></tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${(not blockOfBusinessForm.internalUserAndNotInMimickModeInd) and (not blockOfBusinessForm.firmRepUserInd)}">
			Financial Rep: <strong>${blockOfBusinessForm.financialRepUserName}</strong><br/><br/>

			<table> <tr> <td>Producer Code</td><td>	Firm Name</td></tr>
				<c:forEach var="brokerInfo" items="${theReport.bobSummaryVO.brokerInfoVO}">
					<tr>
						<td><strong>${brokerInfo.producerCode}</strong></td>
						<td><strong>${brokerInfo.bdFirmName}</strong></td>
					</tr>	
				</c:forEach>
			</table>
		</c:if>
		<br />
		
		Active Contract Assets**:
		<strong>
			<c:if test="${!theReport.resultTooBigInd}">
				<report:number property="theReport.bobSummaryVO.activeContractAssets" type="c"/>
			</c:if>
			<c:if test="${theReport.resultTooBigInd}">
				-
			</c:if>
		</strong><br />
		Number of Active Contracts: 
		<strong><report:number property="theReport.bobSummaryVO.numOfActiveContracts" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></strong><br />
		Number of Participants: 
		 <strong>
			<c:if test="${!theReport.resultTooBigInd}">
				<c:if test="${empty theReport.bobSummaryVO.numOfLives}">
					-
				</c:if>
				<c:if test="${not empty theReport.bobSummaryVO.numOfLives}">
					<report:number property="theReport.bobSummaryVO.numOfLives" pattern="<%=BDConstants.NUMBER_9_DIGITS%>"/>
				</c:if>
			</c:if>
			<c:if test="${theReport.resultTooBigInd}">
				-
			</c:if>
		</strong><br />
		
		<br />
		<c:if test="${not blockOfBusinessForm.showPNAndPPContractCountAsOfLatestDateFootnote}">
			Number of Outstanding Proposals: 
		</c:if>
		<c:if test="${blockOfBusinessForm.showPNAndPPContractCountAsOfLatestDateFootnote}">
			Number of Outstanding Proposals^:		
		</c:if>
		<strong><report:number property="theReport.bobSummaryVO.numOfOutstandingProposals" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></strong><br />
		<c:if test="${not blockOfBusinessForm.showPNAndPPContractCountAsOfLatestDateFootnote}">
			Number of Pending Contracts:		
		</c:if>
		<c:if test="${blockOfBusinessForm.showPNAndPPContractCountAsOfLatestDateFootnote}">
			Number of Pending Contracts^:		
		</c:if>
		<strong><report:number property="theReport.bobSummaryVO.numOfPendingContracts" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></strong>	</div>
</c:if>

<%-- Display Intro1/Intro2 test --%>
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>

<%
	String errorKey = ContentProperties.getInstance().getProperty(ContentCacheConstants.ERROR_KEY);

	// Saving the info, warning, error message collections into variables.
	ArrayList<GenericException> infoMsgColl = (ArrayList<GenericException>) request.getAttribute(BDConstants.INFO_MESSAGES);
	ArrayList<GenericException> warningMsgColl = (ArrayList<GenericException>)request.getAttribute(BDConstants.WARNING_MESSAGES);
	ArrayList<GenericException> errorMsgColl = (ArrayList<GenericException>) request.getAttribute(errorKey);
	
	// Check if there is any info message to be displayed above column headers.
	ArrayList<GenericException> infoMsgDisplayedAboveColHeader = (ArrayList<GenericException>) 
		request.getAttribute(BDConstants.INFO_MSG_DISPLAY_ABOVE_COLUMN_HEADER);
	
	if (infoMsgDisplayedAboveColHeader != null && !infoMsgDisplayedAboveColHeader.isEmpty()) {
		request.setAttribute(BDConstants.INFO_MESSAGES, infoMsgDisplayedAboveColHeader);
	}

	// Making the warning message, error messages null so that they do not get displayed here.
	request.setAttribute(BDConstants.WARNING_MESSAGES, null);
	request.setAttribute(errorKey, null);
%>

<report:formatMessages scope="request"/>

<%
	// Putting the original info, warning, error message collections back into the request.
	request.setAttribute(BDConstants.INFO_MESSAGES, infoMsgColl);
	request.setAttribute(BDConstants.WARNING_MESSAGES, warningMsgColl);
	request.setAttribute(errorKey, errorMsgColl);
%>

<%--Layout/intro1--%>
<c:if test="${not empty layoutPageBean.introduction1}">
   	<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

<%--Layout/Intro2--%>
<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
<div class="table_controls_footer"></div>

<report:formatMessages scope="request"/>

<input type="hidden" name="pdfCapped"
value="${blockOfBusinessForm.pdfCapped}" />

<%-- The below image is being loaded so that when a user clicks on the Submit button in BOB page,
the image shows up. Otherwise, the Image will not show up.--%>
<img style="display:none" src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>
