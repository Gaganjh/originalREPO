<%@ page import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder" %>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.bd.web.bob.planReview.sort.PrintPlanReviewRequestColoumn"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bdweb"%>
<%
	String SCROLL_RECORD_LIMIT = BDConstants.SCROLL_RECORD_LIMIT;
	pageContext.setAttribute("SCROLL_RECORD_LIMIT",SCROLL_RECORD_LIMIT,PageContext.PAGE_SCOPE);
	
	String SINGLE_RECORD = BDConstants.SINGLE_RECORD;
	pageContext.setAttribute("SINGLE_RECORD",SINGLE_RECORD,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_PRINT_REQUEST_CONFIRM_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="planRviewRequestConfirmMsg"/>
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

<style type="text/css">
	.message_error dl {
		text-align: left;
	}
	
	#loadingContent img.errorIcon {
		background-color: none;
		width: 16px;
		height: 16px;
	}
</style>

<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="pageRegularlyNavigated"/>
<form:hidden path="contractIdList"/>
<input type="hidden" name="bobResults" class="bob_results" value="${sessionScope.bobResults}"/>

<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants"/>
<input type="hidden" name="previousAction" value="${bdConstants.C0NTRACT_REVIEW_REPORT_RESULTS}"/>
            	<div>
					<ul class="proposal_nav_menu">
							<li><a class="selected_link exempt"><span style="padding: 10px !important;" class=step_number>Request printed copies</span></a></li>
					</ul>
				</div>	
					
					
				<div id=page_section_subheader>
						<div class="table_controls" style="border-top:#febe10 4px solid">	
							<div class=table_action_buttons ></div>
							<div class=table_display_info_abs >
							<strong>Total number of contracts:&nbsp;
${planReviewPrintForm.displayContractReviewReportsSize}</strong>
							</div>
						</div>
				</div>
						
				<div class=report_table>
					<table class=report_table_content>
							  <thead>
									<tr>
									<input type="hidden" name="allContractPrintSelected" id="allContractSelected"/>
									<c:if test="${sessionScope.bobResults!='contract'}">
									<th class=val_str vAlign=bottom rowSpan=2 noWrap width="35px">
									<form:checkbox path="allContractPrintSelected" id="select_all" />


			                         </th>
			                        </c:if>
			                         
									 <c:if test="${sessionScope.bobResults =='contract'}">
			                         <th class=val_str vAlign=bottom rowSpan=2 width="298px">
			                         <report:bdSortLinkViaSubmit formName="planReviewPrintForm" 
					                        styleId="sortField" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NAME.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
										Contract Name
			                             </report:bdSortLinkViaSubmit>
			                           </th>
			                             <th class=val_str vAlign=bottom rowSpan=2 width="170px">
			                             <report:bdSortLinkViaSubmit
					                       styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NUMBER.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
										Contract<br/>Number
			                           </report:bdSortLinkViaSubmit>
			                             </th>
			                            <th class=val_str valign=bottom rowspan=2  width="141px">
			                            <report:bdSortLinkViaSubmit
					                     styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.MONTH_END_DATE.getFieldName() %>"
					                    direction="<%=ReportSort.DESC_DIRECTION%>">Month End</report:bdSortLinkViaSubmit>
			                            </th> 
			                            <th class=val_str valign=bottom rowspan=2  width="158px">
			                            <report:bdSortLinkViaSubmit 
			                            styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.NUMBER_OF_COPIES.getFieldName() %>"
										direction="<%=ReportSort.ASC_DIRECTION%>">Number Of Copies</report:bdSortLinkViaSubmit></th>
			                            </th>
			                         </c:if>                        
			      <c:if test="${sessionScope.bobResults !='contract'}">
						 <c:if test="${planReviewReportForm.displayContractReviewReportsSize == SINGLE_RECORD}">

										     <th class=val_str vAlign=bottom rowSpan=2 width="298px">
											                         <report:bdSortLinkViaSubmit
													                        styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NAME.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
																		Contract Name
											                             </report:bdSortLinkViaSubmit>
											                             </th>
											                             <th class=val_str vAlign=bottom rowSpan=2 width="170px">
											                             <report:bdSortLinkViaSubmit
													                       styleId="sortField"  formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NUMBER.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
																		Contract<br/>Number
											                           </report:bdSortLinkViaSubmit>
											                             </th>
											                            <th class=val_str valign=bottom rowspan=2  width="141px">
											                            <report:bdSortLinkViaSubmit
													                     styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.MONTH_END_DATE.getFieldName() %>"
													                    direction="<%=ReportSort.DESC_DIRECTION%>">Month End</report:bdSortLinkViaSubmit>
											                            </th> 
											                            <th class=val_str valign=bottom rowspan=2  width="158px">
											                            <report:bdSortLinkViaSubmit
													                     styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.NUMBER_OF_COPIES.getFieldName() %>"
													                    direction="<%=ReportSort.ASC_DIRECTION%>">Number Of Copies</report:bdSortLinkViaSubmit></th>
											                            </th>
				</c:if>
			    <c:if test="${planReviewReportForm.displayContractReviewReportsSize != SINGLE_RECORD}">

									   <c:if test="${planReviewPrintForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">
		                               	
			                           <th class=val_str vAlign=bottom rowSpan=2 width="299px">
			                           </c:if>
			                           <c:if test="${planReviewPrintForm.displayContractReviewReportsSize le SCROLL_RECORD_LIMIT}">			
			                           <th class=val_str vAlign=bottom rowSpan=2 width="299px">
		                                </c:if>										
			                             <report:bdSortLinkViaSubmit
					                        styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NAME.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
										Contract Name
			                             </report:bdSortLinkViaSubmit>
			                            </th>
			                           <c:if test="${planReviewPrintForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">	
			                           <th class=val_str vAlign=bottom rowSpan=2 width="170px">
			                           </c:if>
			                            <c:if test="${planReviewPrintForm.displayContractReviewReportsSize le SCROLL_RECORD_LIMIT}">
			                           <th class=val_str vAlign=bottom rowSpan=2 width="170px">
		                                </c:if>			                           
			                          <report:bdSortLinkViaSubmit
					                       styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.CONTRACT_NUMBER.getFieldName() %>" direction="<%=ReportSort.ASC_DIRECTION%>">
										Contract <br/>Number
			                           </report:bdSortLinkViaSubmit>
			                            </th>
			                             <c:if test="${planReviewPrintForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">
			                                   <th class=val_str valign=bottom rowspan=2  width="142px">
			                           </c:if>
			                            <c:if test="${planReviewPrintForm.displayContractReviewReportsSize le SCROLL_RECORD_LIMIT}">			
			                           <th class=val_str valign=bottom rowspan=2  width="142px">
		                                </c:if>										
										 <report:bdSortLinkViaSubmit
					                     styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.MONTH_END_DATE.getFieldName() %>"
					                    direction="<%=ReportSort.DESC_DIRECTION%>">Month End</report:bdSortLinkViaSubmit>
								      	</th>
								      	<c:if test="${planReviewPrintForm.displayContractReviewReportsSize gt SCROLL_RECORD_LIMIT}">
			                           <th class=val_str valign=bottom rowspan=2  width="174px">
			                           </c:if>
			                            <c:if test="${planReviewPrintForm.displayContractReviewReportsSize le SCROLL_RECORD_LIMIT}">
			                           <th class=val_str valign=bottom rowspan=2  width="158px">
		                                </c:if>									
										 <report:bdSortLinkViaSubmit
					                     styleId="sortField" formName="planReviewPrintForm" field="<%= PrintPlanReviewRequestColoumn.NUMBER_OF_COPIES.getFieldName() %>"
					                    direction="<%=ReportSort.ASC_DIRECTION%>">Number Of Copies</report:bdSortLinkViaSubmit></th>	
</c:if>
								</c:if>
																			
									</tr>
									<tr></tr>
								</thead>

					</table>
		
						<div style=" overflow-y: auto;overflow-x: hidden; width: 100%; max-height:770px; border-bottom: #cac8c4 1px solid;">
						<table class=report_table_content id="reportTable1" >
						<tbody >
							<tr>
<c:forEach items="${planReviewPrintForm.displayContractReviewReports}" var="displayContractReviewReportVO" varStatus="indexValue" >
<c:set var="rowIndex"  value="${indexValue.index}"/>

							<c:choose>
									<c:when test="${rowIndex % 2 == 0}">
									<tr>
									</c:when>
									<c:otherwise>
									<tr class="spec">
									</c:otherwise> 
								</c:choose>

<form:hidden  id ="selectContracts${rowIndex}" path="displayContractReviewReports[${rowIndex}].printContractSelected"/>
								 
			                         <c:if test="${sessionScope.bobResults!='contract'}">
					                    <td width="30px">
<form:checkbox path="displayContractReviewReports[${rowIndex}].printContractSelected" id="${rowIndex}" cssClass="selectContracts" cssStyle="margin-left: 7px;"/>


							             </td>
					                </c:if>
								<td  class="downloadPDF"  width="235px" >
<c:if test="${not empty planReviewPrintForm.displayContractReviewReports[rowIndex].contractName}">
${planReviewPrintForm.displayContractReviewReports[rowIndex].contractName} 
</c:if>
<c:if test="${empty displayContractReviewReports[rowIndex].contractName}">
											None
</c:if>
								</td>
								
								<td class="val_num_count" id="contractnum${rowIndex + 1}" width="135px">
<c:if test="${not empty planReviewPrintForm.displayContractReviewReports[rowIndex].contractNumber}">
${planReviewPrintForm.displayContractReviewReports[rowIndex].contractNumber}
</c:if>
<c:if test="${empty planReviewPrintForm.displayContractReviewReports[rowIndex].contractNumber}">
											None
</c:if>
								</td>
								
								<td class="name " width="113x" >
${planReviewPrintForm.displayContractReviewReports[rowIndex].selectedReportMonthEndDate}
										<bdweb:fieldHilight name="selectedReportMonthEndDate${rowIndex}" singleDisplay="true"
													style="float:none" className="errorIcon" />
								</td>							
								<td class=name nowrap="nowrap" width="125px">
<form:select path="displayContractReviewReports[${rowIndex}].numberOfCopies" id="select_name${rowIndex}" cssClass="numberOfCopiesDD" disabled="${displayContractReviewReportVO.enable}" >


<form:options items="${planReviewPrintForm.numberOfCopies}" itemValue="value" itemLabel="label" />

</form:select>
								</td>
								
</c:forEach>
					</tbody>
				</table>
				</div>
			</div>
			
<c:if test="${planReviewPrintForm.displayContractReviewReportsSize == 0}">

					 
			
			<div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
					      <dd>1.&nbsp;<content:getAttribute id="noRecordsToDisplayMessage" attribute="text"/></dd>
					    </dl>
				  	</div>
</c:if>
		<div>
					&nbsp;
			</div>
			<div class="nextButton" id="enabledBottomNextButton">
					
					<div Style="float:right">					
					<input class="blue-btn_big next" id="print_button"  
					 onclick="displayPreview();" value="Print Selected" type=button name=download
					style="background-size: 100% 100% !important; width: 200px;">
					</div>
			</div>
			
			<DIV  id="previewPanel" style="visibility:hidden;BACKGROUND-COLOR: #FBF9EE;">          
        <%-- Dynamic --%>
    		</DIV>
