<%-- taglib used --%>

<%@page import="java.util.Date"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%-- <%@ taglib prefix="ps" uri="manulife/tags/ps" %> --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Imports --%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.DeferralReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusUtils" %>
<%@ page import="com.manulife.pension.ps.web.census.util.DeferralUtils" %>
<%@page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants"%>
<%@page import="com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData"%>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.service.contract.valueobject.DayOfYear"%>
<%@page import="org.apache.commons.lang.StringUtils"%><style type="text/css">

DIV.scroll {
  OVERFLOW: auto;
  WIDTH: auto;
  BORDER-TOP-STYLE: none;
  BORDER-RIGHT-STYLE: none;
  BORDER-LEFT-STYLE: none;
  HEIGHT: 115px;
  BACKGROUND-COLOR: #fff;
  BORDER-BOTTOM-STYLE: none;
  padding: 8px;
  visibility: visible;
}
</style>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%
DeferralReportData theReport = (DeferralReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
DeferralReportForm deferralReportForm = (DeferralReportForm)session.getAttribute("deferralReportForm");
pageContext.setAttribute("deferralReportForm",deferralReportForm,PageContext.PAGE_SCOPE);

%> 


<%-- <jsp:useBean id="deferralReportForm" scope="session" type="com.manulife.pension.ps.web.census.DeferralReportForm" /> --%>



                                   
<content:contentBean contentId="<%=ContentConstants.NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="NoSearchResults"/>                                                                                          

<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>"
                    id="warningDiscardChanges"/>
                    
<content:contentBean contentId="60034" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="terms" />

                    
<%

boolean errors = false;

boolean showPayrollColumn = deferralReportForm.getHasPayrollNumberFeature();
boolean showDivisionColumn = deferralReportForm.getHasDivisionFeature();
int numOfDisplayColumns = DeferralUtils.getNumberOfDisplayColumns(request);
String  numberOfDisplayColumns = String.valueOf(numOfDisplayColumns);
int numOfDisplayColumnsLessTwo = numOfDisplayColumns - 2;
String  numberOfDisplayColumnsLessTwo = String.valueOf(numOfDisplayColumnsLessTwo);

%>

<c:set var="NO_ACTION" value="NO_ACTION" scope="page"/>
  
<script type="text/javascript" >
  var formHasChanged = false;

  // Used to prevent multiple submit
  var submitInd = false;
  
  var isExplorer=eval(navigator.appVersion.indexOf("MSIE")!=-1);
  
  // Called when Save is clicked
  function doSave() { 
    // Used to prevent multiple submit
    if(submitInd) {
      return false;
    }
    
    submitInd = true;
            
    if (document.forms['deferralReportForm']) {                   
      if(isExplorer) {
        document.forms['deferralReportForm'].elements['task'].value = "save"; 
      } else {
        document.forms['deferralReportForm'].elements['task'].value = "save"; 
        document.forms['deferralReportForm'].submit();
      }
    }
    
    return true;
  }
  
  function doContinue() {
    // Used to prevent multiple submit
    if(submitInd) {
      return false;
    }
    
    submitInd = true;
            
    if (document.forms['deferralReportForm']) {                   
      if(isExplorer) {
        document.forms['deferralReportForm'].elements['task'].value = "continue"; 
      } else {
        document.forms['deferralReportForm'].elements['task'].value = "continue"; 
        document.forms['deferralReportForm'].submit();
      }
    }
    
    return true;  
  }
  
  
  function clearName(evt){
  
      //IE or browsers that use the getElementById model
      if (document.getElementById('namePhrase')) {
        if (document.getElementById('namePhrase').value) {
          document.getElementById('namePhrase').value = "";
        }
      }
    
      //Netscape or browsers that use the document model
        evt = (evt) ? evt : (window.event) ? event : null;
        if (evt)
        { 
          var charCode = (evt.charCode) ? evt.charCode :
                       ((evt.keyCode) ? evt.keyCode :
                       ((evt.which) ? evt.which : 0));
          if (charCode == 9) {
            return false;
          }
        } 
      
      if (document.deferralReportForm.ssnOne) {
          if (document.deferralReportForm.ssnOne.value.length >= 0){
              document.deferralReportForm.namePhrase.value = "";
          }
      }
      
      if (document.deferralReportForm.ssnTwo) {
          if (document.deferralReportForm.ssnTwo.value.length >= 0){ 
            document.deferralReportForm.namePhrase.value = "";
          }
      }
    
      if (document.deferralReportForm.ssnThree) {
          if (document.deferralReportForm.ssnThree.value.length >= 0){ 
            document.deferralReportForm.namePhrase.value = "";
          }
      } 
    }
  
  function clearSSN(evt){
  
    //IE or browsers that use the getElementById model
    if (document.getElementById('ssnOne')) {
      if (document.getElementById('ssnOne').value) {
        document.getElementById('ssnOne').value = "";
      }
    } 
  
    if (document.getElementById('ssnTwo')) {
      if (document.getElementById('ssnTwo').value) {
        document.getElementById('ssnTwo').value = "";
      }
    } 
  
    if (document.getElementById('ssnThree')) {
      if (document.getElementById('ssnThree').value) {
        document.getElementById('ssnThree').value = "";
      }
    }  
    
    //Netscape or browsers that use the document model
    evt = (evt) ? evt : (window.event) ? event : null;
      if (evt)
      { 
        var charCode = (evt.charCode) ? evt.charCode :
                     ((evt.keyCode) ? evt.keyCode :
                     ((evt.which) ? evt.which : 0));
        if (charCode == 9) {
          return false;
        }
      }    
    
    if (document.deferralReportForm.namePhrase) {
        if (document.deferralReportForm.namePhrase.value.length >= 0){
          document.deferralReportForm.ssnOne.value = "";
          document.deferralReportForm.ssnTwo.value = "";
          document.deferralReportForm.ssnThree.value = "";
        } 
    }
    
  }
    
  /**
   * Enables the submit buttons on first change
   */
  function enableSubmit() {
    var x=document.getElementById("saveButton");
    x.innerHTML='<input name="button" type="submit" class="button134" value="SAVE" onclick="return doSave();" />';
    
    x=document.getElementById("cancelButton");
    x.innerHTML='<input name="button" type="submit" class="button134" value="CANCEL" onclick="return doCancel();" />';
     
    formHasChanged = true; 
    return false;
  }
    
  function clearLimitAndAmount(indexValue) {     
    var increaseAmtValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].increaseAmt'];   
    var increasePctValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].increasePct'];   
    var limitAmtValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].limitAmt'];   
    var limitPctValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].limitPct'];   
    var limit = document.getElementById("limitID" + indexValue);
    var increase = document.getElementById("increaseID" + indexValue);
    var flag = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].aciSettingsInd'];
    var defType = document.getElementsByName('planDeferralType')[0];
    var incType = document.getElementsByName('theItem1[' + indexValue + '].increaseType')[0];
    
    if (flag && flag.value && flag.value != 'Y') {
      increaseAmtValue="";
      increasePctValue="";
      limitAmtValue="";
      limitPctValue="";
      limit.innerHTML='<input type="text" name="theItem1[' + indexValue + '].limit" maxlength="5" size="2" value="" style="inputField" disabled="true" >';
      increase.innerHTML='<input type="text" name="theItem1[' + indexValue + '].increase" maxlength="4" size="2" value="" style="inputField" disabled="true" >';  
    } else {
      increaseAmtValue="";
      increasePctValue="";
      limitAmtValue="";
      limitPctValue="";
      limit.innerHTML='<input type="text" name="theItem1[' + indexValue + '].limit" maxlength="5" size="2" value="" style="inputField" >';
      increase.innerHTML='<input type="text" name="theItem1[' + indexValue + '].increase" maxlength="4" size="2" value="" style="inputField" >';  
    } 
   
    if (flag && flag.value && flag.value == 'Y')
    {    	
    	if(defType != null && defType.value == 'E')
    	{
    		incType.disabled = false;
    	}
    }else {
    	if(defType != null && defType.value == 'E')
    	{
    		incType.disabled = true;
    	}
    }
    return true;
  }
    
  /**
   * Makes the fields available
   */
  function switchBetweenAmtAndPct(indexValue) { 
    var increaseType = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].increaseType'];    
    var increaseAmtValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].increaseAmt'];   
    var increasePctValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].increasePct'];   
    var limitAmtValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].limitAmt'];   
    var limitPctValue = document.forms['deferralReportForm'].elements['theItem1[' + indexValue + '].limitPct'];   
    var limit = document.getElementById("limitID" + indexValue);
    var increase = document.getElementById("increaseID" + indexValue);
    
    
    if (increaseType != null) {
      if (increaseType.options[increaseType.selectedIndex].value != "%") { 
        limit.innerHTML='<input type="text" name="theItem1[' + indexValue + '].limit" maxlength="4" size="2" value="' + limitAmtValue.value + '" style="inputField">';
        increase.innerHTML='<input type="text" name="theItem1[' + indexValue + '].increase" maxlength="4" size="2" value="' + increaseAmtValue.value + '" style="inputField">';
      } else {
        limit.innerHTML='<input type="text" name="theItem1[' + indexValue + '].limit" maxlength="4" size="2" value="' + limitPctValue.value + '" style="inputField">';        
        increase.innerHTML='<input type="text" name="theItem1[' + indexValue + '].increase" maxlength="4" size="2" value="' + increasePctValue.value + '" style="inputField">';
      }
    } 
    
    return true;
  }

  function enableDNI(indexValue) {
	  var inputs = document.getElementById("deferralReportForm").elements;
    var flag = inputs['theItem1[' + indexValue + '].aciSettingsInd'];
    var targetElement = document.getElementById('nadItem'+indexValue);
    if (flag && flag.value && flag.value != 'Y') {
        targetElement.style.visibility='hidden';
        targetElement.style.display='none';    
    } else {
        targetElement.style.visibility='visible';
        targetElement.style.display='block';
    
    }  
    
  }
  
  function openStatisticsWindow(){
    windowOptions = "'toolbar=yes,location=no,scrollbars=yes,resizable=no,width=610,height=680,left=250,top=50'"
    changeWindow = window.open('/do/census/deferralStatsSnapshot/','changeWin',windowOptions)
  }
  
  function removeNotSelectedOption(dropdown, indexValue) { 
  
	  if(dropdown != null && dropdown.options[0].value == " "){
			dropdown.options[0] = null;
			clearLimitAndAmount(indexValue)
	  }
		
	}
		
</script>

<c:if test="${empty param.printFriendly }" >

<script type="text/javascript" >
  
  // Called when Cancel is clicked
  function doCancel() { 
    // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
          
      if (document.forms['deferralReportForm']) {              
        document.forms['deferralReportForm'].submit();
      } else {
        document.forms.deferralReportForm.submit();
      }
      
      return true;
    }
  }
  
  // Called when reset is clicked
  function doReset() {    
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      document.deferralReportForm.task.value = "reset";
            
      if(isExplorer) {
        // Nothing has to be done
      } else {
        if (document.forms['deferralReportForm']) {              
          document.forms['deferralReportForm'].submit();
        } else {
          document.forms.deferralReportForm.submit();
        }
      }
      
      return true;
    }
  }
  
  // Called when Search is clicked
  function doSubmit(){    
    // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName='warningDiscardChanges' attribute='text' filter='true'/>") == false) {      
      return false;
    } 
    
    if(isExplorer) {
      // Nothing has to be done
    } else {
      if (document.forms['deferralReportForm']) {              
        document.forms['deferralReportForm'].submit();
      } else {
        document.forms.deferralReportForm.submit();
      }
    }
      
    return true; 
  }
  
  // Called when add employee is clicked
  function doAdd() {
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      window.location="/do/census/addEmployee/?source=deferral";
      return true;
    }
  }
 

  // Protects all the links with checks for changes in the page with the exception of pop-ups
  function protectLinks() {
    var hrefs  = document.links;
    if (hrefs != null)
    {
      for (i=0; i<hrefs.length; i++) {
        if(
          hrefs[i].onclick != undefined && 
          (hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        }
        else if(
          hrefs[i].href != undefined && 
          (hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        }
        else if(hrefs[i].onclick != undefined) {
          hrefs[i].onclick = new Function ("var result = discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
        }
        else {
          hrefs[i].onclick = new Function ("return discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');");
        }
      }
    }
    
   }  
   
   // Track changes framework
   function isFormChanged() {
     if (formHasChanged) return true;
     return changeTracker.hasChanged();
   }
    
   // Registration required by Track changes framework
   registerTrackChangesFunction(isFormChanged);
   
   // Registration required to be notified on leaving the page
   if (window.addEventListener) {
     window.addEventListener('load', protectLinks, false);
   } else if (window.attachEvent) {
     window.attachEvent('onload', protectLinks);
   }
   
</script>
</c:if>


<%-- Remove the extra column at the before the report --%>
<c:if test="${empty param.printFriendly }" >
  <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  <td >
</c:if>

<c:if test="${not empty param.printFriendly }" >
  <td>
</c:if>
<c:if test="${empty param.printFriendly }" >
  <p>
 <%--  <c:if test="${not empty sessionScope.psErrors}"> --%>
<c:set var="errorsExist" value="${true}" scope="page" />
    <div id="errordivcs"><content:errors scope="session"/></div>
 <%--  </c:if> --%>
  </p>
</c:if>

  <ps:form method="POST" modelAttribute="deferralReportForm" name="deferralReportForm" action="/do/census/deferral" >
  
  	
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="currentDate" /><%--  input - name="deferralReportForm" --%>
<%-- <form:hidden path="initialSearch"/> --%>
<input type="hidden" name="loadedOnce" value="true"/>
<input type="hidden" name="planDeferralType" /><%--  tag input - name="deferralReportForm" --%>

  <%-- terms and conditions section - display only if user has not accepted it before --%>
  <% if (deferralReportForm.isTermsAndConditionsAccepted() == false) { %>
    
  	     <table border="0" cellspacing="0" cellpadding="0" class="box" vAlign="top" >
              <tr class="tablehead">
                <td colspan="3" class="tableheadTD1"><b><content:getAttribute id='terms' attribute='title'/></b></td>
              </tr>
              <tr>
                <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody">
                   <content:getAttribute id='terms' attribute='text'/>
 				</td>			 	
  	            <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr>
                <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              
                <td align="right">
                      <input name="button" type="submit" class="button134" value="continue" onclick="doContinue();" />&nbsp;
                </td>
  	            <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                
              </tr>
              <tr>
                <td colspan="3">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                </td>
              </tr>              
  		</table>
 
        <br/>&nbsp;
        <br/>
  <% }  %>

<%-- Start of the search table --%>
<table width="700" border="0" cellspacing="0" cellpadding="0">
    
  <%-- TAB section --%>
  <tr>
      <td valign="bottom" colspan="<%=numberOfDisplayColumns%>">
        <DIV class="nav_Main_display" id="div">
          <UL class="">
          	<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
			<c:if test="${empty param.printFriendly }" >
	          	<A href="/do/census/censusSummary/">
	        </c:if>
	        Summary
	        <c:if test="${empty param.printFriendly }" >
	        	</A>
	        </c:if>
	        </LI>
	        <LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
	        <c:if test="${empty param.printFriendly }" >
	          	<A href="/do/participant/participantAddresses">
	        </c:if>
	        Addresses
	        <c:if test="${empty param.printFriendly }" >
	        	</A>
	        </c:if>
	        </LI>
			<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
			<c:if test="${empty param.printFriendly }" >
				<A href="/do/census/employeeEnrollmentSummary">
			</c:if>
			Eligibility
			<c:if test="${empty param.printFriendly }" >
	        	</A>
	        </c:if>
			</LI>
			
			<LI id="tab4" class="on">Deferrals</LI>
   
<c:if test="${deferralReportForm.allowedToAccessVestingTab ==true}">
			<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
			<c:if test="${empty param.printFriendly }" >
				<A href="/do/census/censusVesting/">
			</c:if>
			Vesting
			<c:if test="${empty param.printFriendly }" >
	        	</A>
	        </c:if>
			</LI>
</c:if>
          </UL>
        </DIV>
      </td>
  </tr>
  <tr>
    <td colspan="<%=numberOfDisplayColumns%>" height="25" class="tablesubhead"><b>Employee Search</b></td>
  </tr>
  <% 
    boolean disableSearch = false;
    if (deferralReportForm.isTermsAndConditionsAccepted() == false) { 
    	disableSearch = true;	
    } 
    %>
    
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>" >
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tbody class="datacell2" >
            <tr>
              <td colspan="4" valign="top" class="filterSearch">
                <p>To search for a participant by last name or SSN, make your selection bellow and click "search" to complete your request.</p>
              </td>              
            </tr>            
            <tr>
              <td width="34%" valign="bottom" class="filterSearch"><b>Segment</b><br>         
                <c:if test="${not empty param.printFriendly }" >
<form:select path="segment" disabled="true" >
<form:options items="${deferralReportForm.segmentList}" itemValue="value" itemLabel="label"/>
</form:select>
                </c:if>
                <c:if test="${empty param.printFriendly }" >
 <% if (disableSearch) {  %>
                   <form:select path="segment"  tabindex="10" disabled="true">
<form:options items="${deferralReportForm.segmentList}" itemValue="value" itemLabel="label"/> 
                  </form:select>                  
<% } else { %>
                   <form:select path="segment"  tabindex="10" >
 <form:options  items="${deferralReportForm.segmentList}" itemValue="value" itemLabel="label"/> 
                  </form:select>       
<%} %>               
                </c:if>         
              </td>
              <td width="22%" valign="bottom" class="filterSearch"><strong><span id="label_lastName"><b>Last name</b></span></strong><br>         
                <c:if test="${not empty param.printFriendly }" >
<form:input path="namePhrase" readonly="true" cssStyle="inputField"/>
                </c:if>
                <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) {  %>                
<form:input path="namePhrase" disabled="true" onkeyup="clearSSN(event);" cssStyle="inputField"/>
<% } else { %>
<form:input path="namePhrase" onkeyup="clearSSN(event);" cssStyle="inputField"/>
<% } %>                  
                </c:if> 
              </td>
              <td width="160" valign="bottom" class="filterSearch"><strong><span id="label_ssn"><b>SSN</b></span></strong><br>        
                <c:if test="${not empty param.printFriendly }" >
                  <form:password path="ssnOne" value="${deferralReportForm.ssnOne}" maxlength="3" size="3" style="inputField" readonly="true" id="ssnOneId"/>
                  <form:password path="ssnTwo" value="${deferralReportForm.ssnTwo}" maxlength="2" size="2" style="inputField" readonly="true" id="ssnTwoId" />
                  <form:input path="ssnThree" autocomplete="off" maxlength="4" size="4" style="inputField" readonly="true" id="ssnThreeId" />       
                </c:if>
                <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) {  %>                
                  <form:password path="ssnOne" value="${deferralReportForm.ssnOne}" maxlength="3" size="3" onkeyup="return autoTab(this, 3, event);" onkeypress="clearName(event);" style="inputField" disabled="true" id="ssnOneId" />
                  <form:password path="ssnTwo" value="${deferralReportForm.ssnTwo}" maxlength="2" size="2" onkeyup="return autoTab(this, 2, event);" onkeypress="clearName(event);" style="inputField" disabled="true" id="ssnTwoId"/>
                  <form:input path="ssnThree" autocomplete="off" maxlength="4" size="4" onkeyup="return autoTab(this, 4, event);" onkeypress="clearName(event);" style="inputField" disabled="true" id="ssnThreeId"/>
<% } else { %>
                  <form:password path="ssnOne" value="${deferralReportForm.ssnOne}" maxlength="3" size="3" onkeyup="return autoTab(this, 3, event);" onkeypress="clearName(event);" style="inputField" id="ssnOneId"/>
                  <form:password path="ssnTwo" value="${deferralReportForm.ssnTwo}" maxlength="2" size="2" onkeyup="return autoTab(this, 2, event);" onkeypress="clearName(event);" style="inputField" id="ssnTwoId"/>
                  <form:input path="ssnThree" autocomplete="off" maxlength="4" size="4" onkeyup="return autoTab(this, 4, event);" onkeypress="clearName(event);" style="inputField" id="ssnThreeId" />
<% } %>                          
                </c:if> 
              </td>
              <td width="160" valign="bottom" class="filterSearch">
                  <strong>Employment status </strong><br>   
                  <c:if test="${not empty param.printFriendly }" >
                    <form:select path="employmentStatus" disabled="true" >
            					<%-- set the first value of the select --%>
            					<form:option value="All">All</form:option>
<form:options items="${deferralReportForm.statusList}"  itemLabel="label" itemValue="value"/>
</form:select>
                  </c:if>
                  <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) {  %>
                    <form:select path="employmentStatus" disabled="true">
            					<%-- set the first value of the select --%>
            					<form:option value="All">All</form:option>
<form:options items="${deferralReportForm.statusList}"  itemLabel="label" itemValue="value"/>
</form:select>
<% } else { %>
                    <form:select path="employmentStatus">
            					<%-- set the first value of the select --%>
            					<form:option value="All">All</form:option>
<form:options items="${deferralReportForm.statusList}"  itemLabel="label" itemValue="value"/>
</form:select>
<% } %>          			
                  </c:if>                  
              </td>              
            </tr>
            <tr>
              <td width="34%" valign="top" class="filterSearch">
                <strong>Please note:</strong>
                <br><%=DeferralUtils.getPlanDeferralLimitMessage(deferralReportForm)%>
                <br><%=DeferralUtils.getDefaultDeferralLimitMessage(deferralReportForm)%>
                <br><%=DeferralUtils.getDefaultDeferralIncreaseMessage(deferralReportForm)%> 
              </td>
              <%if(showDivisionColumn){%>          
              <td width="22%" valign="top" class="filterSearch">
                  <strong>Division</strong><br>   
                  <c:if test="${not empty param.printFriendly }" >    
<form:input path="division" readonly="true" cssStyle="inputField"/>
                  </c:if> 
                  <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) { %>                  
<form:input path="division" disabled="true" cssStyle="inputField"/>
<% } else {  %>
<form:input path="division" cssStyle="inputField"/>
<% }  %>                    
                  </c:if>
              </td>
              <% } else { %>
              <td width="22%" valign="top" class="filterSearch">
                  &nbsp;
              </td>
              <%}%>
              <td width="22%" valign="top" class="filterSearch">
              </td>
              <td width="22%" valign="top" class="filterSearch">
                  <strong>Enrollment status </strong><br>   
                  <c:if test="${not empty param.printFriendly }" >
                    <form:select path="enrollmentStatus" disabled="true" >                    
                      <form:option value="All">All</form:option>  
                      <c:if test="${deferralReportForm.ezStartOn}">                  
                      	<form:option value="Pending Enrollment">Pending enrollment</form:option>
                      	<form:option value="Pending Eligibility">Pending eligibility</form:option>
                      </c:if>
                      <form:option value="Not eligible">Not eligible</form:option>
                      <form:option value="No Account">No account</form:option>
                      <form:option value="Enrolled">Enrolled</form:option>                    
</form:select>
                  </c:if>
                  <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) { %>                  
                    <form:select path="enrollmentStatus" disabled="true">
                      <form:option value="All">All</form:option>  
                      <c:if test="${deferralReportForm.ezStartOn}">                                    
                      	<form:option value="Pending Enrollment">Pending enrollment</form:option>
                      	<form:option value="Pending Eligibility">Pending eligibility</form:option>
                      </c:if>
                      <form:option value="Not eligible">Not eligible</form:option>
                      <form:option value="No Account">No account</form:option>
                      <form:option value="Enrolled">Enrolled</form:option>                    
</form:select>
                                       
<% } else {  %>
                    <form:select path="enrollmentStatus" >
                      <form:option value="All">All</form:option>  
                      <c:if test="${deferralReportForm.ezStartOn}">                                    
                      	<form:option value="Pending Enrollment">Pending enrollment</form:option>
                      	<form:option value="Pending Eligibility">Pending eligibility</form:option>
					  </c:if>
                      <form:option value="Not eligible">Not eligible</form:option>
                      <form:option value="No Account">No account</form:option>
                      <form:option value="Enrolled">Enrolled</form:option>                    
</form:select>
<% } %>                    
                  </c:if>
              </td>
            </tr>
            <tr>
              <td colspan="3" valign="top" class="filterSearch">
                <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) { %>
                    <a href="" onclick="return false;" disabled >View deferral stats </a> </br>
                    <a href="" onclick="return false;" disabled >Deferral Tasks </a>
<% } else { %>
                    <a href="javascript:openStatisticsWindow()">View deferral stats </a></br>
                    <a href="/do/participant/taskCenterTasks" >Deferral Tasks </a>
<% } %>                
                </c:if>                
              </td>
              <td width="150" valign="top" class="filterSearch">
                <c:if test="${empty param.printFriendly }" >
<% if (disableSearch) { %>                
<input type="submit" onclick="return doSubmit();" disabled="true" tabindex="48" value="search"/>&nbsp;&nbsp;&nbsp;
<input type="submit" onclick="return doReset();" disabled="true" tabindex="49" value="reset"/>
<% } else { %>
<input type="submit" onclick="return doSubmit();" tabindex="48" value="search"/>&nbsp;&nbsp;&nbsp;
<input type="submit" onclick="return doReset();" tabindex="49" value="reset"/>
<% } %>                  
                </c:if>  
              </td>
            </tr>                          
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>" > 
        <jsp:include flush="true" page="employeeSnapshotErrors.jsp"/>       
      </td>
    </tr>

 <% if (deferralReportForm.isTermsAndConditionsAccepted()) { %>  

    <%-- Leave a space --%>
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>" >&nbsp;</td>
    </tr>
    <%-- Legend --%>
    <c:if test="${empty param.printFriendly}" > 
      <tr>
        <td colspan="<%=numberOfDisplayColumns%>" >
          <strong>Legend:&nbsp;</strong>
          <IMG height=12 src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>&nbsp; View &nbsp;
          <IMG height=12 src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>&nbsp; Edit &nbsp;
          <IMG height=12 src="/assets/unmanaged/images/history_icon.gif" width=12 border=0>&nbsp; Participant transaction history &nbsp;          
        </td>
      </tr>
    </c:if>

    <%-- Set the column spacing for the report if it has payroll or division or neither --%>
    <%= DeferralUtils.getTableHeader(request)%>
  
      <tr class="tablehead">
        <td colspan="<%=numberOfDisplayColumns%>" class="tableheadTD1" >            
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td align="left" width="55%"></td>                
                  <td class="tableheadTDinfo" align="center" width="25%"><b><report:recordCounter report="theReport" label="Employees"/></b></td>
                  <td class="tableheadTDinfo" align="right" width="20%"><b><report:pageCounter report="theReport" formName="deferralReportForm"/></b></td>
                </tr>
              </table>
        </td>
      </tr>
      <tr class="tablesubhead">
      <td rowspan="3" vAlign="bottom" class="databorder" width="1"></td>
      <c:if test="${empty param.printFriendly }" > 
        <td rowspan="3" vAlign="top"></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      </c:if>
      <td rowspan="3" vAlign="top"></td>
      <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <%if(showPayrollColumn){%>
        <td rowspan="3" vAlign="top"></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <%}%>
      <%if(showDivisionColumn){%>
        <td rowspan="3" vAlign="top"></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <%}%>        
      <td colspan="3" vAlign="top"><div align="center"><b><NOBR>Deferral</b></NOBR></div></td> 
      <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <% if(!deferralReportForm.isACIOff()) { %>
        <td rowspan="3" vAlign="top"></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td rowspan="3" vAlign="top"></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td rowspan="3" vAlign="top"></td>        
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td colspan="3" vAlign="top"><div align="center"><b><NOBR>Personal</b></NOBR></div></td> 
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <% } %>
      <td rowspan="3" vAlign="top"></td>     
      <td rowspan="3" vAlign="bottom" class="databox"></td>
      <td rowspan="3" vAlign="bottom" class="databorder"></td>
    </tr>
    <tr class="tablesubhead">
    <td colspan="3" vAlign=bottom class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>                            
  </tr>          
  <tr class="tablesubhead">     
  	<td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
  </tr>
      <tr class="tablesubhead">
        <td rowspan="3" vAlign="bottom" class="databorder" width="1"></td>
        <c:if test="${empty param.printFriendly}" > 
          <td rowspan="3" vAlign="top"><b>Action</b></td>
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </c:if>
        <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="lastName" direction="asc">Name</report:sort></b></NOBR></td>
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <%if(showPayrollColumn){%>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="employerDesignatedID" direction="desc">Employee<br/> ID</b></report:sort></NOBR></td>
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <%}%>
        <%if(showDivisionColumn){%>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="division" direction="asc">Division</b></report:sort></NOBR></td>
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <%}%>        
        <td rowspan="3" vAlign="top"><b><NOBR>Type</b></NOBR></td>  
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="beforeTaxDeferralPct" direction="asc">Amount</b></report:sort></NOBR></td>  
        <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <%if(!deferralReportForm.isACIOff()) { %>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="autoIncrease" direction="asc">Scheduled&nbsp;&nbsp;<br/>deferral&nbsp;&nbsp;<br/>increase&nbsp;&nbsp;</b></report:sort></NOBR></td>
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="dateNextADI" direction="asc">Date of next<br/>increase<br/>(mm/dd/yyyy)</b></report:sort></NOBR></td>
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" vAlign="top"><b><NOBR>Payroll<br/> Type</b></NOBR></td>        
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm"field="increaseAmt" direction="asc">Increase<br/> amount</b></report:sort></NOBR></td>        
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" vAlign="top"><b><NOBR><report:sort formName="deferralReportForm" field="limitAmt" direction="asc">Limit</b></report:sort></NOBR></td>     
          <td rowspan="3" vAlign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <% } %>
        <td rowspan="3" vAlign="top"><b>Alert/<br/>Warning</b></td>     
        <td rowspan="3" vAlign="bottom" class="databox"></td>
        <td rowspan="3" vAlign="bottom" class="databorder"></td>
      </tr>
      <tr class="tablesubhead">
        <td colspan="3" vAlign=bottom class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></td>                            
      </tr>          
      <tr class="tablesubhead">     
      	<td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
      </tr>



 <%--Start of Details --%>
    <% boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
       boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
       boolean highlight = false; // used to determine if the style class has to change
    %>

  <c:if test="${empty param.printFriendly}" >
         
      <% if (theReport.getDetails() == null || theReport.getDetails().size() <= 0) { // we have no search results %>      
       <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="top" colspan="<%=numberOfDisplayColumnsLessTwo%>">
        <%-- no results --%>
          <c:if test="${empty pageScope.errorsExist}">
            <content:getAttribute id="NoSearchResults" attribute="text"/>
          </c:if>
        </td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       </tr>
        
      <% 
         }
         lastLineBkgrdClass = true;
       %>
  </c:if>
 
<c:if test="${not empty theReport.details}">

<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 

<% DeferralDetails theRecord = (DeferralDetails)pageContext.getAttribute("theItem");
Integer theindex=(Integer)pageContext.getAttribute("indexValue");

String temp = pageContext.getAttribute("indexValue").toString();
%>
      
  <% if (Integer.parseInt(temp) % 2 != 0) {
      beigeBackgroundCls = true;
      lastLineBkgrdClass = true; %>
          <tr class="datacell1">
  <% } else {
      beigeBackgroundCls = false;
      lastLineBkgrdClass = false; %>
        <tr class="datacell2">
  <% } 
    
     boolean canEditEZISettings = deferralReportForm.isAllowedToEdit() && 
                                  !deferralReportForm.getTheItem(Integer.parseInt(temp)).hasOutstandingEZiRequest()&& !deferralReportForm.isOptOutPeriod(deferralReportForm.getTheItem(Integer.parseInt(temp)).getNextAD(),deferralReportForm.getOptOutDays());
  
  %>
  
  <%-- Used to identify each row --%>
<input type="hidden" name="profileId" value="<%=deferralReportForm.getTheItem(Integer.parseInt(temp)).getProfileId() %>" />
<input type="hidden" name="beforeTaxDeferralAmt" value="${theItem.beforeTaxDeferralAmt}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="designatedRothDeferralPct" value="${theItem.designatedRothDeferralPct}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="designatedRothDeferralAmt" value="${theItem.designatedRothDeferralAmt}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="limitAmt" value="${theItem.limitAmt}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="limitPct" value="${theItem.limitPct}" /><%-- input - indexed="true" name="theItem" --%>
<input type="hidden" name="increaseAmt" value="${theItem.increaseAmt}" /><%-- input - indexed="true" name="theItem" --%>
<input type="hidden" name="increasePct" value="${theItem.increasePct}" /><%-- input - indexed="true" name="theItem" --%>
       
          <td class="databorder" width="1"></td>
            <c:if test="${empty param.printFriendly}" >
              <td align="left">
              <%--  <a href="/do/census/viewEmployeeSnapshot/?profileId=<%=deferralReportForm.getTheItem(theIndex.intValue()).getProfileId()%>&source=deferral">
                  <img src="/assets/unmanaged/images/view_icon.gif" alt="View Employee Snapshot" border="0"/></a><img src="/assets/unmanaged/images/s.gif" width="3" height="12" />
                <a href="/do/census/editEmployeeSnapshot/?profileId=<%=deferralReportForm.getTheItem(theIndex.intValue()).getProfileId()%>&source=deferral">
                  <img src="/assets/unmanaged/images/edit_icon.gif" alt="Edit Employee Snapshot" border="0"/>
                </a><br/><a href="/do/transaction/participantTransactionHistory/?profileId=<%=deferralReportForm.getTheItem(theIndex.intValue()).getProfileId() %>">
                  <img src="/assets/unmanaged/images/history_icon.gif" alt="View Participant Transaction History" border="0"/>
                </a> --%>
                 <report:actions profile="userProfile" item="theItem" action="evhDeferral"/> 
              </td>
              <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </c:if>
          <td align="left">
<c:if test="${userProfile.welcomePageAccessOnly == false}" >
<c:if test="${theItem.participantInd ==true}">
                <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.fullName}
                </ps:link>
</c:if>
<c:if test="${theItem.participantInd ==false}">
${theItem.fullName}
</c:if>
</c:if>
<c:if test="${userProfile.welcomePageAccessOnly == true}" >
${theItem.fullName}
</c:if>
            <br><NOBR><render:ssn property="theItem.ssn" /></NOBR>
          </td>            
          <td class="datadivider" width="1"></td>     
          <%if(showPayrollColumn){%>     
<td align="left">${theItem.employerDesignatedID}</td>
            <td class="datadivider" width="1"></td>          
          <%}%>
          <%if(showDivisionColumn){%>
<td align="left">${theItem.division}</td>
            <td class="datadivider" width="1"></td>          
          <%}%>   
          <c:if test="${(empty theItem.beforeTaxDeferralPct) && (empty theItem.beforeTaxDeferralAmt) && (empty theItem.designatedRothDeferralPct) && (empty theItem.designatedRothDeferralAmt)}">
            <td colspan="3" align="center" valign="center" align="left">Not on file</td>
          </c:if>       
          <c:if test="${(! empty theItem.beforeTaxDeferralPct) || (! empty theItem.beforeTaxDeferralAmt) || (! empty theItem.designatedRothDeferralPct) || (! empty theItem.designatedRothDeferralAmt)}">            
          <td valign="center" align="left">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top" align="right">                
                <td><c:if test="${(! empty theItem.beforeTaxDeferralPct) || (! empty theItem.beforeTaxDeferralAmt)}">
                401(k)</c:if>&nbsp;</td>
              </tr>
              <tr><td>&nbsp;</td></tr>
              <tr valign="bottom" align="right">
                <td><% if(deferralReportForm.hasRoth()) { %><c:if test="${(! empty theItem.designatedRothDeferralAmt) || (! empty theItem.designatedRothDeferralPct)}">Roth</c:if><% } %>&nbsp;</td>                
              </tr>
            </table>            
          </td>
          <td class="datadivider" width="1"></td>     
          <td valign="center" align="left">                        
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top" align="right">                
                <td>
                  <c:if test="${! empty theItem.beforeTaxDeferralPct}">
                    <render:number property='theItem.beforeTaxDeferralPct' defaultValue = ""  scale='6' type='p' pattern="###.###%"/>
                  </c:if>
                  <c:if test="${! empty theItem.beforeTaxDeferralAmt}">
                    <render:number property='theItem.beforeTaxDeferralAmt' defaultValue = ""  pattern="$###,##0.00"/>
                  </c:if>
                  &nbsp;
                </td>
              </tr>
              <tr><td>&nbsp;</td></tr>
              <tr valign="top" align="right">
                <td>
                  <% if(deferralReportForm.hasRoth()) { %>
                  <c:if test="${! empty theItem.designatedRothDeferralPct }">
                    <render:number property='theItem.designatedRothDeferralPct' defaultValue = ""  scale='6' type='p' pattern="###.###%"/>
                  </c:if>
                  <c:if test="${! empty theItem.designatedRothDeferralAmt}">
                    <render:number property='theItem.designatedRothDeferralAmt' defaultValue = ""  pattern="$###,##0.00"/>
                  </c:if>
                  <% } %>
                  &nbsp;
                </td>
              </tr>
            </table>            
          </td>
          </c:if>
          <td class="datadivider" width="1"></td>  
          <% if(!deferralReportForm.isACIOff()) { %>
          <td align="center">            
            <c:if test="${not empty param.printFriendly}">
<form:select path="theItem1[${indexValue}].aciSettingsInd" disabled="true"  cssStyle="boxbody"><%-- form:select - indexed="true" property="aciSettingsInd" value="<%=theItem.getAciSettingsInd()%>" --%>
                <form:option value=""></form:option>
                <form:option value="Y">On</form:option>
                <form:option value="N">Off</form:option>               
</form:select>
            </c:if>
<c:if test="${empty param.printFriendly}"> 
              <%=DeferralUtils.getAutoIncreaseFlagError(theRecord) %>
              <c:set var="autoIncreaseValue" value="${theItem.autoIncreaseHistory}" scope="page"/>
              <c:set var="ACIIndicator" value="Scheduled deferral increase" scope="page"/>
              
              <% if(canEditEZISettings && deferralReportForm.getTheItem(Integer.parseInt(temp)).isParticipantInd()
            		  && "AC".equals(deferralReportForm.getTheItem(Integer.parseInt(temp)).getParticipantStatusCode())) { %>
<c:if test="${theItem.autoIncreaseChanged ==false}">
                    <div onmouseover="<ps:aciChangeDetail name='' dataElementName='NO_ACTION' />" >
</c:if>
<c:if test="${theItem.autoIncreaseChanged ==true}">
                    <div onmouseover="<ps:aciChangeDetail name='autoIncreaseValue' dataElementName='ACIIndicator' />" >
</c:if>

 <form:select  path="theItem1[${indexValue}].aciSettingsInd" onclick="removeNotSelectedOption(this,'${indexValue}');" onchange="enableDNI('${indexValue}');clearLimitAndAmount('${indexValue}');" onmousedown="enableSubmit();" ><%-- form:select - indexed="true" property="aciSettingsInd" style="boxbody" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getAciSettingsInd() %>" --%>
								<form:option value="Y">On</form:option>
								<form:option value="N">Off</form:option>
								<c:if test="${empty theItem1.aciSettingsInd}">
								<form:option value=" "></form:option>
								</c:if>
							</form:select> 
                  </div>       
              <% } else { %>  
<c:if test="${theItem.autoIncreaseChanged ==false}">
                    <div onmouseover="<ps:aciChangeDetail name='' dataElementName='NO_ACTION' />" >
</c:if>
<c:if test="${theItem.autoIncreaseChanged ==true}">
                    <div onmouseover="<ps:aciChangeDetail name='autoIncreaseValue' dataElementName='ACIIndicator' />" >
</c:if>
 <form:select path="theItem1[${indexValue}].aciSettingsInd"  cssStyle="boxbody"> <%-- form:select - indexed="true" property="aciSettingsInd"  value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getAciSettingsInd() %>" --%>
                  	<form:option value=" "></form:option>
                    <form:option value="Y">On</form:option>
                    <form:option value="N">Off</form:option>                
</form:select> 
                  </div> 
            <% } %>      
            </c:if>            
           </td>           
           <td class="datadivider" width="1"></td>  
           <td align="left" nowrap>
<c:if test="${theItem.aciSettingsInd =='Y'}">
	            <c:if test="${!empty theItem.erEeLimitMessage}"> 
${theItem.erEeLimitMessage}
	            </c:if> 
</c:if>
<c:if test="${theItem.aciSettingsInd !='Y'}">
            	<c:if test="${!empty theItem.erEeLimitMessage}">
            	    <div id=nadItem<%=temp%> name=nadItem<%=temp%> style='display:none'>    
${theItem.erEeLimitMessage}
	                </div>            
		    	</c:if>            
</c:if>
            <c:if test="${empty theItem.erEeLimitMessage}">
                <% String display="display:block"; %> 
	            <c:if test="${empty theItem.dateNextADI}">
	               <% display="display:none"; %>
	            </c:if>
	           
	              <c:if test="${not empty param.printFriendly}" >
<input type="text" value="${theItem.nextADIYear}"	disabled="true" maxlength="4" size="2" cssStyle="inputField" />          
<%-- <form:input path="${theRecord.nextADIYear}" disabled="true" maxlength="4" size="2" cssStyle="inputField" /> form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getNextADIYear() %>" --%>
	              </c:if>
	              <c:if test="${empty param.printFriendly}" >
	                 <div id=nadItem<%=temp%> name=nadItem<%=temp%> style='<%=display%>'>
	                 
	                <%=DeferralUtils.getNextADIYearError(theRecord)%>                
	           
	                <% if(canEditEZISettings && 
	                	  deferralReportForm.getTheItem(Integer.parseInt(temp)).isParticipantInd() && 
	                	  !(deferralReportForm.getTheItem(Integer.parseInt(temp)).hasAlert() &&
	                	    deferralReportForm.getTheItem(Integer.parseInt(temp)).hasOutstandingEZiRequest()) &&
	                	  deferralReportForm.getTheItem(Integer.parseInt(temp)).getAciReqWaitingApprovalAnniversaryDate() == null) {
	                	
	                       if(deferralReportForm.getDniEditState() == 2) { 
	                          if (deferralReportForm.getTheItem(Integer.parseInt(temp)).getNextAD() != null) { %>
<input type="text" value="${theItem.nextAD}" name="theItem1[${indexValue}].nextAD"	maxlength="10" onmousedown="enableSubmit();" size="10" cssStyle="inputField" />	
 <%--<form:input path="theItem1[${indexValue}].nextAD" maxlength="10" onmousedown="enableSubmit();" size="10" cssStyle="inputField" /><%-- form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getNextAD()%>" --%>

                              <% } else {  %>
<input type="text" value="<%=deferralReportForm.getNextAnniversaryDate() %>" name="theItem1[${indexValue}].nextAD"	maxlength="10"  onmousedown="enableSubmit();" size="10" cssStyle="inputField" />                           
<%--  <form:input path="theItem1[${indexValue}].nextAD" maxlength="10" onmousedown="enableSubmit();" size="10" cssStyle="inputField" /> <%--form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getNextAnniversaryDate()%>" --%>

                              <% } %>                              								                    
                              <img src="/assets/unmanaged/images/cal.gif" border="0" onclick="javascript:calDNIDate${indexValue}.popup();enableSubmit();"> 
         
  	                          <%-- create calendar object(s) --%> 
			                  <script type="text/javascript" >
			                     var calDNIDate${indexValue} = new calendar(document.forms['deferralReportForm'].elements['theItem1[<%=Integer.parseInt(temp) %>].nextAD']);
			                     calDNIDate${indexValue}.year_scroll = true;
			                     calDNIDate${indexValue}.time_comp = false;                
			                  </script>
	                <%     } else if (deferralReportForm.getDniEditState() == 3) { 
	                          // may be set as off and have not value, if so, need next anniversary date as default value. 
	                		  if (theRecord.getNextADIMonthDay() != null) { %>
	                
<form:select path="theItem1[${indexValue}].nextADIMonthDay" onmousedown="enableSubmit();" ><%--  form:select - indexed="true" property="nextADIMonthDay" --%>
				                    <% for(DayOfYear dayOfYear:deferralReportForm.getAllowedAnniversaryMonthDay()){				                       
				                           String value = ((dayOfYear.getMonth() <= 9) ? "0" + dayOfYear.getMonth() : StringUtils.EMPTY + dayOfYear.getMonth())
				                           +"/"
				                           + ((dayOfYear.getDay() <= 9) ? "0" + dayOfYear.getDay() : StringUtils.EMPTY + dayOfYear.getDay());
				                           
				                           if (value.equals(theRecord.getNextADIMonthDay())) {
				                        	    out.println("<option value=\""+value+"\" selected>"+value+"</option>");
				                           } else {
				                           		out.println("<option value=\""+value+"\">"+value+"</option>");
				                           }
							           }
									%>
</form:select>
<form:select path="theItem1[${indexValue}].nextADIYear" onmousedown="enableSubmit();">
										<% String yearSel ="";
				                        if (deferralReportForm.getAllowedAnniversaryYear(0).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>      
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(0)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(0)%></option>
						                <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(1).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(1)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(1)%></option>
	                                    <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(2).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>					              
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(2)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(2)%></option>
</form:select>
				                  <%-- <ps:select name="theItem" property="nextADIYear" indexed="true" onmousedown="enableSubmit();">
				                  	    <% String yearSel ="";
				                        if (deferralReportForm.getAllowedAnniversaryYear(0).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>      
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(0)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(0)%></option>
						                <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(1).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(1)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(1)%></option>
	                                    <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(2).equals(theRecord.getNextADIYear())) yearSel = "selected"; %>					              
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(2)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(2)%></option>
				                  </ps:select>  --%>

<%                             } else {  %>			                  			                            
<form:select path="theItem1[${indexValue}].nextADIMonthDay" onmousedown="enableSubmit();" ><%--  form:select - indexed="true" property="nextADIMonthDay" --%>
				                    <% for(DayOfYear dayOfYear:deferralReportForm.getAllowedAnniversaryMonthDay()){				                       
				                           String value = ((dayOfYear.getMonth() <= 9) ? "0" + dayOfYear.getMonth() : StringUtils.EMPTY + dayOfYear.getMonth())
				                           + "/"
				                           + ((dayOfYear.getDay() <= 9) ? "0" + dayOfYear.getDay() : StringUtils.EMPTY + dayOfYear.getDay());
				                           if (value.equals(deferralReportForm.getNextAnniversaryMonthDay())) {
				                        	    out.println("<option value=\""+value+"\" selected>"+value+"</option>");
				                           } else {
				                           		out.println("<option value=\""+value+"\">"+value+"</option>");
				                           }
				                    	}
									%>
</form:select>
<form:select path="theItem1[${indexValue}].nextADIYear" onmousedown="enableSubmit();">
 										<% String yearSel ="";
				                        if (deferralReportForm.getAllowedAnniversaryYear(0).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>      
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(0)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(0)%></option>
						                <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(1).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(1)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(1)%></option>
	                                    <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(2).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>					              
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(2)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(2)%></option>
</form:select>
				                   <%-- <ps:select name="theItem" property="nextADIYear" indexed="true" onmousedown="enableSubmit();">
				                  	    <% String yearSel ="";
				                        if (deferralReportForm.getAllowedAnniversaryYear(0).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>      
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(0)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(0)%></option>
						                <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(1).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(1)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(1)%></option>
	                                    <% yearSel =""; if (deferralReportForm.getAllowedAnniversaryYear(2).equals(deferralReportForm.getNextAnniversaryDateYear())) yearSel = "selected"; %>					              
						                <option value="<%=deferralReportForm.getAllowedAnniversaryYear(2)%>" <%=yearSel%>><%=deferralReportForm.getAllowedAnniversaryYear(2)%></option>
				                  </ps:select>  --%>
<%                             }   
	                       }
	                   } else { %>
 <%--<form:input path="theItem1[${indexValue}].nextAD" disabled="true" maxlength="10" size="10" cssStyle="inputField" /> <%--form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getNextAD() %>" --%>
 <input type="text" value="${theItem.nextAD}" name="theItem1[${indexValue}].nextAD" disabled="true" maxlength="10" size="10" cssStyle="inputField" />

	                <% } %>
	                
	                </div>
	              </c:if>    
	            
	       </c:if> 
           </td>       
           <td class="datadivider" width="1"></td>     
           <td align="center">
            <c:if test="${not empty param.printFriendly}" >
<form:select path="theItem1[${indexValue}].increaseType" disabled="true" ><%-- form:select - indexed="true" property="increaseType" style="boxbody" value="<%=theItem.getIncreaseType()%>" --%>
                <form:option value="%">%</form:option>
                <form:option value="$">$</form:option>                
</form:select>
            </c:if>
            <c:if test="${empty param.printFriendly}"> 
              <%=DeferralUtils.getTypeError(theRecord)%>
              <% if(canEditEZISettings && 
                  deferralReportForm.getTheItem(Integer.parseInt(temp)).isParticipantInd() && 
                  (deferralReportForm.getPlanDeferralType() == null || 
                    Constants.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(deferralReportForm.getPlanDeferralType())&&
                    "Y".equalsIgnoreCase(deferralReportForm.getTheItem(Integer.parseInt(temp)).getAciSettingsInd()))
                ) {
              %>
                  
<form:select path="theItem1[${indexValue}].increaseType" onchange="switchBetweenAmtAndPct('${indexValue}');" onmousedown="enableSubmit();" cssStyle="boxbody"><%--  form:select - indexed="true" property="increaseType"  value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getIncreaseType() %>" --%>
                  <form:option value="%">%</form:option>
                  <form:option value="$">$</form:option>                 
</form:select>

              <% } else { %>
<form:select path="theItem1[${indexValue}].increaseType" disabled="true" cssStyle="boxbody" ><%-- form:select - indexed="true" property="increaseType" value="<%=theItem.getIncreaseType()%>" --%>
                  <form:option value="%">%</form:option>
                  <form:option value="$">$</form:option>                
</form:select>
              <% } %> 
            </c:if>       
           </td>
           <td class="datadivider" width="1"></td>      
<c:if test="${theItem.increaseType =='%'}">
            <c:set var="increaseValue" value="${theItem.increasePctHistory}" scope="page"/>   
            <c:set var="deferralIncrease" value="Deferral Increase Percent" scope="page"/>  
</c:if>
<c:if test="${theItem.increaseType ==$}">
            <c:set var="increaseValue" value="${theItem.increaseAmtHistory}" scope="page"/>   
            <c:set var="deferralIncrease" value="Deferral Increase Amount" scope="page"/>  
</c:if>
           <td id="increaseID<%=temp%>" align="center" >            
            <c:if test="${not empty param.printFriendly }" >     
<input type="text"  name="theItem1[${indexValue}].increase"	value="${theItem.increase}" disabled="true" size="2" cssStyle="inputField" /> <%--  form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getIncrease() %>" --%>
            </c:if>
            <c:if test="${empty param.printFriendly}">              
              <div onmouseover="<ps:aciChangeDetail name='increaseValue' dataElementName='deferralIncrease' />"> 
              <% if(canEditEZISettings && deferralReportForm.getTheItem(Integer.parseInt(temp)).isParticipantInd() && "Y".equalsIgnoreCase(deferralReportForm.getTheItem(Integer.parseInt(temp)).getAciSettingsInd())) { %>
                <%=DeferralUtils.getIncreaseError(theRecord)%>
<input type="text"  	value="${theItem.increase}" name="theItem1[${indexValue}].increase" onmousedown="enableSubmit();" size="2" cssStyle="inputField" />
<%--  form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getIncrease() %>" --%>
              <% } else { %>  

  <input type="text"  	value="${theItem.increase}" name="theItem1[${indexValue}].increase" disabled="true" size="2" cssStyle="inputField" />             
<%--  form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getIncrease() %>" --%>
              <% } %>
              </div> 
            </c:if> 
           </td>                         
          <td class="datadivider" width="1"></td> 
<c:if test="${theItem.increaseType =='%'}">
            <c:set var="limitValue" value="${theItem.limitPctHistory}" scope="page"/>   
            <c:set var="deferralLimit" value="Deferral Limit Percent" scope="page"/>
</c:if>
<c:if test="${theItem.increaseType =='$'}">
            <c:set var="limitValue" value="${theItem.limitAmtHistory}" scope="page"/>   
            <c:set var="deferralLimit" value="Deferral Limit Amount" scope="page"/>
</c:if>
          <td id="limitID<%=temp%>" align="center" >            
            <c:if test="${not empty param.printFriendly }" >   
<%-- <form:input path="${theRecord.limit}" disabled="true" size="2" cssStyle="inputField" /> --%>
<input type="text"  value="${theItem.limit}" name="theItem1[${indexValue}].limit" disabled="true" size="2" cssStyle="inputField" />
<%-- <input type="text" value="${theRecord.limit}" disabled="true" size="2" cssStyle="inputField" /> --%>
<%--  form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getLimit() %>" --%>
            </c:if>
            <c:if test="${empty param.printFriendly}">              
              <div onmouseover="<ps:aciChangeDetail name='limitValue' dataElementName='deferralLimit' />" > 
              <%if(canEditEZISettings && deferralReportForm.getTheItem(Integer.parseInt(temp)).isParticipantInd() && "Y".equalsIgnoreCase(deferralReportForm.getTheItem(Integer.parseInt(temp)).getAciSettingsInd())) { %>  
                <%=DeferralUtils.getLimitError(theRecord)%>
<%-- <form:input path="${theRecord.limit}" onmousedown="enableSubmit();" size="2" cssStyle="inputField" /> form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getLimit() %>" --%>
<%-- <input type="text" value="${theRecord.limit}" onmousedown="enableSubmit();" size="2" cssStyle="inputField" /> --%>
<input type="text"  name="theItem1[${indexValue}].limit"	value="${theItem.limit}" onmousedown="enableSubmit();" size="2" cssStyle="inputField" />
              <% } else { %>
<%-- <form:input path="${theRecord.limit}" disabled="true" size="2" cssStyle="inputField" /> --%>
<%-- <input type="text" value="${theRecord.limit}" disabled="true" size="2" cssStyle="inputField" /> --%>
<input type="text"  name="theItem1[${indexValue}].limit"	value="${theItem.limit}" disabled="true" size="2" cssStyle="inputField" />
<%--  form:input - indexed="true" name="theItem" value="<%=deferralReportForm.getTheItem(theIndex.intValue()).getLimit() %>" --%>
              <% } %>        
              </div>      
            </c:if>
          </td> 
          <td class="datadivider" width="1"></td>  
          <% } %>        
         
          <% if(theRecord.hasAlert() || theRecord.hasWarnings()) { %>          
           <c:set var="alertMessage" scope="page">Click here to go to Deferral Tasks</c:set>      
           <td> 
            <table>
              <tr>    
               <% if(theRecord.hasAlert()) { %> 
               <td align="middle" onmouseover="<ps:warnings name='alertMessage' />" >
                <a href="/do/participant/taskCenterTasks">
                  <IMG height=12 src="/assets/unmanaged/images/alert.gif" width=12 border=0>
                </a>
               </td>          
               <% } %>
               <% if(theRecord.hasWarnings()) { %> 
               <% pageContext.setAttribute("war", theRecord.getWarnings()); %>
               <td align="middle" onmouseover="<ps:warnings name='war' />" >
                    <IMG height=12 src="/assets/unmanaged/images/warning2.gif" width=12 border=0>
                </a>
               </td>          
               <% } %>
              </tr>
            </table>
           </td>
          <% } else { %>
           <%-- No warning or alert --%>    
            <td align="middle" >&nbsp;</td>   
          <% } %>       
          <% if (beigeBackgroundCls) { %>
            <td class="dataheaddivider">
          <% } else { %>
            <td class="beigeborder">
          <% } %>          
          <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
          </td>
          <td class="databorder" width="1"></td>
          
          <% if(!deferralReportForm.isACIOff()) { %>
            <%-- Track changes for the following fields --%>    
		
            <ps:trackChanges name="deferralReportForm" property='<%= "theItem1["+temp+"].aciSettingsInd" %>' />
            <c:if test="${empty theItem.erEeLimitMessage and ! empty theItem.dateNextADI}">
            </c:if>
            <ps:trackChanges name="deferralReportForm" property='<%= "theItem1[" + temp +"].increaseType" %>' />
            <ps:trackChanges name="deferralReportForm" property='<%= "theItem1[" + temp +"].increase" %>' />
            <ps:trackChanges name="deferralReportForm" property='<%= "theItem1[" + temp +"].limit" %>' />
          <% } %>  
    </tr>
</c:forEach>
  </c:if>     

    <%-- End of Details --%>
    <%-- let the last line have the same background colour as the previous line --%>
    <% if (lastLineBkgrdClass) { %>
      <tr class="datacell1">
    <% } else { %>
      <tr class="datacell2">
    <% } %>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
        <c:if test="${empty param.printFriendly}"> 
          <td class="lastrow"></td>
          <td class="datadivider"></td>
        </c:if>
        <td class="lastrow"></td>
        <td class="datadivider"></td> 
        <%if(showPayrollColumn){%>     
          <td class="lastrow"></td>
          <td class="datadivider"></td>
        <%}%>
        <%if(showDivisionColumn){%>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
        <%}%>  
        <td class="lastrow"></td>
        <td class="datadivider"></td>
        <% if(!deferralReportForm.isACIOff()) { %>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
        <%}%>  
        <td class="lastrow"></td>
        <td class="datadivider"></td>
        <td class="lastrow"></td>
        <td rowspan="2" colspan="2" width="5" class="lastrow"  align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
      </tr>
        
<%-- End of Last line --%>        
        <tr>
          <td class="databorder" colspan="<%=numberOfDisplayColumnsLessTwo%>"></td>
        </tr>

        <tr>
          <td colspan="<%=numberOfDisplayColumns%>" align="right">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
  		          <td width="50%"></td>
  	    		    <% if (errors==false) { %>
  	        		<td><b><report:recordCounter report="theReport" label="Employees"/></b></td>
  	        		<td align="right"><report:pageCounter report="theReport" arrowColor="black" formName="deferralReportForm"/></td>
          		  <% } else { %>
                <td colspan="2"></td>
  	            <% } %>
  		        </tr>
  		      </table>
          </td>
        </tr>
        
        <tr>
          <td colspan="<%=numberOfDisplayColumns%>" class="boldText">&nbsp;</td>
        </tr>
        
        <tr> 
          <table> 
            <tbody>
              <tr>
                <td width="45%">&nbsp;</td>                               
                <td id="cancelButton" width="35%" align="right" nowrap>
                  <c:if test="${empty param.printFriendly}" >                     
                    <% if(deferralReportForm.isAllowedToEdit() && !deferralReportForm.isACIOff()) { %> 
                      <input name="button" type="submit" class="button134" value="CANCEL" disabled="true" />
                    <% } %>  
                  </c:if>
                </td>
                <td id="saveButton" width="20%" align="right" nowrap>
                  <c:if test="${empty param.printFriendly}" >
                    <% if(deferralReportForm.isAllowedToEdit() && !deferralReportForm.isACIOff()) { %>
                      <input name="button" type="submit" class="button134" value="SAVE" disabled="true" />
                    <% } %>  
                  </c:if>
                </td> 
              </tr>
            </tbody>            
          </table>
        </tr>
              
 <% } %>    
              
        <tr>
          <td></td>
          <td></td>
          <td colspan="<%=numberOfDisplayColumns%>">
            <br>
            <p><content:pageFooter beanName="layoutPageBean"/></p>
            <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
            <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
          </td>
        </tr> 
      


  
      </table>
    
    </ps:form>  


      <br>
    </td>

    <%-- column to the right of the report --%>
    <td width="15"></td>

<c:if test="${not empty param.printFriendly}" >
  </td>
</c:if>

<c:if test="${not empty param.printFriendly}" >
    <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
            id="globalDisclosure"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
      </tr>
    </table>
</c:if>

<script language="javascript" src="/assets/unmanaged/javascript/oldstyle_tooltip.js"></script>
