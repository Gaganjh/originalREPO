<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesItem" %>
<%@ page import="com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesReportData" %>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>

<content:contentBean contentId="<%=ContentConstants.PS_TED_CURRENT_FILES_ALERT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Alert_Title"/>

<script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/report.js"></script>
<script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<c:set var="printFriendly" value="${param.printFriendly}" />


<style>
	.tableheadTD {padding:0px;}
	.tableheadTD1 {padding:0px;}
</style>

<script language="javascript" type="text/javascript">

    var agt=navigator.userAgent.toLowerCase();
    var is_mac = ( (agt.indexOf("mac")!=-1) || (agt.indexOf("68k")!=-1) || (agt.indexOf("ppc")!=-1) || (agt.indexOf("os x")!=-1));
    var is_ie  =( (agt.indexOf('msie')!=-1) || (agt.indexOf('explorer')!=-1) );


	function doOnload() {
		boxClicked();
	}

        function sortSubmit(sortfield, sortDirection){
            if (document.forms['tedHistoryFilesForm']) {

                document.forms['tedHistoryFilesForm'].elements['task'].value = 'sort';
                document.forms['tedHistoryFilesForm'].elements['myAction'].value = 'sort';
                document.forms['tedHistoryFilesForm'].elements['sortField'].value = sortfield;
                document.forms['tedHistoryFilesForm'].elements['sortDirection'].value = sortDirection;

                if (document.forms['tedHistoryFilesForm']) {
                        document.forms['tedHistoryFilesForm'].submit();
                } else {
                        document.forms.tedHistoryFilesForm.submit();
                }

            }
        }

        function submitFilter() {
           if (countChecks() > 0) {
                if (confirm('Submitting this search will clear your selected checkboxes.\nClick OK to Continue, or Cancel to return to the page.')) {
                      setAllCheckBoxes(false);
                      document.forms.tedHistoryFilesForm.myAction.value = 'filter';
                      document.forms.tedHistoryFilesForm.submit();
                }
           } else {
                   setAllCheckBoxes(false);
                   document.forms.tedHistoryFilesForm.myAction.value = 'filter';
                   document.forms.tedHistoryFilesForm.submit();
           }
        }

        function doDownloadFiles() {
            if (countChecks() > 0) {
                document.forms.tedHistoryFilesForm.singleFileIdentity.value = "";
                document.forms.tedHistoryFilesForm.myAction.value = 'downloadFiles';
                document.forms.tedHistoryFilesForm.submit();
            } else {
                document.forms.tedHistoryFilesForm.myAction.value = 'ERROR_NONE_CHECKED';
                document.forms.tedHistoryFilesForm.submit();
            }

        }

        function doDownloadSingleFile(value) {
            document.forms.tedHistoryFilesForm.myAction.value = 'downloadFiles';
            document.forms.tedHistoryFilesForm.singleFileIdentity.value = value;
            document.forms.tedHistoryFilesForm.submit();
        }

        function toggleAllCheckBoxes(box) {
                if (box.checked) {
                        setAllCheckBoxes(true);
                } else {
                        setAllCheckBoxes(false);
                }
        }

        function setAllCheckBoxes(value) {
                if (document.forms['tedHistoryFilesForm'].elements['selectedFilenames']) {
                        var checkBoxes = document.forms['tedHistoryFilesForm'].elements['selectedFilenames'];
                        if (checkBoxes.length) {
                                for (i = 0; i < checkBoxes.length; i++) {
                                    checkBoxes[i].checked = value;
                                }
                        } else {
                                checkBoxes.checked=value;
                        }
                }
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


        function countChecks() {
		var total=0;
                if (document.forms['tedHistoryFilesForm'].elements['selectedFilenames']) {
                    var checkBoxes = document.forms['tedHistoryFilesForm'].elements['selectedFilenames'];

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

	function boxClicked() {
		var count = countChecks();
		var checkBoxes = document.forms['tedHistoryFilesForm'].elements['selectedFilenames'];
		if (count != checkBoxes.length-1) {
			document.forms['tedHistoryFilesForm'].elements['selectAll'].checked=false;
		} else if (count == checkBoxes.length-1) {
			document.forms['tedHistoryFilesForm'].elements['selectAll'].checked=true;
		}
	}

</script>


<%-- This jsp includes the following CMA content --%>

<%-- Start of report summary --%>
<c:if test="${empty printFriendly}" >
<p>
        <content:errors scope="session" />
</p>
</c:if>
<BR>
<table id="detailsTable" border="0" cellspacing="0" cellpadding="0">

        <%-- Beans used --%>
        <%TedHistoryFilesReportData tedHistoryFilesReportData = (TedHistoryFilesReportData)request.getAttribute(Constants.REPORT_BEAN);
        String returncodes=tedHistoryFilesReportData.getReturnCode();
        pageContext.setAttribute("returncodes",returncodes,PageContext.PAGE_SCOPE);
        pageContext.setAttribute("tedHistoryFilesReportData",tedHistoryFilesReportData,PageContext.PAGE_SCOPE);
%>
        <ps:form cssStyle="margin-bottom:0;" method="POST" action="/do/tpadownload/tedHistoryFilesReport/" modelAttribute="tedHistoryFilesForm" name ="tedHistoryFilesForm"> 
<input type="hidden" name="myAction"/>
<input type="hidden" name="singleFileIdentity"/>
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="sortField"/>
<input type="hidden" name="sortDirection"/>
<form:hidden path="selectedFilenames" value="N/A"/>
 
<!-- top row -->

        <tr>
        
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<c:if test="${empty printFriendly}" >
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
</c:if>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="211"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="400"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
<c:if test="${empty printFriendly}" >
        <tr>
             <td colspan="8"><a href="/do/tpadownload/tedCurrentFilesReport/">View current files</a><BR><BR></td>
        </tr>
</c:if>
        <tr class="tablehead">

<c:if test="${empty printFriendly}" >
             <td class="tableheadTD1" colspan="5" align="left"><img src="/assets/unmanaged/images/s.gif" width="7" height="18"><b>History files</b></td>
</c:if>
<c:if test="${not empty printFriendly}" >
             <td class="tableheadTD1" colspan="3" align="left"><img src="/assets/unmanaged/images/s.gif" width="7" height="18"><b>History files</b></td>
</c:if>

             <td class="tableheadTD" colspan="3" align="right"><b>Files:
<c:if test="${not empty tedHistoryFilesReportData.details}">
${tedHistoryFilesReportData.totalCount}&nbsp;&nbsp;
</c:if>
<c:if test="${empty tedHistoryFilesReportData.details}">0&nbsp;&nbsp;</c:if></b>
             </td>
        </tr>
	<tr class="tablehead">
	    <td class="tableheadTD"></td>
<c:if test="${empty printFriendly}" >
	    <td class="tableheadTD" colspan="7">
</c:if>
<c:if test="${not empty printFriendly}" >
	    <td class="tableheadTD" colspan="5">
</c:if>
	    	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
		    <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="7" height="7"></td>
		    <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="110" height="7"></td>
		    <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="65" height="7"></td>
		    <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="65" height="7"></td>
		    <td class="tableheadTD"><img src="/assets/unmanaged/images/s.gif" width="360" height="7"></td>
		</tr>
		<tr class="tableheadTD">
		    <td class="tableheadTD"></td>		
	            <td class="tableheadTD" valign="top">
			<c:if test="${empty printFriendly}" >
	                      	<ps:label fieldId="firstName" mandatory="false">Contract number:</ps:label>
			</c:if>
	            </td>
	            <td class="tableheadTD" valign="top">
			<c:if test="${empty printFriendly}" >
			<form:input path="contractNumber"  size="7" maxlength="7" value=${contractNumber}/>
			</c:if>
	            </td>
	            <td class="tableheadTD" valign="top">
			<c:if test="${empty printFriendly}" >
	                        <input name="Button" type="Button" onClick="submitFilter()" value="search"/>
	                         <script type="text/javascript" >
	                          var onenter = new OnEnterSubmit('Button', 'search');
	                          onenter.install();
	                         </script>
			</c:if>
	            </td>
		</tr>
<c:if test="${not empty tedHistoryFilesReportData.details}">
                 <tr>
                     <td valign="top" class="tableheadTD">&nbsp;</td>
                     <td valign="top" class="tableheadTD"><strong>Contract number: </strong></td>
                     <td valign="top" class="tableheadTD">&nbsp;<%=tedHistoryFilesReportData.getContractNumber()%></td>
                     <td valign="top" colspan="2" class="tableheadTD"><strong>&nbsp;Contract name:&nbsp;&nbsp;<%=tedHistoryFilesReportData.getContractName()%></td>
                     <td></td>
                 </tr>
</c:if>
		 <tr>
		    <td class="tableheadTD" colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="7"></td>
		 </tr>
                </table>
             </td>
        </tr>
        <tr class="tablesubhead">
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty printFriendly}" >
                <td align="left" valign="top">
                     <input type="checkbox" id="selectAll" name="selectAll" onClick="toggleAllCheckBoxes(this)">
                </td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedHistoryFilesItem.SORT_FIELD_QUARTER_END_DATE%>" direction="desc" formName="tedHistoryFilesForm">Period</report:sortLinkViaSubmit></b></td>
                <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="left"  valign="top" align="right"><b><report:sortLinkViaSubmit field ="<%=TedHistoryFilesItem.SORT_FIELD_LAST_RUN_DATE%>" direction="desc" formName="tedHistoryFilesForm">Date produced</report:sortLinkViaSubmit></b></td>
                <td class="databox"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		
<c:if test="${not empty tedHistoryFilesReportData.details}">
<c:forEach items="${tedHistoryFilesReportData.details}" var="theItem" varStatus="theIndex" >
	<c:set var="tempIndex" value="${theIndex.index}"/>
        <%Integer theIndex = (Integer)pageContext.getAttribute("tempIndex"); %>
		
        <% String cellClass;
           String lastBorder;

           if (theIndex.intValue() % 2 == 0) {
              cellClass="datacell1";
              lastBorder="dataheaddivider";
           } else {
              cellClass="datacell2";
              lastBorder="beigeborder";
           } %>

        <tr class="<%=cellClass%>">
                  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${empty printFriendly}" >
                  <td valign="top" align="left">
                        
                         <input type="checkbox"   name="selectedFilenames" onclick="boxClicked()" value="${theItem.fileName}"/>
                  </td>
                  <td height="20" class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>


<c:if test="${empty printFriendly}" >
                  <td valign="top">${theItem.getQuarterEndLink()}</td>
</c:if>
<c:if test="${not empty printFriendly}" >
                  <td valign="top">${theItem.getQuarterText()}</td>
</c:if>

                  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  <td valign="top"><render:date dateStyle="m" property="theItem.lastRunDate"/></td>
                  <td class="<%=lastBorder%>" valign="top"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                  <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             </tr>
</c:forEach>
</c:if>



<c:if test="${empty printFriendly}" >
<c:if test="${empty tedHistoryFilesReportData.details}">

<td class="databorder"></td>
        <tr>
            <td class="databorder"></td>
        <c:if test="${returncodes=='NA'}" >
            <td align="left" class="datacell1" colspan="5">There are no TED files available to display for the above criteria.</td>
        </c:if>
       <c:if test="${returncodes=='IN'}">
            <td align="left" class="datacell1" colspan="5">There are no TED files available to display for the above criteria.</td>
        </c:if>
        <c:if test="${returncodes=='NCN'}">
            <td align="left" class="datacell1" colspan="5">You must provide a contract number in order to perform a history file search.</td>
        </c:if>
        <c:if test="${returncodes=='NHF'}">
            <td align="left" class="datacell1" colspan="5">There are no TED files available to display for the above criteria.</td>
        </c:if>
        <c:if test="${returncodes=='NOFTP'}">
            <td align="left" class="datacell1" colspan="5"><br/></td>
        </c:if>

            <td class="dataheaddivider" valign="top"></td>
            <td width="1" class="databorder"></td>
        </tr>
   </c:if>
</c:if>


<c:if test="${not empty printFriendly}" >
<c:if test="${empty tedHistoryFilesReportData.details}">

        <tr>
            <td class="databorder"></td>
          
        <c:if test="${returncodes=='NA'}">
            <td align="left" class="datacell1" colspan="3">There are no TED files available to display for the above criteria.</td>
        </c:if>
        <c:if test="${returncodes=='IN'}">
            <td align="left" class="datacell1" colspan="3">There are no TED files available to display for the above criteria.</td>
        </c:if>
        <c:if test="${returncodes=='NCN'}">
            <td align="left" class="datacell1" colspan="3">You must provide a contract number in order to perform a history file search.</td>
        </c:if>
        <c:if test="${returncodes=='NHF'}">
            <td align="left" class="datacell1" colspan="3">There are no TED files available to display for the above criteria.</td>
        </c:if>
       <c:if test="${returncodes=='NOFTP'}">
            <td align="left" class="datacell1" colspan="3"><BR></td>
        </c:if>
            <td class="dataheaddivider" valign="top"></td>
            <td width="1" class="databorder"></td>
        </tr>
   </c:if>
</c:if>




<c:if test="${not empty tedHistoryFilesReportData.details}">
        <%
            String rowClass = (tedHistoryFilesReportData.getDetails().size() % 2 == 1) ? "dataCell1" : "datacell2";
        %>

        <tr class="<%= rowClass %>">
            <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
<c:if test="${empty printFriendly}" >
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
            <td align="right" rowspan="2" colspan="2" width="5" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
        <tr>
<c:if test="${empty printFriendly}" >
            <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:if>
<c:if test="${not empty printFriendly}" >
            <td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:if>

        </tr>

    <c:if test="${empty printFriendly}" >
        <tr>
            <td colspan="8" align="left"><BR>
                <input type="button" class="button100Lg" value="Download" onClick="doDownloadFiles()"/><BR><BR>
            </td>
        </tr>
    </c:if>

</c:if>
<c:if test="${empty tedHistoryFilesReportData.details}">
        <tr class="dataCell1">
            <td width="1" class="databorder"></td>
<c:if test="${empty printFriendly}" >
            <td class="lastrow" colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
</c:if>
<c:if test="${not empty printFriendly}" >
            <td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
</c:if>
            <td align="right" valign="top" rowspan="2" colspan="2" width="5" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" ></td>
        </tr>
        <tr>
<c:if test="${empty printFriendly}" >
            <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:if>
<c:if test="${not empty printFriendly}" >
            <td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:if>
        </tr>
</c:if>
</ps:form>
</table>

<c:if test="${empty printFriendly}" >
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
                <td colspan="3" height="25" class="tableheadTD1"><b><%-- CMA managed--%>&nbsp;&nbsp;<content:getAttribute beanName="Alert_Title" attribute="title"/></b></td>
            </tr>
            <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class ="boxbody"><content:getAttribute beanName="Alert_Title" attribute="text">
                                        <content:param>/do/tpadownload/aboutTED/?from=history</content:param>
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
