<!-- Author:  - 
This page is used display Tab section in Contact Information page and it contains the tab selection / 
suppression rules for the logged in user
-->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%-- TAB section --%>
<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="printFriendly" value="${param.printFriendly}" />
<content:contentBean contentId="<%=ContentConstants.NMC_BUILD_TAB_DISABLED_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="buildDisabledMessage"/>
<content:contentBean
	contentId="<%=ContentConstants.NMC_BUILD_YOUR_PACKAGE_TAB_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="buildYourPackageTabMouseOver" />
	<content:contentBean
	contentId="<%=ContentConstants.NMC_BUILD_YOUR_PACKAGE_TAB_NP_MOUSEOVER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="buildYourPackageTabNPMouseOver" />
<script src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">
	var buildYourPackageTabMouseOver = '<content:getAttribute id="buildYourPackageTabMouseOver" attribute="text" filter="true" escapeJavaScript="true"/>';
	var buildYourPackageTabNpMouseOver = '<content:getAttribute id="buildYourPackageTabNPMouseOver" attribute="text" filter="true" escapeJavaScript="true"/>';
	</script>

<c:set var="uploadAndShareTabName" value="Upload and Share"/>
<c:set var="buildYourPackageTabName" value="Build Your Package"/>
<c:set var="orderStatusTabName" value="Order Status"/>
<td colspan="3">
	<DIV class="nav_Main_display" id="div">
		<UL class="">
				<!-- Upload and Share -->
				<c:if test="${param.tabValue == 1}">
				
					<LI id="tab1" style="color:black !important" class="on"><span><c:out value="${uploadAndShareTabName}"/></span></LI>
<c:if test="${uploadsharedPlandocForm.buildYourPackageTab ==true}">

<c:if test="${uploadsharedPlandocForm.buildYourPackageNPTab ==true}">

							<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
								<c:if test="${printFriendly == null}" >
<c:if test="${uploadsharedPlandocForm.noDocumentAvailableInd ==false}">
										<a href="/do/noticemanager/buildyourpackage/">
</c:if>
<c:if test="${uploadsharedPlandocForm.noDocumentAvailableInd ==true}">
										<script>
											var buildDisabledMessage = "<content:getAttribute attribute="text" beanName="buildDisabledMessage"/>";
										</script>		
										<a onmouseover="Tip(buildDisabledMessage)" onmouseout="UnTip()">
</c:if>
								</c:if> <span><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly == null}" >
									</a>
								</c:if>
							</LI>
</c:if>
</c:if>
					
<c:if test="${uploadsharedPlandocForm.buildYourPackageTab ==false}">

<c:if test="${uploadsharedPlandocForm.buildYourPackageNPTab ==true}">

					<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null}" >
						</c:if> <span onmouseover="Tip(buildYourPackageTabMouseOver)"
																onmouseout="UnTip()"><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly == null}" >
						</c:if>
					</LI>
</c:if>
</c:if>
<c:if test="${uploadsharedPlandocForm.buildYourPackageNPTab ==false}">

							<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
								<c:if test="${printFriendly == null}" >
								</c:if> <span onmouseover="Tip(buildYourPackageTabNpMouseOver)"
																		onmouseout="UnTip()"><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly == null}" >
								</c:if>
							</LI>
</c:if>
					<LI id="tab3" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${printFriendly == null}" >
								<a href="/do/noticemanager/orderstatus/">
							</c:if> <span><c:out value="${orderStatusTabName}"/></span> <c:if test="${printFriendly == null}" >
								</a>
							</c:if>
						</LI>
					</c:if>
				<!-- Build Your Package -->
				<c:if test="${param.tabValue == 2}">
				
					<LI id="tab1" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
				        <c:if test="${printFriendly == null}" >
				          	<a href="/do/noticemanager/uploadandsharepages/">
				        </c:if> <c:out value="${uploadAndShareTabName}"/> <c:if test="${printFriendly == null}" >
				        	</a>
				        </c:if>
			        </LI>
						
			        <LI id="tab2" style="color:black !important" class="on"><span><c:out value="${buildYourPackageTabName}"/></span></LI>
					
					<LI id="tab3" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null}" >
							<a href="/do/noticemanager/orderstatus/">
						</c:if> <span><c:out value="${orderStatusTabName}"/></span> <c:if test="${printFriendly == null}" >
							</a>
						</c:if>
					</LI>
				</c:if> 
				<!-- Order Status -->
				<c:if test="${param.tabValue == 3}">
						
					<LI id="tab1" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
				        <c:if test="${printFriendly ==null }" >
				          	<a href="/do/noticemanager/uploadandsharepages/" >
				        </c:if> <c:out value="${uploadAndShareTabName}"/> <c:if test="${printFriendly == null}" >
				        	</a>
				        </c:if>
			        </LI>
<c:if test="${orderStatusForm.buildYourPackageTab ==true}">

<c:if test="${orderStatusForm.buildYourPackageNPTab ==true}">

							<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
								<c:if test="${printFriendly == null}" >
									<a href="/do/noticemanager/buildyourpackage/">
								</c:if> <span><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly ==null }" >
									</a>
								</c:if>
							</LI>
</c:if>
</c:if>
<c:if test="${orderStatusForm.buildYourPackageNPTab ==true}">
						
<c:if test="${orderStatusForm.buildYourPackageTab ==false}">
																
					<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null}" >
						</c:if> <span onmouseover="Tip(buildYourPackageTabMouseOver)"
																onmouseout="UnTip()"><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly == null}" >
						</c:if>
					</LI>
</c:if>
</c:if>
<c:if test="${orderStatusForm.buildYourPackageNPTab ==false}">

					<LI id="tab2" style="color:black !important" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null}" >
						</c:if> <span onmouseover="Tip(buildYourPackageTabNpMouseOver)"
																onmouseout="UnTip()"><c:out value="${buildYourPackageTabName}"/></span> <c:if test="${printFriendly == null}" >
						</c:if>
					</LI>
</c:if>
				   			
						<LI id="tab3" class="on" style="color:black !important"><span><c:out value="${orderStatusTabName}"/></span></LI>
				</c:if>
		</UL>
	</DIV>
</td>
<style>
DIV.nav_Main_display LI.on {
    background-repeat: no-repeat;
    background-image: url('/assets/unmanaged/images/1line_on_tab_long.gif');
    height: 33px;
    width: 111px;
    text-align: center;
    font-weight: bold;
    color: black !important;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 10px !important;
}
DIV.nav_Main_display LI {
    height: 35px;
    width: 111px;
    text-align: center;
    font-weight: bold;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px !important;
    color: black !important;
    line-height: 13px;
}
DIV.nav_Main_display LI.on {
    background-repeat: no-repeat;
    background-image: url("/assets/unmanaged/images/1line_on_tab_long.gif");
    height: 35px;
    width: 111px;
    text-align: center;
    font-weight: bold;
    color: black !important;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px !important;
}
DIV.nav_Main_display #tab1.on {
    height: 33px;    
}
DIV.nav_Main_display A:hover {
    color: black !important;
}
DIV.nav_Main_display A:visited {
    color: black !important;
}
DIV.nav_Main_display SPAN {
    color: black !important;
}
</style>
<!--[if IE]>
<style>
	DIV.nav_Main_display LI.on {
    height: 35px;
	}
	
	DIV.nav_Main_display #tab1.on {
		height: 35px;    
	}
</style>
<![endif]-->
