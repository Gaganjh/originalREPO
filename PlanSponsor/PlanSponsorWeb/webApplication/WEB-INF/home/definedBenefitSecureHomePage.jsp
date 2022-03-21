<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
 
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- Beans used --%>
<c:set var="contractSummary" value="${contractSummary}" />
<c:set var="participantList" value="${contractSummary.participants}" />

<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_ACCOUNT_MESSAGES_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Account_Messages_Title"/>
<content:contentBean contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Defined_Benefit_Section_Title"/>

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
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



	 	     <img src="<content:pageImage type='pageTitle' id='layoutPageBean' path='${pageTitleFallbackImage}'/>" alt='${layoutBean.getParam('titleImageAltText')}">
		      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	        <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		     <br>
</c:if>
 

        <%-- Start of Plan Summary section ---%> 
        <jsp:include page="planSummary.jsp" flush="true"/> 
        <%-- End of Plan Summary section ---%></td>
		</td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="top" rowspan="3" class="right"><img src="/assets/unmanaged/images/s.gif" width="1" height="22"> 
        <%-- Start of Quick Reports --%>
 	<jsp:include page="/WEB-INF/global/standardreportlistsection.jsp" flush="true" />
        <%-- End of Quick Reports ----%><br> 
        <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	        <%-- START OF 5 MOST POPULAR FORMS --%> <%-- Business rule:
SPR.90. If the contract is a MTA contract do not display the 5 most popular forms section --%> <c:set var="contract" value="${USER_KEY.currentContract}" />  <c:if test="${contract.mta ==false}" ><jsp:include page="mostPopular.jsp"  flush="true"/> </c:if>
	        <br> 
        </ps:isNotJhtc>
        
        <c:if test="${not empty requestScope.mcModel}">
        <%---------------  Start of Message Center box---------------------%>
        <jsp:include page="messageCenterBox.jsp" flush="true"/>
        <%---------------- End of message center box -----------------------%> 
        </c:if>
        
        <jsp:include page="news.jsp" flush="true" />
        <%-- HIDING News - END --%> <br>
        </td>
    </tr>
    <tr>
       <td valign="top"><Br>
        <%-- Defined Benefit box - START --------------%>
        <table width="240" border="0" cellspacing="0" cellpadding="0" class="box">
             <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="238"><img src="/assets/unmanaged/images/s.gif" width="238" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             </tr>
             <tr class="tablehead">
                <td colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="Defined_Benefit_Section_Title" attribute="title"/></b></td>
             </tr>
             <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody"><ul><li><a href="/do/db/definedBenefitAccount/">Defined Benefit account</li><ul></td>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             </tr>
              <tr>
                <td colspan="3">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                </td>
              </tr>        
	</table>
        <%-- END OF Defined Benefit box ----%>
        
        <br><br>
        <%-- START OF CONTIBUTION STATUS ----------%> 
        <jsp:include page="contributionStatus.jsp" flush="true" /> 
        <br><br>
        <%-- START OF BILL COLLECTION  STATUS ----------%> 
        <jsp:include page="billCollectionStatus.jsp" flush="true" /> 
        <br>
        <%-- START OF CONTACTS --------------------%> 
        <jsp:include page="contacts.jsp" flush="true"/> 
        <%-- END OF CONTACTS --------%> <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
        
        </td>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="top"><Br>
        <%-- Account messages removed, replaced by Message Center --%>
	
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

