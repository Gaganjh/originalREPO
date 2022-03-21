<%@ attribute name="mcSummary" type="com.manulife.pension.service.message.report.valueobject.BDMessageCenterSummary" rtexprvalue="true" required="true"%>
<%@ attribute name="enablePreferencesLink" type="java.lang.Boolean" rtexprvalue="true" required="true"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>	
<%@ tag import="com.manulife.util.render.RenderConstants" %>

<%@tag import="com.manulife.pension.bd.web.util.JspHelper"%>

<% try { %>
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="messageCenterTitle" />
<content:contentBean contentId="<%=BDContentConstants.UNREAD_MESSAGES_LABEL%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
id="unreadMessagesLabel" />	
<content:contentBean contentId="<%=BDContentConstants.CHANGE_MY_PREFERENCES_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
id="preferencesText" />	
<h4>	
	<img src="/assets/unmanaged/images/icons/mailappt.jpg" width="26" height="26" hspace="5" align="left" /> 
	<a href="/do/messagecenter/"><content:getAttribute attribute="text" beanName="messageCenterTitle"/></a><br />
</h4>

<p>
	<content:getAttribute attribute="text" beanName="unreadMessagesLabel"> 
		<content:param>
			(${mcSummary.unreadMessageCount})
		</content:param>
	</content:getAttribute>
</p>
<!-- To avoid a script error in IE6 -->
<c:if test="${not empty mcSummary.topMessages}">
<ul>
	<c:forEach var="message" items="${mcSummary.topMessages}" varStatus="status">
		<li class="news_item">
			<p><strong><a class="message_shortText" href="/do/messagecenter/">${message.shortText}</a></strong></p>
			<p><a href="/do/messagecenter/"><render:date property="message.effectiveDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"/></a></p>
			<c:if test="${not empty message.contractName}">
				<p><a href="/do/messagecenter/">${message.contractName}</a></p>
			</c:if>
		</li>
	</c:forEach>
</ul>
</c:if>
<c:choose>
	<c:when test="${enablePreferencesLink}">
		<a href="/do/myprofile/preference" class="profile"><content:getAttribute attribute="text" beanName="preferencesText" /></a>
	</c:when>
	<c:otherwise>
		<span class="profile"><content:getAttribute attribute="text" beanName="preferencesText" /></span>
	</c:otherwise>
</c:choose>
	
<% } catch (Exception e) {
	 JspHelper.log("messageCenter.tag", e, "fails"); 
   }
%>
	