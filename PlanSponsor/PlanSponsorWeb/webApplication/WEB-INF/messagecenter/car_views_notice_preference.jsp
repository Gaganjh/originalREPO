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
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants,com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@page import="com.manulife.util.render.RenderConstants"%> 
<%@page import="com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO"%>


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



var cancelLabel = '<content:getAttribute id="cancelLabel" attribute="text" filter="true"/>'
var saveLabel = '<content:getAttribute id="saveLabel" attribute="text" filter="true"/>'
var saveFinishLabel = '<content:getAttribute id="saveFinishLabel" attribute="text" filter="true"/>'
var alertNameLabel = '<content:getAttribute id="alertNameLabel" attribute="text" filter="true"/>'
var alertFrequencyLabel = '<content:getAttribute id="alertFrequencyLabel" attribute="text" filter="true"/>'
var alertDistributionDateLabel = '<content:getAttribute id="alertDistributionDateLabel" attribute="text" filter="true"/>'
//var alertTimingLabel = '<content:getAttribute id="alertTimingLabel" attribute="text" filter="true"/>'
var alertMessageTypeLabel = '<content:getAttribute id="alertMessageTypeLabel" attribute="text" filter="true"/>'
var addMaxAlertLabel = '<content:getAttribute id="addMaxAlertLabel" attribute="text" filter="true"/>' 
var addNewAlertLabel = '<content:getAttribute id="addNewAlertLabel" attribute="text" filter="true"/>'
var alertTableTitle = '<content:getAttribute id="alertTableTitle" attribute="text" filter="true"/>'



 	function enableButtons() {
    if (document.getElementById('saveAndFinishButton')) {
        document.getElementById('saveAndFinishButton').disabled=true;
    } // fi
    if (document.getElementById('saveButton')) {
        document.getElementById('saveButton').disabled=true;
    } // fi
    if (document.getElementById('cancelButton')) {
    	document.getElementById('cancelButton').disabled=true;
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

<ps:form name="noticePrefForm" modelAttribute="noticePrefForm" action="/do/mcCarView/viewNoticePreferences"  id="noticePrefForm">	
<table border="0" cellpadding="0" cellspacing="0" width="708">
	<tbody>
		<mc:carViewsPersonalizationTab active="noticePrefs" tpa="<%=(Boolean)request.getAttribute(MCConstants.AttrUserIdTpa)%>" noticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ALERT_NOITICE_PREFERENCE) %>"enableNoticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE) %>" />		
	</tbody>
		<TR>
		    <TD class=boxborder width=1><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
			<TD><!-- start table content -->
				<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR>
							<TD class=tableheadTD1 height=25 colSpan=15><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>
						<TR>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=280><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=100><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=100><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=30><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD width=50><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
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
<c:set var="indexValue" value="${index.index}"/> 
<% 				
	UserNoticeManagerAlertVO alertNotice = (UserNoticeManagerAlertVO)pageContext.getAttribute("alertNotice");
	String temp = pageContext.getAttribute("indexValue").toString();
%>
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].contractId" />
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].profileId"/>
<input type="hidden" name="noticePrefForm.userNoticeManagerAlert[${index.index}].alertId"/>
<input type="hidden" name="tempId" value="${alertNotice.alertId}" />
					    <% if (Integer.parseInt(temp) % 2 == 0) { %>
				        <tr class="datacell3 alertrows">
						<% } else { %>
				        <tr class="datacell1 alertrows">
						<% } %>
						<c:if test="${empty param.printFriendly }" >
							<TD align=left><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /><IMG title=Delete border=0 id="Deleteimage" alt=Delete src="/assets/unmanaged/images/delete_icon.gif" ><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /></TD>
						</c:if>
						<c:if test="${not empty param.printFriendly }" >
							<TD align=left><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /><IMG border=0 id="Deleteimage" src="/assets/unmanaged/images/delete_icon.gif" ><IMG src="/assets/unmanaged/images/s.gif" width=3 height=12 /></TD>
						</c:if>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD>
<form:input path="userNoticeManagerAlert[${index.index}].alertName"  disabled="true" maxlength="40" onmouseout="UnTip()" onmouseover="Tip(alertNameLabel)" size="40" cssClass="values"/>
								</TD>
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD><TABLE cellSpacing=0 cellPadding=0>
										<TBODY>
											<TR class="alertrows1">
												<TD>
<form:input path="userNoticeManagerAlert[${index.index}].stringStartDate"  disabled="true" maxlength="10" onmouseout="UnTip()" onmouseover="Tip(alertDistributionDateLabel)" size="10" cssClass="values" id="${index}"/>
												</TD>
												<TD vAlign=bottom>&nbsp;  <IMG border=0 src="/assets/unmanaged/images/cal.gif"> </TD>
											</TR>
										</TBODY>
									</TABLE></TD>
								<TD class=datadivider width=1>
								     <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
                                   <TD>
                                   
                                    <form:select path="userNoticeManagerAlert[${index.index}].alertFrequenceCode" onmouseover="Tip(alertFrequencyLabel)" onmouseout="UnTip()" cssClass="values" disabled="true">
                                   	<form:option value="">Select</form:option>
<form:options items="${noticePrefForm.alertFrequencyCodes}" itemValue="lookupCode" itemLabel="lookupDesc"/>
</form:select>
								</TD>
								<TD class=datadivider width=1>
								   <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
								<TD>
<form:input path="userNoticeManagerAlert[${index.index}].alertTimingCode" disabled="true" onmouseout="UnTip()" onmouseover="Tip(alertTimingLabel)" size="2" cssClass="values"/>
								   
								</TD>
								<TD>
								    Days in advance
								</TD>
								<TD class=datadivider width=1>
								    <IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
								</TD>
								<TD align="center">
								     <form:radiobutton value="N"  path="userNoticeManagerAlert[${index.index}].alertUrgencyName" onmouseover="Tip(alertMessageTypeLabel)" onmouseout="UnTip()" cssClass="values"  id="mode" disabled="true"/>
								</TD>
								
								<TD class=datadivider width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD align="center">
								     <form:radiobutton value="U"  path= "userNoticeManagerAlert[${index.index}].alertUrgencyName" onmouseover="Tip(alertMessageTypeLabel)" onmouseout="UnTip()" cssClass="values"  id="mode" disabled="true"/>
								</TD>
								
						 </TR>
								
</c:forEach>
</c:if>
							<TR>
								<TD class=boxborder colspan="14"><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
							</TR>
					</TBODY>
				</TABLE>
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
</TABLE>
<BR>
	<BR>
	
<table width="708" border="0" cellspacing="0" cellpadding="1">
    <tr>
      <td width="385">
	      <div align="left">
	        <input class="button134" type="button" name="button3" 
            id="back" 
            value="back" onclick="document.location='/do/mcdispatch/'" >
           
      <td width="385">
	      <div align="right">
	         <input class="button134" type="button" name="button1" id="cancelButton"
	            disabled="disabled" 
	            onmouseover="Tip(cancelLabel)" onmouseout="UnTip()"
	            value="cancel"  >
	      </div>
      </td>
      <td width="181">
        <div align="center">
	         <input class="button134" type="submit" name="button2" id="saveButton"
	            disabled="disabled" 
	            onmouseover="Tip(saveLabel)" onmouseout="UnTip()"         
	            value="save">
        </div>
      </td>
      <td width="136">
        <div align="right">
	         <input class="button134" type="button" name="button3" id="saveAndFinishButton" 
	            disabled="disabled" 
	            onmouseover="Tip(saveFinishLabel)" onmouseout="UnTip()"
	            value="save &amp; finish" >
        </div>
      </td>
    </tr>
	      </div>
      </td>
    </tr>
</table>	

</ps:form>





 													
