<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>



<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page
	import="com.manulife.pension.bd.web.bob.participant.ParticipantSummaryReportForm"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData" %>
<%@ page import="com.manulife.pension.bd.web.bob.participant.ParticipantSummaryReportForm" %>



<c:set var="isReportCurrent" value="true" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<%
ParticipantSummaryReportForm participantSummaryReportForm=(ParticipantSummaryReportForm)request.getAttribute("participantSummaryReportForm");
pageContext.setAttribute("participantSummaryReportForm",participantSummaryReportForm,PageContext.PAGE_SCOPE);

%>


<%
ParticipantSummaryReportData theReport = (ParticipantSummaryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>



<form:hidden path="participantSummaryReportForm.pdfCapped"/>



<content:contentBean contentId="<%=BDContentConstants.MESSAGE_PARTICIPANTS_VIEW_ALL%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
					 id="viewAllParticipants" />

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="noSearchResults" />

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_SEARCH_FOR_PARTICIPANTS%>"
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
					 id="searchForParticipants" />

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
        			 type="${contentConstants.TYPE_MISCELLANEOUS}"
			         id="csvIcon"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
        			 type="${contentConstants.TYPE_MISCELLANEOUS}"
			         id="pdfIcon"/>	

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_ROTH_INFO%>" 
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="rothInfo"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<%
	boolean hasRoth = false;
	if (bobContext != null) {
			hasRoth = bobContext.getContractProfile().getContract().hasRothNoExpiryCheck();
	}
%>

<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>
<%--End of Roth Message--%>                      			         				 

<c:if test="${participantSummaryReportForm.asOfDateCurrent == false}">


	<c:set var="isReportCurrent" value="false" />
</c:if>

<script type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >

var fixedTable;
var scrollingTable;
if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
<c:if test="${not empty theReport.details}">
  	scrollingTable = document.getElementById("scrollingTable");
  	fixedTable = document.getElementById("fixedTable");

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	fixedTable.style.overflow='-moz-scrollbars-none'; 
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
  	fixedTable.style.visibility = 'visible';
</c:if>
}

function tooltip(DefInvesValue)
	{
		if(DefInvesValue != null)
		{
			if(DefInvesValue == "TR")
			Tip('Instructions were provided by Trustee - Mapped');
			else if(DefInvesValue == "PR")
			Tip('Instructions prorated - participant instructions incomplete / incorrect');
			else if(DefInvesValue == "PA")
			Tip('Participant Provided');
			else if(DefInvesValue == "DF")
			Tip('Default investment option was used');
			else if(DefInvesValue == "MA")
			Tip('Managed Accounts');
			else			
			UnTip();
			
		}
		else
		{
			UnTip();
		}
	}
</script>
<%-- Start of Summary Box--%>
<c:if test="${not empty theReport.participantSummaryTotals}">
	<div id="summaryBox" style="width: 350px;">
		<h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td colspan="3" nowrap="nowrap">
					<div align="center"><span class="name">Total
Participants: <strong>${theReport.participantSummaryTotals.totalParticipants}</strong></span></div>
				</td>
			</tr>
			<tr>
				<td width="30%" nowrap="nowrap">&nbsp;</td>
				<td width="35%">
					<div align="right"><span class="name">Total</span></div>
				</td>
				<td width="35%">
					<div align="right"><span class="name">Average </span></div>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap"><span class="name">Employee Assets:</span></td>
				<td>
					<div align="right"><span class="name"> <strong>
					<report:number defaultValue="0.00"
							property="theReport.participantSummaryTotals.employeeAssetsTotal"
							type="c" /> </strong> </span></div>
				</td>
				<td>
					<div align="right"><span class="name"><strong><report:number
						defaultValue="0.00"
						property="theReport.participantSummaryTotals.employeeAssetsAverage"
						type="c" /></strong></span></div>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap"><span class="name">Employer
				Assets:</span></td>
				<td>
					<div align="right"><span class="name"><strong> <report:number
						defaultValue="0.00"
						property="theReport.participantSummaryTotals.employerAssetsTotal"
						type="c" /> </strong></span></div>
				</td>
				<td>
					<div align="right"><span class="name"><strong> <report:number
						defaultValue="0.00"
						property="theReport.participantSummaryTotals.employerAssetsAverage"
						type="c" /> </strong></span></div>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">
					<span class="name">Total Assets:</span></td>
				<td>
					<div align="right"><span class="name"><strong> <report:number
						defaultValue="0.00"
						property="theReport.participantSummaryTotals.totalAssets" type="c" />
					</strong></span></div>
				</td>
				<td>
					<div align="right"><strong> <report:number
						defaultValue="0.00"
						property="theReport.participantSummaryTotals.totalAssetsAverage"
						type="c" /> </strong></div>
				</td>
			</tr>
<c:if test="${participantSummaryReportForm.hasLoansFeature  == true}">

				<tr>
					<td align="right" nowrap="nowrap">
						<span class="name">Outstanding Loans:</span></td>
					<td>
						<div align="right"><span class="name"> <strong>
						<report:number defaultValue="0.00"
							property="theReport.participantSummaryTotals.outstandingLoans"
							type="c" /> </strong></span></div>
					</td>
					<td>
						<div align="right"><span class="name"><strong>
						<report:number defaultValue="0.00"
							property="theReport.participantSummaryTotals.outstandingLoansAverage"
							type="c" /> </strong></span></div>
					</td>
				</tr>
</c:if>
		</table>
	</div>
</c:if>
<%-- End of Summary Box--%>
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
<p><content:getAttribute id="layoutPageBean" attribute="body1" /></p>
<% if (hasRoth) { 	%>
<p><content:getAttribute  attribute="text" beanName="rothInfo"/></p>
<% } %>

<%--#Error- message box--%>
<report:formatMessages scope="request" />

<%-- Navigation link--%>
<navigation:contractReportsTab />
<div class="page_section_subheader controls"><%-- Start of quick filter form--%>
	<form name="quickFilterForm" 
		  method="post"
		  action="/do/bob/participant/participantSummary/"
		  class="page_section_filter form">
		  <input type="hidden" name="task" value="filter"/>
		  <form:hidden path="participantSummaryReportForm.resetGateway" value=""/>
		 <form:hidden path="participantSummaryReportForm.gatewayChecked" value=""/>
		   <form:hidden path="participantSummaryReportForm.resetManagedAccount" value=""/>
		  <form:hidden path="participantSummaryReportForm.managedAccountChecked" value=""/>
		 
		  <input type="hidden" name="frmGatewayInd" value="${String.valueOf(participantSummaryReportForm.getHasContractGatewayInd())}"/>
		    <input type="hidden" name="frmManagedAccountInd" value="${String.valueOf(participantSummaryReportForm.getHasManagedAccountInd())}"/>
<form:hidden path="participantSummaryReportForm.showCustomizeFilter" id="showCustomizeFilter" value="${participantSummaryReportForm.showCustomizeFilter}"/>


		<p><label for="investment_allocation_filter">As of: </label></p>
		<form:hidden path="participantSummaryReportForm.baseAsOfDate"/>
		<bd:select name="participantSummaryReportForm" property="asOfDate"
			onchange="setFilterFromSelect(this);disableOrSuppressGifl();disableOrSuppressMA();"
			tabindex="10">
			<bd:dateOption name="<%=BDConstants.BOBCONTEXT_KEY%>"
				property="currentContract.contractDates.asOfDate"
				renderStyle="<%=RenderConstants.LONG_MDY%>" />
			<bd:dateOptions name="<%=BDConstants.BOBCONTEXT_KEY%>"
				property="currentContract.contractDates.monthEndDates"
				renderStyle="<%=RenderConstants.LONG_MDY%>" />
		</bd:select>
		<p><label for="participant_Filter">Search by: </label></p>
		<bd:select name="participantSummaryReportForm"
				   property="participantFilter"
				   onchange="setFilterFromSelect(this);selectQuickFilter();">
			<bd:option value="blank_val">&nbsp;</bd:option>
			<bd:option value="last_name">Last Name</bd:option>
<c:if test="${participantSummaryReportForm.showDivision == true}">
			<bd:option value="division">Division</bd:option>
</c:if>
			<bd:option value="total_assets">Total Assets</bd:option>
			<bd:option value="cont_status">Contribution Status</bd:option>
			<bd:option value="emp_status">Employment Status</bd:option>			<%-- CL 110234 --%>
<c:if test="${participantSummaryReportForm.hasContractGatewayInd == true}">

				<bd:option value="gifl">Guaranteed Income Feature</bd:option>
</c:if>
			<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">

				<bd:option value="ma">Managed Accounts</bd:option>
			</c:if>
		</bd:select>                                                                     
		<p><label for="participant_search">Looking for:&nbsp;</label></p>
		<div id="div_namePhrase">
<form:input path="participantSummaryReportForm.quickFilterNamePhrase" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></div>





		<div id="div_division">
<form:input path="participantSummaryReportForm.quickFilterDivision" maxlength="25" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></div>





		<div id="div_totalAssets">
			<p><label for="investment_allocation_filter">from $</label></p>
<form:input path="participantSummaryReportForm.quickTotalAssetsFrom" maxlength="13" onchange="setFilterFromInput(this);" readonly="false" cssStyle="width: 65px;" cssClass="inputField" />





			<p><label for="investment_allocation_filter">to $</label></p>
<form:input path="participantSummaryReportForm.quickTotalAssetsTo" maxlength="13" onchange="setFilterFromInput(this);" readonly="false" cssStyle="width: 65px;" cssClass="inputField" /></div>





		<div id="div_status">
<form:select path="participantSummaryReportForm.quickFilterStatus" disabled="false" onchange="setFilterFromSelect2(this);" >



			<%-- set the first value of the select --%>
			<form:option value="All">All</form:option>
			<form:option value="Active" />Active
			<c:if test="${bobContext.currentContract.hasEmployerMoneyTypeOnly == false}">


				<form:option value="Active no balance"/>Active no balance
				<form:option value="Active non contributing"/>Active non contributing
				<form:option value="Active opted out"/>Active opted out
</c:if>
			<form:option value="Inactive not vested"/>Inactive not vested
			<form:option value="Inactive with balance"/>Inactive with balance
<c:if test="${bobContext.currentContract.hasEmployerMoneyTypeOnly == false}">


				<form:option value="Opted out not vested"/>Opted out not vested
</c:if>
</form:select></div>
		<%-- CL 110234 Begin --%>
		<div id="div_employmentstatus">
<form:select path="participantSummaryReportForm.quickFilterEmploymentStatus" disabled="false" onchange="setFilterFromSelect2(this);" >



							<%-- set the first value of the select --%>
							<form:option value="All">All</form:option>
<form:options items="${participantSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select></div>
		<%-- CL 110234 End --%>					
<c:if test="${participantSummaryReportForm.hasContractGatewayInd == true}">


			<div id="div_gateway">
			<form:checkbox path="participantSummaryReportForm.quickFilterGatewayChecked" onclick="setQuickFilterGatewayOption(this);" disabled="false" value="true" />
			<p><label for="participant_search">Guaranteed Income Feature:</label></p></div>
</c:if>
		<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">
			<div id="div_managedAccount">
				<form:checkbox
					path="participantSummaryReportForm.quickFilterManagedAccount"
					onclick="setQuickFilterManagedAccountOption(this);"
					disabled="false" value="true" />
				<p>
					<label for="participant_search">Managed Accounts</label>
				</p>
			</div>
		</c:if>

	</form>
	
<c:if test="${empty requestScope.isError}">	
	<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
</c:if>

	<div class="button_header" style="margin-left : 1px; margin-top: 15px; float:right; margin-right : 5px; "><span><input id="customize_report" type="button" value="Advanced Search" style="margin-left : 0px;width:110px;" onclick="openCustomizeFilter()"/></span> </div>
	<div id="quickFilterSubmitDiv" class="button_header" style="margin:0px; margin-left : 1px; margin-right : 4px; margin-top: 15px; float:right;"><span><input id="quick_report" type="button" value="Search" style="margin-left : 0px;width:50px;" onclick="submitData('quickFilter')"/></span> </div>

</div>
<%-- End of quick filter --%>
<div id="participant_summary_report"><%-- Start of Customization filter --%>
		<div id="report_customization_wrapper">
			
			<form id="report_customization" 
				  name="reportCustomizationForm"
				  style="margin-bottom:0;" method="post"
				  action="/do/bob/participant/participantSummary/">
				<table width="100%">
					<tr>
						<td>
						   Last&nbsp;Name:
						</td>
						<td align="left">
<form:input path="participantSummaryReportForm.namePhrase" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" />





						</td>
						<td>
<c:if test="${participantSummaryReportForm.showDivision == true}">
								Division:
</c:if>
						</td>
						<td>
<c:if test="${participantSummaryReportForm.showDivision == true}">
<form:input path="participantSummaryReportForm.division" maxlength="25" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" />





</c:if>
						</td>
					<c:if test="${participantSummaryReportForm.showDivision != true}">
						<td align="right" style="position: relative; padding-left: 329px;"><div
								id="div_show_employment_status">Employment Status:</div></td>
					</c:if>
					<c:if test="${participantSummaryReportForm.showDivision}">
						<td align="right" style="padding-left: 105px;"><div
								id="div_show_employment_status">Employment Status:</div></td>
					</c:if>

					<td align="left">
							<div id="div_show_employment_status_option">
<form:select path="participantSummaryReportForm.employmentStatus" disabled="false" onchange="setFilterFromSelect2(this);" cssStyle="width: 155px;">




								<%-- set the first value of the select --%>
								<form:option value="All">All</form:option>
<form:options items="${participantSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/>/>
</form:select>
							</div>
						</td>
					</tr>
					</table>
					<table>
					<tr>
						<td>Total&nbsp;Assets:&nbsp;&nbsp;from&nbsp;$</td>
						<td>
<form:input path="participantSummaryReportForm.totalAssetsFrom" maxlength="13" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></td>





						<td>to&nbsp;$</td>
						<td>
<form:input path="participantSummaryReportForm.totalAssetsTo" maxlength="13" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" /></td>





						<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							
							<c:if test="${isIE eq false}">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</c:if>

							<c:if test="${participantSummaryReportForm.showDivision eq true && 
									participantSummaryReportForm.hasContractGatewayInd eq true &&
									isIE eq false}">							
							&nbsp;&nbsp;
							</c:if>							
						</td>
									   
						<td align="right"> <div id="div_show_status">Contribution&nbsp;Status:</div></td>
						<td align="left">
							<div id="div_show_status_option">
<form:select path="participantSummaryReportForm.status" disabled="false" cssClass="width: 155px;"onchange="setFilterFromSelect2(this);" style="width: 155px;"><%-- property="status" style="width: 155px;" --%>




								<%-- set the first value of the select --%>
								<form:option value="All"/>All
								<form:option value="Active"/>Active
<c:if test="${bobContext.currentContract.hasEmployerMoneyTypeOnly == false}">


									<form:option value="Active no balance"/>Active no balance
									<form:option value="Active non contributing"/>Active non contributing
									<form:option value="Active opted out"/>Active opted out
</c:if>
								<form:option value="Inactive not vested"/>Inactive not vested
								<form:option value="Inactive with balance"/>Inactive with balance
<c:if test="${bobContext.currentContract.hasEmployerMoneyTypeOnly == false}">


									<form:option value="Opted out not vested"/>Opted out not vested
</c:if>
</form:select></div></td>
					</tr>
				</table>
			<table>
				<tr>
					<c:if test="${participantSummaryReportForm.hasContractGatewayInd}">
						<td align="right"><form:checkbox
								path="participantSummaryReportForm.gatewayChecked"
								onclick="setGatewayOption(this);" disabled="false" value="true" />
						</td>
						<td align="left"><label for="participant_search">Guaranteed
								Income Feature</label></td>
					</c:if>
					<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">
						<td align="right"><form:checkbox
								path="participantSummaryReportForm.managedAccountChecked"
								onclick="setManagedAccountOption(this);" disabled="false"
								value="true" /></td>
						<td align="left"><label for="participant_search">Managed
								Accounts </label></td>
					</c:if>
				</tr>

			</table>
			<div class="selection_input">
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
		 action="/do/bob/participant/participantSummary/" modelAttribute="participantSummaryReportForm">
	<div class="report_table">
		<c:if test="${not empty theReport }">
<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Participants" /></strong></div>
					<div class="table_pagination"><report:pageCounter formName="participantSummaryReportForm"
						report="theReport" arrowColor="black" /></div>
					<div class="table_controls_footer"></div>
				</div>
</c:if>
		</c:if>
		<%-- table_controls --%>
		<div class="table_controls_footer"></div>
		<div  id="fixedTable" style="overflow: hidden; z-index: 3; visibility: hidden; position: absolute; width: 178px;">
		<table class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<c:if test="${isIE eq true}">
						<th rowspan="2"  height="39px"   class="val_str">
					</c:if>
					<c:if test="${isIE eq false}">
						<th rowspan="2"  height="50px"   class="val_str">
					</c:if>					
						<report:sort field="lastName"
									 direction="asc" formName="participantSummaryReportForm">Name</report:sort></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details} ">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%ParticipantSummaryDetails theItem=(ParticipantSummaryDetails)pageContext.getAttribute("theItem"); %>
<c:set var="indexValue" value="${theIndex.index}"/>
<c:set var="theItemVal" value="${theItem}"/>  


						<%
						  String temp = pageContext.getAttribute("indexValue").toString();
								String rowClass = "spec";
									if (Integer.parseInt(temp) % 2 == 1) {
										rowClass = "";
									} else {
										rowClass = "spec";
									}
						%>
						<tr style="BACKGROUND-COLOR: #fafaf8">
							<td>
								<div style="color: #000;">
						<bd:link
										action="/do/bob/participant/participantAccount/?profileId="
					                                            ${theItem.profileId}>
									${theItem.lastName},${theItem.firstName}
									</bd:link>
										<br/>
										<render:ssn property="theItem.ssn" />
										<br/>
									</div>
							</td>
						</tr>
</c:forEach>
				</c:if>
			</tbody>
		</table>
		<c:if test="${isIE eq true}">
		<table>
			<tr><td></td></tr>
		</table>
		</c:if>
		</div>
		
		<div id="scrollingTable" style="width:918px;Overflow-x:auto;Overflow-y:hidden">
		<table class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<c:if test="${isIE eq true}">
						<th rowspan="2"  height="39px"   class="val_str">
					</c:if>
					<c:if test="${isIE eq false}">
						<th rowspan="2"  height="50px"   class="val_str">
					</c:if>					
						<div style="width: 161px;"> 
							<report:sort field="lastName" direction="asc" formName="participantSummaryReportForm">Name</report:sort> 
						</div>
					</th>
<c:if test="${participantSummaryReportForm.showDivision == true}">
				        <th rowspan="2" class="val_str">
						<report:sort field="division"
									 direction="asc" formName="participantSummaryReportForm">Division</report:sort>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
</c:if>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="birthDate" 
									 direction="asc" formName="participantSummaryReportForm">Date Of Birth (Age)</report:sort></th>
					<th colspan="2" class="val_str">
						<div align="center"><strong>Status</strong>
					</th>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="eligibilityDate"
									 direction="asc" formName="participantSummaryReportForm">Eligibility Date</report:sort></th>
					<th rowspan="2" class="val_str">
						<c:if test="${isReportCurrent eq true}">
							<report:sort field="investmentInstructionType" 
										 direction="desc" formName="participantSummaryReportForm">Investment Instruction Type</report:sort></c:if> 
						<c:if test="${isReportCurrent eq false}">Investment Instruction Type</c:if></th>
<c:if test="${participantSummaryReportForm.hasRothFeature == true}">

						<th  rowspan="2" class="val_str">
						<c:if test="${isReportCurrent eq true}">
							<report:sort field="rothInd" direction="desc" formName="participantSummaryReportForm">Roth Money</report:sort></c:if> 
						<c:if test="${isReportCurrent eq false}">Roth Money</c:if></th>
</c:if>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="employeeAssets"
									 direction="desc" formName="participantSummaryReportForm">Employee Assets($)</report:sort></th>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="employerAssets"
									 direction="desc" formName="participantSummaryReportForm">Employer Assets($)</report:sort></th>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="totalAssets"
									 direction="desc" formName="participantSummaryReportForm">Total Assets($)</report:sort></th>
<c:if test="${participantSummaryReportForm.hasLoansFeature == true}">

						<th rowspan="2" class="val_str align_center">
							<report:sort field="outstandingLoans"
										 direction="desc" formName="participantSummaryReportForm">Outstanding Loans($)</report:sort></th>
</c:if>
<c:if test="${participantSummaryReportForm.hasContractGatewayInd == true}">

						<th rowspan="2" class="val_str">
							<c:if test="${isReportCurrent eq true}">
								<report:sort field="participantGatewayInd" direction="desc" formName="participantSummaryReportForm">Guaranteed<br />
								Income<br />Feature</report:sort>
							</c:if> 
							<c:if test="${isReportCurrent eq false}">Guaranteed<br />Income<br />
								Feature
							</c:if></th>
</c:if>
<c:if test="${participantSummaryReportForm.hasManagedAccountInd == true}">

						<th rowspan="2" class="val_str">
							<c:if test="${isReportCurrent eq true}">
								<report:sort field="managedAccountStatusInd" direction="desc" formName="participantSummaryReportForm">Mgd.<br />
								Account<br />Feature</report:sort>
							</c:if> 
							<c:if test="${isReportCurrent eq false}">Mgd.<br />Account<br />
								Feature
							</c:if></th>
</c:if>
</tr>
				<!-- table details second row starts -->
				<tr>
				<!-- Status -->
				  <th align="left" valign="bottom" nowrap="nowrap" >
				 <c:if test="${isReportCurrent eq true}">
					  <report:sort field="employmentStatus" direction="desc" formName="participantSummaryReportForm">Employment</report:sort> 
					</c:if> 
					<c:if test="${isReportCurrent eq false}">
					  Employment
					</c:if> 
				  </th>
				  <th align="left" valign="bottom" nowrap="nowrap" >
				  <c:if test="${isReportCurrent eq true}">
					<report:sort field="contributionStatus" direction="desc" formName="participantSummaryReportForm">Contribution</report:sort>  
					</c:if> 
					<c:if test="${isReportCurrent eq false}">
					  Contribution
					</c:if> 
				  </th> 
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%ParticipantSummaryDetails theItem=(ParticipantSummaryDetails)pageContext.getAttribute("theItem"); %>
 <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
								String rowClass = "spec";
									if (Integer.parseInt(temp) % 2 == 1) {
										rowClass = "";
									} else {
										rowClass = "spec";
									}
						%>
						<tr class="<%=rowClass%>">
							<td>
								<div style="color: #000;">
									<bd:link
										action="/do/bob/participant/participantAccount/" paramId="profileId"
												paramName="theItem" paramProperty="profileId">
${theItem.lastName}, ${theItem.firstName}

									</bd:link>
										<br/>
										<render:ssn property="theItem.ssn" />
										<br/>
									</div>
							</td>
							
				            <!-- Division -->
<c:if test="${participantSummaryReportForm.showDivision == true}">
<td class="name" nowrap="nowrap">${theItem.division} </td>
</c:if>
							
							<td class="date align_center">
								<render:date property="theItem.birthDate"
											 defaultValue="not provided"
								patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /> <%
								if (theItem.getShowAge()) { %>
									(<report:number property="theItem.age" defaultValue="" type="i" />)
								<% } %>
							</td>
							<%-- CL 110234 Begin--%>
							<td class="val_str">
								 <c:if test="${not empty theItem.employmentStatus}">
									<%=theItem.getEmploymentStatusDescription()%>
								 </c:if>
							  </td>
							<%-- CL 110234 End--%>
							<td class="val_str"><%=theItem.getStatus()%></td>
							<td class="date align_center">
								<render:date property="theItem.eligibilityDate" 
											 defaultValue="not provided"
											 patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
							<td class="val_str" onmouseover="tooltip('<%=theItem.getInvestmentInstructionType()%>')" onmouseout="UnTip()"><%=theItem.getInvestmentInstructionType()%></td>
<c:if test="${participantSummaryReportForm.hasRothFeature == true}">

								<td class="val_str"><%=theItem.getRothInd()%></td>
</c:if>
							<td class="cur">
								<report:number property='theItem.employeeAssets' defaultValue="0.00"
								pattern="###,###,##0.00" /></td>
							<td class="cur">
								<report:number property='theItem.employerAssets' defaultValue="0.00"
								pattern="###,###,##0.00" /></td>
							<td class="cur">
								<report:number property='theItem.totalAssets' defaultValue="0.00" 
								pattern="###,###,##0.00" /></td>
<c:if test="${participantSummaryReportForm.hasLoansFeature == true}">


								<td class="cur"><report:number
									property='theItem.outstandingLoans' defaultValue=""
									pattern="###,###,##0.00" /></td>
</c:if>
<c:if test="${participantSummaryReportForm.hasContractGatewayInd == true}">


										 
								<%  boolean liaIndicator = false;
								%>
<c:if test="${theItem.showLIADetailsSection == true}">
									<%  liaIndicator = true;%>
</c:if>
								<td class="name"><c:if test="${isReportCurrent eq true}">
									<% if("Yes".equals(theItem.getParticipantGatewayInd().trim()) && liaIndicator) {%>
${theItem.participantGatewayInd}/LIA
	          						<% }  else { %>
${theItem.participantGatewayInd}
	          						<% } %>
								</c:if> <c:if test="${isReportCurrent eq false}">
									<% if("Yes".equals(theItem.getDefaultGateway().trim()) && liaIndicator) {%>
${theItem.defaultGateway}/LIA
	          						<% }  else { %>
${theItem.defaultGateway}
	          						<% }  %>
								</c:if></td>
</c:if>
								<c:if
									test="${participantSummaryReportForm.hasManagedAccountInd == true}">
									<td class="name"><c:if test="${isReportCurrent eq true}">
											<%
												if ("Yes".equals(theItem.getManagedAccountStatusInd().trim())) {
											%>
${theItem.managedAccountStatusInd}
	          						<%
	          							} else {
	          						%>
${theItem.managedAccountStatusInd}
	          						<%
	          							}
	          						%>
										</c:if> <c:if test="${isReportCurrent eq false}">
											<%
												if ("Yes".equals(theItem.getDefaultManagedAccount().trim())) {
											%>
${theItem.defaultManagedAccount}
	          						<%
	          							} else {
	          						%>
${theItem.defaultManagedAccount}
	          						<%
	          							}
	          						%>
										</c:if></td>
								</c:if>
							</tr>
</c:forEach>
				</c:if>
			</tbody>
		</table>
		<c:if test="${isIE eq true}">
		<table>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
		</table>
		</c:if>
		</div>
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Participants" /></strong></div>
					<div class="table_pagination"><report:pageCounter formName="participantSummaryReportForm"
						report="theReport" arrowColor="black" /></div>
					<div class="table_controls_footer"></div>
				</div>
</c:if>
		</c:if>
		
		<c:if test="${not empty theReport.participantSummaryTotals}">
<c:if test="${empty theReport.details}">
			  <div class="message message_info">
				    <dl>
				    <dt>Information Message</dt>
				     <dd>1.&nbsp;Your search criteria produced no results.
								Please change your search criteria.</dd>
				    </dl>
			  </div>
</c:if> <%-- report table controls --%>
		</c:if>
		
	</div>
</bd:form>
<layout:pageFooter/>
