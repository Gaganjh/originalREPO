<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.platform.web.util.DataUtility" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditVestingDetailBean" %>


<%
VestingDetailsReportData theReport = (VestingDetailsReportData)session.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
EditVestingDetailBean detail = (EditVestingDetailBean)session.getAttribute(Constants.EDIT_VESTING_CONFIRM_DETAIL_DATA);
pageContext.setAttribute("detail",detail,PageContext.PAGE_SCOPE);
EditVestingDetailBean bean = (EditVestingDetailBean)session.getAttribute(Constants.EDIT_VESTING_CONFIRM_DETAIL_DATA);
pageContext.setAttribute("bean",bean,PageContext.PAGE_SCOPE);

%>





        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
			<tr>
              <td width="30" class="big"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
              <td height="20">
              <strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong>
						<table cellspacing="0" cellpadding="0" width="100%" border="0">
                          <tr>
                            <td width="200" valign="top"><%--DWLayoutEmptyCell--%>&nbsp;</td>
                            <td width="300" valign="top"><%--DWLayoutEmptyCell--%>&nbsp;</td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Contract&nbsp;</strong></td>
                            <td><strong class="highlight">
${sessionScope.userProfile.currentContract.contractNumber}

${sessionScope.userProfile.currentContract.companyName}

                            </strong></td>
                          </tr>
						  <tr>
                            <td nowrap><strong>Submission number&nbsp;</strong></td>
<td><strong class="highlight">${sessionScope.editVestingDetailBean.confirmationNumber}</strong></td>
                          </tr>
                          <tr>
	                        <td nowrap><strong>Resubmitted by&nbsp;</strong></td>
<td><strong class="highlight">${sessionScope.editVestingDetailBean.sender}</strong></td>
                          </tr>
						  <tr>
                            <td nowrap><strong>Resubmitted&nbsp;</strong></td>
                            <td ><strong class="highlight"><render:date property="detail.receivedDate" patternOut="MMMM dd, yyyy hh:mm a z" defaultValue=""/></strong></td>
                          </tr>
						  <tr>
							  <td><strong>Number of records&nbsp;</strong></td>
<td><strong class="highlight">${sessionScope.editVestingDetailBean.numberOfRecords}</strong></td>
						  </tr>
						  <tr>
							  <td><strong>Submission type&nbsp;</strong></td>
<td><strong class="highlight">${sessionScope.editVestingDetailBean.submissionType}</strong></td>
						  </tr>
                          <tr>
                      		<td nowrap>&nbsp;</td>
                      		<td>&nbsp;</td>
                          </tr>
                          <tr valign="top">
                            <% if (theReport.getNumWarnings() > 0) { %>
                            	<td height="14"><strong>Status&nbsp;</strong></td>
								<td height="14">
								<content:getAttribute beanName="layoutPageBean" attribute="body1">
									<content:param><%=theReport.getNumWarnings()%></content:param>
								</content:getAttribute>
								</td>
 							<% } else { %>
							    <td height="14" ><img src="/assets/unmanaged/images/spacer.gif" width="1" height="14" border="0"></td>
								<td height="14" >&nbsp;</td>
							<% } %>
                          </tr>
                      </table>
           	</td>
			<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			<td width="210" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
			</td>
			<%--// end column 3 --%>

		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
		<tr align="center">
			<td></td>
			<td colspan="2">
				<p/>
		        <table border="0" cellspacing="0" cellpadding="0" align="center">
		          <tr>
<td><input type="button" onclick="javascript:print(); return false;" name="actionLabel" class="button100Lg" value="print"/></td>               
					<td width="15"></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /><%-- CCAT: Extra attributes for tag td><input - property="actionLabel" --%></td>
		          </tr>
		        </table>
			</td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
	</table>



