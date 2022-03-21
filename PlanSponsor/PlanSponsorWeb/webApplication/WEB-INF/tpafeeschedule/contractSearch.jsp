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
<%@ page import="com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContractSearchReportData"%> 

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>
		<script type="text/javascript" >		
			function doReset() {			
				document.forms['tpaFeeScheduleContractSearchForm'].action = "/do/tpafee/contractSearch/?task=reset";
				document.tpaFeeScheduleContractSearchForm.submit();
			}
			function doSearch() {

				document.forms['tpaFeeScheduleContractSearchForm'].action = "/do/tpafee/contractSearch/?task=filter";
				document.tpaFeeScheduleContractSearchForm.submit();
			}
			
			function doBack() {
		    	document.forms['tpaFeeScheduleContractSearchForm'].action = "/do/viewTpaStandardFeeSchedule/?tpaFirmId="+document.tpaFeeScheduleContractSearchForm.tpaId.value;
		    	document.tpaFeeScheduleContractSearchForm.submit();
			}
		</script>

<%-- Error Tables--%>




<!-- Beans used -->

<%
TPAFeeScheduleContractSearchReportData theReport = (TPAFeeScheduleContractSearchReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 




<content:errors scope="request"/>
<ps:form action="/do/tpafee/contractSearch/"  modelAttribute="tpaFeeScheduleContractSearchForm" name="tpaFeeScheduleContractSearchForm">
<form:hidden path="tpaId" id="selectedTpaId" /><%--  input - name="tpaFeeScheduleContractSearchForm" --%>
<c:if test="${tpaFeeScheduleContractSearchForm.enableContractSearch}"> 
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
			<td width="100%" valign="top">
			
			<div id="messagesBox" class="messagesBox">
				<%-- Override max height if print friendly is on so we don't scroll --%>
				<ps:messages scope="request"
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
															<TD vAlign=top width="19%"><B>Contract number</B></TD>
<TD width="18%"><form:input path="contractNumber" maxlength="10"></form:input></TD>

															<TD width="18%">&nbsp;</TD>
															<TD width="25%">&nbsp;</TD>
															<TD width="20%">&nbsp;</td>
														</TR>
														<TR class=datacell1>
															<TD vAlign=top width="19%"><B>Contract name</B></TD>
<TD width="18%"><form:input path="contractName" maxlength="30"></form:input></TD>

															<TD width="18%" vAlign=bottom><SPAN
style="FLOAT: left"> <input type="button" onclick="return doReset();" name="button" style="width: 50px" value="reset"/> <input type="button" onclick="return doSearch();" name="button" style="width: 50px" value="search"/>



															</SPAN></td>
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

	<jsp:include page="TPAContractSelect.jsp"></jsp:include>
</c:if>
	
<br>
	<TABLE width=765 border="0" cellSpacing="0" cellPadding="0" width="100%">
				
		<tr>
			<c:if test="${empty param.printFriendly }" >
				
<td width="765" align="right" valign="bottom"><input type="button" onclick="return doBack();" name="button" class="button134" value="back"/></td>


			</c:if>
		</tr>
	</table>
</ps:form>




<c:if test="${not empty param.printFriendly }" >
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
