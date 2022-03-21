<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	ContractSnapshotVO contractSnapshot = (ContractSnapshotVO)session.getAttribute(Constants.CONTRACT_SNAPSHOT);
	pageContext.setAttribute("contractSnapshot",contractSnapshot,PageContext.PAGE_SCOPE);
%>
<%-- This jsp includes the following CMA content --%>
<content:contentBean
	contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<%-- Beans used --%>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}" />

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_ROTH_INFO%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="rothInfo"/>
<content:contentBean contentId="<%=ContentConstants.PERA_IS_SELECTED%>" type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="peraSelectedFootNote"/>
<content:contentBean contentId="<%=ContentConstants.PERA_IS_NOT_SELECTED%>" type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="peraNonSelectedFootNote"/>

 <%
	boolean hasRoth = false;
	if (userProfile != null){
		hasRoth = userProfile.getContractProfile().getContract().hasRothNoExpiryCheck();
	}
	pageContext.setAttribute("hasRoth", hasRoth);
%> 

<table border="0" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td width="15" valign="top"><img
			src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
			<img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
		<td width="730" valign="top">
			<table width="730" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="500"><img src="/assets/unmanaged/images/s.gif"
						width="500" height="1"></td>
					<td width="20"><img src="/assets/unmanaged/images/s.gif"
						width="20" height="1"></td>
					<td width="180"><img src="/assets/unmanaged/images/s.gif"
						width="180" height="1"></td>
					<td width="15"><img src="/assets/unmanaged/images/s.gif"
						width="15" height="1"></td>
				</tr>
				<tr>
					<td valign="top"><img src="/assets/unmanaged/images/s.gif"
						width="500" height="23"><br> <!--page title--> <img
						src="<content:pageImage type="pageTitle" beanName="layoutPageBean" path="/assets/unmanaged/images/head_plan_snapshot.gif"/>"
						alt="Contract Snapshot"><br>

						<table width="500" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="5"><img src="/assets/unmanaged/images/s.gif"
									width="5" height="1"></td>
								<td width="295" valign="top"><br> <!--Layout/intro2-->
									<content:getAttribute beanName="layoutPageBean"
										attribute="introduction2" /> <br> <!--Layout/Intro1--> <content:pageIntro
										beanName="layoutPageBean" /> 
										<c:if test="${hasRoth == true}">
										 <br> <content:getAttribute
										attribute="text" beanName="rothInfo" /> </c:if> <br> <c:if
										test="${userProfile.currentContract.hasContractGatewayInd ==true}">
										<content:contentBean
											contentId="<%=ContentConstants.CONTRACT_SNAPSHOT_GIFL_MESSAGE%>"
											type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
											id="giflMessage" />
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%"><content:getAttribute id="giflMessage"
														attribute="text" /></td>
											</tr>
										</table>
									</c:if></td>
								<td width="20"><img src="/assets/unmanaged/images/s.gif"
									width="20" height="1"></td>
								<td width="180" valign="top">
									<% String UserProfKey = Constants.USERPROFILE_KEY; 
									   String.valueOf(Constants.USERPROFILE_KEY);
									   pageContext.setAttribute("UserProfKey", UserProfKey,PageContext.PAGE_SCOPE);
									   pageContext.setAttribute("False", false,PageContext.PAGE_SCOPE); %> 
									   <c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == False}">
										<content:contentBean
											contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
											type="<%=ContentTypeManager.instance().MISCELLANEOUS%>"
											beanName="HowToTitle" />
										<content:howToLinks id="howToLinks"
											layoutBeanName="layoutPageBean" />
										<quickreports:howToBox howToLinks="howToLinks"
											howToTitle="HowToTitle" />
									</c:if>
								</td>
							</tr>
						</table></td>
					<td><img src="/assets/unmanaged/images/s.gif" width="20"
						height="1"></td>
					<td valign="top" class="right"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
						<%-- Start of Quick Reports ------------------------------------------------------------------%>
						<jsp:include page="/WEB-INF/global/standardreportlistsection.jsp"
							flush="true" /> <%-- End of Quick Reports ------------------------------------------------------------------%>
						<%-- print report section --%> <jsp:include
							page="/WEB-INF/global/standardreporttoolssection.jsp"
							flush="true" /> <%-- end of print report section --%></td>
				</tr>
			</table>

			<div id="errordivcs">
				<content:errors scope="session" />
			</div> <%--- start contract snaphot assets---%> <jsp:include
				page="contractSnapshotAssets.jsp" flush="true" /> <%--- end contract snaphot assets --%>

			<%--- start contract snaphot asset growth---%> <jsp:include
				page="contractSnapshotAssetGrowth.jsp" flush="true" /> <%--- end contract snaphot asset growth --%>

			<%--- start contract snaphot asset allocation---%>  <jsp:include
				page="contractSnapshotAssetAllocation.jsp" flush="true" />  <%--- end contract snaphot asset allocation --%>


			<%--- start contract snaphot participant status---%> 
			<c:if test="${definedBenefit != true}">

				<c:if test="${contractSnapshotForm.isRecentDate == true}">
					<jsp:include page="contractSnapshotParticipantStatus.jsp"
						flush="true" />
				</c:if>

				<c:if test="${contractSnapshotForm.isRecentDate == false}">
					<tr class="tablesubhead">
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><b>Participant status</b></td>
									<td align="right">&nbsp;</td>
								</tr>
							</table>
						</td>
						<td class="dataheaddivider" valign="top"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="2" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><b>Participation rate</b></td>
									<td align="right">&nbsp;</td>
								</tr>
							</table>
						</td>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>


					<tr class="datacell1">
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td valign="top" colspan="3">Participant Status and
							Participation rate are not available for the date selected.</td>
						<td></td>
				</c:if>

				<%--- end contract snaphot participant status --%>
			</c:if>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
			width="1" height="1"></td>
	</tr>

	<tr class="datacell1">
		<td class="databorder" width="1"><img
			src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
		<td class="lastrow"><img src="/assets/unmanaged/images/s.gif"
			width="1" height="1"></td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif"
			width="1" height="1"></td>
		<td class="lastrow"><img src="/assets/unmanaged/images/s.gif"
			width="1" height="1"></td>
		<td rowspan="2" colspan="2" width="5" class="lastrow"><img
			src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
	</tr>
	<tr>
		<td class="databorder" colspan="4"><img
			src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<tr>
		<td colspan="4"><br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
				<p class="footnote">
	 				<content:pageFootnotes beanName="layoutPageBean" index="0"/><br>
		 			<c:if test="${not empty contractSnapshot.contractPeraDetailsVO }" >
		 			<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == true}">
			 				<content:getAttribute beanName="peraSelectedFootNote" attribute="text"/><br><br>
			 			</c:if>
			 			<c:if test="${contractSnapsho.contractPeraDetailsVO.contractIsPera == false}">
			 				<content:getAttribute beanName="peraNonSelectedFootNote" attribute="text"/><br><br>
			 			</c:if>
	 				</c:if>
	 				<c:if test="${empty contractSnapshot.contractPeraDetailsVO }" >
	 					<content:getAttribute beanName="peraNonSelectedFootNote" attribute="text"/><br><br>
	 				</c:if>
	 				<content:pageFootnotes beanName="layoutPageBean" index="1"/><br>
	 				<content:pageFootnotes beanName="layoutPageBean" index="2"/><br>
              		<c:if test="${contractSnapshotForm.displayPba == true}">
						<content:getAttribute beanName="footnotePBA" attribute="text"/><br>
					</c:if>
				</p>
			<p class="disclaimer">
				<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
			</p></td>
	</tr>

</table>
<br>
</td>
<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15"
	height="1"></td>
</tr>
</table>
