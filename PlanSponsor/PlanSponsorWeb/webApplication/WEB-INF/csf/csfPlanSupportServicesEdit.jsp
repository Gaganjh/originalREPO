<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants"%>
<%@ page import="com.manulife.pension.service.plan.valueobject.PlanDataLite"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String yesConstant = CsfConstants.CSF_YES;
pageContext.setAttribute("yes", yesConstant, PageContext.PAGE_SCOPE);

PlanDataLite planDataLite = (PlanDataLite) request.getAttribute(CsfConstants.REQ_PLAN_DATA_LITE);
pageContext.setAttribute("planDataLite",planDataLite);
%>
<table border="0" cellpadding="0" cellspacing="0" width="698">
	<tbody>
		<tr class="tablehead">
			<td class="tablehead" width="698" colspan="5"><b><content:getAttribute
				beanName="planSponsorServicesSectionTitle" attribute="text" /></b></td>

		</tr>
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead"><b> <content:getAttribute
				beanName="planSupportServicesSubSectionTitle" attribute="text" /> </b></td>
		</tr>
		<c:choose>
			<c:when test="${planDataLite.planTypeCode != '457'}">
				<tr class="datacell2">
			</c:when>
			<c:otherwise>
				<tr class="datacell1">
			</c:otherwise>
		</c:choose>
			<td width="375"><content:getAttribute
				beanName="planHighlightsCreaedLabel" attribute="text" /> </td>
			<td width="20" align="right"><ps:fieldHilight name="summaryPlanHighlightAvailable" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<c:choose>
				<c:when test="${planDataLite.planTypeCode == '457' or empty planDataLite.planLegalName}">
					<td colspan="2" width="302"><c:if test="${planDataLite.planTypeCode == '457'}">
				  &nbsp;&nbsp;Not allowed. [The Plan is a 457(b) plan.]
			      </c:if> <c:if test="${planDataLite.planTypeCode != '457' and empty planDataLite.planLegalName}">
			      &nbsp;&nbsp;Not allowed. [The Plan has no name.]
			      </c:if></td>
				</c:when>
				<c:otherwise>
<td width="55"><form:radiobutton onclick="checkPlanHighlights();" path="summaryPlanHighlightAvailable" value="Yes" /> Yes</td>

<td width="247"><form:radiobutton onclick="checkPlanHighlights();" path="summaryPlanHighlightAvailable" value="No" /> No</td>

				</c:otherwise>
			</c:choose>
			<ps:trackChanges name="csfForm" property="summaryPlanHighlightAvailable" />
		</tr>
		<tr class="datacell2 sph" id="summaryPlanHighlightReviewed">
			<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute beanName="planHighlightsReviewedLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="summaryPlanHighlightReviewed" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td><form:radiobutton path="summaryPlanHighlightReviewed" value="Yes" />Yes</td>
<td><form:radiobutton path="summaryPlanHighlightReviewed" value="No" />No</td>
			<ps:trackChanges name="csfForm" property="directMailInd" />
		</tr>
		<c:if test="${csfForm.showNoticesEdeliveryInEditPage ==true}">
		<tr class="datacell1">
			<td><content:getAttribute beanName="electronicDeliveryForPlanNoticesAndStatementsText" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"></td>
			<tr class="datacell1">				
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="wiredAtWorkText" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="wiredAtWork" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"></td>
				<td width="55" valign="top"><form:radiobutton onclick="checkNoiaStatus();" path="wiredAtWork" value="Yes" />Yes</td>	
				<td width="247" valign="top"><form:radiobutton path="wiredAtWork" value="No" /> No</td>	
				<ps:trackChanges name="csfForm" property="wiredAtWork" />
			</tr>		
			<tr class="datacell1">				
				<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="noticeOfInternetAvailabilityEdeliveryText" attribute="text" /></td>
				<td width="20" align="right"><ps:fieldHilight name="noticeOfInternetAvailability" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"></td>
				<td width="55" valign="top"><form:radiobutton  path="noticeOfInternetAvailability" value="Yes" />Yes</td>	
				<td width="247" valign="top"><form:radiobutton onclick="checkNoiaStatus();" path="noticeOfInternetAvailability" value="No" /> No</td>	
				<ps:trackChanges name="csfForm" property="noticeOfInternetAvailability" />
			</tr>
		</tr>
		</c:if>		
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell2">
		<%-- This is hard coded. CMA key is required --%>
			<td width="375"><content:getAttribute id="sendServiceTurnOnLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="noticeServiceInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
<td width="55" valign="top"><form:radiobutton onclick="checkSendService();" path="noticeServiceInd" value="Yes" />Yes</td>

<td width="247" valign="top"><form:radiobutton onclick="checkSendService();" path="noticeServiceInd" value="No" /> No</td>

			<ps:trackChanges name="csfForm" property="noticeServiceInd" />
		</tr>
		<tr class="datacell2" id="noticeTypeSelected">
		<%-- This is hard coded. CMA key is required --%>
			<td width="375" valign="top" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute id="sendServiceNoticeTypeLabel" attribute="text" />
			<ps:fieldHilight name="noticeTypeSelected" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="20" align="right" valign="top"></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
			<td colspan="2">
				<table>				
				<tr>				
<td align="left"><form:radiobutton path="noticeTypeSelected" value="1" /><%=CsfConstants.NOTICE_OPT_QDIA_DESC%></td>

				</tr>
				<tr>
<td align="left"><form:radiobutton path="noticeTypeSelected" value="2" /><%=CsfConstants.NOTICE_OPT_AUTO_DESC%></td>

				</tr>
				<tr>
<td align="left"><form:radiobutton path="noticeTypeSelected" value="3" /><%=CsfConstants.NOTICE_OPT_AUTO_QDIA_DESC%></td>

				</tr>
				<tr>
<td align="left"><form:radiobutton path="noticeTypeSelected" value="4" /><%=CsfConstants.NOTICE_OPT_SH_DESC%></td>

				</tr>
				<tr>
<td align="left"><form:radiobutton path="noticeTypeSelected" value="5" /><%=CsfConstants.NOTICE_OPT_SH_QDIA_DESC%></td>

				</tr>
				<tr>
<td align="left"><form:radiobutton path="noticeTypeSelected" value="0" /><%=CsfConstants.NOTICE_OPT_404A5_DESC%></td>

				</tr>		
				<ps:trackChanges name="csfForm" property="noticeTypeSelected" />
				</table>
			</td>
		</tr>
</c:if>
		<%
			int rowCount = 0;
		%>
		<c:if test="${planDataLite.planTypeCode != '457'}"> 
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="375"><content:getAttribute beanName="eligibilityCalculationLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="eligibilityCalculationInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
<td width="55" valign="top"><form:radiobutton onclick="checkECMoneyTypes();" path="eligibilityCalculationInd" value="Yes" />Yes</td>

<td width="247" valign="top"><form:radiobutton onclick="checkECMoneyTypes();" path="eligibilityCalculationInd" value="No" /> No</td>

			<ps:trackChanges name="csfForm" property="eligibilityCalculationInd" />
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1 ecSubSection" id="eligibilityMoneyTypesId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2 ecSubSection" id="eligibilityMoneyTypesId">
</c:if>
			<td width="375" valign="top" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
				beanName="eligibilityMoneyTypesLabel" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td class="greyborder"><img	src="/assets/unmanaged/images/spacer.gif" width="1" border="0"	height="1"></td>
			<td colspan="2">
			<table>
				<tr>
					<td>
					<table cellspacing="0" cellpadding="0">
<c:forEach items="${csfForm.eligibilityServiceMoneyTypes}" var="eligibityServiceMoneyTypeId" varStatus="index" >




							<%
								if (rowCount % 3 == 0) {
							%>
							<tr>
								<%
								}
								rowCount++;
							%>

								<td><form:checkbox path="selectedMoneyTypes" value="${eligibityServiceMoneyTypeId.moneyTypeShortName}">

</form:checkbox> ${eligibityServiceMoneyTypeId.moneyTypeShortName}</td>
								<%
								if (rowCount % 3 == 0) {
							%>
							</tr>
							<%
								}
							%>

</c:forEach>
<form:hidden path="eligibilityServiceMoneyTypesListSize"/>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
<form:hidden path="selectedMoneyTypes"/>
		</c:if>
		<c:choose>
		<c:when test="${csfForm.eligibilityCalculationInd == 'Yes'}">
<form:hidden path="checkEligibilityCalculationInd" value="true"/>
		</c:when>
		<c:otherwise>
<form:hidden path="checkEligibilityCalculationInd" value="false"/>
		</c:otherwise>
		</c:choose>
		
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1">
</c:if>
			<td width="375"><content:getAttribute beanName="jhEzstartLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="autoEnrollInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td width="55"><form:radiobutton onclick="checkAutoEnrollment();" path="autoEnrollInd" value="Yes" /> Yes</td>
<td width="247"><form:radiobutton onclick="checkAutoEnrollment();" path="autoEnrollInd" value="No" /> No</td>
			<ps:trackChanges name="csfForm" property="autoEnrollInd" />
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2 ae" id="initialEnrollmentDateId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1 ae" id="initialEnrollmentDateId">
</c:if>
			<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute beanName="initialEnrollEzstartLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="initialEnrollmentDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			<td colspan="2">&nbsp;
<form:input path="initialEnrollmentDate" maxlength="10" onchange="validateMonthDayYearDate(this)" readonly="true" size="10" id="initialEnrollmentCaleId"/>





                 <img onclick="javascript:popupCalendar('initialEnrollmentDate')"  src="/assets/unmanaged/images/cal.gif" border="0">
				(mm/dd/yyyy)
			</td>
			<ps:trackChanges name="csfForm" property="initialEnrollmentDate" />
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell2 ae" id="directMailIndId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell1 ae" id="directMailIndId">
</c:if>
			<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute beanName="directMailEnrollLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="directMailInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td><form:radiobutton path="directMailInd" value="Yes" /> Yes</td>
<td><form:radiobutton path="directMailInd" value="No" /> No</td>
			<ps:trackChanges name="csfForm" property="directMailInd" />
		</tr>
<form:hidden path="isFreezePeriod" />
<form:hidden path="aciAnniversaryDate" />
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="375"><content:getAttribute beanName="jhEzIncreaseLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="autoContributionIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td><form:radiobutton onclick="checkAutoContributionIncrease();" path="autoContributionIncrease" value="Yes" /> Yes</td>
<td><form:radiobutton onclick="checkAutoContributionIncrease();" path="autoContributionIncrease" value="No" /> No</td>
			<ps:trackChanges name="csfForm" property="autoContributionIncrease" />
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1 aci" id="aciAnniversaryId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2 aci" id="aciAnniversaryId">
</c:if>
			<td width="375" valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute beanName="firstScheduleIncreaseLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="aciAnniversaryYear" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<td colspan="2">&nbsp; ${csfForm.aciAnniversaryDate}(mm/dd)
			&nbsp;&nbsp;&nbsp;&nbsp;starting&nbsp;
<form:input path="aciAnniversaryYear" disabled="${csfForm.isDeferralEZiDisabled && csfForm.aciSignupMethod == 'A'}" maxlength="4" size="4" cssClass="inputField"/>(yyyy)</td>

			<ps:trackChanges name="csfForm" property="aciAnniversaryYear" />
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1 aci" id="increaseAnniversaryId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2 aci" id="increaseAnniversaryId">
</c:if>
			<td width="375">
 			 <table border="0" cellpadding="0" cellspacing="0"> 			  
			  <tr>
			        <td width="18">&nbsp;</td>
					<td width="357">
					<content:getAttribute beanName="initialIncreaseAnnuersityLabel" attribute="text" /></td>
			  </tr>
			 </table>
			</td>
			<td width="20" align="right"><ps:fieldHilight name="increaseAnniversary" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="1" height="15" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			<td colspan="2" width="302">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
<td width="55"><form:radiobutton disabled="${csfForm.isDeferralEZiDisabled && csfForm.aciSignupMethod == 'A'}" path="increaseAnniversary" value="N"/>First</td>
<td width="247"><form:radiobutton disabled="${csfForm.isDeferralEZiDisabled && csfForm.aciSignupMethod == 'A'}" path="increaseAnniversary" value="F"/>Second</td>
				    <ps:trackChanges name="csfForm" property="increaseAnniversary" />
				</tr>
				</table>
			</td>
		</tr>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td colspan="5">
<c:if test="${userProfile.role.planSponsor ==false}">
					 <jsp:include flush="true" page="csfVestingSectionEdit.jsp"></jsp:include>
</c:if>
<c:if test="${userProfile.role.planSponsor ==true}">
					 <jsp:include flush="true" page="csfVestingSectionConfirm.jsp"></jsp:include>
</c:if>
		 	</td>
		</tr>
		
	<!-- IPS -->
<c:if test="${csfForm.ipsServiceSuppressed ==false}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="375"><content:getAttribute beanName="investmentPolicyStatementServiceEdit" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td colspan="2" valign="top">&nbsp;${csfForm.planSponsorServicesData.ipsService}</td>
		</tr>
<c:if test="${csfForm.planSponsorServicesData.ipsService == yes}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<tr class="datacell2">
</c:if>
			<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="annualReviewServiceProcessingDateEdit" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td colspan="2" valign="top">&nbsp;${csfForm.planSponsorServicesData.annualReviewProcessingDateContentParam} (mm/dd)</td>
		</tr>
</c:if>
</c:if>
	  
	  <!-- Display Co-Fid Feature if contract has coFid. -->
<c:if test="${csfForm.coFidFeatureSuppressed ==false}">
<c:forEach items="${csfForm.coFidServiceFeatureDetails}" var="coFidServiceFeatureDetail" varStatus="coFidServiceFeature">
<c:set var="rowIndex" value="${coFidServiceFeature.index}"/>    
<c:if test="${csfForm.displayNoticeGeneration ==true}">
			<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="datacell2">
							</c:when>
							<c:otherwise>
								<tr class="datacell1">
							</c:otherwise>
						</c:choose>
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="datacell1">
							</c:when>
							<c:otherwise>
								<tr class="datacell2">
							</c:otherwise>
						</c:choose>
</c:if>
			<td width="375"><content:getAttribute beanName="coFiduciaryViewPage" attribute="text" >
			 <content:param>${coFidServiceFeatureDetail.coFidServiceProviderDescription}</content:param></content:getAttribute></td>
			<td width="20">&nbsp;</td>
<c:if test="${coFidServiceFeatureDetail.selectedServiceProvider ==true}">
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
				<td colspan="2" valign="top">&nbsp;Yes</td>
</c:if>
<c:if test="${coFidServiceFeatureDetail.selectedServiceProvider ==false}">
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
				<td colspan="2" valign="top">&nbsp;No</td>
</c:if>
		</tr>	
		
<c:if test="${coFidServiceFeatureDetail.selectedServiceProvider ==true}">
<c:if test="${csfForm.displayNoticeGeneration ==true}">
						<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="datacell2">
							</c:when>
							<c:otherwise>
								<tr class="datacell1">
							</c:otherwise>
						</c:choose>
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
			<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="datacell1">
							</c:when>
							<c:otherwise>
								<tr class="datacell2">
							</c:otherwise>
						</c:choose>
</c:if>
			<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute beanName="coFidWilshireAdviserServiceEditPage" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td colspan="2" valign="top">&nbsp;${csfForm.selectedInvestmentProfile}</td>
		</tr>
</c:if>
</c:forEach>
	
</c:if>
	</tbody>
</table>
