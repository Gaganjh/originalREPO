<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.owasp.encoder.Encode"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="formConstants" className="com.manulife.pension.ps.web.plandata.TabPlanDataForm"/>
<content:contentBean contentId="91561" type="${contentConstants.TYPE_MISCELLANEOUS}" id="termsOfUse" />
<content:contentBean contentId="91444" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_VALUE" />

<content:contentBean contentId="58181" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_ACI_ANNUAL_APPLY_DATE" />
<content:contentBean contentId="91433" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_EMPLOYEE_DEFERRAL_MAX" />
<content:contentBean contentId="91434" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_EMPLOYEE_DEFERRAL_MAX_DOLLAR" />
<content:contentBean contentId="91436" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX" />
<content:contentBean contentId="91437" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX_DOLLAR" />

<content:contentBean contentId="91461" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_MAX_ANNUAL_INCREASE_WITH_DECIMAL" />
<content:contentBean contentId="91462" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_ANNUAL_INCREASE_WITH_DECIMAL" />
<content:contentBean contentId="91465" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_MAXIMUM_AUTO_INCREASE" />
<content:contentBean contentId="91473" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_QACA_MATCHING_CONTRIBUTIONS" />
<content:contentBean contentId="91481" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_QACA_SH_MATCH_VESTING1" />
<content:contentBean contentId="91482" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_QACA_SH_MATCH_VESTING2" />
<content:contentBean contentId="91453" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_QACA_SH_NON_ELECTIVE" />
<content:contentBean contentId="67227" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_ACI_APPLIES_EFFECTIVE_DATE" />
<content:contentBean contentId="91440" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_HIRE_AFTER_DATE" />
<!-- Generic error messages -->
<content:contentBean contentId="80716" type="${contentConstants.TYPE_MISCELLANEOUS}" id="UNSAVED_DATA_MESS" />
<content:contentBean contentId="91435" type="${contentConstants.TYPE_MISCELLANEOUS}" id="MISSING_VALUES_MESS" />
<content:contentBean contentId="91438" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SPECIAL_CHARS" />

<!-- SH tab -->
<content:contentBean contentId="91444" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID" />
<content:contentBean contentId="91449" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL1" />
<content:contentBean contentId="91451" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL2" />
<content:contentBean contentId="91452" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL1" />
<content:contentBean contentId="91453" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL2" />
<content:contentBean contentId="91450" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT" />
<content:contentBean contentId="92321" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CHANGES_LOST" />
<content:contentBean contentId="92322" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_NE_CHECKBOX" />
<content:contentBean contentId="92323" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_INVALID_DATE" />
<content:contentBean contentId="91444" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_INVALID_PCT" />
<content:contentBean contentId="92324" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_MIN_MAX_PCT" />
<content:contentBean contentId="92325" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_ADDN_EMP_CONTR" />
<!-- InvInfo Tab -->
<content:contentBean contentId="91493" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_TRANSFER_OUT_DAYS_CUSTOM_NOT_IN_SPECIFIED_LIMIT_OR_NON_NUMERIC" />

<!-- Auto Tab -->
<content:contentBean contentId="91439" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_INVALID_CONTRIBUTION_LESS_PCT" />
<content:contentBean contentId="91445" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL1" />
<content:contentBean contentId="91446" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL2" />
<content:contentBean contentId="91447" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL1" />
<content:contentBean contentId="91448" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL2" />
<content:contentBean contentId="91468" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_AUTO_TAB_NO_OF_DAYS_INVALID" />
<content:contentBean contentId="91457" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_AUTO_CONTRIBUTION_TYPE_NOT_SELECTED" />
<content:contentBean contentId="91456" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_EXCLUDE_ALL_MONEY_TYPE" />
<content:contentBean contentId="92330" type="${contentConstants.TYPE_MISCELLANEOUS}" id="AC_PROVISION_TYPE_CHANGE" />
<content:contentBean contentId="92331" type="${contentConstants.TYPE_MISCELLANEOUS}" id="ERR_EMP_CONTRIB" />
<content:contentBean contentId="92432" type="${contentConstants.TYPE_MESSAGE}" id="CONTRIB_AND_DISTRIB_NOT_COMPLETED_MSG" />
<content:contentBean
  contentId="<%=ContentConstants.ACKNOWLEDGEMENT_TEXT_FOR_TPA_USER_PLAN_DATA%>"
  type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="acknowledgementText" /> 
  
<content:contentBean
	contentId="80833"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="saveButtonHoverOverText" />  
	
	<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	


<c:set var="selectedTab" scope="request" value="${tabPlanDataForm.selectedTab}"/>
<c:set var="disableFieldsForContributions" scope="request" value="true"/>
	
<style type="text/css">
div.fandp_nav li{
	background-image: url(/assets/unmanaged/images/1line_off_tab_long_150px.gif);
	width: 122px;
}

div.fandp_nav li.on{
	background-image: url(/assets/unmanaged/images/1line_on_tab_long_150px.gif);
	width: 120px;
}

div.fandp_nav LI.off_over {
    background-image: url(/assets/unmanaged/images/1line_on_tab_long_150px.gif);
    width: 122px;
}
</style>
<% TabPlanDataForm tabPlanDataForm = (TabPlanDataForm) session.getAttribute(Constants.TPA_PLAN_DATA_FORM);
		   String shDataComplete = tabPlanDataForm.getSafeHarborDataCompleteInd();
		   String cdDataComplete = tabPlanDataForm.getContriAndDistriDataCompleteInd();
		   String noticeServiceIndValue = tabPlanDataForm.getNoticeServiceInd();
		   String noticeTypeSelectedValue = tabPlanDataForm.getNoticeTypeSelected();
		%>
<ps:form method="POST" action="/do/viewNoticePlanData/" modelAttribute="tabPlanDataForm" name="tabPlanDataForm">
<form:hidden  path="fromTab"/>
<form:hidden  path="toTab"/>
<form:hidden  path="selectedTab"/>
<form:hidden  path="contractId"/>
<form:hidden  path="buttonClicked"/>
<form:hidden  path="safeHarborDataCompleteInd" value="${shDataComplete}"/>
<form:hidden  path="contriAndDistriDataCompleteInd" value="${cdDataComplete}"/>
<form:hidden  path="noticeServiceInd" value="${noticeServiceIndValue}"/>
<form:hidden  path="noticeTypeSelected" value="${noticeTypeSelectedValue}"/>
<form:hidden  path="dirty" id="dirtyFlagId"/>
<form:hidden  path="action" id="action" /><%--  input - name="tabPlanDataForm" --%>
	
	<content:getAttribute id="termsOfUse" attribute="text"/>
    
<%-- Error Table --%>
<div class="messagesBox" id="messagesBox" >
<ps:messages scope="session"  suppressDuplicateMessages="true" />
</div>	
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
    <TR>
    <TD width="100%">	
	  <div>
		<table>
		   <tr style="height:10px">
			  <td> <b>${tabPlanDataForm.contractName}</b><img src="/assets/unmanaged/images/spacer.gif" width="1">|<img src="/assets/unmanaged/images/spacer.gif" width="3"><b>Contract:</b><img src="/assets/unmanaged/images/spacer.gif"  width="5">${tabPlanDataForm.contractId}</td>
		   </tr>
		</table>
		<%
		   String selectedTab = tabPlanDataForm.getSelectedTab();
		%>
		<jsp:include flush="true" page="picNavigationTabBar.jsp">
			<jsp:param name="selectedTab" value="<%= Encode.forHtmlContent(selectedTab)%>"/>
		</jsp:include>
		<% if("contributionAndDistribution".equals(selectedTab)){ %>
			<jsp:include flush="true" page="contributionAndDistribution.jsp"></jsp:include>
		<% } else if("safeHarbor".equals(selectedTab)){%>
			<jsp:include flush="true" page="safeHarbor.jsp"></jsp:include>
		<% } else if("automaticContribution".equals(selectedTab)){ %>
			<jsp:include flush="true" page="automaticContribution.jsp"></jsp:include>
		<% } else if("investmentInformation".equals(selectedTab)){ %>
			<jsp:include flush="true" page="investmentInformation.jsp"></jsp:include>
		<% } else if("contactInformation".equals(selectedTab)){ %>
			<jsp:include flush="true" page="contactInformation.jsp"></jsp:include>
		<% } else { %>
			<jsp:include flush="true" page="summary.jsp"></jsp:include>
		<% } %>
				
		<br>
		<div>
			<table>
				<tr><content:getAttribute id="acknowledgementText" attribute="text"/></tr>
			</table>
		</div>
		
		<br>
		<div><table>
		<TR>
		<% String roleId=userProfile.getRole().getRoleId();
		//out.println("Role id"+roleId);
		%>
<c:if test="${tabPlanDataForm.selectedTab != 'summary'}">
<c:if test="${tabPlanDataForm.selectedTab != 'contactInformation'}">
				<td><input type="submit" onclick="return validateData('<%= Encode.forHtmlContent(selectedTab) %>','Save')" value="Save" class="button100Lg"></td>
</c:if>
</c:if>
<c:if test="${tabPlanDataForm.selectedTab !='summary'}">
			<td><input type="button" onclick="return goToUrl('<%= Encode.forHtmlContent(selectedTab) %>','previous')" value="Previous" class="button100Lg" /></td>
</c:if>
<c:if test="${tabPlanDataForm.selectedTab !='contactInformation'}">
			<td><input type="button" onclick="return goToUrl('<%= Encode.forHtmlContent(selectedTab) %>','next')" value="Next" class="button100Lg" /></td>
</c:if>
		<td><input type="button" onclick="return goToUrl('','Exit')" value="Exit" class="button100Lg"></td>
		</TR>
		</table>
		</div>
		
		<% 
			tabPlanDataForm.setToTab(null);
		%>
		
	  </div>
	  </TD>
	  </TR>
	  </TABLE>
</ps:form>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/employeeSnapshot.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script>

		
$(document).ready(function() {
			var hrefs  = document.links;

			if (hrefs != null)
			{
				for (i=0; i<hrefs.length; i++) {

					if(hrefs[i].href != undefined && hrefs[i].href.indexOf("/viewNoticePlanData/") == -1){					

						hrefs[i].onclick = new Function ("return isLostChanges();");
					}
				
				}
			}			
		
	});
	
	
function isFormDirty() {
		var dirtyFlagId = document.getElementById('dirtyFlagId').value
		return (dirtyFlagId);
	}
	
	function isLostChanges(){
			return confirmDiscardChanges(UNSAVED_DATA_MESS);
	}

	
		


function getTabData(selectedTab) {
	var result;
	if(confirmDiscardChanges(UNSAVED_DATA_MESS)){
		document.getElementById('dirtyFlagId').value = "false";
		if(selectedTab == 'summary'){
			result = 'summary';
		}
		if(selectedTab == 'contributionAndDistribution'){
			result = 'contributionAndDistribution';
		}
		if(selectedTab == 'safeHarbor'){
			result = 'safeHarbor';
		}
		if(selectedTab == 'automaticContribution'){
			result = 'automaticContribution';
		}
		if(selectedTab == 'investmentInformation'){
			result = 'investmentInformation';
		}
		if(selectedTab == 'contactInformation'){
			result = 'contactInformation';
		}  
	  	document.tabPlanDataForm.toTab.value = result;
	  	document.getElementById("action").value = 'default';
		document.tabPlanDataForm.submit();
	}
		
}


function validateData(selectedTab, button){
//if (document.getElementById('dirtyFlagId').value == 'true')
	//{
		
	if(selectedTab == 'contributionAndDistribution'){
	
		document.getElementById("action").value = 'contributionAndDistributionSave';
		document.tabPlanDataForm.buttonClicked.value = button;
	
	}
	else if(selectedTab == 'safeHarbor'){
		if(!validateSafeHarborData()){
			document.getElementById("action").value = 'safeHarborSave';
			document.tabPlanDataForm.buttonClicked.value = button;
		}
		else{
			return false;
		}
	}
	else if(selectedTab == 'automaticContribution'){
	if(!validateAutoTabData()){
			document.getElementById("action").value = 'automaticContributionSave';
		document.tabPlanDataForm.buttonClicked.value = button;

		document.getElementById("contributionFeature1PctMissing").value = "false";
		if(document.getElementById("automaticContributionFeature1").checked == true) {
			if(document.getElementById('contributionFeature1Pct').value == "") {
				document.getElementById("contributionFeature1PctMissing").value = "true";
			}
		}
		
		document.getElementById("contributionFeature2DateIdMissing").value = "false";
		if(document.getElementById("automaticContributionFeature2").checked == true) {
			if(document.getElementById('contributionFeature2DateId').value == "") {
				document.getElementById("contributionFeature2DateIdMissing").value = "true";
			}
		}
		
		document.getElementById("contributionFeature3SummaryTextMissing").value = "false";
		if(document.getElementById("automaticContributionFeature3").checked == true) {
			if(document.getElementById('contributionFeature3SummaryText').value == "") {
				document.getElementById("contributionFeature3SummaryTextMissing").value = "true";
			}
		}
		}
		else{
			return false;
		}
		
	}
	else if(selectedTab == 'investmentInformation'){
		if(!validateInvInfoData()){
			document.getElementById("action").value = 'investmentInformationSave';
			document.tabPlanDataForm.buttonClicked.value = button;
		}
		else{
			return false;
		}
		
	}
	else if(selectedTab == 'contactInformation'){
		document.getElementById("action").value = 'contactInformationSave';
		document.tabPlanDataForm.buttonClicked.value = button;
	}
	document.tabPlanDataForm.submit();	
	/*}
	else 
	{
	//TODO replace with new CMA key for TPDD.257
	   alert("No changes have been made on this page, so save will not be permitted");
	   return false;
	}*/
}


function goToUrl(selectedTab, button){
	var result;
	if(button == 'refresh'){
		document.getElementById("dirtyFlagId").value = 'false';
		document.tabPlanDataForm.toTab.value = selectedTab;
		document.getElementById("action").value = 'refresh';
		document.tabPlanDataForm.submit();
	}
	else if(confirmDiscardChanges(UNSAVED_DATA_MESS)){
		document.getElementById('dirtyFlagId').value = "false";
		if(button == 'next'){
			if(selectedTab == 'summary'){
				result = 'contributionAndDistribution';
			}
			if(selectedTab == 'contributionAndDistribution'){
				result = 'safeHarbor';
			}
			if(selectedTab == 'safeHarbor'){
				result = 'automaticContribution';
			}
			if(selectedTab == 'automaticContribution'){
				result = 'investmentInformation';
			}
			if(selectedTab == 'investmentInformation'){
				result = 'contactInformation';
			}
			document.tabPlanDataForm.toTab.value = result;
		}
		else if(button == 'previous'){
			if(selectedTab == 'contributionAndDistribution'){
				result = 'summary';
			}
			if(selectedTab == 'safeHarbor'){
				result = 'contributionAndDistribution';
			}
			if(selectedTab == 'automaticContribution'){
				result = 'safeHarbor';
			}
			if(selectedTab == 'investmentInformation'){
				result = 'automaticContribution';
			}
			if(selectedTab == 'contactInformation'){
				result = 'investmentInformation';
			}
			document.tabPlanDataForm.toTab.value = result;
		}
		else if(button == 'Exit'){
			document.tabPlanDataForm.buttonClicked.value = button;
		}
		document.getElementById("action").value = 'default';
		document.tabPlanDataForm.submit();
	}
}

function confirmDiscardChanges(message){
	if (document.getElementById('dirtyFlagId').value == 'true')
	{
		return window.confirm(message);
	}
	else 
	{
	    return true;
	}
}
var ERR_INVALID_VALUE = "<content:getAttribute id='ERR_INVALID_VALUE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_HIRE_AFTER_DATE = "<content:getAttribute id='ERR_INVALID_HIRE_AFTER_DATE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_ACI_ANNUAL_APPLY_DATE = "<content:getAttribute id='ERR_INVALID_ACI_ANNUAL_APPLY_DATE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_EMPLOYEE_DEFERRAL_MAX = "<content:getAttribute id='ERR_INVALID_EMPLOYEE_DEFERRAL_MAX' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_EMPLOYEE_DEFERRAL_MAX_DOLLAR = "<content:getAttribute id='ERR_INVALID_EMPLOYEE_DEFERRAL_MAX_DOLLAR' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX = "<content:getAttribute id='ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX_DOLLAR = "<content:getAttribute id='ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX_DOLLAR' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";

var ERR_INVALID_MAX_ANNUAL_INCREASE_WITH_DECIMAL = "<content:getAttribute id='ERR_INVALID_MAX_ANNUAL_INCREASE_WITH_DECIMAL' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_ANNUAL_INCREASE_WITH_DECIMAL = "<content:getAttribute id='ERR_INVALID_ANNUAL_INCREASE_WITH_DECIMAL' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_MAXIMUM_AUTO_INCREASE = "<content:getAttribute id='ERR_INVALID_MAXIMUM_AUTO_INCREASE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_QACA_MATCHING_CONTRIBUTIONS = "<content:getAttribute id='ERR_INVALID_QACA_MATCHING_CONTRIBUTIONS' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_QACA_SH_MATCH_VESTING1 = "<content:getAttribute id='ERR_INVALID_QACA_SH_MATCH_VESTING1' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_QACA_SH_MATCH_VESTING2 = "<content:getAttribute id='ERR_INVALID_QACA_SH_MATCH_VESTING2' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_QACA_SH_NON_ELECTIVE = "<content:getAttribute id='ERR_INVALID_QACA_SH_NON_ELECTIVE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_INVALID_ACI_APPLIES_EFFECTIVE_DATE = "<content:getAttribute id='ERR_INVALID_ACI_APPLIES_EFFECTIVE_DATE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";

//SH CMA
var ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL1 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL1' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL2 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL2' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL1 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL1' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL2 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL2' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT = "<content:getAttribute id='ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CHANGES_LOST = "<content:getAttribute id='ERR_SH_CHANGES_LOST' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_NE_CHECKBOX = "<content:getAttribute id='ERR_SH_NE_CHECKBOX' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_INVALID_DATE = "<content:getAttribute id='ERR_SH_INVALID_DATE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_INVALID_PCT = "<content:getAttribute id='ERR_SH_INVALID_PCT' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_MIN_MAX_PCT = "<content:getAttribute id='ERR_SH_MIN_MAX_PCT' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_ADDN_EMP_CONTR = "<content:getAttribute id='ERR_SH_ADDN_EMP_CONTR' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";

//AUTO_CONTRIB CMA
var AC_PROVISION_TYPE_CHANGE = "<content:getAttribute id='AC_PROVISION_TYPE_CHANGE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_EMP_CONTRIB = "<content:getAttribute id='ERR_EMP_CONTRIB' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var CONTRIB_AND_DISTRIB_NOT_COMPLETED_MSG = "<content:getAttribute id='CONTRIB_AND_DISTRIB_NOT_COMPLETED_MSG' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";


//INV INFO CMA
var ERR_TRANSFER_OUT_DAYS_CUSTOM_NOT_IN_SPECIFIED_LIMIT_OR_NON_NUMERIC = "<content:getAttribute id='ERR_TRANSFER_OUT_DAYS_CUSTOM_NOT_IN_SPECIFIED_LIMIT_OR_NON_NUMERIC' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var UNSAVED_DATA_MESS = "<content:getAttribute id='UNSAVED_DATA_MESS' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var MISSING_VALUES_MESS = "<content:getAttribute id='MISSING_VALUES_MESS' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SPECIAL_CHARS = "<content:getAttribute id='ERR_SPECIAL_CHARS' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";

var ERR_INVALID_CONTRIBUTION_LESS_PCT = "<content:getAttribute id='ERR_INVALID_CONTRIBUTION_LESS_PCT' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL1 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL1' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL2 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL2' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL1 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL1' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL2 = "<content:getAttribute id='ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL2' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_AUTO_TAB_NO_OF_DAYS_INVALID = "<content:getAttribute id='ERR_AUTO_TAB_NO_OF_DAYS_INVALID' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_AUTO_CONTRIBUTION_TYPE_NOT_SELECTED = "<content:getAttribute id='ERR_AUTO_CONTRIBUTION_TYPE_NOT_SELECTED' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
var ERR_EXCLUDE_ALL_MONEY_TYPE = "<content:getAttribute id='ERR_EXCLUDE_ALL_MONEY_TYPE' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
</script>

