<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractProfileVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- This jsp includes the following CMA content --%>

<%-- Beans used --%>
<c:set var="contractProfile" value="${ContractProfileVO}" scope="page"/> 
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <c:if test="${empty param.printFriendly }" >
    <td> <img src="/assets/unmanaged/images/s.gif" width="30" height="1"><br>
      <img src="/assets/unmanaged/images/s.gif" height="1"></td>
   </c:if>


<td width="100%">

<div id="errordivcs"><content:errors scope="session"/></div>
<img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="box">
 	<tr>
    	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr class="tablehead">
     	<td colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
    </tr>
	<tr class="tablesubhead">
    	<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td class="boxbody">
   			<table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
                	<td><b>Contact information</b></td>
                	<td align="right">&nbsp;</td>
              	</tr>
            </table>
        </td>
      	<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     </tr>

 <%--- start contract contact information---%>
<jsp:include page="contractProfileInformation.jsp" flush="true" />
 <%--- end contract contact information --%>

  <br>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr class="tablesubhead">
    	<td width="50%"><b>Key dates</b></td>
        <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="50%"><b>Features & services</b></td>
    </tr>
<%--- start key dates --%>
	<jsp:include page="contractProfileDates.jsp" flush="true" />
<%--- end key dates --%>

<%--- start contract access code --%>
     <table width="100%" cellpadding="0" cellspacing="0" border="0">

	
<c:if test="${userProfile.currentContract.isDefinedBenefitContract() == false}">    
         <tr class="tablesubhead">
             <td><b>
             	Enrollment access number
             	</b></td>
          </tr>
          <tr>
             <td class="boxbody">
             	<!---CMA managed -->
             	<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
             	<br>
                 <br>
<b>Enrollment access number:</b> <b><span class="highlight">${contractProfile.contractAccessCode}</span></b></td>
           </tr>
</c:if>     
<%-- <%} %> --%>    
<%--- end key dates --%>

<%--- start contribution types --%>
      <jsp:include page="contractProfileContribution.jsp" flush="true" />
<%--- end contribution types --%>
     </table>
 <%--- start statements info --%>
     <jsp:include page="contractProfileStatement.jsp" flush="true" />
 <%--- end statements info --%>
     </td>
	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 	<td valign="top">

 <%--- start feature & services --%>
     <jsp:include page="contractProfileFeatures.jsp" flush="true" />
 <%--- end feature & services --%>
 	</td>
</tr>
<tr>
	<td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td class="datadivider" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 </tr>
</table> 
		  </td>
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
            <tr bgcolor="#FFF9F2">
				<td colspan="3">
					<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>

 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 				<% String TRUE = Constants.TRUE; 
					String.valueOf(Constants.TRUE);
					pageContext.setAttribute("TRUE", TRUE,PageContext.PAGE_SCOPE); %> 
					<%-- Footnote for GIFL enabled contract --%>
			<c:if test="${userProfile.contractProfile.contract.hasContractGatewayInd == TRUE}">
	 				<content:contentBean contentId="<%=ContentConstants.PSW_CP_GIFL_FOOTNOTE%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" beanName="giflFootNote"/>
					<p class="footnote"><content:getAttribute id="giflFootNote" attribute="text" /></p>
			</c:if>
 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 					</td>
   			</tr>
            </table>
	  <br>
    </td>

<c:if test="${not empty param.printFriendly }" >
    
		<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
            id="globalDisclosure"/>

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
		</c:if>
	<!-- end main content table -->

