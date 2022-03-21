<%@tag import="com.sun.mail.handlers.message_rfc822"%>
<%@ tag
	import="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	import="com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel"
	import="com.manulife.pension.service.message.valueobject.MessageCategory"	
	import="com.manulife.pension.ps.web.messagecenter.util.MCUtils" 	
 %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="tab"
	type="com.manulife.pension.service.message.valueobject.MessageCenterComponent"
	required="true" rtexprvalue="true"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel"
	required="true" rtexprvalue="true"%>
	
	
     <%-- Multiple sections under the tab --%>
     <tr>
		<td width="240" height="26" bgcolor="#CCCCCC">
		&nbsp;
		<c:choose>
			<c:when test="<%=model.getTabMessageCount(tab) != 0 %>">
				  <a href="javascript:gotoLink('<%=model.getUrlGenerator().getTabUrl(tab) %>')">
				  <span class="style1"><c:out value="${tab.name}"/></span></a>
			</c:when>
			<c:otherwise>
				  <span class="style1"><c:out value="${tab.name}"/></span>
			</c:otherwise>
		</c:choose>			
		
		</td>
		<td width="1" class="datadivider">
		  <img src="/assets/unmanaged/images/s.gif"	width="1" height="0">
		 </td>
		<td align="center" bgcolor="#CCCCCC" width="50">&nbsp;</td>
		<td width="1" class="datadivider">
		  <img src="/assets/unmanaged/images/s.gif"	width="1" height="1">
		</td>
		<td align="center" bgcolor="#CCCCCC" width="50">&nbsp;</td>
		<td width="1" class="datadivider">
		   <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td align="center" bgcolor="#CCCCCC" width="50">&nbsp;</td>
		<td width="1" class="datadivider">
		   <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
		</td>
		<td>&nbsp;</td>
	 </tr>
	 
	<%-- For each section in the tab --%> 
	<c:set var="rowNumber" value="1"/>
	<c:forEach var="section" items="${tab.children}">
	   <c:set var="rowNumber" value="${rowNumber + 1}" />
	   <c:set var="section" value="${section}" scope="page"/>
	   <%
	   MessageCenterComponent section = (MessageCenterComponent)jspContext.getAttribute("section");
	   %>
	    	<c:if test="<%=MCUtils.isSectionDisplayed((HttpServletRequest)request, section) %>">
				<c:choose>
					<c:when test="${rowNumber % 2 == 1}">
						<c:set var="trStyleClass" value="datacell4" />
					</c:when>
					<c:otherwise>
						<c:set var="trStyleClass" value="datacell1" />
					</c:otherwise>
				</c:choose>
		
			<tr>
				<td class="${trStyleClass}">&nbsp;&nbsp;&nbsp;
				<%-- Check if the section is nagvigatable, if not, no href --%>
				<c:choose>
				   <c:when test="<%=model.getSectionTotalMessageCount(section) > 0 %>">
					 <A href="javascript:gotoLink('<%=model.getUrlGenerator().getSelectSectionUrl(tab, section)%>')">
					    <c:out value="${section.name }"/>
					 </a>
				   </c:when>
				   <c:otherwise>
				   		<c:out value="${section.name }"/>
				   </c:otherwise>
				</c:choose>
				</td>
				<td width="1" class="datadivider">
				  <img src="/assets/unmanaged/images/s.gif"	width="1" height="0"></td>
				<td  class="${trStyleClass}" align="center">
					&nbsp;
					<c:out value="<%=MCUtils.getCountDisplay(model.getSectionMessageCount(section, MessageCategory.URGENT)) %>"/>
				</td>
				<td width="1" class="datadivider">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td  class="${trStyleClass}" align="center">
					&nbsp;
					<c:out value="<%=MCUtils.getCountDisplay(model.getSectionMessageCount(section, MessageCategory.ACTION)) %>"/>
				</td>
				<td width="1" class="datadivider">
				    <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td  class="${trStyleClass}" align="center">
					&nbsp;
					<c:out value="<%=MCUtils.getCountDisplay(model.getSectionMessageCount(section, MessageCategory.FYI)) %>"/>
				</td>
				<td width="1" class="datadivider">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
				</td>
				<td>&nbsp;</td>
			</tr>
		 </c:if>
	</c:forEach>  
	