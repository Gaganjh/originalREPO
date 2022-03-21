<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<style>
<!--
.tableheadTD2 {
	background-color:#002D62;
	background-position:left top;
	background-repeat:no-repeat;
	color:#FFFFFF;
	font-family:Arial,Helvetica,sans-serif;
	font-size:12px;
	height:20px;
	padding-left:8px;
}

.urgentMessageStyle {
	color: #ffffff;
	font-weight: bold;
}

-->
</style>

<content:contentBean contentId="<%=MCContentConstants.MessageCenterLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="messageCenterLabel" />

<content:contentBean contentId="<%=MCContentConstants.ViewLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="viewLabel" />

<c:set var="counts" value="${requestScope.userMessageCounts}"/>
<c:if test="${not empty counts}">
	  <table width="175" border="0" cellpadding="0" cellspacing="0" class="box" valign="top">
         <tbody>
           <tr class="tablehead">
             <td colspan="5" class="tableheadTD2" style="cursor: pointer;" 
		    	onclick="javascript:document.location='<%=MCConstants.DispatchUrl%>'">
               <font color="#ffffff"><content:getAttribute id="messageCenterLabel" attribute="text"/></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
				href="<%=MCConstants.SummaryTabUrl%>"><font color="#ffffff"><content:getAttribute id="viewLabel" attribute="text"/></font></a>				
             </td>
           </tr>
           <tr>
             <td width="1" rowspan="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
             <td width="33%" class="tablesubhead"><div align="center"><strong>Urgent</strong></div></td>
             <td width="33%" class="tablesubhead"><div align="center"><strong>Action</strong></div></td>

             <td width="33%" class="tablesubhead"><div align="center"><strong>FYI</strong></div></td>
             <td width="1" rowspan="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
           </tr>
           <tr>
             <td width="33%" class="boxbody"><div align="center">${requestScope.userMessageCounts[0][0]}</div></td>
             <td width="33%" class="boxbody"><div align="center">${requestScope.userMessageCounts[0][1]}</div></td>
             <td width="33%" class="boxbody"><div align="center">${requestScope.userMessageCounts[0][2]}</div></td>

           </tr>
           <tr>
             <td height="1" colspan="5" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
           </tr>
         </tbody>
       </table>				  
</c:if>