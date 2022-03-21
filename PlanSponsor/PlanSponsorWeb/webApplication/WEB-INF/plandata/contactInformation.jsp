<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    
  <%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<content:contentBean contentId="<%=ContentConstants.CONTACT_INFO_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="contactInformationInstruction" />
<%

%>
 <jsp:useBean id="noticePlanCommonVO" scope="session" class="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO"/>
  <jsp:useBean id="tabPlanDataForm" scope="session" class="com.manulife.pension.ps.web.plandata.TabPlanDataForm" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<style type="text/css">
.tpaheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	width: 729px;
	font-weight: bolder;
	color: #000000;
	padding: 0px;
	background: #CCCCCC url(/assets/unmanaged/images/box_ul_corner.gif)
		no-repeat left top;
	clear: both;
	vertical-align: middle;
	border-right: 1px solid #CCCCCC;
}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact Information</title>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
</head>

<body>
	<%
	
String contactName = noticePlanCommonVO.getPrimaryContactName();
%>
	<div id="generalTabDivId" class="borderedDataBox">
		<table width="729" class="dataTable">
			<TR>
				<TD class=subsubhead><content:getAttribute
						id="contactInformationInstruction" attribute="text" /></TD>
			</TR>
		</table>
		<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
			<TR>
				<TD width="100%">
					<!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/;/liquid_IE.css" type="text/css">
	<![endif]-->
					<DIV class=evenDataRow>
						<TABLE class=dataTable>
							<TR>
								<TD class=generalLabelColumn style="width: 205px;"><ps:fieldHilight
										name="psNameCI" singleDisplay="true" className="errorIcon"
										displayToolTip="true" /> Name</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.primaryContactName}">

										<%=contactName%>
</c:if></TD>
							</TR>
						</TABLE>
					</DIV>
					<DIV class=oddDataRow>
						<TABLE class=dataTable>
							<TR>
								<TD class=generalLabelColumn style="width: 205px;">
									Telephone number</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorContactPhone}">

${noticePlanCommonVO.planSponsorContactPhone}

</c:if></TD>
							</TR>
						</TABLE>
					</DIV>
					<DIV class=evenDataRow>
						<TABLE class=dataTable>
							<TR>
								<TD class=generalLabelColumn style="width: 205px;">e-mail</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorEmailAddress}">

${noticePlanCommonVO.planSponsorEmailAddress}

</c:if></TD>
							</TR>
						</TABLE>
					</DIV><br>
					<DIV class=oddDataRow>
						<TABLE class=dataTable>
							<TR>
								<TD class=generalLabelColumn style="width: 205px;"><ps:fieldHilight
										name="psMailingAddrCI" singleDisplay="true"
										className="errorIcon" displayToolTip="true" />Address</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorMailingAddress}">

${noticePlanCommonVO.planSponsorMailingAddress}

</c:if></TD>
							</TR>
						</TABLE>
					</DIV>
					<DIV class=evenDataRow>
						<TABLE class=dataTable>
							<TR>
								<TD class=generalLabelColumn style="width: 205px;"><ps:fieldHilight
										name="psCityStateZipCI" singleDisplay="true"
										className="errorIcon" displayToolTip="true" />City, State, ZIP</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorCitySateZip}">

${noticePlanCommonVO.planSponsorCitySateZip}

</c:if></TD>
							</TR>
						</TABLE>
					</DIV>
				</TD>
			</TR>
		</TABLE>
	</div>

</body>
</html>
