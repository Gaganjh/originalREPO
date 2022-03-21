<%-- taglib used --%>
 <%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.EligibilityReportsForm" %>

<jsp:useBean id="eligibilityReportsForm" scope="session" type="com.manulife.pension.ps.web.census.EligibilityReportsForm" />

<script type="text/javascript" >
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

</script>

<style>

// Change the background opacity when a modal dialog is shown. it may not work all the time.
// A more guaranteed approach is to set the opacity of the <Panel_ID>_mask element.
.yui-skin-sam .mask {
    opacity: 0;
}
</style>


<content:contentBean contentId="<%=ContentConstants.NO_CRITERIA_TO_GENERATE_REPORT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="conditionalStatement"/> 
                                   
<script>

function resetReportedToDate() {
	var currentTime = new Date()
	var month = currentTime.getMonth() + 1;
	var day = currentTime.getDate();
	var year = currentTime.getFullYear();
	document.eligibilityReportsForm.reportedToDate.value = month + "/" + day + "/" + year; 
}

function getEligibilityIssuesDetails(){

	var fromDate = document.eligibilityReportsForm.reportedFromDate.value;
	var toDate = document.eligibilityReportsForm.reportedToDate.value;
	var currentDate = new Date();
		
	if(toDate == '') {
       	 resetReportedToDate();
    }
    
	document.eligibilityReportsForm.action = "?task=download&ext=.csv&reportType=eligibilityIssuesReport";
	document.eligibilityReportsForm.submit();
}

function dateValidation(date) {
	if(date != '') {
		return cal_prs_date1(date);
	}
}

function goToStaging(){
	document.eligibilityReportsForm.action = "/do/census/employeeEligibilityReports/";
	document.eligibilityReportsForm.submit();
}

</script>
                                   

<form:form modelAttribute="eligibilityReportsForm" name="eligibilityReportsForm" action="${pageContext.request.contextPath}/census/employeeEligibilityIssuesReports/" method="post">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" valign="top">
			<%-- error line --%>
			<p><content:errors scope="session"/></p>
		</td>
	</tr>
</table>
                                   
<table width="730" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr>

 	<td width="700">
		  <table width="495" align="left" border="0" cellpadding="0" cellspacing="0">
			  <tbody>
			  <tr>

				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="327"><img src="/assets/unmanaged/images/s.gif" width="327" height="1"></td>

				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
				<td width="5"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
				<td width="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td></tr>
			 			  
			  <tr>
		
			  <td colspan="4">
		                  	<table width="495" border="0" cellpadding="0" cellspacing="0">
                    <tbody><tr>

                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="495" height="1"></td>
                      <td width="5"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    <tr>

                      <td class="tableheadTD1" colspan="4" height="25"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>

                      </tr>
                      
                       <tr class="datacell1">
                             <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                             <td colspan="2" valign="top" align="left"><content:getAttribute beanName="layoutPageBean" attribute="body1"/><Br>
                               <Br>
                               <table width="475" cellpadding="2">
                                 <tr>
                                 <td colspan="2"><strong>Report from date</strong></td>
                                 <td colspan="2"><strong>Report to date</strong></td>
								</tr>
								<tr>
								<td colspan="2">

	<form:input path="reportedFromDate" maxlength="10" onchange="dateValidation(this.value)" cssClass="inputField" />	
                           		<a href="javascript:calFromDate.popup();">
                      				<img src="/assets/unmanaged/images/cal.gif" width="16" height="16" 
                      					border="0" alt="Use the Calendar to pick the date">
                    			</a> 
								</td>
                                 <td colspan="2">
<form:input path="reportedToDate" maxlength="10" onchange="dateValidation(this.value)" cssClass="inputField" />
			
                    			<a href="javascript:calToDate.popup();">
                      				<img src="/assets/unmanaged/images/cal.gif" width="16" height="16" 
                      					border="0" alt="Use the Calendar to pick the date">
                    			</a> 
                    			
								</td>
								<td colspan="2"><div align="right">

                                     <input type="button" name="button2" id="button2" value="Download" onclick="getEligibilityIssuesDetails()">
                                   </div></td>
                                 </tr>
                                 <tr>
                                 <td colspan="2">(mm/dd/yyyy)</td>
                                 <td colspan="2">(mm/dd/yyyy)</td>
                                 </tr>
                                 </table>
                                </td>
                                <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                               </tr>
            <tr>
			<td class="databorder" colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td></tr>

			<tr>
				<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td colspan="5">&nbsp; </td></tr>

				<tr>
				<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td width="327">
							<table width="100%">
                              <tbody>

                                <tr>
                                  <td width="53%" align="left"><div align="left">
                                      <input name="button" class="button100Lg" value="eligibility reports" onClick="goToStaging();" type="submit">
                                  </div></td>
                                </tr>
                              </tbody>
                            </table>
					</td>
					</tr>
					</tbody>
				</table>
				</td>
				</tr>
				</tbody>
				</table>
				</td>
				</tr>
				<!--  footer starts -->
		<tr>
			<td >
				<br/>
				<p> <content:pageFooter beanName="layoutPageBean"/></p>
				<p class="footnote"> <content:pageFootnotes beanName="layoutPageBean"/></p>
				<p class="disclaimer"> <content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			</td>
		</tr>
		<!--  footer ended -->
				</tbody>
				</table>
				
	  <script type="text/javascript" >
      <!-- // create calendar object(s) just after form tag closed
        var calFromDate = new calendar(document.forms['eligibilityReportsForm'].elements['reportedFromDate']);
        calFromDate.year_scroll = true;
        calFromDate.time_comp = false;
        var calToDate = new calendar(document.forms['eligibilityReportsForm'].elements['reportedToDate']);
        calToDate.year_scroll = true;
        calToDate.time_comp = false;
        //-->
      </script>
      
</form:form>
