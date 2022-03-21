<%@ tag import="com.manulife.pension.ps.web.Constants" %>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/assets/unmanaged/stylesheet/psMainTabNav.css">


<c:set var="tabs" value="${tpaBobTabsList}"/>
<DIV class="nav_Main_display">
	<UL class="">
		
			<%-- enabled means it is the "selected tab".--%>
			<c:forEach items="${tabs}" var="tabItem"  >
			
			<c:if test="${tabItem.isEnabled}">
				<LI class="on" >${tabItem.title}</LI>
			</c:if>
			<c:if test="${empty param['printFriendly']}">
				<%-- not enabled means it is not the "selected tab". It does not mean, it is disabled.--%>
				<c:if test="${not tabItem.isEnabled}">
					<c:if test="${tabItem.id eq 'pendingTab'}">
						<c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected eq true}">
							<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
								<A  href="${tabItem.actionURL}">${tabItem.title}</A>
							</LI>
						</c:if>
					<!-- The Pending Tab is suppressed when default date is not selected.. -->
						<c:if test="${tpaBlockOfBusinessForm.isDefaultDateSelected ne true}">
							<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
								${tabItem.title}
							</LI>
						</c:if> 
					
					</c:if>
					<c:if test="${tabItem.id ne 'pendingTab'}">
						<LI  onMouseOver="this.className='off_over';" onMouseOut="this.className='';">
							<A  href="${tabItem.actionURL}">${tabItem.title}</A>
						</LI>
					</c:if>
				</c:if>
			</c:if>
			</c:forEach>
			</UL>
</DIV>