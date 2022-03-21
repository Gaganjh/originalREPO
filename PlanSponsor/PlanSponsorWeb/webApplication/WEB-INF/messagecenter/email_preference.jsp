<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
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

<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%><script type="text/javascript">

<%
String monday=WeeklyDay.MONDAY.getValue();
String tuesday=WeeklyDay.TUESDAY.getValue();
String wednesday=WeeklyDay.WEDNESDAY.getValue();
String thursday=WeeklyDay.THURSDAY.getValue();
String friday=WeeklyDay.FRIDAY.getValue();
pageContext.setAttribute("monday",monday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("tuesday",tuesday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("wednesday",wednesday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("thursday",thursday,PageContext.PAGE_SCOPE);
pageContext.setAttribute("friday",friday,PageContext.PAGE_SCOPE);

%>
<!--
   function setFieldStatus(frm, change) {
      for (var i = 0; i < frm.normalMessageFrequency.length; i++) {
        if (frm.normalMessageFrequency[i].value == '<%=MCEmailPreferenceForm.NormalWeekly%>') {
           if ( !frm.normalMessageFrequency[i].checked) {
           	  frm.weekDay.disabled = true;
           	} else {
     	      frm.weekDay.disabled = false;           	    
           	}
          } else if (frm.normalMessageFrequency[i].value == '<%=MCEmailPreferenceForm.NormalBiWeekly%>') {
           if (!frm.normalMessageFrequency[i].checked) {
           		frm.startingDate.disabled = true;
            } else {
            	frm.startingDate.disabled = false;
            	if (frm.startingDate.value == "" && change) {
            	  frm.startingDate.value = '<fmt:formatDate value="${sessionScope.emailPrefForm.defaultStartingBiweeklyDate}" pattern="MM/dd/yyyy"/>'
            	}
            }
           }
      }
   }
<content:contentBean contentId="<%=MCContentConstants.CancelHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cancelLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveFinishHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveFinishLabel" />


var cancelLabel = '<content:getAttribute id="cancelLabel" attribute="text" filter="true"/>'
var saveLabel = '<content:getAttribute id="saveLabel" attribute="text" filter="true"/>'
var saveFinishLabel = '<content:getAttribute id="saveFinishLabel" attribute="text" filter="true"/>'

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



<content:errors scope="session"/>

<ps:form action="/do/messagecenter/personalizeEmail" name="emailPrefForm" modelAttribute="emailPrefForm">
<input type="hidden" name="action" value="save"/>
<table border="0" cellpadding="0" cellspacing="0" width="708">
	<tbody>
		<mc:personalizationTab active="email" global="<%=(Boolean)MCUtils.isInGlobalContext(request)%>" tpa="<%=SessionHelper.getUserProfile(request).getRole().isTPA() %>" noticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ALERT_NOITICE_PREFERENCE) %>" enableNoticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE) %>" />
        <tr>
           <td width="1" class="boxborder">
              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           </td>
           <td width="699">
              <table border="0" cellpadding="3" cellspacing="0" width="710">
                 <tbody>
                   <tr>
                     <td height="25" colspan="9" class="tableheadTD">
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
                         <%String urgentdaily=MCEmailPreferenceForm.UrgentDaily;
                         pageContext.setAttribute("urgentdaily",urgentdaily,PageContext.PAGE_SCOPE);
                         %> 
                          
<form:radiobutton path="urgentMessageFrequency" value="${urgentdaily}"/>
                          </td><td>
							once a day
                          </td>
                          </tr>
                         </table>
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
                         <%String urgentimmediate=MCEmailPreferenceForm.UrgentImmediately;
                         pageContext.setAttribute("urgentimmediate",urgentimmediate,PageContext.PAGE_SCOPE);
                         %> 
<tr><td valign="top"><form:radiobutton path="urgentMessageFrequency" value="${urgentimmediate}"/></td>
                          <td>immediately (plus once a day)</td>
                          </tr>
                         </table>
                      </td>
                      <ps:trackChanges  name="emailPrefForm" property="urgentMessageFrequency" escape="true"/>
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
                         	 <% String normal = MCEmailPreferenceForm.NormalDaily;
                         	pageContext.setAttribute("normal",normal,PageContext.PAGE_SCOPE);
                         	 %>
<form:radiobutton onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${normal}"/>
							 </td><td>daily</td></tr>
                             </table>
						 </td>
                         <td align="left" colspan="2">
                         	  <table cellspacing="0" cellpadding="0" border="0">
                         	  <%String normalweek=MCEmailPreferenceForm.NormalWeekly;
                         	 pageContext.setAttribute("normalweek",normalweek,PageContext.PAGE_SCOPE);
                         	  %>
<tr><td valign="top"><form:radiobutton onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${normalweek}"/></td>
                              <td>weekly on</td>
	                          <ps:trackChanges name="emailPrefForm" property="normalMessageFrequency" escape="true"/>
							  <td>
	                             <img src="/assets/unmanaged/images/s.gif" width="1" height="0">                             
	                             &nbsp;&nbsp; <form:select path="weekDay">
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
		                      <ps:trackChanges name="emailPrefForm" property="weekDay" escape="true"/>
	                     </td>
                   </tr>
                   <tr class="datacell3">
                         <td align="left" valign="top" colspan="2">
                         <table cellspacing="0" cellpadding="0">
                         <%String biweek=MCEmailPreferenceForm.NormalBiWeekly;
                         pageContext.setAttribute("biweek",biweek,PageContext.PAGE_SCOPE);
                         %>
<tr><td valign="top"><form:radiobutton onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${biweek}"/>
                         	<td>biweekly starting</td>
<td>&nbsp;&nbsp;<form:input path="startingDate" size="10"/>
                               <ps:trackChanges name="emailPrefForm" property="startingDate" escape="true"/>
                             </td>
                             <td valign="bottom">&nbsp;
                               <a href="javascript:doStartingDateCal('startingDate')">
                                <img src="/assets/unmanaged/images/cal.gif" border="0">
                               </a>
                             </td></tr>
                          </table>
                          </td>                   
						 <td valign="top" colspan="2">
						 <% String never=MCEmailPreferenceForm.NormalNever;
						 pageContext.setAttribute("never",never,PageContext.PAGE_SCOPE);
						 %>
<form:radiobutton onclick="javascript:setFieldStatus(this.form, true)" path="normalMessageFrequency" value="${never}"/>never</td>
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
<tr><td valign="top"><form:radiobutton path="emailWithNoMessage" value="Y"></form:radiobutton></td>
                          <td >send a summary email anyway</td>
                          </tr>
                         </table>
                        
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
<tr><td valign="top"><form:radiobutton path="emailWithNoMessage" value="N"></form:radiobutton></td>
                          <td>do <b>not</b> send a summary email</td>
                          </tr>
                         </table>
                      </td>
                      <ps:trackChanges name="emailPrefForm" property="emailWithNoMessage" escape="true"/>
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
                        <%String emailformatval=EmailPreference.EmailFormat.HTML.getValue();
                        pageContext.setAttribute("emailformatval",emailformatval,PageContext.PAGE_SCOPE);
                        %>
<tr><td valign="top"><form:radiobutton path="emailFormat" value="${emailformatval}"></form:radiobutton></td>
                          <td><span title="suggested format - includes tables and colors">HTML</span></td>
                          </tr>
                         </table>
                        
                     </td>
                     <td align="left" colspan="2">
                        <table cellspacing="0" cellpadding="0" border="0">
                         <%String emailformattextval=EmailPreference.EmailFormat.TEXT.getValue();
                        pageContext.setAttribute("emailformattextval",emailformattextval,PageContext.PAGE_SCOPE);
                        %>
<tr><td valign="top"><form:radiobutton path="emailFormat" value="${emailformattextval}"></form:radiobutton></td>
                          <td><span title="if your email application doesn't support HTML">plain text</span></td>
                          </tr>
                         </table>
                      </td>
                      <ps:trackChanges name="emailPrefForm" property="emailFormat" escape="true"/>
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
      <td width="385"><div align="right">
         <input class="button134" type="button" name="button1"
            id="cancelButton" 
            disabled="disabled" 
            onmouseover="Tip(cancelLabel)" onmouseout="UnTip()"
            value="cancel" onclick="return doConfirmAndSubmit(this.form, 'cancel')">
      </div></td>
      <td width="181">
        <div align="center">
         <input class="button134" type="submit" name="button2" 
            id="saveButton" 
            disabled="disabled" 
            onmouseover="Tip(saveLabel)" onmouseout="UnTip()"         
            value="save" onclick="return doSubmit(this.form, 'save')">
        </div>
      </td>
      <td width="136">
        <div align="right">
         <input class="button134" type="button" name="button3" 
            id="saveAndFinishButton" 
            disabled="disabled" 
            onmouseover="Tip(saveFinishLabel)" onmouseout="UnTip()"
            value="save &amp; finish" onclick="return doSubmit(this.form, 'finish')" >
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
