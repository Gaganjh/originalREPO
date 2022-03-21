<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesItem" %>
<%@ page import="com.manulife.pension.ps.web.tpadownload.TedCurrentFilesForm" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesReportData" %>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>

<%-- This jsp includes the following CMA content --%>

<content:contentBean contentId="<%=ContentConstants.PS_TED_CURRENT_FILES_ALERT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Alert_Title"/>

<%-- Beans used --%>
<%
	
	TedCurrentFilesReportData theReport = (TedCurrentFilesReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%>  

<jsp:useBean id="tedCurrentFilesForm"  class="com.manulife.pension.ps.web.tpadownload.TedCurrentFilesForm" />

<% boolean showFootnote=false; %>

<style>
	.tableheadTD {padding:0px;}
	.tableheadTD1 {padding:0px;}
</style>



<script language="javascript">

    var agt=navigator.userAgent.toLowerCase();
    var is_mac = ( (agt.indexOf("mac")!=-1) || (agt.indexOf("68k")!=-1) || (agt.indexOf("ppc")!=-1) || (agt.indexOf("os x")!=-1));
    var is_ie  =( (agt.indexOf('msie')!=-1) || (agt.indexOf('explorer')!=-1) );


	function doOnload() {
		boxClicked();
	}


        function sortSubmit(sortfield, sortDirection){
            if (document.forms['tedCurrentFilesForm']) {
                document.forms['tedCurrentFilesForm'].elements['task'].value = 'sort';
                document.forms['tedCurrentFilesForm'].elements['myAction'].value = 'sort';
                document.forms['tedCurrentFilesForm'].elements['sortField'].value = sortfield;
                document.forms['tedCurrentFilesForm'].elements['sortDirection'].value = sortDirection;

                if (document.forms['tedCurrentFilesForm']) {
                        document.forms['tedCurrentFilesForm'].submit();
                } else {
                        document.forms.tedCurrentFilesForm.submit();
                }

            }
        }


        function doDownloadFiles() {
	   var numChecks = countChecks();
           if (numChecks > 0) {
		if (getConfirmation(numChecks)) {
                   document.forms.tedCurrentFilesForm.myAction.value = 'transferFiles';
                   document.forms.tedCurrentFilesForm.submit();
                   setTimeout('top.location.href="/do/tpadownload/tedCurrentFilesReport/?task=sort&sortDirection=<%=tedCurrentFilesForm.getSortDirection()%>&sortField=<%=tedCurrentFilesForm.getSortField()%>";',5000);
		}
           } else {
                document.forms.tedCurrentFilesForm.myAction.value = 'ERROR_NONE_CHECKED';
                document.forms.tedCurrentFilesForm.submit();
           }
        }

	function getConfirmation(numChecks) {
	<% if (Environment.getInstance().getConfirmTPAeDownloads()) { %>
		var msg;
		if (numChecks == 1) {
			msg = numChecks + ' contract has been selected for download.\nPress OK to continue.';
		} else {
			msg = numChecks + ' contracts have been selected for download.\nPress OK to continue.';
		}
                if (confirm(msg)) {
			return true;
		} else {
			return false;
		}
	<% } else { %>
		return true;
	<% } %>

	}

	function boxClicked() {
		var count = countChecks();
		var checkBoxes = document.forms['tedCurrentFilesForm'].elements['selectedIdentities'];
		if (count != checkBoxes.length-1) {
			document.forms['tedCurrentFilesForm'].elements['selectAll'].checked=false;
		} else if (count == checkBoxes.length-1) {
			document.forms['tedCurrentFilesForm'].elements['selectAll'].checked=true;
		}
	}

        function countChecks() {
		var total=0;
                if (document.forms['tedCurrentFilesForm'].elements['selectedIdentities']) {
                    var checkBoxes = document.forms['tedCurrentFilesForm'].elements['selectedIdentities'];

                    if (checkBoxes.length) {
                        for (i = 0; i < checkBoxes.length; i++) {
                            if (checkBoxes[i].checked == true && checkBoxes[i].value != 'N/A') {
                                    total=total+1;
                            }
                        }
                    }
                }
                return total;
        }

        var numb = '0123456789';

        function isValid(parm,val) {
          if (parm == "") return false;
          for (i=0; i<parm.length; i++) {
            if (val.indexOf(parm.charAt(i),0) == -1) return false;
          }
          return true;
        }

        function isNum(parm) {return isValid(parm,numb);}


        function doDownloadSingleFile(value) {
	    if (getConfirmation(1)) {
	            document.forms.tedCurrentFilesForm.myAction.value = 'transferFiles';
	            document.forms.tedCurrentFilesForm.singleFileIdentity.value = value;
	            document.forms.tedCurrentFilesForm.submit();
	            setTimeout('top.location.href="/do/tpadownload/tedCurrentFilesReport/";', 5000);
	    }
        }

        function submitFilter() {
           if (countChecks() > 0) {
                if (confirm('Submitting this search will clear your selected checkboxes.\nClick OK to Continue, or Cancel to return to the page.')) {
                      setAllCheckBoxes(false);
                      document.forms.tedCurrentFilesForm.myAction.value = 'filter';
                      document.forms.tedCurrentFilesForm.submit();
                }
           } else {
                   setAllCheckBoxes(false);
                   document.forms.tedCurrentFilesForm.myAction.value = 'filter';
                   document.forms.tedCurrentFilesForm.submit();
           }
        }

	function toggleAllCheckBoxes(box) {
		if (box.checked) {
			setAllCheckBoxes(true);
		} else {
			setAllCheckBoxes(false);
		}
	}

        function setAllCheckBoxes(value) {
                if (document.forms['tedCurrentFilesForm'].elements['selectedIdentities']) {
                        var checkBoxes = document.forms['tedCurrentFilesForm'].elements['selectedIdentities'];
                        if (checkBoxes.length) {
				for (i = 0; i < checkBoxes.length; i++) {
	                            checkBoxes[i].checked = value;
        	                }
			} else {
				checkBoxes.checked=value;
			}
                }
        }

function clearContractName(evt){
        //IE or browsers that use the getElementById model
        if (document.getElementById('filterContractName')) {
                if (document.getElementById('filterContractName').value) {
                        document.getElementById('filterContractName').value = "";
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
}

function clearContractNumber(evt){
        //IE or browsers that use the getElementById model
        if (document.getElementById('filterContractNumber')) {
                if (document.getElementById('filterContractNumber').value) {
                        document.getElementById('filterContractNumber').value = "";
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
}

</script>

<%-- Start of report summary --%>
<c:if test="${empty param.printFriendly}">
<p>
        <content:errors scope="session" />
</p>
</c:if>
<BR>
<table id="detailsTable" border="0" cellspacing="0" cellpadding="0" align="left">
<c:if test="${empty param.printFriendly}">
        <TR>
                <td colspan="20"><a href="/do/tpadownload/tedHistoryFilesReport/">View history files</a><BR><BR></td>
        </TR>
</c:if>
<ps:form modelAttribute="tedCurrentFilesForm" name="tedCurrentFilesForm" cssStyle="margin-bottom:0;" method="POST" action="/do/tpadownload/tedCurrentFilesReport/" >
<input type="hidden" name="myAction"/>
<input type="hidden" name="singleFileIdentity"/>
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="sortField" value="${tedCurrentFilesForm.getSortField()}" />
<input type="hidden" name="sortDirection" value="${tedCurrentFilesForm.getSortDirection()}"/>
<input type="hidden" name="selectedIdentities" value="N/A"/>

<!-- <input type="hidden" name="report"/> -->
<!-- <input type="hidden" name="filterContractName"/> -->
<!-- <input type="hidden" name="filterContractNumber"/> -->
<!-- <input type="hidden" name="filterDownloadStatus"/> -->
<!-- <input type="hidden" name="filterCorrected"/> -->
<!-- <input type="hidden" name="filterPeriodEndDate"/> -->
<!-- <input type="hidden" name="filterYearEnd"/> -->


    
    
<!-- top row -->
        <tr class="tablehead">
<c:if test="${empty param.printFriendly}">
             <td class="tableheadTD1" colspan="17" align="left"><img src="/assets/unmanaged/images/s.gif" width="7" height="18"><b>Current files</b></td>
</c:if>
<c:if test="${not empty param.printFriendly}">
             <td class="tableheadTD1" colspan="15" align="left"><img src="/assets/unmanaged/images/s.gif" width="7" height="18"><b>Current files</b></td>
</c:if>

             <td class="tableheadTD" colspan="3" align="right"><b>Files:
<c:if test="${not empty theReport.details}">
${theReport.totalNewAndDownloadedFiles}&nbsp;&nbsp;
</c:if>
<c:if test="${empty theReport.details}">0&nbsp;&nbsp;
</c:if>
                </b>
             </td>
        </tr>
<c:if test="${empty param.printFriendly}">
        <tr class="tablehead">
             <td class="tableheadTD" colspan="20">

                <table  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="7"></td>
		</tr>
                <tr>
                       <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Contract name:</td>
<td class="tableheadTD">
<form:input path="filterContractName" maxlength="50" onkeypress="clearContractNumber(event);" size="20" id="filterContractName" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td class="tableheadTD"><b>Corrected?:</td>
                        <td class="tableheadTD">
<form:select name="tedCurrentFilesForm" disabled="false" styleId="filterCorrected" path="filterCorrected">
                                <form:option value="">All</form:option>
                                <form:option value="N">No</form:option>
                                <form:option value="Y">Yes</form:option>
</form:select>
                        </td>
                        <td class="tableheadTD"></td>
                </tr>
                <tr>
                        <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Contract number:&nbsp;&nbsp;</td>
<td class="tableheadTD">
<form:input name="tedCurrentFilesForm" size="7" maxlength="<%=Constants.STR_CONTRACT_NUMBER_MAX_LENGTH%>" path="filterContractNumber"  id="filterContractNumber"
                         onkeypress="clearContractName(event);"/></td>
                        <td class="tableheadTD"><b>Download status:&nbsp;&nbsp;</td>
                        <td class="tableheadTD">
                        
<form:select name="tedCurrentFilesForm" disabled="false" path="filterDownloadStatus">
                                <form:option value="">All</form:option>
                                <form:option value="Y">New + Downloaded today</form:option>
                                <form:option value="D">Previously downloaded</form:option>
</form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                        </td>
                       
                </tr>
                <tr>
                        <!--<td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Period End date:&nbsp;&nbsp;</td>
                        <td class="tableheadTD">
                             <form:select name="tedCurrentFilesForm" disabled="false" path="filterPeriodEndDate">
                                 <form:option value="">All</form:option>
                             </form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                        </td>-->
                        <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Year End:&nbsp;&nbsp;</td>
 
                        <td class="tableheadTD">
<form:select name="tedCurrentFilesForm" disabled="false" path="filterYearEnd">                      
                                <form:option value="">All</form:option>
                                <form:option value="Y">Yes</form:option>
                                <form:option value="N">No</form:option>
</form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                       </td>
                       <td class="tableheadTD"><input name="Button" type="Button" value="search" onClick="submitFilter()"/>
                       <script language="javascript">
                        var onenter = new OnEnterSubmit('Button', 'search');
                        onenter.install();
                       </script>
                      </td>
                </tr>
                        
		<tr><td colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td></tr>
                </table>
             </td>
        </tr>
</c:if>
<c:if test="${not empty param.printFriendly}">
        <tr class="tablehead">
             <td class="tableheadTD" colspan="18">
                <table  border="0" cellspacing="0" cellpadding="0">
                <tr>
                        <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Contract name:</td>
<td class="tableheadTD"width="150">"${tedCurrentFilesForm.filterContractName}"&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td class="tableheadTD"><b>Corrected?:</td>
                        <td class="tableheadTD">
<form:select name="tedCurrentFilesForm" disabled="true" path="filterCorrected">
                                <form:option value="">All</form:option>
                                <form:option value="N">No</form:option>
                                <form:option value="Y">Yes</form:option>
</form:select>
                        </td>
                </tr>
                <tr>
                        <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Contract number:&nbsp;&nbsp;</td>
<td class="tableheadTD" width="150">${tedCurrentFilesForm.filterContractNumber}</td>
                        <td class="tableheadTD"><b>Download status:&nbsp;&nbsp;</td>
                        <td class="tableheadTD">
<form:select name="tedCurrentFilesForm" disabled="true" path="filterDownloadStatus">                        
                                <form:option value="">All</form:option>
                                <form:option value="Y">New + Downloaded today</form:option>
                                <form:option value="D">Previously downloaded</form:option>
</form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                        </td>
                </tr>
                <tr>
                <!--<td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7"><b>Period End date:&nbsp;&nbsp;</td>
                <td class="tableheadTD">
                     <form:select name="tedCurrentFilesForm" disabled="true" path="filterPeriodEndDate">
                         <form:option value="">All</form:option>
                     </form:select>&nbsp;&nbsp;&nbsp;&nbsp;
                </td>-->
                <td class="tableheadTD"><b>Year End:&nbsp;&nbsp;</td>
                <td class="tableheadTD">
<form:select name="tedCurrentFilesForm" disabled="true" path="filterYearEnd">                
                        <form:option value="">All</form:option>
                        <form:option value="Y">Yes</form:option>
                        <form:option value="N">No</form:option>
</form:select>&nbsp;&nbsp;&nbsp;&nbsp;
               </td>
               
                </tr>
        		<tr><td colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td></tr>
        		</table>
           </td>
        </tr>
</c:if>

        <tr class="tablesubhead">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty param.printFriendly }">

                <td align="left" valign="top"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top"><img src="/assets/unmanaged/images/s.gif" width="55" height="1"></td>
</c:if>
<c:if test="${not empty param.printFriendly }">
                <td align="left"  valign="top"><img src="/assets/unmanaged/images/s.gif" width="55" height="1"></td>
</c:if>

                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top"><img src="/assets/unmanaged/images/s.gif" width="200" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="75" height="1"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><img src="/assets/unmanaged/images/s.gif" width="55" height="1"></td>
                <td class="databox"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablesubhead">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty param.printFriendly }">
                <td align="left" valign="top"><input id="selectAll" name="selectAll" type="checkbox" onClick="toggleAllCheckBoxes(this)"></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
                <td align="left"  valign="top"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER%>" direction="asc" formName="tedCurrentFilesForm">Contract<BR>number</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME%>" direction="asc" formName="tedCurrentFilesForm">Contract name</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td> 
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_YEAR_END_IND%>" direction="desc" formName="tedCurrentFilesForm">Year<br>End?</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE%>" direction="desc" formName="tedCurrentFilesForm">Period end<br>date</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_LAST_RUN_DATE%>" direction="desc"  formName="tedCurrentFilesForm" >Date<br>produced</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_CORRECTED_IND%>" direction="asc"   formName="tedCurrentFilesForm">Corrected?</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS%>" direction="asc"   formName="tedCurrentFilesForm">Download<br>status</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedCurrentFilesItem.SORT_FIELD_LAST_DOWNLOAD_DATE%>" direction="desc"   formName="tedCurrentFilesForm">Download<br>date</report:sortLinkViaSubmit></b></td>
                <td class="databox"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

 <c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
      <c:set var="rowId" value="${theIndex.index}" />	
				<c:if test="${rowId%2==0}">
					<c:set var="rowClass" value="datacell1" />
					<c:set var="lastBorder" value="dataheaddivider" />
		</c:if>
				<c:if test="${rowId%2!=0}">
					<c:set var="rowClass" value="datacell2" />
					<c:set var="lastBorder" value="beigeborder" />
</c:if>
  <tr class="${rowClass}">
                 <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty param.printFriendly }">
                  <td valign="top" align="left">
<c:if test="${theItem.downloadStatusCode !='N'}">
                       <form:checkbox  path="selectedIdentities"  value="${theItem.identityString}" onclick="boxClicked()">
${theItem.identityString}
                            </form:checkbox >
</c:if>
                  </td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
                  <td height="20" valign="top">${theItem.getContractNumber()}</td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top">${theItem.getContractName()}</td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top">${theItem.getYearEnd()}</td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty param.printFriendly }">
<c:if test="${theItem.downloadStatusCode !='N'}">
                  <td valign="top">${theItem.getQuarterEndLink()}</td>
</c:if>
<c:if test="${theItem.downloadStatusCode =='N'}">
                  <td valign="top"><render:date dateStyle="m" property="theItem.quarterEndAsDate"/></td>
</c:if>
</c:if>
<c:if test="${not empty param.printFriendly }">
                  <td valign="top"><render:date dateStyle="m" property="theItem.quarterEndAsDate"/></td>
</c:if>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top"><render:date dateStyle="m" property="theItem.lastRunAsDate"/></td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top">${theItem.getCorrectedIndicator()}</td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top">${theItem.getDownloadStatus()}</td>
                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <% 
				 TedCurrentFilesItem theItem = (TedCurrentFilesItem)pageContext.getAttribute("theItem");
				if (theItem.showLastDownloadDate()) { %>
                          <td valign="top"><render:date dateStyle="m" property="theItem.lastDownloadAsDate"/></td>
                  <% } else { %>
                          <td align="center" valign="top">-</td>
                  <% } %>
                  <td class="${lastBorder}"  valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             </tr>

		<% if (theItem.getDownloadStatus().lastIndexOf("**") > 0) {
			showFootnote=true;
		   }
		%>				

</c:forEach>
</c:if> 


<c:if test="${empty theReport.details}">
        <tr>
            <td class="databorder"></td>
<c:if test="${empty param.printFriendly }">
            <td align="left" class="datacell1" colspan="17">There are no TED files available to display for the above criteria.</td>
</c:if>
<c:if test="${not empty param.printFriendly }">
            <td align="left" class="datacell1" colspan="15">There are no TED files available to display for the above criteria.</td>
</c:if>

            <td class="dataheaddivider" valign="top"></td>
            <td width="1" class="databorder"></td>
        </tr>
</c:if>



<c:if test="${not empty theReport.details}">
        <%
            String rowClass = (theReport.getDetails().size() % 2 == 1) ? "dataCell1" : "datacell2";
        %>

        <tr class="<%= rowClass %>">
            <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
<c:if test="${empty param.printFriendly }">
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
</c:if>
<c:if test="${empty theReport.details}">
        <tr class="dataCell1">
            <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
<c:if test="${empty param.printFriendly }">
            <td class="lastrow" colspan="17"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>

<c:if test="${not empty param.printFriendly }">
            <td class="lastrow" colspan="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>

            <td rowspan="2" colspan="2" width="5" class="lastrow" align="right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
</c:if>

        <tr>
<c:if test="${empty param.printFriendly }">
            <td class="databorder" colspan="18"></td>
</c:if>
<c:if test="${not empty param.printFriendly }">
            <td class="databorder" colspan="16"></td>
</c:if>
        </tr>

        <tr>





<c:if test="${empty param.printFriendly }">
            <td colspan="8" align="left">
</c:if>
<c:if test="${not empty param.printFriendly }">
            <td colspan="6" align="left">
</c:if>

<c:if test="${not empty theReport.details}">
   <c:if test="${empty param.printFriendly }">
		<BR>
                <input type="submit" class="button100Lg" value="Download" onClick="doDownloadFiles()"/><BR><BR>
   </c:if>
</c:if>
            </td>
            <td valign="top" colspan="10" align="right">
		<% if (showFootnote) { %>
			<BR>
                      	<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TED_DOWNLOADED_TODAY_FOOTNOTE%>"
                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                  id="downloadedTodayFootnote"/>
 				    <content:getAttribute beanName="downloadedTodayFootnote" attribute="text"/><br/>
		<% } %>
            </td>
        </tr>
        </ps:form>

</table>



<c:if test="${empty param.printFriendly }">
<SCRIPT>
   if (is_mac && is_ie) {
	document.write("<DIV id='alertBox' name='alertBox' style='position:absolute;top:-185px;left:370px'>");
   } else {
	document.write("<DIV id='alertBox' name='alertBox' style='position:absolute;top:180px;left:370px'>");
   }
</SCRIPT>
        <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="100%"><img src="/assets/unmanaged/images/s.gif" width="89%" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
                <td colspan="3" height="25"class="tableheadTD1"><b><%-- CMA managed--%>&nbsp;&nbsp;<content:getAttribute beanName="Alert_Title" attribute="title"/></b><br></td>
            </tr>
            <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class ="boxbody"><content:getAttribute beanName="Alert_Title" attribute="text">
                                        <content:param>/do/tpadownload/aboutTED/</content:param>
                                    </content:getAttribute>
                </td>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td colspan="3">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                        <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                        <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                        <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                </table>
                </td>
            </tr>
        </table>
</DIV>
</c:if>
