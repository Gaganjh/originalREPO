<%-- This jsp is included as part of the secureHomePage.jsp --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="contractSummary" value="${requestScope.contractSummary}" />
<c:set var="participantList" value="${contractSummary.participants}" />
<c:if test="${not empty userProfile.currentContract}">
</c:if>

<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<!--
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

MM_preloadImages('/assets/unmanaged/images/make_contr_roll.jpg');

//-->


<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTRIBUTION_STATUS_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contribution_Status_Section_Title"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTRIBUTION_STATUS_VIEW_DETAILS_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contribution_Status_View_Details_Link"/>

<table width="240" border="0" cellspacing="0" cellpadding="0" class="box">
		                <tr>
		                  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                  <td width="238"><img src="/assets/unmanaged/images/s.gif" width="238" height="1"></td>
		                  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                </tr>
		                <tr class="tablehead">
		                  <td colspan="3" class="tableheadTD1">
		                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="61%" class="tableheadTD"><b><%-- CMA managed--%><content:getAttribute beanName="Contribution_Status_Section_Title" attribute="title"/></b></td>
                      <td width="39%" align="right">
                      <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
<c:if test="${contractSummary.lastAllocationTransactionNumber !=0}">
	                      <content:getAttribute beanName="Contribution_Status_View_Details_Link" attribute="text">
	                      <content:param>
	                      <c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.definedBenefitContract ==true}">
/do/transaction/pptContributionDetailsReport/?task=filter&transactionNumber=${contractSummary.lastAllocationTransactionNumber}&transactionDate=<render:date property="contractSummary.lastPayrollDate" patternOut="<%=RenderConstants.MEDIUM_YMD_DASHED %>" defaultValue=""/>
</c:if>
<c:if test="${userProfile.currentContract.definedBenefitContract !=true}">
/do/transaction/contributionTransactionReport/?task=filter&transactionNumber=${contractSummary.lastAllocationTransactionNumber}&transactionDate=<render:date property="contractSummary.lastPayrollDate" patternOut="<%=RenderConstants.MEDIUM_YMD_DASHED %>" defaultValue=""/>
</c:if>
	                        </c:if>
						  </content:param></content:getAttribute>
</c:if>
					  </ps:isNotJhtc>
					  </td>
                    </tr>
                  </table>
		  				</td>
              </tr>
              <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody" valign="top">

                <table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
	<c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.definedBenefitContract ==true}">
				<td><b>Last allocation</b></td>
</c:if>
<c:if test="${userProfile.currentContract.definedBenefitContract !=true}">
				<td><b>Last payroll allocation</b></td>
</c:if>
	</c:if>			
				<td align="right"><b class="highlight"><render:number property ="contractSummary.lastAllocationAmount" type="c" defaultValue="5000.00" /></b></td>
				</tr>
				</table>
<br>
	 <table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
	<c:if test="${userProfile.currentContract.definedBenefitContract eq true}"> 
		<td><b>For contribution date</b></td>
</c:if>
	<c:if test="${userProfile.currentContract.definedBenefitContract ne true}"> 
		<td><b>For payroll ending</b></td>
</c:if>
		<td align="right"><b class="highlight">
		<render:date property="contractSummary.lastPayrollDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED %>" defaultValue=""/>
		</b></td>

		</tr>
				</table>


<br>
	 <table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
		<td><b>Invested</b></td>
		<td align="right"><b class="highlight">
		<render:date property="contractSummary.lastSubmissionDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED %>" defaultValue=""/>
		</tr>
				</table>
                  <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" ><div align="center"><%-- CMA managed--%>
                     <br>
                     <a href="/do/home/makeContribution/" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Make Contribution','','/assets/unmanaged/images/make_contr_roll.jpg',1)">
                       <img src="/assets/unmanaged/images/make_contr.jpg" name="Make Contribution" width="189" height="19" border="0">
                  </div></ps:isNotJhtc></td>
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


