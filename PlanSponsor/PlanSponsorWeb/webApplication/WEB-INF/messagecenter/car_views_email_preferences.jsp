<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@page import="com.manulife.pension.ps.web.messagecenter.personalization.MCEmailPreferenceForm"%>
<%@page import="com.manulife.pension.service.message.valueobject.EmailPreference.WeeklyDay"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>

<%@page import="com.manulife.pension.service.message.valueobject.EmailPreference.EmailFormat"%>
<%@page import="com.manulife.pension.service.message.valueobject.EmailPreference"%>

<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%><script type="text/javascript">

<%
String monday=WeeklyDay.MONDAY.getValue();
String tuesday=WeeklyDay.TUESDAY.getValue();
String wednesday=WeeklyDay.WEDNESDAY.getValue();
String thursday=WeeklyDay.THURSDAY.getValue();
String friday=WeeklyDay.FRIDAY.getValue();
String UrgentDaily=MCEmailPreferenceForm.UrgentDaily;
String UrgentImmediately=MCEmailPreferenceForm.UrgentImmediately;
String NormalDaily=MCEmailPreferenceForm.NormalDaily;
String NormalWeekly=MCEmailPreferenceForm.NormalWeekly;
String NormalBiWeekly=MCEmailPreferenceForm.NormalBiWeekly;
String NormalNever=MCEmailPreferenceForm.NormalNever;
String emailFormatHTML=EmailPreference.EmailFormat.HTML.getValue();
String emailFormatTEXT=EmailPreference.EmailFormat.TEXT.getValue();
pageContext.setAttribute("monday",monday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("tuesday",tuesday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("wednesday",wednesday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("thursday",thursday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("friday",friday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("UrgentDaily",UrgentDaily,PageContext.PAGE_SCOPE);
pageContext.setAttribute("UrgentImmediately",UrgentImmediately,PageContext.PAGE_SCOPE);
pageContext.setAttribute("NormalDaily",NormalDaily,PageContext.PAGE_SCOPE);
pageContext.setAttribute("NormalWeekly",NormalWeekly,PageContext.PAGE_SCOPE);
pageContext.setAttribute("NormalBiWeekly",NormalBiWeekly,PageContext.PAGE_SCOPE);
pageContext.setAttribute("emailFormatHTML",emailFormatHTML,PageContext.PAGE_SCOPE);
pageContext.setAttribute("emailFormatTEXT",emailFormatTEXT,PageContext.PAGE_SCOPE);
pageContext.setAttribute("NormalNever",NormalNever,PageContext.PAGE_SCOPE);
%>
<!--
   function setFieldStatus(frm, change) {
      for (var i = 0; i < frm.normalMessageFrequency.length; i++) {
        if (frm.normalMessageFrequency[i].value == '<%=MCEmailPreferenceForm.NormalWeekly%>') {

          } else if (frm.normalMessageFrequency[i].value == '<%=MCEmailPreferenceForm.NormalBiWeekly%>') {
           if (!frm.normalMessageFrequency[i].checked) {
            } else {
            	if (frm.startingDate.value == "" && change) {
            	  frm.startingDate.value = '<fmt:formatDate value="${sessionScope.emailPrefForm.defaultStartingBiweeklyDate}" pattern="MM/dd/yyyy"/>'
            	}
            }
           }
      }
   }



//-->
</script>


<content:contentBean contentId="<%=MCContentConstants.PersonalizationFor%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="personalizationFor" />
<content:contentBean contentId="<%=MCContentConstants.PersonalizationIntroText%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="introText" />

<div style="padding-left:5px;">
<content:errors scope="session"/>
<content:getAttribute beanName="introText" attribute="text"/> 
</div>
<br/>                                          
<ps:form name="emailPrefForm" modelAttribute="emailPrefForm" action="/do/mcCarView/viewEmailPreferences" >
<input type="hidden" name="action" value="save"/>
<div style="padding-left:5px;font-weight:bold;">
<content:getAttribute beanName="personalizationFor" attribute="text"/> ${emailPrefForm.firstName} ${emailPrefForm.lastName}
</div>

<table border="0" cellpadding="0" cellspacing="0" width="708">
	<tbody>
		<mc:carViewsPersonalizationTab active="email" tpa="<%=(Boolean)request.getAttribute(MCConstants.AttrUserIdTpa)%>" noticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ALERT_NOITICE_PREFERENCE) %>" enableNoticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE) %>" />
        <tr>
           <td width="1" class="boxborder">
              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           </td>
           <td width="699">
              <table border="0" cellpadding="3" cellspacing="0" width="710">
                 <tbody>
                   <tr>
                     <td height="25" colspan="9" class="tableheadTD1">
                       <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
                       <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
                       <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                   </tr>
                   <tr class="datacell1">
                     <td width="200" valign="middle">
                        Send <i>urgent priority</i> emails
                     </td>
                      <td width="1" class="datadivider">
                        <img src="/assets/unmanaged/images/s.gif" width="1" height="0">
                      </td>
                     <td width="200" align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
                          <tr><td valign="top" >
<form:radiobutton disabled="true" path="urgentMessageFrequency" value="${UrgentDaily}"/>
                          </td><td>
							once a day
                          </td>
                          </tr>
                         </table>
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" path="urgentMessageFrequency" value="${UrgentImmediately}"/></td>
                          <td>immediately (plus once a day)</td>
                          </tr>
                         </table>
                      </td>
                      </tr>
                      <tr class="datacell3">
                         <td width="200" valign="top" rowspan="2">
                            Send summary emails
                         </td>
                      <td width="1" class="datadivider" rowspan="2">
                        <img src="/assets/unmanaged/images/s.gif" width="1" height="0">
                      </td>
                         <td width="200" align="left" valign="top" colspan="2">
                         	 <table cellspacing="0" cellpadding="0" border="0"><tr><td valign="top">
<form:radiobutton disabled="true" onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${NormalDaily}"/>
							 </td><td>daily</td></tr>
                             </table>
						 </td>
                         <td align="left" colspan="2">
                         	  <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${NormalWeekly}"/></td>
                              <td>weekly on</td>
							  <td>
	                             <img src="/assets/unmanaged/images/s.gif" width="1" height="0">                             
	                             &nbsp;&nbsp; <form:select  disabled="true" path="weekDay">
	                              <!-- need more consontration on this not tested -->
	                                <form:option value="${monday}">Monday</form:option>
	                                <form:option value="${tuesday}">Tuesday</form:option>
	                                <form:option value="${wednesday}">Wednesday</form:option>
	                                <form:option value="${thursday}">Thursday</form:option>
	                                <form:option value="${friday}">Friday</form:option>
	                                 <!-- need more consontration on this not tested -->
</form:select>
							  </td></tr>
                              </table>
	                     </td>
                   </tr>
                   <tr class="datacell3">
                         <td align="left" valign="top" colspan="2">
                         <table cellspacing="0" cellpadding="0">
<tr><td valign="top"><form:radiobutton disabled="true" onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${NormalBiWeekly}"/></td>
                         	<td>biweekly starting</td>
<td>&nbsp;&nbsp;<form:input path="startingDate" disabled="true" size="10"/>
                             </td>
                             <td valign="bottom">&nbsp;
                                <img src="/assets/unmanaged/images/cal.gif" border="0">
                             </td></tr>
                          </table>
                          </td>                   
						 <td valign="top" colspan="2">
<form:radiobutton disabled="true" path="normalMessageFrequency" value="${NormalNever}" onclick="javascript:setFieldStatus(this.form, true)"/>never
                         </td>
                   </tr>
                   <tr class="datacell1">
                     <td width="200" valign="top">
                       When there are no messages in my Message Center...
                     </td>
                      <td width="1" class="datadivider">
                        <img src="/assets/unmanaged/images/s.gif" width="1" height="0">
                      </td>
                     <td  align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" path="emailWithNoMessage" value="Y"></form:radiobutton></td>
                          <td >send a summary email anyway</td>
                          </tr>
                         </table>
                        
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" path="emailWithNoMessage" value="N"></form:radiobutton></td>
                          <td>do <b>not</b> send a summary email</td>
                          </tr>
                         </table>
                      </td>
                      </tr> 
                     <tr class="datacell3">
                     <td width="200" valign="top">
                       Email format
                     </td>
                      <td width="1" class="datadivider">
                        <img src="/assets/unmanaged/images/s.gif" width="1" height="0">
                      </td>
                     <td  align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" path="emailFormat" value="${emailFormatHTML}"></form:radiobutton></td>
                          <td><span title="suggested format - includes tables and colors">HTML</span></td>
                          </tr>
                         </table>
                        
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton disabled="true" path="emailFormat" value="${emailFormatTEXT}"></form:radiobutton></td>
                          <td><span title="if your email application doesn't support HTML">plain text</span></td>
                          </tr>
                         </table>
                      </td>
                      </tr>   
                </tbody>
              </table>
             </td>
             <td width="1" class="boxborder">
                 <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
             </td>
        </tr>
        <tr>
           <td colspan="3">
             <table border="0" cellpadding="0" cellspacing="0" width="100%">
               <tbody>
                 <tr>
                   <td class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                 </tr>
                </tbody>
              </table>
            </td>
         </tr>
       </tbody>
</table>
<br>
<br>
<table width="708" border="0" cellspacing="0" cellpadding="1">
    <tr>
      <td width="385">
        <div align="left">
         <input class="button134" type="button" name="button3" 
            id="back" 
            value="back" onclick="document.location='/do/mcdispatch/'" >
        </div>
       </td>
      <td width="181">&nbsp;</td>
      <td width="136">
        <div align="right">
          <c:if test="${emailPrefForm.userCanEdit}">
            <input class="button134" type="button" name="button3" 
              id="edit" 
              value="edit" onclick="document.location='/do/mcCarView/editEmailPreferences?userProfileId=<%=request.getParameter("userProfileId")%>&contractId=<%=request.getParameter("contractId") %>'" >
          </c:if>  
        </div>
      
      </td>
    </tr>
</table>	
</ps:form>

<script type="text/javascript">
<!--
    setFieldStatus(document.forms['emailPrefForm'], false);

var validDates = ${emailPrefForm.validDatesForJavaScript} <%-- scope="session" --%>;
	var startDate = validDates[0];
	var endDate = validDates[validDates.length-1];
	var currentDate = validDates[0];
	var cal = new calendar(document.forms['emailPrefForm'].elements['startingDate'], startDate.valueOf(), endDate.valueOf(), validDates);
	cal.year_scroll = false;
	cal.time_comp = false;
			
   function doStartingDateCal(fieldName) {
      var frm = document.forms['emailPrefForm'];
      if (frm.startingDate.disabled) {
        return;
      }
	   var v = frm.elements['startingDate'].value;
	   if (v == null || v.length == 0) {	   
	      frm.elements['startingDate'].value = "<fmt:formatDate value='${sessionScope.emailPrefForm.defaultStartingBiweeklyDate}' pattern='MM/dd/yyyy'/>";
	   }
	   cal.popup();
	}

//-->
</script>

  <script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
