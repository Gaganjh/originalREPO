<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportData" %>
<%@ page import="com.manulife.pension.bd.web.brokerListing.BrokerListingUtility"%>
<%@ page import="com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility"%>
<%@ page import="com.manulife.pension.service.security.role.BDInternalUser"%>
<%@ page import="com.manulife.pension.service.security.role.BDFirmRep"%>
<%@ page import="com.manulife.pension.service.broker.valueobject.BrokerDealerFirm" %>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentProperties" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentCacheConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.brokerListing.BrokerListingForm"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ page import="com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportVO"%>


<jsp:useBean id="brokerListingForm" scope="session" class="com.manulife.pension.bd.web.brokerListing.BrokerListingForm"/>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<script type="text/javascript">
    /**
     * This method gives the region div ID for Region dropdown box.
     */
    function getRegionID() {
        return "<%=appendPrefix(BrokerListingReportData.FILTER_REGION_ID)%>";
    }

    /**
     * This method gives the Division div ID for Division dropdown box.
     */
    function getDivisionID() {
        return "<%=appendPrefix(BrokerListingReportData.FILTER_DIVISION_ID)%>";
    }

    /**
     * This method is called when the user changes the Region Option. 
     * This method sets the appropriate option in Division Dropdown based on Region selected.
     */
    function selectDivision () {
        setDivision(getRegionID(), getDivisionID());
    }

    /**
     * This method is called when the user changes the Division Option. 
     * This method changes the Region Dropdown so that it contains only those regions belonging to the selected Division.
     */
    function shortenRegionOptions() {
        setRegion(getRegionID(), getDivisionID());
    }

    /**
     * This method is called when the user clicks on Customize Report to open the Advance Filter section. 
     * This method sets the Region, Division drop down options appropriately, based on the current Region / Division 
     * that has been passed from Quick Filter.
     */
    function setRegionAndDivisionAfterAdvFilterIsOpen() {        
        var regionIDOptions = document.getElementById(getRegionID());
        var divisionIDOptions = document.getElementById(getDivisionID());
        var selectedRegion = null;

        if (regionIDOptions != null) {
            if (regionIDOptions.selectedIndex != 0) {
                selectedRegion = regionIDOptions.options[regionIDOptions.selectedIndex].value;
                selectDivision();
            }
        } 
        if (divisionIDOptions != null) {
            if (divisionIDOptions.selectedIndex != 0) {
                shortenRegionOptions();
                // Re set the Region Dropdown with previously selected value.
                for(index=0; index < regionIDOptions.options.length; index++) {
                    if (regionIDOptions.options[index].value == selectedRegion) {
                        regionIDOptions.selectedIndex == index;
                        regionIDOptions.options[index].selected=true;
                        break;
                    }
                }
            }
        }
    }
</script>
<%! 
    /**
    * This method is used to generated ID values for "Advanced Filtering" criteria's. 
    * The way the Javascript has been coded is:
    *   When a user clicks "Customize Report" Link, we need to pass values from Quick Filter section to "Advanced Filtering" section.
    *   For this to happen, and for ease of coding, each criteria in "Quick Filtering" section has a styleId declared to it.
    *   Similarly, each of those filtering criteria in "Advanced Filtering" section have been given a styleId which is equivalent to
    *   "adv" + corresponding styleId of criteria in "Quick Filtering" section.
    *
    *   For example: in Quick Filtering criteria, the contract Name has a styleId of "contractName". If we want to pass this value
    *   to "Advance Filtering" section to its corresponding filtering criteria, the javascript looks out for a Element with
    *   name "advcontractName".
    *
    *   The below method is used to prefix "adv" to the styleId's of criteria in "Advanced Filtering" section.
    */
    public String appendPrefix(String quickFilterID){
   return "adv" + quickFilterID;
} 
%> 

<%-- The below image is being loaded so that when a user clicks on the Submit button in BrokerListing page,
the image shows up. Otherwise, the Image will not show up.--%>
<img style="display:none" src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>
 
 
 <% 
BrokerListingReportData theReport =(BrokerListingReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);


%>
 
<c:if test="${not empty theReport}">


    
    <div id="summaryBox">
        <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
            <% if (userProfile.getRole() instanceof BDInternalUser && !userProfile.isInMimic()) { %>
                User Name: <strong><%=userProfile.getBDPrincipal().getFirstName()%> 
                                    <%=userProfile.getBDPrincipal().getLastName()%></strong><br/>
                <br/>
            <%
                } else if (userProfile.getRole() instanceof BDFirmRep) {
                    ArrayList<String> firmNames = BlockOfBusinessUtility.getAssociatedFirmNamesForFirmRep(userProfile);
                    pageContext.setAttribute("firmNames", firmNames);
                %>
                <table> 
                    <tr>
                        <td valign="top" style="padding-top:4px">Summary: </td>
                        <td valign="top">
                            <table>
                                <c:forEach var="firmName" items="${firmNames}">
                                    <tr><td style="padding-top:0px"><strong>${firmName}</strong></td></tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                </table>
            <%
                }
            %>

        Total Contract Assets: 
        <c:if test="${!theReport.resultTooBigInd}">
	        <c:if test="${!empty theReport.brokerListingSummaryVO.totalContractAssets}"> 
	            <strong><report:number property="theReport.brokerListingSummaryVO.totalContractAssets" type="c"/></strong> 
	        </c:if>
        </c:if>
        <c:if test="${theReport.resultTooBigInd}">
        	-
        </c:if>
        <br />
        Total Number of Contracts:
        <c:if test="${!theReport.resultTooBigInd}">
	        <c:if test="${!empty theReport.brokerListingSummaryVO.totalNumberOfContracts}">
	            <strong><report:number property="theReport.brokerListingSummaryVO.totalNumberOfContracts" type="i"/></strong> 
	        </c:if>
        </c:if>
        <c:if test="${theReport.resultTooBigInd}">
        	-
        </c:if>
        <br />
        Total Number of Financial Representatives: 
        <c:if test="${!empty theReport.brokerListingSummaryVO.totalNumberOfFinancialReps}">
            <strong><report:number property="theReport.brokerListingSummaryVO.totalNumberOfFinancialReps" type="i"/></strong>
        </c:if>
    </div>
</c:if>

<%-- Display Intro1/Intro2 test --%>
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
<!--Layout/intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

<!--Layout/Intro2-->
<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
<div class="table_controls_footer"></div>

<report:formatMessages scope="request"/>

<input type="hidden" name="pdfCapped" value="${brokerListingForm.pdfCapped}" />


<form:hidden  id="showAdvanceFilterID"  path="brokerListingForm.showAdvanceFilter" value="${brokerListingForm.showAdvanceFilter}"/>
<% 
    pageContext.setAttribute("asOfDatesList", BrokerListingUtility.getMonthEndDates());
    pageContext.setAttribute("statesList", BlockOfBusinessUtility.getStatesList());
    pageContext.setAttribute("quickFilterList", brokerListingForm.getEnabledQuickFiltersList());
    if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_RVP_ID)) {
        pageContext.setAttribute("rvpNamesList", BlockOfBusinessUtility.getAllRVPs());
    }
    if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_REGION_ID)) {
        pageContext.setAttribute("salesRegionList", BlockOfBusinessUtility.getAllSalesRegions());
    }
    if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_DIVISION_ID)) {
        pageContext.setAttribute("salesDivisionList", BlockOfBusinessUtility.getAllSalesDivisions());
    }
    pageContext.setAttribute("bdFirmRepAssociatedFirmsList", BlockOfBusinessUtility.getAssociatedFirmsForBDFirmRep(userProfile));
%>

<%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_REGION_ID)) { %>
    <select id="regionToDivisionIDMap" style="display:none;">
        <c:forEach items="${pageScope.salesRegionList}" var="salesRegion">
            <option value="${salesRegion.id}" >${salesRegion.divisionId}</option>
        </c:forEach>
    </select>
    <select id="divisionIDToPartyIDMap" style="display:none;">
        <c:forEach items="${pageScope.salesDivisionList}" var="salesDivision">
            <option value="${salesDivision.divisionId}" >${salesDivision.id}</option>
        </c:forEach>
    </select>
    <select id="regionStore" style="display:none;">
        <option value=""></option>
        <c:forEach items="${pageScope.salesRegionList}" var="salesRegion">
            <option value="${salesRegion.id}" >${salesRegion.name}</option>
        </c:forEach>
    </select>
<%}%>

<div class="page_section_subheader controls">
    <bd:form id="quickFilterForm" method="POST" action="/do/brokerListing/" cssClass="page_section_filter form" modelAttribute="brokerListingForm" name="brokerListingForm">
       <input type="hidden" name="task" value="filter"/>
        <%-- This property is used to see if the Quick Filter was submitted or Advanced Filtering was submitted. --%>
        <input type="hidden" name="fromQuickFilter" value="true"/>

        <p><label for="kindof_filter">Report as of </label></p>

        <c:set var="asOfDatesList" value="${pageScope.asOfDatesList}"/>
        <bd:select name="brokerListingForm" property="asOfDateSelected" onchange="setAsOfDateInAdvFilterForm()">
            <bd:dateOptions name="asOfDatesList" renderStyle="<%=RenderConstants.MEDIUM_STYLE%>"/>
        </bd:select>

        <p><label for="kindof_filter">Filter</label> </p>
	 
        
	<form:select id="quickFilterSelected" path="quickFilterSelected"  onchange="showFilterExpression('true');">
					
					                <form:options items="${pageScope.quickFilterList}"  itemValue="value"
													itemLabel="label"/>
					</form:select>
        <p><label for="kindof_filter">search</label> </p>

        <input id="<%=BDConstants.FILTER_BLANK_ID%>" type="text" readOnly="true" value="" size="10"/>
		<form:input path="quickFilterFinancialRepName" maxlength="60" cssStyle="display:none;" id="<%=BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID%>" />
        
        

        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_BDFIRM_NAME_ID)) { %>
            <%if (userProfile.getRole() instanceof BDFirmRep) { %>
               <form:select id="<%=BrokerListingReportData.FILTER_BDFIRM_NAME_ID%>" path="quickFilterBDFirmID" disabled="false" style="display:none;">
                    <form:option value=""></form:option>
                    <c:forEach items="${pageScope.bdFirmRepAssociatedFirmsList}" var="bdFirmInfo">
                        <form:option value="${bdFirmInfo.id}" >${bdFirmInfo.firmName}</form:option>
                    </c:forEach>
               </form:select>
            <%} else {%>
                <bob:bobFirmSearch firmSearchDivID="quickFilterFirmSearchDIV" inputIDForFirmID="quickFilterBDFirmID" inputNameForFirmID="quickFilterBDFirmID" inputValueForFirmID="${e:forHtmlAttribute(brokerListingForm.quickFilterBDFirmID)}" IDfirmSearchContainer="firmSearchContainer1" inputIDForFirmName="<%=BrokerListingReportData.FILTER_BDFIRM_NAME_ID%>" inputNameForFirmName="quickFilterBDFirmName" inputValueForFirmName="${e:forHtmlAttribute(brokerListingForm.quickFilterBDFirmName)}"/> 
            <%} %>
        <%} %>
 
        <form:input path="quickFilterCityName" maxlength="25" cssStyle="display:none;" id="<%=BrokerListingReportData.FILTER_CITY_NAME_ID%>" />

      <form:select id="<%=BrokerListingReportData.FILTER_STATE_CODE_ID%>" name="brokerListingForm" path="quickFilterStateCode" disabled="false" style="display:none;">
            <form:option value=""></form:option>
            <c:forEach items="${pageScope.statesList}" var="state">
                <form:option value="${state.code}" >${state.code}</form:option>
            </c:forEach>
        </form:select>

        <form:input path="quickFilterZipCode" maxlength="5" cssStyle="display:none;" id= "<%=BrokerListingReportData.FILTER_ZIP_CODE_ID%>" />

<form:input path="quickFilterProducerCode" maxlength="7" cssStyle="display:none;" id= "<%=BrokerListingReportData.FILTER_PRODUCER_CODE_ID%>"/>

        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_RVP_ID)) { %>
            <form:select id="<%=BrokerListingReportData.FILTER_RVP_ID%>" name="brokerListingForm" path="quickFilterRVPName" disabled="false" style="display:none;">
                <form:option value=""></form:option>
                <c:forEach items="${pageScope.rvpNamesList}" var="rvpName">
                    <form:option value="${rvpName.id}" >${rvpName.lastName}, &nbsp;${rvpName.firstName}</form:option>
                </c:forEach>
            </form:select>
        <%}%>
        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_REGION_ID)) { %>
            <form:select id="<%=BrokerListingReportData.FILTER_REGION_ID%>" name="brokerListingForm" path="quickFilterSalesRegion" disabled="false" style="display:none;">
                <form:option value=""></form:option>
                <c:forEach items="${pageScope.salesRegionList}" var="salesRegion">
                    <form:option value="${salesRegion.id}" >${salesRegion.name}</form:option>
                </c:forEach>
            </form:select>
        <%}%>
        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_DIVISION_ID)) { %>
            <form:select id="<%=BrokerListingReportData.FILTER_DIVISION_ID%>" name="brokerListingForm" path="quickFilterSalesDivision" disabled="false" style="display:none;">
                <form:option value=""></form:option>
                <c:forEach items="${pageScope.salesDivisionList}" var="salesDivision">
                    <form:option value="${salesDivision.id}" >${salesDivision.name}</form:option>
                </c:forEach>
            </form:select>
        <%}%>

    </bd:form>
    
    <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>

    <div class="button_header" style="margin-left : 5px; margin-top: 15px; float:right; margin-right : 8px; "><span><input id="customize_report" type="button" value="Advanced Search" style="margin-left : 0px;width:110px;" onclick="openCloseAdvancedFilter()"/></span> </div>
    <div id="quickFilterSubmitDiv" class="button_header"  style="margin:0px; margin-left : 5px; margin-top: 15px; float:right;"><span><input id="quickFilterSubmit" type="button" value="Submit" style="margin-left : 0px" onclick="submitBrokerListing(document.getElementById('quickFilterForm'))"/></span> </div>
</div>

<div class="report_table">
    <div id="bob_overview_report">
        <div id="report_customization_wrapper">
            
           <bd:form id="advancedFilterForm" method="POST" action="/do/brokerListing/" modelAttribute="brokerListingForm" name="brokerListingForm">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="fromQuickFilter" value="false"/>
<input type="hidden" name="asOfDateSelected"/>

                <table width="100%">
                    <tr>
                        <td>Financial Rep:</td>
                    <td><form:input path="financialRepName" maxlength="60" id="<%=appendPrefix(BrokerListingReportData.FILTER_BDFIRM_NAME_ID)%>"/> </td>

                        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_BDFIRM_NAME_ID)) { %>
                            <td>Firm Name:</td>
                            <td>
                                <%if (userProfile.getRole() instanceof BDFirmRep) { %>
                                   <form:select id="<%=appendPrefix(BrokerListingReportData.FILTER_BDFIRM_NAME_ID)%>" path="bdFirmID" disabled="false">
                                        <form:option value=""></form:option>
                                        <c:forEach items="${pageScope.bdFirmRepAssociatedFirmsList}" var="bdFirmInfo">
                                            <form:option value="${bdFirmInfo.id}" >${bdFirmInfo.firmName}</form:option>
                                        </c:forEach>
                                   </form:select>
                                <%} else {%>
                                    <bob:bobFirmSearch firmSearchDivID="firmSearchDivID" inputIDForFirmID="bdFirmID" inputNameForFirmID="bdFirmID" inputValueForFirmID="${brokerListingForm.bdFirmID}" IDfirmSearchContainer="firmSearchContainer2" inputIDForFirmName="<%=appendPrefix(BrokerListingReportData.FILTER_BDFIRM_NAME_ID)%>" inputNameForFirmName="bdFirmName" inputValueForFirmName="${brokerListingForm.bdFirmName}" variableName="oAC2" prepopulatedFirmNameVar="prepopulatedFirmName2"/> 
                                <%} %>
                            </td>
                        <%} %>

                        <td>City:</td>
                        <td><form:input path="cityName" maxlength="25" id="<%=appendPrefix(BrokerListingReportData.FILTER_CITY_NAME_ID)%>" /></td>
                    </tr>
                    <tr>
                        <td>State:</td>
                        <td>
                            <form:select id="<%=appendPrefix(BrokerListingReportData.FILTER_STATE_CODE_ID)%>" name="brokerListingForm" path="stateCode" disabled="false">
                                <form:option value=""></form:option>
                                <c:forEach items="${pageScope.statesList}" var="state">
                                    <form:option value="${state.code}" >${state.code}</form:option>
                                </c:forEach>
                            </form:select>
                        </td>

                        <td>ZIP Code:</td>
                        <td><form:input path="zipCode" maxlength="5" id="<%=appendPrefix(BrokerListingReportData.FILTER_ZIP_CODE_ID)%>" /></td>

                        <td>Producer Code:</td>
                        <td><form:input path="producerCode" maxlength="7" id="<%=appendPrefix(BrokerListingReportData.FILTER_PRODUCER_CODE_ID)%>" /></td>
                    </tr>
                    <tr>
                        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_RVP_ID)) { %>
                            <td>RVP Name:</td>
                            <td>
                                <form:select id="<%=appendPrefix(BrokerListingReportData.FILTER_RVP_ID)%>" name="brokerListingForm"    path="rvpName" disabled="false">
                                    <form:option value=""></form:option>
                                    <c:forEach items="${pageScope.rvpNamesList}" var="rvpName">
                                        <form:option value="${rvpName.id}" >${rvpName.lastName},    &nbsp;${rvpName.firstName}</form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        <%}%>
                        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_REGION_ID)) { %>
							<td>Region:</td>
                            <td>
								<form:select id="<%=appendPrefix(BrokerListingReportData.FILTER_REGION_ID)%>" name="brokerListingForm" path="salesRegion" disabled="false" onchange="selectDivision();">
                                    <form:option value=""></form:option>
                                    <c:forEach items="${pageScope.salesRegionList}" var="salesRegion">
                                        <form:option value="${salesRegion.id}" >${salesRegion.name}</form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        <%}%>
                        <%if(brokerListingForm.isFilterEnabled(BrokerListingReportData.FILTER_DIVISION_ID)) { %>
							<td>Division:</td>
							<td>
                                <form:select id="<%=appendPrefix(BrokerListingReportData.FILTER_DIVISION_ID)%>" name="brokerListingForm" path="salesDivision" disabled="false" onchange="shortenRegionOptions();">
                                    <form:option value=""></form:option>
                                    <c:forEach items="${pageScope.salesDivisionList}" var="salesDivision">
                                        <form:option value="${salesDivision.id}" >${salesDivision.name}</form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        <%}%>
                    </tr>
                </table>
                <div class="selection_input">
                    <div class="button_search">
                        <input type="button" value="Reset" id="cancel_customization" onClick="resetBrokerListing(this.form);"/>
                    </div>
                    <div class="button_search">
                        <input type="button" value="Submit" id="apply_customization" onClick="submitBrokerListing(this.form);"/>
                    </div>
                </div>
            </bd:form>
        </div><!--#report_customization_wrapper-->
    </div><!--#bob_overview_table-->

    <bd:form method="POST" action="/do/brokerListing/" modelAttribute="brokerListingForm" name="brokerListingForm">
        <c:if test="${not empty theReport}">

            <c:if test="${not empty theReport.details}">
                <div class="table_controls">
                    <div class="table_action_buttons"></div>
                    <div class="table_display_info_abs">
                        <strong><report:recordCounter report="theReport" label="Records"/></strong>
                    </div>
                    <div class="table_pagination">
                        <strong><report:pageCounter arrowColor="black" report="theReport" formName="brokerListingForm"/> </strong>
                    </div>
                </div>
            </c:if>
        </c:if>

        <table class="report_table_content">
            <thead>
                <tr>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_FINANCIAL_REP_NAME_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_FIRM_NAME_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_FIRM_NAME_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_CITY_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_CITY_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_STATE_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_STATE_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_ZIP_CODE_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_ZIP_CODE_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_PRODUCER_CODE_ID%>" direction="asc" formName="brokerListingForm">
                                <%=BDConstants.COL_PRODUCER_CODE_TITLE %></report:sort>
                    </th>
                    <th class="val_str">
                        <report:sort field="<%=BrokerListingReportData.COL_NUM_OF_CONTRACTS_ID%>" direction="desc" formName="brokerListingForm">
                                <%=BDConstants.COL_NUM_OF_CONTRACTS_TITLE %></report:sort>
                    </th>
                    <th class="val_str align_center">
                        <report:sort field="<%=BrokerListingReportData.COL_BL_TOTAL_ASSETS_ID%>" direction="desc" formName="brokerListingForm">
                                <%=BDConstants.COL_BL_TOTAL_ASSETS_TITLE %></report:sort>
                    </th>
                </tr>
            </thead>
            <tbody>
              <c:if test="${not empty theReport}">
    
                <c:set var="singleQuot" value="\'"/>
                <c:set var="singleEscapedQuot" value="\\\'"/>
                
                
                <c:if test="${not empty theReport.details}">
												<c:forEach items="${theReport.details}" var="theItem"  varStatus="theIndex" >

													<c:set var="indexValue" value="${theIndex.index}"/>
													<c:if test="${indexValue}% 2 == 0">
														<tr >
														
													</c:if>
													<c:if test="${indexValue}% 2 != 0">
														<tr class="spec">
													</c:if>
                
                
                
 
                            <c:set var="financialRep" value="${fn:replace(theItem.financialRepName, singleQuot, singleEscapedQuot)}"/>
                            <td class="name"><a href="javascript://" onclick="gotoBOB('${financialRep}');return false;">${theItem.financialRepName}</a></td>
                            <td class="name">${theItem.firmName}</td>
                            <td class="name">
                                <c:if test="${empty theItem.city}">
                                    n/a
                                </c:if>
                                <c:if test="${not empty theItem.city}">
                                    ${theItem.city}
                                </c:if>
                            </td>
                            <td class="val_str">
                                <c:if test="${empty theItem.state}">
                                    n/a
                                </c:if>
                                <c:if test="${not empty theItem.state}">
                                    ${theItem.state}
                                </c:if>
                            </td>
                            <td class="name">
                                <c:if test="${empty theItem.zipCode}">
                                    n/a
                                </c:if>
                                <c:if test="${not empty theItem.zipCode}">
                                    ${theItem.zipCode}
                                </c:if>
                            </td>
                            <td class="name">${theItem.producerCode}</td>
                            <td class="name">
                                <c:if test="${not empty theItem.numOfContracts}">
                                    <report:number property="theItem.numOfContracts" type="i"/>
                                </c:if>
                            </td>
                            <td class="cur">
                                <c:if test="${empty theItem.totalAssets}">
                                    -
                                </c:if>
                                <c:if test="${not empty theItem.totalAssets}">
                                    <report:number property="theItem.totalAssets" type="c" sign="false"/>
                                </c:if>
                            </td>
                        </tr>
                   </c:forEach>
</c:if>
            </c:if>   
            </tbody>
        </table>        
        <%--.report_table_content--%>

        <%
        ArrayList<GenericException> infoMsgDisplayedUnderColHeader = (ArrayList<GenericException>) 
                        request.getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
		ArrayList<GenericException> errorMsgDisplayedUnderColHeader = (ArrayList<GenericException>) 
						request.getAttribute(BDConstants.ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        
        if (infoMsgDisplayedUnderColHeader != null && !infoMsgDisplayedUnderColHeader.isEmpty()) {
            request.setAttribute(BDConstants.INFO_MESSAGES, infoMsgDisplayedUnderColHeader);
        }
		if (errorMsgDisplayedUnderColHeader != null && !errorMsgDisplayedUnderColHeader.isEmpty()) {
		    String ERROR_KEY = ContentProperties.getInstance().getProperty(ContentCacheConstants.ERROR_KEY);
			request.setAttribute(ERROR_KEY, errorMsgDisplayedUnderColHeader);
		}
        %>
        <report:formatMessages scope="request"/>

      <c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
            <div class="table_controls">
                <div class="table_action_buttons"></div>
                <div class="table_display_info_abs">
                    <strong><report:recordCounter report="theReport" label="Records"/></strong>
                </div>
                <div class="table_pagination">
                    <strong><report:pageCounter arrowColor="black" report="theReport" formName="brokerListingForm"/> </strong>
                </div>
            </div>
</c:if>
        </c:if>
    </bd:form>
</div>
<!--.report_table-->

<div class="footnotes">
    <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
    <div class="footnotes_footer"></div>
</div> 