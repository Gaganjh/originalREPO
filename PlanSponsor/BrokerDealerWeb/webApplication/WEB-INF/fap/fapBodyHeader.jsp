
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
     <%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile" %>   
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />

<mrtl:noCaching/>

<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>
<script>
	$(document).ready(function() {
		$(window).load(function() {
		var isMerrillAdvisor = ${userProfile.isMerrillAdvisor()};
		var isContractMerrillDistChannel = ${fapForm.cofidDistChannel =='ML'};
			 if (isMerrillAdvisor || isContractMerrillDistChannel) {
				$("#restrictedFundsContent").show();
				document.getElementById("showML").value = "true" ;

			} else {
				$("#restrictedFundsContent").hide();
			} 
			
		});
	});
</script>

<content:contentBean contentId="${bdContentConstants.FUND_AND_PERFORMANCE_LAYOUT_LAYER1}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="Layer1"/>

<content:contentBean contentId="${bdContentConstants.FUND_INFORMATION_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="FundInfoTab"/>

<content:contentBean contentId="${bdContentConstants.PRICES_AND_YTD_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="pricesAndYTDTab"/>

<content:contentBean contentId="${bdContentConstants.PERFORMANCE_DISCLOSURE}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="PerformanceAndFeesTab"/>

<content:contentBean contentId="${bdContentConstants.STANDARD_DEVIATION_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="StandardDeviationTab"/>

<content:contentBean contentId="${bdContentConstants.FUND_CHARACTERISTICS_1_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="Fundchar1Tab"/>

<content:contentBean contentId="${bdContentConstants.FUND_CHARACTERISTICS_2_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="Fundchar2Tab"/>

<content:contentBean contentId="${bdContentConstants.MORNINGSTAR_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="MorningstarTab"/>

<content:contentBean contentId="${bdContentConstants.PRICES_AND_YTD_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="FundScorecardTab"/>

<content:contentBean contentId="${bdContentConstants.FUND_SCORECARD_DISCLOSURE_TEXT}" 
type="${bdContentConstants.TYPE_DISCLAIMER}" beanName="fundScorecardDisclosure"/>

<content:contentBean contentId="${bdContentConstants.FEE_WAIVER_DISCLOSURE_TEXT}" 
type="${bdContentConstants.TYPE_DISCLAIMER}" beanName="feeWaiverDisclosureText"/>

<content:contentBean  contentId="${bdContentConstants.CUSTOM_QUERY_ACKNOWLEDGEMENT}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" id="customFilterAcknowledgement"/>  

<content:contentBean  contentId="${bdContentConstants.MERRILL_RESRICTED_FUNDS_CONTENT}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="restrictedFundsDisclosure"/> 

<content:contentBean contentId="${bdContentConstants.FAP_GENERIC_VIEW_DISCLOSURE}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="GenericViewDisclosure"/>

<content:contentBean contentId="${bdContentConstants.FAP_CONTRACT_VIEW_DISCLOSURE}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="ContractViewDisclosure"/>

    <%-- Additional Information Section--%>
    <a name="anchor1"></a>
    <div id="rightColumn1">
        <h1><content:getAttribute attribute="name" beanName="Layer1"/></h1>
        <p><content:getAttribute attribute="text" beanName="Layer1"/></p>
    </div>

    <%-- Page header and intro text --%>
    <div id="content">
        
        <h1><content:getAttribute attribute="name" beanName="fapLayoutPageBean"/>: <span style="font-size:14px;">${fapForm.asOfDate}</span></h1>
        
<c:if test="${fapForm.contractMode ==true}">

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


            <p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
                <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
            </p>
</c:if>
        
        <div id="customFilterAcknowledgement" style="display: none"><content:getAttribute beanName="customFilterAcknowledgement" attribute="text"/></div>
        
        <!--Layout/Intro1-->
        <c:if test="${not empty layoutPageBean.introduction1}">
            <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
        
        <!--Layout/Intro2-->
        <c:if test="${not empty layoutPageBean.introduction2}">
            <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>

        <div id="fundInformation">
            <p><content:getAttribute attribute="text" beanName="FundInfoTab"/></p>
        </div>
    
        <div id="pricesAndYTD" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="pricesAndYTDTab"/></p>
            <p><content:getAttribute attribute="text" beanName="feeWaiverDisclosureText"/></p>
        </div>
        
        <div id="performanceAndFees" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="PerformanceAndFeesTab"/></p>
            <p><content:getAttribute attribute="text" beanName="feeWaiverDisclosureText"/></p>
        </div>

        <div id="standardDeviation" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="StandardDeviationTab"/></p>
        </div>
    
        <div id="fundCharacteristics1" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="Fundchar1Tab"/></p>
        </div>
        
        <div id="fundCharacteristics2" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="Fundchar2Tab"/></p>
        </div>

        <div id="morningstar" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="MorningstarTab"/></p>
            <p><content:getAttribute attribute="text" beanName="feeWaiverDisclosureText"/></p>
        </div>
    
        <div id="fundScorecard" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="FundScorecardTab"/></p>
            <p><content:getAttribute attribute="text" beanName="feeWaiverDisclosureText"/></p>
            <p><content:getAttribute attribute="text" beanName="fundScorecardDisclosure"/></p>
        </div>
        
        <div id="restrictedFundsContent" style="display:none;">
            <p><content:getAttribute attribute="text" beanName="restrictedFundsDisclosure"/></p>
        </div>
        <c:if test="${fapForm.contractMode ==true}">
	        <div id="contractFundsDisclosure">
	            <p><content:getAttribute attribute="text" beanName="ContractViewDisclosure"/></p>
	        </div>        
        </c:if>
        <c:if test="${fapForm.contractMode !=true}">
	        <div id="allFundsDisclosure">
	            <p><content:getAttribute attribute="text" beanName="GenericViewDisclosure"/></p>
	        </div>
	        
	        <div id="contractFundsDisclosure" style="display:none;">
	            <p><content:getAttribute attribute="text" beanName="ContractViewDisclosure"/></p>
	        </div>
        </c:if>
    </div>
    
    <div class="table_controls_footer"></div>
