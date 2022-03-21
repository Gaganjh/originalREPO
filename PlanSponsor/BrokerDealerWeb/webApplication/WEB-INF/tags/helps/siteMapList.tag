<%@ tag body-content="empty"
    import="com.manulife.pension.bd.web.BDConstants"
    import="com.manulife.pension.bd.web.navigation.UserMenu"
    import="com.manulife.pension.bd.web.navigation.UserMenuItem"
    import="com.manulife.pension.bd.web.navigation.UserNavigation"
    import="com.manulife.pension.bd.web.navigation.UserNavigationFactory"
    import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"
    import="com.manulife.pension.bd.web.content.BDContentConstants"
    import="com.manulife.pension.bd.web.ApplicationHelper"
    import="com.manulife.pension.bd.web.controller.SecurityManager"
    import="com.manulife.pension.bd.web.controller.AuthorizationSubject"
    import="com.manulife.pension.bd.web.navigation.URLConstants"
    import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
    import="com.manulife.pension.bd.web.util.BDSessionHelper"
    import="java.util.HashMap" import="java.util.Map"%>

<%@taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_MESSAGE_CENTER%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="messageCenterInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_BLOCK_OF_BUSINESS%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="blockOfBusinessInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_PRIME_ELEMENTS%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="primeElementsInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_FORMS%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="formsInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_PARTNERING_WITH_US%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="partneringWithUsInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_NEWS_EVENTS%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="newsEventsInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MISCELLANEOUS_FIND_LITERATURE%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="findLiteratureInfo" />
<content:contentBean
    contentId="<%=BDContentConstants.MY_PROFILE_HELP_LINK%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
    id="myProfileHelpPdfUrl" />

<%-- 
siteMpaList.tag
----------------
    This tag is used to Generate the html-content required to display the links in Site Map page in Help tab. 
    
    MenuGenerator creates the UserMenu object and places in Application/session scope. UserMenu object
    would contain the Menu Tree that needs to be displayed on the page.
    
    MenuRenderer constructs the html content to display that Menu Tree.

 --%>



<%
    int userMenuCount = 0;
    boolean isMessageCenterLink = false;
    boolean isRiaStatementsLink = false;
    boolean isContactUsLink = false;
    boolean isMyProfileLink = false;
    boolean isUserManagementLink = false;
    boolean isUserIsInMimic  = false;
    String strMyProfileLink = "";

    BDLayoutBean layoutBean = (BDLayoutBean) request
            .getAttribute(BDConstants.LAYOUT_BEAN);
    UserNavigation userNavigation = UserNavigationFactory.getInstance(
            application).getUserNavigaion(request, application);
    UserMenu userMenu = userNavigation.getUserMenu();
    if (userMenu.getLevelOneuserMenuItems().isEmpty()) {
        return;
    }
    if (userMenu.getLevelOneuserMenuItems() != null
            && userMenu.getLevelOneuserMenuItems().size() > 0) {
        userMenuCount = userMenu.getLevelOneuserMenuItems().size();
        if ((userMenuCount % 2) != 0) {
            userMenuCount = (userMenuCount / 2) + 1;
        } else {
            userMenuCount = userMenuCount / 2;
        }
    }

    BDUserProfile bdUserProfile = BDSessionHelper
            .getUserProfile((HttpServletRequest) request);
    AuthorizationSubject subject = ApplicationHelper
            .getAuthorizationSubject((HttpServletRequest) request);
    SecurityManager securityManager = ApplicationHelper
            .getSecurityManager(application);
    if (securityManager.isUserAuthorized(subject,
            URLConstants.MessageCenter)) {
        isMessageCenterLink = true;
    }
    if (securityManager.isUserAuthorized(subject,
            URLConstants.RiaEstatements)) {
    	isRiaStatementsLink = true;
    }
    if (securityManager.isUserAuthorized(subject,
            URLConstants.ContactUs)) {
        isContactUsLink = true;
    }

    if (bdUserProfile.isInternalUser()) {
        if (securityManager.isUserAuthorized(subject,
        URLConstants.MyProfileInternal)) {
            isMyProfileLink = true;
            strMyProfileLink = URLConstants.MyProfileInternal;
        }
    } else {
        if(bdUserProfile.isInMimic()){
            isUserIsInMimic = true;
        }else{
            if (securityManager.isUserAuthorized(subject,
            URLConstants.MyProfileExternal)) {
                isMyProfileLink = true;
                strMyProfileLink = URLConstants.MyProfileExternal;
            }
        }
    }

    if (securityManager.isUserAuthorized(subject,
            URLConstants.UserManagement)) {
        isUserManagementLink = true;
    }

    this.getJspContext().setAttribute("userMenu", userMenu);
    this.getJspContext().setAttribute("userMenuCount", userMenuCount);

    this.getJspContext().setAttribute("isMessageCenterLink",
            isMessageCenterLink);
    this.getJspContext().setAttribute("isRiaStatementsLink",
            isRiaStatementsLink);
    this.getJspContext().setAttribute("isContactUsLink",
            isContactUsLink);
    this.getJspContext().setAttribute("isMyProfileLink",
            isMyProfileLink);
    this.getJspContext().setAttribute("isUserManagementLink",
            isUserManagementLink);

    this.getJspContext().setAttribute("messageCenterUrl",
            URLConstants.MessageCenter);
    
    this.getJspContext().setAttribute("riaStatementsUrl",
            URLConstants.RiaEstatements);
    
    this.getJspContext().setAttribute("contactUsUrl",
            URLConstants.ContactUs);
    this.getJspContext().setAttribute("myProfileLinkUrl",
            strMyProfileLink);
    this.getJspContext().setAttribute("userManagementUrl",
            URLConstants.UserManagement);
    this.getJspContext().setAttribute("isUserIsInMimic",
            isUserIsInMimic);
    this.getJspContext().setAttribute("homeMenuTitle",
    		BDConstants.HOME_MENU_TITLE);
    this.getJspContext().setAttribute("homePageLink",
    		BDConstants.HOME_PAGE_LINK);
    this.getJspContext().setAttribute("messageCenterLink",
    		BDConstants.MESSAGE_CENTER_LINK);
    this.getJspContext().setAttribute("riaStatementsLink",
    		BDConstants.RIA_STATEMENTS_LINK);
    
    this.getJspContext().setAttribute("contactUsLink",
    		BDConstants.CONTACT_US_LINK);
    this.getJspContext().setAttribute("myProfileLink",
    		BDConstants.MY_PROFILE_LINK);
    this.getJspContext().setAttribute("userManagementLink",
    		BDConstants.USER_MANAGEMENT_LINK);
    this.getJspContext().setAttribute("partneringWithUsMenuTitle",
    		BDConstants.PARTNERING_WITH_US_MENU_TITLE);
    this.getJspContext().setAttribute("primeElementsMenuTitle",
    		BDConstants.PRIME_ELEMENTS_MENU_TITLE);
    this.getJspContext().setAttribute("newsAndEventsMenuTitle",
    		BDConstants.NEWS_AND_EVENTS_MENU_TITLE);
    this.getJspContext().setAttribute("findLiteratureMenuTitle",
    		BDConstants.FIND_LITERATURE_MENU_TITLE);
    this.getJspContext().setAttribute("formsMenuTitle",
    		BDConstants.FORMS_MENU_TITLE);
    this.getJspContext().setAttribute("siteMapMenuTitle",
    		BDConstants.SITE_MAP_MENU_TITLE);
    this.getJspContext().setAttribute("blockOfBusinessMenuTitle",
    		BDConstants.BLOCK_OF_BUSINESS_MENU_TITLE);
%>


<style type="text/css"> 
<!--
 
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td .siteMapTitle {
    font-family: Georgia, "Times New Roman", Times, serif;
    font-size: 16px;
    color: #000;
    font-weight: normal;
    margin-bottom: 0px;
    padding-bottom: 0px;   
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a {
    list-style-type: none;
    list-style-image: none;
    font-family: Verdana, Geneva, sans-serif;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a:hover {
    list-style-type: none;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul {
    list-style-type: none;
    padding-left: 0px;
    padding-top: 0px;
    margin-top: 0px;
    padding-bottom: 0px;
    margin-left: 10px;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li {
    color: #666;
    margin: 0px;
    padding: 0px;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 {
    background-color: #f5f6f1;
    border-top-width: 4px;
    border-top-style: solid;
    border-top-color: #1F2B38;
    margin: 0px 310px 0px 0px;
    margin-top: 20px;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 p {
    width:275px;
}    

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a{
    text-decoration:underline !important;
	color:#005B80 !important;
}

-->
</style>


<c:if test="${not empty layoutPageBean.layer1}">
    <div id="rightColumn1">
		<h1>${layoutPageBean.layer1.displayName}</h1>
    	<p>${layoutPageBean.layer1.text}</p>
    </div>
</c:if>
<div id="contentTitle"><content:getAttribute attribute="name"
    beanName="layoutPageBean" /></div>
<c:if test="${not empty layoutPageBean.introduction1}">
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
        beanName="layoutPageBean" attribute="introduction1" /></p>
</c:if>
<c:if test="${not empty layoutPageBean.introduction2}">
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
        beanName="layoutPageBean" attribute="introduction2" /></p>
</c:if>
<div id="content3">
<table width="100%">
    <tbody>
        <TR>
            <TD width="50%" valign="top"><c:forEach
                var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}"
                varStatus="loopIndex">
                <c:if test="${loopIndex.count le userMenuCount}">
                    <c:choose>
                        <c:when test="${homeMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${homePageLink}</p>
                            <ul>
                                <c:if test="${isMessageCenterLink}">
                                    <li><A href="${messageCenterUrl}">${messageCenterLink}</A></li>
                                    <li><content:getAttribute beanName="messageCenterInfo"
                                        attribute="text" /></li>
                                </c:if>
                                <c:if test="${isRiaStatementsLink}">
                                    <li><A href="${riaStatementsUrl}">${riaStatementsLink}</A></li>
                                </c:if>
                                <c:if test="${isContactUsLink}">
                                    <li><A href="${contactUsUrl}">${contactUsLink}</A></li>
                                </c:if>
                                <c:if test="${isMyProfileLink}">
                                    <li><A href="${myProfileLinkUrl}">${myProfileLink}</A></li>
                                </c:if>
                                <c:if test="${isUserIsInMimic}">
                                    <li style="color: #000;font-family: Verdana,Geneva,sans-serif;">${myProfileLink}</li>
                                    <li style="color: #000;font-family: Verdana,Geneva,sans-serif;"><content:getAttribute beanName="myProfileHelpPdfUrl"
                                        attribute="text" /></li>                                    
                                </c:if>
                                <c:if test="${isUserManagementLink}">
                                    <li><A href="${userManagementUrl}">${userManagementLink}</A></li>
                                </c:if>
                        </c:when>
                        <c:when
                            test="${partneringWithUsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title}</A></li>
                                <li><content:getAttribute beanName="partneringWithUsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${primeElementsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title} </A></li>
                                <li><content:getAttribute beanName="primeElementsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${newsAndEventsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title}</A></li>
                                <li><content:getAttribute beanName="newsEventsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${findLiteratureMenuTitle eq levelOneUserMenuItem.title}">
                        	<c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
                            	<p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            	<ul>
                         			<c:if test="${empty levelOneUserMenuItem.subMenuItems}">
                               			<li><A href="${levelOneUserMenuItem.actionURL}">
                               		 		${levelOneUserMenuItem.title}
                               		 	</A></li>
                               		<li>
                               			<content:getAttribute beanName="findLiteratureInfo" attribute="text" />
                               		</li>
                               </c:if>
							</c:if>
                        </c:when>
                        <c:when test="${formsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title} </A></li>
                                <li><content:getAttribute beanName="formsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${blockOfBusinessMenuTitle eq levelOneUserMenuItem.title}">
                        	<c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                               <c:if test="${empty levelOneUserMenuItem.subMenuItems}">
                               		 <li><A href="${levelOneUserMenuItem.actionURL}">
                               		 	Overview 
                            		 </A></li>
                               		<li>
                               			<content:getAttribute beanName="blockOfBusinessInfo" attribute="text" />
                               		</li>
                               </c:if>
                               </c:if>
                        </c:when>
                        <c:otherwise>
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
	                    <c:forEach var="levelTwoUserMenuItem"
	                        items="${levelOneUserMenuItem.subMenuItems}">
	                        <c:if test="${siteMapMenuTitle ne levelTwoUserMenuItem.title}">
	                            <li><A href="${levelTwoUserMenuItem.actionURL}">
	                            ${levelTwoUserMenuItem.title} </A></li>
	                        </c:if>
	                        <c:if test="${blockOfBusinessMenuTitle eq levelOneUserMenuItem.title and 'Overview' eq levelTwoUserMenuItem.title}">
	                                <li><content:getAttribute beanName="blockOfBusinessInfo"
	                                    attribute="text" /></li>
	                        </c:if>
	                        <c:if test="${findLiteratureMenuTitle eq levelOneUserMenuItem.title and 'Find Literature' eq levelTwoUserMenuItem.title}">
	                                <content:getAttribute beanName="findLiteratureInfo" attribute="text" />
	                        </c:if>
	                    </c:forEach>
	                    <li><br />
	                    </li>
	                </c:if>
                    </ul>
                </c:if>
            </c:forEach></TD>
            <TD width="50%" valign="top"><c:forEach
                var="levelOneUserMenuItem" items="${userMenu.levelOneuserMenuItems}"
                varStatus="loopIndex">
                <c:if test="${loopIndex.count > userMenuCount}">
                    <c:choose>
                        <c:when test="${homeMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${homePageLink}</p>
                            <ul>
                                <c:if test="${isMessageCenterLink}">
                                    <li><A href="${messageCenterUrl}">${messageCenterLink}</A></li>
                                    <li><content:getAttribute beanName="messageCenterInfo"
                                        attribute="text" /></li>
                                </c:if>
                                <c:if test="${isRiaStatementsLink}">
                                    <li><A href="${riaStatementsUrl}">${riaStatementsLink}</A></li>
                                </c:if>
                                <c:if test="${isContactUsLink}">
                                    <li><A href="${contactUsUrl}">${contactUsLink}</A></li>
                                </c:if>
                                <c:if test="${isMyProfileLink}">
                                    <li><A href="${myProfileLinkUrl}">${myProfileLink}</A></li>
                                </c:if>
                                <c:if test="${isUserIsInMimic}">
                                    <li style="color: #000;font-family: Verdana,Geneva,sans-serif;">${myProfileLink}</li>
                                    <li style="color: #000;font-family: Verdana,Geneva,sans-serif;"><content:getAttribute beanName="myProfileHelpPdfUrl"
                                        attribute="text" /></li>                                    
                                </c:if>
                                <c:if test="${isUserManagementLink}">
                                    <li><A href="${userManagementUrl}">${userManagementLink}</A></li>
                                </c:if>
                        </c:when>
                        <c:when
                            test="${partneringWithUsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title}</A></li>
                                <li><content:getAttribute beanName="partneringWithUsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${primeElementsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title} </A></li>
                                <li><content:getAttribute beanName="primeElementsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${newsAndEventsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title}</A></li>
                                <li><content:getAttribute beanName="newsEventsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${findLiteratureMenuTitle eq levelOneUserMenuItem.title}">
                        	<c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
                            	<p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            	<ul>
                         			<c:if test="${empty levelOneUserMenuItem.subMenuItems}">
                               			<li><A href="${levelOneUserMenuItem.actionURL}">
                               		 		${levelOneUserMenuItem.title}
                               		 	</A></li>
                               		<li>
                               			<content:getAttribute beanName="findLiteratureInfo" attribute="text" />
                               		</li>
                               </c:if>
							</c:if>
                        </c:when>
                        <c:when test="${formsMenuTitle eq levelOneUserMenuItem.title}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                                <li><A href="${levelOneUserMenuItem.actionURL}">
                                    ${levelOneUserMenuItem.title} </A></li>
                                <li><content:getAttribute beanName="formsInfo"
                                    attribute="text" /></li>
                        </c:when>
                        <c:when test="${blockOfBusinessMenuTitle eq levelOneUserMenuItem.title}">
                            <c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                               <c:if test="${empty levelOneUserMenuItem.subMenuItems}">
                               		 <li><A href="${levelOneUserMenuItem.actionURL}">
                               		 	Overview 
                            		 </A></li>
                               		<li>
                               			<content:getAttribute beanName="blockOfBusinessInfo" attribute="text" />
                               		</li>
                               </c:if>
                           </c:if>
                        </c:when>
                        <c:otherwise>
                            <p class="siteMapTitle">${levelOneUserMenuItem.title}</p>
                            <ul>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${levelOneUserMenuItem.actionURL ne '#'}">
	                    <c:forEach var="levelTwoUserMenuItem"
	                        items="${levelOneUserMenuItem.subMenuItems}">
	                        <c:if test="${siteMapMenuTitle ne levelTwoUserMenuItem.title}">
	                            <li><A href="${levelTwoUserMenuItem.actionURL}">
	                            ${levelTwoUserMenuItem.title} </A></li>
	                        </c:if>
	                        <c:if test="${blockOfBusinessMenuTitle eq levelOneUserMenuItem.title and 'Overview' eq levelTwoUserMenuItem.title}">
	                                <li><content:getAttribute beanName="blockOfBusinessInfo"
	                                    attribute="text" /></li>
	                        </c:if>
	                         <c:if test="${findLiteratureMenuTitle eq levelOneUserMenuItem.title and 'Find Literature' eq levelTwoUserMenuItem.title}">
	                                <content:getAttribute beanName="findLiteratureInfo" attribute="text" />
	                        </c:if>                
	                    </c:forEach>
	                    <li><br />
	                    </li>
	                 </c:if>
                    </ul>
                </c:if>
            </c:forEach></TD>
        </TR>
    </tbody>
</table>
</div>
