<%@ tag
	import="com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel"
	import="com.manulife.pension.service.message.valueobject.RecipientMessageDetail"
	import="com.manulife.pension.ps.web.messagecenter.MCConstants"
	import="com.manulife.pension.service.message.valueobject.MessageRecipient"
	import="org.apache.commons.lang.StringEscapeUtils"
	import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
	import="com.manulife.pension.ps.web.Constants"
	import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"
	import="com.manulife.pension.ps.web.content.ContentConstants"
	import="com.manulife.pension.service.security.role.UserRole"
	import="com.manulife.pension.ps.web.controller.UserProfile"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel"
	required="true" rtexprvalue="true"%>

<style>
<!--
.tableheadTD2 {
	PADDING-LEFT: 8px;
	FONT-SIZE: 12px;
	COLOR: #ffffff;
	FONT-FAMILY: Arial, Helvetica, sans-serif;
	background-color: #002D62;
	background-repeat: no-repeat;
	background-position: left top;
	height: 20px;
}

.tableheadTD3 {
	PADDING-LEFT: 8px;
	FONT-SIZE: 12px;
	COLOR: #ffffff;
	FONT-FAMILY: Arial, Helvetica, sans-serif;
	background-color: #002D62;
	background-repeat: no-repeat;
	background-position: left top;
	height: 20px;
}

.urgentMessageStyle {
	color: #ffffff;
}

-->
</style>

<c:choose>
 <c:when test="${model.multiContract}">
   <c:set var="detailColWidth" value="430"/>
   <c:set var="moreColWidth" value="10"/>
 </c:when>
 <c:otherwise>
   <c:set var="detailColWidth" value="590"/>
   <c:set var="moreColWidth" value="6"/>
 </c:otherwise>
</c:choose>

<content:contentBean contentId="<%=MCContentConstants.MoreDetailsTitle%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetailsTitle" />	
<content:contentBean contentId="<%=MCContentConstants.LessDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="lessDetails" />		
 <% 
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
 UserRole role = (UserRole)userProfile.getRole();
getJspContext().setAttribute("role",role,PageContext.PAGE_SCOPE);

%>                        

<c:if test="<%=model.getUrgentSectionMessageCount() > 0%>">
      <table cellSpacing="0" cellPadding="0" width="710" border="0">
        <!--DWLayouttable-->
        <tbody>
          <tr>
            <td width="1"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
            <td colSpan="2" width="708"><img height="1" src="/assets/unmanaged/images/s.gif" width="708"></td>
            <td width="1"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          </tr>
          <tr>
          
            <td colSpan=4 class=tableheadtd2>
              <table width="100%">
                <tr>
                 <td>
		              <span class="urgentMessageStyle">
		            	<b>Urgent messages</b>
		                <c:out value="<%=model.getUrgentSectionCount() %>" />
		              </span>
		            </td>
		            <td align="right" colSpan="2" class="tableheadtd3">
			            <c:if test="<%=model.showLessForUrgentSection() && !model.isPrintFriendly()%>">
						  <a href="javascript:gotoLink('<%= model.getUrlGenerator().getUrgentMessageShowAllUrl(false)%>')"><font color="#ffffff"><content:getAttribute id='lessLabel' attribute='text' filter="true"/></font></a> &nbsp;
						</c:if>            
		            </td>
		          </tr>
		       </table>
		    </td>
		  </tr>
          <tr vAlign=bottom>
            <td class=databorder width=1>
              <img height=1 src="/assets/unmanaged/images/s.gif" width=1>
            </td>
            <td width="100%" colspan=2>
            <table cellSpacing=0 cellPadding=0 width="710" border=0>
              <tbody>
                <tr class=tablesubhead>
                  <td class=tablesubhead vAlign=top noWrap width=18>&nbsp;</td>
                  <td class=dataheaddivider vAlign=top width=1><IMG height=1 
                    src="/assets/unmanaged/images/s.gif"  width=1></td>
                  <c:if test="${model.multiContract}">
                     <td class=tablesubhead vAlign=top noWrap width=60>
	                    <b>
							<a href="javascript:gotoLink('<%=model.getUrlGenerator().getUrgentMessageSortingUrl(model, MCConstants.ContractIDAttrName)%>')">Contract number</a>
						</b>
						<mc:sortingIcon name="<%=MCConstants.ContractIDAttrName%>" sectionPref="<%= model.getPreference().getSectionPreference(MCConstants.UrgentMessageSection)%>" />                  
                     </td>
	                 <td class=dataheaddivider vAlign=top width=1>
	                    <IMG height=1 src="/assets/unmanaged/images/s.gif"  width=1>
	                 </td>
                     <td class=tablesubhead vAlign=top noWrap width=100>
	                    <b>
							<a href="javascript:gotoLink('<%=model.getUrlGenerator().getUrgentMessageSortingUrl(model, MCConstants.ContractNameAttrName)%>')">Name</a>
						</b>
						<mc:sortingIcon name="<%=MCConstants.ContractNameAttrName%>" sectionPref="<%= model.getPreference().getSectionPreference(MCConstants.UrgentMessageSection)%>" />                  
                     </td>
	                 <td class=dataheaddivider vAlign=top width=1>
	                    <IMG height=1 src="/assets/unmanaged/images/s.gif"  width=1>
	                 </td>
                  </c:if>  
                  <td class=tablesubhead vAlign=top noWrap width=60>
                    <b>
						<a href="javascript:gotoLink('<%=model.getUrlGenerator().getUrgentMessageSortingUrl(model, MCConstants.PostedTsAttrName)%>')">Posted</a>
					</b>
					<mc:sortingIcon name="<%=MCConstants.PostedTsAttrName%>" sectionPref="<%= model.getPreference().getSectionPreference(MCConstants.UrgentMessageSection)%>" />                  
                  </td>
                  <td class=dataheaddivider vAlign=top width=1>
                    <IMG height=1 src="/assets/unmanaged/images/s.gif"  width=1>
                  </td>
                  <td class=tablesubhead vAlign=top width="${detailColWidth}">
                   <b>
						<a href="javascript:gotoLink('<%=model.getUrlGenerator().getUrgentMessageSortingUrl(model, MCConstants.ShortTextAttrName)%>')">Details</a>
					</b>
					<mc:sortingIcon name="<%=MCConstants.ShortTextAttrName%>" sectionPref="<%= model.getPreference().getSectionPreference(MCConstants.UrgentMessageSection)%>" />
				  </td>
                  <td class=tablesubhead vAlign=top width=15>&nbsp;</td>
                </tr>
				<c:forEach var="message" items="<%= model.getMessages(MCConstants.UrgentMessageSection)%>" varStatus="loopStatus">
				<% RecipientMessageDetail message = (RecipientMessageDetail) jspContext.getAttribute("message"); %>
				<c:choose>
					<c:when test="<%= message.getMessageRecipientStatus().equals(MessageRecipient.RecipientStatus.VISITED)%>">
					<c:set var="textColor" value="${visitedMsgColor}" />
					<c:set var="actIcon" value="${visitedActIcon}" />
				</c:when>
				<c:otherwise>
					<c:set var="textColor" value="${newMsgColor}" />
					<c:set var="actIcon" value="${newActIcon}" />
				</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${loopStatus.index % 2 == 1}">
						<c:set var="trStyleClass" value="datacell4" />
					</c:when>
					<c:otherwise>
						<c:set var="trStyleClass" value="datacell1" />
					</c:otherwise>
				</c:choose>

                <!-- Start of Details -->
                <tr class="${trStyleClass}" id="Message_0_${message.id}">
                  <td width=18 align="center" valign="top" class="${trStyleClass}">
                  	  <c:choose>
					     <c:when test="<%=MCUtils.isEligbileForDone(role, message)%>">
					        <a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); gotoLink('<%= model.getUrlGenerator().getCompleteMessageUrl(null, null, message) %>')">
								<img src="/assets/unmanaged/images/JH-completed.png" title="<content:getAttribute id='completeHoverOver' attribute='text' filter="true"/>" border="0"></a>
					     </c:when>
					     <c:when test="<%=MCUtils.isEligbileForAct(role, message) %>">
					     	<a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); gotoLink('<%= model.getUrlGenerator().getActionUrl(null, null, message) %>')">
								<img src="/assets/unmanaged/images/${actIcon}" title="${message.actionURLDescription}" border="0"></a>
					     </c:when>
					  </c:choose>
                  </td>
                  <td class=datadivider width=1>
                    <img height="1" src="/assets/unmanaged/images/s.gif" width="1">
                   </td>
				  <c:if test="${model.multiContract}">
				  	<c:if test="${not empty message.tpaFirmId}">
					  <td style="color: ${textColor}" class="${trStyleClass}" valign="top" onmouseover="Tip('TPA Firm ID: 	${message.tpaFirmId}')" onmouseout="UnTip()">
				  	</c:if>
				  	<c:if test="${empty message.tpaFirmId}">
					  <td style="color: ${textColor}" class="${trStyleClass}" valign="top">
				  	</c:if>
					 ${message.contractId }
				  </td>
                  <td class=datadivider width=1>
                    <img height="1" src="/assets/unmanaged/images/s.gif" width="1">
                   </td>
				  	<c:if test="${not empty message.tpaFirmId}">
					  <td style="color: ${textColor}" class="${trStyleClass}" valign="top" onmouseover="Tip('TPA Firm ID: 	${message.tpaFirmId}')" onmouseout="UnTip()">
				  	</c:if>
				  	<c:if test="${empty message.tpaFirmId}">
					  <td style="color: ${textColor}" class="${trStyleClass}" valign="top">
				  	</c:if>
					 ${message.contractName }
				  </td>				  
                  <td class=datadivider width=1>
                    <img height="1" src="/assets/unmanaged/images/s.gif" width="1">
                   </td>				  
				  </c:if>                  
				  <td style="color: ${textColor}" class="${trStyleClass}" width="50" valign="top"
				    onmouseover="Tip('<fmt:formatDate value="${message.effectiveTs}" pattern="<%=MCConstants.PostedDateFormat%>" />')"
				    onmouseout="UnTip()" 
				    >
				    <!-- Well, Ie doesn't respect the nowrap or style white-space:nowrap. so they're screwed. -->
					<fmt:formatDate value="${message.effectiveTs}" pattern="MMM" />&nbsp;<fmt:formatDate value="${message.effectiveTs}" pattern="dd" />,&nbsp;<fmt:formatDate value="${message.effectiveTs}" pattern="yyyy" />
				  </td>
                  <td class=datadivider width=1>
                    <img height=1 src="/assets/unmanaged/images/s.gif" width=1>
                   </td>
                  <td style="color: ${textColor}" class="${trStyleClass}" width="590" valign="top" 
                  onmouseover="return Tip('Message id: ${message.id}')"
                  onmouseout="UnTip()"
                  >
				     <c:set var="anchorName" value="Message${message.id}" />
	                 <span id="${anchorName}"/>                                  
                    <b><c:out value="${message.shortText}"/></b>&nbsp;<c:if test="${message.escalated}">${message.escalationTag}</c:if> <br>
                    ${message.longText}
                   	<c:if test="${not empty message.additionalDetails}">
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${empty param.printFriendly}"><span  id="moreDetailsAnchor${message.id}">&lt;<a href="javascript:void(0)" onclick="moreDetails(${message.id})"><content:getAttribute id='moreDetails' attribute='text' filter="true"/></a>&gt;</span></c:if>
						  <c:choose>
						  	<c:when test="${empty param.printFriendly}">
							  <div id="moreDetailsDiv${message.id}" style="display:none">
						  	</c:when>
						  	<c:otherwise>
						  	  <div id="moreDetailsDiv${message.id}">
						  	</c:otherwise>
						  </c:choose>
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
                    <c:if test="${!model.printFriendly}">
	                   <a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); gotoLink('<%= model.getUrlGenerator().getRemoveMessageUrl(null, null, message) %>')">
	                     <img src="/assets/unmanaged/images/JH-remove.png" title="<content:getAttribute id='removeHoverOver' attribute='text' filter="true"/>" border="0"></a>
	                </c:if>
                  </td>
                </tr>
                </c:forEach>
                <c:choose>
	                <c:when test="<%=model.showMoreForUrgentSection() && !model.isPrintFriendly()%>">
						<tr>
							<td colspan="${moreColWidth}" align="right">
							   <a href="javascript:gotoLink('<%= model.getUrlGenerator().getUrgentMessageShowAllUrl(true)%>')"><content:getAttribute id='moreLabel' attribute='text' filter="true"/></a> &nbsp;
							</td>
						</tr>
					</c:when>
	                <c:when test="<%=model.showLessForUrgentSection() && !model.isPrintFriendly()%>">
						<tr>
							<td colspan="${moreColWidth}" align="right">
							   <a href="javascript:gotoLink('<%=model.getUrlGenerator().getUrgentMessageShowAllUrl(false)%>')"><content:getAttribute id='lessLabel' attribute='text' filter="true"/></a> &nbsp;
							</td>
						</tr>
					</c:when>
                </c:choose>
              </tbody>
            </table>
            </td>
            <td class=databorder width=1>
              <IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
            </td>
          </tr>
          <tr>
            <td class=databorder colSpan=4><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
          </tr>
        </tbody>
      </table>
      <br/>
      <br/>
</c:if>
