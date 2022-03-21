<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder" %>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bdweb"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<content:contentBean contentId="<%=BDContentConstants.PLAN_REVIEW_REQUEST_CONFIRM_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="planRviewRequestConfirmMsg"/>
<content:contentBean contentId="<%=BDContentConstants.UPLOADED_COVER_IMAGE_GREATER_THAN_TWO_MB_ERROR%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="UploadedCoverImageGreaterThanTwoMBError" />
<content:contentBean contentId="<%=BDContentConstants.UPLOADED_LOGO_IMAGE_GREATER_THAN_ONE_MB_ERROR%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="UploadedLogoImageGreaterThanOneMBError" />
<content:contentBean contentId="<%=BDContentConstants.UPLOADED_COVER_IMAGE_IS_NOT_JPG_ERROR%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="UploadedCoverImageIsNotJPGError" />
<content:contentBean contentId="<%=BDContentConstants.UPLOADED_LOGO_IMAGE_IS_NOT_JPG_ERROR%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="UploadedLogoImageIsNotJPGError" />
	
<script type="text/javascript">
	function planRviewRequestConfirmMsg() {
		var planRviewRequestConfirmMsg = '<content:getAttribute beanName="planRviewRequestConfirmMsg" attribute="text" filter="true"/>';
		if (confirm(planRviewRequestConfirmMsg)) {
			return true;
		} else {
			return false;
		}
	}
	var UploadedCoverImageGreaterThanTwoMBError = '<content:getAttribute beanName="UploadedCoverImageGreaterThanTwoMBError" attribute="text" filter="true"/>';
	var UploadedLogoImageGreaterThanOneMBError = '<content:getAttribute beanName="UploadedLogoImageGreaterThanOneMBError" attribute="text" filter="true"/>';
	var UploadedCoverImageIsNotJPGError = '<content:getAttribute beanName="UploadedCoverImageIsNotJPGError" attribute="text" filter="true"/>';
	var UploadedLogoImageIsNotJPGError = '<content:getAttribute beanName="UploadedLogoImageIsNotJPGError" attribute="text" filter="true"/>';
</script>

<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="pageRegularlyNavigated"/>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants"/>

<form:hidden path="pathValue" />
<form:hidden path="filterContractNumber" />
<input type="hidden" name="index" />
<input type="hidden" name="bobResults" class="bob_results" value="${sessionScope.bobResults}" />
<%
String SINGLE_RECORD =BDConstants.SINGLE_RECORD;
pageContext.setAttribute("SINGLE_RECORD",SINGLE_RECORD,PageContext.PAGE_SCOPE);
%>
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
		<li><a id= "step1" class="exempt">Step 1</a></li>
		<li><a class="selected_link exempt"><span class="step_number">Step
		2:<span class="step_caption"><c:if test="${sessionScope.bobResults!='contract'}"> Customize your report(s)</c:if>
		<c:if test="${sessionScope.bobResults=='contract'}"> Customize your report</c:if></span></span></a></li>
		<li><a id="step3" class="exempt">Results</a></li>
	</ul>
	<div id=page_section_subheader>
	<div class="table_controls" style="border-top: #febe10 4px solid">
	<div class=table_action_buttons></div>
	<div class=table_display_info_abs>
	<c:if test="${sessionScope.bobResults!='contract'}">
	<strong>Number
of contracts:&nbsp; ${planReviewReportForm.selectedRecordListSize}</strong>


		</c:if>
		</div>

	</div>
	</div>
<form:hidden path="allContractSelected" />
	<div class=report_table>
	<table class=report_table_content>
		<thead>
			<tr>
			<c:if test="${planReviewReportForm.selectedRecordListSize gt 35}">
			
			<th class=val_str valign=bottom rowSpan=2 style="width: 152px;">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="contractName" direction="desc" styleId="sortField">
										Contract Name
			</report:bdSortLinkViaSubmit></th>
			<th class=val_str valign=bottom rowSpan=2 width="76px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="contractNumber" direction="asc" styleId="sortField">
										Contract <br />Number
			</report:bdSortLinkViaSubmit></th>
			<th class=val_str valign=bottom rowSpan=2 width="166px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="presenterName" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Presenter's Name</report:bdSortLinkViaSubmit>
			</th>
			<th class=val_str valign=bottom rowSpan=2 width="167px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="coverImageName" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Cover image</report:bdSortLinkViaSubmit>
				</th>
				<th class=val_str valign=bottom rowSpan=2 width="162px">
				<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="logoImage" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Logo image</report:bdSortLinkViaSubmit>
				</th>
				 <th class=val_str valign=bottom rowSpan=2 width="59px">Preview
				 </th>
			</c:if>
			
			<c:if test="${planReviewReportForm.selectedRecordListSize le 35}">
					
		    <c:if test="${sessionScope.bobResults=='contract'}">
		    <th Class=val_str valign=bottom rowSpan=2  style="width:156px">
			
										Contract Name
			</th>
			<th class=val_str valign=bottom rowSpan=2 width="80px">
			
										Contract <br />Number
			</th>
			<th class=val_str valign=bottom rowSpan=2 width="171px">
			Presenter's Name
			</th>
			<th class=val_str valign=bottom rowSpan=2 width="172px">
			Cover image
				</th>
				<th class=val_str valign=bottom rowSpan=2 width="165px">
				Logo image
				</th>
				<th class=val_str valign=bottom rowSpan=2 width="36px"> Preview
				</th>
				</c:if>
				
				<c:if test="${sessionScope.bobResults!='contract'}">
					
			<th class=val_str valign=bottom rowSpan=2 " style="width:156px">
			<report:bdSortLinkViaSubmit
					field="contractName" formName="planReviewReportForm" direction="desc" styleId="sortField">
										Contract Name
			</report:bdSortLinkViaSubmit></th>
			<th class=val_str valign=bottom rowSpan=2 width="80px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="contractNumber" direction="asc" styleId="sortField">
										Contract <br />Number
			</report:bdSortLinkViaSubmit></th>
			<th class=val_str valign=bottom rowSpan=2 width="171px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="presenterName" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Presenter's Name</report:bdSortLinkViaSubmit>
			</th>
			<th class=val_str valign=bottom rowSpan=2 width="172px">
			<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="coverImageName" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Cover image</report:bdSortLinkViaSubmit>
				</th>
				<th class=val_str valign=bottom rowSpan=2 width="165px">
				<report:bdSortLinkViaSubmit formName="planReviewReportForm" 
					field="logoImage" direction="<%=ReportSort.ASC_DIRECTION%>" styleId="sortField">Logo image</report:bdSortLinkViaSubmit>
				</th>
				<th class=val_str valign=bottom rowSpan=2 width="36px"> Preview
				</th>
				</c:if>
		   </c:if>				
			</tr>
			<tr></tr>
		</thead>
	</table>

	<div
		style=" overflow-y: auto; width: 100%; max-height: 1340px; border-bottom: #cac8c4 1px solid;">
	<table class=report_table_content id="reportTable1" style="width: 99.9%" >
		<tbody>
			<tr>
<c:forEach items="${planReviewReportForm.displayContractReviewReports}" var="selectedRecordVO" varStatus="indexValue" >

<c:set var="rowIndex"  value="${indexValue.index}"/>

					<c:choose>
						<c:when test="${rowIndex % 2 == 0}">
							<tr>
						</c:when>
						<c:otherwise>
							<tr class="spec">
						</c:otherwise>
					</c:choose>
					<td class=name width="160px" style="width: 161px; height: 32px;border-left-width:2px">
						${selectedRecordVO.contractName}
						
					</td>
					<c:choose>
					<c:when test="${rowIndex == 0}">
						<td cssClass="val_num_count checkbox_contract" width="95px" style="width:86px; height: 32px;">
						${selectedRecordVO.contractNumber}						
						</td>
					<td class=name width="177px" style="height:32px">
					
<form:input path="displayContractReviewReports[${rowIndex}].presenterName"  maxlength="50" size="16" cssClass="textbox_name" id="textbox_name${rowIndex}"  name="planReviewReportForm">


</form:input>
						<bdweb:fieldHilight name="presenterName${rowIndex}" singleDisplay="true"
													style="float:none" className="errorIcon" />
<c:if test="${planReviewReportForm.selectedRecordListSize != 1}">

						<a class="trigger applyAll"
						href="#"  title="Apply to all" id=""><img
						src="/assets/unmanaged/images/aIcon.gif" width="10"
						height="12" border="0" ></a>
						<a class="trigger exempt"
						href="javascript:clearAll();"  title="Clear all" id="clearAll"><img
						src="/assets/unmanaged/images/copy_icon.gif" width="10"
						height="12" border="0" ></a>	
</c:if>
					</td>		
					</c:when>
					 <c:otherwise>
					<td class="val_num_count checkbox_contract_sorted"  width="95px" style="width:86px; height: 32px;">
						${selectedRecordVO.contractNumber}						
					</td>
					<td class=name width="177px" style="height:32px">
<form:input path="displayContractReviewReports[${rowIndex}].presenterName" maxlength="50" size="16" cssClass="textbox_name" id="textbox_name${rowIndex}"  name="planReviewReportForm">


</form:input>
						<bdweb:fieldHilight name="presenterName${rowIndex}" singleDisplay="true"
													style="float:none" className="errorIcon" />
					</td>	
					
					</c:otherwise> 
					</c:choose>
					
					
					
					<td class="val_num_cnt" width="155px" id="coverName${rowIndex}" title="${selectedRecordVO.coverImageNameTitle}" style="color:#767676 ;width:142px;height:32px">${selectedRecordVO.coverImageName}</td>
					<td align="left" width="25px" style="height:32px"><a class="trigger exempt"
						href="javascript:coverImage(${selectedRecordVO.contractNumber},${rowIndex});"  title="Customize Cover"><img
						src="/assets/unmanaged/images/planreview_upload_icon.png" width="18"
						height="18" border="0" ></a> </td>
					<td class="val_num_cnt " width="136px" id="logoName${rowIndex}" title="${selectedRecordVO.logoImageNameTitle}" style="color:#767676;height:32px">${selectedRecordVO.logoImageName}</td>
					<td align="center" width="25px" style="height:32px"><a class="exempt" href="javascript:uploadLogo(${selectedRecordVO.contractNumber},${rowIndex});" title="Customize Logo" ><img
						src="/assets/unmanaged/images/planreview_upload_icon.png" width="18"
						height="18" border="0" ></a></td>

					<td align="center" style="width:49px;height:32px"><a class="exempt" href="javascript://" onclick="previewPage(${selectedRecordVO.contractNumber},${rowIndex},'${_csrf.token}');return false;" title="preview"><img
						src="/assets/unmanaged/images/planreview_preview_icon.png" width="18"
						height="18" border="0"></a></td>
</c:forEach>
			</tr>
			
		</tbody>
	</table>
	</div>
	</div>	
	<div>&nbsp;</div>

	<div class="nextButton" id="enabledBottomNextButton">

	<div Style="float: left"><input class="blue-btn next"
		onmouseover="this.className +=' btn-hover'"
		onmouseout="this.className='blue-btn next'" onclick="doBack('back','${_csrf.token}')"
		value=Back type=button name=Back /></div>
	<div style="color: white; float: right"><input
		name="Generate Report" class="generateButton"
		onmouseover="this.className +=' hover'"
		onmouseout="this.className='generateButton'"
		onclick="doNext('generateReport')" type="submit" value="Generate" /></div>

	</div>
	
	
	<DIV  id="previewPanel" style="visibility:hidden;BACKGROUND-COLOR: #FBF9EE;font:16px verdana,arial,helvetica,sans-serif;">          
        <%-- Dynamic --%>
    		</DIV>




