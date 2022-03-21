<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%@tag import="com.manulife.pension.ps.web.messagecenter.MCConstants"
	   import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"
	   import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"
	   import="com.manulife.pension.ps.web.content.ContentConstants"
 %>
 
<%@ attribute name="active"
	type="java.lang.String"
	required="true" rtexprvalue="true"%>

<%@ attribute name="tpa"
	type="java.lang.Boolean"
	required="true" rtexprvalue="true"%>
<%@ attribute name="noticalertTab"
	type="java.lang.Boolean"
	required="true" rtexprvalue="true"%>
<%@ attribute name="enableNoticalertTab"
	type="java.lang.Boolean"
	required="true" rtexprvalue="true"%>

<content:contentBean contentId="<%=MCContentConstants.NoMessagePersonalizationHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="noPersonalizationHoverOver" />
</br>
<tr>
		<td colspan="3" bgcolor="#FFF9F2">
		<DIV class="nav_Main_display_short">
		<UL>
          <c:choose>
             <c:when test="${active=='email'}">
				<LI class="on"><span><strong>Email preferences</strong></span></LI>
			 </c:when>
			 <c:otherwise>
			 	<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
				  <A href="<%=MCConstants.CarViewsEmailPrefs%>?<%=MCConstants.ParamUserProfileId %>=<%=request.getAttribute(MCConstants.ParamUserProfileId) %>&<%=MCConstants.ParamContractId %>=<%=request.getAttribute(MCConstants.ParamContractId) %>"><strong>Email preferences</strong></A></LI>
			 </c:otherwise>
		</c:choose>
		<c:choose>
             <c:when test="${active=='messagePrefs'}">
				<LI class="on" style="width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_on_tab_long_150px.gif)"><span><strong>Message preferences Contract ID: <%=request.getAttribute(MCConstants.ParamContractId) %></strong></span></LI>
			 </c:when>
			 <c:otherwise>
			 	<LI onMouseOver="this.className='off_over_long';" onMouseOut="this.className='';" style="width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_off_tab_long_150px.gif)">
				  <A href="<%=MCConstants.CarViewsMessagePrefs%>?<%=MCConstants.ParamUserProfileId %>=<%=request.getAttribute(MCConstants.ParamUserProfileId) %>&<%=MCConstants.ParamContractId %>=<%=request.getAttribute(MCConstants.ParamContractId) %>"><strong>Message preferences Contract ID: <%=request.getAttribute(MCConstants.ParamContractId) %></strong></A></LI>
			 </c:otherwise>
		</c:choose>
		<c:if test="${tpa}">
		<c:choose>
             <c:when test="${active=='tpaPrefs'}">
				<LI class="on"><span><strong>TPA Firm preferences</strong></span></LI>
			 </c:when>
			 <c:otherwise>
			 	<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
				  <A href="<%=MCConstants.CarViewsTpaMessagePrefs%>?<%=MCConstants.ParamUserProfileId %>=<%=request.getAttribute(MCConstants.ParamUserProfileId) %>&<%=MCConstants.ParamContractId %>=<%=request.getAttribute(MCConstants.ParamContractId) %>"><strong>TPA Firm preferences</strong></A></LI>
			 </c:otherwise>		
		</c:choose>	 	
		</c:if>	
		<c:if test="${noticalertTab}">
		<c:choose>
            <c:when test="${active=='noticePrefs'}">
					<LI class="on" style="width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_on_tab_long_150px.gif)"><span><strong>Notice Manager Alerts Contract ID: <%=request.getAttribute(MCConstants.ParamContractId) %></strong></span></LI>
			</c:when>
			<c:otherwise>
				
			
			<c:choose>
						<c:when test="${enableNoticalertTab}">
						
							<LI  onMouseOver="this.className='off_over_long';" onMouseOut="this.className='';" style="width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_off_tab_long_150px.gif)"> 
					       <A href="<%=MCConstants.CarViewsNoticeMessagePrefs%>?<%=MCConstants.ParamUserProfileId %>=<%=request.getAttribute(MCConstants.ParamUserProfileId) %>&<%=MCConstants.ParamContractId %>=<%=request.getAttribute(MCConstants.ParamContractId) %>"><strong>Notice Manager Alerts Contract ID: <%=request.getAttribute(MCConstants.ParamContractId) %><strong></A></LI>
						</c:when>
						<c:otherwise>
						
						<LI  onMouseOver="this.className='off_over_long';" onMouseOut="this.className='';" style="color:gray;width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_off_tab_long_150px.gif)">
						<strong>Notice Manager Alerts Contract ID: <%=request.getAttribute(MCConstants.ParamContractId) %></strong></LI>
						</c:otherwise>
						</c:choose>
			
			
			</c:otherwise>		
		</c:choose>	
		</c:if>		 
		</UL>
		</DIV>
		</td>
	</tr>
