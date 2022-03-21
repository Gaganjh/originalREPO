<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<content:contentBean contentId="<%=ContentConstants.SUMMARY_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="sumaryTabContent" />
  
<title>Summary</title>
</head>
<body>
<!-- TODO:SAVE BUTTON -->
<div id="summaryTabDivId" class="borderedDataBox">
	<% TabPlanDataForm tabPlanDataForm = (TabPlanDataForm) session.getAttribute(Constants.TPA_PLAN_DATA_FORM);		
		%>
		
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead><content:getAttribute id="sumaryTabContent" attribute="text"/></TD></TR>
		</table>
	
	<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
    <TR>
    <TD width="100%"><!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
      
      <DIV class=evenDataRow>     
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
			<ps:fieldHilight name="planName" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Plan Name: 
			</TD>
			<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.planName}">
<p>${noticePlanCommonVO.planName}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.planName}">
				  <p>Pending Plan Information Completion</p>
</c:if>
			</TD>
        </TR>
        </table></DIV>
       <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
			<ps:fieldHilight name="planYearEndDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Plan Year End:  
			</TD>
			<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.planYearEnd}">
<p>${noticePlanCommonVO.planYearEnd.data}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.planYearEnd}">
				  <p>Pending Plan Information Completion</p>
</c:if>
			</TD>
        </TR>
        
        </table></DIV>
       <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
			<ps:fieldHilight name="contractNumber" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Contract number:  
			</TD>
			<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.contractNumber}">
<p>${noticePlanCommonVO.contractNumber}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.contractNumber}">
				  <p>Pending Plan Information Completion</p>
</c:if>
			</TD>
        </TR>
         </table></DIV>
       <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				Enrollment Access Number (EAN):
			</TD>
			<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.enrollmentAccessNumber}">
<p>${noticePlanCommonVO.enrollmentAccessNumber}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.contractNumber}">
				  <p/>
</c:if>
			</TD>
        </TR>
        
        </TABLE></DIV>
         <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				The contract has selected the following Notice:  
			</TD>
			<TD class=dataColumn>
			<c:if test="${tabPlanDataForm.noticeServiceInd=='Yes'}">
<c:if test="${not empty noticePlanCommonVO.noticeType}">
<p>${noticePlanCommonVO.noticeType}</p>
</c:if>
			</c:if>
			<c:if test="${tabPlanDataForm.noticeServiceInd=='No (De-selected)'}">
				<p>SEND Service is no longer selected.</p>
			</c:if>
			<c:if test="${tabPlanDataForm.noticeServiceInd=='No'}">
				<p>SEND Service has not been selected.</p>
			</c:if>
			</TD>
        </TR>
         </table></DIV>
       
      
         
          
	</TD></TR></TABLE>
<!--end table content -->
</div>

<script>
$(document).ready(function(){
	document.getElementById('dirtyFlagId').value = "false";
});
</script>
</body>
</html>
