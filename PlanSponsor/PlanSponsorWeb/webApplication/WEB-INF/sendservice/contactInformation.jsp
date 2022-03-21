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
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>


<content:contentBean contentId="<%=ContentConstants.CONTACT_INFO_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="contactInformationInstruction" />

<jsp:useBean id="noticePlanCommonVO" scope="session" class="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style type="text/css">
.tpaheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	width: 729px;
	font-weight: bolder;
	color: #000000;
	padding: 0px;
	background : #CCCCCC url(/assets/unmanaged/images/box_ul_corner.gif) no-repeat left top;
	clear: both;
	vertical-align: middle;
	border-right:1px solid #CCCCCC;
}
</style>

<%
String contactName = noticePlanCommonVO.getPrimaryContactName();
%>
<div id="contactinfoTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="contactinfoShowIconId" onclick="expandDataDiv('contactinfo');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="contactinfoHideIconId" onclick="collapseDataDiv('contactinfo');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">Notice Contact</div>
    <div class="sectionHighlightIcon" id="contactinfoSectionHighlightIconId">
      <ps:sectionHilight name="contactInformation" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
<!-- TODO:SAVE BUTTON -->
 <div id="contactinfoDataDivId">
    <!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/;/liquid_IE.css" type="text/css">
	<![endif]-->
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR>
					<TD class=contactinfoLabelColumn style="width: 205px;"><ps:fieldHilight
							name="noticePlanCommonVO.primaryContactName" singleDisplay="true"
							className="errorIcon" displayToolTip="true" />Name</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.primaryContactName}">
							<%=contactName%>
</c:if></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=oddDataRow>
			<TABLE class=dataTable>
				<TR>
					<TD class=contactinfoLabelColumn style="width: 205px;">Telephone number</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorContactPhone}">

${noticePlanCommonVO.planSponsorContactPhone}

</c:if></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR>
					<TD class=contactinfoLabelColumn style="width: 205px;">e-mail</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorEmailAddress}">

${noticePlanCommonVO.planSponsorEmailAddress}

</c:if></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=oddDataRow>
			<TABLE class=dataTable>
				<TR>
					<TD class=contactinfoLabelColumn style="width: 205px;"><ps:fieldHilight
							name="noticePlanCommonVO.planSponsorMailingAddress"
							singleDisplay="true" className="errorIcon" displayToolTip="true" />Address</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorMailingAddress}">

${noticePlanCommonVO.planSponsorMailingAddress}

</c:if></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR>
					<TD class=contactinfoLabelColumn style="width: 205px;"><ps:fieldHilight
							name="noticePlanCommonVO.planSponsorCitySateZip"
							singleDisplay="true" className="errorIcon" displayToolTip="true" />City, State, ZIP</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planSponsorCitySateZip}">

${noticePlanCommonVO.planSponsorCitySateZip}

</c:if></TD>
				</TR>
			</TABLE>
		</DIV>
	</div>
  </div>
        

