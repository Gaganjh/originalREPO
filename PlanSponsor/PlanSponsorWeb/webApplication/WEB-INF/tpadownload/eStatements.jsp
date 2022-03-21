<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsItem" %>
<%@ page import="com.manulife.pension.ps.web.tpadownload.EStatementsForm" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsReportData" %>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>

<% int style=2; %> 

<%
String alltext= EStatementsItem.STATEMENT_TYPE_ALL_TEXT;
String planAdmin=EStatementsItem.STATEMENT_TYPE_PLAN_ADMINISTRATOR;
String planAdmintext=EStatementsItem.STATEMENT_TYPE_PLAN_ADMINISTRATOR_TEXT;
String financial=EStatementsItem.STATEMENT_TYPE_EMPLOYER_FINANCIAL;
String financialtext=EStatementsItem.STATEMENT_TYPE_EMPLOYER_FINANCIAL_TEXT;
String schedulea=EStatementsItem.STATEMENT_TYPE_SCHEDULE_A;
String scheduleatext=EStatementsItem.STATEMENT_TYPE_SCHEDULE_A_TEXT;
String schedulec=EStatementsItem.STATEMENT_TYPE_SCHEDULE_C;
String schedulecText=EStatementsItem.STATEMENT_TYPE_SCHEDULE_C_TEXT;
String auditcertfct=EStatementsItem.STATEMENT_TYPE_AUDIT_CERTIFICATION;
String auditcertfcttext=EStatementsItem.STATEMENT_TYPE_AUDIT_CERTIFICATION_TEXT;
String auditsummary=EStatementsItem.STATEMENT_TYPE_AUDIT_SUMMARY;
String auditsummarytext=EStatementsItem.STATEMENT_TYPE_AUDIT_SUMMARY_TEXT;
String rstatus=EStatementsItem.REGULAR_STATUS;
String cstatus=EStatementsItem.CORRECTED_STATUS;

pageContext.setAttribute("alltext",alltext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("planAdmin",planAdmin,PageContext.PAGE_SCOPE);
pageContext.setAttribute("planAdmintext",planAdmintext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("financial",financial,PageContext.PAGE_SCOPE);
pageContext.setAttribute("financialtext",financialtext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("schedulea",schedulea,PageContext.PAGE_SCOPE);
pageContext.setAttribute("scheduleatext",scheduleatext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("schedulec",schedulec,PageContext.PAGE_SCOPE);
pageContext.setAttribute("schedulecText",schedulecText,PageContext.PAGE_SCOPE);
pageContext.setAttribute("auditcertfct",auditcertfct,PageContext.PAGE_SCOPE);
pageContext.setAttribute("auditcertfcttext",auditcertfcttext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("auditsummary",auditsummary,PageContext.PAGE_SCOPE);
pageContext.setAttribute("auditsummarytext",auditsummarytext,PageContext.PAGE_SCOPE);
pageContext.setAttribute("rstatus",rstatus,PageContext.PAGE_SCOPE);
pageContext.setAttribute("cstatus",cstatus,PageContext.PAGE_SCOPE);

%>

<%
EStatementsReportData theReport = (EStatementsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<%
	String maxlength=Constants.STR_CONTRACT_NUMBER_MAX_LENGTH;
	pageContext.setAttribute("maxlength",maxlength,PageContext.PAGE_SCOPE); 
%>
<jsp:useBean id="eStatementsForm" scope="request" type="com.manulife.pension.ps.web.tpadownload.EStatementsForm" />

<content:contentBean contentId="<%=ContentConstants.WARNING_CHECK_BOXES_SELECTED_BEFORE_SEARCH%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningSearch"/>
                          	


<%-- This jsp includes the following CMA content --%>
<script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/report.js"></script>
<script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>


<script type="text/javascript" >

	function doOnload() {
		boxClicked();
	}

	function showPDF(key) {
  	   var url = "/do/tpadownload/eStatements/?task=downloadFile&clientIdAndFileName=";
	   PDFWindow(url+key);
    }
    
	function sortSubmit(sortfield, sortDirection){	
	    if (document.forms['eStatementsForm']) {
			document.forms['eStatementsForm'].elements['task'].value = 'sort';
			document.forms['eStatementsForm'].elements['sortField'].value = sortfield;
			document.forms['eStatementsForm'].elements['sortDirection'].value = sortDirection;
	
			if (document.forms['eStatementsForm']) {
				document.forms['eStatementsForm'].submit();
			} else {
				document.forms.eStatementsForm.submit();
			}		
	    }
	}
    
    
	function boxClicked() {
		var count = countChecks();
		var checkBoxes = document.forms['eStatementsForm'].elements['selectedStatements'];
		if (count != checkBoxes.length-1) {
			document.forms['eStatementsForm'].elements['selectAll'].checked=false;
		} else if (count == checkBoxes.length-1) {
			document.forms['eStatementsForm'].elements['selectAll'].checked=true;
		}
	}	
	
	
    function countChecks() {
		var total=0;
         if (document.forms['eStatementsForm'].elements['selectedStatements']) {
           var checkBoxes = document.forms['eStatementsForm'].elements['selectedStatements'];

           if (checkBoxes.length) {
             for (i = 0; i < checkBoxes.length; i++) {
               if (checkBoxes[i].checked == true && checkBoxes[i].value != "N/A") {
                 total=total+1;
               }
             }
           }
         }
         return total;
     }	

    // This function is called if there is only one checkbox selected.
    // This functiion returns the selects statement's name
	function getStatementName() {
		if (document.forms['eStatementsForm'].elements['selectedStatements']) {
		    var checkBoxes = document.forms['eStatementsForm'].elements['selectedStatements'];

		    if (checkBoxes.length) {
			  for (i = 0; i < checkBoxes.length; i++) {
			    if (checkBoxes[i].checked == true && checkBoxes[i].value != 'N/A') {
				    return checkBoxes[i].value;
			    }
			  }
		    } else if (checkBoxes) {
			// Then we don't have an array of checkboxes, we only have one checkbox on the page.
			  if (checkBoxes.checked == true) {
			    return checkBoxes.value;
			  }
		    }
		}

		return count;
	}
    

	function doDownloadFiles() {
	    var count = countChecks();

		if (count == 1) {
		  showPDF(getStatementName());
		  return false;
	    }  else {
		  document.forms.eStatementsForm.task.value = 'downloadZipFile';
		  document.forms.eStatementsForm.submit();	  
		  return true;
	    }
	}
	
    function MM_popupMsg(msg) { //v1.0
      var yesno = window.confirm(msg);

      if (yesno==true) {
	     return true;
      } else {
         return false;
      }
    }	

	function doSearch() {
	  var count = countChecks();
	  if ( count > 0 )
	  {
	    if ( MM_popupMsg('<content:getAttribute beanName="warningSearch" attribute="text"/>') == false )	
		   return false;		
		else
		   setAllCheckBoxes(false);
	  }	
	  
	  document.forms.eStatementsForm.task.value = 'filter';
	  document.forms.eStatementsForm.submit();	  		
	  
	  return true;
	}

	function submitFilter() {
	   document.forms.eStatementsForm.myAction.value = 'filter';
	   document.forms.eStatementsForm.submit();
	}

	function toggleAllCheckBoxes(box) {
		if (box.checked) {
			setAllCheckBoxes(true);
		} else {
			setAllCheckBoxes(false);
		}
	}

	function setAllCheckBoxes(value) {
		if (document.forms['eStatementsForm'].elements['selectedStatements']) {
			var checkBoxes = document.forms['eStatementsForm'].elements['selectedStatements'];
			for (i = 0; i < checkBoxes.length; i++) {
			    checkBoxes[i].checked = value;
			}
		}
	}

	function clearContractName()
	{
		document.forms.eStatementsForm.filterContractName.value = "";
	}

	function clearContractNumber()
	{
		document.forms.eStatementsForm.filterContractNumber.value = "";
	}


</script>



<table border="0" cellspacing-"0" cellpadding="0">
 <tr>
  <td width="30"><img src="/assets/unmanaged/images/s.gif"  height="1"> </td>	
  <td>
    <c:if test="${empty param.printFriendly }" >            
      <a href="/do/tpadownload/tedCurrentFilesReport/">TPA e-download</a>
	</c:if>            
  </td>
 </tr> 
 <tr>
  <td width="30"><img src="/assets/unmanaged/images/s.gif"  height="1"> </td>	
  <td>
<%-- Start of report summary --%>
<p>
    <c:if test="${empty param.printFriendly }" >            
        <content:errors scope="session" />
    </c:if>                    
    <%	request.getSession(false).setAttribute(Environment.getInstance().getErrorKey(), new java.util.ArrayList(0)); %>
</p>
<BR>


<ps:form cssStyle="margin-bottom:0;" method="POST" action="/do/tpadownload/eStatements/" modelAttribute="eStatementsForm" name="eStatementsForm">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="sortField" value="${sortfield}"/>
<input type="hidden" name="sortDirection" value="${sortdirection}"/>
	<!-- The following line is added not overcome the problem whne all checkboxes deselected 
	     and sorting requested after -->
<input type="hidden" name="selectedStatements" value="N/A"/>
	<content:errors scope="session" />
      <table border="0" cellspacing="0" cellpadding="0">
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="20"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="20%" class="tableheadTD">
                  <p><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /><br></b>
                  <strong><br></strong></p>
                </td>
                <td width="21%" class="tableheadTD"></td>
                <td width="11%" class="tableheadTD">&nbsp;</td>
<td width="48%" valign="top" align="right" class="tableheadTDinfo"><font color="#FFFFFF">${theReport.totalCount} Statement(s)</font></td>
              </tr>
              <tr>
                <td class="tableheadTD"><strong>Contract name:</strong></td>
                <td class="tableheadTD">
                  <strong><strong>
                    <c:if test="${empty param.printFriendly }" >          
<form:input path="filterContractName" maxlength="50" onkeypress="clearContractNumber();" size="20" />
                    </c:if>         
                    <c:if test="${not empty param.printFriendly }" >          
${eStatementsForm.filterContractName}
                    </c:if>                              
                  </strong></strong>
                </td>
                <td class="tableheadTD"><strong>Period end:</strong></td>
                <td class="tableheadTDinfo"><span class="tableheadTD">
                  From:&nbsp;
		            <ps:select name="eStatementsForm" property="filterReportEndDateFrom">
		              <ps:dateOptions name="eStatementsForm" 
		                    property="reportDateFromList" 
                            patternOut="MMMM yyyy" />
                    </ps:select>
				 &nbsp;
                 To: &nbsp;
		            <ps:select name="eStatementsForm" property="filterReportEndDateTo">
		              <ps:dateOptions name="eStatementsForm" 
		                    property="reportDateToList" 
                            patternOut="MMMM yyyy" />
                    </ps:select>			        
				&nbsp;
                </span>
                </td>
              </tr>
              <tr>
                <td class="tableheadTD"><strong>Contract number:</strong></td>
                <td class="tableheadTD"><strong><span class="tableheadTDinfo"><strong>
                  <c:if test="${empty param.printFriendly }" >    
<form:input path="filterContractNumber" maxlength="${maxlength}" onkeypress="clearContractName();" size="8"/> <%-- name="eStatementsForm" --%>
                  </c:if>                              			      
                  <c:if test="${not empty param.printFriendly }" >          
${eStatementsForm.filterContractNumber}
                  </c:if>                              			      
                  </strong></span></strong>
                </td>
                <td class="tableheadTD"><strong><strong>Statement type:</strong></strong></td>
                <td class="tableheadTDinfo"><span class="tableheadTD"><strong>
                  <c:if test="${empty param.printFriendly }" >          
<form:select path="filterStatementType" disabled="false" >
				      <!-- need more consontration on this not tested -->
				      <form:option value="">${alltext}</form:option>                    				      
				      <form:option value="${planAdmin}">${planAdmintext}</form:option>                    				      
				      <form:option value="${financial}">${financialtext}</form:option>                    				      
				      <form:option value="${schedulea}">${scheduleatext}</form:option>                    				      
				      <form:option value="${schedulec}">${schedulecText}</form:option>
				      <form:option value="${auditcertfct}">${auditcertfcttext}</form:option>
				      <form:option value="${auditsummary}">${auditsummarytext}</form:option>
				       <!-- need more consontration on this not tested -->
</form:select>
                  </c:if>                              
                  <c:if test="${not empty param.printFriendly }" >          
<form:select path="filterStatementType" disabled="true" >
				       <!-- need more consontration on this not tested -->
				      <form:option value="">${alltext}</form:option>                    				      
				      <form:option value="${planAdmin}">${planAdmintext}</form:option>                    				      
				      <form:option value="${financial}">${financialtext}</form:option>                    				      
				      <form:option value="${schedulea}">${scheduleatext}</form:option>    
				      <form:option value="${schedulec}">${schedulecText}</form:option>
				      <form:option value="${auditcertfct}">${auditcertfcttext}</form:option>
				      <form:option value="${auditsummary}">${auditsummarytext}</form:option>   
				       <!-- need more consontration on this not tested -->          				      
</form:select>
			      </c:if>                              			      
                  </strong></span>
                </td>
              </tr>

              <tr>
                <td class="tableheadTD"><strong>Corrected?:</strong></td>
                <td class="tableheadTD">
                  <strong>
                  <c:if test="${empty param.printFriendly }" >          
<form:select path="filterCorrected" disabled="false" >
					   <!-- need more consontration on this not tested -->
					   <form:option value="">All</form:option>
					   <form:option value="${rstatus}">No</form:option>
					   <form:option value="${cstatus}">Yes</form:option>	
					    <!-- need more consontration on this not tested -->			
</form:select>
                  </c:if>                              			      
                  <c:if test="${not empty param.printFriendly }" >          
<form:select path="filterCorrected" disabled="true" >
					    <!-- need more consontration on this not tested -->
					   <form:option value="">All</form:option>
					   <form:option value="${rstatus}">No</form:option>
					   <form:option value="${cstatus}">Yes</form:option>	
					    <!-- need more consontration on this not tested -->			
</form:select>
                  </c:if>                              			      
                  </strong>
                </td>
                <td class="tableheadTD"><strong>Year end?:</strong></td>
                <td class="tableheadTD">
                  <c:if test="${empty param.printFriendly }" >          
<form:select path="filterIsYearEnd" disabled="false" >
					   <form:option value="">All</form:option>
					   <form:option value="N">No</form:option>
					   <form:option value="Y">Yes</form:option>				
</form:select>
                    &nbsp;
                    <span class="tableheadTDinfo">
	   			     <input type="submit" name="Submit" onClick="return doSearch();" value="search">
                    </span>
                  </c:if>
                  <c:if test="${not empty param.printFriendly }" >          
<form:select path="filterIsYearEnd" disabled="true" >
					   <form:option value="">All</form:option>
					   <form:option value="N">No</form:option>
					   <form:option value="Y">Yes</form:option>				
</form:select>
				  </c:if>   
                </td>
              </tr>
          </table></td>
        </tr>

        <tr class="tablesubhead">
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  	      <c:if test="${empty param.printFriendly }" >          
            <td align="left" valign="top"><input id="selectAll" type="checkbox" onClick="toggleAllCheckBoxes(this)"></td>            
          </c:if>

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>                
	        
		  <c:if test="${empty param.printFriendly }" >
             <td width="55"><report:sortLinkViaSubmit field="contractNumber" direction="asc" formName="eStatementsForm"><b>Contract number</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td colspan="2" width="55"><b>Contract number</b></td>
          </c:if>          
          
          
          
          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

		  <c:if test="${empty param.printFriendly }" >
             <td width="110"><report:sortLinkViaSubmit field="contractType" direction="asc" formName="eStatementsForm"><b>Contract type</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td colspan="2" width="110"><b>Contract type</b></td>
          </c:if>          
          
          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          
 
 
		  <c:if test="${empty param.printFriendly }" >
             <td width="200"><report:sortLinkViaSubmit field="contractName" direction="asc" formName="eStatementsForm"><b>Contract name</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
	        <td width="200"><b>Contract name</b></td>
          </c:if>           

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          
		  <c:if test="${empty param.printFriendly }" >
             <td width="117"><report:sortLinkViaSubmit field="statementType" direction="asc" formName="eStatementsForm"><b>Statement type</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td width="117"><b>Statement type</b></td>
          </c:if>                     

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          
		  <c:if test="${empty param.printFriendly }" >
             <td width="72"><report:sortLinkViaSubmit field="periodEndDate" direction="desc" formName="eStatementsForm"><b>Period end date</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td width="72"><b>Period end date</b></td>
          </c:if>                     

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

		<c:if test="${empty param.printFriendly }" >
             <td width="72"><report:sortLinkViaSubmit field="producedDate" direction="desc" formName="eStatementsForm"><b>Date produced</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td width="72"><b>Date produced</b></td>
          </c:if>                     

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

		  <c:if test="${empty param.printFriendly }" >
             <td width="67" class="tablesubhead"><report:sortLinkViaSubmit field="corrected" direction="desc" formName="eStatementsForm"><b>Corrected?</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td width="67" class="tablesubhead"><b>Corrected?</b></td>
          </c:if>                     

          <td width="1" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

		  <c:if test="${empty param.printFriendly }" >
             <td colspan="2"><report:sortLinkViaSubmit field="yearEnd" direction="desc" formName="eStatementsForm"><b>Year end?</b></report:sortLinkViaSubmit></td>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
            <td colspan="2"><b>Year end?</b></td>
          </c:if>                     

          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<c:if test="${theReport.totalCount ==0}">
	  <content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_STATEMENTS_AVAILABLE_FOR_THE_SEARCHED_CRITERIA%>"
                         	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="eStatementsNoResultMessage"/>

      <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td colspan="18" valign="top" class="datacell1">
       	  <content:getAttribute id="eStatementsNoResultMessage" attribute="text"/>                          	
        </td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      </tr>  
</c:if>


<c:if test="${not empty theReport.details}">
<c:set var="pageSize" value="<%=Environment.getInstance().getEStatementsPageSize()%>"/>
<c:forEach items="${theReport.getDetails()}" var="theItem" varStatus="theIndex" >
	
    <c:if test="${theIndex.index lt pageSize}">
		<% if(style==1) style=2;else style=1; %>        
		
        <tr class="datacell<%=style%>">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <c:if test="${empty param.printFriendly }" >                                      
            <td valign="top" class="datacell<%=style%>">
              <form:checkbox  path="selectedStatements"  onclick="boxClicked()" value="${theItem.clientIdAndStatementFileName}" >
					${theItem.clientIdAndStatementFileName}
   	  		  </form:checkbox>
            </td>
          </c:if>                               	  		              

          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly }" > 
<td valign="top" class="datacell<%=style%>">${theItem.contractNumber}</td>
          </c:if>  
          <c:if test="${not empty param.printFriendly }" > 
<td colspan="2" valign="top" class="datacell<%=style%>">${theItem.contractNumber}</td>
          </c:if>  
          

          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly }" > 
<td valign="top" class="datacell<%=style%>">${theItem.contractTypeText}</td>
          </c:if>  
          <c:if test="${not empty param.printFriendly }" > 
<td colspan="2" valign="top" class="datacell<%=style%>">${theItem.contractTypeText}</td>
          </c:if>  


          
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="top" class="datacell<%=style%>">${theItem.contractName}</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="datacell<%=style%>">
            <c:if test="${empty param.printFriendly }" >                            
<a href="javascript:showPDF('${theItem.clientIdAndStatementFileName}')">
${theItem.statementType}
              </a>
            </c:if>  
	        <c:if test="${not empty param.printFriendly }" >                            
${theItem.statementType}
            </c:if>                            	        
          </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><render:date property = "theItem.periodEndDate" defaultValue = "" /></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="datacell<%=style%>"><render:date property = "theItem.producedDate" defaultValue = "" /><br></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="top">${theItem.corrected}</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" valign="top" class="datacell<%=style%>">${theItem.yearEnd}</td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
    </c:if>        
</c:forEach>
</c:if>
        


        <tr>
          <td class="databorder" colspan="19"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <table width="100%"  border="0" cellspacing="1" cellpadding="1">
        <tr>
          <td>&nbsp;</td>

        </tr>
        <tr>
<c:if test="${not empty theReport.details}">
            <c:if test="${empty param.printFriendly }" >                                    
              <td><input name="Submit" type="submit" class="button100Lg" value="download" onClick="return doDownloadFiles();" /></td>
            </c:if>                                        
</c:if>
        </tr>
      </table>
      <br><br>
    </td>
<!--    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>-->
  </tr>




</table>
</ps:form>

  </td>
 </tr> 
</table> 


              
                            
                            
