<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Imports --%>
<%@ page import="java.util.HashMap"%>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="constants"
	className="com.manulife.pension.ps.web.Constants" />

<content:contentBean
	contentId="${contentConstants.CHANGE_CRITERIA_WEIGHTING_LINK}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="changeCriteriaAndWeightingLink" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="50%" valign="top">
		<table width=100%>
<c:forEach items="${ipsAndReviewDetailsForm.criteriaAndWeightingPresentationList}" var="criteriaAndWeightingId" varStatus="index">


				<tr>
					<td align="center">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td height="11" width="11"
									style="background:${criteriaAndWeightingId.colorCode}"><img
									src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
							</tr>
						</table>
					</td>
<td valign="top">${criteriaAndWeightingId.criteriaDesc}</td>

<td valign="top" align="right">${criteriaAndWeightingId.weighting}%</td>

				</tr>
</c:forEach>
			
			<tr>
				<td></td>
				<td><span class="highlightBold">Total</span></td>
<td align="right"><span class="highlightBold">${ipsAndReviewDetailsForm.totalWeighting}%</span></td>

			</tr>
			<tr>
				<td colspan="3"><img src="/assets/unmanaged/images/s.gif"
					height="2"></td>
			</tr>
		</table>
		</td>
		<td width="50%" class="datacell1" align="center" valign="top"><ps:pieChart
			beanName="${constants.IPSR_CRITERIA_WEIGHTING_PIECHART}"
			alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
			title="IPSR Criteria And Weighting" /></td>
	</tr>
	<tr>
		<td valign="top">
			<table width="100%">
				<tr>
					<td width="7%"></td>
					<td width="70%" colspan="2" align="left" valign="top">
						<c:if test="${empty param.printFriendly }" >
							<c:if test="${ipsAndReviewDetailsForm.editLinkAccessible}">
								<content:getAttribute attribute="text" beanName="changeCriteriaAndWeightingLink">
									<content:param>javascript:openEditCriteriaAndWeightingsPage();</content:param>
								</content:getAttribute>
							</c:if>
						</c:if>
					</td>
				</tr>
			</table>
		</td>
		<c:set var="ipsChangeHistory" value="${ipsChangeHistory}"/>
		<td align="center" onmouseover="<ps:ipsAndReviewChangeDetail name='ipsChangeHistory' current='false'/>">Last modified on: <render:date
					patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"
					property="ipsAndReviewDetailsForm.lastModifiedOn" /></td>
	</tr>
	<tr>
		<td colspan="3"><img src="/assets/unmanaged/images/s.gif"
			height="10"></td>
	</tr>
</table>
