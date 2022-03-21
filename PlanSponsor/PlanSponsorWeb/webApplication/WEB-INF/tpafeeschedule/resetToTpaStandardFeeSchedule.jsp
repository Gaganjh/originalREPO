<%-- Tag Libs --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	contentId="<%=ContentConstants.PLAN_PROVISION_IS_NOT_SELECTED_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="planProvisionRestrictionMsg" />
	
<style type="text/css">
	tr.true td {   color: #7e7e7e;
			}
	
	tr.false td { 
		color: #000000;
			}
</style>

<%-- Java Scripts --%>
<c:if test="${empty  technicalDifficulties}">
		<script language="JavaScript1.2" type="text/javascript">
			function doAction(action) {
				document.tpaCustomizeContractForm.action.value = action; 
				document.tpaCustomizeContractForm.submit();
				return false;
			}
			
			function isLostChanges(){
				if (isFormDirty()) {
					
					return confirm('The action you have selected will cause your changes to be lost. \n Select OK to continue or Cancel to return.');
				}
				return true;
			}
			
			function isFormDirty() {
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
												hrefs[i].href.indexOf("/profiles/editMyProfile") != -1){
												hrefs[i].onclick = new Function ("return isLostChanges();");
											}
										
										}
									}
									
									$("a:not(.signOutLayerStyle2)").on("click",
											function() {
													return isLostChanges();
											});
									
									$("tr.true").on("mouseover",function() {    
										Tip('<content:getAttribute beanName="planProvisionRestrictionMsg" attribute="text" />');  }
									).on("mouseout",function(){    UnTip();  });
								});
			} catch (e) {
				// If JQuery is not loaded, exception will be caught here
			}

		</script>
</c:if>

<td>
<br />
<content:errors scope="session"/>
<DIV style="DISPLAY: block" id=basic></DIV><br />
	<TABLE width="730" border="0" cellspacing="0" cellpadding="0">
	
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
									width=148 height=1></TD>
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
								<td class="tableheadTD1" colspan="9" height="25"
									valign="middle"><b>Customize Contract</b></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							<TR class=datacell1>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD colSpan=8>
									<TABLE border=0 width="100%">
										<TBODY>
											<TR class=datacell1>
												<TD vAlign=top width="19%"><B>Contract number</B></TD>
<TD width="78%">${tpaCustomizeContractForm.selectedContract}</TD>


												<TD width="3%">&nbsp;</TD>
												
											</TR>
											<TR class=datacell1>
												<TD vAlign=top width="19%"><B>Contract name</B></TD>
<TD width="78%">${tpaCustomizeContractForm.selectedContractName}</TD>


												<TD width="3%" vAlign=bottom>&nbsp;</td>
											</TR>
									
										</TBODY>
									</TABLE>
								</TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</tr>



							<%-- ----------------- TPA EXPENSE SECTION STARTS ------------------ --%>

							<TR class=tablehead>

								<TD class=tableheadTD height=25 vAlign=middle colSpan=6
									style="padding-left: 6px"><b>Individual Expenses</b></TD>
								<TD class=tableheadTD height=25 vAlign=middle colSpan=3></TD>
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
									<td colspan="4" height="25" align="left" valign="middle"><b>Special
											notes</b></td>
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						    </tr>
							
											<c:set var="count" value="0" scope="page"/>
											<c:if
												test="${ not empty tpaCustomizeContractForm.standardScheduleFeesToReset}">
												<c:forEach var="fee"
													items="${tpaCustomizeContractForm.standardScheduleFeesToReset}" varStatus="indexVar"    >
													 <c:choose>
															<c:when test="${indexVar.index % 2 == 0}">
																	<tr class="datacell2 ${fee.disabled} "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ${fee.disabled} ">
															</c:otherwise>
													</c:choose> 
													    <TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td valign="top" width="25%" style="white-space: wrap;"><b>${fee.feeDescription}</b></td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td align="right" width="22%">${fee.amountValueFormatted}</td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="4" width="53%" style="word-break:break-all;">${fee.notes}</td>
														<TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</tr>
													<c:set var="count" value="${count + 1}" scope="page"/>
												</c:forEach>
											</c:if>
										

							<%-- ----------------- TPA EXPENSE SECTION ENDS ------------------ --%>

							<TR>
								<TD class=databorder colSpan=10><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
						</TBODY>
					</TABLE></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD vAlign=top width=190 align=center><IMG
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
			<%-- ----------------- SUBMIT BUTTON START ------------------ --%>

			<ps:form cssStyle="margin-bottom:0;"
				action="/do/resetToStandardFeeSchedule/" modelAttribute="tpaCustomizeContractForm" name="tpaCustomizeContractForm">
<input type="hidden" name="action" /><%-- input - name="tpaCustomizeContractForm" --%>

				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<tr>

						<td width="410" align="right"></td>
<td width="160" align="right"><input type="button" onclick="return doAction('back');" name="button" class="button134" value="back"/></td>


<c:if test="${tpaCustomizeContractForm.fee404a5AccessPermission ==true}">

<td width="160" align="right"><input type="button" onclick="return doAction('submit');" name="button" class="button134" value="submit"/></td>


</c:if>

					</tr>
				</table>
			</ps:form>
			

			<%-- ----------------- SUBMIT BUTTON ENDS ------------------ --%>
			<TR>
				<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
					height=1></TD>
				<TD></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
	</TABLE>

</td>	
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>
<script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>

<script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
	
