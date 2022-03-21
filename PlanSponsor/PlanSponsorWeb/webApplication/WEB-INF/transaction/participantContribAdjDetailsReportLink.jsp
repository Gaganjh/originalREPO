<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>





<ps:map id="parameterMap">
	<ps:param name="profileId" valueBeanName="participantContribAdjDetailsForm" valueBeanProperty="profileId"/>
</ps:map>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>           	  		              
<ps:link action="/do/transaction/participantTransactionHistory/" name="parameterMap">Participant Transaction History
</ps:link>
<% } %>

<br>
<a href="/do/transaction/transactionHistoryReport/">Transaction history</a>
