<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
     <%@ page import="com.manulife.pension.bd.web.BDConstants" %>   
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>



<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page
	import="com.manulife.pension.bd.web.bob.participant.ParticipantStatementSearchForm"%>
	<%@ page
	import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementItem""%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.platform.web.util.CommonEnvironment"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData" %>
<%@ page import="java.util.Collection"%>
<jsp:useBean id="ParticipantStatementSearchForm" scope="session" type="com.manulife.pension.bd.web.bob.participant.ParticipantStatementSearchForm" />



<c:set var="isReportCurrent" value="true" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	

%>




<%
ParticipantStatementsReportData theReport = (ParticipantStatementsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
Collection errorBean = (Collection)request.getAttribute(CommonEnvironment.getInstance().getErrorKey());
request.setAttribute("errorBean", errorBean);

%>


		 

			 






<input type="hidden" name="pdfCapped" /><%--  input - name="ParticipantStatementSearchForm" --%>
       
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_STMT_NO_PARTICIPANTS_ON_BASIC_SEARCH%>"
                         	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="participantBasicSearchNoParticipantMessage"/>
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_STMT_NO_PARTICIPANTS_ON_ADV_SEARCH%>"
                         	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="participantAdvancedSearchNoParticipantMessage"/>        

<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >


var scrollingTable;
if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
}
</script>
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
<p><content:getAttribute id="layoutPageBean" attribute="body1" /></p>

<%--#Error- message box--%>
<report:formatMessages scope="request" />

<%-- Navigation link--%>
<navigation:contractReportsTab />
<div class="page_section_subheader controls"><%-- Start of quick filter form--%>
	<form name="quickFilterForm" 
		  method="post"
		  action="/do/bob/participant/participantStatementSearch/"
		  class="page_section_filter form" style="width: 75%">
<input type="hidden" name="task" value="filter"/>
<form:hidden path="ParticipantStatementSearchForm.showCustomizeFilter" id="showCustomizeFilter"value="${ParticipantStatementSearchForm.showCustomizeFilter}"/>
				<p><label for="participant_Filter">Search by: </label></p>
		<bd:select name="ParticipantStatementSearchForm"
				   property="participantFilter"
				   onchange="setFilterFromSelect(this);selectQuickFilter();">
			<bd:option value="blank_val">&nbsp;</bd:option>
			<bd:option value="last_name">Last Name</bd:option>
			<bd:option value="first_name">First Name</bd:option>
			<bd:option value="ssn">SSN</bd:option>
		</bd:select>
		<p><label for="participant_search">Looking for: </label></p>
		<div id="div_namePhrase">
 <form:input path="ParticipantStatementSearchForm.quickFilterNamePhrase"  maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></div> 





		<div id="div_firstName">
 <form:input path="ParticipantStatementSearchForm.quickFilterFirstName" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></div> 





		<div id="div_ssn">
 <form:password path="ParticipantStatementSearchForm.quickFilterSsnOne" value="${ParticipantStatementSearchForm.quickFilterSsnOne}" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="3" readonly="false" size="3" /> 






 <form:password path="ParticipantStatementSearchForm.quickFilterSsnTwo" value="${ParticipantStatementSearchForm.quickFilterSsnTwo}" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="2" readonly="false" size="2" /> 






 <form:input path="ParticipantStatementSearchForm.quickFilterSsnThree" maxlength="4" onchange="setFilterFromInput(this);" readonly="false" size="4" cssClass="inputField" /> 






		</div>
	</form>
	
	<div class="button_header" style="margin-left : 1px; margin-top: 15px; float:right; margin-right : 5px; "><span><input id="customize_report" type="button" value="Advanced Search" style="margin-left : 0px;width:110px;" onclick="openCustomizeFilter()"/></span> </div>
	<div id="quickFilterSubmitDiv" class="button_header" style="margin:0px; margin-left : 1px; margin-right : 4px; margin-top: 15px; float:right;"><span><input id="quick_report" type="button" value="Search" style="margin-left : 0px;width:50px;" onclick="submitData('quickFilter')"/></span> </div>

</div>
<%-- End of quick filter --%>
<div id="participant_summary_report"><%-- Start of Customization filter --%>
		<div id="report_customization_wrapper">
			
			<form id="report_customization" 
				  name="reportCustomizationForm"
				  style="margin-bottom:0;" method="post"
				  action="/do/bob/participant/participantStatementSearch/">
				<table width="100%">
					<tr>
						<td align="right">
						   Last&nbsp;Name:
						</td>
						<td align="left">
 <form:input path="ParticipantStatementSearchForm.namePhrase" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /> 





						</td>
						<td align="right">
							First&nbsp;Name:
						</td>
						<td align="left">
 <form:input path="ParticipantStatementSearchForm.firstName" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /> 





						</td>
						<td align="right">
							SSN:
						</td>
						<td align="left">
 <form:password path="ParticipantStatementSearchForm.ssnOne" value="${ParticipantStatementSearchForm.ssnOne}" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="3" readonly="false" size="3" /> 

 <form:password path="ParticipantStatementSearchForm.ssnTwo" value="${ParticipantStatementSearchForm.ssnTwo}" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="2" readonly="false" size="2" /> 

 <form:input path="ParticipantStatementSearchForm.ssnThree" maxlength="4" onchange="setFilterFromInput(this);" readonly="false" size="4" cssClass="inputField" /> 


						</td>
						<td width="120px">
						&nbsp;
						</td>
					</tr>
					</table>
					<br/>
				<div style = "float:right;padding-right: 130pt;"class="selection_input">
				   <div class="button_search">
						<input type="button" 
							   value="Reset"
							   id="cancel_customization" 
							   onclick="doCancel()" /></div>
					<div class="button_search">
						<input type="button" 
							   value="Submit"
							   id="apply_customization" 
							   onclick="submitData('customizeFilter')" /></div>
				</div>
			</form>
		</div>
		<%-- End of Customization filter --%>
		<div class="clear_footer"></div>
</div>
<%-- Start of report table filter --%>
<bd:form method="post" 
		 cssClass="margin-top:0;"
		 action="/do/bob/participant/participantStatementSearch/" modelAttribute="ParticipantStatementSearchForm" name="ParticipantStatementSearchForm">
	<div class="report_table"  >
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong> <report:recordCounter 
						report="theReport" label="Participants" /> </strong></div>
					<div class="table_pagination"><report:pageCounter formName="ParticipantStatementSearchForm"
						report="theReport" arrowColor="black" /></div>
					<div class="table_controls_footer"></div>
				</div>
</c:if>
		</c:if>
		<%-- table_controls --%>
		<div class="table_controls_footer"></div>
		
		<div id="scrollingTable" style="width:918px;Overflow-x:auto;Overflow-y:hidden">
		<table class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<th rowspan="1" class="val_str">
						<report:sort  formName="ParticipantStatementSearchForm" field="PARTICIPANT_LAST_NAME" direction="asc">Last Name</report:sort> 
					</th>
				    <th rowspan="1" class="val_str">
						<report:sort field="PARTICIPANT_FIRST_NAME"   formName="ParticipantStatementSearchForm"
									 direction="asc">First Name</report:sort>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
					<th rowspan="1" class="val_str align_center">
						<report:sort field="SOCIAL_SECURITY_NO"   formName="ParticipantStatementSearchForm"
									 direction="asc">Social Security Number</report:sort></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>
	<%String temp = pageContext.getAttribute("indexValue").toString();
	 
			 ParticipantStatementItem theItem = (ParticipantStatementItem)pageContext.getAttribute("theItem");
	%>
						<%
								String rowClass = "spec";
									if (Integer.parseInt(temp)  % 2 == 1) {
										rowClass = "";
									} else {
										rowClass = "spec";
									}
						%>
						<tr class="<%=rowClass%>">
							<td style='width: 300px'>
									<bd:link
										action='<%="/do/bob/participant/participantStatementResults/?task=fetchStatements&profileId="
					                                            + theItem.getProfileId()%>'>
					                                            ${theItem.lastName}
									</bd:link>
							</td>
							
				            <!-- Division -->
						    
<td style='width: 300px' class="name" nowrap="nowrap">${theItem.firstName} </td>
							
							<td style='width: 300px' class="date align_center">
								<render:ssn property="theItem.ssn" />
							</td>
						</tr>
</c:forEach>
				</c:if>
			</tbody>
		</table>
		</div>
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Participants" /></strong></div>
					<div class="table_pagination"><report:pageCounter formName="ParticipantStatementSearchForm"
						report="theReport" arrowColor="black" /></div>
					<div class="table_controls_footer"></div>
				</div>
</c:if>
		</c:if>
		<c:if test="${empty errorBean}">
			<c:if test="${not empty theReport.totalCount}">
<c:if test="${empty theReport.details}">
				  <div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
					      
<c:if test="${ParticipantStatementSearchForm.showCustomizeFilter =='N'}">
					     	<dd>1.&nbsp;<content:getAttribute id="participantBasicSearchNoParticipantMessage" attribute="text"/></dd>
</c:if>
					     
<c:if test="${ParticipantStatementSearchForm.showCustomizeFilter =='Y'}">
					     	<dd>1.&nbsp;<content:getAttribute id="participantAdvancedSearchNoParticipantMessage" attribute="text"/></dd>
</c:if>
					     
					    </dl>
				  </div>
</c:if> <%-- report table controls --%>
			</c:if>
</c:if>
		
	</div>
</bd:form>
<layout:pageFooter/>
