<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.ContractAssetsByRiskAndParticipantStatusVO"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>	

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<%-- <c:set var="assetsByRisk" value="${BDConstants.CONTRACT_SNAPSHOT%>.contractAssetsByRisk}"  --%><%-- scope="request" type="ContractAssetsByRiskAndParticipantStatusVO" --%>



<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);

	

%>

<jsp:useBean id="contractSnapshot" scope="request"  
	class="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" />


<c:set var="assetsByRisk" value="${contractSnapshot.contractAssetsByRisk}" scope= "request"/>

<c:set var="definedBenefit" value="${bobContext.currentContract.definedBenefitContract}"/>

		   	 
<%--Access through contract snapshot page --%>
<%-- Start of Asset Allocation by Investment Category page --%>
<div class="page_module_long">
<h4>Asset Allocation by Risk/Return Category</h4>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<c:if test="${definedBenefit eq true}">
				<td colspan="2"><b>Allocated Contract Assets</b></td>
</c:if>
			<c:if test="${definedBenefit ne true}">
				<td colspan="2"><b>Allocated Contract Assets</b></td>
				<td><b>Asset Allocation by Age Group</b></td>
</c:if>
		</tr>
		<tr>
			<td width="213" style="font-size: 1em;">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
<c:if test="${contractSnapshotForm.displayLifecycle ==true}">
								<td width="17" valign="top">
								<table border="0" cellpadding="0" cellspacing="0">
									<tbody>
										<tr>
											<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_LIFECYCLE %>;"
												height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
										</tr>
									</tbody>
								</table>
								</td>
								<td width="100" valign="top">Target Date</td>
								<td width="100" align="right" valign="top">
									<report:number property="assetsByRisk.totalAssetsByRisk(LC)" type="c" />
								</td>
</c:if>
<c:if test="${contractSnapshotForm.displayLifecycle ==false}">
								<td colspan="3">&nbsp;</td>
</c:if>
						</tr>
						<tr>
							<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td
											style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_AGRESSIVE %>;"
											height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
									</tr>
								</tbody>
							</table>
							</td>
							<td width="100" valign="top">Aggressive Growth</td>
							<td width="100" align="right" valign="top">
								<report:number property="assetsByRisk.totalAssetsByRisk(AG)" type="c" />
							</td>
						</tr>
						<tr>
							<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td
											style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH %>;"
											height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
									</tr>
								</tbody>
							</table>
							</td>
							<td width="100" valign="top">Growth</td>
							<td width="100" align="right" valign="top">
								<report:number property="assetsByRisk.totalAssetsByRisk(GR)" type="c" />
							</td>
						</tr>
						<tr>
							<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td
											style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %>;"
											height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
									</tr>
								</tbody>
							</table>
							</td>
							<td width="100" valign="top">Growth &amp; Income</td>
							<td width="100" align="right" valign="top">
								<report:number property="assetsByRisk.totalAssetsByRisk(GI)" type="c" />
							</td>
						</tr>
						<tr>
							<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td
											style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_INCOME %>;"
											height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
									</tr>
								</tbody>
							</table>
							</td>
							<td width="100" valign="top">Income</td>
							<td width="100" align="right" valign="top"><report:number
								property="assetsByRisk.totalAssetsByRisk(IN)" type="c" /></td>
						</tr>
						<tr>
							<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td
											style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>;"
											height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
									</tr>
								</tbody>
							</table>
							</td>
							<td width="100" valign="top">Conservative</td>
							<td width="100" align="right" valign="top">
								<report:number property="assetsByRisk.totalAssetsByRisk(CN)" type="c" />
							</td>
						</tr>
						<tr>
<c:if test="${contractSnapshotForm.displayPba ==true}">

								<td valign="top">
								<table border="0" cellpadding="0" cellspacing="0">
									<tbody>
										<tr>
											<td
												style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_PBA %>;"
												height="7" width="7"><img height="7" width="7" src="/assets/unmanaged/images/s.gif"/></td>
										</tr>
									</tbody>
								</table>
								</td>
								<td width="100" valign="top">Personal Brokerage Account</td>
								<td width="100" align="right" valign="top">
									<report:number property="assetsByRisk.totalAssetsByRisk(PB)" type="c" />
								</td>
</c:if>
<c:if test="${contractSnapshotForm.displayPba ==false}">

								<td colspan="3" valign="top">&nbsp;</td>
</c:if>
						</tr>
					</tbody>
				</table>
			</td>
			<td width="103" style="font-size: 1em;">
				<bd:pieChart beanName="<%= BDConstants.CONTRACT_SNAPSHOT_RISK_PIECHART %>"
				alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
				title="Asset Allocation Chart" />
			</td>
			<c:if test="${definedBenefit eq true}">
				<td>&nbsp;</td>
</c:if>
			<c:if test="${definedBenefit ne true}">
				<td style="font-size: 1em;">
					<table border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
							
<c:if test="${assetsByRisk.getAgeGroup('<30').numberOfParticipants !=0}">

									<td align="center">
										<bd:pieChart 
										beanName="<%= BDConstants.CONTRACT_SNAPSHOT_AGE_BELOW_30_PIECHART %>"
										alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
										title="Asset Allocation Chart" /><br />
									</td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('30-39').numberOfParticipants !=0}">

									<td align="center">
										<bd:pieChart
										beanName="<%= BDConstants.CONTRACT_SNAPSHOT_AGE_30_39_PIECHART %>"
										alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
										title="Asset Allocation Chart" /><br />
									</td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('40-49').numberOfParticipants !=0}">

									<td align="center">
										<bd:pieChart
										beanName="<%= BDConstants.CONTRACT_SNAPSHOT_AGE_40_49_PIECHART %>"
										alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
										title="Asset Allocation Chart" /><br />
									</td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('50-59').numberOfParticipants !=0}">

									<td align="center">
										<bd:pieChart
										beanName="<%= BDConstants.CONTRACT_SNAPSHOT_AGE_50_59_PIECHART %>"
										alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
										title="Asset Allocation Chart" /><br />
									</td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('60+').numberOfParticipants !=0}">

									<td align="center">
										<bd:pieChart
										beanName="<%= BDConstants.CONTRACT_SNAPSHOT_AGE_60_ABOVE_PIECHART %>"
										alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
										title="Asset Allocation Chart" />
									</td>
</c:if>
							</tr>
							<tr>
<c:if test="${assetsByRisk.getAgeGroup('<30').numberOfParticipants !=0}">

									<td align="center">&lt;30 years old<br />
${assetsByRisk.getAgeGroup('<30').numberOfParticipants}

												<span class="no_wrap">Participant(s)</span></td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('30-39').numberOfParticipants !=0}">

									<td align="center">30-39 years old<br />
${assetsByRisk.getAgeGroup('30-39').numberOfParticipants}

												<span class="no_wrap">Participant(s)</span></td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('40-49').numberOfParticipants !=0}">

									<td align="center">40-49 years old<br />
${assetsByRisk.getAgeGroup('40-49').numberOfParticipants}

												<span class="no_wrap">Participant(s)</span></td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('50-59').numberOfParticipants !=0}">

									<td align="center">50-59 years old<br />
${assetsByRisk.getAgeGroup('50-59').numberOfParticipants}

												<span class="no_wrap">Participant(s)</span></td>
</c:if>
<c:if test="${assetsByRisk.getAgeGroup('60+').numberOfParticipants !=0}">

									<td align="center">60+ years old<br />
${assetsByRisk.getAgeGroup('60+').numberOfParticipants}

												<span class="no_wrap">Participant(s)</span></td>
</c:if>
							</tr>
							<tr>
								<td colspan="5" valign="top"><c:if test="${assetsByRisk.numberOfDefaultBirthDateParticipants gt 0}"> 
								  	Allocations by age group will not be accurate if complete and correct birth dates have not been provided.
									In these instances, default birth dates are assumed.
									<c:if test="${assetsByRisk.getAgeGroup(DEFAULT1980).numberOfParticipants gt 0}">  
This contract currently has ${assetsByRisk.getAgeGroup(DEFAULT1980).numberOfParticipants}

									participant(s) with a default birth date of January 1, 1980. </c:if>
									</c:if>
				 	 			</td>
							</tr>
						</tbody>
					</table>
				</td>
</c:if>
		</tr>
		
		<c:if test="${definedBenefit ne true}">
			<tr>
				<td colspan="2" valign="top" ><a class="underline" href="/do/bob/investment/investmentAllocationReport/">Go to
						Investment Allocation report</a></td>
				<td valign="top">&nbsp;</td>
			</tr>
</c:if>
	</tbody>
</table>
</div>
<%-- Start of Asset Allocation by Investment Category page --%>
