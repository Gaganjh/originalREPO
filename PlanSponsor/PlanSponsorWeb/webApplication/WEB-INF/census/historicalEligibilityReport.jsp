<%-- taglib used --%>
 
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Imports --%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.EligibilityReportsForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>


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


   



<script>

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showAmendmentPDF(theSelect) {
	var url = "/do/contract/contractDocumentsPDF/?contractDoc=";
	var key = theSelect.options[theSelect.selectedIndex].value;
	if (key.length > 0) {
	url = url+key;
	PDFWindow(url)
	}
}

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showContractPDF(fileName) {
	var url = "/do/contract/contractDocumentsPDF/?contractDoc=";
	url = url+fileName;
	PDFWindow(url)
}


function getHistoryDetails(){
	 document.eligibilityReportsForm.action = "?task=download&ext=.csv&reportType=historicalReport";
	 document.eligibilityReportsForm.submit();
}

function goToStaging(){
	document.eligibilityReportsForm.action = "/do/census/employeeEligibilityReports/";
	 document.eligibilityReportsForm.submit();
}


</script>

<content:contentBean contentId="<%=ContentConstants.NO_CRITERIA_TO_GENERATE_REPORT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="conditionalStatement"/> 

<form:form modelAttribute="eligibilityReportsForm" name="eligibilityReportsForm" action="${pageContext.request.contextPath}/census/historicalEligibilityReport/" method="post">

<content:errors scope="session"/>
<table width="730" border="0" cellpadding="0" cellspacing="0">
   <tbody>
   
   <tr>
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
				<td width="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  </tr>
			 			  
  		   <tr>
		
			<td colspan="4">
		          <table width="495" border="0" cellpadding="0" cellspacing="0">
                    <tbody>
                     <tr>
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
                          <td colspan="2" valign="top" align="left"> 
         				  <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
						  <br><Br>
<c:if test="${not empty eligibilityReportsForm.historyPlanEntryDatesList}">
							<table width="475" cellpadding="2">
  								<tr>
    								<td colspan="3"><strong>Plan entry date</strong></td>
    							</tr>

  								 <tr>
    								<td width="115">  
<form:select path="histroyPlanEntryDate" id="eligibilityReportsForm">
<form:options items="${eligibilityReportsForm.historyPlanEntryDatesList}" />
</form:select>
    								</td>
								    <td width="120"><label></label></td>
								    <td width="240">
								    <div align="right">
      									<input type="button" name="button" id="button" value="Download" onclick="getHistoryDetails()" >
    								</div>
    								</td>
  								</tr>
							</table>
</c:if>
<c:if test="${empty eligibilityReportsForm.historyPlanEntryDatesList}">
								<content:getAttribute id="conditionalStatement" attribute="text"/>
</c:if>
							</td>
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                     </tr>

		   			  <tr>
						<td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					  </tr>
					  <tr>
						<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
						<td colspan="5">&nbsp; </td>
					  </tr>

						<tr>
							<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td width="327">
								<table width="100%">
	                            	<tbody>
		                                <tr>
		                                  <td width="53%" align="left"><div align="left">
		                                      <input name="button2" class="button100Lg" value="eligibility reports" onClick="goToStaging()" type="button">
		                                  	</div>
		                                  </td>
		                                  <td id="cancelButton" width="24%" align="right" nowrap="nowrap">&nbsp;</td>
		                                  <td id="saveButton" width="23%" align="right" nowrap="nowrap">&nbsp;</td>
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
      </tbody>
     </table>
	<table>
	<tr>
      	<td>
      		
			<p> <content:pageFooter beanName="layoutPageBean"/></p>
			<p class="footnote"> <content:pageFootnotes beanName="layoutPageBean"/></p>
			<p class="disclaimer"> <content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
		</td>
		</tr>
	</table>


</form:form>
