<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contractConstants" className="com.manulife.pension.service.contract.valueobject.Contract" />

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	
<c:set var="contractDI" value="${(contractConstants.STATUS_CONTRACT_DISCONTINUED eq userProfile.currentContract.status)}"/>

<jsp:include flush="true" page="vestingInformationContent.jsp"/>
<content:errors scope="session"/>	
<ps:form action="/do/census/viewVestingInformation/" name="vestingInformationForm" modelAttribute="vestingInformationForm">
	<form:hidden path="source"/>
	<form:hidden path="action"/>
	<form:hidden path="profileId"/>
	<form:hidden path="sourceAsOfDate"/>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <c:if test="${not empty param.printFriendly}">
      <tr>
        <td>
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
      	</td>
      </tr>

      <tr>
        <td>
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
      	</td>
      </tr>
      </c:if>
      
      <c:if test="${param.source == 'wd'}">
      <tr>
        <td>
      	  <content:getAttribute beanName="WithdrawalWarning" attribute="text"/>
      	</td>
      </tr>
      </c:if>
      <tr>
        <td>&nbsp;</td>
      </tr>
	</table>
					
	<br><br>
	<jsp:include flush="true" page="vestingInformation.jsp"/>
	<br><br>
	 
	<jsp:include flush="true" page="viewVestingParamSection.jsp"/>
    <br><br>
     
	<c:if test="${empty param.printFriendly}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr vAlign=top>
		<td width="50%">&nbsp;</td>
		<td align="left" width="25%">
			<input type="button" name="button1" value="back" class="button134" onclick="doSubmit('back')"/>
		</td>
		<td align="right" width="25%">
		    <c:if test="${userProfile.allowedSubmitUpdateVesting && !contractDI}">
		    <input type="button" name="button2" value="edit" class="button134"  onclick="doSubmit('edit')"/>
		    </c:if>
		</td>
    </tr>
	</table>
	</c:if>


</ps:form>

<c:if test="${not empty param.printFriendly}">
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
       id="globalDisclosure"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
</table>
</c:if>
