<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.TaskCenterTasksReportForm" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>  
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksReportData" %> 
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksDetails" %>      
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">


<%boolean errors = false;%>

<%
TaskCenterTasksReportData theReport = (TaskCenterTasksReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);


%> 

<%
TaskCenterTasksReportForm taskCenterTasksReportForm = (TaskCenterTasksReportForm)request.getAttribute("taskCenterTasksReportForm");
pageContext.setAttribute("taskCenterTasksReportForm",taskCenterTasksReportForm,PageContext.PAGE_SCOPE);
%>		
			 
<c:set var="disableButtons" value="${disableButtons}" /> 
<c:set var="isPayrollFeedbackServiceEnabled" value="${userProfile.isPayrollFeedbackServiceEnabled()}" />

<%String disableButtons=(String)pageContext.getAttribute("disableButtons"); %>

<content:contentBean contentId="58566"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warnNoOldValue"/>

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

function doSearch() {
    if (discardChanges("Are you sure you want to search and discard changes?")==false) {
        return false;
    }
	document.taskCenterTasksReportForm.task.value = "filter";
	document.taskCenterTasksReportForm.actionValue.value = "";
	return true;
}


function doReset() {
    if (discardChanges("Are you sure you want to reset and discard changes?")==false) {
        return false;
    }
	document.taskCenterTasksReportForm.task.value = "default";
	document.taskCenterTasksReportForm.actionValue.value = "";
	return true;
}

function doCancel() {
    if (discardChanges("Are you sure you want to discard changes?")==false) {
        return false;
    }
	document.taskCenterTasksReportForm.task.value = "default";
	document.taskCenterTasksReportForm.actionValue.value = "";
	return true;
}

function doSave() {
    genSaveMessage();
	document.taskCenterTasksReportForm.actionValue.value = "save";
	return true; // ok to submit
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

	if (document.taskCenterTasksReportForm.ssnOne) {
			if (document.taskCenterTasksReportForm.ssnOne.value.length >= 0){
				document.taskCenterTasksReportForm.lastName.value = "";
			}
	}


	if (document.taskCenterTasksReportForm.ssnTwo) {
			if (document.taskCenterTasksReportForm.ssnTwo.value.length >= 0){
				document.taskCenterTasksReportForm.lastName.value = "";
			}
	}

	if (document.taskCenterTasksReportForm.ssnThree) {
			if (document.taskCenterTasksReportForm.ssnThree.value.length >= 0){
				document.taskCenterTasksReportForm.lastName.value = "";
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


	if (document.taskCenterTasksReportForm.lastName) {
			if (document.taskCenterTasksReportForm.lastName.value.length >= 0){
				document.taskCenterTasksReportForm.ssnOne.value = "";
				document.taskCenterTasksReportForm.ssnTwo.value = "";
				document.taskCenterTasksReportForm.ssnThree.value = "";
			}
	}

}


function areAllSetToDecline() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var decCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].decline'];
      if (decCB.disabled == false) {
	      if (decCB.checked == false) return false;
	  }
   }
   
   return true;
}

function areAllSetToApprove() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var appCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].approve'];
      if (appCB.disabled==false) {
         if (appCB.checked == false) return false;
      }
   }
   
   return true;
}


function declineAll() {
  if (areAllSetToDecline()) {
     uncheckDeclineAll();
  } else {
     checkDeclineAll();
  }
}

function approveAll() {
  if (areAllSetToApprove()) {
     uncheckApproveAll();
  } else {
     checkApproveAll(); 
  }
}

function checkDeclineAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var decCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].decline'];
      if (decCB.disabled==false) {
         decCB.checked=true;
         uncheckApprove(i); 
      }
   }
}

function uncheckDeclineAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var decCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].decline'];
      if (decCB.disabled==false) decCB.checked=false;
   }
}


function checkApproveAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var accCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].approve'];
      if (accCB.disabled==false) {
          accCB.checked=true;
          uncheckDecline(i); 
      }
   }
}

function uncheckApproveAll() {
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var accCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].approve'];
      if (accCB.disabled==false) accCB.checked=false;
   }
}


function uncheckDecline(index) {
   var decCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + index + '].decline'];
   decCB.checked=false;
}

function uncheckApprove(index) {
   var accCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + index + '].approve'];
   accCB.checked=false;
}


// deal with the generation of the warning popup, 8.11.1
function genSaveMessage() {
   // are any marked as Approve, not have old contrib value.
   var displayMsg = false;
   for(i=0; i < <%=theReport.getDetails().size()%>; i++) {
      var accCB = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].approve'];
      if (accCB.checked) {
           var accWarnFlag = document.forms['taskCenterTasksReportForm'].elements['report.details[' + i + '].genWarn'];
           if (accWarnFlag.value == 'true') {
              displayMsg = true;
           }
      }
   } // end for
   if (displayMsg) {
       alert("<content:getAttribute beanName="warnNoOldValue" attribute="text"/>");   // 2.10 message
   }
}


function enableSubmit() {
	var x=document.getElementById("saveButton");
	x.innerHTML='<input name="button" type="submit" class="button134" value="SAVE" onclick="return doSave();" />';
	 
	var y=document.getElementById("cancelButton");
	y.innerHTML='<input name="button" type="submit" class="button134" value="CANCEL" onclick="return doCancel();" />';
}


  // Protects all the links with checks for changes in the page with the exception of pop-ups
  function protectLinks() {
    var hrefs  = document.links;
    if (hrefs != null) {
      for (i=0; i<hrefs.length; i++) {
        if(
          hrefs[i].onclick != undefined && 
          (hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        } else if(
          hrefs[i].href != undefined && 
          (hrefs[i].href.indexOf("openWin") != -1 || 
           hrefs[i].href.indexOf("popup") != -1 || 
           hrefs[i].href.indexOf("approveAll") != -1 || 
           hrefs[i].href.indexOf("declineAll") != -1 || 
           hrefs[i].href.indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        } else if(hrefs[i].onclick != undefined) {
          hrefs[i].onclick = new Function ("var result = discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
        } else {
          hrefs[i].onclick = new Function ("return discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');");
        }
      }
    }
    
   }  

   // Track changes framework
   function isFormChanged() {
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

<%
   // optional columns 
   boolean readOnly = (request.getParameter("printFriendly") !=null);
   boolean showDivColumn  = userProfile.getCurrentContract().hasSpecialSortCategoryInd();
   int divColumn = (showDivColumn ? 2: 0);
   int actionColumn = (readOnly ? 0 : 2);
   int columnSize = 18 + divColumn + actionColumn;
   pageContext.setAttribute("readOnlyflag",readOnly,PageContext.PAGE_SCOPE); 
%>


<c:if test="${empty param.printFriendly }" >
  <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  <td >
</c:if>


<ps:form method="POST" modelAttribute="taskCenterTasksReportForm" name="taskCenterTasksReportForm" action="/do/participant/taskCenterTasks">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="actionValue" value="save"/>


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
		      <td colspan="3">&nbsp;</td>
		  </tr>
		  <tr>
		    <td colspan="1" height="25" class="tablesubhead">&nbsp;<b>Tasks</b>&nbsp;&nbsp;&nbsp;&nbsp;
<c:if test="${empty param.printFriendly }" >		    
		        <b><a href="../participant/taskCenterHistory">History</a></b>
</c:if>
		    </td>
		    <td colspan="3" class="tablesubhead"></td>
		  </tr>

          <tr class="tablehead">
             <td class="filterSearch" colspan="4">
                <b>Search</b></br>
                To search for a participant by last name or SSN, make your selection below and click 'search' to complete your request.
              </td>
          </tr>
          <tr class="filterSearch">
                  <td><b>Last name</b></br>
<form:input path="lastName" maxlength="30" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" readonly="${readOnlyflag}" cssClass="inputField"/>


                  </td>
                  <td><b>SSN</b></br>
                  <form:password path="ssnOne"  styleClass="inputField" onkeypress="clearName(event);"
					        readonly="<%=readOnly%>" value="${taskCenterTasksReportForm.ssnOne}"
							onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" />
					<form:password path="ssnTwo" styleClass="inputField" onkeypress="clearName(event);"
					        readonly="<%=readOnly%>" value="${taskCenterTasksReportForm.ssnTwo}"						
							onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" />
							<form:input path="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"
					        readonly="<%=readOnly%>"						
							onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4" />
                  	</td>
<% if (showDivColumn) { %>                  
                  <td><b>Division</b></br>
<form:input path="division" onchange="setFilterFromInput(this);" readonly="${readOnlyflag}" cssClass="inputField"/>


                  </td>
<% } else { %>
				  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<% } %>                  
                  <td> 
                    <c:if test="${empty param.printFriendly }" >
            	      <input type="submit" name="submit" value="search" onclick="return doSearch();" />
            	      &nbsp;&nbsp;                        
                      <input type="submit" name=" reset " value="reset" onclick="return doReset();" />
                    </c:if>
                  </td>
          </tr> 
        </tr>
      </table>
      
      </td></tr>
      
     <tr>
       <td><img src="/assets/unmanaged/images/s.gif" width="1" height="20"></td>
       <td>
           <jsp:include flush="true" page="taskCenterErrors.jsp"/>       
	   </td> 
     </tr>       
       
       
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
          <td></td>        
          <td class="dataheaddivider" width="1" ></td>
          <td></td>
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
          <td class="dataheaddivider" width="4" height="1"></td>                    
          <td class="dataheaddivider" width="1"></td>                    
        </tr>
      
       <tr>
         <td colspan="<%=columnSize%>">
	    <%-- Legend --%>
	    <c:if test="${empty param.printFriendly}" > 
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
			              <td align="right" class="tableheadTDinfo"><report:pageCounter formName="taskCenterTasksReportForm"  report="theReport"/></td>
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
          <c:if test="${empty param.printFriendly}" >
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td><b>Action</b></td>
          </c:if>  
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>  
          <td><b><report:sort field="lastName" formName="taskCenterTasksReportForm"  direction="asc">Name</b></report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (showDivColumn) { %>          
          <td><b><report:sort field="division"  formName="taskCenterTasksReportForm"  direction="asc">Division</b></report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% } %>          
          <td><b><report:sort field="type" formName="taskCenterTasksReportForm"  direction="asc">Type</b></report:sort></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="initiated" formName="taskCenterTasksReportForm"  direction="asc">Initiated</report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          
          <c:choose>          
          <c:when test="${isPayrollFeedbackServiceEnabled}">
          <td><b><report:sort field="effectiveDate" formName="taskCenterTasksReportForm"  direction="asc">Effective Date</report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>          
           <td colspan="5"><b>Details</b></td>
          </c:when>
          <c:otherwise>
           <td><b><report:sort field="anniversary" formName="taskCenterTasksReportForm"  direction="asc">Anniversary date</report:sort></b></td>
           <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>          
           <td><b>Details</b></td>
          </c:otherwise>
          </c:choose>
          <c:if test="${!isPayrollFeedbackServiceEnabled}">          
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b>Approve</b></br>
<c:if test="${empty param.printFriendly}" >           
           <a href="javascript:approveAll();" onclick="enableSubmit();" >Select all</a>
</c:if>     
          </td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b>Decline</b>&nbsp;</br>
<c:if test="${empty param.printFriendly}" >                     
           <a href="javascript:declineAll();" onclick="enableSubmit();" >Select all</a>
</c:if>           
          </td>         
       </c:if>
        <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
        <td><b>Alert/<br>Warning</b></td>                    
        <td></td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  
        </tr>
     

<% if(errors==false){      
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
<%TaskCenterTasksDetails theItem=(TaskCenterTasksDetails)pageContext.getAttribute("theItem"); %> 
<c:set var="indexValue" value="${theIndex.index}"/>
			<%String temp = pageContext.getAttribute("indexValue").toString(); 
			if (Integer.parseInt(temp)% 2 == 0) { %>
        <tr class="datacell2">
<% } else { %>
        <tr class="datacell1">
<% } %>
<c:if test="${empty param.printFriendly}" >  
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="center">
            <a href="/do/census/viewEmployeeSnapshot/?profileId=<%=theItem.getProfileId()%>&source=taskCenter">
              <img src="/assets/unmanaged/images/view_icon.gif" alt="View" border="0"/></a>
<% if (taskCenterTasksReportForm.isShowActionButtons()) { %>                        
            <a href="/do/census/editEmployeeSnapshot/?profileId=<%=theItem.getProfileId()%>&source=taskCenter">
              <img src="/assets/unmanaged/images/edit_icon.gif" alt="Edit" border="0"/></a>
<% } %>              
          </td>
</c:if>          
          <td class="datadivider" valign="top" width="1" height="1"></td>          
          <td valign="top" nowrap>
<c:if test="${empty param.printFriendly}" >            
<% if (theItem.isAccountHolder()) { %>          
            <a href="/do/participant/participantAccount/?profileId=<%=theItem.getProfileId()%>"><%=theItem.getName()%></a></br>
<% } else { %>
            <%=theItem.getName()%></br>
<% } %>
            <%=theItem.getSsn()%>
</c:if>          
<c:if test="${not empty param.printFriendly}" >            
            <%=theItem.getName()%></br>
            <%=theItem.getSsn()%>
</c:if>                      
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% if (showDivColumn) { %>          
          <td valign="top" nowrap><%=theItem.getDivision()%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% } %>          
          <td valign="top"><%=theItem.getType()%></td>
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
<td valign="top" nowrap>

   <c:choose>          
 <c:when test="${isPayrollFeedbackServiceEnabled}">
 <render:date property="theItem.effectiveDate" patternOut="MMM dd, yyyy" defaultValue=""/>
 <td class="datadivider" valign="top" width="1" height="1"></td>          
<td valign="top" colspan="5" nowrap>${theItem.details}</td>
</c:when>
<c:otherwise>
${theItem.anniversaryDate}
 <td class="datadivider" valign="top" width="1" height="1"></td>          
<td valign="top" nowrap>${theItem.details}</td> 
</c:otherwise>
</c:choose>
</td>
        
 <c:if test="${!isPayrollFeedbackServiceEnabled}">
      
          <td class="datadivider" valign="top" width="1" height="1"></td>              
          <td align="center">          
            <%
                 if (theItem.isFieldInError("approve")) {
                 	out.print(com.manulife.pension.ps.web.Constants.ERROR_ICON_NO_TITLE);
                 }
              %>
              	<c:set var="disabledAP" value="false"/>
              	<c:set var="checkedAPP" value=""/>
              <c:if test="${theItem.getStatusCode()=='AP' or  theItem.isApprove()}">
             	<c:set var="checkedAPP" value="checked"/>
               </c:if>
                <c:if test="${theItem.getStatusCode()!='PA'}">
              	<c:set var="disabledAP" value="true"/>
               </c:if>
               
                <form:checkbox 
               path="report.details[${theIndex.index}].approve" checked="${checkedAPP }"   disabled="${disabledAP}"    onmousedown="uncheckDecline('${theIndex.index}') , enableSubmit();"/>
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>          
          <td align="center" > 
              <% if (theItem.isFieldInError("decline")) {
                 	out.print(com.manulife.pension.ps.web.Constants.ERROR_ICON_NO_TITLE);
                 }
              %>
               	<c:set var="disabledDec" value="false"/>
               	<c:set var="checkedDec" value=""/>
              <c:if test="${theItem.getStatusCode()=='DC' or  theItem.isDecline()}">
             		<c:set var="checkedDec" value="checked"/>
               </c:if>
                <c:if test="${theItem.getStatusCode()!='PA'}">
              	<c:set var="disabledDec" value="true"/>
               </c:if>
               
               <form:checkbox 
               path="report.details[${theIndex.index}].decline"  checked ="${checkedDec}" disabled="${disabledDec}" 
               onmousedown="uncheckApprove('${theIndex.index}'), enableSubmit();" />
          </td>          
         
        </c:if>

           <td class="datadivider" valign="top" width="1" height="1"></td>     
             <% if (theItem.isADHocRequest() || "AP".equals(theItem.getStatusCode())) { %>
                 <input type="hidden" name="theItem[${theIndex.index}].genWarn"  value="false" />                           
              <% } else { %>
                 <input type="hidden" name="theItem[${theIndex.index}].genWarn"  value="<%=theItem.hasOldContribution()%>" />                                         
              <% }  %>    
          <% if (theItem.hasAlert() && theItem.hasWarning()) { %>
	          <td align="center" title="<%=taskCenterTasksReportForm.getAlertText(theItem)%>">
	              <IMG height=16 src="/assets/unmanaged/images/alert.gif" width=16 border=0> 
	              <IMG height=12 src="/assets/unmanaged/images/warning2.gif" width=12 border=0> 	              
	          </td>          
          <% } else if (theItem.hasAlert()) { %>
	          <td align="center" title="<%=taskCenterTasksReportForm.getAlertText(theItem)%>">
	              <IMG height=16 src="/assets/unmanaged/images/alert.gif" width=16 border=0> 
	          </td>          
          <% } else if (theItem.hasWarning()) { %>
              <td align="center" title="<%=taskCenterTasksReportForm.getWarningText(theItem)%>">               
	             <IMG height=12 src="/assets/unmanaged/images/warning2.gif" width=12 border=0> 
	          </td>          
          <% } else { %>
              <td></td>
          <% } %>
          
           <td></td>     
          <td class="databorder" valign="top" width="1" height="1"></td>          
          
          <!-- Track changes for the following fields if editable -->
          
                   
<% if ("PA".equals(theItem.getStatusCode())) { %> 
          <script type="text/javascript" >
              changeTracker.trackElement('report.details[${theIndex.index}].approve',false);
          </script>

           <script type="text/javascript" >
              changeTracker.trackElement('report.details[${theIndex.index}].decline',false);
          </script> 
<% } %> 
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
	      <td align="right"><report:pageCounter report="theReport" formName="taskCenterTasksReportForm" arrowColor="black"/></td>
<% } %>
     	</tr>
     	<c:if test="${!isPayrollFeedbackServiceEnabled}">      
	    <table width="730">
	      <tr><td>&nbsp;</td> </tr>
		  <tr>	           
		   	  <td width="60%">&nbsp;</td>       	        
	     	  <td width="20%" id="cancelButton" align="right" nowrap>
	     	      <% if (taskCenterTasksReportForm.isShowActionButtons()) { %>
	              <c:if test="${empty param.printFriendly }" >
	                <input name="cancelButton" type="submit" class="button134" value="CANCEL" 
	                 onclick="return doCancel();" ${disableButtons} />
	              </c:if>     	  
	              <% } %>
	     	  </td>
	     	  <td width="20%" id="saveButton" align="right" nowrap>
	     	      <% if (taskCenterTasksReportForm.isShowActionButtons()) { %>
	              <c:if test="${empty param.printFriendly }" >
	                <input name="saveButton" type="submit" class="button134" value="SAVE" 
	                 onclick="return doSave();" ${disableButtons} />
	              </c:if>     	  
	              <% } %>
	     	  </td>	     	  
		  </tr>
	    </table>
	    </c:if>      
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
