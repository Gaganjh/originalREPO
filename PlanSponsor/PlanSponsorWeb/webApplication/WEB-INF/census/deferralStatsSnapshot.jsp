<%-- taglibs --%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@ taglib prefix="render" uri="manulife/tags/render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.census.DeferralReportForm"%>
<%@page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants"%>


<%@page import="org.apache.commons.lang.StringUtils"%><script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/barchart.js"></script>


<jsp:useBean id="deferralReportForm" scope="session" type="com.manulife.pension.ps.web.census.DeferralReportForm" />

<%
	String unknown = (String)request.getAttribute(Constants.UNKNOWN);
	String defType = (String) request.getAttribute("deferralType");
	String deferralTypeInfo = StringUtils.EMPTY;
	String participantACIInd = StringUtils.EMPTY;
	String barChartType = StringUtils.EMPTY;
	if ("$".equals(defType) || "E".equals(defType)) {
		deferralTypeInfo = "Dollar Deferrals";
		barChartType = Constants.DOLLAR_DEFERRALS_BARCHART;
	} else {
		deferralTypeInfo = "Percentage Deferrals";
		barChartType = Constants.PERCENT_DEFERRALS_BARCHART;
	}
	if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(deferralReportForm.getAciSignupMethod()) 
			|| ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(deferralReportForm.getAciSignupMethod()))
		participantACIInd = "Scheduled deferral increase Participation";
	
%>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="570">
	<tbody>
		<tr class="tablehead">
			<td colspan="3" class="tableheadTD"><strong>Deferral snapshot</strong> as of <render:date value='<%=new java.util.Date().toString() %>' patternOut="MMMM dd, yyyy" /></td>
		</tr>
		<tr>
			<td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
			<td>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tbody>
					<tr valign="top">
						<td colspan="4" class="tablesubhead"><strong><%=deferralTypeInfo%></strong></td>
						<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						<td colspan="4" class="tablesubhead"><strong><%=participantACIInd%></strong></td>
					</tr>
					<tr valign="top">
						<td colspan="4" class="datacell1"><ps:barChart name="<%=barChartType%>"
							alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Deferrals on file" /></td>
						<td width="1" class="datadivider"><img src="/assets/unmanaged/images/images/s.gif" height="1" width="1" /></td>
						<%
							if (!deferralReportForm.isACIOff()) {
						%>
						<!-- From here the pie chart legend -->
						<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="2"><br />
								</td>
							</tr>
							<tr>
								<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td height="11" width="11" style="background: <%=Constants.ParticipationRatePieChart.PARTICIPANTS%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
									</tr>
								</table>
								</td>
								<td>&nbsp; Default settings</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td height="11" width="11" style="background: <%=Constants.ParticipationRatePieChart.OPT_OUT%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
									</tr>
								</table>
								</td>
								<td>&nbsp; Actively managed</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td height="11" width="11" style="background: <%=Constants.ParticipationRatePieChart.PENDING_ELIGIBILITY%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
									</tr>
								</table>
								</td>
								<td>&nbsp; Enrolled but not signed up</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td height="11" width="11" style="background: <%=Constants.ParticipationRatePieChart.PENDING_ENROLLMENT%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td>
									</tr>
								</table>
								</td>
								<td>&nbsp; Not enrolled</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
						</table>
						</td>
						<!-- End the pie chart legend -->
						<td colspan="3" class="datacell1"><ps:pieChart beanName="<%= Constants.ACI_PARTICIPATION_PIECHART %>"
							alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart" /></td>
						<%
							} else {
						%>
						<td></td>
						<td colspan="3" class="datacell1"></td>
						<%
							}
						%>
					</tr>
					<%
						if ("E".equals(defType)) {
					%>
					<tr valign="top">
						<td colspan="4" class="tablesubhead"><strong>Percentage Deferrals</strong></td>
						<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						<td colspan="4" class="tablesubhead"></td>
					</tr>
					<tr valign="top">
						<td colspan="4" class="datacell1"><ps:barChart name="<%= Constants.PERCENT_DEFERRALS_BARCHART %>"
							alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Deferrals on file" /></td>
						<td width="1" class="datadivider"><img src="/assets/unmanaged/images/images/s.gif" height="1" width="1" /></td>
						<!-- From here the legend -->
						<td></td>
						<!-- End the legend -->
						<td colspan="3" class="datacell1"></td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			</td>
			<td width="1" class="boxborder"><img src="/assets/unmanaged/images/images/s.gif" height="1" width="1" /></td>
		</tr>
		<tr>
			<td colspan="3">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tbody>
					<tr>
						<td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
						<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="570">
	<tbody>
		<tr valign="top">
			<td class="tablesubhead"><strong>Unknown = <%=unknown%></strong></td>
		</tr>
	</tbody>
</table>
<br />
