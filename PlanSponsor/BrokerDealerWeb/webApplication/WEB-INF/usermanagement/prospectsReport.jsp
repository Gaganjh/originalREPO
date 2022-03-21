<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.bd.web.usermanagement.ExtUserReportHelper" %>
<%@ page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@ page import="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportData"%>
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="csvText" />

<script type="text/javascript">
	function reportSubmit(frm,task) {
		document.getElementById("task").value = task;
		frm.submit();
	}
</script>

<% BDExtUserReportData theReport = (BDExtUserReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<jsp:useBean id="reportItem" scope="page" class="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportDetails" />

								
<c:set var="reportItem" value="${item}" scope="page" />



<c:set var="states" value="<%=ExtUserReportHelper.getUSStates()%>"/>
<layout:pageHeader nameStyle="h2"/>

<c:if test="<%=!BDWebCommonUtils.hasErrors(request)%>">
	<c:if test="${theReport.totalCount==0}">
	  <c:choose>
	    <c:when test="${prospectsReportForm.emptyForm}">
	      <utils:info contentId="<%=BDContentConstants.USER_SEARCH_NO_CRITERIA %>"/>
	    </c:when>
	    <c:otherwise>
	     <utils:info contentId="<%=BDContentConstants.USER_SEARCH_NO_RESULT%>"/>
	    </c:otherwise>
	  </c:choose>
	</c:if>
</c:if>

<%
    BDSessionHelper.moveMessageIntoRequest(request);
%>
<report:formatMessages scope="request"/>

<style type="text/css">
	form{display:inline;}
	#filter_labels p {
		color:#FFFFFF;
		font-family:verdana,arial,helvetica,sans-serif;
		font-size:1em;
	}	
	#page_content td, th{
	font-size: 11px;	
	}
</style>

<div id="filter_wrapper"> 
	<div id="filter">
		<ul>
			<li class="active"><em><span>Prospects</span></em></li>
			<li><a href="/do/usermanagement/reports/inforce"><span>Inforce</span></a></li>
		</ul>
	</div>
	<div align="right" style="margin-top:10px;margin-right:10px"><a href="/do/usermanagement/reports/prospects?task=download" title="<content:getAttribute attribute='text' beanName='csvText'/>"><content:image id="csvText" contentfile="image"/></a></div> 
</div>  

<bd:form action="/do/usermanagement/reports/prospects" modelAttribute="prospectsReportForm" name="prospectsReportForm">
<form action="#" class="page_section_filter form">
<input type="hidden" name="task" id="task"/>
	<table width="100%" border="0" id="filter_labels" class="page_section_subheader controls">
		<tr>
			<td width="15%" align="right">
				<p><label for="kindof_filter">Last Name:</label></p>
			</td>
			<td width="20%">
<form:input path="lastName" maxlength="30" size="18"/>
			</td>
			<td width="15%" align="right">
				<p><label for="kindof_filter">First Name:</label></p>
			</td>
			<td width="20%">
<form:input path="firstName" maxlength="30" size="18"/>
			</td>
			<td width="8%" align="right">
				<p><label for="kindof_filter">State:</label></p>
			</td>
			<td >
				<form:select path="state">
<form:options items="${states}" itemLabel="label" itemValue="value"/>
</form:select>
			</td>
		</tr>
		<tr>
			<td align="right">
				<p><label for="kindof_filter">Registered from:</label></p>
			</td>
			<td >
				<div style="float:left;width:130px">
<form:input path="regFromDate" maxlength="10" size="15" id="regFromDate"/>
				</div>			
				<div style="float:left">
					<utils:btnCalendar  dateField="regFromDate" calendarcontainer="calendarcontainer" datefields="datefields"  calendarpicker="calendarpicker"/>
				</div>			
			</td>
			<td align="right">
				<p><label for="kindof_filter">Registered to:</label></p>
			</td>
			<td>
				<div style="float:left;width:130px">									
<form:input path="regToDate" maxlength="10" size="15" id="regToDate"/>
				</div>			
				<div style="float:left">			
					<utils:btnCalendar  dateField="regToDate" calendarcontainer="calendarcontainer1" datefields="datefields1"  calendarpicker="calendarpicker1"/>			
				</div>	
			</td>
			<td colspan="2"> &nbsp; </td>		
		</tr>			
	</table>
</form>
<div class="report_table">
	<div class="table_controls">
		<div class="button_regular"><a href="javascript:reportSubmit(document.prospectsReportForm,'reset')">Reset</a></div>
	  	<div class="button_regular"><a href="javascript:reportSubmit(document.prospectsReportForm,'filter')">Submit</a></div>
		<div class="button_regular"><a href="/do/usermanagement/search">User Management</a></div>
	</div>	
	<div class="table_controls">
		<div class="table_action_buttons"> </div>
		<div class="table_display_info">
			<strong>
				<report:recordCounter report="theReport" label="Prospects"/>
			</strong>
		</div>
		<div class="table_pagination">
			<report:pageCounter report="theReport" arrowColor="black"/>
		</div>
	</div>	

	<table width="100%" class="report_table_content">
		<thead>
			<tr>
				<th width="25%" class="name"><a href="#">Name</a></th>
				<th width="14%" class="name"><a href="#">Registration Date </a></th>
				<th width="28%" class="val_str"><a href="#">Firm Name </a></th>
				<th width="13%" class="name"><a href="#">Phone Number </a></th>
				<th width="20%" class="val_str"><a href="#">Email Address </a></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${theReport.totalCount==0}">
		 			<tr>
		 				<td colspan="5">
		 					&nbsp;
						</td>	  
					</tr>     
				</c:when>
			 	<c:otherwise>
					<c:forEach var="item" items="${theReport.details}" varStatus="loopStatus">


						<c:choose>
							<c:when test="${loopStatus.index % 2 == 1}">
								<c:set var="trStyleClass" value="" />
							</c:when>
							<c:otherwise>
								<c:set var="trStyleClass" value="spec" />
							</c:otherwise>
						</c:choose>
						<tr class="${trStyleClass}">
							<td class="name">
								${item.name}			 	      
							</td>
							<td class="name">
								<render:date property="item.registrationDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" defaultValue="<%=BDConstants.HYPHON_SYMBOL%>"/>
							</td>	
							<td class="val_str">
								${item.firmName}
							</td>
							<td class="name">
								${item.phoneNumber}			 	      
							</td>		 	     
							<td class="val_str">
								${item.emailAddress}			 	      
							</td>		 	     
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>			
		</tbody>
	</table>
	<div class="table_controls">	
		<div class="table_action_buttons"> </div>
		<div class="table_display_info">
			<strong>
				<report:recordCounter report="theReport" label="Prospects"/>
			</strong>
		</div>
		<div class="table_pagination">
			<report:pageCounter report="theReport" arrowColor="black"/>
		</div>
	</div>
</div>
</bd:form>

<layout:pageFooter/>
