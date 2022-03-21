<%@ tag body-content="empty"
 import="com.manulife.pension.bd.web.BDConstants" 
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"
 import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

			 
			

<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
getJspContext().setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="bobUserMenu" scope="session" type="com.manulife.pension.bd.web.navigation.UserMenu" />


<style type="text/css"> 
#detailedAccountReportCSV ul li {
    float: right;
}
#planReviewReport ul li {
    float: right;
}
#planReviewReport1 ul li {
    float: right;
}
</style>                      

<c:if test="${not empty layoutBean.bobMenuId}">
	<div id="filter_wrapper"> 
		<div id="filter">
			<ul>
			<c:forEach var="bobMenuItem" items="${bobUserMenu.levelOneuserMenuItems}">
				<c:if test="${bobMenuItem.id eq layoutBean.bobMenuId and bobMenuItem.isEnabled}">
					<li class="active"><em><span>${bobMenuItem.title}</span></em></li>
					<c:set var="tabact" value="${bobMenuItem.id}"/>
				</c:if>
				<c:if test="${bobMenuItem.id ne layoutBean.bobMenuId}">
					<c:if test="${!bobMenuItem.isEnabled}">
						<li class="inactive"><em><span>${bobMenuItem.title}</span></em></li>
					</c:if>
					<c:if test="${bobMenuItem.isEnabled}">
						<li>
							<a href="javascript://" onclick="gotoTab('${bobMenuItem.actionURL}'); return false;">
								<span>${bobMenuItem.title}</span>
							</a>
						</li>
					</c:if>
				</c:if> 
			</c:forEach>
			</ul>			
			<c:if test="${userProfile.inMimic eq true or userProfile.role.roleType.internal eq false}">
			   <div id="detailedAccountReportCSV" align="left">
			   <ul><li>
			   <a href="javascript://" onClick="doDetailedAccountReportCSV();return false;" >
			   <span>Detailed Account Report</span>
			   </a></li>
			   </ul>
		       </div>
			</c:if>
		
			<c:if test="${(userProfile.role.roleType.internal eq false and  (userProfile.role.roleType.userRoleCode eq 'BRKLV2' or userProfile.role.roleType.userRoleCode eq 'BRKAST' )) or (userProfile.role.roleType.internal eq true and userProfile.role.roleType.userRoleCode eq 'RVP')}">
			   <c:if test="${showPlanReviewReportsLink eq true}">
				   <div id="planReviewReport" align="right">
					   <c:if test="${tabact eq 'activeTab' or tabact eq 'discontinuedTab'}">
						   <ul><li>
						   <a href="javascript:location='/do/bob/planReview/blockOfBusiness/?task=planReviewReports'" >
						   <span>Plan Review Reports</span>
						   </a></li>
						   </ul>
						</c:if> 
			       </div>
		       </c:if>
			</c:if>
		    
		</div><!--#filter-->
	</div>
</c:if>
