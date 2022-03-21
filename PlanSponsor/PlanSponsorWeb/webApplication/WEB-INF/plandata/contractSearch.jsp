<%@page buffer="none" autoFlush="true" isErrorPage="false"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.security.role.InternalUser"%>
<%@ page import="com.manulife.pension.service.security.role.ThirdPartyAdministrator"%>
<%@ page import="com.manulife.pension.service.security.role.PayrollAdministrator"%>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo"%>
<%@ page import="com.manulife.pension.service.security.valueobject.ContractPermission"%>
<%@ page import="java.util.Set"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>
		<script type="text/javascript" >		
			function doReset() {			
				document.forms['tpaPlanDataContractSearchForm'].action = "/do/plandata/contractSearch/?task=reset";
				document.tpaPlanDataContractSearchForm.submit();
			}
			function doSearch() {

				document.forms['tpaPlanDataContractSearchForm'].action = "/do/plandata/contractSearch/?task=filter";
				document.tpaPlanDataContractSearchForm.submit();
			}
			
			function doBack() {
		    	document.forms['tpaPlanDataContractSearchForm'].action = "/do/viewTpaStandardFeeSchedule/?tpaFirmId="+document.tpaPlanDataContractSearchForm.tpaId.value;
		    	document.tpaPlanDataContractSearchForm.submit();
			}
		</script>

<%-- Error Tables--%>




<!-- Beans used -->

<%

TPAPlanDataContractSearchReportData theReport = (TPAPlanDataContractSearchReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>







<ps:form action="/do/plandata/contractSearch/" modelAttribute="tpaPlanDataContractSearchForm" name="tpaPlanDataContractSearchForm">
<input type="hidden" name="tpaId" id="selectedTpaId" /><%--  input - name="tpaPlanDataContractSearchForm" --%>
<c:if test="${tpaPlanDataContractSearchForm.enableContractSearch}"> 
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
			<td width="100%" valign="top">
			<div id="messagesBox" class="messagesBox">
				<%-- Override max height if print friendly is on so we don't scroll --%>
				<ps:messages scope="request"
					maxHeight="${param.printFriendly ? '1000px' : '100px'}"
					suppressDuplicateMessages="true" />
				<ps:messages scope="session"
					maxHeight="${param.printFriendly ? '1000px' : '100px'}"
					suppressDuplicateMessages="true" />
			</div>
							
				<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
					<TBODY>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD vAlign=top width=700>
								<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
									<TBODY>
										<TR>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD class=actions><IMG src="/assets/unmanaged/images/s.gif" width=128 height=1></TD>
											<TD class=submissionDate><IMG src="/assets/unmanaged/images/s.gif" width=35 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD class=type><IMG src="/assets/unmanaged/images/s.gif" width=205 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD class=paymentTotal><IMG src="/assets/unmanaged/images/s.gif" width=260 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class=tablehead>
											<TD class=tableheadTD1 height=25 vAlign=center colSpan=10></TD>
											<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR class=datacell1>
											<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											<TD colSpan=9>
												<TABLE border=0 width="100%">
													<TBODY>
														<TR class=datacell1>
															<TD vAlign=top width="19%">
<c:if test="${empty theReport.details}">
<c:if test="${not empty tpaPlanDataContractSearchForm.contractName}">
																	<img src="/assets/unmanaged/images/error.gif" style="position: absolute; left: 155px;"/>
</c:if>
</c:if>
															<B>Contract name</B></TD>
<TD width="18%"><form:input path="contractName" disabled="${param.printFriendly}" maxlength="30"></form:input></TD>

															<TD width="18%">&nbsp;</TD>
															<TD width="25%">&nbsp;</TD>
															<TD width="20%">&nbsp;</td>
														</TR>
														<TR class=datacell1>
															<TD vAlign=top width="19%">
<c:if test="${empty theReport.details}">
<c:if test="${not empty tpaPlanDataContractSearchForm.contractNumber}">
																	<img src="/assets/unmanaged/images/error.gif" style="position: absolute; left: 155px;"/>
</c:if>
</c:if>
															<B>Contract number</B></TD>
<TD width="18%"><form:input path="contractNumber" disabled="${param.printFriendly}" maxlength="10"></form:input></TD>

															<TD width="18%" vAlign=bottom><SPAN
																style="FLOAT: left"> <c:if test="${empty param.printFriendly}">
<input type="button" onclick="return doSearch();" name="button" style="width: 50px" value="search"/>
<input type="button" onclick="return doReset();" name="button" style="width: 50px" value="reset"/></c:if>
																</SPAN>
															</td>
															<TD width="25%">&nbsp;</TD>
															<TD width="20%">&nbsp;</td>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
											<TD class=databorder><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</tr>
										<TR>
											<TD class=databorder colSpan=11><IMG
												src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										</TR>
										<TR>
											<TD colSpan=5><IMG src="/assets/unmanaged/images/s.gif"
												width=1 height=1></TD>
											<TD class=dark_grey_color vAlign=center colSpan=4></TD>
											<TD class=dark_grey_color colSpan=4 align=right></TD>
											<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
												height=1></TD>
										</TR>
										<tr>
											<td></td>
										</tr>
									</TBODY>
								</TABLE>
							</TD>
						</tr>
				</table>
			</td>
		</tr>
	</table>	
</c:if>


<c:if test="${not empty theReport.details}">
					<div style="position: relative; left: 30px;">
	<jsp:include page="TPAContractSelect.jsp" flush="true"></jsp:include>
	</div>
</c:if>
<c:if test="${empty theReport.details}">

		<div style="position: relative; left: 30px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		
		<td width="100%" valign="top">
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="7" valign="top">
					<table width="735" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"height="1"></td>
							<td width="260"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
									height="1"></td>
							<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="155"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="120"></td>
							
							
						</tr>
						<tr class="tablehead">
                			<td class="tableheadTD1" colspan="2"><b>Contract</b></td>
                      		<td colspan="9">
                      			<table cellspacing="0" cellpadding="0" border="0" width="100%">
                      				
								</table>
                      		</td>
               				
                    	</tr>
                    	
                    	<tr class="tablesubhead">
							<td rowspan="1" height="28" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>	
                      			<strong>
                      				<report:sort field="contractName" formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.DESC_DIRECTION%>">Contract name
                      				</report:sort>
                      			</strong>  
                    		</TD>
							<td rowspan="1" class="dataheaddivider"><imgsrc="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				       	<strong>
                      				<report:sort field="contractId" formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.DESC_DIRECTION%>">Contract number
                      				</report:sort>
                      		   </strong>
                      		</TD>

							

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				          <strong>
                      				<report:sort field="createdTS" formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.DESC_DIRECTION%>">Last updated 
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD align=left> 
           				          <strong>
                      				<report:sort field="createdUser" formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.ASC_DIRECTION%>">Last updated by 
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width=1></td>
						</tr>
						<tr class="datacell1" height="25">
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
						</tr>
						<tr>
							<td class="databorder" colspan="12"><img src="/assets/unmanaged/images/s.gif" width="2" height="1"></td>
						</tr>
                    	</table>
                    	</td>
                    	</tr>
                    	</table>
                    	</td>
                    	</tr>
                    	</table>
                    	</div>
</c:if>
<br>
</ps:form>




<c:if test="${not empty param.printFriendly}" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
					attribute="text" /></td>
		</tr>
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
