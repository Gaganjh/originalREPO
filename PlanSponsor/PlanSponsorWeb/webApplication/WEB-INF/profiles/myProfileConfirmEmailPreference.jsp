<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.profiles.EditMyProfileForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<c:set var="userProfile" value="${userProfile}" scope="session" />

<content:contentBean
	contentId="<%=ContentConstants.EMIAL_PREFERENCE_SECTION_TITLE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="emailPreferenceSectionTitle" />
<content:contentBean contentId="<%=ContentConstants.SPECIAL_ATTRIBUTE_SECTION_TITLE%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="specialAttributeSectionTitle" />
	<jsp:useBean id="theForm" class="com.manulife.pension.ps.web.profiles.EditMyProfileForm" scope="session" />

<% boolean showExpand = request.getParameter("printFriendly") == null && theForm.getContractAccesses().size() > 1; %>

<%if (showExpand) {%>
<br/>
<TABLE cellSpacing=0 cellPadding=0 width="525" border=0>
	<TBODY>
		<TR>
			<TD width="9"><A href="javascript:openChangeWindow()"></A></TD>
			<TD width="357" align=right>
			<div align="left"><STRONG><A
				href="javascript:expandAllSections()"><img
				src="/assets/unmanaged/images/plus_icon_all.gif" border="0"></A>/<A
				href="javascript:contractAllSections()"><img
				src="/assets/unmanaged/images/minus_icon_all.gif" border="0"></A> All Sections </STRONG></div>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<Br>
<%}%>

<c:forEach items="${editMyProfileForm.contractAccesses}" var="contractAccesses" varStatus="theIndex" >
<c:if test="${contractAccesses.showEmailPreferences ==true}">
<%if (showExpand) {%>
<table class=box style="CURSOR: pointer" onclick="expandcontent('sc${theIndex}')"	border="0" cellpadding="0" cellspacing="0" width="525">
<%} else {%>
<table class=box border="0" cellpadding="0" cellspacing="0" width="525">
<%}%>
	<tbody>

		<tr class="tablehead">
			<td class="tableheadTD1"><b>
<%if (showExpand) {%>
			<img src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;
<%}%>
				<ps:isInternalOrTpa name="userProfile" property="role">
				<content:getAttribute beanName="emailPreferenceSectionTitle" attribute="text" />
				</ps:isInternalOrTpa>				
				<ps:isNotInternalOrTpa name="userProfile" property="role">
				<b><content:getAttribute beanName="specialAttributeSectionTitle" attribute="text" /> </b>
				</ps:isNotInternalOrTpa>
				<ps:isNotInternalOrTpa name="userProfile" property="role">
for ${contractAccesses.contractName}${contractAccesses.contractNumber}
				</ps:isNotInternalOrTpa>
			</b></td>
		</tr>
	</tbody>
</table>
<%if (showExpand) {%>
<DIV class="switchcontent" id="sc${theIndex}" style="display:none">
<%} else {%>
<DIV class="switchcontent" id="sc${theIndex}" style="display:block">
<%}%>
<table border="0" cellpadding="0" cellspacing="0" width="525">
	<tbody>
				<tr class="datacell1">
                  <td width="1" rowspan="20" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                  <td colspan="2" valign="top" class="tablesubhead"><b></b></td>
                  <td width="1" rowspan="20" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                </tr>
                <ps:isNotInternalOrTpa name="userProfile" property="role">
<c:if test="${contractAccesses.specialAttributeAllowed ==true}">
<c:if test="${contractAccesses.disableAttributes ==false}">
						<tr class="datacell1">
							<td width="40%" valign="top">Primary contact</td>
							<td valign="top">
							<div align="left">
<c:if test="${contractAccesses.primaryContactChanged ==true}">
								<span class ="highlightBold">
</c:if>
								<render:yesno property="contractAccesses.primaryContact"/>
<c:if test="${contractAccesses.primaryContactChanged ==true}">
								</span>
</c:if>
							</div>
							</td>
						</tr>
						<tr class="datacell1">
							<td width="40%" valign="top">Client mail recipient</td>
							<td valign="top">
							<div align="left">
<c:if test="${contractAccesses.mailRecepientChanged ==true}">
								<span class ="highlightBold">
</c:if>
								<render:yesno property="contractAccesses.mailRecepient"/>
<c:if test="${contractAccesses.mailRecepientChanged ==true}">
								</span>
</c:if>
							</div>
							</td>
						</tr>
<c:if test="${contractAccesses.trusteeMailRecepientAllowed ==true}">
								<tr class="datacell1">
									<td width="40%" valign="top">Trustee mail recipient</td>
									<td valign="top">
									<div align="left">
<c:if test="${contractAccesses.trusteeMailRecepientChanged ==true}">
										<span class ="highlightBold">
</c:if>
										<render:yesno property="contractAccesses.trusteeMailRecepient"/>
<c:if test="${contractAccesses.trusteeMailRecepientChanged ==true}">
										</span>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
</c:if>
</c:if>
                </ps:isNotInternalOrTpa>
                <ps:isInternalOrTpa name="userProfile" property="role">
					<!-- General notifications -->
					<logicext:if name="contractAccesses" property="showEmailNewsletter" op="equal" value="true">
					<logicext:then>

						<tr class="datacell1">
							<td colspan="2" valign="top" class="tablesubhead"><b>General notifications </b></td>
						</tr>
					
<c:if test="${contractAccesses.showEmailNewsletter ==true}">
							<tr class="datacell1">
								<td width="40%" valign="top">Send newsletters, etc by email</td>
								<td valign="top">
								<div align="left">
<c:if test="${contractAccesses.emailNewsLetterChanged ==true}">
									<span class ="highlightBold">
</c:if>
${contractAccesses.emailNewsletter}
<c:if test="${contractAccesses.emailNewsLetterChanged ==true}">
									</span>
</c:if>
								</td>
							</tr>
</c:if>
					
					</logicext:then>
					</logicext:if>

					<!-- Participant services -->
					<logicext:if name="contractAccesses" property="showReceiveILoanEmail" op="equal" value="true">
					<logicext:then>

						<tr class="datacell1">
							<td colspan="2" valign="top" class="tablesubhead"><b>Participant
							services </b></td>
						</tr>

<c:if test="${contractAccesses.showReceiveILoanEmail ==true}">
								<tr class="datacell1">
									<td width="40%" valign="top">Receive i:loans email</td>
									<td valign="top">
									<div align="left">
<c:if test="${contractAccesses.receiveILoanEmailChanged ==true}">
										<span class ="highlightBold">
</c:if>
${contractAccesses.receiveILoanEmail}
<c:if test="${contractAccesses.receiveILoanEmailChanged ==true}">
										</span>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
						
					</logicext:then>
					</logicext:if>
	  			</ps:isInternalOrTpa>
		<tr>
			<td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1"></td>
		</tr>
	</tbody>
</table>
</DIV>
<Br>
</c:if>
</c:forEach>
