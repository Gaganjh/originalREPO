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
	
<content:contentBean
	contentId="<%=ContentConstants.RESTRICT_TPA_UPDATE_ACCESS_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="restrictTpaUpdateAccessMessage" />
	

<style type="text/css">
	tr.true td {   color: #7e7e7e;
			}
	
	tr.false td { 
		color: #000000;
			}
</style>

<%-- Java Scripts --%>
<c:if test="${empty  technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >
		<script language="JavaScript1.2" type="text/javascript">
			function doAction(action) {
				document.tpaCustomizeContractForm.action.value = action; 
				document.tpaCustomizeContractForm.submit();
				return false;
			}

			$(function() {
				$("tr.true").on("mouseover",function() {    
					Tip('<content:getAttribute beanName="planProvisionRestrictionMsg" attribute="text" />');  }
				).on("mouseout",function(){    UnTip();  });
			});

		</script>
	</c:if>
</c:if>

<td>
<div id="messagesBox" class="messagesBox">
	<%-- Override max height if print friendly is on so we don't scroll --%>
	<ps:messages scope="session" suppressDuplicateMessages="true" />
</div>

<br />
<DIV style="DISPLAY: block" id=basic></DIV><br />
	<TABLE width="730" border="0" cellspacing="0" cellpadding="0" padding="0">
		<TBODY>
			<tr>
			</tr>
			<c:if test="${empty param.printFriendly }" >
				<tr>
					<td colspan="9"><a href="javascript:doAction('goToHistory')">Change History</a><br></td>
				</tr>
			</c:if>
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
								<td class="tableheadTD1" colspan="9" height="25"
									valign="center"><b>Customize Contract</b></td>
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
												<TD vAlign=top width="19%"><B>Contract Number</B></TD>
<TD width="30%">${tpaCustomizeContractForm.selectedContract}</TD>


												<TD width="3%">&nbsp;</TD>
												<TD width="25%"><b>Last updated</b></TD>
												<TD width="23%"><render:date
														property="tpaCustomizeContractForm.lastUpdateDate"
														patternOut="MMMM dd, yyyy" /></td>
											</TR>
											<TR class=datacell1>
												<TD vAlign=top width="19%"><B>Contract Name</B></TD>
<TD width="30%">${tpaCustomizeContractForm.selectedContractName}</TD>


												<TD width="3%" vAlign=bottom>&nbsp;</td>
												<TD width="25%"><b>Last updated by</b></TD>
<TD width="23%">${tpaCustomizeContractForm.lastUpdatedUserId}</td>


											</TR>
											<TR class=datacell1>
												<TD vAlign=middle height=25 width="52%" colspan="3"><c:if
														test="${tpaCustomizeContractForm.tpaFeeType eq 'CUSTOMIZED'}">
						               Customized schedule in use.
						           </c:if> <c:if
														test="${tpaCustomizeContractForm.tpaFeeType eq 'STANDARD'}">
						               Standard schedule in use.  Click edit to customize.
						           </c:if> <c:if
														test="${tpaCustomizeContractForm.tpaFeeType eq 'NONE'}">
						               No schedule exists.
						           </c:if></TD>
						                        <TD width="48%" colspan="2"><c:choose>
													<c:when
														test="${(tpaCustomizeContractForm.tpaFeeType ne 'CUSTOMIZED') or  (tpaCustomizeContractForm.fee404a5AccessPermission eq true)}">
													</c:when>
													<c:otherwise>
														<content:getAttribute beanName="restrictTpaUpdateAccessMessage" attribute="text" />
													</c:otherwise>
												</c:choose>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</tr>



							<%-- ----------------- TPA EXPENSE SECTION STARTS ------------------ --%>

							<TR class=tablehead>

								<TD class=tableheadTD height=25 vAlign=center colSpan=6
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
								<td colspan="4" height="25" align="left" valign="middle"><b>Special
										notes</b></td>
								<td class="databorder" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
											<c:if
												test="${ tpaCustomizeContractForm.hasTpaFees eq false }">
											<tr class="datacell1">
											<TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										     <td colspan="8">
											 There is no data for this section.
								             </td>
									         <TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							                </tr>
								            </c:if>
											<c:if
												test="${ not empty tpaCustomizeContractForm.tpaFeesStandard}">
												<c:forEach var="fee"
													items="${tpaCustomizeContractForm.tpaFeesStandard}">
													<c:if test="${ fee.showFee eq true }">
													<c:choose>
													<c:when test="${count % 2 == 0}">
															<tr class="datacell2 ${fee.disabled}"> 
													</c:when>
													<c:otherwise>
															<tr class="datacell1 ${fee.disabled}">
													</c:otherwise>
													</c:choose> 
													    <TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td valign="top" width="25%" style="white-space: wrap;"><b>${fee.feeDescription}</b></td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td align="right" width="22%">${fee.amountValueFormatted}</td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="4" width="52%" style="word-break:break-all;">${fee.notes}</td>
														<TD class=databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</tr>
													<c:set var="count" value="${count + 1}" scope="page"/>
													</c:if>
												</c:forEach>
											</c:if>
											<c:if
												test="${ not empty tpaCustomizeContractForm.tpaFeesCustomized}">
												<c:forEach var="fee"
													items="${tpaCustomizeContractForm.tpaFeesCustomized}">
													<c:if test="${ fee.showFee eq true }">
													<c:choose>
													<c:when test="${count % 2 == 0}">
															<tr class="datacell2 ${fee.disabled}"> 
													</c:when>
													<c:otherwise>
															<tr class="datacell1 ${fee.disabled}">
													</c:otherwise>
													</c:choose> 
													    <TD class =databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td valign="top" width="25%"style="white-space: wrap;"><b>${fee.feeDescription}</b></td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td align="right" width="22%">${fee.amountValueFormatted}</td>
														<TD class=datadivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="4" width="52%" style="word-break:break-all;">${fee.notes}</td>
														<TD class=databorder ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</tr>
													<c:set var="count" value="${count + 1}" scope="page"/>
													</c:if>
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
			<%-- ----------------- EDIT BUTTON START ------------------ --%>

			<ps:form cssStyle="margin-bottom:0;"
				action="/do/viewTpaCustomizedContractFee/" modelAttribute="tpaCustomizeContractForm"  name="tpaCustomizeContractForm">
<input type="hidden" name="action" /><%-- input - name="tpaCustomizeContractForm" --%>

				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<tr>

						<td width="410" align="right"></td>
						<c:if test="${empty param.printFriendly }" >
							<td width="160" align="right"></td>
<c:if test="${tpaCustomizeContractForm.fee404a5AccessPermission ==true}">

<td width="160" align="right"><input type="button" onclick="return doAction('edit');" name="button" class="button134" value="edit"/></td>


</c:if>
					</c:if>

					</tr>
				</table>
				
				<BR>
			
			
			 <c:if test="${not empty param.printFriendly}">
		     <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
		     <table  border=0 cellSpacing=0 cellPadding=0 width=730>
		      <tr>
		     <td width="100%"><p><content:pageFooter beanName="layoutPageBean" /></p></td>
		     </tr>
		     <tr>
		     <td width="100%">&nbsp;</td>
		     </tr>
		     <tr>
		     <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		     </tr>
		     </table>
		    </c:if>


			</ps:form>

			<%-- ----------------- EDIT BUTTON ENDS ------------------ --%>
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
	
