<%-- taglib used --%>
<%@page import="com.manulife.pension.content.valueobject.ContentDescription"%>
<%@page import="com.manulife.pension.service.security.role.InternalUserManager"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


<%@ page import="com.manulife.pension.content.valueobject.LayoutPage"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.delegate.ContractServiceDelegate" %> 
<%@ page import="com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility" %>
<%@ page import="com.manulife.pension.exception.ContractDoesNotExistException" %>
<%@ page import="com.manulife.pension.service.security.role.PilotCAR" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<!--Error- message box-->
<report:formatMessages scope="request" />
<br/>
<%
	String title = "User Guides";
	String strParentId = request.getParameter("parentId");
	int parentId = 0;
	if(strParentId != null) {
		parentId = Integer.parseInt(strParentId);
	}
	pageContext.setAttribute("parentId", parentId, pageContext.PAGE_SCOPE);
	
//GIFL 1C- Start
	boolean giflInd = false;
	String giflVersion3Indicator = "";
	boolean noticeManagerflag = false;
	
	int adminContentId = ContentConstants.LAYOUT_PAGE_NON_GIFL_ADMIN_GUIDE_LINK;
	int quickServiceContentId = ContentConstants.LAYOUT_PAGE_NON_GIFL_QUICK_SERVICE_GUIDE_LINK;
 	UserProfile userprofile = (UserProfile)request.getSession(false).getAttribute(Constants.USERPROFILE_KEY);
	
	
		// Making Validation for the Notice Manager Display.
		try {
			if (!(NoticeManagerUtility.validateProductRestriction(userprofile
					.getCurrentContract())
					|| NoticeManagerUtility.validateContractRestriction(userprofile
							.getCurrentContract())
					|| NoticeManagerUtility.validateDIStatus(
							userprofile.getCurrentContract(), userprofile.getRole())
					|| userprofile.getRole() instanceof PilotCAR || userprofile.isInternalServicesCAR()|| userprofile.getRole()instanceof InternalUserManager) ) {
				noticeManagerflag = true;
			}
			
		} catch (ContractDoesNotExistException ex) {

		}

	
	if(parentId == 73){
		giflInd = userprofile.getCurrentContract().getHasContractGatewayInd();
		giflVersion3Indicator = Constants.GIFL_VERSION_03.equals(userprofile.getCurrentContract().getGiflVersion())? "true":"false";
		
		if(giflInd){
			adminContentId = ContentConstants.LAYOUT_PAGE_GIFL_ADMIN_GUIDE_LINK;
			quickServiceContentId = ContentConstants.LAYOUT_PAGE_GIFL_QUICK_SERVICE_GUIDE_LINK;
		}
	}
	
//GIFL 1C- End
// OB3 T3 Changes - Start 
	boolean deferralInd = false;
	String deferralMode = "";
	if(parentId == 73){
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		deferralMode = contractServiceDelegate.determineSignUpMethod(userprofile.getCurrentContract().getContractNumber());
		if(	deferralMode != null){
			deferralInd = true;
		}
	}
// OB3 T3 Changes - End 
// JIRA#132 Contact us Changes - Start 
	boolean isBundledIndicator  = userprofile.getCurrentContract().isBundledGaIndicator();
// JIRA#131 Contact us Changes  - End 
%>
<c:if test="${parentId eq '73'}">
<script type="text/javascript" >
	init(2, 1);
</script>
</c:if>
<c:if test="${parentId eq '75'}">
<script type="text/javascript" >
	init(2, 4);
</script>
</c:if>
<!-- GIFL 1C- Start -->
<content:contentBean contentId="<%=adminContentId%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="adminPageAdminGuideLinesContent"/>
<content:contentBean contentId="<%=quickServiceContentId%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="adminPageQuickServiceGuideLinesContent"/>
<content:contentBean contentId="91967" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="sendServiceLink"/>
<!-- GIFL 1C- End -->

<content:contentListByParent parentId="<%=parentId%>" collectionName="contentItems"/>

<c:forEach items="${contentItems}" var="item" >
<%		
	boolean foundLandingPage = false;
	ContentDescription item= (ContentDescription)pageContext.getAttribute("item"); 
%>
	 
<%	
if("0".equals(item.getCategory())) {
	foundLandingPage = true;
	
%>
<content:contentBean contentId="${item.getKey()}" type="LayoutPage" beanName="LandingPageBean"/>
<%
}
	if(foundLandingPage == true)
	{
		Object layoutBean = pageContext.getAttribute("LandingPageBean", PageContext.REQUEST_SCOPE);
		LayoutPage landingPage = (LayoutPage)layoutBean;
		title = landingPage.getBrowserTitle();
		break;
	}
	
%>
</c:forEach>

<script type="text/javascript" >
	document.title = "<%=title%>";
</script>

<td width="30" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
<td>
<table width="730" border="0" cellspacing="0" cellpadding="0"> 
  <tr>
    <td width="510" ><img src="/assets/unmanaged/images/s.gif" width="510" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
    <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
  </tr>
  <tr>
     <td valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="510" height="23"><br>
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
		  <!--Retrieve from CMA -->
		  <img src='<content:pageImage type="pageTitle" beanName="LandingPageBean"/>'><br><br>
		  <table width="510" border="0" cellspacing="0" cellpadding="0">
			  <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="510" height="5"></td></tr>
              <tr>
				<td width="15" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td width="495" valign="top">
					<!-- Retrieve from CMA -->
					<content:getAttribute attribute="introduction1" beanName="LandingPageBean"/>
					<content:getAttribute attribute="introduction2" beanName="LandingPageBean"/><br>
					<b><content:getAttribute attribute="body1Header" beanName="LandingPageBean"/></b>
                </td>
              </tr>
              <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
              
			   <tr>
				<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td valign="top">
					<!-- Retrieve from CMA -->
					<content:getAttribute attribute="body1" beanName="LandingPageBean"/>
                  <!--<p>Just getting started with your Manulife plan? Enrolling new
                    participants? Need ideas on how to increase participation?
                    These topics provide step-by step guidelines and tools to
                    get you going.</p>-->
                 </td>
			   </tr>
			   
               <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5">
               <div id="errordivcs"><content:errors scope="session"/></div></td></tr>
				<tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				  <td>
				  	<content:userGuideLinks collectionName="contentItems" category="1" url="/do/contentpages/userguide/secondlevel/">
				  	    <content:param><%=String.valueOf(giflInd)%></content:param>
				    	<content:param><%=giflVersion3Indicator%></content:param>
				    	<!-- OB3 T3 Changes - Start -->
						<content:param><%=String.valueOf(deferralInd)%></content:param>
				    	<content:param><%=deferralMode%></content:param>
				    	<!-- OB3 T3 Changes - End -->
				    	<!-- JIRA#132 Contact us Changes - Start -->
				    	<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
				    	<!-- JIRA#132 Contact us Changes - End -->
				    	
				    	<content:param><%=String.valueOf(parentId)%></content:param>
				    	<content:param><%=String.valueOf(noticeManagerflag)%></content:param>
				    	
					</content:userGuideLinks>				  	  				  
				  </td>
				</tr>
  				
				<!-- Start second group of links -->
				<tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="20"></td></tr>
               	<tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				   <c:if test="${parentId == 75}">
				   <% if(noticeManagerflag) {%>
				  <td width="495" valign="top">
                  <p><b><content:getAttribute attribute="body2Header" beanName="LandingPageBean"/></b>
                  </td>
                   <%} %>
</c:if>
                   <c:if test="${parentId != 75}">
                   <td width="495" valign="top">
                  <p><b><content:getAttribute attribute="body2Header" beanName="LandingPageBean"/></b>
                  </td>
</c:if>
			    </tr>
			    <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
			    <tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				  <!-- Condition to display Notice Manager link for 'Fiduciary Resources' landing page-->
				  <c:if test="${parentId == 75}">
				  <% if(noticeManagerflag) {%>
				  	<td valign="top"><content:getAttribute attribute="body2" beanName="LandingPageBean"/></td>
				  <%} %>
</c:if>
				  <c:if test="${parentId != 75}">
				 	<td valign="top"><content:getAttribute attribute="body2" beanName="LandingPageBean"/></td>
</c:if>
			    </tr>
			   <!-- Send Service link added -->
					<c:if test="${parentId == 75}">
<c:if test="${userProfile.sendServiceAccessible == true}">
						<tr>
							<td colspan="2"><img src="/assets/unmanaged/images/s.gif"
								width="500" height="20"></td>
						</tr>
						<tr>
							<td width="5"><img src="/assets/unmanaged/images/s.gif"
								width="5" height="1"></td>
								<td width="495" valign="top">
									<p>
										<b>SEND Service</b>
								</td>
						</tr>
						<tr>
							<td colspan="2"><img src="/assets/unmanaged/images/s.gif"
								width="500" height="5"></td>
						</tr>
						<tr>
							<td width="5"><img src="/assets/unmanaged/images/s.gif"
								width="5" height="1"></td>
							<!-- Condition to display Send Service link for 'Fiduciary Resources' landing page-->
								<td valign="top"><content:getAttribute attribute="text"
										beanName="sendServiceLink" /></td>
						</tr>
</c:if>
</c:if>
					<tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
				<tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<!-- GIFL 1C- Start -->
				  <td>
				    <content:userGuideLinks collectionName="contentItems" category="2" url="/do/contentpages/userguide/secondlevel/" >
				    	<content:param><%=String.valueOf(giflInd)%></content:param>
				    	<content:param><%=giflVersion3Indicator%></content:param>
				    	<!-- OB3 T3 Changes - Start -->
						<content:param><%=String.valueOf(deferralInd)%></content:param>
				    	<content:param><%=deferralMode%></content:param>
				    	<!-- OB3 T3 Changes - End -->
				    	<!-- JIRA#132 Contact us Changes - Start -->
				    	<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
				    	<!-- JIRA#132 Contact us Changes - End -->
				    	
				    	<content:param><%=String.valueOf(parentId)%></content:param>
				    	<content:param><%=String.valueOf(noticeManagerflag)%></content:param>				    
					</content:userGuideLinks>
					
				  </td>
				
				<!-- GIFL 1C- End -->
  				</tr>	
				
				
  				<!-- Start third group of links -->
				<tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="20"></td></tr>
               	<tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				  <td width="495" valign="top">
                  <p><b><content:getAttribute attribute="body3Header" beanName="LandingPageBean"/></b>
                  </td>
			    </tr>
			    <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
			    <tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				  <td valign="top"><content:getAttribute attribute="body3" beanName="LandingPageBean"/></td>
			    </tr>
			    
			    <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
				<tr>
				  <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				  <td>
				    <content:userGuideLinks collectionName="contentItems" category="3" url="/do/contentpages/userguide/secondlevel/" />
				  </td>
  				</tr>    
			</table>
		 </td>
		 <td width="20" ><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		 
		 <td valign="top">
		   <img src="/assets/unmanaged/images/s.gif" width="1" height="25">            
		   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">     
		   <content:rightHandLayerDisplay layerName="layer1" beanName="LandingPageBean" />   
		   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">

	  <!-- GIFL 1C- Start... modified the code -->
			<c:if test="${parentId ==73}">
		   		<content:adminQuickGuide beanName="adminPageQuickServiceGuideLinesContent"/>
</c:if>
  			<c:if test="${parentId ==75}">
		   <content:rightHandLayerDisplay layerName="layer2" beanName="LandingPageBean" />
  				<ps:linkAccessible path="/do/resources/quarterlyInvestmentGuide/">		  
		   			<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		   			<content:rightHandLayerDisplay layerName="layer3" beanName="LandingPageBean" >
		   				<content:param><%=Constants.QIG_PATH%></content:param>
  					</content:rightHandLayerDisplay>
  				</ps:linkAccessible>		
</c:if>
  			<c:if test="${parentId ==73}">
	   			<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		   		<content:adminQuickGuide beanName="adminPageAdminGuideLinesContent"/>
</c:if>
		<!-- GIFL 1C- End -->	  		  

          </td>
		 <td width="20" ><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
  	</tr>
  <tr>
	 <td>
	   <br>
		 <p><content:pageFooter beanName="LandingPageBean"/></p>
 		 <p class="footnote"><content:pageFootnotes beanName="LandingPageBean"/></p>
 		 <p class="disclaimer"><content:pageDisclaimer beanName="LandingPageBean" index="-1"/></p>
 	 </td>
  </tr>
</table>
</td>

