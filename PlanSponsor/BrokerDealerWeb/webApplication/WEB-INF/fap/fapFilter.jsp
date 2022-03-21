<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<mrtl:noCaching/>

<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
<content:contentBean contentId="${bdContentConstants.FUND_EVALUATOR_TERMS_AND_CONDITION}"
                           type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="termsAndCondition"/>
    <!--      
<script>
	$(document).ready(function() {
		$(window).load(function() {
			$("#applyButton").click(function() {
	  var isMerrillContract = ${userProfile.isMerrillContract()};
				if (isMerrillContract) {
					$("#restrictedFundsContent").show();
				} else {
					$("#restrictedFundsContent").hide();
				}
			});
		});
	});
</script> -->
                           
<bd:form action="/fap/fapFilterAction/" modelAttribute="fapForm" name="fapForm" >
    
    <!-- some hidden attributes to set the form values -->
    <!-- TODO: Actually I need to get rid of this. Need to see a better way rather than using the hidden attributes -->
<input type="hidden" name="action" id="action" value="${fapForm.action}"/>
<input type="hidden" name="companyId" id="companyId" value="${fapForm.companyId}"/>
<input type="hidden" name="advanceFilterEnabled" id="advanceFilterEnabled" value="${fapForm.advanceFilterEnabled}"/>
<input type="hidden" name="showNML" id="showNML" value="${fapForm.showNML}"/>
<input type="hidden" name="showML" id="showML" value="${fapForm.showML}"/>
<input type="hidden" name="cofidDistChannel" id="cofidDistChannel" value="${fapForm.cofidDistChannel}"/>
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
<input type="hidden" name="overwriteExisting" id="overwriteExisting"  value="${fapForm.overwriteExisting}"/>
<input type="hidden" name="eventTriggered" id="eventTriggered" value="${fapForm.eventTriggered}"/>
<input type="hidden" name="inMimic" id="inMimic" value="${fapForm.inMimic}"/>
<input type="hidden" name="selectedFundNames" id="selectedFundNames" value="${fapForm.selectedFundNames}"/>
<input type="hidden" name="asOfDate" id="asOfDate" value="${fapForm.asOfDate}"/>
<input type="hidden" name="selectedContractName" id="selectedContractName" value="${fapForm.selectedContractName}"/>
<input type="hidden" name="contractMode" id="contractMode" value="false" value="${fapForm.contractMode}"/>
<input type="hidden" name="displayOnlyHeaders" id="displayOnlyHeaders" value="false" value="${fapForm.displayOnlyHeaders}"/>
<input type="hidden" name="showMorningstarScorecardMetrics" id="showMorningstarScorecardMetrics" value="true"/>
<input type="hidden" name="showFi360ScorecardMetrics" id="showFi360ScorecardMetrics" value="true"/>
<input type="hidden" name="showRpagScorecardMetrics" id="showRpagScorecardMetrics" value="true"/>

<c:if test="${fapForm.contractMode ==true}">
<input type="hidden" name="baseFilterSelect" id="baseFilterSelect" value="${fapForm.baseFilterSelect}"/>
</c:if>

    <%-- Filter Options --%>
    <table width="920" id="funds_and_performance_filters" border="0">
    <!-- START: Advanced Options Table -->
     <tr>
        <td>
            <div id="filter_wrapper">
            <div id="filter">
                <!-- Base Filter options-->
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
<c:if test="${fapForm.contractMode !=true}">
                    <td width="10%" align="left" valign="bottom" nowrap="nowrap">
                        View:<br/>
                        <form:select path="baseFilterSelect" cssStyle="width:100px;" onchange="viewFundsBy(this)" id="baseFilterSelect">
                             <form:options items="${fapForm.baseFilterList}"  itemLabel="label" itemValue="value"/>
                        </form:select>
                    </td>
                    <td width="8%" align="left" valign="bottom" nowrap="nowrap" id="classContractFilter">
                        <div id="classEntry" style="font-size: 11px">
                            Class:<br/> 
                            <form:select path="classSelect" id="classSelect">
                                <form:options items="${fapForm.fundClassList}" itemLabel="description" itemValue="code" />
                            </form:select>
                        </div>
                        <div id="contractEntry" style="display:none;font-size: 11px;"> 
                            Contract:<br />
<form:input path="contractSearchText" maxlength="30" cssStyle="width:60px;" id="contractSearchText"/>
                        </div>
                    </td>
</c:if>

<c:if test="${fapForm.contractMode ==true}">
                    <td width="20%" align="left" valign="bottom" nowrap="nowrap">
                        Contract:<br />
<form:input path="contractSearchText" cssStyle="width:100px;" id="contractSearchText"/>
                    </td>
</c:if>

                    <td width="10%" align="left" valign="bottom" nowrap="nowrap">
                        Group by:<br />
                        <form:select path="groupBySelect" id="groupBySelect">
<form:options items="${fapForm.groupByList}" itemLabel="label" itemValue="value"/>
</form:select>
                    </td>
                    <td width="45%" align="left" valign="bottom" nowrap="nowrap">
                        <br />
                        <div class="button_login button_fap">
                            <input style="width:45px; font-size:11px;" id="applyButton" type="button" value="Apply"/>
                        </div>
                       <div class="button_login button_fap">
                            <input style="width:150px; font-size:11px;" id="viewAdvancedBtn" type="button" value="Create Custom Fund List" />
                        </div>
	                    <div class="button_login button_fap" style="display: none" id=scorecardMetricsSelect>
	                            <input style="width:46px; font-size:11px;" id="scoreCardMetricsBtn" type="button" value="Scores" />
	                    </div>
                    </td>
                    <td width="24%" align="left" valign="bottom" nowrap="nowrap">
                        <div id="reportsAndDownloads">
                        Reports and Downloads:<br />
                        <form:select id="outputSelect" cssStyle="font-size:11px;width:220px;padding:0px;"  path="selectedReport">
<c:set var="reportList" value="${fapForm.reportList}" />
<c:forEach items="${fapForm.reportList}" var="report">
                               <c:choose>
                               <c:when test="${report.value eq 'N/A' or  report.value eq ''}">
                               <form:option style="background:black;color:white;"  value="${report.value}">${report.key}</form:option>
                               </c:when>
                               <c:otherwise>
                               <form:option value="${report.value}">${report.key}</form:option>
                               </c:otherwise>
                               </c:choose>
</c:forEach>
</form:select>
                        </div>
                    </td>
                    <td  width="6%" style="padding-right:0px;padding-left:3px;padding-top:15px;" nowrap="nowrap">
                        <div id="goButton" class="button_login" style="display:none;margin:0px;">
                            <input id="viewReports" style="width:25px;font-size:11px;" type="button" value="Go"  />
                        </div>
                    </td>
                </tr>
                </table>
                <!-- ## Base Filter options -->
            </div>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <!-- This panel is for listing the contracts in a pop-up -->
            <!-- id = fuzzySearchX for Contract-->
            <div id="modalGlassPanel" class="modal_glass_panel" style="display:none"></div>
            <div class="modal_dialog contact_search_dialog" id="fuzzySearchX" style="display:none;">
                
                <div class="dialog_title">&nbsp;&nbsp;<b>Select Contract:</b></div>
                <br/>
                
                <div class="dialog_content">
                    <div id="contractSearchResultMessage"></div>
                    <br/>
                    <form:select path="contractSelect" id="contractSelect" cssStyle="width:330px;"></form:select><br/>
                    <table>
                    <tr>
                        <td>
                            <div class="button_login">
                                <input style="width:100px; font-size:11px;" id="cancelContractSelectButton" type="button" onclick="contractDialogAction('cancel');" value="Cancel"/>
                            </div>
                        </td>
                        <td>
                            <div class="button_login">
                                <input style="width:100px; font-size:11px;" id="applyContractSelectButton" type="button" value="Ok"/>
                            </div>
                        </td>
                    </tr>
                    </table>
                 </div>
            </div>


            <div class="form_section" id="advancedX" class="full_size" style="display:none;">
            <table class="advanced_filters" id="advancedFilters" width="100%" border="0">
            <tr>
                <td class="left_panel" style="padding-right:3px;">
                <!-- queryResultsPanel -->
                    <div id="queryResultsPanel" class="query_results_panel" style="display:none;padding:0px;">
                        <table id="queryResults" class="full_size">
                        <tr style="height:30px;">
                            <td style="padding-top:8px;font-size:12px;">
                                <div id="btn_sort" class="button_search" style="float:right;margin-top:2px;margin-bottom:0px;margin-right:0px;">
                                    <input type="button" value="Sort By" onclick="displayChangeSort()"/>
                                </div>
                                <span id="queryResultsLabel" class="left_align">Query Results:</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="fundlist_multi_select">
                            <fieldset>
                                <select style="font-family:Courier New;" id="queryResultsSelect" class="fundlist_multi_select"  multiple="multiple">
                                </select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="full_size">
                                <table class="full_size">
                                <tr>
                                    <td style="width:85px;padding:0px;">
                                        <div class="button_search" id="btn_newQuery">
                                            <input class="btn_new" type="button" value="New Query" onclick="startNewQuery()"/>
                                        </div>
                                    </td>
                                    <td style="width:95px;padding:0px;">
                                        <div class="button_search" id="btn_editQuery">
                                            <input class="btn_edit" type="button" value="Edit Criteria" onclick="doEditCriteria()" style="width:85px;"/>
                                        </div>
                                    </td>
                                    <td width="*"></td>
                                    <td style="width:110px;padding:0px;">
                                        <div class="button_search">
                                            <input class="btn_add_all" type="button" onclick="copyQueryFundsToSelectedFunds('all')" value="Add All &gt;&gt;"/>
                                        </div>
                                    </td>
                                    <td style="width:100px;padding:0px;">
                                        <div class="button_search" style="margin-right:0px;">
                                            <input class="btn_add" type="button" onclick="copyQueryFundsToSelectedFunds()" value="Add &gt;"/>
                                        </div>
                                    </td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- querySelectionPanel -->
                    <div id="querySelectionPanel" class="query_selection_panel" >
                        <table class="full_size">
                        <tr>                            
                            <td colspan="2" style="padding-top:9px;"><span class="left_align" style="font-size:12px">Build a customized fund list:</span></td>                            
                        </tr>
                        <tr>
                            <td width="8">&nbsp;</td>
                            <td valign="bottom">
                                <fieldset>
                                    <form:select path="optionalFilterSelect" id="optionalFilterSelect"></form:select>
                                    <br/>
<c:if test="${fapForm.contractMode !=true}">
                                    <div id="includeNMLSection" style="display:none">
<form:checkbox path="includeNML" id="includeNML" value="true"/> Include NML
                                    </div>
                                    <div id="closedFunds" style="display:none">
<form:checkbox path="includeClosedFunds" id="includeClosedFunds" value="true"/> Include Closed Funds
                                    </div>
                                    <div id="signaturePlusFunds" style="display:none">
<form:checkbox path="includeOnlySigPlusFunds" id="includeOnlySigPlusFunds" value="true"/> Display only SIG+ Funds
                                    </div>
</c:if>
                                </fieldset>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- viewResultsButtonPanel -   This panel will be enabled for All Available Funds, Retail funds, sub-Advised Funds, 
                                                    Funds available to Contract, Funds selected to Contract -->
                    <div id="viewResultsButtonPanel" class="query_panel" >
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls"></td>
                        </tr>
                        <tr>
                            <td class="full_size align_right">
                                <div>
                                    <input id="btnViewResults" class="greenViewResults" type="button" value="View Results" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'" />
                                </div>
                            </td>
                        </tr>
                        </table>
                    </div>
                
                    <!-- assetClassFundsQueryPanel - This panel will be enabled for the Asset Class filter option -->
                    <div id="assetClassFundsQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls">
                                <span class="left_align">Select Asset Classes:</span>
                                <br/>
                                <fieldset style="margin-bottom:0px;">
                                    <form:select path="assetClassQuerySelect" id="assetClassQuerySelect" cssStyle="height:140px;font-size:11"  multiple="multiple">
<</form:select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span style="font-size:10px;">Hold &lt;CTRL&gt; to select multiple items</span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="button_search">
                                    <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input type="button" id="btnViewAssetClassResults" value="View Results" class="greenViewResults" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- riskCategoryQueryPanel - This panel will be enabled for the Risk Category filter option -->
                    <div id="riskCategoryQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls">
                                <span class="left_align">Select Risk/Return Categories:</span>
                                <br/>
                                <fieldset>
                                    <form:select path="riskCategoryQuerySelect" id="riskCategoryQuerySelect" cssStyle="height:140px;width:200px;font-size:11px" multiple="multiple">
<</form:select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span style="font-size:10px;">Hold &lt;CTRL&gt; to select multiple items</span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="button_search">
                                    <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input id="btnViewRiskCatResults" type="button" value="View Results" class="greenViewResults" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
                             </td>
                         </tr>
                        </table>
                    </div>

                    <!-- shortlistFundsQueryPanel - This panel will be enabled for the Shortlist funds filter option -->
                    <div id="shortlistFundsQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls" ><span class="left_align">Build a shortlist:</span><br/>
                                <table class="full_size">
                                <tr>
                                    <td width="150" align="right">Shortlist Type:</td>
                                    <td>
                                        <fieldset style="padding-top:1px;padding-bottom:1px;">
                                            <form:select path="shortlistTypeSelect" id="shortlistTypeSelect" cssStyle="width:180px;">
<</form:select>
                                        </fieldset>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">Fund Menu:</td>
                                    <td>
                                        <fieldset style="padding-top:1px;padding-bottom:1px;">
                                            <form:select path="shortlistFundMenuSelect" id="shortlistFundMenuSelect"  cssStyle="width:180px;">
<</form:select>
                                        </fieldset>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="150" align="right">Asset Allocation Group:</td>
                                    <td>
                                        <fieldset style="padding-top:1px;padding-bottom:1px;">
                                            <form:select path="allocationGroupSelect" id="allocationGroupSelect" cssStyle="width:180px;">
<</form:select>
                                        </fieldset>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="150" align="right">Conservative Fund:</td>
                                    <td>
                                        <fieldset style="padding-top:1px;padding-bottom:1px;">
                                            <form:select path="conservativeFundSelect" id="conservativeFundSelect" cssStyle="width:180px;">
<</form:select>
                                        </fieldset>
                                    </td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size align_right">
                                <div class="button_search">
                                    <input type="button" name="btnNewQuery" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input type="button" id="btnViewShortlistResults" value="View Results" class="greenViewResults" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- customQueryPanel -->
                    <div id="customQueryPanel" class="query_panel" style="display:none">
                        <!-- The content will be populated dymanically once the user choose the 'Custom Query' Option -->
                        <!-- The design is implemented in the -->
                    </div>

                    
                    <!-- fundNameQueryPanel - This panel will be enabled for the Fund Name Search filter option -->
                    <div id="fundNameQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls">
                                <span class="left_align">Search for Fund(s) by Name:</span>
                                <br/>
                                <fieldset>
<form:input path="fundNameSearchText" maxlength="30" cssStyle="width:225px;" id="fundNameSearchText" />
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size align_right">
                                <div class="button_search">
                                    <input class="btn_cancel" type="button" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input type="button" id="btnViewFundNameResults" value="View Results" class="greenViewResults" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- savedListsQueryPanel -->
                    <div id="savedListsQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings">
                        <tr>
                            <td class="query_controls">
                                <span class="left_align">My Saved Lists of Funds:</span>
                                <br/>
                                <fieldset>
                                    <form:select path="mySavedListsQuerySelect" id="mySavedListsQuerySelect" cssStyle="width:320px" onchange="enableOrDisableDeleteButton(this, 'deleteList')">
<</form:select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size align_right">
                                <div class="button_search">
                                    <input class="btn_cancel" type="button" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input class="greenViewResults" type="button" value="View Results" onclick="doMySavedData('displayList')" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
<c:if test="${fapForm.inMimic !=true}">
                                    <div class="button_search">
                                        <input class="btn_view_results" type="button" id="deleteList" value="Delete" disabled="disabled" onclick="doMySavedData('deleteList')"/>
                                    </div>
</c:if>
                            </td>
                        </tr>
                        </table>
                    </div>

                    <!-- savedQueriesQueryPanel -->
                    <div id="savedQueriesQueryPanel" class="query_panel" style="display:none">
                        <table class="full_size query_settings" border="0">
                        <tr>
                            <td class="query_controls">
                                <span class="left_align">My Saved Custom Queries:</span>
                                <br/>
                                <fieldset>
                                    <form:select path="mySavedQueriesQuerySelect" id="mySavedQueriesQuerySelect" cssStyle="width:300px" onchange="enableOrDisableDeleteButton(this, 'deleteQuery')" >
<</form:select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size align_right">
                                <div class="button_search">
                                    <input class="btn_new" type="button" value="New Query" onclick="startNewQuery()"/>
                                </div>
                                <div class="button_green">
                                    <input class="greenViewResults" type="button" value="View Results" onclick="doMySavedData('displayQueryResults')" onmouseover="this.className +=' hover'" onmouseout="this.className='greenViewResults'"/>
                                </div>
                                <div class="button_search">
                                    <input class="btn_edit" type="button" value="Edit Criteria" onclick="doMySavedData('editCriteria')"/>
                                </div>
<c:if test="${fapForm.inMimic !=true}">
                                <div class="button_search">
                                    <input class="btn_delete" type="button" id="deleteQuery" value="Delete Query" disabled="disabled" onclick="doMySavedData('deleteQuery')"/>
                                </div>
</c:if>
                            </td>
                        </tr>
                        </table>
                    </div>
                </td>
                
                <!-- Selected fund list section-->
                <td class="right_panel" style="padding-left:3px;">
                    <div id="selectedFundsPanel" class="selected_funds_panel">
                        <table id="selectedFunds" class="full_size" border="0">
                        <tr style="height:30px;">
                            <td>
                                <table class="full_size" border="0"> <tr>
                                    <td><span class="left_align" style="font-size:12">Selected funds:</span></td>
                                    <td align="right">
                                        <a href="javascript://" onClick="doDisplayAssetClass();" >View the asset class definitions</a>
                                    </td>
                                </tr></table>
                            </td>
                        </tr>
                        <tr>
                            <td class="fundlist_multi_select" >
                                <fieldset>
                                    <select id="selectedFundsSelect" style="width:100%;font-family:Courier New;" class="fundlist_multi_select" multiple="multiple"></select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
<c:if test="${fapForm.contractMode !=true}">
<c:if test="${fapForm.inMimic !=true}">
                                <div id="saveListSection" style="background-color:#E0E0E0;width:400px;font-size:11px;border:1px solid #606060;padding:3px;margin-left:5px;display:none;z-index:80;"> 
                                <table style="margin:0px;collapse-borders:collapse;" >
                                <tr>
                                    <!-- Text box to enter the file name -->
                                    <td style="padding:0px;" nowrap="nowrap">
                                        Save this list of funds as:
                                    
<form:input path="saveListName" maxlength="25" cssStyle="font-size:11px;width:90px;" id="saveListName" />&nbsp;&nbsp;
                                    </td>
                        
                                    <!-- Save button -->            
                                    <td style="padding:0px;" nowrap="nowrap">
                                        <div class="button_search" style="align:right;">
                                            <input type="button" value="Save"  id="saveListButton" style="font-size:11px;width:45px;" onclick="doSaveUserData(false)" />
                                        </div>
                                        <div class="button_search" style="align:right;">
                                           <input type="button" value="Cancel"  style="font-size:11px;width:45px;" onclick="hide('saveListSection')"/>
                                        </div>
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
</c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="full_size">
                                <table class="full_size" border="0">
                                <tr>
                                    <td style="width:110px;padding:0px;">
                                        <div class="button_search">
                                            <input id="btnRemove" class="btn_remove" type="button" value="&lt; Remove" onclick="removeSelectedFunds()"/>
                                        </div>
                                    </td>
                                    <td style="width:110px;padding:0px;">
                                        <div class="button_search">
                                            <input id="btnRemoveAll" class="btn_remove_all" type="button" value="&lt;&lt; Remove All" onclick="removeSelectedFunds('all')"/>
                                        </div>
                                    </td>
                                    <td style="padding:0px;">
                                        &nbsp;
                                    </td>
                                    <td style="width:130px;padding:0px;">
<c:if test="${fapForm.contractMode !=true}">
<c:if test="${fapForm.inMimic !=true}">
                                        <div id="saveThisListSection" class="button_search" style="margin-right:0px;">
                                            <input id="btnSaveList" class="btn_save_list" type="button" value="Save this list" onclick="enableSaveFundListSection('saveListSection')"/>
                                        </div>
</c:if>
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
                <td colspan="2">
                    <div id="showLegendTextId" style="display:none;text-align:center;">
                        <script>document.write(unescape("%u221A"))</script> - Funds selected by the contract
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <table class="full_size" border="0">
                    <tr>
                        <td style="text-align:center;vertical-align:middle;">
                            <span id="advancedFilterButton">
                                <input id="applyBelowButton" class="fap-apply-filter-button" onmouseover="this.className +=' hover'" onmouseout="this.className='fap-apply-filter-button'" type="button" value="Apply Selected Funds to Reports &amp; Tables" title="Apply selected funds to tables and reports."/>
                            </span>
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
<</form:select><br/><br/>
            <table>
                <tr><td><div class="button_login">
                    <input style="width:100px; font-size:11px;" id="cancelSortButton" type="button" 
                    onclick="cancelSort()" value="Cancel"/>
                </div></td></tr>
            </table>
        </div>
    </div>

    <div class="modal_dialog change_sort_dialog" id="additionalParamSection" style="display:none;">
    </div>

    <div class="modal_dialog change_sort_dialog" id="termsAndCondition" style="display:none;width:450px; height:160px;">
     <br><br>
     <div class="dialog_content">
     <input type="checkbox"  id="acceptTermsAndCondition"  onclick="acceptTermAndCondition()" />
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
		 style="display: none; width: 400px; height: 200px;">

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
					<div  class="button_login">
						<input style="font-size: 11px; width: 100px;"
							id="fundScorecardMetricsConfirmationButton" type="button" value="OK"
							onclick="proceedWithMetricsSelection(
									document.getElementById('morningStarCheckBox').checked,
									document.getElementById('fi360CheckBox').checked,
									document.getElementById('rpagCheckBox').checked)" />
					</div>
					<div  class="button_login">
						<input style="width: 100px; font-size: 11px;" type="button"
							value="CANCEL" onclick="cancelMetricsSelection()" />
					</div>
				</td>
			</tr>
		    </table> 
		</div>
	</div>

</bd:form>
