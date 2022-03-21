<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.usermanagement.PasscodeExemptionForm" %>


<div id="contentFull">

	<h1>
		<content:getAttribute attribute="name" beanName="layoutPageBean" />
	</h1>

	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
			<content:getAttribute attribute="subHeader" beanName="layoutPageBean" />
		</div>
	</div>

	<c:if test="${not empty layoutBean.layoutPageBean.introduction1}">
		<p>
			<content:getAttribute attribute="introduction1"
				beanName="layoutPageBean" />
		</p>
	</c:if>
	<c:if test="${not empty layoutBean.layoutPageBean.introduction2}">
		<p>
			<content:getAttribute attribute="introduction2"
				beanName="layoutPageBean" />
		</p>
	</c:if>
	<p>
		<content:contentBean contentId="95460"
			type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicContent" />
		<content:getAttribute id='dynamicContent' attribute='text'>
			 <content:param><c:out value="${requestScope.passcodeExemptionForm.firstName} ${requestScope.passcodeExemptionForm.lastName}"/></content:param>
		</content:getAttribute>
	</p>
	
<report:formatMessages scope="request"/>
<bd:form action="/do/usermanagement/passcodeExemption" modelAttribute="passcodeExemptionForm" name="passcodeExemptionForm">

		
 <!--  <form:hidden path="action" value=""/> -->

  
  
<div id="content">

 <br>
        <table width="525" border="0" cellspacing="0" cellpadding="0">
			<tr align="center">
					<td width="131">&nbsp;</td>
					<td width="131">&nbsp;</td>
					<td width="131">&nbsp;</td>
					<td>
					  <div class="formButton"> 
					  <input type="button" class="blue-btn next"
						onmouseover="this.className +=' btn-hover'"
						onmouseout="this.className='blue-btn next'" name="finish"
						value="Finish"
						onclick="return doProtectedSubmitBtn(document.passcodeExemptionForm, 'cancel', this)">
					 
      
        </div> 	
			 <input type="hidden" name="action"> 		

					</td>
				</tr>
		</table>
		</div>
  <br class="clearFloat"/>

  <form:hidden path="userName"/>
  <form:hidden path="firstName"/>
  <form:hidden path="lastName"/>
  <form:hidden path="email"/>
  <form:hidden path="userRole"/>
</bd:form>
</div>

<layout:pageFooter/>