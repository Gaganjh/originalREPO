<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.service.message.report.valueobject.MessageHistoryReportData"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCHistoryUtils"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>


<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>

<%@page import="com.manulife.pension.service.message.report.valueobject.MessageHistoryDetails" %>
<%@page import="com.manulife.pension.service.message.valueobject.Message.MessageStatus"%>
<%@page import="com.manulife.pension.service.message.valueobject.Message"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<content:contentBean contentId="<%=MCContentConstants.MoreDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetails" />	
<content:contentBean contentId="<%=MCContentConstants.MoreDetailsTitle%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="moreDetailsTitle" />	
<content:contentBean contentId="<%=MCContentConstants.LessDetailsLink%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="lessDetails" />		                     
                     
<script type="text/javascript">
<!--
   function undo(action, messageId) {
     <c:if test="${empty param.printFriendly}">
      document.forms['messageHistoryForm'].messageId.value = messageId;
      document.forms['messageHistoryForm'].task.value = action;
      document.forms['messageHistoryForm'].submit();
     </c:if>
   }
   
   function gotoLink(url) {
   	 <c:if test="${empty param.printFriendly}">
   	    window.location=url;
   	 </c:if>
   	 return;
   }

//-->
</script>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_EMPLOYEES_NO_SEARCH_RESULTS%>"
                    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults"/>

<%
MessageHistoryReportData theReport = (MessageHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 





<input type="hidden" name="messageId"/>

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
					  <span class="style2"n style="color:white;font-weight:bold"><report:pageCounter report="theReport" formName="messageHistoryForm"/></span></td>
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
					<td width="30" class="tablesubhead">&nbsp;</td>
					<td width="1" class="dataheaddivider">
					  <img 	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="20" class="tablesubhead" align="center" valign="middle">
					  <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_PRIORITY%>" direction="desc" formName="messageHistoryForm"><img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/></report:sort>
					</td>
					<td width="1" class="dataheaddivider">
					   <img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<c:if test="<%=MCUtils.isInGlobalContext(request)%>">
					<td  class="tablesubhead" align="center" valign="middle" nowrap>
					  <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_CONTRACT_NUMBER%>" direction="desc" formName="messageHistoryForm"><b>Contract number&nbsp;</b> </report:sort>
					</td>
					<td width="1" class="dataheaddivider">
					   <img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td  class="tablesubhead" align="center" valign="middle" nowrap>
					  <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_CONTRACT_NAME%>" direction="desc" formName="messageHistoryForm"><b>Name&nbsp;</b> </report:sort>
					</td>
					<td width="1" class="dataheaddivider">
					   <img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					</c:if>
					<td width="60" class="tablesubhead" valign="middle">
					   <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_EFFECTIVE_TS%>" direction="desc" formName="messageHistoryForm"><b>Posted&nbsp;</b></report:sort>
					</td>
					<td width="1" class="dataheaddivider">
					  <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="60" class="tablesubhead" valign="middle">
					   <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_MESSAGE_STATUS%>" direction="desc" formName="messageHistoryForm"><b>Status&nbsp;</b></report:sort>
					</td>
					<td width="1" class="dataheaddivider">
					  <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>					
					<td width="590" height="25" class="tablesubhead" valign="middle">
					   <report:sort field="<%=MessageHistoryReportData.SORT_FIELD_SHORT_TEXT%>" direction="asc" formName="messageHistoryForm"><b>Details&nbsp;</b></report:sort>
					</td>
				</tr>				
				<c:choose>				
				   <c:when test="${theReport.totalCount == 0}">
				 <tr>
				   <td colspan="19" height="20">
				      <content:getAttribute id="noSearchResults" attribute="text"/>
				    </td>
				   </tr>
				   </c:when>
				   <c:otherwise>
				<c:forEach var="item" items="${theReport.details}" varStatus="loopStatus">
<c:set var="item" value="${item}" />
 <% MessageHistoryDetails item=(MessageHistoryDetails)pageContext.getAttribute("item");
pageContext.setAttribute("item", item, pageContext.PAGE_SCOPE);
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
		            <td valign="top" width="30">
		              <div align="center">
				               <c:choose>
		                 		<c:when test="${item.undoable}">
				                  <a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); undo('undoComplete', ${item.messageId})">
									<img src="/assets/unmanaged/images/JH-completed-undo.png" title="Undo complete" border="0"/></a>
				                 </c:when>
				                 <c:when test="${item.unhiddenable}">
				                  <a href="javascript:void(0)" onclick="this.removeAttribute('href');this.removeAttribute('onclick'); undo('unhide', ${item.messageId})">
				                    <img src="/assets/unmanaged/images/JH-remove-undo.png" title="Undo remove" border="0"/></a>
				                 </c:when>
			                 	<c:when test="${!item.unhiddenable && !item.unhiddenable}">
				                 <img height="1"  src="/assets/unmanaged/images/s.gif" width="30">
				                 </c:when>		                 
				               </c:choose>
		              </div>
		            </td>
		            <td class="datadivider">
		              <img height=1  src="/assets/unmanaged/images/s.gif" width=1>
		            </td>
		            <td align="center" valign="top">	
					   <c:if test="<%=MCUtils.isUrgent(item.getPriority())%>">
					     <img src="/assets/unmanaged/images/icon_urgent.png" title="Urgent" border="0"/>
					   </c:if>
		            </td>
		            <td class="datadivider">
		              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		            </td>
					<c:if test="<%=MCUtils.isInGlobalContext(request)%>">
						<c:if test="${not empty item.tpaFirmId}">
			            <td align="center" valign="top" onmouseover="Tip('TPA Firm ID: 	${item.tpaFirmId}')" onmouseout="UnTip()">
		           		</c:if>
		           		<c:if test="${empty item.tpaFirmId}">
		           		<td align="center" valign="top">
		           	     </c:if>	
		            	<c:out value="${item.contractId}"/>	              
		            </td>
		            <td class="datadivider">
		              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		            </td>
						<c:if test="${not empty item.tpaFirmId}">
			            <td align="center" valign="top" onmouseover="Tip('TPA Firm ID: 	${item.tpaFirmId}')" onmouseout="UnTip()">
		           		</c:if>
		           		<c:if test="${empty item.tpaFirmId}">
		           		<td align="center" valign="top">
		           	     </c:if>	
		            	<c:out value="${item.contractName}"/>	              
		            </td>
		            <td class="datadivider">
		              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		            </td>
				    </c:if>			            
		            <td valign="top" 
		               onmouseover="Tip('<fmt:formatDate value="${item.effectiveTs}" pattern="<%=MCConstants.PostedDateFormat%>" />')"
		               onmouseout="UnTip()">
				    <!-- Well, Ie doesn't respect the nowrap or style white-space:nowrap. so they're screwed. -->
					<fmt:formatDate value="${item.effectiveTs}" pattern="MMM" />&nbsp;<fmt:formatDate value="${item.effectiveTs}" pattern="dd" />,&nbsp;<fmt:formatDate value="${item.effectiveTs}" pattern="yyyy" />
		            </td>
	            <td class="datadivider">
		              <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		            </td>
		            
		            <td valign="top">
		              <c:out value="${item.messageStatusDisplay}" />
		            </td>		            
		            <TD class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></TD>
					
		           <td onmouseover="Tip('<%=StringEscapeUtils.escapeJavaScript(MCHistoryUtils.getMessageStatusText(request, item)) %>')"
		               onmouseout="UnTip()"
		               valign="top">
		               <c:set var="anchorName" value="Message_${item.messageId}" />
						<span id="${anchorName}"/>
		               <b>${item.shortText}</b>&nbsp;<c:if test="${item.escalated}">${item.escalationTag}</c:if>
		               <br>
		               ${item.longText}
						<c:if test="${not empty item.additionalDetails}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${empty param.printFriendly}"><span  id="moreDetailsAnchor${item.messageId}" >&lt;<a href="javascript:void(0)" onclick="moreDetails(${item.messageId})"><content:getAttribute id='moreDetails' attribute='text' filter="true"/></a>&gt;</span></c:if>
						</c:if>
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
		               
		            </td>
		        </tr>
				
				</c:forEach>
				</c:otherwise>				
				</c:choose>
				
                <tr>
		<td width="1" class="databorder" colspan="14">
		  <img height=1	src="/assets/unmanaged/images/s.gif" width=1>
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
							  <span class="style2"><report:pageCounter report="theReport" formName="messageHistoryForm"/></span>
							</td>
						</tr>
					</tbody>
			    </table>
			  <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
	       </td>
</tr>
</table>
