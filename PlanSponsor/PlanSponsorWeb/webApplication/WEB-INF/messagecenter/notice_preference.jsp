<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%> 

<%@page import="com.manulife.pension.ps.web.messagecenter.personalization.MCNoticePreferenceForm"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.util.render.RenderConstants"%> 



<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%><script type="text/javascript">

<!--
<content:contentBean contentId="<%=MCContentConstants.CancelHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cancelLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveFinishHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveFinishLabel" />
                     
<content:contentBean contentId="<%=MCContentConstants.AlertNameHoverOver%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="alertNameLabel" />

<content:contentBean contentId="<%=MCContentConstants.AlertFrequencyHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="alertFrequencyLabel" />
                         
<content:contentBean contentId="<%=MCContentConstants.AlertDistributionDateHoverOver%>"
	                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="alertDistributionDateLabel" />
                             
<content:contentBean contentId="<%=MCContentConstants.AlertTimingHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="alertTimingLabel" />
                                 
<content:contentBean contentId="<%=MCContentConstants.AlertMessageTypeHoverOver%>"
	                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="alertMessageTypeLabel" />
                                         
<content:contentBean contentId="<%=MCContentConstants.AddNewAlertHoverOver%>"
	                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			         id="addMaxAlertLabel" />
	
<content:contentBean contentId="<%=MCContentConstants.AddNewAlertHoverLink%>"
	                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				     id="addNewAlertLabel" />
	               
<content:contentBean contentId="<%=MCContentConstants.AddTableTitle%>"
	                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				     id="alertTableTitle" />
<content:contentBean contentId="<%=MCContentConstants.CalErrorMessage%>"
		                 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
					     id="calErrorMessage" />                
	                 



var cancelLabel = '<content:getAttribute id="cancelLabel" attribute="text" filter="true"/>'
var saveLabel = '<content:getAttribute id="saveLabel" attribute="text" filter="true"/>'
var saveFinishLabel = '<content:getAttribute id="saveFinishLabel" attribute="text" filter="true"/>'
var alertNameLabel = '<content:getAttribute id="alertNameLabel" attribute="text" filter="true"/>'
var alertFrequencyLabel = '<content:getAttribute id="alertFrequencyLabel" attribute="text" filter="true"/>'
var alertDistributionDateLabel = '<content:getAttribute id="alertDistributionDateLabel" attribute="text" filter="true"/>'
var alertTimingLabel = "<content:getAttribute id="alertTimingLabel" attribute="text" filter="true"/>"
var alertMessageTypeLabel = '<content:getAttribute id="alertMessageTypeLabel" attribute="text" filter="true"/>'
var addMaxAlertLabel = '<content:getAttribute id="addMaxAlertLabel" attribute="text" filter="true"/>' 
var addNewAlertLabel = '<content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/>'
var alertTableTitle = '<content:getAttribute id="alertTableTitle" attribute="text" filter="true"/>'
var calErrorMessage = '<content:getAttribute id="calErrorMessage" attribute="text" filter="true"/>'



 	function enableButtons() {
    if (document.getElementById('saveAndFinishButton')) {
        document.getElementById('saveAndFinishButton').disabled=false;
    } // fi
    if (document.getElementById('saveButton')) {
        document.getElementById('saveButton').disabled=false;
    } // fi
    if (document.getElementById('cancelButton')) {
    	document.getElementById('cancelButton').disabled=false;
    } // fi
}

// Registers our function with the functions that are run on load.
if (typeof(runOnLoad) == "function") {
  runOnLoad( enableButtons );
} 
//-->
</script>
<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/noticeCalendar.js"></script>
<c:set var="alertFrequency" value="${noticePrefForm.alertFrequency}" />  
<content:errors scope="session"/>

<ps:form action="/do/messagecenter/personalizeNotice" modelAttribute="noticePrefForm" name="noticePrefForm"  id="noticePrefForm">	
	<input type="hidden" name="action" value="save" />
	<input type="hidden" name="id" value=0 />

																
<table border="0" cellpadding="0" cellspacing="0" width="708">
	<tbody>
		<mc:personalizationTab active="notice" global="<%=(Boolean)MCUtils.isInGlobalContext(request)%>" tpa="<%=SessionHelper.getUserProfile(request).getRole().isTPA() %>" noticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ALERT_NOITICE_PREFERENCE) %>" enableNoticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE) %>" />		
	</tbody>
		<TR>
		    <TD class=boxborder width=1><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
			<TD><!-- start table content -->
<c:if test="${noticePrefForm.alertsPageEnable ==true}">
				<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR>
							<TD class=tableheadTD1 height=25 colSpan=15><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=157><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=100><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=60><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=83><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=50><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=50><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR class=datacell1>
							<TD colSpan=15 align=left><STRONG>Legend</STRONG>:&nbsp;<IMG title=Delete border=0 alt=Delete src="/assets/unmanaged/images/delete_icon.gif">&nbsp;Delete</TD>
						</TR>
						<!-- Start of body title -->
						<TR class=tablesubMainhead>
							<TD class=tablesubMainhead colSpan=15><TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
									<TBODY>
										<TR>
											<TD class=tablesubMainhead width="100%"><B><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></B></TD>
										</TR>
									</TBODY>
								</TABLE></TD>
						</TR>
						<!-- End of body title -->
						<TR class=tablesubhead>
							<TD vAlign=top width=30 rowspan=2><B>Action</B></TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2 onmouseover="Tip(alertNameLabel)" onmouseout="UnTip()" ><B>Alert name</B><br>(Enter name of notice)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2 onmouseover="Tip(alertDistributionDateLabel)" onmouseout="UnTip()" ><B>Notice due date</B><br>(mm/dd/yyyy)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2 onmouseover="Tip(alertFrequencyLabel)" onmouseout="UnTip()" ><B>Alert frequency</B><br></TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top colspan="2" rowspan=2 onmouseover="Tip(alertTimingLabel)" onmouseout="UnTip()" ><B>Alert timing</B><br>(Range: 7-60 days)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top align="center" colspan=3 onmouseover="Tip(alertMessageTypeLabel)" onmouseout="UnTip()" ><B>Message type</B></TD>
						</TR>
						<TR class=tablesubhead onmouseover="Tip(alertMessageTypeLabel)" onmouseout="UnTip()" >
							<TD vAlign=top align="center"><B>Normal</B></TD>
							<TD class=dataheaddivider vAlign=bottom width=1>
							    <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
							</TD>
							<TD vAlign=top align="center"><B>Urgent</B></TD>
						</TR>
<c:if test="${not empty noticePrefForm.userNoticeManagerAlertList}">
<c:forEach items="${noticePrefForm.userNoticeManagerAlertList}" var="alertNotice" varStatus="index">
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].contractId"/>
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].profileId" />
<input type="hidden" name="/>noticePrefForm.userNoticeManagerAlert[${index.index}].alertId" />
<input type="hidden" name="tempId" value="${alertNotice.alertId}" />
<c:set  var="theindexValue" value="${index.index}"/>
					    <% String tempp = pageContext.getAttribute("theindexValue").toString();
					    if (Integer.parseInt(tempp) % 2 == 0) { %>
				        <tr class="datacell3 alertrows">
						<% } else { %>
				        <tr class="datacell1 alertrows">
						<% } %>
							<c:if test="${empty param.printFriendly}" >
<TD align=left><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /><A href="#" id=delete onclick="return doSubmitDelete('delete','${theindexValue}','${alertNotice.alertId}' )" ><IMG title=Delete border=0 id="Deleteimage" alt=Delete src="/assets/unmanaged/images/delete_icon.gif" ></A><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /></TD>
							</c:if>
							<c:if test="${not empty param.printFriendly}" >
							<TD align=left><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /><IMG border=0 id="Deleteimage" src="/assets/unmanaged/images/delete_icon.gif" ><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /></TD>
							</c:if>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<c:if test="${empty param.printFriendly}" >
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertName"  maxlength="40" size="40" cssClass="values"/>
								</TD>
								</c:if>
								<c:if test="${not empty param.printFriendly}" >
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertName"  disabled="true" maxlength="40" size="40" cssClass="values"/>
								</TD>
								</c:if>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD><TABLE cellSpacing=0 cellPadding=0>
										<TBODY>
											<TR class="alertrows1">
											  <c:if test="${empty param.printFriendly}" >
												<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].stringStartDate" maxlength="10" cssClass="values" id="${index.index}"/>
												</TD>
											   </c:if>
											   <c:if test="${not empty param.printFriendly}" >
												<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].stringStartDate"  disabled="true" maxlength="10" size="10" cssClass="values" id="${index.index}"/>
												</TD>
											   </c:if>
											   <c:if test="${empty param.printFriendly}" >
												<TD vAlign=bottom>&nbsp; <A href="#" onclick="clickedid = '${index.index}'; handleDateIconClicked(event, '${index.index}');" > <IMG border=0 src="/assets/unmanaged/images/cal.gif"> </A></TD>
											   </c:if>
											   <c:if test="${not empty param.printFriendly}" >
											    <TD vAlign=bottom>&nbsp;  <IMG border=0 src="/assets/unmanaged/images/cal.gif"> </TD>
											   </c:if>
											</TR>
										</TBODY>
									</TABLE></TD>
								<TD class=datadivider width=1>
								     <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD><c:if test="${empty param.printFriendly}" >
                                   <TD>
                                    <form:select path="userNoticeManagerAlertList[${index.index}].alertFrequenceCode"  cssClass="values" >
                                   	<form:option value="">Select</form:option>
<form:options items="${noticePrefForm.alertFrequencyCodes}" itemValue="lookupCode" itemLabel="lookupDesc"/>
</form:select>
								</TD></c:if>
								<c:if test="${not empty param.printFriendly}" >
								 <TD>
                                    <form:select path="userNoticeManagerAlertList[${index.index}].alertFrequenceCode"  cssClass="values" disabled="true">
                                   	<form:option value="">Select</form:option>
<form:options items="${noticePrefForm.alertFrequencyCodes}" itemValue="lookupCode" itemLabel="lookupDesc"/>
</form:select>
								</TD></c:if>
								<TD class=datadivider width=1>
								   <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD><c:if test="${empty param.printFriendly}" >
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertTimingCode"  size="2" cssClass="values"/>
								   
								</TD></c:if>
								<c:if test="${not empty param.printFriendly}" >
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertTimingCode" disabled="true" size="2" cssClass="values"/>
								   
								</TD></c:if>
								<TD>
								    Days in advance
								</TD>
								<TD class=datadivider width=1>
								    <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD><c:if test="${empty param.printFriendly}" >
								<TD align="center">
<form:radiobutton path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="N"/>
								</TD></c:if>
								<c:if test="${not empty param.printFriendly}" >
								<TD align="center">
<form:radiobutton disabled="true" path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="N"/>
								</TD></c:if>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<c:if test="${empty param.printFriendly}" > 
								<TD align="center">
<form:radiobutton path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="U"/>
								</TD></c:if>
								<c:if test="${not empty param.printFriendly}" >
								<TD align="center">
<form:radiobutton disabled="true" path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="U"/>
								</TD></c:if>
						 </TR>
</c:forEach>
</c:if>
							<TR>
								<TD class=boxborder colspan="14"><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
							</TR>
							<TR class=datacell1><c:if test="${empty param.printFriendly}" > 
							     <TD height="25" align=left colspan="14">
<c:if test="${noticePrefForm.alertMaxSize ==true}">
									 <a  class = "disabledalert noLinkAction" title= '<content:getAttribute id="addMaxAlertLabel" attribute="text" filter="true"/>' ><content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/></a>
									 <style>
										 .disabledalert {
											text-decoration: none;
											cursor: default;
											color: gray !important;
										}   
									</style> 
</c:if>
<c:if test="${noticePrefForm.alertMaxSize !=true}">
									 <a href="#" onclick="return doSubmitAdd('add');"    > <content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/> </a>
</c:if>
								</TD></c:if>
								<c:if test="${not empty param.printFriendly}" >
							     <TD height="25" align=left colspan="14">
									<content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/> 
								</TD></c:if>
							</TR>
					</TBODY>
				</TABLE>
</c:if>
<c:if test="${noticePrefForm.alertsPageEnable ==false}">
				<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR>
							<TD class=tablesubhead height=25 colSpan=15><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=157><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=100><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=60><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=83><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=50><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=50><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR class=datacell1>
							<TD colSpan=15 align=left><STRONG>Legend</STRONG>:&nbsp;<IMG title=Delete border=0 alt=Delete src="/assets/unmanaged/images/delete_icon.gif">&nbsp;Delete</TD>
						</TR>
						<!-- Start of body title -->
						<TR class=tablehead>
							<TD class=tableheadTD1 colSpan=15><TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
									<TBODY>
										<TR>
											<TD class=tableheadTD width="100%"><B><content:getAttribute id="alertTableTitle" attribute="text" filter="true"/></B></TD>
										</TR>
									</TBODY>
								</TABLE></TD>
						</TR>
						<!-- End of body title -->
						<TR class=tablesubhead>
							<TD vAlign=top width=30 rowspan=2><B>Action</B></TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2><B>Alert name</B><br>(Enter name of notice)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2><B>Notice due date</B><br>(mm/dd/yyyy)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top rowspan=2><B>Alert frequency</B><br></TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top colspan="2" rowspan=2><B>Alert timing</B><br>(Range: 7-60 days)</TD>
							<TD class=dataheaddivider vAlign=bottom width=1 rowspan=2><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD vAlign=top align="center" colspan=3><B>Message type</B></TD>
						</TR>
						<TR class=tablesubhead>
							<TD vAlign=top align="center"><B>Normal</B></TD>
							<TD class=dataheaddivider vAlign=bottom width=1>
							    <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
							</TD>
							<TD vAlign=top align="center"><B>Urgent</B></TD>
						</TR>
<c:if test="${not empty noticePrefForm.userNoticeManagerAlertList}">
<c:forEach items="${noticePrefForm.userNoticeManagerAlertList}" var="alertNotice" varStatus="index" >
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].contractId" />
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].profileId" />
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].alertId" />
<input type="hidden" name="tempId" value="${alertNotice.alertId}" />
<c:set var="indexValue" value="${index.index}"/> 
					    <% String temp = pageContext.getAttribute("indexValue").toString();
					    if (Integer.parseInt(temp) % 2 == 0) { %>
				        <tr class="datacell3 alertrows">
						<% } else { %>
				        <tr class="datacell1 alertrows">
						<% } %>
							<TD align=left><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /><IMG title=Delete border=0 id="Deleteimage" alt=Delete src="/assets/unmanaged/images/delete_icon.gif" ><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /></TD>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertName" disabled="true" maxlength="40" size="40" cssClass="values"/>
								</TD>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD><TABLE cellSpacing=0 cellPadding=0>
										<TBODY>
											<TR class="alertrows1">
												<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].stringStartDate" disabled="true" maxlength="10" size="10" cssClass="values" id="${index.index}"/>
												</TD>
												<TD vAlign=bottom>&nbsp;  <IMG border=0 src="/assets/unmanaged/images/cal.gif"> </TD>
											</TR>
										</TBODY>
									</TABLE></TD>
								<TD class=datadivider width=1>
								     <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
                                   <TD>
                                    <form:select path='${userNoticeManager}'  cssClass="values" disabled="true">
                                   	<form:option value="">Select</form:option>
<form:options items="${noticePrefForm.alertFrequencyCodes}" itemValue="lookupCode" itemLabel="lookupDesc"/>
</form:select>
								</TD>
								<TD class=datadivider width=1>
								   <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
								<TD>
<form:input path="userNoticeManagerAlertList[${index.index}].alertTimingCode"  disabled="true" size="2" cssClass="values"/>
								   
								</TD>
								<TD>
								    Days in advance
								</TD>
								<TD class=datadivider width=1>
								    <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
								<TD align="center">
<form:radiobutton disabled="true" path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="N"/>
								</TD>
								
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD align="center">
<form:radiobutton disabled="true" path="userNoticeManagerAlertList[${index.index}].alertUrgencyName" cssClass="values" id="mode" value="U"/>
								</TD>
								
						 </TR>
</c:forEach>
</c:if>
							<TR>
								<TD class=boxborder colspan="14"><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
							</TR>
							<TR class=datacell1>
							     <TD height="25" align=left colspan="14">
									<content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/> 
								</TD>
							</TR>
					</TBODY>
				</TABLE>
</c:if>
			</TD>
			<TD class=boxborder width=1>
			    <IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1>
			</TD>
		</TR>
		<TR>
			<TD colSpan=3>
	              <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
						<TBODY>
							<TR>
								<TD class=boxborder><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
							</TR>
						</TBODY>
				 </TABLE>
			 </TD>
		</TR>
		<tr>
		<td><br></br></td>
		</tr>
<c:if test="${noticePrefForm.alertsPageEnable ==true}">
<table width="708" border="0" cellspacing="0" cellpadding="1"><c:if test="${empty param.printFriendly}" > 
    <tr>
      <td width="385">
	      <div align="right">
	         <input class="button134" type="button" name="button1"
	            id="cancelButton" 
	            disabled="disabled" 
	            onmouseover="Tip(cancelLabel)" onmouseout="UnTip()"
	            value="cancel" onclick="return doConfirmAndSubmit(this.form, 'cancel');">
	      </div>
      </td>
      <td width="181">
        <div align="center">
	         <input class="button134" type="submit" name="button2" 
	            id="saveButton" 
	            disabled="disabled" 
	            onmouseover="Tip(saveLabel)" onmouseout="UnTip()"         
	            value="save" onclick="return doNoticeSubmit(this.form, 'save');">
        </div>
      </td>
      <td width="136">
        <div align="right">
	         <input class="button134" type="button" name="button3" 
	            id="saveAndFinishButton" 
	            disabled="disabled" 
	            onmouseover="Tip(saveFinishLabel)" onmouseout="UnTip()"
	            value="save &amp; finish" onclick="return doNoticeSubmit(this.form, 'finish');" >
        </div>
      </td>
    </tr></c:if>
</table>	
</c:if>
<c:if test="${noticePrefForm.alertsPageEnable == false}">	<table width="708" border="0" cellspacing="0" cellpadding="1"><c:if test="${empty param.printFriendly}" >
    <tr>
      <td width="385">
	      <div align="right">
	         <input class="button134" type="button" name="button1"
	            disabled="disabled" 
	            onmouseover="Tip(cancelLabel)" onmouseout="UnTip()"
	            value="cancel"  >
	      </div>
      </td>
      <td width="181">
        <div align="center">
	         <input class="button134" type="submit" name="button2" 
	            disabled="disabled" 
	            onmouseover="Tip(saveLabel)" onmouseout="UnTip()"         
	            value="save">
        </div>
      </td>
      <td width="136">
        <div align="right">
	         <input class="button134" type="button" name="button3" 
	            disabled="disabled" 
	            onmouseover="Tip(saveFinishLabel)" onmouseout="UnTip()"
	            value="save &amp; finish" >
        </div>
      </td>
    </tr></c:if>
</table>
</c:if></table>
</ps:form>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
         type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
         id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>										