<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>



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
		<bd:form action="/do/forgetPassword/stepUpChallenge" method="POST" onsubmit="return doPreSubmit();" modelAttribute="forgetPasswordForm">
			<p>
				<content:getAttribute attribute="introduction2"
					beanName="layoutPageBean" />
			</p>
			<p>
				<content:contentBean contentId="95516"
					type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
					id="dynamicContent" />
				<content:getAttribute attribute="text" beanName="dynamicContent">
					<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientEmail}" /></content:param>
				</content:getAttribute>
			</p>
			
			<p>
				<report:formatMessages scope="request" />
			</p>
			
				<div class=formButtonPasscode>
					<input type="submit" class="blue-transit-btn next"
						onmouseover="this.className +=' btn-hover'"
						onmouseout="this.className='blue-transit-btn next'"
						value="Send Passcode">
						<input type="hidden" name="action" value="continue"/> 
					<script language="javascript">
						var onenter = new OnEnterSubmit('action', 'continue');
						onenter.install();
					</script>
				</div>

		</bd:form>

	</div>

</div>

<layout:pageFooter />

