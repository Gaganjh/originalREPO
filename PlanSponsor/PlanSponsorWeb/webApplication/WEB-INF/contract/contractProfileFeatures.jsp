<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.content.ContentHelper"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

 <%--- start feature & services --%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td class="boxbody" width="45%"><b><img src="/assets/unmanaged/images/s.gif" width="12" height="10">
			Contract features</b>
		</td>
        <td class="boxbody" width="55%">
        	<ul class="noindent">
			 
			 <c:forEach items="${contractProfile.featuresAndServices.contractFeatures}" var="feature">
				<li>${feature}</li>
			</c:forEach>
			 
             </ul>      
		</td>
     </tr>
	<%-- Display Gifl section for GIFL enabled contracts--%> 
<c:if test="${userProfile.contractProfile.contract.hasContractGatewayInd == true}">
		 <content:contentBean contentId="<%=ContentHelper.getContentId(Constants.PSW_CP_GIFL_SECTION,(UserProfile) session.getAttribute(Constants.USERPROFILE_KEY))%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="gifl"/>
		 <content:getAttribute beanName="gifl" attribute="text">
		   <content:param><%=ContentHelper.getGIFLFeePercentageDisplay(((UserProfile) session.getAttribute(Constants.USERPROFILE_KEY)).getCurrentContract().getContractNumber())%></content:param>
		 </content:getAttribute>
</c:if>
	<%-- Display Managed Accounts feature --%>
	<c:set var="managedAccountService" value='${requestScope["managedAccountService"]}' />
	<c:if test="${not empty managedAccountService}">
		<tr valign="top">
			<td class="boxbody" width="45%">
			<b><img src="/assets/unmanaged/images/s.gif" width="12" height="10">  Managed Accounts</b></td>
			<td class="boxbody" width="55%">
			<ul class="noindent">
					<li>${managedAccountService.serviceDescription}
					</li>
			</ul>
			</td>
		</tr>
		<tr class="datacell1">
			<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td colspan="3" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
	</c:if>
	<tr valign="top">
		<td class="boxbody" width="45%"><b><img src="/assets/unmanaged/images/s.gif" width="12" height="10">
			Investments</b>
		</td>
		<td width="55%">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
 				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td><td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 			</tr>
 			
			<tr class="datacell1">
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

				<td colspan="3"><b>
				<%pageContext.setAttribute("SITEMODEUSA", Constants.SITEMODE_USA,PageContext.PAGE_SCOPE); %>
				<%pageContext.setAttribute("SITEMODENY", Constants.SITEMODE_NY,PageContext.PAGE_SCOPE); %>
				
<c:if test="${environment.siteLocation eq SITEMODEUSA}" >
			   <ps:fundSeriesName location="USA" fundSeries ="<%=((UserProfile) session.getAttribute(Constants.USERPROFILE_KEY)).getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=((UserProfile) session.getAttribute(Constants.USERPROFILE_KEY)).getCurrentContract().getProductId()%>"/></b></td>
</c:if>
<c:if test="${environment.siteLocation eq SITEMODENY}" >
			   <ps:fundSeriesName location="NY" fundSeries ="<%=((UserProfile) session.getAttribute(Constants.USERPROFILE_KEY)).getCurrentContract().getFundPackageSeriesCode()%>" productId ="<%=((UserProfile) session.getAttribute(Constants.USERPROFILE_KEY)).getCurrentContract().getProductId()%>"/></b></td>
</c:if>
							
				</b></td>
	        </tr>
			<tr class="datacell2">
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="50%">Funds available</td>
 				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="50%"><b class="highlight"><render:number property ="contractProfile.featuresAndServices.availableFundsNumber" defaultValue = "0" type="i"/></b></td>
		    </tr>
			<tr class="datacell1">
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td>Funds selected</td>
				<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  				<td><b class="highlight"><render:number property ="contractProfile.featuresAndServices.selectedFundsNumber" defaultValue = "0" type="i"/></b></td>
			</tr>
            <tr class="datacell1">
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            	<td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           </tr>
            
       </table>
       <br/>
       <!-- Default Investment Options Start -->
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
 					<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td><td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 				</tr>
				<tr class="datacell1">
					<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td><nobr><b>Default investment option(s)</b></nobr></td>
<c:if test="${not empty contractProfile.defaultInvestments}">
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td>%</td>
</c:if>
	      </tr>
<c:if test="${empty contractProfile.defaultInvestments}">
	      	<tr class="datacell2">
						<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	      		<td>
							<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_DEFAULT_INVESTMENTS%>"
					            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
					            id="noDefaultInvestment"/>
							<content:getAttribute beanName="noDefaultInvestment" attribute="text"/>
						</td>
	      	</tr>
</c:if>
<c:if test="${not empty contractProfile.defaultInvestments}">
     			<% String cellRecordClass="datacell2";%>
<c:forEach items="${contractProfile.defaultInvestments}" var="defaultInvestment">
					<tr class="<%=cellRecordClass%>">
						<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${defaultInvestment.lifeCycleFund ==true}">
<td>${defaultInvestment.fundFamilyDisplayName}</td>
</c:if>
<c:if test="${defaultInvestment.lifeCycleFund ==false}">
<td>${defaultInvestment.fundName}</td>
</c:if>
						<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				      	<td align="right"><render:number property="defaultInvestment.percentage" type="p" pattern="##0.00"/></td>

			      </tr>
       			<% 
       				cellRecordClass = (cellRecordClass.equals("datacell1"))?"datacell2":"datacell1";
       			%>
</c:forEach>
</c:if>
        <tr class="datacell1">
					<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
         	<td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
			</table>
       <!-- Default Investment Options End -->
	   </td>
 	</tr>
<c:if test="${userProfile.currentContract.isDefinedBenefitContract() == false}"> 	
 	<tr valign="top">
 		<td class="boxbody" width="45%"><b><img src="/assets/unmanaged/images/s.gif" width="12" height="10">
 			Access channels</b>
 		</td>
		<td class="boxbody" width="55%">
        	<ul class="noindent">
<c:forEach items="${contractProfile.featuresAndServices.accessChannels}" var="accessChannel">
<li>${accessChannel}</li>
</c:forEach>
            </ul>
      	</td>
     </tr>
</c:if>    
 	<tr valign="top">
		<td class="boxbody" width="45%"><b><img src="/assets/unmanaged/images/s.gif" width="12" height="10">
			Direct debit selected</b>
		</td>
        <td class="boxbody" width="55%">
        	<ul class="noindent"> <li>
        		<render:yesno property="contractProfile.featuresAndServices.isDirectDebitSelected" />
			</li></ul>
      	</td>
    </tr>
 </table>
 <%--- end feature & services --%>
