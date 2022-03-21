<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType"%>
<%@ page
	import="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO"%>
<%@ page import="java.util.ArrayList"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="quickreports"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>

<%@page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<script type="text/javascript">
      function doGoToContractDocuments() {
    	  document.getElementById('contractDocumentsLink').value='true';
    	  document.contractDocuments.submit();
      }
</script>


<%-- <form name="contractDocuments" method="post" action="/do/bob/contract/contractDocuments/" > --%>
<bd:form action="/do/bob/contract/contractDocuments/" method="post" modelAttribute="contractDocuments" name="contractDocuments"> 
      <input id="contractDocumentsLink" type="hidden" name="contractDocuments" value="false"/>
 </bd:form> 
<%--</form>--%>

<%-- This jsp includes the following CMA content --%> 
<content:contentBean
				contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>"
				type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />
<content:contentBean
				contentId="<%=BDContentConstants.PARTICIPANT_STATUS_NOT_AVAILABLE%>"
				type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="participantStatus" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>	
        <content:contentBean contentId="<%=BDContentConstants.PERA_IS_SELECTED%>" 
				type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="peraSelectedFootNote"/>
<content:contentBean contentId="<%=BDContentConstants.PERA_IS_NOT_SELECTED%>" 
				type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="peraNonSelectedFootNote"/>

<%-- Beans used --%>




<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	ContractSnapshotVO contractSnapshot = (ContractSnapshotVO)request.getAttribute(BDConstants.CONTRACT_SNAPSHOT);
	pageContext.setAttribute("contractSnapshot",contractSnapshot,PageContext.PAGE_SCOPE); 

%>



<c:set var="definedBenefit" value="${bobContext.currentContract.definedBenefitContract}"/>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<script type="text/javascript">
	function selectedDate()
	{
		if (document.contractSnapshotForm.stringDate.selectedIndex == 0){
			document.contractSnapshotForm.isRecentDate.value = "true";
		}else{
			document.contractSnapshotForm.isRecentDate.value = "false";
		}
		document.contractSnapshotForm.submit();
	}
</script>

<content:contentBean
	contentId="<%=BDContentConstants.MISCELLANEOUS_ROTH_INFO%>"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
	beanName="rothInfo" />
<input type="hidden" 
	   name="pdfCapped"
value="${contractSnapshotForm.pdfCapped}" />

<%--Report Title--%>
<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
<div><%-- contract message section --%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<report:formatMessages scope="request"/>
	</table>
</div>
<%--Navigation bar--%>
<navigation:contractReportsTab />
<div class="page_section_subheader controls"><%-- date filter and table heading section --%>
	<h3><content:getAttribute id="layoutPageBean" attribute="body1Header" /></h3>
	<bd:form  method="get"
		action="/do/bob/contract/contractSnapshot/"
		cssClass="page_section_filter form" name="contractSnapshotForm" modelAttribute="contractSnapshotForm">
<input type="hidden" name="isRecentDate" /><%--  input - name="contractSnapshotForm" --%>
		<p><label for="overview_filter_selector"> as of</label></p>
		<bd:select name="contractSnapshotForm" property="stringDate"
			onchange="setFilterFromSelect(this);doFilter();selectedDate();">
			<bd:dateOption name="<%=BDConstants.BOBCONTEXT_KEY%>"
				property="currentContract.contractDates.asOfDate"
				renderStyle="<%=RenderConstants.LONG_MDY%>" />
			<bd:dateOptions name="<%=BDConstants.BOBCONTEXT_KEY%>"
				property="currentContract.contractDates.monthEndDates"
				renderStyle="<%=RenderConstants.LONG_MDY%>" />
		</bd:select>
	</bd:form> 
	<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
</div>

<%--#overview_wrapper--%>
<div id="page_section_container">
	<div class="page_section">
		<div class="page_module"><%-- Contract Assets section --%>
			<h4>Contract Assets<sup>1</sup></h4>
			<table class="overview_table" id="contact_info_table" border="0"
				cellpadding="0" cellspacing="0" align="left">
				<tbody>
					<tr class="spec">
						<td><strong>Total Contract Assets<sup>2</sup></strong></td>
						<td><strong><report:number
							property="contractSnapshot.planAssets.totalPlanAssetsAmount"
							scope="<%=PageContext.REQUEST_SCOPE%>" 
							type="c" /></strong></td>
					</tr>
					<tr>
						<td>Allocated Assets</td>
						<td><report:number
							property="contractSnapshot.planAssets.allocatedAssetsAmount"
							scope="<%=PageContext.REQUEST_SCOPE%>" 
							type="c" /></td>
					</tr>
<c:if test="${contractSnapshot.planAssets.cashAccountAmount !='0.00'}">

						<tr class="spec">
							<td>Cash account</td>
							<td><report:number
								property="contractSnapshot.planAssets.cashAccountAmount"
								scope="<%=PageContext.REQUEST_SCOPE%>" 
								type="c" /></td>
						</tr>
</c:if>
<c:if test="${contractSnapshot.planAssets.uninvestedAssetsAmount !='0.00'}">

						<tr>
							<td>Pending transaction</td>
							<td><report:number
								property="contractSnapshot.planAssets.uninvestedAssetsAmount"
								scope="<%=PageContext.REQUEST_SCOPE%>" 
								type="c" /></td>
						</tr>
</c:if>
<c:if test="${contractSnapshotForm.displayPba ==true}">

						<tr>
							<td>Personal Brokerage Account<sup>&#8224;</sup></td>
							<td><report:number
								property="contractSnapshot.planAssets.personalBrokerageAccountAmount"
								scope="<%=PageContext.REQUEST_SCOPE%>" 
								type="c" /></td>
						</tr>
</c:if>
<c:if test="${contractSnapshotForm.displayLoan ==true}">

						<tr class="spec">
							<td>Loan Assets</td>
							<td><report:number
								property="contractSnapshot.planAssets.loanAssets"
								scope="<%=PageContext.REQUEST_SCOPE%>" type="c" defaultValue="$0.00" /></td>
						</tr>
</c:if>
	<c:if test="${not empty contractSnapshot.contractPeraDetailsVO}">
						<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera==true}"> 
							<c:if test="${contractSnapshotForm.isRecentDate==true}">
									<tr class="spec">
										<td><br/><strong>Total PERA Balance</strong></td>
										<td><br/><strong><report:number
											property="contractSnapshot.contractPeraDetailsVO.availablePeraBalance"
											scope="<%=PageContext.REQUEST_SCOPE%>" type="c" /></strong></td>
									</tr>
									<tr class="spec">
										<td>Prior year balance</td>
										<td><report:number
											property="contractSnapshot.contractPeraDetailsVO.priorBalance"
											scope="<%=PageContext.REQUEST_SCOPE%>" type="c" /></td>
									</tr>
									<tr class="spec">
										<td>Current year balance</td>
										<td><report:number
											property="contractSnapshot.contractPeraDetailsVO.currentBalance"
											scope="<%=PageContext.REQUEST_SCOPE%>" type="c" /></td>
									</tr>
							</c:if>
							<c:if test="${contractSnapshotForm.isRecentDate==false}">
									<tr class="spec">
										<td><br/><strong>Total PERA Balance</strong></td>
										<td><br/>Not available for selected date</td>
									</tr>
									<tr class="spec">
										<td>Prior year balance</td>
										<td></td>
									</tr>
									<tr class="spec">
										<td>Current year balance</td>
										<td></td>
									</tr>
							</c:if>
							</c:if>
					</c:if>
					<tr>
					 <c:if test="${not empty contractSnapshot.contractPeraDetailsVO}">
		        		<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera==true}">
							<c:if test="${contractSnapshotForm.isRecentDate==true}">
							<td colspan="2">Asset figures current as of market close on the selected "as of" date. <br/>PERA balance is current date.</td>
							</c:if>
							<c:if test="${contractSnapshotForm.isRecentDate==false}">
							<td colspan="2">Asset figures current as of market close on the selected "as of" date.</td>
							</c:if>
						</c:if>
						<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == false}">
		        			<td colspan="2">Asset figures current as of market close on the selected "as of" date.</td>
						</c:if>
					</c:if>
					<c:if test="${empty contractSnapshot.contractPeraDetailsVO}">
		        		<td colspan="2">Asset figures current as of market close on the selected "as of" date.</td>
					</c:if>
					</tr>
				</tbody>
			</table>
		
		</div>
		<div class="page_module"><%-- display the assets growth bar chart --%>
			<h4>Asset Growth</h4>
			<table class="overview_table_alt_td zero_padding arial_font" border="0" cellpadding="0" cellspacing="0"
				align="left">
				<tbody>
				    <tr></tr>
					<tr>
						<td colspan="3" border="0" style="font-size:20px;"><bd:barChart
							width="360" name="assetGrowthBarChart" mode="imagemap"
							scope="request"
							alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
							title="Asset Growth BarChart" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<%--.page_section--%>
	<div class="page_section">
	<c:if test="${definedBenefit!=true}">
		<div class="page_module"><%-- display the participant status section --%>
		<h4>Participant Status</h4>
		<table class="overview_table_alt" align="right" cellpadding="0"
			cellspacing="0">
			<tbody>
<c:if test="${contractSnapshotForm.isRecentDate ==true}">

<c:if test="${contractSnapshot.contractAssetsByRisk.hasEmployeeMoneyType ==true}">

						<tr>
							<td width="65%" style="font-size: 1em; vertical-align:top;">
								<table width="100%">
									<tr class="spec">
<c:if test="${contractSnapshot.contractAssetsByRisk.activeParticipantsNumber !=0}">

											<td width="9%">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td width="74%">Active</td>
<td width="17%" align="right">${contractSnapshot.contractAssetsByRisk.activeParticipantsNumber}</td>


</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.activeParticipantsNumber ==0}">

											<td width="100%" colspan="3"></td>
</c:if>
									</tr>
<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber !=0}">

										<tr>
											<td height="26">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Inactive with balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber !=0}">

										<tr>
											<td height="26">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Inactive unvested money only</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber}</td>

										</tr>
</c:if>
									<tr class="spec">
										<td>&nbsp;</td>
										<td><strong>Total</strong></td>
										<td align="right">
											<strong>
${contractSnapshot.contractAssetsByRisk.totalParticipantsNumber}

											</strong>
										</td>
									</tr>
								</table>
							</td>
							<td width="35%" rowspan="5" align="center" style="font-size: 1em;">
								<bd:pieChart scope="<%=PageContext.REQUEST_SCOPE%>" 
											beanName="<%=BDConstants.CONTRACT_SNAPSHOT_STATUS_PIECHART%>"
											alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
											title="Participant Status" />
							</td>
						</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.hasEmployeeMoneyType ==false}">


						<tr>
							<td width="65%" style="font-size: 1em; vertical-align:top;">
								<table width="100%">
									<tr class="spec">
<c:if test="${contractSnapshot.contractAssetsByRisk.activeContributingParticipantsNumber !=0}">

											<td width="9%">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td width="74%">Active</td>
<td width="17%" align="right">${contractSnapshot.contractAssetsByRisk.activeContributingParticipantsNumber}</td>


</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.activeContributingParticipantsNumber ==0}">

											<td width="100%" colspan="3"></td>
</c:if>
									</tr>
<c:if test="${contractSnapshot.contractAssetsByRisk.activeNoBalanceParticipantsNumber !=0}">

										<tr>
											<td height="26">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Active with no balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeNoBalanceParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.activeNotContributingParticipantsNumber !=0}">

										<tr>
											<td height="26">
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Active but not contributing</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeNotContributingParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.activeOptedOutParticipantsNumber !=0}">
										<tr>
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Active opted out</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeOptedOutParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber !=0}">

										<tr>
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Inactive with balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber !=0}">

										<tr class="spec">
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Inactive unvested money only</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.optedOutNotVestedParticipantsNumber !=0}">
										<tr class="spec">
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Opted out not vested</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.optedOutNotVestedParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.optedOutZeroBalanceParticipantsNumber !=0}">
										<tr class="spec">
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_OPTED_OUT_ZERO_BALANCE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Opted out zero balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.optedOutZeroBalanceParticipantsNumber}</td>

										</tr>
</c:if>
<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveParticipantsNumber !=0}">

										<tr class="spec">
											<td>
											<table border="0" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td
															style="background: <%=BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE%>;"
															height="7" width="7"><img src="/assets/unmanaged/images/s.gif"
															alt="" width="7" height="7" /></td>
													</tr>
												</tbody>
											</table>
											</td>
											<td>Inactive</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveParticipantsNumber}</td>

										</tr>
</c:if>
									<tr class="spec">
										<td>&nbsp;</td>
										<td><strong>Total</strong></td>
										<td align="right">
											<strong>
${contractSnapshot.contractAssetsByRisk.totalParticipantsNumber}

											</strong>
										</td>
									</tr>
								</table>
							</td>
							<td width="35%" rowspan="5" align="center" style="font-size: 1em;">
								<bd:pieChart scope="<%=PageContext.REQUEST_SCOPE%>"
											beanName="<%=BDConstants.CONTRACT_SNAPSHOT_STATUS_PIECHART%>"
											alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
											title="Participant Status" />
							</td>
						</tr>
						

</c:if>
</c:if>
<c:if test="${contractSnapshotForm.isRecentDate ==false}">

					<tr>
						<td><img src="/assets/unmanaged/images/s.gif" alt="" width="7" height="7" /></td>
					</tr>
					<tr>
						<td>
							<%
								// to add the info message about participant status not available
								// if selected date is not default date
					 			ArrayList<GenericException> notRecentDateInfoMessages = new ArrayList<GenericException>(1);
					            GenericExceptionWithContentType recentDateException = new GenericExceptionWithContentType(
					                    BDContentConstants.PARTICIPANT_STATUS_NOT_AVAILABLE,ContentTypeManager.instance().MISCELLANEOUS,false);
								notRecentDateInfoMessages.add(recentDateException);
								request.setAttribute(BDConstants.INFO_MESSAGES, notRecentDateInfoMessages);
							%>
							<report:formatMessages scope="request"/>
						</td>
					</tr>
</c:if>
			</tbody>
		</table>
		</div>
</c:if>
	<div class="page_module"><%--display the Contributions &  Withdrawals bar chart --%>
	<h4>Contributions &amp; Withdrawals</h4>
	<table class="overview_table_alt_td zero_padding arial_font" border="0" cellpadding="0"
		cellspacing="0" align="left" >
		<tbody>
			<tr></tr>
			<tr>
				<td colspan="3" border="0" style="font-size:20px;"><bd:barChart
					width="350" name="contrWithdrawalsBarChart" mode="imagemap"
					scope="request"
					alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
					title="Contributions &amp; Withdrawls BarChart" /></td>
			</tr>
		</tbody>
	</table>
	</div>
	</div>
	<%--- start contract snapshot asset allocation---%> 
	<jsp:include page="contractSnapshotAssetAllocation.jsp" flush="true" /> 
	<%--- end contract snapshot asset allocation --%>
	<div class="clear_footer"></div>
</div>
<%--#page_section_container--%>
<%--#footnotes--%>
<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>
<div class="footnotesHeight">
	<div class="footnotes">
	    <div class="footer"><content:pageFooter beanName="layoutPageBean"/></div> 
    	<br>    
	    <c:if test="${not empty footNotes}"> 
	    	<dl><dd><content:pageFootnotes beanName="layoutPageBean" index="0"/></dd></dl>
	    </c:if>
		<c:if test="${not empty contractSnapshot.contractPeraDetailsVO}">
		<c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == true}">
			<dl><dd><content:getAttribute beanName="peraSelectedFootNote" attribute="text"/></dd></dl>
			<br>
		</c:if>
		 <c:if test="${contractSnapshot.contractPeraDetailsVO.contractIsPera == false}">
			<dl><dd><content:getAttribute beanName="peraNonSelectedFootNote" attribute="text"/></dd></dl>
			<br>
		</c:if>
		</c:if>
		<c:if test="${empty contractSnapshot.contractPeraDetailsVO}">
			<dl><dd><content:getAttribute beanName="peraNonSelectedFootNote" attribute="text"/></dd></dl>
		</c:if>
	    <c:if test="${contractSnapshotForm.displayPba == true}">
			<dl><dd><content:getAttribute beanName="footnotePBA" attribute="text" /></dd></dl>
			<br>
</c:if>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
	</div>
</div>
