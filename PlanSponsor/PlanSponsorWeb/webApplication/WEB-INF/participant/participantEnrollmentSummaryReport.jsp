<%-- taglib used --%>

<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>

<%-- Imports --%>


<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantEnrollmentSummaryReportForm" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummary" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.util.render.DateRender" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData" %>

<%
ParticipantEnrollmentSummaryReportData theReport = (ParticipantEnrollmentSummaryReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>




<jsp:useBean id="participantEnrollmentSummaryReportForm" scope="session" type="com.manulife.pension.ps.web.participant.ParticipantEnrollmentSummaryReportForm" />

                                  
<content:contentBean contentId="<%=ContentConstants.NO_ENROLLMENTS_DURING_PAST_30_DAYS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="NoEnrollmentsDuringPast30Days"/> 
                                   
<content:contentBean contentId="<%=ContentConstants.NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="NoSearchResults"/>                                                                                          


<%


boolean showPayrollColumn = false;
boolean showDivisionColumn = false;
boolean hasDetailRecords = false;
boolean hasInternetEnrollments = false;
boolean hasAutoEnrollments = false;

if (participantEnrollmentSummaryReportForm.getHasInternetEnrollments()){
	hasInternetEnrollments = true;
}

if (participantEnrollmentSummaryReportForm.getHasAutoEnrollments()){
	hasAutoEnrollments = true;
}

String pageType = participantEnrollmentSummaryReportForm.getTypeOfPageLayout();

if(!participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.HIDE_BOTH)){
	if((participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.FULL_COLUMNS)) || (participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.HIDE_DIVISION)) ){
		showPayrollColumn = true;
	}
}	

if(!participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.HIDE_BOTH)){
	if((participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.FULL_COLUMNS)) || (participantEnrollmentSummaryReportForm.getTypeOfPageLayout().equals(participantEnrollmentSummaryReportForm.HIDE_PAYROLL)) ){	
		showDivisionColumn = true;
	}
}

boolean initialSearch = false;

if(participantEnrollmentSummaryReportForm.getIsInitialSearch()){
	initialSearch = true;
}

int numOfDisplayColumns = participantEnrollmentSummaryReportForm.getNumberOfDisplayColumns();
String  numberOfDisplayColumns = String.valueOf(numOfDisplayColumns);
int numOfDisplayColumnsLessTwo = numOfDisplayColumns - 2;
String  numberOfDisplayColumnsLessTwo = String.valueOf(numOfDisplayColumnsLessTwo);

%>

                                                                                    


<c:if test="${empty param.printFriendly }" >

<script type="text/javascript" >
function doSubmit(){

	if (document.forms['participantEnrollmentSummaryReportForm']) {
		document.forms['participantEnrollmentSummaryReportForm'].elements['isSearch'].value = "true"; 
	}

	if (document.forms['participantEnrollmentSummaryReportForm']) {
		document.forms['participantEnrollmentSummaryReportForm'].submit();
	} else {
		document.forms.participantEnrollmentSummaryReportFormm.submit();
	}

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


	
	if (document.participantEnrollmentSummaryReportForm.ssnOne) {
			if (document.participantEnrollmentSummaryReportForm.ssnOne.value.length >= 0){
					document.participantEnrollmentSummaryReportForm.namePhrase.value = "";
			}
	}


	if (document.participantEnrollmentSummaryReportForm.ssnTwo) {
			if (document.participantEnrollmentSummaryReportForm.ssnTwo.value.length >= 0){	
				document.participantEnrollmentSummaryReportForm.namePhrase.value = "";
			}
	}

	if (document.participantEnrollmentSummaryReportForm.ssnThree) {
			if (document.participantEnrollmentSummaryReportForm.ssnThree.value.length >= 0){	
				document.participantEnrollmentSummaryReportForm.namePhrase.value = "";
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
	
	
	if (document.participantEnrollmentSummaryReportForm.namePhrase) {
			if (document.participantEnrollmentSummaryReportForm.namePhrase.value.length >= 0){
				document.participantEnrollmentSummaryReportForm.ssnOne.value = "";
				document.participantEnrollmentSummaryReportForm.ssnTwo.value = "";
				document.participantEnrollmentSummaryReportForm.ssnThree.value = "";
			}	
	}
	
}


</script>
</c:if>




<!-- Remove the extra column at the before the report -->
<c:if test="${empty param.printFriendly }" >
	<td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td >
</c:if>

<c:if test="${not empty param.printFriendly }" >
	<td>
</c:if>
<c:if test="${empty param.printFriendly }" >
       		 <p>
       		 	<c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />
            	<div id="errordivcs"><content:errors scope="session"/></div>
            	</c:if>
            </p>
</c:if>

<!-- Beginning of Participant Deferral Changes report body -->




      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<ps:form method="POST" action="/do/participant/participantEnrollmentSummary" modelAttribute="participantEnrollmentSummaryReportForm" name="participantEnrollmentSummaryReportForm">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="isSearch"/>

      <!-- set the column spacing for the report if it has internet enrollments or not, payroll or division or neither-->
    <!--has internet enrollments - show all columns-->
	<%if(hasInternetEnrollments){%>
        <%if(showPayrollColumn){%>
        	<%if(showDivisionColumn){%>
         		<tr>
          			<td width="1"></td>
          			<td width="160"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="66"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="80"></td><!--Payroll number-->
          			<td width="1"></td>
           			<td width="81"></td><!--Division-->
          			<td width="1"></td>
          			<td width="60"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="60"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
          			<td width="60"></td><!--Eligible to Defer-->
          			<td width="1"></td>   
					      <td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	
        	<%}%>
        <%}%>	
    <%}%>    
       
    <!--has internet enrollments - not show payroll && show Division-->
    <%if(hasInternetEnrollments){%>
        <%if(!showPayrollColumn){%>
        	<%if(showDivisionColumn){%>
         		<tr>
          			<td width="1"></td>
          			<td width="195"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="70"></td><!--Birth Date-->
          			<td width="1"></td>
           			<td width="93"></td><!--Division-->
          			<td width="1"></td>
          			<td width="70"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="70"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
          			<td width="70"></td><!--Eligible to Defer-->
          			<td width="1"></td> 
          			<td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<%}%>
        <%}%>	       
	<%}%>        

    <!--has internet enrollments - show payroll && not show Division-->
    <%if(hasInternetEnrollments){%>        
        <%if(showPayrollColumn){%>
        	<%if(!showDivisionColumn){%>
         		<tr>
          			<td width="1"></td>
          			<td width="195"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="70"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="93"></td><!--Payroll number-->
          			<td width="1"></td>
          			<td width="70"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="70"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
          			<td width="70"></td><!--Eligible to Defer-->
          			<td width="1"></td>
          			<td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<%}%>
        <%}%>	       
	<%}%>         

    <!--has internet enrollments - show neither payroll or division columns-->
    <%if(hasInternetEnrollments){%> 
        <%if(!showPayrollColumn){%>
        	<%if(!showDivisionColumn){%>
         		<tr>
          			<td width="1"></td>
          			<td width="199"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="83"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="101"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="83"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
          			<td width="83"></td><!--Eligible to Defer-->
          			<td width="1"></td>
					      <td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<%}%>
        <%}%>	        
    <%}%>     

  <!--no internet enrollments - hide beneficiaries columns, show payroll and division columns-->
	<%if(!hasInternetEnrollments){%>
    <%if(showPayrollColumn){%>
      <%if(showDivisionColumn){%>
         <%if(!hasAutoEnrollments){%>
           	  <tr>
            			<td width="1"></td>
            			<td width="265"></td><!--Name/SSN-->
  					      <td width="1"></td>
            			<td width="93"></td><!--Birth Date-->
            			<td width="1"></td>
            			<td width="86"></td><!--Payroll number-->
            			<td width="1"></td>          			
             			<td width="83"></td><!--Division-->
            			<td width="1"></td>
            			<td width="79"></td><!--Enrollment Method-->
            			<td width="1"></td>
            			<td width="83"></td><!--Enrollment Processing Date-->
  					      <td width="4"></td>
            			<td width="1"></td>
          		</tr>
        <% } else { %>
           		<tr>
            			<td width="1"></td>
            			<td width="265"></td><!--Name/SSN-->
  					      <td width="1"></td>
            			<td width="80"></td><!--Birth Date-->
            			<td width="1"></td>
            			<td width="72"></td><!--Payroll number-->
            			<td width="1"></td>          			
             			<td width="70"></td><!--Division-->
            			<td width="1"></td>
            			<td width="66"></td><!--Enrollment Method-->
            			<td width="1"></td>
            			<td width="70"></td><!--Enrollment Processing Date-->
            			<td width="1"></td>
  					      <td width="78"></td><!--Eligible to Defer-->
  					      <td width="1"></td>
					        <td width="120"></td><!--Deferral at enrollment-->
  					      <td width="4"></td>
            			<td width="1"></td>
          		</tr>
        	<%}%>
        <%}%>	
      <%}%>    
    <%}%>
        
    <!--no internet enrollments - hide beneficiaries columns, not show payroll && show Division-->
    <%if(!hasInternetEnrollments){%>
      <%if(!showPayrollColumn){%>
        <%if(showDivisionColumn){%>
        	<%if(!hasAutoEnrollments){%>
        	  <tr>
          			<td width="1"></td>
          			<td width="306"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="97"></td><!--Birth Date-->
          			<td width="1"></td>          			
           			<td width="97"></td><!--Division-->
          			<td width="1"></td>
          			<td width="93"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="97"></td><!--Enrollment Processing Date-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<% } else { %>
         		<tr>
          			<td width="1"></td>
          			<td width="290"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="81"></td><!--Birth Date-->
          			<td width="1"></td>          			
           			<td width="80"></td><!--Division-->
          			<td width="1"></td>
          			<td width="78"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="80"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
					      <td width="80"></td><!--Eligible to Defer-->
					      <td width="1"></td>
					      <td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        <%}%>
      <%}%>	       
    <%}%>
	<%}%>        

    <!--no internet enrollments - hide beneficiaries columns, show payroll && not show Division-->
    <%if(!hasInternetEnrollments){%>        
      <%if(showPayrollColumn){%>
        <%if(!showDivisionColumn){%>
          <%if(!hasAutoEnrollments){%>
        	  <tr>
          			<td width="1"></td>
          			<td width="306"></td><!--Name/SSN-->
           			<td width="1"></td>
          			<td width="97"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="97"></td><!--Payroll number-->
          			<td width="1"></td>
          			<td width="93"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="97"></td><!--Enrollment Processing Date-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<% } else { %>
         		<tr>
          			<td width="1"></td>
          			<td width="290"></td><!--Name/SSN-->
           			<td width="1"></td>
          			<td width="81"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="81"></td><!--Payroll number-->
          			<td width="1"></td>
          			<td width="77"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="80"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
					      <td width="80"></td><!--Eligible to Defer-->
					      <td width="1"></td>
					      <td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        <%}%>
      <%}%>	       
    <%}%>	
	<%}%>         

    <!--no internet enrollments - hide beneficiaries columns, show neither payroll or division columns-->
    <%if(!hasInternetEnrollments){%> 
      <%if(!showPayrollColumn){%>
        <%if(!showDivisionColumn){%>
          <%if(!hasAutoEnrollments){%>
        	  <tr>
          			<td width="1"></td>
          			<td width="352"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="117"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="105"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="117"></td><!--Enrollment Processing Date-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<% } else { %>
         		<tr>
          			<td width="1"></td>
          			<td width="328"></td><!--Name/SSN-->
          			<td width="1"></td>
          			<td width="96"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="90"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="96"></td><!--Enrollment Processing Date-->
                <td width="1"></td>
					      <td width="80"></td><!--Eligible to Defer-->
					      <td width="1"></td>
					      <td width="120"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>
        	<%}%>
        <%}%>	        
      <%}%>
    <%}%>     
    
 		<tr class="tablehead">
			<td class="tableheadTD1" colspan="<%=numberOfDisplayColumns%>">
            <!-- Participant deferral changes, paging -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr> 
          			<td colspan="3" >


						<table width="680" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td >					
									<b>Participants enrolled </b>&nbsp;from&nbsp;
								</td>
								<td >
             		    			<c:if test="${not empty param.printFriendly }" >
<input readonly="true" type="text" name="fromDate" size="10" value="${participantEnrollmentSummaryReportForm.fromDate}" class="inputAmount">
        							</c:if>
        						
        							<c:if test="${empty param.printFriendly }" >
<input type="text" name="fromDate" size="10" tabindex="10" value="${participantEnrollmentSummaryReportForm.fromDate}" class="inputAmount">
        								<a href="javascript:calFromDate.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
        							</c:if>
									&nbsp;to
								</td>
								<td >
            		    			<c:if test="${not empty param.printFriendly }" >
<input readonly="true" type="text" name="toDate" size="10" value="${participantEnrollmentSummaryReportForm.toDate}" class="inputAmount">
        							</c:if>
        							<c:if test="${empty param.printFriendly }" >
<input type="text" name="toDate" size="10" tabindex="12" value="${participantEnrollmentSummaryReportForm.toDate}" class="inputAmount">
        				    			<a href="javascript:calToDate.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
        							</c:if>
        						</td>
								<td  align="left">
									<ps:label fieldId="lastName" mandatory="false"><b>Participant last name</b></ps:label>
								</td>
								<td >
 									<c:if test="${not empty param.printFriendly }" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" readonly="true" cssClass="inputField" />
									</c:if>
									<c:if test="${empty param.printFriendly }" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" cssClass="inputField" tabindex="30" />
									</c:if>
								</td>    
								<td >					
									<c:if test="${empty param.printFriendly }" >
										<input type="submit" name="search" value="search" tabindex="50" onClick="javascript:doSubmit();"/>										
									</c:if>
								</td>	
							</tr>
	      					<tr>
        						<c:if test="${empty param.printFriendly }" >
          	      					<td>&nbsp;</td>
          	      					<td>(mm/dd/yyyy)</td>
          	      					<td>(mm/dd/yyyy)</td>
	            	  				<td><ps:label fieldId="ssn" mandatory="false"><b>Participant SSN</b></ps:label></td>
	            	  				<td>
										<ps:password  property="ssnOne"  styleClass="inputField" onkeypress="clearName(event);"  onkeyup="return autoTab(this, 3, event);"  size="3" maxlength="3"  tabindex="40"/>
										<ps:password  property="ssnTwo"  styleClass="inputField" onkeypress="clearName(event);"  onkeyup ="return autoTab(this, 2, event);" size="2" maxlength="2"  tabindex="41"/>
										<ps:text  property="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"   onkeyup ="return autoTab(this, 4, event);" size="4" maxlength="4"  tabindex="42"/>
	            	  				</td>
									<td>&nbsp;</td>
        						</c:if>
        						<c:if test="${not empty param.printFriendly }" >
          	      					<td>&nbsp;</td>
          	      					<td>(mm/dd/yyyy)</td>
          	      					<td>(mm/dd/yyyy)</td> 
									<td><ps:label fieldId="ssn" mandatory="false"><b>Participant SSN</b></ps:label></td>
									<td>								
										<ps:password  property="ssnOne"  readonly="true" styleClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
										<ps:password  property="ssnTwo"  readonly="true"  styleClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
										<ps:text  property="ssnThree" autocomplete="off" readonly="true"  styleClass="inputField"  onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
	       							</td>
									<td>&nbsp;</td>
								</c:if>
          	      			<tr>
						</table>




					</td>
          	      </tr>
 
        	    <tr>
			   		<td class="tableheadTD" width="100"></td>
               		<td class="tableheadTDinfo" align="right"><b><report:recordCounter report="theReport" label="Total participant enrollments"/></b></td>
               		<td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="participantEnrollmentSummaryReportForm"/></td>
            	</tr>
             </table>                 




          </td>
        </tr>
        <tr class="tablesubhead"> 
          <td class="databorder" width="1"></td>
          <td valign="bottom"><b><NOBR><report:sort field="lastName" direction="asc" formName="participantEnrollmentSummaryReportForm">Name</report:sort>/SSN</b></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"></td>
				  <td valign="bottom"><b><NOBR><report:sort field="birthDate" direction="asc" formName="participantEnrollmentSummaryReportForm">Date of<br>birth</b></report:sort></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"></td>
          <%if(showPayrollColumn){%>
          		<td valign="bottom"><b><NOBR><report:sort field="employerDesignatedID" direction="desc" formName="participantEnrollmentSummaryReportForm">Payroll<br>number</b></report:sort></NOBR></td>
          		<td class="dataheaddivider" valign="bottom" width="1"></td>
          
          <%}%>
          <%if(showDivisionColumn){%>
          		<td valign="bottom"><b><NOBR><report:sort field="organizationUnitID" direction="desc" formName="participantEnrollmentSummaryReportForm">Division</b></report:sort></NOBR></td>
          		<td class="dataheaddivider" valign="bottom" width="1"></td>
          <%}%>
          <td colspan="1" valign="bottom"><b><NOBR><report:sort field="enrollmentMethod" direction="asc" formName="participantEnrollmentSummaryReportForm">Enrollment<br>method</b></report:sort></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"></td>
<c:if test="${empty param.printFriendly }" >
          <td valign="bottom"><b><NOBR><report:sort field="enrollmentProcessedDate" direction="asc" formName="participantEnrollmentSummaryReportForm">Enrollment<br>processing<br>date</b></report:sort></NOBR></td>
</c:if>
<c:if test="${not empty param.printFriendly }" >
          <td valign="bottom"><b>Enrollment<br>processing<br>date</b></b></td>
</c:if>  
		      <%if(hasInternetEnrollments || hasAutoEnrollments){ %>
		  		    <td class="dataheaddivider" valign="bottom" width="1"></td>		  		
				      <td valign="bottom"><b><NOBR><report:sort field="eligibleToDeferInd" direction="asc" formName="participantEnrollmentSummaryReportForm">Eligible<br>to<br>defer</b></report:sort></NOBR></td>
          <% } %>		  
          <%if(hasInternetEnrollments || hasAutoEnrollments){%>
          		<td class="dataheaddivider" valign="bottom" width="1"></td>
          		<td valign="bottom"><b><NOBR>Deferrals<br>at Enrollment</b></NOBR></td>
           <%}%>	
          <td class="databox"></td>
          <td rowspan="1" class="databorder" width="1"></td>
        </tr>



 <!-- Start of Details -->
<% boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
   boolean highlight = false;	// used to determine if the style class has to change
%>

<c:if test="${empty param.printFriendly }" >

        <tr class="datacell1">
          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="top" colspan="<%=numberOfDisplayColumnsLessTwo%>">

		<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) { // we have search results %>
		<% } else { // no results %>
			<!-- no results -->
			<%if(initialSearch){%>
				<content:getAttribute id="NoEnrollmentsDuringPast30Days" attribute="text"/>
			<%} else {%>
				<c:if test="${empty pageScope.errorsExist}">
					<content:getAttribute id="NoSearchResults" attribute="text"/>
				</c:if>
			<%}%>	
		<% 
		   }
		   lastLineBkgrdClass = true;
		 %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>





<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<% ParticipantEnrollmentSummary theItem=(ParticipantEnrollmentSummary)pageContext.getAttribute("theItem"); %>
<c:set var="theIndex" value="${theIndex.index}"/>
<%String temp = pageContext.getAttribute("indexValue").toString(); %>
<% if (Integer.parseInt(temp) % 2 != 0) {
	 	beigeBackgroundCls = true;
	 	lastLineBkgrdClass = true; %>
        <tr class="datacell1">
<% } else {
		beigeBackgroundCls = false;
		lastLineBkgrdClass = false; %>
    	<tr class="datacell2">
<% } %>
          <td class="databorder" width="1"></td>
           <td align="left"><ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="linkProfileId">
${theItem.lastName},
${theItem.firstName}
          	  	</ps:link>
				<br><NOBR><render:ssn property="theItem.ssn" /></NOBR>
			</td>
          <td class="datadivider" width="1"></td>
					<td align="left"><render:date property="theItem.birthDate" defaultValue="Not entered" patternOut="MM/dd/yyyy"/></td>
          <td class="datadivider" width="1"></td>
          <%if(showPayrollColumn){%>
<td align="left">${theItem.employerDesignatedID}</td><!--Payroll-->
          		<td class="datadivider" width="1"></td>
          <%}%>
          <%if(showDivisionColumn){%>
<td align="left">${theItem.organizationUnitID}</td><!--Division-->
          		<td class="datadivider" width="1"></td>
          <%}%>
<td align="left">${theItem.enrollmentMethod}</td>
          <td class="datadivider" width="1"></td>     
          <td align="left"><render:date property="theItem.enrollmentProcessedDate" patternOut="MM/dd/yyyy"/>
          	  <br>
          	  <ps:link action="/do/participant/participantEnrollmentDetails" paramId="profileId" paramName="theItem" paramProperty="linkProfileId">	
          	  	(details)
          	  </ps:link>
		 </td>
		 <% if(hasInternetEnrollments || hasAutoEnrollments) { %>
		    <% if(theItem.getEnrollmentMethod() != null && 
				(theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Internet") ||
				  theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Auto") ||
					theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Was auto"))) { %>
        		  <%if(theItem.getEligibleToDeferInd() == null || theItem.getEligibleToDeferInd().trim().equals("N/A")){ %>	
        		    <% if(!theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Internet")) { %>
        		      <td class="datadivider" width="1"></td>  
            		  <td align="center">Yes</td>
        		    <% }else{%>
					        <td class="datadivider" width="1"></td>  
<td align="center">${theItem.eligibleToDeferInd}</td>
        		    <% } %>
					    <% }else{%>
					      <td class="datadivider" width="1"></td>  
<td align="center">${theItem.eligibleToDeferInd}</td>
					    <% } %>
				<% }else{%>
				  <td class="datadivider" width="1"></td>  
          <td align="center">N/A</td>				
			  <% } %>		
      <% } %>
      <%if(hasInternetEnrollments || hasAutoEnrollments){%>
 			<td class="datadivider" width="1"></td>   			
			<%if (!theItem.getDeferralComment().trim().equals("Entered")){%>
<td align="right">${theItem.deferralComment}</td>
			<%} else if (theItem.getDeferralComment().trim().equals("Entered")){%>

		<td align="right">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">	
				<%if (theItem.hasTradDeferral()){%>

					<%if (theItem.getContributionAmt() > 0){%>
						<tr>
							<td width="50%" align="left">401(k)</td>
							<td width="50%" align="right"><render:number property='theItem.contributionAmt' defaultValue = ""  pattern="$###,##0.00"/></td>
						</tr>
					<%} else if (theItem.getContributionPct() > 0){ %>
						<tr>
							<td width="50%" align="left">401(k)</td>
							<td width="50%" align="right"><render:number property='theItem.displayContributionPct' defaultValue = ""  pattern="###%"/></td>
						</tr>
					<%} else {%>
						<tr>
							<td width="50%" align="left">401(k)</td>
							<td width="50%" align="right">None</td>
						</tr>
					<%}%>		
				<%} else { %>
				    <tr>
							<td width="50%" align="left">401(k)</td>
							<td width="50%" align="right">N/A</td>
						</tr>
				<%}%>		
				<%if (theItem.hasRothDeferral()){%>		
					<%if (theItem.getContributionAmtRoth() > 0){%>
						<tr>
							<td width="50%" align="left">Roth</td>
							<td width="50%" align="right"><render:number property='theItem.contributionAmtRoth' defaultValue = ""  pattern="$###,##0.00"/></td>
						</tr>
					<%} else if (theItem.getContributionPctRoth() > 0){ %>
						<tr>
							<td width="50%" align="left">Roth</td>
							<td width="50%" align="right"><render:number property='theItem.displayContributionPctRoth' defaultValue = ""  pattern="###%"/></td>
						</tr>
					<%} else {%>
						<tr>
							<td width="50%" align="left">Roth</td>
							<td width="50%" align="right">None</td>
						</tr>
					<%}%>
				<%}%>
			</table>
		</td>
          		




				<%//if (theItem.getContributionAmount() > 0){%>
          			<!--<td align="right"><render:number property='theItem.contributionAmount' defaultValue = ""  pattern="$###,##0.00"/></td>-->
          		<%//}%>
         		<%//if (theItem.getContributionPercent() > 0){%>
         			<!--<td align="right"><render:number property='theItem.displayContributionPct' defaultValue = ""  pattern="###%"/></td>-->
				<%//}%>
			<%}%>
         <%}%>	       
<% if (beigeBackgroundCls) { %>
	<td class="dataheaddivider">
<% } else { %>
	<td class="beigeborder">
<% } %>          <img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="databorder" width="1"></td>
        </tr>


          <!--<td class="beigeborder" width="1">


      </tr>-->
</c:forEach>
</c:if>     





<!-- End of Details -->
<!-- let the last line have the same background colour as the previous line -->
<% if (lastLineBkgrdClass) { %>
	<tr class="datacell1">
<% } else { %>
	<tr class="datacell2">
<% } %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="lastrow"></td>
          <td class="datadivider"></td>
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
          <td class="lastrow"></td>
          <%if(hasInternetEnrollments || hasAutoEnrollments){%>
         	  <td class="datadivider"></td>
          	<td class="lastrow"></td>
          <%}%>
          <%if(hasInternetEnrollments || hasAutoEnrollments){%>
          	<td class="datadivider"></td>
          	<td class="lastrow"></td>
          <%}%>	
          <td rowspan="2" colspan="2" width="5" class="lastrow"  align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
<!-- End of Last line -->




        <tr>
        	<td class="databorder" colspan="<%=numberOfDisplayColumnsLessTwo%>"></td>
        </tr>

        <tr>
         	<td colspan="<%=numberOfDisplayColumns%>" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="participantEnrollmentSummaryReportForm"/></td>
        </tr>
		<tr>
          <td colspan="<%=numberOfDisplayColumns%>" class="boldText">&nbsp;</td>
        </tr>			
		<tr>
	   		<td colspan="<%=numberOfDisplayColumns%>">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
  		 </tr>	





		</ps:form>  

		<script type="text/javascript" >
		<!-- // create calendar object(s) just after form tag closed
			var calFromDate = new calendar(document.forms['participantEnrollmentSummaryReportForm'].elements['fromDate']);
			calFromDate.year_scroll = true;
			calFromDate.time_comp = false;
			var calToDate = new calendar(document.forms['participantEnrollmentSummaryReportForm'].elements['toDate']);
			calToDate.year_scroll = true;
			calToDate.time_comp = false;
		//-->
		</script>

      </table>
    


      <br>
    </td>

    <!-- column to the right of the report -->
    <td width="15"></td>

<c:if test="${not empty param.printFriendly }" >
	</td>
</c:if>

<c:if test="${not empty param.printFriendly }" >
		<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
            id="globalDisclosure"/>

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
</c:if>
