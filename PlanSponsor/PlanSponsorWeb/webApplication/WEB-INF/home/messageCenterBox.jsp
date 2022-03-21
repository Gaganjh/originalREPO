<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
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

<table border="0" cellpadding="0" cellspacing="0" width="180">
	<tbody>
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1"></td>
			<td width="178"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="178"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1"></td>
		</tr>

		<tr class="tableheadTD2" style="cursor: pointer;" onclick="javascript:document.location='<%=MCConstants.DispatchUrl%>'">
			<td colspan="4" style="padding-left:6px;">
				<table id="dgtab1" border="0" width="100%" cellspacing="0" cellpadding="0" ><tbody><tr><td style="padding-left:0px;">
				<font color="#ffffff"><content:getAttribute id="messageCenterLabel" attribute="text"/></font>
                </td><td style="text-align:right;padding-right:5px;width:35px;text-align:center;">
				<a href="<%=MCConstants.SummaryTabUrl%>"><font color="#ffffff"><content:getAttribute id="viewLabel" attribute="text"/></font></a>
                </td></tr>
                </tbody></table>
           </td>
		</tr>

        <mc:summaryBox model="${requestScope.mcModel}"/>
		<tr>
			<td colspan="3">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tbody>
					<tr>
						<td class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>
<br>

<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>