	<%-- Prevent the creation of a session --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.home.WelcomePageForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<jsp:useBean id="welcomePageForm"  class="com.manulife.pension.ps.web.home.WelcomePageForm" scope="session"/>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	// reset the participant summary form -- this way the old criteria
	// does not get carried with the new last name search criteria

%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_WELCOME_PAGE_PLAN_LINK%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PlanLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_WELCOME_PAGE_PLAN_DESC%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PlanDescText"/>
                  
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_WELCOME_PAGE_FUNDCHECK_HEADER%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="FundCheckHeader"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_WELCOME_PAGE_FUDNCHECK_DESC%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundCheckDesc"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_WELCOME_PAGE_FUNDCHECK_LINK%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundCheckLink"/>
                     
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_SUBMIT_A_DOCUMENT_LINK%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="submitDocuments"/>
             

<% int numberOfLinks = welcomePageForm.getNumberOfLinks(); %>
<content:errors scope="session"/>
<table width="730" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="530"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="20"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="180"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      </tr>
      <tr>
      	<td valign="top">
      		<table width="530" border="0" cellspacing="0" cellpadding="0">
      			<tr>
      				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="left" width="508"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      			</tr>
      			<tr class="tablehead">
      				<td colspan="5" class="tableheadTD1"><b><content:getAttribute beanName="layoutPageBean" attribute="subHeader" />&nbsp;</b></td>
      			</tr>
<c:if test="${welcomePageForm.anyAccess ==true}">
<c:if test="${welcomePageForm.toolsPageAccess ==true}">
	      			<% numberOfLinks = numberOfLinks - 1; %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></SPAN><BR>
	                      <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
	                      <A class="landingpageText" href="/do/tools/toolsMenu/">Go to tools</A></P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>
	      			<% if (numberOfLinks != 0)  { %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td vAlign="top"><HR style="COLOR: #cccccc" size="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                </tr>
	                <% } %>
</c:if>
<c:if test="${welcomePageForm.sumbitDocumentAccess ==true}">
	      			<% numberOfLinks = numberOfLinks - 1; %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute id="submitDocuments" attribute="linkText"/></SPAN><BR>
	                      <content:getAttribute id="submitDocuments" attribute="description"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
						  
	                      <A class="landingpageText" href="/do/tools/secureDocumentUpload/submit/"><content:getAttribute id="submitDocuments" attribute="linkText"/></A></P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>
	      			<% if (numberOfLinks != 0)  { %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td vAlign="top"><HR style="COLOR: #cccccc" size="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                </tr>
	                <% } %>
</c:if>
<c:if test="${welcomePageForm.censusSummaryAccess ==true}">
	                <% numberOfLinks = numberOfLinks - 1; %>
	                <tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></SPAN><BR>
	                      <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
	                      <A class="landingpageText" href="/do/census/censusSummary/">View your census information</A></P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>
	      			<% if (numberOfLinks != 0)  { %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td vAlign="top"><HR style="COLOR: #cccccc" size="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                </tr>
	                <% } %>
</c:if>
<c:if test="${welcomePageForm.csfAccess ==true}">
	                <% numberOfLinks = numberOfLinks - 1; %>
	                <tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></SPAN><BR>
	                      <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
	                      <A class="landingpageText" href="/do/contract/viewServiceFeatures/">View your contract service features</A></P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>
	      			<% if (numberOfLinks != 0)  { %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td vAlign="top"><HR style="COLOR: #cccccc" size="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                </tr>
	                <% } %>
</c:if>
<c:if test="${welcomePageForm.fundCheckAccess ==true}">
	                <% numberOfLinks = numberOfLinks - 1; %>
	                <tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute id="FundCheckHeader" attribute="text"/></SPAN><BR>
	                      <content:getAttribute id="fundCheckDesc" attribute="text"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
	                      	<A class="landingpageText" href="/do/fundCheck"><content:getAttribute beanName="fundCheckLink" attribute="text"/></A>
	                      </P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>
	      			<% if (numberOfLinks != 0)  { %>
	      			<tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td vAlign="top"><HR style="COLOR: #cccccc" size="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                </tr>
	                <% } %>					
</c:if>
<c:if test="${welcomePageForm.planAccess ==true}">
	                <% numberOfLinks = numberOfLinks - 1; %>
	                <tr>
	      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td align="left" width="508">
	                      <P><SPAN class="BIG"><content:getAttribute id="PlanLinkText" attribute="text"/></SPAN><BR>
	                      <content:getAttribute id="PlanDescText" attribute="text"/>
	                      </P>
	                      <P><img height="12" src="/assets/unmanaged/images/s.gif" width="21" align="absBottom">
	                      <A class="landingpageText" href="/do/contract/planData/view/">View your plan information</A></P>
	                    </td>
	                    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      			</tr>	      			
</c:if>
</c:if>
      			      			
<c:if test="${welcomePageForm.anyAccess ==false}">
      			<tr>
      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      				<td>No access is available for the current user.</td>
      				<td width="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      			</tr>
</c:if>
      			<tr>
      				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      				<td colspan="3">&nbsp;</td>
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      			</tr>
      			<tr>
      				<td class="databorder" colSpan="5" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      			</tr>
      		</table>
      	</td>
      	<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
        <td width="180" valign="top" height="312">
		<!--	
				<div align="right">
				<img height="225" src="/assets/generalimages/Tran_hist_web_ad.jpg" width="165">
            	</div>
        -->
      	</td>
      </tr>	
</table>			
        
      
              
