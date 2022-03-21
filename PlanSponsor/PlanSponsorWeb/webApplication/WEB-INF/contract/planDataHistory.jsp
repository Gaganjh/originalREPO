<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.PlanDataHistoryReportData" %>

<jsp:useBean id="today" class="java.util.Date" />
<content:contentBean contentId="58549" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="ERR_DATE_FORMAT" />

<%
if(request.getAttribute(Constants.REPORT_BEAN) != null){
	
	PlanDataHistoryReportData theReport = (PlanDataHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}
%>

<SCRIPT language="javascript">
<!--
   var ERR_DATE_FORMAT_from = "<content:getAttribute id='ERR_DATE_FORMAT' attribute='text' filter='true'><content:param>From</content:param></content:getAttribute>";
   var ERR_DATE_FORMAT_to = "<content:getAttribute id='ERR_DATE_FORMAT' attribute='text' filter='true'><content:param>To</content:param></content:getAttribute>";
//-->
</SCRIPT>

<script language="JavaScript">
  function doCalendar(fieldName) {
	cal = new calendar(document.forms['planDataHistoryForm'].elements[fieldName]);
  	cal.year_scoll = true;
  	cal.time_comp = false;
  	cal.popup();
  }

  function onClickReset() {
	document.forms['planDataHistoryForm'].task.value = "default";
	return true;
  }

  function onClickOK() {
    document.location.href = "<c:url value='/do/contract/planData/view/'/>";

  }
</script>

<%boolean allDisabled = true;%>
<c:if test="${empty param.printFriendly}">
<%allDisabled = false;%>
</c:if>
<content:errors scope="session"/>
<ps:form method="POST" action="/do/contract/planDataHistory/" modelAttribute="planDataHistoryForm" name="planDataHistoryForm">
<input type="hidden" name="pdh" property="planId" />
<INPUT type="hidden" value="filter" name="task">

<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
  <TBODY>
  <TR>
    <TD width=30><IMG height=1 src="/assets/unmanaged/images/s.gif" width=30></TD>
    <TD>
      <TABLE width="700" border="0" cellpadding="0" cellspacing="0">
        <TBODY>
        <TR>
          <TD>
            <jsp:include flush="true" page="planDataErrors.jsp"/>
          </TD>
        </TR>
        </TBODY>
      </TABLE>
      <TABLE width="700" border="0" cellpadding="0" cellspacing="0">
        <TBODY>
          <TR>
            <TD colspan="2" valign="middle" bgcolor="#CCCCCC">&nbsp;&nbsp;<b>Plan information history search</b></TD>
            <TD width="181" valign="middle" bgcolor="#CCCCCC"><b> </b><img src="/assets/unmanaged/images/s.gif" width=1 height=25></TD>
            <TD width="72" valign="middle" bgcolor="#CCCCCC">&nbsp;</TD>
          </TR>
          <TR>
            <TD width="80" valign="top" class="filterSearch">
              <b>User ID</b><br>
<form:input path="userName" disabled="<%=allDisabled%>" maxlength="15" size="15" cssClass="inputField"/>
            <TD width="290" valign="top" class="filterSearch">
			<b>Field name </b><br>
              <form:select path="fieldName" disabled="<%=allDisabled%>">
                <form:options items="${planDataHistoryForm.fieldNames}" itemValue="value" itemLabel="label"/>
</form:select>
            </TD>
            <TD colspan="2" valign="top" class="filterSearch">
              <b>Plan history </b><br>
              from <ps:fieldHilight name="fromDate"/> 
<form:input path="fromDate" disabled="<%=allDisabled%>" maxlength="10" onblur="validateFromDate(this)" size="10" cssClass="inputField"/>
                   <c:if test="${empty param.printFriendly}">
                   <a href="javascript:doCalendar('fromDate', 0)"><img src="/assets/unmanaged/images/cal.gif" border="0"></a>
                   </c:if>
                   
to <form:input path="toDate" disabled="<%=allDisabled%>" maxlength="10" onblur="validateToDate(this)" size="10" cssClass="inputField"/>
                 <c:if test="${empty param.printFriendly}">
                 <a href="javascript:doCalendar('toDate', 0)"><img src="/assets/unmanaged/images/cal.gif" border="0"></a>
                 </c:if>
                 <ps:fieldHilight name="toDate"/><br>
              <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)</span>
            </TD>
          </TR>
          <c:if test="${empty param.printFriendly}">
          <TR>
            <TD colspan="4" valign="bottom" class="filterSearch"><div align="right">
              <input name="resetButton" type="submit" value="reset" onClick="return onClickReset();">
              <input name="submitButton" type="submit" value="search"/>
            </div></TD>
          </TR>
          </c:if>
        </TBODY>
      </TABLE>
      <P></P>
      <TABLE cellSpacing=0 cellPadding=0 width=700 border=0>
        <TBODY>
        <TR>
          <TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="12"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=12></TD>
          <TD width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="115"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=115></TD>
          <TD width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="75"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=75></TD>
          <TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="317"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=317></TD>
          <TD width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="175"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=175></TD>
          <TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
        </TR>
        <TR class=tablehead>
          <TD colSpan="8" class=tableheadTD1 align="left">
            <b>Plan information history</b> as of <fmt:formatDate value="${today}" pattern="MMMM dd, yyyy"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <b><report:recordCounter report="theReport" label="Records"/></b>
          </TD>
          <TD colspan="3" class=tableheadTD align="right">
            <report:pageCounter formName="planDataHistoryForm" report="theReport"/>
          </TD>
        </TR>
        <TR class=tablesubhead>
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="12" vAlign=bottom><b>#</b></TD>
          <TD class=dataheaddivider vAlign=bottom width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="130" vAlign=bottom><report:sort formName="planDataHistoryForm" field="createdTs" direction="asc"><b>Date and time</b></report:sort></TD>
          <TD class=dataheaddivider vAlign=bottom width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="70" vAlign=bottom><report:sort formName="planDataHistoryForm"  field="userId" direction="asc"><b>User ID</b></report:sort></TD>
          <TD class=dataheaddivider vAlign=bottom width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="290" vAlign=bottom><report:sort formName="planDataHistoryForm"  field="fieldName" direction="asc"><b>Field name</b></report:sort></TD>
          <TD class=dataheaddivider vAlign=bottom width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="198" align=left vAlign=bottom><b>Value</b></TD>
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
        </TR>
    		<% if (((PlanDataHistoryReportData)pageContext.getAttribute("theReport")).getDetails() == null || ((PlanDataHistoryReportData)pageContext.getAttribute("theReport")).getDetails().size() == 0) { // we have no results %>
        <TR class="datacell1">
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
    		  <TD valign="top" colspan="11">
    				Your search criteria produced no results. Please change your search criteria and try again.
    		  </TD>
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
        </TR>
    		<% } else {%>
<c:forEach items="${theReport.details}" var="historyItem" varStatus="historyItemCount" >
<c:set var ="count" value ="${historyItemCount.index}"/>
        <% int index = Integer.parseInt(pageContext.getAttribute("count").toString()); 
        if (index % 2 != 0) {%>
        <TR class="datacell1">
        <%} else {%>
        <TR class="datacell2">
        <%}%>
        <% 
          int lineNumber = index + ((PlanDataHistoryReportData)pageContext.getAttribute("theReport")).getStartIndex();
        %>
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="12"><%=lineNumber%></TD>
          <TD class=datadivider width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
          <TD width="130"><fmt:formatDate value="${historyItem.createdTs}" pattern="MM/dd/yyyy HH:mm:ss"/></TD>
          <TD class=datadivider width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="70">${historyItem.userId}</TD>
          <TD class=datadivider width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="290">${historyItem.fieldName}</TD>
          <TD class=datadivider width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD width="198">${historyItem.newValue}</TD>
          <TD class=databorder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
        </TR>
</c:forEach>
        <% }%>
        <TR>
          <TD class=databorder colSpan=11><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
        </TR>
        <TR>
          <TD align=right colSpan=11><report:pageCounter formName="planDataHistoryForm" report="theReport" arrowColor="black"/></TD>
        </TR>
        <TR>
          <TD colSpan=11><BR>
            <c:if test="${empty param.printFriendly}">
            <table width="699" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td align="left">
                  <input name="ok" type="button" class="button175"  onClick="onClickOK();" value="return to plan information">
                </td>
              </tr>
            </table>
            </c:if>
            <P class=footnote><BR>If you have any questions regarding the information shown here please refer to the 'Getting help' section or contact your John Hancock USA Client Account Representative.<BR></P>
            <P class=disclaimer>&nbsp;</P>
          </TD>
        </TR>
        </TBODY>
      </TABLE>
    </TD>
    <TD width=15>
      <IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
    </TD>
  </TR>
  </TBODY>
</TABLE>
</ps:form>

<c:if test="${not empty param.printFriendly}">
  <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>

  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
    </tr>
  </table>
</c:if>
