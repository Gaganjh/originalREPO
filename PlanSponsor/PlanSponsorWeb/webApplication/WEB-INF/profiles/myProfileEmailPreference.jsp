<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

     
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>    
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.EditMyProfileForm" %>
<%@ page import="com.manulife.pension.ps.web.profiles.MyProfileContractAccess" %>
<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<content:contentBean
	contentId="<%=ContentConstants.EMIAL_PREFERENCE_SECTION_TITLE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="emailPreferenceSectionTitle" />
<content:contentBean contentId="<%=ContentConstants.SPECIAL_ATTRIBUTE_SECTION_TITLE%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="specialAttributeSectionTitle" />
<jsp:useBean id="theForm" class="com.manulife.pension.ps.web.profiles.EditMyProfileForm" scope="session" />
<c:set var="checkvalue" value="${theForm.getContractAccesses().size()}"/>


<%int contractAccessCount = Integer.parseInt(pageContext.getAttribute("checkvalue").toString());%>

<%if (contractAccessCount > 1) {%>
<br/>
<TABLE cellSpacing=0 cellPadding=0 width="525" border=0>
	<TBODY>
		<TR>
			<TD width="9"><A href="javascript:openChangeWindow()"></A></TD>
			<TD width="357" align=right>
			<div align="left"><STRONG><A
				href="javascript:expandAll()"><img
				src="/assets/unmanaged/images/plus_icon_all.gif" border="0"></A>/<A
				href="javascript:contractAll()"><img
				src="/assets/unmanaged/images/minus_icon_all.gif" border="0"></A> All Sections </STRONG></div>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<Br>
<%}%>
<c:forEach items="${theForm.contractAccesses}" var="contractAccesses" varStatus="theIndex" >
<c:if test="${contractAccesses.showEmailPreferences ==true}">
<%if (contractAccessCount > 1) {%>
<table class=box style="CURSOR: pointer" onclick="expandSection('sc${theIndex}')"	border="0" cellpadding="0" cellspacing="0" width="525">
<%} else {%>
<table class=box border="0" cellpadding="0" cellspacing="0" width="525">
<%}%>
	<tbody>

		<tr class="tablehead">
			<td class="tableheadTD1"><b>
<%if (contractAccessCount > 1) {%>
			<img id="sc${theIndex}img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;
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
${contractAccessCount}
<%if (contractAccessCount > 1) {%>
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
						<tr class="datacell1">
							<td width="320" valign="top">Primary contact</td>
							<td valign="top">
							<div align="left">
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].primaryContact" value="true" /> Yes
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].primaryContact" value="false" /> No
							</div>
							<form:hidden path="contractAccesses[${theIndex.index}].oldPrimaryContact"/>
							</td>
						</tr>
						<tr class="datacell1">
							<td width="320" valign="top">Client mail recipient</td>
							<td valign="top">
							<div align="left">
								
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].mailRecepient" value="true" /> Yes
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].mailRecepient" value="false" /> No
							</div>
							<form:hidden path="contractAccesses[${theIndex.index}].oldMailRecepient"/>
							</td>
						</tr>
<c:if test="${contractAccesses.trusteeMailRecepientAllowed ==true}">
								<tr class="datacell1">
									<td width="320" valign="top">Trustee mail recipient</td>
									<td valign="top">
									<div align="left">
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="true" /> Yes
<form:radiobutton disabled="${contractAccesses.disableAttributes}" path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="false" /> No
									</div>
	 <form:hidden path="contractAccesses[${theIndex.index}].oldTrusteeMailRecepient"/>
									</td>
								</tr>
</c:if>
</c:if>

<form:hidden path="contractAccesses[${theIndex.index}].contractNumber" />
<form:hidden path="contractAccesses[${theIndex.index}].primaryContactFirstName" />
<form:hidden path="contractAccesses[${theIndex.index}].primaryContactLastName" />
<form:hidden path="contractAccesses[${theIndex.index}].clientMailFirstName" />
<form:hidden path="contractAccesses[${theIndex.index}].clientMailLastName" />
<form:hidden path="contractAccesses[${theIndex.index}].trusteeMailLastName" />
<form:hidden path="contractAccesses[${theIndex.index}].trusteeMailFirstName" />
				</ps:isNotInternalOrTpa> 
				<ps:isInternalOrTpa name="userProfile" property="role">
					<!-- General notifications -->
					<c:if test="${contractAccesses.showEmailNewsletter ==true}">

						<tr class="datacell1">
							<td colspan="2" valign="top" class="tablesubhead"><b>General notifications </b></td>
						</tr>
<c:if test="${contractAccesses.showEmailNewsletter ==true}">
							<tr class="datacell1">
								<td width="320" valign="top">Send newsletters, etc by email</td>
								<td valign="top">
								<div align="left">
							<form:radiobutton path="contractAccesses[${theIndex.index}].emailNewsletter" value="Yes"/>Yes
							<form:radiobutton path="contractAccesses[${theIndex.index}].emailNewsletter" value="No"/>No
								</div>
								
							 <form:hidden path="contractAccesses[${theIndex.index}].oldEmailNewsletter"/> 
								</td>
							</tr>
</c:if>
					</c:if>

					<!-- Participant services -->
					<logicext:if name="contractAccesses" property="showReceiveILoanEmail" op="equal" value="true">
					<logicext:then>

						<tr class="datacell1">
							<td colspan="2" valign="top" class="tablesubhead"><b>Participant
							services </b></td>
						</tr>

<c:if test="${contractAccesses.showReceiveILoanEmail ==true}">
								<tr class="datacell1">
									<td width="320" valign="top">Receive i:loans email</td>
									<td valign="top">
									<div align="left">
									<!-- <input type="radio" name="contractAccesses.receiveILoanEmail" value="Yes"/>Yes
								<input type="radio" name="contractAccesses.receiveILoanEmail" value="No"/>No -->
 <form:radiobutton path="contractAccesses[${theIndex.index}].receiveILoanEmail" value="Yes" />Yes
 <form:radiobutton path="contractAccesses[${theIndex.index}].receiveILoanEmail" value="No" />No
								</div>
<input type="hidden" name="contractAccesses[${theIndex.index}].oldReceiveILoanEmail" />
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
