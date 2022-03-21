<%-- tag libraries --%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%-- imports --%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.home.HomePageHelper" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
			        
<un:useConstants var="CommonContentConstants" className="com.manulife.pension.platform.web.content.CommonContentConstants" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<%-- NOTE! don't delete below div element as it is required for Plan review pages --%>
<div style="display:none;">CSRF Error</div>
<table>
	<tr>
		<td width="16" class="big" height="312"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
		<td>
		<table width="665" border="0" cellspacing="0" cellpadding="0">
			<tbody>
				<tr>
					<td width="15%">&nbsp;</td>
					<td style="background: white;">
					<p style="font-size: 20px;"><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></p>
					<div class="alert alert-danger" role="alert"><i
						class="fa fa-times-circle fa-lg"></i>
					<p><font size="2"><content:getAttribute beanName="layoutPageBean" attribute="body1"/></font></p>
					</div>

					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td style="background: white;">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td style="background: white;">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td width="%10" style="padding-left:250px;"> 
						<div class="button_login">
							<input type="submit" value="Back" tabindex="4" style="font-size:15px;align:center;" onclick="javascript:history.go(-1)"/></div> </td>
				</tr>
			</tbody>
		</table>
		</td>
		<td width="20" valign="top" height="312"><img
			src="/assets/unmanaged/images/spacer.gif" width="20" height="1"></td>
		<td width="214" valign="top" height="312">
		<div align="left"><img
			src="/assets/unmanaged/images/s.gif" width="8" height="8"
			border="0" align="top"></div>
		<center></center>
		</td>
	</tr>
</table>
<jsp:include page="/WEB-INF/global/standardfooter.jsp"/>
</html>
