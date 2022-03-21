<%@ page
	import="com.manulife.pension.service.planReview.report.PlanReviewReportPrintHistoryDetailsReportData"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType"%>
<%@ page import="com.manulife.pension.platform.web.CommonConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
PlanReviewReportPrintHistoryDetailsReportData theReport = (PlanReviewReportPrintHistoryDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

                
%>
<form:hidden path="type"/>
<form:hidden path="requestType"/>
<div>
	<ul class="proposal_nav_menu">
		<li><a class="selected_link exempt"> <span style= "padding: 10px !important;" class=step_caption>Results-View
					or download reports</span></a></li>
	</ul>
</div>

<style>
	@media all and (-webkit-min-device-pixel-ratio:0) {
			.val_strh1{
						width:238px !important;
						text-align: left;
			}
			.val_strh2{
						width:128px !important;
						text-align: left;
			}
			.val_strh3{
						width:110px !important;
						text-align: left;
			}
			.val_strh4{
						width:112px !important;
						text-align: left;
			}
			.val_strh5{
						width:149px !important;
						text-align: left;
			}
			.val_strh6{
						width:151px !important;
						text-align: left;
			}
	}
</style>

<style>
	@-moz-document url-prefix() {
	    	.val_strh1{
						width:238px !important;
						text-align: left;
			}
			.val_strh2{
						width:128px !important;
						text-align: left;
			}
			.val_strh3{
						width:110px !important;
						text-align: left;
			}
			.val_strh4{
						width:115px !important;
						text-align: left;
			}
			.val_strh5{
						width:147px !important;
						text-align: left;
			}
			.val_strh6{
						width:149px !important;
						text-align: left;
			}
	}
</style>

<!--[if IE 8]>
	<style>
			.val_strh1{
						width:241px !important;
						text-align: left;
				}
			.val_strh2{
						width:129px !important;
						text-align: left;
				}
			.val_strh3{
						width:110px !important;
						text-align: left;
			}
			.val_strh4{
						width:119px !important;
						text-align: left;
			}
			.val_strh5{
						width:154px !important;
						text-align: left;
			}
			.val_strh6{
						width:152px !important;
						text-align: left;
			}
		</style>						
<![endif]-->

<!--[if IE 9]>
	<style>
			.val_strh1{
						width:242px !important;
						text-align: left;
				}
			.val_strh2{
						width:129px !important;
						text-align: left;
				}
			.val_strh3{
						width:111px !important;
						text-align: left;
			}
			.val_strh4{
						width:116px !important;
						text-align: left;
			}
			.val_strh5{
						width:155px !important;
						text-align: left;
			}
			.val_strh6{
						width:152px !important;
						text-align: left;
			}
	</style>						
<![endif]-->

<style>
	@media all and (-ms-high-contrast: none), (-ms-high-contrast: active) {
				.val_strh1{
							width:230px !important;
							text-align: left;
					}
				.val_strh2{
							width:121px !important;
							text-align: left;
					}
				.val_strh3{
							width:105px !important;
							text-align: left;
				}
				.val_strh4{
							width:111px !important;
							text-align: left;
				}
				.val_strh5{
							width:143px !important;
							text-align: left;
				}
				.val_strh6{
							width:147px !important;
							text-align: left;
				}
	}
</style>

<div class=report_table style="border-top: #febe10 4px solid">
	<table class=report_table_content>
		<thead>
			<tr>
				<th class="val_strh1" valign=bottom rowspan=2 width="238px">Contract
						Name</th>
				<th class="val_strh2" valign=bottom rowspan=2 width="128px">Contract
						Number</th>
				<th class="val_strh3" valign=bottom rowspan=2 width="110px">Month
						End</th>
				<th class="val_strh4" valign=bottom rowspan=2 width="112px">Request
						Status</th>
				<th class="val_strh5" valign=bottom rowspan=2 width="149px">
					Plan Review Report(s)</th>
				<th class="val_strh6" valign=bottom rowspan=2 width="151px">Stand-alone
						Executive Summary</th>
			</tr>
		</thead>
	</table>
		<table class=report_table_content id="reportTable1">
			<tbody>
				<tr>
					<td class="val_num_count" width="228px">${theReport.contractName}</td>
					<td class="val_num_count" width="128px">${theReport.contractNumber}</td>
					<td class="name " width="109px">${theReport.formatedPeriodEndDate}</td>
					<td class="status" width="115px" align="center"><img
						id="processImgcontractStatus" height="18" width="13"
						style='padding-left: 8px;'
						src='/assets/unmanaged/images/icon_done.gif' /> &nbsp;<c:out
							value="<%=PlanReviewReportPrintHistoryDetailsReportData.STATUS_COMPLETED%>" />
					</td>

					<td width="149px" class="name" align="center">
						<div>
<input type="hidden" name="downloadPlanReviewReportIndicator" id="downloadPlanReviewReportInd"/>
<form:checkbox value="downloadPlanReviewReportIndicator" path="downloadPlanReviewReportInd" id="Rselect_all" cssClass="download" title="Select all Plan Review Reports" />



							&nbsp; <a class="exempt" title="Open report" href="javascript://"
					onClick="doView('${planReviewHistoryDetailsReportForm.selectedPlanReviewActivityId}','<%=PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION.getDocumentTypeCode()%>','${_csrf.token}');return false;"> <img
								src="/assets/unmanaged/images/pdf.png" width="18" height="18"
								border="0"/></a> &nbsp;&nbsp; <a
								class="exempt"
					Style="height: 17px; width: 17px; border: 0px;" title="Download directly to your downloads folder"
					href="javascript://"
					onClick="doDownloadPdf(${planReviewHistoryDetailsReportForm.selectedPlanReviewActivityId},'<%=PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION.getDocumentTypeCode()%>' );return false;">
								<img src="/assets/unmanaged/images/planreview_download.png"
								border="0" height="17" width="17"/>
							</a>
						</div>
					</td>
				
					<td width="149px" class="name" align="center">
						<div>
<input type="hidden" name="downloadExcecutiveSummaryIndicator" id="downloadExcecutiveSummaryInd"/>
<form:checkbox  value="downloadExcecutiveSummaryIndicator" path="downloadExcecutiveSummaryInd" id="Eselect_all" cssClass="download" title="Select all Executive Summary Reports" />



							&nbsp; <a class="exempt" title="Open report" href="javascript://"
					onClick="doView('${planReviewHistoryDetailsReportForm.selectedPlanReviewActivityId}','<%=PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY.getDocumentTypeCode()%>','${_csrf.token}');return false;"> <img
								src="/assets/unmanaged/images/pdf.png" width="18" height="18"
								border="0"/></a> &nbsp;&nbsp; <a
								class="exempt"
					Style="height: 17px; width: 17px; border: 0px;" title="Download directly to your downloads folder" href="javascript://"
							onClick="doDownloadPdf(${planReviewHistoryDetailsReportForm.selectedPlanReviewActivityId},'<%=PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY.getDocumentTypeCode()%>' );return false;">
								<img src="/assets/unmanaged/images/planreview_download.png"
								border="0" height="17" width="17"/>
								</a>
						</div>
					</td>
			</tbody>
		</table>
	</div>
<div>&nbsp;</div>

<c:if test="${sessionScope.bobResults !='contract'}">
	<div Style="float: right">

			<input class="blue-btn_big next"
				onmouseover="this.className +=' btn-hover'" id="blue-btn_big"
				onmouseout="this.className='blue-btn_big next'"
				onclick="showViewDisableReportOverlay();" value="Delete" type=button
				name="Delete" title='Delete report' />
	</div>
</c:if>
<c:if test="${sessionScope.bobResults =='contract'}">
	<div Style="float: right">

			<input class="blue-btn_big next"
				onmouseover="this.className +=' btn-hover'" id="blue-btn_big"
				onmouseout="this.className='blue-btn_big next'"
				onclick="showViewDisableReportContractLevelOverlay();" value="Delete" type=button
				name="Delete" title='Delete report' />
	</div>
</c:if>
<div class="nextButton" id="enabledBottomNextButton">
	<div Style="float: right; padding-right: 25px; ">
		<input  class="blue-btn_big next disabled-grey-btn"
			onmouseover="this.className +=' btn-hover'" id="download_button"
			onmouseout="this.className ='blue-btn_big next'"
			onclick="doDownloadSelected('downloadSelectedPdf');"
			value="Download Selected" type=button name=download  style="background-size: 100% 100% !important; width: 200px;"/>
	</div>
</div>

<DIV id="viewDisableReasonPanel" style="visibility: hidden; BACKGROUND-COLOR: white"></DIV>
<div id="download_panel"
	style="visibility: hidden; BACKGROUND-COLOR: rgb(255, 255, 255); font: 16px verdana, arial, helvetica, sans-serif;border-top:0px;border-bottom:0px;width:0px;height:0px;">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
		</ul>
	</div>
	<div style="text-align: right; padding-right: 5px; padding-top: 5px">
		<img class="cancel" height="15"
			width="15" src="/assets/unmanaged/images/s.gif" />
	</div>
	<table>
		<tr>
			<td style="float: left; margin-left: 26px; margin-top: 10px;">
				<img
				style="padding-top: 5px; padding-left: 5px;"
				src="/assets/unmanaged/images/ajax-wait-indicator.gif">
			</td>
			<td style="float: right;font-size:17px">
				<p><b style="font-size:16px;font-weight:bold">PDF
					retrieval in progress.</b><b style="font-size:14px;font-weight:bold"> Note:</b> This process may <br> take several minutes.
			Please do not close your browser.</p><b style="font-size:16px;font-weight:bold">
			</b></td>
		</tr>
	</table>
</div>

