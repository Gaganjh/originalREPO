<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm"%>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_CSV_ALL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvAllIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<content:contentBean contentId="<%=BDContentConstants.BOB_HISTORICAL_CONTRACT_INFO_FOOTNOTE%>"
        type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="historicalContractFootNote"/>

<content:contentBean contentId="<%=BDContentConstants.BOB_PN_PP_CONTRACT_CNT_ASOFLATESTDATE_FOOTNOTE%>"
        type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="PNAndPPContractCountAsOfLatestDateFootnote"/>
        
 <content:contentBean contentId="<%=BDContentConstants.AB_COLUMN_FOOTNOTE%>"
        type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="assetBasedFootnote"/>
        
<content:contentBean contentId="<%=BDContentConstants.DAILY_UPDATE_FOOTNOTE%>"
        type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="dailyUpdateootnote"/>

<jsp:useBean id="blockOfBusinessForm" scope="session" type="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm" />



<report:formatMessages scope="request"/>
<jsp:include page="blockOfBusinessCommon.jsp"/>

<form:hidden  id="showAdvanceFilterID"  path="blockOfBusinessForm.showAdvanceFilter" value="${blockOfBusinessForm.showAdvanceFilter}"/>

<div class="page_section_subheader controls page_section_subheader_increased_height">
	<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>

	<%-- Quick Filtering section. --%>

	<bob:bobFilters filterType="quickFilter" actionURL="/do/bob/blockOfBusiness/Pending/" reportAsOfDtEnabled="false"/>

    <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon" style="margin-right : 5px;" title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" style="margin-right : 3px;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	<a href="javascript://" onClick="doDownloadAllCSV();return false;" style="margin-right : 3px;" class="csv_icon"  title="<content:getAttribute beanName="csvAllIcon"  attribute="text"/>" > <content:image contentfile="image" id="csvAllIcon" /></a>

	<div class="button_header" style="margin-left : 5px; margin-top: 15px; float:right; margin-right : 8px; "><span><input id="customize_report" type="button" value="Advanced Search" style="margin-left : 0px;width:110px;" onclick="openCloseAdvancedFilter()"/></span> </div>
	<div id="quickFilterSubmitDiv" class="button_header"  style="margin:0px; margin-left : 5px; margin-top: 15px; float:right;"><span><input id="quickFilterSubmit" type="button" value="Submit" style="margin-left : 0px" onclick="submitBOB(document.getElementById('quickFilterForm'))"/></span> </div>

</div>
			
<navigation:bobNavigation/>

<div class="report_table">
	<div id="bob_overview_report">
		<div id="report_customization_wrapper">	
			<h4><content:getAttribute id="layoutPageBean" attribute="body2Header"/></h4>
			<%-- Advanced Filtering section. --%>
			<bob:bobFilters filterType="advancedFilter" actionURL="/do/bob/blockOfBusiness/Pending/"/>
		</div><%--#report_customization_wrapper--%>
	</div> <%--#bob_overview_table--%>
	<div class="table_controls_footer"></div>

	<jsp:include page="BlockOfBusinessReportInfo.jsp"/>

</div><%--.report_table--%>

<div class="footnotes">

	<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
	<c:if test="${blockOfBusinessForm.showHistoricalContractInfoFootnote}">
		<dl><dd><content:getAttribute beanName="historicalContractFootNote" attribute="text"/></dd></dl>
	</c:if>
	<c:if test="${blockOfBusinessForm.showPNAndPPContractCountAsOfLatestDateFootnote}">
		<dl><dd><content:getAttribute beanName="PNAndPPContractCountAsOfLatestDateFootnote" attribute="text"/></dd></dl>
	</c:if>
	<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
	<c:if test="${blockOfBusinessForm.compensationSectionDisplayed}">
	   <dl class="legendCls"><dd>Legend</dd></dl>
       <c:forEach var="legend" items="${blockOfBusinessForm.legends}">
	      <dl class="legendCls"><dd>${legend}</dd></dl>
	   </c:forEach>
	</c:if>	
	<c:if test="${blockOfBusinessForm.riaSectionDisplayed}">
		<dl class="riaLegendCls"><dd>Legend</dd></dl>
		<c:forEach var="riaLegend" items="${blockOfBusinessForm.riaLegends}">
		      <dl class="riaLegendCls"><dd>${riaLegend}</dd></dl>
		</c:forEach>
	</c:if>	
	<c:if test="${blockOfBusinessForm.compensationSectionDisplayed}">
		<dl class="legendCls"><dd><content:getAttribute beanName="assetBasedFootnote" attribute="text"/></dd></dl>
		<dl class="legendCls"><dd><content:getAttribute beanName="dailyUpdateootnote" attribute="text"/></dd></dl>
	</c:if>
	<c:if test="${blockOfBusinessForm.fiduciarySectionDisplayed}">
		<dl class="fiduciarylegendCls"><dd>Legend</dd></dl>
		<c:forEach var="fiduciaryServicesTabLegend" items="${blockOfBusinessForm.fiduciaryServicesTabLegends}">
		      <dl class="fiduciarylegendCls"><dd>${fiduciaryServicesTabLegend}</dd></dl>
		</c:forEach>
	</c:if>	
	<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
	<div class="footnotes_footer"></div>
</div> 
