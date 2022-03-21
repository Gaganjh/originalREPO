<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="controlReportsForm" scope="request" type="com.manulife.pension.ps.web.tools.ControlReportsMenuForm" />

<content:contentBean contentId="<%=ContentConstants.PILOT_CONTRACT_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MESSAGE%>" 
	id="pilotContractReportDesc"/>
<content:contentBean contentId="<%=ContentConstants.EXTERNAL_USER_MANAGEMENT_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MESSAGE%>" 
	id="userManagementReportDesc"/>
<content:contentBean contentId="<%=ContentConstants.ADDRESS_CHANGE_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MESSAGE%>" 
	id="addressChangeReportDesc"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TPA_FIRM_NOT_FOUND%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="tpaFirmNotFound" />
<content:contentBean contentId="<%=ContentConstants.INFORCE_DISCLOSURE_REVIEW_LINK%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="inforceDisclosureReviewLink"/>
<content:contentBean contentId="<%=ContentConstants.INFORCE_DISCLOSURE_REVIEW_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="inforceDisclosureReviewDesc"/>		
<content:contentBean contentId="<%=ContentConstants.GIFL_DISCLOSURE_REVIEW%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="giflDisclosureReview"/>
<content:contentBean contentId="<%=ContentConstants.GIFL_DISCLOSURE_REVIEW_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="giflDisclosureReviewDesc"/>
<content:contentBean contentId="<%=ContentConstants.GIFL_DISCLOSURE_REVIEW_DOWNLOAD%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="giflDisclosureReviewDownload"/>
<content:contentBean
	contentName="<%=String.valueOf(ErrorCodes.GIFL_CLASS_NOT_SELECTED)%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="classTypewWarningMessage" />
<content:contentBean
	contentName="<%=String.valueOf(ErrorCodes.GIFL_VERSION_NOT_SELECTED)%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="giflDisclosureReviewWarningMessage" />	
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_REPORT_DESCRIPTION%>" 
	type="<%=ContentConstants.TYPE_MESSAGE%>" 
	id="noticeManagerReportDesc"/>
<content:contentBean contentId="<%=ContentConstants.NOTICE_MANAGER_REPORT_LINK_LABEL%>" 
	type="<%=ContentConstants.TYPE_MESSAGE%>" 
	id="noticeManagerReportlinkLabel"/>

<SCRIPT language=javascript>

var tpaFirmNotFound = "<content:getAttribute id='tpaFirmNotFound' attribute='text' filter='true'></content:getAttribute>";


var listOfTPAFirms = "";

<c:if test="${not empty tpaFirms}">
<c:forEach items="${tpaFirms}" var="tpaFirm" >
	if (listOfTPAFirms.length > 0) listOfTPAFirms += ",";
listOfTPAFirms += "'" + ${tpaFirm} + "'";
</c:forEach>
</c:if>

function doDownload() {
	var firmId = document.getElementById('firmId').value;
	if (firmId == "" || listOfTPAFirms.indexOf("'" + firmId + "'") == -1) {
		alert(tpaFirmNotFound);
		document.getElementById('firmId').value = "";
		document.getElementById('firmId').focus();
	} else {
		window.location="/do/tools/tpaVestingSubmissionReport/?task=download&ext=.csv&tpaFirm=" + firmId;
    }
}

function doReviewDisclosure() {
	var inforceReviewAsOfDate = document.getElementById('inforceReviewAsOfDate').value;
	if (inforceReviewAsOfDate != "") {
		window.location="/do/tools/controlReports/?task=reviewReport&selectedAsOfDate=" + inforceReviewAsOfDate;
	} 
}


function doOpenGIFLDisclosurePDF() {
	var giflVersion = document.getElementById('selectedGIFLVersion');
	var giflVersionSelected = giflVersion.options[giflVersion.selectedIndex].value;
	var classType = document.getElementById('selectedClassType');
	var classTypeSelected = classType.options[classType.selectedIndex].value;
	var reportURL = new URL("/do/giflDisclosurePdfReport/?giflCode="+giflVersionSelected+"&classType="+classTypeSelected);
	if (giflVersionSelected == '' && classTypeSelected == '') {
		var message = '<content:getAttribute beanName="giflDisclosureReviewWarningMessage" attribute="text" escapeJavaScript="true"/>';
		message = message + "\n" + '<content:getAttribute beanName="classTypewWarningMessage" attribute="text" escapeJavaScript="true"/>';
		alert(message);
	} else if (giflVersionSelected == '') {
		var message = '<content:getAttribute beanName="giflDisclosureReviewWarningMessage" attribute="text" escapeJavaScript="true"/>';
		alert(message);
	} else if (classTypeSelected == '') {
		var message = '<content:getAttribute beanName="classTypewWarningMessage" attribute="text" escapeJavaScript="true"/>';
		alert(message);
	} else {
		PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
		window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
	}
}

</SCRIPT>
<content:errors scope="session"/>
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tbody>
		<tr>
			<td width="30">&nbsp;</td>
			<td>
				<table cellSpacing=0 cellPadding=0 width="100%" border=0>
					<tbody>
						<tr>
							<td colSpan=2><img src="/assets/unmanaged/images/s.gif" height="5"></td>
						</tr>
						<tr>
							<td width="1%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td vAlign=top width="99%">
								<table width="100%" border=0>
				       				<tbody>
										<tr>
											<td vAlign=top></td>
											<td vAlign=top><b>Internal</b></td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
				       					</tr>
				       					<tr>
											<td vAlign=top width="6"></td>
											<td vAlign=top width="179">
												<A href="/do/profiles/userManagementChangesInternal/">User management changes</A>
											</td>
											<td vAlign=top width="8"><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<!--Layout/body1-->
												<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
											</td>
										</tr>
				       					<tr><td colSpan=4 height=20></td></tr>
				       					<tr>
				       						<td vAlign=top></td>
											<td vAlign=top>
												<A href="/do/profiles/tpafirmManagementReport/">TPA firm management </A>
											</td>
											<td vAlign=top><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<!--Layout/body2-->
												<content:getAttribute beanName="layoutPageBean" attribute="body2"/>
											</td>
										</tr>
				       					<tr><td colSpan=4 height=20></td></tr>
				       					<tr>
											<td vAlign=top></td>
											<td vAlign=top>
												<A href="/do/profiles/securityRoleConversionReport/">Security role conversion</A>
											</td>
											<td vAlign=top><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<!--Layout/body3-->
												<content:getAttribute beanName="layoutPageBean" attribute="body3"/>
											</td>
										</tr>
				       					<tr>
											<td vAlign=top></td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
										</tr>
										<ps:isAnyPilotAvailable>
				       					<tr>
											<td vAlign=top></td>
				                  			<td vAlign=top><a href="/do/pilot/pilotContractReport/">Pilot contract</a></td>
											<td vAlign=top>&nbsp;</td>
				                  			<td vAlign=top width=294>
												<content:getAttribute beanName="pilotContractReportDesc" attribute="text"/>
											</td>
										</tr>
										</ps:isAnyPilotAvailable>
										
										<c:if test="${hasFDReviewReportPermission != null}">
										<tr><td colspan="4" height=20></td></tr>
				       					<tr>
											<td vAlign=top></td>
				                  			<td vAlign=top><a href="javascript:doReviewDisclosure()"><content:getAttribute beanName="inforceDisclosureReviewLink" attribute="text"/></a></td>
											<td vAlign=top>&nbsp;</td>
				                  			<td vAlign=top width=344>As Of 
				                  			<ps:select name="controlReportsForm" property="selectedAsOfDate" style="width:150px;" styleId="inforceReviewAsOfDate">
											   <ps:option value="Current">Current</ps:option>
											   <ps:dateOptions name="controlReportsForm" property="inforceDisclosureReviewDates"
												               renderStyle="<%=RenderConstants.LONG_STYLE%>" />
										    </ps:select>
											<content:getAttribute beanName="inforceDisclosureReviewDesc" attribute="text"/>
											</td>
										</tr>
										</c:if>
										
										<!-- GIFL Disclosure Review -->
										<c:if test="${hasGIFLReviewReportPermission != null}">
										<tr><td colspan="4" height=20></td></tr>
										<!-- GIFL Disclosure Review line -->
				       					<tr>
											<td vAlign=top></td>
				                  			<td vAlign=top>
				                  				<content:getAttribute beanName="giflDisclosureReview" attribute="text"/>
				                  			</td>
											<td vAlign=top>&nbsp;</td>
				                  			<td vAlign=top width=294>
												<content:getAttribute beanName="giflDisclosureReviewDesc" attribute="text"/>
											</td>
										</tr>
										
										<!-- GIFL Disclosure Review Download -->
										<tr><td colspan="4" height=20></td></tr>
				       					<tr>
											<td vAlign=top></td>
				                  			<td vAlign=top></td>
											<td vAlign=top>&nbsp;</td>
				                  			<td vAlign=top width=294>
				                  			
GIFL Version : <form:select path="controlReportsForm.selectedGIFLVersion" id="selectedGIFLVersion" cssStyle="width:80px">
													<c:forEach items="${controlReportsForm.giflVersionNames}"  var="giflVersionName">
														<form:option value="${giflVersionName.key}" >${giflVersionName.value}</form:option>
													</c:forEach>
</form:select>
Class Type : <form:select path="controlReportsForm.selectedClassType" id="selectedClassType" cssStyle="width:65px" >
												    <c:forEach items="${controlReportsForm.classTypes}"  var="classVar">
														<form:option value="${classVar.classShortName}" >${classVar.classLongName}</form:option>
													</c:forEach>
</form:select>
				                  				<a href="javascript:doOpenGIFLDisclosurePDF()">
				                  					<content:getAttribute beanName="giflDisclosureReviewDownload" attribute="text"/>
				                  				</a>
											</td>
										</tr>
										
										</c:if>
																					
				       					<tr><td colspan="4" vAlign=top></td></tr>
					                    <tr>
				       						<td vAlign=top></td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
										</tr>
										<tr>
											<td vAlign=top></td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
				       					</tr>
										<tr>
											<td vAlign=top></td>
											<td vAlign=top><b>External</b></td>
											<td vAlign=top>&nbsp;</td>
											<td vAlign=top>&nbsp;</td>
										</tr>
					                    <tr>
											<td vAlign=top></td>
											<td vAlign=top>
												<A href="/do/profiles/userManagementChangesExternal/">User management changes</A>
											</td>
											<td vAlign=top><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<content:getAttribute beanName="userManagementReportDesc" attribute="text"/>
											</td>
										</tr>
										 <tr>
											<td vAlign=top></td>
											<td vAlign=top>
												<A href="/do/profiles/addressChangesInternal/">Address changes</A>
											</td>
											<td vAlign=top><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<content:getAttribute beanName="addressChangeReportDesc" attribute="text"/>
											</td>
										</tr>
										 <tr>
											<td vAlign=top></td>
											<td vAlign=top>
												<A href="/do/noticereports/alertsReport/">
												<content:getAttribute beanName="noticeManagerReportLinkLabel" attribute="text"/>
												<c:if test="${empty noticeManagerReportLinkLabel}">
												Notice Manager
</c:if>
												</A>
											</td>
											<td vAlign=top><img src="/assets/unmanaged/images/s.gif"></td>
											<td vAlign=top width=294>
												<content:getAttribute beanName="noticeManagerReportDesc" attribute="text"/>
											</td>
										</tr>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>										
<c:if test="${userProfile.internalUser == true}" >
<c:if test="${userProfile.allowedSubmitUpdateVesting == true}" >
										<tr><td colspan="4">&nbsp;</td></tr>
										<tr><td colspan="4">&nbsp;</td></tr>
										<tr>
											<td valign="top"></td>
											<td valign="top"><a name="vesting"></a><b>TPA Report</b></td>
											<td valign="top">&nbsp;</td>
											<td valign="top">&nbsp;</td>
									    </tr>
									    <tr>
										    <td valign="top"></td>
										    <td valign="middle">Vesting submission report </td>
										    <td valign="top"><img src="/assets/unmanaged/images/s.gif"></td>
										    <td valign="middle">TPA firm ID: <input id="firmId" type="text" size="6"> 
										    <a href="javascript:doDownload()">download</a></td>
									    </tr>
</c:if>
</c:if>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td width="1%"><img src="/assets/unmanaged/images/s.gif"></td>
							<td width="99%" colSpan=3>
								<br><br><brr>
				              				<P><input name="task" type="submit" class="button134" onClick="document.location='/do/home/searchContractDetail/'" value="back">
								</P>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>
