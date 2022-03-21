<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Imports --%>
<%@ page import="java.util.HashMap"%>
<un:useConstants var="renderConstants"
	className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="constants"
	className="com.manulife.pension.ps.web.Constants" />

<content:contentBean
	contentId="${contentConstants.SAVE_HOVER_OVER_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="saveButtonHoverOverText" />

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >

function isFormChanged() {
  return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);


registerWarningOnChangeToLinks(new Array("printReportLink_text","printReportLink_icon"));

function doSubmit(button, popupContent){
	var confirmAction =true; 
	if (isFormChanged()){
		document.iPSAndReviewForm.formChanged.value=true;
	} else {
		document.iPSAndReviewForm.formChanged.value=false;
	}
	
	if(document.iPSAndReviewForm.dateChanged.value == 'true') {
		var newServiceMonth = document.iPSAndReviewForm.newAnnualReviewMonth.value;
		var newServiceDate = document.iPSAndReviewForm.newAnnualReviewDate.value;		
		
		var isInValidServiceDate = false;
		if ("31" == newServiceDate) {
			if ("02" == newServiceMonth || "04" == newServiceMonth
					|| "06" == newServiceMonth
					|| "09" == newServiceMonth
					|| "08" == newServiceMonth
					|| "11" == newServiceMonth) {
				isInValidServiceDate = true;
			} else {
				isInValidServiceDate = false;
			}
		} else if (("30" == newServiceDate || "29" == newServiceDate)
				&& "02" == newServiceMonth) {
				isInValidServiceDate = true;
		} else {
			isInValidServiceDate =  false;
		}		
		
		if(!isInValidServiceDate) {
			var m_names = new Array("","January", "February", "March",
					"April", "May", "June", "July", "August", "September",
					"October", "November", "December");
			if(newServiceMonth < 10) {
				newServiceMonth = newServiceMonth.replace('0','');
			}
			var newReviewDate = '';
			var newServiceMonthText = m_names[newServiceMonth];
			if(newServiceDate < 10){
				newReviewDate = newServiceMonthText.substr(0,3)+' '+0+newServiceDate;
			} else {
				newReviewDate = newServiceMonthText.substr(0,3)+' '+newServiceDate;
			}		
			if(newReviewDate !='${iPSAndReviewForm.annualReviewDate}') {
				var newDate = populateNewServicedate(newServiceMonth, newServiceDate, newServiceMonthText);		
				
				if('ipsNewAnnualReviewDateSaveConfirmationText' == popupContent) {
					confirmAction = confirm('<content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateSaveConfirmationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsNewAnnualReviewDateNoStatusChangeSaveConfirationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsExternalNewAnnualReviewDateSaveConfirmationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsExternalNewAnnualReviewDateSaveConfirmationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				} else if('ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText' == popupContent) {
					confirmAction =  confirm('<content:getAttribute attribute="text" beanName="ipsExternalNewAnnualReviewDateNoStatusChangeSaveConfirationText"><content:param>'+newDate+'</content:param></content:getAttribute>');
				}		
			}
		}
	}
	document.iPSAndReviewForm.action.value=button;
	if(confirmAction) {
		document.forms.iPSAndReviewForm.submit();
	} else {
		return false;
	}
	
}

function populateNewServicedate(newServiceMonth, newServiceDate, newServiceMonthText) {
	var newServiceDateToDisplay = '';
	var currentDate = new Date();		
	
	var currentMonth = (currentDate.getMonth()+1);
	var currentDay = currentDate.getDate();
	
	if(newServiceMonth > currentMonth) {
		newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+currentDate.getFullYear();
	} else if(newServiceMonth == currentMonth) {
		if(newServiceDate >= currentDay) {
			newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+currentDate.getFullYear();
		} else {
			newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+(currentDate.getFullYear()+1);
		}
	} else {
		newServiceDateToDisplay = newServiceMonthText+" "+newServiceDate+", "+(currentDate.getFullYear()+1);
	}
	
	return newServiceDateToDisplay;
}

function doCancel(button){
	if(discardChanges('The action you have selected will cause your changes to be lost. Select OK to continue or cancel to return.')){
		document.iPSAndReviewForm.action.value=button;
		document.forms.iPSAndReviewForm.submit();
	}
}

function CalculateTotal() {
    var total = 0;
    var rowCount = ${iPSAndReviewForm.criteriaAndWeightingPresentationList.size()};
 	// Iterate and get the value from weightings text box
    for (var i=0; i < rowCount; ++i) {
        // Get the current field
        var form_field = document.getElementsByName("criteriaAndWeightingPresentationList["+i+"].weighting");
		if (!isNaN(form_field[0].value)) {
			var weighting = parseInt(form_field[0].value);
			// Count only the whole numbers
			if (weighting >0 && form_field[0].value.toString().indexOf(".") == -1){
				total += weighting;		
		}
	   }
    }
	document.getElementById('totalWeight').innerHTML = total+'%';
	document.iPSAndReviewForm.totalWeighting.value=total;
	document.iPSAndReviewForm.criteriaChanged.value=true;
}

function serviceDateChanged() {
	document.iPSAndReviewForm.dateChanged.value=true;
}

function criteriaWeightingChanged() {
	document.iPSAndReviewForm.criteriaChanged.value=true;
}
</script>
<%
	String criteriaDescMap=(String)request.getAttribute("criteriaDescMap");
	pageContext.setAttribute("criteriaDescMap", criteriaDescMap, PageContext.PAGE_SCOPE); 
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td valign="top">
		<table width="100%">

<c:forEach items="${iPSAndReviewForm.criteriaAndWeightingPresentationList}" var="criteriaAndWeightingId" varStatus="index">

<c:set var="theIndex" value="${index}"/>
				<tr>
					<td align="center">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="11" width="11"
								style="background:${criteriaAndWeightingId.colorCode}"><img
								src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
						</tr>
					</table>
					</td>
					<td align="left">	
					<c:if test="${empty param.printFriendly}" >	
<form:select  path="criteriaAndWeightingPresentationList[${theIndex.index}].criteriaCode" cssClass="greyText" onchange="criteriaWeightingChanged()" size="1" ><%--  indexed="true"  --%>

							 <form:options items="${criteriaDescMap}"/> 
							<%-- <c:choose>
                           <c:when test="${criteriaAndWeightingId.criteriaCode != '' && criteriaAndWeightingId.criteriaCode != null}" >
                                <option value="${criteriaAndWeightingId.criteriaCode}">${criteriaAndWeightingId.criteriaDesc}</option>
                                
                           </c:when> 
                           <c:otherwise>
                           <!-- <option value="" selected="true">-------------- Select --------------</option> -->
                           </c:otherwise>
                           </c:choose>
                           <c:forEach var="item" items="${criteriaDescMap}">
       						 <option value="${item.key}">${item.value}</option>
       						 </c:forEach>  --%>
                           
								
								
</form:select>
						 <ps:trackChanges escape="true" property="criteriaCode" indexPrefix="criteriaAndWeightingId" name="iPSAndReviewForm" />  
					 </c:if>
					 <c:if test="${not empty param.printFriendly}" >
<form:select path="criteriaAndWeightingPresentationList[${theIndex.index}].criteriaCode" cssClass="greyText"  disabled="true" size="1" ><%--  - indexed="true" --%>

							<form:options items="${criteriaDescMap}"/> 
							<%-- <c:choose>
                           <c:when test="${criteriaAndWeightingId.criteriaCode != '' && criteriaAndWeightingId.criteriaCode != null}" >
                                <option value="${criteriaAndWeightingId.criteriaCode}">${criteriaAndWeightingId.criteriaDesc}</option>
                                
                           </c:when> 
                           <c:otherwise>
                           <!-- <option value="" selected="true">-------------- Select --------------</option> -->
                           </c:otherwise>
                           </c:choose>
                          // <form:options items="${criteriaDescMap}"></form:options>
                          <c:forEach var="item" items="${criteriaDescMap}">
       						 <option value="${item.key}">${item.value}</option>
       						 </c:forEach> --%>
</form:select>
					 </c:if>
					</td>
					<td align="left">
					<c:if test="${empty param.printFriendly}" >
						 <ps:trackChanges escape="true" property="weighting" indexPrefix="criteriaAndWeightingId" name="iPSAndReviewForm" /> 
<input type="text" name="criteriaAndWeightingPresentationList[${theIndex.index}].weighting" value="${criteriaAndWeightingId.weighting}" maxlength="3" onchange="CalculateTotal()" size="1" cssClass="greyText"/>
<%--  <form:input path="criteriaAndWeightingPresentationList[${theIndex.index}].weighting" id="criteriaAndWeightingId[${theIndex.index}].weighting" maxlength="3" onchange="CalculateTotal()" size="3" value="${criteriaAndWeightingId.weighting}" cssClass="greyText" > form:input - indexed="true" name="criteriaAndWeightingId"


 </form:input --%>
   <td>%</td>
					</c:if>
					<c:if test="${not empty param.printFriendly}" >
<input type="text" value="${criteriaAndWeightingId.weighting}" maxlength="3" disabled="true" onchange="CalculateTotal()" size="1" cssClass="greyText"/>
    
 <td>%</td>
					</c:if>
					</td>
				</tr>
</c:forEach>
			<tr>
				<td colspan="3"><img src="/assets/unmanaged/images/s.gif"
					height="2"></td>
			</tr>
		</table>
		</td>
		<td class="datacell1" align="center" valign="top"><ps:pieChart
			beanName="${constants.IPSR_CRITERIA_WEIGHTING_PIECHART}"
			alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative."
			title="IPSR Criteria And Weighting" /></td>
	</tr>
	<tr>
		<td valign="top">
		<table width="100%">
			<tr>
				<td width="8%"></td>
				<td width="70%" align="left" valign="top"><span class="highlightBold">Total</span></td>
<td width="22%" align="left" valign="top"><span id = "totalWeight" class="highlightBold">${iPSAndReviewForm.totalWeighting}%</span></td>

			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="3"><img src="/assets/unmanaged/images/s.gif"
			height="10"></td>
	</tr>
	<ps:isInternalUser name="userProfile" property="role">
		<tr>
			<td colspan="3">&nbsp;<content:getAttribute attribute="text" beanName="saveButtonHoverOverText"/></td>
		</tr>
	</ps:isInternalUser>
</table>
