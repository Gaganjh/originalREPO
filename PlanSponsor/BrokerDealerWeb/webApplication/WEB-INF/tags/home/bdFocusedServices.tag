<%@ tag body-content="empty"
 import="com.manulife.pension.bd.web.BDConstants" 
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.content.BDContentConstants"
 import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
 
%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%
	try {
%>
<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
getJspContext().setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<content:contentBean
	contentId="<%=BDContentConstants.BDFOCUSED_LAYER_ID_FOR_BROKER%>"
	type="<%=BDContentConstants.TYPE_LAYER%>" id="BRKLV2" />
<content:contentBean
	contentId="<%=BDContentConstants.BDFOCUSED_LAYER_ID_FOR_BASIC_BROKER%>"
	type="<%=BDContentConstants.TYPE_LAYER%>" id="BRKLV1" />
<content:contentBean
	contentId="<%=BDContentConstants.BDFOCUSED_LAYER_ID_FOR_FIRM_REP%>"
	type="<%=BDContentConstants.TYPE_LAYER%>" id="FRMREP" />
<content:contentBean
	contentId="<%=BDContentConstants.BDFOCUSED_LAYER_ID_FOR_BROKER_ASSIST%>"
	type="<%=BDContentConstants.TYPE_LAYER%>" id="BRKAST" />
<content:contentBean
	contentId="<%=BDContentConstants.BDFOCUSED_LAYER_ID_FOR_RIA%>"
	type="<%=BDContentConstants.TYPE_LAYER%>" id="RIA" />

<c:if test="${userProfile.role.roleType.internal eq false}">

		<c:choose>
		<c:when test="${userProfile.role.roleType.userRoleCode eq 'BRKLV2'}">
			<c:set var="layer" value="${BRKLV2}"></c:set>
		</c:when>
		<c:when test="${userProfile.role.roleType.userRoleCode eq 'BRKLV1'}">
			<c:set var="layer" value="${BRKLV1}"></c:set>
		</c:when>
		<c:when test="${userProfile.role.roleType.userRoleCode eq 'BRKAST'}">
			<c:set var="layer" value="${BRKAST}"></c:set>
		</c:when>
		<c:when test="${userProfile.role.roleType.userRoleCode eq 'FRMREP'}">
			<c:set var="layer" value="${FRMREP}"></c:set>
		</c:when>
		<c:when test="${userProfile.role.roleType.userRoleCode eq 'RIA'}">
			<c:set var="layer" value="${RIA}"></c:set> 
		</c:when>
	</c:choose>



	<c:if test="${(not empty layer) and (not empty layer.title and not empty layer.text )}">
		<div class="page_module" id="fund_search">
			<h4>${layer.title }</h4>

			<ul>
				<li>${layer.text }</li>
			</ul>
			<div class="clear_footer"></div>

		</div>
	</c:if>
</c:if>

<%
	} catch (Exception e) {
		JspHelper.log("ourInvestmentStory.tag", e, "fails");
	}
%>

