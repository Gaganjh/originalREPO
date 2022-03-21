<%@ tag
	import="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	import="com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel"
	import="com.manulife.pension.service.message.valueobject.MessageCategory"	
	import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
	import="com.manulife.pension.ps.web.Constants"	
	import="org.apache.commons.lang.StringEscapeUtils"
	import="com.manulife.pension.service.message.valueobject.RecipientMessageDetail"
	import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"
	import="com.manulife.pension.ps.web.content.ContentConstants" 	
 %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCHomePageBoxModel"
	required="true" rtexprvalue="true"%>

<c:set var="tab" value="${requestScope.userMessageCounts}"/>
<c:set var="role" value="${Constants.USERPROFILE_KEY}"/>

<content:contentBean contentId="<%=MCContentConstants.NoMessageLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="noMessageLabel" />

<content:contentBean contentId="<%=MCContentConstants.SummaryLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="summaryLabel" />

<content:contentBean contentId="<%=MCContentConstants.UrgentLabel%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="urgentLabel" />

<c:if test="${model.totalUrgentMessageCount gt '0'}">
<tr>
	<td class="boxborder">
	  <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
	</td>
	<td>
	<table cellpadding="0" cellspacing="0" width="100%">
		<tbody>
			<tr class="datacell1">
				<td class="tablesubhead" width="180">&nbsp;<content:getAttribute id="urgentLabel" attribute="text"/>
				
				<c:set var="uc" value="${fn:length(model.urgentMessages)}"/>
				<c:if test="${model.totalUrgentMessageCount gt uc}">
				   (${uc}&nbsp;of&nbsp;${model.totalUrgentMessageCount})
				</c:if>
				</td>
			</tr>
			<c:set var="rowNumber" value="1"/>
			<c:forEach var="message" items="${model.urgentMessages}" varStatus="loopStatus">
			<%
			   RecipientMessageDetail message = (RecipientMessageDetail) jspContext.getAttribute("message");
			%>
	   			<c:set var="rowNumber" value="${rowNumber + 1}" />
				<c:choose>
					<c:when test="${rowNumber % 2 == 1}">
						<c:set var="trStyleClass" value="datacell3" />
					</c:when>
					<c:otherwise>
						<c:set var="trStyleClass" value="datacell1" />
					</c:otherwise>
				</c:choose>

			<tr class="${trStyleClass}">
				<td valign="top">
				    	<table cellspacing="0" cellpadding="0" border="0"><tbody>
							<tr><td valign="top" style="padding-left: 5px;padding-right:5px;">&bull;</td>
							<td >			
								<c:choose>
									<c:when test="${MCUtils.isEligbileForAct(role, message)}">
									  <c:set var="url" value="${message.actionURL }"/>
									</c:when> 
								    <c:otherwise>
									  <c:set var="url" value="<%=model.getUrlGenerator().getRedirectSectionUrl(message.getSectionId()) %>"/>
								    </c:otherwise>				
								</c:choose>				
								 <a onmouseover="return Tip('<%=StringEscapeUtils.escapeHtml(message.getPublicLongText()) %>', WIDTH, 0)" onmouseout="UnTip()" href="${url}">${message.shortText}</a>
								 &nbsp; <c:if test="${message.escalated}">${message.escalationTag}</c:if>
							</td>
							</tr>
							</tbody>
							</table>
			     </td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</td>
	<td class="boxborder"><img
		src="/assets/unmanaged/images/s.gif" height="1" width="1">
	</td>
</tr>
</c:if>

<tr>
	<td class="boxborder"><img
		src="/assets/unmanaged/images/s.gif" height="1" width="1">
	</td>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tbody>
				<c:if test="${model.totalMessageCount gt '0' }">
				<tr>
					<td class="tablesubhead" width="145">&nbsp;<content:getAttribute id="summaryLabel" attribute="text"/>&nbsp;</td>
					<td class="tablesubhead" width="35">
					<div align="center"><strong>Total</strong></div>
					</td>
				</tr>
				</c:if>
				<c:set var="rowNumber" value="1"/>
				<c:set var="messageCount" value = "0"/>
				<c:forEach var="tab" items="${model.top.children}" >
				
				  <c:if test="${model.getTabMessageCount(tab) gt '0'}">
		   			<c:set var="rowNumber" value="${rowNumber + 1}" />
		   			<c:set var="messageCount" value = "1"/>
					<c:choose>
						<c:when test="${rowNumber % 2 == 1}">
							<c:set var="trStyleClass" value="datacell3" />
						</c:when>
						<c:otherwise>
							<c:set var="trStyleClass" value="datacell1" />
						</c:otherwise>
					</c:choose>
	
					<tr class="${trStyleClass}">
						<td valign="top" width="145">
						&nbsp;&nbsp;<a href="${model.getUrlGenerator().getTabUrl(tab)}">${tab.name}</a></td>
						<td valign="top" width="35">
						<c:if test="${model.getTabMessageCount(tab) gt '0' }">
							<div align="center">${model.getTabMessageCount(tab)}</div>
						</c:if>
						<c:if test="${model.getTabMessageCount(tab) eq '0' }">
							<div align="center"></div>
						</c:if>
						</td>
					</tr>
				</c:if>
				</c:forEach>
				<c:if test="${messageCount == '0'}">
				<tr class="datacell1">
				  <td colspan="3">
				     <content:getAttribute id="noMessageLabel" attribute="text"></content:getAttribute>
				  </td>
				</tr>
				</c:if>
			</tbody>
		</table>
	</td>
	<td class="boxborder">
	  <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
	</td>
</tr>
