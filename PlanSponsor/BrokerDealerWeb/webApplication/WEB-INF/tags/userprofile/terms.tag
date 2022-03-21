<%@ attribute name="termId" type="java.lang.Integer" required="true" rtexprvalue="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%-- 
   This tag is used to display the terms and conditions.
--%>
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_TERMS_SECTION_TITLE%>"
type="<%=BDContentConstants.TYPE_MESSAGE%>" id="termsSectionTitle" />
<content:contentBean contentId="${termId}" type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="termsText" />

<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="termsSectionTitle"/></div>
</div>
<div class="regSection">
	<div class="inputTextFull">
		<div class="terms"
				style="line-height: 1em !important; overflow: scroll !important; min-height: 7em !important; max-height: 7em !important;">
			<content:getAttribute attribute="body1" beanName="termsText" />
		</div>
	</div>
	<br />
	<div>
		<form:checkbox path="acceptDisclaimer" id="acceptDisclaimer"/>
		* Yes, I have read and I agree to the terms and conditions.
	</div>
</div>