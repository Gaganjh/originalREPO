<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- This page is included by the contractFunds page --%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_INVESTMENT_ALLOCATIONS_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Investment_Allocation_Link"/>
<br>
<content:getAttribute attribute="text" beanName="Investment_Allocation_Link">
	<content:param>/do/investment/investmentAllocationReport/</content:param>
</content:getAttribute>
