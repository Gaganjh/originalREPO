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
<%@ page import="com.manulife.pension.bd.web.usermanagement.ExtUserSearchReportHelper"%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.bd.web.usermanagement.ExtUserReportHelper" %>
<%@ page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@ page import="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserReportData"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@ page import = "com.manulife.pension.service.security.BDUserRoleType" %>

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

<c:set var="userRoles" value="<%=ExtUserReportHelper.getExtUserRoles()%>"/>
<jsp:useBean id="reportItem" scope="page" class="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchDetails" />

								
<c:set var="reportItem" value="${item}" scope="page" />
<layout:pageHeader nameStyle="h2"/>

<c:if test="<%=!BDWebCommonUtils.hasErrors(request)%>">
	<c:if test="${theReport.totalCount==0}">
	  <c:choose>
	    <c:when test="${inforceReportForm.emptyForm}">
	      <utils:info contentId="<%=BDContentConstants.USER_SEARCH_NO_CRITERIA %>"/>
	    </c:when>
	    <c:otherwise>
	     <utils:info contentId="<%=BDContentConstants.USER_SEARCH_NO_RESULT%>"/>
	    </c:otherwise>
	  </c:choose>
	</c:if>
</c:if>
<style type="text/css">
	form{display:inline;}
	#filter_labels p {
		color:#FFFFFF;
		font-family:verdana,arial,helvetica,sans-serif;
		font-size:1em;
	}	
	.hand {
		cursor: pointer;
		cursor: hand;
	} 
	#page_content td, th{
	font-size: 11px;	
	}
		
</style>
<%
    BDSessionHelper.moveMessageIntoRequest(request);
%>
<report:formatMessages scope="request"/>

<div id="filter_wrapper"> 
	<div id="filter">
		<ul>
			<li><a href="/do/usermanagement/reports/prospects"><span>Prospects</span></a></li>		
			<li class="active"><em><span>Inforce</span></em></li>
		</ul>
	</div>
	<div align="right" style="margin-top:10px;margin-right:10px"><a href="/do/usermanagement/reports/inforce?task=download" title="<content:getAttribute attribute='text' beanName='csvText'/>"><content:image id="csvText" contentfile="image"/></a></div> 
</div>  

<bd:form action="/do/usermanagement/reports/inforce" modelAttribute="inforceReportForm" name="inforceReportForm">
	<input type="hidden" name="task" id="task"/>
		<table width="100%" border="0" id="filter_labels" class="page_section_subheader controls">
			<tr>
				<td align="right" nowrap="nowrap" width="%">
					<p><label for="kindof_filter">Last Name:</label></p>
				</td>
				<td align="left">
		<form:input path="lastName" maxlength="30" size="20"/>
				</td>
				<td align="right" width="15%">
					<p><label for="kindof_filter">Role:</label></p>
				</td>
				<td align="left" >
				

					
					
					
					<form:select path="userRoleCode" style="margin-left: 3px">
					
<form:options  items="${userRoles}" itemLabel="label" itemValue="value" />

</form:select>

				</td>
				<td align="right" nowrap="nowrap">
					<p><label for="kindof_filter">Contract Number:</label></p>
				</td>
				<td align="left" width="20%">
<form:input path="contractNum" maxlength="7" size="6"/>
				</td>
			</tr>
			<tr height="16px">
				<td align="right" nowrap="nowrap" width="15%">
					<p><label for="kindof_filter">First Name:</label></p>
				</td>
				<td align="left">
<form:input path="firstName" maxlength="30" size="20"/>
				</td>			
				<td align="right">
					<p><label for="kindof_filter">BD Firm:</label></p>
				</td>
				<td align="left" >
					<div style="margin-bottom:10px">
						<utils:firmSearch firmName="firmName" firmId="firmId"/>
					</div>
				</td>
				<td align="right">
					<p><label for="kindof_filter">RIA Firm:</label></p>
				</td>
				<td align="left" >
					<div style="margin-bottom:10px">
						<utils:riaFirmSearch firmName="selectedRiaFirmName" firmId="selectedRiaFirmId"/>
					</div>
				</td>
				
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">
					<p><label for="kindof_filter">Registered from:</label></p>
				</td>
				<td align="left">
					<div style="float:left;width:90px">
<form:input path="regFromDate" maxlength="10" size="8" id="regFromDate"/>
					</div>
					<div style="float:left">
						<utils:btnCalendar  dateField="regFromDate" calendarcontainer="calendarcontainer" datefields="datefields"  calendarpicker="calendarpicker"/>				
					</div>
				</td>
				<td align="right" nowrap="nowrap">
					<p><label for="kindof_filter">Registered to:</label></p>
				</td>
				<td>
					<div style="float:left;width:90px">
<form:input path="regToDate" maxlength="10" size="8" id="regToDate"/>
					</div>
					<div style="float:left">
						<utils:btnCalendar  dateField="regToDate" calendarcontainer="calendarcontainer1" datefields="datefields1"  calendarpicker="calendarpicker1"/>	
					</div>						
				</td>
			
			</tr>					
		</table>

<div class="report_table">
	<div class="table_controls">
		<div class="button_regular"><a href="javascript:reportSubmit(document.inforceReportForm,'reset')">Reset</a></div>
	  	<div class="button_regular"><a href="javascript:reportSubmit(document.inforceReportForm,'filter')">Submit</a></div>
		<div class="button_regular"><a href="/do/usermanagement/search">User Management</a></div>
	</div>	

	<div class="table_controls">
		<div class="table_action_buttons"> </div>
		<div class="table_display_info">
			<strong>
				<report:recordCounter report="theReport" label="Records"/>
			</strong>
		</div>
		<div class="table_pagination">
			<report:pageCounter report="theReport" arrowColor="black" formName="inforceReportForm"/>
		</div>
	</div>	
	<table width="100%" class="report_table_content">
		<thead>
			<tr>
				<th width="20%" class="name"><report:sort field="<%=BDExtUserReportData.SORT_LAST_NAME%>" direction="asc" formName="inforceReportForm">Name</report:sort></th>
				<th width="25%" class="name"><a href="#">User Role </a></th>
	            <th width="12%" class="name"><a href="#">Registration Date </a></th>
	            <th width="35%" class="val_str"><report:sort field="<%=BDExtUserReportData.SORT_FIRM_NAME%>" direction="asc" formName="inforceReportForm">Firm Name </report:sort></th>
			</tr>
		</thead>
	    <tbody>
			<c:choose>
				<c:when test="${theReport.totalCount==0}">
		 			<tr>
		 				<td colspan="4">
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
							
								<c:set var="roleType" value="${item.roleType}"/>
								
								<%
								BDUserRoleType temp = (BDUserRoleType)pageContext.getAttribute("roleType");	
								String roleCodeVal = ExtUserSearchReportHelper.getUserRoleDisplay(temp);
								pageContext.setAttribute("roleCodeVal", roleCodeVal);
								%>
							<td class="name">
								${roleCodeVal }
							</td>
							<td class="name" style="text-align:center">
								<render:date property="item.registrationDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" defaultValue="<%=BDConstants.HYPHON_SYMBOL%>"/>
							</td>		 	     
							<td class="name">
								${item.firmName}
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
				<report:recordCounter report="theReport" label="Records"/>
			</strong>
		</div>
		<div class="table_pagination">
			<report:pageCounter name="inforceReportForm" report="theReport" arrowColor="black" formName="inforceReportForm"/>
		</div>
	</div>
</div>
</bd:form>
<layout:pageFooter/>
