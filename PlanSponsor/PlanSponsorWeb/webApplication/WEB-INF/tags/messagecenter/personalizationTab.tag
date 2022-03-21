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

<%@ attribute name="global"
	type="java.lang.Boolean"
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
<content:contentBean contentId="<%=MCContentConstants.NoticeManagerGlobalContext%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="noticeAlertsPersonalizationHoverOver" />
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
				  	<c:if test="${empty param.printFriendly }" ><A href="<%=MCConstants.PersonalizationUrl%>">
				  	</c:if><strong>Email preferences</strong><c:if test="${empty param.printFriendly }" ></A></c:if></LI>
			 </c:otherwise>
		   </c:choose>
		   <c:choose>
				<c:when test="${global}">
					<LI onMouseOver="return Tip('<content:getAttribute id="noPersonalizationHoverOver" attribute="text" filter="true"/>')" onMouseOut="return UnTip()">
					   <span><strong>Message preferences</strong><span>
					</LI>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${active=='message'}">
							<LI class="on"><span><strong>Message preferences</strong></span></LI>
						</c:when>
						<c:otherwise>
							<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
							<c:if test="${empty param.printFriendly }" ><A href="<%=MCConstants.PersonalizationMessageUrl%>"></c:if><strong>Message preferences</strong><c:if test="${empty param.printFriendly }" ></A></c:if></LI>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
		    </c:choose>
		  
			<c:if test="${active == 'email' && global && tpa }">
					<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
					<c:if test="${empty param.printFriendly }" ><A href="<%=MCConstants.TPAMessagePreferencesUrl%>"></c:if><strong>TPA Firm preferences</strong><c:if test="${empty param.printFriendly }" ></A></c:if></LI>
			</c:if>
			<c:if test="${active == 'message' && global && tpa}">
					<LI class="on"><span><strong>TPA Firm preferences</strong></span></LI>
			</c:if>
				<c:if test="${active == 'notice' && global && tpa}">
					<LI class="on"><span><strong>TPA Firm preferences</strong></span></LI>
			</c:if>
			  
		    <!-- Notice manager preference -->
		    <c:if test="${noticalertTab}">
		    <c:choose>
				<c:when test="${global}">
					<LI onMouseOver="return Tip('<content:getAttribute id="noticeAlertsPersonalizationHoverOver" attribute="text" filter="true"/>')" onMouseOut="return UnTip()">
					   <span><strong>Notice Manager Alerts</strong><span>
					</LI>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${active=='notice'}">
							<LI class="on"><span><strong>Notice Manager Alerts</strong></span></LI>
						</c:when>
						<c:otherwise>
						
						 <c:choose>
						<c:when test="${enableNoticalertTab}">
							<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
							<c:if test="${empty param.printFriendly }" ><A href="<%=MCConstants.PersonalizationNoticeUrl%>"></c:if><strong>Notice Manager Alerts</strong><c:if test="${empty param.printFriendly }" ></A></c:if></LI>
						</c:when>
						<c:otherwise>
						<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';"
						style="color:gray;width:150px;BACKGROUND-IMAGE: url(/assets/unmanaged/images/1line_off_tab_long_150px.gif); width: 84px !important;">
						<strong>Notice Manager Alerts</strong></LI>
						</c:otherwise>
						</c:choose>
						
						
						</c:otherwise>
					</c:choose>
				</c:otherwise>
		    </c:choose>
		    </c:if>
		</UL>
		</DIV>
		</td>
	</tr>
