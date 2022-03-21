<%@ page import="com.manulife.pension.bd.web.estatement.RiaStatementVO"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.bd.web.estatement.EstatementReportData" %>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>


<content:contentBean
	contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="pdfIcon" />

<content:contentBean
	contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="csvIcon" />

<content:contentBean
	contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="csvIcon" />

<content:contentBean
	contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="pdfIcon" />


<% 
if(request.getAttribute(BDConstants.REPORT_BEAN)!=null){
	EstatementReportData theReport =(EstatementReportData)request.getAttribute(BDConstants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}

%>


<script type="text/javascript">

function sortSubmit(sortfield, sortDirection) {
	document.forms['estatementReportForm'].action = "/do/estatement/fetch?task=sort";
	document.forms['estatementReportForm'].elements['sortField'].value = sortfield;
	document.forms['estatementReportForm'].elements['sortDirection'].value = sortDirection;
	document.forms['estatementReportForm'].submit();
}


function pagingSubmit(pageNumber) {
	if (document.forms['estatementReportForm']) {

		document.forms['estatementReportForm'].action = "/do/estatement/fetch?task=page";
		document.forms['estatementReportForm'].elements['pageNumber'].value = pageNumber;

		document.forms['estatementReportForm'].submit();
	}
}

function doViewDocument(frm,task,fileType,firmId,firmName,genDate,docId)
{
	if(fileType == 'csv'){
		if (confirm(' I acknowledge that the accuracy of data downloaded outside of this application is the responsibility of the user and that neither John Hancock nor its affiliates shall be responsible for any inaccuracy resulting from such use.')) {
			// do nothing
		}else {
			return false;
		}
	}
	
	document.estatementReportForm.fileType.value=fileType;
	document.estatementReportForm.docId.value= docId;
	document.estatementReportForm.statementFirmId.value= firmId;
	document.estatementReportForm.statementFirmName.value= firmName;
	document.estatementReportForm.statementGenDateStr.value= genDate;
	document.estatementReportForm.action="/do/estatement/fetch?task="+task;
	document.estatementReportForm.target = "_blank";
	document.estatementReportForm.submit();
	return false;
	
 } 
function filterRiaStatments(obj,frm,task)
{
	var riaStmDateFilter = document.estatementReportForm.riaStmDateFilter.value;
	var riaFirmFilter = document.estatementReportForm.riaFirmFilter.value;
	
	document.estatementReportForm.action="/do/estatement/fetch";
	document.estatementReportForm.target = "_self";
	document.estatementReportForm.pageNumber.value = 1; 
	document.estatementReportForm.submit();
	return false;
	
 } 

<%--var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';  

function getDocumentsForRIAFirm(frm, action, btn) {
  var lastSelectedFirmName = document.getElementById("lastSelectedFirmName").value;
  if(lastSelectedFirmName != "" && frm.selectedFirmId.value != "") 
  {
	  // user has selected a firm from drop-down
  	if(lastSelectedFirmName == frm.selectedFirmName.value) 
  	{ 
  		//After selecting no changes were made
  		doProtectedSubmitWithRIAFirms(frm, action, btn)
    }
    else 
    { 
    	//Firm name is modified
    	verifyRIAFirmName(frm.selectedFirmName.value, frm, firmError);
    }
  }
  else 
  { 
   	//User has not selected a firm. Might have copied the entire firm name. So we send another AJAX
  	//request to validate the firm name.
	  verifyRIAFirmName(frm.selectedFirmName.value, frm, firmError);
  }
  return false;
}
--%>
	var submitted = false;

	function doProtectedRIASubmitBtn(frm, task, obj) {
		var hrefValue = null;
		if (obj != undefined && obj != null) {
			obj.disabled = true;
		}
		if (!submitted) {
			submitted = true;
			frm.action.value="/do/estatement/fetch?task="+task;;
			try {
				frm.submit();
			} catch (e) {
				obj.disabled = false;
				submitted = false;
			}
		} else {
			window.status = "Transaction already in progress. Please wait.";
		}
		return false;

	}

	function doProtectedSubmitWithRIAFirms(frm, action, btn) {
		doProtectedRIASubmitBtn(frm, action, btn);
		return false;
	}
</script>

<style type="text/css">
form {
	display: inline;
}
</style>


<layout:pageHeader nameStyle="h2" />
<report:formatMessages scope="session" />

<bd:form id="estatementReportForm" action="/do/estatement/fetch" modelAttribute="estatementReportForm" name="estatementReportForm">
	<input type="hidden" name="docId">
	<input type="hidden" name="fileType">
	<input type="hidden" name="userType">
	<input type="hidden" name="statementFirmId">
	<input type="hidden" name="statementFirmName">
	<input type="hidden" name="statementGenDateStr">
<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="pageNumber"/>
	
	<div class="page_section_subheader controls">
		<div>
			<p>
				<label style="color: #fff"> Statement date:</label>
<form:select path="riaStmDateFilter" onchange="filterRiaStatments(this, document.estatementReportForm, 'fetchDocuments');" cssStyle="width:100px;">

				
					<form:option value="All">All</form:option>
<c:forEach items="${estatementReportForm.listDatesStr}" var="listDateStr">

					
					<form:option  value="${listDateStr}"> 
					${listDateStr}
					</form:option>
</c:forEach>
</form:select>
				<c:choose>
					<c:when test="${fn:length(estatementReportForm.listFirms) gt 1}">
						<label style="color: #fff"> Firm name:</label>
<form:select path="riaFirmFilter" onchange="filterRiaStatments(this, document.estatementReportForm, 'fetchDocuments');" cssStyle="width:100px;">

						
							<form:option value="All">All</form:option>
<c:forEach items="${estatementReportForm.listFirms}" var="listFirm">

							<form:option  value="${listFirm}">
							${listFirm}</form:option>
</c:forEach>
</form:select>
					</c:when>
					<c:otherwise>
						<label style="color: #fff" hidden=true> Firm name:</label>
<form:select path="riaFirmFilter" onchange="filterRiaStatments(this, document.estatementReportForm, 'fetchDocuments');" cssStyle="width:100px;visibility:hidden;">

						
							<form:option value="All">All</form:option>
<c:forEach items="${estatementReportForm.listFirms}" var="listFirm">

							<form:option  value="${listFirm}">
							${listFirm}</form:option>
</c:forEach>
</form:select>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
			<span>
	
					 <input type="button" class="blue-btn" Style="margin-left:340px;margin-top:-32px;visibility: hidden;"
						onmouseover="this.className +=' btn-hover'"
						onmouseout="this.className='blue-btn next'" name="Submit"
						value="Submit"
						onclick=" ">
			</span>		


		
	</div>
	<c:if test="${not empty theReport}">
<c:if test="${not empty estatementReportForm.riaStatementListVO}">
					<div class="table_controls">
					<div class="table_action_buttons" style="width:30%;"></div>
					<div class="table_display_info">
					<strong> <report:recordCounter report="theReport" 
							label="Statements" />
					</strong>
				</div>
						<div class="table_pagination">
							<strong>
							<report:pageCounterViaSubmit formName="estatementReportForm" report="theReport" arrowColor="black"/>
							</strong>
						</div>
						</div>
</c:if>
						</c:if>
						
	<div class="report_table">
		<table class="report_table_content" id="participants_table">
			<thead>
				<tr>
					<th rowspan="2" class="val_str"><report:bdSortLinkViaSubmit formName="estatementReportForm"
					field="statementDate" direction="desc" styleId="sortField">Statement Date</report:bdSortLinkViaSubmit></th>
					<th rowspan="2" class="val_str"><report:bdSortLinkViaSubmit formName="estatementReportForm"
					field="firmName" direction="asc" styleId="sortField">Firm Name</report:bdSortLinkViaSubmit></th>
					<th rowspan="2" class="val_str align_center">PDF</th>
					<th rowspan="2" class="val_str align_center">CSV</th>
				</tr>
			</thead>
			<tbody>
<c:if test="${empty estatementReportForm.riaStatementListVO}">


					<tr>
						<td colspan="4">There are no statements available.</td>
					</tr>

</c:if>
<c:if test="${not empty estatementReportForm.riaStatementListVO}">

<c:forEach items="${estatementReportForm.riaStatementListVO}" var="riaStatementList" varStatus="rowIndex" >


						<c:choose>
							<c:when test="${rowIndex.index % 2 == 0}">
								<tr class="">
							</c:when>
							<c:otherwise>
								<tr class="spec">
							</c:otherwise>
						</c:choose>

						<td class="name align_left" nowrap="nowrap"><render:date
								property="riaStatementList.genDate" patternOut="MM-dd-yyyy" /></td>
						<td class="name align_left" nowrap="nowrap">${riaStatementList.firmName}
						</td>
						<td class="name align_center" nowrap="nowrap"><a
								onclick='doViewDocument(document.estatementReportForm,"downloadDocuments","pdf","${riaStatementList.firmId}","<c:out value="${riaStatementList.firmName}"/>","${riaStatementList.genDateStr}","${riaStatementList.pdfDocId}")'>
							<img src="/assets/generalimages/pdf_btn.gif" border="0">
					</a></td>


					<td class="name align_center"><a 
						onclick='doViewDocument(document.estatementReportForm,"downloadDocuments","csv","${riaStatementList.firmId}","<c:out value="${riaStatementList.firmName}"/>","${riaStatementList.genDateStr}","${riaStatementList.csvDocId}")'>
							<img src="/assets/generalimages/csv_btn.gif" border="0">
					</a></td>

						</tr>
</c:forEach>
</c:if>
			</tbody>
		</table>
	</div>
	<c:if test="${not empty theReport}">
<c:if test="${not empty estatementReportForm.riaStatementListVO}">
					<div class="table_controls">
					<div class="table_action_buttons" style="width:30%;"></div>
					<div class="table_display_info">
					<strong> <report:recordCounter report="theReport" 
							label="Statements" />
					</strong>
				</div>
						<div class="table_pagination">
							<strong>
							<report:pageCounterViaSubmit formName="estatementReportForm" report="theReport" arrowColor="black"/>
							</strong>
						</div>
						</div>
</c:if>
						</c:if>
	</div>
</bd:form>

<div class="footnotes">
    <dl>
      <dd><content:pageFooter beanName="layoutPageBean"/></dd> 
    </dl>
    
	<div class="footnotes_footer"></div>
</div>	
