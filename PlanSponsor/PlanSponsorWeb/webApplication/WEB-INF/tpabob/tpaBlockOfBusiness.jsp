<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="tpaBob" tagdir="/WEB-INF/tags/tpaBob"%>
<%@ taglib prefix="reporttag" tagdir="/WEB-INF/tags/report"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.platform.web.util.ContentHelper"%>
<%@ page import="com.manulife.pension.ps.web.tpabob.util.TPABlockOfBusinessUtility"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportVO" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

	


<%
if(request.getAttribute(Constants.REPORT_BEAN) != null){
	TPABlockOfBusinessReportData theReport = (TPABlockOfBusinessReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>
<c:set var="tabs" value="${tpaBobTabsList}"/>

<c:set var="tpaBlockOfBusinessForm" value="${tpaBlockOfBusinessForm}" />

<jsp:useBean id="tpaBlockOfBusinessForm"  class="com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessForm" />
             


<% if (userProfile.isInternalUser()) {%>
    <c:set var="tpaProfileId" value="${sessionScope['tpaUserInfo'].profileId}" />
<%  } else {%>
    <c:set var="tpaProfileId" value="${userProfile.principal.profileId}" />
<%  } %>

<%
    pageContext.setAttribute("asOfDatesList", TPABlockOfBusinessUtility.getMonthEndDates());
    pageContext.setAttribute("carList", TPABlockOfBusinessUtility.getCarDropdownOptions(((Long) pageContext.getAttribute("tpaProfileId")).toString()));    
    
%>

<c:set var="actionURL" value="/do/tpabob/tpaBlockOfBusiness/" />
<c:set var="doStr" value="/do"/>
<c:set var="spaceStr" value="" />

 <c:forEach items="${tabs}" var="tabItem"  >
   	
    <%--  TestTab:${tabItem.isEnabled}  --%>
     
      <c:if test="${tabItem.isEnabled}"> 
       <!--  the actionURL contains string like "/do/tpabob/tpaBlockOfBusiness/". 
        But, we need only "/tpabob/tpaBlockOfBusiness/". So, below code also replaces the "/do".
 -->        
	<%-- <c:set var="actionURL" value="${fn:replace(tabItem.actionURL, doStr , spaceStr)}" />  --%>
    </c:if> 
</c:forEach> 
  
 <table width="730" border="0" cellspacing="0" cellpadding="0">
    <ps:form modelAttribute="tpaBlockOfBusinessForm" name="tpaBlockOfBusinessForm" action="${actionURL}"  >
        <tr> 
            <td width="1"></td>
            <td width="619"></td>
            <td width="81"></td>
            <td width="1"></td>
        </tr>
        <tr>
            <td colspan="2">
              <%--   <reporttag:reportErrors scope="request"/>  --%>
              <content:errors scope="request"/>
                 
            </td>
        </tr> 

        <tr>
            <td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
        </tr>

        <tr>
            <td colspan="4">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="tableheadTD1" width="35%" valign="center">
                            <b><content:getAttribute id="layoutPageBean" attribute="body2Header"/></b>
                        </td>
                        <td class="tableheadTD" width="35%" valign="center">As of 
                            <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
                                         property="tpaBlockOfBusinessForm.selectedDateInDateFormat"/>
                        </td>
                        <td class="tableheadTD" width="30%" valign="center">
                            <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <c:if test="${not empty reportBean}">





            <!-- Summary Section - START -->
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="2">
                    <table width="730" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="3" class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="249" class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="3" class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td width="3" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="75" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="5" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="75" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="5" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="104" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="3" class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td width="3" class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="97" class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="3" class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="97" class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td width="3" class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                        <!-- Summary Section - HEADER - START -->
                        <tr>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color" valign="top"><b class="highlight">Firm information</b></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td colspan="5" class="dark_grey_color" valign="top"><b class="highlight">Active contracts</b></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td colspan="3" class="light_grey_color" valign="top"><b class="highlight">Pending contracts</b></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                        <tr>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color_with_padding" valign="top"><b>Firm name and number</b></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top"><b>Contracts</b></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top"><b>Lives</b></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top"><b>Assets ($)</b></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color_with_padding" valign="top"><b>Contracts</b></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color_with_padding" valign="top"><b>Lives</b></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                       <!--  Summary Section - HEADER - END -->
                        
                        <c:if test="${not empty theReport.tpaBlockOfBusinessSummaryVO}" >
                            <c:forEach var="summaryInfoVO" items="${theReport.tpaBlockOfBusinessSummaryVO.tpaBobSummaryContractVO}">
                                <tr>
                                    <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="white_color_with_padding" valign="top">${summaryInfoVO.firmName} - ${summaryInfoVO.firmNumber}</td>
                                    <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    
                                    <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="dark_grey_color_with_padding" valign="top">
                                        <render:number property="summaryInfoVO.activeContractCount" type="i" sign="false"/>
                                    </td>
                                    <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="dark_grey_color_with_padding" valign="top">
                                        <render:number property="summaryInfoVO.activeContractLives" type="i" sign="false"/>
                                    </td>
                                    <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="dark_grey_color_with_padding" valign="top" align="right">
                                        <render:number property="summaryInfoVO.activeContractAssets" type="c" sign="false"/>
                                    </td>
                                    <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    
                                    <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="light_grey_color_with_padding" valign="top">
                                        <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected eq true}">
                                            <render:number property="summaryInfoVO.pendingContractCount" type="i" sign="false"/>
                                        </c:if>
                                        <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected ne true}">
                                            0
                                        </c:if>
                                    </td>
                                    <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td class="light_grey_color_with_padding" valign="top">
                                        <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected eq true}">
                                            <render:number property="summaryInfoVO.pendingContractLives" type="i" sign="false"/>
                                        </c:if>
                                        <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected ne true}">
                                            0
                                        </c:if>
                                    </td>
                                    <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                </tr>
                            </c:forEach>
                        <tr height="10">
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                        <tr height="1">
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="databorder" style="background-color: #ffffff;"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="databorder" style="background-color: #ffffff;"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="databorder" style="background-color: #ffffff;"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="databorder" style="background-color: #ffffff;"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="databorder" style="background-color: #ffffff;"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                        <tr>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="white_color_with_padding" valign="top"></td>
                            <td class="white_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top">
                                <b><render:number property="theReport.tpaBlockOfBusinessSummaryVO.totalActiveContractCount" type="i" sign="false"/></b>
                            </td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top">
                                <b><render:number property="theReport.tpaBlockOfBusinessSummaryVO.totalActiveContractLives" type="i" sign="false"/></b>
                            </td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="dark_grey_color_with_padding" valign="top" align="right">
                                <b><render:number property="theReport.tpaBlockOfBusinessSummaryVO.totalActiveContractAssets" type="c" sign="false"/></b>
                            </td>
                            <td class="dark_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color_with_padding" valign="top">
                                <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected eq true}">
                                    <b><render:number property="theReport.tpaBlockOfBusinessSummaryVO.totalPendingContractCount" type="i" sign="false"/></b>
                                </c:if>
                                <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected ne true}">
                                    <b>0</b>
                                </c:if>
                            </td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                            <td class="light_grey_color_with_padding" valign="top">
                                <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected eq true}">
                                    <b><render:number property="theReport.tpaBlockOfBusinessSummaryVO.totalPendingContractLives" type="i" sign="false"/></b>
                                </c:if>
                                <c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected ne true}">
                                    <b>0</b>
                                </c:if>
                            </td>
                            <td class="light_grey_color"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        </tr>
                        </c:if>
                    </table>
                </td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td colspan="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
           <!--  Summary Section - END -->
        </c:if>

        <tr>
            <td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
        </tr>

        <tr>
            <td class="tableheadTD1" colspan="4">
                <b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>
            </td>
        </tr>
        
<input type="hidden" name="task" value="filter"/>
        <tr>
            <td colspan="4">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="30%" valign="top" class="filterSearch">
                            <b>Contract name</b>
                            <br />
                             <c:if test="${not empty param.printFriendly }" >
                                <ps:fieldHilight name="<%=Constants.TPA_CONTRACT_NAME_FIELD%>"/>
                               <form:input path="contractName" disabled="true" maxlength="30"/>
                            </c:if>
                            <c:if test="${empty param.printFriendly }" >
                                <ps:fieldHilight name="<%=Constants.TPA_CONTRACT_NAME_FIELD%>"/> 
                                    <form:input path="contractName" maxlength="30"/>
                            </c:if> 
                        </td>
                        <td width="30%" valign="top" class="filterSearch">
                            <b>Client Account Rep</b>
                            <br />
                            <c:if test="${not empty param.printFriendly }" >
                               <form:select path="carName" disabled="true">
                                    <form:option value=""></form:option>
                                    <c:forEach items="${pageScope.carList}" var="carNameVO">
                                        <form:option value="${carNameVO.value}">${carNameVO.label}</form:option>
                                    </c:forEach>
                               </form:select>
                            </c:if>
                            <c:if test="${empty param.printFriendly }" >
                               <form:select path="carName" disabled="false">
                                    <form:option value=""></form:option>
                                    <c:forEach items="${pageScope.carList}" var="carNameVO">
                                        <form:option value="${carNameVO.value}" >${carNameVO.label}</form:option>
                                    </c:forEach>
                              </form:select>
                            </c:if>
                        </td>
                        
                        <td width="20%" valign="top" class="filterSearch">
                            <b>As of Date</b>
                            <br/>
                             <c:if test="${tpaBlockOfBusinessForm.currentTab eq 'pendingTab'}">
                                <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
                                             property="tpaBlockOfBusinessForm.selectedDateInDateFormat"/>
                            </c:if>
                            <c:if test="${tpaBlockOfBusinessForm.currentTab ne 'pendingTab'}">
                               <c:if test="${empty param.printFriendly }" >
                                    <c:set var="asOfDatesList" value="${pageScope.asOfDatesList}"/>
                                    <ps:select name="tpaBlockOfBusinessForm" property="asOfDateSelected">
                                        <ps:dateOptions name="asOfDatesList" renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
                                    </ps:select>
                                </c:if>
                                <c:if test="${not empty param.printFriendly }" >
                                    <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
                                             property="tpaBlockOfBusinessForm.selectedDateInDateFormat"/>
                                </c:if>
                            </c:if>
                        </td>
                        <td width="20%" valign="top" class="filterSearch">
                            <br />
                        </td>
                    </tr>
                    <tr> 
                        <td width="30%" valign="top" class="filterSearch">
                            <b>Contract number</b>
                            <br />
                            <c:if test="${not empty param.printFriendly }" >
                           <form:input path="contractNumber" disabled="true" maxlength="7"/>
                            </c:if>
                            <c:if test="${empty param.printFriendly }" >
                           <form:input path="contractNumber" maxlength="7"/>
                            </c:if>
                        </td>
                        <td width="30%" valign="top" class="filterSearch">
                            <b>Financial Rep Name/Company</b>
                            <br />
                            <c:if test="${not empty param.printFriendly }" >
                          <form:input path="financialRepNameOrOrgName" disabled="true" maxlength="1024"/>
                            </c:if>
                            <c:if test="${empty param.printFriendly }" >
                          <form:input path="financialRepNameOrOrgName" maxlength="1024"/>
                            </c:if>
                        </td>
                        <td width="20%" valign="top" class="filterSearch">
                            <br />
                        </td>
                        <td width="20%" valign="top" class="filterSearch">
                            <br />
                            <c:if test="${empty param.printFriendly }" >
                                <span style="float: right;">
                                    <input name="Button" type="Button" value="search" onClick="applyFilters(this.form);"/>
                                    <input name="Button" type="Button" value="reset" onClick="resetFilters(this.form);"/>
                                </span>
                            </c:if>
                        </td>
                    </tr> 
                </table>
            </td>
        </tr> 
        
       <tr>
            <td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
        </tr>

        <!-- TABS - START -->
        <tr>
            <td colspan="4" bgcolor="#FFF9F2">
                 <tpaBob:tpaBobTabs /> 
            </td>
        </tr>
        <!-- TABS - END

        Detail rows -->
        <c:if test="${not empty reportBean}">
             <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="2">
                    <table width="730" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td colspan="21">
                                <table cellspacing="0" cellpadding="0" border="0" width="100%">
                                    <tr class="tablesubhead">
                                        <td class="dark_grey_color">
                                            <b>As of 
                                            <render:date patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>"
                                                        property="tpaBlockOfBusinessForm.selectedDateInDateFormat"/>
                                            </b>
                                        </td>
                                        <td class="dark_grey_color">
                                            <b><report:recordCounter report="theReport" label="Contracts"/></b>
                                        </td>
                                        <td align="right" class="dark_grey_color">
                                            <report:pageCounter report="theReport" formName="tpaBlockOfBusinessForm"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr> 
                        <tr class="tablesubhead" valign="bottom">
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractName'].isEnabled}">
                                <td width="164">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractName'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractName'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
    
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractNumber'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="64">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractNumber'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractNumber'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractPlanYearEnd'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="54">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractPlanYearEnd'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractPlanYearEnd'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedNumOfLives'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="70">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedNumOfLives'].columnId}" direction="desc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedNumOfLives'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedFirstYearAssets'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="90">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedFirstYearAssets'].columnId}" direction="desc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedFirstYearAssets'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['numOfLives'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="70">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['numOfLives'].columnId}" direction="desc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['numOfLives'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['totalAssets'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="90" align="center">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['totalAssets'].columnId}" direction="desc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['totalAssets'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['discontinuanceDate'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="70">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['discontinuanceDate'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['discontinuanceDate'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['transferredOutAssets'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="90" align="center">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['transferredOutAssets'].columnId}" direction="desc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['transferredOutAssets'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['financialRep'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="134">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['financialRep'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['financialRep'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                            
                            <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['carName'].isEnabled}">
                                <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                <td width="134">
                                    <strong><report:sort field="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['carName'].columnId}" direction="asc" formName="tpaBlockOfBusinessForm">
                                            ${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['carName'].columnTitle}
                                    </report:sort></strong>
                                </td>
                            </c:if>
                        </tr>
<c:if test="${empty theReport.details}">
                            <c:set var="infoMsgCollection" value="${requestScope['tpaBobInfoMsg']}"/>
                            
                            <%pageContext.setAttribute("infoMsgList", ContentHelper.getMessagesUsingContentType((
                                    Collection<GenericException>)pageContext.getAttribute("infoMsgCollection")));%>
                            <c:if test="${not empty infoMsgList}">
                                <tr class="datacell1">
                                    <td colspan="21">
                                        <c:forEach var="infoMsg" items="${infoMsgList}">
                                            ${infoMsg}
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:if>
</c:if>
<c:if test="${not empty theReport.details}">
<input type="hidden" name="contractNumberSelected"/>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
<%TPABlockOfBusinessReportVO theItem = (TPABlockOfBusinessReportVO)pageContext.getAttribute("theItem");
String temp = pageContext.getAttribute("indexValue").toString();%>


                                <% if (Integer.parseInt(temp) % 2 == 0) { %>
                                <tr class="datacell1">
                                <% } else { %> 
                                <tr class="datacell2">
                                 <% } %> 
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractName'].isEnabled}">
                                        <td>
                                            <c:if test="${not empty param.printFriendly }" >
                                                ${theItem.contractName}
                                            </c:if>
                                            <c:if test="${empty param.printFriendly }" >
                                                <c:if test="${tpaBlockOfBusinessForm.currentTab eq 'pendingTab'}">
                                                    ${theItem.contractName}
                                                </c:if>
                                                <c:if test="${tpaBlockOfBusinessForm.currentTab ne 'pendingTab'}">
                                                    <a href="javascript://" onclick="goToContractHomePage(${theItem.contractNumber}); return false;">
                                                    ${theItem.contractName}</a>
                                                
                                                </c:if>
                                            </c:if>
                                        </td>
                                    </c:if>
            
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractNumber'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td>${theItem.contractNumber}</td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['contractPlanYearEnd'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td><render:date patternOut="<%=Constants.DATE_MM_DD_SLASHED%>" property="theItem.contractPlanYearEnd"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedNumOfLives'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td><render:number property="theItem.expectedNumOfLives" type="i" sign="false"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['expectedFirstYearAssets'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td><render:number property="theItem.expectedFirstYearAssets" type="c" sign="false"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['numOfLives'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td>
                                            <c:if test="${theItem.productType eq 'DB'}">
                                                
                                            </c:if>
                                            <c:if test="${theItem.productType ne 'DB'}">
                                                <render:number property="theItem.numOfLives" type="i" sign="false"/>
                                            </c:if>
                                        </td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['totalAssets'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td align="right"><render:number property="theItem.totalAssets" type="c" sign="false"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['discontinuanceDate'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.discontinuanceDate"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['transferredOutAssets'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td align="right"><render:number property="theItem.transferredAssetsPriorToCharges" type="c" sign="false"/></td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['financialRep'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td>${theItem.financialRepName}</td>
                                    </c:if>
                                    
                                    <c:if test="${tpaBlockOfBusinessForm.applicableColumnsForCurrentTab['carName'].isEnabled}">
                                        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                        <td>${theItem.carName}</td>
                                    </c:if>
                                </tr>
</c:forEach>
</c:if>
                    </table>
                </td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td colspan="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
    
            <tr>
                <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="2">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%">
                        <tr>
                            <td align="right">
                              <report:pageCounter report="theReport" formName="tpaBlockOfBusinessForm"/> 
                            </td>
                        </tr>
                    </table>
                </td>
                <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
        </c:if> 
    </ps:form>
</table> 

<c:if test="${empty param.printFriendly }" >
    <p><content:pageFooter id="layoutPageBean"/></p>
</c:if>
<c:if test="${tpaBlockOfBusinessForm.currentTab eq 'discontinuedTab'}">
<content:contentBean contentId="<%=ContentConstants.BOB_ASSETS_COLUMN_IN_DI_TAB_FOOTNOTE%>"
        type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="assetColumnDITabFootnote"/>
<p class="footnote"><content:getAttribute beanName="assetColumnDITabFootnote" attribute="text"/></p>
</c:if>
<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>

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
