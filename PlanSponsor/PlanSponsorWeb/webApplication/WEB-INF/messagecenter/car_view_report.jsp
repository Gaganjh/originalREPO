<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.service.message.report.valueobject.MessageReportData"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCCarViewUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.manulife.pension.service.message.valueobject.MessageRecipient"%>
<%@page import="com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType"%>
<%@page import="com.manulife.pension.service.message.valueobject.MessageTemplate"%>
<%@page import="com.manulife.pension.service.message.valueobject.Message"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@page import="com.manulife.pension.service.message.report.valueobject.MessageReportDetails" %>
<script type="text/javascript">
<!--  
   function gotoLink(url) {
   	 <c:if test="${empty param.printFriendly}">
   	    window.location=url;
   	 </c:if>
   	 return;
   }

//-->
</script>
<%
MessageReportData theReport = (MessageReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 




<content:contentBean contentId="<%=MCContentConstants.MoreDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetails" />	
<content:contentBean contentId="<%=MCContentConstants.MoreDetailsTitle%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetailsTitle" />	
<content:contentBean contentId="<%=MCContentConstants.LessDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="lessDetails" />		
                          

<table width="700" border="0" cellspacing="0" cellpadding="0">
	<!--DWLayoutTable-->
	<tr>
		<td colspan="4" valign="bottom"></td>
	</tr>


	<tr class="tableheadTD1">
		<td colspan="2" class="tableheadTD1">
		<table width="100%" border="0" align="right" cellpadding="1"
			cellspacing="0">
			<tbody>
				<tr>
					<td width="36%">&nbsp;</td>
					<td width="17%" align="right" nowrap>
					 <span class="style2" style="color:white;font-weight:bold"><b><report:recordCounter report="theReport" label="Messages"/></b></span>
					</td>
					<td width="47%" align="right" nowrap>
					  <span class="style2" style="color:white;font-weight:bold"><report:pageCounter report="theReport" formName="carViewForm"/></span></td>
				</tr>
			</tbody>
		</table>
		</td>
		<td colspan="2" rowspan="3" class="databorder"><img
			src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
	<tr valign="bottom">
		<td width="1" class="databorder">
		  <img height=1	src="/assets/unmanaged/images/s.gif" width=1>
		</td>
		<td width="700" >
		<table border="0" cellpadding="2" cellspacing="0" width="710">
			<tbody>
				<tr>
					<!-- buttons -->
					<c:if test="${not empty carViewForm.recipientId}">  
					<td class="tablesubhead" align="center" valign="middle"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="30"></td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>

					<!-- archive -->
					<td class="tablesubhead" align="center" valign="middle">
					<report:sort field="<%=MessageReportData.SORT_FIELD_ARCHIVED%>" direction="desc" formName="carViewForm"><b>Archived</b></report:sort>					  
					</td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					</c:if>
					<!-- contract number -->
					<c:if test="${ (not empty carViewForm.recipientId && sessionScope.carViewForm.allMessages == 'true') || (empty sessionScope.carViewForm.contractId  && not empty sessionScope.carViewForm.messageId)}">  
					<td width="60" class="tablesubhead" align="center" valign="middle">
						<report:sort field="<%=MessageReportData.SORT_FIELD_CONTRACT_NUMBER%>" direction="desc" formName="carViewForm"><b>Contract Number</b></report:sort>					  
					</td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					</c:if>
					
					<!-- posted -->
					<td width="60" class="tablesubhead" valign="middle">
					   <report:sort field="<%=MessageReportData.SORT_FIELD_EFFECTIVE_TS%>" direction="desc" formName="carViewForm"><b>Posted&nbsp;</b></report:sort>
					</td>
					<td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					
					
					<!-- recipeint priority -->
					<c:if test="${not empty carViewForm.recipientId}">
					<td width="20" class="tablesubhead" align="center" valign="middle">
					  <report:sort field="<%=MessageReportData.SORT_FIELD_PRIORITY%>" direction="desc" formName="carViewForm"><img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/></report:sort>
					</td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					</c:if>					

					<!-- details -->
					<td width="570" height="25" class="tablesubhead" valign="middle">
					   <report:sort field="<%=MessageReportData.SORT_FIELD_SHORT_TEXT%>" direction="asc" formName="carViewForm"><b>Details&nbsp;</b></report:sort>
					</td>
				</tr>				
				<c:choose>				
				   <c:when test="${theReport.totalCount == 0}">
						 <tr>
						   <td colspan="11" height="20">
						      Your search criteria produced no results. Please change your search criteria and try again.
						    </td>
						   </tr>
				   </c:when>
				   <c:otherwise>
				<c:forEach var="item" items="${theReport.details}" varStatus="loopStatus">
<c:set var="item" value="${item}" />
<% 				
	MessageReportDetails item = (MessageReportDetails)pageContext.getAttribute("item");
	pageContext.setAttribute("item",item,PageContext.PAGE_SCOPE);
%>

					<c:choose>
						<c:when test="${loopStatus.index % 2 == 1}">
							<c:set var="trStyleClass" value="datacell4" />
						</c:when>
						<c:otherwise>
							<c:set var="trStyleClass" value="datacell1" />
						</c:otherwise>
					</c:choose>
		        <tr class="${trStyleClass }">
					<!-- buttons -->
					<c:if test="${not empty carViewForm.recipientId}">  
		            <td>
						<c:choose>
							<c:when test="<%= item.getRecipientStatus() != null && MessageRecipient.RecipientStatus.VISITED.getValue().equals(item.getRecipientStatus())%>">
								<c:set var="textColor" value="${visitedMsgColor}" />
								<c:set var="actIcon" value="${visitedActIcon}" />
							</c:when>
							<c:otherwise>
								<c:set var="textColor" value="${newMsgColor}" />
								<c:set var="actIcon" value="${newActIcon}" />
							</c:otherwise>
						</c:choose>		            
					  <c:choose>
					     <c:when test="<%=(MessageTemplate.MessageActionType.DECLARE_COMPLETE.compareTo(item.getMessageActionType()) == 0) && (item.getArchived().equals(\"N\"))%>">
							<img src="/assets/unmanaged/images/JH-completed-grey.png" title="Complete" border="0">
					     </c:when>
					     <c:when test="<%=(MessageTemplate.MessageActionType.ACTION.compareTo(item.getMessageActionType()) == 0 )&& (item.getArchived().equals(\"N\") )%>">
								<img src="/assets/unmanaged/images/${actIcon}"  border="0">
					     </c:when>
					     <c:when test="<%=Message.MessageStatus.DONE.equals(MessageReportData.getMessageStatusCode(item.getMessageStatus()))%>">
							<img src="/assets/unmanaged/images/JH-completed-undo-grey.png"  border="0">
					     </c:when>
					     <c:when test="<%=Message.MessageStatus.ACTIVE.equals(MessageReportData.getMessageStatusCode(item.getMessageStatus())) && item.getRecipientStatus().equals(\"Y\") %>">
							<img src="/assets/unmanaged/images/JH-remove-undo-grey.png"  border="0">
					     </c:when>
					  </c:choose>
		            </td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>

					<!-- archive -->
					<td align="center">
						<c:if test="${item.archived == 'Y'}">
							<img src="/assets/unmanaged/images/checkmark.gif">
						</c:if>
					</td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					</c:if>
					
					<!-- contract number -->
					<c:if test="${(not empty carViewForm.recipientId && sessionScope.carViewForm.allMessages == 'true') || (empty sessionScope.carViewForm.contractId  && not empty sessionScope.carViewForm.messageId)}">  
					<c:choose>
					<c:when test="${ not empty item.tpaFirmId}">
					<td valign="top" onmouseover="Tip('TPA Firm ID: ${item.tpaFirmId}')" onmouseout="UnTip()">
					</c:when>
					<c:otherwise>
					<td valign="top" >
					</c:otherwise>
					</c:choose>
					<c:out value="${item.contractId}"/>
					</td>
					<td width="1" class="dataheaddivider"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					</c:if>
					
					<!-- posted -->
		            <td valign="top" 
       	               onmouseover="Tip('<fmt:formatDate value="${item.effectiveTs}" pattern="<%=MCConstants.PostedDateFormat%>" />')"
		               onmouseout="UnTip()">	
				    <!-- Well, Ie doesn't respect the nowrap or style white-space:nowrap. so they're screwed. -->
					<fmt:formatDate value="${item.effectiveTs}" pattern="MMM" />&nbsp;<fmt:formatDate value="${item.effectiveTs}" pattern="dd" />,&nbsp;<fmt:formatDate value="${item.effectiveTs}" pattern="yyyy" />
		            </td>
					<td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					
					<!-- recipeint priority -->
					<c:if test="${not empty carViewForm.recipientId}">
		            <td align="center" valign="top">		              
					   <c:if test="<%=MCUtils.isUrgent(item.getPriority())%>">
					     <img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/>
					   </c:if>
		            </td>
					<td width="1" class="dataheaddivider"></td>
					</c:if>					

					<!-- details -->
		            <td onmouseover="Tip('<%=StringEscapeUtils.escapeJavaScript(MCCarViewUtils.getMessageStatusText(request, item)) %>')"
		               onmouseout="UnTip()"
		               valign="top">
						<c:set var="anchorName" value="Message_${item.messageId}" />
						<span id="${anchorName}"/>		            
		               <b>
							<c:if test="${empty param.printFriendly}">
		               			<a href="/do/mcCarView/messageDetail?messageId=${item.messageId}&contractId=${item.contractId}">
							</c:if>
		               			${item.shortText}
							<c:if test="${empty param.printFriendly}">
		               			</a>
							</c:if>
						</b>
	 					<c:if test="${item.escalated}">${item.escalationTag}</c:if>
						<br>
		               ${item.longText}
						<c:if test="${not empty item.additionalDetails}">
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${empty param.printFriendly}"><span id="moreDetailsAnchor${item.messageId}">&lt;<a href="javascript:void(0)" onclick="moreDetails(${item.messageId})"><content:getAttribute id='moreDetails' attribute='text' filter="true"/></a>&gt;</span></c:if>
						  <c:choose>
						  	<c:when test="${empty param.printFriendly}">
							  <div id="moreDetailsDiv${item.messageId}" style="display:none">
						  	</c:when>
						  	<c:otherwise>
						  	  <div id="moreDetailsDiv${item.messageId}">
						  	</c:otherwise>
						  </c:choose>
						  	<br/>
						  	<div><b><content:getAttribute id='moreDetailsTitle' attribute='text' filter="true"/></b></div>
						  	<br/>
						  	<div>${item.additionalDetails}</div>
						  	<br/>
						  	<div>&lt;<a href="javascript:void(0)" onclick="lessDetails(${item.messageId})"><content:getAttribute id='lessDetails' attribute='text' filter="true"/></a>&gt;</div>
						  	<br/>
						  </div>
						</c:if>			
		            </td>					

		        </tr>
				
				</c:forEach>
				</c:otherwise>				
				</c:choose>
				
                <tr>
                     <td colspan="11" class="boxborder" height="1"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
                     </td>
                </tr>
             </tbody>
        </table>
       </td>       
      </tr>
      <tr>
        <td class="databorder" width="1" colspan="4">
        </td>
      </tr>
      <tr>
		     <td colspan="4" valign="top">
		       <img src="/assets/unmanaged/images/s.gif" width="1" height="1">		     
			  	 <table width="100%" border="0" align="right" cellpadding="1" cellspacing="0">
					<tbody>
						<tr>
							<td width="39%">&nbsp;</td>
							<td width="41%" align="right" nowrap>
							<div align="left"><strong><report:recordCounter report="theReport" label="Messages"></report:recordCounter> </strong></div>
							</td>
							<td align="right" nowrap>
							  <span class="style2"><report:pageCounter report="theReport" formName="carViewForm"/></span>
							</td>
						</tr>
					</tbody>
			    </table>
			  <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
	       </td>
</tr>
</table>
