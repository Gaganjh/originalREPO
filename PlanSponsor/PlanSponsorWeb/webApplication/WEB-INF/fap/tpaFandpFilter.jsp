<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<mrtl:noCaching/>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<content:errors scope="request"/>
<ps:form modelAttribute="fapForm" name="fapForm" action="/do/fap/fapFilterAction/">
    
    <!-- some hidden attributes to set the form values -->
    <!-- TODO: Actually I need to get rid of this. Need to see a better way rather than using the hidden attributes -->
<input type="hidden" name="action" id="action" value="${fapForm.action}"/>
<input type="hidden" name="companyId" id="companyId" value="${fapForm.companyId}"/>
<input type="hidden" name="advanceFilterEnabled" id="advanceFilterEnabled" value="${fapForm.advanceFilterEnabled}"/>
<input type="hidden" name="showNML" id="showNML" value="${fapForm.showNML}"/>
<input type="hidden" name="selectedfundIds" id="selectedfundIds" value="${fapForm.selectedfundIds}"/>
<input type="hidden" name="filterKey" id="filterKey" value="${fapForm.filterKey}"/>
<input type="hidden" name="customQueryRowIndex" id="customQueryRowIndex" value="${fapForm.customQueryRowIndex}"/>
<input type="hidden" name="tabSelected" id="tabSelected" value="${fapForm.tabSelected}"/>
<input type="hidden" name="selectedAdvanceFilterOption" id="selectedAdvanceFilterOption" value="${fapForm.selectedAdvanceFilterOption}"/>
<input type="hidden" name="advanceFilterForIReports" id="advanceFilterForIReports" value="${fapForm.advanceFilterForIReports}"/>
<input type="hidden" name="queryResultsValues" id="queryResultsValues" value="${fapForm.queryResultsValues}"/>
<input type="hidden" name="selectedFundsValues" id="selectedFundsValues" value="${fapForm.selectedFundsValues}"/>
<input type="hidden" name="selectedAssetOrRiskValues" id="selectedAssetOrRiskValues" value="${fapForm.selectedAssetOrRiskValues}"/>
<input type="hidden" name="selectedCompanyName" id="selectedCompanyName" value="${fapForm.selectedCompanyName}"/>
<input type="hidden" name="overwriteExisting" id="overwriteExisting" value="${fapForm.overwriteExisting}"/>
<input type="hidden" name="eventTriggered" id="eventTriggered" value="${fapForm.eventTriggered}"/>
<input type="hidden" name="inMimic" id="inMimic" value="${fapForm.inMimic}"/>
<input type="hidden" name="selectedFundNames" id="selectedFundNames" value="${fapForm.selectedFundNames}"/>
<input type="hidden" name="asOfDate" id="asOfDate" value="${fapForm.asOfDate}"/>
<input type="hidden" name="selectedContractName" id="selectedContractName" value="${fapForm.selectedContractName}"/>
<input type="hidden" name="baseFilterSelect" id="baseFilterSelect" value="${fapForm.baseFilterSelect}"/>
<input type="hidden" name="showMorningstarScorecardMetrics" id="showMorningstarScorecardMetrics" value="true" />
<input type="hidden" name="showFi360ScorecardMetrics" id="showFi360ScorecardMetrics" value="true" />
<input type="hidden" name="showRpagScorecardMetrics" id="showRpagScorecardMetrics" value="true" />
    
    
    <table width="730" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <!-- Standard Filters section -->
            <div id="standardFlterSection">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="13%"  id="classFilterLabel">Class:</td>
                    <td width="22%">Group by:</td>
                    <td width="30%"></td>
                    <td width="27%">Reports and Downloads:</td>
                    <td width="6%"></td>
                </tr>
                <tr>
                    <td nowrap="nowrap" id="classFilter">
                        <form:select path="classSelect" id="classSelect">
<c:set var="classList" value="${fapForm.fundClassList}" /> 
                            <form:options items="${classList}" itemLabel="description" itemValue="code" />
</form:select>
                    </td>
                    <td nowrap="nowrap">
                        <form:select path="groupBySelect" id="groupBySelect">
<form:options items="${fapForm.groupByList}" itemLabel="label" itemValue="value"/>
</form:select>
                    </td>
                    <td nowrap="nowrap" width="270">
                        <input style="width:50px; font-size:11px;" id="applyButton" type="button" value="Apply"/>&nbsp;
                        <input style="width:150px; font-size:11px;" id="viewAdvancedBtn" type="button" value="Create Custom Fund List" />&nbsp
                         <span class="button_login" style="display: none" id=scorecardMetricsSelect>
	                            <input style="width:50px; font-size:11px;" id="scoreCardMetricsBtn" type="button" value="Scores" />
	                    </span>
                        
                    </td>
                    <td nowrap="nowrap">
                   
                        <form:select id="outputSelect" cssStyle="font-size:11px;width:220px;padding:0px;"  path="selectedReport">
<c:set var="reportList" value="${fapForm.reportList}" />
<c:forEach items="${fapForm.reportList}" var="report">
                            <logicext:if name="report" property="value" op="equal" value="N/A">
                                <logicext:or name="report" property="value" op="equal" value="" />
                                    <logicext:then>                                
                                        <form:option style="background:black;color:white;"  value="${report.value}">${report.key}</form:option>
                                    </logicext:then>
                                    <logicext:else>
                                        <form:option value="${report.value}">${report.key}</form:option>
                                    </logicext:else>
                                </logicext:if>
</c:forEach>
</form:select>
                    </td>
                    <td nowrap="nowrap">
                        <div id="goButton" style="display:none;margin:0px;">
                            <input id="viewReports" style="width:30px;font-size:11px;" type="button" value="Go"/>
                        </div>
                    </td>
                </tr>
                </table>
            </div>
            <!-- ## Standard Filters section -->
        </td>
    </tr>
    <tr>
        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
    </tr>
    <tr>
        <td>
            <!--  Advance Filters section -->
            <div class="advanceFilterSection" id="advancedX" style="display:none;">
            <table id="advancedFilters" width="100%" border="0">
            <tr>
                <td class="left_advanceFilterSection" style="padding-right:3px;">
               
                    <!-- Query Results Panel -->
                    <div id="queryResultsPanel" style="display:none;padding:0px;">
                        <table id="queryResults" border="0">
                        <tr style="height:26px;">
                            <td>
                                <div id="button_sort">
                                    <input type="button" value="Sort By" onclick="displayChangeSort()"/>
                                </div>
                                <span id="queryResultsLabel" class="left_align">Query Results:</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="fundlist_multi_select">
                                <select style="font-family:Courier New;" id="queryResultsSelect" class="fundlist_multi_select"  multiple="multiple"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table>
                                <tr>
                                    <td class="button_newQuery" align="left">
                                        <input class="btn_new" type="button" value="New Query" onclick="startNewQuery()"/>
                                    </td>
                                    <td class="button_newQuery" align="left">
                                        <div id="btn_editQuery">
                                            <input type="button" value="Edit Criteria" onclick="doEditCriteria()"/>
                                        </div>
                                    </td>
                                    <td class="button_addAll" align="right">
                                        <input type="button" onclick="copyQueryFundsToSelectedFunds('all')" value="Add All &gt;&gt;"/>
                                    </td>
                                    <td class="button_add" align="right">
                                        <input type="button" onclick="copyQueryFundsToSelectedFunds()" value="Add &gt;"/>
                                    </td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ## Query Results Panel -->

                    <!-- Query Selection Panel -->
                    <div id="querySelectionPanel">
                        <table border="0">
                        <tr>
                            <td width="80" valign="top" style="font-size:12">Select a query:</td>
                            <td valign="top">
                                    <form:select path="optionalFilterSelect" id="optionalFilterSelect"></form:select>
                                    <br/>
                                    <div id="closedFunds" style="display:none">
<form:checkbox path="includeClosedFunds" id="includeClosedFunds" value="true"/>
                                        Include Closed Funds 
                                    </div>
                                    <div id="signaturePlusFunds" style="display:none">
<form:checkbox path="includeOnlySigPlusFunds" id="includeOnlySigPlusFunds" value="true"/> Display only SIG+ Funds
                                    </div>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ## Query Selection Panel -->

                    <!-- viewResultsButtonPanel:This panel will be enabled for All Available Funds, Retail funds, sub-Advised Funds, 
                                                Funds available to Contract, Funds selected to Contract -->
                    <div id="viewResultsButtonPanel" class="query_panel" >
                        <table>
                        <tr>
                            <td> <br/>
                                <input id="btnViewResults" type="button" value="View Results"/>
                            </td>
                        </tr>
                        </table>
                    </div>
                
                    <!-- Asset Class Funds Query Panel - This panel will be enabled for the Asset Class filter option -->
                    <div id="assetClassFundsQueryPanel" class="query_panel" style="display:none">
                        <table>
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">Select Asset Classes:</span><br/><br/>
                                <form:select path="assetClassQuerySelect" id="assetClassQuerySelect" cssStyle="height:140px;font-size:10"  multiple="multiple"></form:select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span style="font-size:10px;">(Hold &lt;Ctrl&gt; to select multiple items)</span><br/><br/>
                                <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                <input type="button" id="btnViewAssetClassResults" value="View Results" class="btn_view_results"/>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ## Asset Class Funds Query Panel -->

                    <!-- Risk Category Query Panel - This panel will be enabled for the Risk Category filter option -->
                    <div id="riskCategoryQueryPanel" class="query_panel" style="display:none">
                        <table>
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">Select Risk/Return Categories:</span><br/><br/>
                                <form:select path="riskCategoryQuerySelect" id="riskCategoryQuerySelect" cssStyle="height:140px;width:200px;font-size:11px" multiple="multiple"></form:select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span style="font-size:10px;">(Hold &lt;Ctrl&gt; to select multiple items)</span><br/><br/>
                                <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                <input id="btnViewRiskCatResults" type="button" value="View Results"/>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ##Risk Category Query Panel -->


                    <!-- Shortlist Funds Query Panel - This panel will be enabled for the Shortlist funds filter option -->
                    <div id="shortlistFundsQueryPanel" class="query_panel" style="display:none">
                        <table>
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">Build a shortlist:</span><br/>
                                <table cellpadding="0" cellspacing="5">
                                <tr>
                                    <td width="150" align="right">Shortlist Type:</td>
                                    <td>
                                        <form:select path="shortlistTypeSelect" id="shortlistTypeSelect" cssStyle="width:180px;"></form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                                </tr>
                                <tr>
                                    <td align="right">Fund Menu:</td>
                                    <td>
                                        <form:select path="shortlistFundMenuSelect" id="shortlistFundMenuSelect"  cssStyle="width:180px;"></form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                                </tr>
                                <tr>
                                    <td width="150" align="right">Asset Allocation Group:</td>
                                    <td>
                                        <form:select path="allocationGroupSelect" id="allocationGroupSelect" cssStyle="width:180px;"></form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                                </tr>
                                <tr>
                                    <td width="150" align="right">Conservative Fund:</td>
                                    <td>
                                        <form:select path="conservativeFundSelect" id="conservativeFundSelect" cssStyle="width:180px;"></form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                <input type="button" id="btnViewShortlistResults" value="View Results"/>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ### Shortlist Funds Query Panel -->

                    <!-- Custom Query Panel -->
                    <div id="customQueryPanel" class="query_panel" style="display:none">
                        <!-- The content will be populated dymanically once the user choose the 'Custom Query' Option -->
                        <!-- The design is implemented in the -->
                    </div>
                    <!-- ### Custom Query Panel -->
                    
                    <!-- Fund Name Query Panel - This panel will be enabled for the Fund Name Search filter option -->
                    <div id="fundNameQueryPanel" class="query_panel" style="display:none">
                        <table>
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">Search for Fund(s) by Name:</span><br/><br/>
<form:input path="fundNameSearchText" maxlength="30" cssStyle="width:225;" id="fundNameSearchText" />
                            </td>
                        </tr>
                        <tr>
                            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                        </tr>
                        <tr>
                            <td>
                                <input class="btn_cancel" type="button" value="New Query" onclick="startNewQuery()"/>
                                <input type="button" id="btnViewFundNameResults" value="View Results"/>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ### Fund Name Query Panel -->


                    <!-- My Saved List Query Panel -->
                    <div id="savedListsQueryPanel" class="query_panel" style="display:none">
                        <table>
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">My Saved Lists of Funds:</span><br/><br/>
                                <form:select path="mySavedListsQuerySelect" id="mySavedListsQuerySelect" cssStyle="width:250px" onchange="enableOrDisableDeleteButton(this, 'deleteList')"></form:select>
                            </td>
                        </tr>
                        <tr>
                            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                        </tr>
                        <tr>
                            <td>
                                <input type="button" value="New Query" onclick="startNewQuery()"/>
                                <input type="button" value="View Results" onclick="doMySavedData('displayList')"/>
                                
                                <!-- internal user are not allowed to delete -->
<c:if test="${userProfile.internalUser !=true}">
                                    <input type="button" id="deleteList" value="Delete" disabled="disabled" onclick="doMySavedData('deleteList')"/>
</c:if>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ### My Saved List Query Panel -->

                    <!-- My Saved Custom Queries Panel -->
                    <div id="savedQueriesQueryPanel" class="query_panel" style="display:none">
                        <table border="0">
                        <tr>
                            <td>
                                <span class="left_align" style="font-size:12">My Saved Custom Queries:</span><br/><br/>
                                <form:select path="mySavedQueriesQuerySelect" id="mySavedQueriesQuerySelect" cssStyle="width:300px" onchange="enableOrDisableDeleteButton(this, 'deleteQuery')"></form:select>
                            </td>
                        </tr>
                        <tr>
                            <td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
                        </tr>
                        <tr>
                            <td>
                                <input type="button" value="New Query" style="width:80px" onclick="startNewQuery()"/>
                                <input type="button" value="View Results" style="width:80px" onclick="doMySavedData('displayQueryResults')"/>
                                <input type="button" value="Edit Criteria" style="width:80px" onclick="doMySavedData('editCriteria')"/>
                                
                                <!-- internal users are not allowed to delete -->
<c:if test="${userProfile.internalUser !=true}">
                                    <input type="button" id="deleteQuery" style="width:80px" value="Delete Query" disabled="disabled" onclick="doMySavedData('deleteQuery')"/>
</c:if>
                            </td>
                        </tr>
                        </table>
                    </div>
                    <!-- ### My Saved Custom Queries Panel -->
                </td>
                
                <!-- Selected fund list section-->
                <td class="right_advanceFilterSection" style="padding-left:3px;">
                    <div id="selectedFundsPanel">
                        <table id="selectedFunds" border="0">
                        <tr style="height:26px;">
                            <td>
                                <table width="100%" border="0"> <tr>
                                    <td><span class="left_align" style="font-size:12">Selected funds:</span></td>
                                    <td align="right">
                                        <a href="javascript://" onClick="doDisplayAssetClass();" >View the asset class definitions</a>
                                    </td>
                                </tr></table>
                            </td>
                        </tr>
                        <tr>
                            <td class="fundlist_multi_select" >
                                <select id="selectedFundsSelect" style="font-family:Courier New;" class="fundlist_multi_select" multiple="multiple"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
<c:if test="${userProfile.internalUser !=true}">
                                <div id="saveListSection" style="display:none;"> 
                                <table style="margin:0px;collapse-borders:collapse;" border="0">
                                <tr>
                                    <!-- Text box to enter the file name -->
                                    <td style="padding:0px;" >
                                        Save this list of funds as:
<form:input path="saveListName" maxlength="25" cssStyle="font-size:11px;width:90px;" id="saveListName" />&nbsp;&nbsp;
                                    </td>
                        
                                    <!-- Save button -->            
                                    <td style="padding:0px;">
                                        <input type="button" value="Save"  id="saveListButton" style="font-size:11px;width:45px;" onclick="doSaveUserData(false)" />
                                        <input type="button" value="Cancel"  style="font-size:11px;width:45px;" onclick="hide('saveListSection')"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        Saved funds lists will appear under "My Saved Lists". 
                                    </td>
                                </tr>
                                </table>
                                </div>
</c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size">
                                <table class="full_size" border="0">
                                <tr>
                                    <td style="width:80px;padding:0px;">
                                        <input id="btnRemove" class="btn_remove" type="button" value="&lt; Remove" onclick="removeSelectedFunds()"/>
                                    </td>
                                    <td style="width:90px;padding:0px;">
                                        <input id="btnRemoveAll" class="btn_remove_all" type="button" value="&lt;&lt; Remove All" onclick="removeSelectedFunds('all')"/>
                                    </td>
                                    <td style="padding:0px;">
                                        &nbsp;
                                    </td>
                                    <td style="width:130px;padding:0px;" align="right">
<c:if test="${userProfile.internalUser !=true}">
                                            <input id="btnSaveList" class="btn_save_list" type="button" value="Save this list" onclick="enableSaveFundListSection('saveListSection')"/>
</c:if>
                                    </td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        </table>
                    </div>
                </td>
            </tr>
            
            <tr>
                <td colspan="2" align="center">
                    <table border="0">
                    <tr>
                        <td style="text-align:center;vertical-align:middle;">                        
                            <input id="applyBelowButton" type="button" value="Apply Selected Funds to Reports & Tables" title="Apply selected funds to tables and reports."/>
                        </td>                        
                    </tr>
                    </table>
                </td>
            </tr>
            </table>
            </div>
        </td>
    </tr>
    </table>
    
    <!-- modal layout for the sort option-->
    <div id="modalGlassPanel" class="modal_glass_panel" style="display:none"></div>
    <!-- id = changeSortOrder -->
    <div class="modal_dialog change_sort_dialog" id="changeSortX" style="display:none;">
        <div class="dialog_title">&nbsp;&nbsp;<b>Change Sort Order:</b></div><br/>
        <div class="dialog_content">
            This setting allows you to changes the sort order for the selection boxes 
            used to customize the fund lists.<br/><br/> It does not change the sort order 
            of the final report output.<br/><br/>
            Sort funds:
 <form:select path="sortPreferenceSelect" id="sortPreferenceSelect" cssStyle="width:330px;" >

                <form:option value="filterRiskCategoryFunds">Risk/Return</form:option>
                <form:option value="filterAssetClassFunds">Asset Class</form:option>
                <form:option value="fundName">Alphabetically by Fund Name</form:option>
</form:select><br/><br/>
            <table>
                <tr><td>
                    <input style="width:100px; font-size:11px;" id="cancelSortButton" type="button" 
                    onclick="cancelSort()" value="Cancel"/>
                </td></tr>
            </table>
        </div>
    </div>

    <div class="modal_dialog change_sort_dialog" id="additionalParamSection" style="display:none;">
    </div>

     <div class="modal_dialog change_sort_dialog" id="termsAndCondition" style="display:none;width:450px; height:160px;">
     <br><br>
     <div class="dialog_content">
     <input type="checkbox"  id="acceptTermsAndCondition"  onclick="acceptTermAndCondition()" />
      <content:contentBean contentId="${contentConstants.FUND_EVALUATOR_TERMS_AND_CONDITION}"
                           type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="termsAndCondition"/>
      <content:getAttribute attribute="text" beanName="termsAndCondition"/>
      </div>
    <table>
    <tr>
        <td>
        <div style="float: left;">
             <input style="font-size: 11px;width:100px;" id="showAdvanceFilterButton" disabled="disabled" type="button" value="OK" onclick="showAdvanceFilter()"/>
        </div>
        <div style="float: left; padding-left: 15px">
            <input style="width: 100px; font-size: 11px;"  type="button" value="CANCEL" onclick="cancelAdvanceFilter()"/>
       </div>
        </td>
    </tr>
    </table>
    </div>
    
    <div class="modal_dialog change_sort_dialog" id="fundScoreCardSelection"
		 style="display: none; width: 350px; height: 170px;">

		<div class="dialog_title">
			&nbsp;&nbsp;<b>Customize JH Signature Fund Scorecard : </b>
		</div>
		<br>
		<div class="dialog_content">
			 <input type="checkbox"  id="morningStarCheckBox" value="true" checked="checked"
				    name="morningStarCheckBox" onclick="validateMetricSelection()" />
			    Display Morningstar Metrics <br>
			 <input type="checkbox"  id="fi360CheckBox" value="true" checked="checked"
				    name="fi360CheckBox" onclick="validateMetricSelection()" />
			    Display Fi360 Metrics <br>
			 <input type="checkbox"  id="rpagCheckBox" value="true" checked="checked"
				    name="rpagCheckBox" onclick="validateMetricSelection()" />
			    Display RPAG Metrics
			    
			<br/>   <br/>  
						    
			<table>
			<tr>
				<td>
					<span  class="button_login">
						<input style="font-size: 11px; width: 100px;"
							id="fundScorecardMetricsConfirmationButton" type="button" value="OK"
							onclick="proceedWithMetricsSelection(
									document.getElementById('morningStarCheckBox').checked,
									document.getElementById('fi360CheckBox').checked,
									document.getElementById('rpagCheckBox').checked)" />
					</span>
					<span  class="button_login">
						<input style="width: 100px; font-size: 11px;" type="button"
							value="CANCEL" onclick="cancelMetricsSelection()" />
					</span>
				</td>
			</tr>
		    </table> 
		</div>
	</div>
   
    </ps:form>
