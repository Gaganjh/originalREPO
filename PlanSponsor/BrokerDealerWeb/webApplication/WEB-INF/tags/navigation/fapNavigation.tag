<%@ tag body-content="empty"
import="com.manulife.pension.platform.web.fap.constants.FapConstants"
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.navigation.UserNavigation" 
 import="java.util.ArrayList" 
 import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"
 import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
 import="com.manulife.pension.bd.web.BDConstants" 
 %>
<%@tag import="com.manulife.pension.delegate.ContractServiceDelegate" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="reportName" required="false" %> 


<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
getJspContext().setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
			 
<jsp:useBean id="userNavigation" class="com.manulife.pension.bd.web.navigation.UserNavigation"/>


<jsp:useBean id="userMenuItemsSelected" class="java.util.ArrayList" />
<c:set var="menuID" value="${layoutBean.selectedTabId}" />
<% 
	BDLayoutBean layoutBean = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
	if (session.getAttribute("fapTabNavigation") == null) { 
	
			UserNavigation fapTabNavigation= new UserNavigation();;
			UserMenu fapUserMenu = new UserMenu();
		
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.FUND_INFORMATION_TAB_ID, FapConstants.FUND_INFORMATION_TAB, FapConstants.FUND_INFORMATION_URL));
		
			fapUserMenu.addLevelOneUserMenuItem( new UserMenuItem(
					FapConstants.PRICES_YTD_TAB_ID, FapConstants.PRICES_YTD_TAB, FapConstants.PRICES_YTD_URL));
		
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.PERFORMANCE_FEES_TAB_ID, FapConstants.PERFORMANCE_FEES_TAB, FapConstants.PERFORMANCE_FEES_URL));
		
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.STANDARD_DEVIATION_TAB_ID, FapConstants.STANDARD_DEVIATION_TAB, FapConstants.STANDARD_DEVIATION_URL));
			
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.FUND_CHAR_I_TAB_ID, FapConstants.FUND_CHAR_I_TAB, FapConstants.FUND_CHAR_I_URL));
			
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.FUND_CHAR_II_TAB_ID, FapConstants.FUND_CHAR_II_TAB, FapConstants.FUND_CHAR_II_URL));
		
			fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.MORNINGSTAR_TAB_ID, FapConstants.MORNINGSTAR_TAB, FapConstants.MORNINGSTAR_URL));
			if(!userProfile.isMerrillAdvisor()) {
            fapUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
					FapConstants.FUNDSCORECARD_TAB_ID, FapConstants.FUNDSCORECARD_TAB, FapConstants.FUNDSCORECARD_URL));
			} 
			fapTabNavigation.setUserMenu(fapUserMenu);
			
			session.setAttribute("fapTabNavigation", fapTabNavigation);
	}

	userNavigation = (UserNavigation) session.getAttribute("fapTabNavigation"); 
	UserMenu userMenu =  userNavigation.getUserMenu();
	this.getJspContext().setAttribute("userMenu", userMenu);
	userMenuItemsSelected = userMenu.getSelectedMenuItems((String)this.getJspContext().getAttribute("menuID"));
	this.getJspContext().setAttribute("userMenuItemsSelected", userMenuItemsSelected);
%>

<c:set var="levelOneMenuID" value="${userMenuItemsSelected[0]}" /> 
		
	<div class="page_nav">
	<ul>
		<c:forEach var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}">
			<c:choose>
				<c:when test="${levelOneMenuID eq levelOneUserMenuItem.id}">
					<li class="active"><em class="fap_navigation_header" >${levelOneUserMenuItem.title}</em></li>
				</c:when>
				<c:otherwise>
					<li><a href="#" class="fap_navigation_header" onClick="applyFilter('tabsClick${levelOneUserMenuItem.id}', '${levelOneUserMenuItem.actionURL}');">${levelOneUserMenuItem.title}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

	</ul>
	</div>