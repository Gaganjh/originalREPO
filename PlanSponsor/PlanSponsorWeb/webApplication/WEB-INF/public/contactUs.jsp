<%-- Prevent the creation of a session --%>
<%@page session="false" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.Constants, com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContactVO" %>
<%
UserProfile userProfile = (UserProfile)request.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Section_Title"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MORE_INFORMATION_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_More_Information_Link"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_YOUR_MANULIFE_ACCOUNT_REPRESENTATIVE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Your_Manulife_Account_Representative_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Account_Representative_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_CUSTOMER_TECHNOLOGY_SERVICES_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Customer_Technology_Services_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_CUSTOMER_TECHNOLOGY_SERVICES_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Customer_Technology_Services_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Manuline_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Manuline_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_EMAIL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Email"/>
<content:contentBean contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_ACCOUNT_REP_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Defined_Benefit_Account_Rep_Phone"/>

<c:if test="${parm == 'contactUs'}"> 
<script type="text/javascript" >
	init(3, 1);
</script>

</c:if>

<c:if test="${not empty requestScope.contractSummary}">
<c:set var="contractSummary" value="${requestScope.contractSummary}" /> 
<c:set var="contact" value="${contractSummary.carContact}" />

<c:if test="${not empty contact.email}">
<c:set var="emailAddress" value="${contact.email}" /> 
</c:if>

<c:if test="${empty contact.email}">
<c:set var="emailAddress" value="${Contacts_Default_Email.text}" /> 
</c:if>


<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>

</c:if>
<c:if test="${empty requestScope.contractSummary}">
<c:set var="emailAddress" value="${Contacts_Default_Email.text}" /> 
<c:set var="emailSubject" value="${' '}" />
</c:if>

<%-- start contactUs.jsp area --%>
<table width="765" border="0" cellspacing="0" cellpadding="0" />

  <tr>
  	<td>&nbsp;</td>
  </tr>

  <tr>
	<%-- column 1 --%>

   	<td width="90">
    	<img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0">&nbsp;
    </td>

    <%--// end menu (column 1) --%>

    <%-- column 2 --%>
    <td width="435" valign="top" class="greyText">
		<table width="435" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
			<tr>
				<td class="greyText" align="left">
				<p><img src='<content:pageImage beanName="layoutPageBean" type="pagetitle"/>' alt="Contact Us">
				</p>
				</td>
			</tr>

			<tr>
				<td class="greyText" align="left">
				<p>
	              <c:if test="${not empty layoutPageBean.introduction1}">
					<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
</c:if>
					<br>
					<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
				</p>
				<p>
				<b><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></b><br>
				<content:getAttribute attribute="body1" beanName="layoutPageBean">
					<c:if test="${not empty requestScope.contractSummary}">
					<content:param>
						<c:if test="${not empty contact.name }">
						    <c:if test="${definedBenefit eq true}">
						        <content:getAttribute beanName="Defined_Benefit_Account_Rep_Phone" attribute="text"/>
</c:if>
						    <c:if test="${definedBenefit ne true}">
							<content:getAttribute beanName="Contacts_Account_Representative_Phone" attribute="text"/>
</c:if>
							<c:if test="${contact.extension != '' }">
extension ${contact.extension}
</c:if>
</c:if>
						<c:if test="${empty contact.name  }">
							<content:getAttribute beanName="Contacts_Default_Phone" attribute="text"/>
</c:if>
					</content:param>
					<content:param>
						<c:if test="${not empty contact.name  }">
${contact.name}
</c:if>
						<c:if test="${empty contact.name  }">
							a client representative
</c:if>
					</content:param>
					</c:if>
					<c:if test="${empty requestScope.contractSummary}">
					<content:param>
						<content:getAttribute beanName="Contacts_Default_Phone" attribute="text"/>
					</content:param>
					<content:param>
						a client representative
					</content:param>
					</c:if>
				</content:getAttribute>
			</p>
			<p>
			<p>
			<b><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></b> <br>
				<content:getAttribute attribute="body2" beanName="layoutPageBean">
				<content:param>
<a href='mailto:${emailAddress}?subject=<c:if test="${not empty userProfile}">${userProfile.principal.firstName}${userProfile.principal.lastName}-${userProfile.currentContract.contractNumber} </c:if>'>
${emailAddress}
				</a>

				</content:param>
				</content:getAttribute>
			</p>
			</td>
			</tr>

			<tr>
			<td>
			<br>
			<br>
<input type="button" onclick="javascript:history.back();" name="button" class="button100Lg" value="previous"/>
			</td>
			</tr>
		</table>
	</td>

    <%--// emd column 2 --%>


    <%-- column 3 HELPFUL HINT --%>

    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
    <td width="180" valign="top" >
       <center>
			<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
	   </center>
    </td>

    <%--// end column 3 --%>
    </tr>
</table>
<%-- end contactUs.jsp area --%>
<br>
