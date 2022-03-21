<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.home.RIRSHotLinkUtil" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%-- Beans used --%>
<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%>

<c:set var="contractSummary" value="${requestScope.contractSummary}" /> 
<c:set var="participantList" value="${contractSummary.participants}" /> 

<%-- This jsp includes the following CMA contents --%>
<content:contentBean contentId="<%=ContentConstants.PS_ACCOUNT_MESSAGES_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Account_Messages_Title"/>

<td width="30" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" align="top" width="8" height="8"> <br>
<img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
<td width="100%" valign="top">


<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="240"><img src="/assets/unmanaged/images/s.gif" width="240" height="1"></td>
        <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
        <td width="240"><img src="/assets/unmanaged/images/s.gif" width="240" height="1"></td>
        <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
        <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
    </tr> 
    <tr>
        <td colspan="3" valign="top"><img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
          <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
<c:if test="${not empty layoutBean.params['titleImageFallback']}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.params['titleImageFallback']}" /> 



	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${ pageTitleFallbackImage }"/>"
alt="${layoutBean.params['titleImageAltText']}">
		      <br>
</c:if>
<c:if test="${empty layoutBean.params['titleImageFallback']}">
	        <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		     <br>
</c:if>
 
		<content:errors scope="session"/>

        <%-- Start of Plan Summary section ---%> <jsp:include page="planSummary.jsp" flush="true"/> <%-- End of Plan Summary section ---%></td>
		</td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="top" rowspan="3" class="right"><img src="/assets/unmanaged/images/s.gif" width="1" height="22">
        <%-- Start of Quick Reports --------------------------------------------------------------%>

 	        <jsp:include page="/WEB-INF/global/standardreportlistsection.jsp" flush="true" />

        <%-- End of Quick Reports ----%><br> 
        
        <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	        <%-- START OF 5 MOST POPULAR FORMS --%> <%-- Business rule:
			SPR.90.	If the contract is a MTA contract do not display the 5 most popular forms section--%>
			<c:if test="${not empty userProfile.currentContract}">
<c:set var="contract" value="${USER_KEY.currentContract}" />
<c:if test="${contract.mta ==false}"> <jsp:include page="mostPopular.jsp"  flush="true"/> </c:if>
			</c:if>
			<br>
		</ps:isNotJhtc>
		<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	        <%--- RIRS Smart Link Section --%>		
			<c:if test="<%=RIRSHotLinkUtil.isRIRSHotLinkOn(application) %>">
				<c:if test="<%= RIRSHotLinkUtil.showRIRSHotLink(userProfile)%>">
					<jsp:include page="rirsHotLinkSection.jsp" flush="true"/>
					<br>
		 		</c:if>		
			</c:if>
		</ps:isNotJhtc>

        <c:if test="${not empty requestScope.mcModel}">
        <%---------------  Start of Message Center box---------------------%>
        <jsp:include page="messageCenterBox.jsp" flush="true"/>
        <%---------------- End of message center box -----------------------%> 
        </c:if>
                		
		<%-- Account messages section removed replaced by Message Center --%>
		<br>
		<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
			<jsp:include page="activityPlanner.jsp"  flush="true"/>
        </ps:isNotJhtc>
		<%-- HIDING News - START --%> 
        <jsp:include page="news.jsp" flush="true" />
        <%-- HIDING News - END --%> <br>
        </td>
    </tr>
    <tr>
        <td valign="top"><Br>
        <%-- PARTICIPANT LIST - START --------------%> <jsp:include page="participantsList.jsp" flush="true"/> <%-- END OF PARTICIPANT LIST ----%></td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="top"><Br>

        <%-- START OF CONTIBUTION STATUS ----------%> <jsp:include page="contributionStatus.jsp" flush="true" /> <%-- END OF CONTIBUTION STATUS -----%> <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
        
        <%-- START OF BILL COLLECTION STATUS ----------%> <jsp:include page="billCollectionStatus.jsp" flush="true" /> <%-- END OF BILL COLLECTION STATUS -----%> <img src="/assets/unmanaged/images/s.gif" width="1" height="20">
        
        <%-- START OF CONTACTS --------------------%>   <jsp:include page="contacts.jsp" flush="true"/> <%-- END OF CONTACTS --------%> <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
	      
<c:if test="${not empty contractSummary.webMessages}">
        <%-- START OF TPA WEB MESSAGE --------------------%> <jsp:include page="webMessage.jsp" flush="true"/> <%-- END OF TPA WEB MESSAGE --------%> <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
</c:if>
      
        

        
 	</td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr>
        <td><br>
        <br>
        <br>
        <td>
    </tr>
    <tr>
	   		<td colspan="5">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
  		    </tr>	
    
</table>

</td>
<td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>

<%-- End of Secure Body content --%>
