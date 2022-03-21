<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>

<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bd-logicext.tld" prefix="logicext"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>

<%@ page import="com.manulife.pension.bd.web.bob.secureDocumentUpload.SDUHistoryTabController"%>
<%@ page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabForm"%>
<%@page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabReportData" %>


<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collections"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.hateoas.PagedResources.PageMetadata"%>




<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<%
BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<%
SDUHistoryTabForm sduHistoryTabForm=(SDUHistoryTabForm)request.getAttribute("sduHistoryTabForm");
pageContext.setAttribute("sduHistoryTabForm",sduHistoryTabForm,PageContext.SESSION_SCOPE);
%>

<%
SDUHistoryTabReportData theReport = (SDUHistoryTabReportData)request.getAttribute(SDUHistoryTabController.HISTORY_RESULTS);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

					 
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
					 
<content:contentBean contentId="<%=BDContentConstants.SDU_NO_RECORDS_TO_DISPLAY_MESSAGE%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="NoResultsMessage"/>
                    
<content:contentBean contentId="<%=BDContentConstants.SDU_HISTORY_TAB_INTRO%>"
                     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                     id="sduHistoryTabIntro"/>


<c:set var="contract" value="${bobContext.contractProfile.contract.contractNumber}"
	scope="request" />
	
<script type="text/javascript">

function filterSubmit(){
	document.forms['sduHistoryTabForm'].elements['task'].value = "filter";
	document.forms['sduHistoryTabForm'].elements['pageNumber'].value = 1;
	if(document.forms['sduHistoryTabForm'].elements['justMine'].checked){
		setFilterFromInput(document.forms['sduHistoryTabForm'].elements['justMine'].value);
	}
	else {
		document.forms['sduHistoryTabForm'].elements['justMine'].value = false;
		setFilterFromInput(document.forms['sduHistoryTabForm'].elements['justMine'].value);
	}
	document.forms['sduHistoryTabForm'].submit();	
}

function sortSubmit(sortField, sortDirection) {
	document.forms['sduHistoryTabForm'].elements['task'].value = "sort";
	document.forms['sduHistoryTabForm'].elements['pageNumber'].value = 1;
	document.forms['sduHistoryTabForm'].elements['sortField'].value = sortField;
	document.forms['sduHistoryTabForm'].elements['sortDirection'].value = sortDirection;
	document.forms['sduHistoryTabForm'].submit();
}

function pagingSubmit(pageNumber){
	if (document.forms['sduHistoryTabForm']) {
		document.forms['sduHistoryTabForm'].elements['task'].value = "page";
		document.forms['sduHistoryTabForm'].elements['pageNumber'].value = pageNumber;
		document.forms['sduHistoryTabForm'].submit();
	}
}

function validateCalInput(event) { 
    var key = event.which;
	//47 to 57 : /0123456789
	//0 : all non printable chars (del,right arrow, left arrow)
	//8 : Backspace
	//3 & 22 : ctrl+c ctrl+v	
    if ((key>=47 && key<=57) || key === 0 || key === 3 || key === 22 || key === 8){
		 return true;	
	}
	else{
		event.preventDefault();
        return false;	
	}
} 
</script>

<style type="text/css">
	#calendarmenu {
	POSITION:absolute;
	z-index:100 !important; 
	}	
	#buttoncalendar {
        width:155px!important;
    }
</style>



<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<!--Layout/intro1-->
    <p><content:getAttribute beanName="sduHistoryTabIntro" attribute="text"/></p>

<div class="table_controls_footer"></div>

<report:formatMessages scope="request"/>

<DIV id=errordivcs>
	<content:errors scope="session" />
</DIV>


<% if (sduHistoryTabForm.isPendingContract()) { %>							
	<jsp:include page="sduNavigationBar.jsp" flush="true">
						<jsp:param name="selectedTab" value="HistoryTab" />
	</jsp:include>
<% }else{ %>
	<navigation:contractReportsTab />
<% }%>	
	
<div class="page_section_subheader controls">
        <h3>Document Submissions </h3>
        


	 <bd:form method="post" action="/do/bob/secureDocumentUpload/history/" modelAttribute="sduHistoryTabForm" name="sduHistoryTabForm"
				cssClass="page_section_filter form">
		<input type="hidden" name="task" value="default"/>
		<form:hidden path="pageNumber"/>
		<form:hidden path="sortField"/>
		<form:hidden path="sortDirection"/>	
		<input type="hidden" name="pdfCapped" />		
		<p> <label for="fromDateFilter">from: </label></p> 
		<form:input path="filterStartDate" maxlength="10" size="9" onkeypress="return validateCalInput(event)"  style="padding-right: 4px;" cssClass="page_section_filter input" />	
		<utils:btnCalendar dateField="filterStartDate"
																	calendarcontainer="calendarcontainer1" datefields="datefields1"
																	calendarpicker="calendarpicker1" />
		
		<p> <label for="toDateFilter">&nbsp;to: </label></p> 
		<form:input path="filterEndDate" maxlength="10" size="9" onkeypress="return validateCalInput(event)" style="padding-right: 4px;" cssClass="page_section_filter input" />
		<utils:btnCalendar dateField="filterEndDate"
									calendarcontainer="calendarcontainer" datefields="datefields"
									calendarpicker="calendarpicker" />
		<p><label>&nbsp;(mm/dd/yyyy)</label></p>
		<p style=" margin-left: 20px; margin-right: 20px;"> <form:checkbox path="justMine" id="justMine" value="true"/> <label> Just mine</label></p>
		<a class="buttonheader" style="float: right;" href="javascript:filterSubmit();"><span>Search</span></a>          
        </bd:form>
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  valign="top" title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
		<a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  valign="top" title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
      </div>       

	

<div class="report_table">



		<c:if test="${not empty theReport }">
			<c:if test="${not empty theReport.details}">
							<div class="table_controls">
								<div class="table_action_buttons"></div>
								<div class="table_display_info"><strong><report:recordCounter
									report="theReport" label="Documents" /></strong></div>
								<div class="table_pagination">
									<report:pageCounterViaSubmit formName="sduHistoryTabForm" report="theReport" arrowColor="black"/> 
								</div>
								<div class="table_controls_footer"></div>
							</div>
			</c:if>
		</c:if>
				
		<table class="report_table_content" id="history_table" style="word-wrap:break-word; table-layout: fixed;">
			<thead>
				<tr colspan="6">
					<th rowspan="2" width="15%" class="val_str">				
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_ID%>" 
					direction="desc">Submission Number</report:bdSortLinkViaSubmit>
					</th>						    
					<th rowspan="2" width="11%" class="val_str">
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_DATE%>" 
					direction="desc">Submission<br/>Date/Time</report:bdSortLinkViaSubmit>
					</th>
					<th rowspan="2" width="25%" class="val_str">
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_DOCUMENT_NAME%>" 
					direction="asc">Document Name</report:bdSortLinkViaSubmit>
					</th>
					<th rowspan="2" width="23%" class="val_str">
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMITTER_NAME%>" 
					direction="asc">Submitted By</report:bdSortLinkViaSubmit>
					</th>
					<th rowspan="2" width="15%" class="val_str">
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMITTER_ROLE%>" 
					direction="asc">Role </report:bdSortLinkViaSubmit>
					</th>
					<th rowspan="2" width="11%" class="val_str">
						<report:bdSortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_STATUS%>" 
					direction="asc">Upload Status </report:bdSortLinkViaSubmit>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty theReport.details}">
					<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
												
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
					<tr class="<%=rowClass%>" colspan="6">
						<td class="val_str">&nbsp;${theItem.submissionId} </td>
						<td class="date">
							<render:date property="theItem.submissionTs" patternOut="MM/dd/yyyy'<br>'hh:mm a" defaultValue=""/>
						</td>                  
						<td class="val_str"> ${theItem.fileName} </td>
						<td class="val_str">${theItem.clientUserName} </td>
						<td class="val_str">${theItem.clientUserRole} </td>
						<td class="val_str"> ${theItem.submissionStatus} </td>							
					</tr>							
					</c:forEach>
				</c:if>
			</tbody>	
		</table>

		
		<c:if test="${not empty theReport }">
			<c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Documents" /></strong></div>
					<div class="table_pagination">
						<report:pageCounterViaSubmit formName="sduHistoryTabForm" report="theReport" arrowColor="black"/> 
					</div>
					<div class="table_controls_footer"></div>
				</div>
			</c:if>
		</c:if>
		
		<c:if test="${theReport.totalCount == 0}">
			  <div class="message message_info">
					<dl>
					<dt>Information Message</dt>
					 <dd><content:getAttribute beanName="NoResultsMessage" attribute="text" /></dd>
					</dl>
			  </div>
		</c:if>

</div>

	
<div class="clear_footer">&nbsp;</div>	
		  
</br>
<layout:pageFooter/>
	
  