<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>
<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />

<content:contentBean
	contentId="<%=ContentConstants.CUSTOM_SCHEDULE_APPLIED_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="customScheduleAppliedMessage" />
		






<c:if test="${empty  technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >

		<script language="JavaScript1.2" type="text/javascript">
		
		
		function isLostChanges(){
			
			if(isFormDirty()){
				return confirm('<content:getAttribute beanName="warningMessage" attribute="text"/>');
			}
			return true;
		}
			try {
				$(document)
						.ready(
								function() {

									registerTrackChangesFunction(isFormDirty);
									var hrefs  = document.links;
									if (hrefs != null)
									{
										for (i=0; i<hrefs.length; i++) { 
											if(hrefs[i].href != undefined && 
												hrefs[i].href.indexOf("profiles/editMyProfile") != -1){
												hrefs[i].onclick = new Function ("return isLostChanges();");
											}
										
										}
									}

							// To alert user that the changes were not yet saved
									$("a:not(.signOutLayerStyle2)").on("click",function(event){
										return isLostChanges();
									});

								});
			} catch (e) {
				// If JQuery is not loaded, exception will be caught here
			}

			function isFormDirty() {
				return $("#dirtyFlagId").val() == 'true';
			}

			function isTpaScheduleNotCustomized() {
				var isNotCustomized = false;
<c:if test="${empty tpaCustomizeContractForm.tpaCustomizedScheduleFees}">
					isNotCustomized = true;
</c:if>
				
				return isNotCustomized;
			}

			function doBack() {
				document.tpaCustomizeContractForm.action.value = "back";
				document.tpaCustomizeContractForm.submit();
			}

			function doSubmit() {

				if (isTpaScheduleNotCustomized()) {
					var message;
					message = '<content:getAttribute beanName="customScheduleAppliedMessage" attribute="text" />';
					var response = confirm(message);
					if (response == true) {
						document.tpaCustomizeContractForm.action.value = "save";
						document.tpaCustomizeContractForm.submit();
					}else{
						return false;
					}
				} else {
					document.tpaCustomizeContractForm.action.value = "save";
					document.tpaCustomizeContractForm.submit();
				}
			}
		</script>
	</c:if>
</c:if>
<%-- technical difficulties --%>



<td>

<div id="messagesBox" class="messagesBox">
	<%-- Override max height if print friendly is on so we don't scroll --%>
	<ps:messages scope="session" suppressDuplicateMessages="true" />
</div>

	<TABLE width="730" border="0" cellspacing="0" cellpadding="0"
		padding="0">
		<TBODY>
			<tr>
			</tr>

			<TR>
				<TD>&nbsp;</TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>

			<TR>
				<TD vAlign=top width=700><TABLE border=0 cellSpacing=0
						cellPadding=0 width=700>
						<TBODY>
							<TR>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=actions><IMG src="/assets/unmanaged/images/s.gif"
									width=203 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=type><IMG src="/assets/unmanaged/images/s.gif"
									width=145 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=paymentTotal><IMG
									src="/assets/unmanaged/images/s.gif" width=200 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=180
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
							</TR>

							<tr class="tablehead">
								<td class="tableheadTD1" colspan="8" height="25"
									valign="center"><b>Customize Contract</b></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							<TR class="datacell1">
								<TD class="databorder" width="1"><IMG
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
								<TD class="datacell1" height=25 vAlign=center colSpan=7
									style="padding-left: 6px">
									<TABLE border=0 width="100%">
										<TBODY>
											<TR class=datacell1>
												<TD vAlign=top width="19%"><B>Contract number</B></TD>
<TD width="18%">${tpaCustomizeContractForm.selectedContract}</TD>


												<TD width="18%">&nbsp;</TD>
												<TD width="25%">&nbsp;</TD>
												<TD width="32px" align="right">&nbsp;</td>
											</TR>
											<TR class=datacell1>
												<TD vAlign=top width="19%"><B>Contract name</B></TD>
<TD width="25%">${tpaCustomizeContractForm.selectedContractName}</TD>


												<TD width="18%">&nbsp;</TD>
												<TD width="25%">&nbsp;</TD>
												<TD width="32px" align="right">&nbsp;</td>
											</TR>
										</TBODY>
									</TABLE>
								</TD>

								<TD class="databorder" width="1"><IMG
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							</TR>
							<c:if
								test="${ not empty tpaCustomizeContractForm.updatedTpaFeesStandard
											or not  empty tpaCustomizeContractForm.updatedTpaFeesCustomized}">
								<TR class=tablehead>

									<TD class=tableheadTD height=25 vAlign=center colSpan=5
										style="padding-left: 6px"><b>Individual Expenses </b></TD>
									<TD class=tableheadTD height=25 vAlign=center colSpan=3></TD>
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>


								<tr class="tablesubhead tpaFees">
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td><b>Fee type</b></td>
									<td class="dataheaddivider" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td height="25" align="right" valign="middle"><b>Value</b></td>
									<td class="dataheaddivider" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="3" height="25" align="left" valign="middle"><b>Special
											notes</b></td>
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
								
												<c:if
													test="${ not empty tpaCustomizeContractForm.updatedTpaFeesStandard}">
													<c:set var="count" value="0" scope="page"/>
													<c:forEach var="fee"
														items="${tpaCustomizeContractForm.updatedTpaFeesStandard}">
														   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
															</c:choose> 
														    <TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td valign="top" width="25%" style="white-space: wrap;"><b>${fee.feeDescription}</b></td>
															<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="3" width="52%" style="word-break:break-all;">${fee.notes}</td>
															<TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														</tr>
														<c:set var="count" value="${count + 1}" scope="page"/>
													</c:forEach>
												</c:if>
												<c:if
													test="${ not empty tpaCustomizeContractForm.updatedTpaFeesCustomized}">
													<c:forEach var="fee"
														items="${tpaCustomizeContractForm.updatedTpaFeesCustomized}">
														    <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
															</c:choose> 
														    <TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td valign="top" width="25%" style="white-space: wrap;"><b>${fee.feeDescription}</b></td>
															<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="3" width="52%" style="word-break:break-all;">${fee.notes}</td>
															<TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														</tr>
														<c:set var="count" value="${count + 1}" scope="page"/>
													</c:forEach>
												</c:if>
							 </c:if>
							 <TR>
								<TD class=databorder colSpan=10><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
						</TBODY>
					</TABLE></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD vAlign=top width=190 align=middle><IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=25> <IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=5> <IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=5></TD>
			</TR>
			<TR>
				<TD></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
					height=1></TD>
				<TD></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<c:if test="${empty param.printFriendly }" >
				<ps:form cssStyle="margin-bottom:0;" 
					action="/do/confirmTpaCustomizedContractFee/" modelAttribute="tpaCustomizeContractForm" name="tpaCustomizeContractForm">
<input type="hidden" name="dirty" id="dirtyFlagId"/>
<input type="hidden" name="action" /><%--  input - name="tpaCustomizeContractForm" --%>

					<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
						<tr>

							<td width="500" align="right"></td>

<td width="80" align="right"><input type="submit" onclick="return doBack();" name="button" class="button134" value="back"/></td>



							<td width="10" align="right"></td>

<td width="80" align="right"><input type="submit" onclick="return doSubmit();" name="button" class="button134" value="submit"/></td>



						</tr>
					</TABLE>
				</ps:form>
			</c:if>
			<BR>
	<TR>
		<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
			height=1></TD>
		<TD></TD>
		<TD width=10><IMG border=0
			src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
		<TD>&nbsp;</TD>
	</TR>
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<td><br>
			<p>
				<content:pageFooter beanName="layoutPageBean" />
			</p>
			<p class="footnote">
				<content:pageFootnotes beanName="layoutPageBean" />
			</p>
			<p class="disclaimer">
				<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
			</p></td>
	</table>
</c:if>
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>
