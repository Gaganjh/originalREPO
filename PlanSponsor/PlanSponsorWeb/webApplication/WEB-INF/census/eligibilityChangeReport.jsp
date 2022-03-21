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



<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.manulife.pension.service.environment.valueobject.LabelValueBean"%>
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

function getChangeDetails(){
	 document.eligibilityReportsForm.action = "?task=download&ext=.csv&reportType=eligibilityChangeReport";
	 document.eligibilityReportsForm.submit();
}

function goToStaging(){
	document.eligibilityReportsForm.action = "/do/census/employeeEligibilityReports/";
	 document.eligibilityReportsForm.submit();
}

window.onload = loadMoneyTypes;

var outerArray = new Array();
var mapped ;
function loadMoneyTypes(){

		if(document.getElementById('selectedMoneyType') != null){
	
		document.getElementById('selectedMoneyType').options.length = 0;
		<% Map<String,List<LabelValueBean>> map =  eligibilityReportsForm.getMoneyTypePEDMap();
		  List<LabelValueBean> moneyTypes=  map.get(eligibilityReportsForm.getReportedPED());	
		  if(moneyTypes != null){
		  if(moneyTypes.size() > 1 ){ %>
		 	var newOption = document.createElement("OPTION");
			newOption.value = 'All';
			newOption.text = 'All';
			var elementInd = 'selectedMoneyType'; 
			document.getElementById(elementInd).options.add(newOption); 
		 <% }
		  for(int ind = 0; ind < moneyTypes.size(); ind++){
		%>
			var newOption = document.createElement("OPTION");
			newOption.value = '<%= moneyTypes.get(ind).getValue() %>';
			newOption.text = '<%= moneyTypes.get(ind).getLabel() %>';
			var elementInd = 'selectedMoneyType'; 
			document.getElementById(elementInd).options.add(newOption);
			
		<%} }%>

		

		<% 
		
		Map<String,List<LabelValueBean>> map2 =  eligibilityReportsForm.getMoneyTypePEDMap();
		Set<String> set = map2.keySet();
		
		Iterator<String> it = set.iterator();
		int ind = 0;
		while(it.hasNext()){ %>
			var innerArray = new Array();
			<%
			String ped = (String)it.next();
			
			List<LabelValueBean> moneyTypes3 = map2.get(ped);
			for(int i=0; i < moneyTypes3.size(); i++){ %>
			//alert('ped-->'+ '<%=map2%>');
			innerArray['<%=i%>'] = new Array(2);
			innerArray['<%=i%>']['0'] = '<%=moneyTypes3.get(i).getValue()%>';
			innerArray['<%=i%>']['1'] = '<%=moneyTypes3.get(i).getLabel()%>';
			//alert('innerArray[]-->'+innerArray['<%=i%>']);
		<%
			} %>
			outerArray['<%=ind%>'] = innerArray;
			<%
			ind ++;
		}		
		
		
		%>

			 mapped = 
				 
			   {
					<% 
					Iterator it1 = set.iterator();
					int size = set.size();
						int count = 0;
					while(it1.hasNext()){ %>
						<%
							String ped = (String)it1.next(); %>
						
							'<%=ped%>' : outerArray[<%=count%>] 
							<%  if(size != (count+1)){%>
								,
							<% }%>
						<% count ++ ;}%>
					
			   };	

		}
		
		
}

function loadMoneyTypesOnChange(){
	
	
	var innerArray1 = new Array();
	innerArray1 = mapped[''+document.eligibilityReportsForm.reportedPED.value];
	document.getElementById('selectedMoneyType').options.length = 0;
	//alert('selectedVal-->'+document.eligibilityReportsForm.reportedPED.value);
	//alert('innerArray1.length-->'+innerArray1.length);
	//alert('innerArray1-->'+innerArray1);
	if(innerArray1.length > 1){
		var newOption = document.createElement("OPTION");
		newOption.value = 'All';
		newOption.text = 'All';
		var elementInd = 'selectedMoneyType'; 
		document.getElementById(elementInd).options.add(newOption);
	}
	for(var i = 0; i < innerArray1.length ; i++){
		//alert('innerArray[i]-->'+innerArray1[i]);
		var newOption = document.createElement("OPTION");
		newOption.value = innerArray1[i]['0'];
		newOption.text = innerArray1[i]['1'];
		var elementInd = 'selectedMoneyType'; 
		document.getElementById(elementInd).options.add(newOption);
		
	}
	
	
}




</script>

<content:contentBean contentId="<%=ContentConstants.COMPUTATION_PERIOD_REPORT_HEADING%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="computationPeriodReportHeading"/>
	
<content:contentBean contentId="<%=ContentConstants.COMPUTATION_PERIOD_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="computationPeriodReportDescription"/>

<form:form modelAttribute="eligibilityReportsForm"  name="eligibilityReportsForm"  action="${pageContext.request.contextPath}/census/employeeEligibilityReports/" method="post">

<content:errors scope="session"/>
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
			<input name="isAciSignupAuto" value="true" type="hidden">
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
<c:if test="${not empty eligibilityReportsForm.eligibilityChangePEDsList}">
                               <table width="475" cellpadding="2">
                                 <tr>
                                   <td width="115"><strong>Plan entry date</strong></td>

                                   <td width="120"><strong>Money type</strong></td>
                                   <td width="240">&nbsp;</td>
                                 </tr>
                                 <tr>
                                   <td width="115">
<form:select path="reportedPED" onchange="loadMoneyTypesOnChange()" id="reportedPED">
<form:options  items="${eligibilityReportsForm.eligibilityChangePEDsList}"/>
</form:select>

                                   </td>
                                   <td width="120">
                                   
<form:select path="selectedMoneyType" id="selectedMoneyType">
				
</form:select>
                                   
                                  </td>
                                   <td width="240"><div align="right">

                                     <input type="button" name="button2" id="button2" value="Download" onclick="getChangeDetails()">
                                   </div></td>
                                 </tr>
                               </table>
</c:if>
<c:if test="${empty eligibilityReportsForm.eligibilityChangePEDsList}">
								<content:getAttribute id="conditionalStatement" attribute="text"/>
</c:if>
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



</form:form>
