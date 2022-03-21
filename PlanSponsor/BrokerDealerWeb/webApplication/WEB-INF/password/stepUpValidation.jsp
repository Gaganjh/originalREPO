<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>

<content:contentBean contentId="95496"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="resendPassword" />
<content:contentBean contentId="95216"
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
	id="sessionExpiryTimerMessage" />

<div id="stepup_Header">
	<h1>
		<content:getAttribute attribute="name" beanName="layoutPageBean" />
	</h1>

	<div class="BottomBorder">
		<div class="SubTitle" style="overflow: visible">
			<content:getAttribute attribute="subHeader" beanName="layoutPageBean" />
		</div>
	</div>
</div>

<div class="page_section" id="passcode_page">
	<div class="page_module_passcode" id="page_module_passcode">



		<bd:form action="/do/forgetPassword/stepUpValidation" method="POST" onsubmit="return doPreSubmit();" modelAttribute="forgetPasswordForm" name="forgetPasswordForm">
			<p>
				<content:getAttribute attribute="introduction2"
					beanName="layoutPageBean" />
			</p>

			<p>

				<c:choose>
					<c:when test="${sessionScope.PASSCODE_SESSION_KEY.resendFlag == true}">
						<content:contentBean contentId="95223"
							type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
							id="dynamicContent" />
						<content:getAttribute attribute="text" beanName="dynamicContent">
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientEmail}" /></content:param>
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.fromAddress}" /></content:param>
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.createdTimeStamp}" /></content:param>
						</content:getAttribute>
					</c:when>
					<c:otherwise>
						<content:contentBean contentId="95215"
							type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
							id="dynamicContent" />
						<content:getAttribute attribute="text" beanName="dynamicContent">
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientEmail}"/></content:param>
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.fromAddress}"/></content:param>
							<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.createdTimeStamp}"/></content:param>
						</content:getAttribute>
					</c:otherwise>
				</c:choose>
			</p>
			<p>
				<report:formatMessages scope="request" />
			</p>
			<table width="100%"
				style="padding: 0px; border: none; margin-top: 0px;">
				<tr>
					<td style="vertical-align: top">
						<div class="labelPasscode">Passcode:</div>
						<div class="inputText">
							<label><input type="password" name="passcode"
								id="passcode" maxlength="12" autocomplete="off"
								class="inputField"> </label>
						</div>
						<div class=inputTimer>
							<content:getAttribute attribute="text"
								beanName="sessionExpiryTimerMessage">
								<content:param>
									<span>16:00</span>
								</content:param>
								<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.expriredTimeStamp}" /></content:param>
							</content:getAttribute>
						</div>
					</td>
				</tr>
				<tr>
					<td style="vertical-align: top">
						<div class=formButtonPasscode>
							<input type="submit" class="grey-btn next"
								onmouseover="this.className +=' btn-hover'"
								onmouseout="this.className='grey-btn next'" name="action"
								value="Cancel">
							<script language="javascript">
								var onenter = new OnEnterSubmit('action',
										'Cancel');
								onenter.install();
							</script>

							<input type="submit" class="blue-btn next"
								onmouseover="this.className +=' btn-hover'"
								onmouseout="this.className='blue-btn next'" name="action"
								value="Verify">
							<script language="javascript">
								var onenter = new OnEnterSubmit('action',
										'Verify');
								onenter.install();
							</script>
						</div>

					</td>
				</tr>

			</table>

			<br class="clearFloat" />
			<p>
				<content:getAttribute beanName="resendPassword" attribute="text" />
				<br class="clearFloat" /> <br class="clearFloat" /> <strong><a
					href="javascript:;"
					onclick="doPreSubmit(),document.forms['forgetPasswordForm'].submit();"> Resend
						passcode </a></strong> <input type="hidden" name="action" value="resend" />
			</p>
		</bd:form>

	</div>

</div>

<script>
	setFocusOnFirstInputField("forgetPasswordForm");
	if(window.history.replaceState){
		window.history.replaceState(null,null,window.location.href);
	}
</script>

<layout:pageFooter />

