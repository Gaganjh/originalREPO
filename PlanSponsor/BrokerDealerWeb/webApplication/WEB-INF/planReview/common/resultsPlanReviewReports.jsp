<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType"%>
<%--  Tag Libraries  --%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_CONFIRM_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="planRviewRequestConfirmMsg"/>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_NO_RECORDS_TO_DISPLAY_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="noRecordsToDisplayMessage"/>


<script type="text/javascript">
	function planRviewRequestConfirmMsg() {
		var planRviewRequestConfirmMsg = '<content:getAttribute beanName="planRviewRequestConfirmMsg" attribute="text" filter="true"/>';
		if (confirm(planRviewRequestConfirmMsg)) {
			return true;
		} else {
			return false;
		}
	}
</script>
<%
	String SCROLL_RECORD_LIMIT = BDConstants.SCROLL_RECORD_LIMIT;
	pageContext.setAttribute("SCROLL_RECORD_LIMIT",SCROLL_RECORD_LIMIT,PageContext.PAGE_SCOPE);
	
%>

<%
	// we are using  this variable for differnciating the BOb leve and contact level Result page.
	String bobLevelResultPage = (String) request.getAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE);
%>		

<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<form:hidden path="pageRegularlyNavigated"/>
<form:hidden path="type"/>
<form:hidden path="requestType"/>
<form:hidden path="downloadContractId"/>
<form:hidden path="bobResults" class="bob_results" value="${sessionScope.bobResults}" />
<form:hidden path="incompleteErrorMessage" id="errorInd" />
	<!--<bdweb:fieldHilight	name="contractsWhichAlereadyReachedLimit" singleDisplay="true"
					   style="float:none;align:right" className="errorIcon" /> -->
					   
<style>
	@media all and (-webkit-min-device-pixel-ratio:0) {
		ul.proposal_nav_menu li span {
			display: inline;
		    padding: 9px 10px 10px 10px;
		    color: #FFF;
		    background-color: #455660;
		}
	}
</style>

<style>
	@-moz-document url-prefix() {
	    ul.proposal_nav_menu li span {
			display: inline;
		    padding: 9px 10px 10px 10px;
		    color: #FFF;
		    background-color: #455660;
		}
	}
</style>

<!--[if IE 8]>
<style>
	ul.proposal_nav_menu li span {
		display: inline;
		padding: 9px 12px 10px 10px;
		color: #FFF;
		background-color: #455660;
		padding-right: 0px;
	}
	ul.proposal_nav_menu li a span.step_caption{
		padding-right: 10px !important;
	}
	.selected_link {
		background-color: transparent !important;
		background-image: none !important;
	}
</style>
<![endif]-->

<!--[if IE 9]>
<style>
	ul.proposal_nav_menu li span {
		display: inline;
		padding: 9px 12px 10px 10px;
		color: #FFF;
		background-color: #455660;
		padding-right: 0px;
	}
	ul.proposal_nav_menu li a span.step_caption{
		padding-right: 10px !important;
	}
	.selected_link {
		background-color: transparent !important;
		background-image: none !important;
	}
</style>
<![endif]-->
					   
<div>	
<ul class="proposal_nav_menu">
	<li><a class="exempt" id=step1>Step 1</a></li>
	<li><a class="exempt" id=step1> Step 2 </a></li>
	<li><a class="selected_link exempt"><span class=step_number>Results-</span><span
		class=step_caption><c:if test="${planReviewResultForm.requestFromHistory=='true'}">
History results for ${planReviewResultForm.reportMonthEndDate}

	</c:if>
	<c:if test="${planReviewResultForm.requestFromHistory=='false'}">
	View or download reports
	</c:if>
	</span></a></li>
</ul>
</div>

<div id=page_section_subheader>
<div class="table_controls" style="border-top: #febe10 4px solid">
<div class=table_action_buttons></div>
<div class=table_display_info_abs>
<c:if test="${sessionScope.bobResults!='contract'}">
<strong> <span
id='processedReqs'>${planReviewResultForm.requestsProcessed}</span> of&nbsp;${planReviewResultForm.contractResultVOListSize}&nbsp;requests processed </strong>
</c:if>
</div>
</div>
</div>

<style>
	@media all and (-webkit-min-device-pixel-ratio:0) {
			.val_str1{
						width:230px !important;
						text-align: left;
			}
			.val_str2{
						width:129px !important;
						text-align: left;
			}
			.val_str3{
						width:107px !important;
						text-align: left;
			}
			.val_str4{
						width:121px !important;
						text-align: left;
			}
			.val_str5{
						width:154px !important;
						text-align: left;
			}
			.val_str6{
						width:155px !important;
						text-align: left;
			}
	}
</style>

<style>
	@-moz-document url-prefix() {
	    	.val_str1{
						width:230px !important;
						text-align: left;
			}
			.val_str2{
						width:129px !important;
						text-align: left;
			}
			.val_str3{
						width:107px !important;
						text-align: left;
			}
			.val_str4{
						width:121px !important;
						text-align: left;
			}
			.val_str5{
						width:154px !important;
						text-align: left;
			}
			.val_str6{
						width:155px !important;
						text-align: left;
			}
	}
</style>

<!--[if IE 8]>
	<style>
			.val_str1{
						width:229px !important;
						text-align: left;
				}
			.val_str2{
						width:129px !important;
						text-align: left;
				}
			.val_str3{
						width:108px !important;
						text-align: left;
			}
			.val_str4{
						width:121px !important;
						text-align: left;
			}
			.val_str5{
						width:157px !important;
						text-align: left;
			}
			.val_str6{
						width:155px !important;
						text-align: left;
			}
		</style>						
<![endif]-->

<!--[if IE 9]>
	<style>
			.val_str1{
						width:229px !important;
						text-align: left;
				}
			.val_str2{
						width:134px !important;
						text-align: left;
				}
			.val_str3{
						width:107px !important;
						text-align: left;
			}
			.val_str4{
						width:121px !important;
						text-align: left;
			}
			.val_str5{
						width:157px !important;
						text-align: left;
			}
			.val_str6{
						width:156px !important;
						text-align: left;
			}
		</style>						
<![endif]-->

<style>
	@media all and (-ms-high-contrast: none), (-ms-high-contrast: active) {
				.val_str1{
							width:230px !important;
							text-align: left;
					}
				.val_str2{
							width:129px !important;
							text-align: left;
					}
				.val_str3{
							width:109px !important;
							text-align: left;
				}
				.val_str4{
							width:121px !important;
							text-align: left;
				}
				.val_str5{
							width:158px !important;
							text-align: left;
				}
				.val_str6{
							width:156px !important;
							text-align: left;
				}
	}
</style>

<div class=report_table>
<table class=report_table_content>
	<thead>
		<tr>
		<c:if test="${planReviewResultForm.contractResultVOListSize gt SCROLL_RECORD_LIMIT}">
				<th class="val_str1" valign=bottom rowspan=2 width="225px">
				Contract Name
			</th>
			<th class="val_str2" valign=bottom rowspan=2 width="128px">
			Contract Number
			</th>
			<th class="val_str3" valign=bottom rowspan=2 width="106px">
			Month End
			</th>
			<th class="val_str4" valign=bottom rowspan=2 width="118px">
			Request Status
			</th>
			<th class="val_str5" valign=bottom rowspan=2 width="152px">
			<!--<c:if test="${planReviewResultForm.contractResultVOListSize==BDConstants.SINGLE_RECORD}">
				<%
					if (bobLevelResultPage.equalsIgnoreCase("bob")) {
				%>
				<form:checkbox disabled="true" styleId="Rselect_all"
					name="planReviewResultForm" path="allPlanReviewReportPdfsSelected" />
				<%
					}
				%>		                                
			</c:if>

			<c:if test="${planReviewResultForm.contractResultVOListSize!=BDConstants.SINGLE_RECORD}">
				<form:checkbox title="Select all Plan Review Reports"
					styleId="Rselect_all" name="planReviewResultForm"
					path="allPlanReviewReportPdfsSelected" cssClass="selectAllCheckbox"/>
			</c:if>-->
			Plan Review <br>Report(s)
			</th>
			<th class="val_str6" valign=bottom rowspan=2 width="172px">
			<!--<c:if test="${planReviewResultForm.contractResultVOListSize!=BDConstants.SINGLE_RECORD}">
				<form:checkbox title="Select all reports"
					styleId="Eselect_all" cssClass="download selectAllCheckbox"
					name="planReviewResultForm"
					path='allExeSummaryPdfsSelected' />
			</c:if>
			<c:if test="${planReviewResultForm.contractResultVOListSize==BDConstants.SINGLE_RECORD}">
				<%
					if (bobLevelResultPage.equalsIgnoreCase("bob")) {
				%>
				<form:checkbox disabled="true" styleId="Rselect_all"
					cssClass="download" name="planReviewResultForm"
					path="allExeSummaryPdfsSelected" />
				<%
					}
				%>
			</c:if>-->
			Stand-alone <br>Executive Summary
			</th>
			</c:if>
			<c:if test="${planReviewResultForm.contractResultVOListSize le 35}">
				<th class="val_str1" valign=bottom rowspan=2 width="230px">
				Contract Name
			</th>
			<th class="val_str2" valign=bottom rowspan=2 width="129px">
			Contract Number
			</th>
			<th class="val_str3" valign=bottom rowspan=2 width="107px">
			Month End
			</th>
			<th class="val_str4" valign=bottom rowspan=2 width="119px">
			Request Status
			</th>
			<th class="val_str5" valign=bottom rowspan=2 width="154px">
			<!--<c:if test="${planReviewResultForm.contractResultVOListSize==BDConstants.SINGLE_RECORD}">
				<%
					if (bobLevelResultPage.equalsIgnoreCase("bob")) {
				%>
				<form:checkbox disabled="true" styleId="Rselect_all"
					name="planReviewResultForm" path="allPlanReviewReportPdfsSelected" />
				<%
					}
				%>		                                
			</c:if>

			<c:if test="${planReviewResultForm.contractResultVOListSize!=BDConstants.SINGLE_RECORD}">
				<form:checkbox title="Select all reports" cssClass="selectAllCheckbox"
					styleId="Rselect_all" name="planReviewResultForm"
					path="allPlanReviewReportPdfsSelected" />
			</c:if>-->
			Plan Review <BR>&nbsp;&nbsp;Report(s)
			</th>
			<th class="val_str6" valign=bottom rowspan=2 width="153px">
			<!--<c:if test="${planReviewResultForm.contractResultVOListSize!=BDConstants.SINGLE_RECORD}">
				<form:checkbox title="Select all reports"
					styleId="Eselect_all" cssClass="download selectAllCheckbox"
					name="planReviewResultForm"
					path='allExeSummaryPdfsSelected' />
			</c:if>
			<c:if test="${planReviewResultForm.contractResultVOListSize==BDConstants.SINGLE_RECORD}">
				<%
					if (bobLevelResultPage.equalsIgnoreCase("bob")) {
				%>
				<form:checkbox disabled="true" styleId="Eselect_all"
					styleClass="download" name="planReviewResultForm"
					path="allExeSummaryPdfsSelected" />
				<%
					}
				%>
			</c:if>-->
			Stand-alone <br>Executive Summary
			</th>
			</c:if>
<form:hidden path="allPlanReviewReportPdfsSelected" style="downloadAllContract"/>

<form:hidden path="allExeSummaryPdfsSelected" style="downloadAllSumContract"/>
		</tr>
		<tr></tr>
	</thead>
</table>
<div
	style="overflow-y: auto; width: 100%; max-height: 895px; border-bottom: #cac8c4 1px solid;" id="checkboxgroup">
<table class=report_table_content id="reportTable1">
	<tbody>
<c:forEach items="${planReviewResultForm.contractResultVOList}" var="planReviewReportVO" varStatus="indexValue" >

<c:set var="rowIndex"  value="${indexValue.index}"/>

				<c:choose>
					<c:when test="${rowIndex % 2 == 0}">
						<tr>
					</c:when>
					<c:otherwise>
						<tr class="spec">
					</c:otherwise>
				</c:choose>

 <td class="downloadPDF" width="223px" style="border-left-width: 2px;"><c:if test="${not empty planReviewResultForm.contractResultVOList[rowIndex].contractName}">


${planReviewResultForm.contractResultVOList[rowIndex].contractName}

</c:if> <c:if test="${empty planReviewResultForm.contractResultVOList[rowIndex].contractName}">

											None
</c:if></td>

				<td class="val_num_count" id="contractnum${rowIndex + 1}"
width="132px"><c:if test="${not empty planReviewResultForm.contractResultVOList[rowIndex].contractNumber}">


${planReviewResultForm.contractResultVOList[rowIndex].contractNumber}

</c:if> <c:if test="${empty planReviewResultForm.contractResultVOList[rowIndex].contractNumber}">

											None
</c:if></td>

<td class="name " width="107x"> ${planReviewResultForm.contractResultVOList[rowIndex].selectedReportMonthEndDate}


				</td>
				
				<c:if test="${planReviewResultForm.requestFromHistory=='true'}">
					<td class="status" width="121px" id="contractStatus${rowIndex + 1}">
					<img
					id="processImgcontractStatus${rowIndex + 1}" height="18" width="13"
					style='padding-left: 8px;'
					src='${planReviewReportVO.statusImage}' />
<span title="${planReviewReportVO.statusImageTitle}" id="contractStatus${rowIndex + 1}">  
${planReviewResultForm.contractResultVOList[rowIndex].status}</span>


					</td>
				</c:if>
				
				<c:if test="${planReviewResultForm.requestFromHistory=='false'}">
				<td class="status" width="121px"><img
					id="processImgcontractStatus${rowIndex + 1}" height="18" width="13"
					style='padding-left: 8px;'
					src='/assets/unmanaged/images/ajax-wait-indicator.gif' /> 
<form:hidden path="contractResultVOList[${rowIndex}].status" style="planReviewcontractStatus${rowIndex + 1}"/>

<span id="contractStatus${rowIndex + 1}">${planReviewResultForm.contractResultVOList[rowIndex].status}


				</span></td>
				</c:if>

				
				
				
				
				
			<td width="159px" class="name">
				<div id="enablePDFcontractStatus${rowIndex + 1}"
style="display: none; height: 17px;"> 
<form:hidden id="planReviewReportPdfsSelected" path="planReviewReportPdfsSelected" /> 
<form:hidden  id="planReviewReportPdfsSelected${rowIndex}" path="contractResultVOList[${rowIndex}].planReviewReportPdfsSelected"  />  
<form:checkbox path="contractResultVOList[${rowIndex}].planReviewReportPdfsSelected" id="${rowIndex}" cssClass="planReviewReportPdfsSelected download" onclick="doDownloadPdfLimit();"/> 


				&nbsp;&nbsp;&nbsp; <a class="exempt" href="javascript://" title="Open report"
					onClick="doView(${planReviewReportVO.contractNumber},'<%=PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION.getDocumentTypeCode()%>','${_csrf.token}');return false;">
				<img src="/assets/unmanaged/images/pdf.png" width="18" height="18"
					border="0"></a> &nbsp;&nbsp;&nbsp; <a class="exempt"
					Style="height: 17px; width: 17px; border: 0px;" title="Download directly to your downloads folder"
					href="javascript://"
					onClick="doDownloadPdf(${planReviewReportVO.contractNumber},'<%=PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION.getDocumentTypeCode()%>' );return false;">
				<img src="/assets/unmanaged/images/planreview_download.png"
					border="0" height="17" width="17"> </a></div>
				</td>

				<td width="158px" class="name">
				<div id="enableSumPDFcontractStatus${rowIndex + 1}"
style="display: none; height: 17px;">
<form:hidden  path="exeSummaryPdfsSelected" id="exeSummaryPdfsSelected"/>
<form:hidden  path="contractResultVOList[${rowIndex}].exeSummaryPdfsSelected" id="exeSummaryPdfsSelected${rowIndex}"  />
 <form:checkbox path="contractResultVOList[${rowIndex}].exeSummaryPdfsSelected"  id="${rowIndex}" cssClass="exeSummaryPdfsSelected download" onclick="doDownloadPdfLimit();" /> 

				&nbsp;&nbsp;&nbsp; <a class="exempt" title="Open report" href="javascript://"
					onClick="doView('${planReviewReportVO.contractNumber}','<%=PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY.getDocumentTypeCode()%>','${_csrf.token}');return false;">
				<img src="/assets/unmanaged/images/pdf.png" width="18" height="18"
					border="0"></a> &nbsp;&nbsp;&nbsp; <a class="exempt"
					Style="height: 17px; width: 17px; border: 0px;" title="Download directly to your downloads folder"
					href="javascript://"
					onClick="doDownloadPdf(${planReviewReportVO.contractNumber},'<%=PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY.getDocumentTypeCode()%>' );return false;">
				<img src="/assets/unmanaged/images/planreview_download.png"
					border="0" height="17" width="17"> </a></div>
				</td>	
				
				
				
				
				</tr>
</c:forEach>
			
<c:if test="${empty planReviewResultForm.contractResultVOList}">

				
				 <div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
					      
					     	<dd>1.&nbsp;<content:getAttribute id="noRecordsToDisplayMessage" attribute="text"/></dd>
					     
					    </dl>
				  	</div>
				
</c:if>
	</tbody>
</table>
</div>
</div>
<div>&nbsp;</div>
<div>
<div style="position: relative; float:left"><c:if test="${planReviewResultForm.requestFromHistory eq true}">
		<input 
	class="blue-btn_big next" onmouseover="this.className +=' btn-hover'" style="background-size: 100% 100%; width: 200px;"
	id="doHistory" onmouseout="this.className='blue-btn_big next'" onclick="doBackToHistory('backToMainHistory');"
	value="Back" type=button name=download>
	</c:if></div>

<div class="nextButton" id="enabledBottomNextButton">

<div Style="position: relative; float: right"><input disabled="disabled" style="background-size: 100% 100%; width: 200px;"
	class="blue-btn_big next disabled-grey-btn" onmouseover="this.className +=' btn-hover'"
	id="blue-btn_big" onmouseout="this.className='blue-btn_big next'"
	onclick="doDownloadSelected('downloadSelectedPdf');"
	value="Download Selected" type=button name=download></div>
</div>
</div>
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
