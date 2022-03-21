
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- 
   This tag is used to display the Yes/No radio buttons for 
   License Verification.
--%>
<%@attribute name="renderSection" type="java.lang.String" required="false"%>

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="licenseSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />

<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="licenseSectionTitle"/></div>
</div>
<c:if test="${not empty renderSection}">
<div class="regSection">
</c:if>
<div class="report_table">
	<p> <content:getAttribute attribute="text" beanName="licenseText"/> </p>
	<label>
	    <form:radiobutton id="yes" path="producerLicense" value="true"/>
		Yes
	</label>
    <label>
		<form:radiobutton id="no" path="producerLicense" value="false"/>
	    No 
    </label>
	</p>
</div>

<c:if test="${not empty renderSection}">
</div>
</c:if>