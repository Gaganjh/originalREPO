<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.TaskCenterHistoryReportForm" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
 <%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryReportData" %>       
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryDetails" %>
<%@ page import="com.manulife.pension.ps.web.participant.TaskCenterHistoryReportForm" %>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">


<%boolean errors = false;%>
<%
TaskCenterHistoryReportData theReport = (TaskCenterHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%
TaskCenterHistoryReportForm taskCenterHistoryReportForm = (TaskCenterHistoryReportForm)session.getAttribute("taskCenterHistoryReportForm");
pageContext.setAttribute("taskCenterHistoryReportForm",taskCenterHistoryReportForm,PageContext.PAGE_SCOPE);
%>
		

<c:set var="enablePrint" value="${enablePrint}" />
<%Boolean enablePrint=(Boolean)pageContext.getAttribute("enablePrint"); %>

<style type="text/css">
DIV.scroll {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 118px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}
</style>


<c:if test="${empty param.printFriendly }" >

<script type="text/javascript" >


function doOnload() {

<% if (request.getAttribute("openPrintWindow") !=null ) { %>


   doNewHistoryPrintWindow();
   
<% } %>

}


function doReset() {
	document.taskCenterHistoryReportForm.task.value = "default";
	return true;
}


function doSave() {
    document.forms['taskCenterHistoryReportForm'].elements['task'].value = "save"; 
    document.forms['taskCenterHistoryReportForm'].submit();
}


function clearName(evt){

	//IE or browsers that use the getElementById model
	if (document.getElementById('lastName')) {
		if (document.getElementById('lastName').value) {
			document.getElementById('lastName').value = "";
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

	if (document.taskCenterHistoryReportForm.ssnOne) {
			if (document.taskCenterHistoryReportForm.ssnOne.value.length >= 0){
				document.taskCenterHistoryReportForm.lastName.value = "";
			}
	}


	if (document.taskCenterHistoryReportForm.ssnTwo) {
			if (document.taskCenterHistoryReportForm.ssnTwo.value.length >= 0){
				document.taskCenterHistoryReportForm.lastName.value = "";
			}
	}

	if (document.taskCenterHistoryReportForm.ssnThree) {
			if (document.taskCenterHistoryReportForm.ssnThree.value.length >= 0){
				document.taskCenterHistoryReportForm.lastName.value = "";
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


	if (document.taskCenterHistoryReportForm.lastName) {
			if (document.taskCenterHistoryReportForm.lastName.value.length >= 0){
				document.taskCenterHistoryReportForm.ssnOne.value = "";
				document.taskCenterHistoryReportForm.ssnTwo.value = "";
				document.taskCenterHistoryReportForm.ssnThree.value = "";
			}
	}

}



function checkPrintAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var printCB = document.forms['taskCenterHistoryReportForm'].elements['report.details[' + i + '].print'];
      printCB.checked=true;
   }
}

function uncheckPrintAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var printCB = document.forms['taskCenterHistoryReportForm'].elements['report.details[' + i + '].print'];
      printCB.checked=false;
   }
}


function areAllSetToPrint() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var printCB = document.forms['taskCenterHistoryReportForm'].elements['report.details[' + i + '].print'];
      if (printCB.checked == false) return false;
   }
   
   return true;
}

function isAnyPrintCheckboxSelected() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var printCB = document.forms['taskCenterHistoryReportForm'].elements['report.details[' + i + '].print'];
      if (printCB.checked) {
          return true;
      }
   }
   
   return false;
}

function printAll() {
  if (areAllSetToPrint()) {
     uncheckPrintAll();
  } else {
     checkPrintAll(); 
  }

  // Either enable or disable Print button. Can't do it in onClick of PrintAll 
  // link, as the order matters and onClick is invoked before printAll
  enableSubmit();
}

function enableSubmit() {
	var x=document.getElementById("printButton");
	if (isAnyPrintCheckboxSelected()) {
     	x.disabled = false;
  	} else {
	    x.disabled = true;
    }
}


function doHistoryPrint() {
  document.taskCenterHistoryReportForm.task.value = "save";

  return true;
}



/**
 * Opens up a new window and perform the same request again (with printFriendly
 * parameter.
 */
function doNewHistoryPrintWindow() {
  
  var reportURL = new URL();
  reportURL.setParameter("task", "historyPrint");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,menubar,resizable,toolbar,scrollbars,");

  return true;
}

</script>

</c:if>

<%
   // optional columns 
boolean readOnly = (request.getParameter("printFriendly") !=null);
   boolean showDivColumn  = userProfile.getCurrentContract().hasSpecialSortCategoryInd();
   int divColumn = (showDivColumn ? 2: 0);
   int actionColumn = (readOnly ? 0 : 2);
   int columnSize = 14 + divColumn + actionColumn;
   pageContext.setAttribute("readOnlyflag",readOnly,PageContext.PAGE_SCOPE); 
%>


<c:if test="${empty param.printFriendly }" >
  <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  <td >
</c:if>


<ps:form method="POST" modelAttribute="taskCenterHistoryReportForm" name="taskCenterHistoryReportForm" action="/do/participant/taskCenterHistory">
<input type="hidden" name="task" value="filter"/>


<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
     <td>
       <img src="/assets/unmanaged/images/s.gif" width="1" height="20">
     </td>
     <td>        
   		<c:if test="${sessionScope.psErrors ne null}">
   		  <table width="730" border="0" cellspacing="0" cellpadding="0">
   		    <tr><td>
<c:set var="errorsExist" value="true" scope="page" />
	        	<div id="errordivcs"><content:errors scope="session"/></div>
	        	<%errors=true;%>
	        	</br>
        	</td></tr>
          </table>
        </c:if>
      </td>
  </tr>
  <tr>
    <td>
       <img src="/assets/unmanaged/images/s.gif" width="1" height="20">
    </td>
    <td  valign="top">
      <table width="730" border="0" cellspacing="0" cellpadding="0">
		  <%-- TAB section --%>
		  <tr>
		      <td valign="bottom" colspan="1">
		        <DIV class="nav_Main_display" id="div">
		          <UL class="">
		          <c:if test="${not empty param.printFriendly }" >           
		            <LI id="tab1" class="on" >Deferrals</LI>
		          </c:if>
		          <c:if test="${empty param.printFriendly }" >
		            <LI id="tab3" class="on">Deferrals</LI>
		          </c:if>
		          </UL>
		        </DIV>
		      </td>
		      <td colspan="4">&nbsp;</td>
		  </tr>
		  <tr>
		    <td colspan="1" height="25" class="tablesubhead">&nbsp;<b>
                <c:if test="${empty param.printFriendly }" >		    
		          <a href="../participant/taskCenterTasks">Tasks</a>
		        </c:if>
		        </b>&nbsp;&nbsp;&nbsp;&nbsp;<b>History</b>
		    </td>
		    <td colspan="4" class="tablesubhead"></td>
		  </tr>

          <tr class="tablehead">
             <td class="filterSearch" colspan="5">
                <b>Search</b></br>
                To search for a participant by last name or SSN or initiated date, make your selection below and click 'search' to complete your request.
              </td>
          </tr>
          <tr class="filterSearch">
                  <td valign="top"><b>Last name</b></br>
<form:input path="lastName" maxlength="30" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" readonly="${readOnlyflag}" cssClass="inputField"/>


                  </td>
                  <td valign="top"><b>SSN</b></br>
                  <form:password path="ssnOne"  styleClass="inputField" onkeypress="clearName(event);"
					        readonly="<%=readOnly%>" value="${taskCenterHistoryReportForm.ssnOne}"
							onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" />
				   <form:password path="ssnTwo"  styleClass="inputField" onkeypress="clearName(event);"
					        readonly="<%=readOnly%>" value="${taskCenterHistoryReportForm.ssnTwo}"					
							onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" />
					<form:input path="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"
					        readonly="<%=readOnly%>"						
							onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4" />
                  	</td>
<% if (showDivColumn) { %>                                      
                  <td valign="top"><b>Division</b></br>
<form:input path="division" onchange="setFilterFromInput(this);" readonly="${readOnlyflag}" cssClass="inputField"/>


                  </td>
<% } else { %>
				  <td valign="top"></td>
<% } %>                  
                  <td valign="top"> 
                    <b>Initiate date</b></br>
                    <table>
                     <tr>
                       <td>From</td>
<td><form:input path="fromDate" maxlength="10" size="10" cssClass="inputField"/>&nbsp;<a href="javascript:calFromDate.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a></td>
                       <td>&nbsp;&nbsp;</td>
                       <td>To</td>
<td><form:input path="toDate" maxlength="10" readonly="<%=readOnly%>" size="10" cssClass="inputField"/>&nbsp;<a href="javascript:calToDate.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a></td>
                      </tr>
                      <tr>
                       <td/>
                       <td>(mm/dd/yyyy)</td>
                       <td/>
                       <td/>
                       <td>(mm/dd/yyyy)</td>
                      <tr>
                    </table>                        
                  </td>
                  <td width="30"></td>
          </tr> 
          <tr class="filterSearch">
            <td colspan="5"/>
          </tr>
          <tr class="filterSearch">
              <td colspan="3"></td>
              <td height="30" align="right">
                    <c:if test="${empty param.printFriendly }" >
            	      <input type="submit" name="submit" value="search"  />
            	      &nbsp;&nbsp;                        
                      <input type="submit" name=" reset " value="reset" onclick="return doReset();"/>
                    </c:if>              
              </td>
              <td></td>
          </tr>
        </tr>
      </table>
      
      </td></tr>
             
       
     <tr>
       <td><img src="/assets/unmanaged/images/s.gif" width="1" height="20"></td>
       <td>
	  
      <table width="730" border="0" cellspacing="0" cellpadding="0">      
         <tr>
<c:if test="${empty param.printFriendly }" >         
          <td class="dataheaddivider" width="1" ></td>
          <td></td>
</c:if>          
          <td class="dataheaddivider" width="1" ></td>
          <td></td>
          <td class="dataheaddivider" width="1" ></td>
<% if (showDivColumn) { %>                    
          <td></td>
          <td class="dataheaddivider" width="1" ></td>
<% } %>          
          <td></td>
          <td class="dataheaddivider" width="1" ></td>
          <td></td>
          <td class="dataheaddivider" width="1"></td>
          <td></td>          
          <td class="dataheaddivider" width="1"></td>
          <td></td>                    
          <td class="dataheaddivider" width="1"></td>
          <td></td>
          <td class="dataheaddivider" width="4" height="1"></td>                    
          <td class="dataheaddivider" width="1"></td>                    
        </tr>
          
       <tr>
         <td colspan="<%=columnSize%>">&nbsp;</td>
       </tr>
       </tr>   
          
       <tr>
         <td colspan="<%=columnSize%>">
		    <%-- Legend --%>
		    <c:if test="${empty param.printFriendly }" > 
		          <strong>Legend:&nbsp;</strong>
		          <IMG height=12 src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>&nbsp; View &nbsp;
		          <IMG height=12 src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>&nbsp; Edit &nbsp;
		    </c:if>         
         </td>
       </tr>
    
  
       <tr class="tablehead">
            <td class="tableheadTD1" colspan="<%=columnSize%>">
          
			<table  border="0" cellspacing="0" cellpadding="0" width="100%">
			          <tr>
			            <%if(errors==false){%>
			              <td class="tableheadTD"></td>
			              <td align="center" class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Requests"/></b></td>
			              <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="taskCenterHistoryReportForm"/></td>
			            <%} else { %>
			              <td class="tableheadTD"></td> 
			              <td align="left" class="tableheadTDinfo">&nbsp;</td>
			              <td align="right" class="tableheadTDinfo"></td>                 
			            <%} %>
			          </tr>			
			</table>       
	      </td>
       </tr>

       <tr class="tablesubhead">
<c:if test="${empty param.printFriendly }" >       
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>&nbsp;<b>Action</b>&nbsp;</td>
</c:if>          
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>  
          <td><b><report:sort field="lastName" direction="asc" formName="taskCenterHistoryReportForm">Name</b></report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (showDivColumn) { %>                    
          <td><b><report:sort field="division" direction="asc" formName="taskCenterHistoryReportForm">Division</b></report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% } %>          
          <td><b><report:sort field="type" direction="asc" formName="taskCenterHistoryReportForm">Type</b></report:sort></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="initiated" direction="asc" formName="taskCenterHistoryReportForm">Initiated</report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td nowrap><b><report:sort field="actionDate" direction="asc" formName="taskCenterHistoryReportForm">Action Taken</report:sort></b></td>          
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>          
          <td><b>Details</b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>          
          <td nowrap><b>Print</b></br>
<c:if test="${empty param.printFriendly}" >          
            <a href="javascript:printAll();">Print all</a>
</c:if>            
          </td>          
          <td></td>                    
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>          
        </tr>
     

<% if(errors==false){ 
   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM d, yyyy");

   if (theReport.getDetails().size() == 0)  { %>
    <tr class="datacell1">
       <td class="databorder"></td>
       <td colspan="<%=(columnSize-2)%>"> 
         No tasks found for the current search criteria
       </td>
       <td class="databorder" valign="top" width="1" height="1"></td>                     
    </tr>
<% } %>


<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%TaskCenterHistoryDetails theItem=(TaskCenterHistoryDetails)pageContext.getAttribute("theItem"); %> 
<c:set var="indexValue" value="${theIndex.index}"/>
			<%String temp = pageContext.getAttribute("indexValue").toString(); 
			if (Integer.parseInt(temp)% 2 == 0) { %>
        <tr class="datacell2">
<% } else { %>
        <tr class="datacell1">
<% } %>

<c:if test="${empty param.printFriendly }" >
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="center">
            <a href="/do/census/viewEmployeeSnapshot/?profileId=<%=theItem.getProfileId()%>&source=taskCenterHistory">
              <img src="/assets/unmanaged/images/view_icon.gif" alt="View" border="0"/></a>
            
            <a href="/do/census/editEmployeeSnapshot/?profileId=<%=theItem.getProfileId()%>&source=taskCenterHistory">
              <img src="/assets/unmanaged/images/edit_icon.gif" alt="Edit" border="0"/></a>
          </td>
</c:if>          
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <td valign="top" nowrap>
<c:if test="${empty param.printFriendly }" >          
            <a href="/do/participant/participantAccount/?profileId=<%=theItem.getProfileId()%>"><%=theItem.getName()%></a></br>            
            <%=theItem.getSsn()%>
</c:if>            
<c:if test="${not empty param.printFriendly }" >          
            <%=theItem.getName()%></br>            
            <%=theItem.getSsn()%>
</c:if>            
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% if (showDivColumn) { %>                    
          <td valign="top" nowrap><%=theItem.getDivision()%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% } %>          
          <td valign="top" nowrap><%=theItem.getType(theReport.getAutoOrSignup())%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% if (userProfile.isInternalUser()) { %>	            	 			          
          <td valign="top" nowrap 
           title="Source of change: <%=theItem.getInitiatedSource()+"&#10;&#13;Initiated by: "+theItem.getInitiatedByInternalView()%>">
<% } else { %>
		  <td valign="top" nowrap 
		   title="Source of change: <%=theItem.getInitiatedSource()+"&#10;&#13;Initiated by: "+theItem.getInitiatedByExternalView()%>">
<% } %>
${theItem.initiated}
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% if (userProfile.isInternalUser()) { %>	            	 			          
          <td valign="top" nowrap 
           title="Processed location: <%=theItem.getProcessedSource()+"&#10;&#13;Processed by: "+theItem.getProcessedByInternal()%>">
<% } else { %>
		  <td valign="top" nowrap 
		   title="Processed location: <%=theItem.getProcessedSource()+"&#10;&#13;Processed by: "+theItem.getProcessedByExternal()%>">
<% } %>          
${theItem.actionDate}
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>          
<% if (theItem.isRemarksLong()) { %>
          <td valign="top" title="<%=theItem.getRemarks()%>" nowrap>
<% } else { %>
          <td valign="top" nowrap>
<% } %>          
            <%=theItem.getDetails()%><br/><%=theItem.getRemarksForDisplay()%>
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>          
          <td valign="top" align="center" nowrap>
          
               
                <c:set var="disabledAP" value="false"/>
              	<c:set var="checkedAPP" value=""/>
              <c:if test="${theItem.isPrint()==true}">
             	<c:set var="checkedAPP" value="checked"/>
               </c:if>
                <c:if test="${theItem.isPrint()==''}">
              	<c:set var="disabledAP" value="false"/>
               </c:if>
			    <form:checkbox path="report.details[${theIndex.index}].print" checked="${checkedAPP}"   disabled="${disabledAP}"  onclick="enableSubmit();"/>
          </td>          
          <td></td>                    
          <td class="databorder" valign="top" width="1" height="1"></td>                    
        </tr>
</c:forEach>
</c:if>

<% } %>
        
        <ps:roundedCorner numberOfColumns="<%=String.valueOf(columnSize)%>"
                          emptyRowColor="white"
                          oddRowColor="beige"
                          evenRowColor="white"
                          name="theReport"
	                      property="details"/>   
        
	    </table>
	    
	    <table width="730">
	   	<tr>	                  	        
<%if(errors==false){%>	      
	  	  <td align="center"><b><report:recordCounter report="theReport" label="Requests"/></b></td>
	      <td align="right"><report:pageCounter report="theReport" arrowColor="black" formName="taskCenterHistoryReportForm"/></td>
<% } %>
     	</tr>
        <tr><td>&nbsp;</td> </tr>
		 <tr>	                  	        
	     	<td colspan="2" align="right" nowrap  title="Prints history items, one per page">
              <c:if test="${empty param.printFriendly }" >
                <input id="printButton" type="submit" class="button134" value="Print" 
                 onclick="return doHistoryPrint();" <%= enablePrint ? "" : "disabled" %> />
              </c:if>     	  
	        </td>
        </tr>
      
 		<script type="text/javascript" >
		<!-- // create calendar object(s) just after form tag closed
		
		//-->
		</script>
      
		 <tr>
 		    <td colspan="<%=columnSize%>">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
         </tr>
         
        </table>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
    </tr>
      
 </table>
 </td></tr>
 
</table>


</ps:form>

<script>
var calFromDate = new calendar(document.forms['taskCenterHistoryReportForm'].elements['fromDate']);
calFromDate.year_scroll = true;
calFromDate.time_comp = false;
var calToDate = new calendar(document.forms['taskCenterHistoryReportForm'].elements['toDate']);
calToDate.year_scroll = true;
calToDate.time_comp = false;
</script>



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
