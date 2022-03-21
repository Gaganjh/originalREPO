<%@tag import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ tag
	import="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	import="com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel"
	import="com.manulife.pension.ps.web.messagecenter.MCConstants"
	import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
	import="com.manulife.pension.service.message.valueobject.MessageContainer"
	import="com.manulife.pension.service.message.valueobject.RecipientMessageDetail"	
	import="com.manulife.pension.service.message.valueobject.MessageRecipient"
	import="org.apache.commons.lang.StringUtils"
	import="com.manulife.pension.ps.web.Constants"
	import="com.manulife.pension.ps.web.util.SessionHelper"
	import="org.apache.commons.lang.StringEscapeUtils"
	import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"
	import="com.manulife.pension.ps.web.content.ContentConstants"
	import="com.manulife.pension.service.security.role.UserRole"	
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel"
	required="true" rtexprvalue="true"%>

<%@ attribute name="section"
	type="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	required="true" rtexprvalue="true"%>

<%@ attribute name="displayHeader" type="java.lang.Boolean"
	required="true" rtexprvalue="true"%>

<% 

UserProfile userProfile =(UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
UserRole role=(UserRole)userProfile.getRole();
getJspContext().setAttribute("role",role,PageContext.PAGE_SCOPE);

%> 

<content:contentBean contentId="<%=MCContentConstants.MoreDetailsTitle%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetailsTitle" />	
<content:contentBean contentId="<%=MCContentConstants.LessDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="lessDetails" />		
                     
<c:choose>
 <c:when test="${model.multiContract}">
   <c:set var="detailColWidth" value="400"/>
   <c:set var="moreColWidth" value="12"/>
 </c:when>
 <c:otherwise>
   <c:set var="detailColWidth" value="575"/>
   <c:set var="moreColWidth" value="8"/>
 </c:otherwise>
</c:choose>

<c:if test="<%=model.getSectionMessageCount(section) != 0%>">
    <script type="text/javascript">
        addSectionIds(${section.id.value});
    </script>
	<c:set var="sectionExpand" value="<%=model.isSectionExpand(section) %>" />   
    <c:set var="sectionAnchor" value="<%= model.getUrlGenerator().getSectionAnchorName(section)%>"/>    
	<a name="${sectionAnchor}"/>
	<table width="755" border="0" cellspacing="0" cellpadding="1">
	<tr height="25">
	  <td class="tableheadTD">
	<c:choose>
		<c:when test="${sectionExpand}">
			<a
				href="javascript:gotoLink('<%=model.getUrlGenerator().getExpandSectionUrl(model.getSelectedTab(), section, false) %>')">
			<img src="/assets/unmanaged/images/minus_icon.gif" width="13"
				height="13" border="0"></a>
		</c:when>
		<c:otherwise>
			<a
				href="javascript:gotoLink('<%=model.getUrlGenerator().getExpandSectionUrl(model.getSelectedTab(), section, true) %>')">
			<img src="/assets/unmanaged/images/plus_icon.gif" width="13"
				height="13" border="0"></a>
		</c:otherwise>
	</c:choose>
	<b>
	 ${section.name }
	</b> <c:out value="<%=model.getSectionCount(section)%>" />
	</td>
	<td class="tableheadTD" align="right">
		  <c:if test="<%=model.showLessForSection(section) && !model.isPrintFriendly()%>">
			   <a href="javascript:gotoLink('<%=model.getUrlGenerator().getShowAllSectionUrl(model.getSelectedTab(), section, false) %>')"><font color="#ffffff"><content:getAttribute id='lessLabel' attribute='text' filter="true"/></font></a>&nbsp;
		  </c:if>	
	</td>
	</tr>
	</table>

	<c:if test="${sectionExpand}">
		<table cellSpacing=0 cellPadding=0 width="754" border=0>
		<tbody>
		<tr class=tablesubhead>
		<td class=tablesubhead vAlign=middle noWrap width=25>&nbsp;</td>
		<td class=dataheaddivider vAlign=top width=1>
		<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
		<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
		</td>
		<!--  arrow_triangle_up.gif arrow_triangle_down.gif-->
		<td class=tablesubhead vAlign=middle noWrap width=20 align="center">
		<b>
		<a
			href="javascript:gotoLink('<%=model.getUrlGenerator().getSortingUrl(model, section, MCConstants.PriorityAttrName)%>')">
			<img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/></a>
		</b>
		<mc:sortingIcon name="<%= MCConstants.PriorityAttrName%>"
			sectionPref="<%= model.getPreference().getSectionPreference(section)%>" />
		</td>
		<td class=dataheaddivider vAlign=middle width=1>
		<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
		</td>
        <c:if test="${model.multiContract}">
		<td class=tablesubhead vAlign=middle noWrap width=60>
		<b>
		<a
			href="javascript:gotoLink('<%=model.getUrlGenerator().getSortingUrl(model, section, MCConstants.ContractIDAttrName)%>')">Contract number</a>
		</b>
		<mc:sortingIcon name="<%=MCConstants.ContractIDAttrName%>"
			sectionPref="<%= model.getPreference().getSectionPreference(section)%>" />
		</td>

		<td class=dataheaddivider vAlign=middle width=1>
		<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
		</td>
		<td class=tablesubhead vAlign=middle noWrap width=100>
		<b>
		<a
			href="javascript:gotoLink('<%=model.getUrlGenerator().getSortingUrl(model, section, MCConstants.ContractNameAttrName)%>')">Name</a>
		</b>
		<mc:sortingIcon name="<%=MCConstants.ContractNameAttrName%>"
			sectionPref="<%= model.getPreference().getSectionPreference(section)%>" />
		</td>


		<td class=dataheaddivider vAlign=middle width=1>
		<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
		</td>
        
        </c:if>
		<td class=tablesubhead vAlign=middle noWrap width=60>
		<b>
		<a
			href="javascript:gotoLink('<%=model.getUrlGenerator().getSortingUrl(model, section, MCConstants.PostedTsAttrName)%>')">Posted</a>
		</b>
		<mc:sortingIcon name="<%=MCConstants.PostedTsAttrName%>"
			sectionPref="<%= model.getPreference().getSectionPreference(section)%>" />

		</td>
		<td class=dataheaddivider vAlign=middle width=1>
		</td>
		<td colspan="2" class=tablesubhead vAlign=middle width="560">
		<b>
		<a
			href="javascript:gotoLink('<%=model.getUrlGenerator().getSortingUrl(model, section, MCConstants.ShortTextAttrName)%>')">Details</a>
		</b>
		<mc:sortingIcon name="<%=MCConstants.ShortTextAttrName%>"
			sectionPref="<%= model.getPreference().getSectionPreference(section)%>" />
		</td>
		</tr>
		<!-- Start of Details -->
		<c:forEach var="message" items="<%= model.getMessages(section)%>" varStatus="loopStatus">
			<% RecipientMessageDetail message = (RecipientMessageDetail) jspContext.getAttribute("message"); %>
 		  <c:set var="message" value="${message}" scope="page"/>
			<c:choose>
				<c:when test="${loopStatus.index % 2 == 1}">
					<c:set var="trStyleClass" value="datacell4" />
				</c:when>
				<c:otherwise>
					<c:set var="trStyleClass" value="datacell1" />
				</c:otherwise>
			</c:choose>

			<c:choose>
			<c:when test="<%= message.getMessageRecipientStatus() != null && MessageRecipient.RecipientStatus.VISITED.compareTo(message.getMessageRecipientStatus()) == 0%>">
					<c:set var="textColor" value="${visitedMsgColor}" />
					<c:set var="actIcon" value="${visitedActIcon}" />
				</c:when>
				<c:otherwise>
					<c:set var="textColor" value="${newMsgColor}" />
					<c:set var="actIcon" value="${newActIcon}" />
				</c:otherwise>
			</c:choose>

			<tr class="${trStyleClass}" id="Message_${section.id.value}_${message.id}">
				<td class="${trStyleClass}" width=18 align="center" valign="top">
					  <c:choose>
					     <c:when test="<%=MCUtils.isEligbileForDone(role, message) %>">
					       <a href="javascript:gotoLink('${model.getUrlGenerator().getCompleteMessageUrl(model.getSelectedTab(), section, message)}')">
					       
								<img src="/assets/unmanaged/images/JH-completed.png" title="<content:getAttribute id='completeHoverOver' attribute='text' filter="true"/>" border="0"></a>
					     </c:when>
					     <c:when test="<%=MCUtils.isEligbileForAct(role, message)%>">
							<a href="javascript:gotoLink('${model.getUrlGenerator().getActionUrl(model.getSelectedTab(), section, message)}')">
							<img src="/assets/unmanaged/images/${actIcon}" title="${message.actionURLDescription}" border="0"></a>
					     </c:when>
					  </c:choose>
				</td>
			<td class=datadivider width=1>
				<img height=1 src="/assets/unmanaged/images/s.gif" width="1">
			</td>
			<td class="${trStyleClass}" width="20" valign="top" align="center">
			   <c:if test="<%=MCUtils.isUrgent(message)%>">
			     <img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/>
			   </c:if>
			</td>
			<td class=datadivider width=1>
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
			</td>
			<c:if test="${model.multiContract}">
			<c:if test="${not empty message.tpaFirmId}">
			<td class="${trStyleClass}" valign="top" align="left" onmouseover="Tip('TPA Firm ID: 	${message.tpaFirmId}')" onmouseout="UnTip()">
			</c:if>
			<c:if test="${empty message.tpaFirmId}">
			<td class="${trStyleClass}" valign="top" align="left" >
			</c:if>
			  ${message.contractId}
			</td>
			<td class=datadivider width=1>
				<img height=1 src="/assets/unmanaged/images/s.gif" width="1">
			</td>
			<c:if test="${not empty message.tpaFirmId}">
			<td class="${trStyleClass}" valign="top" align="left" onmouseover="Tip('TPA Firm ID: 	${message.tpaFirmId}')" onmouseout="UnTip()">
			</c:if>
			<c:if test="${empty message.tpaFirmId}">
			<td class="${trStyleClass}" valign="top" align="left" >
			</c:if>
			  ${message.contractName}
			</td>
			<td class=datadivider width=1>
				<img height=1 src="/assets/unmanaged/images/s.gif" width="1">
			</td>			
			</c:if>
			<td style="color: ${textColor}" class="${trStyleClass}" width="50" valign="top" 
			     onmouseover="return Tip('<fmt:formatDate value="${message.effectiveTs}" pattern="<%=MCConstants.PostedDateFormat%>" /><br/><c:if test="${message.escalated}"><fmt:formatDate value="${message.escalatedTs}" pattern="<%=MCConstants.EscalatedDateFormat%>" /></c:if>')"
			     onmouseout="UnTip()">
				<!-- Well, Ie doesn't respect the nowrap or style white-space:nowrap. so they're screwed. -->
				<fmt:formatDate value="${message.effectiveTs}" pattern="MMM" />&nbsp;<fmt:formatDate value="${message.effectiveTs}" pattern="dd" />,&nbsp;<fmt:formatDate value="${message.effectiveTs}" pattern="yyyy" />
			</td>
			<td class=datadivider width=1>
				<img height=1 src="/assets/unmanaged/images/s.gif" width=1>
			</td>
			<td style="color: ${textColor}" class="${trStyleClass}" width="${detailColWidth}" valign="top" 
			onmouseover="return Tip('Message id: ${message.id}')" onmouseout="UnTip()">
				<c:set var="anchorName" value="Message_${section.id.value}_${message.id}" />
				<span id="${anchorName}"/>
			    <b><c:out value="${message.shortText}"/></b>&nbsp;<c:if test="${message.escalated}">${message.escalationTag}</c:if><br>
				${message.longText}
				<c:if test="${not empty message.additionalDetails}">
				  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				  <span id="moreDetailsAnchor${message.id}">&lt;<a  href="javascript:void(0)" onclick="moreDetails(${message.id})"><content:getAttribute id='moreDetails' attribute='text' filter="true"/></a>&gt;</span>
						  <div id="moreDetailsDiv${message.id}" style="display:none">
						  	<br/>
						  	<div><b><content:getAttribute id='moreDetailsTitle' attribute='text' filter="true"/></b></div>
						  	<br/>
						  	<div>${message.additionalDetails}</div>
						  	<br/>
						  	<div>&lt;<a href="javascript:void(0)" onclick="lessDetails(${message.id})"><content:getAttribute id='lessDetails' attribute='text' filter="true"/></a>&gt;</div>
						  	<br/>
						  </div>				     
				</c:if>
			</td>
            <td width="15" valign="top">
               <c:if test="<%=!model.isPrintFriendly()%>">
	             <a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); gotoLink('${model.getUrlGenerator().getRemoveMessageUrl(model.getSelectedTab(), section, message) }')">
	               <img src="/assets/unmanaged/images/JH-remove.png" title="<content:getAttribute id='removeHoverOver' attribute='text' filter="true"/>" border="0"></a>
               </c:if>
            </td>			
		</tr>
		</c:forEach>
		<c:choose>
		  <c:when test="<%=model.showMoreForSection(section) && !model.isPrintFriendly()%>">
			<tr>
				<td colspan="${moreColWidth}" align="right">
				   <a href="javascript:gotoLink('<%=model.getUrlGenerator().getShowAllSectionUrl(model.getSelectedTab(), section, true) %>')"><content:getAttribute id='moreLabel' attribute='text' filter="true"/></a>&nbsp;
				</td>
			</tr>
		  </c:when>
		  <c:when test="<%=model.showLessForSection(section) && !model.isPrintFriendly()%>">
			<tr>
				<td colspan="${moreColWidth}" align="right">
				   <a href="javascript:gotoLink('<%=model.getUrlGenerator().getShowAllSectionUrl(model.getSelectedTab(), section, false) %>')"><content:getAttribute id='lessLabel' attribute='text' filter="true"/></a>&nbsp;
				</td>
			</tr>
		  </c:when>
		</c:choose>
		<tr>
		  <td colspan="${moreColWidth}" class="databorder"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
		</tr>
		</tbody>
	  </table>
	</c:if>
</c:if>