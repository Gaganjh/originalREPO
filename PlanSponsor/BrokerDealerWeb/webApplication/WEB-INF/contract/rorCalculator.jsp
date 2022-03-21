<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ page import="com.manulife.pension.bd.web.bob.investment.RateOfReturnCalculatorForm"%>
<%@page import="java.util.Date"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>

<content:contentBean
    contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>"
type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
		
<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>
<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

	
	<script type="text/javascript">

var isPageSubmitted = false;

window.onload = function() {
	if(document.getElementById("radio99").checked != true)
		disableDateFields();
}

function doBack() {
    document.rateOfReturnCalculatorForm.action.value = 'back';
    submitForm();
}

function doReset() {
    document.rateOfReturnCalculatorForm.action.value = 'reset';
    submitForm();
}

function doCalculate() {
    document.rateOfReturnCalculatorForm.action.value = 'calculate';
    submitForm();
}

function submitForm() {
    if (isPageSubmitted) {
        window.status = "Transaction already in progress.  Please wait.";
    } else {
        isPageSubmitted = true;
        document.rateOfReturnCalculatorForm.submit();
    }
}
    
function clearDateFields() {
    var frm = document.rateOfReturnCalculatorForm;
    document.getElementById("regFromDate").value="";
    document.getElementById("regToDate").value="";
	disableDateFields();
}

function enableDateFields() {
	var inputFromDate = document.getElementById("regFromDate");
	var inputToDate = document.getElementById("regToDate");
	inputFromDate.disabled=false;
	inputFromDate.style.backgroundColor = "rgb(235,235,228)";
    inputToDate.disabled=false;
	inputToDate.style.backgroundColor = "rgb(235,235,228)";
	enableCalendarPicker();
}

function disableDateFields() {
	var inputFromDate = document.getElementById("regFromDate");
	var inputToDate = document.getElementById("regToDate");
	inputFromDate.disabled=true;
	inputFromDate.style.backgroundColor = "rgb(200,200,200)";
    inputToDate.disabled=true;
	inputToDate.style.backgroundColor = "rgb(200,200,200)";
	disableCalendarPicker();
}


function enableCalendarPicker() {
	document.getElementById("calendarpicker1").firstChild.firstChild.disabled = false;
	document.getElementById("calendarpicker").firstChild.firstChild.disabled = false;
}

function disableCalendarPicker() {
	document.getElementById("calendarpicker1").firstChild.firstChild.disabled = true;
	document.getElementById("calendarpicker").firstChild.firstChild.disabled = true;
}

function checkDateFieldRadio() {
	document.getElementById("radio99").checked = true;
}

function doPrintPDF() {
	var reportURL = new URL("/do/bob/contract/RORCalculator/?action=printPDFReport");
	reportURL.setParameter("action", "printPDFReport");
   	window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

</script>
<!--start of global section-->
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>


<jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />

<navigation:contractReportsTab />

<bd:form action="/do/bob/contract/RORCalculator/" method="POST" modelAttribute="rateOfReturnCalculatorForm" name="rateOfReturnCalculatorForm">
<input type="hidden" name="action"/>
<div class="page_section_subheader controls">
      <%-- date filter and table heading section --%>
<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> <span style="font-size: 13px;">${rateOfReturnCalculatorForm.participantLastName} , ${rateOfReturnCalculatorForm.participantFirstName}</span></h3>
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==false}">
			 <a href="javascript://" class="pdf_icon" style="margin-right : 5px; cursor: default;" title="Please calculate the Rate of Return first and then click here to <content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
</c:if>
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==true}">
			 <a href="javascript://" onClick="doPrintPDF()" class="pdf_icon" style="margin-right : 5px;" title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
</c:if>
   </div>
<report:formatMessages scope="session" />


 	  
	  
	  

<TABLE border="0" cellSpacing="0" cellPadding="0" style="table-layout:fixed; width: 100%;">
				                    <TBODY style="background-color: rgba(226, 228, 229, 1);">
<TR>
				                            <TD width="1">&nbsp;</TD>
				                            <TD width="290">&nbsp;</TD>
				                            <TD width="1" style="background-color: white; padding: 1px;">&nbsp;</TD>
				                            <TD width="275">&nbsp;</TD>
				                            <TD width="4">&nbsp;</TD>
				                            <TD width="1">&nbsp;</TD>
				                        </TR>

<TR class="tablesubhead">
				                            <TD class="databorder" width="1">&nbsp;</TD>
				                            <TD vAlign="bottom" align="left" style="font: normal 16px georgia, times, serif; color: rgb(55, 56, 51);"><B>Choose a time period beginning from:</B></TD>
				                            <TD class="datadivider" vAlign="bottom" width="1" style="background-color: white; padding: 1px;"><B>&nbsp;</B></TD>
				                            <TD vAlign="bottom" colSpan="2" style="font: normal 16px georgia, times, serif; color: rgb(55, 56, 51);"><B>Rate of Return</B></TD>
				                            <TD class="databorder">&nbsp;</TD>
</TR>

<TR>
<TD class="databorder">&nbsp;</TD>
<TD class="datacell1" vAlign=top>
<TABLE border="0" cellSpacing="0" cellPadding="0" style="font: normal 16px verdana, arial, helvetica, sans-serif;" >
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" id="radio1" value="1" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">1 month ago</td>
				                                    </tr>
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" id="radio3" value="3" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">3 months ago</td>
				                                    </tr>
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" id="radio6" value="6" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">6 months ago</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" id="radio12" value="12" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">12 months ago</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" id="radio0" value="0" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">Year to date</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="enableDateFields();" path="timePeriodFromToday" id="radio99" value="99" /></td>
				                                        <td width="90%" style="color: rgb(55, 56, 51); font-size: 12px;">Or enter a specific time period</td>
				                                    </tr>
				                                </table>

<table border="0" style="font: normal 16px verdana, arial, helvetica, sans-serif;" onload="alert('Hello');">
				                                    <tr>
				                                        <td width="auto" style="color: rgb(55, 56, 51); font-size: 12px;">Start date:</br>
				                                        
														<div style="float: left; width: 90px">
<form:input path="startDate" maxlength="10" onfocus="checkDateFieldRadio()" size="8" id="regFromDate" />


														</div>
														<div style="float: left">
															<utils:btnCalendar dateField="regFromDate"
																	calendarcontainer="calendarcontainer1" datefields="datefields1"
																	calendarpicker="calendarpicker1" />
														</div>
				                                        </td>
														<td width="auto" style="color: rgb(55, 56, 51); font-size: 12px;">End date:</br>
														<div style="float: left; width: 90px">  
<form:input path="endDate" maxlength="10" onfocus="checkDateFieldRadio()" size="8" id="regToDate" />


									</div>
									<div style="float: left">
									<utils:btnCalendar dateField="regToDate"
									calendarcontainer="calendarcontainer" datefields="datefields"
									calendarpicker="calendarpicker" />
									</div>                 
				                                        			
				                                        </td>
				                                    </tr>                                    
				                                                                   
				                                </table>
</TD>
<TD class="datadivider" style="background-color: white; padding: 1px;">&nbsp;</TD>
				                            <TD class="datacell1" colSpan="2" vAlign="top" style="padding: 4px; background-color: rgba(226, 228, 229, 1); color: rgb(55, 56, 51); font-size: 12px;">
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==true}">
Rate of Return for the period beginning ${rateOfReturnCalculatorForm.resultStartDate} and
ending ${rateOfReturnCalculatorForm.resultEndDate} is
<b class="highlight">${rateOfReturnCalculatorForm.rateOfReturn}</b>%
</c:if>
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==false}">
				                                	Please select the time period and click calculate button to determine the contract's rate of return
</c:if>
				                            </TD>
				                            <TD class="databorder">&nbsp;</TD>
</TR>

<TR>
				                            <TD class="databorder" height="4" width="1"></TD>
				                            <TD class="whiteborder" width="1">&nbsp;</TD>
				                            <TD class="datadivider" width="1" style="background-color: white; padding: 1px;">&nbsp;</TD>
				                            <TD class="whiteborder" width="1">&nbsp;</TD>
				                            <TD rowspan="2" colspan="2"></TD>
				                        </TR>
				                        <TR valign="top">
				                             <TD class="databorder" colSpan="2" valign="top"></TD>
											<TD class="whiteborder" width="1" style="background-color: white; padding: 1px;">&nbsp;</TD>
											<TD class="databorder" colSpan="2" valign="top"></TD>
				                        </TR>
										<TR>
				                            <TD class="databorder" height="4" width="1"></TD>
				                            <TD class="whiteborder" width="1">&nbsp;</TD>
				                            <TD class="datadivider" width="1" style="background-color: white; padding: 1px;">&nbsp;</TD>
				                            <TD class="whiteborder" width="1">&nbsp;</TD>
				                            <TD rowspan="2" colspan="2"></TD>
				                        </TR>
				                        <TR valign="top">
				                            <TD class="databorder" colSpan="2" valign="top"></TD>
											<TD class="whiteborder" width="1" style="background-color: white; padding: 1px;">&nbsp;</TD>
											<TD class="databorder" colSpan="2" valign="top"></TD>
				                        </TR>
				                    </TBODY>
									</TABLE>
									
				                <TABLE border="0" cellSpacing="0" cellPadding="0">
				                    <TBODY>
				                        <TR>
				                            
				                            <TD>
				                                <DIV align="left" class="button_regular">
													<a onclick="return doReset();" style="width: 39px;font-size: 11px; height: 13px;">Clear</a>
				                                </DIV>
				                            </TD>
				                            <TD width="136">
				                                <DIV align="right" class="button_regular">
													<a onclick="return doCalculate();" style="width: 55px;font-size: 11px; height: 13px;">Calculate</a>
				                                </DIV>
				                            </TD>
				                        </TR>
				                    </TBODY>
				                </TABLE>

							
			
<!--page footer section-->
<div class="footnotes">
    
  
      <dl>
         <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
      </dl>
   
 	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>	


</bd:form>	
