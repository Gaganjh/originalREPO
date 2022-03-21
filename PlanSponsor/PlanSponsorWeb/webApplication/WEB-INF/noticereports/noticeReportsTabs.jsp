<!--  This page is used to display Notice Manager Control ReportsTabs -->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- TAB section --%>
<%@page import="com.manulife.pension.ps.web.Constants"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="printFriendly" value="${param.printFriendly}" />
<c:set var="alertsTabName" value="Alerts"/>
<c:set var="uploadandShareTabName" value="Upload and Share"/>
<c:set var="buildYourPackageTabName" value="Build Your Package"/>
<c:set var="orderStatusTabName" value="Order Status"/>
<c:set var="planSponsorWebStatsTabName" value="Plan Sponsor Website"/>
<c:set var="participantWebStatsTabName" value="Participant Website"/>
<td bgcolor="#fff9f2" colspan="3">
	<DIV class="nav_Main_display" id="div">
		<UL class="">
		
			<!-- Alert Preferences -->
			<c:if test="${param.tabValue == 1}">
				<LI id="tab1" class="on"><span><strong><c:out value="${alertsTabName}"/></strong></span></LI>
				<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/uploadAndShareReport/">
					</c:if> <span><strong><c:out value="${uploadandShareTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/buildYourPackageReport/">
						</c:if> <span><strong><c:out value="${buildYourPackageTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/orderStatusReport/">
						</c:if> <span><strong><c:out value="${orderStatusTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/planSponsorWebsiteReport/">
						</c:if> <span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab6" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/participantWebsiteReport/">
						</c:if> <span><strong><c:out value="${participantWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
			</c:if>
			
			<!-- Upload and Share -->
			<c:if test="${param.tabValue == 2}">
				<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/alertsReport/">
					</c:if> <span><strong><c:out value="${alertsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab2" class="on"><span><strong><c:out value="${uploadandShareTabName}"/></strong></span></LI>
				<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/buildYourPackageReport/">
						</c:if> <span><strong><c:out value="${buildYourPackageTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/orderStatusReport/">
						</c:if> <span><strong><c:out value="${orderStatusTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/planSponsorWebsiteReport/">
						</c:if> <span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab6" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/participantWebsiteReport/">
						</c:if> <span><strong><c:out value="${participantWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
			</c:if>	
			
			<!-- Build Your Package -->
			<c:if test="${param.tabValue == 3}">
				<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/alertsReport/">
					</c:if> <span><strong><c:out value="${alertsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/uploadAndShareReport/">
					</c:if> <span><strong><c:out value="${uploadandShareTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab3" class="on"><span><strong><c:out value="${buildYourPackageTabName}"/></strong></span></LI>
				<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/orderStatusReport/">
						</c:if> <span><strong><c:out value="${orderStatusTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/planSponsorWebsiteReport/">
						</c:if> <span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab6" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/participantWebsiteReport/">
						</c:if> <span><strong><c:out value="${participantWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
			</c:if>	
								
			<!-- Order Status -->
			<c:if test="${param.tabValue == 4}">
				<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/alertsReport/">
					</c:if> <span><strong><c:out value="${alertsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/uploadAndShareReport/">
					</c:if> <span><strong><c:out value="${uploadandShareTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/buildYourPackageReport/">
						</c:if> <span><strong><c:out value="${buildYourPackageTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab4" class="on"><span><strong><c:out value="${orderStatusTabName}"/></strong></span></LI>
				<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/planSponsorWebsiteReport/">
						</c:if> <span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab6" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/participantWebsiteReport/">
						</c:if> <span><strong><c:out value="${participantWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
			</c:if>	
			
			<!-- Plan Sponsor Web Stats -->
			<c:if test="${param.tabValue == 5}">
				<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/alertsReport/">
					</c:if> <span><strong><c:out value="${alertsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
					<c:if test="${printFriendly == null }" >
						<a href="/do/noticereports/uploadAndShareReport/">
					</c:if> <span><strong><c:out value="${uploadandShareTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
						</a>
					</c:if>
				</LI>
				<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/buildYourPackageReport/">
						</c:if> <span><strong><c:out value="${buildYourPackageTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/orderStatusReport/">
						</c:if> <span><strong><c:out value="${orderStatusTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
				<LI id="tab5" class="on"><span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span></LI>
				<LI id="tab6" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/participantWebsiteReport/">
						</c:if> <span><strong><c:out value="${participantWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
				</LI>
			</c:if>	
			
			<!-- Plan Sponsor Web Stats -->
			<c:if test="${param.tabValue == 6}">
					<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/alertsReport/">
						</c:if> <span><strong><c:out value="${alertsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
					</LI>
					<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${printFriendly == null }" >
							<a href="/do/noticereports/uploadAndShareReport/">
						</c:if> <span><strong><c:out value="${uploadandShareTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
							</a>
						</c:if>
					</LI>
					<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${printFriendly == null }" >
								<a href="/do/noticereports/buildYourPackageReport/">
							</c:if> <span><strong><c:out value="${buildYourPackageTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
								</a>
							</c:if>
					</LI>
					<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${printFriendly == null }" >
								<a href="/do/noticereports/orderStatusReport/">
							</c:if> <span><strong><c:out value="${orderStatusTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
								</a>
							</c:if>
					</LI>
					<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${printFriendly == null }" >
								<a href="/do/noticereports/planSponsorWebsiteReport/">
							</c:if> <span><strong><c:out value="${planSponsorWebStatsTabName}"/></strong></span> <c:if test="${printFriendly == null }" >
								</a>
							</c:if>
					</LI>
					<LI id="tab6" class="on"><span><strong><c:out value="${participantWebStatsTabName}"/></strong></span></LI>
				</c:if>	
			
		</UL>
	</DIV>
</td>
<style>

DIV.nav_Main_display LI {
    height: 35px;
    width: 111px;
    text-align: center;
    font-weight: bold;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 10px !important;
}
DIV.nav_Main_display LI span{
    color: #000099;
}
DIV.nav_Main_display LI.on {
    background-repeat: no-repeat;
    background-image: url('/assets/unmanaged/images/1line_on_tab_long.gif');
    height: 35px;
    width: 111px;
    text-align: center;
    font-weight: bold;
    color: #000;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 10px !important;
}

</style>
