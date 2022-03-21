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
	color:#FFFFFF;
	font-family:Arial,Helvetica,sans-serif;
	font-size:12px;
	height:20px;
	padding-left:8px;
}

.tableheadStart{
	background-repeat:no-repeat;
	background-image:url(/assets/unmanaged/images/box_ul_corner.gif);
}
.borderLeft {
	border-left: 1px solid #002D62; 
}
.borderRight {
	border-right: 1px solid #002D62; 
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
	  <table border="0" width="250" cellpadding="0" cellspacing="0" valign="top">
         <tbody>
           <tr class="tablehead">
             <td colspan="3" class="tableheadTD2" style="cursor: pointer;" 
		    	onclick="javascript:document.location='<%=MCConstants.DispatchUrl%>'">
               <font color="#ffffff"><content:getAttribute id="messageCenterLabel" attribute="text"/></font>
             </td>
             <td style="text-align:right;padding-right:10px" class="tableheadTD2">
               <a href="<%=MCConstants.SummaryTabUrl%>"><font color="#ffffff"><content:getAttribute id="viewLabel" attribute="text"/></font></a>
             </td>
           </tr>
           <tr>
           	 <td class="borderLeft tablesubhead">&nbsp;</td>
             <td width="22%" class="tablesubhead"><div align="center"><strong>Urgent</strong></div></td>
             <td width="22%" class="tablesubhead"><div align="center"><strong>Action</strong></div></td>
             <td width="22%" class="borderRight tablesubhead"><div align="center"><strong>FYI</strong></div></td>
           </tr>
           <tr>
			 <td nowrap="nowrap" class="borderLeft boxbody"><span style="padding-left:3px;font-weight:bold;font-size:9pt;"">Contract messages</span></td>
             <td width="22%" class="boxbody"><div align="center">${requestScope.userMessageCounts[0][0]}</div></td>
             <td width="22%" class="boxbody"><div align="center">${requestScope.userMessageCounts[0][1]}</div></td>
             <td width="22%" class="borderRight boxbody"><div align="center">${requestScope.userMessageCounts[0][2]}</div></td>

           </tr>
           <tr>
			 <td nowrap="nowrap"  class="borderLeft boxbody" ><span style="padding-left:3px;font-weight:bold;font-size:9pt;">TPA Firm messages</span></td>
             <td width="22%" class="boxbody"><div align="center">${requestScope.userMessageCounts[1][0]}</div></td>
             <td width="22%" class="boxbody"><div align="center">${requestScope.userMessageCounts[1][1]}</div></td>
             <td width="22%" class="borderRight boxbody"><div align="center">${requestScope.userMessageCounts[1][2]}</div></td>

           </tr>
           <tr>
             <td height="1" colspan="5" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
           </tr>
         </tbody>
       </table>				  
</c:if>