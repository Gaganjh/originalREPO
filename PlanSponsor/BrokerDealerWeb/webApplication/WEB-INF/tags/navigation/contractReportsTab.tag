<%@ tag body-content="empty"
 import="java.util.ArrayList"
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.navigation.UserNavigation" 
 import="com.manulife.pension.bd.web.bob.BobContext"
 import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
 import="com.manulife.pension.bd.web.BDConstants"
 import="com.manulife.pension.bd.web.bob.BobContext"
 import="com.manulife.pension.bd.web.util.BDSessionHelper"
 import="com.manulife.pension.bd.web.navigation.contractNavigation.ContractNavigationGenerator"
 import="com.manulife.pension.delegate.ContractServiceDelegate"
 import="com.manulife.pension.service.contract.valueobject.Contract"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ attribute name="attributeName" required="false" %>
<%@ attribute name="scope" required="false" %>
<%@ attribute name="menuID" required="false" %>  

<jsp:useBean id="userNavigation" class="com.manulife.pension.bd.web.navigation.UserNavigation"/>
<jsp:useBean id="userMenuItemsSelected" class="java.util.ArrayList" />
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />





<% 
BobContext bobContext =(BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
getJspContext().setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);


BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
getJspContext().setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:choose>
	<c:when test="${scope eq 'page'}">
		<% userNavigation = (UserNavigation) getJspContext().getAttribute(attributeName); %>	
	</c:when>
	<c:when test="${scope eq 'request'}">
		<% userNavigation = (UserNavigation) request.getAttribute(attributeName); %>	
	</c:when>
	<c:when test="${scope eq 'session'}">
		<% userNavigation = (UserNavigation) session.getAttribute(attributeName); %>	
	</c:when>
	<c:otherwise>
		<% userNavigation =ContractNavigationGenerator.getInstance((HttpServletRequest)request).generateUserNavigation(userProfile, bobContext); %>
	</c:otherwise>
</c:choose>

<%
	if (bobContext != null) {
	    Contract contract = bobContext.getCurrentContract();
	    boolean isDBContract = false;
	    if (contract != null) {
	        isDBContract = contract.isDefinedBenefitContract();
	    }
	    getJspContext().setAttribute("isDefinedBenefitContract", isDBContract);
	}
%>

<c:if test="${empty menuID}">
	<c:set var="menuID" value="${layoutBean.contractReportMenuId}" />

	<c:if test="${isDefinedBenefitContract eq true}">
		<c:if test="${empty layoutBean.contractReportMenuIdForDB}">
			<c:set var="menuID" value="${layoutBean.contractReportMenuId}" />
		</c:if>
		<c:if test="${not empty layoutBean.contractReportMenuIdForDB}">
			<c:set var="menuID" value="${layoutBean.contractReportMenuIdForDB}" />
		</c:if>
	</c:if>
</c:if>

<c:if test="${not empty menuID}">
	<%
		if (userNavigation == null) {
		    return;
		}
		UserMenu userMenu =  userNavigation.getUserMenu();
		this.getJspContext().setAttribute("userMenu", userMenu);
		userMenuItemsSelected = userMenu.getSelectedMenuItems((String)this.getJspContext().getAttribute("menuID"));
		Integer numOfUserMenuItems = 0;
		if (userMenuItemsSelected != null && !(userMenuItemsSelected.isEmpty())) {
			numOfUserMenuItems = userMenuItemsSelected.size();			
		}
		this.getJspContext().setAttribute("userMenuItemsSelected", userMenuItemsSelected);
		this.getJspContext().setAttribute("numOfUserMenuSelected", numOfUserMenuItems);
	%>
</c:if>

<c:if test="${not empty menuID}">
	<c:if test="${not empty userMenuItemsSelected}">
		<c:if test="${numOfUserMenuSelected ge 1}">
			<c:set var="hasFirstLevelNavigMenu" value="true" />
			<c:set var="levelOneMenuID" value="${userMenuItemsSelected[0]}" /> 
		</c:if>
		<c:if test="${hasFirstLevelNavigMenu eq true}">
			<div id="page_primary_nav" class="page_nav">
				<ul>
		</c:if>
		<c:forEach var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}">
			<c:choose>
				<c:when test="${levelOneMenuID eq levelOneUserMenuItem.id}">
					<li class="active"><em>${levelOneUserMenuItem.title}</em></li>
				</c:when>
				<c:otherwise>
					<li><a  href="${levelOneUserMenuItem.actionURL}">${levelOneUserMenuItem.title}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${hasFirstLevelNavigMenu eq true}">
				</ul>
			</div>
		</c:if>
			<!--#page_primary_nav-->
			<div id="page_primary_nav_footer"></div>

			
		<c:if test="${numOfUserMenuSelected ge 2}">
			<c:set var="hasSecondLevelNavigMenu" value="true" />
			<c:set var="levelTwoMenuID" value="${userMenuItemsSelected[1]}" />
		</c:if>
		<c:if test="${hasSecondLevelNavigMenu eq true}">
			<div id="page_secondary_nav">
				<ul>
		</c:if>
		<c:forEach var="levelTwoUserMenuItem" items="${userMenu.menuMap[levelOneMenuID].subMenuItems}">
			<c:choose>
				<c:when test="${levelTwoMenuID eq levelTwoUserMenuItem.id}">
                     <c:if test="${levelTwoUserMenuItem.id eq 'contractDocuments'}">
                           <li class="active"><strong><a href="javascript:doGoToContractDocuments();">${levelTwoUserMenuItem.title}</a></strong></li>
                    </c:if>
                    <c:if test="${levelTwoUserMenuItem.id eq 'pptStatements'}">
                           <li ><a href="#" onclick="pptStatemetns('${levelTwoUserMenuItem.actionURL}');" >${levelTwoUserMenuItem.title}</a></li>
                    </c:if>
                    <c:if test="${ (levelTwoUserMenuItem.id ne 'contractDocuments') &&  (levelTwoUserMenuItem.id ne 'pptStatements') }">
					     <li class="active"><strong><a href="${levelTwoUserMenuItem.actionURL}">${levelTwoUserMenuItem.title}</a></strong></li>
                    </c:if>
                    
				</c:when>
				<c:otherwise>
                    <c:if test="${levelTwoUserMenuItem.id eq 'contractDocuments'}">
                          <li><a  href="javascript:doGoToContractDocuments();">${levelTwoUserMenuItem.title}</a></li>
                    </c:if>
                     <c:if test="${levelTwoUserMenuItem.id eq 'pptStatements'}">
		    
                           <li ><a href="#" onclick="pptStatemetns('${levelTwoUserMenuItem.actionURL}');" >${levelTwoUserMenuItem.title}</a></li>
                    </c:if>
                    <c:if test="${(levelTwoUserMenuItem.id ne 'contractDocuments') &&  (levelTwoUserMenuItem.id ne 'pptStatements')}">
					      <li><a  href="${levelTwoUserMenuItem.actionURL}">${levelTwoUserMenuItem.title}</a></li>
                    </c:if>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${hasSecondLevelNavigMenu eq true}">
  				</ul>
			</div>
		</c:if>
		
	</c:if>
</c:if>
<script type="text/javascript">
	function pptStatemetns(url){
		var vsWin = window.open(url);
		vsWin.focus();
		return true;
	}
	


</script>
