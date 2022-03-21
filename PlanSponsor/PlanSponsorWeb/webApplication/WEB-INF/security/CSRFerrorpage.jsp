<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isErrorPage="true"%>
<%@ page import="com.manulife.pension.exception.SystemException"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository"%>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean"%>
<%@ page import="com.manulife.pension.exception.ExceptionHandlerUtility"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<un:useConstants var="CommonContentConstants" className="com.manulife.pension.platform.web.content.CommonContentConstants" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CSRF Error</title>
</head>

<table>
	<tr>
		<td width="16" class="big" height="312"><img
			src="/assets/unmanaged/images/spacer.gif" width="15" height="1"
			border="0"></td>
		<td>
		<table width="665" border="0" cellspacing="0" cellpadding="0"
			class="fixedTable">
			<tbody>
				<tr>
					<td width="15%">&nbsp;</td>
					<td style="background: white;">
					<h2>Error!</h2>
					<div class="alert alert-danger" role="alert"><i
						class="fa fa-times-circle fa-lg"></i> <content:contentBean
						contentId="91596" type="${CommonContentConstants.TYPE_MESSAGE}"
						id="messagesNoContractDoc" />
					<p><content:getAttribute beanName="messagesNoContractDoc" attribute="text" /></p>
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
					<td width="%10">&nbsp;</td>
<td width="100%" align="center" style="background: white;"><input type="button" onclick="javascript:history.go(-1)" name="button" class="button100Lg" value="Back"/></td>


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
</html>
