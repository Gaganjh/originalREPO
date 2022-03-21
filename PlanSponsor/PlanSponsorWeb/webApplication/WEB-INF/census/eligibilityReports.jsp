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
function getEligibilityChangeReportPage(){
	 document.eligibilityReportsForm.action = "/do/census/eligibilityChangeReport/";
	 document.eligibilityReportsForm.method = "get";
	 document.eligibilityReportsForm.submit();
}

function getHistoricalReportPage(){
	document.eligibilityReportsForm.action = "/do/census/historicalEligibilityReport/";
	document.eligibilityReportsForm.method = "get";
	document.eligibilityReportsForm.submit();
}

function getCompPeriodReport(){
	document.eligibilityReportsForm.action = "?task=download&ext=.csv&reportType=compPeriodChangeReport";
	document.eligibilityReportsForm.submit();
}

function getEligibilityReport(){
	window.location = "/do/census/employeeEnrollmentSummary?task=download&reportType=allDetails&ext=.csv&source=eligibilityReports"; 
}

function getEligibilityIssuesReportPage(){
		 document.eligibilityReportsForm.action = "/do/census/employeeEligibilityIssuesReports/";
		 document.eligibilityReportsForm.method = "get";
		 document.eligibilityReportsForm.submit();
	 <!-- document.eligibilityReportsForm.action = "?task=download&ext=.csv&reportType=eligibilityIssuesReport"; -->
}

</script>

<content:contentBean contentId="<%=ContentConstants.COMPUTATION_PERIOD_REPORT_HEADING%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="computationPeriodReportHeading"/>
	
<content:contentBean contentId="<%=ContentConstants.COMPUTATION_PERIOD_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="computationPeriodReportDescription"/>
	
<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_ISSUES_REPORT_HEADING%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="eligibilityIssuesReportHeading"/>
	
<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_ISSUES_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="eligibilityIssuesReportDescription"/>

<form:form modelAttribute="eligibilityReportsForm" name="eligibilityReportsForm" action="${pageContext.request.contextPath}/census/employeeEligibilityReports/" method="post">

	
  <content:errors scope="session"/>
	<table> 
	<tr> 
	<td>
 		 <table width="495" align="left" border="0" cellpadding="0" cellspacing="0">
			  <tbody>
			
                    <tr>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="495" height="1"></td>
                      <td width="5"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                    
                    <tr>
                      <td class="tableheadTD1" colspan="4">&nbsp;</td>
                    </tr>
                 	
                    <tr class="datacell1">
                      <td rowspan="10" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"> <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
                      <td width="1" rowspan="10" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>

                    <tr class="datacell1">
                      <td colspan="2" valign="top" align="left">
                      <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
                      <br><br>
                        <table width="475" cellpadding="2">
                          <tr>
                            <td width="115"></td>
                            <td width="120">&nbsp;</td>
                            <td width="240">

                                <div align="right">
                                  <input type="button" name="button1" id="button1" value="Download" onclick="javascript:getEligibilityReport()"  >
                                </div>
                              </td>
                          </tr>
                        </table></td>
                      </tr>

                      
                      <tr class="datacell2">
                        <td colspan="2" class="tablesubhead"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
                      </tr>
                      <tr class="datacell2">
                           
                        <td colspan="2" valign="top" align="left"> 
     						<content:getAttribute beanName="layoutPageBean" attribute="body2"/>


							<table width="475" cellpadding="2">
                           <tr>
                            <td width="115"></td>
                            <td width="220">&nbsp;</td>
                            <td width="150">
                                   	<label>
                                     <div align="right">
                                       <input  type="button" name="button2" id="button2" value="Select" onclick="javascript:getHistoricalReportPage()" >
                                     </div>
                                   </label>
                            </td>
                          
                           </tr>
                           
                         </table>

					   </td>
                     </tr>
                     
                     <tr class="datacell1">
                       <td colspan="2" class="tablesubhead"><b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b>
                       </td>
                     </tr>
                     
                     <tr class="datacell1">

                       <td colspan="2" valign="top" align="left">  <content:getAttribute beanName="layoutPageBean" attribute="body3"/><br><br>
                         <table width="475" cellpadding="2">
                           <tr>
                            <td width="115"></td>
                            <td width="120">&nbsp;</td>
                            <td width="240">
                                   	<label>
                                     <div align="right">
                                       <input type="button" name="button3" id="button3" value="Select" onclick="javascript:getEligibilityChangeReportPage()" >
                                     </div>
                                   </label>
                            </td>
                          
                           </tr>
                           
                         </table>

                        </td>
                     </tr>
                     
                     <!-- Eligibility Issues Report -->
                     <tr class="datacell2">
                        <td colspan="2" class="tablesubhead"><b><content:getAttribute beanName="eligibilityIssuesReportHeading" attribute="text"/></b></td>
                     </tr>
                     <tr class="datacell2">
                           
                       <td colspan="2" valign="top" align="left"> 
     				 	<content:getAttribute beanName="eligibilityIssuesReportDescription" attribute="text"/>

						<table width="475" cellpadding="2">
                           <tr>
                            <td width="115"></td>
                            <td width="220">&nbsp;</td>
                            <td width="150">
                                   	<label>
                                     <div align="right">
                                       <input  type="button" name="button5" id="button5" value="Select" onclick="javascript:getEligibilityIssuesReportPage()" >
                                     </div>
                                   </label>
                            </td>
                           </tr>
                        </table>
					   </td>
                     </tr>
                     <!-- End oF Eligibility Issues Report -->
                   
<c:if test="${eligibilityReportsForm.eligibiltyCalcOn ==true}">
                     <% if(eligibilityReportsForm.getMoneyTypesHoS().size() > 0 ) { %>	
                     
                     <tr class="datacell1">
                       <td colspan="2" class="tablesubhead"><b><content:getAttribute attribute="text" beanName="computationPeriodReportHeading"/></b></td>
                     </tr>
                     
                     
                     <tr class="datacell1">
                             <td colspan="2" valign="top" align="left"><content:getAttribute attribute="text" beanName="computationPeriodReportDescription"/><br><Br>
                               <table width="475" cellpadding="2">
                                 <tr>
                                   <td width="115"><label></label></td>
                                   <td width="120">&nbsp;</td>

                                   <td width="240"><label>
                                     <div align="right">
                                       <input type="button" name="button4" id="button4" value="Download" onclick="javascript:getCompPeriodReport()" >
                                       </div>
                                   </label></td>
                                 </tr>
                               </table>
                            </td>
                    </tr>
                     
                    <% } %>
</c:if>
                     <tr>
						<td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					 </tr>
					
			</tbody>
			
		</table>
		</td>
		</tr>
		<!-- footer starts -->
		<tr>
   					<td> <br>
						<p><content:pageFooter beanName="layoutPageBean"/></p>
						<p class="footnote"> <content:pageFootnotes beanName="layoutPageBean"/></p>
						<p class="disclaimer"> <content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
					</td>
					</tr>
					<!-- footer end -->
		</table>
		
		
		
</form:form>
