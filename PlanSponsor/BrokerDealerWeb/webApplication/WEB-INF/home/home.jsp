<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/home" prefix="home" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib tagdir="/WEB-INF/tags/security" prefix="security" %>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        <%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>

<style>
.messagePenTest {
    margin: 20px auto 20px auto;
    padding: 0px 10px 0px 10px;
    width: 85%;
    max-height:140px;
    overflow:auto;
    font-family: verdana, arial, helvetica, sans-serif;
}
.message_errorPenTest {
    background: #fed189 url('/assets/unmanaged/images/icons/icon_message_error.gif') no-repeat scroll 0px 0px;
    border: 1px solid #fdab29;
}
</style>


<%
	BDUserProfile userProfile = (BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<content:contentBean contentId="<%=BDContentConstants.HOME_PAGE_IMAGE_LAYER%>" 
      type="<%=BDContentConstants.TYPE_LAYER%>" id="homePageImageLayer" />
      
<content:contentBean contentId="<%=BDContentConstants.BOB_SUMMARY_FOOTNOTE%>"
                     type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
                     id="footnoteBOB"/>

<script type="text/javascript">
	function goToBOBSetup() {
		window.location = "/do/myprofile/createBOB";
	}

	window.onload = function() {
		$('.message').addClass('messagePenTest').removeClass('message');
		$('.message_error').addClass('message_errorPenTest').removeClass('message_error');
	}
</script>

<%
    BDSessionHelper.moveMessageIntoRequest(request);
%>
<report:formatMessages scope="request"/>

<c:set var="form" value="${requestScope.securedHomePageForm}" scope="page"/>
<div class="page_section" id="resources_dealernews">
	<div class="page_module" id="key_resources">
		<home:quickLinks layerId="${form.layerId}" showIeval="${form.showIeval}"/>
	</div>
<c:if test="${userProfile.role.roleType.userRoleCode ne 'RIA'}">
	<div class="page_module" id="news_updates">
		<home:newsEvents/>
    </div>	
    </c:if>		
</div><!--.page_section-->
			
<div class="page_section" id="message_noteworthy">
    <c:choose>    
	<c:when test="${form.showMyBOB}">	
		<div class="page_module_message_center" id="message_center">
			<home:myBOB myBOBSummary="${form.myBOBSummary}"/>
		</div>
	</c:when>
	<c:otherwise>
       <content:getAttribute id="homePageImageLayer" attribute="text"></content:getAttribute>	
	</c:otherwise>
	</c:choose>
	<c:if test="${not empty form.whatsNewContents || form.showWhatsNewLink}">	
		<div class="page_module" id="news_noteworthy">
			<home:whatsNew contents="${form.whatsNewContents}" showAll="${form.allWhatsNewContents}" showLink="${form.showWhatsNewLink}"/>
		</div>			
	</c:if>
 </div><!--.page_section-->
			
<div class="page_section">
	
		<c:if test="${form.showMessageCenter}">
		<div class="page_module" id="user_portfolio">
			<home:messageCenter mcSummary="${form.messageCenterSummary}" enablePreferencesLink="${form.enableMCPreferencesLink}"/>
		</div>
	</c:if>
	
	<c:if test="${form.showMessageCenter eq true and (userProfile.role.roleType.internal eq false
					 and  (userProfile.role.roleType.userRoleCode eq 'BRKLV2' 
					 		or userProfile.role.roleType.userRoleCode eq 'BRKAST' 
					 		or userProfile.role.roleType.userRoleCode eq 'FRMREP' 
					 		or userProfile.role.roleType.userRoleCode eq 'BRKLV1' 
					 		or userProfile.role.roleType.userRoleCode eq 'RIA')) }">
					<home:bdFocusedServices/>
	</c:if>
		
	<c:if test="${form.showBOBSetup}">
		<div class="page_module" id="fund_search">
			<h4><content:getAttribute attribute="body1Header" beanName="layoutPageBean" /></h4>
			<ul>
				<li>
					<content:getAttribute attribute="body1" beanName="layoutPageBean" />
				</li>
			</ul>
			<c:set var="disabledBoB" value="disabled"/>
			<security:isAuthorized url="/do/myprofile/createBOB">
			    <c:set var="disabledBoB" value=""/>
			</security:isAuthorized>
			<input type="button" onclick="goToBOBSetup()" value="Set up my block of business" ${disabledBoB}/>
			<div class="clear_footer"></div>
		</div>
	</c:if>	
	
	<c:if test="${form.showMessageCenter eq false and (userProfile.role.roleType.internal eq false
					 and  (userProfile.role.roleType.userRoleCode eq 'BRKLV2' 
					 		or userProfile.role.roleType.userRoleCode eq 'BRKAST' 
					 		or userProfile.role.roleType.userRoleCode eq 'FRMREP' 
					 		or userProfile.role.roleType.userRoleCode eq 'BRKLV1' 
					 		or  userProfile.role.roleType.userRoleCode eq 'RIA')) }">
					<home:bdFocusedServices/>
	</c:if>
	

    <c:if test="${not empty layoutPageBean.layer1}">	
		<div class="page_module" id="fund_search">
			<home:ourInvestmentStory layer="${layoutPageBean.layer1}" />
		</div>
	</c:if>
	<c:if test="${form.showMarketingCommentary}">
	    <div class="page_module" id="fund_search">
		    <home:marketCommentaries contents="${form.marketingCommentaryContents}" />
		</div>
	</c:if>
</div>		
<br style="clear:both;" />	

<div class="footnotes">
    <dl>
      <dd><content:pageFooter beanName="layoutPageBean"/></dd> 
    </dl>
    <c:if test="${form.showMyBOB}">
      <dl>
         <dd><content:getAttribute beanName="footnoteBOB" attribute="text"/></dd> 
      </dl>
    </c:if>
    <dl>
      <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
    </dl>
	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>	
