<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<% 
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("USER_PROFILE",userProfile,PageContext.PAGE_SCOPE);
%>

<%
	String fundPackageRetail = (String)session.getAttribute(Constants.FUND_PACKAGE_RETAIL);
	String fundPackageHybrid = (String)session.getAttribute(Constants.FUND_PACKAGE_HYBRID);
	String fundPackageBroker = (String)session.getAttribute(Constants.FUND_PACKAGE_BROKER);
	String fundPackageVenture = (String)session.getAttribute(Constants.FUND_PACKAGE_VENTURE);
	String fundPackageMulticlass = (String)session.getAttribute(Constants.FUND_PACKAGE_MULTICLASS);
	
	String fundPackageVRS = (String)session.getAttribute(Constants.FUND_PACKAGE_VRS);
	String qigVentureBDProductId = (String)session.getAttribute(Constants.QIG_VENTURE_BD_PRODUCT_ID);
	String qigVentureBDProductNYId = (String)session.getAttribute(Constants.QIG_VENTURE_BD_PRODUCT_NY_ID);
	
	pageContext.setAttribute("fundPackageRetail",fundPackageRetail,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageHybrid",fundPackageHybrid,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageBroker",fundPackageBroker,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageVenture",fundPackageVenture,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageMulticlass",fundPackageMulticlass,PageContext.PAGE_SCOPE);
	
	pageContext.setAttribute("fundPackageVRS",fundPackageVRS,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("qigVentureBDProductId",qigVentureBDProductId,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("qigVentureBDProductNYId",qigVentureBDProductNYId,PageContext.PAGE_SCOPE);


%>

	<c:if test="${userProfile.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageRetail }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_RETAIL_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
	


<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >

<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVenture }" >
<c:if test="${userProfile.currentContract.productId == qigVentureBDProductId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_BROKER_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
<c:if test="${userProfile.currentContract.productId != qigVentureBDProductId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>
</c:if>

<c:if test="${applicationScope.environment.siteLocation == 'ny' }" >
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVenture }" >
<c:if test="${userProfile.currentContract.productId == qigVentureBDProductNYId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_BROKER_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
<c:if test="${userProfile.currentContract.productId != qigVentureBDProductNYId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>
</c:if>

<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVRS }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageHybrid }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_HYBRID_NON_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>

<c:if test="${userProfile.currentContract.nml ==true}" >
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageRetail }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_RETAIL_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
	
	

<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >

<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVenture }" >
<c:if test="${userProfile.currentContract.productId == qigVentureBDProductId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_BROKER_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
<c:if test="${userProfile.currentContract.productId != qigVentureBDProductId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>
</c:if>
<c:if test="${applicationScope.environment.siteLocation == 'ny'}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVenture }" >
<c:if test="${userProfile.currentContract.productId == qigVentureBDProductNYId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_BROKER_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
<c:if test="${userProfile.currentContract.productId != qigVentureBDProductNYId }" >
				<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageVRS }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_VENTURE_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>

<c:if test="${userProfile.currentContract.fundPackageSeriesCode == fundPackageHybrid }" >
		<content:contentBean contentId="<%=ContentConstants.LAYOUT_PAGE_LS_SPINOFF_HYBRID_NML%>" type="LayoutPage" beanName="LSLayoutPageBean"/>
</c:if>
</c:if>

	<td width="23%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td width="54%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        <tr>
          <td colspan="3" >
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src='<content:pageImage type="pageTitle" beanName="LSLayoutPageBean"/>' alt="Investments" ><br>
		  <table width="500" border="0" cellspacing="0" cellpadding="0">
		  		<tr><td><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
          		<tr>
					<td valign="top">
						<c:if test="${LSLayoutPageBean.introduction1 != '' }">
				    		<br>
				    		<content:getAttribute attribute="introduction1" beanName="LSLayoutPageBean"/>
</c:if>
				    	<c:if test="${LSLayoutPageBean.introduction2 != '' }">
				    		<br>
				    		<content:getAttribute attribute="introduction2" beanName="LSLayoutPageBean"/>
</c:if>
				    	<c:if test="${LSLayoutPageBean.body1Header != '' }">	
				    		<br>
				  			<content:getAttribute attribute="body1Header" beanName="LSLayoutPageBean"/>
</c:if>
				  		<br>
				  		<content:getAttribute attribute="body1" beanName="LSLayoutPageBean"/>
						<br>
						<br>
          			</td>
        		</tr>

        		<tr>
          			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          			<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        		</tr>
      	  		<tr>
          			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          			<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        		</tr>
      	  </table>



		  <table width="500" border="0" cellspacing="0" cellpadding="0">
		  		<tr>
				    		
				    <td colspan=3 width="100%">
						<p><content:pageFooter beanName="LSLayoutPageBean"/></p>
 						<p class="footnote"><content:pageFootnotes beanName="LSLayoutPageBean"/></p> 
 						<p class="disclaimer"><content:pageDisclaimer beanName="LSLayoutPageBean" index="-1"/></p> 
 					</td>
 				</tr>		  	
 		  </table>
 		  </td>
 		</tr>
 	  </table>
 	
 	<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
