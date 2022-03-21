<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/bd-logicext.tld" prefix="logicext"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="fundEvalConstants" className="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants"/>

<%-- Content Id definations --%>
<content:contentBean contentId="<%=BDContentConstants.WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR%>"
        type="<%=CommonContentConstants.TYPE_MESSAGE%>"
        id="navigateAwayWarningContent"/>

<content:contentBean contentId="<%=BDContentConstants.EQUITY_FUNDS_LABEL%>"
        type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
        id="equityFundsLabel"/>
                     
<content:contentBean contentId="<%=BDContentConstants.FIXED_INCOME_FUNDS_LABEL%>"
        type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
        id="fixedIncomeFundsLabel"/>

<content:contentBean contentId="<%=BDContentConstants.HYBRID_INDEX_SECTOR_FUNDS_LABEL%>"
        type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
        id="hybridIndexSectorFundsLabel"/>      

<content:contentBean contentId="<%=BDContentConstants.GURANTEED_ACCOUNTS_FUNDS_LABEL%>"
        type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
        id="guranteedAccountsFundsLabel"/>      

<content:contentBean contentId="<%=BDContentConstants.FUNDS_DESELECT_WARNING%>"
        type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
        id="fundDeselectWarning"/>          
<c:set var="intro1" value="${layoutPageBean.introduction1}"/>
<c:set var="intro2" value="${layoutPageBean.introduction2}"/>
<c:set var="benifits" value="${layoutPageBean.layer1.text}"/>

<SCRIPT type=text/javascript>
window.onload = initializeAssetClassBoxes;
//window.onDomReady = initializeAssetClassBoxes;
var messageWarningFundsDeselect = "<content:getAttribute beanName="fundDeselectWarning" attribute="text" filter="true"/>"

/*
* Asset class information which is created and initialized at page loading and will
* be used basically for baseline funds. 
* This object will not be refreshed later after updates from the overlays.
*/
var assetClassesInfo = [
    <c:forEach var="assetClass" items="${fundEvaluatorForm.assetClassDetails}" varStatus="assetClassStatus" >
                <c:set var="assetClassInfo" value="${assetClass.value}" />
                    {
                        "assetClassId" : "${assetClassInfo.assetClassId}",
                        "assetClassName" : "${assetClassInfo.assetClassName}",
                        "totalFundsSelected" : "${assetClassInfo.totalFundsSelected}",
                        "totalBaseLineFunds" : "${assetClassInfo.totalBaseLineFunds}",
                        }
                        <c:if test="${!assetClassStatus.last}">
                            ,
                        </c:if> 
    </c:forEach>
];

/**
* Display the asset class boxes with proper styles and selected funds value on page load.
* If baseline funds for the asset class is zero than asset class box will be shown as blank.  
*/

function initializeAssetClassBoxes() {

	var targetRiskAssetClassBox = "${fundEvalConstants.ASSET_CLASS_ID_LIFESTYLE}";
	var baseLineFundsLSC = 0;
	var selectedFundsLSC = 0;
	var baselineFundsLSFGIFL = 0;
	var selectedFundsLSFGIFL = 0;

	for (var i = 0; i < assetClassesInfo.length; i++) {
		if(assetClassesInfo[i].assetClassId == '${fundEvalConstants.ASSET_CLASS_ID_LIFESTYLE}' || assetClassesInfo[i].assetClassId == '${fundEvalConstants.ASSET_CLASS_ID_GIFL_SELECT}') {
			baselineFundsLSFGIFL = baselineFundsLSFGIFL + parseInt(assetClassesInfo[i].totalBaseLineFunds);
			selectedFundsLSFGIFL = selectedFundsLSFGIFL + parseInt(assetClassesInfo[i].totalFundsSelected);
		} else  {
			// For all other asset class box's
			showOrHideAssetClassBox(assetClassesInfo[i].assetClassId, assetClassesInfo[i].totalBaseLineFunds, assetClassesInfo[i].totalFundsSelected);
		}
	}
	// Balanced asset class box includes both Target Risk and GIFL select funds.
	showOrHideAssetClassBox(targetRiskAssetClassBox, baselineFundsLSFGIFL, selectedFundsLSFGIFL);

	<c:if test="${fundEvaluatorForm.hasGuranteedAccountsFunds eq 'false'}"> 
		document.getElementById("GATable").innerHTML = "";
	</c:if>
}

/**
* Show or hide the asset class box on the page.
*   If baseline funds equal to zero, means if there are no funds to select
*   from the asset class, asset class box will be shown as blank.
*   
*/
function showOrHideAssetClassBox(assetClassId, baseLineFunds, totalFundsSelected) {
    
    var assetClassBox = document.getElementById("asset-class-" + assetClassId);
    
    if (assetClassBox) {
    
    	 assetClassBox.style.display = "inline";
    	
	    if(baseLineFunds > 0) {
					
	        // If asset class has funds to select or deselect
	        if(totalFundsSelected > 0) {
	            document.getElementById("fund-count-" + assetClassId).innerHTML = totalFundsSelected ;
	        }
	        document.getElementById(assetClassId).onclick = showAssetClassFunds;  // Adding click event.
	    }
    
    }
    
}


/**
* Returns true if user checked/uncheck the svf competing fund on the overlay
*
**/
function isSvfCompetingFund(selectedFundId) {
    var svfCompetingFund = false;
	<c:set var="svfCompetingFundsList" value="${requestScope.svfCompetingFunds}" scope="page" />
    <c:forEach var="svfCompetingFund" items="${svfCompetingFundsList}" >
        if("${svfCompetingFund}" == selectedFundId) {
            svfCompetingFund = true;
        } 
    </c:forEach>

    return svfCompetingFund;
}

/**
* Returns true if user checked/uncheck the stable value fund(SVF) on the overlay.
*
**/
function isSvfFund(selectedFundId) {
    var svfFund = false;
	<c:set var="svfFunds" value="${requestScope.stableValueFunds}" scope="page" />
    <c:forEach var="svfFundId" items="${svfFunds}" >
        if("${svfFundId}" == selectedFundId) {
            svfFund = true;
        } 
    </c:forEach>

    return svfFund;
}

/**
* Selects all the SVF fund check boxes.
*
*/
function checkSvfFunds() {
	<c:set var="svfFunds" value="${requestScope.stableValueFunds}" scope="page" />

    var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;
    
    <c:forEach var="svfFundId" items="${svfFunds}" >
        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[1] == "${svfFundId}") {
                fundsDisplayed[index].checked = true;
             }
        }
    </c:forEach>
}

/**
* Unchecks all the SVF funds.
*
*/
function unCheckSvfFunds() {
    var uncheckedAnyFund = false;
	<c:set var="svfFunds" value="${requestScope.stableValueFunds}" scope="page" />
    var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;
    
    <c:forEach var="svfFundId" items="${svfFunds}" >
        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[1] == "${svfFundId}") {
                if(fundsDisplayed[index].checked) {
                    fundsDisplayed[index].checked = false;
                    uncheckedAnyFund = true;
                }
             }
        }
    </c:forEach>
    return uncheckedAnyFund;
}

/**
* Selects all the SVF competing fund check boxes.
*
*/
function checkSvfCompetingFunds() {
    <c:set var="svfCompetingFundsCollection" value="${requestScope.svfCompetingFunds}" scope="page" />
	var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;
    
    <c:forEach var="svfCompetingFundId" items="${svfCompetingFundsCollection}" >
        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[1] == "${svfCompetingFundId}") {
                    fundsDisplayed[index].checked = true;
             }
        }
    </c:forEach>
}

/**
* Unchecks all SVF competing funds.
*
*/
function unCheckSvfCompetingFunds(selectedFundId) {
    var isUnCheckedAnyFund = false;
<c:set var="svfCompetingFundsCollection" value="${requestScope.svfCompetingFunds}" scope="page" />
    var fundsDisplayed = document.fundEvaluatorForm.selectedFunds;
    
    <c:forEach var="svfCompetingFundId" items="${svfCompetingFundsCollection}" >
        for(var index=0; index < fundsDisplayed.length; index++ ) {
            assetClsAndFundIdName = fundsDisplayed[index].value.split("-");
            if (assetClsAndFundIdName[1] == "${svfCompetingFundId}" && selectedFundId != "${svfCompetingFundId}") {
                if(fundsDisplayed[index].checked) {
                    fundsDisplayed[index].checked = false;
                    isUnCheckedAnyFund = true;
                }
             }
        }
    </c:forEach>
    
    return isUnCheckedAnyFund;
}

// To forwards to next page.
function doNext() {
    document.fundEvaluatorForm.action="?action=" + customizeReport;
    document.fundEvaluatorForm.submit();
}

// To forwards to previous page.
function doPrevious() {
    dialogWarningFundsDeselect.show(messageWarningFundsDeselect);
}

    // Call back function for get assetclass funds listing.
    var callback_displayAssetClassFunds =    {
        cache   : false,
        success : displayDetailPanel,
        failure : utilities.handleFailure,
        argument: callbackArgument
    };

</SCRIPT>

<%-- Need to find out the final div tag for using yahoo skin class. --%>
<bd:form method="POST" action="/do/fundEvaluator/" modelAttribute="fundEvaluatorForm" name="fundEvaluatorForm">


<input type="hidden" name="navigateAwayWarning" value="${navigateAwayWarningContent.text}"/>
<form:hidden path="assetClassId" />
<form:hidden path="rankingOrder" />
<form:hidden path="selectedAnySVF" />
<form:hidden path="selectedAnySVFCompeting" />
<form:hidden path="newPlanClosedFund" />
<form:hidden path="existingClientClosedFund" />
<form:hidden path="includeGIFLSelectFunds" />
<form:hidden path="lifeStylePortfolios" />
<form:hidden path="lifeCyclePortfolios" />
<form:hidden path="stableValueFunds" />
<form:hidden path="moneyMarketFunds" />
<form:hidden path="includedOptItem1" />
<form:hidden path="includedOptItem3" />
<form:hidden path="includedOptItem4" />
<form:hidden path="includedOptItem5" />
<form:hidden path="includedOptItem6" />
<form:hidden path="includedOptItem7" />
<form:hidden path="nml" />
<form:hidden path="previousAction" value="${fundEvalConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION}" />
<form:hidden path="dataModified" />
<form:hidden path="edwardJones" />

		<DIV id="contentOuterWrapper">
			<DIV id="contentWrapper">
				<%--Display the benefit section if the content is not empty --%>     
				<c:if test="${!empty benifits}">
					<div id="rightColumn1">
						<%-- FundEvaluator benefits layer --%>
						${layoutPageBean.layer1.text}		
					</div>
				</c:if>
				<DIV id="contentTitle">
					<content:getAttribute id="layoutPageBean" attribute="name"/>
				</DIV>
				<c:if test="${!empty intro1}">
					<p id="contentIntro">
						<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
					</p>
				</c:if>
				<c:if test="${!empty intro2}">
					<p id="contentIntro">
						<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
					</p>
				</c:if>
				<c:if test="${!empty benifits}">
					<br class="clearFloat" /><br class="clearFloat" />
				</c:if>
				<DIV id="contentievalfull">
					<div id="fundEvaluatorButtons">
						<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
						<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
					</div>

					<DIV style="CLEAR: both">
						<UL class="proposal_nav_menu">
							<LI>
								<A id="step1">Step 1</A> 
							</LI>
							<LI><A id="step2">Step 2</A> </LI>
							  <LI><A id="step3">Step 3</A> </LI>
							  <LI><A class="selected_link"><SPAN class="step_number">Step 4:</SPAN><SPAN class="step_caption"> Review and customize line up</SPAN></A> 
							  </LI>
							  <LI>	<A id="step5">Step 5</A> </LI>
						</UL>
						
						<DIV class="selection_mode">
							<DIV id="assetHouse">
								<TABLE class="structure" >
									<TBODY>
										<TR>
											<TD width=410>&nbsp;</TD>
											<TD style="VERTICAL-ALIGN: top" align=right width=478>
												<TABLE cellSpacing=0 cellPadding=0 border=0>
													<TBODY>
														<TR>
															<TD class="previewSelected">
																 Total: 
															    <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
																<SPAN class="previewNum" id="totalSelectedFunds">
${fundEvaluatorForm.totalSelectedFunds}
																</SPAN>
																<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
																Funds
															</TD>
															<TD class="previewFunds">
<logicext:if name="fundEvaluatorForm" property="totalSelectedFunds"
																	op="equal" value="0">
<logicext:then>
																<DIV id="previewLinkDisabled" style="display:">
																	Preview Funds
																</DIV>
																<DIV id="previewLinkEnabled" style="display:none">
																	<A class="preiveFundsLink" title="Prview" href="javascript:displayPreview()">
																		Preview Funds
																	</A>
																</DIV>
</logicext:then>
<logicext:else>
        												<DIV id="previewLinkDisabled" style="display:none">
																	Preview Funds
																</DIV>
																<DIV id="previewLinkEnabled" style="display:">
																	<A class="preiveFundsLink" title="Prview" href="javascript:displayPreview()">
																		Preview Funds
																	</A>
																</DIV>
</logicext:else>
</logicext:if>
								        			</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD>
												<TABLE class="assetTable" cellspacing=4 >
													<TBODY>
														<TR>
															<TH colspan="3">
																<%-- Equity funds --%>
																<content:getAttribute beanName="equityFundsLabel" attribute="text" filter="true"/>
															</TH>
														</TR>
														<TR>
										          <TD class="asset-group" colspan="3">
										            <TABLE border="0" cellspacing="0" cellpadding="0">
										              <TBODY>
											              <TR class="asset-heading">
											                <TD class="asset-heading" colspan="3">
											                	<STRONG>Large Cap</STRONG>
											                </TD>
											              </TR>
											              <TR>
																			<%-- Large Cap Value asset class box --%>
																			<TD id="LCV" class="asset-box">
																			  <DIV class="asset-box-label" id="asset-class-LCV">Value</DIV>
																			  <DIV class="fund-count" id="fund-count-LCV"></DIV>
																			</TD>
																			<%-- Large Cap Blend asset class box --%>
																			<TD id="LCB" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-LCB">Blend</DIV>
																				<DIV class="fund-count" id="fund-count-LCB"></DIV>
																			</TD>
																			<%-- Large Cap growth asset class box --%>
																			<TD id="LCG" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-LCG">Growth</DIV>
																				<DIV class="fund-count" id="fund-count-LCG"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
														<TR>
										          <TD class="asset-group" colspan="3">
										            <TABLE border="0" cellspacing="0" cellpadding="0">
										              <TBODY>
											              <TR class="asset-heading">
											                <TD class="asset-heading" colspan="3">
											                	<STRONG>Mid Cap</STRONG>
											                </TD>
											              </TR>
											              <TR>
																			<%-- Mid Cap Value asset class box --%>
																			<TD id="MCV" class="asset-box">
																			  <DIV class="asset-box-label" id="asset-class-MCV">Value</DIV>
																			  <DIV class="fund-count" id="fund-count-MCV"></DIV>
																			</TD>
																			<%-- Mid Cap Blend asset class box --%>
																			<TD id="MCB" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-MCB">Blend</DIV>
																				<DIV class="fund-count" id="fund-count-MCB"></DIV>
																			</TD>
																			<%-- Mid Cap growth asset class box --%>
																			<TD id="MCG" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-MCG">Growth</DIV>
																				<DIV class="fund-count" id="fund-count-MCG"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
														<TR>
										          <TD class="asset-group" colspan="3">
										            <TABLE border="0" cellspacing="0" cellpadding="0">
										              <TBODY>
											              <TR class="asset-heading">
											                <TD class="asset-heading" colspan="3">
											                	<STRONG>Small Cap</STRONG>
											                </TD>
											              </TR>
											              <TR>
																			<%-- Small Cap Value asset class box --%>
																			<TD id="SCV" class="asset-box">
																			  <DIV class="asset-box-label" id="asset-class-SCV">Value</DIV>
																			  <DIV class="fund-count" id="fund-count-SCV"></DIV>
																			</TD>
																			<%-- Small Cap Blend asset class box --%>
																			<TD id="SCB" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-SCB">Blend</DIV>
																				<DIV class="fund-count" id="fund-count-SCB"></DIV>
																			</TD>
																			<%-- Small Cap growth asset class box --%>
																			<TD id="SCG" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-SCG">Growth</DIV>
																				<DIV class="fund-count" id="fund-count-SCG"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
														<TR>
										          <TD class="asset-group" colspan="3">
										            <TABLE border="0" cellspacing="0" cellpadding="0">
										              <TBODY>
											              <TR class="asset-heading">
											                <TD class="asset-heading" colspan="3">
											                	<STRONG>International/Global</STRONG>
											                </TD>
											              </TR>
											              <TR>
																			<%-- International/Global Cap Value asset class box --%>
																			<TD id="IGV" class="asset-box">
																			  <DIV class="asset-box-label" id="asset-class-IGV">Value</DIV>
																			  <DIV class="fund-count" id="fund-count-IGV"></DIV>
																			</TD>
																			<%-- International/Global Cap Blend asset class box --%>
																			<TD id="IGB" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-IGB">Blend</DIV>
																				<DIV class="fund-count" id="fund-count-IGB"></DIV>
																			</TD>
																			<%-- International/Global Cap growth asset class box --%>
																			<TD id="IGG" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-IGG">Growth</DIV>
																				<DIV class="fund-count" id="fund-count-IGG"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
											<TD>
												<TABLE class="assetTable" cellspacing="4">
													<TBODY>
														<TR>
															<TH colspan="4"> Asset Allocation </TH>
														</TR>
														<TR>
															<TD class="asset-group" colspan="4">
																<TABLE border="0" cellspacing="0" cellpadding="0"> 
																	<TBODY>
																		<TR>
																			<TD id="LCF" class="asset-box-wide" colspan="2">
																				<DIV class="asset-box-label" id="asset-class-LCF">Target Date</DIV>
																				<DIV class="fund-count" id="fund-count-LCF"></DIV>
																			</TD>
																			<TD id="LSF" class="asset-box-wide" colspan="2">
																				<DIV class="asset-box-label" id="asset-class-LSF">Target Risk</DIV>
																				<DIV class="fund-count" id="fund-count-LSF"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
												<TABLE class="assetTable" cellspacing="4">
													<TBODY>
														<TR>
															<TH colspan="4"> Fixed Income </TH>
														</TR>
														<TR>
															<TD class="asset-group" colspan="4">
																<TABLE border="0" cellspacing="0" cellpadding="0">
																	<TBODY>
																		<TR class="asset-heading">
																			<TD class="asset-heading" colspan="4">
																				<STRONG>Short Term</STRONG>
																			</TD>
																		</TR>
																		<TR>
																			<TD id="HQS" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-HQS">High Quality</DIV>
																				<DIV class="fund-count" id="fund-count-HQS"></DIV>
																			</TD>
																			<TD id="MQI" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-MQI">Medium Quality</DIV>
																				<DIV class="fund-count" id="fund-count-MQI"></DIV>
																			</TD>
																			<TD id="LQS" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-LQS">Low Quality</DIV>
																				<DIV class="fund-count" id="fund-count-LQS"></DIV>
																			</TD>
																			<TD id="GLS" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-GLS">Global</DIV>
																				<DIV class="fund-count" id="fund-count-GLS"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD class="asset-group" colspan="4">
																<TABLE border="0" cellspacing="0" cellpadding="0">
																	<TBODY>
																		<TR class="asset-heading">
																			<TD class="asset-heading" colspan="4">
																				<STRONG>Intermediate Term</STRONG>
																			</TD>
																		</TR>
																		<TR>
																			<TD id="FXI" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-FXI">High Quality</DIV>
																				<DIV class="fund-count" id="fund-count-FXI"></DIV>
																			</TD>
																			<TD id="FXM" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-FXM">Medium Quality</DIV>
																				<DIV class="fund-count" id="fund-count-FXM"></DIV>
																			</TD>
																			<TD id="LQI" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-LQI">Low Quality</DIV>
																				<DIV class="fund-count" id="fund-count-LQI"></DIV>
																			</TD>
																			<TD id="GLI" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-GLI">Global</DIV>
																				<DIV class="fund-count" id="fund-count-GLI"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD class="asset-group" colspan="4">
																<TABLE border="0" cellspacing="0" cellpadding="0">
																	<TBODY>
																		<TR class="asset-heading">
																			<TD class="asset-heading" colspan="4">
																				<STRONG>Long Term</STRONG>
																			</TD>
																		</TR>
																		<TR>
																			<TD id="FXL" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-FXL">High Quality</DIV>
																				<DIV class="fund-count" id="fund-count-FXL"></DIV>
																			</TD>
																			<TD id="MQL" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-MQL">Medium Quality</DIV>
																				<DIV class="fund-count" id="fund-count-MQL"></DIV>
																			</TD>
																			<TD id="LQL" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-LQL">Low Quality</DIV>
																				<DIV class="fund-count" id="fund-count-LQL"></DIV>
																			</TD>
																			<TD id="GLL" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-GLL">Global</DIV>
																				<DIV class="fund-count" id="fund-count-GLL"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD>
												<TABLE class="assetTable" cellspacing="4">
													<TBODY>
														<TR>
															<TH colspan="3"> Specialty/Sector/Balanced </TH>
														</TR>
														<TR>
															<TD class="asset-group" colspan="3">
																<TABLE border="0" cellspacing="0" cellpadding="0">
																	<TBODY>
																		<TR>
																			<TD id="SPE" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-SPE">Specialty</DIV>
																				<DIV class="fund-count" id="fund-count-SPE"></DIV>
																			</TD>
																			<TD id="SEC" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-SEC">Sector</DIV>
																				<DIV class="fund-count" id="fund-count-SEC"></DIV>
																			</TD>
																			<TD id="BAL" class="asset-box">
																				<DIV class="asset-box-label" id="asset-class-BAL">Balanced</DIV>
																				<DIV class="fund-count" id="fund-count-BAL"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
											<TD id="GATable" >
												<TABLE class="assetTable" cellspacing="4">
													<TBODY>
														<TR>
															<TH id="GALabel" colspan="3"> Guaranteed Accounts^ </TH>
														</TR>
														<TR>
															<TD class="asset-group" colspan="3">
																<TABLE border="0" cellspacing="0" cellpadding="0">
																	<TBODY>
																		<TR>
																			<TD class="asset-box" id="GA3">
																				<DIV class="asset-box-label" id="asset-class-GA3">3 Year</DIV>
																				<DIV class="fund-count" id="fund-count-GA3"></DIV>
																			</TD>
																			<TD class="asset-box" id="GA5">
																				<DIV class="asset-box-label" id="asset-class-GA5">5 Year</DIV>
																				<DIV class="fund-count" id="fund-count-GA5"></DIV>
																			</TD>
																			<TD class="asset-box" id="G10">
																				<DIV class="asset-box-label" id="asset-class-G10">10 Year</DIV>
																				<DIV class="fund-count" id="fund-count-G10"></DIV>
																			</TD>
																		</TR>
																	</TBODY>
																</TABLE>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</DIV>
						</DIV>
						<BR/>
						<div id="fundEvaluatorButtons">
							<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
							<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
						</div>
				</DIV>
			</DIV>
		</DIV>
	</DIV>
	<DIV  id="indexPanel" style="visibility:hidden;BACKGROUND-COLOR: white">
		<%-- Dynamic --%>
	</DIV>
	<DIV  id="previewPanel" style="visibility:hidden;BACKGROUND-COLOR: white">          
		<%-- Dynamic --%>
	</DIV>
	<DIV  id="detailPanel" style="visibility:hidden;BACKGROUND-COLOR: white">
		<%-- Dynamic --%>
	</DIV>
	
</bd:form>

<c:if test="${fundEvaluatorForm.hasGuranteedAccountsFunds eq 'true'}"> 
	<content:contentBean contentId="<%=BDContentConstants.GA_DISCLOSURE%>" type="<%=CommonContentConstants.TYPE_PAGEFOOTNOTE%>" beanName="gaDisclosure"/>
	<div class="footnotes">
    <div class="footer"><sup>^</sup><content:getAttribute beanName="gaDisclosure" attribute="text"/></div> 
    </div>
</c:if>

<%-- Page specific footer elements --%>
<layout:pageFooter/>
