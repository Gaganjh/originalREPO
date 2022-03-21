<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@ page import="com.manulife.pension.platform.web.fap.FapForm"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>

<mrtl:noCaching/>

<input type="hidden" name="isAdditionalparamsSectionEnabled" id="isAdditionalparamsEnabled" value="${fapForm.isAdditionalparamsSectionEnabled}"/>

<div class="dialog_title">&nbsp;&nbsp;<b>Select the Parameters:</b></div>
<br />
<div class="dialog_content">
<c:if test="${fapForm.isIsfEnabled}">
<form:checkbox path="fapForm.isfSelected" /> Include a Contract investment administration form?
                <br /><br />
</c:if> 
<c:if test="${fapForm.isClientNameEnabled}">
Prepared For: <form:input path="fapForm.clientName" maxlength="30" />
<br /><br />
</c:if>
<c:if test="${fapForm.advisorNameDisplayed == false }">
<input type="hidden" name="advisorName" value="${fapForm.advisorName}"/>
</c:if>  
<c:if test="${fapForm.advisorNameDisplayed}">
Enter Advisor Name: <form:input path="fapForm.advisorName" maxlength="30" />
<br /><br />
</c:if> 
<input type="hidden" name="advisorNameDisplayed" value="${fapForm.advisorNameDisplayed}"/>
<table>
    <tr>
        <td>
        <div class="button_login">
            <input style="font-size: 11px;width:100px;" id="triggerIreport" type="button" value="Go" onclick="doViewReports()"/></div>
        <div class="button_login">
            <input style="width: 100px; font-size: 11px;" id="cancelIreport" type="button" value="Cancel" onclick="doCancelIreport()"/></div>
        </td>
    </tr>
</table>
</div>
