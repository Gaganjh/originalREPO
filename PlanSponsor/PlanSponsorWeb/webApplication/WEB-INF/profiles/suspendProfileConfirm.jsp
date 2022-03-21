
<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page import="com.manulife.pension.ps.web.profiles.UserProfileForm" %>
<c:set var="theForm" value="${userProfileForm}" scope="request" />



<script type="text/javascript" >
<!--
	var ie4 = (document.all != null);
	var ns4 = (document.layers != null); // not supported
	var ns6 = ((document.getElementById) && (navigator.appName
			.indexOf('Netscape') != -1));
	var isMac = (navigator.appVersion.indexOf("Mac") != -1);

	function MM_goToURL() { //v3.0
		var i, args = MM_goToURL.arguments;
		document.MM_returnValue = false;
		for (i = 0; i < (args.length - 1); i += 2)
			eval(args[i] + ".location='" + args[i + 1] + "'");
	}

	function doFinish() {
		var url = new URL(window.location.href);
		url.setParameter('action', 'finish');
		window.location.href = url.encodeURL();
	}
//-->
</script>


<ps:form cssClass="margin-bottom:0;" method="POST"
	action="/do/profiles/suspendProfile/" name="userProfileForm" modelAttribute="userProfileForm">

		<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="500"><img src="/assets/unmanaged/images/s.gif"
				width="500" height="1"></td>
			<td width="20"><img src="/assets/unmanaged/images/s.gif"
				width="20" height="1"></td>
			<td width="65"><img src="/assets/unmanaged/images/s.gif"
				width="65" height="1"></td>
		</tr>
		<tr>
			<td valign="top">
				<table width="525" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><b><content:pageSubHeader beanName="layoutPageBean" /></b></td>
					</tr>
					<tr>
						<td><content:getAttribute beanName="layoutPageBean"
								attribute="body1">
								<content:param>
${userProfileForm.fullName}
								</content:param>
							</content:getAttribute></td>
					</tr>
				</table>
				<br>
			<br>
			<br>
				<table width="525" border="0" cellspacing="0" cellpadding="0">
					<tr align="center">
						<td width="131">&nbsp;</td>
						<td width="131">&nbsp;</td>
<td width="131"><input type="button" onclick="javascript:window.print()" name="abc" class="button100Lg" value="print"/>

						</td>
<td width="132"><input type="button" onclick="doFinish()" name="finish" class="button100Lg" value="finish"/></td>
					</tr>
				</table>
			</td>
			<td><img src="/assets/unmanaged/images/s.gif" width="20"
				height="1"></td>
			<td valign="top">&nbsp;</td>
		</tr>
	</table>
</ps:form>
