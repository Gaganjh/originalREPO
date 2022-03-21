<%@ tag body-content="empty"
 import="java.util.ArrayList"
 import="java.util.Collections"  
 import="com.manulife.pension.bd.web.BDConstants" 
 import="com.manulife.pension.bd.web.navigation.UserMenu"
 import="com.manulife.pension.bd.web.navigation.UserMenuItem"
 import="com.manulife.pension.bd.web.navigation.UserNavigation"
 import="com.manulife.pension.bd.web.navigation.UserNavigationFactory"
 import="com.manulife.pension.platform.web.CommonConstants"
 import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
MenuRenderer.tag
----------------
	This tag is used to Generate the html-content required to display the Menu. 
	
	MenuGenerator creates the UserMenu object and places in Application/session scope. UserMenu object
	would contain the Menu Tree that needs to be displayed on the page.
	
	MenuRenderer constructs the html content to display that Menu Tree.

 --%>


<jsp:useBean id="userMenuItemsSelected" class="java.util.ArrayList" />

<%
    BDLayoutBean layoutBean = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
	UserNavigation userNavigation = UserNavigationFactory.getInstance(application).getUserNavigaion(request, application);
	UserMenu userMenu = userNavigation.getUserMenu();
    if (userMenu.getLevelOneuserMenuItems().isEmpty()) {
    	return;
    }
	this.getJspContext().setAttribute("userMenu",userMenu);
	//Get the menuItems which need to be highlighted
	userMenuItemsSelected = userMenu.getSelectedMenuItems(layoutBean.getMenuId());
	Integer numOfUserMenuItems = 0;
	if (userMenuItemsSelected != null && !(userMenuItemsSelected.isEmpty())) {
		numOfUserMenuItems = userMenuItemsSelected.size();			
	}
	this.getJspContext().setAttribute("userMenuItemsSelected", userMenuItemsSelected);
	this.getJspContext().setAttribute("numOfUserMenuSelected", numOfUserMenuItems);
	HttpSession sessions = request.getSession(false);
	String updatePasswordInd = null ;
	String updatePasswordsuccessInd = null;
	if(sessions != null){
	if(request.getSession(false).getAttribute("isUpdatePasswordSuccessInd")!= null){ 
		updatePasswordInd = (String) request.getSession(false).getAttribute("isUpdatePasswordSuccessInd");
	} 
	if(updatePasswordInd != null ){
		updatePasswordsuccessInd = updatePasswordInd;	
	}
	else{
		updatePasswordsuccessInd = "false";
	}
	}
	else{
		updatePasswordsuccessInd = "false";
	}
	String referrer = null; 
	if(request.getHeader("referer")!=null){
	referrer = request.getHeader("referer");
	} 
	String refererInd = null;
	if(null!= referrer && referrer.contains("/updatePassword/") ){
		refererInd="true";
	}
	else{
		refererInd="false";
	}  
%>

<c:if test="${not empty userMenuItemsSelected}">
	<c:if test="${numOfUserMenuSelected ge 1}">
		<c:set var="levelOneMenuID" value="${userMenuItemsSelected[0]}" /> 
	</c:if>
</c:if>
 <% if (refererInd !="true" || updatePasswordsuccessInd == "true") {%>
<DIV id="MainMenu" class="id_nav_top">
	<TABLE class="" cellSpacing=0> 
		<TBODY>
			<TR>
<c:forEach var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}">
	<c:choose>
		<c:when test="${empty levelOneMenuID}">
			    <TD id="${levelOneUserMenuItem.id}MainMenu">
					<a id="${levelOneUserMenuItem.id}" class="off"
						href="${levelOneUserMenuItem.actionURL}"  
			      		rel="${levelOneUserMenuItem.id}rel">
			      		${levelOneUserMenuItem.title}
					</a>
				</TD>
		</c:when>
		<c:when test="${levelOneMenuID eq levelOneUserMenuItem.id}">
			    <TD id="${levelOneUserMenuItem.id}MainMenu" class="on">
					<a id="${levelOneUserMenuItem.id}" 
    	  				href="${levelOneUserMenuItem.actionURL}">
    	  				${levelOneUserMenuItem.title}
					</a>
				</TD>
		</c:when>
		<c:otherwise>
		   <c:set var="cursorStyle" value=""/>
		   <c:choose>
			   <c:when test="${levelOneUserMenuItem.actionURL eq '#'}">
			    <TD id="${levelOneUserMenuItem.id}MainMenu">
					<a id="${levelOneUserMenuItem.id}" class="off"
			      		rel="${levelOneUserMenuItem.id}rel" style="cursor: text">
			      		${levelOneUserMenuItem.title}
					</a>
				</TD>
			   </c:when>
			   <c:otherwise>
			    <TD id="${levelOneUserMenuItem.id}MainMenu">
					<a id="${levelOneUserMenuItem.id}" class="off"
						href="${levelOneUserMenuItem.actionURL}"  
			      		rel="${levelOneUserMenuItem.id}rel">
			      		${levelOneUserMenuItem.title}
					</a>
				</TD>			   
			   </c:otherwise>
		   </c:choose>
		</c:otherwise>
	</c:choose>
</c:forEach>
			</TR>
		</TBODY> 
	</TABLE> 
</DIV>
 <% } %> 
<DIV id=ctl00_menuBar_dropDownMenuControl_pnlDropDownMenu>

	<c:forEach var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}">
		<DIV id="${levelOneUserMenuItem.id}rel" class=dropmenudiv>
		<c:forEach var="levelTwoUserMenuItem" items="${levelOneUserMenuItem.subMenuItems}">
			<c:choose>
				<c:when test="${levelTwoUserMenuItem.id eq 'glossary'}">
					<a href="${levelTwoUserMenuItem.actionURL}" target="_blank">
						${levelTwoUserMenuItem.title}</a> 
				</c:when>
				<c:otherwise>
					<A href="${levelTwoUserMenuItem.actionURL}">
						${levelTwoUserMenuItem.title}
					</A>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</DIV>
	</c:forEach>

	<SCRIPT type=text/javascript>
	    cssdropdown.startchrome("MainMenu")
	</SCRIPT>
</DIV>

<div id="primary_nav_footer"></div>
		
<!-- Note: If there are no secondary nav items, then the secondary_nav div should not be shown -->
<c:if test="${(numOfUserMenuSelected ge 2) and (userMenu.menuMap[levelOneMenuID].childrenSize gt 0)}">
	<c:set var="hasSecondLevelNavigMenu" value="true" />
	<c:set var="levelTwoMenuID" value="${userMenuItemsSelected[1]}" />
</c:if>
<c:if test="${hasSecondLevelNavigMenu eq true}">
	 <% if (refererInd !="true" || updatePasswordsuccessInd == "true") {%>
	<div id="secondary_nav">
		<ul>
	<c:forEach var="levelTwoUserMenuItem" items="${userMenu.menuMap[levelOneMenuID].subMenuItems}">
		<c:choose>
			<c:when test="${levelTwoMenuID eq levelTwoUserMenuItem.id}">
				<li class="active"><strong>${levelTwoUserMenuItem.title}</strong></li>				
			</c:when>
			<c:otherwise>
				<li>
				<c:choose>
					<c:when test="${levelTwoUserMenuItem.id eq 'glossary'}">
						<a href="${levelTwoUserMenuItem.actionURL}" target="_blank">
							${levelTwoUserMenuItem.title}</a>
					</c:when>
					<c:otherwise>
						<a href="${levelTwoUserMenuItem.actionURL}">
								${levelTwoUserMenuItem.title}</a> 
								<%--  In <a> xyz</a>, there should be no spaces between the 
								content to be presented (i.e., xyz) and closing tag.--%>
					</c:otherwise>
				</c:choose>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
		</ul>
	</div><!--#secondary_nav-->
	  <% } %>
</c:if>
