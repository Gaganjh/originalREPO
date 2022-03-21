<%@ page import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder" %>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.platform.web.util.BaseSessionHelper"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bdweb"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>

<form:hidden path="pageRegularlyNavigated"/>

<!-- <style>
	html[data-useragent*='MSIE 10.0'] td.td1 {
		width: 26px !important;
	}
	html[data-useragent*='MSIE 10.0'] td.td2 {
		width: 149px !important;
	}
	html[data-useragent*='MSIE 10.0'] td.td3 {
		width: 58px !important;
	}
	html[data-useragent*='MSIE 10.0'] td.td4 {
		width: 428px !important;
	}
	html[data-useragent*='MSIE 10.0'] td.td5 {
		width: 98px !important;
	}
	html[data-useragent*='MSIE 10.0'] td.td6 {
		width: 79px !important;
	}
</style> -->

<style>
	td.td1 {
		width: 26px !important;
	}
	td.td2 {
		width: 156px !important;
	}
	td.td3 {
		width: 66px !important;
	}
	td.td4 {
		width: 417px !important;
	}
	td.td5 {
		width: 108px !important;
	}
	td.td6 {
		width: 92px !important;
	}
</style>

<style>
@media all and (-webkit-min-device-pixel-ratio:0) {
	td.td1 {
		width: 26px !important;
	}
	td.td2 {
		width: 153px !important;
	}
	td.td3 {
		width: 66px !important;
	}
	td.td4 {
		width: 411px !important;
	}
	td.td5 {
		width: 109px !important;
	}
	td.td6 {
		width: 91px !important;
	}
}
</style>

<!--[if IE 7]>
<style>
	td.td1 {
		width: 26px !important;
	}
	td.td2 {
		width: 154px !important;
	}
	td.td3 {
		width: 65px !important;
	}
	td.td4 {
		width: 413px !important;
	}
	td.td5 {
		width: 108px !important;
	}
	td.td6 {
		width: 92px !important;
	}
</style>
<![endif]-->

<!--[if IE 8]>
<style>
	td.td1 {
		width: 26px !important;
	}
	td.td2 {
		width: 154px !important;
	}
	td.td3 {
		width: 66px !important;
	}
	td.td4 {
		width: 415px !important;
	}
	td.td5 {
		width: 108px !important;
	}
	td.td6 {
		width: 92px !important;
	}
</style>
<![endif]-->

<!--[if IE 9]>
<style>
	td.td1 {
		width: 26px !important;
	}
	td.td2 {
		width: 154px !important;
	}
	td.td3 {
		width: 66px !important;
	}
	td.td4 {
		width: 415px !important;
	}
	td.td5 {
		width: 108px !important;
	}
	td.td6 {
		width: 92px !important;
	}
</style>
<![endif]-->


<script>
	$(document).ready(function() {
		$(".select:disabled").prop("title", "");
		$(".select").each(function() {
			var title = $(this).prop("title");
			$(this).prop("titleBack", title);
			if ($(this).val() != " ") {
				$(this).prop("title", "");
			}
		});

		$(".select").on('change',function() {
			$(this).prop("title", "");
			if ($(this).val() == " ") {
				$(this).prop("title", $(this).prop("titleBack"));
			} else {
				$(this).prop("title", "");
			}
		});
	});
</script>

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
		padding: 9px 10px 10px 10px;
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
		padding: 9px 10px 10px 10px;
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

<ul class="proposal_nav_menu">
		<li><a class="selected_link exempt"><SPAN class="step_number">Step1:
			<SPAN class="step_caption"><c:if test="${sessionScope.bobResults!='contract'}"> Select your plan(s)</c:if>
			<c:if test="${sessionScope.bobResults=='contract'}">Select your plan</c:if></SPAN></SPAN>
		</a></li>
		<li><a class="exempt" id="step2"> Step 2 </a></li>
		<li><a class="exempt" id="step3">Results</a></li>
	</ul>

	<div id=page_section_subheader>
	<div class="table_controls" style="border-top: #febe10 4px solid">
	<div class=table_action_buttons></div>
	<div class=table_display_info_abs>
	<c:if test="${sessionScope.bobResults!='contract'}">
	<strong>Number of contracts :&nbsp;${planReviewReportForm.displayContractReviewReportsSize}</strong>
		</c:if>
		</div>

	</div>
	</div>

	<div class=report_table>
	
	<table class=report_table_content>
		<thead>
			<tr>
			<c:if test="${sessionScope.bobResults!='contract'}">
				<form:hidden id ="allContractSelected" path='allContractSelected'/>
				<th class=val_str vAlign=bottom rowSpan=2 noWrap width="21px">
				<form:checkbox id="select_all"
					path="allContractSelected"  />
				</th>	
				
			</c:if>
			<% 
				String SINGLE_RECORD=BDConstants.SINGLE_RECORD;
				String SCROLL_RECORD_LIMIT=BDConstants.SCROLL_RECORD_LIMIT;
				pageContext.setAttribute("SINGLE_RECORD",SINGLE_RECORD,PageContext.PAGE_SCOPE);
				pageContext.setAttribute("SCROLL_RECORD_LIMIT",SCROLL_RECORD_LIMIT,PageContext.PAGE_SCOPE);
  
				String NO_RECORD=BDConstants.NO_RECORD;
				pageContext.setAttribute("NO_RECORD",NO_RECORD,PageContext.PAGE_SCOPE);
			%> 
			<form:hidden cssClass ="displayContractReviewReportsSize" path="displayContractReviewReportsSize"/>
			<form:hidden cssClass ="bob_results" path="bobResults" value="${sessionScope.bobResults}"/>
			<c:if test="${planReviewReportForm.displayContractReviewReportsSize == SINGLE_RECORD}">
			 
				<th class=val_str vAlign=bottom rowSpan=2 width="160px" style="width: 160px;">
					Contract Name
				</th>	
			
				<th class=val_str vAlign=bottom rowSpan=2 width="66px" style="width: 62px;">		
					Contract <br />Number
				</th>			
				<th class=val_str vAlign=bottom rowSpan=2  width="487px" style="width: 442px;">		   
					Industry Segment 
						for Benchmarking<span>*</span>
				</th>		   			 			
				<th class=val_str vAlign=bottom rowSpan=2 width="108px" style="width: 108px;">		
					Month End <br>(mm/dd/yyyy)
				</th>
				<th class=val_str vAlign=bottom rowSpan=2 width="93px" style="width: 93px;">	
					Include <br/>Performance and <br/>Expense Ratios
				</th>
			</c:if>	
						
			<c:if test="${planReviewReportForm.displayContractReviewReportsSize != SINGLE_RECORD}">
				<c:if test="${planReviewReportForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">	
		    
					<th class=val_str vAlign=bottom rowSpan=2 width="172px">
						<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="contractName" direction="desc" styleId="sortField">
										Contract Name
						</report:bdSortLinkViaSubmit>
					</th>
					<th class=val_str vAlign=bottom rowSpan=2 width="52px">
						<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="contractNumber" direction="asc" styleId="sortField">
										Contract <br />Number
						</report:bdSortLinkViaSubmit>
					</th>
					<th class=val_str vAlign=bottom rowSpan=2 width="566px">
						<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedIndustrySegment"
								direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField" >Industry Segment 
								for Benchmarking<span >*</span></report:bdSortLinkViaSubmit>
					</th>
					<th class=val_str vAlign=bottom rowSpan=2 width="108px">
						<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedReportMonthEndDate"
								direction="<%=ReportSort.DESC_DIRECTION%>" styleId="sortField">Month End <br>(mm/dd/yyyy)</report:bdSortLinkViaSubmit>
					</th>
						<th class=val_str vAlign=bottom rowSpan=2 width="93px">
						<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedperformanceAndExpenseRatio"
							direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">
							Include <br/>Performance and <br/>Expense Ratios
						</report:bdSortLinkViaSubmit>
					</th>
				</c:if>
			
				<c:if test="${planReviewReportForm.displayContractReviewReportsSize le SCROLL_RECORD_LIMIT}">
		   
					<th class=val_str vAlign=bottom rowSpan=2 width="154px !important">
				
					<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="contractName" direction="desc" styleId="sortField">
												Contract Name
					</report:bdSortLinkViaSubmit>
					</th>
					<th class=val_str vAlign=bottom rowSpan=2 width="66px" style="width: 64px !important;" >	
					<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="contractNumber" direction="asc" styleId="sortField">
												Contract <br />Number
					</report:bdSortLinkViaSubmit>
					</th>
					<th class=val_str vAlign=bottom rowSpan=2 width="487px" style="width: 458px !important;">
					<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedIndustrySegment"
							direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField" >Industry Segment 
							for Benchmarking<span >*</span></report:bdSortLinkViaSubmit>
							</th>
							<th class=val_str vAlign=bottom rowSpan=2 width="108px" style="width: 115px !important;">
					<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedReportMonthEndDate"
							direction="<%=ReportSort.DESC_DIRECTION%>" styleId="sortField">Month End<br>(mm/dd/yyyy)</report:bdSortLinkViaSubmit>
							</th>
							<th class=val_str vAlign=bottom rowSpan=2 width="70px" style="width: 76px;">
							<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
							field="selectedperformanceAndExpenseRatio"
							direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">
							Include <br/>Performance and <br/>Expense Ratios
						</report:bdSortLinkViaSubmit>
						</th>
				</c:if>													
            </c:if>
			</tr>
			<tr></tr>
		</thead>
	</table>

	<c:if test="${planReviewReportForm.displayContractReviewReportsSize !=SINGLE_RECORD}">
	
		<c:if test="${planReviewReportForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">	
		    <style>
			td.td1 {
				width: 25px !important;
			}
			td.td2 {
				width: 144px !important;
			}
			td.td3 {
				width: 57px !important;
			}
			td.td4 {
				width: 420px !important;
			}
			td.td5 {
				width: 92px !important;
			}
			td.td6 {
				width: 76px !important;
			}
			.table-container {
				overflow-y: auto; width: 919px; max-height: 1270px; border-bottom: #cac8c4 1px solid;
				overflow-x: hidden;
			}
			</style>
		    
		    <!--[if gte IE 7]>
		    <style>
		    td.td1 {
				width: 27px !important;
			}
			td.td2 {
				width: 144px !important;
			}
			td.td3 {
				width: 54px !important;
			}
			td.td4 {
				width: 420px !important;
			}
			td.td5 {
				width: 98px !important;
			}
			td.td6 {
				width: 84px !important;
			}
		    </style>
		    <![endif] -->
		    
		</c:if>
	</c:if>
		    

	<c:if test="${planReviewReportForm.displayContractReviewReportsSize !=SINGLE_RECORD}">

		<c:if test="${planReviewReportForm.displayContractReviewReportsSize le BDConstants.SCROLL_RECORD_LIMIT}">
			
			
		    <style>
			td.td1 {
				width: 26px !important;
			}
			td.td2 {
				width: 143px !important;
			}
			td.td3 {
				width: 66px !important;
			}
			td.td4 {
				width: 402px !important;
			}
			td.td5 {
				width: 109px !important;
			}
			td.td6 {
				width: 75px !important;
			}
			.table-container {
				overflow-y: auto; width: 919px; max-height: 1270px; border-bottom: #cac8c4 1px solid;
			}
			</style>
		
		   <!--[if IE 7]>
		   <style>
		    td.td1 {
				width: 25px !important;
			}
			td.td2 {
				width: 143px !important;
			}
			td.td3 {
				width: 68px !important;
			}
			td.td4 {
				width: 405px !important;
			}
			td.td5 {
				width: 115px !important;
			}
			td.td6 {
				width: 76px !important;
			}
		    </style>	
		    <![endif]-->	
		    
		    <!--[if IE 8]>
		   <style>
		    td.td1 {
				width: 25px !important;
			}
			td.td2 {
				width: 146px !important;
			}
			td.td3 {
				width: 67px !important;
			}
			td.td4 {
				width: 409px !important;
			}
			td.td5 {
				width: 115px !important;
			}
			td.td6 {
				width: 78px !important;
			}
		    </style>	
		    <![endif]-->
		    
		    <!--[if IE 9.0]>
		   <style>
		    td.td1 {
				width: 25px !important;
			}
			td.td2 {
				width: 146px !important;
			}
			td.td3 {
				width: 66px !important;
			}
			td.td4 {
				width: 409px !important;
			}
			td.td5 {
				width: 115px !important;
			}
			td.td6 {
				width: 77px !important;
			}
		    </style>	
		    <![endif]-->
		    
		    <style>
		    @media all and (-webkit-min-device-pixel-ratio:0) { 	
		    td.td1 {
				width: 25px !important;
			}
			td.td2 {
				width: 137px !important;
			}
			td.td3 {
	 		    width: 64px !important;
			}
			td.td4 {
	  			width: 387px !important;
			}
			td.td5 {
	 		 	width: 110px !important;
			}
			td.td6 {
				width: 74px !important;
			}
		}
		</style>
		<script type="text/javascript">
			$(document).ready(function (){
				if (navigator.userAgent.indexOf('MSIE') != -1)
					 var detectIEregexp = /MSIE (\d+\.\d+);/ 
				else 
					 var detectIEregexp = /Trident.*rv[ :]*(\d+\.\d+)/ 
				
				if (detectIEregexp.test(navigator.userAgent)){ 
					var ieversion=new Number(RegExp.$1) 
				if (ieversion>=10){
				
					var tds=document.getElementById('reportTable1').getElementsByTagName('td');
					
					for(var i=0;i<tds.length;i++){
						
						if(tds[i].className==='td1'){
							
						    $('.td1').prop('style', 'width: 25px !important');
	
						}
						if(tds[i].className==='name td2'){
							
							$("[class='name td2']").prop('style', 'width: 141px !important');
						}
						if(tds[i].className==='val_num_count td3'){
							
							$("[class='val_num_count td3']").prop('style', 'width: 63px !important');
						}
						if(tds[i].className==='name td4'){
							
							$("[class='name td4']").prop('style', 'width: 394px !important');
						}
						if(tds[i].className==='name td5'){
							
							$("[class='name td5']").prop('style', 'width: 110px !important');
						}
						if(tds[i].className==='name td6'){
							
							$("[class='name td6']").prop('style', 'width: 75px !important');
						}
					}
				}	
				}
			});
		</script>
		
		    
		    
		</c:if>
	</c:if>
		    
		    
	<div class="table-container"  >
	<table class=report_table_content id="reportTable1"  style="width:917px">
		<tbody>
				<c:forEach items="${planReviewReportForm.displayContractReviewReports}" var="displayContractReviewReportVO"
					varStatus="theIndex">
					<c:set var="rowIndex"  value="${theIndex.index}"/>
				<c:choose>
					<c:when test="${rowIndex % 2 == 0}">
						<tr class="disabled forIterate">
					</c:when>
					<c:otherwise>
						<tr class="spec disabled forIterate">
					</c:otherwise>
				</c:choose>
				
				<form:hidden id ="selectContracts${rowIndex}" path="displayContractReviewReports[${rowIndex}].contractSelected"/>
				<c:if test="${sessionScope.bobResults!='contract'}">
				<td class=td1 width="34px" style="width: 34px;border-left-width:2px;height:31px;" >
					<form:checkbox cssClass="selectContracts" id="${rowIndex}" cssStyle="margin-left: 7px;"
					path="displayContractReviewReports[${rowIndex}].contractSelected"/> 
				</td>
				</c:if>
				<td class="name td2" width="142px" style="width: 318px;height:31px;">
					<c:if test="${not empty displayContractReviewReportVO.contractName}">
						<c:out value="${displayContractReviewReportVO.contractName}"/>
					</c:if>
					<c:if test="${empty displayContractReviewReportVO.contractName}">
						None
					</c:if>
				</td>
				<form:hidden cssClass ="ContractStatusCode"  path="displayContractReviewReports[${rowIndex}].contractStatusCode"/>
				<form:hidden cssClass ="isContractDisabled"  path="displayContractReviewReports[${rowIndex}].planReviewReportDisabled"/>
				<form:hidden cssClass ="contractDisabledMsg" path="displayContractReviewReports[${rowIndex}].planReviewReportDisabledText"/>
				<td class="val_num_count td3" width="72px" style="width: 90px;height:31px;">
					<c:if test="${not empty planReviewReportForm.displayContractReviewReports[rowIndex].contractNumber}">
						<c:out value="${displayContractReviewReportVO.contractNumber}"/>
						<bdweb:fieldHilight	name="planReviewReportForm.contractNumber${rowIndex}" singleDisplay="true"
					   style="float:none;align:right" className="warningIcon" />
					</c:if> 
					<c:if test="${empty planReviewReportForm.displayContractReviewReports[rowIndex].contractNumber}">

											None
					</c:if>
				</td>	
				
					<td class="name td4" nowrap="nowrap" width="540px" style="height:31px;" >
					<c:if test="${not empty displayContractReviewReportVO.industrySegementOptions}">
						<form:select cssClass="select"
										title="Industry segment used for benchmarking.  If left blank, the report will include national averages."
										path ="displayContractReviewReports[${rowIndex}].selectedIndustrySegment" 
										disabled="${displayContractReviewReportVO.enable}">

										<form:options items="${displayContractReviewReportVO.industrySegementOptions}" itemLabel="value"
											itemValue="label"/>
						</form:select>

									<bdweb:fieldHilight name="selectedIndustrySegment${rowIndex}"
										singleDisplay="true" style="float:none"
										className="warningIcon" />
					</c:if>
					<c:if test="${empty displayContractReviewReportVO.industrySegementOptions}">
					<span class ="ISeg">
					Not Applicable
					</span>
					</c:if>
				</td>
				
				<td class="name td5" width="90px" style="width: 106px;height:31px;">
					<c:choose>
					<c:when test="${!empty displayContractReviewReportVO.reportMonthEndDates}">
					
					<form:select id = "number" cssClass = "NA selectedReportMonthEndDate" path="displayContractReviewReports[${rowIndex}].selectedReportMonthEndDate">
					
					<c:forEach items="${displayContractReviewReportVO.reportMonthEndDates}" var="periodEndingReportDateVO" >

					<c:if test="${periodEndingReportDateVO.statusIndicator=='Y'}">
						<form:option   value="${ periodEndingReportDateVO.periodEndingReportDate}">${ periodEndingReportDateVO.periodEndingReportDate}</form:option>
					</c:if>
					<c:if test="${periodEndingReportDateVO.statusIndicator=='N'}">
						<form:option  style="color: grey;" value="${ periodEndingReportDateVO.periodEndingReportDate}">${ periodEndingReportDateVO.periodEndingReportDate}</form:option>
					</c:if>
					<c:if test="${periodEndingReportDateVO.statusIndicator !='N' && periodEndingReportDateVO.statusIndicator !='Y' }">
						<form:option  style="color: red;" value="${ periodEndingReportDateVO.periodEndingReportDate}">${ periodEndingReportDateVO.periodEndingReportDate}</form:option>
					</c:if>
					</c:forEach>
					</form:select>

					<bdweb:fieldHilight	name="selectedReportMonthEndDate${rowIndex}" singleDisplay="true"
					   style="float:none;align:right" className="errorIcon" />
					</c:when>
					<c:otherwise>
					<span class ="NA" title="Plan review reports are currently not available for this contract">
					Not available
					</span>
					</c:otherwise>
					</c:choose>
				</td>
				 	<form:hidden id="selectedperformanceAndExpense${rowIndex}" path="displayContractReviewReports[${rowIndex}].selectedperformanceAndExpenseRatio"/>
				<td class="name td6" width="191px" style="width: 193px;height:31px;" align="center">
					<form:checkbox cssClass="selectedperformanceAndExpenseRatio" id="${rowIndex}" cssStyle="margin-left: 34px" path="displayContractReviewReports[${rowIndex}].selectedperformanceAndExpenseRatio"/>
				</td>
			</tr>
			</c:forEach>
			<c:if test="${planReviewReportForm.displayContractReviewReportsSize == NO_RECORD}">
					
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
	

	<c:choose>
      <c:when test="${planReviewReportForm.warningExist }">
	    <div class="continueButton" >
	    <div style="color: white; float: right"><input
		class="blue-btn next" 		
		onclick="doContinue('continue');" value="Continue" type="button" name="Continue" >
	</div>
	</div>
 </c:when>
	<c:otherwise>
	<c:if test="${planReviewReportForm.disabledContract !=true}">
<div class="nextButton" id="enabledBottomNextButton">
	<div style="color: white; float: right"><input
		class="blue-btn next disabled-grey-btn" onmouseover="this.className +=' btn-hover'"
		onclick="doNext('customize');" onmouseout="this.className='blue-btn next'"
		 value="Next" type="button" name="Next" disabled="disabled" >
	</div>
	
	</div>
	</c:if>
	<c:if test="${planReviewReportForm.disabledContract ==true}">

	
	<div class="nextButton" id="enabledBottomNextButton">
	<div style="color: white; float: right"><c:if test="${sessionScope.bobResults!='contract'}">
	<input
		class="blue-btn next disabled-grey-btn"
		onclick="doNext('customize');" value="Next" type="button" name="Next" disabled="true">
	</c:if>
	<c:if test="${sessionScope.bobResults=='contract'}">
	<input class="blue-btn next"
		onclick="doNext('customize');" value="Next" type="button" name="Next">
	</c:if>
	</div>
	</div>
	</c:if>
	
</c:otherwise>		
	</c:choose>		
	