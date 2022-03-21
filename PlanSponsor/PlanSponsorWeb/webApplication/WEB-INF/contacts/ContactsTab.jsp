<%-- Author: Venkatesh Kasiraj - This JSP was added for Contact Management Project June 2010 
This page is used display Tab section in Contact Information page and it contains the tab selection / 
suppression rules for the logged in user
--%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- TAB section --%>
<%@page import="com.manulife.pension.ps.web.Constants"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="jhContactsTabName" value="John Hancock Contacts"/>
<c:set var="psContactsTabName" value="Plan Sponsor"/>
<c:set var="tpaContactsTabName" value="Third Party Administrator"/>
<c:set var="brokerContactsTabName" value="Financial Representative"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="bottom" colspan="13">
		<DIV class="nav_Main_display" id="div">
		<UL class="">
		<c:choose>
			<c:when test="${userProfile.role.planSponsor && userProfile.selectedAccess}">
				<LI id="tab1" class="on"><c:out value="${jhContactsTabName}"/></LI>
			</c:when>
			<c:otherwise>
				<%-- JH Contacts --%>
				<c:if test="${param.tabValue == 1}">
					<LI id="tab1" class="on"><c:out value="${jhContactsTabName}"/></LI>
					<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly}" >
							<A href="/do/contacts/planSponsor/">
						</c:if> <c:out value="${psContactsTabName}"/> <c:if test="${empty param.printFriendly}" >
							</A>
						</c:if>
					</LI>
					<c:if test="${!(userProfile.role.planSponsor && userProfile.currentContract.bundledGaIndicator)}">					
					<c:if test="${param.tpaFirmAccessForContract == true}">
						<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${empty param.printFriendly}" >
								<A href="/do/contacts/thirdPartyAdministrator/">
							</c:if> <c:out value="${tpaContactsTabName}"/> <c:if test="${empty param.printFriendly}" >
								</A>
							</c:if>
						</LI>
					</c:if>
					</c:if>
					<c:if test="${displayBrokerTab == true}">
					<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly }" >
							<A href="/do/contacts/broker/">
						</c:if>
							<c:out value="${brokerContactsTabName}"/>
						<c:if test="${empty param.printFriendly}" >
							</A>
						</c:if>
					</LI>
					</c:if>
				</c:if>
				<%-- Plan Sponsor --%>
				<c:if test="${param.tabValue == 2}">
					<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
				        <c:if test="${empty param.printFriendly}" >
				          	<A href="/do/contacts/jhcontacts/">
				        </c:if> <c:out value="${jhContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
				        	</A>
				        </c:if>
			        </LI>
			        <LI id="tab2" class="on"><c:out value="${psContactsTabName}"/></LI>
					<c:if test="${!(userProfile.role.planSponsor && userProfile.currentContract.bundledGaIndicator)}">					
				        <c:if test="${param.tpaFirmAccessForContract == true}">
							<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
								<c:if test="${empty param.printFriendly}" >
									<A href="/do/contacts/thirdPartyAdministrator/">
								</c:if> <c:out value="${tpaContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
									</A>
								</c:if>
							</LI>
						</c:if>
					</c:if>
					<c:if test="${displayBrokerTab == true}">
					<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly }" >
							<A href="/do/contacts/broker/">
						</c:if>
							<c:out value="${brokerContactsTabName}"/>
						<c:if test="${empty param.printFriendly }" >
							</A>
						</c:if>
					</LI>
					</c:if>
				</c:if>
				<%-- TPA Contacts --%>
				<c:if test="${param.tabValue == 3}">
					<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
				        <c:if test="${empty param.printFriendly }" >
				          	<A href="/do/contacts/jhcontacts/">
				        </c:if> <c:out value="${jhContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
				        	</A>
				        </c:if>
			        </LI>
					<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly }" >
	        				<A href="/do/contacts/planSponsor/">
	        			</c:if>	<c:out value="${psContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
				        	</A>
				        </c:if>
				    </LI>
				    <c:if test="${!(userProfile.role.planSponsor && userProfile.currentContract.bundledGaIndicator)}">					
						<LI id="tab3" class="on"><c:out value="${tpaContactsTabName}"/></LI>
					</c:if>
					<c:if test="${displayBrokerTab == true}">
					<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly }" >
							<A href="/do/contacts/broker/">
						</c:if>
							<c:out value="${brokerContactsTabName}"/>
						<c:if test="${empty param.printFriendly }" >
							</A>
						</c:if>
					</LI>
					</c:if>
				</c:if>
				
				<%-- Broker Contacts --%>				
				<c:if test="${param.tabValue == 4}">
					<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
				        <c:if test="${empty param.printFriendly }" >
				          	<A href="/do/contacts/jhcontacts/">
				        </c:if> <c:out value="${jhContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
				        	</A>
				        </c:if>
			        </LI>
					<LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
						<c:if test="${empty param.printFriendly }" >
	        				<A href="/do/contacts/planSponsor/">
	        			</c:if>	<c:out value="${psContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
				        	</A>
				        </c:if>
				    </LI>
   				    <c:if test="${!(userProfile.role.planSponsor && userProfile.currentContract.bundledGaIndicator)}">					
					<c:if test="${param.tpaFirmAccessForContract == true}">
						<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
							<c:if test="${empty param.printFriendly }" >
								<A href="/do/contacts/thirdPartyAdministrator/">
							</c:if> <c:out value="${tpaContactsTabName}"/> <c:if test="${empty param.printFriendly }" >
								</A>
							</c:if>
						</LI>
					</c:if>
					</c:if>
					<LI id="tab4" class="on"><c:out value="${brokerContactsTabName}"/></LI>
				</c:if>

			</c:otherwise>
			</c:choose>
		</UL>
		</DIV>
		</td>
	</tr>
	</table>
